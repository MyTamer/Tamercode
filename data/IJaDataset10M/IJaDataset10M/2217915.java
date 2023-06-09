package org.paja.group.capa.visual.casos.paneles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.paja.group.capa.visual.casos.paneles.PanelAdjunto;
import org.paja.group.util.capa.visual.util.JOptionPaneLoader;

/**
 *
 * @author  Claver Isaac
 */
public class PanelListaAdjunto extends javax.swing.JPanel {

    private String nombre;

    private PanelAdjunto panelPadre;

    private byte[] data;

    /** Creates new form PanelListaAdjunto */
    public PanelListaAdjunto(String nombre, PanelAdjunto panelPadre) {
        this.nombre = nombre;
        this.panelPadre = panelPadre;
        initComponents();
        lblNombre.setText(nombre);
        lblNombre.setIcon(PanelAdjunto.establecerIcono(nombre, true));
    }

    public PanelListaAdjunto(String nombre, byte[] data) {
        this.nombre = nombre;
        this.data = data;
        initComponents();
        lblNombre.setText(nombre);
        jLinkButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/bajar.png")));
        lblNombre.setIcon(PanelAdjunto.establecerIcono(nombre, true));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jFileChooser1 = new javax.swing.JFileChooser();
        lblNombre = new javax.swing.JLabel();
        jLinkButton1 = new com.l2fprod.common.swing.JLinkButton();
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setOpaque(false);
        setLayout(new java.awt.BorderLayout());
        lblNombre.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblNombre.setText("Nombre del Adjunto");
        add(lblNombre, java.awt.BorderLayout.CENTER);
        jLinkButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/descartar.png")));
        jLinkButton1.setText("Remover");
        jLinkButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLinkButton1ActionPerformed(evt);
            }
        });
        add(jLinkButton1, java.awt.BorderLayout.LINE_END);
    }

    private void jLinkButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (panelPadre != null) panelPadre.removerAdjunto(this); else {
            int answ = jFileChooser1.showDialog(null, "Guardar Archivo");
            if (answ == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser1.getSelectedFile();
                if (file != null) {
                    try {
                        FileOutputStream output = new FileOutputStream(file);
                        output.write(data);
                        output.close();
                        JOptionPaneLoader.JOptionPane_Information("Archivo guardado con exito!!");
                    } catch (IOException ex) {
                        Logger.getLogger(PanelListaAdjunto.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPaneLoader.JOptionPane_Error("Error al guardar el archivo");
                    }
                }
            }
        }
    }

    private javax.swing.JFileChooser jFileChooser1;

    private com.l2fprod.common.swing.JLinkButton jLinkButton1;

    private javax.swing.JLabel lblNombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
