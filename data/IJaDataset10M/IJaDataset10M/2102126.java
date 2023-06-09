package org.openXpertya.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import org.openXpertya.model.MClient;
import org.openXpertya.model.M_Column;
import org.openXpertya.model.M_Table;
import org.openXpertya.util.DB;
import org.openXpertya.util.DisplayType;
import org.openXpertya.util.ErrorUsuarioOXP;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class TranslationDocSync extends SvrProcess {

    /**
     * Descripción de Método
     *
     */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null) {
                ;
            } else {
                log.log(Level.SEVERE, "Unknown Parameter: " + name);
            }
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     *
     * @throws Exception
     */
    protected String doIt() throws Exception {
        MClient client = MClient.get(getCtx());
        if (client.isMultiLingualDocument()) {
            throw new ErrorUsuarioOXP("@AD_Client_ID@: @IsMultiLingualDocument@");
        }
        log.info("" + client);
        String sql = "SELECT * FROM AD_Table " + "WHERE TableName LIKE '%_Trl' AND TableName NOT LIKE 'AD%' " + "ORDER BY TableName";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                processTable(new M_Table(getCtx(), rs, null), client.getAD_Client_ID());
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        return "OK";
    }

    /**
     * Descripción de Método
     *
     *
     * @param table
     * @param AD_Client_ID
     */
    private void processTable(M_Table table, int AD_Client_ID) {
        StringBuffer sql = new StringBuffer();
        M_Column[] columns = table.getColumns(false);
        for (int i = 0; i < columns.length; i++) {
            M_Column column = columns[i];
            if ((column.getAD_Reference_ID() == DisplayType.String) || (column.getAD_Reference_ID() == DisplayType.Text)) {
                String columnName = column.getColumnName();
                if (sql.length() != 0) {
                    sql.append(",");
                }
                sql.append(columnName);
            }
        }
        String baseTable = table.getTableName();
        baseTable = baseTable.substring(0, baseTable.length() - 4);
        log.config(baseTable + ": " + sql);
        String columnNames = sql.toString();
        sql = new StringBuffer();
        sql.append("UPDATE ").append(table.getTableName()).append(" t SET (").append(columnNames).append(") = (SELECT ").append(columnNames).append(" FROM ").append(baseTable).append(" b WHERE t.").append(baseTable).append("_ID=b.").append(baseTable).append("_ID) WHERE AD_Client_ID=").append(AD_Client_ID);
        int no = DB.executeUpdate(sql.toString());
        addLog(0, null, new BigDecimal(no), baseTable);
    }
}
