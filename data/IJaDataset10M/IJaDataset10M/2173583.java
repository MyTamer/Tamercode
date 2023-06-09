package org.dmd.dmp.shared.generated.types;

import java.io.Serializable;
import org.dmd.dmc.DmcInputStreamIF;
import org.dmd.dmc.DmcOutputStreamIF;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dmp.shared.generated.enums.*;

/**
 * This is the generated DmcAttribute derivative for values of type ScopeEnum
 * <P>
 * Generated from the dmp schema at version 0.1
 * <P>
 * This code was auto-generated by the dmogenerator utility and shouldn't be alterred manually!
 * Generated from: org.dmd.dms.util.DmoTypeFormatter.dumpEnumType(DmoTypeFormatter.java:291)
 */
@SuppressWarnings("serial")
public abstract class DmcTypeScopeEnum extends DmcAttribute<ScopeEnum> implements Serializable {

    public DmcTypeScopeEnum() {
    }

    public DmcTypeScopeEnum(DmcAttributeInfo ai) {
        super(ai);
    }

    protected ScopeEnum typeCheck(Object value) throws DmcValueException {
        ScopeEnum rc = null;
        if (value instanceof ScopeEnum) {
            rc = (ScopeEnum) value;
        } else if (value instanceof String) {
            rc = ScopeEnum.get((String) value);
        } else if (value instanceof Integer) {
            rc = ScopeEnum.get((Integer) value);
        } else {
            throw (new DmcValueException("Object of class: " + value.getClass().getName() + " passed where object compatible with ScopeEnum expected."));
        }
        if (rc == null) {
            throw (new DmcValueException("Value: " + value.toString() + " is not a valid ScopeEnum value."));
        }
        return (rc);
    }

    /**
     * Returns a clone of a value associated with this type.
     */
    public ScopeEnum cloneValue(ScopeEnum val) {
        return (val);
    }

    /**
     * Writes a ScopeEnum.
     */
    public void serializeValue(DmcOutputStreamIF dos, ScopeEnum value) throws Exception {
        dos.writeShort(value.intValue());
    }

    /**
     * Reads a ScopeEnum.
     */
    public ScopeEnum deserializeValue(DmcInputStreamIF dis) throws Exception {
        return (ScopeEnum.get(dis.readShort()));
    }
}
