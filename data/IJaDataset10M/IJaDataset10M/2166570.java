package solowiki;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.RowSorter;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Giuseppe Profiti
 */
public class OptionsDialog extends javax.swing.JDialog {

    ConfigurableBundle configurableValues;

    /** Creates new form OptionsDialog */
    public OptionsDialog(java.awt.Frame parent, boolean modal, ConfigurableBundle conf) {
        super(parent, modal);
        configurableValues = conf;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        fileChooser = new javax.swing.JFileChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        confTable = new javax.swing.JTable();
        buttonsPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        loadMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("*.properties", "properties"));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("solowiki/resources/MainWindow");
        setTitle(bundle.getString("options"));
        setLocationByPlatform(true);
        confTable.setAutoCreateRowSorter(true);
        confTable.setModel(new javax.swing.table.DefaultTableModel(configurableValues.getData(), new java.util.Vector(java.util.Arrays.asList(new String[] { java.util.ResourceBundle.getBundle("solowiki/resources/MainWindow").getString("options.property"), java.util.ResourceBundle.getBundle("solowiki/resources/MainWindow").getString("options.value") }))) {

            boolean[] canEdit = new boolean[] { false, true };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        sortTable();
        jScrollPane1.setViewportView(confTable);
        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeSettings(evt);
            }
        });
        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("solowiki/resources/UpdatesDialog");
        cancelButton.setText(bundle1.getString("cancel"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelPressed(evt);
            }
        });
        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(buttonsPanelLayout.createSequentialGroup().addGap(115, 115, 115).addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(cancelButton).addContainerGap(105, Short.MAX_VALUE)));
        buttonsPanelLayout.setVerticalGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(buttonsPanelLayout.createSequentialGroup().addContainerGap().addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(okButton).addComponent(cancelButton)).addContainerGap(12, Short.MAX_VALUE)));
        fileMenu.setText(bundle.getString("filemenu"));
        loadMenuItem.setText(bundle.getString("filemenu.openmenuitem"));
        loadMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(loadMenuItem);
        saveMenuItem.setText(bundle.getString("filemenu.saveasmenuitem"));
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);
        jMenuBar1.add(fileMenu);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        pack();
    }

    private void changeSettings(java.awt.event.ActionEvent evt) {
        javax.swing.table.TableModel model = confTable.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String key = (String) model.getValueAt(i, 0);
            String value = (String) model.getValueAt(i, 1);
            configurableValues.setValue(key, value);
        }
        setVisible(false);
    }

    private void cancelPressed(java.awt.event.ActionEvent evt) {
        setTableValues(configurableValues.getData());
        setVisible(false);
    }

    private void loadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        int res = fileChooser.showOpenDialog(this);
        if (res == javax.swing.JFileChooser.APPROVE_OPTION) {
            try {
                FileInputStream fin = new FileInputStream(fileChooser.getSelectedFile());
                java.util.Properties prop = new java.util.Properties();
                prop.load(fin);
                fin.close();
                configurableValues.setValues(prop);
                setTableValues(configurableValues.getData());
                sortTable();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(OptionsDialog.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(OptionsDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        boolean proceed = true;
        java.io.File aFile = null;
        int res = fileChooser.showSaveDialog(this);
        if (res == javax.swing.JFileChooser.APPROVE_OPTION) {
            try {
                aFile = fileChooser.getSelectedFile();
                if (aFile.exists()) {
                    int res2 = JOptionPane.showConfirmDialog(this, java.util.ResourceBundle.getBundle("solowiki/resources/MainWindow").getString("Overwrite_") + aFile.getName() + "?", java.util.ResourceBundle.getBundle("solowiki/resources/MainWindow").getString("Warning"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (res2 != JOptionPane.YES_OPTION) {
                        proceed = false;
                    }
                } else {
                    aFile.createNewFile();
                }
            } catch (IOException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (res == javax.swing.JFileChooser.ERROR_OPTION) {
            JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("solowiki/resources/MainWindow").getString("Unable_to_save_file."), java.util.ResourceBundle.getBundle("solowiki/resources/MainWindow").getString("Save_error"), JOptionPane.WARNING_MESSAGE);
            proceed = false;
        } else {
            proceed = false;
        }
        if (proceed) {
            java.util.Properties prop = new java.util.Properties();
            javax.swing.table.TableModel model = confTable.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                String key = (String) model.getValueAt(i, 0);
                String value = (String) model.getValueAt(i, 1);
                prop.setProperty(key, value);
            }
            try {
                prop.store(new java.io.FileOutputStream(aFile), null);
                configurableValues.setValues(prop);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("solowiki/resources/MainWindow").getString("Unable_to_save_file."), java.util.ResourceBundle.getBundle("solowiki/resources/MainWindow").getString("Save_error"), JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void setTableValues(Vector<Vector<String>> values) {
        confTable.setModel(new javax.swing.table.DefaultTableModel(values, new java.util.Vector(java.util.Arrays.asList(new String[] { java.util.ResourceBundle.getBundle("solowiki/resources/MainWindow").getString("options.property"), java.util.ResourceBundle.getBundle("solowiki/resources/MainWindow").getString("options.value") }))) {

            boolean[] canEdit = new boolean[] { false, true };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
    }

    private void sortTable() {
        TableRowSorter<javax.swing.table.DefaultTableModel> sorter = new TableRowSorter(confTable.getModel());
        java.util.List<RowSorter.SortKey> sortKeys = new java.util.ArrayList<RowSorter.SortKey>();
        sortKeys.add(new RowSorter.SortKey(0, javax.swing.SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(1, javax.swing.SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        confTable.setRowSorter(sorter);
    }

    private javax.swing.JPanel buttonsPanel;

    private javax.swing.JButton cancelButton;

    private javax.swing.JTable confTable;

    private javax.swing.JFileChooser fileChooser;

    private javax.swing.JMenu fileMenu;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JMenuItem loadMenuItem;

    private javax.swing.JButton okButton;

    private javax.swing.JMenuItem saveMenuItem;
}
