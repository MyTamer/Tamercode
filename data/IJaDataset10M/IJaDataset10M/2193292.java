package sun.jkernel;

import static sun.jkernel.StandaloneByteArrayAccess.*;

/**
 * This is a slightly modified subset of the
 * <code>sun.security.provider.SHA</code> class that
 * is not dependent on the regular Java Security framework classes. It
 * implements the Secure Hash Algorithm (SHA-1) developed by
 * the National Institute of Standards and Technology along with the
 * National Security Agency.  This is the updated version of SHA
 * fip-180 as superseded by fip-180-1.
 * <p>
 * The <code>sun.security.provider.SHA.clonde()</code> method is not
 * implemented and other, formerly public methods, are package private.
 *
 */
final class StandaloneSHA extends StandaloneMessageDigest {

    static final boolean debug = false;

    private final int[] W;

    private final int[] state;

    /**
     * Creates a new StandaloneSHA object.
     */
    StandaloneSHA() {
        super("SHA-1", 20, 64);
        state = new int[5];
        W = new int[80];
        implReset();
    }

    /**
     * Resets the buffers and hash value to start a new hash.
     */
    void implReset() {
        if (debug) {
            System.out.print("StandaloneSHA.implR: ");
        }
        state[0] = 0x67452301;
        state[1] = 0xefcdab89;
        state[2] = 0x98badcfe;
        state[3] = 0x10325476;
        state[4] = 0xc3d2e1f0;
    }

    /**
     * Computes the final hash and copies the 20 bytes to the output array.
     */
    void implDigest(byte[] out, int ofs) {
        if (debug) {
            System.out.print("StandaloneSHA.implD: ");
        }
        long bitsProcessed = bytesProcessed << 3;
        int index = (int) bytesProcessed & 0x3f;
        int padLen = (index < 56) ? (56 - index) : (120 - index);
        engineUpdate(padding, 0, padLen);
        StandaloneByteArrayAccess.i2bBig4((int) (bitsProcessed >>> 32), buffer, 56);
        StandaloneByteArrayAccess.i2bBig4((int) bitsProcessed, buffer, 60);
        implCompress(buffer, 0);
        StandaloneByteArrayAccess.i2bBig(state, 0, out, ofs, 20);
    }

    private static final int round1_kt = 0x5a827999;

    private static final int round2_kt = 0x6ed9eba1;

    private static final int round3_kt = 0x8f1bbcdc;

    private static final int round4_kt = 0xca62c1d6;

    /**
     * Compute a the hash for the current block.
     *
     * This is in the same vein as Peter Gutmann's algorithm listed in
     * the back of Applied Cryptography, Compact implementation of
     * "old" NIST Secure Hash Algorithm.
     */
    void implCompress(byte[] buf, int ofs) {
        if (debug) {
            System.out.print("StandaloneSHA.implC: ");
            for (int i = ofs; i < buf.length; i++) {
                System.out.format("%02X", buf[i]);
            }
            System.out.println();
        }
        StandaloneByteArrayAccess.b2iBig(buf, ofs, W, 0, 64);
        for (int t = 16; t <= 79; t++) {
            int temp = W[t - 3] ^ W[t - 8] ^ W[t - 14] ^ W[t - 16];
            W[t] = (temp << 1) | (temp >>> 31);
        }
        int a = state[0];
        int b = state[1];
        int c = state[2];
        int d = state[3];
        int e = state[4];
        for (int i = 0; i < 20; i++) {
            int temp = ((a << 5) | (a >>> (32 - 5))) + ((b & c) | ((~b) & d)) + e + W[i] + round1_kt;
            e = d;
            d = c;
            c = ((b << 30) | (b >>> (32 - 30)));
            b = a;
            a = temp;
        }
        for (int i = 20; i < 40; i++) {
            int temp = ((a << 5) | (a >>> (32 - 5))) + (b ^ c ^ d) + e + W[i] + round2_kt;
            e = d;
            d = c;
            c = ((b << 30) | (b >>> (32 - 30)));
            b = a;
            a = temp;
        }
        for (int i = 40; i < 60; i++) {
            int temp = ((a << 5) | (a >>> (32 - 5))) + ((b & c) | (b & d) | (c & d)) + e + W[i] + round3_kt;
            e = d;
            d = c;
            c = ((b << 30) | (b >>> (32 - 30)));
            b = a;
            a = temp;
        }
        for (int i = 60; i < 80; i++) {
            int temp = ((a << 5) | (a >>> (32 - 5))) + (b ^ c ^ d) + e + W[i] + round4_kt;
            e = d;
            d = c;
            c = ((b << 30) | (b >>> (32 - 30)));
            b = a;
            a = temp;
        }
        state[0] += a;
        state[1] += b;
        state[2] += c;
        state[3] += d;
        state[4] += e;
    }
}
