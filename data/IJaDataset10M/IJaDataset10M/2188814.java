package freedbimporter.gui;

import freedbimporter.cddb.CddbServer;
import freedbimporter.data.adaption.validation.DefaultDiscValidator;
import freedbimporter.data.adaption.validation.ValidationException;
import java.awt.Dimension;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class CddbConfigurator extends javax.swing.JFrame implements MySQLConfiguratorListener, ThreadListener {

    static final DefaultDiscValidator VALIDATOR = new DefaultDiscValidator();

    MySQLConfigurator myConfigurator;

    boolean lookingLeft;

    CddbServer cddbServer;

    /** Creates new form SpoolConfigurator */
    public CddbConfigurator() {
        initComponents();
        Dimension dim = new Dimension(700, 300);
        setMaximumSize(dim);
        setPreferredSize(dim);
        setTitle(getClass().getSimpleName());
        pack();
        myConfigurator = new MySQLConfigurator(this, false);
        try {
            cddbServerHostNameTextField.setText(java.net.InetAddress.getLocalHost().getHostName());
        } catch (java.net.UnknownHostException u) {
            cddbServerHostNameTextField.setText(CddbServer.class.getSimpleName());
            MainGUI.LOGGER.warn(u);
        }
        setTitle(getClass().getSimpleName());
        lookingLeft = true;
        lookLeft();
        setStartButtonAbility();
    }

    class HostNameInputVerifier extends InputVerifier {

        public boolean verify(JComponent input) {
            String t = ((JTextField) input).getText();
            try {
                VALIDATOR.getValidatedString(t);
            } catch (ValidationException v) {
                MainGUI.LOGGER.warn(v);
                return false;
            }
            return true;
        }
    }

    public void toggleTux() {
        if (lookingLeft) {
            lookingLeft = false;
            lookRight();
        } else {
            lookingLeft = true;
            lookLeft();
        }
    }

    void lookRight() {
        tuxPanel.getGraphics().clearRect(0, 0, tuxPanel.getWidth(), tuxPanel.getHeight());
        tuxPanel.getGraphics().drawImage(MainGUI.RIGHT_LOOKING_TUX, 0, 0, null);
    }

    void lookLeft() {
        tuxPanel.getGraphics().clearRect(0, 0, tuxPanel.getWidth(), tuxPanel.getHeight());
        tuxPanel.getGraphics().drawImage(MainGUI.LEFT_LOOKING_TUX, 0, 0, null);
    }

    public void report(String reportersName, String report) {
        ReporterJDialog reporter = new ReporterJDialog(this, reportersName, report);
        reporter.setVisible(true);
        reporter.dispose();
        setStartButtonAbility();
        setGuiStoppedState();
        setTitle(getClass().getSimpleName());
    }

    boolean setStartButtonAbility() {
        if ((myConfigurator.connector == null) || !myConfigurator.connector.isAlive() || !myConfigurator.connector.tablesExist()) {
            startButton.setEnabled(false);
            return false;
        }
        if ((cddbServerHostNameTextField.getText() != null) && (cddbServerHostNameTextField.getText().trim().length() == 0)) {
            startButton.setEnabled(false);
            return false;
        }
        startButton.setEnabled(true);
        return true;
    }

    public void configuratorChanged() {
        if (myConfigurator.connector != null) {
            connectionLabel.setText(myConfigurator.connector.toString());
        } else {
            connectionLabel.setText(null);
        }
        setStartButtonAbility();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        tuxPanel = new javax.swing.JPanel();
        connectionButton = new javax.swing.JButton();
        connectionLabel = new javax.swing.JLabel();
        startButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        cddbServerHostNameTextField = new javax.swing.JTextField();
        cddbServerPortTextField = new javax.swing.JTextField();
        cddbServerPortLabel = new javax.swing.JLabel();
        cddbServerHostNameLabel = new javax.swing.JLabel();
        timeoutTextField = new javax.swing.JTextField();
        connectionPoolSizeTextField = new javax.swing.JTextField();
        timeoutLabel = new javax.swing.JLabel();
        connectionsPoolSizeLabel = new javax.swing.JLabel();
        setName("cddbConfigurator");
        getContentPane().setLayout(new java.awt.GridBagLayout());
        tuxPanel.setMaximumSize(new java.awt.Dimension(200, 235));
        tuxPanel.setMinimumSize(new java.awt.Dimension(200, 235));
        tuxPanel.setName("tux");
        tuxPanel.setOpaque(false);
        tuxPanel.setPreferredSize(new java.awt.Dimension(200, 235));
        tuxPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        getContentPane().add(tuxPanel, gridBagConstraints);
        connectionButton.setText("MySQL-Connection");
        connectionButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectionButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.16;
        getContentPane().add(connectionButton, gridBagConstraints);
        connectionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        connectionLabel.setToolTipText("MySQL-Database the Cddb-Server uses");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.16;
        getContentPane().add(connectionLabel, gridBagConstraints);
        startButton.setText("Start Cddb-Server");
        startButton.setToolTipText("Starts cddb-database (server)");
        startButton.setEnabled(false);
        startButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.16;
        getContentPane().add(startButton, gridBagConstraints);
        stopButton.setText("Stop Cddb-Server");
        stopButton.setToolTipText("Stops cddb-database (server)");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.16;
        getContentPane().add(stopButton, gridBagConstraints);
        cddbServerHostNameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cddbServerHostNameTextField.setText("localhost");
        cddbServerHostNameTextField.setToolTipText("Hostname the server uses to identify itself");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.16;
        getContentPane().add(cddbServerHostNameTextField, gridBagConstraints);
        cddbServerPortTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cddbServerPortTextField.setText("8880");
        cddbServerPortTextField.setToolTipText("Port the Cddb-Server listens to");
        cddbServerPortTextField.setInputVerifier(new PortInputVerifier());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.16;
        getContentPane().add(cddbServerPortTextField, gridBagConstraints);
        cddbServerPortLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cddbServerPortLabel.setText("Cddb-Server port");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.16;
        getContentPane().add(cddbServerPortLabel, gridBagConstraints);
        cddbServerHostNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cddbServerHostNameLabel.setText("Cddb-Server hostname");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.16;
        getContentPane().add(cddbServerHostNameLabel, gridBagConstraints);
        timeoutTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        timeoutTextField.setText("10");
        timeoutTextField.setToolTipText("Cddb-Server's socket-timeout in seconds");
        timeoutTextField.setInputVerifier(new PortInputVerifier());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.16;
        getContentPane().add(timeoutTextField, gridBagConstraints);
        connectionPoolSizeTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        connectionPoolSizeTextField.setText("10");
        connectionPoolSizeTextField.setToolTipText("Number of concurrent connections allowed");
        connectionPoolSizeTextField.setInputVerifier(new PortInputVerifier());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.16;
        getContentPane().add(connectionPoolSizeTextField, gridBagConstraints);
        timeoutLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timeoutLabel.setText("Cddb-Server timeoutime");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.16;
        getContentPane().add(timeoutLabel, gridBagConstraints);
        connectionsPoolSizeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        connectionsPoolSizeLabel.setText("Cddb-Server children");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.16;
        getContentPane().add(connectionsPoolSizeLabel, gridBagConstraints);
        pack();
    }

    private void connectionButtonActionPerformed(java.awt.event.ActionEvent evt) {
        myConfigurator.setVisible(true);
    }

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
        lookLeft();
        if (setStartButtonAbility()) {
            CddbServer.HOST = cddbServerHostNameTextField.getText().trim();
            int port = (new Integer(cddbServerPortTextField.getText())).intValue();
            int connectionsPoolSize = new Integer(connectionPoolSizeTextField.getText()).intValue();
            if (connectionsPoolSize < 1) connectionsPoolSize = 10;
            int timeOutMilliSeconds = new Integer(timeoutTextField.getText()).intValue();
            if (timeOutMilliSeconds < 1) timeOutMilliSeconds = 999; else timeOutMilliSeconds = timeOutMilliSeconds * 1000;
            cddbServer = new CddbServer(port, connectionsPoolSize, timeOutMilliSeconds, myConfigurator.connector, this);
            cddbServer.start();
            setGuiStartedState();
        }
    }

    private void setGuiStartedState() {
        cddbServerHostNameTextField.setEnabled(false);
        cddbServerPortTextField.setEnabled(false);
        timeoutTextField.setEnabled(false);
        connectionPoolSizeTextField.setEnabled(false);
        myConfigurator.setVisible(false);
        connectionButton.setEnabled(false);
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    private void setGuiStoppedState() {
        cddbServerHostNameTextField.setEnabled(true);
        cddbServerPortTextField.setEnabled(true);
        timeoutTextField.setEnabled(true);
        connectionPoolSizeTextField.setEnabled(true);
        setTitle(getClass().getSimpleName());
        connectionButton.setEnabled(true);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {
        lookRight();
        if (cddbServer != null) {
            cddbServer.shutDown();
            setTitle(getClass().getSimpleName());
            setGuiStoppedState();
        }
    }

    private javax.swing.JLabel cddbServerHostNameLabel;

    private javax.swing.JTextField cddbServerHostNameTextField;

    private javax.swing.JLabel cddbServerPortLabel;

    private javax.swing.JTextField cddbServerPortTextField;

    private javax.swing.JButton connectionButton;

    private javax.swing.JLabel connectionLabel;

    private javax.swing.JTextField connectionPoolSizeTextField;

    private javax.swing.JLabel connectionsPoolSizeLabel;

    private javax.swing.JButton startButton;

    private javax.swing.JButton stopButton;

    private javax.swing.JLabel timeoutLabel;

    private javax.swing.JTextField timeoutTextField;

    private javax.swing.JPanel tuxPanel;
}
