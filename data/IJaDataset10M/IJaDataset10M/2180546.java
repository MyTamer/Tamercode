package dr.app.beast;

import dr.app.beagle.evomodel.treelikelihood.PartialsRescalingScheme;
import dr.app.gui.FileDrop;
import dr.app.gui.components.WholeNumberField;
import jam.html.SimpleLinkListener;
import jam.panels.OptionsPanel;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

public class BeastDialog {

    private final JFrame frame;

    private final OptionsPanel optionPanel;

    private final WholeNumberField seedText = new WholeNumberField((long) 1, Long.MAX_VALUE);

    private final JCheckBox overwriteCheckBox = new JCheckBox("Allow overwriting of log files");

    private final JCheckBox beagleCheckBox = new JCheckBox("Use BEAGLE library if available:");

    private final JCheckBox beagleInfoCheckBox = new JCheckBox("Show list of available BEAGLE resources and Quit");

    private final JComboBox beagleResourceCombo = new JComboBox(new Object[] { "CPU", "GPU" });

    private final JCheckBox beagleSSECheckBox = new JCheckBox("Use CPU's SSE extensions");

    private final JComboBox beaglePrecisionCombo = new JComboBox(new Object[] { "Double", "Single" });

    private final JComboBox beagleScalingCombo = new JComboBox(new Object[] { "Default", "Dynamic", "Delayed", "Always", "Never" });

    private final JComboBox threadsCombo = new JComboBox(new Object[] { "Automatic", 0, 1, 2, 3, 4, 5, 6, 7, 8 });

    private File inputFile = null;

    public BeastDialog(final JFrame frame, final String titleString, final Icon icon) {
        this.frame = frame;
        optionPanel = new OptionsPanel(12, 12);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        final JLabel titleText = new JLabel(titleString);
        titleText.setIcon(icon);
        optionPanel.addSpanningComponent(titleText);
        titleText.setFont(new Font("sans-serif", 0, 12));
        final JButton inputFileButton = new JButton("Choose File...");
        final JTextField inputFileNameText = new JTextField("not selected", 16);
        inputFileButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                FileDialog dialog = new FileDialog(frame, "Select target file...", FileDialog.LOAD);
                dialog.setVisible(true);
                if (dialog.getFile() == null) {
                    return;
                }
                inputFile = new File(dialog.getDirectory(), dialog.getFile());
                inputFileNameText.setText(inputFile.getName());
            }
        });
        inputFileNameText.setEditable(false);
        JPanel panel1 = new JPanel(new BorderLayout(0, 0));
        panel1.add(inputFileNameText, BorderLayout.CENTER);
        panel1.add(inputFileButton, BorderLayout.EAST);
        optionPanel.addComponentWithLabel("BEAST XML File: ", panel1);
        Color focusColor = UIManager.getColor("Focus.color");
        Border focusBorder = BorderFactory.createMatteBorder(2, 2, 2, 2, focusColor);
        new FileDrop(null, inputFileNameText, focusBorder, new FileDrop.Listener() {

            public void filesDropped(java.io.File[] files) {
                inputFile = files[0];
                inputFileNameText.setText(inputFile.getName());
            }
        });
        optionPanel.addComponent(overwriteCheckBox);
        optionPanel.addSeparator();
        seedText.setColumns(12);
        optionPanel.addComponentWithLabel("Random number seed: ", seedText);
        optionPanel.addComponentWithLabel("Thread pool size: ", threadsCombo);
        optionPanel.addSeparator();
        optionPanel.addSpanningComponent(beagleCheckBox);
        beagleCheckBox.setSelected(true);
        final OptionsPanel optionPanel1 = new OptionsPanel(0, 12);
        optionPanel1.setBorder(new TitledBorder(""));
        OptionsPanel optionPanel2 = new OptionsPanel(0, 12);
        optionPanel2.setBorder(BorderFactory.createEmptyBorder());
        final JLabel label1 = optionPanel2.addComponentWithLabel("Prefer use of: ", beagleResourceCombo);
        beagleSSECheckBox.setSelected(true);
        final JLabel label2 = optionPanel2.addComponentWithLabel("Prefer precision: ", beaglePrecisionCombo);
        final JLabel label3 = optionPanel2.addComponentWithLabel("Rescaling scheme: ", beagleScalingCombo);
        optionPanel2.addComponent(beagleInfoCheckBox);
        optionPanel1.addComponent(optionPanel2);
        final JEditorPane beagleInfo = new JEditorPane("text/html", "<html><div style=\"font-family:sans-serif;font-size:12;\"><p>BEAGLE is a high-performance phylogenetic library that can make use of<br>" + "additional computational resources such as graphics boards. It must be<br>" + "downloaded and installed independently of BEAST:</p>" + "<pre><a href=\"http://beagle-lib.googlecode.com/\">http://beagle-lib.googlecode.com/</a></pre></div>");
        beagleInfo.setOpaque(false);
        beagleInfo.setEditable(false);
        beagleInfo.addHyperlinkListener(new SimpleLinkListener());
        optionPanel1.addComponent(beagleInfo);
        optionPanel.addSpanningComponent(optionPanel1);
        beagleInfoCheckBox.setEnabled(false);
        beagleCheckBox.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                beagleInfo.setEnabled(beagleCheckBox.isSelected());
                beagleInfoCheckBox.setEnabled(beagleCheckBox.isSelected());
                label1.setEnabled(beagleCheckBox.isSelected());
                beagleResourceCombo.setEnabled(beagleCheckBox.isSelected());
                beagleSSECheckBox.setEnabled(beagleCheckBox.isSelected());
                label2.setEnabled(beagleCheckBox.isSelected());
                beaglePrecisionCombo.setEnabled(beagleCheckBox.isSelected());
                label3.setEnabled(beagleCheckBox.isSelected());
                beagleScalingCombo.setEnabled(beagleCheckBox.isSelected());
            }
        });
        beagleCheckBox.setSelected(false);
        beagleResourceCombo.setSelectedItem("CPU");
    }

    public boolean showDialog(String title, long seed) {
        JOptionPane optionPane = new JOptionPane(optionPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, new String[] { "Run", "Quit" }, "Run");
        optionPane.setBorder(new EmptyBorder(12, 12, 12, 12));
        seedText.setValue(seed);
        final JDialog dialog = optionPane.createDialog(frame, title);
        dialog.pack();
        dialog.setVisible(true);
        return optionPane.getValue().equals("Run");
    }

    public long getSeed() {
        return seedText.getLongValue();
    }

    public boolean allowOverwrite() {
        return overwriteCheckBox.isSelected();
    }

    public boolean useBeagle() {
        return beagleCheckBox.isSelected();
    }

    public boolean preferBeagleGPU() {
        return beagleResourceCombo.getSelectedItem().equals("GPU");
    }

    public boolean preferBeagleCPU() {
        return beagleResourceCombo.getSelectedItem().equals("CPU");
    }

    public boolean preferBeagleSSE() {
        return preferBeagleCPU();
    }

    public boolean preferBeagleSingle() {
        return beaglePrecisionCombo.getSelectedItem().equals("Single");
    }

    public boolean preferBeagleDouble() {
        return beaglePrecisionCombo.getSelectedItem().equals("Double");
    }

    public String scalingScheme() {
        return ((String) beagleScalingCombo.getSelectedItem()).toLowerCase();
    }

    public boolean showBeagleInfo() {
        return beagleInfoCheckBox.isSelected();
    }

    public int getThreadPoolSize() {
        if (threadsCombo.getSelectedIndex() == 0) {
            return -1;
        }
        return (Integer) threadsCombo.getSelectedItem();
    }

    public File getInputFile() {
        return inputFile;
    }
}
