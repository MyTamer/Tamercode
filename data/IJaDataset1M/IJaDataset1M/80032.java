package org.codecover.instrumentation.cobol85.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> &lt;ALPHABET&gt;
 * f1 -> AlphabetName()
 * f2 -> [ &lt;IS&gt; ]
 * f3 -> ( &lt;STANDARD_1&gt; | &lt;STANDARD_2&gt; | &lt;NATIVE&gt; | CobolWord() | ( Literal() [ ( ( &lt;THROUGH&gt; | &lt;THRU&gt; ) Literal() | ( &lt;ALSO&gt; Literal() )+ ) ] )+ )
 * </PRE>
 */
public class AlphabetClause implements Node {

    private Node parent;

    public NodeToken f0;

    public AlphabetName f1;

    public NodeOptional f2;

    public NodeChoice f3;

    public AlphabetClause(NodeToken n0, AlphabetName n1, NodeOptional n2, NodeChoice n3) {
        f0 = n0;
        if (f0 != null) f0.setParent(this);
        f1 = n1;
        if (f1 != null) f1.setParent(this);
        f2 = n2;
        if (f2 != null) f2.setParent(this);
        f3 = n3;
        if (f3 != null) f3.setParent(this);
    }

    public AlphabetClause(AlphabetName n0, NodeOptional n1, NodeChoice n2) {
        f0 = new NodeToken("alphabet");
        if (f0 != null) f0.setParent(this);
        f1 = n0;
        if (f1 != null) f1.setParent(this);
        f2 = n1;
        if (f2 != null) f2.setParent(this);
        f3 = n2;
        if (f3 != null) f3.setParent(this);
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
