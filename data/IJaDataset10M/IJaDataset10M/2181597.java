package org.codecover.instrumentation.cobol85.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> [ &lt;ORGANIZATION&gt; ]
 * f1 -> [ &lt;IS&gt; ]
 * f2 -> ( SequentialOrganizationClause() | LineSequentialOrganizationClause() | RelativeOrganizationClause() | IndexedOrganizationClause() )
 * </PRE>
 */
public class OrganizationClause implements Node {

    private Node parent;

    public NodeOptional f0;

    public NodeOptional f1;

    public NodeChoice f2;

    public OrganizationClause(NodeOptional n0, NodeOptional n1, NodeChoice n2) {
        f0 = n0;
        if (f0 != null) f0.setParent(this);
        f1 = n1;
        if (f1 != null) f1.setParent(this);
        f2 = n2;
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
