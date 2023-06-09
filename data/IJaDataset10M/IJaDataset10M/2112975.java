package org.osgi.service.component;

import java.util.Dictionary;

/**
 * When a component is declared with the <code>factory</code> attribute on its
 * <code>component</code> element, the Service Component Runtime will register
 * a Component Factory service to allow new component configurations to be
 * created and activated rather than automatically creating and activating
 * component configuration as necessary.
 * 
 * @version $Revision: 1.19 $
 */
public interface ComponentFactory {

    /**
	 * Create and activate a new component configuration. Additional properties
	 * may be provided for the component configuration.
	 * 
	 * @param properties Additional properties for the component configuration
	 *        or <code>null</code> if there are no additional properties.
	 * @return A <code>ComponentInstance</code> object encapsulating the
	 *         component instance of the component configuration. The component
	 *         configuration has been activated and, if the component specifies
	 *         a <code>service</code> element, the component instance has been
	 *         registered as a service.
	 * @throws ComponentException If the Service Component Runtime is unable to
	 *         activate the component configuration.
	 */
    public ComponentInstance newInstance(Dictionary properties);
}
