package org.jactr.tools.shell;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.core.concurrent.ExecutorServices;
import org.jactr.core.runtime.ACTRRuntime;
import org.jactr.core.runtime.event.ACTRRuntimeEvent;
import org.jactr.core.runtime.event.IACTRRuntimeListener;

public class RuntimeListener implements IACTRRuntimeListener {

    /**
   * Logger definition
   */
    private static final transient Log LOGGER = LogFactory.getLog(RuntimeListener.class);

    private static RuntimeListener _default = null;

    public static void setEnabled(boolean enable) {
        synchronized (RuntimeListener.class) {
            if (!enable && _default != null) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug("Removing runtime listener");
                ACTRRuntime.getRuntime().removeListener(_default);
                _default = null;
            } else if (enable && _default == null) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug("Installing runtime listener");
                _default = new RuntimeListener();
                ACTRRuntime.getRuntime().addListener(_default, ExecutorServices.INLINE_EXECUTOR);
            }
        }
    }

    public static boolean isEnabled() {
        synchronized (RuntimeListener.class) {
            return _default != null;
        }
    }

    public void modelAdded(ACTRRuntimeEvent event) {
    }

    public void modelRemoved(ACTRRuntimeEvent event) {
    }

    public void runtimeResumed(ACTRRuntimeEvent event) {
        try {
            Controller.getInterpreter().set("runState", "[running]");
        } catch (Exception e) {
        }
    }

    /**
   * when called, this makes "runtime" variable accessible
   * 
   * @param event
   * @see org.jactr.core.runtime.event.IACTRRuntimeListener#runtimeStarted(org.jactr.core.runtime.event.ACTRRuntimeEvent)
   */
    public void runtimeStarted(ACTRRuntimeEvent event) {
        try {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Making runtime available");
            Controller.getInterpreter().set("runtime", event.getSource());
            if (event.getSource().getModels().size() == 1) Controller.getInterpreter().eval("use(\"" + event.getSource().getModels().iterator().next() + "\")");
            Controller.getInterpreter().set("runState", "[running]");
        } catch (Exception e) {
            LOGGER.error("Could not set runtime context : ", e);
        }
    }

    /**
   * when executed blocks the runtime entirely, preventing disposal until quit
   * is called
   * 
   * @param event
   * @see org.jactr.core.runtime.event.IACTRRuntimeListener#runtimeStopped(org.jactr.core.runtime.event.ACTRRuntimeEvent)
   */
    public void runtimeStopped(ACTRRuntimeEvent event) {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Blocking until Controller.quit() is called");
        try {
            Controller.getInterpreter().set("runState", "[stopped]");
        } catch (Exception e) {
        }
        Controller.block();
    }

    public void runtimeSuspended(ACTRRuntimeEvent event) {
        try {
            Controller.getInterpreter().set("runState", "[suspended]");
        } catch (Exception e) {
        }
    }
}
