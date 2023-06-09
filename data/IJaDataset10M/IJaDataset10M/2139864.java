package com.volantis.mcs.dom2wbsax;

import com.volantis.charset.CharacterRepresentable;
import com.volantis.charset.Encoding;
import com.volantis.mcs.protocols.wml.WMLVariable;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * This class takes an array of Java characters, identifies the "components"
 * which may be translated into indivudual WBSAX values, and forwards those
 * component values on to the {@link com.volantis.mcs.dom2wbsax.WBSAXValueSerialiser} that it is using
 * to turn them into individual WBSAX events.
 * <p>
 * This involves:
 * <ol>
 *   <li>checking for WML variables, which must be serialised as WBSAX
 *      extension values,
 *   <li>checking for unrepresentable characters, which must be serialised as
 *      WBSAX entities,
 *   <li>serialising the remaining components as WBSAX inline strings.
 * </ol>
 */
public class WBSAXStringSerialiser {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(WBSAXStringSerialiser.class);

    /**
     * Final variable to create a char array holding "$$"
     */
    private static final char[] DOLLAR_LITERAL = { '$', '$' };

    /**
     * The encoding, used to identify unrepresentable characters.
     */
    private Encoding encoding;

    /**
     * The value serialiser used to serialise value components once they have
     * been split by this class.
     */
    private WBSAXValueSerialiser valueSerialiser;

    /**
     * Construct an instance of this class.
     *
     * @param encoding the encoding to use to identify unrepresentable
     *      characters.
     */
    public WBSAXStringSerialiser(Encoding encoding, WBSAXValueSerialiser valueSerialiser) {
        this.encoding = encoding;
        this.valueSerialiser = valueSerialiser;
    }

    /**
     * Walk through the character array.  If we find an unrepresentable
     * character, write out the array up to that character, then write
     * out the unrepresentable character.
     *
     * @param chars The character array to parse
     * @param length The number of characters to parse.
    */
    public void parseValue(char[] chars, int length) throws WBSAXException {
        int writeStart = 0;
        int writeLength = 0;
        char lastWMLVariable = ' ';
        if (logger.isDebugEnabled()) {
            logger.debug("Content = " + new String(chars, 0, length));
        }
        for (int i = 0; i < length; i++) {
            char c = chars[i];
            switch(c) {
                case WMLVariable.WMLV_BRACKETED:
                case WMLVariable.WMLV_NOBRACKETS:
                    if (lastWMLVariable == ' ') {
                        writeChars(chars, writeStart, writeLength);
                        lastWMLVariable = c;
                    } else {
                        valueSerialiser.addExtensionString(Extension.TWO, chars, writeStart, writeLength);
                        lastWMLVariable = ' ';
                    }
                    writeStart = i + 1;
                    writeLength = 0;
                    break;
                case WMLVariable.WMLV_NOESC:
                    valueSerialiser.addExtensionString(Extension.TWO, chars, writeStart, writeLength);
                    lastWMLVariable = ' ';
                    writeStart = i + 1;
                    writeLength = 0;
                    break;
                case WMLVariable.WMLV_ESCAPE:
                    valueSerialiser.addExtensionString(Extension.ZERO, chars, writeStart, writeLength);
                    lastWMLVariable = ' ';
                    writeStart = i + 1;
                    writeLength = 0;
                    break;
                case WMLVariable.WMLV_UNESC:
                    valueSerialiser.addExtensionString(Extension.ONE, chars, writeStart, writeLength);
                    lastWMLVariable = ' ';
                    writeStart = i + 1;
                    writeLength = 0;
                    break;
                case '$':
                    writeChars(chars, writeStart, writeLength);
                    writeChars(DOLLAR_LITERAL, 0, 1);
                    writeStart = i + 1;
                    writeLength = 0;
                    break;
                default:
                    CharacterRepresentable rep = encoding.checkCharacter(c);
                    if (rep.isRepresentable()) {
                        writeLength++;
                    } else {
                        writeChars(chars, writeStart, writeLength);
                        valueSerialiser.addEntity(c);
                        writeStart = i + 1;
                        writeLength = 0;
                    }
                    break;
            }
        }
        writeChars(chars, writeStart, writeLength);
    }

    /**
     * Convenience method add the characters in the buffer to the content
     * handler.
     *
     * @param chars Characters to write
     * @param offset Start character
     * @param length Length of write.
     * @throws com.volantis.mcs.wbsax.WBSAXException A WBSAX problem
     */
    private void writeChars(char[] chars, int offset, int length) throws WBSAXException {
        if (length > 0) {
            valueSerialiser.addString(chars, offset, length);
        }
    }
}
