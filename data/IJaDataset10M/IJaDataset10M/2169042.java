package net.sf.mpxj.mpd;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;
import net.sf.mpxj.MPXJException;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.reader.ProjectReader;

/**
 * This class provides a generic front end to read project data from
 * a database.
 */
public final class MPDDatabaseReader implements ProjectReader {

    /**
    * Populates a Map instance representing the IDs and names of
    * projects available in the current database.
    * 
    * @return Map instance containing ID and name pairs
    * @throws MPXJException
    */
    public Map<Integer, String> listProjects() throws MPXJException {
        MPD9DatabaseReader reader = new MPD9DatabaseReader();
        return reader.listProjects();
    }

    /**
    * Read project data from a database.
    * 
    * @return ProjectFile instance
    * @throws MPXJException
    */
    public ProjectFile read() throws MPXJException {
        MPD9DatabaseReader reader = new MPD9DatabaseReader();
        reader.setProjectID(m_projectID);
        reader.setPreserveNoteFormatting(m_preserveNoteFormatting);
        reader.setDataSource(m_dataSource);
        reader.setConnection(m_connection);
        ProjectFile project = reader.read();
        return (project);
    }

    /**
    * Set the ID of the project to be read.
    * 
    * @param projectID project ID
    */
    public void setProjectID(int projectID) {
        m_projectID = Integer.valueOf(projectID);
    }

    /**
    * This method sets a flag to indicate whether the RTF formatting associated
    * with notes should be preserved or removed. By default the formatting
    * is removed.
    *
    * @param preserveNoteFormatting boolean flag
    */
    public void setPreserveNoteFormatting(boolean preserveNoteFormatting) {
        m_preserveNoteFormatting = preserveNoteFormatting;
    }

    /**
    * Set the data source. A DataSource or a Connection can be supplied
    * to this class to allow connection to the database.
    * 
    * @param dataSource data source
    */
    public void setDataSource(DataSource dataSource) {
        m_dataSource = dataSource;
    }

    /**
    * Sets the connection. A DataSource or a Connection can be supplied
    * to this class to allow connection to the database.
    * 
    * @param connection database connection
    */
    public void setConnection(Connection connection) {
        m_connection = connection;
    }

    /**
    * This is a convenience method which reads the first project
    * from the named MPD file using the JDBC-ODBC bridge driver.
    * 
    * @param accessDatabaseFileName access database file name
    * @return ProjectFile instance
    * @throws MPXJException
    */
    public ProjectFile read(String accessDatabaseFileName) throws MPXJException {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            String url = "jdbc:odbc:DRIVER=Microsoft Access Driver (*.mdb);DBQ=" + accessDatabaseFileName;
            m_connection = DriverManager.getConnection(url);
            m_projectID = Integer.valueOf(1);
            return (read());
        } catch (ClassNotFoundException ex) {
            throw new MPXJException("Failed to load JDBC driver", ex);
        } catch (SQLException ex) {
            throw new MPXJException("Failed to create connection", ex);
        } finally {
            if (m_connection != null) {
                try {
                    m_connection.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    /**
    * {@inheritDoc}
    */
    public ProjectFile read(File file) throws MPXJException {
        return (read(file.getAbsolutePath()));
    }

    /**
    * {@inheritDoc}
    */
    public ProjectFile read(InputStream inputStream) {
        throw new UnsupportedOperationException();
    }

    private Integer m_projectID;

    private DataSource m_dataSource;

    private Connection m_connection;

    private boolean m_preserveNoteFormatting;
}
