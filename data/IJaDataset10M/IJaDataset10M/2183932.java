package org.tigr.microarray.mev.cgh.CGHGuiObj.CGHBrowser;

import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTable;
import org.tigr.microarray.mev.cgh.CGHDataModel.CGHBrowserModelAdaptor;
import org.tigr.microarray.mev.cgh.CGHDataModel.CGHChartDataModel;
import org.tigr.microarray.mev.cgh.CGHDataModel.CGHTableDataModel;
import org.tigr.microarray.mev.cgh.CGHDataObj.ICGHDataRegion;
import org.tigr.microarray.mev.cgh.CGHUtil.CGHUtility;
import org.tigr.microarray.mev.cluster.gui.IData;

/**
 *
 * @author  Adam Margolin
 * @author Raktim Sinha
 */
public class CGHBrowser extends javax.swing.JFrame implements ActionListener {

    private CGHChartPanel chartPanel;

    private IData data;

    private CGHBrowserModelAdaptor browserModel;

    /** Creates new form CGHBrowser */
    public CGHBrowser(IData data, CGHChartDataModel chartModel, CGHTableDataModel tableModel) {
        this(data, 0, 0, chartModel, tableModel, CGHBrowserModelAdaptor.CLONE_VALUES_DYE_SWAP, true);
    }

    public CGHBrowser(IData data, int sampleIndex, int chromosomeIndex, CGHChartDataModel chartModel, CGHTableDataModel tableModel, int cloneValueType, boolean hasDyeSwap) {
        this.data = data;
        browserModel = new CGHBrowserModelAdaptor(data, sampleIndex, chromosomeIndex, cloneValueType);
        browserModel.setCloneValueType(cloneValueType);
        chartModel.setAdaptor(browserModel);
        tableModel.setAdaptor(browserModel);
        browserModel.addChangeListener(chartModel);
        browserModel.addChangeListener(tableModel);
        chartPanel = new CGHChartPanel(sampleIndex, chromosomeIndex, browserModel);
        initComponents();
        setSize(1000, 800);
        setChartModel(chartModel);
        setTableModel(tableModel);
        initCustomComponents();
        initMenus(sampleIndex, chromosomeIndex, cloneValueType, hasDyeSwap);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        grpXAxisOrder = new javax.swing.ButtonGroup();
        grpXAxisLabels = new javax.swing.ButtonGroup();
        grpCloneValues = new javax.swing.ButtonGroup();
        grpChartTypes = new javax.swing.ButtonGroup();
        pnlMain = new javax.swing.JPanel();
        menubar = new javax.swing.JMenuBar();
        mnuExperiment = new javax.swing.JMenu();
        mnuChromosome = new javax.swing.JMenu();
        mnuView = new javax.swing.JMenu();
        mnuChartType = new javax.swing.JMenu();
        rdoPlot = new javax.swing.JRadioButtonMenuItem();
        rdoScatterPlot = new javax.swing.JRadioButtonMenuItem();
        rdoBar = new javax.swing.JRadioButtonMenuItem();
        rdoArea = new javax.swing.JRadioButtonMenuItem();
        mnuXAxisValues = new javax.swing.JMenu();
        rdoXAxisByPosition = new javax.swing.JRadioButtonMenuItem();
        rdoXAxisByLinearOrder = new javax.swing.JRadioButtonMenuItem();
        mnuXAxisLabels = new javax.swing.JMenu();
        rdoXAxisLabelsNone = new javax.swing.JRadioButtonMenuItem();
        rdoXAxisLabelsByClone = new javax.swing.JRadioButtonMenuItem();
        rdoXAxisLabelsByValue = new javax.swing.JRadioButtonMenuItem();
        chkShowLegend = new javax.swing.JCheckBoxMenuItem();
        chkSmoothUnconfirmed = new javax.swing.JCheckBoxMenuItem();
        btnPointSize = new javax.swing.JMenuItem();
        btnDisplayRange = new javax.swing.JMenuItem();
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        pnlMain.setLayout(new java.awt.BorderLayout());
        getContentPane().add(pnlMain, java.awt.BorderLayout.CENTER);
        mnuExperiment.setText("Experiment");
        menubar.add(mnuExperiment);
        mnuChromosome.setText("Chromosome");
        menubar.add(mnuChromosome);
        mnuView.setText("View");
        mnuChartType.setText("Chart Type");
        rdoPlot.setSelected(true);
        rdoPlot.setText("Plot");
        grpChartTypes.add(rdoPlot);
        rdoPlot.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPlotActionPerformed(evt);
            }
        });
        mnuChartType.add(rdoPlot);
        rdoScatterPlot.setText("Scatter Plot");
        grpChartTypes.add(rdoScatterPlot);
        rdoScatterPlot.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoScatterPlotActionPerformed(evt);
            }
        });
        mnuChartType.add(rdoScatterPlot);
        rdoBar.setText("Bar");
        grpChartTypes.add(rdoBar);
        rdoBar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBarActionPerformed(evt);
            }
        });
        mnuChartType.add(rdoBar);
        rdoArea.setText("Area");
        grpChartTypes.add(rdoArea);
        rdoArea.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAreaActionPerformed(evt);
            }
        });
        mnuChartType.add(rdoArea);
        mnuView.add(mnuChartType);
        mnuXAxisValues.setText("X Axis Order");
        rdoXAxisByPosition.setText("Position");
        grpXAxisOrder.add(rdoXAxisByPosition);
        rdoXAxisByPosition.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoXAxisByPositionActionPerformed(evt);
            }
        });
        mnuXAxisValues.add(rdoXAxisByPosition);
        rdoXAxisByLinearOrder.setSelected(true);
        rdoXAxisByLinearOrder.setText("Linear Order");
        grpXAxisOrder.add(rdoXAxisByLinearOrder);
        rdoXAxisByLinearOrder.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoXAxisByLinearOrderActionPerformed(evt);
            }
        });
        mnuXAxisValues.add(rdoXAxisByLinearOrder);
        mnuView.add(mnuXAxisValues);
        mnuXAxisLabels.setText("X Axis Labels");
        rdoXAxisLabelsNone.setSelected(true);
        rdoXAxisLabelsNone.setText("None");
        grpXAxisLabels.add(rdoXAxisLabelsNone);
        rdoXAxisLabelsNone.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoXAxisLabelsNoneActionPerformed(evt);
            }
        });
        mnuXAxisLabels.add(rdoXAxisLabelsNone);
        rdoXAxisLabelsByClone.setText("Clone");
        grpXAxisLabels.add(rdoXAxisLabelsByClone);
        rdoXAxisLabelsByClone.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoXAxisLabelsByCloneActionPerformed(evt);
            }
        });
        mnuXAxisLabels.add(rdoXAxisLabelsByClone);
        rdoXAxisLabelsByValue.setText("Value");
        grpXAxisLabels.add(rdoXAxisLabelsByValue);
        rdoXAxisLabelsByValue.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoXAxisLabelsByValueActionPerformed(evt);
            }
        });
        mnuXAxisLabels.add(rdoXAxisLabelsByValue);
        mnuView.add(mnuXAxisLabels);
        chkShowLegend.setText("Show Legend");
        chkShowLegend.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowLegendActionPerformed(evt);
            }
        });
        mnuView.add(chkShowLegend);
        chkSmoothUnconfirmed.setText("Smooth Unconfirmed");
        chkSmoothUnconfirmed.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSmoothUnconfirmedActionPerformed(evt);
            }
        });
        mnuView.add(chkSmoothUnconfirmed);
        btnPointSize.setText("Point Size");
        btnPointSize.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPointSizeActionPerformed(evt);
            }
        });
        mnuView.add(btnPointSize);
        btnDisplayRange.setText("Set Display Range");
        btnDisplayRange.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplayRangeActionPerformed(evt);
            }
        });
        mnuView.add(btnDisplayRange);
        menubar.add(mnuView);
        setJMenuBar(menubar);
        pack();
    }

    private void rdoBarActionPerformed(java.awt.event.ActionEvent evt) {
        chartPanel.setChartType(com.klg.jclass.chart.JCChart.BAR);
    }

    private void rdoAreaActionPerformed(java.awt.event.ActionEvent evt) {
        chartPanel.setChartType(com.klg.jclass.chart.JCChart.AREA);
    }

    private void rdoPlotActionPerformed(java.awt.event.ActionEvent evt) {
        chartPanel.setChartType(com.klg.jclass.chart.JCChart.PLOT);
    }

    private void rdoScatterPlotActionPerformed(java.awt.event.ActionEvent evt) {
        chartPanel.setChartType(com.klg.jclass.chart.JCChart.SCATTER_PLOT);
    }

    private void btnDisplayRangeActionPerformed(java.awt.event.ActionEvent evt) {
        SetChartRangeDialog crd = new SetChartRangeDialog(this, Float.parseFloat(chartPanel.getChart().getYAxisMin()), Float.parseFloat(chartPanel.getChart().getYAxisMax()));
        if (crd.showModal() == JOptionPane.OK_OPTION) {
            float min = crd.getMin();
            float max = crd.getMax();
            chartPanel.setChartDisplayRange(min, max);
        }
    }

    private void btnPointSizeActionPerformed(java.awt.event.ActionEvent evt) {
        String s = (String) JOptionPane.showInputDialog(null, "Enter Point Size", "Data Point Size", JOptionPane.PLAIN_MESSAGE);
        if (s == null || s.length() == 0) {
            return;
        }
        try {
            int val = Integer.parseInt(s);
            chartPanel.setPointSize(val);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Input Must Be An Integer", "Number Format Error", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void rdoXAxisLabelsByValueActionPerformed(java.awt.event.ActionEvent evt) {
        chartPanel.setXAxisLabelsByValue();
    }

    private void rdoXAxisLabelsByCloneActionPerformed(java.awt.event.ActionEvent evt) {
        chartPanel.setXAxisLabelsByClone();
    }

    private void rdoXAxisLabelsNoneActionPerformed(java.awt.event.ActionEvent evt) {
        chartPanel.setXAxisLabelsNone();
    }

    private void chkShowLegendActionPerformed(java.awt.event.ActionEvent evt) {
        chartPanel.setShowLegend(chkShowLegend.isSelected());
    }

    private void chkSmoothUnconfirmedActionPerformed(java.awt.event.ActionEvent evt) {
        chartPanel.smoothUnconfirmed(chkSmoothUnconfirmed.isSelected());
    }

    private void rdoXAxisByLinearOrderActionPerformed(java.awt.event.ActionEvent evt) {
        chartPanel.setXAxisPositionsByLinearOrder();
    }

    private void rdoXAxisByPositionActionPerformed(java.awt.event.ActionEvent evt) {
        chartPanel.setXAxisPositionsByChromLocation();
    }

    private void initCustomComponents() {
        setTitle("CGH Browser");
        org.tigr.microarray.mev.cgh.CGHGuiObj.GuiUtil.GuiUtil.center(this);
        pnlMain.add(chartPanel, java.awt.BorderLayout.CENTER);
        initChart();
    }

    private void initChart() {
        chartPanel.setShowLegend(chkShowLegend.isSelected());
        chartPanel.smoothUnconfirmed(chkSmoothUnconfirmed.isSelected());
        if (rdoXAxisByLinearOrder.isSelected()) {
            chartPanel.setXAxisPositionsByLinearOrder();
        } else {
            chartPanel.setXAxisPositionsByChromLocation();
        }
        if (rdoXAxisLabelsByValue.isSelected()) {
            chartPanel.setXAxisLabelsByValue();
        } else if (rdoXAxisLabelsByClone.isSelected()) {
            chartPanel.setXAxisLabelsByClone();
        } else if (rdoXAxisLabelsNone.isSelected()) {
            chartPanel.setXAxisLabelsNone();
        }
    }

    private void initMenus(int sampleIndex, int chromosomeIndex, int cloneValueType, boolean hasDyeSwap) {
        CGHBrowserMenubar bm = new CGHBrowserMenubar(this);
        menubar.add(bm.createCloneValuesMenu(browserModel.getCloneValueType(), hasDyeSwap, data.getDataType() == IData.DATA_TYPE_RATIO_ONLY, data.isLog2Data(), data.hasCloneDistribution()));
        ButtonGroup buttonGroup = new ButtonGroup();
        for (int exp = 0; exp < data.getFeaturesCount(); exp++) {
            if (exp == sampleIndex) {
                mnuExperiment.add(createJRadioButtonMenuItem(createBrowserSampleSelectedAction(exp), buttonGroup, true));
            } else {
                mnuExperiment.add(createJRadioButtonMenuItem(createBrowserSampleSelectedAction(exp), buttonGroup));
            }
        }
        Action action = new SampleSelectedAction("All Experiments");
        action.putValue("SelectionIndex", new Integer(CGHBrowserModelAdaptor.ALL_EXPERIMENTS));
        if (chromosomeIndex == CGHBrowserModelAdaptor.ALL_EXPERIMENTS) {
            mnuExperiment.add(createJRadioButtonMenuItem(action, buttonGroup, true));
        } else {
            mnuExperiment.add(createJRadioButtonMenuItem(action, buttonGroup));
        }
        buttonGroup = new ButtonGroup();
        for (int chrom = 0; chrom < data.getNumChromosomes(); chrom++) {
            if (chromosomeIndex == chrom) {
                mnuChromosome.add(createJRadioButtonMenuItem(createBrowserChromosomeSelectedAction(chrom), buttonGroup, true));
            } else {
                mnuChromosome.add(createJRadioButtonMenuItem(createBrowserChromosomeSelectedAction(chrom), buttonGroup));
            }
        }
        action = new ChromosomeSelectedAction("All Chromosomes");
        action.putValue("SelectionIndex", new Integer(CGHBrowserModelAdaptor.ALL_CHROMOSOMES));
        mnuChromosome.add(createJRadioButtonMenuItem(action, buttonGroup));
    }

    private Action createBrowserSampleSelectedAction(int index) {
        Action action = new SampleSelectedAction(data.getSampleName(index));
        action.putValue("SelectionIndex", new Integer(index));
        return action;
    }

    private Action createBrowserChromosomeSelectedAction(int index) {
        String name = CGHUtility.convertChromToLongString(index + 1, data.getCGHSpecies());
        Action action = new ChromosomeSelectedAction(name);
        action.putValue("SelectionIndex", new Integer(index));
        return action;
    }

    /**
     * Creates a menu item with specified name and acton command.
     */
    protected JMenuItem createJMenuItem(String name, String command, ActionListener listener) {
        JMenuItem item = new JMenuItem(name);
        item.setActionCommand(command);
        item.addActionListener(listener);
        return item;
    }

    /**
     * Creates a radio button menu item from specified action.
     */
    protected JMenuItem createJRadioButtonMenuItem(Action action, ButtonGroup buttonGroup) {
        return createJRadioButtonMenuItem(action, buttonGroup, false);
    }

    protected JMenuItem createJRadioButtonMenuItem(Action action, ButtonGroup buttonGroup, boolean isSelected) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(action);
        item.setSelected(isSelected);
        if (buttonGroup != null) {
            buttonGroup.add(item);
        }
        return item;
    }

    public void setSelectedRegion(ICGHDataRegion selectedRegion) {
        chartPanel.setSelectedRegion(selectedRegion);
    }

    public void setChartModel(CGHChartDataModel chartModel) {
        chartPanel.setChartModel(chartModel);
    }

    /** Getter for property tableModel.
     * @return Value of property tableModel.
     */
    public void setTableModel(CGHTableDataModel tableModel) {
        chartPanel.setTableModel(tableModel);
    }

    public void setCloneValueType(int cloneValueType) {
        JTable tblData = chartPanel.getTblData();
        int[] selectedRows = tblData.getSelectedRows();
        browserModel.setCloneValueType(cloneValueType);
        if (selectedRows.length > 0) {
            tblData.setRowSelectionInterval(selectedRows[0], selectedRows[selectedRows.length - 1]);
        }
    }

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {
        setVisible(false);
        dispose();
    }

    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        String command = actionEvent.getActionCommand();
        if (command.equals(CGHBrowserActionManager.CLONE_VALUES_DYE_SWAP)) {
            setCloneValueType(CGHBrowserModelAdaptor.CLONE_VALUES_DYE_SWAP);
        } else if (command.equals(CGHBrowserActionManager.CLONE_VALUES_LOG_AVERAGE_INVERTED)) {
            setCloneValueType(CGHBrowserModelAdaptor.CLONE_VALUES_LOG_AVERAGE_INVERTED);
        } else if (command.equals(CGHBrowserActionManager.CLONE_VALUES_LOG_DYE_SWAP)) {
            setCloneValueType(CGHBrowserModelAdaptor.CLONE_VALUES_LOG_DYE_SWAP);
        } else if (command.equals(CGHBrowserActionManager.CLONE_VALUES_P_VALUES)) {
            setCloneValueType(CGHBrowserModelAdaptor.CLONE_VALUES_P_SCORE);
        } else if (command.equals(CGHBrowserActionManager.CLONE_VALUES_LOG_RATIOS)) {
            setCloneValueType(CGHBrowserModelAdaptor.CLONE_VALUES_LOG_RATIOS);
        } else if (command.equals(CGHBrowserActionManager.CLONE_VALUES_RATIOS)) {
            setCloneValueType(CGHBrowserModelAdaptor.CLONE_VALUES_RATIOS);
        } else if (command.equals(CGHBrowserActionManager.CLONE_VALUES_P_VALUES)) {
            setCloneValueType(CGHBrowserModelAdaptor.CLONE_VALUES_P_SCORE);
        }
    }

    class SampleSelectedAction extends AbstractAction {

        public SampleSelectedAction(String name) {
            super(name);
        }

        public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            int sampleIndex = ((Integer) getValue("SelectionIndex")).intValue();
            chartPanel.setExperimentIndex(sampleIndex);
        }
    }

    class ChromosomeSelectedAction extends AbstractAction {

        public ChromosomeSelectedAction(String name) {
            super(name);
        }

        public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            int chromosomeIndex = ((Integer) getValue("SelectionIndex")).intValue();
            chartPanel.setChromosomeIndex(chromosomeIndex);
        }
    }

    private javax.swing.JMenu mnuChromosome;

    private javax.swing.JRadioButtonMenuItem rdoXAxisLabelsByClone;

    private javax.swing.ButtonGroup grpXAxisOrder;

    private javax.swing.JCheckBoxMenuItem chkShowLegend;

    private javax.swing.JRadioButtonMenuItem rdoArea;

    private javax.swing.JCheckBoxMenuItem chkSmoothUnconfirmed;

    private javax.swing.ButtonGroup grpChartTypes;

    private javax.swing.JMenuBar menubar;

    private javax.swing.JRadioButtonMenuItem rdoXAxisLabelsByValue;

    private javax.swing.JRadioButtonMenuItem rdoBar;

    private javax.swing.JPanel pnlMain;

    private javax.swing.JMenuItem btnPointSize;

    private javax.swing.JMenu mnuView;

    private javax.swing.JMenu mnuXAxisValues;

    private javax.swing.JMenuItem btnDisplayRange;

    private javax.swing.JRadioButtonMenuItem rdoXAxisLabelsNone;

    private javax.swing.ButtonGroup grpCloneValues;

    private javax.swing.ButtonGroup grpXAxisLabels;

    private javax.swing.JMenu mnuXAxisLabels;

    private javax.swing.JMenu mnuExperiment;

    private javax.swing.JRadioButtonMenuItem rdoScatterPlot;

    private javax.swing.JRadioButtonMenuItem rdoPlot;

    private javax.swing.JRadioButtonMenuItem rdoXAxisByPosition;

    private javax.swing.JRadioButtonMenuItem rdoXAxisByLinearOrder;

    private javax.swing.JMenu mnuChartType;
}
