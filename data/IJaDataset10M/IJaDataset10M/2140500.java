package org.jmlspecs.eclipse.jmldom;

import java.util.ArrayList;
import java.util.List;
import org.jmlspecs.eclipse.jmlast.IJavaASTVisitor;

/**
 * Do statement AST node type.
 *
 * <pre>
 * DoStatement:
 *    <b>do</b> Statement <b>while</b> <b>(</b> Expression <b>)</b> <b>;</b>
 * </pre>
 * 
 * @since 2.0
 */
public class DoStatement extends Statement {

    /**
	 * The "expression" structural property of this node type.
	 * @since 3.0
	 */
    public static final ChildPropertyDescriptor EXPRESSION_PROPERTY = new ChildPropertyDescriptor(DoStatement.class, "expression", Expression.class, MANDATORY, CYCLE_RISK);

    /**
	 * The "body" structural property of this node type.
	 * @since 3.0
	 */
    public static final ChildPropertyDescriptor BODY_PROPERTY = new ChildPropertyDescriptor(DoStatement.class, "body", Statement.class, MANDATORY, CYCLE_RISK);

    /**
	 * A list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
    private static final List PROPERTY_DESCRIPTORS;

    static {
        List properyList = new ArrayList(3);
        createPropertyList(DoStatement.class, properyList);
        addProperty(EXPRESSION_PROPERTY, properyList);
        addProperty(BODY_PROPERTY, properyList);
        PROPERTY_DESCRIPTORS = reapPropertyList(properyList);
    }

    /**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 * 
	 * @param apiLevel the API level; one of the
	 * <code>AST.JLS*</code> constants

	 * @return a list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor})
	 * @since 3.0
	 */
    public static List propertyDescriptors(int apiLevel) {
        return PROPERTY_DESCRIPTORS;
    }

    /**
	 * The expression; lazily initialized; defaults to an unspecified, but 
	 * legal, expression.
	 */
    private Expression expression = null;

    /**
	 * The body statement; lazily initialized; defaults to an empty block.
	 */
    private Statement body = null;

    /**
	 * Creates a new unparented do statement node owned by the given 
	 * AST. By default, the expresssion is unspecified, but legal,
	 * and the body statement is an empty block.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
    DoStatement(AST ast) {
        super(ast);
    }

    final List internalStructuralPropertiesForType(int apiLevel) {
        return propertyDescriptors(apiLevel);
    }

    final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
        if (property == EXPRESSION_PROPERTY) {
            if (get) {
                return getExpression();
            } else {
                setExpression((Expression) child);
                return null;
            }
        }
        if (property == BODY_PROPERTY) {
            if (get) {
                return getBody();
            } else {
                setBody((Statement) child);
                return null;
            }
        }
        return super.internalGetSetChildProperty(property, get, child);
    }

    final int getNodeType0() {
        return DO_STATEMENT;
    }

    ASTNode clone0(AST target) {
        DoStatement result = new DoStatement(target);
        result.setSourceRange(this.getStartPosition(), this.getLength());
        result.copyLeadingComment(this);
        result.setExpression((Expression) getExpression().clone(target));
        result.setBody((Statement) getBody().clone(target));
        return result;
    }

    final boolean subtreeMatch0(ASTMatcher matcher, Object other) {
        return matcher.match(this, other);
    }

    void accept0(IJavaASTVisitor visitor) {
        boolean visitChildren = visitor.visit(this);
        if (visitChildren) {
            acceptChild(visitor, getBody());
            acceptChild(visitor, getExpression());
        }
        visitor.endVisit(this);
    }

    /**
	 * Returns the expression of this do statement.
	 * 
	 * @return the expression node
	 */
    public Expression getExpression() {
        if (this.expression == null) {
            synchronized (this) {
                if (this.expression == null) {
                    preLazyInit();
                    this.expression = new SimpleName(this.ast);
                    postLazyInit(this.expression, EXPRESSION_PROPERTY);
                }
            }
        }
        return this.expression;
    }

    /**
	 * Sets the expression of this do statement.
	 * 
	 * @param expression the expression node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */
    public void setExpression(Expression expression) {
        if (expression == null) {
            throw new IllegalArgumentException();
        }
        ASTNode oldChild = this.expression;
        preReplaceChild(oldChild, expression, EXPRESSION_PROPERTY);
        this.expression = expression;
        postReplaceChild(oldChild, expression, EXPRESSION_PROPERTY);
    }

    /**
	 * Returns the body of this do statement.
	 * 
	 * @return the body statement node
	 */
    public Statement getBody() {
        if (this.body == null) {
            synchronized (this) {
                if (this.body == null) {
                    preLazyInit();
                    this.body = new Block(this.ast);
                    postLazyInit(this.body, BODY_PROPERTY);
                }
            }
        }
        return this.body;
    }

    /**
	 * Sets the body of this do statement.
	 * <p>
	 * Special note: The Java language does not allow a local variable declaration
	 * to appear as the body of a do statement (they may only appear within a
	 * block). However, the AST will allow a <code>VariableDeclarationStatement</code>
	 * as the body of a <code>DoStatement</code>. To get something that will
	 * compile, be sure to embed the <code>VariableDeclarationStatement</code>
	 * inside a <code>Block</code>.
	 * </p>
	 * 
	 * @param statement the body statement node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */
    public void setBody(Statement statement) {
        if (statement == null) {
            throw new IllegalArgumentException();
        }
        ASTNode oldChild = this.body;
        preReplaceChild(oldChild, statement, BODY_PROPERTY);
        this.body = statement;
        postReplaceChild(oldChild, statement, BODY_PROPERTY);
    }

    int memSize() {
        return super.memSize() + 2 * 4;
    }

    int treeSize() {
        return memSize() + (this.expression == null ? 0 : getExpression().treeSize()) + (this.body == null ? 0 : getBody().treeSize());
    }
}
