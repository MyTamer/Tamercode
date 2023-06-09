package newgen.presentation.administration;

import newgen.presentation.*;

/**
 *
 * @author  Administrator
 */
public class CreatePatronCategory extends javax.swing.JInternalFrame {

    /** Creates new form CreatePatronCategory */
    public CreatePatronCategory() {
        initComponents();
        String cols[] = { "", "" };
        this.dtmPrivilege = new javax.swing.table.DefaultTableModel(cols, 0) {

            public boolean isCellEditable(int row, int column) {
                if (column == 2) return true; else return false;
            }
        };
        this.privilegeTable.setModel(this.dtmPrivilege);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanel1 = new javax.swing.JPanel();
        bok = new javax.swing.JButton();
        bhelp = new javax.swing.JButton();
        bcsh = new javax.swing.JButton();
        bcancel = new javax.swing.JButton();
        bexit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        unicodeTextField1 = new newgen.presentation.UnicodeTextField();
        jLabel2 = new javax.swing.JLabel();
        unicodeTextField2 = new newgen.presentation.UnicodeTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        privilegeTable = new javax.swing.JTable();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(NewGenMain.getAppletInstance().getMyResource().getBundle("Administration").getString("CreatePatronCategory"));
        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        bok.setMnemonic('o');
        bok.setText(NewGenMain.getAppletInstance().getMyResource().getBundle("Administration").getString("Ok"));
        jPanel1.add(bok);
        bhelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/help.gif")));
        bhelp.setMnemonic('h');
        jPanel1.add(bhelp);
        bcsh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/helpcsh.gif")));
        jPanel1.add(bcsh);
        bcancel.setMnemonic('c');
        bcancel.setText(NewGenMain.getAppletInstance().getMyResource().getBundle("Administration").getString("Cancel"));
        jPanel1.add(bcancel);
        bexit.setMnemonic('e');
        bexit.setText(NewGenMain.getAppletInstance().getMyResource().getBundle("Administration").getString("Exit"));
        jPanel1.add(bexit);
        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));
        jPanel3.setLayout(new java.awt.GridBagLayout());
        jLabel1.setText(NewGenMain.getAppletInstance().getMyResource().getBundle("Administration").getString("LibraryName"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel3.add(jLabel1, gridBagConstraints);
        unicodeTextField1.setEditable(false);
        jPanel3.add(unicodeTextField1, new java.awt.GridBagConstraints());
        jLabel2.setForeground(new java.awt.Color(255, 0, 51));
        jLabel2.setText(NewGenMain.getAppletInstance().getMyResource().getBundle("Administration").getString("PatronCategory"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel3.add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        jPanel3.add(unicodeTextField2, gridBagConstraints);
        jPanel2.add(jPanel3);
        jPanel4.setLayout(new java.awt.BorderLayout());
        jPanel4.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), NewGenMain.getAppletInstance().getMyResource().getBundle("Administration").getString("Privileges")));
        jPanel4.setPreferredSize(new java.awt.Dimension(10, 100));
        privilegeTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        jScrollPane1.setViewportView(privilegeTable);
        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jPanel2.add(jPanel4);
        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
        pack();
    }

    private javax.swing.JButton bcancel;

    private javax.swing.JButton bcsh;

    private javax.swing.JButton bexit;

    private javax.swing.JButton bhelp;

    private javax.swing.JButton bok;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTable privilegeTable;

    private newgen.presentation.UnicodeTextField unicodeTextField1;

    private newgen.presentation.UnicodeTextField unicodeTextField2;

    private javax.swing.table.DefaultTableModel dtmPrivilege;
}
