package com.mysql.jdbc.jdbc2.optional;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

/**
 * This class is used to obtain a physical connection and instantiate and return
 * a MysqlPooledConnection. J2EE application servers map client calls to
 * dataSource.getConnection to this class based upon mapping set within
 * deployment descriptor. This class extends MysqlDataSource.
 * 
 * @see javax.sql.PooledConnection
 * @see javax.sql.ConnectionPoolDataSource
 * @see org.gjt.mm.mysql.MysqlDataSource
 * @author Todd Wolff <todd.wolff_at_prodigy.net>
 */
public class MysqlConnectionPoolDataSource extends MysqlDataSource implements ConnectionPoolDataSource {

    /**
	 * Returns a pooled connection.
	 * 
	 * @exception SQLException
	 *                if an error occurs
	 * @return a PooledConnection
	 */
    public synchronized PooledConnection getPooledConnection() throws SQLException {
        Connection connection = getConnection();
        MysqlPooledConnection mysqlPooledConnection = new MysqlPooledConnection((com.mysql.jdbc.Connection) connection);
        return mysqlPooledConnection;
    }

    /**
	 * This method is invoked by the container. Obtains physical connection
	 * using mySql.Driver class and returns a mysqlPooledConnection object.
	 * 
	 * @param s
	 *            user name
	 * @param s1
	 *            password
	 * @exception SQLException
	 *                if an error occurs
	 * @return a PooledConnection
	 */
    public synchronized PooledConnection getPooledConnection(String s, String s1) throws SQLException {
        Connection connection = getConnection(s, s1);
        MysqlPooledConnection mysqlPooledConnection = new MysqlPooledConnection((com.mysql.jdbc.Connection) connection);
        return mysqlPooledConnection;
    }
}
