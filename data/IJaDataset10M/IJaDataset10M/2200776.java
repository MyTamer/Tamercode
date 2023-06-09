package de.johannesluderschmidt.throng.applications;

import java.awt.EventQueue;
import com.muchsoft.util.Sys;
import de.johannesluderschmidt.simpleDebug.Debug;
import de.johannesluderschmidt.throng.oscRecorderPlayer.ui.ThrongOSCDeckGUI;

public class ThrongOSCDeckApplication {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        if (Sys.isMacOSX()) {
            System.setProperty("apple.awt.graphics.EnableQ2DX", "true");
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "OSCRecorderPlayer");
        } else {
        }
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    new ThrongOSCDeckGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        int i = 0;
        boolean correctParameterFound = false;
        for (String arg : args) {
            if (arg.equals("-debug")) {
                correctParameterFound = true;
                try {
                    Debug.enableDebug(Boolean.parseBoolean(args[i + 1]));
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
            i = i + 1;
        }
        if (args.length > 0 && !correctParameterFound) {
            System.out.println("Usage: -debug true|false.");
        }
    }
}
