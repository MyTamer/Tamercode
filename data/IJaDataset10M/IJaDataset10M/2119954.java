package org.jhotdraw.gui;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.plaf.metal.*;
import org.jhotdraw.gui.event.ActivityManagerEvent;
import org.jhotdraw.gui.event.ActivityManagerListener;
import org.jhotdraw.util.ResourceBundleUtil;
import org.jhotdraw.util.prefs.PreferencesUtil;

/**
 * The {@code JActivityWindow} displays all progress models registered in the
 * progress manager.
 * <p>
 * Once created, {@code JActivityWindow} becomes visible automatically if the
 * if a progress model is added to the progress manager.
 * <p>
 * If an activity finishes successfully, it is automatically removed.
 * <p>
 * You typically only want to create a single instance. To do this,
 * call {@code JActivityWindow.getInstance();}.
 * 
 * <hr>
 * <b>Design Patterns</b>
 *
 * <p><em>Framework</em><br>
 * The interfaces and classes listed below define a framework for progress
 * management.<br>
 * Contract: {@link ActivityManager}, {@link ActivityModel}, 
 * {@link JActivityWindow}, {@link JActivityIndicator}.
 * 
 * @author Werner Randelshofer
 * @version $Id$
 */
public class JActivityWindow extends javax.swing.JFrame {

    private static JActivityWindow instance;

    private JPanel progressPanel;

    private ActivityManager manager;

    private Object activityOwner;

    /** Delay for automatic removal of successfully completed activities. 
     * Specify 0 for immediate removal. Specify -1 for no removal.
     * 
     * FIXME - Changing this value to -1 requires changing code in JActivityView.
     */
    private int normalRemovalDelay = 1500;

    /** Delay for automatic removal of completed activities with a warning.
     * Specify 0 for immediate removal. Specify -1 for no removal.
     * 
     * FIXME - Changing this value to -1 requires changing code in JActivityView.
     */
    private int warningRemovalDelay = 3000;

    /** Delay for automatic removal of completed activities with an error.
     * Specify 0 for immediate removal. Specify -1 for no removal.
     * 
     * FIXME - Changing this value to -1 requires changing code in JActivityView.
     */
    private int errorRemovalDelay = -1;

    private HashMap<ActivityModel, JActivityView> views = new HashMap<ActivityModel, JActivityView>();

    private class Handler implements ActivityManagerListener, PropertyChangeListener {

        @Override
        public void activityModelAdded(ActivityManagerEvent evt) {
            ActivityModel pm = evt.getActivityModel();
            if (activityOwner == null || activityOwner.equals(pm.getOwner())) {
                addActivityModel(pm);
            }
        }

        @Override
        public void activityModelRemoved(ActivityManagerEvent evt) {
            final ActivityModel pm = evt.getActivityModel();
            int delay = (pm.getError() != null) ? errorRemovalDelay : ((pm.getWarning() != null) ? warningRemovalDelay : normalRemovalDelay);
            if (delay == -1) {
                JActivityWindow.this.setVisible(true);
                return;
            }
            ActionListener tt = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    removeActivityModel(pm);
                }
            };
            if (delay == 0) {
                tt.actionPerformed(null);
            } else {
                Timer t = new Timer(pm.getError() != null ? delay : delay, tt);
                t.setRepeats(false);
                t.start();
            }
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName() == JActivityView.REQUEST_REMOVE_PROPERTY) {
                removeActivityModel(((JActivityView) evt.getSource()).getModel());
            }
        }
    }

    private Handler handler = new Handler();

    private ResourceBundleUtil labels;

    /** Creates new form JActivityWindow */
    public JActivityWindow() {
        this(ActivityManager.getInstance());
    }

    public JActivityWindow(ActivityManager pm) {
        labels = ResourceBundleUtil.getBundle("org.jhotdraw.gui.Labels");
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        progressPanel = new javax.swing.JPanel() {

            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width = 300;
                return d;
            }
        };
        progressPanel.setLayout(new javax.swing.BoxLayout(progressPanel, javax.swing.BoxLayout.Y_AXIS));
        scrollPane.setViewportView(progressPanel);
        disclosureToggle.setIcon(UIManager.getIcon("Tree.collapsedIcon"));
        disclosureToggle.setSelectedIcon(UIManager.getIcon("Tree.expandedIcon"));
        disclosureToggle.setUI((MetalToggleButtonUI) MetalToggleButtonUI.createUI(disclosureToggle));
        PreferencesUtil.installFramePrefsHandler(Preferences.userNodeForPackage(JActivityWindow.class), "progressFrame", this);
        setActivityManager(pm);
    }

    public void setActivityManager(ActivityManager newValue) {
        if (manager != null) {
            manager.removeActivityManagerListener(handler);
        }
        this.manager = newValue;
        if (manager != null) {
            manager.addActivityManagerListener(handler);
        }
        updateActivityModels();
        updateInfoPanel();
    }

    public ActivityManager getActivityManager() {
        return manager;
    }

    @SuppressWarnings("unchecked")
    private void updateActivityModels() {
        for (JActivityView pv : views.values()) {
            pv.setModel(null);
            progressPanel.remove(pv);
        }
        for (ActivityModel am : ((HashMap<ActivityModel, JActivityView>) views.clone()).keySet()) {
            removeActivityModel(am);
        }
        if (manager != null) {
            ArrayList<ActivityModel> pms = manager.getActivityModels();
            for (ActivityModel pm : pms) {
                addActivityModel(pm);
            }
            if (!views.isEmpty()) {
            }
        }
    }

    public static JActivityWindow getInstance() {
        if (instance == null) {
            instance = new JActivityWindow();
        }
        return instance;
    }

    public void addActivityModel(final ActivityModel model) {
        if (!views.containsKey(model)) {
            JActivityView viewer = new JActivityView(model);
            viewer.addPropertyChangeListener(handler);
            progressPanel.add(viewer);
            views.put(model, viewer);
            updateInfoPanel();
            pack();
            if (!isVisible()) {
            }
            viewer.repaint();
        }
    }

    /** Set to a non-null value to only display progress models
     * of a specific owner. Set to null to display all models.
     * 
     * @param newValue 
     */
    public void setActivityOwner(Object newValue) {
        Object oldValue = this.activityOwner;
        this.activityOwner = newValue;
        if (oldValue != newValue) {
            updateActivityModels();
        }
    }

    /** Owner is used to filter the progress models.
     * 
     * @return The owner of the progress models being shown or
     * null, if all models are shown.
     */
    public Object getActivityOwner() {
        return activityOwner;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.height = Math.min(600, d.height);
        return d;
    }

    public void removeActivityModel(final ActivityModel model) {
        if (views.containsKey(model)) {
            JActivityView viewer = views.get(model);
            viewer.removePropertyChangeListener(handler);
            progressPanel.remove(viewer);
            views.remove(model);
            updateInfoPanel();
            pack();
        }
    }

    /**
     * Updates the info label and the cancel all button on the
     * info panel.
     */
    private void updateInfoPanel() {
        int count = views.size();
        switch(count) {
            case 0:
                infoLabel.setText(labels.getString("ActivityWindow.noActivities.text"));
                cancelAllButton.setEnabled(false);
                break;
            case 1:
                infoLabel.setText(labels.getString("ActivityWindow.oneActivity.text"));
                cancelAllButton.setEnabled(true);
                break;
            default:
                infoLabel.setText(labels.getFormatted("ActivityWindow.nActivities.text", count));
                cancelAllButton.setEnabled(true);
                break;
        }
    }

    private static void invokeAndWait(Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(r);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        setActivityManager(null);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        infoPanel = new javax.swing.JPanel();
        disclosureToggle = new javax.swing.JToggleButton();
        infoLabel = new javax.swing.JLabel();
        cancelAllButton = new javax.swing.JButton();
        strutPanel = new javax.swing.JPanel();
        viewPanel = new javax.swing.JPanel();
        separator = new javax.swing.JSeparator();
        scrollPane = new javax.swing.JScrollPane();
        FormListener formListener = new FormListener();
        setTitle("Activity");
        infoPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        infoPanel.setLayout(new java.awt.GridBagLayout());
        disclosureToggle.setSelected(true);
        disclosureToggle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        disclosureToggle.setFocusPainted(false);
        disclosureToggle.setContentAreaFilled(false);
        disclosureToggle.setBorderPainted(false);
        disclosureToggle.setRequestFocusEnabled(false);
        disclosureToggle.addItemListener(formListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        infoPanel.add(disclosureToggle, gridBagConstraints);
        infoLabel.setFont(new java.awt.Font("Dialog", 0, 11));
        infoLabel.setText(labels.getString("ActivityWindow.noActivities.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        infoPanel.add(infoLabel, gridBagConstraints);
        cancelAllButton.setText(labels.getString("ActivityWindow.cancelAll.text"));
        cancelAllButton.setEnabled(false);
        cancelAllButton.addActionListener(formListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        infoPanel.add(cancelAllButton, gridBagConstraints);
        getContentPane().add(infoPanel, java.awt.BorderLayout.NORTH);
        strutPanel.setPreferredSize(new java.awt.Dimension(400, 0));
        strutPanel.setLayout(null);
        getContentPane().add(strutPanel, java.awt.BorderLayout.SOUTH);
        viewPanel.setLayout(new java.awt.BorderLayout());
        viewPanel.add(separator, java.awt.BorderLayout.NORTH);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        viewPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
        getContentPane().add(viewPanel, java.awt.BorderLayout.CENTER);
        pack();
    }

    private class FormListener implements java.awt.event.ActionListener, java.awt.event.ItemListener {

        FormListener() {
        }

        public void actionPerformed(java.awt.event.ActionEvent evt) {
            if (evt.getSource() == cancelAllButton) {
                JActivityWindow.this.cancelAll(evt);
            }
        }

        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            if (evt.getSource() == disclosureToggle) {
                JActivityWindow.this.disclosureStateChanged(evt);
            }
        }
    }

    private void disclosureStateChanged(java.awt.event.ItemEvent evt) {
        viewPanel.setVisible(disclosureToggle.isSelected());
        pack();
    }

    private void cancelAll(java.awt.event.ActionEvent evt) {
        Component[] components = progressPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JActivityView) {
                ((JActivityView) components[i]).getModel().cancel();
            }
        }
    }

    private javax.swing.JButton cancelAllButton;

    private javax.swing.JToggleButton disclosureToggle;

    private javax.swing.JLabel infoLabel;

    private javax.swing.JPanel infoPanel;

    private javax.swing.JScrollPane scrollPane;

    private javax.swing.JSeparator separator;

    private javax.swing.JPanel strutPanel;

    private javax.swing.JPanel viewPanel;
}
