package newgen.presentation.cataloguing;

/**
 *
 * @author  administrator
 */
public class UploadCatalogue extends javax.swing.JInternalFrame implements newgen.presentation.component.NewGenLibScreen {

    private static UploadCatalogue instance = null;

    private static UploadCataloguePanel uploadCataloguePanel = null;

    public static UploadCatalogue getInstance() {
        if (instance == null) {
            instance = new UploadCatalogue();
        }
        instance.reloadLocales();
        return instance;
    }

    /** Creates new form UploadCatalogue */
    public UploadCatalogue() {
        initComponents();
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        uploadCataloguePanel = new UploadCataloguePanel();
        this.jPanel1.add(uploadCataloguePanel);
        this.setSize(630, 300);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("UploadCatalogueRecordsToUnionCatalogue"));
        jPanel1.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 15));
        jPanel2.setBorder(new javax.swing.border.EtchedBorder());
        jButton1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);
        jButton2.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        jPanel2.add(jButton2);
        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        uploadCataloguePanel.getDetails();
    }

    public void reloadLocales() {
        uploadCataloguePanel.reloadLocales();
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("UploadCatalogueRecordsToUnionCatalogue"));
        jButton1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        jButton2.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private newgen.presentation.cataloguing.UploadCataloguePanel upCatPanel;
}
