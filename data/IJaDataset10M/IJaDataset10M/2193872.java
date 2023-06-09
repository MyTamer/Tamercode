package org.openrtk.idl.epp0503.contact;

/**
 * Class that contains the elements used to represent contact status with different type.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0503/contact/epp_ContactStatus.java,v 1.1 2003/03/21 16:18:28 tubadanm Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2003/03/21 16:18:28 $<br>
 * @see org.openrtk.idl.epp0503.contact.epp_ContactInfoRsp
 * @see org.openrtk.idl.epp0503.contact.epp_ContactUpdateAddRemove
 */
public class epp_ContactStatus implements org.omg.CORBA.portable.IDLEntity {

    /**
   * The type of the contact status.
   * @see #setType(org.openrtk.idl.epp0503.contact.epp_ContactStatusType)
   * @see #getType()
   */
    public org.openrtk.idl.epp0503.contact.epp_ContactStatusType m_type = null;

    /**
   * The language used to express the contact status value.
   * @see #setLang(String)
   * @see #getLang()
   */
    public String m_lang = null;

    /**
   * The value of the contact status.
   * @see #setValue(String)
   * @see #getValue()
   */
    public String m_value = null;

    /**
   * Empty constructor
   */
    public epp_ContactStatus() {
    }

    /**
   * The constructor with initializing variables.
   * @param _m_type The type of the contact status
   * @param _m_lang The language used to express the contact status value
   * @param _m_value The value of the contact status
   */
    public epp_ContactStatus(org.openrtk.idl.epp0503.contact.epp_ContactStatusType _m_type, String _m_lang, String _m_value) {
        m_type = _m_type;
        m_lang = _m_lang;
        m_value = _m_value;
    }

    /**
   * Accessor method for the type of the contact status
   * @param value The contact status type
   * @see #m_type
   */
    public void setType(org.openrtk.idl.epp0503.contact.epp_ContactStatusType value) {
        m_type = value;
    }

    /**
   * Accessor method for the type of the contact status
   * @return The contact status type
   * @see #m_type
   */
    public org.openrtk.idl.epp0503.contact.epp_ContactStatusType getType() {
        return m_type;
    }

    /**
   * Accessor method for the language used to express the contact status value
   * @param value The language of the status value
   * @see #m_lang
   */
    public void setLang(String value) {
        m_lang = value;
    }

    /**
   * Accessor method for the language used to express the contact status value
   * @return The language of the status value
   * @see #m_lang
   */
    public String getLang() {
        return m_lang;
    }

    /**
   * Accessor method for the value of the contact status
   * @param value The contact status value
   * @see #m_value
   */
    public void setValue(String value) {
        m_value = value;
    }

    /**
   * Accessor method for the value of the contact status
   * @return The contact status value
   * @see #m_value
   */
    public String getValue() {
        return m_value;
    }

    /**
   * Converts this class into a string.
   * Typically used to view the object in debug output.
   * @return The string representation of this object instance
   */
    public String toString() {
        return this.getClass().getName() + ": { m_type [" + m_type + "] m_lang [" + m_lang + "] m_value [" + m_value + "] }";
    }
}
