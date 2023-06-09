package org.dmd.dmp.shared.generated.types;

import java.io.Serializable;
import org.dmd.dmc.DmcInputStreamIF;
import org.dmd.dmc.DmcOutputStreamIF;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dmp.shared.generated.dmo.LoginRequestDMO;

/**
 * This is the generated DmcAttribute derivative for values of type LoginRequest
 * <P>
 * Generated from the dmp schema at version 0.1
 * <P>
 * This code was auto-generated by the dmogenerator utility and shouldn't be alterred manually!
 * Generated from: org.dmd.dms.util.DmoTypeFormatter.dumpNormalREFType(DmoTypeFormatter.java:197)
 */
@SuppressWarnings("serial")
public abstract class DmcTypeLoginRequestREF extends DmcAttribute<LoginRequestDMO> implements Serializable {

    public DmcTypeLoginRequestREF() {
    }

    public DmcTypeLoginRequestREF(DmcAttributeInfo ai) {
        super(ai);
    }

    protected LoginRequestDMO typeCheck(Object value) throws DmcValueException {
        if (value instanceof LoginRequestDMO) return ((LoginRequestDMO) value);
        throw (new DmcValueException("Object of class: " + value.getClass().getName() + " passed where object compatible with LoginRequestDMO expected."));
    }

    @Override
    public void serializeValue(DmcOutputStreamIF dos, LoginRequestDMO value) throws Exception {
        value.serializeIt(dos);
    }

    @Override
    public LoginRequestDMO deserializeValue(DmcInputStreamIF dis) throws Exception {
        LoginRequestDMO rc = (LoginRequestDMO) dis.getDMOInstance(dis);
        rc.deserializeIt(dis);
        return (rc);
    }

    @Override
    public LoginRequestDMO cloneValue(LoginRequestDMO value) {
        return ((LoginRequestDMO) value.cloneIt());
    }
}
