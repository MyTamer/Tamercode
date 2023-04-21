package org.jdbc4olap.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import org.jdbc4olap.xmla.XmlaConn;

/**
 * @author <a href="mailto:fsiberchicot@jdbc4olap.org">Florian SIBERCHICOT</a>
 * @author Dan Rollo
 */
class OlapStatement implements Statement {

    final DatabaseMetaData olapMetadata;

    final XmlaConn xmlaConn;

    OlapResultSet resultSet;

    OlapStatement(final OlapConnection c) throws SQLException {
        olapMetadata = c.getMetaData();
        xmlaConn = c.getXmlaConn();
        resultSet = OlapResultSet.createEmptyResultSet();
    }

    public void addBatch(final String sql) throws SQLException {
        throw new SQLException("jdbc4olap driver does not support batch");
    }

    public void cancel() throws SQLException {
    }

    public void clearBatch() throws SQLException {
        throw new SQLException("jdbc4olap driver does not support batch");
    }

    public void clearWarnings() throws SQLException {
    }

    public void close() throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
        resultSet = null;
    }

    public boolean execute(final String sql) throws SQLException {
        resultSet = (OlapResultSet) executeQuery(sql);
        return true;
    }

    public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
        return execute(sql);
    }

    public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
        return execute(sql);
    }

    public boolean execute(final String sql, final String[] columnNames) throws SQLException {
        return execute(sql);
    }

    public int[] executeBatch() throws SQLException {
        throw new SQLException("jdbc4olap driver does not support batch");
    }

    public ResultSet executeQuery(final String sql) throws SQLException {
        if (sql == null || "".equals(sql)) {
            resultSet = OlapResultSet.createEmptyResultSet();
            return resultSet;
        }
        StatementHelper statementHelper = new StatementHelper(sql, olapMetadata, xmlaConn);
        resultSet = (OlapResultSet) statementHelper.processQuery();
        resultSet.setStatement(this);
        return resultSet;
    }

    public int executeUpdate(final String sql) throws SQLException {
        execute(sql);
        return 0;
    }

    public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        execute(sql);
        return 0;
    }

    public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        execute(sql);
        return 0;
    }

    public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
        execute(sql);
        return 0;
    }

    public Connection getConnection() throws SQLException {
        return olapMetadata.getConnection();
    }

    public int getFetchDirection() throws SQLException {
        return resultSet.getFetchDirection();
    }

    public int getFetchSize() throws SQLException {
        return resultSet.getFetchSize();
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        return OlapResultSet.createEmptyResultSet();
    }

    public int getMaxFieldSize() throws SQLException {
        return 0;
    }

    public int getMaxRows() throws SQLException {
        return 0;
    }

    public boolean getMoreResults() throws SQLException {
        return false;
    }

    public boolean getMoreResults(final int current) throws SQLException {
        return false;
    }

    public int getQueryTimeout() throws SQLException {
        return 0;
    }

    public ResultSet getResultSet() throws SQLException {
        return resultSet;
    }

    public int getResultSetConcurrency() throws SQLException {
        return ResultSet.CONCUR_READ_ONLY;
    }

    public int getResultSetHoldability() throws SQLException {
        return ResultSet.HOLD_CURSORS_OVER_COMMIT;
    }

    public int getResultSetType() throws SQLException {
        return resultSet.getType();
    }

    public int getUpdateCount() throws SQLException {
        return -1;
    }

    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    public void setCursorName(final String name) throws SQLException {
    }

    public void setEscapeProcessing(final boolean enable) throws SQLException {
    }

    public void setFetchDirection(final int direction) throws SQLException {
        resultSet.setFetchDirection(direction);
    }

    public void setFetchSize(final int rows) throws SQLException {
        resultSet.setFetchSize(rows);
    }

    public void setMaxFieldSize(final int max) throws SQLException {
    }

    public void setMaxRows(final int max) throws SQLException {
    }

    public void setQueryTimeout(final int seconds) throws SQLException {
    }
}