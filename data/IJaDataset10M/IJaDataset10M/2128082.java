package cconverter;

import java.awt.Cursor;
import java.awt.Desktop;
import java.net.URI;
import org.jdesktop.application.Action;

/**
 *
 * @author SapunBoj
 */
public class FrmAbout extends javax.swing.JFrame {

    /** Creates new form FrmAbout */
    public FrmAbout() {
        initComponents();
        myInit();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        lblTitle = new javax.swing.JLabel();
        lblVersion = new javax.swing.JLabel();
        lblAuthor = new javax.swing.JLabel();
        lblMain = new javax.swing.JLabel();
        lblLinkPre = new javax.swing.JLabel();
        lblLink = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cconverter.CConverterApp.class).getContext().getResourceMap(FrmAbout.class);
        setTitle(resourceMap.getString("Form.title"));
        setLocationByPlatform(true);
        setName("Form");
        setResizable(false);
        lblTitle.setFont(resourceMap.getFont("lblTitle.font"));
        lblTitle.setIcon(resourceMap.getIcon("lblTitle.icon"));
        lblTitle.setText(resourceMap.getString("lblTitle.text"));
        lblTitle.setName("lblTitle");
        lblVersion.setText(resourceMap.getString("lblVersion.text"));
        lblVersion.setName("lblVersion");
        lblAuthor.setText(resourceMap.getString("lblAuthor.text"));
        lblAuthor.setName("lblAuthor");
        lblMain.setText(resourceMap.getString("lblMain.text"));
        lblMain.setName("lblMain");
        lblLinkPre.setText(resourceMap.getString("lblLinkPre.text"));
        lblLinkPre.setName("lblLinkPre");
        lblLink.setForeground(resourceMap.getColor("lblLink.foreground"));
        lblLink.setText(resourceMap.getString("lblLink.text"));
        lblLink.setName("lblLink");
        lblLink.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLinkMouseClicked(evt);
            }
        });
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(cconverter.CConverterApp.class).getContext().getActionMap(FrmAbout.class, this);
        btnClose.setAction(actionMap.get("_exit"));
        btnClose.setText(resourceMap.getString("btnClose.text"));
        btnClose.setName("btnClose");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblTitle).addComponent(lblVersion).addComponent(lblAuthor).addComponent(lblMain)).addContainerGap(97, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(207, Short.MAX_VALUE).addComponent(btnClose).addContainerGap()).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(lblLinkPre).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblLink).addContainerGap(103, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(lblTitle).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblVersion).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblAuthor).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblMain).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblLinkPre).addComponent(lblLink)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(btnClose).addContainerGap()));
        pack();
    }

    private void lblLinkMouseClicked(java.awt.event.MouseEvent evt) {
        try {
            final URI _uri = new URI("http://ze-soft.blogspot.com/2009/08/currency-converter-cconverter-v13.html");
            Desktop _desktop = Desktop.getDesktop();
            _desktop.browse(_uri);
        } catch (Exception ex) {
            new FrmInfo(this, true).setInfo("Error", "URL Error : " + ex.getLocalizedMessage());
        }
    }

    @Action
    public void _exit() {
        this.dispose();
    }

    private void myInit() {
        this.setIconImage(Sets.icon);
        lblLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setLocationRelativeTo(null);
    }

    private javax.swing.JButton btnClose;

    private javax.swing.JLabel lblAuthor;

    private javax.swing.JLabel lblLink;

    private javax.swing.JLabel lblLinkPre;

    private javax.swing.JLabel lblMain;

    private javax.swing.JLabel lblTitle;

    private javax.swing.JLabel lblVersion;
}
