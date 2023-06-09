package org.fudaa.dodico.corba.sipor;

/**
* org/fudaa/dodico/corba/sipor/SParametresHorairesNavires2Helper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/devel/fudaa/fudaa_devel/dodico/idl/code/sipor.idl
* mercredi 6 ao�t 2008 22 h 27 CEST
*/
public abstract class SParametresHorairesNavires2Helper {

    private static String _id = "IDL:sipor/SParametresHorairesNavires2:1.0";

    public static void insert(org.omg.CORBA.Any a, org.fudaa.dodico.corba.sipor.SParametresHorairesNavires2 that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static org.fudaa.dodico.corba.sipor.SParametresHorairesNavires2 extract(org.omg.CORBA.Any a) {
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
                    org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember[48];
                    org.omg.CORBA.TypeCode _tcOf_members0 = null;
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[0] = new org.omg.CORBA.StructMember("lundiCreneau1HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[1] = new org.omg.CORBA.StructMember("lundiCreneau1HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[2] = new org.omg.CORBA.StructMember("lundiCreneau2HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[3] = new org.omg.CORBA.StructMember("lundiCreneau2HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[4] = new org.omg.CORBA.StructMember("lundiCreneau3HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[5] = new org.omg.CORBA.StructMember("lundiCreneau3HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[6] = new org.omg.CORBA.StructMember("mardiCreneau1HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[7] = new org.omg.CORBA.StructMember("mardiCreneau1HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[8] = new org.omg.CORBA.StructMember("mardiCreneau2HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[9] = new org.omg.CORBA.StructMember("mardiCreneau2HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[10] = new org.omg.CORBA.StructMember("mardiCreneau3HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[11] = new org.omg.CORBA.StructMember("mardiCreneau3HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[12] = new org.omg.CORBA.StructMember("mercrediCreneau1HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[13] = new org.omg.CORBA.StructMember("mercrediCreneau1HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[14] = new org.omg.CORBA.StructMember("mercrediCreneau2HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[15] = new org.omg.CORBA.StructMember("mercrediCreneau2HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[16] = new org.omg.CORBA.StructMember("mercrediCreneau3HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[17] = new org.omg.CORBA.StructMember("mercrediCreneau3HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[18] = new org.omg.CORBA.StructMember("jeudiCreneau1HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[19] = new org.omg.CORBA.StructMember("jeudiCreneau1HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[20] = new org.omg.CORBA.StructMember("jeudiCreneau2HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[21] = new org.omg.CORBA.StructMember("jeudiCreneau2HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[22] = new org.omg.CORBA.StructMember("jeudiCreneau3HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[23] = new org.omg.CORBA.StructMember("jeudiCreneau3HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[24] = new org.omg.CORBA.StructMember("vendrediCreneau1HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[25] = new org.omg.CORBA.StructMember("vendrediCreneau1HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[26] = new org.omg.CORBA.StructMember("vendrediCreneau2HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[27] = new org.omg.CORBA.StructMember("vendrediCreneau2HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[28] = new org.omg.CORBA.StructMember("vendrediCreneau3HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[29] = new org.omg.CORBA.StructMember("vendrediCreneau3HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[30] = new org.omg.CORBA.StructMember("samediCreneau1HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[31] = new org.omg.CORBA.StructMember("samediCreneau1HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[32] = new org.omg.CORBA.StructMember("samediCreneau2HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[33] = new org.omg.CORBA.StructMember("samediCreneau2HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[34] = new org.omg.CORBA.StructMember("samediCreneau3HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[35] = new org.omg.CORBA.StructMember("samediCreneau3HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[36] = new org.omg.CORBA.StructMember("dimancheCreneau1HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[37] = new org.omg.CORBA.StructMember("dimancheCreneau1HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[38] = new org.omg.CORBA.StructMember("dimancheCreneau2HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[39] = new org.omg.CORBA.StructMember("dimancheCreneau2HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[40] = new org.omg.CORBA.StructMember("dimancheCreneau3HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[41] = new org.omg.CORBA.StructMember("dimancheCreneau3HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[42] = new org.omg.CORBA.StructMember("ferieCreneau1HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[43] = new org.omg.CORBA.StructMember("ferieCreneau1HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[44] = new org.omg.CORBA.StructMember("ferieCreneau2HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[45] = new org.omg.CORBA.StructMember("ferieCreneau2HeureArrivee", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[46] = new org.omg.CORBA.StructMember("ferieCreneau3HeureDep", _tcOf_members0, null);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_double);
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_alias_tc(org.fudaa.dodico.corba.base.ReelHelper.id(), "Reel", _tcOf_members0);
                    _members0[47] = new org.omg.CORBA.StructMember("ferieCreneau3HeureArrivee", _tcOf_members0, null);
                    __typeCode = org.omg.CORBA.ORB.init().create_struct_tc(org.fudaa.dodico.corba.sipor.SParametresHorairesNavires2Helper.id(), "SParametresHorairesNavires2", _members0);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static org.fudaa.dodico.corba.sipor.SParametresHorairesNavires2 read(org.omg.CORBA.portable.InputStream istream) {
        org.fudaa.dodico.corba.sipor.SParametresHorairesNavires2 value = new org.fudaa.dodico.corba.sipor.SParametresHorairesNavires2();
        value.lundiCreneau1HeureDep = istream.read_double();
        value.lundiCreneau1HeureArrivee = istream.read_double();
        value.lundiCreneau2HeureDep = istream.read_double();
        value.lundiCreneau2HeureArrivee = istream.read_double();
        value.lundiCreneau3HeureDep = istream.read_double();
        value.lundiCreneau3HeureArrivee = istream.read_double();
        value.mardiCreneau1HeureDep = istream.read_double();
        value.mardiCreneau1HeureArrivee = istream.read_double();
        value.mardiCreneau2HeureDep = istream.read_double();
        value.mardiCreneau2HeureArrivee = istream.read_double();
        value.mardiCreneau3HeureDep = istream.read_double();
        value.mardiCreneau3HeureArrivee = istream.read_double();
        value.mercrediCreneau1HeureDep = istream.read_double();
        value.mercrediCreneau1HeureArrivee = istream.read_double();
        value.mercrediCreneau2HeureDep = istream.read_double();
        value.mercrediCreneau2HeureArrivee = istream.read_double();
        value.mercrediCreneau3HeureDep = istream.read_double();
        value.mercrediCreneau3HeureArrivee = istream.read_double();
        value.jeudiCreneau1HeureDep = istream.read_double();
        value.jeudiCreneau1HeureArrivee = istream.read_double();
        value.jeudiCreneau2HeureDep = istream.read_double();
        value.jeudiCreneau2HeureArrivee = istream.read_double();
        value.jeudiCreneau3HeureDep = istream.read_double();
        value.jeudiCreneau3HeureArrivee = istream.read_double();
        value.vendrediCreneau1HeureDep = istream.read_double();
        value.vendrediCreneau1HeureArrivee = istream.read_double();
        value.vendrediCreneau2HeureDep = istream.read_double();
        value.vendrediCreneau2HeureArrivee = istream.read_double();
        value.vendrediCreneau3HeureDep = istream.read_double();
        value.vendrediCreneau3HeureArrivee = istream.read_double();
        value.samediCreneau1HeureDep = istream.read_double();
        value.samediCreneau1HeureArrivee = istream.read_double();
        value.samediCreneau2HeureDep = istream.read_double();
        value.samediCreneau2HeureArrivee = istream.read_double();
        value.samediCreneau3HeureDep = istream.read_double();
        value.samediCreneau3HeureArrivee = istream.read_double();
        value.dimancheCreneau1HeureDep = istream.read_double();
        value.dimancheCreneau1HeureArrivee = istream.read_double();
        value.dimancheCreneau2HeureDep = istream.read_double();
        value.dimancheCreneau2HeureArrivee = istream.read_double();
        value.dimancheCreneau3HeureDep = istream.read_double();
        value.dimancheCreneau3HeureArrivee = istream.read_double();
        value.ferieCreneau1HeureDep = istream.read_double();
        value.ferieCreneau1HeureArrivee = istream.read_double();
        value.ferieCreneau2HeureDep = istream.read_double();
        value.ferieCreneau2HeureArrivee = istream.read_double();
        value.ferieCreneau3HeureDep = istream.read_double();
        value.ferieCreneau3HeureArrivee = istream.read_double();
        return value;
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.fudaa.dodico.corba.sipor.SParametresHorairesNavires2 value) {
        ostream.write_double(value.lundiCreneau1HeureDep);
        ostream.write_double(value.lundiCreneau1HeureArrivee);
        ostream.write_double(value.lundiCreneau2HeureDep);
        ostream.write_double(value.lundiCreneau2HeureArrivee);
        ostream.write_double(value.lundiCreneau3HeureDep);
        ostream.write_double(value.lundiCreneau3HeureArrivee);
        ostream.write_double(value.mardiCreneau1HeureDep);
        ostream.write_double(value.mardiCreneau1HeureArrivee);
        ostream.write_double(value.mardiCreneau2HeureDep);
        ostream.write_double(value.mardiCreneau2HeureArrivee);
        ostream.write_double(value.mardiCreneau3HeureDep);
        ostream.write_double(value.mardiCreneau3HeureArrivee);
        ostream.write_double(value.mercrediCreneau1HeureDep);
        ostream.write_double(value.mercrediCreneau1HeureArrivee);
        ostream.write_double(value.mercrediCreneau2HeureDep);
        ostream.write_double(value.mercrediCreneau2HeureArrivee);
        ostream.write_double(value.mercrediCreneau3HeureDep);
        ostream.write_double(value.mercrediCreneau3HeureArrivee);
        ostream.write_double(value.jeudiCreneau1HeureDep);
        ostream.write_double(value.jeudiCreneau1HeureArrivee);
        ostream.write_double(value.jeudiCreneau2HeureDep);
        ostream.write_double(value.jeudiCreneau2HeureArrivee);
        ostream.write_double(value.jeudiCreneau3HeureDep);
        ostream.write_double(value.jeudiCreneau3HeureArrivee);
        ostream.write_double(value.vendrediCreneau1HeureDep);
        ostream.write_double(value.vendrediCreneau1HeureArrivee);
        ostream.write_double(value.vendrediCreneau2HeureDep);
        ostream.write_double(value.vendrediCreneau2HeureArrivee);
        ostream.write_double(value.vendrediCreneau3HeureDep);
        ostream.write_double(value.vendrediCreneau3HeureArrivee);
        ostream.write_double(value.samediCreneau1HeureDep);
        ostream.write_double(value.samediCreneau1HeureArrivee);
        ostream.write_double(value.samediCreneau2HeureDep);
        ostream.write_double(value.samediCreneau2HeureArrivee);
        ostream.write_double(value.samediCreneau3HeureDep);
        ostream.write_double(value.samediCreneau3HeureArrivee);
        ostream.write_double(value.dimancheCreneau1HeureDep);
        ostream.write_double(value.dimancheCreneau1HeureArrivee);
        ostream.write_double(value.dimancheCreneau2HeureDep);
        ostream.write_double(value.dimancheCreneau2HeureArrivee);
        ostream.write_double(value.dimancheCreneau3HeureDep);
        ostream.write_double(value.dimancheCreneau3HeureArrivee);
        ostream.write_double(value.ferieCreneau1HeureDep);
        ostream.write_double(value.ferieCreneau1HeureArrivee);
        ostream.write_double(value.ferieCreneau2HeureDep);
        ostream.write_double(value.ferieCreneau2HeureArrivee);
        ostream.write_double(value.ferieCreneau3HeureDep);
        ostream.write_double(value.ferieCreneau3HeureArrivee);
    }
}
