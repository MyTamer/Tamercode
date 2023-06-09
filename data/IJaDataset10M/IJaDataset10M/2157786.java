package MainProgram.Walkthrough.UI.SegmentStepListPanels;

import MainProgram.Walkthrough.UI.WalkthroughWindow;

/**
 *
 * @author Stephen
 */
public class BaseSegmentListPanel extends javax.swing.JPanel {

    WalkthroughWindow m_walkthroughWindow = null;

    /** Creates new form Segment2StepListPanel */
    public BaseSegmentListPanel(WalkthroughWindow window) {
        initComponents();
        m_walkthroughWindow = window;
    }

    public void UpdateControls() {
    }

    public void FocusOnFirstButton() {
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        setMaximumSize(new java.awt.Dimension(190, 300));
        setMinimumSize(new java.awt.Dimension(190, 300));
        setName("Form");
        setPreferredSize(new java.awt.Dimension(190, 300));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 190, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));
    }
}
