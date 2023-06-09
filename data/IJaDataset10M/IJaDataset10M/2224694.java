package com.volantis.xml.pipeline.testtools.sqlconnector;

import com.volantis.synergetics.testtools.HypersonicManager;
import com.volantis.synergetics.testtools.stubs.DataSourceStub;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * A class that provides utility methods for setting up an Hypersonic database
 * and using it in the testing and configuration of an SQL Connector.
 */
public class HypersonicHelper {

    public static String EMPLOYEE_TABLE = "Employees";

    public static String DATASOURCE_NAME = "HH_DataSource";

    /**
     * An alternative data source
     */
    public static String ALTERNATIVE_DATASOURCE_NAME = "HH_2_DataSource";

    public static String FOOTBALL_TEAMS = "Football_Teams";

    public static final String EXPECTED_SELECT_ALL = "<sql-result:result xmlns:sql-result=\"http://www.volantis.com/xmlns/marlin-sql-result\">" + "<sql-result:row>" + "<sql-result:string column=\"NAME\">Brian</sql-result:string>" + "<sql-result:date column=\"START_DAY\">2003-06-11</sql-result:date>" + "<sql-result:boolean column=\"REDUNDANT\">false</sql-result:boolean>" + "<sql-result:int column=\"AGE\">32</sql-result:int>" + "<sql-result:long column=\"SECONDS_EMPLOYED\">8989</sql-result:long>" + "<sql-result:time column=\"CLOCKIN_TIME\">09:09:09</sql-result:time>" + "<sql-result:timestamp column=\"TERMINATION_TIME\">2003-06-11 10:10:10.1</sql-result:timestamp>" + "<sql-result:decimal column=\"WEIGHT_KG\">200.8</sql-result:decimal>" + "<sql-result:float column=\"CLOUD_COVER\">7.3</sql-result:float>" + "<sql-result:double column=\"GENUM\">3.141592653589793</sql-result:double>" + "</sql-result:row>" + "<sql-result:row>" + "<sql-result:string column=\"NAME\">Derek</sql-result:string>" + "<sql-result:date column=\"START_DAY\">2003-06-11</sql-result:date>" + "<sql-result:boolean column=\"REDUNDANT\">true</sql-result:boolean>" + "<sql-result:int column=\"AGE\">32</sql-result:int>" + "<sql-result:long column=\"SECONDS_EMPLOYED\">92342</sql-result:long>" + "<sql-result:time column=\"CLOCKIN_TIME\">10:10:10</sql-result:time>" + "<sql-result:timestamp column=\"TERMINATION_TIME\">2003-06-11 11:11:11.11</sql-result:timestamp>" + "<sql-result:decimal column=\"WEIGHT_KG\">99.8</sql-result:decimal>" + "<sql-result:float column=\"CLOUD_COVER\">2.3</sql-result:float>" + "<sql-result:double column=\"GENUM\">3.141592653589793</sql-result:double>" + "</sql-result:row>" + "</sql-result:result>\n";

    public static final String SELECT_ALL = "select * from " + EMPLOYEE_TABLE;

    /**
     * Clean up the files generated by the HypersonicManager.
     */
    public void cleanupHypersonicManager(HypersonicManager hsManager) throws SQLException, IOException, ClassNotFoundException {
        Class.forName(HypersonicManager.DEFAULT_DRIVER_CLASS);
        Connection connection = createHypersonicConnection(hsManager);
        Statement statement = connection.createStatement();
        statement.execute("shutdown");
        statement.close();
        hsManager.cleanupFiles();
    }

    /**
     * Create a connection to an Hypersonic database.
     * @param hsManager The HypersonicManager to assist in obtaining the
     * connection.
     * @return A Connection.
     * @throws java.sql.SQLException If there is a problem obtaining the Connection.
     */
    private Connection createHypersonicConnection(HypersonicManager hsManager) throws SQLException {
        return DriverManager.getConnection(hsManager.getUrl(), HypersonicManager.DEFAULT_USERNAME, HypersonicManager.DEFAULT_PASSWORD);
    }

    /**
     * Use a Connection to an Hypersonic database to set up the contents of
     * this database for testing  - i.e. create a table and put some data into
     * it.
     * @param hsConnection The Connection.
     * @throws java.sql.SQLException If there is a problem accessing the database.
     */
    private void setupHypersonicDatabase(Connection hsConnection) throws SQLException {
        Statement statement = hsConnection.createStatement();
        String sql = "CREATE TABLE " + EMPLOYEE_TABLE + " ( " + "NAME VARCHAR (128), " + "START_DAY DATE, " + "REDUNDANT BIT, " + "AGE INT, " + "SECONDS_EMPLOYED BIGINT, " + "CLOCKIN_TIME TIME, " + "TERMINATION_TIME TIMESTAMP, " + "WEIGHT_KG DECIMAL, " + "CLOUD_COVER FLOAT, " + "GENUM DOUBLE" + " ) ";
        statement.execute(sql);
        sql = "INSERT INTO " + EMPLOYEE_TABLE + " values ( " + "'Brian', '2003-06-11', 0, 32, 8989, " + "'09:09:09', '2003-06-11 10:10:10.1', 200.8, 7.3, PI() )";
        statement.execute(sql);
        sql = "INSERT INTO " + EMPLOYEE_TABLE + " values ( " + "'Derek', '2003-06-11', 1, 32, 92342, " + "'10:10:10', '2003-06-11 11:11:11.11', 99.8, 2.3, PI() )";
        statement.execute(sql);
        sql = "CREATE TABLE " + FOOTBALL_TEAMS + "(" + "NAME VARCHAR (128) NOT NULL, " + "GROUND VARCHAR (32) NOT NULL, " + "MANAGER VARCHAR (64) NOT NULL, " + "LAST_SEASON VARCHAR (256) NULL" + " ) ";
        statement.execute(sql);
        sql = "INSERT INTO " + FOOTBALL_TEAMS + " values (" + "'Glasgow Rangers', 'Ibrox', 'Alex McLeish'," + "'<LastSeason><league>winners</league>" + "<scottishCup>winners</scottishCup>" + "<leagueCup>winners</leagueCup></LastSeason>')";
        statement.execute(sql);
        sql = "INSERT INTO " + FOOTBALL_TEAMS + " values (" + "'Glasgow Celtic', 'Parkhead', 'Martin ONeill'," + "'<uefa:LastSeason xmlns:uefa=\"http://uefacup.com\">" + "<league>runners up</league>" + "<scottishCup>4th round</scottishCup>" + "<leagueCup>runners up</leagueCup></uefa:LastSeason>')";
        statement.execute(sql);
        sql = "INSERT INTO " + FOOTBALL_TEAMS + " values (" + "'Partick Thistle', 'Firhill', 'John Lambie'," + "'<LastSeason xmlns:urid=\"http://www.volantis.com/uri-driver\">" + "<urid:fetch href=\"partickThistle2003Honours.xml\"/>" + "</LastSeason>')";
        statement.execute(sql);
        sql = "INSERT INTO " + FOOTBALL_TEAMS + " values (" + "'Queen of the South', 'No Idea', 'Not a clue', NULL)";
        statement.execute(sql);
    }

    /**
     * Create a Connection to an Hypersonic database and use this connection
     * to set up the database. Then return the connection.
     * @param hsManager The HypersonicManager to assist in obtaining the
     * connection.
     * @return The Connection.
     * @throws java.sql.SQLException If there is a problem creating the connection or
     * setting up the database.
     */
    public Connection provideConnectionToDatabase(HypersonicManager hsManager) throws SQLException, IOException, ClassNotFoundException {
        cleanupHypersonicManager(hsManager);
        Connection connection = createHypersonicConnection(hsManager);
        try {
            setupHypersonicDatabase(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Create a DataSource based on a Hypersonic database that has been set
     * up using provideConnectionToDatabase().
     * @param hsManager The HypersonicManager to assist in obtaining the
     * data source.
     * @return A DataSource whose getConnection() method provides a Connection
     * to the Hypersonic database set up by this class. No other method in the
     * returned DataSource does anything useful.
     */
    public DataSource provideDataSourceToDatabase(HypersonicManager hsManager) throws SQLException, IOException, ClassNotFoundException {
        final Connection connection = provideConnectionToDatabase(hsManager);
        DataSource dataSource = new DataSourceStub() {

            public Connection getConnection() {
                return connection;
            }
        };
        return dataSource;
    }
}
