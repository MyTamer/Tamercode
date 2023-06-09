package com.vividsolutions.jump.workbench.ui.renderer.style;

import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.CollectionUtil;
import com.vividsolutions.jump.util.Range;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ColorPanel;
import com.vividsolutions.jump.workbench.ui.EnableableToolBar;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.style.BasicStylePanel;
import com.vividsolutions.jump.workbench.ui.style.StylePanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class ColorThemingStylePanel extends JPanel implements StylePanel {

    private static final String CUSTOM_ENTRY = "Custom...";

    public static final String TITLE = "Colour Theming";

    public static final String COLOR_SCHEME_KEY = ColorThemingStylePanel.class.getName() + " - COLOR SCHEME";

    private WorkbenchContext workbenchContext;

    private Layer layer;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JPanel jPanel1 = new JPanel();

    private JScrollPane scrollPane = new JScrollPane();

    private DefaultTableCellRenderer allOtherValuesRenderer = new DefaultTableCellRenderer();

    private JTable table = new JTable() {

        public TableCellRenderer getCellRenderer(int row, int column) {
            TableCellRenderer renderer = getCellRendererProper(row, column);
            if (renderer instanceof JComponent) {
                updateBackground((JComponent) renderer);
            }
            return renderer;
        }

        private TableCellRenderer getCellRendererProper(int row, int column) {
            if ((row == 0) && (column == attributeColumn())) {
                return allOtherValuesRenderer;
            }
            if ((row == 0) && (column == labelColumn())) {
                return allOtherValuesRenderer;
            }
            TableCellRenderer renderer = super.getCellRenderer(row, column);
            if (renderer instanceof JLabel) {
                ((JLabel) renderer).setHorizontalAlignment(JLabel.LEFT);
            }
            return renderer;
        }
    };

    private JPanel jPanel3 = new JPanel();

    private GridBagLayout gridBagLayout2 = new GridBagLayout();

    private GridBagLayout gridBagLayout4 = new GridBagLayout();

    private JComboBox attributeNameComboBox = new JComboBox();

    private JLabel attributeLabel = new JLabel();

    private JLabel statusLabel = new JLabel() {

        public void setText(String text) {
            super.setText(text);
            setToolTipText(text);
        }
    };

    private EnableableToolBar toolBar = new EnableableToolBar();

    private JPanel jPanel4 = new JPanel();

    private GridBagLayout gridBagLayout5 = new GridBagLayout();

    private JComboBox colorSchemeComboBox = new JComboBox();

    private JLabel colorSchemeLabel = new JLabel();

    private boolean updatingComponents = false;

    private boolean initializing = false;

    private BasicStyleListCellRenderer basicStyleListCellRenderer = new BasicStyleListCellRenderer();

    public BasicStylePanel basicStylePanel;

    private TableCellEditor basicStyleTableCellEditor = new TableCellEditor() {

        private DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();

        private BasicStyle originalStyle;

        private DefaultCellEditor editor;

        private JComboBox comboBox = new JComboBox(comboBoxModel) {

            public void setSelectedItem(Object anObject) {
                if (anObject != CUSTOM_ENTRY) {
                    super.setSelectedItem(anObject);
                    return;
                }
                BasicStyle style = promptBasicStyle(originalStyle);
                if (style == null) {
                    return;
                }
                comboBox.addItem(style);
                super.setSelectedItem(style);
            }
        };

        {
            comboBox.setRenderer(basicStyleListCellRenderer);
            editor = new DefaultCellEditor(comboBox);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            originalStyle = (BasicStyle) value;
            comboBoxModel.removeAllElements();
            comboBoxModel.addElement(CUSTOM_ENTRY);
            comboBoxModel.addElement(value);
            for (Iterator i = ColorScheme.create((String) colorSchemeComboBox.getSelectedItem()).getColors().iterator(); i.hasNext(); ) {
                Color color = (Color) i.next();
                comboBoxModel.addElement(new BasicStyle(color));
            }
            comboBoxModel.setSelectedItem(value);
            return editor.getTableCellEditorComponent(table, value, isSelected, row, column);
        }

        public Object getCellEditorValue() {
            return editor.getCellEditorValue();
        }

        public boolean isCellEditable(EventObject anEvent) {
            return editor.isCellEditable(anEvent);
        }

        public boolean shouldSelectCell(EventObject anEvent) {
            return editor.shouldSelectCell(anEvent);
        }

        public boolean stopCellEditing() {
            return editor.stopCellEditing();
        }

        public void cancelCellEditing() {
            editor.cancelCellEditing();
        }

        public void addCellEditorListener(CellEditorListener l) {
            editor.addCellEditorListener(l);
        }

        public void removeCellEditorListener(CellEditorListener l) {
            editor.removeCellEditorListener(l);
        }
    };

    private DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();

    private JPanel fillerPanel = new JPanel();

    private String lastAttributeName;

    private ColorScheme colorSchemeForInserts = null;

    private MyPlugIn insertPlugIn = new MyPlugIn() {

        public String getName() {
            return "Insert Row";
        }

        public Icon getIcon() {
            return GUIUtil.toSmallIcon(IconLoader.icon("Plus.gif"));
        }

        public boolean execute(PlugInContext context) throws Exception {
            reportNothingToUndoYet(context);
            stopCellEditing();
            tableModel().insertAttributeValue((table.getSelectedRowCount() > 0) ? table.getSelectedRows()[0] : table.getRowCount(), getColorSchemeForInserts());
            if (table.getSelectedRowCount() == 0) {
                table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0, true));
            }
            if (table.getSelectedRowCount() != 0) {
                int firstSelectedRow = table.getSelectedRows()[0];
                table.clearSelection();
                table.addRowSelectionInterval(firstSelectedRow, firstSelectedRow);
            }
            return true;
        }
    };

    private MyPlugIn deletePlugIn = new MyPlugIn() {

        public String getName() {
            return "Delete Row";
        }

        public Icon getIcon() {
            return GUIUtil.toSmallIcon(IconLoader.icon("Delete.gif"));
        }

        public boolean execute(PlugInContext context) throws Exception {
            reportNothingToUndoYet(context);
            stopCellEditing();
            tableModel().removeAttributeValues(table.getSelectedRows());
            return true;
        }
    };

    private HashSet errorMessages = new HashSet();

    private DiscreteColorThemingState discreteColorThemingState = new DiscreteColorThemingState(table);

    private RangeColorThemingState rangeColorThemingState;

    private State state = discreteColorThemingState;

    private JPanel jPanel5 = new JPanel();

    private GridBagLayout gridBagLayout3 = new GridBagLayout();

    private JCheckBox enableColorThemingCheckBox = new JCheckBox();

    private JCheckBox byRangeCheckBox = new JCheckBox();

    private JSlider transparencySlider = new JSlider();

    public ColorThemingStylePanel(Layer layer, WorkbenchContext workbenchContext) {
        initializing = true;
        try {
            basicStylePanel = new BasicStylePanel(workbenchContext.getWorkbench().getBlackboard(), ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            this.layer = layer;
            this.workbenchContext = workbenchContext;
            rangeColorThemingState = new RangeColorThemingState(this);
            jbInit();
            byRangeCheckBox.setSelected(colorThemingStyleHasRanges(layer));
            state = byRangeCheckBox.isSelected() ? (State) rangeColorThemingState : discreteColorThemingState;
            initTable(layer);
            setState(state);
            initAttributeNameComboBox(layer);
            initColorSchemeComboBox(layer.getLayerManager());
            initTransparencySlider(layer);
            initToolBar();
            enableColorThemingCheckBox.setSelected(ColorThemingStyle.get(layer).isEnabled());
            updateComponents();
            GUIUtil.sync(basicStylePanel.getTransparencySlider(), transparencySlider);
            basicStylePanel.setSynchronizingLineColor(layer.isSynchronizingLineColor());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            initializing = false;
        }
    }

    private void updateBackground(JComponent component) {
        component.setBackground(enableColorThemingCheckBox.isSelected() ? Color.white : jPanel1.getBackground());
    }

    private int attributeColumn() {
        return table.convertColumnIndexToView(ColorThemingTableModel.ATTRIBUTE_COLUMN);
    }

    private int labelColumn() {
        return table.convertColumnIndexToView(ColorThemingTableModel.LABEL_COLUMN);
    }

    private int colorColumn() {
        return table.convertColumnIndexToView(ColorThemingTableModel.COLOR_COLUMN);
    }

    public String getTitle() {
        return TITLE;
    }

    public void updateStyles() {
        boolean firingEvents = layer.getLayerManager().isFiringEvents();
        layer.getLayerManager().setFiringEvents(false);
        try {
            layer.removeStyle(ColorThemingStyle.get(layer));
            layer.addStyle(new ColorThemingStyle(getAttributeName(), state.toExternalFormat(tableModel().getAttributeValueToBasicStyleMap()), state.toExternalFormat(tableModel().getAttributeValueToLabelMap()), tableModel().getDefaultStyle()));
            ColorThemingStyle.get(layer).setAlpha(getAlpha());
            ColorThemingStyle.get(layer).setEnabled(enableColorThemingCheckBox.isSelected());
            layer.getBasicStyle().setEnabled(!enableColorThemingCheckBox.isSelected());
        } finally {
            layer.getLayerManager().setFiringEvents(firingEvents);
        }
        layer.fireAppearanceChanged();
    }

    private String getAttributeName() {
        return (String) attributeNameComboBox.getSelectedItem();
    }

    private void stopCellEditing() {
        if (table.getCellEditor() instanceof DefaultCellEditor) {
            ((DefaultCellEditor) table.getCellEditor()).stopCellEditing();
        }
    }

    public JCheckBox getSynchronizeCheckBox() {
        return basicStylePanel.getSynchronizeCheckBox();
    }

    public Layer getLayer() {
        return layer;
    }

    private void initTransparencySlider(Layer layer) {
        transparencySlider.setValue(transparencySlider.getMaximum() - ColorThemingStyle.get(layer).getDefaultStyle().getAlpha());
        transparencySlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                basicStyleListCellRenderer.setAlpha(getAlpha());
            }
        });
        basicStyleListCellRenderer.setAlpha(getAlpha());
    }

    private boolean colorThemingStyleHasRanges(Layer layer) {
        return !ColorThemingStyle.get(layer).getAttributeValueToBasicStyleMap().isEmpty() && ColorThemingStyle.get(layer).getAttributeValueToBasicStyleMap().keySet().iterator().next() instanceof Range;
    }

    private void initToolBar() {
        EnableCheck atLeast1RowMustBeSelectedCheck = new EnableCheck() {

            public String check(JComponent component) {
                return (table.getSelectedRowCount() == 0) ? "At least 1 row must be selected" : null;
            }
        };
        EnableCheck layerMustHaveAtLeast1AttributeCheck = new EnableCheck() {

            public String check(JComponent component) {
                return (attributeNameComboBox.getItemCount() == 0) ? "Layer must have at least 1 attribute" : null;
            }
        };
        EnableCheck colorThemingMustBeEnabledCheck = new EnableCheck() {

            public String check(JComponent component) {
                return (!enableColorThemingCheckBox.isSelected()) ? "Colour theming must be enabled" : null;
            }
        };
        addPlugIn(insertPlugIn, new MultiEnableCheck().add(layerMustHaveAtLeast1AttributeCheck).add(colorThemingMustBeEnabledCheck));
        addPlugIn(deletePlugIn, new MultiEnableCheck().add(layerMustHaveAtLeast1AttributeCheck).add(atLeast1RowMustBeSelectedCheck).add(colorThemingMustBeEnabledCheck));
    }

    private void addPlugIn(MyPlugIn plugIn, EnableCheck enableCheck) {
        JButton button = new JButton();
        toolBar.add(button, plugIn.getName(), plugIn.getIcon(), AbstractPlugIn.toActionListener(plugIn, workbenchContext, null), enableCheck);
    }

    private void updateComponents() {
        if (updatingComponents) {
            return;
        }
        updatingComponents = true;
        try {
            attributeLabel.setEnabled(enableColorThemingCheckBox.isSelected());
            attributeNameComboBox.setEnabled(enableColorThemingCheckBox.isSelected());
            state.getPanel().setEnabled(enableColorThemingCheckBox.isSelected() && (attributeNameComboBox.getItemCount() > 0));
            colorSchemeLabel.setEnabled(enableColorThemingCheckBox.isSelected() && (attributeNameComboBox.getItemCount() > 0));
            byRangeCheckBox.setEnabled(enableColorThemingCheckBox.isSelected() && (attributeNameComboBox.getItemCount() > 0));
            colorSchemeComboBox.setEnabled(enableColorThemingCheckBox.isSelected() && (attributeNameComboBox.getItemCount() > 0));
            table.setEnabled(enableColorThemingCheckBox.isSelected() && (attributeNameComboBox.getItemCount() > 0));
            scrollPane.setEnabled(enableColorThemingCheckBox.isSelected() && (attributeNameComboBox.getItemCount() > 0));
            transparencySlider.setEnabled(enableColorThemingCheckBox.isSelected() && (attributeNameComboBox.getItemCount() > 0));
            statusLabel.setEnabled(enableColorThemingCheckBox.isSelected());
            toolBar.updateEnabledState();
            if (!setErrorMessage(new ErrorMessage("Cannot colour-theme layer with no attributes"), attributeNameComboBox.getItemCount() == 0)) {
                setErrorMessage(new ErrorMessage("Table must not be empty"), table.getRowCount() == 0);
            }
            updateErrorDisplay();
            if (table.getColumnCount() > 0) {
                table.getColumnModel().getColumn(table.convertColumnIndexToView(ColorThemingTableModel.ATTRIBUTE_COLUMN)).setHeaderValue(state.getAttributeValueColumnTitle());
            }
        } finally {
            updatingComponents = false;
        }
    }

    /**
     * @return null if user hits Cancel
     */
    private BasicStyle promptBasicStyle(BasicStyle basicStyle) {
        int originalTransparencySliderValue = transparencySlider.getValue();
        basicStylePanel.setBasicStyle(basicStyle);
        basicStylePanel.getTransparencySlider().setValue(originalTransparencySliderValue);
        OKCancelPanel okCancelPanel = new OKCancelPanel();
        final JDialog dialog = new JDialog((JDialog) SwingUtilities.windowForComponent(this), "Custom", true);
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(basicStylePanel, BorderLayout.CENTER);
        dialog.getContentPane().add(okCancelPanel, BorderLayout.SOUTH);
        okCancelPanel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });
        dialog.pack();
        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);
        if (!okCancelPanel.wasOKPressed()) {
            transparencySlider.setValue(originalTransparencySliderValue);
        }
        return okCancelPanel.wasOKPressed() ? basicStylePanel.getBasicStyle() : null;
    }

    private void initTable(Layer layer) {
        table.setModel(new ColorThemingTableModel(ColorThemingStyle.get(layer).getDefaultStyle(), ColorThemingStyle.get(layer).getAttributeName(), attributeValueToBasicStyleMap(layer), attributeValueToLabelMap(layer), layer.getFeatureCollectionWrapper().getFeatureSchema()) {

            public Object getValueAt(int rowIndex, int columnIndex) {
                if ((rowIndex == 0) && (columnIndex == ColorThemingTableModel.ATTRIBUTE_COLUMN)) {
                    return state.getAllOtherValuesDescription();
                }
                if ((rowIndex == 0) && (columnIndex == ColorThemingTableModel.LABEL_COLUMN)) {
                    return "";
                }
                return super.getValueAt(rowIndex, columnIndex);
            }
        });
        table.createDefaultColumnsFromModel();
        table.setRowSelectionAllowed(true);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                updateComponents();
            }
        });
        table.getTableHeader().addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && (e.getClickCount() == 1) && table.isEnabled()) {
                    tableModel().sort();
                }
            }
        });
        table.getColumnModel().getColumn(colorColumn()).setCellRenderer(new TableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JComponent renderer = (JComponent) basicStyleListCellRenderer.getListCellRendererComponent(new JList(), value, row, isSelected, hasFocus);
                if (!isSelected) {
                    updateBackground(renderer);
                }
                return renderer;
            }
        });
        table.getColumnModel().getColumn(colorColumn()).setCellEditor(basicStyleTableCellEditor);
        table.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                updateComponents();
                Object duplicateAttributeValue = tableModel().findDuplicateAttributeValue();
                setErrorMessage(new ErrorMessage("Table must not have duplicate attribute values ", "(" + duplicateAttributeValue + ")"), duplicateAttributeValue != null);
                setErrorMessage(new ErrorMessage("Table must not have null attribute values"), tableModel().containsNullAttributeValues());
            }
        });
        int colorWidth = 10 + (int) basicStyleListCellRenderer.getListCellRendererComponent(new JList(), new BasicStyle(), 0, false, false).getPreferredSize().getWidth();
        table.getColumnModel().getColumn(colorColumn()).setPreferredWidth(colorWidth);
        table.getColumnModel().getColumn(colorColumn()).setMinWidth(colorWidth);
        table.getColumnModel().getColumn(colorColumn()).setMaxWidth(colorWidth);
    }

    private Map attributeValueToBasicStyleMap(Layer layer) {
        if (!colorThemingAttributeValid(layer)) {
            return new TreeMap();
        }
        return state.fromExternalFormat(ColorThemingStyle.get(layer).getAttributeValueToBasicStyleMap());
    }

    private Map attributeValueToLabelMap(Layer layer) {
        if (!colorThemingAttributeValid(layer)) {
            return new TreeMap();
        }
        return state.fromExternalFormat(ColorThemingStyle.get(layer).getAttributeValueToLabelMap());
    }

    private boolean colorThemingAttributeValid(Layer layer) {
        if (ColorThemingStyle.get(layer).getAttributeName() == null) {
            return false;
        }
        if (!layer.getFeatureCollectionWrapper().getFeatureSchema().hasAttribute(ColorThemingStyle.get(layer).getAttributeName())) {
            return false;
        }
        return true;
    }

    private void initColorSchemeComboBox(LayerManager layerManager) {
        colorSchemeComboBox.setRenderer(new ColorSchemeListCellRenderer() {

            protected void color(ColorPanel colorPanel, Color fillColor, Color lineColor) {
                super.color(colorPanel, GUIUtil.alphaColor(fillColor, getAlpha()), GUIUtil.alphaColor(lineColor, getAlpha()));
            }

            protected ColorScheme colorScheme(String name) {
                return state.filterColorScheme(super.colorScheme(name));
            }
        });
    }

    private int getAlpha() {
        return transparencySlider.getMaximum() - transparencySlider.getValue();
    }

    private void initAttributeNameComboBox(Layer layer) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i = 0; i < layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount(); i++) {
            if (i == layer.getFeatureCollectionWrapper().getFeatureSchema().getGeometryIndex()) {
                continue;
            }
            model.addElement(layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeName(i));
        }
        attributeNameComboBox.setModel(model);
        if (model.getSize() == 0) {
            return;
        }
        attributeNameComboBox.setSelectedItem(ColorThemingStyle.get(layer).getAttributeName());
    }

    private void jbInit() throws Exception {
        this.setLayout(gridBagLayout1);
        jPanel1.setLayout(gridBagLayout2);
        jPanel3.setLayout(gridBagLayout4);
        attributeLabel.setText("Attribute: ");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusLabel.setText(" ");
        jPanel4.setLayout(gridBagLayout5);
        colorSchemeLabel.setText("Colour Scheme: ");
        attributeNameComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                attributeNameComboBox_actionPerformed(e);
            }
        });
        colorSchemeComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                colorSchemeComboBox_actionPerformed(e);
            }
        });
        jPanel5.setLayout(gridBagLayout3);
        enableColorThemingCheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                enableColorThemingCheckBox_actionPerformed(e);
            }
        });
        enableColorThemingCheckBox.setText("Enable colour theming");
        byRangeCheckBox.setText("by range");
        byRangeCheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                byRangeCheckBox_actionPerformed(e);
            }
        });
        transparencySlider.setMaximum(255);
        transparencySlider.setPreferredSize(new Dimension(75, 24));
        transparencySlider.setMinimumSize(new Dimension(75, 24));
        transparencySlider.setToolTipText("Transparency");
        transparencySlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                transparencySlider_stateChanged(e);
            }
        });
        this.add(jPanel1, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(attributeNameComboBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        jPanel1.add(attributeLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        jPanel1.add(fillerPanel, new GridBagConstraints(4, 0, 1, 1, 1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(scrollPane, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        this.add(jPanel3, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        jPanel3.add(statusLabel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        jPanel3.add(toolBar, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel3.add(transparencySlider, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(jPanel4, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        jPanel4.add(colorSchemeComboBox, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jPanel4.add(colorSchemeLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(jPanel5, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel5.add(enableColorThemingCheckBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        jPanel5.add(byRangeCheckBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        scrollPane.getViewport().add(table);
    }

    protected void enableColorThemingCheckBox_actionPerformed(ActionEvent e) {
        if (table.getRowCount() == 1) {
            populateTable();
        }
        updateComponents();
    }

    void attributeNameComboBox_actionPerformed(ActionEvent e) {
        try {
            if (initializing) {
                return;
            }
            if (attributeNameComboBox.getItemCount() == 0) {
                return;
            }
            Object selectedItem = attributeNameComboBox.getSelectedItem();
            if (selectedItem != null && selectedItem.equals(lastAttributeName)) {
                return;
            }
            stopCellEditing();
            populateTable();
        } finally {
            lastAttributeName = getAttributeName();
            if (table.getModel() instanceof ColorThemingTableModel) {
                tableModel().setAttributeName(getAttributeName());
            }
        }
    }

    public ColorThemingTableModel tableModel() {
        return (ColorThemingTableModel) table.getModel();
    }

    private SortedSet getNonNullAttributeValues() {
        TreeSet values = new TreeSet();
        for (Iterator i = layer.getFeatureCollectionWrapper().getFeatures().iterator(); i.hasNext(); ) {
            Feature feature = (Feature) i.next();
            if (feature.getAttribute(getAttributeName()) != null) {
                values.add(ColorThemingStyle.trimIfString(feature.getAttribute(getAttributeName())));
            }
        }
        return values;
    }

    public void populateTable() {
        if (!(enableColorThemingCheckBox.isSelected() && (attributeNameComboBox.getItemCount() > 0))) {
            return;
        }
        stopCellEditing();
        tableModel().clear();
        tableModel().setMaps(toAttributeValueToBasicStyleMap(filteredAttributeValues()), toAttributeValueToLabelMap(filteredAttributeValues()));
        tableModel().sort(tableModel().wasLastSortAscending());
        applyColorScheme();
    }

    private Collection filteredAttributeValues() {
        return state.filterAttributeValues(getNonNullAttributeValues());
    }

    private Map toAttributeValueToLabelMap(Collection attributeValues) {
        Map attributeValueToAttributeValueMap = new TreeMap();
        for (Iterator i = attributeValues.iterator(); i.hasNext(); ) {
            Object attributeValue = i.next();
            attributeValueToAttributeValueMap.put(attributeValue, attributeValue);
        }
        Map attributeValueToLabelMap = CollectionUtil.inverse(state.toExternalFormat(attributeValueToAttributeValueMap));
        for (Iterator i = attributeValueToLabelMap.keySet().iterator(); i.hasNext(); ) {
            Object attributeValue = i.next();
            attributeValueToLabelMap.put(attributeValue, attributeValueToLabelMap.get(attributeValue).toString());
        }
        return attributeValueToLabelMap;
    }

    private Map toAttributeValueToBasicStyleMap(Collection attributeValues) {
        Map attributeValueToBasicStyleMap = new TreeMap();
        for (Iterator i = attributeValues.iterator(); i.hasNext(); ) {
            Object attributeValue = i.next();
            attributeValueToBasicStyleMap.put(attributeValue, new BasicStyle());
        }
        return attributeValueToBasicStyleMap;
    }

    void colorSchemeComboBox_actionPerformed(ActionEvent e) {
        if (initializing) {
            return;
        }
        stopCellEditing();
        layer.getLayerManager().getBlackboard().put(COLOR_SCHEME_KEY, colorSchemeComboBox.getSelectedItem());
        applyColorScheme();
        colorSchemeForInserts = null;
    }

    private ColorScheme getColorSchemeForInserts() {
        if ((colorSchemeForInserts == null) || !colorSchemeForInserts.getName().equalsIgnoreCase((String) colorSchemeComboBox.getSelectedItem())) {
            colorSchemeForInserts = ColorScheme.create((String) colorSchemeComboBox.getSelectedItem());
        }
        return colorSchemeForInserts;
    }

    public void applyColorScheme() {
        stopCellEditing();
        state.applyColorScheme(state.filterColorScheme(ColorScheme.create((String) colorSchemeComboBox.getSelectedItem())));
    }

    private void cancelCellEditing() {
        if (table.getCellEditor() instanceof DefaultCellEditor) {
            ((DefaultCellEditor) table.getCellEditor()).cancelCellEditing();
        }
    }

    public String validateInput() {
        stopCellEditing();
        return internalValidateInput();
    }

    private String internalValidateInput() {
        if (!enableColorThemingCheckBox.isSelected()) {
            return null;
        }
        if (errorMessages.isEmpty()) {
            return null;
        }
        return errorMessages.iterator().next().toString();
    }

    /**
     * @return enabled
     */
    private boolean setErrorMessage(ErrorMessage message, boolean enabled) {
        errorMessages.remove(message);
        if (enabled) {
            errorMessages.add(message);
        }
        updateErrorDisplay();
        return enabled;
    }

    private void updateErrorDisplay() {
        String errorMessage = internalValidateInput();
        if (errorMessage != null) {
            statusLabel.setText(errorMessage);
            statusLabel.setIcon(GUIUtil.toSmallIcon(IconLoader.icon("Delete.gif")));
        } else {
            statusLabel.setText(" ");
            statusLabel.setIcon(null);
        }
    }

    private void setState(State state) {
        this.state.deactivate();
        jPanel1.remove(this.state.getPanel());
        this.state = state;
        jPanel1.add(state.getPanel(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        initializing = true;
        try {
            colorSchemeComboBox.setModel(new DefaultComboBoxModel(new Vector(state.getColorSchemeNames())));
            colorSchemeComboBox.setSelectedItem(layer.getLayerManager().getBlackboard().get(COLOR_SCHEME_KEY, colorSchemeComboBox.getItemAt(0)));
        } finally {
            initializing = false;
        }
        updateComponents();
        this.state.activate();
        jPanel1.repaint();
    }

    void byRangeCheckBox_actionPerformed(ActionEvent e) {
        setState(byRangeCheckBox.isSelected() ? (State) rangeColorThemingState : discreteColorThemingState);
        populateTable();
    }

    void transparencySlider_stateChanged(ChangeEvent e) {
        repaint();
    }

    public JSlider getTransparencySlider() {
        return transparencySlider;
    }

    public JTable getTable() {
        return table;
    }

    public static interface State {

        public String getAllOtherValuesDescription();

        public ColorScheme filterColorScheme(ColorScheme colorScheme);

        public void activate();

        public void deactivate();

        public Collection getColorSchemeNames();

        public void applyColorScheme(ColorScheme scheme);

        public Collection filterAttributeValues(SortedSet attributeValues);

        public String getAttributeValueColumnTitle();

        public JComponent getPanel();

        /**
         * Performs any necessary modifications to the map before applying
         * it to the layer.
         */
        public Map toExternalFormat(Map attributeValueToObjectMap);

        public Map fromExternalFormat(Map attributeValueToObjectMap);
    }

    private abstract class MyPlugIn extends AbstractPlugIn {

        public abstract Icon getIcon();
    }

    private class ErrorMessage {

        private String commonPart;

        private String specificPart;

        public ErrorMessage(String commonPart) {
            this(commonPart, "");
        }

        public ErrorMessage(String commonPart, String specificPart) {
            this.commonPart = commonPart;
            this.specificPart = specificPart;
        }

        public int hashCode() {
            return commonPart.hashCode();
        }

        public boolean equals(Object obj) {
            return commonPart.equals(((ErrorMessage) obj).commonPart);
        }

        public String toString() {
            return commonPart + specificPart;
        }
    }
}
