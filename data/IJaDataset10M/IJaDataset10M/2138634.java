package antlr;

import antlr.collections.AST;

/** ASTPair:  utility class used for manipulating a pair of ASTs
 * representing the current AST root and current AST sibling.
 * This exists to compensate for the lack of pointers or 'var'
 * arguments in Java.
 */
public class ASTPair {

    public AST root;

    public AST child;

    /** Make sure that child is the last sibling */
    public final void advanceChildToEnd() {
        if (child != null) {
            while (child.getNextSibling() != null) {
                child = child.getNextSibling();
            }
        }
    }

    /** Copy an ASTPair.  Don't call it clone() because we want type-safety */
    public ASTPair copy() {
        ASTPair tmp = new ASTPair();
        tmp.root = root;
        tmp.child = child;
        return tmp;
    }

    public String toString() {
        String r = root == null ? "null" : root.getText();
        String c = child == null ? "null" : child.getText();
        return "[" + r + "," + c + "]";
    }
}
