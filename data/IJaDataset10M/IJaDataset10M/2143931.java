package org.python.parser.ast;

import org.python.parser.SimpleNode;
import java.io.DataOutputStream;
import java.io.IOException;

public class Exec extends stmtType {

    public exprType body;

    public exprType globals;

    public exprType locals;

    public Exec(exprType body, exprType globals, exprType locals) {
        this.body = body;
        this.globals = globals;
        this.locals = locals;
    }

    public Exec(exprType body, exprType globals, exprType locals, SimpleNode parent) {
        this(body, globals, locals);
        this.beginLine = parent.beginLine;
        this.beginColumn = parent.beginColumn;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Exec[");
        sb.append("body=");
        sb.append(dumpThis(this.body));
        sb.append(", ");
        sb.append("globals=");
        sb.append(dumpThis(this.globals));
        sb.append(", ");
        sb.append("locals=");
        sb.append(dumpThis(this.locals));
        sb.append("]");
        return sb.toString();
    }

    public void pickle(DataOutputStream ostream) throws IOException {
        pickleThis(22, ostream);
        pickleThis(this.body, ostream);
        pickleThis(this.globals, ostream);
        pickleThis(this.locals, ostream);
    }

    public Object accept(VisitorIF visitor) throws Exception {
        return visitor.visitExec(this);
    }

    public void traverse(VisitorIF visitor) throws Exception {
        if (body != null) body.accept(visitor);
        if (globals != null) globals.accept(visitor);
        if (locals != null) locals.accept(visitor);
    }
}
