package edu.harvard.fas.rregan.repository.jpa;

import java.util.Collection;
import java.util.Map;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import edu.harvard.fas.rregan.SystemInitializer;
import edu.harvard.fas.rregan.command.Command;

/**
 * AOP Advice that wraps domain objects in proxies for transactional activities
 * after being detached from the JPA session/transactions.
 * 
 * @author ron
 */
@Aspect
@Component("domainObjectWrappingAdvice")
@Scope("singleton")
public class DomainObjectWrappingAdvice {

    protected static final Logger log = Logger.getLogger(DomainObjectWrappingAdvice.class);

    private final DomainObjectWrapper domainObjectWrapper;

    /**
	 * @param domainObjectWrapper
	 */
    @Autowired
    public DomainObjectWrappingAdvice(DomainObjectWrapper domainObjectWrapper) {
        this.domainObjectWrapper = domainObjectWrapper;
    }

    /**
	 * After the execution any method on a repository or a getter on a command,
	 * wrap returned entities in a proxy so that references to properties always
	 * return the latest persisted state of the object from the database and
	 * load lazy objects as needed.<br>
	 * TODO: remove the logging and make this an after advice
	 * 
	 * @param pjp
	 * @return the return type of the repository method with entity objects
	 *         wrapped in a proxy.
	 * @throws Throwable
	 */
    @Around(value = "execution(* edu.harvard.fas.rregan.repository.jpa.AbstractJpaRepository+.*(..)) || execution(* edu.harvard.fas.rregan.command.Command+.get*(..))")
    public Object wrapReturnedObject(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; i++) {
            args[i] = EntityProxyInterceptor.unwrap(args[i]);
        }
        StringBuffer sb = null;
        if (log.isDebugEnabled()) {
            sb = new StringBuffer();
            sb.append(pjp.getTarget().getClass().getSimpleName());
            sb.append(".");
            sb.append(pjp.getSignature().getName());
            sb.append("(");
            for (int i = 0; i < args.length; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                if (args[i] instanceof Map) {
                    if (((Map) args[i]).size() > 10) {
                        sb.append("<big map>");
                    } else {
                        sb.append(args[i]);
                    }
                } else if (args[i] instanceof Collection) {
                    if (((Collection) args[i]).size() > 10) {
                        sb.append("<big collection>");
                    } else {
                        sb.append(args[i]);
                    }
                } else {
                    sb.append(args[i]);
                }
            }
            sb.append(")");
        }
        Object retVal = pjp.proceed(args);
        if (log.isDebugEnabled() && (sb != null)) {
            sb.append(" -> ");
            if (retVal instanceof Map) {
                if (((Map) retVal).size() > 10) {
                    sb.append("<big map>");
                } else {
                    sb.append(retVal);
                }
            } else if (retVal instanceof Collection) {
                if (((Collection) retVal).size() > 10) {
                    sb.append("<big collection>");
                } else {
                    sb.append(retVal);
                }
            } else {
                sb.append(retVal);
            }
            log.debug(sb.toString());
        }
        boolean wrapReturnVal = true;
        try {
            for (int index = 1; index < Integer.MAX_VALUE; index++) {
                Class<?> callerClass = sun.reflect.Reflection.getCallerClass(index);
                if (callerClass == null) {
                    break;
                }
                if ((Command.class.isAssignableFrom(callerClass) || SystemInitializer.class.isAssignableFrom(callerClass)) && (!Advised.class.isAssignableFrom(callerClass))) {
                    wrapReturnVal = false;
                    break;
                }
            }
        } catch (Throwable t) {
            log.warn("terminated search of call stack by a throw: " + t, t);
        }
        if (wrapReturnVal) {
            retVal = domainObjectWrapper.wrapPersistentEntities(retVal, System.currentTimeMillis());
        }
        return retVal;
    }
}
