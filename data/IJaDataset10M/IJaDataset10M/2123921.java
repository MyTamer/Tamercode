package net.sourceforge.squirrel_sql.plugins.informix.exp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.INodeExpander;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.ObjectTreeNode;
import net.sourceforge.squirrel_sql.client.session.schemainfo.ObjFilterMatcher;
import net.sourceforge.squirrel_sql.fw.sql.DatabaseObjectInfo;
import net.sourceforge.squirrel_sql.fw.sql.DatabaseObjectType;
import net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectInfo;
import net.sourceforge.squirrel_sql.fw.sql.ISQLConnection;
import net.sourceforge.squirrel_sql.fw.sql.SQLDatabaseMetaData;
import net.sourceforge.squirrel_sql.fw.sql.SQLUtilities;

/**
 * This class handles the expanding of the "Sequence Group"
 * node. It will give a list of all the Sequences available in the schema.
 *
 * @author manningr
 */
public class SequenceParentExpander implements INodeExpander {

    /** SQL used to load sequence names  */
    private static final String SQL = "SELECT  T2.tabname AS sequence_name " + "FROM   informix.syssequences AS T1, " + "       informix.systables    AS T2 " + "WHERE   T2.tabid = T1.tabid " + "and T2.owner = ? " + "and T2.tabname like ? ";

    /**
	 * Default ctor.
	 */
    public SequenceParentExpander() {
        super();
    }

    /**
	 * Create the child nodes for the passed parent node and return them. Note
	 * that this method should <B>not</B> actually add the child nodes to the
	 * parent node as this is taken care of in the caller.
	 *
	 * @param	session	Current session.
	 * @param	node	Node to be expanded.
	 *
	 * @return	A list of <TT>ObjectTreeNode</TT> objects representing the child
	 *			nodes for the passed node.
	 */
    public List<ObjectTreeNode> createChildren(ISession session, ObjectTreeNode parentNode) throws SQLException {
        final List<ObjectTreeNode> childNodes = new ArrayList<ObjectTreeNode>();
        final IDatabaseObjectInfo parentDbinfo = parentNode.getDatabaseObjectInfo();
        final ISQLConnection conn = session.getSQLConnection();
        final SQLDatabaseMetaData md = session.getSQLConnection().getSQLMetaData();
        final String catalogName = parentDbinfo.getCatalogName();
        final String schemaName = parentDbinfo.getSchemaName();
        final ObjFilterMatcher filterMatcher = new ObjFilterMatcher(session.getProperties());
        final PreparedStatement pstmt = conn.prepareStatement(SQL);
        ResultSet rs = null;
        try {
            pstmt.setString(1, schemaName);
            pstmt.setString(2, filterMatcher.getSqlLikeMatchString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                IDatabaseObjectInfo si = new DatabaseObjectInfo(catalogName, schemaName, rs.getString(1), DatabaseObjectType.SEQUENCE, md);
                if (filterMatcher.matches(si.getSimpleName())) {
                    childNodes.add(new ObjectTreeNode(session, si));
                }
            }
        } finally {
            SQLUtilities.closeResultSet(rs);
            SQLUtilities.closeStatement(pstmt);
        }
        return childNodes;
    }
}
