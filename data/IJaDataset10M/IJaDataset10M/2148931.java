package org.ramadda.plugins.community;

import org.ramadda.repository.*;
import org.ramadda.repository.auth.*;
import org.ramadda.repository.metadata.*;
import org.ramadda.repository.output.*;
import org.ramadda.repository.type.*;
import org.w3c.dom.*;
import ucar.unidata.sql.Clause;
import ucar.unidata.sql.SqlUtil;
import ucar.unidata.sql.SqlUtil;
import ucar.unidata.util.DateUtil;
import ucar.unidata.util.HtmlUtil;
import ucar.unidata.util.HttpServer;
import ucar.unidata.util.IOUtil;
import ucar.unidata.util.LogUtil;
import ucar.unidata.util.Misc;
import ucar.unidata.util.StringUtil;
import ucar.unidata.util.WikiUtil;
import ucar.unidata.xml.XmlUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

/**
 *
 *
 */
public class UserTypeHandler extends ExtensibleGroupTypeHandler {

    /** _more_ */
    public static String TYPE_USER = "users_user";

    /**
     * _more_
     *
     * @param repository _more_
     * @param entryNode _more_
     *
     * @throws Exception _more_
     */
    public UserTypeHandler(Repository repository, Element entryNode) throws Exception {
        super(repository, entryNode);
    }

    /**
     * _more_
     *
     * @param requst _more_
     * @param entry _more_
     * @param output _more_
     *
     * @return _more_
     */
    public boolean shouldShowInHtml(Request requst, Entry entry, OutputType output) {
        return output.equals(OutputHandler.OUTPUT_HTML) || output.equals(HtmlOutputHandler.OUTPUT_INLINE);
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param entry _more_
     * @param sb _more_
     */
    public void handleNoEntriesHtml(Request request, Entry entry, StringBuffer sb) {
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param group _more_
     * @param subGroups _more_
     * @param entries _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    public Result getHtmlDisplay(Request request, Entry group, List<Entry> subGroups, List<Entry> entries) throws Exception {
        if (true) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        boolean canAdd = getAccessManager().canDoAction(request, group, Permission.ACTION_NEW);
        if (canAdd) {
        }
        subGroups.addAll(entries);
        for (Entry entry : subGroups) {
            String link = HtmlUtil.href(request.entryUrl(getRepository().URL_ENTRY_SHOW, entry), HtmlUtil.img(getRepository().iconUrl(ICON_ENTRY), msg("View entry details")));
            sb.append("<a name=" + entry.getId() + "></a>");
            sb.append("<li>");
            sb.append(" ");
            sb.append(HtmlUtil.b(entry.getName()));
            sb.append(HtmlUtil.br());
            sb.append(entry.getDescription());
        }
        return new Result(msg("Missing Person"), sb);
    }
}
