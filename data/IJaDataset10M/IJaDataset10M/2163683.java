package org.xmlcml.cml.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;
import nu.xom.UnavailableCharacterException;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.Util;

/**
 * 
 * <p>
 * Serializer for customising CML output
 * </p>
 * This allows (a) writing to a String (b) customising elements and attributes
 * Still experimental
 * @author Peter Murray-Rust
 * @version 5.0
 * 
 */
public class CMLSerializer extends Serializer implements CMLConstants {

    static final Logger logger = Logger.getLogger(CMLSerializer.class);

    ByteArrayOutputStream baos;

    /** creates serializer to ByteArrayOutputStream.
     * @see #getXML(Document)
     * @see #getXML(Element)
     * recover with getXML()
     */
    public CMLSerializer() {
        super(new ByteArrayOutputStream());
        baos = new ByteArrayOutputStream();
        try {
            this.setOutputStream(baos);
        } catch (IOException e) {
            Util.BUG(e);
        }
    }

    /**
     * creates normal Serializer.
     * 
     * @param os
     */
    public CMLSerializer(OutputStream os) {
        super(os);
    }

    /**
     * write a Document to string.
     * 
     * @param doc
     *            the document
     * @return XML for document
     */
    public String getXML(Document doc) {
        try {
            this.write(doc);
        } catch (IOException e) {
            Util.BUG(e);
        }
        return baos.toString();
    }

    /**
     * write XML for an element node. copies elements so it doesn't have a
     * document parent
     * 
     * @param elem
     * @return the XML String
     */
    public String getXML(Element elem) {
        Element clone = (Element) elem.copy();
        Document doc = new Document(clone);
        return getXML(doc);
    }

    /** overrides attribute writing.
     * <p>
     * Writes an attribute in the form <code><i>name</i>="<i>value</i>"</code>.
     * Characters in the attribute value are escaped as necessary.
     * </p>
     * 
     * @param attribute
     *            the <code>Attribute</code> to write
     * 
     * @throws IOException
     *             if the underlying output stream encounters an I/O error
     * @throws UnavailableCharacterException
     *             if the attribute name contains a character that is not
     *             available in the current encoding
     * 
     */
    protected void write(Attribute attribute) throws IOException {
        super.write(attribute);
    }

    /** overrides element writing. allows updating of XOM before
     * 
     * @param element
     * @throws IOException
     */
    protected void write(Element element) throws IOException {
        if (element instanceof CMLElement) {
        }
        super.write(element);
    }
}
