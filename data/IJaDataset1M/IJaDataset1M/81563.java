package net.sf.fmj.ui.dialogs;

import java.awt.*;
import net.sf.fmj.ui.objeditor.*;
import net.sf.fmj.ui.utils.*;

/**
 * 
 * @author Ken Larson
 */
public class RTPReceivePanel extends javax.swing.JPanel implements ObjEditor {

    public static String run(Frame parent) {
        final RTPReceivePanel p = new RTPReceivePanel();
        return (String) ObjEditorOKCancelDialog.run(parent, p, "", "Open RTP Session");
    }

    private String url;

    private javax.swing.JComboBox comboTTL;

    private javax.swing.JLabel labelAddress;

    private javax.swing.JLabel labelPort;

    private javax.swing.JLabel labelTTL;

    private javax.swing.JTextField textAddress;

    private javax.swing.JTextField textPort;

    /** Creates new form RTPReceivePanel */
    public RTPReceivePanel() {
        initComponents();
    }

    public Component getComponent() {
        return this;
    }

    public Object getObject() {
        return url;
    }

    public String getUrl() {
        return url;
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        labelAddress = new javax.swing.JLabel();
        textAddress = new javax.swing.JTextField();
        labelPort = new javax.swing.JLabel();
        textPort = new javax.swing.JTextField();
        labelTTL = new javax.swing.JLabel();
        comboTTL = new javax.swing.JComboBox();
        setLayout(new java.awt.GridBagLayout());
        labelAddress.setText("Address:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(labelAddress, gridBagConstraints);
        textAddress.setPreferredSize(new java.awt.Dimension(80, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(textAddress, gridBagConstraints);
        labelPort.setText("Port:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(labelPort, gridBagConstraints);
        textPort.setPreferredSize(new java.awt.Dimension(40, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(textPort, gridBagConstraints);
        labelTTL.setText("TTL:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(labelTTL, gridBagConstraints);
        comboTTL.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "8", "16", "32", "64", "128", "255" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(comboTTL, gridBagConstraints);
    }

    public void setObjectAndUpdateControl(Object o) {
        this.url = (String) o;
    }

    public boolean validateAndUpdateObj() {
        url = null;
        ComponentValidator v = new ComponentValidator();
        try {
            v.validateNotEmpty(textAddress, labelAddress);
            v.validateNotEmpty(textPort, labelPort);
            v.validateInteger(textPort, labelPort);
            v.validateNotEmpty(comboTTL, labelTTL);
            v.validateInteger(comboTTL, labelTTL);
        } catch (ComponentValidationException e) {
            ErrorDialog.showError(this, e.getMessage());
            return false;
        }
        final String sessionAddress = textAddress.getText();
        final int port = Integer.parseInt(textPort.getText());
        final int ttl = Integer.parseInt((String) comboTTL.getSelectedItem());
        url = "rtp://" + sessionAddress + ":" + port;
        return true;
    }
}
