package br.unb.unbiquitous.driver.cellphone;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import br.unb.unbiquitous.driver.cellphone.keyboard.KeyboardDriver;
import br.unb.unbiquitous.driver.cellphone.mouse.MouseDriver;
import br.unb.unbiquitous.ubiquitos.me.driver.DeviceDriver;
import br.unb.unbiquitous.ubiquitos.me.uos.Logger;
import br.unb.unbiquitous.ubiquitos.me.uos.UosDeviceManager;
import br.unb.unbiquitous.ubiquitos.me.uos.UosException;
import br.unb.unbiquitous.ubiquitos.me.uos.network.bluetooth.BluetoothNetworkAdapter;

/**
 * @author      Lucas Almeida <lucas.augusto.almeida@gmail.com>
 * @version     1.0
 * @since       2011.0709
 * @see			CommandListener
 */
public class CellPhoneScreen implements CommandListener {

    private static Logger logger = new Logger(CellPhoneScreen.class);

    private CellPhoneMidlet midlet;

    private Display display;

    private UosDeviceManager deviceManager = null;

    private KeyboardDriver keyboardDriver = null;

    private MouseDriver mouseDriver = null;

    private Form identificationForm = null;

    private TextField nameField;

    private Command saveNameCommand;

    private Command exitCommand;

    private Form runningServiceForm = null;

    private StringItem messageField1;

    private ChoiceGroup driverChoices;

    private Command selectDriverCommand;

    private Command stopServiceCommand;

    /**
	 * Initializes the class for the various control screens.
	 *
	 * @param cellPhoneMidlet Reference to the midlet
	 * @param display Reference to the display
	 * @since 2011.0709
	 */
    public CellPhoneScreen(CellPhoneMidlet cellPhoneMidlet, Display display) {
        this.midlet = cellPhoneMidlet;
        this.display = display;
    }

    /**
	 * Creates the first form, that identifies the local device name
	 *
	 * @since 2011.0709
	 */
    public void identificationScreen() {
        if (identificationForm == null) {
            identificationForm = new Form("CellPhone Driver");
            String localDeviceName = new String();
            try {
                LocalDevice localDevice = LocalDevice.getLocalDevice();
                localDeviceName = localDevice.getFriendlyName();
                if (localDeviceName == null || localDeviceName.trim().equals("")) localDeviceName = "D" + localDevice.getBluetoothAddress();
            } catch (BluetoothStateException e) {
                logger.error(e.getMessage());
            }
            nameField = new TextField("Device Name", localDeviceName, 100, TextField.ANY);
            saveNameCommand = new Command("Save", Command.OK, 0);
            exitCommand = new Command("Exit", Command.EXIT, 1);
            identificationForm.append(nameField);
            identificationForm.addCommand(saveNameCommand);
            identificationForm.addCommand(exitCommand);
            identificationForm.setCommandListener(this);
        }
        display.setCurrent(identificationForm);
    }

    /**
	 * Screen to allow the driver user to select which service will be provided.
	 *
	 * @since 2011.0715
	 */
    public void runningServiceScreen() {
        if (runningServiceForm == null) {
            runningServiceForm = new Form("CellPhone Driver");
            messageField1 = new StringItem("Running Service", "Choose the Driver you wish to send messages with.");
            driverChoices = new ChoiceGroup("Service to provide:", Choice.EXCLUSIVE);
            driverChoices.append("Mouse", null);
            driverChoices.append("Keyboard", null);
            selectDriverCommand = new Command("Enter selected Driver", Command.ITEM, 0);
            stopServiceCommand = new Command("Stop", Command.STOP, 1);
            runningServiceForm.append(messageField1);
            runningServiceForm.append(driverChoices);
            runningServiceForm.addCommand(selectDriverCommand);
            runningServiceForm.addCommand(stopServiceCommand);
            runningServiceForm.setCommandListener(this);
        }
        display.setCurrentItem(driverChoices);
        display.setCurrent(runningServiceForm);
    }

    /**
	 * Handles the events generated by the commands in the forms.
	 *
	 * @param command Command that called the event
	 * @param currentForm Current displayable in where the command was called.
	 * @since 2011.0709
	 */
    public void commandAction(Command command, Displayable currentForm) {
        if (currentForm == identificationForm) {
            if (command == saveNameCommand) {
                if (!nameField.getString().trim().equals("")) {
                    runningServiceScreen();
                } else {
                    Alert alert = new Alert("Invalid name", "Device name is a required field.", null, null);
                    alert.setTimeout(3000);
                    display.setCurrent(alert);
                }
            } else if (command == exitCommand) {
                midlet.exitMIDlet();
            }
        } else if (currentForm == runningServiceForm) {
            if (command == selectDriverCommand) {
                broadcastDrivers();
                String driverChosen = driverChoices.getString(driverChoices.getSelectedIndex());
                if (driverChosen.equals("Mouse")) {
                    mouseDriver.showMouseScreen();
                } else if (driverChosen.equals("Keyboard")) {
                    keyboardDriver.showKeyboardScreen();
                }
            } else if (command == stopServiceCommand) {
                keyboardDriver = null;
                mouseDriver = null;
                deviceManager = null;
                identificationScreen();
            }
        }
    }

    /**
	 * Registers the provided Drivers in the middleware.
	 *
	 * @since 2011.0715
	 */
    private void broadcastDrivers() {
        if (deviceManager == null) {
            display.vibrate(300);
            deviceManager = new UosDeviceManager(nameField.getString(), display);
            deviceManager.addNetworkAdapter(new BluetoothNetworkAdapter(true));
            try {
                deviceManager.addDriver(new DeviceDriver(), "defaultDeviceDriver");
                mouseDriver = new MouseDriver();
                deviceManager.addDriver(mouseDriver, "defaultMouseDriver");
                keyboardDriver = new KeyboardDriver();
                deviceManager.addDriver(keyboardDriver, "defaultKeyboardDriver");
            } catch (UosException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
