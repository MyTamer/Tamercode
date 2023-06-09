package org.codecover.instrumentation.cobol85.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> &lt;BLOCK&gt;
 * f1 -> [ &lt;CONTAINS&gt; ]
 * f2 -> [ IntegerConstant() &lt;TO&gt; ]
 * f3 -> IntegerConstant()
 * f4 -> [ &lt;RECORDS&gt; | &lt;CHARACTERS&gt; ]
 * </PRE>
 */
public class BlockContainsClause implements Node {

    private Node parent;

    public NodeToken f0;

    public NodeOptional f1;

    public NodeOptional f2;

    public IntegerConstant f3;

    public NodeOptional f4;

    public BlockContainsClause(NodeToken n0, NodeOptional n1, NodeOptional n2, IntegerConstant n3, NodeOptional n4) {
        f0 = n0;
        if (f0 != null) f0.setParent(this);
        f1 = n1;
        if (f1 != null) f1.setParent(this);
        f2 = n2;
        if (f2 != null) f2.setParent(this);
        f3 = n3;
        if (f3 != null) f3.setParent(this);
        f4 = n4;
        if (f4 != null) f4.setParent(this);
    }

    public BlockContainsClause(NodeOptional n0, NodeOptional n1, IntegerConstant n2, NodeOptional n3) {
        f0 = new NodeToken("block");
        if (f0 != null) f0.setParent(this);
        f1 = n0;
        if (f1 != null) f1.setParent(this);
        f2 = n1;
        if (f2 != null) f2.setParent(this);
        f3 = n2;
        if (f3 != null) f3.setParent(this);
        f4 = n3;
        if (f4 != null) f4.setParent(this);
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
