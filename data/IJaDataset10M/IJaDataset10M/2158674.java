package codeposter;

import formatter.PosterPreferences;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import looknfeel.LnFListener;

/**
 *
 * @author Rajgopal
 */
public class FormatterSettings extends javax.swing.JDialog {

    private String colors[] = { "", "aqua", "blue", "fuchsia", "gold", "gray", "green", "lime", "maroon", "navy", "olive", "orange", "pink", "purple", "red", "silver", "teal", "violet", "yellow" };

    private LnFListener lnf = null;

    private PosterPreferences posterPrefs;

    /** Creates new form FormatterSettings
     * @param parent
     * @param modal
     * @param lnf 
     */
    public FormatterSettings(java.awt.Frame parent, boolean modal, LnFListener lnf, PosterPreferences posterPrefs) {
        super(parent, modal);
        this.lnf = lnf;
        this.posterPrefs = posterPrefs;
        initComponents();
        loadProperties();
    }

    @Override
    public void setVisible(boolean b) {
        pack();
        Rectangle r = getGraphicsConfiguration().getBounds();
        setLocation(r.width / 2 - getWidth() / 2, r.height / 2 - getHeight() / 2);
        try {
            lnf.setThemeToContainer(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        getRootPane().setDefaultButton(saveButton);
        super.setVisible(b);
    }

    @Override
    protected JRootPane createRootPane() {
        ActionListener actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
            }
        };
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        JRootPane myRootPane = new JRootPane();
        myRootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        return myRootPane;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        kwLabel = new javax.swing.JLabel();
        kwBold = new javax.swing.JCheckBox();
        kwItalics = new javax.swing.JCheckBox();
        kwUnderline = new javax.swing.JCheckBox();
        kwColors = new javax.swing.JComboBox();
        commentsLabel = new javax.swing.JLabel();
        commentsBold = new javax.swing.JCheckBox();
        commentsItalics = new javax.swing.JCheckBox();
        commentsUnderline = new javax.swing.JCheckBox();
        commentsColors = new javax.swing.JComboBox();
        stringLabel = new javax.swing.JLabel();
        stringBold = new javax.swing.JCheckBox();
        stringItalics = new javax.swing.JCheckBox();
        stringUnderline = new javax.swing.JCheckBox();
        stringColors = new javax.swing.JComboBox();
        macroLabel = new javax.swing.JLabel();
        macroBold = new javax.swing.JCheckBox();
        macroItalics = new javax.swing.JCheckBox();
        macroUnderline = new javax.swing.JCheckBox();
        macroColors = new javax.swing.JComboBox();
        constLabel = new javax.swing.JLabel();
        constBold = new javax.swing.JCheckBox();
        constItalics = new javax.swing.JCheckBox();
        constUnderline = new javax.swing.JCheckBox();
        constColors = new javax.swing.JComboBox();
        lnLabel = new javax.swing.JLabel();
        lnBold = new javax.swing.JCheckBox();
        lnItalics = new javax.swing.JCheckBox();
        lnUnderline = new javax.swing.JCheckBox();
        lnColors = new javax.swing.JComboBox();
        cancelButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        autoCopy = new javax.swing.JCheckBox();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("FormatterSettings");
        setForeground(java.awt.Color.white);
        kwLabel.setText("Keywords");
        kwBold.setText("Bold");
        kwItalics.setText("Italics");
        kwUnderline.setText("Underline");
        kwColors.setModel(new javax.swing.DefaultComboBoxModel(colors));
        commentsLabel.setText("Comments");
        commentsBold.setText("Bold");
        commentsItalics.setText("Italics");
        commentsUnderline.setText("Underline");
        commentsColors.setModel(new javax.swing.DefaultComboBoxModel(colors));
        stringLabel.setText("String");
        stringBold.setText("Bold");
        stringItalics.setText("Italics");
        stringUnderline.setText("Underline");
        stringColors.setModel(new javax.swing.DefaultComboBoxModel(colors));
        macroLabel.setText("Macros");
        macroBold.setText("Bold");
        macroItalics.setText("Italics");
        macroUnderline.setText("Underline");
        macroColors.setModel(new javax.swing.DefaultComboBoxModel(colors));
        constLabel.setText("Constants");
        constBold.setText("Bold");
        constItalics.setText("Italics");
        constUnderline.setText("Underline");
        constColors.setModel(new javax.swing.DefaultComboBoxModel(colors));
        lnLabel.setText("Line Numbers");
        lnBold.setText("Bold");
        lnItalics.setText("Italics");
        lnUnderline.setText("Underline");
        lnColors.setModel(new javax.swing.DefaultComboBoxModel(colors));
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        autoCopy.setText("Copy formatted text to clipboard on viewing");
        autoCopy.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoCopyActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(autoCopy).add(layout.createSequentialGroup().add(lnLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(lnBold).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(lnItalics).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(lnUnderline).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(lnColors, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(layout.createSequentialGroup().add(kwLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 17, Short.MAX_VALUE).add(kwBold).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(kwItalics).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(kwUnderline).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(kwColors, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(layout.createSequentialGroup().add(commentsLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 14, Short.MAX_VALUE).add(commentsBold).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(commentsItalics).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(commentsUnderline).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(commentsColors, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(stringLabel).add(macroLabel)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 30, Short.MAX_VALUE).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(stringBold).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(stringItalics).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(stringUnderline).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(stringColors, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(macroBold).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(macroItalics).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(macroUnderline).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(macroColors, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(constLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 15, Short.MAX_VALUE).add(constBold).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(constItalics).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(constUnderline).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(constColors, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(saveButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cancelButton))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(kwLabel).add(kwBold).add(kwItalics).add(kwUnderline).add(kwColors, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(commentsLabel).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(commentsItalics).add(commentsUnderline).add(commentsBold)).add(commentsColors, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(stringBold).add(stringItalics).add(stringUnderline).add(stringColors, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(stringLabel)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(macroItalics).add(macroUnderline).add(macroLabel).add(macroBold)).add(macroColors, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(constColors, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(constLabel).add(constUnderline).add(constItalics).add(constBold))).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(lnColors, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(lnLabel).add(lnUnderline).add(lnItalics).add(lnBold))).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(autoCopy).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 6, Short.MAX_VALUE).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(saveButton).add(cancelButton)).addContainerGap()));
        pack();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void loadProperties() {
        posterPrefs.readPreferences();
        kwBold.setSelected(posterPrefs.isKwBold());
        kwItalics.setSelected(posterPrefs.isKwItalics());
        kwUnderline.setSelected(posterPrefs.isKwUnderline());
        kwColors.setSelectedItem(posterPrefs.getKwColor());
        commentsBold.setSelected(posterPrefs.isCommentBold());
        commentsItalics.setSelected(posterPrefs.isCommentItalics());
        commentsUnderline.setSelected(posterPrefs.isCommentUnderline());
        commentsColors.setSelectedItem(posterPrefs.getCommentColor());
        constBold.setSelected(posterPrefs.isConstBold());
        constItalics.setSelected(posterPrefs.isConstItalics());
        constUnderline.setSelected(posterPrefs.isConstUnderline());
        constColors.setSelectedItem(posterPrefs.getConstColor());
        lnBold.setSelected(posterPrefs.isLnBold());
        lnItalics.setSelected(posterPrefs.isLnItalics());
        lnUnderline.setSelected(posterPrefs.isLnUnderline());
        lnColors.setSelectedItem(posterPrefs.getLnColor());
        macroBold.setSelected(posterPrefs.isMacroBold());
        macroItalics.setSelected(posterPrefs.isMacroItalics());
        macroUnderline.setSelected(posterPrefs.isMacroUnderline());
        macroColors.setSelectedItem(posterPrefs.getMacroColor());
        stringBold.setSelected(posterPrefs.isStringBold());
        stringItalics.setSelected(posterPrefs.isStringItalics());
        stringUnderline.setSelected(posterPrefs.isStringUnderline());
        stringColors.setSelectedItem(posterPrefs.getStringColor());
        autoCopy.setSelected(posterPrefs.getAutoCopy());
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        posterPrefs.setKwBold(kwBold.isSelected());
        posterPrefs.setKwItalics(kwItalics.isSelected());
        posterPrefs.setKwUnderline(kwUnderline.isSelected());
        posterPrefs.setKwColor((String) kwColors.getSelectedItem());
        posterPrefs.setCommentBold(commentsBold.isSelected());
        posterPrefs.setCommentItalics(commentsItalics.isSelected());
        posterPrefs.setCommentUnderline(commentsUnderline.isSelected());
        posterPrefs.setCommentColor((String) commentsColors.getSelectedItem());
        posterPrefs.setConstBold(constBold.isSelected());
        posterPrefs.setConstItalics(constItalics.isSelected());
        posterPrefs.setConstUnderline(constUnderline.isSelected());
        posterPrefs.setConstColor((String) constColors.getSelectedItem());
        posterPrefs.setLnBold(lnBold.isSelected());
        posterPrefs.setLnItalics(lnItalics.isSelected());
        posterPrefs.setLnUnderline(lnUnderline.isSelected());
        posterPrefs.setLnColor((String) lnColors.getSelectedItem());
        posterPrefs.setMacroBold(macroBold.isSelected());
        posterPrefs.setMacroItalics(macroItalics.isSelected());
        posterPrefs.setMacroUnderline(macroUnderline.isSelected());
        posterPrefs.setMacroColor((String) macroColors.getSelectedItem());
        posterPrefs.setStringBold(stringBold.isSelected());
        posterPrefs.setStringItalics(stringItalics.isSelected());
        posterPrefs.setStringUnderline(stringUnderline.isSelected());
        posterPrefs.setStringColor((String) stringColors.getSelectedItem());
        posterPrefs.setAutoCopy(autoCopy.isSelected());
        posterPrefs.writePreferences();
        dispose();
    }

    private void autoCopyActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private javax.swing.JCheckBox autoCopy;

    private javax.swing.JButton cancelButton;

    private javax.swing.JCheckBox commentsBold;

    private javax.swing.JComboBox commentsColors;

    private javax.swing.JCheckBox commentsItalics;

    private javax.swing.JLabel commentsLabel;

    private javax.swing.JCheckBox commentsUnderline;

    private javax.swing.JCheckBox constBold;

    private javax.swing.JComboBox constColors;

    private javax.swing.JCheckBox constItalics;

    private javax.swing.JLabel constLabel;

    private javax.swing.JCheckBox constUnderline;

    private javax.swing.JCheckBox kwBold;

    private javax.swing.JComboBox kwColors;

    private javax.swing.JCheckBox kwItalics;

    private javax.swing.JLabel kwLabel;

    private javax.swing.JCheckBox kwUnderline;

    private javax.swing.JCheckBox lnBold;

    private javax.swing.JComboBox lnColors;

    private javax.swing.JCheckBox lnItalics;

    private javax.swing.JLabel lnLabel;

    private javax.swing.JCheckBox lnUnderline;

    private javax.swing.JCheckBox macroBold;

    private javax.swing.JComboBox macroColors;

    private javax.swing.JCheckBox macroItalics;

    private javax.swing.JLabel macroLabel;

    private javax.swing.JCheckBox macroUnderline;

    private javax.swing.JButton saveButton;

    private javax.swing.JCheckBox stringBold;

    private javax.swing.JComboBox stringColors;

    private javax.swing.JCheckBox stringItalics;

    private javax.swing.JLabel stringLabel;

    private javax.swing.JCheckBox stringUnderline;
}
