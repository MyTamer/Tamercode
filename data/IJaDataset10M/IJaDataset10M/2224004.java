package sgeci.Interface;

/**
 *
 * @author  Ronaldo
 */
public class ConsultaRelatorioCxEmergencia extends javax.swing.JInternalFrame {

    /** Creates new form ConsultaRelatorioCxEmergencia */
    public ConsultaRelatorioCxEmergencia() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jpLogo9 = new javax.swing.JPanel();
        jlLogo9 = new javax.swing.JLabel();
        jpAlarmeImage3 = new javax.swing.JPanel();
        jlAlarmeVisual7 = new javax.swing.JLabel();
        jpTipo7 = new javax.swing.JPanel();
        jrbAlarmeVisual7 = new javax.swing.JRadioButton();
        jrbOpcao41 = new javax.swing.JRadioButton();
        jrbOpcao42 = new javax.swing.JRadioButton();
        jrbOpcao43 = new javax.swing.JRadioButton();
        jpOpcoes7 = new javax.swing.JPanel();
        jpOpcoesAlarmeVisual3 = new javax.swing.JPanel();
        jlConsulta47 = new javax.swing.JLabel();
        jlConsulta48 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox();
        jCheckBox18 = new javax.swing.JCheckBox();
        jlConsulta49 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jComboBox11 = new javax.swing.JComboBox();
        jlConsulta50 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jComboBox12 = new javax.swing.JComboBox();
        jpBotao7 = new javax.swing.JPanel();
        jbtConsultar7 = new javax.swing.JButton();
        jbtRelatorio7 = new javax.swing.JButton();
        jbtCancelar7 = new javax.swing.JButton();
        jbtSair8 = new javax.swing.JButton();
        setClosable(true);
        setIconifiable(true);
        setTitle("Consulta & Relatório - Caixa de Chave de Emergência");
        setName("jifConsultaRelatorioCxEmergencia");
        jpLogo9.setBackground(java.awt.Color.white);
        jpLogo9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpLogo9.setName("jpLogo");
        jlLogo9.setBackground(java.awt.Color.white);
        jlLogo9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Petrobras.jpg")));
        jlLogo9.setName("jlLogo");
        javax.swing.GroupLayout jpLogo9Layout = new javax.swing.GroupLayout(jpLogo9);
        jpLogo9.setLayout(jpLogo9Layout);
        jpLogo9Layout.setHorizontalGroup(jpLogo9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpLogo9Layout.createSequentialGroup().addComponent(jlLogo9).addContainerGap(626, Short.MAX_VALUE)));
        jpLogo9Layout.setVerticalGroup(jpLogo9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpLogo9Layout.createSequentialGroup().addComponent(jlLogo9, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE).addContainerGap()));
        jpAlarmeImage3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpAlarmeImage3.setName("jlAlarmeImage");
        jlAlarmeVisual7.setFont(new java.awt.Font("Tahoma", 1, 11));
        jlAlarmeVisual7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/caixa de chave.png")));
        jlAlarmeVisual7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlAlarmeVisual7.setName("jlAlarmeVisual");
        jlAlarmeVisual7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        javax.swing.GroupLayout jpAlarmeImage3Layout = new javax.swing.GroupLayout(jpAlarmeImage3);
        jpAlarmeImage3.setLayout(jpAlarmeImage3Layout);
        jpAlarmeImage3Layout.setHorizontalGroup(jpAlarmeImage3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpAlarmeImage3Layout.createSequentialGroup().addGap(29, 29, 29).addComponent(jlAlarmeVisual7).addContainerGap(40, Short.MAX_VALUE)));
        jpAlarmeImage3Layout.setVerticalGroup(jpAlarmeImage3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpAlarmeImage3Layout.createSequentialGroup().addContainerGap().addComponent(jlAlarmeVisual7).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jpTipo7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Habilitar Opção", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        jpTipo7.setName("jlTipo");
        jrbAlarmeVisual7.setFont(new java.awt.Font("Tahoma", 1, 11));
        jrbAlarmeVisual7.setText("Opção 1");
        jrbAlarmeVisual7.setName("jrbAlarmeVisual");
        jrbOpcao41.setFont(new java.awt.Font("Tahoma", 1, 11));
        jrbOpcao41.setText("Opção 2");
        jrbOpcao41.setName("jrbOpcao2");
        jrbOpcao42.setFont(new java.awt.Font("Tahoma", 1, 11));
        jrbOpcao42.setText("Opção 3");
        jrbOpcao42.setName("jrbOpcao2");
        jrbOpcao43.setFont(new java.awt.Font("Tahoma", 1, 11));
        jrbOpcao43.setText("Opção 4");
        jrbOpcao43.setName("jrbOpcao2");
        javax.swing.GroupLayout jpTipo7Layout = new javax.swing.GroupLayout(jpTipo7);
        jpTipo7.setLayout(jpTipo7Layout);
        jpTipo7Layout.setHorizontalGroup(jpTipo7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpTipo7Layout.createSequentialGroup().addContainerGap().addComponent(jrbAlarmeVisual7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jrbOpcao41).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jrbOpcao42).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jrbOpcao43).addContainerGap(374, Short.MAX_VALUE)));
        jpTipo7Layout.setVerticalGroup(jpTipo7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpTipo7Layout.createSequentialGroup().addContainerGap().addGroup(jpTipo7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jrbAlarmeVisual7).addComponent(jrbOpcao41).addComponent(jrbOpcao42).addComponent(jrbOpcao43)).addContainerGap(34, Short.MAX_VALUE)));
        jpOpcoes7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Opções", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        jpOpcoes7.setName("jpOpcoes");
        jpOpcoesAlarmeVisual3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jlConsulta47.setFont(new java.awt.Font("Tahoma", 1, 11));
        jlConsulta47.setText("1.");
        jlConsulta47.setName("jlConsulta1");
        jlConsulta48.setFont(new java.awt.Font("Tahoma", 1, 11));
        jlConsulta48.setText("2.");
        jlConsulta48.setName("jlConsulta1");
        jLabel17.setText("Dados da Caixa de Chave de Emergência - Código:");
        jComboBox10.setMaximumRowCount(3);
        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecione" }));
        jCheckBox18.setText("Total de Caixa de Chave de Emergência");
        jlConsulta49.setFont(new java.awt.Font("Tahoma", 1, 11));
        jlConsulta49.setText("3.");
        jlConsulta49.setName("jlConsulta1");
        jLabel18.setText("Total de Caixa de Chave de Emergência - Local:");
        jComboBox11.setMaximumRowCount(3);
        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecione" }));
        jlConsulta50.setFont(new java.awt.Font("Tahoma", 1, 11));
        jlConsulta50.setText("4.");
        jlConsulta50.setName("jlConsulta1");
        jLabel19.setText("Relação de Caixa de Chave de Emergência - Local:");
        jComboBox12.setMaximumRowCount(3);
        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecione" }));
        javax.swing.GroupLayout jpOpcoesAlarmeVisual3Layout = new javax.swing.GroupLayout(jpOpcoesAlarmeVisual3);
        jpOpcoesAlarmeVisual3.setLayout(jpOpcoesAlarmeVisual3Layout);
        jpOpcoesAlarmeVisual3Layout.setHorizontalGroup(jpOpcoesAlarmeVisual3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpOpcoesAlarmeVisual3Layout.createSequentialGroup().addContainerGap().addGroup(jpOpcoesAlarmeVisual3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpOpcoesAlarmeVisual3Layout.createSequentialGroup().addComponent(jlConsulta48).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jCheckBox18)).addGroup(jpOpcoesAlarmeVisual3Layout.createSequentialGroup().addGroup(jpOpcoesAlarmeVisual3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpOpcoesAlarmeVisual3Layout.createSequentialGroup().addComponent(jlConsulta47).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel17)).addGroup(jpOpcoesAlarmeVisual3Layout.createSequentialGroup().addComponent(jlConsulta49).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel18))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jpOpcoesAlarmeVisual3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(jpOpcoesAlarmeVisual3Layout.createSequentialGroup().addComponent(jlConsulta50).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel19).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(79, Short.MAX_VALUE)));
        jpOpcoesAlarmeVisual3Layout.setVerticalGroup(jpOpcoesAlarmeVisual3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpOpcoesAlarmeVisual3Layout.createSequentialGroup().addContainerGap().addGroup(jpOpcoesAlarmeVisual3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jlConsulta47).addComponent(jLabel17).addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jpOpcoesAlarmeVisual3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jlConsulta48).addComponent(jCheckBox18)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jpOpcoesAlarmeVisual3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jlConsulta49).addComponent(jLabel18).addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jpOpcoesAlarmeVisual3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jlConsulta50).addComponent(jLabel19).addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(39, Short.MAX_VALUE)));
        javax.swing.GroupLayout jpOpcoes7Layout = new javax.swing.GroupLayout(jpOpcoes7);
        jpOpcoes7.setLayout(jpOpcoes7Layout);
        jpOpcoes7Layout.setHorizontalGroup(jpOpcoes7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpOpcoes7Layout.createSequentialGroup().addContainerGap().addComponent(jpOpcoesAlarmeVisual3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(50, Short.MAX_VALUE)));
        jpOpcoes7Layout.setVerticalGroup(jpOpcoes7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpOpcoes7Layout.createSequentialGroup().addContainerGap().addComponent(jpOpcoesAlarmeVisual3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(76, Short.MAX_VALUE)));
        jpBotao7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jbtConsultar7.setFont(new java.awt.Font("Tahoma", 1, 11));
        jbtConsultar7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/pesquisa.png")));
        jbtConsultar7.setText("Consultar");
        jbtConsultar7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtConsultar7.setName("jbtConsultar");
        jbtConsultar7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtRelatorio7.setFont(new java.awt.Font("Tahoma", 1, 11));
        jbtRelatorio7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/relatorio.png")));
        jbtRelatorio7.setText("Emitir Relatório");
        jbtRelatorio7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtRelatorio7.setName("jbtRelatorio");
        jbtRelatorio7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtCancelar7.setFont(new java.awt.Font("Tahoma", 1, 11));
        jbtCancelar7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Cancelar32.png")));
        jbtCancelar7.setText("Cancelar");
        jbtCancelar7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtCancelar7.setName("jbtCancelar");
        jbtCancelar7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtSair8.setFont(new java.awt.Font("Tahoma", 1, 11));
        jbtSair8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/sair.png")));
        jbtSair8.setText("Sair");
        jbtSair8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtSair8.setName("jbtSair");
        jbtSair8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtSair8.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSair8ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jpBotao7Layout = new javax.swing.GroupLayout(jpBotao7);
        jpBotao7.setLayout(jpBotao7Layout);
        jpBotao7Layout.setHorizontalGroup(jpBotao7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpBotao7Layout.createSequentialGroup().addContainerGap().addGroup(jpBotao7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jbtConsultar7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE).addComponent(jbtSair8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE).addComponent(jbtCancelar7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE).addComponent(jbtRelatorio7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        jpBotao7Layout.setVerticalGroup(jpBotao7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpBotao7Layout.createSequentialGroup().addContainerGap().addComponent(jbtConsultar7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbtRelatorio7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbtCancelar7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbtSair8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jpLogo9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(jpAlarmeImage3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jpTipo7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(jpOpcoes7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jpBotao7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jpLogo9, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jpAlarmeImage3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jpTipo7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jpOpcoes7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jpBotao7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void jbtSair8ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private javax.swing.JCheckBox jCheckBox18;

    private javax.swing.JComboBox jComboBox10;

    private javax.swing.JComboBox jComboBox11;

    private javax.swing.JComboBox jComboBox12;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JButton jbtCancelar7;

    private javax.swing.JButton jbtConsultar7;

    private javax.swing.JButton jbtRelatorio7;

    private javax.swing.JButton jbtSair8;

    private javax.swing.JLabel jlAlarmeVisual7;

    private javax.swing.JLabel jlConsulta47;

    private javax.swing.JLabel jlConsulta48;

    private javax.swing.JLabel jlConsulta49;

    private javax.swing.JLabel jlConsulta50;

    private javax.swing.JLabel jlLogo9;

    private javax.swing.JPanel jpAlarmeImage3;

    private javax.swing.JPanel jpBotao7;

    private javax.swing.JPanel jpLogo9;

    private javax.swing.JPanel jpOpcoes7;

    private javax.swing.JPanel jpOpcoesAlarmeVisual3;

    private javax.swing.JPanel jpTipo7;

    private javax.swing.JRadioButton jrbAlarmeVisual7;

    private javax.swing.JRadioButton jrbOpcao41;

    private javax.swing.JRadioButton jrbOpcao42;

    private javax.swing.JRadioButton jrbOpcao43;
}
