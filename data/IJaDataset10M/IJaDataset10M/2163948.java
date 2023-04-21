package org.dmd.dmv.server.generated.dmw;

import org.dmd.dmc.*;
import org.dmd.dms.*;
import org.dmd.dms.generated.dmo.MetaDMSAG;
import org.dmd.dms.generated.types.DmcTypeModifierMV;
import org.dmd.dmv.server.extended.AttributeRule;
import org.dmd.dmv.server.extended.IntegerRangeRule;
import org.dmd.dmv.shared.generated.dmo.IntegerRangeRuleDMO;

/**
 * null
 * <P>
 * Generated from the dmv schema at version 0.1
 * <P>
 * This code was auto-generated by the dmggenerator utility and shouldn't be alterred manually!
 * Generated from: org.dmd.dmg.generators.BaseDMWGeneratorNew.dumpWrapper(BaseDMWGeneratorNew.java:365)
 */
public abstract class IntegerRangeRuleDMW extends AttributeRule {

    public IntegerRangeRuleDMW() {
        super(new IntegerRangeRuleDMO(), org.dmd.dmv.server.generated.DmvSchemaAG._IntegerRangeRule);
    }

    public IntegerRangeRuleDMW(DmcTypeModifierMV mods) {
        super(new IntegerRangeRuleDMO(mods), org.dmd.dmv.server.generated.DmvSchemaAG._IntegerRangeRule);
    }

    public IntegerRangeRule getModificationRecorder() {
        IntegerRangeRule rc = new IntegerRangeRule();
        rc.setModifier(new DmcTypeModifierMV(MetaDMSAG.__modify));
        return (rc);
    }

    public IntegerRangeRuleDMW(IntegerRangeRuleDMO obj) {
        super(obj, org.dmd.dmv.server.generated.DmvSchemaAG._IntegerRangeRule);
    }

    public IntegerRangeRule cloneIt() {
        IntegerRangeRule rc = new IntegerRangeRule();
        rc.setDmcObject(getDMO().cloneIt());
        return (rc);
    }

    public IntegerRangeRuleDMO getDMO() {
        return ((IntegerRangeRuleDMO) core);
    }

    protected IntegerRangeRuleDMW(IntegerRangeRuleDMO obj, ClassDefinition cd) {
        super(obj, cd);
    }

    public String getRuleTitle() {
        return (((IntegerRangeRuleDMO) core).getRuleTitle());
    }

    public void setRuleTitle(Object value) throws DmcValueException {
        ((IntegerRangeRuleDMO) core).setRuleTitle(value);
    }

    public void setRuleTitle(String value) {
        ((IntegerRangeRuleDMO) core).setRuleTitle(value);
    }

    public void remRuleTitle() {
        ((IntegerRangeRuleDMO) core).remRuleTitle();
    }
}