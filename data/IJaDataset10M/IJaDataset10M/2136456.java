package pl.xperios.toolers.view.db;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import javax.inject.Inject;
import javax.swing.table.AbstractTableModel;
import pl.xperios.toolers.common.CustomDateEditor;
import pl.xperios.toolers.common.InitializePanel;
import pl.xperios.toolers.view.chooser.TableChooser;
import pl.xperios.toolers.common.Returnable;
import pl.xperios.toolers.dao.TableDao;
import pl.xperios.toolers.domains.Table;
import pl.xperios.toolers.service.ChangeLogService;
import pl.xperios.toolers.service.SettingsService;
import pl.xperios.toolers.view.MainApplicationWindow;

/**
 *
 * @author Praca
 */
public class NewDataPanelImpl extends javax.swing.JPanel implements NewDataPanel {

    @Inject
    MainApplicationWindow mainApp;

    @Inject
    TableChooser tableChooser;

    @Inject
    TableDao tableDao;

    @Inject
    ChangeLogService changeLogService;

    @Inject
    SettingsService settingsService;

    private Table table;

    private ArrayList<Object[]> rows;

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel15 = new javax.swing.JLabel();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        setOpaque(false);
        jLabel15.setText("Tabela:");
        jButton24.setText("Zapisz");
        jButton24.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        jButton25.setText("Kasuj");
        jButton25.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });
        jButton23.setText("...");
        jButton23.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        jScrollPane2.setViewportView(jTable3);
        jLabel1.setText("Komentarz:");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(jLabel15).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton23, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton25)).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton24))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel15).addComponent(jButton23).addComponent(jButton25)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton24).addComponent(jLabel1).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
    }

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            tableChooser.getTable(new Returnable<Table>() {

                @Override
                public void returning(Table returning) {
                    selectTableForNewData(returning);
                }
            });
        } catch (Exception ex) {
            mainApp.throwException(ex);
        }
    }

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {
        deleteSelectedRows(jTable3.getSelectedRows());
        ((AbstractTableModel) jTable3.getModel()).fireTableDataChanged();
    }

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            changeLogService.addInsert(settingsService.getChangelogFilePath(), settingsService.getAuthor(), rows.subList(0, rows.size() - 1), table.getColumnNameArray(), table.getName(), true, jTextField1.getText());
        } catch (Exception ex) {
            mainApp.throwException(ex);
        }
    }

    private javax.swing.JButton jButton23;

    private javax.swing.JButton jButton24;

    private javax.swing.JButton jButton25;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JTable jTable3;

    private javax.swing.JTextField jTextField1;

    @Override
    public InitializePanel init() {
        initComponents();
        jTable3.setDefaultEditor(Date.class, new CustomDateEditor());
        return this;
    }

    @Override
    public Component getMainComponent() {
        return this;
    }

    private void selectTableForNewData(Table returning) {
        table = returning;
        jTextField1.setText("Dodanie nowych rekordów do tabeli " + returning.getName());
        jButton23.setText(table.getName());
        prepareTooltipForTable(table);
        prepareTable(table);
    }

    private void prepareTooltipForTable(Table table) {
        String tooltip = "ID min=";
        try {
            tooltip += tableDao.getTableMinId(table, table.getColumns().get(0).getName());
        } catch (Exception exception) {
            exception.printStackTrace();
            tooltip += "null";
        }
        tooltip += ", max=";
        try {
            tooltip += tableDao.getTableMaxId(table, table.getColumns().get(0).getName());
        } catch (Exception exception) {
            exception.printStackTrace();
            tooltip += "null";
        }
        jButton23.setToolTipText(tooltip);
    }

    private void prepareTable(final Table table) {
        rows = new ArrayList<Object[]>();
        rows.add(new Object[table.getColumnCount()]);
        jTable3.setModel(new AbstractTableModel() {

            @Override
            public int getRowCount() {
                return rows.size();
            }

            @Override
            public int getColumnCount() {
                return table.getColumnCount();
            }

            @Override
            public String getColumnName(int columnIndex) {
                return table.getColumns().get(columnIndex).getName();
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return table.getColumns().get(columnIndex).getColumnClass();
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return true;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return rows.get(rowIndex)[columnIndex];
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                rows.get(rowIndex)[columnIndex] = aValue;
                if (checkLastRowIfAnythingIsNotNull(rows)) {
                    rows.add(new Object[table.getColumnCount()]);
                    fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
                }
            }

            private boolean checkLastRowIfAnythingIsNotNull(ArrayList<Object[]> rows) {
                Object[] lastRow = rows.get(rows.size() - 1);
                for (int i = 0; i < lastRow.length; i++) {
                    if (lastRow[i] != null) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void deleteSelectedRows(int[] selectedRows) {
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            int j = selectedRows[i];
            if (j == rows.size() - 1) {
                continue;
            }
            rows.remove(j);
        }
    }
}
