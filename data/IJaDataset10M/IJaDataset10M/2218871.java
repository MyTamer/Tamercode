package synthdrivers.RolandJV80;

import javax.swing.JOptionPane;
import core.BankDriver;
import core.DriverUtil;
import core.Patch;

/**
 * @author Sander Brandenburg
 * @version $Id: RolandJV80BankDriver.java 851 2005-01-27 07:36:36Z jbackhaus $
 */
public class RolandJV80BankDriver extends BankDriver {

    final RolandJV80PatchDriver patchDriver;

    static final int PATCHES_PER_BANK = 64;

    public RolandJV80BankDriver(RolandJV80PatchDriver patchDriver) {
        super("Bank", "Sander Brandenburg", PATCHES_PER_BANK, 4);
        bankNumbers = JV80Constants.BANKS;
        deviceIDoffset = JV80Constants.DEVICEIDOFFSET;
        patchNameStart = JV80Constants.PATCH_NAME_START;
        patchNameSize = JV80Constants.PATCH_NAME_SIZE;
        patchNumbers = DriverUtil.generateNumbers(1, PATCHES_PER_BANK, "00");
        singleSysexID = JV80Constants.SYSEXID;
        sysexID = JV80Constants.SYSEXID;
        this.patchDriver = patchDriver;
    }

    void setup() {
        singleSize = patchDriver.getPatchSize();
        patchSize = PATCHES_PER_BANK * singleSize;
    }

    RolandJV80PatchDriver getPatchDriver() {
        return patchDriver;
    }

    public void calculateChecksum(Patch p) {
        for (int i = 0; i < PATCHES_PER_BANK; i++) {
            patchDriver.calculateChecksum(p, getStartOffset(i));
        }
    }

    public Patch createNewPatch() {
        byte[] bankSysex = new byte[patchSize];
        Patch patch = patchDriver.createNewPatch();
        for (int i = 0; i < PATCHES_PER_BANK; i++) {
            patchDriver.setPatchNum(patch.sysex, 0, i);
            System.arraycopy(patch.sysex, 0, bankSysex, i * singleSize, patch.sysex.length);
        }
        Patch bankPatch = new Patch(bankSysex, this);
        calculateChecksum(bankPatch);
        return bankPatch;
    }

    public Patch getPatch(Patch bank, int patchNum) {
        byte[] sysex = new byte[singleSize];
        System.arraycopy(bank.sysex, getStartOffset(patchNum), sysex, 0, sysex.length);
        return new Patch(sysex, patchDriver);
    }

    public String getPatchName(Patch p, int patchNum) {
        int offset = getStartOffset(patchNum);
        return new String(p.sysex, offset + patchNameStart, patchNameSize);
    }

    public int getStartOffset(int patchnr) {
        return patchnr * singleSize;
    }

    public void putPatch(Patch bank, Patch p, int patchNum) {
        if (!canHoldPatch(p)) {
            JOptionPane.showMessageDialog(null, "This type of patch does not fit in to this type of bank.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int offset = getStartOffset(patchNum);
        System.arraycopy(p.sysex, 0, bank.sysex, offset, p.sysex.length);
        patchDriver.calculateChecksum(bank, offset);
    }

    public void requestPatchDump(final int bankNum, int patchNum) {
        new Thread() {

            public void run() {
                setPriority(MIN_PRIORITY);
                for (int i = 0; i < PATCHES_PER_BANK; i++) {
                    patchDriver.requestPatchDump(bankNum, i);
                    try {
                        Thread.sleep(80);
                    } catch (Exception ignore) {
                    }
                }
            }
        }.start();
    }

    public void setPatchNum(byte[] sysex, int bankNum) {
        for (int i = 0; i < PATCHES_PER_BANK; i++) {
            patchDriver.setPatchNum(sysex, getStartOffset(i), bankNum, i);
        }
    }

    public void setPatchName(Patch p, int patchNum, String name) {
        int offset = getStartOffset(patchNum);
        byte data[] = name.getBytes();
        if (data.length > patchNameSize) return;
        System.arraycopy(data, 0, p.sysex, offset + patchNameStart, data.length);
    }

    public void storePatch(Patch p, int bankNum, int patchNum) {
        setPatchNum(p.sysex, bankNum);
        calculateChecksum(p);
        sendPatchWorker(p);
    }

    public void deletePatch(Patch bank, int patchNum) {
        Patch patch = patchDriver.createNewPatch();
        int offset = getStartOffset(patchNum);
        System.arraycopy(patch.sysex, 0, bank.sysex, offset, patch.sysex.length);
    }
}
