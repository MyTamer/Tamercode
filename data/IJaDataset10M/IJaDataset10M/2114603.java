package tac.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import tac.gui.atlastree.JAtlasTree;
import tac.gui.components.JCollapsiblePanel;
import tac.gui.components.JProfilesComboBox;
import tac.program.model.Profile;
import tac.utilities.GBC;
import tac.utilities.Utilities;

public class JProfilesPanel extends JCollapsiblePanel {

    private JProfilesComboBox profilesCombo;

    private JButton reloadButton;

    private JButton deleteButton;

    private JButton saveAsButton;

    public JProfilesPanel(JAtlasTree atlasTree) {
        super("Saved profiles", new GridBagLayout());
        if (atlasTree == null) throw new NullPointerException();
        profilesCombo = new JProfilesComboBox();
        profilesCombo.setToolTipText("Select an atlas creation profile\n " + "or enter a name for a new profile");
        deleteButton = new JButton("Delete profile");
        deleteButton.addActionListener(new DeleteProfileListener());
        deleteButton.setToolTipText("Delete atlas profile from list");
        saveAsButton = new JButton("Save as profile");
        saveAsButton.setToolTipText("Save atlas profile");
        saveAsButton.addActionListener(new SaveAsProfileListener(atlasTree));
        GBC gbc = GBC.eol().fill().insets(5, 5, 5, 5);
        reloadButton = new JButton(Utilities.loadResourceImageIcon("refresh.png"));
        reloadButton.setToolTipText("reload the profiles list");
        reloadButton.addActionListener(new ReloadListener());
        reloadButton.setPreferredSize(new Dimension(24, 0));
        JPanel p = new JPanel(new BorderLayout());
        p.add(profilesCombo, BorderLayout.CENTER);
        p.add(reloadButton, BorderLayout.EAST);
        contentContainer.add(p, gbc);
        contentContainer.add(saveAsButton, gbc.toggleEol());
        contentContainer.add(deleteButton, gbc.toggleEol());
        saveAsButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    public void initialize() {
        profilesCombo.loadProfilesList();
        deleteButton.setEnabled(false);
    }

    public void reloadProfileList() {
        initialize();
    }

    public JProfilesComboBox getProfilesCombo() {
        return profilesCombo;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getSaveAsButton() {
        return saveAsButton;
    }

    public Profile getSelectedProfile() {
        return profilesCombo.getSelectedProfile();
    }

    private class SaveAsProfileListener implements ActionListener {

        JAtlasTree jAtlasTree;

        public SaveAsProfileListener(JAtlasTree atlasTree) {
            super();
            jAtlasTree = atlasTree;
        }

        public void actionPerformed(ActionEvent e) {
            if (!jAtlasTree.testAtlasContentValid()) return;
            Object selObject = profilesCombo.getEditor().getItem();
            String profileName = null;
            Profile profile = null;
            boolean profileInList = false;
            if (selObject instanceof Profile) {
                profile = (Profile) selObject;
                profileName = profile.getName();
                profileInList = true;
            } else profileName = (String) selObject;
            if (profileName.length() == 0) {
                JOptionPane.showMessageDialog(null, "Please enter a profile name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            profile = new Profile(profileName);
            if (profile.exists()) {
                int response = JOptionPane.showConfirmDialog(null, "Profile \"" + profileName + "\" already exists. Overwrite?", "Please confirm", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.NO_OPTION) return;
            }
            if (jAtlasTree.save(profile)) {
                if (!profileInList) profilesCombo.addItem(profile);
                JOptionPane.showMessageDialog(null, "Profile \"" + profileName + "\" has been successfully saved", "Profile save", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    private class DeleteProfileListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            profilesCombo.deleteSelectedProfile();
        }
    }

    private class ReloadListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            reloadProfileList();
        }
    }
}
