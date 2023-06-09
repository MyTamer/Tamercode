package sun.io;

/**
 * Tables and data to convert Unicode to Cp1142
 *
 * @author  ConverterGenerator tool
 * @version >= JDK1.1.7
 */
public class CharToByteCp1142 extends CharToByteSingleByte {

    public String getCharacterEncoding() {
        return "Cp1142";
    }

    public CharToByteCp1142() {
        super.mask1 = 0xFF00;
        super.mask2 = 0x00FF;
        super.shift = 8;
        super.index1 = index1;
        super.index2 = index2;
    }

    private static final String index2 = " 7-./" + "%\f\r" + "<=2&" + "?\'" + "@OJglP}" + "M]\\Nk`Ka" + "ðñòóôõö÷" + "øùz^L~no" + "ÁÂÃÄÅÆÇ" + "ÈÉÑÒÓÔÕÖ" + "×ØÙâãäåæ" + "çèéà_m" + "y" + "" + "¢£¤¥¦" + "§¨©»GÜ" + " !\"#$" + "()*+,\t\n" + "013456\b" + "89:;>ÿ" + "Aª°± ²pµ" + "½´ºÊ¯¼" + "êú¾ ¶³" + "Ú·¸¹«" + "debfc[{h" + "tqrsxuvw" + "¬iíîëïì¿" + "|ýþûü­®Y" + "DEBFCÐÀH" + "TQRSXUVW" + "IÍÎËÏÌá" + "jÝÞÛ¡ß" + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "Z       " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "    ";

    private static final short index1[] = { 0, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 340, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256, 256 };
}
