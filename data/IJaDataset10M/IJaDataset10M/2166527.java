package org.tracfoundation.trac2001.gui;

import org.tracfoundation.trac2001.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A basic GUI frame for running the TRAC 2001 processor when
 * the program can't recognize the host system.
 *
 * This was mostly generated by JBuilder.
 *
 * @author Edith Mooers, Trac Foundation http://tracfoundation.org
 * @version 1.0 (c) 2001
 */
public class TRACFrame {

    boolean packFrame = false;

    public TRACFrame(TRAC2001 trac, JFrame frame) {
        if (packFrame) {
            frame.pack();
        } else {
            frame.validate();
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
}