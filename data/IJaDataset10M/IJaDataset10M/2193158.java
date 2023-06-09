package com.jmex.editors.swing.particles;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.jmex.editors.swing.widget.ValuePanel;
import com.jmex.effects.particles.ParticleSystem;

public class ParticleWorldPanel extends ParticleEditPanel {

    private static final long serialVersionUID = 1L;

    private JLabel countLabel;

    private ValuePanel speedPanel = new ValuePanel("Time Modifier: ", "x", 0f, Float.MAX_VALUE, 0.01f);

    private ValuePanel precisionPanel = new ValuePanel("Precision: ", "s", 0f, Float.MAX_VALUE, 0.001f);

    private JComboBox renderQueueCB;

    public ParticleWorldPanel() {
        super();
        setLayout(new GridBagLayout());
        initPanel();
    }

    private void initPanel() {
        countLabel = createBoldLabel("Particles: 300");
        JButton countButton = new JButton(new AbstractAction("Change...") {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                countButton_actionPerformed(e);
            }
        });
        countButton.setFont(new Font("Arial", Font.BOLD, 12));
        countButton.setMargin(new Insets(2, 2, 2, 2));
        JPanel countPanel = new JPanel(new GridBagLayout());
        countPanel.setBorder(createTitledBorder("PARTICLE COUNT"));
        countPanel.add(countLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 5, 10), 0, 0));
        countPanel.add(countButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 5, 10), 0, 0));
        speedPanel.setBorder(createTitledBorder("TIME"));
        speedPanel.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                getEdittedParticles().getParticleController().setSpeed(speedPanel.getFloatValue());
            }
        });
        precisionPanel.setBorder(createTitledBorder("UPDATE PRECISION"));
        precisionPanel.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                getEdittedParticles().getParticleController().setPrecision(precisionPanel.getFloatValue());
            }
        });
        final JLabel queueLabel = createBoldLabel("Render Queue:");
        renderQueueCB = new JComboBox(new String[] { "INHERIT", "SKIP", "OPAQUE", "TRANSPARENT", "ORTHO" });
        renderQueueCB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                getEdittedParticles().setRenderQueueMode(renderQueueCB.getSelectedIndex());
            }
        });
        final JPanel queuePanel = new JPanel(new GridBagLayout());
        queuePanel.setBorder(createTitledBorder("RENDER QUEUE"));
        queuePanel.add(queueLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        queuePanel.add(renderQueueCB, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        add(countPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 10), 0, 0));
        add(speedPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 0, 0));
        add(precisionPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 0, 0));
        add(queuePanel, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 5), 0, 0));
    }

    /**
     * updateCountLabels
     */
    private void updateCountLabels() {
        int val = getEdittedParticles().getNumParticles();
        countLabel.setText("Particles: " + val);
    }

    @Override
    public void updateWidgets() {
        updateCountLabels();
        speedPanel.setValue(getEdittedParticles().getParticleController().getSpeed());
        precisionPanel.setValue(getEdittedParticles().getParticleController().getPrecision());
        ParticleSystem system = getEdittedParticles();
        renderQueueCB.setSelectedIndex(system.getRenderQueueMode());
    }

    private void countButton_actionPerformed(ActionEvent e) {
        String response = JOptionPane.showInputDialog(this, "Please enter a new particle count for this system:", "How many particles?", JOptionPane.PLAIN_MESSAGE);
        if (response == null) return;
        int particles = 100;
        try {
            particles = Integer.parseInt(response);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number entered.  Using 100 instead.", "Invalid", JOptionPane.WARNING_MESSAGE);
            particles = 100;
        }
        getEdittedParticles().recreate(particles);
        updateCountLabels();
        validate();
    }
}
