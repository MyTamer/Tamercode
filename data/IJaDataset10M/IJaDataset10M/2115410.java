package org.fudaa.dodico.corba.sinavi3;

/**
* org/fudaa/dodico/corba/sinavi3/Sinavi3ResultatListeevenementsSimuHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/Documents and Settings/mederic.fargeix/Mes documents/developpement/fudaa/fudaa_devel/dodico/idl/code/sinavi3.idl
* mercredi 20 janvier 2010 10 h 13 CET
*/
public abstract class Sinavi3ResultatListeevenementsSimuHelper {

    private static String _id = "IDL:sinavi3/Sinavi3ResultatListeevenementsSimu:1.0";

    public static void insert(org.omg.CORBA.Any a, org.fudaa.dodico.corba.sinavi3.Sinavi3ResultatListeevenementsSimu that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static org.fudaa.dodico.corba.sinavi3.Sinavi3ResultatListeevenementsSimu extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;

    private static boolean __active = false;

    public static synchronized org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            synchronized (org.omg.CORBA.TypeCode.class) {
                if (__typeCode == null) {
                    if (__active) {
                        return org.omg.CORBA.ORB.init().create_recursive_tc(_id);
                    }
                    __active = true;
                    org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember[2];
                    org.omg.CORBA.TypeCode _tcOf_members0 = null;
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_long);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.EntierHelper.id(), "Entier", _tcOf_members0);
                    _members0[0] = new org.omg.CORBA.StructMember("nombreNavires", _tcOf_members0, null);
                    _tcOf_members0 = org.fudaa.dodico.corba.sinavi3.Sinavi3ResultatsSimulationHelper.type();
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_sequence_tc(0, _tcOf_members0);
                    _members0[1] = new org.omg.CORBA.StructMember("listeEvenements", _tcOf_members0, null);
                    __typeCode = org.omg.CORBA.ORB.init().create_struct_tc(org.fudaa.dodico.corba.sinavi3.Sinavi3ResultatListeevenementsSimuHelper.id(), "Sinavi3ResultatListeevenementsSimu", _members0);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static org.fudaa.dodico.corba.sinavi3.Sinavi3ResultatListeevenementsSimu read(org.omg.CORBA.portable.InputStream istream) {
        org.fudaa.dodico.corba.sinavi3.Sinavi3ResultatListeevenementsSimu value = new org.fudaa.dodico.corba.sinavi3.Sinavi3ResultatListeevenementsSimu();
        value.nombreNavires = istream.read_long();
        int _len0 = istream.read_long();
        value.listeEvenements = new org.fudaa.dodico.corba.sinavi3.Sinavi3ResultatsSimulation[_len0];
        for (int _o1 = 0; _o1 < value.listeEvenements.length; ++_o1) value.listeEvenements[_o1] = org.fudaa.dodico.corba.sinavi3.Sinavi3ResultatsSimulationHelper.read(istream);
        return value;
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.fudaa.dodico.corba.sinavi3.Sinavi3ResultatListeevenementsSimu value) {
        ostream.write_long(value.nombreNavires);
        ostream.write_long(value.listeEvenements.length);
        for (int _i0 = 0; _i0 < value.listeEvenements.length; ++_i0) org.fudaa.dodico.corba.sinavi3.Sinavi3ResultatsSimulationHelper.write(ostream, value.listeEvenements[_i0]);
    }
}