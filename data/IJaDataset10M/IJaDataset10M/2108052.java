package org.makumba.providers.query.mql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Properties;
import org.makumba.DataDefinition;
import org.makumba.FieldDefinition;
import org.makumba.commons.ClassResource;
import org.makumba.commons.NameResolver;
import org.makumba.providers.QueryAnalysisProvider;
import org.makumba.providers.query.hql.HQLQueryAnalysisProvider;
import org.makumba.providers.query.oql.QueryAST;
import antlr.collections.AST;

/**
 * Test the Mql analyzer against a query corpus found in queries.txt. Compare the generated sql with the one of the old
 * OQL parser.
 * 
 * @author Cristian Bogdan
 * @version $Id: ParserTest.java,v 1.1 Aug 5, 2008 5:55:08 PM cristi Exp $
 */
public class ParserTest {

    private static QueryAnalysisProvider qap = new HQLQueryAnalysisProvider();

    private static PrintWriter pw = new PrintWriter(System.out);

    private static NameResolver nr;

    static {
        String databaseProperties = "test/localhost_mysql_makumba.properties";
        Properties p = new Properties();
        try {
            p.load(org.makumba.commons.ClassResource.get(databaseProperties).openStream());
        } catch (Exception e) {
            throw new org.makumba.ConfigFileError(databaseProperties);
        }
        nr = new NameResolver();
    }

    public static void main(String[] argv) {
        int line = 1;
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader((InputStream) ClassResource.get("org/makumba/providers/query/mql/queries.txt").getContent()));
            String query = null;
            while ((query = rd.readLine()) != null) {
                if (!query.trim().startsWith("#")) {
                    analyseQuery(line, query);
                }
                line++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("analyzed " + line + " queries");
    }

    /** cleanup of generated SQL code for MQL-OQL comparison purposes */
    static String cleanUp(String s, String toRemove) {
        StringBuffer ret = new StringBuffer();
        char lastNonSpace = 0;
        boolean prevSpace = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (toRemove.indexOf(c) == -1) {
                if (prevSpace) {
                    if (Character.isJavaIdentifierStart(c) && Character.isJavaIdentifierPart(lastNonSpace)) {
                        ret.append(' ');
                    }
                }
                lastNonSpace = c;
                ret.append(c);
                prevSpace = false;
                continue;
            }
            prevSpace = true;
        }
        return ret.toString();
    }

    private static void analyseQuery(int line, String query) {
        AST hql_sql = null;
        boolean passedMql = false;
        Throwable thr = null;
        boolean printedError;
        String oql_sql = null;
        String mql_sql = null;
        MqlQueryAnalysis mq = null;
        Throwable mqlThr = null;
        try {
            mq = new MqlQueryAnalysis(query, false);
            mql_sql = mq.writeInSQLQuery(nr).toLowerCase();
        } catch (Throwable t) {
            mqlThr = t;
        }
        try {
            QueryAST oq = (QueryAST) QueryAST.parseQueryFundamental(query);
            oql_sql = oq.writeInSQLQuery(nr).toLowerCase();
            if (mqlThr == null) {
                oql_sql = cleanUp(oql_sql, " ").replace('\"', '\'');
                mql_sql = cleanUp(mql_sql, " ");
                if (!oql_sql.equals(mql_sql) && !cleanUp(oql_sql, "()").equals(cleanUp(mql_sql, "()"))) {
                    System.out.println(line + ": MQL!=OQL: " + query + "\n\t" + mql_sql + "\n\t" + oql_sql);
                }
                StringBuffer sb = new StringBuffer();
                compareMdds("parameter", sb, mq.getParameterTypes(), oq.getParameterTypes());
                if (sb.length() > 0) System.out.println(line + ": " + sb + " " + query);
                sb = new StringBuffer();
                compareMdds("projection", sb, mq.getProjectionType(), oq.getProjectionType());
                if (sb.length() > 0) System.out.println(line + ": " + sb + " " + query);
                String mqLabels = mq.getLabelTypes().toString();
                if (!mqLabels.equals(oq.getLabelTypes().toString()) && !mqLabels.equals("{c=org.makumba.db.makumba.Catalog}")) {
                    System.out.println(line + ": " + query + "\n\t" + mq.getLabelTypes() + "\n\t" + oq.getLabelTypes());
                }
            }
        } catch (Throwable t) {
            if (mqlThr != null) System.err.println(line + ": MQL: " + mqlThr.getMessage() + " " + query);
            System.err.println(line + ":" + (mqlThr == null ? " only in" : "") + " OQL: " + t.getMessage() + " " + query);
            if (mqlThr == null) System.out.println(line + ": MQL SQL: " + mql_sql);
            return;
        }
        if (mqlThr != null) {
            System.err.println(line + ": only in MQL: " + mqlThr.getMessage() + " " + query);
            System.out.println(line + ": OQL SQL: " + oql_sql);
            if (!(mqlThr instanceof antlr.ANTLRException)) mqlThr.printStackTrace();
        }
    }

    private static void compareMdds(String what, StringBuffer sb, DataDefinition mdd1, DataDefinition mdd2) {
        HashSet<String> fieldsDone = new HashSet<String>();
        for (String s : mdd1.getFieldNames()) {
            FieldDefinition fd1 = mdd1.getFieldDefinition(s);
            FieldDefinition fd2 = mdd2.getFieldDefinition(s);
            if (fd2 == null) {
                sb.append("extra MQL ").append(what).append(": ");
                appendFieldDefinition(sb, fd1);
                sb.append("\n");
                continue;
            }
            if (!fd1.isAssignableFrom(fd2) && !(fd1.getType().equals("boolean") && fd2.getType().equals("int"))) {
                sb.append(what).append(" ").append(s).append(" MQL: ");
                appendFieldDefinition(sb, fd1);
                sb.append(" OQL: ");
                appendFieldDefinition(sb, fd2);
                sb.append("\n");
            }
            fieldsDone.add(s);
        }
        if (mdd2 != null) for (String s : mdd2.getFieldNames()) {
            if (!fieldsDone.contains(s)) {
                FieldDefinition fd2 = mdd2.getFieldDefinition(s);
                sb.append("extra OQL ").append(what).append(": ");
                appendFieldDefinition(sb, fd2);
                sb.append("\n");
            }
        }
    }

    private static void appendFieldDefinition(StringBuffer sb, FieldDefinition fd) {
        sb.append(fd.getName()).append("=").append(fd.getType());
    }
}
