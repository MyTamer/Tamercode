package org.gamenet.application.mm8leveleditor.data.mm7.fileFormat;

import java.util.ArrayList;
import java.util.List;
import org.gamenet.application.mm8leveleditor.data.DObjListBin;
import org.gamenet.swing.controls.ComparativeTableControl;
import org.gamenet.util.ByteConversions;

public class DObjListBinMM7 implements DObjListBin {

    private static final int DOBJLIST_BIN_RECORD_SIZE = 56;

    private static final int ATTRIBUTE_NO_SPRITE = 0x0001;

    private static final int ATTRIBUTE_NO_EFFECT_ON_MOVEMENT = 0x0002;

    private static final int ATTRIBUTE_LIMITED_LIFESPAN = 0x0004;

    private static final int ATTRIBUTE_LIFESPAN_BASED_ON_SPRITE_ANIMATION_PROPERTY = 0x0008;

    private static final int ATTRIBUTE_FIXED_IN_PLACE = 0x0010;

    private static final int ATTRIBUTE_NOT_AFFECTED_BY_GRAVITY = 0x0020;

    private static final int ATTRIBUTE_ACTION_ON_CONTACT = 0x0040;

    private static final int ATTRIBUTE_BOUNCES = 0x0080;

    private static final int ATTRIBUTE_TRAILS_BITS = 0x0100;

    private static final int ATTRIBUTE_MM7_EMITS_FIRE = 0x0200;

    private static final int ATTRIBUTE_MM7_LINE_OF_BITS = 0x0400;

    private int dObjListBinOffset = 0;

    private byte dObjListBinData[] = null;

    public DObjListBinMM7() {
        super();
    }

    public DObjListBinMM7(String fileName) {
        super();
        this.dObjListBinData = new byte[DOBJLIST_BIN_RECORD_SIZE];
    }

    public int initialize(byte dataSrc[], int offset) {
        this.dObjListBinOffset = offset;
        this.dObjListBinData = new byte[DOBJLIST_BIN_RECORD_SIZE];
        System.arraycopy(dataSrc, offset, this.dObjListBinData, 0, this.dObjListBinData.length);
        offset += this.dObjListBinData.length;
        return offset;
    }

    public static boolean checkDataIntegrity(byte[] data, int offset, int expectedNewOffset) {
        int count = ByteConversions.getIntegerInByteArrayAtPosition(data, offset);
        offset += 4;
        offset += count * DOBJLIST_BIN_RECORD_SIZE;
        return (offset == expectedNewOffset);
    }

    public static int populateObjects(byte[] data, int offset, List dObjListBinList) {
        int dObjListBinCount = ByteConversions.getIntegerInByteArrayAtPosition(data, offset);
        offset += 4;
        for (int dObjListBinIndex = 0; dObjListBinIndex < dObjListBinCount; ++dObjListBinIndex) {
            DObjListBinMM7 dObjListBin = new DObjListBinMM7();
            dObjListBinList.add(dObjListBin);
            offset = dObjListBin.initialize(data, offset);
        }
        return offset;
    }

    public static int updateData(byte[] newData, int offset, List dObjListBinList) {
        ByteConversions.setIntegerInByteArrayAtPosition(dObjListBinList.size(), newData, offset);
        offset += 4;
        for (int dObjListBinIndex = 0; dObjListBinIndex < dObjListBinList.size(); ++dObjListBinIndex) {
            DObjListBinMM7 dObjListBin = (DObjListBinMM7) dObjListBinList.get(dObjListBinIndex);
            System.arraycopy(dObjListBin.getDObjListBinData(), 0, newData, offset, dObjListBin.getDObjListBinData().length);
            offset += dObjListBin.getDObjListBinData().length;
        }
        return offset;
    }

    public byte[] getDObjListBinData() {
        return this.dObjListBinData;
    }

    public int getDObjListBinOffset() {
        return this.dObjListBinOffset;
    }

    public static int getRecordSize() {
        return DOBJLIST_BIN_RECORD_SIZE;
    }

    public static List getOffsetList() {
        List offsetList = new ArrayList();
        offsetList.add(new ComparativeTableControl.OffsetData(0, 32, ComparativeTableControl.REPRESENTATION_STRING, "Name"));
        offsetList.add(new ComparativeTableControl.OffsetData(32, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "type"));
        offsetList.add(new ComparativeTableControl.OffsetData(34, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "radius"));
        offsetList.add(new ComparativeTableControl.OffsetData(36, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "height"));
        offsetList.add(new ComparativeTableControl.OffsetData(38, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "attributes"));
        offsetList.add(new ComparativeTableControl.OffsetData(40, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "Dsft ID #"));
        offsetList.add(new ComparativeTableControl.OffsetData(42, 8, ComparativeTableControl.REPRESENTATION_BYTE_DEC, "16-bit color"));
        offsetList.add(new ComparativeTableControl.OffsetData(50, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "speed"));
        offsetList.add(new ComparativeTableControl.OffsetData(52, 4, ComparativeTableControl.REPRESENTATION_BYTE_DEC, "4-bit color"));
        return offsetList;
    }

    public static ComparativeTableControl.DataSource getComparativeDataSource(final List dObjListBinList) {
        return new ComparativeTableControl.DataSource() {

            public int getRowCount() {
                return dObjListBinList.size();
            }

            public byte[] getData(int dataRow) {
                DObjListBinMM7 dObjListBin = (DObjListBinMM7) dObjListBinList.get(dataRow);
                return dObjListBin.getDObjListBinData();
            }

            public int getAdjustedDataRowIndex(int dataRow) {
                return dataRow;
            }

            public String getIdentifier(int dataRow) {
                return "";
            }

            public int getOffset(int dataRow) {
                return 0;
            }
        };
    }
}
