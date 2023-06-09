package repast.simphony.engine.watcher.query;

import repast.simphony.context.Context;

public class ASTColocated extends SimpleNode {

    public ASTColocated(int id) {
        super(id);
    }

    public ASTColocated(QueryParser p, int id) {
        super(p, id);
    }

    public IBooleanExpression buildExpression(Context context) {
        return new ColocatedBooleanExpression(context);
    }
}
