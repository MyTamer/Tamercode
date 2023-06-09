package org.eclipse.jface.operation;

/**
 * A thread listener is an object that is interested in receiving notifications
 * of thread changes.  For example, a thread listener can be used to notify a 
 * runnable of the thread that will execute it, allowing the runnable to transfer
 * thread-local state from the calling thread before control passes to the new thread.
 * 
 * @since 3.1
 */
public interface IThreadListener {

    /**
	 * Notification that a thread change is occurring.
	 * 
	 * @param thread The new thread
	 */
    public void threadChange(Thread thread);
}
