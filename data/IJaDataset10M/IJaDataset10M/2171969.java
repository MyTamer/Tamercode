package com.google.gdata.model.gd;

import com.google.gdata.model.DefaultRegistry;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

/**
 * Prefix to a person's name.
 *
 * 
 */
public class NamePrefix extends Element {

    /**
   * The key for this element.
   */
    public static final ElementKey<String, NamePrefix> KEY = ElementKey.of(new QName(Namespaces.gNs, "namePrefix"), String.class, NamePrefix.class);

    static {
        ElementCreator builder = DefaultRegistry.build(KEY);
    }

    /**
   * Default mutable constructor.
   */
    public NamePrefix() {
        this(KEY);
    }

    /**
   * Create an instance using a different key.
   */
    public NamePrefix(ElementKey<String, ? extends NamePrefix> key) {
        super(key);
    }

    /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as the key
   * for the element.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
    public NamePrefix(ElementKey<String, ? extends NamePrefix> key, Element source) {
        super(key, source);
    }

    /**
   * Constructs a new instance with the given value.
   *
   * @param value value.
   */
    public NamePrefix(String value) {
        this();
        setValue(value);
    }

    @Override
    public NamePrefix lock() {
        return (NamePrefix) super.lock();
    }

    /**
   * Returns the value.
   *
   * @return value
   */
    public String getValue() {
        return super.getTextValue(KEY);
    }

    /**
   * Sets the value.
   *
   * @param value value or <code>null</code> to reset
   */
    public NamePrefix setValue(String value) {
        super.setTextValue(value);
        return this;
    }

    /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
    public boolean hasValue() {
        return super.hasTextValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!sameClassAs(obj)) {
            return false;
        }
        NamePrefix other = (NamePrefix) obj;
        return eq(getValue(), other.getValue());
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        if (getValue() != null) {
            result = 37 * result + getValue().hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        return "{NamePrefix value=" + getTextValue() + "}";
    }
}
