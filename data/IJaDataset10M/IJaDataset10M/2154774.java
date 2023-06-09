package org.apache.harmony.rmi.server;

import java.util.Hashtable;
import java.rmi.server.ObjID;

/**
 * Defines structure for storing RMIObjects and methods for finding them by
 * different separate keys, which are unique inside the table: ObjID and
 * RMIReference to the object's implementation.
 *
 * @author  Mikhail A. Markov
 */
final class RMIObjectTable {

    private Hashtable idTable = new Hashtable();

    private Hashtable refTable = new Hashtable();

    private class TablesLock {
    }

    private Object tablesLock = new TablesLock();

    /**
     * Adds specified info to the table if there are no elements with the
     * same ObjID or RMIReference to the impl there.
     *
     * @param info RMIObjectInfo to be added to the table
     *
     * @return true if the table did not contain the given info and the info
     *         was successfully added to the table and false otherwise
     *
     * @throws NullPointerException if info is null or info.id is null
     */
    public boolean add(RMIObjectInfo info) {
        synchronized (tablesLock) {
            if (!idTable.containsKey(info.id)) {
                idTable.put(info.id, info);
                refTable.put(info.ref, info);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the table contains element with specified RMIReference
     * and false otherwise.
     *
     * @param ref RMIReference to the remote object implementation to be used
     *        as a key
     *
     * @return true if the table contains element with specified ref and false
     *         otherwise
     */
    public boolean containsByRef(RMIReference ref) {
        synchronized (tablesLock) {
            return refTable.containsKey(ref);
        }
    }

    /**
     * Returns true if the table contains element with specified id and false
     * otherwise.
     *
     * @param id ObjID to be used as a key
     *
     * @return true if the table contains element with specified id and false
     *         otherwise
     */
    public boolean containsById(ObjID id) {
        synchronized (tablesLock) {
            return idTable.containsKey(id);
        }
    }

    /**
     * Returns true if the table contains element which is equal to the info
     * specified.
     *
     * @param info RMIObjectInfo to be used as a key
     *
     * @return true if the table contains element which is equal to the info
     *         specified
     */
    public boolean contains(RMIObjectInfo info) {
        if (info != null && info.id != null) {
            synchronized (tablesLock) {
                return containsById(info.id);
            }
        }
        return false;
    }

    /**
     * Finds and returns RMIObjectInfo found by specified reference to remote
     * object implementation or null if record has not been found.
     *
     * @param ref RMIReference to the remote object implementation to be used
     *        as a key
     *
     * @return RMIObjectInfo if found in the table or null if matching record
     *         has not been found
     */
    public RMIObjectInfo getByRef(RMIReference ref) {
        synchronized (tablesLock) {
            return (RMIObjectInfo) refTable.get(ref);
        }
    }

    /**
     * Finds and returns RMIObjectInfo found by specified ObjID or null
     * if record has not been found.
     *
     * @param id ObjID to use as a key
     *
     * @return RMIObjectInfo if found in the table or null if matching record
     *         has not been found
     */
    public RMIObjectInfo getById(ObjID id) {
        synchronized (tablesLock) {
            return (RMIObjectInfo) idTable.get(id);
        }
    }

    /**
     * Removes RMIObjectInfo found by specified reference to remote object
     * implementation from the table.
     *
     * @param ref RMIReference to the remote object implementation to be used
     *        as a key
     *
     * @return RMIObjectInfo if found in the table or null if matching record
     *         has not been found
     */
    public RMIObjectInfo removeByRef(RMIReference ref) {
        RMIObjectInfo info;
        synchronized (tablesLock) {
            info = (RMIObjectInfo) refTable.remove(ref);
            if (info != null) {
                idTable.remove(info.id);
            }
        }
        return info;
    }

    /**
     * Removes RMIObjectInfo found by specified ObjID from the table.
     *
     * @param id ObjID to use as a key
     *
     * @return RMIObjectInfo if found in the table or null if matching record
     *         has not been found
     */
    public RMIObjectInfo removeById(ObjID id) {
        RMIObjectInfo info;
        synchronized (tablesLock) {
            info = (RMIObjectInfo) idTable.remove(id);
            if (info != null) {
                refTable.remove(info.ref);
            }
        }
        return info;
    }

    /**
     * Returns true if this table contains no records and false otherwise.
     *
     * @return true if this table contains no records and false otherwise
     */
    public boolean isEmpty() {
        synchronized (tablesLock) {
            return idTable.isEmpty();
        }
    }
}
