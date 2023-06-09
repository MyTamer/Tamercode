package com.j256.ormlite.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Types;
import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.stmt.GenericRowMapper;
import com.j256.ormlite.stmt.StatementBuilder.StatementType;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;
import com.j256.ormlite.support.GeneratedKeyHolder;

/**
 * Wrapper around a JDBC {@link Connection} object which we delegate to.
 * 
 * @author graywatson
 */
public class JdbcDatabaseConnection implements DatabaseConnection {

    private static Logger logger = LoggerFactory.getLogger(JdbcDatabaseConnection.class);

    private static final String JDBC_META_TABLE_NAME_COLUMN = "TABLE_NAME";

    private static Object[] noArgs = new Object[0];

    private static FieldType[] noArgTypes = new FieldType[0];

    private static GenericRowMapper<Long> longWrapper = new OneLongWrapper();

    private Connection connection;

    private Boolean supportsSavePoints = null;

    public JdbcDatabaseConnection(Connection connection) {
        this.connection = connection;
        logger.trace("connection opened: {}", connection);
    }

    public boolean isAutoCommitSupported() {
        return true;
    }

    public boolean isAutoCommit() throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        logger.trace("connection autoCommit is {}", autoCommit);
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
        logger.trace("connection set autoCommit to {}", autoCommit);
    }

    public Savepoint setSavePoint(String name) throws SQLException {
        if (supportsSavePoints == null) {
            DatabaseMetaData metaData = connection.getMetaData();
            supportsSavePoints = metaData.supportsSavepoints();
            logger.trace("connection supports save points is {}", supportsSavePoints);
        }
        if (supportsSavePoints) {
            Savepoint savepoint = connection.setSavepoint(name);
            logger.trace("save-point {} set with name {}", savepoint, name);
            return savepoint;
        } else {
            return null;
        }
    }

    public void commit(Savepoint savepoint) throws SQLException {
        if (savepoint == null) {
            connection.commit();
            logger.trace("connection committed");
        } else {
            Object obj = savepoint.getSavepointName();
            if (obj == null) {
                obj = savepoint;
            }
            connection.commit();
            logger.trace("connection is committed for save-point {}", obj);
        }
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        if (savepoint == null) {
            connection.rollback();
            logger.trace("connection is rolled back");
        } else {
            Object obj = savepoint.getSavepointName();
            if (obj == null) {
                obj = savepoint;
            }
            connection.rollback(savepoint);
            logger.trace("save-point {} is rolled back", obj);
        }
    }

    public CompiledStatement compileStatement(String statement, StatementType type, FieldType[] argFieldTypes) throws SQLException {
        return compileStatement(statement, type, argFieldTypes, DEFAULT_RESULT_FLAGS);
    }

    public CompiledStatement compileStatement(String statement, StatementType type, FieldType[] argFieldTypes, int resultFlags) throws SQLException {
        if (resultFlags == DatabaseConnection.DEFAULT_RESULT_FLAGS) {
            resultFlags = ResultSet.TYPE_FORWARD_ONLY;
        }
        JdbcCompiledStatement compiledStatement = new JdbcCompiledStatement(connection.prepareStatement(statement, resultFlags, ResultSet.CONCUR_READ_ONLY), type);
        logger.trace("compiled statement: {}", statement);
        return compiledStatement;
    }

    public void close() throws SQLException {
        connection.close();
        logger.trace("connection closed: {}", connection);
    }

    /**
	 * Returns whether the connection has already been closed. Used by {@link JdbcConnectionSource}.
	 */
    public boolean isClosed() throws SQLException {
        boolean isClosed = connection.isClosed();
        logger.trace("connection is closed returned {}", isClosed);
        return isClosed;
    }

    public int insert(String statement, Object[] args, FieldType[] argFieldTypes, GeneratedKeyHolder keyHolder) throws SQLException {
        PreparedStatement stmt;
        if (keyHolder == null) {
            stmt = connection.prepareStatement(statement);
        } else {
            stmt = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        }
        try {
            statementSetArgs(stmt, args, argFieldTypes);
            int rowN = stmt.executeUpdate();
            logger.trace("insert statement is prepared and executed: {}", statement);
            if (keyHolder != null) {
                ResultSet resultSet = stmt.getGeneratedKeys();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int colN = metaData.getColumnCount();
                while (resultSet.next()) {
                    for (int colC = 1; colC <= colN; colC++) {
                        Number id = getIdColumnData(resultSet, metaData, colC);
                        keyHolder.addKey(id);
                    }
                }
            }
            return rowN;
        } finally {
            stmt.close();
        }
    }

    public int update(String statement, Object[] args, FieldType[] argFieldTypes) throws SQLException {
        return update(statement, args, argFieldTypes, "update");
    }

    public int delete(String statement, Object[] args, FieldType[] argFieldTypes) throws SQLException {
        return update(statement, args, argFieldTypes, "delete");
    }

    public <T> Object queryForOne(String statement, Object[] args, FieldType[] argFieldTypes, GenericRowMapper<T> rowMapper, ObjectCache objectCache) throws SQLException {
        return queryForOne(statement, args, argFieldTypes, rowMapper, objectCache, "query for one");
    }

    public long queryForLong(String statement) throws SQLException {
        return queryForLong(statement, noArgs, noArgTypes);
    }

    public long queryForLong(String statement, Object[] args, FieldType[] argFieldTypes) throws SQLException {
        Object result = queryForOne(statement, args, argFieldTypes, longWrapper, null, "query for long");
        if (result == null) {
            throw new SQLException("No results returned in query-for-long: " + statement);
        } else if (result == MORE_THAN_ONE) {
            throw new SQLException("More than 1 result returned in query-for-long: " + statement);
        } else {
            return (Long) result;
        }
    }

    public boolean isTableExists(String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        logger.trace("Got meta data from connection");
        ResultSet results = null;
        try {
            results = metaData.getTables(null, null, "%", new String[] { "TABLE" });
            if (!results.next()) {
                return false;
            }
            int col = results.findColumn(JDBC_META_TABLE_NAME_COLUMN);
            do {
                String dbTableName = results.getString(col);
                if (tableName.equalsIgnoreCase(dbTableName)) {
                    return true;
                }
            } while (results.next());
            return false;
        } finally {
            if (results != null) {
                results.close();
            }
        }
    }

    /**
	 * Return the internal database connection. Most likely for testing purposes.
	 */
    public Connection getInternalConnection() {
        return connection;
    }

    /**
	 * Set the internal database connection. Most likely for testing purposes.
	 */
    public void setInternalConnection(Connection connection) {
        this.connection = connection;
    }

    private int update(String statement, Object[] args, FieldType[] argFieldTypes, String label) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(statement);
        try {
            statementSetArgs(stmt, args, argFieldTypes);
            int rowCount = stmt.executeUpdate();
            logger.trace("{} statement is prepared and executed returning {}: {}", label, rowCount, statement);
            return rowCount;
        } finally {
            stmt.close();
        }
    }

    private <T> Object queryForOne(String statement, Object[] args, FieldType[] argFieldTypes, GenericRowMapper<T> rowMapper, ObjectCache objectCache, String label) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(statement, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        try {
            statementSetArgs(stmt, args, argFieldTypes);
            DatabaseResults results = new JdbcDatabaseResults(stmt, stmt.executeQuery(), objectCache);
            logger.trace("{} statement is prepared and executed: {}", label, statement);
            if (!results.first()) {
                return null;
            }
            T first = rowMapper.mapRow(results);
            if (results.next()) {
                return MORE_THAN_ONE;
            } else {
                return first;
            }
        } finally {
            stmt.close();
        }
    }

    /**
	 * Return the id associated with the column.
	 */
    private Number getIdColumnData(ResultSet resultSet, ResultSetMetaData metaData, int columnIndex) throws SQLException {
        int typeVal = metaData.getColumnType(columnIndex);
        switch(typeVal) {
            case Types.BIGINT:
            case Types.DECIMAL:
            case Types.NUMERIC:
                return (Number) resultSet.getLong(columnIndex);
            case Types.INTEGER:
                return (Number) resultSet.getInt(columnIndex);
            default:
                String columnName = metaData.getColumnName(columnIndex);
                throw new SQLException("Unexpected ID column type " + TypeValMapper.getSqlTypeForTypeVal(typeVal) + " (typeVal " + typeVal + ") in column " + columnName + "(#" + columnIndex + ") is not a number");
        }
    }

    private void statementSetArgs(PreparedStatement stmt, Object[] args, FieldType[] argFieldTypes) throws SQLException {
        if (args == null) {
            return;
        }
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            int typeVal = TypeValMapper.getTypeValForSqlType(argFieldTypes[i].getSqlType());
            if (arg == null) {
                stmt.setNull(i + 1, typeVal);
            } else {
                stmt.setObject(i + 1, arg, typeVal);
            }
        }
    }

    /**
	 * Row mapper that handles a single long result.
	 */
    private static class OneLongWrapper implements GenericRowMapper<Long> {

        public Long mapRow(DatabaseResults rs) throws SQLException {
            return rs.getLong(0);
        }
    }
}
