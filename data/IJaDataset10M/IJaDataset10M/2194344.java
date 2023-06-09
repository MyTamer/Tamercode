package com.google.gdata.data.gtt;

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * Describes a translation complete.
 *
 * 
 */
@ExtensionDescription.Default(nsAlias = GttNamespace.GTT_ALIAS, nsUri = GttNamespace.GTT, localName = TranslationComplete.XML_NAME)
public class TranslationComplete extends AbstractExtension {

    /** XML element name */
    static final String XML_NAME = "translationComplete";

    /** Value */
    private Boolean value = null;

    /**
   * Default mutable constructor.
   */
    public TranslationComplete() {
        super();
    }

    /**
   * Immutable constructor.
   *
   * @param value value.
   */
    public TranslationComplete(Boolean value) {
        super();
        setValue(value);
        setImmutable(true);
    }

    /**
   * Returns the value.
   *
   * @return value
   */
    public Boolean getValue() {
        return value;
    }

    /**
   * Sets the value.
   *
   * @param value value or <code>null</code> to reset
   */
    public void setValue(Boolean value) {
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
        ExtensionDescription desc = ExtensionDescription.getDefaultDescription(TranslationComplete.class);
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
        value = helper.consumeBoolean(null, true);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!sameClassAs(obj)) {
            return false;
        }
        TranslationComplete other = (TranslationComplete) obj;
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
        return "{TranslationComplete value=" + value + "}";
    }
}
