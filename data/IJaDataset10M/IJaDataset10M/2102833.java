package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.*;

/**
 <code>'$builtin_set_diff'/3</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @author Douglas R. Miles (dmiles@users.sourceforge.net) for Object(s) 
 @version 1.0-dmiles
*/
class PRED_$builtin_set_diff_3 extends PredicateBase {

    public Object arg1, arg2, arg3;

    public PRED_$builtin_set_diff_3(Object a1, Object a2, Object a3, Predicate cont) {
        arg1 = a1;
        arg2 = a2;
        arg3 = a3;
        this.cont = cont;
    }

    public PRED_$builtin_set_diff_3() {
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
        return "$builtin_set_diff";
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
        return "'$builtin_set_diff'(" + argString(arg1, newParam) + "," + argString(arg2, newParam) + "," + argString(arg3, newParam) + ")";
    }

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        engine.setB0();
        Object a1, a2, a3, a4, a5;
        Predicate p1, p2;
        a1 = arg1;
        a2 = arg2;
        a3 = arg3;
        a4 = engine.makeVariable(this);
        a5 = engine.makeVariable(this);
        p1 = new PRED_$builtin_set_diff0_3(a4, a5, a3, cont);
        p2 = new PRED_sort_2(a2, a5, p1);
        return exit(engine, new PRED_sort_2(a1, a4, p2));
    }
}
