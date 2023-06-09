package fr.tango.tangopanels.devicepanels;

import java.awt.Dimension;
import javax.swing.JFrame;
import fr.esrf.tangoatk.core.*;
import fr.esrf.tangoatk.widget.util.JSmoothLabel;

/**
 *
 * @author  poncet
 */
public class Id14eh3CryoPanel extends javax.swing.JPanel {

    private Device cryoDev = null;

    private INumberScalar temp = null, tempEvap = null, n2Level = null, gasHeater = null;

    private INumberScalar icingStatus = null, dryerStatus = null, superDryerSatus = null;

    private AttributeList nsAttList, statusAttList, globalAttList;

    private int maxStatusLabelHeight, currH = 0;

    /** Creates new form Id14eh3CryoPanel */
    public Id14eh3CryoPanel(String cryoDevName) throws ConnectionException, IllegalArgumentException {
        IEntity att;
        nsAttList = new AttributeList();
        statusAttList = new AttributeList();
        globalAttList = new AttributeList();
        try {
            cryoDev = DeviceFactory.getInstance().getDevice(cryoDevName);
            if (cryoDev == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "Id14 Cryo Spy Panel : Cannot connect to the device.\n" + "Check the device name;" + " Id14 Cryo Spy Panel will abort ...\n", "Connection to device failed", javax.swing.JOptionPane.ERROR_MESSAGE);
                throw new IllegalArgumentException("Cannot connect to the id14 Cryo device.");
            }
        } catch (ConnectionException ce) {
            javax.swing.JOptionPane.showMessageDialog(null, "Id14 Cryo Spy Panel : Cannot connect to the device.\n" + "Check the device name;" + " Id14 Cryo Spy Panel will abort ...\n" + "Connection Exception : " + ce, "Connection to device failed", javax.swing.JOptionPane.ERROR_MESSAGE);
            throw ce;
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Id14 Cryo Spy Panel : Cannot connect to the device.\n" + "Check the device name;" + " Id14 Cryo Spy Panel will abort ...\n" + "Connection Exception : " + ex.getMessage(), "Connection to device failed", javax.swing.JOptionPane.ERROR_MESSAGE);
            throw new ConnectionException(ex);
        }
        try {
            att = nsAttList.add(cryoDevName + "/Temperature");
            temp = (INumberScalar) att;
            att = globalAttList.add(cryoDevName + "/Temperature");
            att = nsAttList.add(cryoDevName + "/TemperatureEvaporator");
            tempEvap = (INumberScalar) att;
            att = globalAttList.add(cryoDevName + "/TemperatureEvaporator");
            att = nsAttList.add(cryoDevName + "/N2Level");
            n2Level = (INumberScalar) att;
            att = globalAttList.add(cryoDevName + "/N2Level");
            att = nsAttList.add(cryoDevName + "/GasHeater");
            gasHeater = (INumberScalar) att;
            att = globalAttList.add(cryoDevName + "/GasHeater");
            att = statusAttList.add(cryoDevName + "/IcingStatus");
            icingStatus = (INumberScalar) att;
            att = globalAttList.add(cryoDevName + "/IcingStatus");
            att = statusAttList.add(cryoDevName + "/DryerStatus");
            dryerStatus = (INumberScalar) att;
            att = globalAttList.add(cryoDevName + "/DryerStatus");
            att = statusAttList.add(cryoDevName + "/SuperDryerStatus");
            superDryerSatus = (INumberScalar) att;
            att = globalAttList.add(cryoDevName + "/SuperDryerStatus");
        } catch (Exception ex) {
            String missingAtt;
            missingAtt = getMissingAttName();
            javax.swing.JOptionPane.showMessageDialog(null, "Id14 Cryo Spy Panel : Cannot find " + missingAtt + " attribute.\n" + "Check if the device is a tango cryo warpper;" + " Id14 Cryo Spy Panel will abort ...\n\n", ex.getMessage(), javax.swing.JOptionPane.ERROR_MESSAGE);
            throw new ConnectionException(ex);
        }
        initComponents();
        globalAttList.startRefresher();
    }

    private String getMissingAttName() {
        String str;
        if (cryoDev == null) return (" ");
        str = new String(cryoDev.getName());
        if (temp == null) return (str + "/Temperature");
        if (tempEvap == null) return (str + "/TemperatureEvaporator");
        if (n2Level == null) return (str + "/N2Level");
        if (gasHeater == null) return (str + "/GasHeater");
        if (icingStatus == null) return (str + "/IcingStatus");
        if (dryerStatus == null) return (str + "/DryerStatus");
        if (superDryerSatus == null) return (str + "/SuperDryerStatus");
        return (" ");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        attListViewer = new fr.esrf.tangoatk.widget.attribute.NumberScalarListViewer();
        cryoStatusViewer = new fr.esrf.tangoatk.widget.device.StatusViewer();
        statePanel = new javax.swing.JPanel();
        icingNsStateViewer = new fr.esrf.tangoatk.widget.attribute.NumberScalarStateViewer();
        icingLabelViewer = new fr.esrf.tangoatk.widget.properties.LabelViewer();
        dryerNsStateViewer = new fr.esrf.tangoatk.widget.attribute.NumberScalarStateViewer();
        dryerLabelViewer = new fr.esrf.tangoatk.widget.properties.LabelViewer();
        superDryerNsStateViewer = new fr.esrf.tangoatk.widget.attribute.NumberScalarStateViewer();
        superDryerLabelViewer = new fr.esrf.tangoatk.widget.properties.LabelViewer();
        setLayout(new java.awt.GridBagLayout());
        attListViewer.setTheFont(new java.awt.Font("Lucida Bright", 1, 14));
        attListViewer.setModel(nsAttList);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(attListViewer, gridBagConstraints);
        cryoStatusViewer.setPreferredSize(new java.awt.Dimension(250, 200));
        cryoStatusViewer.setModel(cryoDev);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 10);
        add(cryoStatusViewer, gridBagConstraints);
        statePanel.setLayout(new java.awt.GridBagLayout());
        icingNsStateViewer.setModel(icingStatus);
        icingStatus.refresh();
        currH = icingNsStateViewer.getPreferredSize().height;
        if (currH > maxStatusLabelHeight) maxStatusLabelHeight = currH;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        statePanel.add(icingNsStateViewer, gridBagConstraints);
        icingLabelViewer.setBackground(getBackground());
        icingLabelViewer.setHorizontalAlignment(JSmoothLabel.LEFT_ALIGNMENT);
        icingLabelViewer.setModel(icingStatus);
        icingStatus.refresh();
        currH = icingLabelViewer.getPreferredSize().height;
        if (currH > maxStatusLabelHeight) maxStatusLabelHeight = currH;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        statePanel.add(icingLabelViewer, gridBagConstraints);
        dryerNsStateViewer.setModel(dryerStatus);
        dryerStatus.refresh();
        currH = dryerNsStateViewer.getPreferredSize().height;
        if (currH > maxStatusLabelHeight) maxStatusLabelHeight = currH;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        statePanel.add(dryerNsStateViewer, gridBagConstraints);
        dryerLabelViewer.setBackground(getBackground());
        dryerLabelViewer.setHorizontalAlignment(JSmoothLabel.LEFT_ALIGNMENT);
        dryerLabelViewer.setModel(dryerStatus);
        dryerStatus.refresh();
        currH = dryerLabelViewer.getPreferredSize().height;
        if (currH > maxStatusLabelHeight) maxStatusLabelHeight = currH;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        statePanel.add(dryerLabelViewer, gridBagConstraints);
        superDryerNsStateViewer.setModel(superDryerSatus);
        superDryerSatus.refresh();
        currH = superDryerNsStateViewer.getPreferredSize().height;
        if (currH > maxStatusLabelHeight) maxStatusLabelHeight = currH;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        statePanel.add(superDryerNsStateViewer, gridBagConstraints);
        superDryerLabelViewer.setBackground(getBackground());
        superDryerLabelViewer.setHorizontalAlignment(JSmoothLabel.LEFT_ALIGNMENT);
        superDryerLabelViewer.setModel(superDryerSatus);
        superDryerSatus.refresh();
        currH = superDryerLabelViewer.getPreferredSize().height;
        if (currH > maxStatusLabelHeight) maxStatusLabelHeight = currH;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        statePanel.add(superDryerLabelViewer, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        add(statePanel, gridBagConstraints);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        String devname = "id14/cryospy/eh3";
        Id14eh3CryoPanel cryoPanel = null;
        JFrame jf = null;
        try {
            cryoPanel = new Id14eh3CryoPanel(devname);
            jf = new JFrame();
            jf.setTitle(devname);
            jf.getContentPane().add(cryoPanel);
            jf.pack();
            jf.show();
        } catch (Exception e) {
            System.out.println("Caught exception from the constructor of Id14eh3CryoPanel.\n" + e);
            System.exit(-1);
        }
    }

    private fr.esrf.tangoatk.widget.properties.LabelViewer icingLabelViewer;

    private javax.swing.JPanel statePanel;

    private fr.esrf.tangoatk.widget.properties.LabelViewer dryerLabelViewer;

    private fr.esrf.tangoatk.widget.attribute.NumberScalarStateViewer superDryerNsStateViewer;

    private fr.esrf.tangoatk.widget.attribute.NumberScalarStateViewer icingNsStateViewer;

    private fr.esrf.tangoatk.widget.attribute.NumberScalarListViewer attListViewer;

    private fr.esrf.tangoatk.widget.properties.LabelViewer superDryerLabelViewer;

    private fr.esrf.tangoatk.widget.device.StatusViewer cryoStatusViewer;

    private fr.esrf.tangoatk.widget.attribute.NumberScalarStateViewer dryerNsStateViewer;
}
