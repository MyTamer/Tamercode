package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/**
 * A task to provide "nice-ness" to the current thread, and/or to
 * query the current value.
 * Examples:
 * <pre> &lt;nice currentPriority="current.value" &gt;</pre><p>
 * Set <code>currentPriority</code> to the current priority
 * <pre> &lt;nice newPriority="10" &gt;</pre><p>
 * Raise the priority of the build process (But not forked programs)
 * <pre> &lt;nice currentPriority="old" newPriority="3" &gt;</pre><p>
 * Lower the priority of the build process (But not forked programs), and save
 * the old value to the property <code>old</code>.
 *
 * @ant.task name="nice" category="control"
 */
public class Nice extends Task {

    /**
     * the new priority
     */
    private Integer newPriority;

    /**
     * the current priority
     */
    private String currentPriority;

    /**
     * Execute the task
     * @exception BuildException if something goes wrong with the build
     */
    public void execute() throws BuildException {
        Thread self = Thread.currentThread();
        int priority = self.getPriority();
        if (currentPriority != null) {
            String current = Integer.toString(priority);
            getProject().setNewProperty(currentPriority, current);
        }
        if (newPriority != null && priority != newPriority.intValue()) {
            try {
                self.setPriority(newPriority.intValue());
            } catch (SecurityException e) {
                log("Unable to set new priority -a security manager is in the way", Project.MSG_WARN);
            } catch (IllegalArgumentException iae) {
                throw new BuildException("Priority out of range", iae);
            }
        }
    }

    /**
     * The name of a property to set to the value of the current
     * thread priority. Optional
     * @param currentPriority the property name.
     */
    public void setCurrentPriority(String currentPriority) {
        this.currentPriority = currentPriority;
    }

    /**
     * the new priority, in the range 1-10.
     * @param newPriority the new priority value.
     */
    public void setNewPriority(int newPriority) {
        if (newPriority < Thread.MIN_PRIORITY || newPriority > Thread.MAX_PRIORITY) {
            throw new BuildException("The thread priority is out of the range 1-10");
        }
        this.newPriority = new Integer(newPriority);
    }
}
