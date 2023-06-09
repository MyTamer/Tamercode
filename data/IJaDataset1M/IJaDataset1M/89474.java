package org.bungeni.utils;

import java.awt.Component;
import javax.swing.JOptionPane;

public class MessageBox extends Object {

    public static void OK(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg);
    }

    public static void OK(Component parent, Object[] msgs) {
        JOptionPane.showMessageDialog(parent, msgs);
    }

    public static void OK(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public static void OK(Component parent, String msg, String title, int type) {
        JOptionPane.showMessageDialog(parent, msg, title, type);
    }

    public static int Confirm(Component parent, String msg, String title) {
        int ret = JOptionPane.showConfirmDialog(parent, msg, title, JOptionPane.YES_NO_OPTION);
        return ret;
    }
}
