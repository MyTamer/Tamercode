package COM.winserver.wildcat;

import java.io.IOException;

public class TViewArchive_Data extends TWildcatRequest {

    public int size;

    public String data;

    public static final int SIZE = TWildcatRequest.SIZE + 2 + 1024;

    public TViewArchive_Data() {
    }

    public TViewArchive_Data(byte[] x) {
        fromByteArray(x);
    }

    protected void writeTo(WcOutputStream out) throws IOException {
        super.writeTo(out);
        out.writeShort(size);
        out.writeString(data, 1024);
    }

    protected void readFrom(WcInputStream in) throws IOException {
        super.readFrom(in);
        size = in.readUnsignedShort();
        data = in.readString(1024);
    }
}
