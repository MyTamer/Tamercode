package org.exmaralda.partitureditor.sound;

/**
 *
 * @author  thomas
 */
public class AbstractSaveMediaDialog extends org.exmaralda.partitureditor.jexmaraldaswing.JEscapeDialog {

    /** Creates new form SaveSnapshotDialog */
    public AbstractSaveMediaDialog(java.awt.Frame parent, boolean modal, String fn, boolean enableLink) {
        super(parent, modal);
        initComponents();
        sendToLinkPanelCheckBox.setSelected(enableLink);
        sendToLinkPanelCheckBox.setEnabled(enableLink);
        getRootPane().setDefaultButton(okButton);
        extraInfoPanel.setVisible(false);
        pack();
    }

    public void setExtraInfo(String text) {
        extraInfoLabel.setText(text);
        extraInfoPanel.setVisible(text.length() > 0);
        pack();
    }

    String findFreeFilename(String fn, String suffix) {
        int index = fn.lastIndexOf('.');
        String nakedFilename = fn;
        if (index >= 0) {
            nakedFilename = fn.substring(0, fn.lastIndexOf('.') - 1);
        }
        while ((nakedFilename.length() > 1) && (Character.isDigit(nakedFilename.charAt(nakedFilename.length() - 1)))) {
            nakedFilename = nakedFilename.substring(0, nakedFilename.length() - 1);
        }
        int count = 1;
        boolean fileExists = true;
        String checkFilename = "";
        while (fileExists) {
            checkFilename = nakedFilename + Integer.toString(count) + "." + suffix;
            System.out.println("Now checking " + checkFilename);
            fileExists = new java.io.File(checkFilename).canRead();
            count++;
        }
        return checkFilename;
    }

    public String getFilename() {
        return filenameTextField.getText();
    }

    public boolean sendLink() {
        return sendToLinkPanelCheckBox.isEnabled();
    }

    private void initComponents() {
        mainPanel = new javax.swing.JPanel();
        filenamePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        filenameTextField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        sendToLinkPanelPanel = new javax.swing.JPanel();
        sendToLinkPanelCheckBox = new javax.swing.JCheckBox();
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        extraInfoPanel = new javax.swing.JPanel();
        extraInfoLabel = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Save Snapshot");
        mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.Y_AXIS));
        filenamePanel.setLayout(new javax.swing.BoxLayout(filenamePanel, javax.swing.BoxLayout.LINE_AXIS));
        jLabel1.setText("File name: ");
        filenamePanel.add(jLabel1);
        filenameTextField.setMaximumSize(new java.awt.Dimension(1000, 20));
        filenameTextField.setPreferredSize(new java.awt.Dimension(400, 20));
        filenamePanel.add(filenameTextField);
        browseButton.setText("Browse...");
        browseButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });
        filenamePanel.add(browseButton);
        mainPanel.add(filenamePanel);
        sendToLinkPanelPanel.setLayout(new javax.swing.BoxLayout(sendToLinkPanelPanel, javax.swing.BoxLayout.LINE_AXIS));
        sendToLinkPanelCheckBox.setSelected(true);
        sendToLinkPanelCheckBox.setText("Link to transcription");
        sendToLinkPanelPanel.add(sendToLinkPanelCheckBox);
        mainPanel.add(sendToLinkPanelPanel);
        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(okButton);
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(cancelButton);
        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
        extraInfoPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        extraInfoLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
        extraInfoLabel.setForeground(new java.awt.Color(0, 51, 153));
        extraInfoLabel.setText("Extra info");
        extraInfoPanel.add(extraInfoLabel);
        getContentPane().add(extraInfoPanel, java.awt.BorderLayout.NORTH);
        pack();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        change = false;
        setVisible(false);
        dispose();
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        change = true;
        setVisible(false);
        dispose();
    }

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        fc.setCurrentDirectory(new java.io.File(filenameTextField.getText()).getParentFile());
        int retVal = fc.showSaveDialog(this.getParent());
        if (retVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            filenameTextField.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    }

    private javax.swing.JButton browseButton;

    private javax.swing.JPanel buttonPanel;

    private javax.swing.JButton cancelButton;

    private javax.swing.JLabel extraInfoLabel;

    private javax.swing.JPanel extraInfoPanel;

    private javax.swing.JPanel filenamePanel;

    public javax.swing.JTextField filenameTextField;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel mainPanel;

    private javax.swing.JButton okButton;

    private javax.swing.JCheckBox sendToLinkPanelCheckBox;

    private javax.swing.JPanel sendToLinkPanelPanel;
}
