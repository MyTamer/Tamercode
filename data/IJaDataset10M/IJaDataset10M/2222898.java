package COM.winserver.wildcat;

import java.io.IOException;

public class TGetPredefinedChatChannels_Response extends TWildcatRequest {

    public int Id;

    public int Flags;

    public int Length;

    public String ChannelName;

    public static final int SIZE = TWildcatRequest.SIZE + 4 + 4 + 4 + 260;

    public TGetPredefinedChatChannels_Response() {
    }

    public TGetPredefinedChatChannels_Response(byte[] x) {
        fromByteArray(x);
    }

    protected void writeTo(WcOutputStream out) throws IOException {
        super.writeTo(out);
        out.writeInt(Id);
        out.writeInt(Flags);
        out.writeInt(Length);
        out.writeString(ChannelName, 260);
    }

    protected void readFrom(WcInputStream in) throws IOException {
        super.readFrom(in);
        Id = in.readInt();
        Flags = in.readInt();
        Length = in.readInt();
        ChannelName = in.readString(260);
    }
}
