package megamek.common.loaders;

import java.util.Vector;
import megamek.common.BipedMech;
import megamek.common.CriticalSlot;
import megamek.common.Engine;
import megamek.common.Entity;
import megamek.common.EquipmentType;
import megamek.common.LocationFullException;
import megamek.common.Mech;
import megamek.common.MiscType;
import megamek.common.Mounted;
import megamek.common.QuadMech;
import megamek.common.TechConstants;
import megamek.common.WeaponType;
import megamek.common.util.BuildingBlock;

public class BLKMechFile extends BLKFile implements IMechLoader {

    public static final int HD = 0;

    public static final int LA = 1;

    public static final int LF = 2;

    public static final int LB = 3;

    public static final int CF = 4;

    public static final int CB = 5;

    public static final int RF = 6;

    public static final int RB = 7;

    public static final int RA = 8;

    public static final int LL = 9;

    public static final int RL = 10;

    public static final int CT = 4;

    public static final int RT = 6;

    public static final int LT = 2;

    public BLKMechFile(BuildingBlock bb) {
        dataFile = bb;
    }

    @SuppressWarnings("unchecked")
    public Entity getEntity() throws EntityLoadingException {
        int chassisType = 0;
        if (!dataFile.exists("chassis_type")) {
            chassisType = 0;
        } else {
            chassisType = dataFile.getDataAsInt("chassis_type")[0];
        }
        Mech mech = null;
        if (chassisType == 1) {
            mech = new QuadMech();
        } else {
            mech = new BipedMech();
        }
        if (!dataFile.exists("name")) {
            throw new EntityLoadingException("Could not find block.");
        }
        mech.setChassis(dataFile.getDataAsString("Name")[0]);
        if (!dataFile.exists("model")) {
            throw new EntityLoadingException("Could not find block.");
        }
        mech.setModel(dataFile.getDataAsString("Model")[0]);
        setTechLevel(mech);
        setFluff(mech);
        checkManualBV(mech);
        if (!dataFile.exists("tonnage")) {
            throw new EntityLoadingException("Could not find block.");
        }
        mech.setWeight(dataFile.getDataAsFloat("tonnage")[0]);
        int engineCode = BLKFile.FUSION;
        if (dataFile.exists("engine_type")) {
            engineCode = dataFile.getDataAsInt("engine_type")[0];
        }
        int engineFlags = 0;
        if (mech.isClan()) {
            engineFlags = Engine.CLAN_ENGINE;
        }
        if (!dataFile.exists("walkingMP")) {
            throw new EntityLoadingException("Could not find walkingMP block.");
        }
        int engineRating = dataFile.getDataAsInt("walkingMP")[0] * (int) mech.getWeight();
        mech.setEngine(new Engine(engineRating, BLKFile.translateEngineCode(engineCode), engineFlags));
        if (!dataFile.exists("jumpingMP")) {
            throw new EntityLoadingException("Could not find block.");
        }
        mech.setOriginalJumpMP(dataFile.getDataAsInt("jumpingMP")[0]);
        if (!dataFile.exists("heatsinks")) {
            throw new EntityLoadingException("Could not find block.");
        }
        mech.addEngineSinks(dataFile.getDataAsInt("heatsinks")[0], MiscType.F_HEAT_SINK);
        if (dataFile.exists("internal_type")) {
            mech.setStructureType(dataFile.getDataAsInt("internal_type")[0]);
        } else {
            mech.setStructureType(EquipmentType.T_STRUCTURE_STANDARD);
        }
        boolean patchworkArmor = false;
        if (dataFile.exists("armor_type")) {
            if (dataFile.getDataAsInt("armor_type")[0] == EquipmentType.T_ARMOR_PATCHWORK) {
                patchworkArmor = true;
            } else {
                mech.setArmorType(dataFile.getDataAsInt("armor_type")[0]);
            }
        } else {
            mech.setArmorType(EquipmentType.T_ARMOR_STANDARD);
        }
        if (!patchworkArmor && dataFile.exists("armor_tech")) {
            mech.setArmorTechLevel(dataFile.getDataAsInt("armor_tech")[0]);
        }
        if (patchworkArmor) {
            for (int i = 0; i < mech.locations(); i++) {
                mech.setArmorType(dataFile.getDataAsInt(mech.getLocationName(i) + "_armor_type")[0], i);
                mech.setArmorTechLevel(dataFile.getDataAsInt(mech.getLocationName(i) + "_armor_type")[0], i);
            }
        }
        if (!dataFile.exists("armor")) {
            throw new EntityLoadingException("Could not find block.");
        }
        int[] armor = new int[11];
        if (dataFile.getDataAsInt("armor").length < 11) {
            System.err.println("BLKMechFile->Read armor array doesn't match my armor array...");
            throw new EntityLoadingException("Could not find block.");
        }
        armor = dataFile.getDataAsInt("Armor");
        mech.initializeArmor(armor[BLKMechFile.HD], Mech.LOC_HEAD);
        mech.initializeArmor(armor[BLKMechFile.LA], Mech.LOC_LARM);
        mech.initializeArmor(armor[BLKMechFile.RA], Mech.LOC_RARM);
        mech.initializeArmor(armor[BLKMechFile.LL], Mech.LOC_LLEG);
        mech.initializeArmor(armor[BLKMechFile.RL], Mech.LOC_RLEG);
        mech.initializeArmor(armor[BLKMechFile.CF], Mech.LOC_CT);
        mech.initializeArmor(armor[BLKMechFile.LF], Mech.LOC_LT);
        mech.initializeArmor(armor[BLKMechFile.RF], Mech.LOC_RT);
        mech.initializeRearArmor(armor[BLKMechFile.CB], Mech.LOC_CT);
        mech.initializeRearArmor(armor[BLKMechFile.LB], Mech.LOC_LT);
        mech.initializeRearArmor(armor[BLKMechFile.RB], Mech.LOC_RT);
        if (!dataFile.exists("internal armor")) {
            mech.setInternal(3, (armor[CF] + armor[CB]) / 2, (armor[LF] + armor[LB]) / 2, (armor[LA] / 2), (armor[LL] / 2));
        } else {
            armor = dataFile.getDataAsInt("internal armor");
            mech.setInternal(armor[HD], armor[CT], armor[LT], armor[LA], armor[LL]);
        }
        if (!dataFile.getDataAsString("ra criticals")[2].trim().equalsIgnoreCase("Lower Arm Actuator")) {
            mech.removeCriticals(Mech.LOC_RARM, new CriticalSlot(CriticalSlot.TYPE_SYSTEM, Mech.ACTUATOR_LOWER_ARM));
        }
        if (!dataFile.getDataAsString("ra criticals")[3].trim().equalsIgnoreCase("Hand Actuator")) {
            mech.removeCriticals(Mech.LOC_RARM, new CriticalSlot(CriticalSlot.TYPE_SYSTEM, Mech.ACTUATOR_HAND));
        }
        if (!dataFile.getDataAsString("la criticals")[2].trim().equalsIgnoreCase("Lower Arm Actuator")) {
            mech.removeCriticals(Mech.LOC_LARM, new CriticalSlot(CriticalSlot.TYPE_SYSTEM, Mech.ACTUATOR_LOWER_ARM));
        }
        if (!dataFile.getDataAsString("la criticals")[3].trim().equalsIgnoreCase("Hand Actuator")) {
            mech.removeCriticals(Mech.LOC_LARM, new CriticalSlot(CriticalSlot.TYPE_SYSTEM, Mech.ACTUATOR_HAND));
        }
        Vector<String>[] criticals = new Vector[8];
        criticals[Mech.LOC_HEAD] = dataFile.getDataAsVector("hd criticals");
        criticals[Mech.LOC_LARM] = dataFile.getDataAsVector("la criticals");
        criticals[Mech.LOC_RARM] = dataFile.getDataAsVector("ra criticals");
        criticals[Mech.LOC_LLEG] = dataFile.getDataAsVector("ll criticals");
        criticals[Mech.LOC_RLEG] = dataFile.getDataAsVector("rl criticals");
        criticals[Mech.LOC_LT] = dataFile.getDataAsVector("lt criticals");
        criticals[Mech.LOC_RT] = dataFile.getDataAsVector("rt criticals");
        criticals[Mech.LOC_CT] = dataFile.getDataAsVector("ct criticals");
        String prefix;
        if (mech.getTechLevel() == TechConstants.T_CLAN_TW) {
            prefix = "Clan ";
        } else {
            prefix = "IS ";
        }
        for (int loc = 0; loc < criticals.length; loc++) {
            for (int c = 0; c < criticals[loc].size(); c++) {
                String critName = criticals[loc].get(c).toString().trim();
                boolean rearMounted = false;
                boolean turretMounted = false;
                boolean armored = false;
                if (critName.startsWith("(R) ")) {
                    rearMounted = true;
                    critName = critName.substring(4);
                }
                if (critName.startsWith("(T) ")) {
                    turretMounted = true;
                    critName = critName.substring(4);
                }
                if (critName.startsWith("(A) ")) {
                    armored = true;
                    critName = critName.substring(4);
                }
                int facing = -1;
                if (critName.startsWith("(FL) ")) {
                    facing = 5;
                    critName = critName.substring(5);
                }
                if (critName.startsWith("(RL) ")) {
                    facing = 5;
                    critName = critName.substring(4);
                }
                if (critName.startsWith("(FR) ")) {
                    facing = 5;
                    critName = critName.substring(1);
                }
                if (critName.startsWith("(RR) ")) {
                    facing = 5;
                    critName = critName.substring(2);
                }
                if (critName.indexOf("Engine") != -1) {
                    mech.setCritical(loc, c, new CriticalSlot(CriticalSlot.TYPE_SYSTEM, Mech.SYSTEM_ENGINE, true, armored, null));
                    continue;
                } else if (critName.equalsIgnoreCase("Life Support")) {
                    mech.setCritical(loc, c, new CriticalSlot(CriticalSlot.TYPE_SYSTEM, Mech.SYSTEM_LIFE_SUPPORT, true, armored, null));
                    continue;
                } else if (critName.equalsIgnoreCase("Sensors")) {
                    mech.setCritical(loc, c, new CriticalSlot(CriticalSlot.TYPE_SYSTEM, Mech.SYSTEM_SENSORS, true, armored, null));
                    continue;
                } else if (critName.equalsIgnoreCase("Cockpit")) {
                    mech.setCritical(loc, c, new CriticalSlot(CriticalSlot.TYPE_SYSTEM, Mech.SYSTEM_COCKPIT, true, armored, null));
                    continue;
                } else if (critName.equalsIgnoreCase("Gyro")) {
                    mech.setCritical(loc, c, new CriticalSlot(CriticalSlot.TYPE_SYSTEM, Mech.SYSTEM_GYRO, true, armored, null));
                    continue;
                }
                EquipmentType etype = EquipmentType.get(critName);
                if (etype == null) {
                    etype = EquipmentType.get(prefix + critName);
                }
                if (etype != null) {
                    try {
                        Mounted mount = mech.addEquipment(etype, loc, rearMounted, false, false, turretMounted);
                        if ((etype instanceof WeaponType) && etype.hasFlag(WeaponType.F_VGL)) {
                            if (facing == -1) {
                                if (rearMounted) {
                                    mount.setFacing(3);
                                } else {
                                    mount.setFacing(0);
                                }
                            } else {
                                mount.setFacing(facing);
                            }
                        }
                    } catch (LocationFullException ex) {
                        throw new EntityLoadingException(ex.getMessage());
                    }
                }
            }
        }
        if (mech.isClan()) {
            mech.addClanCase();
        }
        if (dataFile.exists("omni")) {
            mech.setOmni(true);
        }
        return mech;
    }
}
