package org.codecover.instrumentation.cobol85.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> &lt;VALUE&gt;
 * f1 -> &lt;OF&gt;
 * f2 -> ( SystemName() &lt;IS&gt; ( QualifiedDataName() | Literal() ) )+
 * </PRE>
 */
public class ValueOfClause implements Node {

    private Node parent;

    public NodeToken f0;

    public NodeToken f1;

    public NodeList f2;

    public ValueOfClause(NodeToken n0, NodeToken n1, NodeList n2) {
        f0 = n0;
        if (f0 != null) f0.setParent(this);
        f1 = n1;
        if (f1 != null) f1.setParent(this);
        f2 = n2;
        if (f2 != null) f2.setParent(this);
    }

    public ValueOfClause(NodeList n0) {
        f0 = new NodeToken("value");
        if (f0 != null) f0.setParent(this);
        f1 = new NodeToken("of");
        if (f1 != null) f1.setParent(this);
        f2 = n0;
        if (f2 != null) f2.setParent(this);
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
