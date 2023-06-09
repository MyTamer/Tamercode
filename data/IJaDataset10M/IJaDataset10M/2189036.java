package jde.parser.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> LocalVariableDeclaration()
 *       | StatementExpressionList()
 * </PRE>
 */
public class ForInit implements Node {

    public NodeChoice f0;

    public ForInit(NodeChoice n0) {
        f0 = n0;
    }

    public void accept(jde.parser.visitor.Visitor v) {
        v.visit(this);
    }
}
