package syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> ConditionalExpression()
 * f1 -> [ AssignmentOperator() Expression() ]
 * </PRE>
 */
public class Expression implements Node {

    private Node parent;

    public ConditionalExpression f0;

    public NodeOptional f1;

    public Expression(ConditionalExpression n0, NodeOptional n1) {
        f0 = n0;
        if (f0 != null) f0.setParent(this);
        f1 = n1;
        if (f1 != null) f1.setParent(this);
    }

    public void accept(visitor.Visitor v) {
        v.visit(this);
    }

    public <R, A> R accept(visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }

    public void setParent(Node n) {
        parent = n;
    }

    public Node getParent() {
        return parent;
    }
}
