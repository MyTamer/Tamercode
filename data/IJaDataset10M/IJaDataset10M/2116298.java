package org.jtools.sql.delegate;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

public class DelegationDatabaseMetaData extends DelegationWrapper<DatabaseMetaData> implements DatabaseMetaData {

    public DelegationDatabaseMetaData(DatabaseMetaData delegate) {
        super(delegate);
    }

    public boolean allProceduresAreCallable() throws SQLException {
        return delegate.allProceduresAreCallable();
    }

    public boolean allTablesAreSelectable() throws SQLException {
        return delegate.allTablesAreSelectable();
    }

    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        return delegate.autoCommitFailureClosesAllResultSets();
    }

    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        return delegate.dataDefinitionCausesTransactionCommit();
    }

    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        return delegate.dataDefinitionIgnoredInTransactions();
    }

    public boolean deletesAreDetected(int type) throws SQLException {
        return delegate.deletesAreDetected(type);
    }

    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        return delegate.doesMaxRowSizeIncludeBlobs();
    }

    public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
        return delegate.getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern);
    }

    public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
        return delegate.getBestRowIdentifier(catalog, schema, table, scope, nullable);
    }

    public ResultSet getCatalogs() throws SQLException {
        return delegate.getCatalogs();
    }

    public String getCatalogSeparator() throws SQLException {
        return delegate.getCatalogSeparator();
    }

    public String getCatalogTerm() throws SQLException {
        return delegate.getCatalogTerm();
    }

    public ResultSet getClientInfoProperties() throws SQLException {
        return delegate.getClientInfoProperties();
    }

    public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
        return delegate.getColumnPrivileges(catalog, schema, table, columnNamePattern);
    }

    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        return delegate.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
    }

    public Connection getConnection() throws SQLException {
        return delegate.getConnection();
    }

    public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
        return delegate.getCrossReference(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable);
    }

    public int getDatabaseMajorVersion() throws SQLException {
        return delegate.getDatabaseMajorVersion();
    }

    public int getDatabaseMinorVersion() throws SQLException {
        return delegate.getDatabaseMinorVersion();
    }

    public String getDatabaseProductName() throws SQLException {
        return delegate.getDatabaseProductName();
    }

    public String getDatabaseProductVersion() throws SQLException {
        return delegate.getDatabaseProductVersion();
    }

    public int getDefaultTransactionIsolation() throws SQLException {
        return delegate.getDefaultTransactionIsolation();
    }

    public int getDriverMajorVersion() {
        return delegate.getDriverMajorVersion();
    }

    public int getDriverMinorVersion() {
        return delegate.getDriverMinorVersion();
    }

    public String getDriverName() throws SQLException {
        return delegate.getDriverName();
    }

    public String getDriverVersion() throws SQLException {
        return delegate.getDriverVersion();
    }

    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        return delegate.getExportedKeys(catalog, schema, table);
    }

    public String getExtraNameCharacters() throws SQLException {
        return delegate.getExtraNameCharacters();
    }

    public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
        return delegate.getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern);
    }

    public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
        return delegate.getFunctions(catalog, schemaPattern, functionNamePattern);
    }

    public String getIdentifierQuoteString() throws SQLException {
        return delegate.getIdentifierQuoteString();
    }

    public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
        return delegate.getImportedKeys(catalog, schema, table);
    }

    public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
        return delegate.getIndexInfo(catalog, schema, table, unique, approximate);
    }

    public int getJDBCMajorVersion() throws SQLException {
        return delegate.getJDBCMajorVersion();
    }

    public int getJDBCMinorVersion() throws SQLException {
        return delegate.getJDBCMinorVersion();
    }

    public int getMaxBinaryLiteralLength() throws SQLException {
        return delegate.getMaxBinaryLiteralLength();
    }

    public int getMaxCatalogNameLength() throws SQLException {
        return delegate.getMaxCatalogNameLength();
    }

    public int getMaxCharLiteralLength() throws SQLException {
        return delegate.getMaxCharLiteralLength();
    }

    public int getMaxColumnNameLength() throws SQLException {
        return delegate.getMaxColumnNameLength();
    }

    public int getMaxColumnsInGroupBy() throws SQLException {
        return delegate.getMaxColumnsInGroupBy();
    }

    public int getMaxColumnsInIndex() throws SQLException {
        return delegate.getMaxColumnsInIndex();
    }

    public int getMaxColumnsInOrderBy() throws SQLException {
        return delegate.getMaxColumnsInOrderBy();
    }

    public int getMaxColumnsInSelect() throws SQLException {
        return delegate.getMaxColumnsInSelect();
    }

    public int getMaxColumnsInTable() throws SQLException {
        return delegate.getMaxColumnsInTable();
    }

    public int getMaxConnections() throws SQLException {
        return delegate.getMaxConnections();
    }

    public int getMaxCursorNameLength() throws SQLException {
        return delegate.getMaxCursorNameLength();
    }

    public int getMaxIndexLength() throws SQLException {
        return delegate.getMaxIndexLength();
    }

    public int getMaxProcedureNameLength() throws SQLException {
        return delegate.getMaxProcedureNameLength();
    }

    public int getMaxRowSize() throws SQLException {
        return delegate.getMaxRowSize();
    }

    public int getMaxSchemaNameLength() throws SQLException {
        return delegate.getMaxSchemaNameLength();
    }

    public int getMaxStatementLength() throws SQLException {
        return delegate.getMaxStatementLength();
    }

    public int getMaxStatements() throws SQLException {
        return delegate.getMaxStatements();
    }

    public int getMaxTableNameLength() throws SQLException {
        return delegate.getMaxTableNameLength();
    }

    public int getMaxTablesInSelect() throws SQLException {
        return delegate.getMaxTablesInSelect();
    }

    public int getMaxUserNameLength() throws SQLException {
        return delegate.getMaxUserNameLength();
    }

    public String getNumericFunctions() throws SQLException {
        return delegate.getNumericFunctions();
    }

    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        return delegate.getPrimaryKeys(catalog, schema, table);
    }

    public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
        return delegate.getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern);
    }

    public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
        return delegate.getProcedures(catalog, schemaPattern, procedureNamePattern);
    }

    public String getProcedureTerm() throws SQLException {
        return delegate.getProcedureTerm();
    }

    public int getResultSetHoldability() throws SQLException {
        return delegate.getResultSetHoldability();
    }

    public RowIdLifetime getRowIdLifetime() throws SQLException {
        return delegate.getRowIdLifetime();
    }

    public ResultSet getSchemas() throws SQLException {
        return delegate.getSchemas();
    }

    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
        return delegate.getSchemas(catalog, schemaPattern);
    }

    public String getSchemaTerm() throws SQLException {
        return delegate.getSchemaTerm();
    }

    public String getSearchStringEscape() throws SQLException {
        return delegate.getSearchStringEscape();
    }

    public String getSQLKeywords() throws SQLException {
        return delegate.getSQLKeywords();
    }

    public int getSQLStateType() throws SQLException {
        return delegate.getSQLStateType();
    }

    public String getStringFunctions() throws SQLException {
        return delegate.getStringFunctions();
    }

    public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        return delegate.getSuperTables(catalog, schemaPattern, tableNamePattern);
    }

    public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
        return delegate.getSuperTypes(catalog, schemaPattern, typeNamePattern);
    }

    public String getSystemFunctions() throws SQLException {
        return delegate.getSystemFunctions();
    }

    public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        return delegate.getTablePrivileges(catalog, schemaPattern, tableNamePattern);
    }

    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        return delegate.getTables(catalog, schemaPattern, tableNamePattern, types);
    }

    public ResultSet getTableTypes() throws SQLException {
        return delegate.getTableTypes();
    }

    public String getTimeDateFunctions() throws SQLException {
        return delegate.getTimeDateFunctions();
    }

    public ResultSet getTypeInfo() throws SQLException {
        return delegate.getTypeInfo();
    }

    public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
        return delegate.getUDTs(catalog, schemaPattern, typeNamePattern, types);
    }

    public String getURL() throws SQLException {
        return delegate.getURL();
    }

    public String getUserName() throws SQLException {
        return delegate.getUserName();
    }

    public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
        return delegate.getVersionColumns(catalog, schema, table);
    }

    public boolean insertsAreDetected(int type) throws SQLException {
        return delegate.insertsAreDetected(type);
    }

    public boolean isCatalogAtStart() throws SQLException {
        return delegate.isCatalogAtStart();
    }

    public boolean isReadOnly() throws SQLException {
        return delegate.isReadOnly();
    }

    public boolean locatorsUpdateCopy() throws SQLException {
        return delegate.locatorsUpdateCopy();
    }

    public boolean nullPlusNonNullIsNull() throws SQLException {
        return delegate.nullPlusNonNullIsNull();
    }

    public boolean nullsAreSortedAtEnd() throws SQLException {
        return delegate.nullsAreSortedAtEnd();
    }

    public boolean nullsAreSortedAtStart() throws SQLException {
        return delegate.nullsAreSortedAtStart();
    }

    public boolean nullsAreSortedHigh() throws SQLException {
        return delegate.nullsAreSortedHigh();
    }

    public boolean nullsAreSortedLow() throws SQLException {
        return delegate.nullsAreSortedLow();
    }

    public boolean othersDeletesAreVisible(int type) throws SQLException {
        return delegate.othersDeletesAreVisible(type);
    }

    public boolean othersInsertsAreVisible(int type) throws SQLException {
        return delegate.othersInsertsAreVisible(type);
    }

    public boolean othersUpdatesAreVisible(int type) throws SQLException {
        return delegate.othersUpdatesAreVisible(type);
    }

    public boolean ownDeletesAreVisible(int type) throws SQLException {
        return delegate.ownDeletesAreVisible(type);
    }

    public boolean ownInsertsAreVisible(int type) throws SQLException {
        return delegate.ownInsertsAreVisible(type);
    }

    public boolean ownUpdatesAreVisible(int type) throws SQLException {
        return delegate.ownUpdatesAreVisible(type);
    }

    public boolean storesLowerCaseIdentifiers() throws SQLException {
        return delegate.storesLowerCaseIdentifiers();
    }

    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        return delegate.storesLowerCaseQuotedIdentifiers();
    }

    public boolean storesMixedCaseIdentifiers() throws SQLException {
        return delegate.storesMixedCaseIdentifiers();
    }

    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        return delegate.storesMixedCaseQuotedIdentifiers();
    }

    public boolean storesUpperCaseIdentifiers() throws SQLException {
        return delegate.storesUpperCaseIdentifiers();
    }

    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        return delegate.storesUpperCaseQuotedIdentifiers();
    }

    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        return delegate.supportsAlterTableWithAddColumn();
    }

    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        return delegate.supportsAlterTableWithDropColumn();
    }

    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        return delegate.supportsANSI92EntryLevelSQL();
    }

    public boolean supportsANSI92FullSQL() throws SQLException {
        return delegate.supportsANSI92FullSQL();
    }

    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        return delegate.supportsANSI92IntermediateSQL();
    }

    public boolean supportsBatchUpdates() throws SQLException {
        return delegate.supportsBatchUpdates();
    }

    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        return delegate.supportsCatalogsInDataManipulation();
    }

    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        return delegate.supportsCatalogsInIndexDefinitions();
    }

    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        return delegate.supportsCatalogsInPrivilegeDefinitions();
    }

    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        return delegate.supportsCatalogsInProcedureCalls();
    }

    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        return delegate.supportsCatalogsInTableDefinitions();
    }

    public boolean supportsColumnAliasing() throws SQLException {
        return delegate.supportsColumnAliasing();
    }

    public boolean supportsConvert() throws SQLException {
        return delegate.supportsConvert();
    }

    public boolean supportsConvert(int fromType, int toType) throws SQLException {
        return delegate.supportsConvert(fromType, toType);
    }

    public boolean supportsCoreSQLGrammar() throws SQLException {
        return delegate.supportsCoreSQLGrammar();
    }

    public boolean supportsCorrelatedSubqueries() throws SQLException {
        return delegate.supportsCorrelatedSubqueries();
    }

    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        return delegate.supportsDataDefinitionAndDataManipulationTransactions();
    }

    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        return delegate.supportsDataManipulationTransactionsOnly();
    }

    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        return delegate.supportsDifferentTableCorrelationNames();
    }

    public boolean supportsExpressionsInOrderBy() throws SQLException {
        return delegate.supportsExpressionsInOrderBy();
    }

    public boolean supportsExtendedSQLGrammar() throws SQLException {
        return delegate.supportsExtendedSQLGrammar();
    }

    public boolean supportsFullOuterJoins() throws SQLException {
        return delegate.supportsFullOuterJoins();
    }

    public boolean supportsGetGeneratedKeys() throws SQLException {
        return delegate.supportsGetGeneratedKeys();
    }

    public boolean supportsGroupBy() throws SQLException {
        return delegate.supportsGroupBy();
    }

    public boolean supportsGroupByBeyondSelect() throws SQLException {
        return delegate.supportsGroupByBeyondSelect();
    }

    public boolean supportsGroupByUnrelated() throws SQLException {
        return delegate.supportsGroupByUnrelated();
    }

    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        return delegate.supportsIntegrityEnhancementFacility();
    }

    public boolean supportsLikeEscapeClause() throws SQLException {
        return delegate.supportsLikeEscapeClause();
    }

    public boolean supportsLimitedOuterJoins() throws SQLException {
        return delegate.supportsLimitedOuterJoins();
    }

    public boolean supportsMinimumSQLGrammar() throws SQLException {
        return delegate.supportsMinimumSQLGrammar();
    }

    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        return delegate.supportsMixedCaseIdentifiers();
    }

    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        return delegate.supportsMixedCaseQuotedIdentifiers();
    }

    public boolean supportsMultipleOpenResults() throws SQLException {
        return delegate.supportsMultipleOpenResults();
    }

    public boolean supportsMultipleResultSets() throws SQLException {
        return delegate.supportsMultipleResultSets();
    }

    public boolean supportsMultipleTransactions() throws SQLException {
        return delegate.supportsMultipleTransactions();
    }

    public boolean supportsNamedParameters() throws SQLException {
        return delegate.supportsNamedParameters();
    }

    public boolean supportsNonNullableColumns() throws SQLException {
        return delegate.supportsNonNullableColumns();
    }

    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        return delegate.supportsOpenCursorsAcrossCommit();
    }

    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        return delegate.supportsOpenCursorsAcrossRollback();
    }

    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        return delegate.supportsOpenStatementsAcrossCommit();
    }

    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        return delegate.supportsOpenStatementsAcrossRollback();
    }

    public boolean supportsOrderByUnrelated() throws SQLException {
        return delegate.supportsOrderByUnrelated();
    }

    public boolean supportsOuterJoins() throws SQLException {
        return delegate.supportsOuterJoins();
    }

    public boolean supportsPositionedDelete() throws SQLException {
        return delegate.supportsPositionedDelete();
    }

    public boolean supportsPositionedUpdate() throws SQLException {
        return delegate.supportsPositionedUpdate();
    }

    public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
        return delegate.supportsResultSetConcurrency(type, concurrency);
    }

    public boolean supportsResultSetHoldability(int holdability) throws SQLException {
        return delegate.supportsResultSetHoldability(holdability);
    }

    public boolean supportsResultSetType(int type) throws SQLException {
        return delegate.supportsResultSetType(type);
    }

    public boolean supportsSavepoints() throws SQLException {
        return delegate.supportsSavepoints();
    }

    public boolean supportsSchemasInDataManipulation() throws SQLException {
        return delegate.supportsSchemasInDataManipulation();
    }

    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        return delegate.supportsSchemasInIndexDefinitions();
    }

    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        return delegate.supportsSchemasInPrivilegeDefinitions();
    }

    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        return delegate.supportsSchemasInProcedureCalls();
    }

    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        return delegate.supportsSchemasInTableDefinitions();
    }

    public boolean supportsSelectForUpdate() throws SQLException {
        return delegate.supportsSelectForUpdate();
    }

    public boolean supportsStatementPooling() throws SQLException {
        return delegate.supportsStatementPooling();
    }

    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        return delegate.supportsStoredFunctionsUsingCallSyntax();
    }

    public boolean supportsStoredProcedures() throws SQLException {
        return delegate.supportsStoredProcedures();
    }

    public boolean supportsSubqueriesInComparisons() throws SQLException {
        return delegate.supportsSubqueriesInComparisons();
    }

    public boolean supportsSubqueriesInExists() throws SQLException {
        return delegate.supportsSubqueriesInExists();
    }

    public boolean supportsSubqueriesInIns() throws SQLException {
        return delegate.supportsSubqueriesInIns();
    }

    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        return delegate.supportsSubqueriesInQuantifieds();
    }

    public boolean supportsTableCorrelationNames() throws SQLException {
        return delegate.supportsTableCorrelationNames();
    }

    public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
        return delegate.supportsTransactionIsolationLevel(level);
    }

    public boolean supportsTransactions() throws SQLException {
        return delegate.supportsTransactions();
    }

    public boolean supportsUnion() throws SQLException {
        return delegate.supportsUnion();
    }

    public boolean supportsUnionAll() throws SQLException {
        return delegate.supportsUnionAll();
    }

    public boolean updatesAreDetected(int type) throws SQLException {
        return delegate.updatesAreDetected(type);
    }

    public boolean usesLocalFilePerTable() throws SQLException {
        return delegate.usesLocalFilePerTable();
    }

    public boolean usesLocalFiles() throws SQLException {
        return delegate.usesLocalFiles();
    }
}
