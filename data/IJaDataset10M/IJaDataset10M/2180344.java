package org.soda.util.concurrent;

import java.util.concurrent.Executors;

/**
 * A task that returns a result and may throw an exception. Implementors define a single method with no arguments called
 * <tt>call</tt>.
 * 
 * <p>
 * The <tt>Callable</tt> interface is similar to {@link java.lang.Runnable}, in that both are designed for classes
 * whose instances are potentially executed by another thread. A <tt>Runnable</tt>, however, does not return a result
 * and cannot throw a checked exception.
 * 
 * <p>
 * The {@link Executors} class contains utility methods to convert from other common forms to <tt>Callable</tt>
 * classes.
 * 
 * @see Executor
 * @since 1.5
 * @author Doug Lea
 */
public interface Callable {

    /**
	 * Computes a result, or throws an exception if unable to do so.
	 * 
	 * @return computed result
	 * @throws Exception
	 *             if unable to compute a result
	 */
    Object call() throws Exception;
}
