package mobac.program.atlascreators.impl.rmp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RmpTools {

    /**
	 * Copies the given value into the stream as binary value, with least
	 * significant byte first.
	 * 
	 * @param stream
	 *            stream to write to
	 * @param value
	 *            value to write
	 * @param length
	 *            number of bytes to write to stream
	 * @throws IOException
	 */
    public static void writeValue(OutputStream stream, int value, int length) throws IOException {
        int i;
        for (i = 0; i < length; i++) {
            stream.write(value & 0xFF);
            value >>= 8;
        }
    }

    public static void writeValue(OutputStream stream, long value, int length) throws IOException {
        int i;
        for (i = 0; i < length; i++) {
            stream.write((int) (value & 0xFF));
            value >>= 8;
        }
    }

    /**
	 * Writes the given string into the stream. The string is written with a
	 * fixed length. If the length is longer than the string, then 00 bytes are
	 * written
	 * 
	 * @param stream
	 *            stream to write to
	 * @param str
	 *            strign to write
	 * @param length
	 *            number of bytes to write
	 * @throws IOException
	 */
    public static void writeFixedString(OutputStream stream, String str, int length) throws IOException {
        int i;
        int value;
        for (i = 0; i < length; i++) {
            if (i < str.length()) value = str.charAt(i); else value = 0;
            stream.write(value);
        }
    }

    /**
	 * Write a double value into a byte array
	 * 
	 * @param os
	 *            stream to write to
	 * @param value
	 *            value to write
	 */
    public static void writeDouble(OutputStream os, double value) throws IOException {
        ByteArrayOutputStream bo;
        DataOutputStream dos;
        byte[] b;
        byte help;
        bo = new ByteArrayOutputStream();
        dos = new DataOutputStream(bo);
        dos.writeDouble(value);
        dos.close();
        b = bo.toByteArray();
        for (int i = 0; i < 4; i++) {
            help = b[i];
            b[i] = b[7 - i];
            b[7 - i] = help;
        }
        os.write(b);
    }

    /**
	 * Build an image name from a filename. The image name is the name of the
	 * file without path and extension . The length of the name is limited to 8
	 * chars. We use only 6 chars, so we can use 99 images
	 */
    public static String buildImageName(String name) {
        int index;
        index = name.indexOf('.');
        if (index != -1) name = name.substring(0, index);
        if (name.length() > 8) name = name.substring(0, 8);
        return name.toLowerCase().trim();
    }

    /**
	 * Builds a tile name from a basename and an index.
	 */
    public static String buildTileName(String basename, int index) {
        String indexstr;
        indexstr = String.valueOf(index);
        if (indexstr.length() + basename.length() > 8) basename = basename.substring(0, 8 - indexstr.length());
        return basename.trim() + indexstr;
    }
}
