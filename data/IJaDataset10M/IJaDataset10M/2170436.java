package org.postgresql.jdbc4;

public class Jdbc4Clob extends AbstractJdbc4Clob implements java.sql.Clob {

    public Jdbc4Clob(org.postgresql.core.BaseConnection conn, long oid) throws java.sql.SQLException {
        super(conn, oid);
    }
}
