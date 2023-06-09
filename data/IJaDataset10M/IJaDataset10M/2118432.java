package pl.xperios.testingshortcuts.panels;

import javax.swing.JOptionPane;
import pl.xperios.testingshortcuts.commons.interfaces.ContextHelpable;

/**
 *
 * @author Praca
 */
public class Panel2 extends javax.swing.JPanel implements ContextHelpable {

    /**
   * Creates new form Panel2
   */
    public Panel2() {
        initComponents();
    }

    /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel1.setText("Panel2");
        jButton1.setMnemonic('a');
        jButton1.setText("Mój przycisk");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jButton1)).addContainerGap(188, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1).addContainerGap(159, Short.MAX_VALUE)));
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Panel2");
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    @Override
    public ContextHelpable.ContextHelp getContextHelp() {
        return ContextHelpable.Util.getContextHelpFromComponents(this);
    }
}
