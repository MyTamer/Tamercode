package megamek.common.weapons;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author BATTLEMASTER
 */
public class ISGAC4 extends ACWeapon {

    /**
     * 
     */
    private static final long serialVersionUID = 49211848611799265L;

    /**
     * 
     */
    public ISGAC4() {
        super();
        techLevel = TechConstants.T_IS_UNOFFICIAL;
        name = "GAC/4";
        setInternalName(name);
        addLookupName("IS Gatling Auto Cannon/4");
        addLookupName("Gatling Auto Cannon/4");
        addLookupName("Gatling AutoCannon/4");
        addLookupName("ISGAC4");
        addLookupName("IS Gatling Autocannon/4");
        heat = 4;
        damage = 8;
        rackSize = 4;
        minimumRange = 4;
        shortRange = 8;
        mediumRange = 16;
        longRange = 24;
        extremeRange = 32;
        tonnage = 12.0f;
        criticals = 4;
        bv = 175;
        cost = 200000;
        explosive = true;
        shortAV = 8;
        medAV = 8;
        longAV = 8;
        extAV = 8;
        maxRange = RANGE_LONG;
        explosionDamage = damage;
        toHitModifier = -1;
        flags = flags.or(F_DIRECT_FIRE).or(F_BALLISTIC).or(F_MECH_WEAPON).or(F_AERO_WEAPON).or(F_TANK_WEAPON).or(F_PULSE);
        ammoType = AmmoType.T_AC;
        techRating = RATING_X;
        atClass = CLASS_AC;
    }
}
