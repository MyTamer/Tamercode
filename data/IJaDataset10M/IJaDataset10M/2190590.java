package org.python.antlr.ast;

import org.python.antlr.PythonTree;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;

public abstract class modType extends PythonTree {

    public modType(int ttype, Token token) {
        super(ttype, token);
    }

    public modType(Token token) {
        super(token);
    }

    public modType(PythonTree node) {
        super(node);
    }
}
