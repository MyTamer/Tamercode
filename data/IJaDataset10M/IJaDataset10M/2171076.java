package sun.io;

/**
 * A table to convert Cp855 to Unicode
 *
 * @author  ConverterGenerator tool
 * @version >= JDK1.1.6
 */
public class ByteToCharCp855 extends ByteToCharSingleByte {

    public String getCharacterEncoding() {
        return "Cp855";
    }

    public ByteToCharCp855() {
        super.byteToCharTable = byteToCharTable;
    }

    private static final String byteToCharTable = "ђЂѓЃёЁєЄ" + "ѕЅіІїЇјЈ" + "љЉњЊћЋќЌ" + "ўЎџЏюЮъЪ" + "аАбБцЦдД" + "еЕфФгГ«»" + "░▒▓│┤хХи" + "И╣║╗╝йЙ┐" + "└┴┬├─┼кК" + "╚╔╩╦╠═╬¤" + "лЛмМнНоО" + "п┘┌█▄Пя▀" + "ЯрРсСтТу" + "УжЖвВьЬ№" + "­ыЫзЗшШэ" + "ЭщЩчЧ§■ " + " " + "\b\t\n\f\r" + "" + "" + " !\"#$%&\'" + "()*+,-./" + "01234567" + "89:;<=>?" + "@ABCDEFG" + "HIJKLMNO" + "PQRSTUVW" + "XYZ[\\]^_" + "`abcdefg" + "hijklmno" + "pqrstuvw" + "xyz{|}~";
}
