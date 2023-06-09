package coffer.ast;

import polyglot.ast.*;
import polyglot.types.*;
import polyglot.visit.*;
import coffer.types.*;

/**
 * An AST node for a <code>Key</code>.  The key may be ambiguous. 
 */
public interface KeyNode extends Node {

    public Key key();

    public String name();

    public KeyNode key(Key key);
}
