package org.tearsinrain.jcodemodel;

/**
 * Marker interface for code components that can be placed to
 * the left of '=' in an assignment.
 * 
 * A left hand value can always be a right hand value, so
 * this interface derives from {@link JExpression}. 
 */
public interface JAssignmentTarget extends JGenerable, JExpression {

    JExpression assign(JExpression rhs);

    JExpression assignPlus(JExpression rhs);
}
