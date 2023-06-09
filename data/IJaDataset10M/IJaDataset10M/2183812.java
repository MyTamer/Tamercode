package com.maddyhome.idea.copyright.ui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Font;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class OverviewTab {

    public OverviewTab() {
        setupControls();
    }

    public JComponent getMainComponent() {
        return mainPanel;
    }

    private void setupControls() {
        try {
            InputStream is = getClass().getResourceAsStream("/README");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                overviewTxt.append(line);
                overviewTxt.append("\n");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        Font font = overviewTxt.getFont();
        Font newFont = new Font("Monospaced", Font.PLAIN, font.getSize());
        overviewTxt.setFont(newFont);
        overviewTxt.setColumns(80);
        overviewTxt.setCaretPosition(0);
        scrollpane.getVerticalScrollBar().setValue(0);
    }

    {
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR call it in your
     * code!
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        scrollpane = new JScrollPane();
        mainPanel.add(scrollpane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null));
        overviewTxt = new JTextArea();
        overviewTxt.setColumns(80);
        overviewTxt.setEditable(false);
        overviewTxt.setRows(30);
        scrollpane.setViewportView(overviewTxt);
    }

    private JPanel mainPanel;

    private JTextArea overviewTxt;

    private JScrollPane scrollpane;

    private static Logger logger = Logger.getInstance(OverviewTab.class.getName());
}
