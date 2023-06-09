package net.sourceforge.squirrel_sql.plugins.h2.exp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.INodeExpander;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.ObjectTreeNode;
import net.sourceforge.squirrel_sql.fw.sql.DatabaseObjectInfo;
import net.sourceforge.squirrel_sql.fw.sql.DatabaseObjectType;
import net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectInfo;
import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;
import net.sourceforge.squirrel_sql.fw.sql.SQLDatabaseMetaData;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;

/**
 * 
 * @author manningr
 *
 */
public class TriggerParentExpander implements INodeExpander {

    /** Logger for this class. */
    private static final ILogger s_log = LoggerController.createLogger(TriggerParentExpander.class);

    private static String SQL = "select trigger_name " + "from INFORMATION_SCHEMA.TRIGGERS " + "where TABLE_SCHEMA = ? " + "and TABLE_NAME = ? ";

    /**
     * 
     */
    public TriggerParentExpander() {
        super();
    }

    /**
     * Create the child nodes for the passed parent node and return them. Note
     * that this method should <B>not </B> actually add the child nodes to the
     * parent node as this is taken care of in the caller.
     * 
     * @param session
     *            Current session.
     * @param node
     *            Node to be expanded.
     * 
     * @return A list of <TT>ObjectTreeNode</TT> objects representing the
     *         child nodes for the passed node.
     */
    public List createChildren(ISession session, ObjectTreeNode parentNode) throws SQLException {
        final List childNodes = new ArrayList();
        final IDatabaseObjectInfo parentDbinfo = parentNode.getDatabaseObjectInfo();
        final SQLConnection conn = session.getSQLConnection();
        final SQLDatabaseMetaData md = session.getSQLConnection().getSQLMetaData();
        final String schemaName = parentDbinfo.getSchemaName();
        final String catalogName = parentDbinfo.getCatalogName();
        final IDatabaseObjectInfo tableInfo = ((TriggerParentInfo) parentDbinfo).getTableInfo();
        PreparedStatement pstmt = null;
        try {
            if (s_log.isDebugEnabled()) {
                s_log.debug("Running SQL to find triggers for table (" + tableInfo.getSimpleName() + ") : " + SQL);
                s_log.debug("Schema: " + tableInfo.getSchemaName());
            }
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, tableInfo.getSchemaName());
            pstmt.setString(2, tableInfo.getSimpleName());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                DatabaseObjectInfo doi = new DatabaseObjectInfo(catalogName, schemaName, rs.getString(1), DatabaseObjectType.TRIGGER, md);
                childNodes.add(new ObjectTreeNode(session, doi));
            }
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
        }
        return childNodes;
    }
}
