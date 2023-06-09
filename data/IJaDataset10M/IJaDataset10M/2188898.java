package com.google.gdata.data.projecthosting;

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * DateTime this issue is closed.
 *
 * 
 */
@ExtensionDescription.Default(nsAlias = ProjectHostingNamespace.ISSUES_ALIAS, nsUri = ProjectHostingNamespace.ISSUES, localName = ClosedDate.XML_NAME)
public class ClosedDate extends AbstractExtension {

    /** XML element name */
    static final String XML_NAME = "closedDate";

    /** Value */
    private DateTime value = null;

    /**
   * Default mutable constructor.
   */
    public ClosedDate() {
        super();
    }

    /**
   * Immutable constructor.
   *
   * @param value value.
   */
    public ClosedDate(DateTime value) {
        super();
        setValue(value);
        setImmutable(true);
    }

    /**
   * Returns the value.
   *
   * @return value
   */
    public DateTime getValue() {
        return value;
    }

    /**
   * Sets the value.
   *
   * @param value value or <code>null</code> to reset
   */
    public void setValue(DateTime value) {
        throwExceptionIfImmutable();
        this.value = value;
    }

    /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
    public boolean hasValue() {
        return getValue() != null;
    }

    @Override
    protected void validate() {
        if (value == null) {
            throw new IllegalStateException("Missing text content");
        }
    }

    /**
   * Returns the extension description, specifying whether it is required, and
   * whether it is repeatable.
   *
   * @param required   whether it is required
   * @param repeatable whether it is repeatable
   * @return extension description
   */
    public static ExtensionDescription getDefaultDescription(boolean required, boolean repeatable) {
        ExtensionDescription desc = ExtensionDescription.getDefaultDescription(ClosedDate.class);
        desc.setRequired(required);
        desc.setRepeatable(repeatable);
        return desc;
    }

    @Override
    protected void putAttributes(AttributeGenerator generator) {
        generator.setContent(value.toString());
    }

    @Override
    protected void consumeAttributes(AttributeHelper helper) throws ParseException {
        value = helper.consumeDateTime(null, true);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!sameClassAs(obj)) {
            return false;
        }
        ClosedDate other = (ClosedDate) obj;
        return eq(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        if (value != null) {
            result = 37 * result + value.hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        return "{ClosedDate value=" + value + "}";
    }
}
