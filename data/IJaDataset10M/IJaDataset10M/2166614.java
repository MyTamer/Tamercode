package ui;

import core.Icon;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

/**
 *
 * @author Brian Gibowski <brian@brgib.com>
 */
public class ProgressMonitorDialog extends javax.swing.JDialog {

    {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in Main Gui Setup");
        }
    }

    /** Creates new form ProgressMonitorDialog */
    public ProgressMonitorDialog(final java.awt.Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        initComponents();
        setIconImages(Icon.getIcons());
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(parent);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                setVisible(true);
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        progressBar = new javax.swing.JProgressBar();
        cancelButton = new javax.swing.JButton();
        progressLabel = new javax.swing.JTextField();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        progressLabel.setEditable(false);
        progressLabel.setBorder(null);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(progressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE).addComponent(progressLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(208, Short.MAX_VALUE).addComponent(cancelButton).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(progressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton).addContainerGap()));
        pack();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        isCanceled = true;
        dispose();
    }

    /**
     * Returns an integer representing progress of a task given the
     * two long variables.
     *
     * @param countComplete The long value representing a value of the work completed.
     *
     * @param totalCount The total amount of work to do.
     *
     * @return An integer between 0 and 100 representing the percentage of work
     * complete.
     */
    public static int calculatePercentProgress(long countComplete, long totalCount) {
        if (countComplete > totalCount) {
            throw new IllegalArgumentException("Count complete:  " + countComplete + " is greater than total count:  " + totalCount);
        }
        if (countComplete < 0 || totalCount < 0) {
            throw new IllegalArgumentException("Can not have negative numbers to" + " computer percent complete.");
        }
        return (int) ((double) countComplete / (double) totalCount * 100.0);
    }

    /**
     * Returns a boolean for whether or not the cancel button has been pressed.
     *
     * @return If true, the user has selected the cancel button.
     */
    public boolean isCanceled() {
        return isCanceled;
    }

    /**
     * Sets the progress bar value to the integer specified.
     *
     * @param n The integer value to set the progress bar to.
     */
    public void setProgress(int n) {
        if (n < 0 || n > 100) {
            throw new IllegalArgumentException("Value must be between 0 and 100");
        }
        if (n == 100) {
            dispose();
        }
        progressBar.setValue(n);
    }

    /**
     * Sets the progress bar to a percentage calculated given the two
     * values of completed work and total work.
     * @param countComplete The amount of work completed.
     *
     * @param totalCount The total amount of work to do.
     */
    public void setProgress(long countComplete, long totalCount) {
        setProgress(calculatePercentProgress(countComplete, totalCount));
    }

    /**
     * Sets the progress bar to an indeterminate state.
     *
     * @param b If true, the progress bar is set to indeterminate.  If false,
     * the progress bar is set to determiante.
     */
    public void setIndeterminate(final boolean b) {
        progressBar.setIndeterminate(b);
    }

    /**
     * Disposes the progress dialog box.
     */
    public void close() {
        dispose();
    }

    /**
     * Sets the text of the progress dialog.
     *
     * @param label The text to display below the progress bar.
     */
    public void setText(String label) {
        if (label == null) {
            throw new NullPointerException("Label is null");
        }
        progressLabel.setText(label);
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                ProgressMonitorDialog dialog = new ProgressMonitorDialog(new javax.swing.JFrame(), "Test", true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private javax.swing.JButton cancelButton;

    private javax.swing.JProgressBar progressBar;

    private javax.swing.JTextField progressLabel;

    private boolean isCanceled = false;
}