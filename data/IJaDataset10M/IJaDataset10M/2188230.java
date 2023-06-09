package gnu.xml.validation.datatype;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.relaxng.datatype.DatatypeException;
import org.relaxng.datatype.ValidationContext;

/**
 * The XML Schema token type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class TokenType extends AtomicSimpleType {

    static final int[] CONSTRAINING_FACETS = { Facet.LENGTH, Facet.MIN_LENGTH, Facet.MAX_LENGTH, Facet.PATTERN, Facet.ENUMERATION, Facet.WHITESPACE };

    TokenType() {
        super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "token"), TypeLibrary.NORMALIZED_STRING);
    }

    public int[] getConstrainingFacets() {
        return CONSTRAINING_FACETS;
    }

    public void checkValid(String value, ValidationContext context) throws DatatypeException {
        super.checkValid(value, context);
        int len = value.length();
        if (len == 0) throw new DatatypeException(0, "invalid token value");
        if (value.charAt(0) == ' ' || value.charAt(len - 1) == ' ') throw new DatatypeException(0, "invalid token value");
        char last = ' ';
        for (int i = 0; i < len; i++) {
            char c = value.charAt(i);
            if (c == 0x0a || c == 0x0d || c == 0x09) throw new DatatypeException(i, "invalid token value");
            if (c == ' ' && last == ' ') throw new DatatypeException(i, "invalid token value");
            last = c;
        }
    }
}
