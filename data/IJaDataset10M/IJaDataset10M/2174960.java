package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;

/**
 * Helper class for attributes that can only take one of a fixed list
 * of values.
 *
 * <p>See {@link org.apache.tools.ant.taskdefs.FixCRLF FixCRLF} for an
 * example.
 *
 */
public abstract class EnumeratedAttribute {

    /**
     * The selected value in this enumeration.
     */
    protected String value;

    /**
     * the index of the selected value in the array.
     */
    private int index = -1;

    /**
     * This is the only method a subclass needs to implement.
     *
     * @return an array holding all possible values of the enumeration.
     * The order of elements must be fixed so that <tt>indexOfValue(String)</tt>
     * always return the same index for the same value.
     */
    public abstract String[] getValues();

    /** bean constructor */
    protected EnumeratedAttribute() {
    }

    /**
     * Factory method for instantiating EAs via API in a more
     * developer friendly way.
     * @param clazz             Class, extending EA, which to instantiate
     * @param value             The value to set on that EA
     * @return                  Configured EA
     * @throws BuildException   If the class could not be found or the value
     *                          is not valid for the given EA-class.
     * @see <a href="http://issues.apache.org/bugzilla/show_bug.cgi?id=14831">
     * http://issues.apache.org/bugzilla/show_bug.cgi?id=14831</a>
     */
    public static EnumeratedAttribute getInstance(Class clazz, String value) throws BuildException {
        if (!EnumeratedAttribute.class.isAssignableFrom(clazz)) {
            throw new BuildException("You have to provide a subclass from EnumeratedAttribut as clazz-parameter.");
        }
        EnumeratedAttribute ea = null;
        try {
            ea = (EnumeratedAttribute) clazz.newInstance();
        } catch (Exception e) {
            throw new BuildException(e);
        }
        ea.setValue(value);
        return ea;
    }

    /**
     * Invoked by {@link org.apache.tools.ant.IntrospectionHelper IntrospectionHelper}.
     * @param value the <code>String</code> value of the attribute
     * @throws BuildException if the value is not valid for the attribute
     */
    public final void setValue(String value) throws BuildException {
        int idx = indexOfValue(value);
        if (idx == -1) {
            throw new BuildException(value + " is not a legal value for this attribute");
        }
        this.index = idx;
        this.value = value;
    }

    /**
     * Is this value included in the enumeration?
     * @param value the <code>String</code> value to look up
     * @return true if the value is valid
     */
    public final boolean containsValue(String value) {
        return (indexOfValue(value) != -1);
    }

    /**
     * get the index of a value in this enumeration.
     * @param value the string value to look for.
     * @return the index of the value in the array of strings
     * or -1 if it cannot be found.
     * @see #getValues()
     */
    public final int indexOfValue(String value) {
        String[] values = getValues();
        if (values == null || value == null) {
            return -1;
        }
        for (int i = 0; i < values.length; i++) {
            if (value.equals(values[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @return the selected value.
     */
    public final String getValue() {
        return value;
    }

    /**
     * @return the index of the selected value in the array.
     * @see #getValues()
     */
    public final int getIndex() {
        return index;
    }

    /**
     * Convert the value to its string form.
     *
     * @return the string form of the value.
     */
    public String toString() {
        return getValue();
    }
}
