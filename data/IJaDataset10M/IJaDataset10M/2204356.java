package com.openbravo.pos.printer.config;

import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.base.DrivceLocal;
import com.openbravo.pos.config.ParametersConfig;
import com.openbravo.pos.util.StringParser;
import java.awt.Component;

/**
 *
 * @author adrian
 */
public class ParametersPrinter extends javax.swing.JPanel implements ParametersConfig {

    private String othersizename = "standard";

    /** Creates new form ParametersPrinter */
    public ParametersPrinter(String[] printernames) {
        initComponents();
        jPrinters.addItem("(Default)");
        jPrinters.addItem("(Show dialog)");
        for (String name : printernames) {
            jPrinters.addItem(name);
        }
    }

    public Component getComponent() {
        return this;
    }

    public void addDirtyManager(DirtyManager dirty) {
        jPrinters.addActionListener(dirty);
        jReceiptPrinter.addActionListener(dirty);
    }

    public void setParameters(StringParser p) {
        jPrinters.setSelectedItem(p.nextToken(','));
        String sizename = p.nextToken(',');
        jReceiptPrinter.setSelected("receipt".equals(sizename));
        othersizename = "receipt".equals(sizename) ? "standard" : sizename;
    }

    public String getParameters() {
        return comboValue(jPrinters.getSelectedItem()) + "," + boolValue(jReceiptPrinter.isSelected());
    }

    private static String comboValue(Object value) {
        return value == null ? "" : value.toString();
    }

    private String boolValue(boolean value) {
        return value ? "receipt" : othersizename;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPrinters = new javax.swing.JComboBox();
        jReceiptPrinter = new javax.swing.JCheckBox();
        jReceiptPrinter.setSelected(true);
        jReceiptPrinter.setText(DrivceLocal.getIntString("label.receiptprinter"));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 430, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jReceiptPrinter).addContainerGap(129, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 61, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jReceiptPrinter)).addContainerGap(37, Short.MAX_VALUE)));
    }

    private javax.swing.JComboBox jPrinters;

    private javax.swing.JCheckBox jReceiptPrinter;
}