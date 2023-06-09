package csa.jmom.grafics;

import csa.gui.ModalInternalFrame;
import csa.jportal.gui.*;
import java.util.*;

/**
 *
 * @author Malban
 */
public class MustSaveAsOneDialog extends javax.swing.JPanel {

    public boolean ok = false;

    /** Creates new form GetStringDialog */
    public MustSaveAsOneDialog() {
        initComponents();
    }

    ModalInternalFrame mDialog = null;

    void setDialog(ModalInternalFrame dialog) {
        mDialog = dialog;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldFilename = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel1.setText("<html> \nSome of the images do not have a source file yet.\nYou must save the images to a <b>new </b> file.<br><p></p>  \nEnter a filename and press Save.  <br><p></p>  \n\n(File will be saved as a PNG-Image, the extension \".png\" will be added automatically)\n\n</html> ");
        jLabel1.setName("jLabel1");
        jButton1.setText("Save");
        jButton1.setName("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jLabel2.setText("Filename");
        jLabel2.setName("jLabel2");
        jTextFieldFilename.setName("jTextFieldFilename");
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel3.setText("Save as new Image!");
        jLabel3.setName("jLabel3");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextFieldFilename, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jButton1)).addComponent(jLabel3)).addGap(35, 35, 35)).addGroup(layout.createSequentialGroup().addComponent(jLabel1, 0, 0, Short.MAX_VALUE).addContainerGap()))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jLabel3).addGap(18, 18, 18).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jTextFieldFilename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton1)).addGap(52, 52, 52)));
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        ok = true;
        if (mDialog != null) {
            mDialog.setVisible(false);
        }
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JTextField jTextFieldFilename;

    public String getFilename() {
        return jTextFieldFilename.getText();
    }
}
