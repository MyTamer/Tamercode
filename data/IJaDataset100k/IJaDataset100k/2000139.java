package org.firebirdsql.management;

import java.sql.SQLException;
import org.firebirdsql.gds.ISCConstants;

/**
 * A <code>MaintenanceManager</code> is responsible for replicating
 * the functionality provided by the <code>gfix</code> command-line tool.
 * Among the responsibilities of this class are:
 * <ul>
 *      <li>Database shutdown
 *      <li>Changing database mode to read-only or read-write
 *      <li>Enabling or disabling forced writes in the database
 *      <li>Changing the dialect of the database
 *      <li>Setting the cache size at database-level
 *      <li>Mending databases and making minor repairs
 *      <li>Sweeping databases
 *      <li>Displaying, committing, or recovering limbo transactions
 *      <li>Activating and killing shadow files
 *      <li>Configuring page fill
 * </ul>
 *
 * @author <a href="mailto:gab_reid@users.sourceforge.net">Gabriel Reid</a>
 */
public interface MaintenanceManager extends ServiceManager {

    /** 
     * Database read-write mode
     */
    public static final int ACCESS_MODE_READ_WRITE = ISCConstants.isc_spb_prp_am_readwrite;

    /**
     * Database read-only mode
     */
    public static final int ACCESS_MODE_READ_ONLY = ISCConstants.isc_spb_prp_am_readonly;

    /**
     * Don't allow new connections while waiting to shut down.
     */
    public static final int SHUTDOWN_ATTACH = ISCConstants.isc_spb_prp_deny_new_attachments;

    /**
     * Don't allow new transactions while waiting to shut down.
     */
    public static final int SHUTDOWN_TRANSACTIONAL = ISCConstants.isc_spb_prp_deny_new_transactions;

    /**
     * Forced database shutdown.
     */
    public static final int SHUTDOWN_FORCE = ISCConstants.isc_spb_prp_shutdown_db;

    /**
     * Only report corrupt or misallocated structures, don't fix.
     */
    public static final int VALIDATE_READ_ONLY = ISCConstants.isc_spb_rpr_check_db;

    /**
     * Ignore checksums during repair operations.
     */
    public static final int VALIDATE_IGNORE_CHECKSUM = ISCConstants.isc_spb_rpr_ignore_checksum;

    /**
     * Do a full check on record and pages structures, releasing unassigned
     * record fragments.
     */
    public static final int VALIDATE_FULL = ISCConstants.isc_spb_rpr_full;

    /**
     * Fully fill pages when inserting records.
     */
    public static final int PAGE_FILL_FULL = ISCConstants.isc_spb_prp_res_use_full;

    /**
     * While inserting recrods, reserve 20% of each page for later
     * record deltas
     */
    public static final int PAGE_FILL_RESERVE = ISCConstants.isc_spb_prp_res;

    /**
     * Set the database to have read-write or read-only access.
     *
     * @param mode Must be either <code>ACCESS_MODE_READ_WRITE</code> 
     *        or <code>ACCESS_MODE_READ_ONLY</code>
     * @throws SQLException if a database access error occurs
     */
    public void setDatabaseAccessMode(int mode) throws SQLException;

    /**
     * Set the database's dialect.
     *
     * @param dialect The database dialect, must be either 1 or 3
     * @throws SQLException if a database access error occurs 
     */
    public void setDatabaseDialect(int dialect) throws SQLException;

    /**
     * Set the default page-buffer count to be cached in the database. The
     * Firebird default is 2048.
     *
     * @param pageCount The number of pages to be cached, must be a positive 
     * @throws SQLException If the given page count cannot be set, or a
     *         database access error occurs
     */
    public void setDefaultCacheBuffer(int pageCount) throws SQLException;

    /**
     * Enable or disable forced (synchronous) writes in the database.
     * Note, it is considered to be a <b>very</b> bad idea to use buffered 
     * writing on Windows platforms.
     *
     * @param forced If <code>true</code>, forced writes will be used in the
     *        database, otherwise buffered writes will be used.
     * @throws SQLException if a database access error occurs
     */
    public void setForcedWrites(boolean forced) throws SQLException;

    /**
     * Set the page fill strategy for when inserting records.
     * <code>pageFill</code> can be one of:
     * <ul>
     *      <li><code>PAGE_FILL_FULL</code> Fully fill database pages
     *      <li><code>PAGE_FILL_RESERVE</code> Reserve 20% of page space for
     *      later record deltas
     * </ul>
     *
     * @param pageFill The page-filling strategy, either 
     *        <code>PAGE_FILL_FULL</code> or <code>PAGE_FILL_RESERVE</code>
     * @throws SQLException if a database access error occurs
     */
    public void setPageFill(int pageFill) throws Exception;

    /**
     * Shutdown the current database.
     * Shutdown can be done in three modes:
     * <ul>
     *      <li><code>SHUTDOWN_ATTACH</code> - No new non-owner connections 
     *      will be allowed to the database during the shutdown, and shutdown
     *      is cancelled if there are still processes connected at the end
     *      of the timeout.
     *
     *      <li><code>SHUTDOWN_TRANSACTIONAL</code> - No new transactions can 
     *      be started during the timeout period, and shutdown is cancelled
     *      if there are still active transactions at the end of the timeout.
     *
     *      <li><code>SHUTDOWN_FORCE</code> - Forcefully shuts down the 
     *      database at the end of the timeout.
     * </ul>
     *
     * @param shutdownMode One of <code>SHUTDOWN_ATTACH</code>, 
     *        <code>SHUTDOWN_TRANSACTIONAL</code>, 
     *        or <code>SHUTDOWN_FORCE</code>.
     * @param timeout The maximum amount of time allocated for the operation,
     *        in seconds
     * @throws SQLException if the requested operation cannot be completed 
     *         within the given timeout, or a database access error occurs
     */
    public void shutdownDatabase(int shutdownMode, int timeout) throws SQLException;

    /**
     * Bring a shutdown database online.
     *
     * @throws SQLException if a database access error occurs
     */
    public void bringDatabaseOnline() throws SQLException;

    /**
     * Mark corrupt records in the database as unavailable.
     * This operation ensures that the corrupt records are skipped (for 
     * example, during a subsequent backup).
     *
     * @throws SQLException if a database access error occurs
     */
    public void markCorruptRecords() throws SQLException;

    /**
     * Locate and release database pages that are allocated but unassigned
     * to any data structures. This method also reports corrupt structures.
     *
     * @throws SQLException if a database access error occurs
     */
    public void validateDatabase() throws SQLException;

    /**
     * Locate and release database pages that are allocated but unassigned
     * to any data structures. This method also reports corrupt structures.
     * The value supplied for <code>options</code> must be one of the
     * following:
     * <ul>
     *      <li>0 - Simple validation
     *      <li><code>VALIDATE_READ_ONLY</code> - read-only validation, 
     *      no repair
     *      <li><code>VALIDATE_FULL</code> - full validation and repair
     * </ul>
     *
     * The value for <code>options</code> can additionally be combined in
     * a bitmask with <code>VALIDATE_IGNORE_CHECKSUM</code> to ignore
     * checksums while performing validation.
     *
     * @param options Either 0, <code>VALIDATE_READ_ONLY</code>, or
     *        <code>VALIDATE_FULL</code>
     * @throws SQLException if a database access error occurs
     */
    public void validateDatabase(int options) throws SQLException;

    /**
     * Set the database automatic sweep interval to a given number of 
     * transactions. The Firebird default value is 20,000. If 
     * <code>transactions</code> is 0, automatic sweeping is disabled.
     *
     * @param transactions The interval of transactions between automatic
     *        sweeps of the database. Can be set to 0, which disables
     *        automatic sweeping of the database.
     * @throws SQLException if a database access error occurs
     */
    public void setSweepThreshold(int transactions) throws SQLException;

    /**
     * Perform an immediate sweep of the database.
     *
     * @throws SQLException if a database access error occurs
     */
    public void sweepDatabase() throws SQLException;

    /**
     * Activate a database shadow file to be used as the actual database.
     * This method is the equivalent of <b><code>gfix -activate</code></b>.
     *
     * @throws SQLException if a database access error occurs
     */
    public void activateShadowFile() throws SQLException;

    /**
     * Remove references to unavailable shadow files. This method is the
     * equivalent of <b><code>gfix -kill</code></b>.
     *
     * @throws SQLException if a database access error occurs
     */
    public void killUnavailableShadows() throws SQLException;

    /**
     * Retrieve the ID of each limbo transaction. The output of this  method 
     * is written to the logger.
     *
     * @throws SQLException if a database access error occurs
     */
    public void listLimboTransactions() throws SQLException;

    /**
     * Commit a limbo transaction based on its ID.
     *
     * @param transactionId The ID of the limbo transaction to be committed
     * @throws SQLException if a database access error occurs or the 
     *         given transaction ID is not valid
     */
    public void commitTransaction(int transactionId) throws SQLException;

    /**
     * Rollback a limbo transaction based on its ID.
     *
     * @param transactionId The ID of the limbo transaction to be rolled back
     * @throws SQLException if a database access error occurs or the
     *         given transaction ID is not valid
     */
    public void rollbackTransaction(int transactionId) throws SQLException;
}
