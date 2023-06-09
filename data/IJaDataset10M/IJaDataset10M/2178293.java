package org.openrtk.idl.epp0503.host;

/**
 * Class that contains the elements necessary to create a host
 * in the registry.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0503/host/epp_HostCreateReq.java,v 1.2 2003/09/10 21:29:57 tubadanm Exp $<br>
 * $Revision: 1.2 $<br>
 * $Date: 2003/09/10 21:29:57 $<br>
 * @see com.tucows.oxrs.epp0503.rtk.xml.EPPHostCreate
 * @see org.openrtk.idl.epp0503.host.epp_HostCreateRsp
 */
public class epp_HostCreateReq implements org.omg.CORBA.portable.IDLEntity {

    /**
   * The common and generic command element.
   * @see #setCmd(org.openrtk.idl.epp0503.epp_Command)
   * @see #getCmd()
   */
    public org.openrtk.idl.epp0503.epp_Command m_cmd = null;

    /**
   * The name of the host object to be created in the registry.
   * @see #setName(String)
   * @see #getName()
   */
    public String m_name = null;

    /**
   * The array of IP addresses to be associated with the host object.
   * @see #setAddresses(org.openrtk.idl.epp0503.host.epp_HostAddress[])
   * @see #getAddresses()
   */
    public org.openrtk.idl.epp0503.host.epp_HostAddress m_addresses[] = null;

    /**
   * Empty constructor
   */
    public epp_HostCreateReq() {
    }

    /**
   * The constructor with initializing variables.
   * @param _m_cmd The common and generic command element
   * @param _m_name The name of the host object to be created
   * @param _m_addresses The array of IP addresses to be associated with the host object
   */
    public epp_HostCreateReq(org.openrtk.idl.epp0503.epp_Command _m_cmd, String _m_name, org.openrtk.idl.epp0503.host.epp_HostAddress[] _m_addresses) {
        m_cmd = _m_cmd;
        m_name = _m_name;
        m_addresses = _m_addresses;
    }

    /**
   * Accessor method for the common and generic command element
   * @param value The command element
   * @see #m_cmd
   */
    public void setCmd(org.openrtk.idl.epp0503.epp_Command value) {
        m_cmd = value;
    }

    /**
   * Accessor method for the common and generic command element
   * @return The command element
   * @see #m_cmd
   */
    public org.openrtk.idl.epp0503.epp_Command getCmd() {
        return m_cmd;
    }

    /**
   * Accessor method for the name of the host object to be created
   * @param value The host name
   * @see #m_name
   */
    public void setName(String value) {
        m_name = value;
    }

    /**
   * Accessor method for the name of the host object to be created
   * @return The host name
   * @see #m_name
   */
    public String getName() {
        return m_name;
    }

    /**
   * Accessor method for the array of IP addresses to be associated with the host object
   * @param value The array of host IP addresses
   * @see #m_addresses
   */
    public void setAddresses(org.openrtk.idl.epp0503.host.epp_HostAddress[] value) {
        m_addresses = value;
    }

    /**
   * Accessor method for the array of IP addresses to be associated with the host object
   * @return The array of host IP addresses
   * @see #m_addresses
   */
    public org.openrtk.idl.epp0503.host.epp_HostAddress[] getAddresses() {
        return m_addresses;
    }

    /**
   * Converts this class into a string.
   * Typically used to view the object in debug output.
   * @return The string representation of this object instance
   */
    public String toString() {
        return this.getClass().getName() + ": { m_cmd [" + m_cmd + "] m_name [" + m_name + "] m_addresses [" + (m_addresses != null ? java.util.Arrays.asList(m_addresses) : null) + "] }";
    }
}
