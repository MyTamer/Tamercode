package GUI;

/**
 *
 * @author haris
 */
public class ExperimentCollectionSelectionView extends javax.swing.JFrame {

    /** Creates new form ExperimentCollectionSelectionView */
    public ExperimentCollectionSelectionView() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        searchCollectionsTextField = new javax.swing.JTextField();
        searchCollectionsLabel = new javax.swing.JLabel();
        collectionScrollPane = new javax.swing.JScrollPane();
        collectionList = new javax.swing.JList();
        cancelButton = new javax.swing.JButton();
        searchButton = new javax.swing.JButton();
        openButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form");
        searchCollectionsTextField.setName("searchCollectionsTextField");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(GUI.InfraRedApp.class).getContext().getResourceMap(ExperimentCollectionSelectionView.class);
        searchCollectionsLabel.setText(resourceMap.getString("searchCollectionsLabel.text"));
        searchCollectionsLabel.setName("searchCollectionsLabel");
        collectionScrollPane.setName("collectionScrollPane");
        collectionList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        collectionList.setName("collectionList");
        collectionScrollPane.setViewportView(collectionList);
        cancelButton.setText(resourceMap.getString("cancelButton.text"));
        cancelButton.setName("cancelButton");
        searchButton.setText(resourceMap.getString("searchButton.text"));
        searchButton.setName("searchButton");
        openButton.setText(resourceMap.getString("openButton.text"));
        openButton.setName("openButton");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20, 20, 20).addComponent(searchCollectionsLabel).addGap(18, 18, 18).addComponent(searchCollectionsTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE).addGap(31, 31, 31).addComponent(searchButton).addContainerGap(114, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(collectionScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE).addContainerGap()).addGroup(layout.createSequentialGroup().addGap(116, 116, 116).addComponent(openButton, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(32, 32, 32).addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(8, 8, 8).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(searchCollectionsLabel).addComponent(searchCollectionsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(searchButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(collectionScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(openButton).addComponent(cancelButton)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private javax.swing.JButton cancelButton;

    private javax.swing.JList collectionList;

    private javax.swing.JScrollPane collectionScrollPane;

    private javax.swing.JButton openButton;

    private javax.swing.JButton searchButton;

    private javax.swing.JLabel searchCollectionsLabel;

    private javax.swing.JTextField searchCollectionsTextField;
}
