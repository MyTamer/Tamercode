package org.codecover.instrumentation.java15.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> "implements"
 * f1 -> ClassOrInterfaceType()
 * f2 -> ( "," ClassOrInterfaceType() )*
 * </PRE>
 */
@SuppressWarnings("all")
public class ImplementsList implements Node {

    private Node parent;

    public NodeToken f0;

    public ClassOrInterfaceType f1;

    public NodeListOptional f2;

    public ImplementsList(NodeToken n0, ClassOrInterfaceType n1, NodeListOptional n2) {
        f0 = n0;
        if (f0 != null) f0.setParent(this);
        f1 = n1;
        if (f1 != null) f1.setParent(this);
        f2 = n2;
        if (f2 != null) f2.setParent(this);
    }

    public ImplementsList(ClassOrInterfaceType n0, NodeListOptional n1) {
        f0 = new NodeToken("implements");
        if (f0 != null) f0.setParent(this);
        f1 = n0;
        if (f1 != null) f1.setParent(this);
        f2 = n1;
        if (f2 != null) f2.setParent(this);
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
