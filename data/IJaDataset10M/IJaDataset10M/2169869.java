package sun.io;

/**
 * A table to convert MacCroatian to Unicode
 *
 * @author  ConverterGenerator tool
 * @version >= JDK1.1.6
 */
public class ByteToCharMacCroatian extends ByteToCharSingleByte {

    public String getCharacterEncoding() {
        return "MacCroatian";
    }

    public ByteToCharMacCroatian() {
        super.byteToCharTable = byteToCharTable;
    }

    private static final String byteToCharTable = "ÄÅÇÉÑÖÜá" + "àâäãåçéè" + "êëíìîïñó" + "òôöõúùûü" + "†°¢£§•¶ß" + "®Š™´¨≠ŽØ" + "∞±≤≥∆µ∂∑" + "∏š∫ªºΩžø" + "¿¡¬√ƒ≈Ć«" + "Č… ÀÃÕŒœ" + "Đ—“”‘’÷◊" + "©⁄¤‹›Æ»" + "–·‚„‰ÂćÁ" + "čÈÍÎÏÌÓÔ" + "đÒÚÛÙıˆ˜" + "¯πË˚¸Êæˇ" + " " + "\b\t\n\f\r" + "" + "" + " !\"#$%&\'" + "()*+,-./" + "01234567" + "89:;<=>?" + "@ABCDEFG" + "HIJKLMNO" + "PQRSTUVW" + "XYZ[\\]^_" + "`abcdefg" + "hijklmno" + "pqrstuvw" + "xyz{|}~";
}
