package ch.ethz.sg.cuttlefish.gui.widgets;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import ch.ethz.sg.cuttlefish.gui.BrowserWidget;
import ch.ethz.sg.cuttlefish.misc.Utils;
import ch.ethz.sg.cuttlefish.networks.TemporalNetwork;

public class ExportPanel extends BrowserWidget {

    private static final long serialVersionUID = 1L;

    private JButton dotButton = null;

    private JButton pstricksButton = null;

    private JButton adjlistButton = null;

    private JButton edgelistButton = null;

    private JTextField jTextField = null;

    private JLabel jLabel = null;

    /**
	 * This is the default constructor
	 */
    public ExportPanel() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.anchor = GridBagConstraints.WEST;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints3.gridy = 0;
        jLabel = new JLabel();
        jLabel.setText("prefix:");
        jLabel.setVisible(false);
        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints21.gridy = 1;
        gridBagConstraints21.weightx = 1.0;
        gridBagConstraints21.gridwidth = 2;
        gridBagConstraints21.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints21.gridx = 0;
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 1;
        gridBagConstraints11.gridy = 2;
        gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints11.weightx = 1.0;
        gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.gridy = 2;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints2.fill = GridBagConstraints.BOTH;
        gridBagConstraints2.weightx = 1.0;
        gridBagConstraints2.gridy = 3;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridy = 3;
        this.setSize(287, 230);
        this.setLayout(new GridBagLayout());
        this.add(getDotButton(), gridBagConstraints);
        this.add(getPstricksButton(), gridBagConstraints2);
        this.add(getAdjlistButton(), gridBagConstraints1);
        this.add(getEdgelistButton(), gridBagConstraints11);
        this.add(getJTextField(), gridBagConstraints21);
        this.add(jLabel, gridBagConstraints3);
    }

    /**
	 * This method initializes dotButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getDotButton() {
        if (dotButton == null) {
            dotButton = new JButton();
            dotButton.setText("dot");
            dotButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    exportToDot();
                }
            });
        }
        return dotButton;
    }

    /**
	 * This method initializes pstricksButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getPstricksButton() {
        if (pstricksButton == null) {
            pstricksButton = new JButton();
            pstricksButton.setText("pstricks");
            pstricksButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    exportToPsTricks();
                }
            });
        }
        return pstricksButton;
    }

    /**
	 * This method initializes adjlistButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getAdjlistButton() {
        if (adjlistButton == null) {
            adjlistButton = new JButton();
            adjlistButton.setText("adj. matrix");
            adjlistButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        PrintStream p = new PrintStream(new File("adjlists/" + getNetwork().getName() + "_adjmatrix.dat"));
                        int[][] myAdjMatrix = Utils.graphToAdjacencyMatrix(getNetwork());
                        Utils.printMatrix(myAdjMatrix, p);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        return adjlistButton;
    }

    public void exportToDot() {
        try {
            PrintStream p = new PrintStream(new File(getNetwork().getName() + ".dot"));
            Utils.graphToDot(getNetwork(), p);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exportToPsTricks() {
        try {
            String fileName = null;
            if (getNetwork() instanceof TemporalNetwork && ((TemporalNetwork) getNetwork()).getDate() != null) {
                DateFormat format = new SimpleDateFormat("yyyy");
                fileName = "pics/" + getNetwork().getName() + format.format(((TemporalNetwork) getNetwork()).getDate()) + ".tex";
            } else {
                fileName = "pics/" + getNetwork().getName() + ".tex";
            }
            System.out.println("exporting to " + fileName);
            PrintStream p = new PrintStream(new File(fileName));
            Utils.exportGraphToPSTricks(getNetwork(), p, getBrowser().getNetworkLayout());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exportToPos() {
        try {
            PrintStream p = new PrintStream(getBrowser().getPositionFile());
            Utils.writePositions(getNetwork(), p, getBrowser().getNetworkLayout());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
    }

    /**
	 * This method initializes edgelistButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getEdgelistButton() {
        if (edgelistButton == null) {
            edgelistButton = new JButton();
            edgelistButton.setText("edge list");
            edgelistButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        PrintStream p = new PrintStream(new File("edgelists/" + getNetwork().getName() + ".edgelist"));
                        Utils.writeEdgeList(getNetwork(), p);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        return edgelistButton;
    }

    /**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJTextField() {
        if (jTextField == null) {
            jTextField = new JTextField();
            jTextField.setVisible(false);
        }
        return jTextField;
    }
}
