package com.sun.xacml.cond;

import com.sun.xacml.EvaluationCtx;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.DoubleAttribute;
import com.sun.xacml.attr.IntegerAttribute;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class that implements all the *-multiply functions. It takes two
 * operands of the appropriate type and returns the product of the
 * operands. If either of the operands is indeterminate, an indeterminate
 * result is returned.
 *
 * @since 1.0
 * @author Steve Hanna
 * @author Seth Proctor
 */
public class MultiplyFunction extends FunctionBase {

    /**
     * Standard identifier for the integer-multiply function.
     */
    public static final String NAME_INTEGER_MULTIPLY = FUNCTION_NS + "integer-multiply";

    /**
     * Standard identifier for the double-multiply function.
     */
    public static final String NAME_DOUBLE_MULTIPLY = FUNCTION_NS + "double-multiply";

    private static final int ID_INTEGER_MULTIPLY = 0;

    private static final int ID_DOUBLE_MULTIPLY = 1;

    /**
     * Creates a new <code>MultiplyFunction</code> object.
     *
     * @param functionName the standard XACML name of the function to be
     *                     handled by this object, including the full namespace
     *
     * @throws IllegalArgumentException if the function is unknown
     */
    public MultiplyFunction(String functionName) {
        super(functionName, getId(functionName), getArgumentType(functionName), false, 2, getArgumentType(functionName), false);
    }

    /**
     * Private helper that returns the internal identifier used for the
     * given standard function.
     */
    private static int getId(String functionName) {
        if (functionName.equals(NAME_INTEGER_MULTIPLY)) return ID_INTEGER_MULTIPLY; else if (functionName.equals(NAME_DOUBLE_MULTIPLY)) return ID_DOUBLE_MULTIPLY; else throw new IllegalArgumentException("unknown multiply function " + functionName);
    }

    /**
     * Private helper that returns the type used for the given standard
     * function. Note that this doesn't check on the return value since the
     * method always is called after getId, so we assume that the function
     * is present.
     */
    private static String getArgumentType(String functionName) {
        if (functionName.equals(NAME_INTEGER_MULTIPLY)) return IntegerAttribute.identifier; else return DoubleAttribute.identifier;
    }

    /**
     * Returns a <code>Set</code> containing all the function identifiers
     * supported by this class.
     *
     * @return a <code>Set</code> of <code>String</code>s
     */
    public static Set getSupportedIdentifiers() {
        Set set = new HashSet();
        set.add(NAME_INTEGER_MULTIPLY);
        set.add(NAME_DOUBLE_MULTIPLY);
        return set;
    }

    /**
     * Evaluate the function, using the specified parameters.
     *
     * @param inputs a <code>List</code> of <code>Evaluatable</code>
     *               objects representing the arguments passed to the function
     * @param context an <code>EvaluationCtx</code> so that the
     *                <code>Evaluatable</code> objects can be evaluated
     * @return an <code>EvaluationResult</code> representing the
     *         function's result
     */
    public EvaluationResult evaluate(List inputs, EvaluationCtx context) {
        AttributeValue[] argValues = new AttributeValue[inputs.size()];
        EvaluationResult result = evalArgs(inputs, context, argValues);
        if (result != null) return result;
        switch(getFunctionId()) {
            case ID_INTEGER_MULTIPLY:
                {
                    long arg0 = ((IntegerAttribute) argValues[0]).getValue();
                    long arg1 = ((IntegerAttribute) argValues[1]).getValue();
                    long product = arg0 * arg1;
                    result = new EvaluationResult(new IntegerAttribute(product));
                    break;
                }
            case ID_DOUBLE_MULTIPLY:
                {
                    double arg0 = ((DoubleAttribute) argValues[0]).getValue();
                    double arg1 = ((DoubleAttribute) argValues[1]).getValue();
                    double product = arg0 * arg1;
                    result = new EvaluationResult(new DoubleAttribute(product));
                    break;
                }
        }
        return result;
    }
}
