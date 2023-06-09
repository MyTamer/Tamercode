package reconcile.weka.classifiers.functions.neural;

import java.io.Serializable;

/**
 * This is an interface used to create classes that can be used by the 
 * neuralnode to perform all it's computations.
 *
 * @author Malcolm Ware (mfw4@cs.waikato.ac.nz)
 * @version $Revision: 1.1 $
 */
public interface NeuralMethod extends Serializable {

    /**
   * This function calculates what the output value should be.
   * @param node The node to calculate the value for.
   * @return The value.
   */
    double outputValue(NeuralNode node);

    /**
   * This function calculates what the error value should be.
   * @param node The node to calculate the error for.
   * @return The error.
   */
    double errorValue(NeuralNode node);

    /**
   * This function will calculate what the change in weights should be
   * and also update them.
   * @param node The node to update the weights for.
   * @param learn The learning rate to use.
   * @param momentum The momentum to use.
   */
    void updateWeights(NeuralNode node, double learn, double momentum);
}
