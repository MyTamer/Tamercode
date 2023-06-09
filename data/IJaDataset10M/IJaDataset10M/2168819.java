package GUI;

/**
 *
 * @author haris
 */
public class AddUserView extends javax.swing.JFrame {

    /** Creates new form AddUserView */
    public AddUserView() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        usernameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        repeatPasswordLabel = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();
        usernametextField = new javax.swing.JTextField();
        passwordPasswordField = new javax.swing.JPasswordField();
        repeatPasswordPasswordField = new javax.swing.JPasswordField();
        userRoleLabel = new javax.swing.JLabel();
        userRoleComboBox = new javax.swing.JComboBox();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(GUI.InfraRedApp.class).getContext().getResourceMap(AddUserView.class);
        usernameLabel.setText(resourceMap.getString("usernameLabel.text"));
        usernameLabel.setName("usernameLabel");
        passwordLabel.setText(resourceMap.getString("passwordLabel.text"));
        passwordLabel.setName("passwordLabel");
        repeatPasswordLabel.setText(resourceMap.getString("repeatPasswordLabel.text"));
        repeatPasswordLabel.setName("repeatPasswordLabel");
        addButton.setText(resourceMap.getString("addButton.text"));
        addButton.setName("addButton");
        usernametextField.setText(resourceMap.getString("usernametextField.text"));
        usernametextField.setName("usernametextField");
        passwordPasswordField.setText(resourceMap.getString("passwordPasswordField.text"));
        passwordPasswordField.setName("passwordPasswordField");
        repeatPasswordPasswordField.setText(resourceMap.getString("repeatPasswordPasswordField.text"));
        repeatPasswordPasswordField.setName("repeatPasswordPasswordField");
        userRoleLabel.setText(resourceMap.getString("userRoleLabel.text"));
        userRoleLabel.setName("userRoleLabel");
        userRoleComboBox.setName("userRoleComboBox");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(86, 86, 86).addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(97, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(userRoleLabel).addComponent(passwordLabel).addComponent(usernameLabel).addComponent(repeatPasswordLabel)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(userRoleComboBox, 0, 112, Short.MAX_VALUE).addComponent(repeatPasswordPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE).addComponent(passwordPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE).addComponent(usernametextField, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)).addGap(33, 33, 33)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(28, 28, 28).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(usernameLabel).addComponent(usernametextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(userRoleLabel).addComponent(userRoleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(passwordLabel).addComponent(passwordPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(repeatPasswordLabel).addComponent(repeatPasswordPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(addButton).addGap(23, 23, 23)));
        pack();
    }

    private javax.swing.JButton addButton;

    private javax.swing.JLabel passwordLabel;

    private javax.swing.JPasswordField passwordPasswordField;

    private javax.swing.JLabel repeatPasswordLabel;

    private javax.swing.JPasswordField repeatPasswordPasswordField;

    private javax.swing.JComboBox userRoleComboBox;

    private javax.swing.JLabel userRoleLabel;

    private javax.swing.JLabel usernameLabel;

    private javax.swing.JTextField usernametextField;
}
