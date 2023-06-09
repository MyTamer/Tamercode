package zet.gui.main.toolbar;

import batch.BatchResult;
import batch.BatchResultEntry;
import de.tu_berlin.math.coga.common.localization.Localized;
import gui.GUIControl;
import gui.ZETMain;
import de.tu_berlin.math.coga.datastructure.NamedIndex;
import gui.components.framework.Button;
import gui.components.framework.IconSet;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import statistic.graph.Controller;
import zet.gui.GUILocalization;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class JStatisticGraphToolBar extends JToolBar implements ActionListener, Localized {

    private final GUIControl control;

    /** The localization class. */
    static final GUILocalization loc = GUILocalization.getSingleton();

    private JButton btnExit;

    private JButton btnOpenResults;

    private JButton btnSaveResults;

    private JLabel labelBatchName;

    private BatchResultEntryGRSComboBoxModel entryModelGraph;

    public JStatisticGraphToolBar(GUIControl control) {
        this.control = control;
        createStatisticsToolBar();
        control.setGraphStatisticToolBar(this);
    }

    /**
	 * Creates the {@code JToolBar} for the statistic view.
	 */
    private void createStatisticsToolBar() {
        loc.setPrefix("gui.toolbar.");
        btnExit = Button.newButton(IconSet.Exit, this, "exit", loc.getString("Exit"));
        add(btnExit);
        addSeparator();
        btnOpenResults = Button.newButton(IconSet.Open, null, "loadBatchResult", loc.getString("Open"));
        add(btnOpenResults);
        btnSaveResults = Button.newButton(IconSet.Save, null, "saveResultAs", loc.getString("Save"));
        add(btnSaveResults);
        addSeparator();
        labelBatchName = new JLabel(loc.getString("Visualization.BatchName"));
        add(labelBatchName);
        entryModelGraph = new BatchResultEntryGRSComboBoxModel();
        JComboBox cbxBatchEntry = new JComboBox(entryModelGraph);
        cbxBatchEntry.setMaximumRowCount(10);
        cbxBatchEntry.setMaximumSize(new Dimension(250, cbxBatchEntry.getPreferredSize().height));
        cbxBatchEntry.setPreferredSize(new Dimension(250, cbxBatchEntry.getPreferredSize().height));
        cbxBatchEntry.setAlignmentX(0);
        add(cbxBatchEntry);
        loc.setPrefix("");
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("exit")) control.exit(); else if (e.getActionCommand().equals("loadBatchResult")) {
        } else if (e.getActionCommand().equals("saveResultsAs")) {
        } else ZETMain.sendError(loc.getString("gui.UnknownCommand") + " '" + e.getActionCommand() + "'. " + loc.getString("gui.ContactDeveloper"));
    }

    public void localize() {
        loc.setPrefix("gui.toolbar.");
        labelBatchName.setText(loc.getString("Visualization.BatchName"));
        btnExit.setToolTipText(loc.getString("Exit"));
        btnSaveResults.setToolTipText(loc.getString("Save"));
        btnOpenResults.setToolTipText(loc.getString("Open"));
        loc.clearPrefix();
    }

    public void rebuild(BatchResult result) {
        entryModelGraph.rebuild(result);
    }

    /**
	 * This class serves as a model for the JComboBox that contains the
	 * BatchResultEntries for the Graph statistics Tab.
	 */
    private class BatchResultEntryGRSComboBoxModel extends DefaultComboBoxModel {

        BatchResult result;

        public void rebuild(BatchResult result) {
            this.result = result;
            removeAllElements();
            int index = 0;
            for (String e : result.getEntryNames()) super.addElement(new NamedIndex(e, index++));
        }

        @Override
        public void setSelectedItem(Object object) {
            super.setSelectedItem(object);
            BatchResultEntry entry = (BatchResultEntry) getSelectedItem();
            Controller.getInstance().setFlow(entry.getGraph(), entry.getFlow());
        }

        @Override
        public Object getSelectedItem() {
            try {
                if (result != null && super.getSelectedItem() != null) return result.getResult(((NamedIndex) super.getSelectedItem()).getIndex()); else return null;
            } catch (IOException ex) {
                ZETMain.sendError("Error while loading temp file: " + ex.getLocalizedMessage());
                return null;
            }
        }
    }
}
