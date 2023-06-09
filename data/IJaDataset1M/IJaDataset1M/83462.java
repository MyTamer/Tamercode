package gate.gui;

import gate.Controller;
import gate.Corpus;
import gate.CorpusController;
import gate.Gate;
import gate.LanguageResource;
import gate.ProcessingResource;
import gate.Resource;
import gate.creole.AbstractVisualResource;
import gate.creole.AnalyserRunningStrategy;
import gate.creole.ConditionalController;
import gate.creole.ConditionalSerialAnalyserController;
import gate.creole.ExecutionException;
import gate.creole.ExecutionInterruptedException;
import gate.creole.Parameter;
import gate.creole.ResourceData;
import gate.creole.ResourceInstantiationException;
import gate.creole.RunningStrategy;
import gate.creole.RunningStrategy.UnconditionalRunningStrategy;
import gate.creole.SerialAnalyserController;
import gate.creole.SerialController;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.GuiType;
import gate.event.ControllerEvent;
import gate.event.ControllerListener;
import gate.event.CreoleEvent;
import gate.event.CreoleListener;
import gate.event.ProgressListener;
import gate.event.StatusListener;
import gate.swing.XJPopupMenu;
import gate.swing.XJTable;
import gate.util.Benchmark;
import gate.util.Err;
import gate.util.GateException;
import gate.util.GateRuntimeException;
import gate.util.NameComparator;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

@CreoleResource(name = "Serial Application Editor", guiType = GuiType.LARGE, resourceDisplayed = "gate.creole.SerialController", mainViewer = true)
public class SerialControllerEditor extends AbstractVisualResource implements CreoleListener, ControllerListener, ActionsPublisher {

    public SerialControllerEditor() {
    }

    public void setTarget(Object target) {
        if (!(target instanceof SerialController)) throw new IllegalArgumentException("gate.gui.ApplicationViewer can only be used for serial controllers\n" + target.getClass().toString() + " is not a gate.creole.SerialController!");
        if (controller != null) controller.removeControllerListener(this);
        this.controller = (SerialController) target;
        controller.addControllerListener(this);
        corpusControllerMode = controller instanceof CorpusController;
        analyserMode = controller instanceof SerialAnalyserController || controller instanceof ConditionalSerialAnalyserController;
        conditionalMode = controller instanceof ConditionalController;
        initLocalData();
        initGuiComponents();
        initListeners();
        loadedPRsTableModel.fireTableDataChanged();
        memberPRsTableModel.fireTableDataChanged();
    }

    public void setHandle(Handle handle) {
        this.handle = handle;
        if (handle instanceof StatusListener) addStatusListener((StatusListener) handle);
        if (handle instanceof ProgressListener) addProgressListener((ProgressListener) handle);
    }

    public Resource init() throws ResourceInstantiationException {
        super.init();
        return this;
    }

    protected void initLocalData() {
        actionList = new ArrayList<Action>();
        runAction = new RunAction();
        actionList.add(null);
        actionList.add(runAction);
        addPRAction = new AddPRAction();
        removePRAction = new RemovePRAction();
    }

    protected void initGuiComponents() {
        setLayout(new BorderLayout());
        JPanel topSplit = new JPanel();
        topSplit.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridy = 0;
        loadedPRsTableModel = new LoadedPRsTableModel();
        loadedPRsTable = new XJTable();
        loadedPRsTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        loadedPRsTable.setSortable(false);
        loadedPRsTable.setModel(loadedPRsTableModel);
        loadedPRsTable.setDragEnabled(true);
        loadedPRsTable.setDefaultRenderer(ProcessingResource.class, new ResourceRenderer());
        loadedPRsTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {

            @Override
            public Dimension getMaximumSize() {
                Dimension dim = super.getMaximumSize();
                if (dim != null) {
                    dim.width = Integer.MAX_VALUE;
                    setMaximumSize(dim);
                }
                return dim;
            }

            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }
        });
        final int width1 = new JLabel("Loaded Processing resources").getPreferredSize().width + 30;
        JScrollPane scroller = new JScrollPane() {

            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.width = Math.max(dim.width, width1);
                return dim;
            }

            public Dimension getMinimumSize() {
                Dimension dim = super.getMinimumSize();
                dim.width = Math.max(dim.width, width1);
                return dim;
            }
        };
        scroller.getViewport().setView(loadedPRsTable);
        scroller.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), " Loaded Processing resources "));
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 5);
        topSplit.add(scroller, constraints);
        addButton = new JButton(addPRAction);
        addButton.setText("");
        addButton.setEnabled(false);
        removeButton = new JButton(removePRAction);
        removeButton.setText("");
        removeButton.setEnabled(false);
        Box buttonsBox = Box.createVerticalBox();
        buttonsBox.add(Box.createVerticalGlue());
        buttonsBox.add(addButton);
        buttonsBox.add(Box.createVerticalStrut(5));
        buttonsBox.add(removeButton);
        buttonsBox.add(Box.createVerticalGlue());
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = new Insets(0, 0, 0, 5);
        topSplit.add(buttonsBox, constraints);
        memberPRsTableModel = new MemberPRsTableModel();
        memberPRsTable = new XJTable();
        memberPRsTable.setSortable(false);
        memberPRsTable.setModel(memberPRsTableModel);
        memberPRsTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        memberPRsTable.setDefaultRenderer(ProcessingResource.class, new ResourceRenderer());
        memberPRsTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {

            @Override
            public Dimension getMaximumSize() {
                Dimension dim = super.getMaximumSize();
                if (dim != null) {
                    dim.width = Integer.MAX_VALUE;
                    setMaximumSize(dim);
                }
                return dim;
            }

            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }
        });
        memberPRsTable.setDefaultRenderer(Icon.class, new IconRenderer());
        memberPRsTable.setDragEnabled(true);
        final int width2 = new JLabel("Selected Processing resources").getPreferredSize().width + 30;
        scroller = new JScrollPane() {

            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.width = Math.max(dim.width, width2);
                return dim;
            }

            public Dimension getMinimumSize() {
                Dimension dim = super.getMinimumSize();
                dim.width = Math.max(dim.width, width2);
                return dim;
            }
        };
        scroller.getViewport().setView(memberPRsTable);
        scroller.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), " Selected Processing resources "));
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 5);
        topSplit.add(scroller, constraints);
        moveUpButton = new JButton(MainFrame.getIcon("up"));
        moveUpButton.setMnemonic(KeyEvent.VK_UP);
        moveUpButton.setToolTipText("Move the selected resources up.");
        moveUpButton.setEnabled(false);
        moveDownButton = new JButton(MainFrame.getIcon("down"));
        moveDownButton.setMnemonic(KeyEvent.VK_DOWN);
        moveDownButton.setToolTipText("Move the selected resources down.");
        moveDownButton.setEnabled(false);
        buttonsBox = Box.createVerticalBox();
        buttonsBox.add(Box.createVerticalGlue());
        buttonsBox.add(moveUpButton);
        buttonsBox.add(Box.createVerticalStrut(5));
        buttonsBox.add(moveDownButton);
        buttonsBox.add(Box.createVerticalGlue());
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = new Insets(0, 0, 0, 0);
        topSplit.add(buttonsBox, constraints);
        JPanel bottomSplit = new JPanel();
        bottomSplit.setLayout(new GridBagLayout());
        constraints.gridy = 0;
        if (conditionalMode) {
            strategyPanel = new JPanel();
            strategyPanel.setLayout(new BoxLayout(strategyPanel, BoxLayout.X_AXIS));
            strategyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            runBtnGrp = new ButtonGroup();
            yes_RunRBtn = new JRadioButton("Yes", true);
            yes_RunRBtn.setHorizontalTextPosition(AbstractButton.LEFT);
            runBtnGrp.add(yes_RunRBtn);
            no_RunRBtn = new JRadioButton("No", false);
            no_RunRBtn.setHorizontalTextPosition(AbstractButton.LEFT);
            runBtnGrp.add(no_RunRBtn);
            conditional_RunRBtn = new JRadioButton("If value of feature", false);
            conditional_RunRBtn.setHorizontalTextPosition(AbstractButton.LEFT);
            runBtnGrp.add(conditional_RunRBtn);
            featureNameTextField = new JTextField("", 25);
            featureNameTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, featureNameTextField.getPreferredSize().height));
            featureValueTextField = new JTextField("", 25);
            featureValueTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, featureValueTextField.getPreferredSize().height));
            strategyPanel.add(new JLabel(MainFrame.getIcon("greenBall")));
            strategyPanel.add(yes_RunRBtn);
            strategyPanel.add(Box.createHorizontalStrut(5));
            strategyPanel.add(new JLabel(MainFrame.getIcon("redBall")));
            strategyPanel.add(no_RunRBtn);
            strategyPanel.add(Box.createHorizontalStrut(5));
            strategyPanel.add(new JLabel(MainFrame.getIcon("yellowBall")));
            strategyPanel.add(conditional_RunRBtn);
            strategyPanel.add(Box.createHorizontalStrut(5));
            strategyPanel.add(featureNameTextField);
            strategyPanel.add(Box.createHorizontalStrut(5));
            strategyPanel.add(new JLabel("is"));
            strategyPanel.add(Box.createHorizontalStrut(5));
            strategyPanel.add(featureValueTextField);
            strategyPanel.add(Box.createHorizontalStrut(5));
            strategyBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), " No processing resource selected... ");
            strategyPanel.setBorder(strategyBorder);
            constraints.weightx = 1;
            constraints.weighty = 0;
            constraints.insets = new Insets(0, 0, 0, 0);
            bottomSplit.add(strategyPanel, constraints);
            constraints.gridy++;
        }
        if (corpusControllerMode) {
            corpusCombo = new JComboBox(corpusComboModel = new CorporaComboModel());
            corpusCombo.setRenderer(new ResourceRenderer());
            corpusCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, corpusCombo.getPreferredSize().height));
            Corpus corpus = null;
            if (controller instanceof CorpusController) {
                corpus = ((CorpusController) controller).getCorpus();
            } else {
                throw new GateRuntimeException("Controller editor in corpus " + "controller mode " + "but the target controller is not an " + "CorpusController!");
            }
            if (corpus != null) {
                corpusCombo.setSelectedItem(corpus);
            } else {
                if (corpusCombo.getModel().getSize() > 1) corpusCombo.setSelectedIndex(1); else corpusCombo.setSelectedIndex(0);
            }
            JPanel horBox = new JPanel();
            horBox.setLayout(new BoxLayout(horBox, BoxLayout.X_AXIS));
            horBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            horBox.add(new JLabel("Corpus:"));
            horBox.add(Box.createHorizontalStrut(5));
            horBox.add(corpusCombo);
            horBox.add(Box.createHorizontalStrut(5));
            constraints.weightx = 1;
            constraints.anchor = GridBagConstraints.WEST;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weighty = 0;
            constraints.insets = new Insets(0, 0, 0, 0);
            bottomSplit.add(horBox, constraints);
            constraints.gridwidth = 1;
            constraints.gridy++;
        }
        parametersPanel = new JPanel();
        parametersPanel.setLayout(new BoxLayout(parametersPanel, BoxLayout.Y_AXIS));
        parametersPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        parametersBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), " No selected processing resource ");
        parametersPanel.setBorder(parametersBorder);
        parametersEditor = new ResourceParametersEditor();
        parametersEditor.init(null, null);
        parametersPanel.add(new JScrollPane(parametersEditor));
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 0, 0, 0);
        bottomSplit.add(parametersPanel, constraints);
        constraints.gridy++;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        bottomSplit.add(new JButton(runAction), constraints);
        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topSplit, bottomSplit);
        splitPane.addAncestorListener(new AncestorListener() {

            public void ancestorRemoved(AncestorEvent event) {
            }

            public void ancestorMoved(AncestorEvent event) {
            }

            /**
       * One-shot ancestor listener that places the divider location on first
       * show, and then de-registers self. 
       */
            public void ancestorAdded(AncestorEvent event) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        splitPane.setDividerLocation(0.5);
                    }
                });
                splitPane.removeAncestorListener(this);
            }
        });
        add(splitPane, BorderLayout.CENTER);
    }

    protected void initListeners() {
        Gate.getCreoleRegister().addCreoleListener(this);
        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                processMouseEvent(e);
            }

            public void mouseReleased(MouseEvent e) {
                processMouseEvent(e);
            }

            protected void processMouseEvent(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    if (handle != null && handle.getPopup() != null) {
                        handle.getPopup().show(SerialControllerEditor.this, e.getX(), e.getY());
                    }
                }
            }
        });
        moveUpButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int rows[] = memberPRsTable.getSelectedRows();
                if (rows == null || rows.length == 0) {
                    JOptionPane.showMessageDialog(SerialControllerEditor.this, "Please select some components to be moved " + "from the list of used components!\n", "GATE", JOptionPane.ERROR_MESSAGE);
                } else {
                    Arrays.sort(rows);
                    for (int row : rows) {
                        if (row > 0) {
                            ProcessingResource value = controller.remove(row);
                            controller.add(row - 1, value);
                        }
                    }
                    for (int row : rows) {
                        int newRow;
                        if (row > 0) newRow = row - 1; else newRow = row;
                        memberPRsTable.addRowSelectionInterval(newRow, newRow);
                    }
                    memberPRsTable.requestFocusInWindow();
                }
            }
        });
        moveDownButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int rows[] = memberPRsTable.getSelectedRows();
                if (rows == null || rows.length == 0) {
                    JOptionPane.showMessageDialog(SerialControllerEditor.this, "Please select some components to be moved " + "from the list of used components!\n", "GATE", JOptionPane.ERROR_MESSAGE);
                } else {
                    Arrays.sort(rows);
                    for (int i = rows.length - 1; i >= 0; i--) {
                        int row = rows[i];
                        if (row < controller.getPRs().size() - 1) {
                            ProcessingResource value = controller.remove(row);
                            controller.add(row + 1, value);
                        }
                    }
                    for (int row : rows) {
                        int newRow;
                        if (row < controller.getPRs().size() - 1) newRow = row + 1; else newRow = row;
                        memberPRsTable.addRowSelectionInterval(newRow, newRow);
                    }
                    memberPRsTable.requestFocusInWindow();
                }
            }
        });
        loadedPRsTable.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    processMouseEvent(e);
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    processMouseEvent(e);
                }
            }

            public void mouseClicked(MouseEvent e) {
                processMouseEvent(e);
            }

            protected void processMouseEvent(MouseEvent e) {
                int row = loadedPRsTable.rowAtPoint(e.getPoint());
                if (row == -1) {
                    return;
                }
                ProcessingResource pr = (ProcessingResource) loadedPRsTable.getValueAt(row, loadedPRsTable.convertColumnIndexToView(0));
                if (e.isPopupTrigger()) {
                    if (!loadedPRsTable.isRowSelected(row)) {
                        loadedPRsTable.getSelectionModel().setSelectionInterval(row, row);
                    }
                    JPopupMenu popup = new XJPopupMenu();
                    popup.add(addPRAction);
                    popup.show(loadedPRsTable, e.getPoint().x, e.getPoint().y);
                } else if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                    if (e.getClickCount() == 2) {
                        addPRAction.actionPerformed(null);
                    }
                }
            }
        });
        loadedPRsTable.setTransferHandler(new TransferHandler() {

            String source = "";

            public int getSourceActions(JComponent c) {
                return MOVE;
            }

            protected Transferable createTransferable(JComponent c) {
                return new StringSelection("loadedPRsTable");
            }

            protected void exportDone(JComponent c, Transferable data, int action) {
            }

            public boolean canImport(JComponent c, DataFlavor[] flavors) {
                for (DataFlavor flavor : flavors) {
                    if (DataFlavor.stringFlavor.equals(flavor)) {
                        return true;
                    }
                }
                return false;
            }

            public boolean importData(JComponent c, Transferable t) {
                if (canImport(c, t.getTransferDataFlavors())) {
                    try {
                        source = (String) t.getTransferData(DataFlavor.stringFlavor);
                        if (source.startsWith("memberPRsTable")) {
                            removePRAction.actionPerformed(null);
                            return true;
                        } else {
                            return false;
                        }
                    } catch (UnsupportedFlavorException ufe) {
                    } catch (IOException ioe) {
                    }
                }
                return false;
            }
        });
        memberPRsTable.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    processMouseEvent(e);
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    processMouseEvent(e);
                }
            }

            public void mouseClicked(MouseEvent e) {
                processMouseEvent(e);
            }

            protected void processMouseEvent(MouseEvent e) {
                int row = memberPRsTable.rowAtPoint(e.getPoint());
                if (row == -1) {
                    return;
                }
                if (e.isPopupTrigger()) {
                    if (!memberPRsTable.isRowSelected(row)) {
                        memberPRsTable.getSelectionModel().setSelectionInterval(row, row);
                    }
                    JPopupMenu popup = new XJPopupMenu();
                    popup.add(removePRAction);
                    popup.show(memberPRsTable, e.getPoint().x, e.getPoint().y);
                } else if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                    if (e.getClickCount() == 2) {
                        removePRAction.actionPerformed(null);
                    }
                }
            }
        });
        memberPRsTable.setTransferHandler(new TransferHandler() {

            String source = "";

            public int getSourceActions(JComponent c) {
                return MOVE;
            }

            protected Transferable createTransferable(JComponent c) {
                int selectedRows[] = memberPRsTable.getSelectedRows();
                Arrays.sort(selectedRows);
                return new StringSelection("memberPRsTable" + Arrays.toString(selectedRows));
            }

            protected void exportDone(JComponent c, Transferable data, int action) {
            }

            public boolean canImport(JComponent c, DataFlavor[] flavors) {
                for (DataFlavor flavor : flavors) {
                    if (DataFlavor.stringFlavor.equals(flavor)) {
                        return true;
                    }
                }
                return false;
            }

            public boolean importData(JComponent c, Transferable t) {
                if (!canImport(c, t.getTransferDataFlavors())) {
                    return false;
                }
                try {
                    source = (String) t.getTransferData(DataFlavor.stringFlavor);
                    if (source.startsWith("memberPRsTable")) {
                        int insertion = memberPRsTable.getSelectedRow();
                        int initialInsertion = insertion;
                        List<ProcessingResource> prs = new ArrayList<ProcessingResource>();
                        source = source.replaceFirst("^memberPRsTable\\[", "");
                        source = source.replaceFirst("\\]$", "");
                        String selectedRows[] = source.split(", ");
                        if (Integer.valueOf(selectedRows[0]) < insertion) {
                            insertion++;
                        }
                        for (String row : selectedRows) {
                            if (Integer.valueOf(row) == initialInsertion) {
                                return false;
                            }
                            prs.add((ProcessingResource) memberPRsTable.getValueAt(Integer.valueOf(row), memberPRsTable.convertColumnIndexToView(1)));
                            if (Integer.valueOf(row) < initialInsertion) {
                                insertion--;
                            }
                        }
                        for (ProcessingResource pr : prs) {
                            controller.remove(pr);
                        }
                        for (ProcessingResource pr : prs) {
                            controller.add(insertion, pr);
                            insertion++;
                        }
                        memberPRsTable.addRowSelectionInterval(insertion - selectedRows.length, insertion - 1);
                        return true;
                    } else if (source.equals("loadedPRsTable")) {
                        addPRAction.actionPerformed(null);
                        return true;
                    } else {
                        return false;
                    }
                } catch (UnsupportedFlavorException ufe) {
                    return false;
                } catch (IOException ioe) {
                    return false;
                }
            }
        });
        loadedPRsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                addButton.setEnabled(loadedPRsTable.getSelectedRowCount() > 0);
            }
        });
        memberPRsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                removeButton.setEnabled(memberPRsTable.getSelectedRowCount() > 0);
                moveUpButton.setEnabled(memberPRsTable.getSelectedRowCount() > 0 && !memberPRsTable.isRowSelected(0));
                moveDownButton.setEnabled(memberPRsTable.getSelectedRowCount() > 0 && !memberPRsTable.isRowSelected(memberPRsTable.getRowCount() - 1));
                if (memberPRsTable.getSelectedRowCount() == 1) {
                    selectPR(memberPRsTable.getSelectedRow());
                } else {
                    selectPR(-1);
                }
            }
        });
        if (conditionalMode) {
            ItemListener strategyModeListener = new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    if (selectedPRRunStrategy != null) {
                        if (selectedPRRunStrategy instanceof AnalyserRunningStrategy) {
                            AnalyserRunningStrategy strategy = (AnalyserRunningStrategy) selectedPRRunStrategy;
                            if (yes_RunRBtn.isSelected()) {
                                strategy.setRunMode(RunningStrategy.RUN_ALWAYS);
                            } else if (no_RunRBtn.isSelected()) {
                                strategy.setRunMode(RunningStrategy.RUN_NEVER);
                            } else if (conditional_RunRBtn.isSelected()) {
                                strategy.setRunMode(RunningStrategy.RUN_CONDITIONAL);
                            }
                        } else if (selectedPRRunStrategy instanceof UnconditionalRunningStrategy) {
                            UnconditionalRunningStrategy strategy = (UnconditionalRunningStrategy) selectedPRRunStrategy;
                            if (yes_RunRBtn.isSelected()) {
                                strategy.shouldRun(true);
                            } else if (no_RunRBtn.isSelected()) {
                                strategy.shouldRun(false);
                            }
                        }
                    }
                    memberPRsTable.repaint();
                }
            };
            yes_RunRBtn.addItemListener(strategyModeListener);
            no_RunRBtn.addItemListener(strategyModeListener);
            conditional_RunRBtn.addItemListener(strategyModeListener);
            featureNameTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    changeOccured(e);
                }

                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    changeOccured(e);
                }

                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    changeOccured(e);
                }

                protected void changeOccured(javax.swing.event.DocumentEvent e) {
                    if (selectedPRRunStrategy != null && selectedPRRunStrategy instanceof AnalyserRunningStrategy) {
                        AnalyserRunningStrategy strategy = (AnalyserRunningStrategy) selectedPRRunStrategy;
                        strategy.setFeatureName(featureNameTextField.getText());
                    }
                    conditional_RunRBtn.setSelected(true);
                }
            });
            featureValueTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    changeOccured(e);
                }

                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    changeOccured(e);
                }

                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    changeOccured(e);
                }

                protected void changeOccured(javax.swing.event.DocumentEvent e) {
                    if (selectedPRRunStrategy != null && selectedPRRunStrategy instanceof AnalyserRunningStrategy) {
                        AnalyserRunningStrategy strategy = (AnalyserRunningStrategy) selectedPRRunStrategy;
                        strategy.setFeatureValue(featureValueTextField.getText());
                    }
                    conditional_RunRBtn.setSelected(true);
                }
            });
        }
        if (corpusControllerMode) {
            corpusCombo.addPopupMenuListener(new PopupMenuListener() {

                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    corpusComboModel.fireDataChanged();
                }

                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                }

                public void popupMenuCanceled(PopupMenuEvent e) {
                }
            });
        }
        addAncestorListener(new AncestorListener() {

            public void ancestorAdded(AncestorEvent event) {
                loadedPRsTableModel.fireTableDataChanged();
                memberPRsTableModel.fireTableDataChanged();
            }

            public void ancestorRemoved(AncestorEvent event) {
            }

            public void ancestorMoved(AncestorEvent event) {
            }
        });
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("F3"), "Run");
        getActionMap().put("Run", runAction);
    }

    public List getActions() {
        return actionList;
    }

    /**
   * Cleans the internal data and prepares this object to be collected
   */
    public void cleanup() {
        Gate.getCreoleRegister().removeCreoleListener(this);
        controller.removeControllerListener(this);
        controller = null;
        progressListeners.clear();
        statusListeners.clear();
        parametersEditor.cleanup();
        handle = null;
    }

    /**
   * Called when a PR has been selected in the member PRs table;
   * @param index row index in {@link #memberPRsTable} (or -1 if no PR is
   * currently selected)
   */
    protected void selectPR(int index) {
        if (parametersEditor.getResource() != null) {
            try {
                parametersEditor.setParameters();
            } catch (ResourceInstantiationException rie) {
                JOptionPane.showMessageDialog(SerialControllerEditor.this, "Failed to set parameters for \"" + parametersEditor.getResource().getName() + "\"!\n", "GATE", JOptionPane.ERROR_MESSAGE);
                rie.printStackTrace(Err.getPrintWriter());
            }
            if (conditionalMode) {
                if (selectedPRRunStrategy != null && selectedPRRunStrategy instanceof AnalyserRunningStrategy) {
                    AnalyserRunningStrategy strategy = (AnalyserRunningStrategy) selectedPRRunStrategy;
                    strategy.setFeatureName(featureNameTextField.getText());
                    strategy.setFeatureValue(featureValueTextField.getText());
                }
                selectedPRRunStrategy = null;
            }
        }
        ProcessingResource pr = null;
        if (index >= 0 && index < controller.getPRs().size()) {
            pr = (ProcessingResource) ((java.util.List) controller.getPRs()).get(index);
        }
        if (pr != null) {
            ResourceData rData = (ResourceData) Gate.getCreoleRegister().get(pr.getClass().getName());
            parametersBorder.setTitle(" Runtime Parameters for the \"" + pr.getName() + "\" " + rData.getName() + ": ");
            List<List<Parameter>> parameters = rData.getParameterList().getRuntimeParameters();
            if (corpusControllerMode) {
                List<List<Parameter>> oldParameters = parameters;
                parameters = new ArrayList<List<Parameter>>();
                for (List<Parameter> aDisjunction : oldParameters) {
                    List<Parameter> newDisjunction = new ArrayList<Parameter>();
                    for (Parameter parameter : aDisjunction) {
                        if (!parameter.getName().equals("corpus") && !parameter.getName().equals("document")) {
                            newDisjunction.add(parameter);
                        }
                    }
                    if (!newDisjunction.isEmpty()) parameters.add(newDisjunction);
                }
            }
            parametersEditor.init(pr, parameters);
            if (conditionalMode) {
                strategyBorder.setTitle(" Run \"" + pr.getName() + "\"? ");
                yes_RunRBtn.setEnabled(true);
                no_RunRBtn.setEnabled(true);
                conditional_RunRBtn.setEnabled(true);
                selectedPRRunStrategy = null;
                RunningStrategy newStrategy = (RunningStrategy) ((List) ((ConditionalController) controller).getRunningStrategies()).get(index);
                if (newStrategy instanceof AnalyserRunningStrategy) {
                    featureNameTextField.setEnabled(true);
                    featureValueTextField.setEnabled(true);
                    conditional_RunRBtn.setEnabled(true);
                    featureNameTextField.setText(((AnalyserRunningStrategy) newStrategy).getFeatureName());
                    featureValueTextField.setText(((AnalyserRunningStrategy) newStrategy).getFeatureValue());
                } else {
                    featureNameTextField.setEnabled(false);
                    featureValueTextField.setEnabled(false);
                    conditional_RunRBtn.setEnabled(false);
                }
                switch(newStrategy.getRunMode()) {
                    case RunningStrategy.RUN_ALWAYS:
                        {
                            yes_RunRBtn.setSelected(true);
                            break;
                        }
                    case RunningStrategy.RUN_NEVER:
                        {
                            no_RunRBtn.setSelected(true);
                            break;
                        }
                    case RunningStrategy.RUN_CONDITIONAL:
                        {
                            conditional_RunRBtn.setSelected(true);
                            break;
                        }
                }
                selectedPRRunStrategy = newStrategy;
            }
        } else {
            parametersBorder.setTitle(" No processing resource selected... ");
            parametersEditor.init(null, null);
            if (conditionalMode) {
                strategyBorder.setTitle(" No processing resource selected... ");
                yes_RunRBtn.setEnabled(false);
                no_RunRBtn.setEnabled(false);
                conditional_RunRBtn.setEnabled(false);
                featureNameTextField.setText("");
                featureNameTextField.setEnabled(false);
                featureValueTextField.setText("");
                featureValueTextField.setEnabled(false);
            }
        }
        SerialControllerEditor.this.repaint(100);
    }

    public void resourceLoaded(CreoleEvent e) {
        if (Gate.getHiddenAttribute(e.getResource().getFeatures())) return;
        if (e.getResource() instanceof ProcessingResource) {
            loadedPRsTableModel.fireTableDataChanged();
            memberPRsTableModel.fireTableDataChanged();
        } else if (e.getResource() instanceof LanguageResource) {
            if (e.getResource() instanceof Corpus && corpusControllerMode) {
                corpusComboModel.fireDataChanged();
            }
        }
    }

    public void resourceUnloaded(CreoleEvent e) {
        if (Gate.getHiddenAttribute(e.getResource().getFeatures())) return;
        if (e.getResource() instanceof ProcessingResource) {
            ProcessingResource pr = (ProcessingResource) e.getResource();
            if (controller != null && controller.getPRs().contains(pr)) {
                controller.remove(pr);
            }
            loadedPRsTableModel.fireTableDataChanged();
            memberPRsTableModel.fireTableDataChanged();
        } else if (e.getResource() instanceof LanguageResource) {
            if (e.getResource() instanceof Corpus && corpusControllerMode) {
                Corpus c = (Corpus) e.getResource();
                if (controller instanceof CorpusController) {
                    if (c == ((CorpusController) controller).getCorpus()) {
                        ((CorpusController) controller).setCorpus(null);
                    }
                } else {
                    throw new GateRuntimeException("Controller editor in analyser mode " + "but the target controller is not an " + "analyser!");
                }
                corpusComboModel.fireDataChanged();
            }
        }
    }

    public void resourceRenamed(Resource resource, String oldName, String newName) {
        if (Gate.getHiddenAttribute(resource.getFeatures())) return;
        if (resource instanceof ProcessingResource) {
            repaint(100);
        }
    }

    public void datastoreOpened(CreoleEvent e) {
    }

    public void datastoreCreated(CreoleEvent e) {
    }

    public void datastoreClosed(CreoleEvent e) {
    }

    public synchronized void removeStatusListener(StatusListener l) {
        if (statusListeners != null && statusListeners.contains(l)) {
            Vector<StatusListener> v = (Vector) statusListeners.clone();
            v.removeElement(l);
            statusListeners = v;
        }
    }

    public void resourceAdded(ControllerEvent evt) {
        loadedPRsTableModel.fireTableDataChanged();
        memberPRsTableModel.fireTableDataChanged();
    }

    public void resourceRemoved(ControllerEvent evt) {
        loadedPRsTableModel.fireTableDataChanged();
        memberPRsTableModel.fireTableDataChanged();
    }

    public synchronized void addStatusListener(StatusListener l) {
        Vector<StatusListener> v = statusListeners == null ? new Vector(2) : (Vector) statusListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            statusListeners = v;
        }
    }

    private boolean firstIncludesOrEqualsSecond(Controller first, Controller second) {
        if (first.equals(second)) {
            return true;
        } else {
            for (Object object : first.getPRs()) {
                if (object instanceof Controller) {
                    if (firstIncludesOrEqualsSecond((Controller) object, second)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
   * Table model for all the loaded processing resources that are not part of
   * the controller.
   */
    class LoadedPRsTableModel extends AbstractTableModel {

        public int getRowCount() {
            List<ProcessingResource> loadedPRs = new ArrayList<ProcessingResource>(Gate.getCreoleRegister().getPrInstances());
            if (controller != null) {
                loadedPRs.removeAll(controller.getPRs());
            }
            Iterator prsIter = loadedPRs.iterator();
            while (prsIter.hasNext()) {
                ProcessingResource aPR = (ProcessingResource) prsIter.next();
                if (Gate.getHiddenAttribute(aPR.getFeatures()) || (aPR instanceof Controller && firstIncludesOrEqualsSecond((Controller) aPR, controller))) {
                    prsIter.remove();
                }
            }
            return loadedPRs.size();
        }

        public Object getValueAt(int row, int column) {
            List<ProcessingResource> loadedPRs = new ArrayList<ProcessingResource>(Gate.getCreoleRegister().getPrInstances());
            if (controller != null) {
                loadedPRs.removeAll(controller.getPRs());
            }
            Iterator prsIter = loadedPRs.iterator();
            while (prsIter.hasNext()) {
                ProcessingResource aPR = (ProcessingResource) prsIter.next();
                if (Gate.getHiddenAttribute(aPR.getFeatures()) || (aPR instanceof Controller && firstIncludesOrEqualsSecond((Controller) aPR, controller))) {
                    prsIter.remove();
                }
            }
            Collections.sort(loadedPRs, nameComparator);
            ProcessingResource pr = loadedPRs.get(row);
            switch(column) {
                case 0:
                    return pr;
                case 1:
                    {
                        ResourceData rData = Gate.getCreoleRegister().get(pr.getClass().getName());
                        if (rData == null) return pr.getClass(); else return rData.getName();
                    }
                default:
                    return null;
            }
        }

        public int getColumnCount() {
            return 2;
        }

        public String getColumnName(int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return "Name";
                case 1:
                    return "Type";
                default:
                    return "?";
            }
        }

        public Class getColumnClass(int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return ProcessingResource.class;
                case 1:
                    return String.class;
                default:
                    return Object.class;
            }
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        }

        NameComparator nameComparator = new NameComparator();
    }

    /**
   * A model for a combobox containing the loaded corpora in the system
   */
    protected class CorporaComboModel extends AbstractListModel implements ComboBoxModel {

        public int getSize() {
            java.util.List loadedCorpora = null;
            try {
                loadedCorpora = Gate.getCreoleRegister().getAllInstances("gate.Corpus");
            } catch (GateException ge) {
                ge.printStackTrace(Err.getPrintWriter());
            }
            return loadedCorpora == null ? 1 : loadedCorpora.size() + 1;
        }

        public Object getElementAt(int index) {
            if (index == 0) return "<none>"; else {
                java.util.List loadedCorpora = null;
                try {
                    loadedCorpora = Gate.getCreoleRegister().getAllInstances("gate.Corpus");
                } catch (GateException ge) {
                    ge.printStackTrace(Err.getPrintWriter());
                }
                return loadedCorpora == null ? "" : loadedCorpora.get(index - 1);
            }
        }

        public void setSelectedItem(Object anItem) {
            if (controller instanceof CorpusController) ((CorpusController) controller).setCorpus((Corpus) (anItem.equals("<none>") ? null : anItem));
        }

        public Object getSelectedItem() {
            Corpus corpus = null;
            if (controller instanceof CorpusController) {
                corpus = ((CorpusController) controller).getCorpus();
            } else {
                throw new GateRuntimeException("Controller editor in corpus " + "controller mode " + "but the target controller is not a " + "CorpusController!");
            }
            return (corpus == null ? (Object) "<none>" : (Object) corpus);
        }

        void fireDataChanged() {
            fireContentsChanged(this, 0, getSize());
        }
    }

    /**
   *  Renders JLabel by simply displaying them
   */
    class IconRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
            setIcon((Icon) value);
            return this;
        }

        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }
    }

    /**
   * Table model for all the processing resources in the controller.
   */
    class MemberPRsTableModel extends AbstractTableModel {

        public MemberPRsTableModel() {
            green = MainFrame.getIcon("greenBall");
            red = MainFrame.getIcon("redBall");
            yellow = MainFrame.getIcon("yellowBall");
        }

        public int getRowCount() {
            return controller == null ? 0 : controller.getPRs().size();
        }

        public Object getValueAt(int row, int column) {
            ProcessingResource pr = (ProcessingResource) ((List) controller.getPRs()).get(row);
            switch(column) {
                case 0:
                    {
                        if (conditionalMode) {
                            RunningStrategy strategy = (RunningStrategy) ((List) ((ConditionalController) controller).getRunningStrategies()).get(row);
                            switch(strategy.getRunMode()) {
                                case RunningStrategy.RUN_ALWAYS:
                                    return green;
                                case RunningStrategy.RUN_NEVER:
                                    return red;
                                case RunningStrategy.RUN_CONDITIONAL:
                                    return yellow;
                            }
                        }
                        return green;
                    }
                case 1:
                    return pr;
                case 2:
                    {
                        ResourceData rData = (ResourceData) Gate.getCreoleRegister().get(pr.getClass().getName());
                        if (rData == null) return pr.getClass(); else return rData.getName();
                    }
                default:
                    return null;
            }
        }

        public int getColumnCount() {
            return 3;
        }

        public String getColumnName(int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return "!";
                case 1:
                    return "Name";
                case 2:
                    return "Type";
                default:
                    return "?";
            }
        }

        public Class getColumnClass(int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return Icon.class;
                case 1:
                    return ProcessingResource.class;
                case 2:
                    return String.class;
                default:
                    return Object.class;
            }
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        }

        protected Icon green, red, yellow;
    }

    /** Adds a PR to the controller*/
    class AddPRAction extends AbstractAction {

        AddPRAction() {
            putValue(NAME, "Add selected resources to the application");
            putValue(SHORT_DESCRIPTION, "Add selected resources to the application");
            putValue(SMALL_ICON, MainFrame.getIcon("right-arrow"));
            putValue(MNEMONIC_KEY, KeyEvent.VK_RIGHT);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                List<ProcessingResource> prs = new ArrayList<ProcessingResource>();
                int selectedRows[] = loadedPRsTable.getSelectedRows();
                Arrays.sort(selectedRows);
                for (int row : selectedRows) {
                    prs.add((ProcessingResource) loadedPRsTable.getValueAt(row, loadedPRsTable.convertColumnIndexToView(0)));
                }
                selectedRows = memberPRsTable.getSelectedRows();
                Arrays.sort(selectedRows);
                int insertion = selectedRows.length == 0 ? memberPRsTable.getRowCount() : selectedRows[selectedRows.length - 1] + 1;
                for (ProcessingResource pr : prs) {
                    controller.add(insertion, pr);
                    insertion++;
                }
                for (ProcessingResource pr : prs) {
                    for (int row = 0; row < memberPRsTable.getRowCount(); row++) {
                        if (memberPRsTable.getValueAt(row, memberPRsTable.convertColumnIndexToView(1)) == pr) {
                            memberPRsTable.addRowSelectionInterval(row, row);
                        }
                    }
                }
                memberPRsTable.requestFocusInWindow();
            } catch (GateRuntimeException ex) {
                JOptionPane.showMessageDialog(SerialControllerEditor.this, "Could not add PR to pipeline:\n" + ex.getMessage(), "GATE", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    /** Removes a PR from the controller*/
    class RemovePRAction extends AbstractAction {

        RemovePRAction() {
            putValue(NAME, "Remove selected resouces from the application");
            putValue(SHORT_DESCRIPTION, "Remove selected resouces from the application");
            putValue(SMALL_ICON, MainFrame.getIcon("left-arrow"));
            putValue(MNEMONIC_KEY, KeyEvent.VK_LEFT);
        }

        public void actionPerformed(ActionEvent e) {
            List<ProcessingResource> prs = new ArrayList<ProcessingResource>();
            for (int row : memberPRsTable.getSelectedRows()) {
                prs.add((ProcessingResource) memberPRsTable.getValueAt(row, memberPRsTable.convertColumnIndexToView(1)));
            }
            for (ProcessingResource pr : prs) {
                controller.remove(pr);
            }
            for (ProcessingResource pr : prs) {
                for (int row = 0; row < loadedPRsTable.getRowCount(); row++) {
                    if (loadedPRsTable.getValueAt(row, loadedPRsTable.convertColumnIndexToView(0)) == pr) {
                        loadedPRsTable.addRowSelectionInterval(row, row);
                    }
                }
            }
            loadedPRsTable.requestFocusInWindow();
            if (memberPRsTable.getSelectedRowCount() == 0) {
                parametersEditor.init(null, null);
                parametersBorder.setTitle("No selected processing resource");
            }
        }
    }

    /** Runs the Application*/
    class RunAction extends AbstractAction {

        RunAction() {
            super("Run this Application");
            super.putValue(SHORT_DESCRIPTION, "<html>Run this application" + "&nbsp;&nbsp;<font color=#667799><small>F3" + "&nbsp;&nbsp;</small></font></html>");
        }

        public void actionPerformed(ActionEvent e) {
            Runnable runnable = new Runnable() {

                public void run() {
                    if (memberPRsTable.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(SerialControllerEditor.this, "Add at least one processing resource in the right table\n" + "that contains the resources of the application to be run.", "GATE", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        parametersEditor.setParameters();
                    } catch (ResourceInstantiationException rie) {
                        JOptionPane.showMessageDialog(SerialControllerEditor.this, "Could not set parameters for the \"" + parametersEditor.getResource().getName() + "\" processing resource:\nSee \"Messages\" tab for details!", "GATE", JOptionPane.ERROR_MESSAGE);
                        rie.printStackTrace(Err.getPrintWriter());
                        return;
                    }
                    if (corpusControllerMode) {
                        Object value = corpusCombo.getSelectedItem();
                        Corpus corpus = value.equals("<none>") ? null : (Corpus) value;
                        if (analyserMode && corpus == null) {
                            JOptionPane.showMessageDialog(SerialControllerEditor.this, "No corpus provided!\n" + "Please select a corpus and try again!", "GATE", JOptionPane.ERROR_MESSAGE);
                            corpusCombo.requestFocusInWindow();
                            return;
                        }
                        if (controller instanceof CorpusController) ((CorpusController) controller).setCorpus(corpus);
                    }
                    List badPRs;
                    try {
                        badPRs = controller.getOffendingPocessingResources();
                    } catch (ResourceInstantiationException rie) {
                        JOptionPane.showMessageDialog(SerialControllerEditor.this, "Could not check runtime parameters for " + "the processing resources:\n" + rie.toString(), "GATE", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (badPRs != null && !badPRs.isEmpty()) {
                        String badPRsString = "";
                        for (Object badPR : badPRs) {
                            badPRsString += "- " + ((ProcessingResource) badPR).getName() + "\n";
                        }
                        JOptionPane.showMessageDialog(SerialControllerEditor.this, "Some required runtime parameters are not set\n" + "in the following resources:\n" + badPRsString, "GATE", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    StatusListener sListener = new InternalStatusListener();
                    ProgressListener pListener = new InternalProgressListener();
                    controller.addStatusListener(sListener);
                    controller.addProgressListener(pListener);
                    Gate.setExecutable(controller);
                    int corpusSize = 1;
                    if (controller instanceof CorpusController) {
                        Corpus corpus = ((CorpusController) controller).getCorpus();
                        if (corpus != null) {
                            corpusSize = corpus.size();
                        }
                    }
                    MainFrame.lockGUI("Running " + controller.getName() + " on " + corpusSize + (corpusSize == 1 ? " document" : " documents"));
                    long startTime = System.currentTimeMillis();
                    fireStatusChanged("Start running " + controller.getName() + " on " + corpusSize + (corpusSize == 1 ? " document" : " documents"));
                    fireProgressChanged(0);
                    try {
                        Benchmark.executeWithBenchmarking(controller, Benchmark.createBenchmarkId(controller.getName(), null), RunAction.this, null);
                    } catch (ExecutionInterruptedException eie) {
                        MainFrame.unlockGUI();
                        JOptionPane.showMessageDialog(SerialControllerEditor.this, "Interrupted!\n" + eie.toString(), "GATE", JOptionPane.ERROR_MESSAGE);
                    } catch (ExecutionException ee) {
                        ee.printStackTrace(Err.getPrintWriter());
                        MainFrame.unlockGUI();
                        JOptionPane.showMessageDialog(SerialControllerEditor.this, "Execution error while running \"" + controller.getName() + "\" :\nSee \"Messages\" tab for details!", "GATE", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        MainFrame.unlockGUI();
                        JOptionPane.showMessageDialog(SerialControllerEditor.this, "Unhandled execution error!\n " + "See \"Messages\" tab for details!", "GATE", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace(Err.getPrintWriter());
                    } finally {
                        MainFrame.unlockGUI();
                        Gate.setExecutable(null);
                    }
                    controller.removeStatusListener(sListener);
                    controller.removeProgressListener(pListener);
                    long endTime = System.currentTimeMillis();
                    fireProcessFinished();
                    fireStatusChanged(controller.getName() + " run in " + NumberFormat.getInstance().format((double) (endTime - startTime) / 1000) + " seconds");
                }
            };
            Thread thread = new Thread(Thread.currentThread().getThreadGroup(), runnable, "ApplicationViewer1");
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }
    }

    /**
   * A simple progress listener used to forward the events upstream.
   */
    protected class InternalProgressListener implements ProgressListener {

        public void progressChanged(int i) {
            fireProgressChanged(i);
        }

        public void processFinished() {
            fireProcessFinished();
        }
    }

    /**
   * A simple status listener used to forward the events upstream.
   */
    protected class InternalStatusListener implements StatusListener {

        public void statusChanged(String message) {
            fireStatusChanged(message);
        }
    }

    /** The controller this editor edits */
    protected SerialController controller;

    /**
   * The list of actions provided by this editor
   */
    protected List<Action> actionList;

    /**
   * Contains all the PRs loaded in the sytem that are not already part of the
   * serial controller
   */
    protected XJTable loadedPRsTable;

    /**
   * model for the {@link #loadedPRsTable} JTable.
   */
    protected LoadedPRsTableModel loadedPRsTableModel;

    /**
   * Displays the PRs in the controller
   */
    protected XJTable memberPRsTable;

    /** model for {@link #memberPRsTable}*/
    protected MemberPRsTableModel memberPRsTableModel;

    /** Adds one or more PR(s) to the controller*/
    protected JButton addButton;

    /** Removes one or more PR(s) from the controller*/
    protected JButton removeButton;

    /** Moves the module up in the controller list*/
    protected JButton moveUpButton;

    /** Moves the module down in the controller list*/
    protected JButton moveDownButton;

    /** A component for editing the parameters of the currently selected PR*/
    protected ResourceParametersEditor parametersEditor;

    /** A JPanel containing the {@link #parametersEditor}*/
    protected JPanel parametersPanel;

    /** A border for the {@link #parametersPanel} */
    protected TitledBorder parametersBorder;

    /** A JPanel containing the running strategy options*/
    protected JPanel strategyPanel;

    /** A border for the running strategy options box */
    protected TitledBorder strategyBorder;

    /**
   * Button for run always.
   */
    protected JRadioButton yes_RunRBtn;

    /**
   * Button for never run.
   */
    protected JRadioButton no_RunRBtn;

    /**
   * Button for conditional run.
   */
    protected JRadioButton conditional_RunRBtn;

    /**
   * The group for run strategy buttons;
   */
    protected ButtonGroup runBtnGrp;

    /**
   * Text field for the feature name for conditional run.
   */
    protected JTextField featureNameTextField;

    /**
   * Text field for the feature value for conditional run.
   */
    protected JTextField featureValueTextField;

    /**
   * A combobox that allows selection of a corpus from the list of loaded
   * corpora.
   */
    protected JComboBox corpusCombo;

    protected CorporaComboModel corpusComboModel;

    /** Action that runs the application*/
    protected RunAction runAction;

    /**
   * Is the controller displayed a CorpusController?
   */
    protected boolean corpusControllerMode = false;

    /**
   * Is the controller displayed an analyser controller?
   */
    protected boolean analyserMode = false;

    /**
   * Is the controller displayed conditional?
   */
    protected boolean conditionalMode = false;

    /**
   * The PR currently selected (having its parameters set)
   */
    protected ProcessingResource selectedPR = null;

    /**
   * The running strategy for the selected PR.
   */
    protected RunningStrategy selectedPRRunStrategy = null;

    private transient Vector<StatusListener> statusListeners;

    private transient Vector<ProgressListener> progressListeners;

    private AddPRAction addPRAction;

    private RemovePRAction removePRAction;

    protected void fireStatusChanged(String e) {
        if (statusListeners != null) {
            Vector<StatusListener> listeners = statusListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                listeners.elementAt(i).statusChanged(e);
            }
        }
    }

    public synchronized void removeProgressListener(ProgressListener l) {
        if (progressListeners != null && progressListeners.contains(l)) {
            Vector<ProgressListener> v = (Vector) progressListeners.clone();
            v.removeElement(l);
            progressListeners = v;
        }
    }

    public synchronized void addProgressListener(ProgressListener l) {
        Vector<ProgressListener> v = progressListeners == null ? new Vector(2) : (Vector) progressListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            progressListeners = v;
        }
    }

    protected void fireProgressChanged(int e) {
        if (progressListeners != null) {
            Vector<ProgressListener> listeners = progressListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                listeners.elementAt(i).progressChanged(e);
            }
        }
    }

    protected void fireProcessFinished() {
        if (progressListeners != null) {
            Vector<ProgressListener> listeners = progressListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                listeners.elementAt(i).processFinished();
            }
        }
    }
}
