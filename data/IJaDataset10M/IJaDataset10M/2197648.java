package com.sun.ukit.xml;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class SAXParserFactoryImp extends SAXParserFactory {

    private boolean namespaces = false;

    private boolean prefixes = true;

    /**
	 * Creates a new instance of a SAXParser using the currently
	 * configured factory parameters.
	 *
	 * @return A new instance of a SAXParser.
	 *
	 * @exception ParserConfigurationException if a parser cannot
	 * be created which satisfies the requested configuration.
	 */
    public SAXParser newSAXParser() throws ParserConfigurationException, SAXException {
        if (isValidating() == true) {
            throw new ParserConfigurationException("");
        }
        SAXParser parser = new SAXParserImp();
        if ((namespaces == true) && (prefixes == false)) {
            parser.getXMLReader().setFeature(ParserSAX.FEATURE_NS, true);
            parser.getXMLReader().setFeature(ParserSAX.FEATURE_PREF, false);
        } else if ((namespaces == false) && (prefixes == true)) {
            parser.getXMLReader().setFeature(ParserSAX.FEATURE_PREF, true);
            parser.getXMLReader().setFeature(ParserSAX.FEATURE_NS, false);
        } else {
            throw new ParserConfigurationException("");
        }
        return parser;
    }

    /**
	 * Specifies that the parser produced by this code will
	 * provide support for XML namespaces. By default the value of this is set
	 * to <code>false</code>.
	 *
	 * @param awareness true if the parser produced by this code will
	 *                  provide support for XML namespaces; false otherwise.
	 */
    public void setNamespaceAware(boolean awareness) {
        super.setNamespaceAware(awareness);
        if (awareness == true) {
            namespaces = true;
            prefixes = false;
        } else {
            namespaces = false;
            prefixes = true;
        }
    }

    /**
	 * Sets the particular feature in the underlying implementation of
	 * DefaultHandler.
	 * A list of the core features and properties can be found at
	 * <a href="http://www.saxproject.org/?selected=get-set">
	 * http://www.saxproject.org/?selected=get-set </a>
	 *
	 * @param name The name of the feature to be set.
	 * @param value The value of the feature to be set.
	 * @exception SAXNotRecognizedException When the underlying DefaultHandler 
	 *            does not recognize the property name.
	 *
	 * @exception SAXNotSupportedException When the underlying DefaultHandler
	 *            recognizes the property name but doesn't support the
	 *            property.
	 */
    public void setFeature(String name, boolean value) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
        if (ParserSAX.FEATURE_NS.equals(name) == true) {
            namespaces = value;
        } else if (ParserSAX.FEATURE_PREF.equals(name) == true) {
            prefixes = value;
        } else {
            throw new SAXNotRecognizedException(name);
        }
    }

    /**
	 * Returns the particular property requested for in the underlying
	 * implementation of DefaultHandler.
	 *
	 * @param name The name of the property to be retrieved.
	 * @return Value of the requested property.
	 *
	 * @exception SAXNotRecognizedException When the underlying DefaultHandler does
	 *            not recognize the property name.
	 *
	 * @exception SAXNotSupportedException When the underlying DefaultHandler
	 *            recognizes the property name but doesn't support the
	 *            property.
	 */
    public boolean getFeature(String name) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
        if (ParserSAX.FEATURE_NS.equals(name) == true) {
            return namespaces;
        } else if (ParserSAX.FEATURE_PREF.equals(name) == true) {
            return prefixes;
        } else {
            throw new SAXNotRecognizedException(name);
        }
    }
}
