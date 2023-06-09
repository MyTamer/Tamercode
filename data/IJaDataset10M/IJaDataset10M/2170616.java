package org.cgsuite.ui.explorer;

import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Logger;
import org.cgsuite.lang.CgsuiteCollection;
import org.cgsuite.lang.CgsuiteObject;
import org.cgsuite.lang.CgsuitePackage;
import org.cgsuite.lang.Domain;
import org.cgsuite.lang.Game;
import org.cgsuite.lang.explorer.EditorPanel;
import org.cgsuite.lang.explorer.Explorer;
import org.cgsuite.lang.explorer.ExplorerNode;
import org.cgsuite.lang.explorer.ExplorerWindow;
import org.cgsuite.lang.output.Output;
import org.cgsuite.lang.output.StyledTextOutput;
import org.cgsuite.ui.worksheet.CalculationCapsule;
import org.cgsuite.ui.worksheet.InputPane;
import org.openide.util.NbBundle;
import org.openide.util.Task;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.RequestProcessor;
import org.openide.util.TaskListener;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.cgsuite.ui.explorer//Explorer//EN", autostore = false)
public final class ExplorerTopComponent extends TopComponent implements ExplorerWindow, ExplorerTreeListener, KeyListener, TaskListener {

    private static ExplorerTopComponent instance;

    private static final String PREFERRED_ID = "ExplorerTopComponent";

    private String enterCommandHint;

    private Explorer explorer;

    private EditorPanel editorPanel;

    private CalculationCapsule currentCapsule;

    private String requestedEvaluationText;

    private String activeEvaluationText;

    private ExplorerNode activeEvaluationNode;

    public ExplorerTopComponent() {
        initComponents();
        inputPanel.getInputPane().addKeyListener(this);
        editorScrollPane.getViewport().setBackground(Color.white);
        treeScrollPane.getViewport().setBackground(Color.white);
        analysisScrollPane.getViewport().setBackground(Color.white);
        analysisWorksheetPanel.clear();
        inputPanel.getInputPane().activate();
        setName(NbBundle.getMessage(ExplorerTopComponent.class, "CTL_ExplorerTopComponent"));
        setToolTipText(NbBundle.getMessage(ExplorerTopComponent.class, "HINT_ExplorerTopComponent"));
        enterCommandHint = NbBundle.getMessage(ExplorerTopComponent.class, "HINT_EnterCommand");
        commandComboBox.insertItemAt(enterCommandHint, 0);
        commandComboBox.setSelectedIndex(0);
    }

    public void setExplorer(Explorer explorer) {
        this.explorer = explorer;
        tree.setExplorer(explorer);
        tree.addExplorerTreeListener(this);
        updateEditor();
    }

    private void initComponents() {
        treePopupMenu = new javax.swing.JPopupMenu();
        expandSensibleOptionsMenuItem = new javax.swing.JMenuItem();
        expandSensibleLinesMenuItem = new javax.swing.JMenuItem();
        primarySplitPane = new javax.swing.JSplitPane();
        detailSplitPane = new javax.swing.JSplitPane();
        editorScrollPane = new javax.swing.JScrollPane();
        analysisPanel = new javax.swing.JPanel();
        commandPanel = new javax.swing.JPanel();
        inputPanel = new org.cgsuite.ui.worksheet.InputPanel();
        commandComboBox = new javax.swing.JComboBox();
        analysisScrollPane = new javax.swing.JScrollPane();
        analysisWorksheetPanel = new org.cgsuite.ui.worksheet.WorksheetPanel();
        treeScrollPane = new javax.swing.JScrollPane();
        tree = new org.cgsuite.ui.explorer.ExplorerTreePanel();
        infoPanel = new javax.swing.JPanel();
        addPositionButton = new javax.swing.JButton();
        typeLabel = new javax.swing.JLabel();
        org.openide.awt.Mnemonics.setLocalizedText(expandSensibleOptionsMenuItem, org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.expandSensibleOptionsMenuItem.text"));
        expandSensibleOptionsMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expandSensibleOptionsMenuItemActionPerformed(evt);
            }
        });
        treePopupMenu.add(expandSensibleOptionsMenuItem);
        org.openide.awt.Mnemonics.setLocalizedText(expandSensibleLinesMenuItem, org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.expandSensibleLinesMenuItem.text"));
        expandSensibleLinesMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expandSensibleLinesMenuItemActionPerformed(evt);
            }
        });
        treePopupMenu.add(expandSensibleLinesMenuItem);
        setBackground(java.awt.Color.white);
        setLayout(new java.awt.BorderLayout());
        primarySplitPane.setBackground(new java.awt.Color(255, 255, 255));
        primarySplitPane.setDividerLocation(480);
        detailSplitPane.setDividerLocation(480);
        detailSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        detailSplitPane.setLeftComponent(editorScrollPane);
        analysisPanel.setBackground(new java.awt.Color(255, 255, 255));
        analysisPanel.setLayout(new java.awt.BorderLayout());
        commandPanel.setBackground(new java.awt.Color(255, 255, 255));
        commandPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        commandPanel.setLayout(new javax.swing.BoxLayout(commandPanel, javax.swing.BoxLayout.Y_AXIS));
        inputPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        commandPanel.add(inputPanel);
        commandComboBox.setFont(new java.awt.Font("Monospaced", 0, 13));
        commandComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selection.CanonicalForm", "Selection.Thermograph.Plot()", "Selection.AtomicWeight" }));
        commandComboBox.setAlignmentX(0.0F);
        commandComboBox.setMinimumSize(new java.awt.Dimension(90, 27));
        commandComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commandComboBoxActionPerformed(evt);
            }
        });
        commandPanel.add(commandComboBox);
        analysisPanel.add(commandPanel, java.awt.BorderLayout.PAGE_START);
        analysisScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        analysisScrollPane.setViewportView(analysisWorksheetPanel);
        analysisPanel.add(analysisScrollPane, java.awt.BorderLayout.CENTER);
        detailSplitPane.setRightComponent(analysisPanel);
        primarySplitPane.setLeftComponent(detailSplitPane);
        tree.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treeMouseClicked(evt);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                treeMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                treeMouseReleased(evt);
            }
        });
        treeScrollPane.setViewportView(tree);
        primarySplitPane.setRightComponent(treeScrollPane);
        add(primarySplitPane, java.awt.BorderLayout.CENTER);
        infoPanel.setBackground(new java.awt.Color(255, 255, 255));
        infoPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        infoPanel.setLayout(new javax.swing.BoxLayout(infoPanel, javax.swing.BoxLayout.LINE_AXIS));
        org.openide.awt.Mnemonics.setLocalizedText(addPositionButton, org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.addPositionButton.text"));
        addPositionButton.setEnabled(false);
        addPositionButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPositionButtonActionPerformed(evt);
            }
        });
        infoPanel.add(addPositionButton);
        typeLabel.setBackground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(typeLabel, org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.typeLabel.text"));
        typeLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 4));
        infoPanel.add(typeLabel);
        add(infoPanel, java.awt.BorderLayout.PAGE_START);
    }

    private CgsuiteObject leftOptions, rightOptions;

    private CgsuiteObject leftLines, rightLines;

    private void expandSensibleOptionsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        final ExplorerNode node = tree.getSelectedNode();
        if (node == null) return;
        final Game g = node.getG();
        RequestProcessor.Task task = CalculationCapsule.REQUEST_PROCESSOR.create(new Runnable() {

            @Override
            public void run() {
                leftOptions = g.invokeMethod("SensibleLeftOptions$get");
                rightOptions = g.invokeMethod("SensibleRightOptions$get");
            }
        });
        task.schedule(0);
        task.waitFinished();
        for (CgsuiteObject gl : (CgsuiteCollection) leftOptions) {
            node.addLeftChild((Game) gl);
        }
        for (CgsuiteObject gr : (CgsuiteCollection) rightOptions) {
            node.addRightChild((Game) gr);
        }
        tree.refresh();
    }

    private void treeMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.isPopupTrigger()) doTreePopup(evt);
    }

    private void treeMousePressed(java.awt.event.MouseEvent evt) {
        if (evt.isPopupTrigger()) doTreePopup(evt);
    }

    private void treeMouseReleased(java.awt.event.MouseEvent evt) {
        if (evt.isPopupTrigger()) doTreePopup(evt);
    }

    private void addPositionButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Game g = (Game) editorPanel.constructObject();
        ExplorerNode node = explorer.findOrAdd(g);
        tree.setSelectedNode(node);
    }

    private void commandComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        setCommand((String) commandComboBox.getSelectedItem());
    }

    private void expandSensibleLinesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        final ExplorerNode node = tree.getSelectedNode();
        if (node == null) return;
        final Game g = node.getG();
        RequestProcessor.Task task = CalculationCapsule.REQUEST_PROCESSOR.create(new Runnable() {

            @Override
            public void run() {
                leftLines = g.invokeMethod("SensibleLeftLines$get");
                rightLines = g.invokeMethod("SensibleRightLines$get");
            }
        });
        task.schedule(0);
        task.waitFinished();
        for (CgsuiteObject line : (CgsuiteCollection) leftLines) {
            ExplorerNode curNode = node;
            boolean left = true;
            for (CgsuiteObject follower : (CgsuiteCollection) line) {
                curNode = curNode.addChild((Game) follower, left);
                left = !left;
            }
        }
        for (CgsuiteObject line : (CgsuiteCollection) rightLines) {
            ExplorerNode curNode = node;
            boolean left = false;
            for (CgsuiteObject follower : (CgsuiteCollection) line) {
                curNode = curNode.addChild((Game) follower, left);
                left = !left;
            }
        }
        tree.refresh();
    }

    private void doTreePopup(MouseEvent evt) {
        treePopupMenu.show(tree, evt.getX(), evt.getY());
    }

    private javax.swing.JButton addPositionButton;

    private javax.swing.JPanel analysisPanel;

    private javax.swing.JScrollPane analysisScrollPane;

    private org.cgsuite.ui.worksheet.WorksheetPanel analysisWorksheetPanel;

    private javax.swing.JComboBox commandComboBox;

    private javax.swing.JPanel commandPanel;

    private javax.swing.JSplitPane detailSplitPane;

    private javax.swing.JScrollPane editorScrollPane;

    private javax.swing.JMenuItem expandSensibleLinesMenuItem;

    private javax.swing.JMenuItem expandSensibleOptionsMenuItem;

    private javax.swing.JPanel infoPanel;

    private org.cgsuite.ui.worksheet.InputPanel inputPanel;

    private javax.swing.JSplitPane primarySplitPane;

    private org.cgsuite.ui.explorer.ExplorerTreePanel tree;

    private javax.swing.JPopupMenu treePopupMenu;

    private javax.swing.JScrollPane treeScrollPane;

    private javax.swing.JLabel typeLabel;

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized ExplorerTopComponent getDefault() {
        if (instance == null) {
            instance = new ExplorerTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ExplorerTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized ExplorerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(ExplorerTopComponent.class.getName()).warning("Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof ExplorerTopComponent) {
            return (ExplorerTopComponent) win;
        }
        Logger.getLogger(ExplorerTopComponent.class.getName()).warning("There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    @Override
    public void selectionPathChanged(List<ExplorerNode> newPath) {
        updateEditor();
        reeval();
    }

    private void updateEditor() {
        ExplorerNode node = tree.getSelectedNode();
        if (node == null) {
            this.typeLabel.setText("No position is selected.");
        } else {
            this.editorPanel = node.getG().toEditor();
            this.editorScrollPane.setViewportView(editorPanel);
            this.addPositionButton.setEnabled(true);
            this.typeLabel.setText("Exploring " + node.getG().getCgsuiteClass().getQualifiedName() + ".");
        }
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    private void setCommand(String command) {
        if (requestedEvaluationText != null && requestedEvaluationText.equals(command)) {
            return;
        }
        requestedEvaluationText = command;
        commandComboBox.insertItemAt(requestedEvaluationText, 0);
        for (int i = 1; i < commandComboBox.getItemCount(); i++) {
            if (enterCommandHint.equals(commandComboBox.getItemAt(i)) || requestedEvaluationText.equals(commandComboBox.getItemAt(i))) {
                commandComboBox.removeItemAt(i);
                i--;
            }
        }
        commandComboBox.setSelectedIndex(0);
        reeval();
    }

    private synchronized void reeval() {
        if (currentCapsule != null) {
            return;
        }
        if (activeEvaluationText != null && activeEvaluationText.equals(requestedEvaluationText) && activeEvaluationNode == tree.getSelectedNode()) {
            return;
        }
        activeEvaluationText = requestedEvaluationText;
        activeEvaluationNode = tree.getSelectedNode();
        analysisWorksheetPanel.clear();
        if (activeEvaluationText != null && tree.getSelectedNode() != null && !activeEvaluationText.equals(enterCommandHint)) {
            Domain domain = new Domain(explorer, null);
            CalculationCapsule capsule = new CalculationCapsule(activeEvaluationText, domain);
            RequestProcessor.Task task = CalculationCapsule.REQUEST_PROCESSOR.create(capsule);
            task.addTaskListener(this);
            task.schedule(0);
            boolean finished = false;
            try {
                finished = task.waitFinished(50);
            } catch (InterruptedException exc) {
            }
            Output[] output;
            if (finished) {
                output = capsule.getOutput();
                assert output != null;
            } else {
                output = new Output[] { new StyledTextOutput("Calculating ...") };
                this.currentCapsule = capsule;
            }
            analysisWorksheetPanel.postOutput(output);
        }
        analysisScrollPane.validate();
    }

    @Override
    public ExplorerNode getSelectedNode() {
        return tree.getSelectedNode();
    }

    @Override
    public List<ExplorerNode> getSelectionPath() {
        return tree.getSelectionPath();
    }

    @Override
    public synchronized void taskFinished(Task task) {
        if (currentCapsule == null) return;
        analysisWorksheetPanel.clear();
        Output[] output = currentCapsule.getOutput();
        assert output != null;
        currentCapsule = null;
        analysisWorksheetPanel.postOutput(output);
        analysisScrollPane.validate();
        reeval();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        InputPane source = (InputPane) evt.getSource();
        switch(evt.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                if (evt.getModifiers() == 0) {
                    evt.consume();
                    if (!source.getText().equals("")) {
                        setCommand(source.getText());
                        source.setText("");
                    }
                } else if (evt.getModifiers() == KeyEvent.SHIFT_MASK) {
                    evt.consume();
                    source.insert("\n", source.getCaretPosition());
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
