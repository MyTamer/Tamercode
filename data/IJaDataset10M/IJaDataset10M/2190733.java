package org.drftpd.tools.installer.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.drftpd.tools.installer.InstallerConfig;
import org.drftpd.tools.installer.PluginData;
import org.drftpd.tools.installer.PluginTools;
import org.java.plugin.registry.Documentation;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

/**
 * @author djb61
 * @version $Id: PluginPanel.java 1883 2008-02-24 19:30:17Z djb61 $
 */
public class PluginPanel extends JPanel {

    private ArrayList<PluginData> _plugins;

    private JTextArea _desc;

    private JTabbedPane _parentPane;

    public PluginPanel(PluginRegistry registry, JTabbedPane parent, InstallerConfig config) {
        _plugins = PluginTools.getPluginData(registry);
        _parentPane = parent;
        for (PluginData plugin : _plugins) {
            Boolean sel = config.getPluginSelections().get(plugin.getName());
            if (sel != null) {
                plugin.setSelected(sel);
            }
        }
        BorderLayout pluginLayout = new BorderLayout();
        setLayout(pluginLayout);
        _desc = new JTextArea();
        _desc.setLineWrap(true);
        _desc.setEditable(false);
        _desc.setWrapStyleWord(true);
        _desc.setOpaque(false);
        _desc.setText("No plugin selected");
        JScrollPane descPane = new JScrollPane(_desc);
        descPane.setBorder(new TitledBorder(new EtchedBorder(), "Plugin Description"));
        JTable table = createTable(registry);
        JScrollPane tablePane = new JScrollPane(table);
        tablePane.setBorder(new TitledBorder(new EtchedBorder(), "Select plugins"));
        add(tablePane, BorderLayout.CENTER);
        add(descPane, BorderLayout.SOUTH);
    }

    private JTable createTable(PluginRegistry registry) {
        String columnNames[] = { "Build", "Plugin Name", "Version" };
        JTable table = new JTable(new SwingTableModel(columnNames, _plugins, registry));
        table.setDefaultRenderer(Boolean.class, new PluginCellRenderer(_plugins, table.getDefaultRenderer(Boolean.class)));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setMaxWidth(100);
        table.getColumnModel().getColumn(0).setResizable(false);
        table.getColumnModel().getColumn(2).setMaxWidth(100);
        table.getColumnModel().getColumn(2).setResizable(false);
        ListSelectionModel tableLSM = table.getSelectionModel();
        tableLSM.addListSelectionListener(new TableListSelectionListener(this));
        return table;
    }

    protected ArrayList<PluginData> getPlugins() {
        return _plugins;
    }

    protected void selectAllPlugins() {
        for (PluginData plugin : _plugins) {
            if (!plugin.isSelected()) {
                plugin.invertSelected();
            }
        }
        this.repaint();
    }

    protected void updateDescription(int pluginID) {
        Documentation<PluginDescriptor> doc = _plugins.get(pluginID).getDescriptor().getDocumentation();
        if (doc != null) {
            _desc.setText(doc.getText().replaceAll("\r\n|\n|\r", " "));
        } else {
            _desc.setText("This plugin has no description set, please contact the author.");
        }
        _desc.repaint();
        _parentPane.repaint();
    }
}

class PluginCellRenderer extends DefaultTableCellRenderer {

    private ArrayList<PluginData> _plugins;

    private TableCellRenderer _defaultRenderer;

    public PluginCellRenderer(ArrayList<PluginData> plugins, TableCellRenderer defaultRenderer) {
        super();
        _plugins = plugins;
        _defaultRenderer = defaultRenderer;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = _defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!_plugins.get(row).isModifiable()) {
            component.setEnabled(false);
        } else {
            component.setEnabled(true);
        }
        return component;
    }
}

class SwingTableModel extends AbstractTableModel implements TableModelListener {

    private String[] _columnNames;

    private ArrayList<PluginData> _plugins;

    private PluginRegistry _registry;

    private boolean _internalChange = false;

    public SwingTableModel(String[] columnNames, ArrayList<PluginData> plugins, PluginRegistry registry) {
        _columnNames = columnNames;
        _plugins = plugins;
        _registry = registry;
        addTableModelListener(this);
    }

    public int getColumnCount() {
        return _columnNames.length;
    }

    public int getRowCount() {
        return _plugins.size();
    }

    public String getColumnName(int col) {
        return _columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        switch(col) {
            case 0:
                return new Boolean(_plugins.get(row).isSelected());
            case 1:
                return _plugins.get(row).getName();
            case 2:
                return _plugins.get(row).getDescriptor().getVersion().toString();
            default:
                return null;
        }
    }

    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        if (col > 0) {
            return false;
        } else if (!_plugins.get(row).isModifiable()) {
            return false;
        } else {
            return true;
        }
    }

    public void setValueAt(Object value, int row, int col) {
        if (col == 0) {
            _plugins.get(row).setSelected(value);
        }
        if (_internalChange) {
            return;
        }
        fireTableCellUpdated(row, col);
    }

    public void tableChanged(TableModelEvent tme) {
        if (tme.getColumn() == 0 && tme.getFirstRow() == tme.getLastRow()) {
            PluginData selPlugin = _plugins.get(tme.getFirstRow());
            for (int i = 0; i < getRowCount(); i++) {
                PluginData dep = _plugins.get(i);
                if (selPlugin.isSelected()) {
                    if (PluginTools.isDependsInclImpl(selPlugin, dep, _registry)) {
                        if (!dep.isSelected()) {
                            dep.invertSelected();
                            _internalChange = true;
                            setValueAt(new Boolean(dep.isSelected()), i, 0);
                            _internalChange = false;
                        }
                    }
                } else {
                    if (PluginTools.isDependsInclImpl(dep, selPlugin, _registry)) {
                        if (dep.isSelected()) {
                            dep.invertSelected();
                            _internalChange = true;
                            setValueAt(new Boolean(dep.isSelected()), i, 0);
                            _internalChange = false;
                        }
                    }
                }
            }
            fireTableDataChanged();
        }
    }
}

class TableListSelectionListener implements ListSelectionListener {

    private PluginPanel _panel;

    public TableListSelectionListener(PluginPanel panel) {
        _panel = panel;
    }

    public void valueChanged(ListSelectionEvent lse) {
        ListSelectionModel lsm = (ListSelectionModel) lse.getSource();
        if (lsm.getMinSelectionIndex() != -1) {
            _panel.updateDescription(lsm.getMinSelectionIndex());
        }
    }
}
