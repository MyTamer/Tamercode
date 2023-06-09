package GUI;

import compresor.ThreadCompress;
import compresor.ThreadDecompress;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Liberal, Peker
 */
public class ventanaPrincipal extends javax.swing.JFrame {

    private File f;

    private boolean bloqueado;

    private GestorVentanaPrincipal gestor;

    ThreadCompress hiloComprimir;

    private enum accion {

        comprimir, descomprimir
    }

    ;

    private accion estado;

    ThreadTiempos hiloTiempos;

    ThreadDecompress hiloDescomprimir;

    private boolean compress_or_descompress;

    /** Creates new form ventanaPrincipal */
    public ventanaPrincipal() {
        try {
            initComponents();
            this.jpbArchivo.setVisible(true);
            this.jpbProceso.setVisible(true);
            this.jpbArchivo.setValue(0);
            this.jpbProceso.setValue(0);
            this.jpbArchivo.setStringPainted(true);
            this.jpbProceso.setStringPainted(true);
            gestor = new GestorVentanaPrincipal(txtArchivo, jpbArchivo, txtTiempoProceso, txtTiempoRestante, lblEstado, jpbProceso);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al inicializar aplicacion", "Error", JOptionPane.ERROR_MESSAGE);
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
        jlPath = new javax.swing.JLabel();
        btnSeleccionar = new javax.swing.JButton();
        txtPath = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jpbArchivo = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        txtArchivo = new javax.swing.JLabel();
        jpbProceso = new javax.swing.JProgressBar();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTiempoProceso = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtTiempoRestante = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        btnComprimir = new javax.swing.JButton();
        btnDescomprimir = new javax.swing.JButton();
        jbtnDetener = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Compresor - Descompresor de Archivos - DLC 2011");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Archivo o Directorio a comprimir: "));
        jlPath.setText("Path:");
        btnSeleccionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/imagenes/search.png")));
        btnSeleccionar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarActionPerformed(evt);
            }
        });
        txtPath.setEditable(false);
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jlPath).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtPath, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jlPath).addComponent(txtPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(btnSeleccionar)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { btnSeleccionar, jlPath, txtPath });
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Progreso:"));
        jLabel1.setText("Archivo:");
        jpbProceso.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jpbProcesoPropertyChange(evt);
            }
        });
        jLabel3.setText("Tiempo de Actividad:");
        jLabel4.setText("Procesado:");
        txtTiempoProceso.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtTiempoProceso.setText("s");
        jLabel7.setText("Tiempo Restante:");
        txtTiempoRestante.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtTiempoRestante.setText("s");
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jpbArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE).addComponent(jpbProceso, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel4).addComponent(jLabel7).addComponent(jLabel3)).addGap(207, 207, 207).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(txtTiempoRestante, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE).addComponent(txtTiempoProceso, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))).addComponent(lblEstado, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(txtArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(lblEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jpbArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(txtTiempoProceso)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(txtTiempoRestante)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jpbProceso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(36, 36, 36)));
        btnComprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/imagenes/zip18x18.png")));
        btnComprimir.setText("Comprimir");
        btnComprimir.setEnabled(false);
        btnComprimir.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComprimirActionPerformed(evt);
            }
        });
        btnDescomprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/imagenes/unarchiver_icon18x18.png")));
        btnDescomprimir.setText("Descomprimir");
        btnDescomprimir.setEnabled(false);
        btnDescomprimir.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescomprimirActionPerformed(evt);
            }
        });
        jbtnDetener.setText("Detener");
        jbtnDetener.setEnabled(false);
        jbtnDetener.setPreferredSize(new java.awt.Dimension(117, 27));
        jbtnDetener.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDetenerActionPerformed(evt);
            }
        });
        jMenu1.setText("Archivo");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenu1MouseEntered(evt);
            }
        });
        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Salir");
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem1MouseClicked(evt);
            }
        });
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1accion(evt);
            }
        });
        jMenu1.add(jMenuItem1);
        jMenuBar1.add(jMenu1);
        jMenu2.setText("Ayuda");
        jMenuItem2.setText("Acerca de");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);
        jMenuBar1.add(jMenu2);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jbtnDetener, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnDescomprimir).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnComprimir).addGap(26, 26, 26)))));
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { btnComprimir, btnDescomprimir });
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnDescomprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jbtnDetener, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)).addComponent(btnComprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap(33, Short.MAX_VALUE)));
        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { btnComprimir, btnDescomprimir });
        jPanel2.getAccessibleContext().setAccessibleName("Archivo o directorio a comprimir: ");
        pack();
    }

    private void jMenuItem1MouseClicked(java.awt.event.MouseEvent evt) {
    }

    private void jMenuItem1accion(java.awt.event.ActionEvent evt) {
        if (bloqueado) {
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Seguro que desea salir ?", "Confirmacion", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION) System.exit(1);
    }

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {
    }

    private void jMenu1MouseEntered(java.awt.event.MouseEvent evt) {
    }

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Realizado por: \n\tLiberal Rodrigo Leg: 51685\n\tPeker, Julian Leg:51935\nUniversidad Tecnológica Nacional - Facultad Regional Córdoba\nCátedra: Diseño de Lenguajes de Consulta 2011");
    }

    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            File aux = null;
            aux = Explorar();
            if (aux != null) {
                f = aux;
                this.txtPath.setText(f.getAbsolutePath());
                String nombre = f.getName();
                String extension = "";
                int inicio = nombre.indexOf(".");
                if (inicio > -1) {
                    extension = nombre.substring(inicio, nombre.length());
                }
                if (extension.compareTo(".clincker") == 0) {
                    this.btnComprimir.setEnabled(false);
                    this.btnDescomprimir.setEnabled(true);
                    compress_or_descompress = false;
                } else {
                    this.btnComprimir.setEnabled(true);
                    this.btnDescomprimir.setEnabled(false);
                    compress_or_descompress = true;
                }
            } else {
                this.btnComprimir.setEnabled(false);
                this.btnDescomprimir.setEnabled(false);
            }
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Seleccione una carpeta", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al leer archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnComprimirActionPerformed(java.awt.event.ActionEvent evt) {
        this.estado = accion.comprimir;
        if (f.exists()) {
            jbtnDetener.setEnabled(true);
            hiloComprimir = new ThreadCompress(f, gestor);
            hiloTiempos = new ThreadTiempos(gestor);
            btnComprimir.setEnabled(false);
            hiloTiempos.start();
            hiloComprimir.start();
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un archivo", "Atencion", JOptionPane.INFORMATION_MESSAGE);
        }
        btnComprimir.setEnabled(false);
    }

    private void btnDescomprimirActionPerformed(java.awt.event.ActionEvent evt) {
        if (f.exists()) {
            jbtnDetener.setEnabled(true);
            hiloDescomprimir = new ThreadDecompress(f, gestor);
            btnDescomprimir.setEnabled(false);
            hiloDescomprimir.start();
        }
        btnDescomprimir.setEnabled(false);
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        if (JOptionPane.showConfirmDialog(this, "Seguro que desea salir ?", "Confirmacion", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION) System.exit(1);
    }

    @SuppressWarnings("static-access")
    private void jbtnDetenerActionPerformed(java.awt.event.ActionEvent evt) {
        jbtnDetener.setEnabled(false);
        if (compress_or_descompress) {
            hiloComprimir.stopRequest();
            hiloComprimir.stopRequested = true;
        } else {
            hiloDescomprimir.stopRequest();
            hiloDescomprimir.stopRequested = true;
        }
    }

    private void jpbProcesoPropertyChange(java.beans.PropertyChangeEvent evt) {
        if (jpbProceso.getValue() == 100) {
            jbtnDetener.setEnabled(false);
        }
    }

    public File Explorar() {
        JFileChooser jfd = new JFileChooser("c:\\temp\\");
        jfd.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int r = jfd.showOpenDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            File arch = jfd.getSelectedFile();
            if (arch != null) {
                return arch;
            }
        }
        return null;
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ventanaPrincipal().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnComprimir;

    private javax.swing.JButton btnDescomprimir;

    private javax.swing.JButton btnSeleccionar;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenu jMenu2;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JMenuItem jMenuItem1;

    private javax.swing.JMenuItem jMenuItem2;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JButton jbtnDetener;

    private javax.swing.JLabel jlPath;

    private javax.swing.JProgressBar jpbArchivo;

    private javax.swing.JProgressBar jpbProceso;

    private javax.swing.JLabel lblEstado;

    private javax.swing.JLabel txtArchivo;

    private javax.swing.JTextField txtPath;

    private javax.swing.JLabel txtTiempoProceso;

    private javax.swing.JLabel txtTiempoRestante;
}
