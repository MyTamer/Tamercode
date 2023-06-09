package org.personalsmartspace.pss_psm_pssmanagergui.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.onm.api.platform.PeerGroupInfo;
import org.personalsmartspace.onm.api.platform.PeerInfo;
import org.personalsmartspace.pss_psm_pssmanager.api.platform.IPssManager;
import org.personalsmartspace.pss_psm_pssmanager.api.pss3p.PssManagerException;
import org.personalsmartspace.pss_psm_pssmanagergui.impl.IServiceFinder;
import org.personalsmartspace.pss_sm_api.impl.ServiceMgmtException;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;

public class AddService extends javax.swing.JPanel {

    private IServiceFinder serviceFinder;

    private PSSLog logger = new PSSLog(this);

    /** Creates new form AddService */
    public AddService(IServiceFinder serviceFinder) {
        this.serviceFinder = serviceFinder;
        initComponents();
        customiseComponents();
    }

    private void customiseComponents() {
        this.peerSelectionCheckBox.setEnabled(true);
        this.peerSelectionCheckBox.setSelected(true);
        this.peerComboBox.setEnabled(false);
        this.peerSelectionCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (AddService.this.peerSelectionCheckBox.isSelected()) {
                    AddService.this.peerComboBox.setEnabled(false);
                } else {
                    AddService.this.peerComboBox.setEnabled(true);
                    String peers[] = AddService.this.getPSSPeers();
                    AddService.this.peerComboBox.setModel(new javax.swing.DefaultComboBoxModel(peers));
                    AddService.this.peerComboBox.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JComboBox cb = (JComboBox) e.getSource();
                            String selectedPeerName = (String) cb.getSelectedItem();
                        }
                    });
                }
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        txtServiceURI = new javax.swing.JTextField();
        btnAddService = new javax.swing.JButton();
        peerPanel = new javax.swing.JPanel();
        peerComboBox = new javax.swing.JComboBox();
        peerSelectionCheckBox = new javax.swing.JCheckBox();
        setName("Form");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(PssManagerApp.class).getContext().getResourceMap(AddService.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        txtServiceURI.setText(resourceMap.getString("txtServiceURI.text"));
        txtServiceURI.setName("txtServiceURI");
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(PssManagerApp.class).getContext().getActionMap(AddService.class, this);
        btnAddService.setAction(actionMap.get("addService"));
        btnAddService.setText(resourceMap.getString("btnAddService.text"));
        btnAddService.setName("btnAddService");
        peerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("peerPanel.border.title")));
        peerPanel.setName("peerPanel");
        peerComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Local" }));
        peerComboBox.setName("peerComboBox");
        javax.swing.GroupLayout peerPanelLayout = new javax.swing.GroupLayout(peerPanel);
        peerPanel.setLayout(peerPanelLayout);
        peerPanelLayout.setHorizontalGroup(peerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(peerPanelLayout.createSequentialGroup().addContainerGap().addComponent(peerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        peerPanelLayout.setVerticalGroup(peerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(peerPanelLayout.createSequentialGroup().addComponent(peerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        peerSelectionCheckBox.setText(resourceMap.getString("peerSelectionCheckBox.text"));
        peerSelectionCheckBox.setName("peerSelectionCheckBox");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtServiceURI, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE).addContainerGap()).addGroup(layout.createSequentialGroup().addComponent(peerSelectionCheckBox).addContainerGap(151, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(btnAddService).addComponent(peerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGap(22, 22, 22)))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtServiceURI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(peerSelectionCheckBox).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(peerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnAddService, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)));
    }

    @Action
    public void allowPeerSelection() {
        if (this.peerSelectionCheckBox.isSelected()) {
            this.peerComboBox.setEnabled(false);
        } else {
            this.peerComboBox.setEnabled(true);
        }
    }

    @Action
    public void addService() {
        try {
            IPssManager pssMgr = this.serviceFinder.getPssManager();
            IServiceIdentifier serviceId = null;
            if (null != pssMgr && null != pssMgr.getPeerIdentifier()) {
                if (this.peerSelectionCheckBox.isSelected()) {
                    serviceId = pssMgr.addService(txtServiceURI.getText());
                } else {
                    String selectedPeerId = this.getPeerId((String) this.peerComboBox.getSelectedItem());
                    JOptionPane.showMessageDialog(null, "Selected peer is  : " + selectedPeerId);
                    serviceId = pssMgr.addService(txtServiceURI.getText(), selectedPeerId);
                }
                JOptionPane.showMessageDialog(null, "Service Identifier for OSGi deployed service is  : " + serviceId + " Service now being registered");
            } else {
                JOptionPane.showMessageDialog(null, "A service cannot be added until the peer has started");
            }
        } catch (Exception e) {
            logger.error("Add service exception", e);
            JOptionPane.showMessageDialog(null, "Unable to start service - " + e.getMessage());
        }
    }

    /**
     * Get a list of the current peers
     * 
     * @return String array
     */
    private String[] getPSSPeers() {
        String peerList[] = null;
        ArrayList<PeerInfo> peers = null;
        try {
            IPssManager pssMgr = this.serviceFinder.getPssManager();
            try {
                PeerGroupInfo groupInfo = pssMgr.getPeerGroupInfo();
                peers = groupInfo.getPeerList();
                peerList = new String[peers.size() + 1];
                for (int i = 0; i < peers.size(); i++) {
                    peerList[i] = peers.get(i).getPeerName() + " (" + peers.get(i).getPeerID() + ")";
                }
            } catch (PssManagerException e) {
                logger.error("PssManager exception", e);
            }
        } catch (ServiceMgmtException e) {
            logger.error("PssManager exception", e);
        }
        return peerList;
    }

    /**
     * Extract the Peer Identifier
     * 
     * @param selectedItem
     * @return String
     */
    private String getPeerId(String selectedItem) {
        String peerId = null;
        int start = selectedItem.indexOf("(");
        int finish = selectedItem.indexOf(")");
        return selectedItem.substring(start + 1, finish);
    }

    private javax.swing.JComboBox peerComboBox;

    private javax.swing.JPanel peerPanel;

    private javax.swing.JCheckBox peerSelectionCheckBox;

    private javax.swing.JButton btnAddService;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JTextField txtServiceURI;
}
