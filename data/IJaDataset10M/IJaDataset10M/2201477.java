package Bibl;

/**
* Bibl/BibliotekaHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Biblioteka.idl
* poniedzia�ek, 2 kwiecie� 2007 00:47:33 CEST
*/
public final class BibliotekaHolder implements org.omg.CORBA.portable.Streamable {

    public Bibl.Biblioteka value = null;

    public BibliotekaHolder() {
    }

    public BibliotekaHolder(Bibl.Biblioteka initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = Bibl.BibliotekaHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        Bibl.BibliotekaHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return Bibl.BibliotekaHelper.type();
    }
}