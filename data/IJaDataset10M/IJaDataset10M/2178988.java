package org.openrdf.query.parser.sparql.ast;

public class ASTNot extends SimpleNode {

    public ASTNot(int id) {
        super(id);
    }

    public ASTNot(SyntaxTreeBuilder p, int id) {
        super(p, id);
    }

    @Override
    public Object jjtAccept(SyntaxTreeBuilderVisitor visitor, Object data) throws VisitorException {
        return visitor.visit(this, data);
    }
}
