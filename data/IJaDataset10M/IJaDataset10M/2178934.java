package org.xmlsoap.schemas.soap.encoding.impl;

/**
 * An XML unsignedLong(@http://schemas.xmlsoap.org/soap/encoding/).
 *
 * This is an atomic type that is a restriction of org.xmlsoap.schemas.soap.encoding.UnsignedLong.
 */
public class UnsignedLongImpl extends org.apache.xmlbeans.impl.values.JavaIntegerHolderEx implements org.xmlsoap.schemas.soap.encoding.UnsignedLong {

    private static final javax.xml.namespace.QName ID$0 = new javax.xml.namespace.QName("", "id");

    private static final javax.xml.namespace.QName HREF$2 = new javax.xml.namespace.QName("", "href");

    public UnsignedLongImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType, true);
    }

    protected UnsignedLongImpl(org.apache.xmlbeans.SchemaType sType, boolean b) {
        super(sType, b);
    }

    /**
     * Gets the "id" attribute
     */
    public java.lang.String getId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(ID$0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "id" attribute
     */
    public org.apache.xmlbeans.XmlID xgetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID) get_store().find_attribute_user(ID$0);
            return target;
        }
    }

    /**
     * True if has "id" attribute
     */
    public boolean isSetId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(ID$0) != null;
        }
    }

    /**
     * Sets the "id" attribute
     */
    public void setId(java.lang.String id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(ID$0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(ID$0);
            }
            target.setStringValue(id);
        }
    }

    /**
     * Sets (as xml) the "id" attribute
     */
    public void xsetId(org.apache.xmlbeans.XmlID id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID) get_store().find_attribute_user(ID$0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlID) get_store().add_attribute_user(ID$0);
            }
            target.set(id);
        }
    }

    /**
     * Unsets the "id" attribute
     */
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ID$0);
        }
    }

    /**
     * Gets the "href" attribute
     */
    public java.lang.String getHref() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(HREF$2);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "href" attribute
     */
    public org.apache.xmlbeans.XmlAnyURI xgetHref() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI) get_store().find_attribute_user(HREF$2);
            return target;
        }
    }

    /**
     * True if has "href" attribute
     */
    public boolean isSetHref() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(HREF$2) != null;
        }
    }

    /**
     * Sets the "href" attribute
     */
    public void setHref(java.lang.String href) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(HREF$2);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(HREF$2);
            }
            target.setStringValue(href);
        }
    }

    /**
     * Sets (as xml) the "href" attribute
     */
    public void xsetHref(org.apache.xmlbeans.XmlAnyURI href) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI) get_store().find_attribute_user(HREF$2);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlAnyURI) get_store().add_attribute_user(HREF$2);
            }
            target.set(href);
        }
    }

    /**
     * Unsets the "href" attribute
     */
    public void unsetHref() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HREF$2);
        }
    }
}
