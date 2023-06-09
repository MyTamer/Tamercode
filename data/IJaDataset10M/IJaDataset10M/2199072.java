package org.codecover.eclipse.tscmanager;

import java.lang.reflect.InvocationTargetException;
import java.util.ConcurrentModificationException;
import org.codecover.eclipse.tscmanager.exceptions.CancelException;
import org.codecover.model.TestSessionContainer;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * An <code>ActiveTSContainerRunnable</code> which checks before execution if an other test session
 * container was activated in the time between the runnable was created and the execution of the runnable was
 * initiated. If this is the case a <code>ConcurrentModificationException</code> (wrapped in an
 * <code>InvocationTargetException</code>) is thrown when the runnable is executed and the task isn't
 * performed. This procedure ensures that the runnable doesn't work with stale references when it is executed.
 * <p>
 * The task to perform has to be defined by overriding
 * {@link #runTask(ActiveTSContainerInfo, IProgressMonitor)}.
 * 
 * @author Robert Hanussek
 * @version 1.0 ($Id: AbstractActiveTSContainerRunnable.java 49 2009-06-01 08:52:19Z ahija $)
 */
public abstract class AbstractActiveTSContainerRunnable implements ActiveTSContainerRunnable {

    private final TestSessionContainer tsc;

    /**
   * Constructs an <code>ActiveTSContainerRunnable</code> which checks before execution if an other test
   * session container was selected in the time between the runnable was created and the execution of the
   * runnable was initiated.
   * 
   * @param tsc the test session container the user wants to operate on with this runnable
   */
    public AbstractActiveTSContainerRunnable(TestSessionContainer tsc) {
        if (tsc == null) {
            throw new NullPointerException("tsc musn't be null");
        }
        this.tsc = tsc;
    }

    public abstract String getDescription();

    public void run(ActiveTSContainerInfo activeTSCInfo, IProgressMonitor monitor) throws InvocationTargetException, CancelException {
        if (!isTSCstillActive(this.tsc, activeTSCInfo)) {
            throw new InvocationTargetException(new ConcurrentModificationException("An other test session container was" + " selected in the time between the runnable" + " was created and the execution of the" + " runnable was initiated."));
        }
        this.runTask(activeTSCInfo, monitor);
    }

    private static boolean isTSCstillActive(TestSessionContainer tsc, ActiveTSContainerInfo activeTSCInfo) {
        return (tsc == activeTSCInfo.getTestSessionContainer());
    }

    /**
   * Override this method to define the task to execute on the active test session container, see
   * {@link ActiveTSContainerRunnable#run(ActiveTSContainerInfo, IProgressMonitor)} for details.
   */
    public abstract void runTask(ActiveTSContainerInfo activeTSCInfo, IProgressMonitor monitor) throws InvocationTargetException, CancelException;
}
