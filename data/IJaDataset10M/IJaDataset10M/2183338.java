package org.cerg.quincasMarket.painel.mistura;

import org.cerg.quincasMarket.bean.painelJogo.PainelJogo;
import org.cerg.quincasMarket.nagegacao.focusSystem.MyOwnFocusTraversalPolicyToFields;

/**
 *
 * @author diego
 */
public class PainelMisturaProdutos extends PainelJogo {

    /** Creates new form BeanForm */
    public PainelMisturaProdutos() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabelPMPTitulo = new javax.swing.JLabel();
        jLabelPMQuantidadeMinimaItem1 = new javax.swing.JLabel();
        jLabelPMQuantidadeMinimaItem2 = new javax.swing.JLabel();
        jLabelPMQuantidadeMinimaItem3 = new javax.swing.JLabel();
        jLabelPMQuantidadeMinimaItem4 = new javax.swing.JLabel();
        jLabelPMEstoque = new javax.swing.JLabel();
        jLabelPMEstoqueItem1 = new javax.swing.JLabel();
        jLabelPMEstoqueItem2 = new javax.swing.JLabel();
        jLabelPMEstoqueItem3 = new javax.swing.JLabel();
        jLabelPMEstoqueItem4 = new javax.swing.JLabel();
        jLabelPMUnidadesAProduzir = new javax.swing.JLabel();
        jTextFieldPMQtMinimaItem1 = new javax.swing.JTextField();
        jTextFieldPMQtMinimaItem2 = new javax.swing.JTextField();
        jTextFieldPMQtMinimaItem3 = new javax.swing.JTextField();
        jTextFieldPMQtMinimaItem4 = new javax.swing.JTextField();
        jTextFieldPMEstoqueItem1 = new javax.swing.JTextField();
        jTextFieldPMEstoqueItem2 = new javax.swing.JTextField();
        jTextFieldPMEstoqueItem3 = new javax.swing.JTextField();
        jTextFieldPMEstoqueItem4 = new javax.swing.JTextField();
        jTextFieldPMUnidadesASeremProduzir = new javax.swing.JTextField();
        jButtonPMProduzir = new javax.swing.JButton();
        jTextFieldPMAlerta = new javax.swing.JTextField();
        jLabelPMPrecisa = new javax.swing.JLabel();
        setToolTipText("Painel de Mistura");
        setLayout(null);
        jLabelPMPTitulo.setFont(new java.awt.Font("Times New Roman", 0, 24));
        jLabelPMPTitulo.setForeground(new java.awt.Color(207, 60, 60));
        jLabelPMPTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPMPTitulo.setText("Mistura de Produtos");
        jLabelPMPTitulo.setToolTipText("Titulo");
        add(jLabelPMPTitulo);
        jLabelPMPTitulo.setBounds(60, 10, 360, 29);
        jLabelPMPTitulo.getAccessibleContext().setAccessibleDescription("Titulo do Painel Mistura de Produtos");
        jLabelPMQuantidadeMinimaItem1.setText("jLabel1");
        jLabelPMQuantidadeMinimaItem1.setToolTipText("Nome do Primeiro Item.");
        add(jLabelPMQuantidadeMinimaItem1);
        jLabelPMQuantidadeMinimaItem1.setBounds(60, 80, 180, 16);
        jLabelPMQuantidadeMinimaItem2.setText("jLabel2");
        jLabelPMQuantidadeMinimaItem2.setToolTipText("Nome do Segundo Item.");
        add(jLabelPMQuantidadeMinimaItem2);
        jLabelPMQuantidadeMinimaItem2.setBounds(60, 110, 180, 16);
        jLabelPMQuantidadeMinimaItem3.setText("jLabel3");
        jLabelPMQuantidadeMinimaItem3.setToolTipText("Nome do Terceiro Item.");
        add(jLabelPMQuantidadeMinimaItem3);
        jLabelPMQuantidadeMinimaItem3.setBounds(60, 140, 180, 16);
        jLabelPMQuantidadeMinimaItem4.setText("jLabel4");
        jLabelPMQuantidadeMinimaItem4.setToolTipText("Nome do Quarto Item.");
        add(jLabelPMQuantidadeMinimaItem4);
        jLabelPMQuantidadeMinimaItem4.setBounds(60, 170, 180, 16);
        jLabelPMEstoque.setText("Voce têm em estoque:");
        jLabelPMEstoque.setToolTipText("Abaixo Mostra o Quanto Você Tem no Estoque");
        add(jLabelPMEstoque);
        jLabelPMEstoque.setBounds(60, 200, 190, 16);
        jLabelPMEstoqueItem1.setText("jLabel6");
        jLabelPMEstoqueItem1.setToolTipText("Nome do Primeiro Item.");
        add(jLabelPMEstoqueItem1);
        jLabelPMEstoqueItem1.setBounds(60, 226, 180, 20);
        jLabelPMEstoqueItem2.setText("jLabel7");
        jLabelPMEstoqueItem2.setToolTipText("Nome do Segundo Item.");
        add(jLabelPMEstoqueItem2);
        jLabelPMEstoqueItem2.setBounds(60, 256, 180, 20);
        jLabelPMEstoqueItem3.setText("jLabel8");
        jLabelPMEstoqueItem3.setToolTipText("Nome do Terceiro Item.");
        add(jLabelPMEstoqueItem3);
        jLabelPMEstoqueItem3.setBounds(60, 286, 180, 20);
        jLabelPMEstoqueItem4.setText("jLabel9");
        jLabelPMEstoqueItem4.setToolTipText("Nome do Quarto Item.");
        add(jLabelPMEstoqueItem4);
        jLabelPMEstoqueItem4.setBounds(60, 316, 180, 20);
        jLabelPMUnidadesAProduzir.setText("Unidades a serem produzidas:");
        jLabelPMUnidadesAProduzir.setToolTipText("Ao Lado você informará Quantas Unidades Você Desja produzir.");
        add(jLabelPMUnidadesAProduzir);
        jLabelPMUnidadesAProduzir.setBounds(60, 350, 190, 16);
        jTextFieldPMQtMinimaItem1.setEditable(false);
        add(jTextFieldPMQtMinimaItem1);
        jTextFieldPMQtMinimaItem1.setBounds(250, 70, 90, 28);
        jTextFieldPMQtMinimaItem2.setEditable(false);
        add(jTextFieldPMQtMinimaItem2);
        jTextFieldPMQtMinimaItem2.setBounds(250, 100, 90, 28);
        jTextFieldPMQtMinimaItem3.setEditable(false);
        add(jTextFieldPMQtMinimaItem3);
        jTextFieldPMQtMinimaItem3.setBounds(250, 130, 90, 28);
        jTextFieldPMQtMinimaItem4.setEditable(false);
        add(jTextFieldPMQtMinimaItem4);
        jTextFieldPMQtMinimaItem4.setBounds(250, 160, 90, 28);
        jTextFieldPMEstoqueItem1.setEditable(false);
        add(jTextFieldPMEstoqueItem1);
        jTextFieldPMEstoqueItem1.setBounds(250, 220, 90, 28);
        jTextFieldPMEstoqueItem2.setEditable(false);
        add(jTextFieldPMEstoqueItem2);
        jTextFieldPMEstoqueItem2.setBounds(250, 250, 90, 28);
        jTextFieldPMEstoqueItem3.setEditable(false);
        add(jTextFieldPMEstoqueItem3);
        jTextFieldPMEstoqueItem3.setBounds(250, 280, 90, 28);
        jTextFieldPMEstoqueItem4.setEditable(false);
        add(jTextFieldPMEstoqueItem4);
        jTextFieldPMEstoqueItem4.setBounds(250, 310, 90, 28);
        add(jTextFieldPMUnidadesASeremProduzir);
        jTextFieldPMUnidadesASeremProduzir.setBounds(250, 340, 90, 28);
        getMyComponentOrderToMyNewPolice().add(jLabelPMPTitulo);
        getMyComponentOrderToMyNewPolice().add(jLabelPMPrecisa);
        getMyComponentOrderToMyNewPolice().add(jLabelPMQuantidadeMinimaItem1);
        getMyComponentOrderToMyNewPolice().add(jTextFieldPMQtMinimaItem1);
        getMyComponentOrderToMyNewPolice().add(jLabelPMQuantidadeMinimaItem2);
        getMyComponentOrderToMyNewPolice().add(jTextFieldPMQtMinimaItem2);
        getMyComponentOrderToMyNewPolice().add(jLabelPMQuantidadeMinimaItem3);
        getMyComponentOrderToMyNewPolice().add(jTextFieldPMQtMinimaItem3);
        getMyComponentOrderToMyNewPolice().add(jLabelPMQuantidadeMinimaItem4);
        getMyComponentOrderToMyNewPolice().add(jTextFieldPMQtMinimaItem4);
        getMyComponentOrderToMyNewPolice().add(jLabelPMEstoque);
        getMyComponentOrderToMyNewPolice().add(jLabelPMEstoqueItem1);
        getMyComponentOrderToMyNewPolice().add(jTextFieldPMEstoqueItem1);
        getMyComponentOrderToMyNewPolice().add(jLabelPMEstoqueItem2);
        getMyComponentOrderToMyNewPolice().add(jTextFieldPMEstoqueItem2);
        getMyComponentOrderToMyNewPolice().add(jLabelPMEstoqueItem3);
        getMyComponentOrderToMyNewPolice().add(jTextFieldPMEstoqueItem3);
        getMyComponentOrderToMyNewPolice().add(jLabelPMEstoqueItem4);
        getMyComponentOrderToMyNewPolice().add(jTextFieldPMEstoqueItem4);
        getMyComponentOrderToMyNewPolice().add(jLabelPMUnidadesAProduzir);
        getMyComponentOrderToMyNewPolice().add(jTextFieldPMUnidadesASeremProduzir);
        getMyComponentOrderToMyNewPolice().add(jButtonPMProduzir);
        getMyComponentOrderToMyNewPolice().add(jTextFieldPMAlerta);
        setMyNewFocusPolicy(new MyOwnFocusTraversalPolicyToFields(getMyComponentOrderToMyNewPolice()));
        jButtonPMProduzir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/cerg/quincasMarket/resource/imagem/botao/bt_produzir.png")));
        jButtonPMProduzir.setToolTipText("Aperte Aqui para Produzir as Unidades.");
        jButtonPMProduzir.setActionCommand("continuar");
        jButtonPMProduzir.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPMProduzirActionPerformed(evt);
            }
        });
        add(jButtonPMProduzir);
        jButtonPMProduzir.setBounds(130, 410, 170, 40);
        jTextFieldPMAlerta.setEditable(false);
        jTextFieldPMAlerta.setToolTipText("Este é um Campo de Alerta de Erro.");
        add(jTextFieldPMAlerta);
        jTextFieldPMAlerta.setBounds(110, 380, 220, 28);
        jLabelPMPrecisa.setText("Para UMA Unidade você precisa:");
        jLabelPMPrecisa.setToolTipText("Informa o quanto você precisa para obter UMA unidade de seu produto.");
        add(jLabelPMPrecisa);
        jLabelPMPrecisa.setBounds(60, 50, 210, 16);
    }

    private void jButtonPMProduzirActionPerformed(java.awt.event.ActionEvent evt) {
        this.getDirector().changed(this, evt);
    }

    private javax.swing.JButton jButtonPMProduzir;

    private javax.swing.JLabel jLabelPMEstoque;

    private javax.swing.JLabel jLabelPMEstoqueItem1;

    private javax.swing.JLabel jLabelPMEstoqueItem2;

    private javax.swing.JLabel jLabelPMEstoqueItem3;

    private javax.swing.JLabel jLabelPMEstoqueItem4;

    private javax.swing.JLabel jLabelPMPTitulo;

    private javax.swing.JLabel jLabelPMPrecisa;

    private javax.swing.JLabel jLabelPMQuantidadeMinimaItem1;

    private javax.swing.JLabel jLabelPMQuantidadeMinimaItem2;

    private javax.swing.JLabel jLabelPMQuantidadeMinimaItem3;

    private javax.swing.JLabel jLabelPMQuantidadeMinimaItem4;

    private javax.swing.JLabel jLabelPMUnidadesAProduzir;

    private javax.swing.JTextField jTextFieldPMAlerta;

    private javax.swing.JTextField jTextFieldPMEstoqueItem1;

    private javax.swing.JTextField jTextFieldPMEstoqueItem2;

    private javax.swing.JTextField jTextFieldPMEstoqueItem3;

    private javax.swing.JTextField jTextFieldPMEstoqueItem4;

    private javax.swing.JTextField jTextFieldPMQtMinimaItem1;

    private javax.swing.JTextField jTextFieldPMQtMinimaItem2;

    private javax.swing.JTextField jTextFieldPMQtMinimaItem3;

    private javax.swing.JTextField jTextFieldPMQtMinimaItem4;

    private javax.swing.JTextField jTextFieldPMUnidadesASeremProduzir;

    /**
     * @return the jButtonPMProduzir
     */
    public javax.swing.JButton getjButtonPMProduzir() {
        return jButtonPMProduzir;
    }

    /**
     * @return the jLabelPMEstoque
     */
    public javax.swing.JLabel getjLabelPMEstoque() {
        return jLabelPMEstoque;
    }

    /**
     * @return the jLabelPMEstoqueItem1
     */
    public javax.swing.JLabel getjLabelPMEstoqueItem1() {
        return jLabelPMEstoqueItem1;
    }

    /**
     * @return the jLabelPMEstoqueItem2
     */
    public javax.swing.JLabel getjLabelPMEstoqueItem2() {
        return jLabelPMEstoqueItem2;
    }

    /**
     * @return the jLabelPMEstoqueItem3
     */
    public javax.swing.JLabel getjLabelPMEstoqueItem3() {
        return jLabelPMEstoqueItem3;
    }

    /**
     * @return the jLabelPMEstoqueItem4
     */
    public javax.swing.JLabel getjLabelPMEstoqueItem4() {
        return jLabelPMEstoqueItem4;
    }

    /**
     * @return the jLabelPMPTitulo
     */
    public javax.swing.JLabel getjLabelPMPTitulo() {
        return jLabelPMPTitulo;
    }

    /**
     * @return the jLabelPMPrecisa
     */
    public javax.swing.JLabel getjLabelPMPrecisa() {
        return jLabelPMPrecisa;
    }

    /**
     * @return the jLabelPMQuantidadeMinimaItem1
     */
    public javax.swing.JLabel getjLabelPMQuantidadeMinimaItem1() {
        return jLabelPMQuantidadeMinimaItem1;
    }

    /**
     * @return the jLabelPMQuantidadeMinimaItem2
     */
    public javax.swing.JLabel getjLabelPMQuantidadeMinimaItem2() {
        return jLabelPMQuantidadeMinimaItem2;
    }

    /**
     * @return the jLabelPMQuantidadeMinimaItem3
     */
    public javax.swing.JLabel getjLabelPMQuantidadeMinimaItem3() {
        return jLabelPMQuantidadeMinimaItem3;
    }

    /**
     * @return the jLabelPMQuantidadeMinimaItem4
     */
    public javax.swing.JLabel getjLabelPMQuantidadeMinimaItem4() {
        return jLabelPMQuantidadeMinimaItem4;
    }

    /**
     * @return the jLabelPMUnidadesAProduzir
     */
    public javax.swing.JLabel getjLabelPMUnidadesAProduzir() {
        return jLabelPMUnidadesAProduzir;
    }

    /**
     * @return the jTextFieldPMAlerta
     */
    public javax.swing.JTextField getjTextFieldPMAlerta() {
        return jTextFieldPMAlerta;
    }

    /**
     * @return the jTextFieldPMEstoqueItem1
     */
    public javax.swing.JTextField getjTextFieldPMEstoqueItem1() {
        return jTextFieldPMEstoqueItem1;
    }

    /**
     * @return the jTextFieldPMEstoqueItem2
     */
    public javax.swing.JTextField getjTextFieldPMEstoqueItem2() {
        return jTextFieldPMEstoqueItem2;
    }

    /**
     * @return the jTextFieldPMEstoqueItem3
     */
    public javax.swing.JTextField getjTextFieldPMEstoqueItem3() {
        return jTextFieldPMEstoqueItem3;
    }

    /**
     * @return the jTextFieldPMEstoqueItem4
     */
    public javax.swing.JTextField getjTextFieldPMEstoqueItem4() {
        return jTextFieldPMEstoqueItem4;
    }

    /**
     * @return the jTextFieldPMQtMinimaItem1
     */
    public javax.swing.JTextField getjTextFieldPMQtMinimaItem1() {
        return jTextFieldPMQtMinimaItem1;
    }

    /**
     * @return the jTextFieldPMQtMinimaItem2
     */
    public javax.swing.JTextField getjTextFieldPMQtMinimaItem2() {
        return jTextFieldPMQtMinimaItem2;
    }

    /**
     * @return the jTextFieldPMQtMinimaItem3
     */
    public javax.swing.JTextField getjTextFieldPMQtMinimaItem3() {
        return jTextFieldPMQtMinimaItem3;
    }

    /**
     * @return the jTextFieldPMQtMinimaItem4
     */
    public javax.swing.JTextField getjTextFieldPMQtMinimaItem4() {
        return jTextFieldPMQtMinimaItem4;
    }

    /**
     * @return the jTextFieldPMUnidadesASeremProduzir
     */
    public javax.swing.JTextField getjTextFieldPMUnidadesASeremProduzir() {
        return jTextFieldPMUnidadesASeremProduzir;
    }
}