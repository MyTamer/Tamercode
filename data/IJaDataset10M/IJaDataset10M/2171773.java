package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.BaseMultivariateOptimizer;
import org.apache.commons.math3.optimization.BaseMultivariateSimpleBoundsOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

/**
 * Base class for implementing optimizers for multivariate scalar functions,
 * subject to simple bounds: The valid range of the parameters is an interval.
 * The interval can possibly be infinite (in one or both directions).
 * This base class handles the boiler-plate methods associated to thresholds
 * settings, iterations and evaluations counting.
 *
 * @param <FUNC> Type of the objective function to be optimized.
 *
 * @version $Id$
 * @since 3.0
 */
public abstract class BaseAbstractMultivariateSimpleBoundsOptimizer<FUNC extends MultivariateFunction> extends BaseAbstractMultivariateOptimizer<FUNC> implements BaseMultivariateOptimizer<FUNC>, BaseMultivariateSimpleBoundsOptimizer<FUNC> {

    /** Lower bounds. */
    private double[] lowerBound;

    /** Upper bounds. */
    private double[] upperBound;

    /**
     * Simple constructor with default settings.
     * The convergence checker is set to a
     * {@link org.apache.commons.math3.optimization.SimpleValueChecker} and
     * the allowed number of evaluations is set to {@link Integer#MAX_VALUE}.
     *
     * @see BaseAbstractMultivariateOptimizer#BaseAbstractMultivariateOptimizer()
     */
    protected BaseAbstractMultivariateSimpleBoundsOptimizer() {
    }

    /**
     * @param checker Convergence checker.
     */
    protected BaseAbstractMultivariateSimpleBoundsOptimizer(ConvergenceChecker<PointValuePair> checker) {
        super(checker);
    }

    /**
     * @return the lower bounds.
     */
    public double[] getLowerBound() {
        return lowerBound.clone();
    }

    /**
     * @return the upper bounds.
     */
    public double[] getUpperBound() {
        return upperBound.clone();
    }

    /** {@inheritDoc} */
    public PointValuePair optimize(int maxEval, FUNC f, GoalType goalType, double[] startPoint) {
        return optimize(maxEval, f, goalType, startPoint, null, null);
    }

    /** {@inheritDoc} */
    public PointValuePair optimize(int maxEval, FUNC f, GoalType goalType, double[] startPoint, double[] lower, double[] upper) {
        final int dim = startPoint.length;
        if (lower != null) {
            if (lower.length != dim) {
                throw new DimensionMismatchException(lower.length, dim);
            }
            for (int i = 0; i < dim; i++) {
                final double v = startPoint[i];
                final double lo = lower[i];
                if (v < lo) {
                    throw new NumberIsTooSmallException(v, lo, true);
                }
            }
        }
        if (upper != null) {
            if (upper.length != dim) {
                throw new DimensionMismatchException(upper.length, dim);
            }
            for (int i = 0; i < dim; i++) {
                final double v = startPoint[i];
                final double hi = upper[i];
                if (v > hi) {
                    throw new NumberIsTooLargeException(v, hi, true);
                }
            }
        }
        if (lower == null) {
            lowerBound = new double[dim];
            for (int i = 0; i < dim; i++) {
                lowerBound[i] = Double.NEGATIVE_INFINITY;
            }
        } else {
            lowerBound = lower.clone();
        }
        if (upper == null) {
            upperBound = new double[dim];
            for (int i = 0; i < dim; i++) {
                upperBound[i] = Double.POSITIVE_INFINITY;
            }
        } else {
            upperBound = upper.clone();
        }
        return super.optimize(maxEval, f, goalType, startPoint);
    }
}
