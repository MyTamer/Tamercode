package org.nfunk.jep.function;

import java.lang.Math;
import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;

/**
 * Natural logarithm.
 *
 * RJM Change: fixed so ln(positive Double) is Double.
 */
public class NaturalLogarithm extends PostfixMathCommand {

    public NaturalLogarithm() {
        numberOfParameters = 1;
    }

    public void run(Stack inStack) throws ParseException {
        checkStack(inStack);
        Object param = inStack.pop();
        inStack.push(ln(param));
        return;
    }

    public Object ln(Object param) throws ParseException {
        if (param instanceof Complex) {
            return ((Complex) param).log();
        } else if (param instanceof Number) {
            double num = ((Number) param).doubleValue();
            if (num >= 0) return new Double(Math.log(num)); else if (num != num) return new Double(Double.NaN); else {
                Complex temp = new Complex(num);
                return temp.log();
            }
        }
        throw new ParseException("Invalid parameter type");
    }
}
