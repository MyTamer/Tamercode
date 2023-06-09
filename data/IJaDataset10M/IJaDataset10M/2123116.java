package COM.winserver.wildcat;

import java.io.IOException;

public class TGuiConfDesc extends WcRecord {

    public int Number;

    public String Name;

    public String ConfSysop;

    public int MailType;

    public int HiMsg;

    public int HiMsgId;

    public int LoMsg;

    public int LoMsgId;

    public int LastRead;

    public int ConfAccess;

    public int ConfFlags;

    public int ValidNames;

    public int FirstUnread;

    public int ReadFlags;

    public static final int SIZE = 0 + 4 + 60 + 72 + 1 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4;

    public TGuiConfDesc() {
    }

    public TGuiConfDesc(byte[] x) {
        fromByteArray(x);
    }

    protected void writeTo(WcOutputStream out) throws IOException {
        super.writeTo(out);
        out.writeInt(Number);
        out.writeString(Name, 60);
        out.writeString(ConfSysop, 72);
        out.writeByte(MailType);
        out.writeInt(HiMsg);
        out.writeInt(HiMsgId);
        out.writeInt(LoMsg);
        out.writeInt(LoMsgId);
        out.writeInt(LastRead);
        out.writeInt(ConfAccess);
        out.writeInt(ConfFlags);
        out.writeInt(ValidNames);
        out.writeInt(FirstUnread);
        out.writeInt(ReadFlags);
    }

    protected void readFrom(WcInputStream in) throws IOException {
        super.readFrom(in);
        Number = in.readInt();
        Name = in.readString(60);
        ConfSysop = in.readString(72);
        MailType = in.readUnsignedByte();
        HiMsg = in.readInt();
        HiMsgId = in.readInt();
        LoMsg = in.readInt();
        LoMsgId = in.readInt();
        LastRead = in.readInt();
        ConfAccess = in.readInt();
        ConfFlags = in.readInt();
        ValidNames = in.readInt();
        FirstUnread = in.readInt();
        ReadFlags = in.readInt();
    }
}
