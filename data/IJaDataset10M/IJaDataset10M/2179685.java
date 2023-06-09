package net.sourceforge.squirrel_sql.fw.sql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import net.sourceforge.squirrel_sql.fw.id.IIdentifier;
import net.sourceforge.squirrel_sql.fw.persist.ValidationException;
import net.sourceforge.squirrel_sql.fw.util.DuplicateObjectException;
import net.sourceforge.squirrel_sql.fw.util.IMessageHandler;
import net.sourceforge.squirrel_sql.fw.util.IObjectCacheChangeListener;
import net.sourceforge.squirrel_sql.fw.util.NullMessageHandler;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;
import net.sourceforge.squirrel_sql.fw.xml.XMLException;
import net.sourceforge.squirrel_sql.fw.xml.XMLObjectCache;

/**
 * XML cache of JDBC drivers and aliases.
 *
 * @author <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class DataCache {

    /** Internationalized strings for this class. */
    private static final StringManager s_stringMgr = StringManagerFactory.getStringManager(DataCache.class);

    /** Class for objects that define aliases to JDBC data sources. */
    private static final Class SQL_ALIAS_IMPL = SQLAlias.class;

    /** Class for objects that define JDBC drivers. */
    private static final Class SQL_DRIVER_IMPL = SQLDriver.class;

    /** Logger for this class. */
    private static final ILogger s_log = LoggerController.createLogger(DataCache.class);

    /** Driver manager. */
    private final SQLDriverManager _driverMgr;

    /** Cache that contains data. */
    private final XMLObjectCache _cache = new XMLObjectCache();

    /**
	 * Ctor. Loads drivers and aliases from the XML document.
	 *
	 * @param	driverMgr		Manages JDBC drivers.
	 * @param	driversFile		<TT>File</TT> to load drivers from.
	 * @param	aliasesFile		<TT>File</TT> to load aliases from.
	 * @param	dftDriversURL	URL that the default rivers can be loaded from.
	 * @param	msgHandler		Message handler to report on errors in this object.
	 *
	 * @throws	IllegalArgumentException
	 *			Thrown if null <TT>SQLDriverManager</TT>, <TT>driversFile</TT>,
	 *			<TT>aliasesFile</TT> or <TT>dftDriversURL</TT> passed.
	 */
    public DataCache(SQLDriverManager driverMgr, File driversFile, File aliasesFile, URL dftDriversURL, IMessageHandler msgHandler) {
        super();
        if (driverMgr == null) {
            throw new IllegalArgumentException("SQLDriverManager == null");
        }
        if (driversFile == null) {
            throw new IllegalArgumentException("driversFile == null");
        }
        if (aliasesFile == null) {
            throw new IllegalArgumentException("aliasesFile == null");
        }
        if (dftDriversURL == null) {
            throw new IllegalArgumentException("dftDriversURL == null");
        }
        _driverMgr = driverMgr;
        IMessageHandler myMsgHandler = msgHandler;
        if (myMsgHandler == null) {
            myMsgHandler = NullMessageHandler.getInstance();
        }
        loadDrivers(driversFile, dftDriversURL, myMsgHandler);
        loadAliases(aliasesFile, myMsgHandler);
    }

    /**
	 * Save JDBC drivers to the passed file as XML.
	 *
	 * @param	file	File to save drivers to.
	 *
	 * @throws	IllegalArgumentException
	 * 			Thrown if <TT>null</TT> <TT>File</TT> passed.
	 * @throws	IOException
	 * 			Thrown if an I/O error occurs saving.
	 * @throws	XMLException
	 * 			Thrown if an error occurs translating drivers to XML.
	 */
    public void saveDrivers(File file) throws IOException, XMLException {
        if (file == null) {
            throw new IllegalArgumentException("File == null");
        }
        _cache.saveAllForClass(file.getPath(), SQL_DRIVER_IMPL);
    }

    /**
	 * Save aliases to the passed file as XML.
	 *
	 * @param	file	File to save aliases to.
	 *
	 * @throws	IllegalArgumentException
	 * 			Thrown if <TT>null</TT> <TT>File</TT> passed.
	 * @throws	IOException
	 * 			Thrown if an I/O error occurs saving.
	 * @throws	XMLException
	 * 			Thrown if an error occurs translating aliases to XML.
	 */
    public void saveAliases(File file) throws IOException, XMLException {
        if (file == null) {
            throw new IllegalArgumentException("File == null");
        }
        _cache.saveAllForClass(file.getPath(), SQL_ALIAS_IMPL);
    }

    /**
	 * Retrieve the <TT>ISQLDriver</TT> for the passed identifier.
	 *
	 * @param	id	Identifier to retrieve driver for.
	 *
	 * @return	the <TT>ISQLDriver</TT> for the passed identifier.
	 *
	 * @throws	IllegalArgumentException
	 * 			Thrown if <TT>null</TT> <TT>ISQLDriver</TT> passed.
	 */
    public ISQLDriver getDriver(IIdentifier id) {
        if (id == null) {
            throw new IllegalArgumentException("ISQLDriver == null");
        }
        return (ISQLDriver) _cache.get(SQL_DRIVER_IMPL, id);
    }

    /**
	 * Add a driver to the cache.
	 *
	 * @param	sqlDriver	The driver to add.
	 *
	 * @throws	IllegalArgumentException
	 * 			Thrown if <TT>ISQLDriver</TT> is null.
	 */
    public void addDriver(ISQLDriver sqlDriver) throws ClassNotFoundException, IllegalAccessException, InstantiationException, DuplicateObjectException, MalformedURLException {
        if (sqlDriver == null) {
            throw new IllegalArgumentException("ISQLDriver == null");
        }
        _driverMgr.registerSQLDriver(sqlDriver);
        _cache.add(sqlDriver);
    }

    public void removeDriver(ISQLDriver sqlDriver) {
        _cache.remove(SQL_DRIVER_IMPL, sqlDriver.getIdentifier());
        _driverMgr.unregisterSQLDriver(sqlDriver);
    }

    public Iterator drivers() {
        return _cache.getAllForClass(SQL_DRIVER_IMPL);
    }

    public void addDriversListener(IObjectCacheChangeListener lis) {
        _cache.addChangesListener(lis, SQL_DRIVER_IMPL);
    }

    public void removeDriversListener(IObjectCacheChangeListener lis) {
        _cache.removeChangesListener(lis, SQL_DRIVER_IMPL);
    }

    public ISQLAlias getAlias(IIdentifier id) {
        return (ISQLAlias) _cache.get(SQL_ALIAS_IMPL, id);
    }

    public Iterator aliases() {
        return _cache.getAllForClass(SQL_ALIAS_IMPL);
    }

    public void addAlias(ISQLAlias alias) throws DuplicateObjectException {
        _cache.add(alias);
    }

    public void removeAlias(ISQLAlias alias) {
        _cache.remove(SQL_ALIAS_IMPL, alias.getIdentifier());
    }

    public Iterator getAliasesForDriver(ISQLDriver driver) {
        ArrayList data = new ArrayList();
        for (Iterator it = aliases(); it.hasNext(); ) {
            ISQLAlias alias = (ISQLAlias) it.next();
            if (driver.equals(getDriver(alias.getDriverIdentifier()))) {
                data.add(alias);
            }
        }
        return data.iterator();
    }

    public void addAliasesListener(IObjectCacheChangeListener lis) {
        _cache.addChangesListener(lis, SQL_ALIAS_IMPL);
    }

    public void removeAliasesListener(IObjectCacheChangeListener lis) {
        _cache.removeChangesListener(lis, SQL_ALIAS_IMPL);
    }

    /**
	 * Load <TT>IISqlDriver</TT> objects from the XML file <TT>driversFile</TT>.
	 * If file not found then load from the default drivers.
	 *
	 * @param	driversFile		<TT>File</TT> to load drivers from.
	 * @param	dftDriversURL	<TT>URL</TT> to load default drivers from.
	 * @param	msgHandler		Message handler to write any errors to.
	 *
	 *@throws	IllegalArgumentException
	 *			Thrown if <TT>null</TT> <TT>driversFile</TT>,
	 *			<TT>dftDriversURL</TT>, or <TT>msgHandler</TT> passed.
	 */
    private void loadDrivers(File driversFile, URL dftDriversURL, IMessageHandler msgHandler) {
        if (driversFile == null) {
            throw new IllegalArgumentException("driversFile == null");
        }
        if (dftDriversURL == null) {
            throw new IllegalArgumentException("dftDriversURL == null");
        }
        if (msgHandler == null) {
            throw new IllegalArgumentException("msgHandler == null");
        }
        try {
            try {
                _cache.load(driversFile.getPath());
                if (!drivers().hasNext()) {
                    loadDefaultDrivers(dftDriversURL);
                } else {
                    fixupDrivers();
                }
            } catch (FileNotFoundException ex) {
                loadDefaultDrivers(dftDriversURL);
            } catch (Exception ex) {
                String msg = s_stringMgr.getString("DataCache.error.loadingdrivers", driversFile.getPath());
                s_log.error(msg, ex);
                msgHandler.showErrorMessage(msg);
                msgHandler.showErrorMessage(ex);
                loadDefaultDrivers(dftDriversURL);
            }
        } catch (XMLException ex) {
            s_log.error("Error loading drivers", ex);
        } catch (IOException ex) {
            s_log.error("Error loading drivers", ex);
        }
        registerDrivers(msgHandler);
    }

    public ISQLAlias createAlias(IIdentifier id) {
        return new SQLAlias(id);
    }

    public ISQLDriver createDriver(IIdentifier id) {
        return new SQLDriver(id);
    }

    public void loadDefaultDrivers(URL url) throws IOException, XMLException {
        InputStreamReader isr = new InputStreamReader(url.openStream());
        try {
            _cache.load(isr, null, true);
        } catch (DuplicateObjectException ex) {
            s_log.error("Received an unexpected DuplicateObjectException", ex);
        } finally {
            isr.close();
        }
    }

    private void registerDrivers(IMessageHandler msgHandler) {
        SQLDriverManager driverMgr = _driverMgr;
        for (Iterator it = drivers(); it.hasNext(); ) {
            ISQLDriver sqlDriver = (ISQLDriver) it.next();
            try {
                driverMgr.registerSQLDriver(sqlDriver);
            } catch (ClassNotFoundException ignore) {
            } catch (Throwable th) {
                String msg = s_stringMgr.getString("DataCache.error.registerdriver", sqlDriver.getName());
                s_log.error(msg, th);
                msgHandler.showErrorMessage(msg);
                msgHandler.showErrorMessage(th);
            }
        }
    }

    private void loadAliases(File aliasesFile, IMessageHandler msgHandler) {
        try {
            _cache.load(aliasesFile.getPath());
        } catch (FileNotFoundException ignore) {
        } catch (Exception ex) {
            String msg = s_stringMgr.getString("DataCache.error.loadingaliases", aliasesFile.getPath());
            s_log.error(msg, ex);
            msgHandler.showErrorMessage(msg);
            msgHandler.showErrorMessage(ex);
        }
    }

    /**
	 * In 1.1beta? the jar file for a driver was changed from only one allowed
	 * to multiple ones allowed. This method changes the driver from the old
	 * version to the new one to allow for loading old versions of the
	 * SQLDrivers.xml file.
	 */
    private void fixupDrivers() {
        for (Iterator it = drivers(); it.hasNext(); ) {
            ISQLDriver driver = (ISQLDriver) it.next();
            String[] fileNames = driver.getJarFileNames();
            if (fileNames == null || fileNames.length == 0) {
                String fileName = driver.getJarFileName();
                if (fileName != null && fileName.length() > 0) {
                    driver.setJarFileNames(new String[] { fileName });
                    try {
                        driver.setJarFileName(null);
                    } catch (ValidationException ignore) {
                    }
                }
            }
        }
    }
}
