package org.springframework.webflow.engine;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.springframework.core.style.StylerUtils;
import org.springframework.webflow.core.collection.CollectionUtils;
import org.springframework.webflow.execution.FlowExecutionException;

/**
 * A typed set of state exception handlers, mainly for use internally by artifacts that can apply state exception
 * handling logic.
 * 
 * @see FlowExecutionExceptionHandler
 * @see Flow#getExceptionHandlerSet()
 * @see State#getExceptionHandlerSet()
 * 
 * @author Keith Donald
 */
public class FlowExecutionExceptionHandlerSet {

    /**
	 * The set of exception handlers.
	 */
    private List exceptionHandlers = new LinkedList();

    /**
	 * Add a state exception handler to this set.
	 * @param exceptionHandler the exception handler to add
	 * @return true if this set's contents changed as a result of the add operation
	 */
    public boolean add(FlowExecutionExceptionHandler exceptionHandler) {
        if (contains(exceptionHandler)) {
            return false;
        }
        return exceptionHandlers.add(exceptionHandler);
    }

    /**
	 * Add a collection of state exception handler instances to this set.
	 * @param exceptionHandlers the exception handlers to add
	 * @return true if this set's contents changed as a result of the add operation
	 */
    public boolean addAll(FlowExecutionExceptionHandler[] exceptionHandlers) {
        return CollectionUtils.addAllNoDuplicates(this.exceptionHandlers, exceptionHandlers);
    }

    /**
	 * Tests if this state exception handler is in this set.
	 * @param exceptionHandler the exception handler
	 * @return true if the state exception handler is contained in this set, false otherwise
	 */
    public boolean contains(FlowExecutionExceptionHandler exceptionHandler) {
        return exceptionHandlers.contains(exceptionHandler);
    }

    /**
	 * Remove the exception handler instance from this set.
	 * @param exceptionHandler the exception handler to add
	 * @return true if this set's contents changed as a result of the remove operation
	 */
    public boolean remove(FlowExecutionExceptionHandler exceptionHandler) {
        return exceptionHandlers.remove(exceptionHandler);
    }

    /**
	 * Returns the size of this state exception handler set.
	 * @return the exception handler set size
	 */
    public int size() {
        return exceptionHandlers.size();
    }

    /**
	 * Convert this list to a typed state exception handler array.
	 * @return the exception handler list, as a typed array
	 */
    public FlowExecutionExceptionHandler[] toArray() {
        return (FlowExecutionExceptionHandler[]) exceptionHandlers.toArray(new FlowExecutionExceptionHandler[exceptionHandlers.size()]);
    }

    /**
	 * Handle an exception that occurred during the context of the current flow execution request.
	 * <p>
	 * This implementation iterates over the ordered set of exception handler objects, delegating to each handler in the
	 * set until one handles the exception that occurred.
	 * @param exception the exception that occurred
	 * @param context the flow execution control context
	 * @return true if the exception was handled
	 */
    public boolean handleException(FlowExecutionException exception, RequestControlContext context) {
        Iterator it = exceptionHandlers.iterator();
        while (it.hasNext()) {
            FlowExecutionExceptionHandler handler = (FlowExecutionExceptionHandler) it.next();
            if (handler.canHandle(exception)) {
                handler.handle(exception, context);
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return StylerUtils.style(exceptionHandlers);
    }
}
