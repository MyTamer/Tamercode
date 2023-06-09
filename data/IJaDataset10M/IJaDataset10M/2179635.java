package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import org.w3c.dom.NamedNodeMap;

/**
 * DeferredElementNSImpl is to ElementNSImpl, what DeferredElementImpl is to
 * ElementImpl. 
 * 
 * @xerces.internal
 * 
 * @see DeferredElementImpl
 */
public class DeferredElementNSImpl extends ElementNSImpl implements DeferredNode {

    /** Serialization version. */
    static final long serialVersionUID = -5001885145370927385L;

    /** Node index. */
    protected transient int fNodeIndex;

    /**
     * This is the deferred constructor. Only the fNodeIndex is given here. All
     * other data, can be requested from the ownerDocument via the index.
     */
    DeferredElementNSImpl(DeferredDocumentImpl ownerDoc, int nodeIndex) {
        super(ownerDoc, null);
        fNodeIndex = nodeIndex;
        needsSyncChildren(true);
    }

    /** Returns the node index. */
    public final int getNodeIndex() {
        return fNodeIndex;
    }

    /** Synchronizes the data (name and value) for fast nodes. */
    protected final void synchronizeData() {
        needsSyncData(false);
        DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl) this.ownerDocument;
        boolean orig = ownerDocument.mutationEvents;
        ownerDocument.mutationEvents = false;
        name = ownerDocument.getNodeName(fNodeIndex);
        int index = name.indexOf(':');
        if (index < 0) {
            localName = name;
        } else {
            localName = name.substring(index + 1);
        }
        namespaceURI = ownerDocument.getNodeURI(fNodeIndex);
        type = (XSTypeDefinition) ownerDocument.getTypeInfo(fNodeIndex);
        setupDefaultAttributes();
        int attrIndex = ownerDocument.getNodeExtra(fNodeIndex);
        if (attrIndex != -1) {
            NamedNodeMap attrs = getAttributes();
            do {
                NodeImpl attr = (NodeImpl) ownerDocument.getNodeObject(attrIndex);
                attrs.setNamedItem(attr);
                attrIndex = ownerDocument.getPrevSibling(attrIndex);
            } while (attrIndex != -1);
        }
        ownerDocument.mutationEvents = orig;
    }

    /**
     * Synchronizes the node's children with the internal structure.
     * Fluffing the children at once solves a lot of work to keep
     * the two structures in sync. The problem gets worse when
     * editing the tree -- this makes it a lot easier.
     */
    protected final void synchronizeChildren() {
        DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl) ownerDocument();
        ownerDocument.synchronizeChildren(this, fNodeIndex);
    }
}
