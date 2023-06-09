package sun.io;

/**
 * A table to convert Cp037 to Unicode
 *
 * @author  ConverterGenerator tool
 * @version >= JDK1.1.6
 */
public class ByteToCharCp037 extends ByteToCharSingleByte {

    public String getCharacterEncoding() {
        return "Cp037";
    }

    public ByteToCharCp037() {
        super.byteToCharTable = byteToCharTable;
    }

    private static final String byteToCharTable = "Øabcdefg" + "hi«»ðýþ±" + "°jklmnop" + "qrªºæ¸Æ¤" + "µ~stuvwx" + "yz¡¿ÐÝÞ®" + "^£¥·©§¶¼" + "½¾[]¯¨´×" + "{ABCDEFG" + "HI­ôöòóõ" + "}JKLMNOP" + "QR¹ûüùúÿ" + "\\÷STUVWX" + "YZ²ÔÖÒÓÕ" + "01234567" + "89³ÛÜÙÚ" + " \t" + "\f\r" + "\b" + "" + "\n" + "" + "" + "" + "  âäàáãå" + "çñ¢.<(+|" + "&éêëèíîï" + "ìß!$*);¬" + "-/ÂÄÀÁÃÅ" + "ÇÑ¦,%_>?" + "øÉÊËÈÍÎÏ" + "Ì`:#@\'=\"";
}
