package org.exmaralda.partitureditor.jexmaraldaswing;

import org.exmaralda.common.helpers.Internationalizer;
import org.exmaralda.partitureditor.interlinearText.PageOutputParameters;
import org.exmaralda.partitureditor.interlinearText.*;

/**
 *
 * @author  thomas
 */
public class PageSetupDialog extends javax.swing.JDialog {

    PageOutputParameters pageOutputParameters;

    public boolean OK = false;

    /** Creates new form PageSetupDialog */
    public PageSetupDialog(PageOutputParameters pop, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        pageOutputParameters = pop;
        short CM = PageOutputParameters.CM_UNIT;
        widthTextField.setText(Double.toString(Math.round(100 * pop.getPaperMeasure("paper:width", CM)) / 100.0));
        heightTextField.setText(Double.toString(Math.round(100 * pop.getPaperMeasure("paper:height", CM)) / 100.0));
        leftMarginTextField.setText(Double.toString(Math.round(100 * pop.getPaperMeasure("margin:left", CM)) / 100.0));
        rightMarginTextField.setText(Double.toString(Math.round(100 * pop.getPaperMeasure("margin:right", CM)) / 100.0));
        topMarginTextField.setText(Double.toString(Math.round(100 * pop.getPaperMeasure("margin:top", CM)) / 100.0));
        bottomMarginTextField.setText(Double.toString(Math.round(100 * pop.getPaperMeasure("margin:bottom", CM)) / 100.0));
        if (pop.landscape) {
            landscapeRadioButton.setSelected(true);
        } else {
            portraitRadioButton.setSelected(true);
        }
        Internationalizer.internationalizeDialogToolTips(this);
    }

    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        okCancelPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        paperSizePanel = new javax.swing.JPanel();
        widthPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        widthTextField = new javax.swing.JTextField();
        cmLabel6 = new javax.swing.JLabel();
        heightPanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        heightTextField = new javax.swing.JTextField();
        cmLabel7 = new javax.swing.JLabel();
        orientationPanel = new javax.swing.JPanel();
        portraitRadioButton = new javax.swing.JRadioButton();
        landscapeRadioButton = new javax.swing.JRadioButton();
        marginsPanel = new javax.swing.JPanel();
        topMarginPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        topMarginTextField = new javax.swing.JTextField();
        cmLabel = new javax.swing.JLabel();
        bottomMarginPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        bottomMarginTextField = new javax.swing.JTextField();
        cmLabel5 = new javax.swing.JLabel();
        leftMarginPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        leftMarginTextField = new javax.swing.JTextField();
        cmLabel3 = new javax.swing.JLabel();
        rightMarginPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        rightMarginTextField = new javax.swing.JTextField();
        cmLabel4 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Page setup");
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        okCancelPanel.add(okButton);
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        okCancelPanel.add(cancelButton);
        getContentPane().add(okCancelPanel, java.awt.BorderLayout.SOUTH);
        mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.Y_AXIS));
        paperSizePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Paper format"));
        paperSizePanel.setLayout(new java.awt.GridLayout(1, 2, 15, 0));
        widthPanel.setLayout(new javax.swing.BoxLayout(widthPanel, javax.swing.BoxLayout.LINE_AXIS));
        jLabel8.setText("Width: ");
        jLabel8.setMaximumSize(new java.awt.Dimension(65, 14));
        jLabel8.setMinimumSize(new java.awt.Dimension(65, 14));
        jLabel8.setPreferredSize(new java.awt.Dimension(65, 14));
        widthPanel.add(jLabel8);
        widthTextField.setMaximumSize(new java.awt.Dimension(60, 30));
        widthTextField.setPreferredSize(new java.awt.Dimension(60, 30));
        widthTextField.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                widthTextFieldFocusGained(evt);
            }
        });
        widthPanel.add(widthTextField);
        cmLabel6.setText(" cm");
        widthPanel.add(cmLabel6);
        paperSizePanel.add(widthPanel);
        heightPanel.setLayout(new javax.swing.BoxLayout(heightPanel, javax.swing.BoxLayout.LINE_AXIS));
        jLabel9.setText("Height: ");
        jLabel9.setMaximumSize(new java.awt.Dimension(65, 14));
        jLabel9.setMinimumSize(new java.awt.Dimension(65, 14));
        jLabel9.setPreferredSize(new java.awt.Dimension(65, 14));
        heightPanel.add(jLabel9);
        heightTextField.setMaximumSize(new java.awt.Dimension(60, 30));
        heightTextField.setPreferredSize(new java.awt.Dimension(60, 30));
        heightTextField.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                heightTextFieldFocusGained(evt);
            }
        });
        heightPanel.add(heightTextField);
        cmLabel7.setText(" cm");
        heightPanel.add(cmLabel7);
        paperSizePanel.add(heightPanel);
        mainPanel.add(paperSizePanel);
        orientationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Orientation"));
        orientationPanel.setLayout(new java.awt.GridLayout(1, 1, 0, 10));
        buttonGroup1.add(portraitRadioButton);
        portraitRadioButton.setText("Portrait     ");
        portraitRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        portraitRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        orientationPanel.add(portraitRadioButton);
        buttonGroup1.add(landscapeRadioButton);
        landscapeRadioButton.setText("Landscape");
        landscapeRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        landscapeRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        orientationPanel.add(landscapeRadioButton);
        mainPanel.add(orientationPanel);
        marginsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Margins"));
        marginsPanel.setLayout(new java.awt.GridLayout(2, 2, 15, 0));
        topMarginPanel.setLayout(new javax.swing.BoxLayout(topMarginPanel, javax.swing.BoxLayout.LINE_AXIS));
        jLabel1.setText("Top: ");
        jLabel1.setMaximumSize(new java.awt.Dimension(65, 14));
        jLabel1.setMinimumSize(new java.awt.Dimension(65, 14));
        jLabel1.setPreferredSize(new java.awt.Dimension(65, 14));
        topMarginPanel.add(jLabel1);
        topMarginTextField.setMaximumSize(new java.awt.Dimension(60, 30));
        topMarginTextField.setPreferredSize(new java.awt.Dimension(60, 30));
        topMarginTextField.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                topMarginTextFieldFocusGained(evt);
            }
        });
        topMarginPanel.add(topMarginTextField);
        cmLabel.setText(" cm");
        topMarginPanel.add(cmLabel);
        marginsPanel.add(topMarginPanel);
        bottomMarginPanel.setLayout(new javax.swing.BoxLayout(bottomMarginPanel, javax.swing.BoxLayout.LINE_AXIS));
        jLabel7.setText("Bottom: ");
        jLabel7.setMaximumSize(new java.awt.Dimension(65, 14));
        jLabel7.setMinimumSize(new java.awt.Dimension(65, 14));
        jLabel7.setPreferredSize(new java.awt.Dimension(65, 14));
        bottomMarginPanel.add(jLabel7);
        bottomMarginTextField.setMaximumSize(new java.awt.Dimension(60, 30));
        bottomMarginTextField.setPreferredSize(new java.awt.Dimension(60, 30));
        bottomMarginTextField.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                bottomMarginTextFieldFocusGained(evt);
            }
        });
        bottomMarginPanel.add(bottomMarginTextField);
        cmLabel5.setText(" cm");
        bottomMarginPanel.add(cmLabel5);
        marginsPanel.add(bottomMarginPanel);
        leftMarginPanel.setLayout(new javax.swing.BoxLayout(leftMarginPanel, javax.swing.BoxLayout.LINE_AXIS));
        jLabel4.setText("Left: ");
        jLabel4.setMaximumSize(new java.awt.Dimension(65, 14));
        jLabel4.setMinimumSize(new java.awt.Dimension(65, 14));
        jLabel4.setPreferredSize(new java.awt.Dimension(65, 14));
        leftMarginPanel.add(jLabel4);
        leftMarginTextField.setMaximumSize(new java.awt.Dimension(60, 30));
        leftMarginTextField.setPreferredSize(new java.awt.Dimension(60, 30));
        leftMarginTextField.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                leftMarginTextFieldFocusGained(evt);
            }
        });
        leftMarginPanel.add(leftMarginTextField);
        cmLabel3.setText(" cm");
        leftMarginPanel.add(cmLabel3);
        marginsPanel.add(leftMarginPanel);
        rightMarginPanel.setLayout(new javax.swing.BoxLayout(rightMarginPanel, javax.swing.BoxLayout.LINE_AXIS));
        jLabel5.setText("Right:");
        jLabel5.setMaximumSize(new java.awt.Dimension(65, 14));
        jLabel5.setMinimumSize(new java.awt.Dimension(65, 14));
        jLabel5.setPreferredSize(new java.awt.Dimension(65, 14));
        rightMarginPanel.add(jLabel5);
        rightMarginTextField.setMaximumSize(new java.awt.Dimension(60, 30));
        rightMarginTextField.setPreferredSize(new java.awt.Dimension(60, 30));
        rightMarginTextField.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                rightMarginTextFieldFocusGained(evt);
            }
        });
        rightMarginPanel.add(rightMarginTextField);
        cmLabel4.setText(" cm");
        rightMarginPanel.add(cmLabel4);
        marginsPanel.add(rightMarginPanel);
        mainPanel.add(marginsPanel);
        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);
        pack();
    }

    private void rightMarginTextFieldFocusGained(java.awt.event.FocusEvent evt) {
        rightMarginTextField.selectAll();
    }

    private void leftMarginTextFieldFocusGained(java.awt.event.FocusEvent evt) {
        leftMarginTextField.selectAll();
    }

    private void bottomMarginTextFieldFocusGained(java.awt.event.FocusEvent evt) {
        bottomMarginTextField.selectAll();
    }

    private void topMarginTextFieldFocusGained(java.awt.event.FocusEvent evt) {
        topMarginTextField.selectAll();
    }

    private void widthTextFieldFocusGained(java.awt.event.FocusEvent evt) {
        widthTextField.selectAll();
    }

    private void heightTextFieldFocusGained(java.awt.event.FocusEvent evt) {
        heightTextField.selectAll();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        OK = false;
        setVisible(false);
        dispose();
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (parseValues()) {
            OK = true;
            setVisible(false);
            dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog((java.awt.Component) null, "One or more values are illegal.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
            }
        });
    }

    private javax.swing.JPanel bottomMarginPanel;

    private javax.swing.JTextField bottomMarginTextField;

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JButton cancelButton;

    private javax.swing.JLabel cmLabel;

    private javax.swing.JLabel cmLabel3;

    private javax.swing.JLabel cmLabel4;

    private javax.swing.JLabel cmLabel5;

    private javax.swing.JLabel cmLabel6;

    private javax.swing.JLabel cmLabel7;

    private javax.swing.JPanel heightPanel;

    private javax.swing.JTextField heightTextField;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JRadioButton landscapeRadioButton;

    private javax.swing.JPanel leftMarginPanel;

    private javax.swing.JTextField leftMarginTextField;

    private javax.swing.JPanel mainPanel;

    private javax.swing.JPanel marginsPanel;

    private javax.swing.JButton okButton;

    private javax.swing.JPanel okCancelPanel;

    private javax.swing.JPanel orientationPanel;

    private javax.swing.JPanel paperSizePanel;

    private javax.swing.JRadioButton portraitRadioButton;

    private javax.swing.JPanel rightMarginPanel;

    private javax.swing.JTextField rightMarginTextField;

    private javax.swing.JPanel topMarginPanel;

    private javax.swing.JTextField topMarginTextField;

    private javax.swing.JPanel widthPanel;

    private javax.swing.JTextField widthTextField;

    public void show() {
        java.awt.Dimension dialogSize = this.getPreferredSize();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width / 2 - dialogSize.width / 2, screenSize.height / 2 - dialogSize.height / 2);
        super.show();
    }

    public PageOutputParameters getPageOutputParameters() {
        return pageOutputParameters;
    }

    boolean parseValues() {
        try {
            pageOutputParameters.setPaperMeasure("paper:width", Double.parseDouble(widthTextField.getText()), PageOutputParameters.CM_UNIT);
            pageOutputParameters.setPaperMeasure("paper:height", Double.parseDouble(heightTextField.getText()), PageOutputParameters.CM_UNIT);
            pageOutputParameters.setPaperMeasure("margin:left", Double.parseDouble(leftMarginTextField.getText()), PageOutputParameters.CM_UNIT);
            pageOutputParameters.setPaperMeasure("margin:right", Double.parseDouble(rightMarginTextField.getText()), PageOutputParameters.CM_UNIT);
            pageOutputParameters.setPaperMeasure("margin:top", Double.parseDouble(topMarginTextField.getText()), PageOutputParameters.CM_UNIT);
            pageOutputParameters.setPaperMeasure("margin:bottom", Double.parseDouble(bottomMarginTextField.getText()), PageOutputParameters.CM_UNIT);
            pageOutputParameters.landscape = landscapeRadioButton.isSelected();
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
