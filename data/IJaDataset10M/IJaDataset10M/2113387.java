package org.fudaa.dodico.corba.mascaret;

/**
* org/fudaa/dodico/corba/mascaret/SParametresZoneSeche.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/devel/fudaa/fudaa_devel/dodico/idl/code/mascaret.idl
* mercredi 14 janvier 2009 02 h 05 CET
*/
public final class SParametresZoneSeche implements org.omg.CORBA.portable.IDLEntity {

    public int nb = (int) 0;

    public int branche[] = null;

    public double absDebut[] = null;

    public double absFin[] = null;

    public SParametresZoneSeche() {
    }

    public SParametresZoneSeche(int _nb, int[] _branche, double[] _absDebut, double[] _absFin) {
        nb = _nb;
        branche = _branche;
        absDebut = _absDebut;
        absFin = _absFin;
    }
}
