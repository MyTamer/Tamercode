package org.dmd.dmv.server.generated.dmw;

import org.dmd.dmc.*;
import org.dmd.dms.*;
import org.dmd.dms.generated.dmo.MetaDMSAG;
import org.dmd.dms.generated.types.DmcTypeModifierMV;
import org.dmd.dmv.server.extended.ClassRule;
import org.dmd.dmv.server.extended.MayRule;
import org.dmd.dmv.shared.generated.dmo.MayRuleDMO;

/**
 * The MustRule verifies that an object has its must attributes.
 * <P>
 * Generated from the dmv schema at version 0.1
 * <P>
 * This code was auto-generated by the dmggenerator utility and shouldn't be alterred manually!
 * Generated from: org.dmd.dmg.generators.BaseDMWGeneratorNew.dumpWrapper(BaseDMWGeneratorNew.java:365)
 */
public abstract class MayRuleDMW extends ClassRule {

    public MayRuleDMW() {
        super(new MayRuleDMO(), org.dmd.dmv.server.generated.DmvSchemaAG._MayRule);
    }

    public MayRuleDMW(DmcTypeModifierMV mods) {
        super(new MayRuleDMO(mods), org.dmd.dmv.server.generated.DmvSchemaAG._MayRule);
    }

    public MayRule getModificationRecorder() {
        MayRule rc = new MayRule();
        rc.setModifier(new DmcTypeModifierMV(MetaDMSAG.__modify));
        return (rc);
    }

    public MayRuleDMW(MayRuleDMO obj) {
        super(obj, org.dmd.dmv.server.generated.DmvSchemaAG._MayRule);
    }

    public MayRule cloneIt() {
        MayRule rc = new MayRule();
        rc.setDmcObject(getDMO().cloneIt());
        return (rc);
    }

    public MayRuleDMO getDMO() {
        return ((MayRuleDMO) core);
    }

    protected MayRuleDMW(MayRuleDMO obj, ClassDefinition cd) {
        super(obj, cd);
    }

    public String getRuleTitle() {
        return (((MayRuleDMO) core).getRuleTitle());
    }

    public void setRuleTitle(Object value) throws DmcValueException {
        ((MayRuleDMO) core).setRuleTitle(value);
    }

    public void setRuleTitle(String value) {
        ((MayRuleDMO) core).setRuleTitle(value);
    }

    public void remRuleTitle() {
        ((MayRuleDMO) core).remRuleTitle();
    }
}
