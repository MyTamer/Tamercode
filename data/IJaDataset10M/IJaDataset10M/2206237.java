package org.objectstyle.cayenne.access.jdbc;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.objectstyle.cayenne.dba.TypesMapping;
import org.objectstyle.cayenne.map.DbAttribute;
import org.objectstyle.cayenne.map.ObjAttribute;
import org.objectstyle.cayenne.map.ProcedureParameter;

/**
 * A descriptor of a ResultSet column.
 * 
 * @since 1.1
 * @author Andrei Adamchik
 */
public class ColumnDescriptor implements Serializable {

    protected String tableName;

    protected String procedureName;

    protected String name;

    protected String qualifiedColumnName;

    protected String label;

    /**
     * @deprecated since 1.2
     */
    protected boolean primaryKey;

    protected int jdbcType;

    protected String javaClass;

    /**
     * Creates a ColumnDescriptor
     */
    public ColumnDescriptor() {
    }

    /**
     * Creates a column descriptor with user-specified parameters.
     * 
     * @since 1.2
     */
    public ColumnDescriptor(String columnName, int jdbcType, String javaClass) {
        this.name = columnName;
        this.qualifiedColumnName = columnName;
        this.label = columnName;
        this.jdbcType = jdbcType;
        this.javaClass = javaClass;
    }

    /**
     * Creates a ColumnDescriptor from Cayenne ObjAttribute and DbAttribute.
     * 
     * @deprecated since 1.2 use constructor with column alias parameter.
     */
    public ColumnDescriptor(ObjAttribute objAttribute, DbAttribute dbAttribute) {
        this(objAttribute, dbAttribute, null);
    }

    /**
     * Creates a ColumnDescriptor from Cayenne DbAttribute.
     * 
     * @since 1.2
     */
    public ColumnDescriptor(DbAttribute attribute, String columnAlias) {
        this.name = attribute.getName();
        this.qualifiedColumnName = attribute.getAliasedName(columnAlias);
        this.label = name;
        this.jdbcType = attribute.getType();
        this.primaryKey = attribute.isPrimaryKey();
        this.javaClass = getDefaultJavaClass(attribute.getMaxLength(), attribute.getPrecision());
        if (attribute.getEntity() != null) {
            this.tableName = attribute.getEntity().getName();
        }
    }

    /**
     * @since 1.2
     */
    public ColumnDescriptor(ObjAttribute objAttribute, DbAttribute dbAttribute, String columnAlias) {
        this(dbAttribute, columnAlias);
        this.label = objAttribute.getDbAttributePath();
        this.javaClass = objAttribute.getType();
    }

    /**
     * Creates a ColumnDescriptor from stored procedure parameter.
     * 
     * @since 1.2
     */
    public ColumnDescriptor(ProcedureParameter parameter) {
        this.name = parameter.getName();
        this.qualifiedColumnName = name;
        this.label = name;
        this.jdbcType = parameter.getType();
        this.javaClass = getDefaultJavaClass(parameter.getMaxLength(), parameter.getPrecision());
        if (parameter.getProcedure() != null) {
            this.procedureName = parameter.getProcedure().getName();
        }
    }

    /**
     * Creates a ColumnDescriptor using ResultSetMetaData.
     * 
     * @since 1.2
     */
    public ColumnDescriptor(ResultSetMetaData metaData, int position) throws SQLException {
        String name = metaData.getColumnLabel(position);
        if (name == null || name.length() == 0) {
            name = metaData.getColumnName(position);
            if (name == null || name.length() == 0) {
                name = "column_" + position;
            }
        }
        this.name = name;
        this.qualifiedColumnName = name;
        this.label = name;
        this.jdbcType = metaData.getColumnType(position);
        this.javaClass = getDefaultJavaClass(metaData.getColumnDisplaySize(position), metaData.getScale(position));
    }

    /**
     * Returns true if another object is a ColumnDescriptor with the same name, name
     * prefix, table and procedure names. Other fields are ignored in the equality test.
     * 
     * @since 1.2
     */
    public boolean equals(Object o) {
        if (!(o instanceof ColumnDescriptor)) {
            return false;
        }
        ColumnDescriptor rhs = (ColumnDescriptor) o;
        return new EqualsBuilder().append(name, rhs.name).append(qualifiedColumnName, rhs.qualifiedColumnName).append(procedureName, rhs.procedureName).append(label, rhs.label).append(tableName, rhs.tableName).isEquals();
    }

    /**
     * @since 1.2
     */
    public int hashCode() {
        return new HashCodeBuilder(23, 43).append(name).append(qualifiedColumnName).append(procedureName).append(tableName).append(label).toHashCode();
    }

    /**
     * @since 1.2
     */
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("namePrefix", getQualifiedColumnName());
        builder.append("name", getName());
        builder.append("label", getLabel());
        builder.append("tableName", getTableName());
        builder.append("procedureName", getProcedureName());
        builder.append("javaClass", getJavaClass());
        builder.append("jdbcType", getJdbcType());
        return builder.toString();
    }

    /**
     * Returns a default Java class for an internal JDBC type.
     * 
     * @since 1.2
     */
    public String getDefaultJavaClass(int size, int scale) {
        return TypesMapping.getJavaBySqlType(getJdbcType(), size, scale);
    }

    /**
     * Returns "qualifiedColumnName" property.
     * 
     * @since 1.2
     */
    public String getQualifiedColumnName() {
        return qualifiedColumnName != null ? qualifiedColumnName : name;
    }

    public int getJdbcType() {
        return jdbcType;
    }

    /**
     * Retunrs column name. Name is an unqualified column name in a query.
     */
    public String getName() {
        return name;
    }

    public void setJdbcType(int i) {
        jdbcType = i;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @deprecated since 1.2
     */
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public String getJavaClass() {
        return javaClass;
    }

    /**
     * @deprecated since 1.2
     */
    public void setPrimaryKey(boolean b) {
        primaryKey = b;
    }

    public void setJavaClass(String string) {
        javaClass = string;
    }

    /**
     * Returns the name of the parent table.
     * 
     * @since 1.2
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @since 1.2
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Returns the name of the parent stored procedure.
     * 
     * @since 1.2
     */
    public String getProcedureName() {
        return procedureName;
    }

    /**
     * @since 1.2
     */
    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    /**
     * @since 1.2
     */
    public void setQualifiedColumnName(String namePrefix) {
        this.qualifiedColumnName = namePrefix;
    }

    /**
     * Returns "label" used in a DataRow for column value.
     * 
     * @since 1.2
     */
    public String getLabel() {
        return (label != null) ? label : getName();
    }

    /**
     * @since 1.2
     */
    public void setLabel(String columnName) {
        this.label = columnName;
    }
}
