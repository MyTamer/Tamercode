package org.DragonPokerClient.graphic;

import javax.swing.DefaultListModel;
import org.DragonPokerClient.Client;
import org.Comunicator.Element;
import org.Comunicator.Message;

/**
 *
 * @author gonçalo.margalho
 */
public class prova extends javax.swing.JFrame {

    /** Creates new form prova */
    public prova() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ListRooms = new javax.swing.JList();
        jButton2 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        ListRooms.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        jScrollPane1.setViewportView(ListRooms);
        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jButton1).addComponent(jButton2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(351, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addComponent(jButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2))).addContainerGap(147, Short.MAX_VALUE)));
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        Message m = new Message();
        m.setAction("Rooms");
        Client.sendMessage(m);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        Message m = new Message();
        m.setAction("CreateRoom");
        m.getParameters().add(new Element("Name", "Test"));
        Client.sendMessage(m);
        m = new Message();
        m.setAction("Rooms");
        Client.sendMessage(m);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new prova().setVisible(true);
            }
        });
    }

    public void loadRooms(Message m) {
        DefaultListModel dlm = new DefaultListModel();
        for (Element e : m.getParameters()) {
            System.out.println(e.getTitle() + e.getValue());
            if ("Name".equals(e.getTitle())) {
                dlm.addElement(e.getValue());
            }
        }
        ListRooms.setModel(dlm);
    }

    private javax.swing.JList ListRooms;

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JScrollPane jScrollPane1;
}