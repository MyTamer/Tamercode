package com.google.visualization.datasource.query.scalarfunction;

import com.google.visualization.datasource.base.InvalidQueryException;
import com.google.visualization.datasource.datatable.value.NumberValue;
import com.google.visualization.datasource.datatable.value.Value;
import com.google.visualization.datasource.datatable.value.ValueType;
import java.util.List;

/**
 * The binary scalar function product().
 * Returns the product of two number values.
 *
 * @author Liron L.
 */
public class Product implements ScalarFunction {

    /**
   * The name of this function.
   */
    private static final String FUNCTION_NAME = "product";

    /**
   *  A singleton instance of this class.
   */
    private static final Product INSTANCE = new Product();

    /**
   * A private constructor, to prevent instantiation other than by the singleton.
   */
    private Product() {
    }

    /**
   * Returns the singleton instance of this class.
   *
   * @return The singleton instance of this class.
   */
    public static Product getInstance() {
        return INSTANCE;
    }

    /**
   * {@inheritDoc}
   */
    public String getFunctionName() {
        return FUNCTION_NAME;
    }

    /**
   * Executes the scalar function product() on the given values. Returns the
   * product of the given values. All values are number values.
   * The method does not validate the parameters, the user must check the
   * parameters before calling this method.
   *
   * @param values A list of values on which the scalar function is performed.
   *
   * @return Value with the product of all given values, or number null value
   *     if one of the values is null.
   */
    public Value evaluate(List<Value> values) {
        if (values.get(0).isNull() || values.get(1).isNull()) {
            return NumberValue.getNullValue();
        }
        double product = ((NumberValue) values.get(0)).getValue() * ((NumberValue) values.get(1)).getValue();
        return new NumberValue(product);
    }

    /**
   * Returns the return type of the function. In this case, NUMBER. The method
   * does not validate the parameters, the user must check the parameters
   * before calling this method.
   *
   * @param types A list of the types of the scalar function parameters.
   *
   * @return The type of the returned value: Number.
   */
    public ValueType getReturnType(List<ValueType> types) {
        return ValueType.NUMBER;
    }

    /**
   * Validates that all function parameters are of type NUMBER, and that there
   * are exactly 2 parameters. Throws a ScalarFunctionException otherwise.
   *
   * @param types A list with parameters types.
   *
   * @throws InvalidQueryException Thrown if the parameters are invalid.
   */
    public void validateParameters(List<ValueType> types) throws InvalidQueryException {
        if (types.size() != 2) {
            throw new InvalidQueryException("The function " + FUNCTION_NAME + " requires 2 parmaeters ");
        }
        for (ValueType type : types) {
            if (type != ValueType.NUMBER) {
                throw new InvalidQueryException("Can't perform the function " + FUNCTION_NAME + " on values that are not numbers");
            }
        }
    }

    /**
   * {@inheritDoc}
   */
    public String toQueryString(List<String> argumentsQueryStrings) {
        return "(" + argumentsQueryStrings.get(0) + " * " + argumentsQueryStrings.get(1) + ")";
    }
}
