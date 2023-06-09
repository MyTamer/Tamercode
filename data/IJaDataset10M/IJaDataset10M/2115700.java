package nz.org.venice.ui;

import java.util.StringTokenizer;
import java.util.Vector;
import java.awt.Dimension;
import java.beans.PropertyVetoException;
import javax.swing.*;
import javax.swing.event.*;
import nz.org.venice.main.*;
import nz.org.venice.prefs.PreferencesManager;
import nz.org.venice.prefs.PreferencesException;
import nz.org.venice.util.Locale;

/**
 * This class manages activities to do with internal frames on the desktop
 */
public class DesktopManager extends javax.swing.DefaultDesktopManager implements java.io.Serializable {

    private static int DEFAULT_FRAME_WIDTH = 450;

    private static int DEFAULT_FRAME_HEIGHT = 375;

    /** Tile windows horizontally */
    public static final int HORIZONTAL = 0;

    /** Tile windows vertically */
    public static final int VERTICAL = 1;

    /** Cascade windows, resizing based on desktop size */
    public static final int CASCADE = 2;

    /** Arrange windows in a grid. */
    public static final int ARRANGE = 3;

    private static JDesktopPane desktop_instance = null;

    private static EventListenerList moduleListeners = new EventListenerList();

    private static int messagesDisplayed = 0;

    /**
     * Set the desktop we are managing.
     *
     * @param desktop the desktop to manage.
     */
    public static void setDesktop(JDesktopPane desktop) {
        desktop_instance = desktop;
    }

    /**
     * Get the desktop we are managing.
     *
     * @return the desktop we are managing.
     */
    public static JDesktopPane getDesktop() {
        return desktop_instance;
    }

    /**
     * Create a new Venice desktop manager to manage the given desktop.
     *
     * @param desktop the desktop to manage.
     */
    public DesktopManager(JDesktopPane desktop) {
        super();
        setDesktop(desktop);
    }

    /**
     * Add a listener to list for module events.
     *
     * @param moduleListener the object to be informed about module events.
     */
    public void addModuleListener(ModuleListener moduleListener) {
        moduleListeners.add(ModuleListener.class, moduleListener);
    }

    /**
     * Remove a listener of module events.
     *
     * @param moduleListener the object to no longer be informed about
     *                       module events.
     */
    public void removeModuleListener(ModuleListener moduleListener) {
        moduleListeners.remove(ModuleListener.class, moduleListener);
    }

    /**
     * Inform all the module listeners that this module has been added.
     *
     * @param module that has been added.
     */
    private void fireModuleAdded(Module module) {
        ModuleEvent event = null;
        Object[] listeners = moduleListeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ModuleListener.class) {
                if (event == null) event = new ModuleEvent(module);
                ((ModuleListener) listeners[i + 1]).moduleAdded(event);
            }
        }
    }

    /**
     * Inform all the module listeners that this module has been removed.
     *
     * @param module that has been removed.
     */
    public void fireModuleRemoved(Module module) {
        ModuleEvent event = null;
        Object[] listeners = moduleListeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ModuleListener.class) {
                if (event == null) event = new ModuleEvent(module);
                ((ModuleListener) listeners[i + 1]).moduleRemoved(event);
            }
        }
    }

    /**
     * Inform all the module listneres that this module has been renamed.
     *
     * @param module that has been renamed.
     */
    public void fireModuleRenamed(Module module) {
        ModuleEvent event = null;
        Object[] listeners = moduleListeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ModuleListener.class) {
                if (event == null) event = new ModuleEvent(module);
                ((ModuleListener) listeners[i + 1]).moduleRenamed(event);
            }
        }
    }

    /**
     * Show a simple warning message to the user. Unlike the
     * show error message dialog, this will not cancel the
     * current thread. Use this if the operation is not totally
     * unsuccessful.
     *
     * @param	message	the warning message to display
     */
    public static void showWarningMessage(final String message) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                String multiLineMessage = breakUpMessage(message);
                incrementMessagesDisplayed();
                JOptionPane.showInternalMessageDialog(desktop_instance, multiLineMessage, Locale.getString("VENICE_PROBLEM_TITLE"), JOptionPane.WARNING_MESSAGE);
                decrementMessagesDisplayed();
            }
        });
    }

    /**
     * Show a simple error message to the user. This will also
     * cancel the current thread if the progress dialog is up.
     *
     * @param message	the error message to display
     */
    public static void showErrorMessage(String message) {
        showErrorMessage(Locale.getString("VENICE_PROBLEM_TITLE"), message);
    }

    /**
     * Show a simple error message to the user. This will also
     * cancel the current thread if the progress dialog is up.
     *
     * @param title   the title of the dialog to display
     * @param message the error message to display
     */
    public static void showErrorMessage(final String title, final String message) {
        if (ProgressDialogManager.isProgressDialogUp()) Thread.currentThread().interrupt();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                String multiLineMessage = breakUpMessage(message);
                incrementMessagesDisplayed();
                JOptionPane.showInternalMessageDialog(desktop_instance, multiLineMessage, title, JOptionPane.ERROR_MESSAGE);
                decrementMessagesDisplayed();
            }
        });
    }

    /**
     * Return whether we are currently displaying any warning or error messages.
     *
     * @return <code>true</code> if we are displaying a message,
     *         <code>false</code> otherwise.
     */
    public static boolean isDisplayingMessage() {
        synchronized (desktop_instance) {
            return messagesDisplayed > 0;
        }
    }

    private static void incrementMessagesDisplayed() {
        synchronized (desktop_instance) {
            messagesDisplayed++;
        }
    }

    private static void decrementMessagesDisplayed() {
        synchronized (desktop_instance) {
            messagesDisplayed--;
        }
    }

    private static String breakUpMessage(String message) {
        StringTokenizer tokeniser = new StringTokenizer(message, " \n", true);
        String multiLineMessage = "";
        final int MAX_LINE_LENGTH = 40;
        int lineLength = 0;
        while (tokeniser.hasMoreTokens()) {
            String token = tokeniser.nextToken();
            if (token.equals("\n")) lineLength = 0;
            if (lineLength > MAX_LINE_LENGTH) {
                multiLineMessage = multiLineMessage.concat("\n");
                lineLength = 0;
            }
            if (lineLength > 0 || !token.equals(" ")) multiLineMessage = multiLineMessage.concat(token);
            if (!token.equals("\n")) lineLength += token.length();
        }
        return multiLineMessage;
    }

    /**
     * Tiles all the windows in the desktop according to the given style.
     *
     * @param style one of {@link #HORIZONTAL}, {@link #VERTICAL}, {@link #CASCADE}
     *              or {@link #ARRANGE}
     */
    public static void tileFrames(int style) {
        Dimension deskDim = desktop_instance.getSize();
        int deskWidth = deskDim.width;
        int deskHeight = deskDim.height;
        JInternalFrame[] frames = desktop_instance.getAllFrames();
        int frameCount = frames.length;
        int frameWidth = 0;
        int frameHeight = 0;
        int xpos = 0;
        int ypos = 0;
        double scale = 0.6;
        int spacer = 30;
        int frameCounter = 0;
        Vector frameVec = new Vector(1, 1);
        boolean areIcons = false;
        int tempy = 0, tempx = 0;
        for (int i = 0; i < frameCount; i++) {
            if (frames[i].isVisible() && !frames[i].isIcon() && frames[i].isResizable()) {
                frameVec.addElement(frames[i]);
                frameCounter++;
            } else if (frames[i].isIcon()) areIcons = true;
        }
        if (areIcons) deskHeight = deskHeight - 50;
        switch(style) {
            case (HORIZONTAL):
                for (int i = 0; i < frameCounter; i++) {
                    JInternalFrame temp = (JInternalFrame) frameVec.elementAt(i);
                    frameWidth = deskWidth;
                    frameHeight = (int) (deskHeight / frameCounter);
                    temp.reshape(xpos, ypos, frameWidth, frameHeight);
                    ypos = ypos + frameHeight;
                    temp.moveToFront();
                }
                break;
            case (VERTICAL):
                for (int i = 0; i < frameCounter; i++) {
                    JInternalFrame temp = (JInternalFrame) frameVec.elementAt(i);
                    frameWidth = (int) (deskWidth / frameCounter);
                    frameHeight = deskHeight;
                    if (temp.isResizable()) temp.reshape(xpos, ypos, frameWidth, frameHeight); else temp.setLocation(xpos, ypos);
                    xpos = xpos + frameWidth;
                    temp.moveToFront();
                }
                break;
            case (CASCADE):
                for (int i = 0; i < frameCounter; i++) {
                    JInternalFrame temp = (JInternalFrame) frameVec.elementAt(i);
                    frameWidth = (int) (deskWidth * scale);
                    frameHeight = (int) (deskHeight * scale);
                    if (temp.isResizable()) temp.reshape(xpos, ypos, frameWidth, frameHeight); else temp.setLocation(xpos, ypos);
                    temp.moveToFront();
                    xpos = xpos + spacer;
                    ypos = ypos + spacer;
                    if ((xpos + frameWidth > deskWidth) || (ypos + frameHeight > deskHeight - 50)) {
                        xpos = 0;
                        ypos = 0;
                    }
                }
                break;
            case (ARRANGE):
                int row = new Long(Math.round(Math.sqrt(new Integer(frameCounter).doubleValue()))).intValue();
                if (row == 0) break;
                int col = frameCounter / row;
                if (col == 0) break;
                int rem = frameCounter % row;
                int rowCount = 1;
                frameWidth = (int) deskWidth / col;
                frameHeight = (int) deskHeight / row;
                for (int i = 0; i < frameCounter; i++) {
                    JInternalFrame temp = (JInternalFrame) frameVec.elementAt(i);
                    if (rowCount <= row - rem) {
                        if (temp.isResizable()) temp.reshape(xpos, ypos, frameWidth, frameHeight); else temp.setLocation(xpos, ypos);
                        if (xpos + 10 < deskWidth - frameWidth) xpos = xpos + frameWidth; else {
                            ypos = ypos + frameHeight;
                            xpos = 0;
                            rowCount++;
                        }
                    } else {
                        frameWidth = (int) deskWidth / (col + 1);
                        if (temp.isResizable()) temp.reshape(xpos, ypos, frameWidth, frameHeight); else temp.setLocation(xpos, ypos);
                        if (xpos + 10 < deskWidth - frameWidth) xpos = xpos + frameWidth; else {
                            ypos = ypos + frameHeight;
                            xpos = 0;
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * Minimises all windows that are iconifiable.
     */
    public static void minimizeWindows() {
        JInternalFrame[] openWindows = desktop_instance.getAllFrames();
        for (int i = 0; i < openWindows.length; i++) if (openWindows[i].isIconifiable()) {
            try {
                openWindows[i].setIcon(true);
            } catch (java.beans.PropertyVetoException pve) {
                pve.printStackTrace();
            }
        }
    }

    /**
     * Restores all minimised windows.
     */
    public static void restoreAll() {
        JInternalFrame[] openWindows = desktop_instance.getAllFrames();
        for (int i = 0; i < openWindows.length; i++) {
            if (openWindows[i].isIcon()) try {
                openWindows[i].setIcon(false);
            } catch (java.beans.PropertyVetoException pve) {
                pve.printStackTrace();
            }
        }
    }

    /**
     * Closes all open windows.
     */
    public static void closeAllWindows() {
        JInternalFrame[] openWindows = desktop_instance.getAllFrames();
        for (int i = 0; i < openWindows.length; i++) {
            openWindows[i].dispose();
        }
    }

    /**
     * Display a new frame upon the current desktop. Frame will be
     * displayed at (0,0) and not centred.
     *
     * @param module the module to render in the frame
     * @return	module frame
     */
    public ModuleFrame newFrame(Module module) {
        return newFrame(module, false, false, true);
    }

    /**
     * Display a new frame upon the current desktop
     *
     * @param module the module to render in the frame
     * @param centre should the frame be centred?
     * @param honourSize should we honour the frame's preferred size?
     * @param resizable  is the frame allowed to be resized?
     * @return	module frame
     */
    public ModuleFrame newFrame(Module module, boolean centre, boolean honourSize, boolean resizable) {
        ModuleFrame frame = new ModuleFrame(this, module, centre, honourSize, resizable);
        fireModuleAdded(module);
        desktop_instance.add(frame);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException v) {
        }
        frame.moveToFront();
        return frame;
    }

    public void activateFrame(JInternalFrame frame) {
    }

    /**
     * Call save() on every open module. This will save all the modules'
     * preferences data.
     */
    public void save() {
        JInternalFrame[] frames = desktop_instance.getAllFrames();
        for (int i = 0; i < frames.length; i++) {
            JInternalFrame frame = frames[i];
            if (frame instanceof ModuleFrame) {
                ModuleFrame moduleFrame = (ModuleFrame) frame;
                Module module = moduleFrame.getModule();
                module.save();
                try {
                    PreferencesManager.putModuleFrameSettings(moduleFrame);
                } catch (PreferencesException pfe) {
                }
            }
        }
    }
}
