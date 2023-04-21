package gui;

import bussiness.AsignarRedInterface;
import bussiness.PoolLogic;
import bussiness.RedLogic;
import connection.Ip;
import connection.Pool;
import connection.Red;
import connection.RedPublica;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.jdesktop.application.Action;
import paqueteComponent.DefaultMutableTreeIPNode;
import paqueteComponent.ListBD;

/**
 *
 * @author  ciga
 */
public class BusquedaRedGUI extends javax.swing.JDialog {

    private Statement statement;

    private Connection con;

    private AsignarRedInterface inter;

    private Ip publica;

    private DefaultMutableTreeIPNode nodo_sel;

    /** Creates new form BusquedaRedGUI */
    public BusquedaRedGUI(java.awt.Frame parent, boolean modal, Connection con, AsignarRedInterface inter) {
        super(parent, modal);
        try {
            this.inter = inter;
            this.con = con;
            statement = this.con.createStatement();
            initComponents();
        } catch (SQLException ex) {
            Logger.getLogger(BusquedaRedGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel2 = new javax.swing.JPanel();
        txtBuscar = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        try {
            lstPools = new ListBD(statement, "select nombre from pool");
        } catch (SQLException ex) {
        }
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form");
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Asignacion de  Ip Publica"));
        jPanel2.setName("jPanel2");
        txtBuscar.setName("txtBuscar");
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });
        jScrollPane2.setName("jScrollPane2");
        lstPools.setName("lstPools");
        lstPools.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstPoolsValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lstPools);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ipmanager.IPManagerApp.class).getContext().getResourceMap(BusquedaRedGUI.class);
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        jScrollPane1.setName("jScrollPane1");
        jTree1.setModel(null);
        jTree1.setName("jTree1");
        jTree1.setRootVisible(false);
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))).addContainerGap()));
        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
        jPanel1.setName("jPanel1");
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(ipmanager.IPManagerApp.class).getContext().getActionMap(BusquedaRedGUI.class, this);
        jButton1.setAction(actionMap.get("aceptar"));
        jButton1.setText(resourceMap.getString("jButton1.text"));
        jButton1.setName("jButton1");
        jPanel1.add(jButton1);
        jButton2.setAction(actionMap.get("cancelar"));
        jButton2.setText(resourceMap.getString("jButton2.text"));
        jButton2.setName("jButton2");
        jPanel1.add(jButton2);
        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            seleccionarDefault();
            return;
        }
        try {
            ((ListBD) lstPools).update("select nombre from pool where nombre like ?", true, new String[] { txtBuscar.getText() + "%" });
        } catch (SQLException ex) {
            Logger.getLogger(PoolGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void lstPoolsValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting()) {
            return;
        }
        Pool pool = new Pool();
        pool.setNombre((String) lstPools.getSelectedValue());
        cargarRed(PoolLogic.buscarParent(pool, con));
    }

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        seleccionarDefault();
    }

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        if (node == null) {
            nodo_sel = null;
            return;
        }
        if (node instanceof DefaultMutableTreeIPNode) {
            nodo_sel = (DefaultMutableTreeIPNode) node;
        }
    }

    private void seleccionarDefault() {
        lstPools.setSelectedIndex(0);
    }

    private void cargarRed(List<Red> redes) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        DefaultTreeModel modelo = new DefaultTreeModel(root);
        for (Red red : redes) {
            DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(red.getRed());
            if (RedLogic.hasSubnet(red, con, true).isSubNet()) {
                Iterator<Red> sreds = red.getSubred().iterator();
                while (sreds.hasNext()) {
                    RedPublica sredpublica = (RedPublica) sreds.next();
                    DefaultMutableTreeNode subred = new DefaultMutableTreeNode(sredpublica.getRed());
                    PoolLogic.getChildForNetwork(con, sredpublica);
                    Iterator<Ip> iterator = sredpublica.getIp().iterator();
                    while (iterator.hasNext()) {
                        Ip ip = (Ip) iterator.next();
                        DefaultMutableTreeIPNode ipnodo = new DefaultMutableTreeIPNode(ip);
                        subred.add(ipnodo);
                    }
                    nodo.add(subred);
                }
            } else {
                Iterator<Ip> ips = red.getIp().iterator();
                DefaultMutableTreeIPNode ipnodo;
                while (ips.hasNext()) {
                    Ip ip = (Ip) ips.next();
                    ipnodo = new DefaultMutableTreeIPNode(ip);
                    nodo.add(ipnodo);
                }
            }
            root.add(nodo);
        }
        jTree1.setModel(modelo);
    }

    @Action
    public void cancelar() {
        this.dispose();
    }

    @Action
    public void aceptar() {
        if (nodo_sel.getIp() != null) inter.asignarPublica(nodo_sel.getIp());
        this.dispose();
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JTree jTree1;

    private javax.swing.JList lstPools;

    private javax.swing.JTextField txtBuscar;
}