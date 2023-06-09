package sun.io;

/**
 * A table to convert Cp1145 to Unicode
 *
 * @author  ConverterGenerator tool
 * @version >= JDK1.1.7
 */
public class ByteToCharCp1145 extends ByteToCharSingleByte {

    public String getCharacterEncoding() {
        return "Cp1145";
    }

    public ByteToCharCp1145() {
        super.byteToCharTable = byteToCharTable;
    }

    private static final String byteToCharTable = "Øabcdefg" + "hi«»ðýþ±" + "°jklmnop" + "qrªºæ¸Æ€" + "µ¨stuvwx" + "yz¡¿ÐÝÞ®" + "¢£¥·©§¶¼" + "½¾^!¯~´×" + "{ABCDEFG" + "HI­ôöòóõ" + "}JKLMNOP" + "QR¹ûüùúÿ" + "\\÷STUVWX" + "YZ²ÔÖÒÓÕ" + "01234567" + "89³ÛÜÙÚ" + " \t" + "\f\r" + "\b" + "" + "\n" + "" + "" + "" + "  âäàáãå" + "ç¦[.<(+|" + "&éêëèíîï" + "ìß]$*);¬" + "-/ÂÄÀÁÃÅ" + "Ç#ñ,%_>?" + "øÉÊËÈÍÎÏ" + "Ì`:Ñ@\'=\"";
}
