package languageacquisition;

import java.util.ArrayList;
import jade.core.*;
import jade.core.Runtime;
import jade.tools.rma.rma;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

/**
 *
 * @author  Administrator
 */
public class Manager extends javax.swing.JFrame {

    /** Creates new form ManagerGUI */
    public Manager() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        O_AGENT = new javax.swing.JLabel();
        O_number = new javax.swing.JLabel();
        desLength = new javax.swing.JLabel();
        whichFeature = new javax.swing.JLabel();
        O_gamma = new javax.swing.JLabel();
        O_period = new javax.swing.JLabel();
        P_AGENT = new javax.swing.JLabel();
        P_number = new javax.swing.JLabel();
        P_observePeriod = new javax.swing.JLabel();
        P_talkPeriod = new javax.swing.JLabel();
        OA_number = new javax.swing.JTextField();
        A_desLength = new javax.swing.JTextField();
        A_whichFeature = new javax.swing.JTextField();
        OA_gamma = new javax.swing.JTextField();
        OA_period = new javax.swing.JTextField();
        PA_number = new javax.swing.JTextField();
        PA_observePeriod = new javax.swing.JTextField();
        PA_talkPeriod = new javax.swing.JTextField();
        start = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        O_AGENT.setText("OBJECT  AGENT:");
        O_number.setText("number of item");
        desLength.setText("number of features");
        whichFeature.setText("select one of them");
        O_gamma.setText("gamma - it dermines probability of feature value change");
        O_period.setText("period - it dermines frequency of feature value change");
        P_AGENT.setText("POPULATION  AGENT:");
        P_number.setText("number of item");
        P_observePeriod.setText("observe period - it dermines frequency of obesrvation");
        P_talkPeriod.setText("talk period - it dermines frequency of communication");
        OA_number.setText("5");
        OA_number.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OA_numberActionPerformed(evt);
            }
        });
        A_desLength.setText("10");
        A_desLength.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_desLengthActionPerformed(evt);
            }
        });
        A_whichFeature.setText("3");
        A_whichFeature.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_whichFeatureActionPerformed(evt);
            }
        });
        OA_gamma.setText("0.1");
        OA_period.setText("3000");
        PA_number.setText("3");
        PA_observePeriod.setText("1000");
        PA_talkPeriod.setText("4000");
        start.setText("START");
        start.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addGap(44, 44, 44).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(PA_talkPeriod).addComponent(PA_number).addComponent(OA_period).addComponent(OA_gamma).addComponent(OA_number, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE).addComponent(PA_observePeriod, javax.swing.GroupLayout.Alignment.LEADING)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(P_talkPeriod).addComponent(O_gamma).addComponent(O_period).addComponent(P_number).addComponent(O_number).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(start).addComponent(P_observePeriod)))).addGroup(layout.createSequentialGroup().addComponent(O_AGENT).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(A_desLength, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(desLength).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(A_whichFeature, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(whichFeature).addGap(1, 1, 1)).addComponent(P_AGENT)).addContainerGap(23, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20, 20, 20).addComponent(O_AGENT).addGap(9, 9, 9).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(OA_number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(O_number))).addGroup(layout.createSequentialGroup().addGap(3, 3, 3).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(A_desLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(desLength, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(A_whichFeature, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(whichFeature, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(OA_gamma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(O_gamma)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(O_period).addComponent(OA_period, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(30, 30, 30).addComponent(P_AGENT).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(P_number).addComponent(PA_number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(PA_observePeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(P_observePeriod)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(PA_talkPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(P_talkPeriod)).addGap(18, 18, 18).addComponent(start).addContainerGap()));
        pack();
    }

    private void A_desLengthActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void OA_numberActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void A_whichFeatureActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void startActionPerformed(java.awt.event.ActionEvent evt) {
        Visualization2.Visualization2GUI(((int) (Double.parseDouble(A_desLength.getText()))), ((int) (Double.parseDouble(OA_number.getText()))), ((int) (Double.parseDouble(PA_number.getText()))));
        String name;
        Object[] argsOA = new Object[3];
        argsOA[0] = A_desLength.getText();
        argsOA[1] = OA_gamma.getText();
        argsOA[2] = OA_period.getText();
        Object[] argsPA = new Object[4];
        argsPA[0] = A_desLength.getText();
        argsPA[1] = PA_observePeriod.getText();
        argsPA[2] = PA_talkPeriod.getText();
        argsPA[3] = A_whichFeature.getText();
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        ContainerController cc = rt.createMainContainer(p);
        AgentController ac;
        for (int numberOA = 0; numberOA < ((int) (Double.parseDouble(OA_number.getText()))); numberOA++) {
            AgentController agent;
            try {
                name = ObjectAgent.SERVICE_TYPE + Integer.toString(numberOA);
                agent = cc.createNewAgent(name, "languageacquisition.ObjectAgent", argsOA);
                agent.start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }
        for (int numberPA = 0; numberPA < ((int) (Double.parseDouble(PA_number.getText()))); numberPA++) {
            AgentController agent;
            try {
                name = PopulationAgent.SERVICE_TYPE + Integer.toString(numberPA);
                agent = cc.createNewAgent(name, "languageacquisition.PopulationAgent", argsPA);
                agent.start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }
        try {
            ac = cc.createNewAgent("NEW", "languageacquisition.NewAgent", argsPA);
            ac.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Manager().setVisible(true);
            }
        });
    }

    private javax.swing.JTextField A_desLength;

    private javax.swing.JTextField A_whichFeature;

    private javax.swing.JTextField OA_gamma;

    private javax.swing.JTextField OA_number;

    private javax.swing.JTextField OA_period;

    private javax.swing.JLabel O_AGENT;

    private javax.swing.JLabel O_gamma;

    private javax.swing.JLabel O_number;

    private javax.swing.JLabel O_period;

    private javax.swing.JTextField PA_number;

    private javax.swing.JTextField PA_observePeriod;

    private javax.swing.JTextField PA_talkPeriod;

    private javax.swing.JLabel P_AGENT;

    private javax.swing.JLabel P_number;

    private javax.swing.JLabel P_observePeriod;

    private javax.swing.JLabel P_talkPeriod;

    private javax.swing.JLabel desLength;

    private javax.swing.JLabel whichFeature;

    private javax.swing.JButton start;
}
