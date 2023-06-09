package edu.byu.ece.edif.util.jsap;

import com.martiansoftware.jsap.ParseException;
import com.martiansoftware.jsap.StringParser;
import com.martiansoftware.jsap.stringparsers.FloatStringParser;

/**
 * A {@link com.martiansoftware.jsap.StringParser} for parsing Floats with
 * optional minimum and maximum values. The {@link #parse(String)} method
 * delegates the actual parsing to <code>new Float(String)</code>. If
 * <code>new Float(String)</code> throws a {@link NumberFormatException}, it
 * is encapsulated in a {@link ParseException} and re-thrown. Also, if the
 * parameter is lower than the minimum or higher than the maximum, a
 * <code>ParseException</code> is thrown.
 * <p>
 * The default minimum limit is {@link Float#MIN_VALUE} and the default maximum
 * limit is {@link Float#MAX_VALUE}.
 * 
 * @author <a href="mailto:jcarroll@byu.net">James Carroll</a>, Brigham Young
 * University
 * @see com.martiansoftware.jsap.StringParser
 * @see FloatStringParser
 * @see java.lang.Float
 */
class BoundedFloatStringParser extends FloatStringParser {

    /**
     * Returns a {@link BoundedFloatStringParser} with the specified minimum and
     * maximum values. Also, the minimum and maximum can each optionally be
     * inclusive or non-inclusive.
     * 
     * @param min Minimum acceptable value. If null, the minimum will be set to
     * Float.MIN_VALUE
     * @param max Maximum acceptable value. If null, the maximum will be set to
     * Float.MAX_VALUE
     * @param includeMax Determines if max should be considered part of the
     * valid range. If true, the valid range includes max. If null, includeMax
     * will default to true.
     * @throws IllegalArgumentException if min is greater than max.
     * @deprecated Use {@link #getParser(Float, Float, Boolean, Boolean)}.
     */
    @Deprecated
    public BoundedFloatStringParser(Float min, Float max, Boolean includeMin, Boolean includeMax) throws IllegalArgumentException {
        if (min == null) this.min = Float.MIN_VALUE; else this.min = min;
        if (max == null) this.max = Float.MAX_VALUE; else this.max = max;
        if (this.min > this.max) {
            throw new IllegalArgumentException("Invalid limits. Minimum " + min + " is larger than the maximum " + max);
        }
        if (includeMin == null) this.includeMin = true; else this.includeMin = includeMin;
        if (includeMax == null) this.includeMax = true; else this.includeMax = includeMax;
        this.range = (this.includeMin ? "[" : "(") + this.min + "," + this.max + (this.includeMax ? "]" : ")");
    }

    /**
     * @param min Minimum acceptable value. If null, the minimum will be set to
     * Float.MIN_VALUE
     * @param max Maximum acceptable value. If null, the maximum will be set to
     * Float.MAX_VALUE
     * @param includeMin Determines if min should be considered part of the
     * valid range. If true, the valid range includes min. If null, includeMin
     * will default to true.
     * @param includeMax Determines if max should be considered part of the
     * valid range. If true, the valid range includes max. If null, includeMax
     * will default to true.
     * @return A BoundedFloatStringParser with the specified lower and upper
     * bounds and specified inclusion or non-inclusion for each of the bounds.
     * @throws IllegalArgumentException if min is greater than max.
     */
    public static BoundedFloatStringParser getParser(Float min, Float max, Boolean includeMin, Boolean includeMax) throws IllegalArgumentException {
        return new BoundedFloatStringParser(min, max, includeMin, includeMax);
    }

    /**
     * @param min Minimum acceptable value. If null, the minimum will be set to
     * Float.MIN_VALUE
     * @param max Maximum acceptable value. If null, the maximum will be set to
     * Float.MAX_VALUE
     * @return A BoundedFloatStringParser with the specified lower and upper
     * bounds, inclusive.
     * @throws IllegalArgumentException if min is greater than max.
     */
    public static BoundedFloatStringParser getParser(Float min, Float max) throws IllegalArgumentException {
        return getParser(min, max, null, null);
    }

    /**
     * @throws IllegalArgumentException Only if getParser(Float, Float, Boolean,
     * Boolean) does so.
     * @return A BoundedFloatStringParser with no lower or upper limit (other
     * than Float.MIN_VALUE and Float.MAX_VALUE). This should behave the same as
     * {@link com.martiansoftware.jsap.JSAP#FLOAT_PARSER} and is provided for
     * complete flexibility.
     * @see com.martiansoftware.jsap.stringparsers.FloatStringParser#getParser()
     */
    public static BoundedFloatStringParser getParser() throws IllegalArgumentException {
        return getParser(null, null, null, null);
    }

    /**
     * @return A String representation of the range, as illustrated in
     * {@link #range}.
     */
    public String getRange() {
        return this.range;
    }

    /**
     * Parses the specified argument into a Float. This method delegates the
     * actual parsing to <code>new Float(String)</code>. If
     * <code>new Float(String)</code> throws a
     * <code>NumberFormatException</code>, it is encapsulated in a
     * <code>ParseException</code> and re-thrown. Also ensures that the value
     * specified is in the range for this parser.<br>
     * Overrides {@link FloatStringParser#parse(String)}
     * 
     * @param arg the argument to parse
     * @return a Float object with the value contained in the specified
     * argument.
     * @throws ParseException if the specified value is outside the valid range
     * or if <code>new Float(arg)</code> throws a NumberFormatException.
     * @see java.lang.Float
     * @see StringParser#parse(String)
     */
    @Override
    public Object parse(String arg) throws ParseException {
        Float result = null;
        try {
            result = new Float(arg);
        } catch (NumberFormatException e) {
            throw (new ParseException("Unable to convert '" + arg + "' to a Float.", e));
        }
        if (result < min || (!includeMin && result <= min)) throw new ParseException(arg + " is out of range: " + getRange());
        if (result > max || (!includeMax && result >= max)) throw new ParseException(arg + " is out of range: " + getRange());
        return (result);
    }

    /**
     * If true, the valid range includes the minimum value.
     */
    protected Boolean includeMin = true;

    /**
     * If true, the valid range includes the maximum value.
     */
    protected Boolean includeMax = true;

    /**
     * Minimum acceptable value.
     */
    protected Float min = Float.MIN_VALUE;

    /**
     * Maximum acceptable value.
     */
    protected Float max = Float.MAX_VALUE;

    /**
     * <p>
     * Text representation of the range.
     * <p>
     * Examples:
     * <dl>
     * <dt>[0,1]
     * <dd> Zero to one, inclusive
     * <dt>(-100,100)
     * <dd> -100 to 100, non-inclusive
     * <dt>[3.14,6.28)
     * <dd> 3.14 to 6.28, including 3.14 and not including 6.28
     * </dl>
     */
    protected String range;
}
