package org.shalma.service.internal;

import org.osgi.framework.ServiceReference;

/**
 * The <code>ServiceTrackerCustomizer</code> interface allows a
 * <code>ServiceTracker</code> object to customize the service objects that
 * are tracked. The <code>ServiceTrackerCustomizer</code> object is called
 * when a service is being added to the <code>ServiceTracker</code> object.
 * The <code>ServiceTrackerCustomizer</code> can then return an object for the
 * tracked service. The <code>ServiceTrackerCustomizer</code> object is also
 * called when a tracked service is modified or has been removed from the
 * <code>ServiceTracker</code> object.
 * 
 * <p>
 * The methods in this interface may be called as the result of a
 * <code>ServiceEvent</code> being received by a <code>ServiceTracker</code>
 * object. Since <code>ServiceEvent</code> s are synchronously delivered by
 * the Framework, it is highly recommended that implementations of these methods
 * do not register (<code>BundleContext.registerService</code>), modify (
 * <code>ServiceRegistration.setProperties</code>) or unregister (
 * <code>ServiceRegistration.unregister</code>) a service while being
 * synchronized on any object.
 * 
 * @version $Revision: 1.8 $
 */
public interface ServiceTrackerCustomizer {

    /**
	 * A service is being added to the <code>ServiceTracker</code> object.
	 * 
	 * <p>
	 * This method is called before a service which matched the search
	 * parameters of the <code>ServiceTracker</code> object is added to it.
	 * This method should return the service object to be tracked for this
	 * <code>ServiceReference</code> object. The returned service object is
	 * stored in the <code>ServiceTracker</code> object and is available from
	 * the <code>getService</code> and <code>getServices</code> methods.
	 * 
	 * @param reference
	 *            Reference to service being added to the
	 *            <code>ServiceTracker</code> object.
	 * @return The service object to be tracked for the
	 *         <code>ServiceReference</code> object or <code>null</code> if
	 *         the <code>ServiceReference</code> object should not be tracked.
	 */
    public Object addingService(ServiceReference reference);

    /**
	 * A service tracked by the <code>ServiceTracker</code> object has been
	 * modified.
	 * 
	 * <p>
	 * This method is called when a service being tracked by the
	 * <code>ServiceTracker</code> object has had it properties modified.
	 * 
	 * @param reference
	 *            Reference to service that has been modified.
	 * @param service
	 *            The service object for the modified service.
	 */
    public void modifiedService(ServiceReference reference, Object service);

    /**
	 * A service tracked by the <code>ServiceTracker</code> object has been
	 * removed.
	 * 
	 * <p>
	 * This method is called after a service is no longer being tracked by the
	 * <code>ServiceTracker</code> object.
	 * 
	 * @param reference
	 *            Reference to service that has been removed.
	 * @param service
	 *            The service object for the removed service.
	 */
    public void removedService(ServiceReference reference, Object service);
}
