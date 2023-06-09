package websiteschema.analyzer.browser.left.sample;

import java.util.ArrayList;
import java.util.List;
import websiteschema.analyzer.browser.left.AnalysisPanel;
import websiteschema.analyzer.context.BrowserContext;
import websiteschema.cluster.analyzer.ClusterAnalyzer;
import websiteschema.cluster.analyzer.ClusterAnalyzerImpl;
import websiteschema.cluster.analyzer.IFieldAnalyzer;
import websiteschema.model.domain.Site;
import websiteschema.persistence.Mapper;
import websiteschema.persistence.hbase.SampleMapper;
import websiteschema.persistence.rdbms.SiteMapper;

/**
 *
 * @author ray
 */
public class ClustererFrame extends javax.swing.JFrame {

    private String siteId;

    private AnalysisPanel analysisPanel;

    /** Creates new form ClustererFrame */
    public ClustererFrame() {
        initComponents();
        int screenWidth = ((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
        int screenHeight = ((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
        int sizeWidth = this.getWidth();
        int sizeHeight = this.getHeight();
        this.setLocation((screenWidth - sizeWidth) / 2, (screenHeight - sizeHeight) / 2);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        infoArea = new javax.swing.JTextArea();
        jToolBar1 = new javax.swing.JToolBar();
        startButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        retrainCheckBox = new javax.swing.JCheckBox();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        infoArea.setColumns(20);
        infoArea.setRows(5);
        jScrollPane1.setViewportView(infoArea);
        jToolBar1.setRollover(true);
        startButton.setText("开始聚类");
        startButton.setToolTipText("开始聚类分析websiteschema");
        startButton.setFocusable(false);
        startButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        startButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        startButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(startButton);
        jLabel1.setText("训练全新的模型");
        jToolBar1.add(jLabel1);
        retrainCheckBox.setToolTipText("如果训练全新的模型，请选择，否则就在目前训练结果的基础上再训练。");
        retrainCheckBox.setFocusable(false);
        retrainCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        retrainCheckBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(retrainCheckBox);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)));
        pack();
    }

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
        WebsiteschemaClusterer clusterer = new WebsiteschemaClusterer();
        clusterer.setSiteId(getSiteId());
        clusterer.setRetrain(this.retrainCheckBox.isSelected());
        clusterer.setParentComponent(this);
        clusterer.setSampleMapper(BrowserContext.getSpringContext().getBean("sampleMapper", Mapper.class));
        clusterer.setCmMapper(BrowserContext.getSpringContext().getBean("clusterModelMapper", Mapper.class));
        clusterer.setWebsiteschemaMapper(BrowserContext.getSpringContext().getBean("websiteschemaMapper", Mapper.class));
        clusterer.setAnalyzer(createClusterAnalyzer());
        clusterer.setPanel(analysisPanel);
        clusterer.setTextArea(this.infoArea);
        new Thread(clusterer).start();
    }

    private ClusterAnalyzer createClusterAnalyzer() {
        SiteMapper siteMapper = BrowserContext.getSpringContext().getBean("siteMapper", SiteMapper.class);
        Site site = siteMapper.getBySiteId(getSiteId());
        if (null != site) {
            String siteType = site.getSiteType();
            String className = BrowserContext.getConfigure().getProperty("ClusterAnalysis", siteType, null);
            if (null != className) {
                try {
                    Class clazz = Class.forName(className);
                    ClusterAnalyzer ret = (ClusterAnalyzer) clazz.newInstance();
                    ret.setFieldAnalyzers(createFieldAnalyzers(site));
                    return ret;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return new ClusterAnalyzerImpl();
    }

    private List<IFieldAnalyzer> createFieldAnalyzers(Site site) {
        List<IFieldAnalyzer> ret = new ArrayList<IFieldAnalyzer>();
        if (null != site) {
            String siteType = site.getSiteType();
            List<String> analyzerTypes = BrowserContext.getConfigure().getListProperty("FieldAnalysis", siteType);
            if (null != analyzerTypes) {
                for (String clazzName : analyzerTypes) {
                    try {
                        Class clazz = Class.forName(clazzName);
                        IFieldAnalyzer analyzer = (IFieldAnalyzer) clazz.newInstance();
                        ret.add(analyzer);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return ret;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public AnalysisPanel getAnalysisPanel() {
        return analysisPanel;
    }

    public void setAnalysisPanel(AnalysisPanel analysisPanel) {
        this.analysisPanel = analysisPanel;
    }

    private javax.swing.JTextArea infoArea;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JToolBar jToolBar1;

    private javax.swing.JCheckBox retrainCheckBox;

    private javax.swing.JButton startButton;
}
