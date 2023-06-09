package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.*;

/**
 <code>'$parse_tokens_error'/3</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @author Douglas R. Miles (dmiles@users.sourceforge.net) for Object(s) 
 @version 1.0-dmiles
*/
class PRED_$parse_tokens_error_3 extends PredicateBase {

    static Object s1 = makeAtom("{SYNTAX ERROR}");

    static Object s2 = makeAtom("** ");

    static Object s3 = makeAtom(" **");

    static Object s4 = makeAtom("[]");

    static Object s5 = makeAtom(":", 2);

    static Object s6 = makeAtom("jp.ac.kobe_u.cs.prolog.builtin");

    static Object s7 = makeAtom("$tokens", 1);

    public Object arg1, arg2, arg3;

    public PRED_$parse_tokens_error_3(Object a1, Object a2, Object a3, Predicate cont) {
        arg1 = a1;
        arg2 = a2;
        arg3 = a3;
        this.cont = cont;
    }

    public PRED_$parse_tokens_error_3() {
    }

    public void setArgument(Object[] args, Predicate cont) {
        arg1 = args[0];
        arg2 = args[1];
        arg3 = args[2];
        this.cont = cont;
    }

    public int arity() {
        return 3;
    }

    public String nameUQ() {
        return "$parse_tokens_error";
    }

    public void sArg(int i0, Object val) {
        switch(i0) {
            case 0:
                arg1 = val;
                break;
            case 1:
                arg2 = val;
                break;
            case 2:
                arg3 = val;
                break;
            default:
                newIndexOutOfBoundsException("setarg", i0, val);
        }
    }

    public Object gArg(int i0) {
        switch(i0) {
            case 0:
                return arg1;
            case 1:
                return arg2;
            case 2:
                return arg3;
            default:
                return newIndexOutOfBoundsException("getarg", i0, null);
        }
    }

    public String toPrologString(java.util.Collection newParam) {
        return "'$parse_tokens_error'(" + argString(arg1, newParam) + "," + argString(arg2, newParam) + "," + argString(arg3, newParam) + ")";
    }

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        engine.setB0();
        Object a1, a2, a3, a4, a5, a6;
        Predicate p1, p2, p3, p4, p5, p6, p7, p8, p9;
        a1 = arg1;
        a2 = arg2;
        a3 = arg3;
        a4 = engine.makeVariable(this);
        Object[] y1 = { a4 };
        a5 = makeStructure(s7, y1);
        Object[] y2 = { s6, a5 };
        a6 = makeStructure(s5, y2);
        p1 = new PRED_fail_0(cont);
        p2 = new PRED_$parse_tokens_error1_2(a4, a2, p1);
        p3 = new PRED_clause_2(a6, engine.makeVariable(this), p2);
        p4 = new PRED_$parse_tokens_error1_2(s4, a2, p3);
        p5 = new PRED_nl_0(p4);
        p6 = new PRED_write_1(s3, p5);
        p7 = new PRED_$parse_tokens_write_message_1(a1, p6);
        p8 = new PRED_write_1(s2, p7);
        p9 = new PRED_nl_0(p8);
        return exit(engine, new PRED_write_1(s1, p9));
    }
}
