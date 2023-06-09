package org.openrtk.idl.epp0705;

/**
 * Class that contains the elements generic to all the check command responses.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0705/epp_CheckResult.java,v 1.1 2003/03/20 22:42:17 tubadanm Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2003/03/20 22:42:17 $<br>
 * @see org.openrtk.idl.epp0705.domain.epp_DomainCheckRsp#getResults()
 * @see org.openrtk.idl.epp0705.host.epp_HostCheckRsp#getResults()
 * @see org.openrtk.idl.epp0705.contact.epp_ContactCheckRsp#getResults()
 */
public class epp_CheckResult implements org.omg.CORBA.portable.IDLEntity {

    /**
   * The boolean value indicating whether an object is available in the registry or not.
   * @see #setAvail(boolean)
   * @see #getAvail()
   */
    public boolean m_avail = false;

    /**
   * The identifier or name of the object checked in the registry.
   * @see #setValue(String)
   * @see #getValue()
   */
    public String m_value = null;

    /**
   * Server-specific text to explain why the object is unavailable
   * Max length 32 by the XSD specifications
   * @see #setReason(String)
   * @see #getReason()
   */
    public String m_reason = null;

    /**
   * The language for the reason text, default "en"
   * @see #setLang(String)
   * @see #getLang()
   */
    public String m_lang = null;

    /**
   * Empty constructor
   */
    public epp_CheckResult() {
    }

    /**
   * The constructor with initializing variables.
   * @param _m_exists The boolean value indicating whether an object exists in the registry or not
   * @param _m_value The identifier or name of the object checked in the registry
   * @deprecated Please use epp_CheckResult(String _m_value, boolean _m_exists, String _m_reason, String _m_lang) instead
   */
    public epp_CheckResult(boolean _m_exists, String _m_value) {
        m_avail = !_m_exists;
        m_value = _m_value;
    }

    /**
   * The constructor with initializing variables.
   * @param _m_value The identifier or name of the object checked in the registry
   * @param _m_avail The boolean value indicating whether an object is available or not
   * @param _m_reason The Server-specific text to explain why the object is unavailable
   * @param _m_lang The language of the reason text
   */
    public epp_CheckResult(String _m_value, boolean _m_avail, String _m_reason, String _m_lang) {
        m_value = _m_value;
        m_avail = _m_avail;
        m_reason = _m_reason;
        m_lang = _m_lang;
    }

    /**
   * Accessor method for the boolean value indicating whether an object exists in the registry or not
   * @param value The boolean value indicating whether an object exists in the registry or not
   * @deprecated Please use setAvail() instead
   */
    public void setExists(boolean value) {
        m_avail = !value;
    }

    /**
   * Accessor method for the boolean value indicating whether an object exists in the registry or not
   * @return The boolean value indicating whether an object exists in the registry or not
   * @deprecated Please use getAvail() instead
   */
    public boolean getExists() {
        return !m_avail;
    }

    /**
   * Accessor method for the boolean value indicating whether an object is available or not
   * @param value The boolean value indicating whether an object is available or not
   * @see #m_avail
   */
    public void setAvail(boolean value) {
        m_avail = value;
    }

    /**
   * Accessor method for the boolean value indicating whether an object is available or not
   * @return The boolean value indicating whether an object is available or not
   * @see #m_avail
   */
    public boolean getAvail() {
        return m_avail;
    }

    /**
   * Accessor method for the identifier or name of the object checked in the registry
   * @param value The identifier or name of the object
   * @see #m_value
   */
    public void setValue(String value) {
        m_value = value;
    }

    /**
   * Accessor method for the identifier or name of the object checked in the registry
   * @return The identifier or name of the object
   * @see #m_value
   */
    public String getValue() {
        return m_value;
    }

    /**
   * Accessor method for the reason text
   * @param value The reason text
   * @see #m_reason
   */
    public void setReason(String value) {
        m_reason = value;
    }

    /**
   * Accessor method for the reason text
   * @return The reason text
   * @see #m_reason
   */
    public String getReason() {
        return m_reason;
    }

    /**
   * Accessor method for the language of the reason text
   * @param value The language of the reason text
   * @see #m_lang
   */
    public void setLang(String value) {
        m_lang = value;
    }

    /**
   * Accessor method for the language of the reason text
   * @return The language of the reason text
   * @see #m_lang
   */
    public String getLang() {
        return m_lang;
    }

    /**
   * Converts this class into a string.
   * Typically used to view the object in debug output.
   * @return The string representation of this object instance
   */
    public String toString() {
        return this.getClass().getName() + ": { m_value [" + m_value + "] m_avail [" + m_avail + "] m_reason [" + m_reason + "] m_lang [" + m_lang + "] }";
    }
}
