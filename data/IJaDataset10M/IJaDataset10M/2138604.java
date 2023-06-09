package gvo.image.try1;

/** Instances of this class create internal frames to display given images.
*/
public class ImageFrame extends javax.swing.JInternalFrame {

    /** ImageFrame constructor.
    * It creates new internal frame containing the given image and displays it.
    */
    public ImageFrame(String imageName) {
        initComponents();
        setTitle(imageName);
        imageLabel.setIcon(new javax.swing.ImageIcon(imageName));
    }

    /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the FormEditor.
    */
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        imageLabel = new javax.swing.JLabel();
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        getAccessibleContext().setAccessibleName("Image Internal Frame");
        getAccessibleContext().setAccessibleDescription("Image internal frame.");
        jScrollPane1.setViewportView(imageLabel);
        imageLabel.getAccessibleContext().setAccessibleName("Image Label");
        imageLabel.getAccessibleContext().setAccessibleDescription("Image label.");
        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }

    private javax.swing.JLabel imageLabel;

    private javax.swing.JScrollPane jScrollPane1;
}
