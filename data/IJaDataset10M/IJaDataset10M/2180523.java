package megamek.common.weapons;

import megamek.common.AmmoType;
import megamek.common.IGame;
import megamek.common.TechConstants;
import megamek.common.ToHitData;
import megamek.common.actions.WeaponAttackAction;
import megamek.server.Server;

/**
 * @author Jay Lawson
 */
public class StingrayWeapon extends SubCapitalMissileWeapon {

    /**
     * 
     */
    private static final long serialVersionUID = 3827228773281489872L;

    /**
     * 
     */
    public StingrayWeapon() {
        super();
        this.techLevel = TechConstants.T_IS_ADVANCED;
        this.name = "Stingray";
        this.setInternalName(this.name);
        this.addLookupName("Stingray");
        this.heat = 9;
        this.damage = 3;
        this.ammoType = AmmoType.T_STINGRAY;
        this.shortRange = 7;
        this.mediumRange = 14;
        this.longRange = 21;
        this.extremeRange = 28;
        this.tonnage = 120.0f;
        this.bv = 496;
        this.cost = 85000;
        this.shortAV = 3.5;
        this.medAV = 3.5;
        this.maxRange = RANGE_MED;
    }

    @Override
    protected AttackHandler getCorrectHandler(ToHitData toHit, WeaponAttackAction waa, IGame game, Server server) {
        return new StingrayHandler(toHit, waa, game, server);
    }
}
