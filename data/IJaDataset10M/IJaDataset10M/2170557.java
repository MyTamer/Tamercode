package core;

import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.jsynthlib.jsynthlib.Dummy;
import org.jsynthlib.utils.AWTUtils;

public class AppConfig {

    static final int GUI_MDI = 0;

    static final int GUI_SDI = 1;

    private static ArrayList deviceList = new ArrayList();

    private static Preferences prefs = Preferences.userNodeForPackage(Dummy.class);

    private static Preferences prefsDev = prefs.node("devices");

    /**
     * Initialize.
     */
    static {
        try {
            prefs.sync();
        } catch (BackingStoreException e) {
            ErrorMsg.reportStatus(e);
        }
        setLookAndFeel(getLookAndFeel());
    }

    /**
     * Restore deviceList.
     */
    static boolean loadPrefs() {
        try {
            String[] devs;
            if (prefsDev.nodeExists("Generic#0")) addDevice("synthdrivers.Generic.GenericDevice", prefsDev.node("Generic#0")); else addDevice("synthdrivers.Generic.GenericDevice");
            devs = prefsDev.childrenNames();
            for (int i = 0; i < devs.length; i++) {
                if (devs[i].equals("Generic#0")) continue;
                String s = devs[i].substring(0, devs[i].indexOf('#'));
                DevicesConfig devConfig = DevicesConfig.getInstance();
                String className = devConfig.getClassNameForShortName(s);
                addDevice(className, prefsDev.node(devs[i]));
            }
            return true;
        } catch (BackingStoreException e) {
            ErrorMsg.reportStatus("loadPrefs: " + e);
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            ErrorMsg.reportStatus("loadPrefs: " + e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This routine just saves the current settings in the config
     * file. Its called when the user quits the app.
     */
    static void savePrefs() {
        try {
            store();
        } catch (Exception e) {
            ErrorMsg.reportError("Error", "Unable to Save Preferences", e);
        }
    }

    /**
     * @throws BackingStoreException
     */
    private static void store() throws BackingStoreException {
        prefs.flush();
    }

    /** Getter for libPath for library/scene file. */
    static String getLibPath() {
        return prefs.get("libPath", ".");
    }

    /** Setter for libPath for library/scene file. */
    static void setLibPath(String libPath) {
        prefs.put("libPath", libPath);
    }

    /** Getter for XML Path */
    static String getXMLpaths() {
        return prefs.get("XMLpaths", "");
    }

    /** Setter for XML Path */
    static void setXMLpaths(String libPath) {
        prefs.put("XMLpaths", libPath);
    }

    /** Getter for sysexPath for import/export Sysex Message. */
    static String getSysexPath() {
        return prefs.get("sysexPath", ".");
    }

    /** Setter for sysexPath for import/export Sysex Message. */
    static void setSysexPath(String sysexPath) {
        prefs.put("sysexPath", sysexPath);
    }

    /** Getter for default library which is open at start-up. */
    static String getDefaultLibrary() {
        return prefs.get("defaultLib", "");
    }

    /** Setter for default library which is open at start-up. */
    static void setDefaultLibrary(String file) {
        prefs.put("defaultLib", file);
    }

    /** Getter for sequencerEnable */
    static boolean getSequencerEnable() {
        return prefs.getBoolean("sequencerEnable", false);
    }

    /** Setter for sequencerEnable */
    static void setSequencerEnable(boolean sequencerEnable) {
        prefs.putBoolean("sequencerEnable", sequencerEnable);
    }

    /** Getter for midi file (Sequence) to play */
    static String getSequencePath() {
        return prefs.get("sequencePath", "");
    }

    /** Setter for midi file (Sequence) to play */
    static void setSequencePath(String sequencePath) {
        prefs.put("sequencePath", sequencePath);
    }

    /** Getter for note */
    public static int getNote() {
        return prefs.getInt("note", 0);
    }

    /** Setter for note */
    static void setNote(int note) {
        prefs.putInt("note", note);
    }

    /** Getter for velocity */
    public static int getVelocity() {
        return prefs.getInt("velocity", 0);
    }

    /** Setter for velocity */
    static void setVelocity(int velocity) {
        prefs.putInt("velocity", velocity);
    }

    /** Getter for delay */
    public static int getDelay() {
        return prefs.getInt("delay", 0);
    }

    /** Setter for delay */
    static void setDelay(int delay) {
        prefs.putInt("delay", delay);
    }

    /**Getter for RepositoryURL */
    static String getRepositoryURL() {
        return prefs.get("repositoryURL", "http://www.jsynthlib.org");
    }

    /**Setter for RepositoryURL */
    static void setRepositoryURL(String url) {
        prefs.put("repositoryURL", url);
    }

    /**Getter for RepositoryUser */
    static String getRepositoryUser() {
        return prefs.get("repositoryUser", "");
    }

    /**Setter for RepositoryUser */
    static void setRepositoryUser(String user) {
        prefs.put("repositoryUser", user);
    }

    /**Getter for RepositoryPass */
    static String getRepositoryPass() {
        return prefs.get("repositoryPass", "");
    }

    /**Setter for RepositoryPass */
    static void setRepositoryPass(String password) {
        prefs.put("repositoryPass", password);
    }

    /** Getter for lookAndFeel */
    static String getLookAndFeel() {
        return prefs.get("lookAndFeel", "Nimbus");
    }

    /** Setter for lookAndFeel */
    static void setLookAndFeel(String lookAndFeel) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        prefs.put("lookAndFeel", lookAndFeel);
        UIManager.LookAndFeelInfo[] installedLF;
        installedLF = UIManager.getInstalledLookAndFeels();
        try {
            for (UIManager.LookAndFeelInfo info : installedLF) {
                if (info.getName().equals(lookAndFeel)) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            ErrorMsg.reportStatus(e);
        }
    }

    /** Getter for guiStyle */
    static int getGuiStyle() {
        return prefs.getInt("guiStyle", MacUtils.isMac() ? GUI_SDI : GUI_MDI);
    }

    /** Setter for guiStyle */
    static void setGuiStyle(int guiStyle) {
        prefs.putInt("guiStyle", guiStyle);
    }

    /** Getter for tool bar */
    static boolean getToolBar() {
        return prefs.getBoolean("toolBar", MacUtils.isMac());
    }

    /** Setter for tool bar */
    static void setToolBar(boolean b) {
        prefs.putBoolean("toolBar", b);
    }

    /**
     * Getter for midiEnable. Returns false if either MIDI input nor
     * output is not available.
     */
    static boolean getMidiEnable() {
        return ((MidiUtil.isOutputAvailable() || MidiUtil.isInputAvailable()) && prefs.getBoolean("midiEnable", false));
    }

    /** Setter for midiEnable */
    static void setMidiEnable(boolean midiEnable) {
        prefs.putBoolean("midiEnable", midiEnable);
    }

    /** Getter for initPortIn */
    static int getInitPortIn() {
        String uniqueName = prefs.get("initPortIn", "");
        String[] inputNames = MidiUtil.getInputNames();
        for (int i = 0; i < inputNames.length; ++i) {
            if (inputNames[i].equals(uniqueName)) {
                return i;
            }
        }
        return 0;
    }

    /** Setter for initPortIn */
    static void setInitPortIn(int initPortIn) {
        if (initPortIn < 0) initPortIn = 0;
        prefs.put("initPortIn", MidiUtil.getInputNames()[initPortIn]);
    }

    /** Getter for initPortOut */
    static int getInitPortOut() {
        String uniqueName = prefs.get("initPortOut", "");
        String[] outputNames = MidiUtil.getOutputNames();
        for (int i = 0; i < outputNames.length; ++i) {
            if (outputNames[i].equals(uniqueName)) {
                return i;
            }
        }
        return 0;
    }

    /** Setter for initPortOut */
    static void setInitPortOut(int initPortOut) {
        if (initPortOut < 0) initPortOut = 0;
        prefs.put("initPortOut", MidiUtil.getOutputNames()[initPortOut]);
    }

    /**
     * Getter for masterInEnable. Returns false if either MIDI input or
     * output is unavailable.
     */
    static boolean getMasterInEnable() {
        return (MidiUtil.isOutputAvailable() && MidiUtil.isInputAvailable() && getMidiEnable() && prefs.getBoolean("masterInEnable", false));
    }

    /** Setter for masterInEnable */
    static void setMasterInEnable(boolean masterInEnable) {
        PatchEdit.masterInEnable(masterInEnable);
        prefs.putBoolean("masterInEnable", masterInEnable);
    }

    /** Getter for masterController */
    static int getMasterController() {
        return prefs.getInt("masterController", 0);
    }

    /** Setter for masterController */
    static void setMasterController(int masterController) {
        prefs.putInt("masterController", masterController);
    }

    /** Getter for MIDI Output Buffer size. */
    static int getMidiOutBufSize() {
        return prefs.getInt("midiOutBufSize", 0);
    }

    /** Setter for MIDI Output Buffer size. */
    static void setMidiOutBufSize(int size) {
        prefs.putInt("midiOutBufSize", size);
    }

    /** Getter for MIDI Output delay time (msec). */
    static int getMidiOutDelay() {
        return prefs.getInt("midiOutDelay", 0);
    }

    /** Setter for MIDI Output delay time (msec). */
    static void setMidiOutDelay(int msec) {
        prefs.putInt("midiOutDelay", msec);
    }

    /**
     * Getter for faderEnable. Returns false if MIDI input is
     * unavailable.
     */
    static boolean getFaderEnable() {
        return (MidiUtil.isOutputAvailable() && getMidiEnable() && prefs.getBoolean("faderEnable", false));
    }

    /** Setter for faderEnable */
    static void setFaderEnable(boolean faderEnable) {
        prefs.putBoolean("faderEnable", faderEnable);
    }

    /** Getter for faderPort */
    static int getFaderPort() {
        return prefs.getInt("faderPort", 0);
    }

    /** Setter for faderPort */
    static void setFaderPort(int faderPort) {
        prefs.putInt("faderPort", faderPort);
    }

    /** Indexed getter for fader Channel number */
    static int getFaderChannel(int i) {
        return prefs.getInt("faderChannel" + i, 0);
    }

    /** Indexed setter for fader Channel number */
    static void setFaderChannel(int i, int faderChannel) {
        prefs.putInt("faderChannel" + i, faderChannel);
    }

    /** Indexed getter for fader Control number */
    static int getFaderControl(int i) {
        int n = prefs.getInt("faderControl" + i, 0);
        return n > 120 ? 120 : n;
    }

    /** Indexed setter for fader Control number. */
    static void setFaderControl(int i, int faderControl) {
        prefs.putInt("faderControl" + i, faderControl);
    }

    /** Getter for Multiple MIDI Interface enable */
    static boolean getMultiMIDI() {
        return prefs.getBoolean("multiMIDI", false);
    }

    /** Setter for midiEnable */
    static void setMultiMIDI(boolean enable) {
        prefs.putBoolean("multiMIDI", enable);
    }

    /**
     * Add Device into <code>deviceList</code>.
     *
     * @param className name of Device class
     * (ex. "synthdrivers.KawaiK4.KawaiK4Device").
     * @param prefs <code>Preferences</code> node for the  Device.
     * @return a <code>Device</code> value created.
     */
    private static Device addDevice(String className, Preferences prefs) {
        DevicesConfig devConfig = DevicesConfig.getInstance();
        Device device = devConfig.createDevice(className, prefs);
        if (device != null) {
            device.setup();
            deviceList.add(device);
        }
        return device;
    }

    static Device addDevice(String className) {
        return addDevice(className, getDeviceNode(className));
    }

    /** returns the 1st unused device node name for Preferences. */
    private static Preferences getDeviceNode(String s) {
        ErrorMsg.reportStatus("getDeviceNode: " + s);
        s = DevicesConfig.getShortNameForClassName(s);
        ErrorMsg.reportStatus("getDeviceNode: -> " + s);
        int i;
        try {
            for (i = 0; prefsDev.nodeExists(s + "#" + i); i++) ;
            return prefsDev.node(s + "#" + i);
        } catch (BackingStoreException e) {
            ErrorMsg.reportStatus(e);
            return null;
        }
    }

    /** Indexed getter for deviceList elements */
    public static Device getDevice(int i) {
        return (Device) deviceList.get(i);
    }

    /**
     * Remover for deviceList elements.
     * The caller must call reassignDeviceDriverNums and revalidateLibraries.
     * @return <code>Device</code> object removed.
     */
    static Device removeDevice(int i) {
        Device ret = (Device) deviceList.remove(i);
        try {
            ret.getPreferences().removeNode();
        } catch (BackingStoreException e) {
            ErrorMsg.reportStatus(e);
        }
        return ret;
    }

    /** Size query for deviceList */
    public static int deviceCount() {
        return deviceList.size();
    }

    /** Getter for the index of <code>device</code>. */
    static int getDeviceIndex(Device device) {
        return deviceList.indexOf(device);
    }

    /**
     * Returns null driver of Generic Device.  It is used when proper
     * driver is not found.
     */
    public static IPatchDriver getNullDriver() {
        return (IPatchDriver) getDevice(0).getDriver(0);
    }

    /**
	 * Set frame to remembered size. Updates position, size, and 'maximized'
	 * state.
	 * 
	 * @return true if frame's size was changed
	 */
    public static boolean getMainWindowBounds(JFrame frame) {
        String val = prefs.get("mainWindow", null);
        if (val == null) return false; else {
            String[] ints = val.split("\\s+");
            try {
                int x = Integer.parseInt(ints[0]);
                int y = Integer.parseInt(ints[1]);
                int width = Integer.parseInt(ints[2]);
                int height = Integer.parseInt(ints[3]);
                boolean isMax = Boolean.parseBoolean(ints[4]);
                frame.setBounds(x, y, width, height);
                if (isMax) frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                AWTUtils.moveOnScreen(frame);
                return true;
            } catch (Throwable t) {
                return false;
            }
        }
    }

    public static void setMainWindowBounds(JFrame frame) {
        boolean isMax = (frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0;
        String val = String.format("%d %d %d %d %s", frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight(), isMax);
        prefs.put("mainWindow", val);
    }
}
