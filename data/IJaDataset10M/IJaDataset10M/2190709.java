package org.dmd.mvw.tools.mvwgenerator.generated.types;

import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dmc.DmcOutputStreamIF;
import org.dmd.dmc.DmcInputStreamIF;
import org.dmd.dmc.types.DmcTypeNamedObjectREF;
import org.dmd.dmc.types.CamelCaseName;
import org.dmd.mvw.tools.mvwgenerator.generated.types.ActivityREF;
import org.dmd.mvw.tools.mvwgenerator.generated.dmo.ActivityDMO;

/**
 * This is the generated DmcAttribute derivative for values of type Activity
 * <P>
 * Generated from the mvw schema at version 0.1
 * <P>
 * This code was auto-generated by the dmogenerator utility and shouldn't be alterred manually!
 * Generated from: org.dmd.dms.util.DmoTypeFormatter.dumpNamedREFHelperType(DmoTypeFormatter.java:585)
 */
@SuppressWarnings("serial")
public abstract class DmcTypeActivityREF extends DmcTypeNamedObjectREF<ActivityREF, CamelCaseName> {

    public DmcTypeActivityREF() {
    }

    public DmcTypeActivityREF(DmcAttributeInfo ai) {
        super(ai);
    }

    @Override
    protected ActivityREF getNewHelper() {
        return (new ActivityREF());
    }

    @Override
    protected CamelCaseName getNewName() {
        return (new CamelCaseName());
    }

    @Override
    protected String getDMOClassName() {
        return (ActivityDMO.class.getName());
    }

    @Override
    protected boolean isDMO(Object value) {
        if (value instanceof ActivityDMO) return (true);
        return (false);
    }

    @Override
    protected ActivityREF typeCheck(Object value) throws DmcValueException {
        ActivityREF rc = null;
        if (value instanceof ActivityREF) rc = (ActivityREF) value; else if (value instanceof ActivityDMO) rc = new ActivityREF((ActivityDMO) value); else if (value instanceof CamelCaseName) rc = new ActivityREF((CamelCaseName) value); else if (value instanceof String) rc = new ActivityREF((String) value); else throw (new DmcValueException("Object of class: " + value.getClass().getName() + " passed where object compatible with ActivityREF, ActivityDMO or String expected."));
        return (rc);
    }

    @Override
    public void serializeValue(DmcOutputStreamIF dos, ActivityREF value) throws Exception {
        value.serializeIt(dos);
    }

    @Override
    public ActivityREF deserializeValue(DmcInputStreamIF dis) throws Exception {
        ActivityREF rc = new ActivityREF();
        rc.deserializeIt(dis);
        return (rc);
    }

    @Override
    public ActivityREF cloneValue(ActivityREF value) {
        return (new ActivityREF(value));
    }
}
