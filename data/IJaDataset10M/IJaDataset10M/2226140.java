package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.*;

/**
 <code>'$gen_indexing_keys'/3</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @author Douglas R. Miles (dmiles@users.sourceforge.net) for Object(s) 
 @version 1.0-dmiles
*/
class PRED_$gen_indexing_keys_3 extends PredicateBase {

    static Object s1 = makeAtom(":-", 2);

    static Object s2 = makeAtom("all");

    static Object s3 = makeAtom("[]");

    static Object s4 = makeList(s2, s3);

    static Object si5 = makeInteger(1);

    static Predicate _fail_0 = new PRED_fail_0();

    static Predicate _$gen_indexing_keys_3_var = new PRED_$gen_indexing_keys_3_var();

    static Predicate _$gen_indexing_keys_3_var_1 = new PRED_$gen_indexing_keys_3_var_1();

    static Predicate _$gen_indexing_keys_3_1 = new PRED_$gen_indexing_keys_3_1();

    static Predicate _$gen_indexing_keys_3_2 = new PRED_$gen_indexing_keys_3_2();

    public Object arg1, arg2, arg3;

    public PRED_$gen_indexing_keys_3(Object a1, Object a2, Object a3, Predicate cont) {
        arg1 = a1;
        arg2 = a2;
        arg3 = a3;
        this.cont = cont;
    }

    public PRED_$gen_indexing_keys_3() {
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
        return "$gen_indexing_keys";
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
        return "'$gen_indexing_keys'(" + argString(arg1, newParam) + "," + argString(arg2, newParam) + "," + argString(arg3, newParam) + ")";
    }

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        engine_aregs[1] = arg1;
        engine_aregs[2] = arg2;
        engine_aregs[3] = arg3;
        engine.cont = cont;
        engine.setB0();
        return engine.switch_on_term(_$gen_indexing_keys_3_var, _fail_0, _fail_0, _fail_0, _$gen_indexing_keys_3_var, _fail_0);
    }
}

class PRED_$gen_indexing_keys_3_var extends PRED_$gen_indexing_keys_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        return engine.jtry(_$gen_indexing_keys_3_1, _$gen_indexing_keys_3_var_1);
    }
}

class PRED_$gen_indexing_keys_3_var_1 extends PRED_$gen_indexing_keys_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        return engine.trust(_$gen_indexing_keys_3_2);
    }
}

class PRED_$gen_indexing_keys_3_1 extends PRED_$gen_indexing_keys_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        Object a1, a2, a3, a4, a5;
        Predicate cont;
        a1 = engine_aregs[1];
        a2 = engine_aregs[2];
        a3 = engine_aregs[3];
        cont = engine.cont;
        a1 = deref(a1);
        if (isCompound(a1)) {
            if (!functorOf(s1, a1)) return fail(engine);
            Object[] args = args(a1);
            a4 = args[0];
        } else if (isVariable(a1)) {
            a4 = engine.makeVariable(this);
            Object[] args = { a4, engine.makeVariable(this) };
            bind(a1, makeStructure(s1, args));
        } else {
            return fail(engine);
        }
        if (!unify(s4, a3)) return fail(engine);
        a5 = engine.makeVariable(this);
        if (!unify(a5, makeInteger(engine.B0))) {
            return fail(engine);
        }
        a4 = deref(a4);
        if (!isAtomTerm(a4)) {
            return fail(engine);
        }
        a5 = deref(a5);
        if (!isCutter(a5)) {
            throw new IllegalTypeException("integer", a5);
        } else {
            engine.cut((a5));
        }
        return exit(engine, cont);
    }
}

class PRED_$gen_indexing_keys_3_2 extends PRED_$gen_indexing_keys_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        Object a1, a2, a3, a4, a5;
        Predicate p1;
        Predicate cont;
        a1 = engine_aregs[1];
        a2 = engine_aregs[2];
        a3 = engine_aregs[3];
        cont = engine.cont;
        a1 = deref(a1);
        if (isCompound(a1)) {
            if (!functorOf(s1, a1)) return fail(engine);
            Object[] args = args(a1);
            a4 = args[0];
        } else if (isVariable(a1)) {
            a4 = engine.makeVariable(this);
            Object[] args = { a4, engine.makeVariable(this) };
            bind(a1, makeStructure(s1, args));
        } else {
            return fail(engine);
        }
        a5 = engine.makeVariable(this);
        p1 = new PRED_$gen_indexing_keys0_3(a5, a2, a3, cont);
        return exit(engine, new PRED_arg_3(si5, a4, a5, p1));
    }
}
