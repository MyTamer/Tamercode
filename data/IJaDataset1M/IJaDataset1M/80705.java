package net.sourceforge.poi.hssf.record;

import net.sourceforge.poi.util.LittleEndian;

/**
 * Title:        Refresh All Record <P>
 * Description:  Flag whether to refresh all external data when loading a sheet.
 *               (which hssf doesn't support anyhow so who really cares?)<P>
 * Copyright:    Copyright (c) 2001 SuperLink Software, Inc. <P>
 * REFERENCE:  PG 376 Microsoft Excel 97 Developer's Kit (ISBN: 1-57231-498-2)<P>
 * @author Andrew C. Oliver (andycoliver@excite.com)
 * @version 2.0-pre
 */
public class RefreshAllRecord extends Record {

    public static final short sid = 0x1B7;

    private short field_1_refreshall;

    public RefreshAllRecord() {
    }

    /**
     * Constructs a RefreshAll record and sets its fields appropriately.
     *
     * @param short id must be 0x187 or an exception will be throw upon validation
     * @param short size the size of the data area of the record
     * @param byte[] data of the record (should not contain sid/len)
     */
    public RefreshAllRecord(short id, short size, byte[] data) {
        super(id, size, data);
    }

    /**
     * Constructs a RefreshAll record and sets its fields appropriately.
     *
     * @param short id must be 0x187 or an exception will be throw upon validation
     * @param short size the size of the data area of the record
     * @param byte[] data of the record (should not contain sid/len)
     * @param offset of the record data
     */
    public RefreshAllRecord(short id, short size, byte[] data, int offset) {
        super(id, size, data, offset);
    }

    protected void validateSid(short id) {
        if (id != sid) {
            throw new RecordFormatException("NOT A REFRESHALL RECORD");
        }
    }

    protected void fillFields(byte[] data, short size, int offset) {
        field_1_refreshall = LittleEndian.getShort(data, 0 + offset);
    }

    /**
     * set whether to refresh all external data when loading a sheet
     * @param refreshall or not
     */
    public void setRefreshAll(boolean refreshall) {
        if (refreshall) {
            field_1_refreshall = 1;
        } else {
            field_1_refreshall = 0;
        }
    }

    /**
     * get whether to refresh all external data when loading a sheet
     * @return refreshall or not
     */
    public boolean getRefreshAll() {
        return (field_1_refreshall == 1);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[REFRESHALL]\n");
        buffer.append("    .refreshall      = ").append(getRefreshAll()).append("\n");
        buffer.append("[/REFRESHALL]\n");
        return buffer.toString();
    }

    public int serialize(int offset, byte[] data) {
        LittleEndian.putShort(data, 0 + offset, sid);
        LittleEndian.putShort(data, 2 + offset, ((short) 0x02));
        LittleEndian.putShort(data, 4 + offset, field_1_refreshall);
        return getRecordSize();
    }

    public int getRecordSize() {
        return 6;
    }

    public short getSid() {
        return this.sid;
    }
}
