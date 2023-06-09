package test;

import sequor.component.*;

/**
 *
 * @author  elisha
 */
public class TestSettingsPanel extends javax.swing.JFrame {

    /** Creates new form TestSettingsPanel */
    public TestSettingsPanel() {
        initComponents();
        settingsPanel2.setDisplayType(SettingsPanel.DISPLAY_TABLE);
        jMenuBar1.add(new SettingsMenu());
        jMenuBar1.validate();
    }

    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        settingsPanel1 = new sequor.component.SettingsPanel();
        settingsPanel2 = new sequor.component.SettingsPanel();
        settingsBar1 = new sequor.component.SettingsBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Settings Test Application");
        jTabbedPane1.addTab("Regular", settingsPanel1);
        jTabbedPane1.addTab("Table", settingsPanel2);
        settingsBar1.setRollover(true);
        jTabbedPane1.addTab("ToolBar", settingsBar1);
        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);
        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE));
        pack();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new TestSettingsPanel().setVisible(true);
            }
        });
    }

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenu jMenu2;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JTabbedPane jTabbedPane1;

    private sequor.component.SettingsBar settingsBar1;

    private sequor.component.SettingsPanel settingsPanel1;

    private sequor.component.SettingsPanel settingsPanel2;
}
