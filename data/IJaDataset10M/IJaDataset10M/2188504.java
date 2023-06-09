package edacc.parametergrapheditor;

import edacc.parameterspace.domain.Domain;
import edacc.parameterspace.domain.IntegerDomain;

/**
 *
 * @author simon
 */
public class SelectIntegerDomainPanel extends javax.swing.JPanel implements IDomainPanel {

    /** Creates new form SelectIntegerDomainPanel */
    public SelectIntegerDomainPanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        txtLow = new javax.swing.JTextField();
        txtHigh = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        setName("Form");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edacc.EDACCApp.class).getContext().getResourceMap(SelectIntegerDomainPanel.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        txtLow.setText(resourceMap.getString("txtLow.text"));
        txtLow.setName("txtLow");
        txtHigh.setText(resourceMap.getString("txtHigh.text"));
        txtHigh.setName("txtHigh");
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jLabel2)).addGap(8, 8, 8).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtHigh, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE).addComponent(txtLow, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 68, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(txtLow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(txtHigh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JTextField txtHigh;

    private javax.swing.JTextField txtLow;

    @Override
    public Domain getDomain() throws InvalidDomainException {
        try {
            int lo = Integer.parseInt(txtLow.getText());
            int hi = Integer.parseInt(txtHigh.getText());
            return new IntegerDomain(lo, hi);
        } catch (NumberFormatException e) {
            throw new InvalidDomainException("You have to specify integers for lower and upper limit for integer domain.");
        }
    }

    @Override
    public void setDomain(Domain domain) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
