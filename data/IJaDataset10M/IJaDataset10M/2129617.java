package org.springframework.ejb.access;

import javax.naming.NamingException;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>Convenient factory for remote SLSB proxies.
 * If you want control over interceptor chaining, use an AOP ProxyFactoryBean
 * with SimpleRemoteSlsbInvokerInterceptor rather than rely on this class.</p>
 * 
 * <p>See {@link org.springframework.jndi.JndiObjectLocator} for info on
 * how to specify the JNDI location of the target EJB.</p>
 * 
 * <p>In a bean container, this class is normally best used as a singleton. However,
 * if that bean container pre-instantiates singletons (as do the XML ApplicationContext
 * variants) you may have a problem if the bean container is loaded before the EJB
 * container loads the target EJB. That is because by default the JNDI lookup will be
 * performed in the init method of this class and cached, but the EJB will not have been
 * bound at the target location yet. The best solution is to set the lookupHomeOnStartup
 * property to false, in which case the home will be fetched on first access to the EJB.
 * (This flag is only true by default for backwards compatibility reasons).</p>
 * 
 * <p>This proxy factory is typically used with an RMI business interface, which serves
 * as super-interface of the EJB component interface. Alternatively, this factory
 * can also proxy a remote SLSB with a matching non-RMI business interface, i.e. an
 * interface that mirrors the EJB business methods but does not declare RemoteExceptions.
 * In the latter case, RemoteExceptions thrown by the EJB stub will automatically get
 * converted to Spring's unchecked RemoteAccessException.</p>
 *
 * @author Rod Johnson
 * @author Colin Sampaleanu
 * @author Juergen Hoeller
 * @since 09.05.2003
 * @see org.springframework.remoting.RemoteAccessException
 * @see AbstractSlsbInvokerInterceptor#setLookupHomeOnStartup
 * @see AbstractSlsbInvokerInterceptor#setCacheHome
 * @see AbstractRemoteSlsbInvokerInterceptor#setRefreshHomeOnConnectFailure
 */
public class SimpleRemoteStatelessSessionProxyFactoryBean extends SimpleRemoteSlsbInvokerInterceptor implements FactoryBean {

    /** The business interface of the EJB we're proxying */
    private Class businessInterface;

    /** EJBObject */
    private Object proxy;

    /**
	 * Set the business interface of the EJB we're proxying.
	 * This will normally be a super-interface of the EJB remote component interface.
	 * Using a business methods interface is a best practice when implementing EJBs.
	 * <p>You can also specify a matching non-RMI business interface, i.e. an interface
	 * that mirrors the EJB business methods but does not declare RemoteExceptions.
	 * In this case, RemoteExceptions thrown by the EJB stub will automatically get
	 * converted to Spring's generic RemoteAccessException.
	 * @param businessInterface the business interface of the EJB
	 */
    public void setBusinessInterface(Class businessInterface) {
        this.businessInterface = businessInterface;
    }

    /**
	 * Return the business interface of the EJB we're proxying.
	 */
    public Class getBusinessInterface() {
        return businessInterface;
    }

    public void afterPropertiesSet() throws NamingException {
        super.afterPropertiesSet();
        if (this.businessInterface == null) {
            throw new IllegalArgumentException("businessInterface is required");
        }
        this.proxy = ProxyFactory.getProxy(this.businessInterface, this);
    }

    public Object getObject() {
        return this.proxy;
    }

    public Class getObjectType() {
        return (this.proxy != null) ? this.proxy.getClass() : this.businessInterface;
    }

    public boolean isSingleton() {
        return true;
    }
}
