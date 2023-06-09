package sun.io;

/**
 * A table to convert Cp922 to Unicode
 *
 * @author  ConverterGenerator tool
 * @version >= JDK1.1.6
 */
public class ByteToCharCp922 extends ByteToCharSingleByte {

    public String getCharacterEncoding() {
        return "Cp922";
    }

    public ByteToCharCp922() {
        super.byteToCharTable = byteToCharTable;
    }

    private static final String byteToCharTable = "" + "" + "" + "" + " ¡¢£¤¥¦§" + "¨©ª«¬­®‾" + "°±²³´µ¶·" + "¸¹º»¼½¾¿" + "ÀÁÂÃÄÅÆÇ" + "ÈÉÊËÌÍÎÏ" + "ŠÑÒÓÔÕÖ×" + "ØÙÚÛÜÝŽß" + "àáâãäåæç" + "èéêëìíîï" + "šñòóôõö÷" + "øùúûüýžÿ" + " " + "\b\t\n\f\r" + "" + "" + " !\"#$%&\'" + "()*+,-./" + "01234567" + "89:;<=>?" + "@ABCDEFG" + "HIJKLMNO" + "PQRSTUVW" + "XYZ[\\]^_" + "`abcdefg" + "hijklmno" + "pqrstuvw" + "xyz{|}~";
}
