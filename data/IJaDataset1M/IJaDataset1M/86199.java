package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class ISMRM30IOS extends MRMWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 7118245780649534184L;

    /**
     *
     */
    public ISMRM30IOS() {
        super();
        techLevel = TechConstants.T_IS_ADVANCED;
        name = "MRM 30 (I-OS)";
        setInternalName(name);
        addLookupName("IOS MRM-30");
        addLookupName("ISMRM30 (IOS)");
        addLookupName("IS MRM 30 (IOS)");
        heat = 10;
        rackSize = 30;
        shortRange = 3;
        mediumRange = 8;
        longRange = 15;
        extremeRange = 16;
        tonnage = 9.5f;
        criticals = 5;
        bv = 34;
        flags = flags.or(F_ONESHOT);
        cost = 180000;
        shortAV = 18;
        medAV = 18;
        maxRange = RANGE_MED;
    }
}
