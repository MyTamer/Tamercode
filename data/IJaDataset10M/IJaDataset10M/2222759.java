package owl2prefuse.tree.rdf;

import owl2prefuse.Constants;
import prefuse.Visualization;
import prefuse.action.assignment.ColorAction;
import prefuse.visual.VisualItem;

/**
 * This class is a specific ColorAction for the nodes in the tree.
 * <p/>
 * Project OWL2Prefuse <br/>
 * RDFNodeColorAction.java created 2 januari 2007, 14:45
 * <p/>
 * Copyright &copy 2006 Jethro Borsje
 * @author <a href="mailto:info@jborsje.nl">Jethro Borsje</a>
 * @version $$Revision:$$, $$Date:$$
 */
public class RDFNodeColorAction extends ColorAction {

    /**
     * Creates a new instance of RDFNodeColorAction.
     * @param p_group The data group for which this ColorAction provides the colors.
     * @param p_vis A reference to the visualization processed by this Action.
     */
    public RDFNodeColorAction(String p_group, Visualization p_vis) {
        super(p_group, VisualItem.FILLCOLOR);
        m_vis = p_vis;
    }

    /**
     * This method returns the color of the given VisualItem.
     * @param p_item The node for which the color needs to be retreived.
     * @return The color of the given node.
     */
    public int getColor(VisualItem p_item) {
        int retval = Constants.NODE_DEFAULT_COLOR;
        if (m_vis.isInGroup(p_item, Visualization.SEARCH_ITEMS)) retval = Constants.NODE_COLOR_SEARCH; else if (m_vis.isInGroup(p_item, Visualization.FOCUS_ITEMS)) retval = Constants.NODE_COLOR_SELECTED; else if (p_item.getDOI() > -1) retval = Constants.NODE_COLOR_HIGHLIGHTED; else if (p_item.canGetString("type")) {
            if (p_item.getString("type") != null) {
                if (p_item.getString("type").equals("resource")) retval = Constants.NODE_COLOR_RESOURCE; else if (p_item.getString("type").equals("literal")) retval = Constants.NODE_COLOR_LITERAL;
            }
        }
        return retval;
    }
}
