package projeto.view;

import javax.swing.JTable;
import projeto.controller.ComparaRecomendacao;
import projeto.model.RecomendaCosseno;
import projeto.model.RecomendaEscalar;
import projeto.model.RecomendaPopularidade;

/**
 *
 * @author Walter Alves
 */
public class PainelComparaAlgoritmos extends javax.swing.JPanel {

    protected static boolean aberto = false;

    private static javax.swing.table.DefaultTableModel modelo;

    /** Creates new form PainelComparaAlgoritmos */
    private String[][] dados = new String[0][0];

    private static String[] novosDados = new String[2];

    private static double porcentagem;

    private static int qntd_rec;

    public PainelComparaAlgoritmos(int qntd_rec) {
        initComponents();
        this.qntd_rec = qntd_rec;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jSeparator1 = new javax.swing.JSeparator();
        tituloCadastroUsuario = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        setPreferredSize(new java.awt.Dimension(780, 550));
        tituloCadastroUsuario.setFont(new java.awt.Font("Tahoma", 1, 18));
        tituloCadastroUsuario.setText("Comparação de Algoritmos");
        modelo = new javax.swing.table.DefaultTableModel(dados, new String[] { "Algoritmo", "Percentual de acerto" }) {

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        jTable1.setModel(modelo);
        jScrollPane1.setViewportView(jTable1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addGap(267, 267, 267).addComponent(tituloCadastroUsuario)).addGroup(layout.createSequentialGroup().addGap(182, 182, 182).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(tituloCadastroUsuario).addGap(68, 68, 68).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(83, Short.MAX_VALUE)));
    }

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JSeparator jSeparator1;

    private static javax.swing.JTable jTable1;

    private javax.swing.JLabel tituloCadastroUsuario;

    public static void executa() {
        porcentagem = ComparaRecomendacao.acertosRecomendacao(FramePrincipal.estabelecimentos, FramePrincipal.userControl.getListaDeUsuarios(), qntd_rec, new RecomendaEscalar());
        novosDados[0] = "Escalar";
        novosDados[1] = String.format("%.2f", porcentagem);
        modelo.addRow(novosDados);
        porcentagem = ComparaRecomendacao.acertosRecomendacao(FramePrincipal.estabelecimentos, FramePrincipal.userControl.getListaDeUsuarios(), qntd_rec, new RecomendaPopularidade());
        novosDados[0] = "Popularidade";
        novosDados[1] = "" + porcentagem;
        modelo.addRow(novosDados);
        porcentagem = ComparaRecomendacao.acertosRecomendacao(FramePrincipal.estabelecimentos, FramePrincipal.userControl.getListaDeUsuarios(), qntd_rec, new RecomendaCosseno());
        novosDados[0] = "Cosseno";
        novosDados[1] = "" + porcentagem;
        modelo.addRow(novosDados);
        jTable1.repaint();
    }
}