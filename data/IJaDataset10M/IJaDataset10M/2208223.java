package com.celiosilva.swingDK.dataFields;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextArea;

/**
 *
 * @author Administrador
 */
public class TextAreaField extends JTextArea implements KeyListener {

    public TextAreaField() {
        this.addKeyListener(this);
    }

    public void keyTyped(KeyEvent e) {
        Pattern p = Pattern.compile("[\\w ,._-]");
        Matcher m = p.matcher(String.valueOf(e.getKeyChar()));
        if (m.find()) {
            e.setKeyChar(Character.toUpperCase(e.getKeyChar()));
        } else {
            e.setKeyChar(' ');
        }
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
