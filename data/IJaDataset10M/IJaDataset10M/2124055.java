package megamek.common;

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author  Ben
 * @version
 */
public class MtfFile implements MechLoader {

    String version;

    String name;

    String model;

    String chassisConfig;

    String techBase;

    String techYear;

    String rulesLevel;

    String tonnage;

    String engine;

    String internalType;

    String myomerType;

    String heatSinks;

    String walkMP;

    String jumpMP;

    String armorType;

    String larmArmor;

    String rarmArmor;

    String ltArmor;

    String rtArmor;

    String ctArmor;

    String headArmor;

    String llegArmor;

    String rlegArmor;

    String ltrArmor;

    String rtrArmor;

    String ctrArmor;

    String weaponCount;

    String[] weaponData;

    String[][] critData;

    Hashtable hSharedEquip = new Hashtable();

    Vector vSplitWeapons = new Vector();

    /** Creates new MtfFile */
    public MtfFile(InputStream is) throws EntityLoadingException {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            version = r.readLine();
            name = r.readLine();
            model = r.readLine();
            r.readLine();
            chassisConfig = r.readLine();
            techBase = r.readLine();
            techYear = r.readLine();
            rulesLevel = r.readLine();
            r.readLine();
            tonnage = r.readLine();
            engine = r.readLine();
            internalType = r.readLine();
            myomerType = r.readLine();
            r.readLine();
            heatSinks = r.readLine();
            walkMP = r.readLine();
            jumpMP = r.readLine();
            r.readLine();
            armorType = r.readLine();
            larmArmor = r.readLine();
            rarmArmor = r.readLine();
            ltArmor = r.readLine();
            rtArmor = r.readLine();
            ctArmor = r.readLine();
            headArmor = r.readLine();
            llegArmor = r.readLine();
            rlegArmor = r.readLine();
            ltrArmor = r.readLine();
            rtrArmor = r.readLine();
            ctrArmor = r.readLine();
            r.readLine();
            weaponCount = r.readLine();
            int a = 9;
            int weapons = Integer.parseInt(weaponCount.substring(8));
            weaponData = new String[weapons];
            for (int i = 0; i < weapons; i++) {
                weaponData[i] = r.readLine();
            }
            critData = new String[8][12];
            readCrits(r, Mech.LOC_LARM);
            readCrits(r, Mech.LOC_RARM);
            readCrits(r, Mech.LOC_LT);
            readCrits(r, Mech.LOC_RT);
            readCrits(r, Mech.LOC_CT);
            readCrits(r, Mech.LOC_HEAD);
            readCrits(r, Mech.LOC_LLEG);
            readCrits(r, Mech.LOC_RLEG);
            r.close();
        } catch (IOException ex) {
            throw new EntityLoadingException("I/O Error reading file");
        } catch (StringIndexOutOfBoundsException ex) {
            throw new EntityLoadingException("StringIndexOutOfBoundsException reading file (format error)");
        } catch (NumberFormatException ex) {
            throw new EntityLoadingException("NumberFormatException reading file (format error)");
        }
    }

    private void readCrits(BufferedReader r, int loc) throws IOException {
        r.readLine();
        r.readLine();
        for (int i = 0; i < 12; i++) {
            critData[loc][i] = r.readLine();
        }
    }

    public Entity getEntity() throws EntityLoadingException {
        try {
            Mech mech;
            if (chassisConfig.indexOf("Quad") != -1) {
                mech = new QuadMech();
            } else {
                mech = new BipedMech();
            }
            if (!version.trim().equalsIgnoreCase("Version:1.0")) {
                throw new EntityLoadingException("Wrong MTF file version.");
            }
            int pindex = name.indexOf("(");
            if (pindex == -1) {
                mech.setChassis(name.trim());
            } else {
                mech.setChassis(name.substring(0, pindex - 1).trim());
            }
            mech.setModel(model.trim());
            mech.setYear(Integer.parseInt(this.techYear.substring(4).trim()));
            if (chassisConfig.indexOf("Omni") != -1) {
                mech.setOmni(true);
            }
            boolean innerSphere = "Inner Sphere".equals(this.techBase.substring(9).trim());
            switch(Integer.parseInt(rulesLevel.substring(12).trim())) {
                case 1:
                    mech.setTechLevel(TechConstants.T_IS_LEVEL_1);
                    break;
                case 2:
                    mech.setTechLevel(innerSphere ? TechConstants.T_IS_LEVEL_2 : TechConstants.T_CLAN_LEVEL_2);
                    break;
                default:
                    throw new EntityLoadingException("Unsupported tech base and/or level: " + this.techBase.substring(9) + " (level " + this.rulesLevel.substring(12) + ")");
            }
            mech.weight = (float) Integer.parseInt(tonnage.substring(5));
            mech.setOriginalWalkMP(Integer.parseInt(walkMP.substring(8)));
            mech.setOriginalJumpMP(Integer.parseInt(jumpMP.substring(8)));
            boolean dblSinks = heatSinks.substring(14).equalsIgnoreCase("Double");
            int expectedSinks = Integer.parseInt(heatSinks.substring(11, 14).trim());
            mech.autoSetInternal();
            mech.initializeArmor(Integer.parseInt(larmArmor.substring(9)), Mech.LOC_LARM);
            mech.initializeArmor(Integer.parseInt(rarmArmor.substring(9)), Mech.LOC_RARM);
            mech.initializeArmor(Integer.parseInt(ltArmor.substring(9)), Mech.LOC_LT);
            mech.initializeArmor(Integer.parseInt(rtArmor.substring(9)), Mech.LOC_RT);
            mech.initializeArmor(Integer.parseInt(ctArmor.substring(9)), Mech.LOC_CT);
            mech.initializeArmor(Integer.parseInt(headArmor.substring(9)), Mech.LOC_HEAD);
            mech.initializeArmor(Integer.parseInt(llegArmor.substring(9)), Mech.LOC_LLEG);
            mech.initializeArmor(Integer.parseInt(rlegArmor.substring(9)), Mech.LOC_RLEG);
            mech.initializeRearArmor(Integer.parseInt(ltrArmor.substring(10)), Mech.LOC_LT);
            mech.initializeRearArmor(Integer.parseInt(rtrArmor.substring(10)), Mech.LOC_RT);
            mech.initializeRearArmor(Integer.parseInt(ctrArmor.substring(10)), Mech.LOC_CT);
            compactCriticals(mech);
            for (int i = mech.locations() - 1; i >= 0; i--) {
                parseCrits(mech, i);
            }
            if (mech.isClan()) {
                mech.addClanCase();
            }
            mech.addEngineSinks(expectedSinks - mech.heatSinks(), dblSinks);
            return mech;
        } catch (NumberFormatException ex) {
            throw new EntityLoadingException("NumberFormatException parsing file");
        } catch (NullPointerException ex) {
            throw new EntityLoadingException("NullPointerException parsing file");
        } catch (StringIndexOutOfBoundsException ex) {
            throw new EntityLoadingException("StringIndexOutOfBoundsException parsing file");
        }
    }

    private void parseCrits(Mech mech, int loc) throws EntityLoadingException {
        if (!(mech instanceof QuadMech)) {
            if (loc == Mech.LOC_LARM || loc == Mech.LOC_RARM) {
                if (!critData[loc][3].equals("Hand Actuator")) {
                    mech.setCritical(loc, 3, null);
                }
                if (!critData[loc][2].equals("Lower Arm Actuator")) {
                    mech.setCritical(loc, 2, null);
                }
            }
        }
        for (int i = 0; i < mech.getNumberOfCriticals(loc); i++) {
            if (mech.getCritical(loc, i) != null) {
                continue;
            }
            String critName = critData[loc][i];
            boolean rearMounted = false;
            boolean split = false;
            if (critName.equalsIgnoreCase("Fusion Engine")) {
                mech.setCritical(loc, i, new CriticalSlot(CriticalSlot.TYPE_SYSTEM, 3));
                continue;
            }
            if (critName.endsWith("(R)")) {
                rearMounted = true;
                critName = critName.substring(0, critName.length() - 3).trim();
            }
            if (critName.endsWith("(Split)")) {
                split = true;
                critName = critName.substring(0, critName.length() - 7).trim();
            }
            try {
                EquipmentType etype = EquipmentType.getByMtfName(critName);
                if (etype != null) {
                    if (etype.isSpreadable()) {
                        Mounted m = (Mounted) hSharedEquip.get(etype);
                        if (m != null) {
                            mech.addCritical(loc, new CriticalSlot(CriticalSlot.TYPE_EQUIPMENT, mech.getEquipmentNum(m), etype.isHittable()));
                            continue;
                        } else {
                            m = mech.addEquipment(etype, loc, rearMounted);
                            hSharedEquip.put(etype, m);
                        }
                    } else if (split) {
                        Mounted m = null;
                        boolean bFound = false;
                        for (int x = 0, n = vSplitWeapons.size(); x < n; x++) {
                            m = (Mounted) vSplitWeapons.elementAt(x);
                            int nLoc = m.getLocation();
                            if ((nLoc == loc || loc == Mech.getInnerLocation(nLoc)) && m.getType() == etype) {
                                bFound = true;
                                break;
                            }
                        }
                        if (bFound) {
                            m.setFoundCrits(m.getFoundCrits() + 1);
                            if (m.getFoundCrits() >= etype.getCriticals(mech)) {
                                vSplitWeapons.removeElement(m);
                            }
                            m.setLocation(Mech.mostRestrictiveLoc(loc, m.getLocation()));
                        } else {
                            m = new Mounted(mech, etype);
                            m.setSplit(true);
                            m.setFoundCrits(1);
                            mech.addEquipment(m, loc, rearMounted);
                            vSplitWeapons.addElement(m);
                        }
                        mech.addCritical(loc, new CriticalSlot(CriticalSlot.TYPE_EQUIPMENT, mech.getEquipmentNum(m), etype.isHittable()));
                    } else {
                        mech.addEquipment(etype, loc, rearMounted);
                    }
                } else {
                    if (!critName.equals("-Empty-")) {
                        System.out.print("MtfFile: unknown critical ");
                        System.out.println(critName);
                        critData[loc][i] = "-Empty-";
                        compactCriticals(mech, loc);
                        i--;
                    }
                }
            } catch (LocationFullException ex) {
                throw new EntityLoadingException(ex.getMessage());
            }
        }
    }

    /**
     * This function moves all "empty" slots to the end of a location's
     * critical list.
     *
     * MegaMek adds equipment to the first empty slot available in a
     * location.  This means that any "holes" (empty slots not at the
     * end of a location), will cause the file crits and MegaMek's crits
     * to become out of sync.
     */
    private void compactCriticals(Mech mech) {
        for (int loc = 0; loc < mech.locations(); loc++) {
            compactCriticals(mech, loc);
        }
    }

    private void compactCriticals(Mech mech, int loc) {
        if (loc == Mech.LOC_HEAD) {
            return;
        }
        int firstEmpty = -1;
        for (int slot = 0; slot < mech.getNumberOfCriticals(loc); slot++) {
            if (critData[loc][slot].equals("-Empty-")) {
                firstEmpty = slot;
            }
            if (firstEmpty != -1 && !critData[loc][slot].equals("-Empty-")) {
                critData[loc][firstEmpty] = critData[loc][slot];
                critData[loc][slot] = "-Empty-";
                slot = firstEmpty;
                firstEmpty = -1;
            }
        }
    }
}
