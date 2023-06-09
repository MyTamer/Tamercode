package jmri.jmrix.openlcb.swing.tie;

import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Pane for showing the tie table
 * @author	 Bob Jacobsen 2008
 * @version	 $Revision: 1.2 $
 * @since 2.3.7
 */
public class TieTablePane extends JPanel {

    static ResourceBundle rb = ResourceBundle.getBundle("jmri.jmrix.openlcb.swing.tie.TieBundle");

    protected JTable table = null;

    protected TableModel tableModel = null;

    public void initComponents() throws Exception {
        setSize(500, 300);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        tableModel = new TieTableModel();
        table = jmri.util.JTableUtil.sortableDataModel(tableModel);
        table.setRowSelectionAllowed(true);
        table.setPreferredScrollableViewportSize(new java.awt.Dimension(300, 350));
        TableColumnModel columnModel = table.getColumnModel();
        TableColumn column;
        column = columnModel.getColumn(TieTableModel.USERNAME_COLUMN);
        column.setMinWidth(20);
        column.setResizable(true);
        column = columnModel.getColumn(TieTableModel.ID_COLUMN);
        column.setMinWidth(40);
        column.setResizable(true);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }

    static org.apache.log4j.Category log = org.apache.log4j.Logger.getLogger(ProducerTablePane.class.getName());
}
