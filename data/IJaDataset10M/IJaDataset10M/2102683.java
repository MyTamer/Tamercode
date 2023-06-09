package org.jfree.xml.attributehandlers;

import org.jfree.util.Log;

/**
 * A class that handles the conversion of {@link Integer} attributes to and from an appropriate
 * {@link String} representation.
 */
public class IntegerAttributeHandler implements AttributeHandler {

    /**
     * Creates a new attribute handler.
     */
    public IntegerAttributeHandler() {
        super();
    }

    /**
     * Converts the attribute to a string.
     *
     * @param o  the attribute ({@link Integer} expected).
     * 
     * @return A string representing the integer value.
     */
    public String toAttributeValue(final Object o) {
        try {
            final Integer in = (Integer) o;
            return in.toString();
        } catch (ClassCastException cce) {
            if (o != null) {
                Log.debug("ClassCastException: Expected Integer, found " + o.getClass());
            }
            throw cce;
        }
    }

    /**
     * Converts a string to a {@link Integer}.
     *
     * @param s  the string.
     *
     * @return a {@link Integer}.
     */
    public Object toPropertyValue(final String s) {
        return new Integer(s);
    }
}
