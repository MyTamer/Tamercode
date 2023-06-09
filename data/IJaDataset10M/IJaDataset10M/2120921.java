package megamek.common.weapons;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import megamek.common.AmmoType;
import megamek.common.BattleArmor;
import megamek.common.Compute;
import megamek.common.Coords;
import megamek.common.Entity;
import megamek.common.IGame;
import megamek.common.Infantry;
import megamek.common.Mech;
import megamek.common.Minefield;
import megamek.common.MiscType;
import megamek.common.Mounted;
import megamek.common.RangeType;
import megamek.common.Report;
import megamek.common.Tank;
import megamek.common.Targetable;
import megamek.common.ToHitData;
import megamek.common.WeaponType;
import megamek.common.actions.WeaponAttackAction;
import megamek.server.Server;

/**
 * @author Sebastian Brocks
 */
public class ATMHandler extends MissileWeaponHandler {

    /**
     *
     */
    private static final long serialVersionUID = -2536312899803153911L;

    /**
     * @param t
     * @param w
     * @param g
     * @param s
     */
    public ATMHandler(ToHitData t, WeaponAttackAction w, IGame g, Server s) {
        super(t, w, g, s);
    }

    @Override
    protected int calcDamagePerHit() {
        double toReturn;
        AmmoType atype = (AmmoType) ammo.getType();
        if (atype.getMunitionType() == AmmoType.M_HIGH_EXPLOSIVE) {
            sSalvoType = " high-explosive missile(s) ";
            toReturn = 3;
        } else if (atype.getMunitionType() == AmmoType.M_EXTENDED_RANGE) {
            sSalvoType = " extended-range missile(s) ";
            toReturn = 1;
        } else {
            toReturn = 2;
        }
        if ((target instanceof Infantry) && !(target instanceof BattleArmor)) {
            toReturn = Compute.directBlowInfantryDamage(wtype.getRackSize() * toReturn, bDirect ? toHit.getMoS() / 3 : 0, wtype.getInfantryDamageClass(), ((Infantry) target).isMechanized());
            if (bGlancing) {
                toReturn /= 2;
            }
        }
        return (int) toReturn;
    }

    @Override
    protected int calcnCluster() {
        return 5;
    }

    @Override
    protected int calcHits(Vector<Report> vPhaseReport) {
        if ((target instanceof Infantry) && !(target instanceof BattleArmor)) {
            return 1;
        }
        int hits;
        AmmoType atype = (AmmoType) ammo.getType();
        if (atype.getMunitionType() == AmmoType.M_HIGH_EXPLOSIVE) {
            hits = super.calcHits(vPhaseReport);
        } else {
            hits = calcStandardAndExtendedAmmoHits(vPhaseReport);
        }
        hits = nDamPerHit * hits;
        nDamPerHit = 1;
        return hits;
    }

    /**
     * Calculate the attack value based on range
     *
     * @return an <code>int</code> representing the attack value at that range.
     */
    @Override
    protected int calcAttackValue() {
        int av = 0;
        int range = RangeType.rangeBracket(nRange, wtype.getATRanges(), true);
        AmmoType atype = (AmmoType) ammo.getType();
        if (atype.getMunitionType() == AmmoType.M_HIGH_EXPLOSIVE) {
            if (range == WeaponType.RANGE_SHORT) {
                av = wtype.getRoundShortAV();
                av = av + av / 2;
            }
        } else if (atype.getMunitionType() == AmmoType.M_EXTENDED_RANGE) {
            if (range == WeaponType.RANGE_SHORT) {
                av = wtype.getRoundShortAV();
            } else if (range == WeaponType.RANGE_MED) {
                av = wtype.getRoundMedAV();
            } else if (range == WeaponType.RANGE_LONG) {
                av = wtype.getRoundLongAV();
            } else if (range == WeaponType.RANGE_EXT) {
                av = wtype.getRoundLongAV();
            }
            av = av / 2;
        } else {
            if (range == WeaponType.RANGE_SHORT) {
                av = wtype.getRoundShortAV();
            } else if (range == WeaponType.RANGE_MED) {
                av = wtype.getRoundMedAV();
            } else if (range == WeaponType.RANGE_LONG) {
                av = wtype.getRoundLongAV();
            } else if (range == WeaponType.RANGE_EXT) {
                av = wtype.getRoundExtAV();
            }
        }
        return av;
    }

    protected int calcStandardAndExtendedAmmoHits(Vector<Report> vPhaseReport) {
        if ((target instanceof Infantry) && !(target instanceof BattleArmor)) {
            if (ae instanceof BattleArmor) {
                bSalvo = true;
                Report r = new Report(3325);
                r.subject = subjectId;
                r.add(wtype.getRackSize() * ((BattleArmor) ae).getShootingStrength());
                r.add(sSalvoType);
                r.add(toHit.getTableDesc());
                vPhaseReport.add(r);
                return ((BattleArmor) ae).getShootingStrength();
            }
            Report r = new Report(3325);
            r.subject = subjectId;
            r.add(wtype.getRackSize());
            r.add(sSalvoType);
            r.add(toHit.getTableDesc());
            vPhaseReport.add(r);
            return 1;
        }
        Entity entityTarget = (target.getTargetType() == Targetable.TYPE_ENTITY) ? (Entity) target : null;
        int missilesHit;
        int nMissilesModifier = nSalvoBonus;
        if (game.getOptions().booleanOption("tacops_range") && (nRange > wtype.getRanges(weapon)[RangeType.RANGE_LONG])) {
            nMissilesModifier -= 2;
        }
        boolean bMekTankStealthActive = false;
        if ((ae instanceof Mech) || (ae instanceof Tank)) {
            bMekTankStealthActive = ae.isStealthActive();
        }
        Mounted mLinker = weapon.getLinkedBy();
        AmmoType atype = (AmmoType) ammo.getType();
        boolean bECMAffected = false;
        if (Compute.isAffectedByECM(ae, ae.getPosition(), target.getPosition())) {
            bECMAffected = true;
        }
        if (((mLinker != null) && (mLinker.getType() instanceof MiscType) && !mLinker.isDestroyed() && !mLinker.isMissing() && !mLinker.isBreached() && mLinker.getType().hasFlag(MiscType.F_ARTEMIS)) && (atype.getMunitionType() == AmmoType.M_ARTEMIS_CAPABLE)) {
            if (bECMAffected) {
                Report r = new Report(3330);
                r.subject = subjectId;
                r.newlines = 0;
                vPhaseReport.addElement(r);
            } else if (bMekTankStealthActive) {
                Report r = new Report(3335);
                r.subject = subjectId;
                r.newlines = 0;
                vPhaseReport.addElement(r);
            } else {
                nMissilesModifier += 2;
            }
        } else if (atype.getAmmoType() == AmmoType.T_ATM) {
            if (bECMAffected) {
                Report r = new Report(3330);
                r.subject = subjectId;
                r.newlines = 0;
                vPhaseReport.addElement(r);
            } else if (bMekTankStealthActive) {
                Report r = new Report(3335);
                r.subject = subjectId;
                r.newlines = 0;
                vPhaseReport.addElement(r);
            } else {
                nMissilesModifier += 2;
            }
        } else if ((entityTarget != null) && (entityTarget.isNarcedBy(ae.getOwner().getTeam()) || entityTarget.isINarcedBy(ae.getOwner().getTeam()))) {
            boolean bTargetECMAffected = false;
            bTargetECMAffected = Compute.isAffectedByECM(ae, target.getPosition(), target.getPosition());
            if (((atype.getAmmoType() == AmmoType.T_LRM) || (atype.getAmmoType() == AmmoType.T_SRM)) || ((atype.getAmmoType() == AmmoType.T_MML) && (atype.getMunitionType() == AmmoType.M_NARC_CAPABLE) && ((weapon.curMode() == null) || !weapon.curMode().equals("Indirect")))) {
                if (bTargetECMAffected) {
                    Report r = new Report(3330);
                    r.subject = subjectId;
                    r.newlines = 0;
                    vPhaseReport.addElement(r);
                } else {
                    nMissilesModifier += 2;
                }
            }
        }
        if (bGlancing) {
            nMissilesModifier -= 4;
        }
        if (bDirect) {
            nMissilesModifier += (toHit.getMoS() / 3) * 2;
        }
        if (game.getPlanetaryConditions().hasEMI()) {
            nMissilesModifier -= 2;
        }
        nMissilesModifier += getAMSHitsMod(vPhaseReport);
        if (allShotsHit()) {
            missilesHit = wtype.getRackSize();
        } else {
            if (ae instanceof BattleArmor) {
                missilesHit = Compute.missilesHit(wtype.getRackSize() * ((BattleArmor) ae).getShootingStrength(), nMissilesModifier, weapon.isHotLoaded(), false, advancedAMS && amsEnganged);
            } else {
                missilesHit = Compute.missilesHit(wtype.getRackSize(), nMissilesModifier, weapon.isHotLoaded(), false, advancedAMS && amsEnganged);
            }
        }
        if (missilesHit > 0) {
            Report r = new Report(3325);
            r.subject = subjectId;
            r.add(missilesHit);
            r.add(sSalvoType);
            r.add(toHit.getTableDesc());
            r.newlines = 0;
            vPhaseReport.addElement(r);
            if (nMissilesModifier != 0) {
                if (nMissilesModifier > 0) {
                    r = new Report(3340);
                } else {
                    r = new Report(3341);
                }
                r.subject = subjectId;
                r.add(nMissilesModifier);
                r.newlines = 0;
                vPhaseReport.addElement(r);
            }
        }
        Report r = new Report(3345);
        r.subject = subjectId;
        vPhaseReport.addElement(r);
        bSalvo = true;
        return missilesHit;
    }

    @Override
    protected boolean specialResolution(Vector<Report> vPhaseReport, Entity entityTarget) {
        if (!bMissed && (target.getTargetType() == Targetable.TYPE_MINEFIELD_CLEAR)) {
            Report r = new Report(3255);
            r.indent(1);
            r.subject = subjectId;
            vPhaseReport.addElement(r);
            Coords coords = target.getPosition();
            Enumeration<Minefield> minefields = game.getMinefields(coords).elements();
            ArrayList<Minefield> mfRemoved = new ArrayList<Minefield>();
            while (minefields.hasMoreElements()) {
                Minefield mf = minefields.nextElement();
                if (server.clearMinefield(mf, ae, Minefield.CLEAR_NUMBER_WEAPON, vPhaseReport)) {
                    mfRemoved.add(mf);
                }
            }
            for (Minefield mf : mfRemoved) {
                server.removeMinefield(mf);
            }
            return true;
        }
        return false;
    }
}
