package com.sun.org.apache.xml.internal.security.utils;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Class SignatureElementProxy
 *
 * @author $Author: raul $
 * @version $Revision: 1.7 $
 */
public abstract class SignatureElementProxy extends ElementProxy {

    /**
    * Constructor SignatureElementProxy
    *
    * @param doc
    */
    public SignatureElementProxy(Document doc) {
        super(doc);
    }

    /**
    * Constructor SignatureElementProxy
    *
    * @param element
    * @param BaseURI
    * @throws XMLSecurityException
    */
    public SignatureElementProxy(Element element, String BaseURI) throws XMLSecurityException {
        super(element, BaseURI);
    }

    /** @inheritDoc */
    public String getBaseNamespace() {
        return Constants.SignatureSpecNS;
    }
}
