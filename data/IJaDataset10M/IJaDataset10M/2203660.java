package de.uniwue.dltk.textmarker.parser.ast;

import org.eclipse.dltk.ast.expressions.ExpressionConstants;
import de.uniwue.dltk.textmarker.internal.core.parsers.TextMarkerParser;

/**
 * @author Martin Toepfer
 * 
 */
public interface TMActionConstants {

    public static final int A_CALL = ExpressionConstants.USER_EXPRESSION_START + TextMarkerParser.CALL;

    public static final int A_ASSIGN = ExpressionConstants.USER_EXPRESSION_START + TextMarkerParser.ASSIGN;

    public static final int A_CREATE = ExpressionConstants.USER_EXPRESSION_START + TextMarkerParser.CREATE;

    public static final int A_FILL = ExpressionConstants.USER_EXPRESSION_START + TextMarkerParser.FILL;

    public static final int A_LOG = ExpressionConstants.USER_EXPRESSION_START + TextMarkerParser.LOG;
}
