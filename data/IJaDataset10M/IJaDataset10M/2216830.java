package org.openrtk.idl.epp02;

/**
* org/openrtk/idl/epp/epp_LoginRsp.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from epp.idl
* Thursday, July 26, 2001 6:26:04 PM EDT
*/
public class epp_LoginRsp implements org.omg.CORBA.portable.IDLEntity {

    public org.openrtk.idl.epp02.epp_Response m_rsp = null;

    public epp_LoginRsp() {
    }

    public epp_LoginRsp(org.openrtk.idl.epp02.epp_Response _m_rsp) {
        m_rsp = _m_rsp;
    }

    public void setRsp(org.openrtk.idl.epp02.epp_Response value) {
        m_rsp = value;
    }

    public org.openrtk.idl.epp02.epp_Response getRsp() {
        return m_rsp;
    }

    public String toString() {
        return this.getClass().getName() + ": { m_rsp [" + m_rsp + "] }";
    }
}
