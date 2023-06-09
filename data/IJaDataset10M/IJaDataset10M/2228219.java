package simplejavaeditor;

import java.awt.event.*;

/**
 *
 * @author Kyle
 */
public class Log extends javax.swing.JFrame {

    /** Creates new form Log */
    public Log() {
        initComponents();
        jTextPane1.setText(SimpleJavaEditor.log);
        jTextPane2.setText(SimpleJavaEditor.compLog);
        ActionListener taskPerformer = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                int sV = jScrollPane1.getVerticalScrollBar().getValue();
                System.out.println("sV=" + sV);
                jTextPane1.setText(SimpleJavaEditor.log);
                jTextPane2.setText(SimpleJavaEditor.compLog);
                if (sV + jScrollPane1.getVerticalScrollBar().getMaximum() / 10 >= jScrollPane1.getVerticalScrollBar().getMaximum()) {
                    System.out.println("max!");
                } else {
                    System.out.println("NOT MAX");
                    jScrollPane1.getVerticalScrollBar().setValue(sV);
                    jScrollPane1.setVerticalScrollBar(jScrollPane1.getVerticalScrollBar());
                }
            }
        };
        new javax.swing.Timer(500, taskPerformer).start();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Log Files");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jTextPane1.setEditable(false);
        jScrollPane1.setViewportView(jTextPane1);
        jTabbedPane1.addTab("All logs", jScrollPane1);
        jTextPane2.setBorder(null);
        jTextPane2.setEditable(false);
        jScrollPane2.setViewportView(jTextPane2);
        jTabbedPane1.addTab("Compiler logs", jScrollPane2);
        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 570));
        pack();
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Log().setVisible(true);
            }
        });
    }

    public javax.swing.JScrollPane jScrollPane1;

    public javax.swing.JScrollPane jScrollPane2;

    public javax.swing.JTabbedPane jTabbedPane1;

    public javax.swing.JTextPane jTextPane1;

    public javax.swing.JTextPane jTextPane2;
}
