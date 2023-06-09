package org.jalgo.tests.ebnf.ebnfinput.ChoicePanel;

import javax.swing.JFrame;

public class ChoicePanel extends JFrame {

    /** Creates new form Find */
    public ChoicePanel() {
        initComponents();
    }

    private void initComponents() {
        ChoicePanel = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        transButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        buttonPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bitte Wählen Sie...", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12)));
        transButton.setFont(new java.awt.Font("Tahoma", 0, 14));
        transButton.setText("<html><center>trans()-Algorithmus<br>anwenden</center></html>");
        editButton.setFont(new java.awt.Font("Tahoma", 0, 14));
        editButton.setText("<html><center>Definition<br>überarbeiten</center></html>");
        org.jdesktop.layout.GroupLayout buttonPanelLayout = new org.jdesktop.layout.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(buttonPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, buttonPanelLayout.createSequentialGroup().addContainerGap(416, Short.MAX_VALUE).add(transButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(editButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 151, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        buttonPanelLayout.setVerticalGroup(buttonPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(buttonPanelLayout.createSequentialGroup().add(buttonPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(editButton).add(transButton)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        org.jdesktop.layout.GroupLayout ChoicePanelLayout = new org.jdesktop.layout.GroupLayout(ChoicePanel);
        ChoicePanel.setLayout(ChoicePanelLayout);
        ChoicePanelLayout.setHorizontalGroup(ChoicePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(ChoicePanelLayout.createSequentialGroup().addContainerGap().add(buttonPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        ChoicePanelLayout.setVerticalGroup(ChoicePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(ChoicePanelLayout.createSequentialGroup().addContainerGap().add(buttonPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(ChoicePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(ChoicePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ChoicePanel().setVisible(true);
            }
        });
    }

    private javax.swing.JPanel ChoicePanel;

    private javax.swing.JPanel buttonPanel;

    private javax.swing.JButton editButton;

    private javax.swing.JButton transButton;
}
