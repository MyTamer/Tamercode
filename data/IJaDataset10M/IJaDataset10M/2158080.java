package org.codecover.instrumentation.cobol85.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> &lt;LINE&gt;
 * f1 -> &lt;SEQUENTIAL&gt;
 * </PRE>
 */
public class LineSequentialOrganizationClause implements Node {

    private Node parent;

    public NodeToken f0;

    public NodeToken f1;

    public LineSequentialOrganizationClause(NodeToken n0, NodeToken n1) {
        f0 = n0;
        if (f0 != null) f0.setParent(this);
        f1 = n1;
        if (f1 != null) f1.setParent(this);
    }

    public LineSequentialOrganizationClause() {
        f0 = new NodeToken("line");
        if (f0 != null) f0.setParent(this);
        f1 = new NodeToken("sequential");
        if (f1 != null) f1.setParent(this);
    }

    public void accept(org.codecover.instrumentation.cobol85.visitor.Visitor v) {
        v.visit(this);
    }

    public <R, A> R accept(org.codecover.instrumentation.cobol85.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(org.codecover.instrumentation.cobol85.visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(org.codecover.instrumentation.cobol85.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }

    public void setParent(Node n) {
        parent = n;
    }

    public Node getParent() {
        return parent;
    }
}
