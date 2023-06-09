package org.python.parser.ast;

import org.python.parser.SimpleNode;
import java.io.DataOutputStream;
import java.io.IOException;

public class Assign extends stmtType {

    public exprType[] targets;

    public exprType value;

    public Assign(exprType[] targets, exprType value) {
        this.targets = targets;
        this.value = value;
    }

    public Assign(exprType[] targets, exprType value, SimpleNode parent) {
        this(targets, value);
        this.beginLine = parent.beginLine;
        this.beginColumn = parent.beginColumn;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Assign[");
        sb.append("targets=");
        sb.append(dumpThis(this.targets));
        sb.append(", ");
        sb.append("value=");
        sb.append(dumpThis(this.value));
        sb.append("]");
        return sb.toString();
    }

    public void pickle(DataOutputStream ostream) throws IOException {
        pickleThis(10, ostream);
        pickleThis(this.targets, ostream);
        pickleThis(this.value, ostream);
    }

    public Object accept(VisitorIF visitor) throws Exception {
        return visitor.visitAssign(this);
    }

    public void traverse(VisitorIF visitor) throws Exception {
        if (targets != null) {
            for (int i = 0; i < targets.length; i++) {
                if (targets[i] != null) targets[i].accept(visitor);
            }
        }
        if (value != null) value.accept(visitor);
    }
}
