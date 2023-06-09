package org.apache.commons.math.optimization;

import java.io.Serializable;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction;

/** 
 * This interface represents an optimization algorithm for {@link DifferentiableMultivariateVectorialFunction
 * vectorial differentiable objective functions}.
 * <p>Optimization algorithms find the input point set that either {@link GoalType
 * maximize or minimize} an objective function.</p>
 * @see MultivariateRealOptimizer
 * @see DifferentiableMultivariateRealOptimizer
 * @version $Revision: 758049 $ $Date: 2009-03-24 23:09:34 +0100 (Di, 24 Mrz 2009) $
 * @since 2.0
 */
public interface DifferentiableMultivariateVectorialOptimizer extends Serializable {

    /** Set the maximal number of iterations of the algorithm.
     * @param maxIterations maximal number of function calls
     * .
     */
    void setMaxIterations(int maxIterations);

    /** Get the maximal number of iterations of the algorithm.
      * @return maximal number of iterations
     */
    int getMaxIterations();

    /** Get the number of iterations realized by the algorithm.
     * @return number of iterations
    */
    int getIterations();

    /** Get the number of evaluations of the objective function.
     * <p>
     * The number of evaluation correspond to the last call to the
     * {@link #optimize(ObjectiveFunction, GoalType, double[]) optimize}
     * method. It is 0 if the method has not been called yet.
     * </p>
     * @return number of evaluations of the objective function
     */
    int getEvaluations();

    /** Get the number of evaluations of the objective function jacobian .
     * <p>
     * The number of evaluation correspond to the last call to the
     * {@link #optimize(ObjectiveFunction, GoalType, double[]) optimize}
     * method. It is 0 if the method has not been called yet.
     * </p>
     * @return number of evaluations of the objective function jacobian
     */
    int getJacobianEvaluations();

    /** Set the convergence checker.
     * @param checker object to use to check for convergence
     */
    void setConvergenceChecker(VectorialConvergenceChecker checker);

    /** Get the convergence checker.
     * @return object used to check for convergence
     */
    VectorialConvergenceChecker getConvergenceChecker();

    /** Optimizes an objective function.
     * <p>
     * Optimization is considered to be a weighted least-squares minimization.
     * The cost function to be minimized is
     * &sum;weight<sub>i</sub>(objective<sub>i</sub>-target<sub>i</sub>)<sup>2</sup>
     * </p>
     * @param f objective function
     * @param target target value for the objective functions at optimum
     * @param weights weight for the least squares cost computation
     * @param startPoint the start point for optimization
     * @return the point/value pair giving the optimal value for objective function
     * @exception FunctionEvaluationException if the objective function throws one during
     * the search
     * @exception OptimizationException if the algorithm failed to converge
     * @exception IllegalArgumentException if the start point dimension is wrong
     */
    VectorialPointValuePair optimize(DifferentiableMultivariateVectorialFunction f, double[] target, double[] weights, double[] startPoint) throws FunctionEvaluationException, OptimizationException, IllegalArgumentException;
}
