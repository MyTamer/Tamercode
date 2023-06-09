package org.dbmaintain.dbsupport;

import org.dbmaintain.util.DbMaintainException;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Very simple DataSource that provides access to a database without any connection pooling facilities. Only the
 * parameterless getConnection() method is implemented.
 *
 * @author Filip Neven
 * @author Tim Ducheyne
 * @since 3-jan-2009
 */
public class DbMaintainDataSource {

    /**
     * Static factory that returns a datasource providing access to the database using the driver with the given driver
     * classname, the given connection url, username and password
     *
     * @param driverClassName The name of the JDBC driver
     * @param url URL that points to the database
     * @param userName database userName
     * @param password database password
     * @return a DataSource that gives access to the database
     */
    public static DataSource createDataSource(String driverClassName, String url, String userName, String password) {
        return (DataSource) Proxy.newProxyInstance(DbMaintainDataSource.class.getClassLoader(), new Class<?>[] { DataSource.class }, new DbMaintainDataSourceInvocationHandler(driverClassName, url, userName, password));
    }

    /**
     * Invocation handler for a dynamic proxy that implements the javax.sql.DataSource interface. This invocation handler
     * implements the getConnection method: this method will create a new database connection to the database configured
     * with the driverClassName, url, userName and password given with the constructor invocation.
     */
    protected static class DbMaintainDataSourceInvocationHandler implements InvocationHandler {

        private String driverClassName;

        private String url;

        private String userName;

        private String password;

        protected DbMaintainDataSourceInvocationHandler(String driverClassName, String url, String userName, String password) {
            this.driverClassName = driverClassName;
            this.url = url;
            this.userName = userName;
            this.password = password;
        }

        /**
         * Handles all invocations on the DataSource. We only implement the parameterless getConnection method, since this
         * is the only method used in dbmaintain
         *
         * @param dataSourceProxy The proxy that represents the datasource
         * @param method The method invoked on the datasource
         * @param args The arguments of the invoked method
         * @return The return object
         * @throws Throwable
         */
        public Object invoke(Object dataSourceProxy, Method method, Object[] args) throws Throwable {
            if (isEqualsMethod(method)) {
                return dataSourceProxy == args[0];
            } else if (isHashCodeMethod(method)) {
                return hashCode();
            } else if (isParameterLessGetConnectionMethod(method)) {
                return getDatabaseConnection();
            }
            return null;
        }

        /**
         * @return A connection to the database
         */
        protected Connection getDatabaseConnection() {
            try {
                Class.forName(driverClassName);
            } catch (ClassNotFoundException e) {
                throw new DbMaintainException("Driver class not found: " + driverClassName, e);
            }
            try {
                return DriverManager.getConnection(url, userName, password);
            } catch (SQLException e) {
                throw new DbMaintainException("Unable to load driver for url: " + url, e);
            }
        }

        /**
         * @param method The invoked method
         * @return Whether the given method is the equals method
         */
        protected boolean isEqualsMethod(Method method) {
            return "equals".equals(method.getName()) && method.getParameterTypes().length == 1 && Object.class.equals(method.getParameterTypes()[0]);
        }

        /**
         * @param method The invoked method
         * @return Whether the given method is the hashCode method
         */
        protected boolean isHashCodeMethod(Method method) {
            return "hashCode".equals(method.getName()) && method.getParameterTypes().length == 0;
        }

        /**
         * @param method The method invoked on the datasource proxy
         * @return whether the given method is the method DataSource.getConnection()
         */
        protected boolean isParameterLessGetConnectionMethod(Method method) {
            return "getConnection".equals(method.getName()) && method.getParameterTypes().length == 0;
        }
    }
}
