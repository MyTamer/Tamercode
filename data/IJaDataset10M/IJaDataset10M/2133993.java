package org.codecover.instrumentation.java15.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> "++"
 * f1 -> PrimaryExpression()
 * </PRE>
 */
@SuppressWarnings("all")
public class PreIncrementExpression implements Node {

    private Node parent;

    public NodeToken f0;

    public PrimaryExpression f1;

    public PreIncrementExpression(NodeToken n0, PrimaryExpression n1) {
        f0 = n0;
        if (f0 != null) f0.setParent(this);
        f1 = n1;
        if (f1 != null) f1.setParent(this);
    }

    public PreIncrementExpression(PrimaryExpression n0) {
        f0 = new NodeToken("++");
        if (f0 != null) f0.setParent(this);
        f1 = n0;
        if (f1 != null) f1.setParent(this);
    }

    public void accept(org.codecover.instrumentation.java15.visitor.Visitor v) {
        v.visit(this);
    }

    public void accept(org.codecover.instrumentation.java15.visitor.VisitorWithException v) throws java.io.IOException {
        v.visit(this);
    }

    public <R, A> R accept(org.codecover.instrumentation.java15.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(org.codecover.instrumentation.java15.visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(org.codecover.instrumentation.java15.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }

    public void setParent(Node n) {
        parent = n;
    }

    public Node getParent() {
        return parent;
    }

    /** for debugging purposes */
    @Override
    public String toString() {
        return org.codecover.instrumentation.java15.visitor.TreeSourceFileImageDumper.convertToString(this);
    }
}
