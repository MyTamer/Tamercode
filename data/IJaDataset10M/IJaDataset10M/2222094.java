package nl.lemval.nododue.dialog;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.jdesktop.application.Action;

/**
 *
 * @author Michael
 */
public class WriteConfigBox extends javax.swing.JDialog {

    private boolean confirmed;

    /** Creates new form WriteConfigBox */
    public WriteConfigBox(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        confirmed = false;
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        javax.swing.JLabel titleLabel = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();
        checkHome = new javax.swing.JCheckBox();
        checkDate = new javax.swing.JCheckBox();
        checkTimers = new javax.swing.JCheckBox();
        checkSettings = new javax.swing.JCheckBox();
        checkOutput = new javax.swing.JCheckBox();
        checkWireConfig = new javax.swing.JCheckBox();
        checkOther = new javax.swing.JCheckBox();
        message = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(nl.lemval.nododue.NodoDueManager.class).getContext().getResourceMap(WriteConfigBox.class);
        setTitle(resourceMap.getString("Form.title"));
        setModal(true);
        setName("Form");
        setResizable(false);
        titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getStyle() | java.awt.Font.BOLD, titleLabel.getFont().getSize() + 4));
        titleLabel.setText(resourceMap.getString("titleLabel.text"));
        titleLabel.setName("titleLabel");
        descriptionLabel.setText(resourceMap.getString("descriptionLabel.text"));
        descriptionLabel.setName("descriptionLabel");
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(nl.lemval.nododue.NodoDueManager.class).getContext().getActionMap(WriteConfigBox.class, this);
        checkHome.setAction(actionMap.get("homeUnitClicked"));
        checkHome.setText(resourceMap.getString("checkHome.text"));
        checkHome.setToolTipText(resourceMap.getString("checkHome.toolTipText"));
        checkHome.setName("checkHome");
        checkHome.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                checkBoxMouseEntered(evt);
            }
        });
        checkHome.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                checkBoxFocusGained(evt);
            }
        });
        checkDate.setSelected(true);
        checkDate.setText(resourceMap.getString("checkDate.text"));
        checkDate.setToolTipText(resourceMap.getString("checkDate.toolTipText"));
        checkDate.setName("checkDate");
        checkDate.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                checkBoxMouseEntered(evt);
            }
        });
        checkDate.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                checkBoxFocusGained(evt);
            }
        });
        checkTimers.setSelected(true);
        checkTimers.setText(resourceMap.getString("checkTimers.text"));
        checkTimers.setToolTipText(resourceMap.getString("checkTimers.toolTipText"));
        checkTimers.setName("checkTimers");
        checkTimers.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                checkBoxMouseEntered(evt);
            }
        });
        checkTimers.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                checkBoxFocusGained(evt);
            }
        });
        checkSettings.setSelected(true);
        checkSettings.setText(resourceMap.getString("checkSettings.text"));
        checkSettings.setName("checkSettings");
        checkSettings.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                checkBoxMouseEntered(evt);
            }
        });
        checkSettings.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                checkBoxFocusGained(evt);
            }
        });
        checkOutput.setSelected(true);
        checkOutput.setText(resourceMap.getString("checkOutput.text"));
        checkOutput.setName("checkOutput");
        checkOutput.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                checkBoxMouseEntered(evt);
            }
        });
        checkOutput.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                checkBoxFocusGained(evt);
            }
        });
        checkWireConfig.setSelected(true);
        checkWireConfig.setText(resourceMap.getString("checkWireConfig.text"));
        checkWireConfig.setName("checkWireConfig");
        checkWireConfig.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                checkBoxMouseEntered(evt);
            }
        });
        checkWireConfig.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                checkBoxFocusGained(evt);
            }
        });
        checkOther.setSelected(true);
        checkOther.setText(resourceMap.getString("checkOther.text"));
        checkOther.setName("checkOther");
        checkOther.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                checkBoxMouseEntered(evt);
            }
        });
        checkOther.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                checkBoxFocusGained(evt);
            }
        });
        message.setForeground(resourceMap.getColor("message.foreground"));
        message.setText(resourceMap.getString("message.text"));
        message.setFocusable(false);
        message.setName("message");
        message.setRequestFocusEnabled(false);
        jPanel1.setName("jPanel1");
        cancelButton.setAction(actionMap.get("cancel"));
        cancelButton.setText(resourceMap.getString("cancelButton.text"));
        cancelButton.setName("cancelButton");
        jPanel1.add(cancelButton);
        closeButton.setAction(actionMap.get("close"));
        closeButton.setText(resourceMap.getString("closeButton.text"));
        closeButton.setName("closeButton");
        jPanel1.add(closeButton);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(checkWireConfig).addContainerGap()).add(layout.createSequentialGroup().add(titleLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE).add(136, 136, 136)).add(layout.createSequentialGroup().add(descriptionLabel).addContainerGap(43, Short.MAX_VALUE)).add(layout.createSequentialGroup().add(checkOther).addContainerGap(283, Short.MAX_VALUE)).add(checkOutput).add(checkSettings).add(checkTimers).add(checkDate).add(checkHome).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addContainerGap()).add(layout.createSequentialGroup().add(message, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addContainerGap()))));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(titleLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(descriptionLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(checkHome).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(checkDate).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(checkTimers).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(checkSettings).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(checkOutput).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(checkWireConfig).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(checkOther).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(message).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)));
        pack();
    }

    private void checkBoxFocusGained(java.awt.event.FocusEvent evt) {
        Component c = evt.getComponent();
        if (c instanceof JCheckBox) {
            JCheckBox box = (JCheckBox) c;
            message.setText(box.getToolTipText());
        }
    }

    private void checkBoxMouseEntered(java.awt.event.MouseEvent evt) {
        Component c = evt.getComponent();
        if (c instanceof JCheckBox) {
            JCheckBox box = (JCheckBox) c;
            message.setText(box.getToolTipText());
        }
    }

    @Action
    public void cancel() {
        dispose();
    }

    @Action
    public void close() {
        confirmed = true;
        dispose();
    }

    private javax.swing.JButton cancelButton;

    private javax.swing.JCheckBox checkDate;

    private javax.swing.JCheckBox checkHome;

    private javax.swing.JCheckBox checkOther;

    private javax.swing.JCheckBox checkOutput;

    private javax.swing.JCheckBox checkSettings;

    private javax.swing.JCheckBox checkTimers;

    private javax.swing.JCheckBox checkWireConfig;

    private javax.swing.JButton closeButton;

    private javax.swing.JLabel descriptionLabel;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JLabel message;

    public boolean isConfirmed() {
        return confirmed;
    }

    public boolean includeDateFunctions() {
        return checkDate.isSelected();
    }

    public boolean includeUnitFunction() {
        return checkHome.isSelected();
    }

    public boolean includeOutputFunctions() {
        return checkOutput.isSelected();
    }

    public boolean includeOtherFunctions() {
        return checkOther.isSelected();
    }

    public boolean includeSettings() {
        return checkSettings.isSelected();
    }

    public boolean includeTimers() {
        return checkTimers.isSelected();
    }

    public boolean includeWireFunctions() {
        return checkWireConfig.isSelected();
    }

    @Action
    public void homeUnitClicked() {
        if (checkHome.isSelected()) {
            checkWireConfig.setSelected(false);
            checkWireConfig.setEnabled(false);
            checkDate.setSelected(false);
            checkDate.setEnabled(false);
            checkTimers.setSelected(false);
            checkTimers.setEnabled(false);
            checkSettings.setSelected(false);
            checkSettings.setEnabled(false);
            checkOutput.setSelected(false);
            checkOutput.setEnabled(false);
            checkOther.setSelected(false);
            checkOther.setEnabled(false);
        } else {
            checkWireConfig.setEnabled(true);
            checkDate.setEnabled(true);
            checkTimers.setEnabled(true);
            checkSettings.setEnabled(true);
            checkOutput.setEnabled(true);
            checkOther.setEnabled(true);
        }
    }
}
