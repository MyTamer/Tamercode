package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.*;

/**
 <code>'$length'/3</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @author Douglas R. Miles (dmiles@users.sourceforge.net) for Object(s) 
 @version 1.0-dmiles
*/
class PRED_$length_3 extends PredicateBase {

    static Object s1 = makeAtom("[]");

    static Object si2 = makeInteger(1);

    static Predicate _$length_3_top = new PRED_$length_3_top();

    static Predicate _fail_0 = new PRED_fail_0();

    static Predicate _$length_3_var = new PRED_$length_3_var();

    static Predicate _$length_3_var_1 = new PRED_$length_3_var_1();

    static Predicate _$length_3_1 = new PRED_$length_3_1();

    static Predicate _$length_3_2 = new PRED_$length_3_2();

    public Object arg1, arg2, arg3;

    public PRED_$length_3(Object a1, Object a2, Object a3, Predicate cont) {
        arg1 = a1;
        arg2 = a2;
        arg3 = a3;
        this.cont = cont;
    }

    public PRED_$length_3() {
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
        return "$length";
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
        return "'$length'(" + argString(arg1, newParam) + "," + argString(arg2, newParam) + "," + argString(arg3, newParam) + ")";
    }

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        engine_aregs[1] = arg1;
        engine_aregs[2] = arg2;
        engine_aregs[3] = arg3;
        engine.cont = cont;
        return exit(engine, _$length_3_top);
    }
}

class PRED_$length_3_top extends PRED_$length_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        engine.setB0();
        return engine.switch_on_term(_$length_3_var, _fail_0, _fail_0, _$length_3_1, _fail_0, _$length_3_2);
    }
}

class PRED_$length_3_var extends PRED_$length_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        return engine.jtry(_$length_3_1, _$length_3_var_1);
    }
}

class PRED_$length_3_var_1 extends PRED_$length_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        return engine.trust(_$length_3_2);
    }
}

class PRED_$length_3_1 extends PRED_$length_3 {

    public Predicate exec(Prolog engine) {
        enter(engine);
        Object[] engine_aregs = engine.getAreg();
        Object a1, a2, a3;
        Predicate cont;
        a1 = engine_aregs[1];
        a2 = engine_aregs[2];
        a3 = engine_aregs[3];
        cont = engine.cont;
        a1 = deref(a1);
        if (isAtomTerm(a1)) {
            if (!prologEquals(a1, s1)) return fail(engine);
        } else if (isVariable(a1)) {
            bind(a1, s1);
        } else {
            return fail(engine);
        }
        if (!unify(a2, a3)) return fail(engine);
        return exit(engine, cont);
    }
}

class PRED_$length_3_2 extends PRED_$length_3 {

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
        if (isListTerm(a1)) {
            Object[] args = consArgs(a1);
            a4 = args[1];
        } else if (isVariable(a1)) {
            a4 = engine.makeVariable(this);
            bind(a1, makeList(engine.makeVariable(this), a4));
        } else {
            return fail(engine);
        }
        a5 = engine.makeVariable(this);
        try {
            if (!unify(a5, add(Arithmetic.evaluate(a2), si2))) {
                return fail(engine);
            }
        } catch (BuiltinException e) {
            e.goal = this;
            throw e;
        }
        engine_aregs[1] = a4;
        engine_aregs[2] = a5;
        engine_aregs[3] = a3;
        engine.cont = cont;
        return exit(engine, _$length_3_top);
    }
}
