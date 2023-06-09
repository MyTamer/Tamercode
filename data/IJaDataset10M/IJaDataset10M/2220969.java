package org.springframework.binding.value.support;

import org.springframework.rules.closure.Closure;
import org.springframework.core.style.StylerUtils;
import org.springframework.util.Assert;

/**
 * ValueModel adding a <code>refreshable</code> aspect. A {@link Closure} is
 * given that may be executed using the {@link #refresh()} method or forced to
 * be executed on every {@link #getValue()}. The given refresh Closure is used
 * to set the value before reading it.
 *
 * @author Keith Donald
 */
public class RefreshableValueHolder extends ValueHolder {

    private final Closure refreshFunction;

    private boolean alwaysRefresh;

    /**
	 * Constructor supplying a refresh <code>Closure</code>. Refresh has to be triggered
	 * manually.
	 */
    public RefreshableValueHolder(Closure refreshFunction) {
        this(refreshFunction, false);
    }

    /**
	 * Constructor supplying a refresh <code>Closure</code> that allways has to be triggered
	 * when reading the value.
	 */
    public RefreshableValueHolder(Closure refreshFunction, boolean alwaysRefresh) {
        this(refreshFunction, alwaysRefresh, true);
    }

    /**
	 * Constructor supplying a refresh <code>Closure</code> that allways has to be triggered
	 * when reading the value. Additionally a refresh is triggered on construction.
	 */
    public RefreshableValueHolder(Closure refreshFunction, boolean alwaysRefresh, boolean lazyInit) {
        super();
        Assert.notNull(refreshFunction, "The refresh callback cannot be null");
        this.refreshFunction = refreshFunction;
        this.alwaysRefresh = alwaysRefresh;
        if (!lazyInit) {
            refresh();
        }
    }

    /**
	 * {@inheritDoc}
	 *
	 * If allwaysRefresh is set, this method will trigger refresh() each time it is executed.
	 */
    public Object getValue() {
        if (alwaysRefresh) {
            refresh();
        }
        return super.getValue();
    }

    /**
	 * Refresh te value by executing the refresh <code>Closure</code>.
	 */
    public void refresh() {
        if (logger.isDebugEnabled()) {
            logger.debug("Refreshing held value '" + StylerUtils.style(super.getValue()) + "'");
        }
        setValue(refreshFunction.call(null));
    }
}
