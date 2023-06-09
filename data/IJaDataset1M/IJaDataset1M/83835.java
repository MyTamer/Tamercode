package org.openrdf.query.parser.serql.ast;

public class ASTExists extends ASTBooleanExpr {

    public ASTExists(int id) {
        super(id);
    }

    public ASTExists(SyntaxTreeBuilder p, int id) {
        super(p, id);
    }

    @Override
    public Object jjtAccept(SyntaxTreeBuilderVisitor visitor, Object data) throws VisitorException {
        return visitor.visit(this, data);
    }

    public ASTTupleQuery getOperand() {
        return (ASTTupleQuery) children.get(0);
    }
}
