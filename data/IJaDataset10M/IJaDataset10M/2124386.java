package my.LifeRockEditor;

/**
 *
 * @author timwestlake
 */
public class editSkillUI extends javax.swing.JFrame {

    Skill skillToEdit = new Skill();

    FileFrameUI parentForm = new FileFrameUI();

    /** Creates new form editSkillUI */
    public editSkillUI() {
        initComponents();
    }

    public editSkillUI(Skill skillParameter, FileFrameUI parentFormParameter) {
        initComponents();
        parentForm = parentFormParameter;
        skillToEdit = skillParameter;
        skillName.setText(skillToEdit.getSkillName());
        strain.setSelectedIndex(skillToEdit.getStrain());
        reference.setText(skillToEdit.getReference());
        edVersion.setSelectedItem(skillToEdit.getEdVersion());
        actionType.setSelectedItem(skillToEdit.getActionType());
        associatedAttribute.setSelectedItem(skillToEdit.getAssociatedAttribute());
        defaultSkill.setSelected(skillToEdit.getDefaultSkill());
        isOfficial.setSelected(skillToEdit.isOfficial());
    }

    public editSkillUI(Skill skillParameter) {
        initComponents();
        skillToEdit = skillParameter;
        skillName.setText(skillToEdit.getSkillName());
        strain.setSelectedItem(skillToEdit.getStrain());
        reference.setText(skillToEdit.getReference());
        edVersion.setSelectedItem(skillToEdit.getEdVersion());
        actionType.setSelectedItem(skillToEdit.getActionType());
        associatedAttribute.setSelectedItem(skillToEdit.getAssociatedAttribute());
        defaultSkill.setSelected(skillToEdit.getDefaultSkill());
        isOfficial.setSelected(skillToEdit.isOfficial());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        skillName = new javax.swing.JTextField();
        defaultSkill = new javax.swing.JCheckBox();
        strain = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        associatedAttribute = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        edVersion = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        reference = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        debugText = new javax.swing.JTextField();
        exitButton = new javax.swing.JButton();
        isOfficial = new javax.swing.JCheckBox();
        actionType = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jLabel1.setText("Skill Name");
        defaultSkill.setText("Default Skill?");
        strain.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6" }));
        jLabel2.setText("Strain");
        jLabel3.setText("Associated Attribute");
        associatedAttribute.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Strength", "Dexterity", "Toughness", "Perception", "Willpower", "Charisma" }));
        jLabel4.setText("Earthdawn Verison");
        edVersion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "Earthdawn 1st Edition", "Earthdawn Classic", "Earthdawn 2nd Edition", "Earthdawn 3rd Edition" }));
        jLabel5.setText("Reference");
        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        debugText.setEditable(false);
        debugText.setEnabled(false);
        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        isOfficial.setText("Official?");
        actionType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Standard", "Simple", "Sustained", "Free", "Not Applicable" }));
        jLabel6.setText("Action Type");
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jLabel1).add(isOfficial)).add(layout.createSequentialGroup().add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(strain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(7, 7, 7).add(skillName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(defaultSkill).add(55, 55, 55)))).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(org.jdesktop.layout.GroupLayout.LEADING, debugText).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().add(saveButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(exitButton)).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().add(jLabel5).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(reference, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 173, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().add(jLabel4).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(edVersion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel3).add(jLabel6)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 70, Short.MAX_VALUE).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(actionType, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(associatedAttribute, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))).add(20, 20, 20)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(37, 37, 37).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(skillName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel1)).add(18, 18, 18).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(strain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel2).add(defaultSkill)).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(isOfficial).add(11, 11, 11).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(actionType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel6)).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3).add(associatedAttribute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4).add(edVersion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(4, 4, 4).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel5).add(reference, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(1, 1, 1).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(saveButton).add(exitButton)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(debugText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        skillToEdit.setSkillName(skillName.getText());
        skillToEdit.setStrain(strain.getSelectedIndex());
        skillToEdit.setDefaultSkill(defaultSkill.isSelected());
        skillToEdit.setIsOfficial(isOfficial.isSelected());
        skillToEdit.setReference(reference.getText());
        skillToEdit.setEdVersion(String.valueOf(edVersion.getSelectedItem()));
        skillToEdit.setActionType(String.valueOf(actionType.getSelectedItem()));
        skillToEdit.setAssociatedAttribute(String.valueOf(associatedAttribute.getSelectedItem()));
        parentForm.refreshObjectListBox();
    }

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        this.dispose();
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new editSkillUI().setVisible(true);
            }
        });
    }

    private javax.swing.JComboBox actionType;

    private javax.swing.JComboBox associatedAttribute;

    private javax.swing.JTextField debugText;

    private javax.swing.JCheckBox defaultSkill;

    private javax.swing.JComboBox edVersion;

    private javax.swing.JButton exitButton;

    private javax.swing.JCheckBox isOfficial;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JTextField reference;

    private javax.swing.JButton saveButton;

    private javax.swing.JTextField skillName;

    private javax.swing.JComboBox strain;
}
