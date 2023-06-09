package jp.ac.kobe_u.cs.prolog.builtin;

import java.io.PrintWriter;
import jp.ac.kobe_u.cs.prolog.lang.*;

public class PRED_put_char_2 extends PredicateBase {

    /**
   * 
   */
    private static final long serialVersionUID = 7467101593709017114L;

    public Object arg1;

    public Object arg2;

    @Override
    public int arity() {
        return 2;
    }

    public PRED_put_char_2() {
    }

    public PRED_put_char_2(Object a1, Object a2, Predicate cont) {
        this.arg1 = a1;
        this.arg2 = a2;
        this.cont = cont;
    }

    @Override
    public Predicate exec(Prolog engine) {
        final Object[] engine_aregs = engine.getAreg();
        engine.setB0();
        Object a1 = this.arg1;
        Object a2 = this.arg2;
        Object stream = null;
        a2 = deref(a2);
        if (isVariable(a2)) throw new PInstantiationException(this, 2);
        if (!isAtomTerm(a2)) throw new IllegalTypeException(this, 2, "character", a2);
        a1 = deref(a1);
        if (isVariable(a1)) throw new PInstantiationException(this, 1);
        if (isAtomTerm(a1)) {
            if (!engine.getStreamManager().containsKey(a1)) throw new ExistenceException(this, 1, "stream", a1, "");
            stream = toJava(engine.getStreamManager().get(a1));
        } else if (isJavaObject(a1)) stream = toJava(a1); else throw new IllegalDomainException(this, 1, "stream_or_alias", a1);
        if (!(stream instanceof PrintWriter)) throw new PermissionException(this, "output", "stream", a1, "");
        final String str = nameUQ(a2);
        if (str.length() != 1) throw new IllegalTypeException(this, 2, "character", a2);
        final char c = str.charAt(0);
        if (!Character.isDefined(c)) throw new RepresentationException(this, 2, "character");
        ((PrintWriter) stream).print(c);
        return this.cont;
    }

    @Override
    public void setArgument(Object[] args, Predicate cont) {
        this.arg1 = args[0];
        this.arg2 = args[1];
        this.cont = cont;
    }
}
