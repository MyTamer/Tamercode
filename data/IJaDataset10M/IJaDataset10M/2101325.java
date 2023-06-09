package org.firebirdsql.management;

import org.firebirdsql.gds.*;
import org.firebirdsql.gds.impl.AbstractGDS;
import org.firebirdsql.gds.impl.GDSFactory;
import org.firebirdsql.gds.impl.GDSType;
import org.firebirdsql.logging.Logger;
import org.firebirdsql.logging.LoggerFactory;

/**
 * The class <code>FBManager</code> is a simple jmx mbean that allows you
 * to create and drop databases.  in particular, they can be created and
 * dropped using the jboss service lifecycle operations start and stop.
 *
 * @author <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @version 1.0
 * @jmx.mbean
 */
public class FBManager implements FBManagerMBean {

    private static final int DEFAULT_PORT = 3050;

    private static final Logger log = LoggerFactory.getLogger(FBManager.class, false);

    private GDS gds;

    private DatabaseParameterBuffer c;

    private String host = "localhost";

    private Integer port;

    private String fileName;

    private String userName;

    private String password;

    private boolean forceCreate = false;

    private boolean createOnStart = false;

    private boolean dropOnStop = false;

    private String state = STOPPED;

    private static final String STOPPED = "Stopped";

    private static final String STARTED = "Started";

    private GDSType type;

    public FBManager() {
        this(((AbstractGDS) GDSFactory.getDefaultGDS()).getType());
    }

    public FBManager(GDSType type) {
        this.type = type;
    }

    public FBManager(String type) {
        this.type = GDSType.getType(type);
    }

    /**
     * @jmx.managed-operation
     */
    public void start() throws Exception {
        gds = GDSFactory.getGDSForType(type);
        c = gds.createDatabaseParameterBuffer();
        c.addArgument(DatabaseParameterBuffer.DUMMY_PACKET_INTERVAL, new byte[] { 120, 10, 0, 0 });
        c.addArgument(DatabaseParameterBuffer.SQL_DIALECT, new byte[] { 3, 0, 0, 0 });
        state = STARTED;
        if (isCreateOnStart()) {
            createDatabase(getFileName(), getUserName(), getPassword());
        }
    }

    /**
     * @jmx.managed-operation
     */
    public void stop() throws Exception {
        if (isDropOnStop()) {
            dropDatabase(getFileName(), getUserName(), getPassword());
        }
        c = null;
        gds.close();
        gds = null;
        state = STOPPED;
    }

    /**
     * @jmx.managed-attribute
     */
    public String getState() {
        return state;
    }

    /**
     * @jmx.managed-attribute
     */
    public String getName() {
        return "Firebird Database manager";
    }

    /**
     * @jmx.managed-attribute
     */
    public void setServer(final String host) {
        this.host = host;
    }

    /**
     * @jmx.managed-attribute
     */
    public String getServer() {
        return host;
    }

    public String getType() {
        return this.type.toString();
    }

    /**
     * @jmx.managed-attribute
     */
    public void setPort(int port) {
        this.port = new Integer(port);
    }

    /**
     * @jmx.managed-attribute
     */
    public int getPort() {
        return port != null ? port.intValue() : DEFAULT_PORT;
    }

    /**
     * mbean get-set pair for field fileName
     * Get the value of fileName
     * @return value of fileName
     *
     * @jmx:managed-attribute
     */
    public String getFileName() {
        return fileName;
    }

    public void setType(String type) {
        final GDSType gdsType = GDSType.getType(type);
        if (gdsType == null) throw new RuntimeException("Unrecognized type '" + type + "'");
        this.type = gdsType;
    }

    /**
     * Set the value of fileName
     * @param fileName  Value to assign to fileName
     *
     * @jmx:managed-attribute
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * mbean get-set pair for field userName
     * Get the value of userName
     * @return value of userName
     *
     * @jmx:managed-attribute
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the value of userName
     * @param userName  Value to assign to userName
     *
     * @jmx:managed-attribute
     */
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * mbean get-set pair for field password
     * Get the value of password
     * @return value of password
     *
     * @jmx:managed-attribute
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the value of password
     * @param password  Value to assign to password
     *
     * @jmx:managed-attribute
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * mbean get-set pair for field createOnStart
     * Get the value of createOnStart
     * @return value of createOnStart
     *
     * @jmx:managed-attribute
     */
    public boolean isCreateOnStart() {
        return createOnStart;
    }

    /**
     * Set the value of createOnStart
     * @param createOnStart  Value to assign to createOnStart
     *
     * @jmx:managed-attribute
     */
    public void setCreateOnStart(final boolean createOnStart) {
        this.createOnStart = createOnStart;
    }

    /**
     * mbean get-set pair for field dropOnStop
     * Get the value of dropOnStop
     * @return value of dropOnStop
     *
     * @jmx:managed-attribute
     */
    public boolean isDropOnStop() {
        return dropOnStop;
    }

    /**
     * Set the value of dropOnStop
     * @param dropOnStop  Value to assign to dropOnStop
     *
     * @jmx:managed-attribute
     */
    public void setDropOnStop(final boolean dropOnStop) {
        this.dropOnStop = dropOnStop;
    }

    /**
     * Get the ForceCreate value.
     * @return the ForceCreate value.
     *
     * @jmx:managed-attribute
     */
    public boolean isForceCreate() {
        return forceCreate;
    }

    /**
     * Set the ForceCreate value.
     * @param forceCreate The new ForceCreate value.
     *
     * @jmx:managed-attribute
     */
    public void setForceCreate(boolean forceCreate) {
        this.forceCreate = forceCreate;
    }

    /**
     * @jmx.managed-operation
     */
    public void createDatabase(String fileName, String user, String password) throws Exception {
        IscDbHandle db = null;
        db = gds.createIscDbHandle();
        try {
            DatabaseParameterBuffer dpb = c.deepCopy();
            dpb.addArgument(DatabaseParameterBuffer.USER_NAME, user);
            dpb.addArgument(DatabaseParameterBuffer.PASSWORD, password);
            gds.iscAttachDatabase(getConnectString(fileName), db, dpb);
            if (forceCreate) gds.iscDropDatabase(db); else {
                gds.iscDetachDatabase(db);
                return;
            }
        } catch (GDSException e) {
        }
        db = gds.createIscDbHandle();
        try {
            DatabaseParameterBuffer dpb = c.deepCopy();
            dpb.addArgument(DatabaseParameterBuffer.USER_NAME, user);
            dpb.addArgument(DatabaseParameterBuffer.PASSWORD, password);
            gds.iscCreateDatabase(getConnectString(fileName), db, dpb);
            gds.iscDetachDatabase(db);
        } catch (Exception e) {
            if (log != null) {
                log.error("Exception creating database", e);
            }
            throw e;
        }
    }

    /**
     * @jmx.managed-operation
     */
    public void dropDatabase(String fileName, String user, String password) throws Exception {
        try {
            IscDbHandle db = gds.createIscDbHandle();
            DatabaseParameterBuffer dpb = c.deepCopy();
            dpb.addArgument(DatabaseParameterBuffer.USER_NAME, user);
            dpb.addArgument(DatabaseParameterBuffer.PASSWORD, password);
            gds.iscAttachDatabase(getConnectString(fileName), db, dpb);
            gds.iscDropDatabase(db);
        } catch (Exception e) {
            if (log != null) {
                log.error("Exception dropping database", e);
            }
            throw e;
        }
    }

    public boolean isDatabaseExists(String fileName, String user, String password) throws Exception {
        IscDbHandle db = null;
        db = gds.createIscDbHandle();
        try {
            DatabaseParameterBuffer dpb = c.deepCopy();
            dpb.addArgument(DatabaseParameterBuffer.USER_NAME, user);
            dpb.addArgument(DatabaseParameterBuffer.PASSWORD, password);
            gds.iscAttachDatabase(getConnectString(fileName), db, dpb);
            gds.iscDetachDatabase(db);
            return true;
        } catch (GDSException e) {
            return false;
        }
    }

    private String getConnectString(String filename) throws GDSException {
        return GDSFactory.getDatabasePath(type, host, port, filename);
    }
}
