package cx.ath.contribs.internal.xerces.dom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.TypeInfo;

/**
 * Attribute represents an XML-style attribute of an
 * Element. Typically, the allowable values are controlled by its
 * declaration in the Document Type Definition (DTD) governing this
 * kind of document.
 * <P>
 * If the attribute has not been explicitly assigned a value, but has
 * been declared in the DTD, it will exist and have that default. Only
 * if neither the document nor the DTD specifies a value will the
 * Attribute really be considered absent and have no value; in that
 * case, querying the attribute will return null.
 * <P>
 * Attributes may have multiple children that contain their data. (XML
 * allows attributes to contain entity references, and tokenized
 * attribute types such as NMTOKENS may have a child for each token.)
 * For convenience, the Attribute object's getValue() method returns
 * the string version of the attribute's value.
 * <P>
 * Attributes are not children of the Elements they belong to, in the
 * usual sense, and have no valid Parent reference. However, the spec
 * says they _do_ belong to a specific Element, and an INUSE exception
 * is to be thrown if the user attempts to explicitly share them
 * between elements.
 * <P>
 * Note that Elements do not permit attributes to appear to be shared
 * (see the INUSE exception), so this object's mutability is
 * officially not an issue.
 * <p>
 * Note: The ownerNode attribute is used to store the Element the Attr
 * node is associated with. Attr nodes do not have parent nodes.
 * Besides, the getOwnerElement() method can be used to get the element node
 * this attribute is associated with.
 * <P>
 * AttrImpl does not support Namespaces. AttrNSImpl, which inherits from
 * it, does.
 *
 * <p>AttrImpl used to inherit from ParentNode. It now directly inherits from
 * NodeImpl and provide its own implementation of the ParentNode's behavior.
 * The reason is that we now try and avoid to always create a Text node to
 * hold the value of an attribute. The DOM spec requires it, so we still have
 * to do it in case getFirstChild() is called for instance. The reason
 * attribute values are stored as a list of nodes is so that they can carry
 * more than a simple string. They can also contain EntityReference nodes.
 * However, most of the times people only have a single string that they only
 * set and get through Element.set/getAttribute or Attr.set/getValue. In this
 * new version, the Attr node has a value pointer which can either be the
 * String directly or a pointer to the first ChildNode. A flag tells which one
 * it currently is. Note that while we try to stick with the direct String as
 * much as possible once we've switched to a node there is no going back. This
 * is because we have no way to know whether the application keeps referring to
 * the node we once returned.
 * <p> The gain in memory varies on the density of attributes in the document.
 * But in the tests I've run I've seen up to 12% of memory gain. And the good
 * thing is that it also leads to a slight gain in speed because we allocate
 * fewer objects! I mean, that's until we have to actually create the node...
 * <p>
 * To avoid too much duplicated code, I got rid of ParentNode and renamed
 * ChildAndParentNode, which I never really liked, to ParentNode for
 * simplicity, this doesn't make much of a difference in memory usage because
 * there are only very few objects that are only a Parent. This is only true
 * now because AttrImpl now inherits directly from NodeImpl and has its own
 * implementation of the ParentNode's node behavior. So there is still some
 * duplicated code there.
 * <p>
 * This class doesn't directly support mutation events, however, it notifies
 * the document when mutations are performed so that the document class do so.
 *
 * <p><b>WARNING</b>: Some of the code here is partially duplicated in
 * ParentNode, be careful to keep these two classes in sync!
 *
 * @xerces.internal
 *
 * @see AttrNSImpl 
 *
 * @author Arnaud  Le Hors, IBM
 * @author Joe Kesselman, IBM
 * @author Andy Clark, IBM 
 * @version $Id: AttrImpl.java,v 1.3 2007/07/13 07:23:26 paul Exp $
 * @since PR-DOM-Level-1-19980818.
 *
 */
public class AttrImpl extends NodeImpl implements Attr, TypeInfo {

    /** Serialization version. */
    static final long serialVersionUID = 7277707688218972102L;

    /** DTD namespace. **/
    static final String DTD_URI = "http://www.w3.org/TR/REC-xml";

    /** This can either be a String or the first child node. */
    protected Object value = null;

    /** Attribute name. */
    protected String name;

    transient Object type;

    protected static TextImpl textNode = null;

    /**
     * Attribute has no public constructor. Please use the factory
     * method in the Document class.
     */
    protected AttrImpl(CoreDocumentImpl ownerDocument, String name) {
        super(ownerDocument);
        this.name = name;
        isSpecified(true);
        hasStringValue(true);
    }

    protected AttrImpl() {
    }

    void rename(String name) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.name = name;
    }

    protected void makeChildNode() {
        if (hasStringValue()) {
            if (value != null) {
                TextImpl text = (TextImpl) ownerDocument().createTextNode((String) value);
                value = text;
                text.isFirstChild(true);
                text.previousSibling = text;
                text.ownerNode = this;
                text.isOwned(true);
            }
            hasStringValue(false);
        }
    }

    /**
     * NON-DOM
     * set the ownerDocument of this node and its children
     */
    protected void setOwnerDocument(CoreDocumentImpl doc) {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        super.setOwnerDocument(doc);
        if (!hasStringValue()) {
            for (ChildNode child = (ChildNode) value; child != null; child = child.nextSibling) {
                child.setOwnerDocument(doc);
            }
        }
    }

    /**
     * NON-DOM: set the type of this attribute to be ID type.
     * 
     * @param id
     */
    public void setIdAttribute(boolean id) {
        if (needsSyncData()) {
            synchronizeData();
        }
        isIdAttribute(id);
    }

    /** DOM Level 3: isId*/
    public boolean isId() {
        return isIdAttribute();
    }

    public NodeImpl cloneNode(boolean deep) {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        AttrImpl clone = (AttrImpl) super.cloneNode(deep);
        if (!clone.hasStringValue()) {
            clone.value = null;
            for (Node child = (Node) value; child != null; child = child.getNextSibling()) {
                clone.appendChild(child.cloneNode(true));
            }
        }
        clone.isSpecified(true);
        return clone;
    }

    /**
     * A short integer indicating what type of node this is. The named
     * constants for this value are defined in the org.w3c.dom.Node interface.
     */
    public short getNodeType() {
        return Node.ATTRIBUTE_NODE;
    }

    /**
     * Returns the attribute name
     */
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return name;
    }

    /**
     * Implicit in the rerouting of getNodeValue to getValue is the
     * need to redefine setNodeValue, for symmetry's sake.  Note that
     * since we're explicitly providing a value, Specified should be set
     * true.... even if that value equals the default.
     */
    public void setNodeValue(String value) throws DOMException {
        setValue(value);
    }

    /**
     * @see org.w3c.dom.TypeInfo#getTypeName()
     */
    public String getTypeName() {
        return (String) type;
    }

    /**
     * @see org.w3c.dom.TypeInfo#getTypeNamespace()
     */
    public String getTypeNamespace() {
        if (type != null) {
            return DTD_URI;
        }
        return null;
    }

    /**
     * Method getSchemaTypeInfo.
     * @return TypeInfo
     */
    public TypeInfo getSchemaTypeInfo() {
        return this;
    }

    /**
     * In Attribute objects, NodeValue is considered a synonym for
     * Value.
     *
     * @see #getValue()
     */
    public String getNodeValue() {
        return getValue();
    }

    /**
     * In Attributes, NodeName is considered a synonym for the
     * attribute's Name
     */
    public String getName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return name;
    }

    /**
     * The DOM doesn't clearly define what setValue(null) means. I've taken it
     * as "remove all children", which from outside should appear
     * similar to setting it to the empty string.
     */
    public void setValue(String newvalue) {
        CoreDocumentImpl ownerDocument = ownerDocument();
        if (ownerDocument.errorChecking && isReadOnly()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, msg);
        }
        Element ownerElement = getOwnerElement();
        String oldvalue = "";
        if (needsSyncData()) {
            synchronizeData();
        }
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        if (value != null) {
            if (ownerDocument.getMutationEvents()) {
                if (hasStringValue()) {
                    oldvalue = (String) value;
                    if (textNode == null) {
                        textNode = (TextImpl) ownerDocument.createTextNode((String) value);
                    } else {
                        textNode.data = (String) value;
                    }
                    value = textNode;
                    textNode.isFirstChild(true);
                    textNode.previousSibling = textNode;
                    textNode.ownerNode = this;
                    textNode.isOwned(true);
                    hasStringValue(false);
                    internalRemoveChild(textNode, true);
                } else {
                    oldvalue = getValue();
                    while (value != null) {
                        internalRemoveChild((Node) value, true);
                    }
                }
            } else {
                if (hasStringValue()) {
                    oldvalue = (String) value;
                } else {
                    oldvalue = getValue();
                    ChildNode firstChild = (ChildNode) value;
                    firstChild.previousSibling = null;
                    firstChild.isFirstChild(false);
                    firstChild.ownerNode = ownerDocument;
                }
                value = null;
                needsSyncChildren(false);
            }
            if (isIdAttribute() && ownerElement != null) {
                ownerDocument.removeIdentifier(oldvalue);
            }
        }
        isSpecified(true);
        if (ownerDocument.getMutationEvents()) {
            internalInsertBefore(ownerDocument.createTextNode(newvalue), null, true);
            hasStringValue(false);
            ownerDocument.modifiedAttrValue(this, oldvalue);
        } else {
            value = newvalue;
            hasStringValue(true);
            changed();
        }
        if (isIdAttribute() && ownerElement != null) {
            ownerDocument.putIdentifier(newvalue, ownerElement);
        }
    }

    /**
     * The "string value" of an Attribute is its text representation,
     * which in turn is a concatenation of the string values of its children.
     */
    public String getValue() {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        if (value == null) {
            return "";
        }
        if (hasStringValue()) {
            return (String) value;
        }
        ChildNode firstChild = ((ChildNode) value);
        String data = null;
        if (firstChild.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
            data = ((EntityReferenceImpl) firstChild).getEntityRefValue();
        } else {
            data = firstChild.getNodeValue();
        }
        ChildNode node = firstChild.nextSibling;
        if (node == null || data == null) return (data == null) ? "" : data;
        StringBuffer value = new StringBuffer(data);
        while (node != null) {
            if (node.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
                data = ((EntityReferenceImpl) node).getEntityRefValue();
                if (data == null) return "";
                value.append(data);
            } else {
                value.append(node.getNodeValue());
            }
            node = node.nextSibling;
        }
        return value.toString();
    }

    /**
     * The "specified" flag is true if and only if this attribute's
     * value was explicitly specified in the original document. Note that
     * the implementation, not the user, is in charge of this
     * property. If the user asserts an Attribute value (even if it ends
     * up having the same value as the default), it is considered a
     * specified attribute. If you really want to revert to the default,
     * delete the attribute from the Element, and the Implementation will
     * re-assert the default (if any) in its place, with the appropriate
     * specified=false setting.
     */
    public boolean getSpecified() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return isSpecified();
    }

    /**
     * Returns the element node that this attribute is associated with,
     * or null if the attribute has not been added to an element.
     *
     * @see #getOwnerElement
     *
     * @deprecated Previous working draft of DOM Level 2. New method
     *             is <tt>getOwnerElement()</tt>.
     */
    public Element getElement() {
        return (Element) (isOwned() ? ownerNode : null);
    }

    /**
     * Returns the element node that this attribute is associated with,
     * or null if the attribute has not been added to an element.
     *
     * @since WD-DOM-Level-2-19990719
     */
    public Element getOwnerElement() {
        return (Element) (isOwned() ? ownerNode : null);
    }

    public void normalize() {
        if (isNormalized() || hasStringValue()) return;
        Node kid, next;
        ChildNode firstChild = (ChildNode) value;
        for (kid = firstChild; kid != null; kid = next) {
            next = kid.getNextSibling();
            if (kid.getNodeType() == Node.TEXT_NODE) {
                if (next != null && next.getNodeType() == Node.TEXT_NODE) {
                    ((Text) kid).appendData(next.getNodeValue());
                    removeChild(next);
                    next = kid;
                } else {
                    if (kid.getNodeValue() == null || kid.getNodeValue().length() == 0) {
                        removeChild(kid);
                    }
                }
            }
        }
        isNormalized(true);
    }

    /** NON-DOM, for use by parser */
    public void setSpecified(boolean arg) {
        if (needsSyncData()) {
            synchronizeData();
        }
        isSpecified(arg);
    }

    /**
	 * NON-DOM: used by the parser
	 * @param type
	 */
    public void setType(Object type) {
        this.type = type;
    }

    /** NON-DOM method for debugging convenience */
    public String toString() {
        return getName() + "=" + "\"" + getValue() + "\"";
    }

    /**
     * Test whether this node has any children. Convenience shorthand
     * for (Node.getFirstChild()!=null)
     */
    public boolean hasChildNodes() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return value != null;
    }

    /**
     * Obtain a NodeList enumerating all children of this node. If there
     * are none, an (initially) empty NodeList is returned.
     * <p>
     * NodeLists are "live"; as children are added/removed the NodeList
     * will immediately reflect those changes. Also, the NodeList refers
     * to the actual nodes, so changes to those nodes made via the DOM tree
     * will be reflected in the NodeList and vice versa.
     * <p>
     * In this implementation, Nodes implement the NodeList interface and
     * provide their own getChildNodes() support. Other DOMs may solve this
     * differently.
     */
    public NodeList getChildNodes() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return this;
    }

    /** The first child of this Node, or null if none. */
    public NodeImpl getFirstChild() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        makeChildNode();
        return (NodeImpl) value;
    }

    /** The last child of this Node, or null if none. */
    public NodeImpl getLastChild() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return lastChild();
    }

    final ChildNode lastChild() {
        makeChildNode();
        return value != null ? ((ChildNode) value).previousSibling : null;
    }

    final void lastChild(ChildNode node) {
        if (value != null) {
            ((ChildNode) value).previousSibling = node;
        }
    }

    /**
     * Move one or more node(s) to our list of children. Note that this
     * implicitly removes them from their previous parent.
     *
     * @param newChild The Node to be moved to our subtree. As a
     * convenience feature, inserting a DocumentNode will instead insert
     * all its children.
     *
     * @param refChild Current child which newChild should be placed
     * immediately before. If refChild is null, the insertion occurs
     * after all existing Nodes, like appendChild().
     *
     * @return newChild, in its new state (relocated, or emptied in the case of
     * DocumentNode.)
     *
     * @throws DOMException(HIERARCHY_REQUEST_ERR) if newChild is of a
     * type that shouldn't be a child of this node, or if newChild is an
     * ancestor of this node.
     *
     * @throws DOMException(WRONG_DOCUMENT_ERR) if newChild has a
     * different owner document than we do.
     *
     * @throws DOMException(NOT_FOUND_ERR) if refChild is not a child of
     * this node.
     *
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if this node is
     * read-only.
     */
    public NodeImpl insertBefore(Node newChild, Node refChild) throws DOMException {
        return internalInsertBefore(newChild, refChild, false);
    }

    /** NON-DOM INTERNAL: Within DOM actions,we sometimes need to be able
     * to control which mutation events are spawned. This version of the
     * insertBefore operation allows us to do so. It is not intended
     * for use by application programs.
     */
    NodeImpl internalInsertBefore(Node newChild, Node refChild, boolean replace) throws DOMException {
        CoreDocumentImpl ownerDocument = ownerDocument();
        boolean errorChecking = ownerDocument.errorChecking;
        if (newChild.getNodeType() == Node.DOCUMENT_FRAGMENT_NODE) {
            if (errorChecking) {
                for (Node kid = newChild.getFirstChild(); kid != null; kid = kid.getNextSibling()) {
                    if (!ownerDocument.isKidOK(this, kid)) {
                        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null);
                        throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, msg);
                    }
                }
            }
            while (newChild.hasChildNodes()) {
                insertBefore(newChild.getFirstChild(), refChild);
            }
            return (NodeImpl) newChild;
        }
        if (newChild == refChild) {
            refChild = refChild.getNextSibling();
            removeChild(newChild);
            insertBefore(newChild, refChild);
            return (NodeImpl) newChild;
        }
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        if (errorChecking) {
            if (isReadOnly()) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, msg);
            }
            if (newChild.getOwnerDocument() != ownerDocument) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null);
                throw new DOMException(DOMException.WRONG_DOCUMENT_ERR, msg);
            }
            if (!ownerDocument.isKidOK(this, newChild)) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null);
                throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, msg);
            }
            if (refChild != null && refChild.getParentNode() != this) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
                throw new DOMException(DOMException.NOT_FOUND_ERR, msg);
            }
            boolean treeSafe = true;
            for (NodeImpl a = this; treeSafe && a != null; a = a.parentNode()) {
                treeSafe = newChild != a;
            }
            if (!treeSafe) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null);
                throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, msg);
            }
        }
        makeChildNode();
        ownerDocument.insertingNode(this, replace);
        ChildNode newInternal = (ChildNode) newChild;
        Node oldparent = newInternal.parentNode();
        if (oldparent != null) {
            oldparent.removeChild(newInternal);
        }
        ChildNode refInternal = (ChildNode) refChild;
        newInternal.ownerNode = this;
        newInternal.isOwned(true);
        ChildNode firstChild = (ChildNode) value;
        if (firstChild == null) {
            value = newInternal;
            newInternal.isFirstChild(true);
            newInternal.previousSibling = newInternal;
        } else {
            if (refInternal == null) {
                ChildNode lastChild = firstChild.previousSibling;
                lastChild.nextSibling = newInternal;
                newInternal.previousSibling = lastChild;
                firstChild.previousSibling = newInternal;
            } else {
                if (refChild == firstChild) {
                    firstChild.isFirstChild(false);
                    newInternal.nextSibling = firstChild;
                    newInternal.previousSibling = firstChild.previousSibling;
                    firstChild.previousSibling = newInternal;
                    value = newInternal;
                    newInternal.isFirstChild(true);
                } else {
                    ChildNode prev = refInternal.previousSibling;
                    newInternal.nextSibling = refInternal;
                    prev.nextSibling = newInternal;
                    refInternal.previousSibling = newInternal;
                    newInternal.previousSibling = prev;
                }
            }
        }
        changed();
        ownerDocument.insertedNode(this, newInternal, replace);
        checkNormalizationAfterInsert(newInternal);
        return (NodeImpl) newChild;
    }

    /**
     * Remove a child from this Node. The removed child's subtree
     * remains intact so it may be re-inserted elsewhere.
     *
     * @return oldChild, in its new state (removed).
     *
     * @throws DOMException(NOT_FOUND_ERR) if oldChild is not a child of
     * this node.
     *
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if this node is
     * read-only.
     */
    public NodeImpl removeChild(Node oldChild) throws DOMException {
        if (hasStringValue()) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
            throw new DOMException(DOMException.NOT_FOUND_ERR, msg);
        }
        return internalRemoveChild(oldChild, false);
    }

    /** NON-DOM INTERNAL: Within DOM actions,we sometimes need to be able
     * to control which mutation events are spawned. This version of the
     * removeChild operation allows us to do so. It is not intended
     * for use by application programs.
     */
    NodeImpl internalRemoveChild(Node oldChild, boolean replace) throws DOMException {
        CoreDocumentImpl ownerDocument = ownerDocument();
        if (ownerDocument.errorChecking) {
            if (isReadOnly()) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, msg);
            }
            if (oldChild != null && oldChild.getParentNode() != this) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null);
                throw new DOMException(DOMException.NOT_FOUND_ERR, msg);
            }
        }
        ChildNode oldInternal = (ChildNode) oldChild;
        ownerDocument.removingNode(this, oldInternal, replace);
        if (oldInternal == value) {
            oldInternal.isFirstChild(false);
            value = oldInternal.nextSibling;
            ChildNode firstChild = (ChildNode) value;
            if (firstChild != null) {
                firstChild.isFirstChild(true);
                firstChild.previousSibling = oldInternal.previousSibling;
            }
        } else {
            ChildNode prev = oldInternal.previousSibling;
            ChildNode next = oldInternal.nextSibling;
            prev.nextSibling = next;
            if (next == null) {
                ChildNode firstChild = (ChildNode) value;
                firstChild.previousSibling = prev;
            } else {
                next.previousSibling = prev;
            }
        }
        ChildNode oldPreviousSibling = oldInternal.previousSibling();
        oldInternal.ownerNode = ownerDocument;
        oldInternal.isOwned(false);
        oldInternal.nextSibling = null;
        oldInternal.previousSibling = null;
        changed();
        ownerDocument.removedNode(this, replace);
        checkNormalizationAfterRemove(oldPreviousSibling);
        return oldInternal;
    }

    /**
     * Make newChild occupy the location that oldChild used to
     * have. Note that newChild will first be removed from its previous
     * parent, if any. Equivalent to inserting newChild before oldChild,
     * then removing oldChild.
     *
     * @return oldChild, in its new state (removed).
     *
     * @throws DOMException(HIERARCHY_REQUEST_ERR) if newChild is of a
     * type that shouldn't be a child of this node, or if newChild is
     * one of our ancestors.
     *
     * @throws DOMException(WRONG_DOCUMENT_ERR) if newChild has a
     * different owner document than we do.
     *
     * @throws DOMException(NOT_FOUND_ERR) if oldChild is not a child of
     * this node.
     *
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if this node is
     * read-only.
     */
    public NodeImpl replaceChild(Node newChild, Node oldChild) throws DOMException {
        makeChildNode();
        CoreDocumentImpl ownerDocument = ownerDocument();
        ownerDocument.replacingNode(this);
        internalInsertBefore(newChild, oldChild, true);
        if (newChild != oldChild) {
            internalRemoveChild(oldChild, true);
        }
        ownerDocument.replacedNode(this);
        return (NodeImpl) oldChild;
    }

    /**
     * NodeList method: Count the immediate children of this node
     * @return int
     */
    public int getLength() {
        if (hasStringValue()) {
            return 1;
        }
        ChildNode node = (ChildNode) value;
        int length = 0;
        for (; node != null; node = node.nextSibling) {
            length++;
        }
        return length;
    }

    /**
     * NodeList method: Return the Nth immediate child of this node, or
     * null if the index is out of bounds.
     * @return org.w3c.dom.Node
     * @param index int
     */
    public NodeImpl item(int index) {
        if (hasStringValue()) {
            if (index != 0 || value == null) {
                return null;
            } else {
                makeChildNode();
                return (NodeImpl) value;
            }
        }
        if (index < 0) {
            return null;
        }
        ChildNode node = (ChildNode) value;
        for (int i = 0; i < index && node != null; i++) {
            node = node.nextSibling;
        }
        return node;
    }

    /**
     * DOM Level 3 WD- Experimental.
     * Override inherited behavior from ParentNode to support deep equal.
     * isEqualNode is always deep on Attr nodes.
     */
    public boolean isEqualNode(Node arg) {
        return super.isEqualNode(arg);
    }

    /**
     * Introduced in DOM Level 3. <p>
     * Checks if a type is derived from another by restriction. See:
     * http://www.w3.org/TR/DOM-Level-3-Core/core.html#TypeInfo-isDerivedFrom
     * 
     * @param typeNamespaceArg 
     *        The namspace of the ancestor type declaration
     * @param typeNameArg
     *        The name of the ancestor type declaration
     * @param derivationMethod
     *        The derivation method
     * 
     * @return boolean True if the type is derived by restriciton for the
     *         reference type
     */
    public boolean isDerivedFrom(String typeNamespaceArg, String typeNameArg, int derivationMethod) {
        return false;
    }

    /**
     * Override default behavior so that if deep is true, children are also
     * toggled.
     * @see Node
     * <P>
     * Note: this will not change the state of an EntityReference or its
     * children, which are always read-only.
     */
    public void setReadOnly(boolean readOnly, boolean deep) {
        super.setReadOnly(readOnly, deep);
        if (deep) {
            if (needsSyncChildren()) {
                synchronizeChildren();
            }
            if (hasStringValue()) {
                return;
            }
            for (ChildNode mykid = (ChildNode) value; mykid != null; mykid = mykid.nextSibling) {
                if (mykid.getNodeType() != Node.ENTITY_REFERENCE_NODE) {
                    mykid.setReadOnly(readOnly, true);
                }
            }
        }
    }

    /**
     * Override this method in subclass to hook in efficient
     * internal data structure.
     */
    protected void synchronizeChildren() {
        needsSyncChildren(false);
    }

    /**
     * Checks the normalized state of this node after inserting a child.
     * If the inserted child causes this node to be unnormalized, then this
     * node is flagged accordingly.
     * The conditions for changing the normalized state are:
     * <ul>
     * <li>The inserted child is a text node and one of its adjacent siblings
     * is also a text node.
     * <li>The inserted child is is itself unnormalized.
     * </ul>
     *
     * @param insertedChild the child node that was inserted into this node
     *
     * @throws NullPointerException if the inserted child is <code>null</code>
     */
    void checkNormalizationAfterInsert(ChildNode insertedChild) {
        if (insertedChild.getNodeType() == Node.TEXT_NODE) {
            ChildNode prev = insertedChild.previousSibling();
            ChildNode next = insertedChild.nextSibling;
            if ((prev != null && prev.getNodeType() == Node.TEXT_NODE) || (next != null && next.getNodeType() == Node.TEXT_NODE)) {
                isNormalized(false);
            }
        } else {
            if (!insertedChild.isNormalized()) {
                isNormalized(false);
            }
        }
    }

    /**
     * Checks the normalized of this node after removing a child.
     * If the removed child causes this node to be unnormalized, then this
     * node is flagged accordingly.
     * The conditions for changing the normalized state are:
     * <ul>
     * <li>The removed child had two adjacent siblings that were text nodes.
     * </ul>
     *
     * @param previousSibling the previous sibling of the removed child, or
     * <code>null</code>
     */
    void checkNormalizationAfterRemove(ChildNode previousSibling) {
        if (previousSibling != null && previousSibling.getNodeType() == Node.TEXT_NODE) {
            ChildNode next = previousSibling.nextSibling;
            if (next != null && next.getNodeType() == Node.TEXT_NODE) {
                isNormalized(false);
            }
        }
    }

    /** Serialize object. */
    private void writeObject(ObjectOutputStream out) throws IOException {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        out.defaultWriteObject();
    }

    /** Deserialize object. */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        needsSyncChildren(false);
    }
}
