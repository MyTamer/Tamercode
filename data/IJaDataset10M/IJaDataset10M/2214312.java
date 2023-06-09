package org.gzigzag.mediaserver.ids;

import org.gzigzag.mediaserver.*;
import java.security.*;
import java.io.*;
import org.gzigzag.util.HexUtil;

/** An InputStream that checks whether a given Mediaserver.Id matches.
 *  When being closed, this throws an exception if the hash doesn't match.
 */
public class IDCheckInputStream extends FilterInputStream {

    private MessageDigest d;

    private DigestInputStream dis;

    private byte[] hash;

    public IDCheckInputStream(InputStream is, byte[] hash) {
        this(is, null, hash);
    }

    public IDCheckInputStream(InputStream is, byte[] idstart, byte[] hash) {
        super(new DigestInputStream(is, null));
        this.dis = (DigestInputStream) this.in;
        try {
            d = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            throw new Error("Can't create IDCheckInputStream: " + e);
        }
        if (idstart != null) {
            int l = idstart.length;
            d.update((byte) ((l >>> 24) & 0xff));
            d.update((byte) ((l >>> 16) & 0xff));
            d.update((byte) ((l >>> 8) & 0xff));
            d.update((byte) (l & 0xff));
            d.update(idstart);
        }
        dis.setMessageDigest(d);
        this.hash = hash;
    }

    public void close() throws IOException {
        super.close();
        byte[] dig = d.digest();
        if (dig.length != hash.length) throw new Error("Hash did not match (different lengths)");
        for (int i = 0; i < dig.length; i++) if (dig[i] != hash[i]) throw new Error("Wrong hash!\n" + "Id: " + HexUtil.byteArrToHex(hash) + "\n" + "Generated: " + HexUtil.byteArrToHex(dig) + "\n");
    }
}
