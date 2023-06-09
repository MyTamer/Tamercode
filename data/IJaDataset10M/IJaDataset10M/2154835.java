package org.apache.jetspeed.om.security.turbine;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import org.apache.torque.manager.AbstractBaseManager;
import org.apache.torque.manager.CacheListener;
import org.apache.torque.manager.MethodResultCache;
import org.apache.torque.om.ObjectKey;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.om.Persistent;
import org.apache.torque.util.Criteria;

/**
 * This class manages TurbineGroup objects.
 * This class was autogenerated by Torque on:
 *
 * [Thu Jun 10 23:17:32 JST 2004]
 *
  *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TurbineGroupManager
 */
public abstract class BaseTurbineGroupManager extends AbstractBaseManager {

    /** The name of the manager */
    protected static String MANAGED_CLASS = "org.apache.jetspeed.om.security.turbine.TurbineGroup";

    /** The name of our class to pass to Torque as the default manager. */
    protected static String DEFAULT_MANAGER_CLASS = "org.apache.jetspeed.om.security.turbine.TurbineGroupManager";

    /**
     * Retrieves an implementation of the manager, based on the settings in
     * the configuration.
     *
     * @return an implementation of TurbineGroupManager.
     */
    public static TurbineGroupManager getManager() {
        return (TurbineGroupManager) Torque.getManager(TurbineGroupManager.MANAGED_CLASS, TurbineGroupManager.DEFAULT_MANAGER_CLASS);
    }

    /**
     * Static accessor for the @see #getInstanceImpl().
     *
     * @return a <code>TurbineGroup</code> value
     * @exception TorqueException if an error occurs
     */
    public static TurbineGroup getInstance() throws TorqueException {
        return getManager().getInstanceImpl();
    }

    /**
     * Static accessor for the @see #getInstanceImpl(ObjectKey).
     *
     * @param id an <code>ObjectKey</code> value
     * @return a <code>TurbineGroup</code> value
     * @exception TorqueException if an error occurs
     */
    public static TurbineGroup getInstance(ObjectKey id) throws TorqueException {
        return getManager().getInstanceImpl(id);
    }

    /**
     * Static accessor for the @see #getInstanceImpl(ObjectKey, boolean).
     *
     * @param id an <code>ObjectKey</code> value
     * @return a <code>TurbineGroup</code> value
     * @exception TorqueException if an error occurs
     */
    public static TurbineGroup getInstance(ObjectKey id, boolean fromCache) throws TorqueException {
        return getManager().getInstanceImpl(id, fromCache);
    }

    /**
     * Static accessor for the @see #getInstanceImpl(ObjectKey).
     *
     * @param id an <code>ObjectKey</code> value
     * @return a <code>TurbineGroup</code> value
     * @exception TorqueException if an error occurs
     */
    public static TurbineGroup getInstance(int id) throws TorqueException {
        return getManager().getInstanceImpl(SimpleKey.keyFor(id));
    }

    /**
     * Static accessor for the @see #getInstanceImpl(ObjectKey).
     *
     * @param id an <code>ObjectKey</code> value
     * @return a <code>TurbineGroup</code> value
     * @exception TorqueException if an error occurs
     */
    public static TurbineGroup getInstance(int id, boolean fromCache) throws TorqueException {
        return getManager().getInstanceImpl(SimpleKey.keyFor(id), fromCache);
    }

    /**
     * Static accessor for the @see #getInstancesImpl(List).
     *
     * @param ids a <code>List</code> value
     * @return a <code>List</code> value
     * @exception TorqueException if an error occurs
     */
    public static List getInstances(List ids) throws TorqueException {
        return getManager().getInstancesImpl(ids);
    }

    /**
     * Static accessor for the @see #getInstancesImpl(List, boolean).
     *
     * @param ids a <code>List</code> value
     * @return a <code>List</code> value
     * @exception TorqueException if an error occurs
     */
    public static List getInstances(List ids, boolean fromCache) throws TorqueException {
        return getManager().getInstancesImpl(ids, fromCache);
    }

    public static void putInstance(Persistent om) throws TorqueException {
        getManager().putInstanceImpl(om);
    }

    public static void clear() throws TorqueException {
        getManager().clearImpl();
    }

    public static boolean exists(TurbineGroup obj) throws TorqueException {
        return getManager().existsImpl(obj);
    }

    public static MethodResultCache getMethodResult() {
        return getManager().getMethodResultCache();
    }

    public static void addCacheListener(CacheListener listener) {
        getManager().addCacheListenerImpl(listener);
    }

    /**
     * Creates a new <code>BaseTurbineGroupManager</code> instance.
     *
     * @exception TorqueException if an error occurs
     */
    public BaseTurbineGroupManager() throws TorqueException {
        setClassName("org.apache.jetspeed.om.security.turbine.TurbineGroup");
    }

    /**
     * Get a fresh instance of a TurbineGroupManager
     */
    protected TurbineGroup getInstanceImpl() throws TorqueException {
        TurbineGroup obj = null;
        try {
            obj = (TurbineGroup) getOMInstance();
        } catch (Exception e) {
            throw new TorqueException(e);
        }
        return obj;
    }

    /**
     * Get a TurbineGroup with the given id.
     *
     * @param id <code>ObjectKey</code> value
     */
    protected TurbineGroup getInstanceImpl(ObjectKey id) throws TorqueException {
        return (TurbineGroup) getOMInstance(id);
    }

    /**
     * Get a TurbineGroup with the given id.
     *
     * @param id <code>ObjectKey</code> value
     * @param fromCache if true, look for cached TurbineGroups before loading
     * from storage.
     */
    protected TurbineGroup getInstanceImpl(ObjectKey id, boolean fromCache) throws TorqueException {
        return (TurbineGroup) getOMInstance(id, fromCache);
    }

    /**
     * Gets a list of TurbineGroups based on id's.
     *
     * @param ids a List of <code>ObjectKeys</code> value
     * @return a <code>List</code> of TurbineGroups
     * @exception TorqueException if an error occurs
     */
    protected List getInstancesImpl(List ids) throws TorqueException {
        return getOMs(ids);
    }

    /**
     * Gets a list of TurbineGroups based on id's.
     *
     * @param ids a List of <code>ObjectKeys</code> value
     * @param fromCache if true, look for cached TurbineGroups before loading
     * from storage.
     * @return a <code>List</code> of TurbineGroups
     * @exception TorqueException if an error occurs
     */
    protected List getInstancesImpl(List ids, boolean fromCache) throws TorqueException {
        return getOMs(ids, fromCache);
    }

    /**
     * check for a duplicate project name
     */
    protected boolean existsImpl(TurbineGroup om) throws TorqueException {
        Criteria crit = TurbineGroupPeer.buildCriteria((TurbineGroup) om);
        return TurbineGroupPeer.doSelect(crit).size() > 0;
    }

    protected Persistent retrieveStoredOM(ObjectKey id) throws TorqueException {
        return TurbineGroupPeer.retrieveByPK(id);
    }

    /**
     * Gets a list of ModuleEntities based on id's.
     *
     * @param moduleIds a <code>NumberKey[]</code> value
     * @return a <code>List</code> value
     * @exception TorqueException if an error occurs
     */
    protected List retrieveStoredOMs(List ids) throws TorqueException {
        return TurbineGroupPeer.retrieveByPKs(ids);
    }
}
