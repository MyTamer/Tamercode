package sun.io;

import sun.nio.cs.ext.JIS_X_0208_Solaris_Decoder;

/**
 * Tables and data to convert JIS0208_Solaris to Unicode
 *
 * Vendor defined chars added for benefit of vendor defined character
 * supplemented mappings for EUC-JP-Solaris/PCK Solaris variants of EUC-JP
 * and SJIS/Shift_JIS (4765370)
 *
 * @author  ConverterGenerator tool
 */
public class ByteToCharJIS0208_Solaris extends ByteToCharDoubleByte {

    public String getCharacterEncoding() {
        return "JIS0208_Solaris";
    }

    protected char convSingleByte(int b) {
        return REPLACE_CHAR;
    }

    public ByteToCharJIS0208_Solaris() {
        super.index1 = JIS_X_0208_Solaris_Decoder.getIndex1();
        super.index2 = JIS_X_0208_Solaris_Decoder.getIndex2();
        start = 0x21;
        end = 0x7E;
    }
}
