package org.fudaa.dodico.corba.hydraulique1d.loi;

/**
* org/fudaa/dodico/corba/hydraulique1d/loi/VILoiSeuilHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/devel/fudaa/fudaa_devel/dodico/idl/metier/hydraulique1d.idl
* mercredi 6 ao�t 2008 22 h 27 CEST
*/
public final class VILoiSeuilHolder implements org.omg.CORBA.portable.Streamable {

    public org.fudaa.dodico.corba.hydraulique1d.loi.ILoiSeuil value[] = null;

    public VILoiSeuilHolder() {
    }

    public VILoiSeuilHolder(org.fudaa.dodico.corba.hydraulique1d.loi.ILoiSeuil[] initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = org.fudaa.dodico.corba.hydraulique1d.loi.VILoiSeuilHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        org.fudaa.dodico.corba.hydraulique1d.loi.VILoiSeuilHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.fudaa.dodico.corba.hydraulique1d.loi.VILoiSeuilHelper.type();
    }
}
