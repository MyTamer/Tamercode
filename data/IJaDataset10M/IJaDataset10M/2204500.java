package org.openrdf.query.parser.sparql.ast;

import org.openrdf.query.algebra.Compare.CompareOp;

public class ASTCompare extends SimpleNode {

    private CompareOp operator;

    public ASTCompare(int id) {
        super(id);
    }

    public ASTCompare(SyntaxTreeBuilder p, int id) {
        super(p, id);
    }

    @Override
    public Object jjtAccept(SyntaxTreeBuilderVisitor visitor, Object data) throws VisitorException {
        return visitor.visit(this, data);
    }

    public CompareOp getOperator() {
        return operator;
    }

    public void setOperator(CompareOp operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + operator + ")";
    }
}
