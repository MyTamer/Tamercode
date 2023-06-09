package org.codecover.instrumentation.cobol85.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> ( &lt;LPARENCHAR&gt; Condition() &lt;RPARENCHAR&gt; | RelationCondition() | ClassCondition() | ConditionNameCondition() )
 * </PRE>
 */
public class SimpleCondition implements Node {

    private Node parent;

    public NodeChoice f0;

    public SimpleCondition(NodeChoice n0) {
        f0 = n0;
        if (f0 != null) f0.setParent(this);
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
