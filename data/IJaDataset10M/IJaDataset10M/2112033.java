package org.neuroph.netbeans.main.easyneurons.samples;

import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.JSlider;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.netbeans.api.settings.ConvertAsProperties;
import org.neuroph.netbeans.main.easyneurons.NeuralNetworkTraining;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.neuroph.netbeans.main.easyneurons.samples//NFRSample//EN", autostore = false)
public final class NFRSampleTopComponent extends TopComponent {

    private static NFRSampleTopComponent instance;

    private static final String PREFERRED_ID = "NFRSampleTopComponent";

    public NFRSampleTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(NFRSampleTopComponent.class, "CTL_NFRSampleTopComponent"));
        setToolTipText(NbBundle.getMessage(NFRSampleTopComponent.class, "HINT_NFRSampleTopComponent"));
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanel1 = new javax.swing.JPanel();
        pointsSlider = new javax.swing.JSlider();
        timeSlider = new javax.swing.JSlider();
        pointsField = new javax.swing.JTextField();
        timeField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        classifyButton = new javax.swing.JButton();
        trainButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        badPanel = new javax.swing.JPanel();
        goodPanel = new javax.swing.JPanel();
        veryGoodPanel = new javax.swing.JPanel();
        excellentPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        setLayout(new java.awt.BorderLayout());
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new java.awt.GridBagLayout());
        pointsSlider.setMajorTickSpacing(10);
        pointsSlider.setMinorTickSpacing(1);
        pointsSlider.setPaintLabels(true);
        pointsSlider.setPaintTicks(true);
        pointsSlider.setPreferredSize(new java.awt.Dimension(330, 40));
        pointsSlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                pointsSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 15, 5);
        jPanel1.add(pointsSlider, gridBagConstraints);
        timeSlider.setMajorTickSpacing(10);
        timeSlider.setMaximum(120);
        timeSlider.setMinimum(15);
        timeSlider.setMinorTickSpacing(1);
        timeSlider.setPaintLabels(true);
        timeSlider.setPaintTicks(true);
        timeSlider.setPreferredSize(new java.awt.Dimension(330, 40));
        timeSlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                timeSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 15, 5);
        jPanel1.add(timeSlider, gridBagConstraints);
        pointsField.setBackground(new java.awt.Color(247, 247, 247));
        pointsField.setColumns(5);
        pointsField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        jPanel1.add(pointsField, gridBagConstraints);
        timeField.setBackground(new java.awt.Color(247, 247, 247));
        timeField.setColumns(5);
        timeField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        jPanel1.add(timeField, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(NFRSampleTopComponent.class, "NFRSampleTopComponent.jLabel1.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        jPanel1.add(jLabel1, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(NFRSampleTopComponent.class, "NFRSampleTopComponent.jLabel2.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        jPanel1.add(jLabel2, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(classifyButton, org.openide.util.NbBundle.getMessage(NFRSampleTopComponent.class, "NFRSampleTopComponent.classifyButton.text"));
        classifyButton.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                classifyButtonMousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        jPanel1.add(classifyButton, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(trainButton, org.openide.util.NbBundle.getMessage(NFRSampleTopComponent.class, "NFRSampleTopComponent.trainButton.text"));
        trainButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trainButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        jPanel1.add(trainButton, gridBagConstraints);
        add(jPanel1, java.awt.BorderLayout.NORTH);
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new java.awt.GridBagLayout());
        badPanel.setBackground(new java.awt.Color(255, 0, 0));
        badPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        badPanel.setOpaque(false);
        badPanel.setPreferredSize(new java.awt.Dimension(102, 102));
        javax.swing.GroupLayout badPanelLayout = new javax.swing.GroupLayout(badPanel);
        badPanel.setLayout(badPanelLayout);
        badPanelLayout.setHorizontalGroup(badPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        badPanelLayout.setVerticalGroup(badPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 10, 10);
        jPanel2.add(badPanel, gridBagConstraints);
        goodPanel.setBackground(new java.awt.Color(255, 255, 51));
        goodPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        goodPanel.setOpaque(false);
        goodPanel.setPreferredSize(new java.awt.Dimension(102, 102));
        javax.swing.GroupLayout goodPanelLayout = new javax.swing.GroupLayout(goodPanel);
        goodPanel.setLayout(goodPanelLayout);
        goodPanelLayout.setHorizontalGroup(goodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        goodPanelLayout.setVerticalGroup(goodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 10, 10);
        jPanel2.add(goodPanel, gridBagConstraints);
        veryGoodPanel.setBackground(new java.awt.Color(0, 0, 204));
        veryGoodPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        veryGoodPanel.setOpaque(false);
        veryGoodPanel.setPreferredSize(new java.awt.Dimension(102, 102));
        javax.swing.GroupLayout veryGoodPanelLayout = new javax.swing.GroupLayout(veryGoodPanel);
        veryGoodPanel.setLayout(veryGoodPanelLayout);
        veryGoodPanelLayout.setHorizontalGroup(veryGoodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        veryGoodPanelLayout.setVerticalGroup(veryGoodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 10, 10);
        jPanel2.add(veryGoodPanel, gridBagConstraints);
        excellentPanel.setBackground(new java.awt.Color(0, 204, 0));
        excellentPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        excellentPanel.setOpaque(false);
        excellentPanel.setPreferredSize(new java.awt.Dimension(102, 102));
        javax.swing.GroupLayout excellentPanelLayout = new javax.swing.GroupLayout(excellentPanel);
        excellentPanel.setLayout(excellentPanelLayout);
        excellentPanelLayout.setHorizontalGroup(excellentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        excellentPanelLayout.setVerticalGroup(excellentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 10, 10);
        jPanel2.add(excellentPanel, gridBagConstraints);
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(NFRSampleTopComponent.class, "NFRSampleTopComponent.jLabel3.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel2.add(jLabel3, gridBagConstraints);
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(NFRSampleTopComponent.class, "NFRSampleTopComponent.jLabel4.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel2.add(jLabel4, gridBagConstraints);
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(NFRSampleTopComponent.class, "NFRSampleTopComponent.jLabel5.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel2.add(jLabel5, gridBagConstraints);
        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(NFRSampleTopComponent.class, "NFRSampleTopComponent.jLabel6.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        jPanel2.add(jLabel6, gridBagConstraints);
        add(jPanel2, java.awt.BorderLayout.CENTER);
    }

    private void pointsSliderStateChanged(javax.swing.event.ChangeEvent evt) {
        JSlider source = (JSlider) evt.getSource();
        if (!source.getValueIsAdjusting()) {
            int value = source.getValue();
            pointsField.setText((new Integer(value)).toString());
        }
    }

    private void timeSliderStateChanged(javax.swing.event.ChangeEvent evt) {
        JSlider source = (JSlider) evt.getSource();
        if (!source.getValueIsAdjusting()) {
            int value = source.getValue();
            timeField.setText((new Integer(value)).toString());
        }
    }

    private void classifyButtonMousePressed(java.awt.event.MouseEvent evt) {
        badPanel.setOpaque(false);
        goodPanel.setOpaque(false);
        veryGoodPanel.setOpaque(false);
        excellentPanel.setOpaque(false);
        String inStr = pointsField.getText().trim() + " " + timeField.getText().trim();
        controller.setInput(inStr);
        double[] output = controller.getNetwork().getOutput();
        if (output[0] == 1) badPanel.setOpaque(true);
        if (output[1] == 1) goodPanel.setOpaque(true);
        if (output[2] == 1) veryGoodPanel.setOpaque(true);
        if (output[3] == 1) excellentPanel.setOpaque(true);
        this.paintAll(this.getGraphics());
        this.repaint();
    }

    private void trainButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controller.getNetwork().learnInNewThread(controller.getTrainingSet());
    }

    private javax.swing.JPanel badPanel;

    private javax.swing.JButton classifyButton;

    private javax.swing.JPanel excellentPanel;

    private javax.swing.JPanel goodPanel;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JTextField pointsField;

    private javax.swing.JSlider pointsSlider;

    private javax.swing.JTextField timeField;

    private javax.swing.JSlider timeSlider;

    private javax.swing.JButton trainButton;

    private javax.swing.JPanel veryGoodPanel;

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized NFRSampleTopComponent getDefault() {
        if (instance == null) {
            instance = new NFRSampleTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the NFRSampleTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized NFRSampleTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(NFRSampleTopComponent.class.getName()).warning("Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof NFRSampleTopComponent) {
            return (NFRSampleTopComponent) win;
        }
        Logger.getLogger(NFRSampleTopComponent.class.getName()).warning("There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
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

    private NeuralNetworkTraining controller;

    /** Creates new form NFRStudentDemoNew */
    public void setNeuralNetworkTrainingController(NeuralNetworkTraining controller) {
        this.controller = controller;
        initComponents();
        pointsField.setText((new Integer(pointsSlider.getValue())).toString());
        timeField.setText((new Integer(timeSlider.getValue())).toString());
    }
}
