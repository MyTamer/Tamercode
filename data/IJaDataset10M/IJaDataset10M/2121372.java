package nl.vu.cs.pato.gui;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import nl.vu.cs.pato.clusters.Cluster;
import nl.vu.cs.pato.clusters.MeasurementCalculator;
import nl.vu.cs.pato.net.Network;

/**
 * The similarity measurement panel of the Pajek Tools GUI.
 *
 * @version $Rev: 235 $
 * @author Maarten Menken
 */
public class SimilarityMeasurementPanel extends MainWindowPanel {

    private static final long serialVersionUID = 1L;

    private static NumberFormat formatter = NumberFormat.getInstance(Locale.US);

    private static Logger myLogger = Logger.getLogger(SimilarityMeasurementPanel.class.getName());

    static {
        if (formatter instanceof DecimalFormat) {
            ((DecimalFormat) formatter).applyPattern("0.0%");
        }
    }

    /**
     * Creates the similarity measurement panel.
     *
     * @param mainWindow the main window of the Pato GUI
     */
    public SimilarityMeasurementPanel(MainWindow mainWindow) {
        super(mainWindow);
        initComponents();
        Logger.getLogger(SimilarityMeasurementPanel.class.getName()).addHandler(mainWindow.messageHandler);
        Logger.getLogger(MeasurementCalculator.class.getName()).addHandler(mainWindow.messageHandler);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        openNetworkFileChooser = new javax.swing.JFileChooser();
        openCluster1DirChooser = new javax.swing.JFileChooser();
        openCluster2DirChooser = new javax.swing.JFileChooser();
        descriptionPanel = new javax.swing.JPanel();
        descriptionScrollPane = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        requiredArgumentsPanel = new javax.swing.JPanel();
        networkFileLabel = new javax.swing.JLabel();
        networkFileTextField = new javax.swing.JTextField();
        networkFileChooserButton = new javax.swing.JButton();
        cluster1DirLabel = new javax.swing.JLabel();
        cluster1DirTextField = new javax.swing.JTextField();
        cluster1DirChooserButton = new javax.swing.JButton();
        cluster2DirLabel = new javax.swing.JLabel();
        cluster2DirTextField = new javax.swing.JTextField();
        cluster2DirChooserButton = new javax.swing.JButton();
        compareButton = new javax.swing.JButton();
        openNetworkFileChooser.setDialogTitle("Open");
        openNetworkFileChooser.setFileFilter(Utils.NETWORK_FILE_FILTER);
        openCluster1DirChooser.setDialogTitle("Open");
        openCluster1DirChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        openCluster2DirChooser.setDialogTitle("Open");
        openCluster2DirChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        setLayout(new java.awt.GridBagLayout());
        descriptionPanel.setLayout(new java.awt.GridBagLayout());
        descriptionPanel.setBorder(new javax.swing.border.TitledBorder("Description"));
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setText("Compares two decompositions using similarity measurements.\n\nThe measures implemented are adaptations of the classical precision and recall measures as well as a method called EdgeSim. The first two measures are based on the number of intrapairs, which are pairs that are in the same cluster. Precision is defined as the percentage of intrapairs in the first cluster that are also intrapairs in the second cluster. Recall is defined as the percentage of intrapairs in the second cluster that are also intrapairs in the first. The EdgeSim considers both the vertices and the links and is not sensitive to the size and number of the clusters (as are precision and recall). Both intralinks (i.e. links that connect nodes within the same cluster) and interlinks (i.e. links that connect nodes in different clusters) are taken into account to calculate the link similarity between two decompositions. The three measures give an indication of how well the partitioning was performed and therefore what relations and strengths give best results.\n\nOnly classes are taken into account. Properties and instances are ignored.");
        descriptionTextArea.setCaretPosition(0);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionScrollPane.setViewportView(descriptionTextArea);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        descriptionPanel.add(descriptionScrollPane, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(descriptionPanel, gridBagConstraints);
        requiredArgumentsPanel.setLayout(new java.awt.GridBagLayout());
        requiredArgumentsPanel.setBorder(new javax.swing.border.TitledBorder("Required arguments"));
        networkFileLabel.setText("Pajek network file");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        requiredArgumentsPanel.add(networkFileLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        requiredArgumentsPanel.add(networkFileTextField, gridBagConstraints);
        networkFileChooserButton.setText("Browse...");
        networkFileChooserButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                networkFileChooserButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        requiredArgumentsPanel.add(networkFileChooserButton, gridBagConstraints);
        cluster1DirLabel.setText("First cluster directory");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        requiredArgumentsPanel.add(cluster1DirLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        requiredArgumentsPanel.add(cluster1DirTextField, gridBagConstraints);
        cluster1DirChooserButton.setText("Browse...");
        cluster1DirChooserButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cluster1DirChooserButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        requiredArgumentsPanel.add(cluster1DirChooserButton, gridBagConstraints);
        cluster2DirLabel.setText("Second cluster directory");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        requiredArgumentsPanel.add(cluster2DirLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        requiredArgumentsPanel.add(cluster2DirTextField, gridBagConstraints);
        cluster2DirChooserButton.setText("Browse...");
        cluster2DirChooserButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cluster2DirChooserButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        requiredArgumentsPanel.add(cluster2DirChooserButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(requiredArgumentsPanel, gridBagConstraints);
        compareButton.setText("Compare");
        compareButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compareButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(compareButton, gridBagConstraints);
    }

    private void compareButtonActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.setEnabled(false);
        SwingWorker worker = new SwingWorker() {

            public Object construct() {
                mainWindow.messageHandler.clearTextArea();
                myLogger.info("Testing if the specified files are acceptable");
                String networkFilename = networkFileTextField.getText();
                String cluster1DirName = cluster1DirTextField.getText();
                String cluster2DirName = cluster2DirTextField.getText();
                if (networkFilename.equals("")) {
                    myLogger.warning("You did not specify a network file.");
                    mainWindow.setEnabled(true);
                    JOptionPane.showMessageDialog(mainWindow, "Please specify a network file.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                if (cluster1DirName.equals("")) {
                    myLogger.warning("You did not specify the first cluster directory.");
                    mainWindow.setEnabled(true);
                    JOptionPane.showMessageDialog(mainWindow, "Please specify the first cluster directory.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                if (cluster2DirName.equals("")) {
                    myLogger.warning("You did not specify the first second directory.");
                    mainWindow.setEnabled(true);
                    JOptionPane.showMessageDialog(mainWindow, "Please specify the second cluster directory.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                File networkFile = new File(networkFilename);
                File cluster1Dir = new File(cluster1DirName);
                File cluster2Dir = new File(cluster2DirName);
                if (!networkFile.exists()) {
                    myLogger.warning("The network file does not exist.");
                    mainWindow.setEnabled(true);
                    JOptionPane.showMessageDialog(mainWindow, "The network file does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                if (!cluster1Dir.exists()) {
                    myLogger.warning("The first cluster directory does not exist.");
                    mainWindow.setEnabled(true);
                    JOptionPane.showMessageDialog(mainWindow, "The first cluster directory does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                if (!cluster2Dir.exists()) {
                    myLogger.warning("The second cluster directory does not exist.");
                    mainWindow.setEnabled(true);
                    JOptionPane.showMessageDialog(mainWindow, "The second cluster directory does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                myLogger.info("Reading the network");
                Network network = new Network();
                try {
                    network.read(networkFile);
                    myLogger.info("Read " + network.getVertices().size() + " vertices, " + network.getArcs().size() + " arcs, " + network.getEdges().size() + " edges");
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                    myLogger.severe(e.getMessage());
                    myLogger.severe("An I/O error occurred while reading the network.");
                    mainWindow.setEnabled(true);
                    JOptionPane.showMessageDialog(mainWindow, "An I/O error occurred while reading the network.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                List<Cluster> decomposition1;
                try {
                    myLogger.info("Searching for a cluster index file in directory " + cluster1Dir.getAbsolutePath());
                    File[] clusterIndexFiles = cluster1Dir.listFiles(Cluster.CLUSTER_INDEX_FILENAME_FILTER);
                    if (clusterIndexFiles.length == 0) {
                        myLogger.info("No cluster index file found in directory " + cluster1Dir.getAbsolutePath() + ". Falling back to processing the network files to generate the clusters.");
                        decomposition1 = Cluster.readClusters(cluster1Dir);
                    } else if (clusterIndexFiles.length == 1) {
                        File clusterIndexFile = clusterIndexFiles[0];
                        myLogger.info("Found one cluster index file " + clusterIndexFile.getAbsolutePath());
                        List<Integer> clusterIds = Cluster.readClusterIds(clusterIndexFile);
                        decomposition1 = Cluster.generate(network, clusterIds);
                    } else {
                        myLogger.info("Multiple cluster index files found in directory " + cluster1Dir.getAbsolutePath() + ". Falling back to processing the network files to generate the clusters.");
                        decomposition1 = Cluster.readClusters(cluster1Dir);
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                    myLogger.severe(e.getMessage());
                    myLogger.severe("An I/O error occurred while reading the first decomposition.");
                    mainWindow.setEnabled(true);
                    JOptionPane.showMessageDialog(mainWindow, "An I/O error occurred while reading the first decomposition.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                } catch (IndexOutOfBoundsException e) {
                    myLogger.warning("The number of vertices in the network and the first cluster directory do not match.");
                    mainWindow.setEnabled(true);
                    JOptionPane.showMessageDialog(mainWindow, "The number of vertices in the network and the first cluster directory do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                List<Cluster> decomposition2;
                try {
                    myLogger.info("Searching for a cluster index file in directory " + cluster2Dir.getAbsolutePath());
                    File[] clusterIndexFiles = cluster2Dir.listFiles(Cluster.CLUSTER_INDEX_FILENAME_FILTER);
                    if (clusterIndexFiles.length == 0) {
                        myLogger.info("No cluster index file found in directory " + cluster2Dir.getAbsolutePath() + ". Falling back to processing the network files to generate the clusters.");
                        decomposition2 = Cluster.readClusters(cluster2Dir);
                    } else if (clusterIndexFiles.length == 1) {
                        File clusterIndexFile = clusterIndexFiles[0];
                        myLogger.info("Found one cluster index file " + clusterIndexFile.getAbsolutePath());
                        List<Integer> clusterIds = Cluster.readClusterIds(clusterIndexFile);
                        decomposition2 = Cluster.generate(network, clusterIds);
                    } else {
                        myLogger.info("Multiple cluster index files found in directory " + cluster2Dir.getAbsolutePath() + ". Falling back to processing the network files to generate the clusters.");
                        decomposition2 = Cluster.readClusters(cluster2Dir);
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                    myLogger.severe(e.getMessage());
                    myLogger.severe("An I/O error occurred while reading the second decomposition.");
                    mainWindow.setEnabled(true);
                    JOptionPane.showMessageDialog(mainWindow, "An I/O error occurred while reading the second decomposition.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                } catch (IndexOutOfBoundsException e) {
                    myLogger.warning("The number of vertices in the network and the second cluster directory do not match.");
                    mainWindow.setEnabled(true);
                    JOptionPane.showMessageDialog(mainWindow, "The number of vertices in the network and the second cluster directory do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                myLogger.info("Calculating the similarity measures");
                double precision = MeasurementCalculator.getPrecision(decomposition1, decomposition2);
                double recall = MeasurementCalculator.getRecall(decomposition1, decomposition2);
                double edgesim = MeasurementCalculator.getEdgeSim(network, decomposition1, decomposition2);
                mainWindow.setEnabled(true);
                StringBuffer message = new StringBuffer();
                message.append("The similarity measurements are:\n");
                message.append("Precision: ");
                if (precision == -1) {
                    message.append("NaN");
                } else {
                    message.append(formatter.format(precision));
                }
                message.append("\n");
                message.append("Recall: ");
                if (recall == -1) {
                    message.append("NaN");
                } else {
                    message.append(formatter.format(recall));
                }
                message.append("\n");
                message.append("EdgeSim: ");
                if (edgesim == -1) {
                    message.append("NaN");
                } else {
                    message.append(formatter.format(edgesim));
                }
                myLogger.info(message.toString());
                JOptionPane.showMessageDialog(mainWindow, message.toString(), "Measurement completed", JOptionPane.INFORMATION_MESSAGE);
                return null;
            }
        };
        worker.start();
    }

    private void cluster2DirChooserButtonActionPerformed(java.awt.event.ActionEvent evt) {
        File file = new File(cluster2DirTextField.getText());
        Utils.selectFile(openCluster2DirChooser, file);
        if (openCluster2DirChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            file = openCluster2DirChooser.getSelectedFile();
            cluster2DirTextField.setText(file.getAbsolutePath());
        }
    }

    private void cluster1DirChooserButtonActionPerformed(java.awt.event.ActionEvent evt) {
        File file = new File(cluster1DirTextField.getText());
        Utils.selectFile(openCluster1DirChooser, file);
        if (openCluster1DirChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            file = openCluster1DirChooser.getSelectedFile();
            cluster1DirTextField.setText(file.getAbsolutePath());
        }
    }

    private void networkFileChooserButtonActionPerformed(java.awt.event.ActionEvent evt) {
        File file = new File(networkFileTextField.getText());
        Utils.selectFile(openNetworkFileChooser, file);
        if (openNetworkFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            file = openNetworkFileChooser.getSelectedFile();
            networkFileTextField.setText(file.getAbsolutePath());
        }
    }

    /**
     * Stores the settings in a set of properties.
     *
     * @param properties a set of properties
     */
    public void storeSettings(Properties properties) {
        properties.setProperty("similarity measurement - network file", networkFileTextField.getText());
        properties.setProperty("similarity measurement - network file chooser directory", openNetworkFileChooser.getCurrentDirectory().getAbsolutePath());
        properties.setProperty("similarity measurement - first cluster directory", cluster1DirTextField.getText());
        properties.setProperty("similarity measurement - first cluster directory chooser directory", openCluster1DirChooser.getCurrentDirectory().getAbsolutePath());
        properties.setProperty("similarity measurement - second cluster directory", cluster2DirTextField.getText());
        properties.setProperty("similarity measurement - second cluster directory chooser directory", openCluster2DirChooser.getCurrentDirectory().getAbsolutePath());
    }

    /**
     * Loads the settings from a set of properties.
     *
     * @param properties a set of properties
     */
    public void loadSettings(Properties properties) {
        networkFileTextField.setText(properties.getProperty("similarity measurement - network file"));
        openNetworkFileChooser.setCurrentDirectory(new File(properties.getProperty("similarity measurement - network file chooser directory")));
        cluster1DirTextField.setText(properties.getProperty("similarity measurement - first cluster directory"));
        openCluster1DirChooser.setCurrentDirectory(new File(properties.getProperty("similarity measurement - first cluster directory chooser directory")));
        cluster2DirTextField.setText(properties.getProperty("similarity measurement - second cluster directory"));
        openCluster2DirChooser.setCurrentDirectory(new File(properties.getProperty("similarity measurement - second cluster directory chooser directory")));
    }

    private javax.swing.JButton cluster1DirChooserButton;

    private javax.swing.JLabel cluster1DirLabel;

    protected javax.swing.JTextField cluster1DirTextField;

    private javax.swing.JButton cluster2DirChooserButton;

    private javax.swing.JLabel cluster2DirLabel;

    private javax.swing.JTextField cluster2DirTextField;

    private javax.swing.JButton compareButton;

    private javax.swing.JPanel descriptionPanel;

    private javax.swing.JScrollPane descriptionScrollPane;

    private javax.swing.JTextArea descriptionTextArea;

    private javax.swing.JButton networkFileChooserButton;

    private javax.swing.JLabel networkFileLabel;

    protected javax.swing.JTextField networkFileTextField;

    private javax.swing.JFileChooser openCluster1DirChooser;

    private javax.swing.JFileChooser openCluster2DirChooser;

    private javax.swing.JFileChooser openNetworkFileChooser;

    private javax.swing.JPanel requiredArgumentsPanel;
}
