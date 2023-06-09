package japa.parser.ast.type;

import japa.parser.ast.visitor.GenericVisitor;
import japa.parser.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class WildcardType extends Type {

    private final ReferenceType ext;

    private final ReferenceType sup;

    public WildcardType(int line, int column, ReferenceType ext, ReferenceType sup) {
        super(line, column);
        assert ext == null || sup == null;
        this.ext = ext;
        this.sup = sup;
    }

    public ReferenceType getExtends() {
        return ext;
    }

    public ReferenceType getSuper() {
        return sup;
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}
