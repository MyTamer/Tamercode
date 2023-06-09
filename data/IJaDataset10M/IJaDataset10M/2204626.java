package android.database.sqlite;

import android.util.Config;
import android.util.Log;

/**
 * Provides debugging info about all SQLite databases running in the current process.
 * 
 * {@hide}
 */
public final class SQLiteDebug {

    /**
     * Controls the printing of SQL statements as they are executed.
     */
    public static final boolean DEBUG_SQL_STATEMENTS = Log.isLoggable("SQLiteStatements", Log.VERBOSE);

    /**
     * Controls the stack trace reporting of active cursors being
     * finalized.
     */
    public static final boolean DEBUG_ACTIVE_CURSOR_FINALIZATION = Log.isLoggable("SQLiteCursorClosing", Log.VERBOSE);

    /**
     * Controls the tracking of time spent holding the database lock. 
     */
    public static final boolean DEBUG_LOCK_TIME_TRACKING = Log.isLoggable("SQLiteLockTime", Log.VERBOSE);

    /**
     * Controls the printing of stack traces when tracking the time spent holding the database lock. 
     */
    public static final boolean DEBUG_LOCK_TIME_TRACKING_STACK_TRACE = Log.isLoggable("SQLiteLockStackTrace", Log.VERBOSE);

    /**
     * Contains statistics about the active pagers in the current process.
     * 
     * @see #getPagerStats(PagerStats)
     */
    public static class PagerStats {

        /** The total number of bytes in all pagers in the current process */
        public long totalBytes;

        /** The number of bytes in referenced pages in all pagers in the current process */
        public long referencedBytes;

        /** The number of bytes in all database files opened in the current process */
        public long databaseBytes;

        /** The number of pagers opened in the current process */
        public int numPagers;
    }

    /**
     * Gathers statistics about all pagers in the current process.
     */
    public static native void getPagerStats(PagerStats stats);

    /**
     * Returns the size of the SQLite heap.
     * @return The size of the SQLite heap in bytes.
     */
    public static native long getHeapSize();

    /**
     * Returns the amount of allocated memory in the SQLite heap.
     * @return The allocated size in bytes.
     */
    public static native long getHeapAllocatedSize();

    /**
     * Returns the amount of free memory in the SQLite heap.
     * @return The freed size in bytes.
     */
    public static native long getHeapFreeSize();

    /**
     * Determines the number of dirty belonging to the SQLite
     * heap segments of this process.  pages[0] returns the number of
     * shared pages, pages[1] returns the number of private pages
     */
    public static native void getHeapDirtyPages(int[] pages);

    private static int sNumActiveCursorsFinalized = 0;

    /**
     * Returns the number of active cursors that have been finalized. This depends on the GC having
     * run but is still useful for tests.
     */
    public static int getNumActiveCursorsFinalized() {
        return sNumActiveCursorsFinalized;
    }

    static synchronized void notifyActiveCursorFinalized() {
        sNumActiveCursorsFinalized++;
    }
}
