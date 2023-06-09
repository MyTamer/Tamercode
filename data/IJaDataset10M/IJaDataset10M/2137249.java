package moa.evaluation;

import moa.MOAObject;
import moa.core.Measurement;
import weka.core.Instance;

/**
 * Interface implemented by learner evaluators to monitor
 * the results of the learning process.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @version $Revision: 7 $
 */
public interface ClassificationPerformanceEvaluator extends MOAObject {

    /**
     * Resets this evaluator. It must be similar to
     * starting a new evaluator from scratch.
     *
     */
    public void reset();

    /**
     * Adds a learning result to this evaluator.
     *
     * @param inst the instance to be classified
     * @param classVotes an array containing the estimated membership
     * probabilities of the test instance in each class
     * @return an array of measurements monitored in this evaluator
     */
    public void addResult(Instance inst, double[] classVotes);

    /**
     * Gets the current measurements monitored by this evaluator.
     *
     * @return an array of measurements monitored by this evaluator
     */
    public Measurement[] getPerformanceMeasurements();
}
