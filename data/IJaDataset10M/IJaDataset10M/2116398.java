package org.openrdf.query.parser.sparql.ast;

public class ASTQName extends SimpleNode {

    private String value;

    public ASTQName(int id) {
        super(id);
    }

    public ASTQName(SyntaxTreeBuilder p, int id) {
        super(p, id);
    }

    @Override
    public Object jjtAccept(SyntaxTreeBuilderVisitor visitor, Object data) throws VisitorException {
        return visitor.visit(this, data);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + value + ")";
    }
}
