package org.neuroph.easyneurons.dialog;

import org.jdesktop.application.Action;
import org.neuroph.easyneurons.EasyNeuronsApplicationView;

/**
 *
 * @author  Zoran Sevarac <sevarac@gmail.com>
 */
public class BamWizard extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;

    EasyNeuronsApplicationView application;

    /** Creates new form BamWizard */
    public BamWizard(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public BamWizard(java.awt.Frame parent, boolean modal, EasyNeuronsApplicationView application) {
        super(parent, modal);
        initComponents();
        this.application = application;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        fieldPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        inputsField = new javax.swing.JTextField();
        outputsField = new javax.swing.JTextField();
        buttonPanel = new javax.swing.JPanel();
        createButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        helpButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.neuroph.easyneurons.EasyNeuronsApplication.class).getContext().getResourceMap(BamWizard.class);
        setTitle(resourceMap.getString("Form.title"));
        setName("Form");
        getContentPane().setLayout(new java.awt.GridBagLayout());
        fieldPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        fieldPanel.setName("fieldPanel");
        fieldPanel.setLayout(new java.awt.GridBagLayout());
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        fieldPanel.add(jLabel1, gridBagConstraints);
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        fieldPanel.add(jLabel2, gridBagConstraints);
        inputsField.setColumns(8);
        inputsField.setText(resourceMap.getString("inputsField.text"));
        inputsField.setName("inputsField");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 10);
        fieldPanel.add(inputsField, gridBagConstraints);
        outputsField.setColumns(8);
        outputsField.setText(resourceMap.getString("outputsField.text"));
        outputsField.setName("outputsField");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 10);
        fieldPanel.add(outputsField, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(fieldPanel, gridBagConstraints);
        buttonPanel.setName("buttonPanel");
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.neuroph.easyneurons.EasyNeuronsApplication.class).getContext().getActionMap(BamWizard.class, this);
        createButton.setAction(actionMap.get("create"));
        createButton.setName("createButton");
        buttonPanel.add(createButton);
        cancelButton.setAction(actionMap.get("cancel"));
        cancelButton.setName("cancelButton");
        buttonPanel.add(cancelButton);
        helpButton.setText(resourceMap.getString("helpButton.text"));
        helpButton.setEnabled(false);
        helpButton.setName("helpButton");
        buttonPanel.add(helpButton);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(buttonPanel, gridBagConstraints);
        pack();
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                BamWizard dialog = new BamWizard(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    @Action
    public void create() {
        int inputNeuronsCount = Integer.parseInt(this.inputsField.getText().trim());
        int outputNeuronsCount = Integer.parseInt(this.outputsField.getText().trim());
        application.newBamNetwork(inputNeuronsCount, outputNeuronsCount);
        this.setVisible(false);
    }

    @Action
    public void cancel() {
        this.dispose();
    }

    private javax.swing.JPanel buttonPanel;

    private javax.swing.JButton cancelButton;

    private javax.swing.JButton createButton;

    private javax.swing.JPanel fieldPanel;

    private javax.swing.JButton helpButton;

    private javax.swing.JTextField inputsField;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JTextField outputsField;
}
