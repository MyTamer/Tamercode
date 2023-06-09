package au.edu.qut.yawl.editor.actions.tools;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import au.edu.qut.yawl.editor.YAWLEditor;
import au.edu.qut.yawl.editor.actions.YAWLBaseAction;
import au.edu.qut.yawl.editor.analyser.YAWLResetAnalyser;
import au.edu.qut.yawl.editor.swing.AbstractDoneDialog;
import au.edu.qut.yawl.editor.thirdparty.wofyawl.WofYAWLProxy;

public class ConfigureAnalysisToolsAction extends YAWLBaseAction {

    /**
   * 
   */
    private static final long serialVersionUID = 1L;

    private static final ConfigureAnalysisDialog dialog = new ConfigureAnalysisDialog();

    private boolean invokedAtLeastOnce = false;

    {
        putValue(Action.SHORT_DESCRIPTION, " Configure Specification Analysis ");
        putValue(Action.NAME, "Configure Specification Analysis");
        putValue(Action.LONG_DESCRIPTION, "Configure Specification Analysis.");
        putValue(Action.MNEMONIC_KEY, new Integer(java.awt.event.KeyEvent.VK_A));
    }

    public ConfigureAnalysisToolsAction() {
    }

    public void actionPerformed(ActionEvent event) {
        if (!invokedAtLeastOnce) {
            invokedAtLeastOnce = true;
            dialog.setLocationRelativeTo(YAWLEditor.getInstance());
        }
        dialog.setVisible(true);
    }
}

class ConfigureAnalysisDialog extends AbstractDoneDialog {

    /**
   * 
   */
    private static final long serialVersionUID = 1L;

    private static final Preferences prefs = Preferences.userNodeForPackage(YAWLEditor.class);

    private JTabbedPane tabbedPane;

    private JCheckBox wofYawlAnalysisCheckBox;

    private JCheckBox relaxedSoundnessCheckBox;

    private JCheckBox transitionInvariantCheckBox;

    private JCheckBox extendedCoverabilityCheckBox;

    private JCheckBox resetNetAnalysisCheckBox;

    private JCheckBox weakSoundnessCheckBox;

    private JCheckBox soundnessCheckBox;

    private JCheckBox cancellationCheckBox;

    private JCheckBox orjoinCheckBox;

    private JCheckBox showObservationsCheckBox;

    private JCheckBox useYAWLReductionRulesCheckBox;

    private JCheckBox useResetReductionRulesCheckBox;

    public ConfigureAnalysisDialog() {
        super("Configure Specification Analysis", true);
        setContentPanel(getConfigurationPanel());
        getDoneButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                rememberResetNetAnalysisPreferences();
                rememberWofYAWLAnalysisPreferences();
            }
        });
    }

    private void rememberResetNetAnalysisPreferences() {
        prefs.putBoolean(YAWLResetAnalyser.RESET_NET_ANALYSIS_PREFERENCE, resetNetAnalysisCheckBox.isSelected());
        prefs.putBoolean(YAWLResetAnalyser.WEAKSOUNDNESS_ANALYSIS_PREFERENCE, weakSoundnessCheckBox.isSelected());
        prefs.putBoolean(YAWLResetAnalyser.SOUNDNESS_ANALYSIS_PREFERENCE, soundnessCheckBox.isSelected());
        prefs.putBoolean(YAWLResetAnalyser.CANCELLATION_ANALYSIS_PREFERENCE, cancellationCheckBox.isSelected());
        prefs.putBoolean(YAWLResetAnalyser.ORJOIN_ANALYSIS_PREFERENCE, orjoinCheckBox.isSelected());
        prefs.putBoolean(YAWLResetAnalyser.SHOW_OBSERVATIONS_PREFERENCE, showObservationsCheckBox.isSelected());
        prefs.putBoolean(YAWLResetAnalyser.USE_YAWLREDUCTIONRULES_PREFERENCE, useYAWLReductionRulesCheckBox.isSelected());
        prefs.putBoolean(YAWLResetAnalyser.USE_RESETREDUCTIONRULES_PREFERENCE, useResetReductionRulesCheckBox.isSelected());
    }

    private void rememberWofYAWLAnalysisPreferences() {
        prefs.putBoolean(WofYAWLProxy.WOFYAWL_ANALYSIS_PREFERENCE, wofYawlAnalysisCheckBox.isSelected());
        prefs.putBoolean(WofYAWLProxy.STRUCTURAL_ANALYSIS_PREFERENCE, relaxedSoundnessCheckBox.isSelected());
        prefs.putBoolean(WofYAWLProxy.BEHAVIOURAL_ANALYSIS_PREFERENCE, transitionInvariantCheckBox.isSelected());
        prefs.putBoolean(WofYAWLProxy.EXTENDED_COVERABILITY_PREFERENCE, extendedCoverabilityCheckBox.isSelected());
    }

    protected void makeLastAdjustments() {
        pack();
        setResizable(false);
    }

    private JPanel getConfigurationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 5, 0, 5));
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Reset Net Analysis", getResetNetPanel());
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_R);
        tabbedPane.addTab("WofYAWL Analysis", getWofYAWLPanel());
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_W);
        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel getResetNetPanel() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel panel = new JPanel(gbl);
        panel.setBorder(new EmptyBorder(12, 12, 0, 11));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(getResetNetAnalysisCheckBox(), gbc);
        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        panel.add(new JLabel(), gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 12, 5, 12);
        panel.add(new JSeparator(), gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(getUseYAWLReductionRulesCheckBox(), gbc);
        gbc.gridy++;
        panel.add(getUseResetReductionRulesCheckBox(), gbc);
        gbc.gridy++;
        panel.add(getWeakSoundnessCheckBox(), gbc);
        gbc.gridy++;
        panel.add(getCancellationCheckBox(), gbc);
        gbc.gridy++;
        panel.add(getOrjoinCheckBox(), gbc);
        gbc.gridy++;
        panel.add(getSoundnessCheckBox(), gbc);
        gbc.gridy++;
        panel.add(getShowObservationsCheckBox(), gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;
        gbc.gridy++;
        panel.add(new JLabel("<html>These tests may overlap in identifying unreachable tasks in the<br>" + "specification, potential deadlocks and possible unfinished work in<br>" + "completed workflow cases.</html>"), gbc);
        return panel;
    }

    private JCheckBox getResetNetAnalysisCheckBox() {
        resetNetAnalysisCheckBox = new JCheckBox("Use the reset net analysis algorithm.");
        resetNetAnalysisCheckBox.setMnemonic(KeyEvent.VK_U);
        resetNetAnalysisCheckBox.setSelected(true);
        resetNetAnalysisCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                enableResetNetCheckBoxesAsAppropriate();
            }
        });
        return resetNetAnalysisCheckBox;
    }

    private void enableResetNetCheckBoxesAsAppropriate() {
        if (resetNetAnalysisCheckBox.isSelected()) {
            weakSoundnessCheckBox.setEnabled(true);
            soundnessCheckBox.setEnabled(true);
            cancellationCheckBox.setEnabled(true);
            orjoinCheckBox.setEnabled(true);
            showObservationsCheckBox.setEnabled(true);
            useYAWLReductionRulesCheckBox.setEnabled(true);
            useResetReductionRulesCheckBox.setEnabled(true);
        } else {
            weakSoundnessCheckBox.setEnabled(false);
            soundnessCheckBox.setEnabled(false);
            cancellationCheckBox.setEnabled(false);
            orjoinCheckBox.setEnabled(false);
            showObservationsCheckBox.setEnabled(false);
            useYAWLReductionRulesCheckBox.setEnabled(false);
            useResetReductionRulesCheckBox.setEnabled(false);
        }
    }

    private JCheckBox getWeakSoundnessCheckBox() {
        weakSoundnessCheckBox = new JCheckBox("Check for weak soundness property using coverability.");
        weakSoundnessCheckBox.setMnemonic(KeyEvent.VK_W);
        weakSoundnessCheckBox.setSelected(true);
        return weakSoundnessCheckBox;
    }

    private JCheckBox getSoundnessCheckBox() {
        soundnessCheckBox = new JCheckBox("Check for soundness property using reachability results from bounded nets.");
        soundnessCheckBox.setMnemonic(KeyEvent.VK_S);
        soundnessCheckBox.setSelected(true);
        return soundnessCheckBox;
    }

    private JCheckBox getCancellationCheckBox() {
        cancellationCheckBox = new JCheckBox("Check for unnecessary cancellation regions.");
        cancellationCheckBox.setMnemonic(KeyEvent.VK_C);
        cancellationCheckBox.setSelected(true);
        return cancellationCheckBox;
    }

    private JCheckBox getOrjoinCheckBox() {
        orjoinCheckBox = new JCheckBox("Check for unnecessary or-joins.");
        orjoinCheckBox.setMnemonic(KeyEvent.VK_O);
        orjoinCheckBox.setSelected(true);
        return orjoinCheckBox;
    }

    private JCheckBox getShowObservationsCheckBox() {
        showObservationsCheckBox = new JCheckBox("Show observations in analysis results.");
        showObservationsCheckBox.setMnemonic(KeyEvent.VK_S);
        showObservationsCheckBox.setSelected(true);
        return showObservationsCheckBox;
    }

    private JCheckBox getUseYAWLReductionRulesCheckBox() {
        useYAWLReductionRulesCheckBox = new JCheckBox("Use YAWL reduction rules before analysis for optimisation.");
        useYAWLReductionRulesCheckBox.setMnemonic(KeyEvent.VK_Y);
        useYAWLReductionRulesCheckBox.setSelected(true);
        return useYAWLReductionRulesCheckBox;
    }

    private JCheckBox getUseResetReductionRulesCheckBox() {
        useResetReductionRulesCheckBox = new JCheckBox("Use Reset reduction rules before analysis for optimisation.");
        useResetReductionRulesCheckBox.setMnemonic(KeyEvent.VK_R);
        useResetReductionRulesCheckBox.setSelected(true);
        return useResetReductionRulesCheckBox;
    }

    private JPanel getWofYAWLPanel() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel panel = new JPanel(gbl);
        panel.setBorder(new EmptyBorder(12, 12, 0, 11));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(getWofYawlAnalysisCheckBox(), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(5, 12, 5, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JSeparator(), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(getRelaxedSoundnessCheckBox(), gbc);
        gbc.gridy++;
        panel.add(getTransitionInvariantCheckBox(), gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy++;
        panel.add(new JLabel("<html>These tests may overlap in identifying unreachable tasks in the<br>" + "specification, potential deadlocks and possible unfinished work in<br>" + "completed workflow cases.</html>"), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(5, 12, 5, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JSeparator(), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(getExtendedCoverabilityCheckBox(), gbc);
        return panel;
    }

    private JCheckBox getWofYawlAnalysisCheckBox() {
        wofYawlAnalysisCheckBox = new JCheckBox("Use the WofYAWL analysis algorithm.");
        wofYawlAnalysisCheckBox.setMnemonic(KeyEvent.VK_U);
        wofYawlAnalysisCheckBox.setSelected(true);
        wofYawlAnalysisCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                enableWofYAWLCheckBoxesAsAppropriate();
            }
        });
        return wofYawlAnalysisCheckBox;
    }

    private JCheckBox getRelaxedSoundnessCheckBox() {
        relaxedSoundnessCheckBox = new JCheckBox("Structural check for relaxed soundness in a bounded analysis net");
        relaxedSoundnessCheckBox.setMnemonic(KeyEvent.VK_R);
        relaxedSoundnessCheckBox.setDisplayedMnemonicIndex(2);
        relaxedSoundnessCheckBox.setSelected(true);
        return relaxedSoundnessCheckBox;
    }

    private JCheckBox getTransitionInvariantCheckBox() {
        transitionInvariantCheckBox = new JCheckBox("Behaviourial check for semi-positive transition invariants in a short-circuited analysis net");
        transitionInvariantCheckBox.setMnemonic(KeyEvent.VK_S);
        transitionInvariantCheckBox.setSelected(true);
        return transitionInvariantCheckBox;
    }

    private JCheckBox getExtendedCoverabilityCheckBox() {
        extendedCoverabilityCheckBox = new JCheckBox("Extend coverability graph of an unbounded analysis net (slow)");
        extendedCoverabilityCheckBox.setMnemonic(KeyEvent.VK_E);
        extendedCoverabilityCheckBox.setSelected(true);
        return extendedCoverabilityCheckBox;
    }

    public void setVisible(boolean visible) {
        if (visible) {
            displayResetNetAnalysisPreferences();
            if (WofYAWLProxy.wofYawlAvailable()) {
                tabbedPane.setEnabledAt(1, true);
                displayWofYAWLPreferences();
            } else {
                tabbedPane.setEnabledAt(1, false);
            }
        }
        super.setVisible(visible);
    }

    private void displayResetNetAnalysisPreferences() {
        resetNetAnalysisCheckBox.setSelected(prefs.getBoolean(YAWLResetAnalyser.RESET_NET_ANALYSIS_PREFERENCE, true));
        weakSoundnessCheckBox.setSelected(prefs.getBoolean(YAWLResetAnalyser.WEAKSOUNDNESS_ANALYSIS_PREFERENCE, true));
        soundnessCheckBox.setSelected(prefs.getBoolean(YAWLResetAnalyser.SOUNDNESS_ANALYSIS_PREFERENCE, true));
        cancellationCheckBox.setSelected(prefs.getBoolean(YAWLResetAnalyser.CANCELLATION_ANALYSIS_PREFERENCE, true));
        orjoinCheckBox.setSelected(prefs.getBoolean(YAWLResetAnalyser.ORJOIN_ANALYSIS_PREFERENCE, true));
        showObservationsCheckBox.setSelected(prefs.getBoolean(YAWLResetAnalyser.SHOW_OBSERVATIONS_PREFERENCE, true));
        useYAWLReductionRulesCheckBox.setSelected(prefs.getBoolean(YAWLResetAnalyser.USE_YAWLREDUCTIONRULES_PREFERENCE, true));
        useResetReductionRulesCheckBox.setSelected(prefs.getBoolean(YAWLResetAnalyser.USE_RESETREDUCTIONRULES_PREFERENCE, true));
        enableResetNetCheckBoxesAsAppropriate();
    }

    private void displayWofYAWLPreferences() {
        wofYawlAnalysisCheckBox.setSelected(prefs.getBoolean(WofYAWLProxy.WOFYAWL_ANALYSIS_PREFERENCE, true));
        relaxedSoundnessCheckBox.setSelected(prefs.getBoolean(WofYAWLProxy.STRUCTURAL_ANALYSIS_PREFERENCE, true));
        transitionInvariantCheckBox.setSelected(prefs.getBoolean(WofYAWLProxy.BEHAVIOURAL_ANALYSIS_PREFERENCE, true));
        extendedCoverabilityCheckBox.setSelected(prefs.getBoolean(WofYAWLProxy.EXTENDED_COVERABILITY_PREFERENCE, true));
        enableWofYAWLCheckBoxesAsAppropriate();
    }

    private void enableWofYAWLCheckBoxesAsAppropriate() {
        if (wofYawlAnalysisCheckBox.isSelected()) {
            relaxedSoundnessCheckBox.setEnabled(true);
            transitionInvariantCheckBox.setEnabled(true);
            extendedCoverabilityCheckBox.setEnabled(true);
        } else {
            relaxedSoundnessCheckBox.setEnabled(false);
            transitionInvariantCheckBox.setEnabled(false);
            extendedCoverabilityCheckBox.setEnabled(false);
        }
    }
}
