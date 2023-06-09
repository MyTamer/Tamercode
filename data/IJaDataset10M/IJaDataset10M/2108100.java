package org.python.parser.ast;

import org.python.parser.SimpleNode;
import java.io.DataOutputStream;
import java.io.IOException;

public class Tuple extends exprType implements expr_contextType {

    public exprType[] elts;

    public int ctx;

    public Tuple(exprType[] elts, int ctx) {
        this.elts = elts;
        this.ctx = ctx;
    }

    public Tuple(exprType[] elts, int ctx, SimpleNode parent) {
        this(elts, ctx);
        this.beginLine = parent.beginLine;
        this.beginColumn = parent.beginColumn;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Tuple[");
        sb.append("elts=");
        sb.append(dumpThis(this.elts));
        sb.append(", ");
        sb.append("ctx=");
        sb.append(dumpThis(this.ctx, expr_contextType.expr_contextTypeNames));
        sb.append("]");
        return sb.toString();
    }

    public void pickle(DataOutputStream ostream) throws IOException {
        pickleThis(43, ostream);
        pickleThis(this.elts, ostream);
        pickleThis(this.ctx, ostream);
    }

    public Object accept(VisitorIF visitor) throws Exception {
        return visitor.visitTuple(this);
    }

    public void traverse(VisitorIF visitor) throws Exception {
        if (elts != null) {
            for (int i = 0; i < elts.length; i++) {
                if (elts[i] != null) elts[i].accept(visitor);
            }
        }
    }
}
