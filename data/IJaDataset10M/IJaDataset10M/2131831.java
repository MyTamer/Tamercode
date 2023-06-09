package Interfaz;

import aml.Main;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  Administrador
 */
public class AjustarCantTeoricaMateriaPrima extends javax.swing.JFrame {

    private void centrarVentana() {
        this.pack();
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
    }

    Vector inv = new Vector();

    /** Creates new form AjustarCantTeoricaMateriaPrima */
    public AjustarCantTeoricaMateriaPrima() {
        initComponents();
        centrarVentana();
        setCursor(Main.control.hourglassCursor);
        inv = Main.control.obtenerInventarioMP();
        DefaultTableModel modelo = (DefaultTableModel) this.tabla.getModel();
        int size = (inv.size() / 5);
        int codMP = 0;
        int MP = 1;
        int pres = 2;
        int teo = 3;
        int fis = 4;
        for (int i = 0; i < size; i++) {
            modelo.addRow(new Object[] { inv.get(codMP), inv.get(MP), inv.get(pres), inv.get(teo), inv.get(fis) });
            codMP = codMP + 5;
            MP = MP + 5;
            pres = pres + 5;
            teo = teo + 5;
            fis = fis + 5;
        }
        setCursor(Main.control.normalCursor);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        realizar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nueva = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        desc = new javax.swing.JTextArea();
        aplicar = new javax.swing.JButton();
        cancelar = new javax.swing.JButton();
        jMenuBar16 = new javax.swing.JMenuBar();
        menu5 = new javax.swing.JMenu();
        salir3 = new javax.swing.JMenu();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ajustar Cantidad Teorica del inventario de Materia Prima ");
        tabla.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "Codigo Materia Prima", "Nombre Materia prima", "Presentacion", "Cantidad Teorica", "Cantidad Fisica", "Nueva Cantidad Fisica", "Descripción" }) {

            Class[] types = new Class[] { java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };

            boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tabla);
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Ajustar Cantidad Física del inventario de Materia Prima ");
        realizar.setText("Realizar Cambios");
        realizar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                realizarActionPerformed(evt);
            }
        });
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setText("Seleccione de la tabla la materia prima a la cual le quiere modificar la cantidad teorica:");
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Introduzca la nueva cantidad fisica y la descripcion del cambio:");
        jLabel4.setText("Nueva cantidad fisica:");
        jLabel5.setText("Descripción:");
        nueva.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                nuevaKeyTyped(evt);
            }
        });
        desc.setColumns(20);
        desc.setRows(5);
        desc.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                descKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(desc);
        aplicar.setText("Aplicar Cambios");
        aplicar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aplicarActionPerformed(evt);
            }
        });
        cancelar.setText("Cancelar");
        cancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });
        menu5.setText("Menu principal");
        menu5.setPreferredSize(new java.awt.Dimension(99, 16));
        menu5.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu5jMenu1MouseClicked(evt);
            }
        });
        jMenuBar16.add(menu5);
        salir3.setText("Salir del sistema");
        salir3.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salir3jMenu2MouseClicked(evt);
            }
        });
        jMenuBar16.add(salir3);
        setJMenuBar(jMenuBar16);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 895, Short.MAX_VALUE).addGap(30, 30, 30)).addGroup(layout.createSequentialGroup().addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 915, Short.MAX_VALUE).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(realizar).addGap(79, 79, 79).addComponent(cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(318, 318, 318)))).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 886, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGap(191, 191, 191).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel4).addComponent(jLabel5)).addGap(43, 43, 43).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(nueva).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(77, 77, 77).addComponent(aplicar).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(39, 39, 39)).addGroup(layout.createSequentialGroup().addGap(21, 21, 21).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 895, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(19, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGap(18, 18, 18).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(nueva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel5))).addGroup(layout.createSequentialGroup().addGap(38, 38, 38).addComponent(aplicar))).addGap(27, 27, 27).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(realizar).addComponent(cancelar)).addGap(47, 47, 47)));
        pack();
    }

    private void realizarActionPerformed(java.awt.event.ActionEvent evt) {
        setCursor(Main.control.hourglassCursor);
        int row = tabla.getRowCount();
        int column = tabla.getColumnCount();
        String[][] inventario = new String[row][4];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < 4; j++) {
                inventario[i][j] = null;
            }
        }
        Float dif = new Float(0);
        Float cfm = null;
        Float cf = null;
        String a;
        for (int i = 0; i < row; i++) {
            int j = 0;
            int k = 0;
            inventario[i][k] = (String) tabla.getValueAt(i, j);
            k = k + 1;
            j = j + 5;
            if (tabla.getValueAt(i, j) == null) {
                inventario[i][k + 1] = null;
                dif = new Float(0);
            } else {
                inventario[i][k] = tabla.getValueAt(i, j).toString();
                cfm = Float.parseFloat(inventario[i][k]);
                a = (String) tabla.getValueAt(i, j - 1);
                cf = Float.parseFloat(a);
                dif = cfm - cf;
                inventario[i][k + 1] = dif.toString();
                k = k + 2;
                j = j + 1;
                if (tabla.getValueAt(i, j) == null) {
                    inventario[i][k] = "";
                } else {
                    inventario[i][k] = (String) tabla.getValueAt(i, j);
                }
            }
        }
        int opcion = JOptionPane.showConfirmDialog(this, "Seguro que desea realizar los cambios?", "Confirmacion", JOptionPane.YES_NO_OPTION);
        setCursor(Main.control.normalCursor);
        if (opcion == 0) {
            setCursor(Main.control.hourglassCursor);
            Main.control.datosCantidadTeoricaMP(inventario, row);
            setCursor(Main.control.normalCursor);
            this.setVisible(false);
        }
    }

    private void nuevaKeyTyped(java.awt.event.KeyEvent evt) {
        Character letra = evt.getKeyChar();
        javax.swing.JTextField campo = (JTextField) evt.getSource();
        int pos = nueva.getCaretPosition();
        if ((Main.control.validarSoloNumeros2(letra, pos) == false) || (campo.getText().length() == 5) && (campo.getSelectedText() == null)) {
            evt.consume();
        }
    }

    private void descKeyTyped(java.awt.event.KeyEvent evt) {
        javax.swing.JTextArea campo = (JTextArea) evt.getSource();
        if ((campo.getText().length() == 100) && (campo.getSelectedText() == null)) {
            evt.consume();
        }
    }

    private void aplicarActionPerformed(java.awt.event.ActionEvent evt) {
        if (tabla.getSelectedRow() == -1) {
            alertas.seleccionarMP();
        } else {
            if (nueva.getText().isEmpty()) {
                alertas.ingreseCantidad();
            } else {
                setCursor(Main.control.hourglassCursor);
                int row = tabla.getSelectedRow();
                if (tabla.getValueAt(row, 2).equals("rollo")) {
                    tabla.setValueAt(Float.parseFloat(nueva.getText()), row, 5);
                    tabla.setValueAt(desc.getText(), row, 6);
                    nueva.setText("");
                    desc.setText("");
                } else {
                    String[] aux = { "", "" };
                    String a = nueva.getText().toString();
                    aux = nueva.getText().toString().split("\\.");
                    setCursor(Main.control.normalCursor);
                    if (aux.length == 2) {
                        alertas.noPuedeserDecimal();
                    } else {
                        tabla.setValueAt(Integer.parseInt(nueva.getText()), row, 5);
                        tabla.setValueAt(desc.getText(), row, 6);
                        nueva.setText("");
                        desc.setText("");
                    }
                }
            }
        }
        setCursor(Main.control.normalCursor);
    }

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {
        int opcion = JOptionPane.showConfirmDialog(this, "Seguro que desea ir al menu principal?", "Confirmacion", JOptionPane.YES_NO_OPTION);
        if (opcion == 0) {
            this.setVisible(false);
            Main.menu.setEnabled(true);
        }
    }

    private void menu5jMenu1MouseClicked(java.awt.event.MouseEvent evt) {
        int opcion = JOptionPane.showConfirmDialog(this, "Seguro que desea ir al menu principal y descartar el ajuste?", "Confirmacion", JOptionPane.YES_NO_OPTION);
        if (opcion == 0) {
            this.setVisible(false);
            Main.menu.setEnabled(true);
        }
    }

    private void salir3jMenu2MouseClicked(java.awt.event.MouseEvent evt) {
        int opcion = JOptionPane.showConfirmDialog(this, "Seguro que desea salir del sistema y descartar el ajuste?", "Confirmacion", JOptionPane.YES_NO_OPTION);
        if (opcion == 0) {
            this.setVisible(false);
            System.exit(1);
        }
    }

    private javax.swing.JButton aplicar;

    private javax.swing.JButton cancelar;

    private javax.swing.JTextArea desc;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JMenuBar jMenuBar16;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JMenu menu5;

    private javax.swing.JTextField nueva;

    private javax.swing.JButton realizar;

    private javax.swing.JMenu salir3;

    private javax.swing.JTable tabla;
}
