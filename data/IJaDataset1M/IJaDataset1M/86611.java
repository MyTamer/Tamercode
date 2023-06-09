package org.icepdf.core.tag.query;

import org.icepdf.core.tag.TaggedDocument;

/**
 * @author mcollette
 * @since 4.0
 */
public abstract class Operator implements Expression {

    public static int SCOPE_TAG = 1;

    public static int SCOPE_IMAGE = 2;

    private static final String[] SCOPE_NAMES = new String[] { "", "TAG", "IMAGE" };

    protected int scope;

    protected Expression[] childExpressions;

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public void setChildExpressions(Expression[] children) {
        childExpressions = children;
    }

    public String describe(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) sb.append("  ");
        String className = getClass().getName();
        className = className.substring(className.lastIndexOf(".") + 1);
        sb.append(className);
        sb.append("  scope: ");
        sb.append(SCOPE_NAMES[scope]);
        sb.append('\n');
        int num = (childExpressions != null) ? childExpressions.length : 0;
        for (int i = 0; i < num; i++) sb.append(childExpressions[i].describe(indent + 1));
        return sb.toString();
    }
}
