package org.codecover.instrumentation.cobol85.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> ( &lt;FD&gt; | &lt;SD&gt; )
 * f1 -> FileName()
 * f2 -> ( [ &lt;DOT&gt; ] FileAndSortDescriptionEntryClause() )*
 * f3 -> &lt;DOT&gt;
 * </PRE>
 */
public class FileAndSortDescriptionEntry implements Node {

    private Node parent;

    public NodeChoice f0;

    public FileName f1;

    public NodeListOptional f2;

    public NodeToken f3;

    public FileAndSortDescriptionEntry(NodeChoice n0, FileName n1, NodeListOptional n2, NodeToken n3) {
        f0 = n0;
        if (f0 != null) f0.setParent(this);
        f1 = n1;
        if (f1 != null) f1.setParent(this);
        f2 = n2;
        if (f2 != null) f2.setParent(this);
        f3 = n3;
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
