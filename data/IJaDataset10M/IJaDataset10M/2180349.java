package org.cerg.quincasMarket.game.tempo;

import org.cerg.quincasMarket.game.venda.*;
import org.cerg.quincasMarket.game.resultadoDaCompra.*;
import org.cerg.quincasMarket.game.compra.*;
import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 *
 * @author  anjos
 */
public class TelaTempo extends javax.swing.JPanel {

    /** Creates new form TelaDeCompra */
    public TelaTempo() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Edit.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        painelTempo1 = new org.cerg.quincasMarket.painel.tempo.PainelTempo();
        setBackground(new java.awt.Color(254, 254, 254));
        setEnabled(false);
        setFocusable(false);
        setRequestFocusEnabled(false);
        setVerifyInputWhenFocusTarget(false);
        setLayout(new java.awt.BorderLayout());
        add(painelTempo1, java.awt.BorderLayout.CENTER);
    }

    private org.cerg.quincasMarket.painel.tempo.PainelTempo painelTempo1;

    public static void main(String args[]) {
        JFrame jf = new JFrame("Tela de Compra");
        TelaTempo tc = new TelaTempo();
        jf.setSize(500, 500);
        jf.setLayout(new BorderLayout());
        jf.add(tc, BorderLayout.CENTER);
        jf.setFocusTraversalPolicy(tc.getPainelTempo1().getMyNewFocusPolicy());
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    /**
     * @return the painelTempo1
     */
    public org.cerg.quincasMarket.painel.tempo.PainelTempo getPainelTempo1() {
        return painelTempo1;
    }
}
