package synthdrivers.RolandD10;

import static synthdrivers.RolandD10.D10Constants.*;
import synthdrivers.RolandD10.message.D10DataSetMessage;
import synthdrivers.RolandD10.message.D10RequestMessage;
import synthdrivers.RolandD10.message.D10TransferMessage;
import core.Driver;
import core.JSLFrame;
import core.Patch;

public class RolandD10PatchDriver extends Driver {

    public RolandD10PatchDriver() {
        super("Patch", "Roger Westerlund");
        sysexID = "F041**1612";
        patchSize = SIZE_HEADER_DT1 + PATCH_SIZE.getIntValue() + SIZE_TRAILER;
        deviceIDoffset = OFS_DEVICE_ID;
        checksumOffset = patchSize - SIZE_TRAILER;
        checksumStart = OFS_ADDRESS;
        checksumEnd = checksumOffset - 1;
        patchNameStart = SIZE_HEADER_DT1 + PATCH_NAME.getIntValue();
        patchNameSize = PATCH_NAME_SIZE.getIntValue();
        bankNumbers = new String[] {};
        patchNumbers = RolandD10Support.createPatchNumbers();
    }

    protected Patch createNewPatch() {
        D10TransferMessage message = new D10DataSetMessage(PATCH_SIZE, Entity.ZERO);
        Patch patch = new Patch(message.getBytes(), this);
        setPatchName(patch, "New Patch");
        calculateChecksum(patch);
        return patch;
    }

    public void requestPatchDump(int bankNum, int patchNumber) {
        D10RequestMessage message = new D10RequestMessage(Entity.createFromIntValue(patchNumber).multiply(PATCH_SIZE).add(BASE_PATCH_MEMORY), PATCH_SIZE);
        send(message.getBytes());
    }

    protected void sendPatch(Patch patch) {
        D10DataSetMessage message = new D10DataSetMessage(patch.sysex);
        message.setAddress(BASE_PATCH_TEMP_AREA);
        send(message.getBytes());
    }

    protected void storePatch(Patch patch, int bankNum, int patchNumber) {
        sendPatch(patch);
        D10DataSetMessage message = new D10DataSetMessage(2, BASE_WRITE_REQUEST.add(PATCH_WRITE_REQUEST).getDataValue());
        message.setData(0, (byte) patchNumber);
        message.setData(1, (byte) 0);
        send(message.getBytes());
    }

    public JSLFrame editPatch(Patch patch) {
        return new RolandD10PatchEditor(patch);
    }

    protected String getPatchName(Patch patch) {
        return RolandD10Support.trimName(super.getPatchName(patch));
    }
}
