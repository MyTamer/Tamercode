package org.jcp.xml.dsig.internal.dom;

import javax.xml.crypto.*;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.*;
import java.security.Provider;
import java.util.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DOM-based implementation of XMLObject.
 *
 * @author Sean Mullan
 */
public final class DOMXMLObject extends DOMStructure implements XMLObject {

    private final String id;

    private final String mimeType;

    private final String encoding;

    private final List content;

    /**
     * Creates an <code>XMLObject</code> from the specified parameters.
     *
     * @param content a list of {@link XMLStructure}s. The list
     *    is defensively copied to protect against subsequent modification.
     *    May be <code>null</code> or empty.
     * @param id the Id (may be <code>null</code>)
     * @param mimeType the mime type (may be <code>null</code>)
     * @param encoding the encoding (may be <code>null</code>)
     * @return an <code>XMLObject</code>
     * @throws ClassCastException if <code>content</code> contains any
     *    entries that are not of type {@link XMLStructure}
     */
    public DOMXMLObject(List content, String id, String mimeType, String encoding) {
        if (content == null || content.isEmpty()) {
            this.content = Collections.EMPTY_LIST;
        } else {
            List contentCopy = new ArrayList(content);
            for (int i = 0, size = contentCopy.size(); i < size; i++) {
                if (!(contentCopy.get(i) instanceof XMLStructure)) {
                    throw new ClassCastException("content[" + i + "] is not a valid type");
                }
            }
            this.content = Collections.unmodifiableList(contentCopy);
        }
        this.id = id;
        this.mimeType = mimeType;
        this.encoding = encoding;
    }

    /**
     * Creates an <code>XMLObject</code> from an element.
     *
     * @param objElem an Object element
     * @throws MarshalException if there is an error when unmarshalling
     */
    public DOMXMLObject(Element objElem, XMLCryptoContext context, Provider provider) throws MarshalException {
        this.encoding = DOMUtils.getAttributeValue(objElem, "Encoding");
        this.id = DOMUtils.getAttributeValue(objElem, "Id");
        this.mimeType = DOMUtils.getAttributeValue(objElem, "MimeType");
        NodeList nodes = objElem.getChildNodes();
        int length = nodes.getLength();
        List content = new ArrayList(length);
        for (int i = 0; i < length; i++) {
            Node child = nodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element childElem = (Element) child;
                String tag = childElem.getLocalName();
                if (tag.equals("Manifest")) {
                    content.add(new DOMManifest(childElem, context, provider));
                    continue;
                } else if (tag.equals("SignatureProperties")) {
                    content.add(new DOMSignatureProperties(childElem));
                    continue;
                } else if (tag.equals("X509Data")) {
                    content.add(new DOMX509Data(childElem));
                    continue;
                }
            }
            content.add(new javax.xml.crypto.dom.DOMStructure(child));
        }
        if (content.isEmpty()) {
            this.content = Collections.EMPTY_LIST;
        } else {
            this.content = Collections.unmodifiableList(content);
        }
    }

    public List getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getEncoding() {
        return encoding;
    }

    public void marshal(Node parent, String dsPrefix, DOMCryptoContext context) throws MarshalException {
        Document ownerDoc = DOMUtils.getOwnerDocument(parent);
        Element objElem = DOMUtils.createElement(ownerDoc, "Object", XMLSignature.XMLNS, dsPrefix);
        DOMUtils.setAttributeID(objElem, "Id", id);
        DOMUtils.setAttribute(objElem, "MimeType", mimeType);
        DOMUtils.setAttribute(objElem, "Encoding", encoding);
        for (int i = 0, size = content.size(); i < size; i++) {
            XMLStructure object = (XMLStructure) content.get(i);
            if (object instanceof DOMStructure) {
                ((DOMStructure) object).marshal(objElem, dsPrefix, context);
            } else {
                javax.xml.crypto.dom.DOMStructure domObject = (javax.xml.crypto.dom.DOMStructure) object;
                DOMUtils.appendChild(objElem, domObject.getNode());
            }
        }
        parent.appendChild(objElem);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof XMLObject)) {
            return false;
        }
        XMLObject oxo = (XMLObject) o;
        boolean idsEqual = (id == null ? oxo.getId() == null : id.equals(oxo.getId()));
        boolean encodingsEqual = (encoding == null ? oxo.getEncoding() == null : encoding.equals(oxo.getEncoding()));
        boolean mimeTypesEqual = (mimeType == null ? oxo.getMimeType() == null : mimeType.equals(oxo.getMimeType()));
        return (idsEqual && encodingsEqual && mimeTypesEqual && equalsContent(oxo.getContent()));
    }

    private boolean equalsContent(List otherContent) {
        if (content.size() != otherContent.size()) {
            return false;
        }
        for (int i = 0, osize = otherContent.size(); i < osize; i++) {
            XMLStructure oxs = (XMLStructure) otherContent.get(i);
            XMLStructure xs = (XMLStructure) content.get(i);
            if (oxs instanceof javax.xml.crypto.dom.DOMStructure) {
                if (!(xs instanceof javax.xml.crypto.dom.DOMStructure)) {
                    return false;
                }
                Node onode = ((javax.xml.crypto.dom.DOMStructure) oxs).getNode();
                Node node = ((javax.xml.crypto.dom.DOMStructure) xs).getNode();
                if (!DOMUtils.nodesEqual(node, onode)) {
                    return false;
                }
            } else {
                if (!(xs.equals(oxs))) {
                    return false;
                }
            }
        }
        return true;
    }
}
