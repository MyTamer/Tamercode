package repast.simphony.chart2.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.SwingUtilities;
import repast.simphony.chart2.engine.TimeSeriesComponentControllerAction;
import repast.simphony.chart2.wizard.TimeSeriesEditorWizard;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.util.DataUtilities;
import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.ui.plugin.AbstractEditorMenuItem;

/**
 * Implements the "Add Time Series Chart" menu item.
 * 
 * @author Nick Collier
 */
public class TimeSeriesChartMenuItem extends AbstractEditorMenuItem {

    private static final long serialVersionUID = 5878701522312055100L;

    public TimeSeriesChartMenuItem() {
        super("Add Time Series Chart");
    }

    private TimeSeriesComponentControllerAction createAction(Component parent, List<DataSetDescriptor> dataSets) {
        TimeSeriesEditorWizard wizard = new TimeSeriesEditorWizard(dataSets);
        wizard.showDialog(parent, "Time Series Editor");
        if (wizard.wasCancelled()) return null;
        TimeSeriesComponentControllerAction action = new TimeSeriesComponentControllerAction(wizard.getModel().getDescriptor());
        return action;
    }

    /**
   * Invoked when an action occurs.
   */
    public void actionPerformed(ActionEvent e) {
        Object contextID = evt.getContextID();
        TimeSeriesComponentControllerAction action = createAction(SwingUtilities.getWindowAncestor(evt.getTree()), DataUtilities.getDataSetDescriptors(evt.getRegistry(), evt.getContextID()));
        if (action != null) {
            ControllerRegistry registry = evt.getRegistry();
            ControllerAction parent = registry.findAction(contextID, ControllerActionConstants.CHART_ROOT);
            registry.addAction(contextID, parent, action);
            evt.addActionToTree(action);
        }
    }
}
