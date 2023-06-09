package jde.parser.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> "case" Expression() ":"
 *       | "default" ":"
 * </PRE>
 */
public class SwitchLabel implements Node {

    public NodeChoice f0;

    public SwitchLabel(NodeChoice n0) {
        f0 = n0;
    }

    public void accept(jde.parser.visitor.Visitor v) {
        v.visit(this);
    }
}
