package org.omegat.gui.filters2;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import org.omegat.filters2.AbstractFilter;
import org.omegat.filters2.master.FilterMaster;
import org.omegat.util.OStrings;
import org.openide.awt.Mnemonics;

/**
 * Editor for a single instance of the filter.
 * E.g. HTML filter may have two instances -- one for .html and 
 * other for .htm files.
 *
 * @author  Maxym Mykhalchuk
 */
public class InstanceEditor extends JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;

    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    private void init2(boolean sourceEncodingVariable, boolean targetEncodingVariable, String hint) {
        getRootPane().setDefaultButton(addOrUpdateButton);
        this.sourceEncodingField.setEnabled(sourceEncodingVariable);
        this.targetEncodingField.setEnabled(targetEncodingVariable);
        ((TitledBorder) tfnpPanel.getBorder()).setTitle(OStrings.getString("INSTANCEEDITOR_Target_Filename_Pattern"));
        sourceFilenameMaskField.setText("*.*");
        targetFilenamePatternField.setText("${filename}");
        if (hint != null && hint.length() != 0) hintTextArea.setText(hint); else hintTextArea.setVisible(false);
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);
        pack();
    }

    /** 
     * Creates an InstanceEditor form,
     * that is used to add a new filter instance.
     */
    public InstanceEditor(Dialog parent, boolean sourceEncodingVariable, boolean targetEncodingVariable, String hint) {
        super(parent, true);
        initComponents();
        init2(sourceEncodingVariable, targetEncodingVariable, hint);
        setTitle(OStrings.getString("INSTANCEEDITOR_TITLE_ADD"));
        Mnemonics.setLocalizedText(addOrUpdateButton, OStrings.getString("BUTTON_OK"));
    }

    /** 
     * Creates an InstanceEditor form,
     * that is used to edit an existing filter instance.
     */
    public InstanceEditor(Dialog parent, boolean sourceEncodingVariable, boolean targetEncodingVariable, String hint, String sourceFilenameMask, String sourceEncoding, String targetEncoding, String targetFilenamePattern) {
        super(parent, true);
        initComponents();
        init2(sourceEncodingVariable, targetEncodingVariable, hint);
        setTitle(OStrings.getString("INSTANCEEDITOR_TITLE_UPDATE"));
        Mnemonics.setLocalizedText(addOrUpdateButton, OStrings.getString("BUTTON_OK"));
        sourceFilenameMaskField.setText(sourceFilenameMask);
        sourceEncodingField.setSelectedItem(sourceEncoding);
        targetEncodingField.setSelectedItem(targetEncoding);
        targetFilenamePatternField.setText(targetFilenamePattern);
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    private String sourceFilenameMask;

    public String getSourceFilenameMask() {
        return sourceFilenameMask;
    }

    private String sourceEncoding;

    public String getSourceEncoding() {
        return sourceEncoding;
    }

    private String targetEncoding;

    public String getTargetEncoding() {
        return targetEncoding;
    }

    private String targetFilenamePattern;

    public String getTargetFilenamePattern() {
        return targetFilenamePattern;
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        buttonPanel = new javax.swing.JPanel();
        addOrUpdateButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        hintTextArea = new javax.swing.JTextArea();
        tfnpPanel = new javax.swing.JPanel();
        insertButton = new javax.swing.JButton();
        substitute = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        targetFilenamePatternField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        sourceFilenameMaskField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        sourceEncodingField = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        targetEncodingField = new javax.swing.JComboBox();
        getContentPane().setLayout(new java.awt.GridBagLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(OStrings.getString("INSTANCEEDITOR_TITLE_ADD"));
        setResizable(false);
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        org.openide.awt.Mnemonics.setLocalizedText(addOrUpdateButton, OStrings.getString("BUTTON_ADD"));
        addOrUpdateButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addOrUpdateButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(addOrUpdateButton);
        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, OStrings.getString("BUTTON_CANCEL"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(cancelButton);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(buttonPanel, gridBagConstraints);
        hintTextArea.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        hintTextArea.setEditable(false);
        hintTextArea.setFont(new JLabel().getFont());
        hintTextArea.setLineWrap(true);
        hintTextArea.setWrapStyleWord(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(hintTextArea, gridBagConstraints);
        tfnpPanel.setLayout(new java.awt.GridBagLayout());
        tfnpPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Target Filename Pattern"));
        org.openide.awt.Mnemonics.setLocalizedText(insertButton, OStrings.getString("BUTTON_INSERT"));
        insertButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        tfnpPanel.add(insertButton, gridBagConstraints);
        substitute.setModel(new DefaultComboBoxModel(AbstractFilter.TARGET_FILENAME_PATTERNS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        tfnpPanel.add(substitute, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, OStrings.getString("INSTANCEEDITOR_Substituted_Variable"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        tfnpPanel.add(jLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        tfnpPanel.add(targetFilenamePatternField, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(tfnpPanel, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, OStrings.getString("INSTANCEEDITOR_SOURCE_MASK"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(sourceFilenameMaskField, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, OStrings.getString("INSTANCEEDITOR_SOURCE_ENCODING"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel3, gridBagConstraints);
        sourceEncodingField.setModel(new DefaultComboBoxModel(new Vector<String>(FilterMaster.getSupportedEncodings())));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(sourceEncodingField, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, OStrings.getString("INSTANCEEDITOR_Target_Encoding"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel6, gridBagConstraints);
        targetEncodingField.setModel(new DefaultComboBoxModel(new Vector<String>(FilterMaster.getSupportedEncodings())));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(targetEncodingField, gridBagConstraints);
        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2);
    }

    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int caret = targetFilenamePatternField.getCaretPosition();
        String oldtext = targetFilenamePatternField.getText();
        String newtext = oldtext.substring(0, caret) + substitute.getSelectedItem().toString() + oldtext.substring(caret);
        targetFilenamePatternField.setText(newtext);
        targetFilenamePatternField.setCaretPosition(caret + substitute.getSelectedItem().toString().length());
        targetFilenamePatternField.requestFocus();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        doClose(RET_CANCEL);
    }

    private void addOrUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {
        sourceFilenameMask = sourceFilenameMaskField.getText();
        sourceEncoding = sourceEncodingField.getSelectedItem().toString();
        targetEncoding = targetEncodingField.getSelectedItem().toString();
        targetFilenamePattern = targetFilenamePatternField.getText();
        doClose(RET_OK);
    }

    private int returnStatus = RET_CANCEL;

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    private javax.swing.JButton addOrUpdateButton;

    private javax.swing.JPanel buttonPanel;

    private javax.swing.JButton cancelButton;

    private javax.swing.JTextArea hintTextArea;

    private javax.swing.JButton insertButton;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JComboBox sourceEncodingField;

    private javax.swing.JTextField sourceFilenameMaskField;

    private javax.swing.JComboBox substitute;

    private javax.swing.JComboBox targetEncodingField;

    private javax.swing.JTextField targetFilenamePatternField;

    private javax.swing.JPanel tfnpPanel;
}
