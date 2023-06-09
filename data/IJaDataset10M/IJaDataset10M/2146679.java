package org.wwweeeportal.util.convert;

import org.springframework.core.convert.converter.*;

/**
 * An abstract base class used to ease the implementation of a {@link Converter} by taking care of
 * <code>Exception</code> and <code>null</code> handling.
 * 
 * @param <S> The type of object this converter accepts as a source.
 * @param <T> The type of object this converter generates as a target.
 */
public abstract class AbstractConverter<S, T> implements Converter<S, T> {

    /**
   * Should <code>null</code> input values still be run through the conversion process?
   * 
   * @return <code>true</code> if <code>null</code> inputs will be processed (defaults to <code>false</code>).
   */
    protected boolean convertNullSource() {
        return false;
    }

    /**
   * What value should be returned by {@link #convert(Object)} when a <code>null</code> input value is encountered?
   * 
   * @return The value to be returned.
   */
    protected T getNullSourceResult() {
        return null;
    }

    /**
   * Get a value which will be substituted for a <code>null</code> input to the conversion process.
   * 
   * @return The value to be converted, instead of <code>null</code> (the default).
   */
    protected S getNullSourceDefault() {
        return null;
    }

    /**
   * What value should be returned by {@link #convert(Object)} when a <code>null</code> output value is encountered?
   * 
   * @return The value to be returned.
   */
    protected T getNullTargetResult() {
        return null;
    }

    /**
   * Convert the supplied <code>source</code>.
   * 
   * @param source The object to convert.
   * @return The target object generated by converting the given <code>source</code>.
   * @throws Exception If there was a problem converting the <code>source</code>.
   * @see #convert(Object)
   */
    protected abstract T convertImpl(final S source) throws Exception;

    @Override
    public T convert(S source) throws IllegalArgumentException {
        if ((source == null) && (!convertNullSource())) return getNullSourceResult();
        if (source == null) source = getNullSourceDefault();
        final T target;
        try {
            target = convertImpl(source);
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return (target != null) ? target : getNullTargetResult();
    }
}
