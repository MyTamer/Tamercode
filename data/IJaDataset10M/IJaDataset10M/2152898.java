package repast.simphony.engine.watcher.query;

import repast.simphony.context.Context;

public class ASTWithin extends SimpleNode {

    public ASTWithin(int id) {
        super(id);
    }

    public ASTWithin(QueryParser p, int id) {
        super(p, id);
    }

    public IBooleanExpression buildExpression(Context context) {
        ASTNumber num = (ASTNumber) children[0];
        if (this.children.length == 1) {
            return new WithinBooleanExpression(num.getNumber(), context);
        } else {
            ASTName name = (ASTName) children[1];
            return new NamedWithinBooleanExpression(num.getNumber(), name.getName(), context);
        }
    }
}
