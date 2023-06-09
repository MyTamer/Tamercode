package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.*;

/**
 <code>'$new_indexing_hash'/3</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @author Douglas R. Miles (dmiles@users.sourceforge.net) for Object(s) 
 @version 1.0-dmiles
*/
public class PRED_$new_indexing_hash_3 extends PredicateBase {

    static Object s1 = makeAtom("all");

    static Object s2 = makeAtom("[]");

    static Object s3 = makeAtom("var");

    static Object s4 = makeAtom("lis");

    static Object s5 = makeAtom("str");

    static Predicate _$new_indexing_hash_3_sub_1 = new PRED_$new_indexing_hash_3_sub_1();

    static Predicate _$new_indexing_hash_3_1 = new PRED_$new_indexing_hash_3_1();

    static Predicate _$new_indexing_hash_3_2 = new PRED_$new_indexing_hash_3_2();

    public Object arg1, arg2, arg3;

    public PRED_$new_indexing_hash_3(Object a1, Object a2, Object a3, Predicate cont) {
        arg1 = a1;
        arg2 = a2;
        arg3 = a3;
        this.cont = cont;
    }

    public PRED_$new_indexing_hash_3() {
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
        return "$new_indexing_hash";
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
        return "'$new_indexing_hash'(" + argString(arg1, newParam) + "," + argString(arg2, newParam) + "," + argString(arg3, newParam) + ")";
    }

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        engine_aregs[1] = arg1;
        engine_aregs[2] = arg2;
        engine_aregs[3] = arg3;
        engine.cont = cont;
        engine.setB0();
        return engine.jtry(_$new_indexing_hash_3_1, _$new_indexing_hash_3_sub_1);
    }
}

class PRED_$new_indexing_hash_3_sub_1 extends PRED_$new_indexing_hash_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        return engine.trust(_$new_indexing_hash_3_2);
    }
}

class PRED_$new_indexing_hash_3_1 extends PRED_$new_indexing_hash_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        Object a1, a2, a3, a4;
        Predicate p1, p2;
        Predicate cont;
        a1 = engine_aregs[1];
        a2 = engine_aregs[2];
        a3 = engine_aregs[3];
        cont = engine.cont;
        a4 = engine.makeVariable(this);
        if (!unify(a4, makeInteger(engine.B0))) {
            return fail(engine);
        }
        p1 = new PRED_hash_get_3(a1, a2, a3, cont);
        p2 = new PRED_$cut_1(a4, p1);
        return exit(engine, new PRED_hash_contains_key_2(a1, a2, p2));
    }
}

class PRED_$new_indexing_hash_3_2 extends PRED_$new_indexing_hash_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        Object a1, a2, a3;
        Predicate p1, p2, p3, p4, p5;
        Predicate cont;
        a1 = engine_aregs[1];
        a2 = engine_aregs[2];
        a3 = engine_aregs[3];
        cont = engine.cont;
        p1 = new PRED_hash_put_3(a1, a2, a3, cont);
        p2 = new PRED_hash_put_3(a3, s5, s2, p1);
        p3 = new PRED_hash_put_3(a3, s4, s2, p2);
        p4 = new PRED_hash_put_3(a3, s3, s2, p3);
        p5 = new PRED_hash_put_3(a3, s1, s2, p4);
        return exit(engine, new PRED_new_hash_1(a3, p5));
    }
}
