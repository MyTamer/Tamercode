package org.fudaa.dodico.corba.hydraulique1d.qualitedeau;

/**
* org/fudaa/dodico/corba/hydraulique1d/qualitedeau/LOptionDiffus.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/devel/fudaa/fudaa_devel/dodico/idl/metier/hydraulique1d.idl
* mercredi 6 ao�t 2008 22 h 27 CEST
*/
public class LOptionDiffus implements org.omg.CORBA.portable.IDLEntity {

    private int __value;

    private static int __size = 2;

    private static org.fudaa.dodico.corba.hydraulique1d.qualitedeau.LOptionDiffus[] __array = new org.fudaa.dodico.corba.hydraulique1d.qualitedeau.LOptionDiffus[__size];

    public static final int _K_C1U_C2 = 0;

    public static final org.fudaa.dodico.corba.hydraulique1d.qualitedeau.LOptionDiffus K_C1U_C2 = new org.fudaa.dodico.corba.hydraulique1d.qualitedeau.LOptionDiffus(_K_C1U_C2);

    public static final int _ISAWA_AGA = 1;

    public static final org.fudaa.dodico.corba.hydraulique1d.qualitedeau.LOptionDiffus ISAWA_AGA = new org.fudaa.dodico.corba.hydraulique1d.qualitedeau.LOptionDiffus(_ISAWA_AGA);

    public int value() {
        return __value;
    }

    public static org.fudaa.dodico.corba.hydraulique1d.qualitedeau.LOptionDiffus from_int(int value) {
        if (value >= 0 && value < __size) return __array[value]; else throw new org.omg.CORBA.BAD_PARAM();
    }

    protected LOptionDiffus(int value) {
        __value = value;
        __array[__value] = this;
    }
}
