package de.realriu.snipzel.gui;

import de.realriu.snipzel.Settings;
import de.realriu.snipzel.control.Controller;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author riu
 */
public class SettingDialog extends javax.swing.JDialog {

    private Controller control;

    /** Creates new form SettingDialog */
    public SettingDialog(java.awt.Frame parent, boolean modal, Controller c) {
        super(parent, modal);
        control = c;
        initComponents();
        try {
            tx_version.setText(control.getMyVersion() + "");
            String s = Settings.getSetting("check-intervall");
            long l = Long.parseLong(s) / 1000;
            sp_update.setValue(l);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        cb_storage = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        sp_update = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        tx_version = new javax.swing.JTextField();
        btn_update = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        cb_storage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FileStorage" }));
        jLabel1.setText("Storage Type:");
        jLabel2.setText("Update Intervall:");
        sp_update.setValue(15);
        sp_update.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sp_updateStateChanged(evt);
            }
        });
        sp_update.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                sp_updateKeyTyped(evt);
            }
        });
        jLabel3.setText("seconds");
        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jLabel4.setText("Version:");
        tx_version.setEditable(false);
        tx_version.setText("0");
        btn_update.setText("update");
        btn_update.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cb_storage, 0, 177, Short.MAX_VALUE)).add(layout.createSequentialGroup().add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(sp_update, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel3)).add(layout.createSequentialGroup().add(jLabel4).add(18, 18, 18).add(tx_version, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btn_update))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cb_storage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel1)).add(18, 18, 18).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jLabel2).add(sp_update, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel3)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 117, Short.MAX_VALUE).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4).add(tx_version, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(btn_update)).add(18, 18, 18).add(jButton1).addContainerGap()));
        pack();
    }

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            control.updateMyData();
            tx_version.setText("" + control.getMyVersion());
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private void sp_updateStateChanged(javax.swing.event.ChangeEvent evt) {
        Settings.setSetting("check-intervall", (Long.parseLong(sp_update.getValue().toString()) * 1000) + "");
    }

    private void sp_updateKeyTyped(java.awt.event.KeyEvent evt) {
        try {
            long u = Long.parseLong(sp_update.getValue().toString());
            sp_updateStateChanged(null);
        } catch (Exception e) {
            sp_update.setValue(15);
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private javax.swing.JButton btn_update;

    private javax.swing.JComboBox cb_storage;

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JSpinner sp_update;

    private javax.swing.JTextField tx_version;
}
