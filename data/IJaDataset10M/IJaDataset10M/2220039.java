package net.sourceforge.toscanaj.dbviewer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.sourceforge.toscanaj.controller.db.DatabaseException;
import net.sourceforge.toscanaj.gui.dialog.ErrorDialog;
import org.tockit.swing.preferences.ExtendedPreferences;

/**
 * This abstract class gives a framework for implementing database viewers with
 * paging buttons.
 * 
 * If a specific database viewer can handle only one item at a time, this class
 * can be used to add a set of buttons to navigate through the items given.
 * These buttons implement going to the first, previous, next and last item. In
 * addition a label shows the current position and the total number of items.
 * 
 * To use this class derive from it and implement the two abstract methods. The
 * getPanel() method has to return the Swing component used in your viewer,
 * while the showItem(String) should cause your viewer to show the item for the
 * specific object key given. See the documentation of DatabaseViewer about
 * getting the table and object key information.
 */
public abstract class PagingDatabaseViewer implements DatabaseViewer {

    private static final ExtendedPreferences preferences = ExtendedPreferences.userNodeForClass(PagingDatabaseViewer.class);

    protected interface PageViewPanel {

        void showItem(String keyValue) throws DatabaseViewerException;

        Component getComponent() throws DatabaseViewerException;
    }

    private DatabaseViewerManager viewerManager;

    private class PagingDatabaseViewerDialog extends JDialog {

        private List<String> fieldNames;

        private String[] keyValues;

        private int position;

        private final JButton navStartButton;

        private final JButton navPrevButton;

        private final JButton navNextButton;

        private final JButton navEndButton;

        private final JLabel infoLabel;

        private final PageViewPanel viewPanel;

        protected void showView(final String whereClause) {
            try {
                this.fieldNames = new ArrayList<String>();
                this.fieldNames.add(PagingDatabaseViewer.this.viewerManager.getKeyName());
                final List<String[]> results = PagingDatabaseViewer.this.viewerManager.getConnection().executeQuery(this.fieldNames, PagingDatabaseViewer.this.viewerManager.getTableName(), whereClause);
                this.keyValues = new String[results.size()];
                int i = 0;
                for (final String[] row : results) {
                    this.keyValues[i] = row[0];
                    i++;
                }
                this.position = 0;
                enableButtons();
                showCurrentItem();
                setVisible(true);
            } catch (final DatabaseException e) {
                ErrorDialog.showError(this, e, "Failed to query database");
            }
        }

        public PagingDatabaseViewerDialog(final Frame frame) throws DatabaseViewerException {
            super(frame, "View Items", false);
            this.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(final WindowEvent e) {
                    closeDialog();
                }
            });
            final JButton closeButton = new JButton("Close");
            closeButton.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    closeDialog();
                }
            });
            this.viewPanel = createPanel();
            getRootPane().setDefaultButton(closeButton);
            this.navStartButton = new JButton("<<");
            this.navStartButton.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    start();
                }
            });
            this.navPrevButton = new JButton("<");
            this.navPrevButton.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    prev();
                }
            });
            this.navNextButton = new JButton(">");
            this.navNextButton.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    next();
                }
            });
            this.navEndButton = new JButton(">>");
            this.navEndButton.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    end();
                }
            });
            this.infoLabel = new JLabel("");
            final JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
            buttonPane.setBorder(BorderFactory.createEtchedBorder());
            buttonPane.add(this.navStartButton);
            buttonPane.add(this.navPrevButton);
            buttonPane.add(this.navNextButton);
            buttonPane.add(this.navEndButton);
            buttonPane.add(Box.createHorizontalGlue());
            buttonPane.add(this.infoLabel);
            buttonPane.add(Box.createHorizontalGlue());
            buttonPane.add(closeButton);
            final Container contentPane = getContentPane();
            contentPane.add(this.viewPanel.getComponent(), BorderLayout.CENTER);
            contentPane.add(buttonPane, BorderLayout.SOUTH);
        }

        protected void closeDialog() {
            preferences.storeWindowPlacement(this);
            this.dispose();
        }

        private void enableButtons() {
            final int last = this.keyValues.length - 1;
            this.navStartButton.setEnabled(this.position != 0);
            this.navEndButton.setEnabled(this.position != last);
            this.navPrevButton.setEnabled(this.position != 0);
            this.navNextButton.setEnabled(this.position != last);
        }

        private void next() {
            this.position++;
            showCurrentItem();
            enableButtons();
        }

        private void prev() {
            this.position--;
            showCurrentItem();
            enableButtons();
        }

        private void showCurrentItem() {
            try {
                this.viewPanel.showItem(this.keyValues[this.position]);
            } catch (final DatabaseViewerException e) {
                ErrorDialog.showError(this, e, "Failed to show item");
            }
            this.infoLabel.setText((this.position + 1) + "/" + this.keyValues.length);
        }

        private void start() {
            this.position = 0;
            showCurrentItem();
            enableButtons();
        }

        private void end() {
            this.position = this.keyValues.length - 1;
            showCurrentItem();
            enableButtons();
        }
    }

    protected abstract PageViewPanel createPanel() throws DatabaseViewerException;

    protected DatabaseViewerManager getManager() {
        return this.viewerManager;
    }

    public PagingDatabaseViewer() {
    }

    public void initialize(final DatabaseViewerManager manager) {
        this.viewerManager = manager;
    }

    public final void showView(final String whereClause) throws DatabaseViewerException {
        final Frame parentWindow = DatabaseViewerManager.getParentWindow();
        PagingDatabaseViewerDialog dialog;
        try {
            dialog = new PagingDatabaseViewerDialog(parentWindow);
            preferences.restoreWindowPlacement(dialog, new Rectangle(100, 100, 350, 300));
            dialog.showView(whereClause);
        } catch (final DatabaseViewerException e) {
            ErrorDialog.showError(parentWindow, e, "Viewer could not be initialized");
        }
    }
}
