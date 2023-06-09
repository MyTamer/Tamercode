package org.bungeni.extutils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JRootPane;

/**
 *
 * @author Ashok Hariharan
 */
public class CommonUIFunctions {

    public static void setPanelBackground(JPanel panel) {
        String genBackColor = BungeniEditorProperties.getEditorProperty("genericPanelBackColor");
        if (genBackColor.trim().length() == 0) {
            panel.setBackground(Color.LIGHT_GRAY);
        } else {
            panel.setBackground(Color.decode(genBackColor));
        }
    }

    public static ArrayList<String> findComponentsWithNames(Container container) {
        ArrayList<String> fieldsWithNames = new ArrayList<String>(0);
        for (Component component : container.getComponents()) {
            String sCompName = component.getName();
            if (sCompName == null) {
                continue;
            } else {
                if (sCompName.trim().length() == 0) {
                    continue;
                } else {
                    fieldsWithNames.add(sCompName.trim());
                }
            }
        }
        return fieldsWithNames;
    }

    public static Component findComponentByName(Container container, String componentName) {
        for (Component component : container.getComponents()) {
            if (componentName.equals(component.getName())) {
                return component;
            }
            if (component instanceof JRootPane) {
                JRootPane nestedJRootPane = (JRootPane) component;
                return findComponentByName(nestedJRootPane.getContentPane(), componentName);
            }
            if (component instanceof JPanel) {
                JPanel nestedJPanel = (JPanel) component;
                return findComponentByName(nestedJPanel, componentName);
            }
        }
        return null;
    }
}
