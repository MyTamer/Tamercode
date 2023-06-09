package org.openrtk.idl.epprtk.domain;

/**
 * Class that contains the elements necessary to modify a domain
 * in the registry.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epprtk/domain/epp_DomainUpdateReq.java,v 1.1 2004/12/07 15:27:50 ewang2004 Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2004/12/07 15:27:50 $<br>
 * @see com.tucows.oxrs.epprtk.rtk.xml.EPPDomainUpdate
 * @see org.openrtk.idl.epprtk.domain.epp_DomainUpdateRsp
 */
public class epp_DomainUpdateReq implements org.omg.CORBA.portable.IDLEntity {

    /**
   * The common and generic command element.
   * @see #setCmd(org.openrtk.idl.epprtk.epp_Command)
   * @see #getCmd()
   */
    public org.openrtk.idl.epprtk.epp_Command m_cmd = null;

    /**
   * The name of the domain object to be modified in the registry.
   * @see #setName(String)
   * @see #getName()
   */
    public String m_name = null;

    /**
   * The add element that contains attribute values to be added to the domain object.
   * @see #setAdd(org.openrtk.idl.epprtk.domain.epp_DomainUpdateAddRemove)
   * @see #getAdd()
   */
    public org.openrtk.idl.epprtk.domain.epp_DomainUpdateAddRemove m_add = null;

    /**
   * The change element that contains attribute values to be changed in the domain object.
   * @see #setChange(org.openrtk.idl.epprtk.domain.epp_DomainUpdateChange)
   * @see #getChange()
   */
    public org.openrtk.idl.epprtk.domain.epp_DomainUpdateChange m_change = null;

    /**
   * The remove element that contains attribute values to be removed from the domain object.
   * @see #setRemove(org.openrtk.idl.epprtk.domain.epp_DomainUpdateAddRemove)
   * @see #getRemove()
   */
    public org.openrtk.idl.epprtk.domain.epp_DomainUpdateAddRemove m_remove = null;

    /**
   * Empty constructor
   */
    public epp_DomainUpdateReq() {
    }

    /**
   * The constructor with initializing variables.
   * @param _m_cmd The common and generic command element
   * @param _m_name The name of the domain object to be modified in the registry
   * @param _m_add The add element that contains attribute values to be added to the domain object
   * @param _m_change The change element that contains attribute values to be changed
   * @param _m_remove The remove element that contains attribute values to be removed from the domain object
   */
    public epp_DomainUpdateReq(org.openrtk.idl.epprtk.epp_Command _m_cmd, String _m_name, org.openrtk.idl.epprtk.domain.epp_DomainUpdateAddRemove _m_add, org.openrtk.idl.epprtk.domain.epp_DomainUpdateChange _m_change, org.openrtk.idl.epprtk.domain.epp_DomainUpdateAddRemove _m_remove) {
        m_cmd = _m_cmd;
        m_name = _m_name;
        m_add = _m_add;
        m_change = _m_change;
        m_remove = _m_remove;
    }

    /**
   * Accessor method for the common and generic command element
   * @param value The command element
   * @see #m_cmd
   */
    public void setCmd(org.openrtk.idl.epprtk.epp_Command value) {
        m_cmd = value;
    }

    /**
   * Accessor method for the common and generic command element
   * @return The command element
   * @see #m_cmd
   */
    public org.openrtk.idl.epprtk.epp_Command getCmd() {
        return m_cmd;
    }

    /**
   * Accessor method for the name of the domain object to be modified in the registry
   * @param value The domain name
   * @see #m_name
   */
    public void setName(String value) {
        m_name = value;
    }

    /**
   * Accessor method for the name of the domain object to be modified in the registry
   * @return The domain name
   * @see #m_name
   */
    public String getName() {
        return m_name;
    }

    /**
   * Accessor method for the add element that contains attribute values to be added to the domain object
   * @param value The add element
   * @see #m_add
   */
    public void setAdd(org.openrtk.idl.epprtk.domain.epp_DomainUpdateAddRemove value) {
        m_add = value;
    }

    /**
   * Accessor method for the add element that contains attribute values to be added to the domain object
   * @return The add element
   * @see #m_add
   */
    public org.openrtk.idl.epprtk.domain.epp_DomainUpdateAddRemove getAdd() {
        return m_add;
    }

    /**
   * Accessor method for the change element that contains attribute values to be changed
   * @param value The change element
   * @see #m_change
   */
    public void setChange(org.openrtk.idl.epprtk.domain.epp_DomainUpdateChange value) {
        m_change = value;
    }

    /**
   * Accessor method for the change element that contains attribute values to be changed
   * @return The change element
   * @see #m_change
   */
    public org.openrtk.idl.epprtk.domain.epp_DomainUpdateChange getChange() {
        return m_change;
    }

    /**
   * Accessor method for the remove element that contains attribute values to be removed from the domain object
   * @param value The remove element
   * @see #m_remove
   */
    public void setRemove(org.openrtk.idl.epprtk.domain.epp_DomainUpdateAddRemove value) {
        m_remove = value;
    }

    /**
   * Accessor method for the remove element that contains attribute values to be removed from the domain object
   * @return The remove element
   * @see #m_remove
   */
    public org.openrtk.idl.epprtk.domain.epp_DomainUpdateAddRemove getRemove() {
        return m_remove;
    }

    /**
   * Converts this class into a string.
   * Typically used to view the object in debug output.
   * @return The string representation of this object instance
   */
    public String toString() {
        return this.getClass().getName() + ": { m_cmd [" + m_cmd + "] m_name [" + m_name + "] m_add [" + m_add + "] m_change [" + m_change + "] m_remove [" + m_remove + "] }";
    }
}
