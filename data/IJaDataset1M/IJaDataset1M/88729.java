package weka.classifiers.trees.j48;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.RevisionUtils;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;
import java.util.Random;

/**
 * Class implementing a "no-split"-split (leaf node) for naive bayes
 * trees.
 *
 * @author Mark Hall (mhall@cs.waikato.ac.nz)
 * @version $Revision: 1.4 $
 */
public final class NBTreeNoSplit extends ClassifierSplitModel {

    /** for serialization */
    private static final long serialVersionUID = 7824804381545259618L;

    /** the naive bayes classifier */
    private NaiveBayesUpdateable m_nb;

    /** the discretizer used */
    private Discretize m_disc;

    /** errors on the training data at this node */
    private double m_errors;

    public NBTreeNoSplit() {
        m_numSubsets = 1;
    }

    /**
   * Build the no-split node
   *
   * @param instances an <code>Instances</code> value
   * @exception Exception if an error occurs
   */
    public final void buildClassifier(Instances instances) throws Exception {
        m_nb = new NaiveBayesUpdateable();
        m_disc = new Discretize();
        m_disc.setInputFormat(instances);
        Instances temp = Filter.useFilter(instances, m_disc);
        m_nb.buildClassifier(temp);
        if (temp.numInstances() >= 5) {
            m_errors = crossValidate(m_nb, temp, new Random(1));
        }
        m_numSubsets = 1;
    }

    /**
   * Return the errors made by the naive bayes model at this node
   *
   * @return the number of errors made
   */
    public double getErrors() {
        return m_errors;
    }

    /**
   * Return the discretizer used at this node
   *
   * @return a <code>Discretize</code> value
   */
    public Discretize getDiscretizer() {
        return m_disc;
    }

    /**
   * Get the naive bayes model at this node
   *
   * @return a <code>NaiveBayesUpdateable</code> value
   */
    public NaiveBayesUpdateable getNaiveBayesModel() {
        return m_nb;
    }

    /**
   * Always returns 0 because only there is only one subset.
   */
    public final int whichSubset(Instance instance) {
        return 0;
    }

    /**
   * Always returns null because there is only one subset.
   */
    public final double[] weights(Instance instance) {
        return null;
    }

    /**
   * Does nothing because no condition has to be satisfied.
   */
    public final String leftSide(Instances instances) {
        return "";
    }

    /**
   * Does nothing because no condition has to be satisfied.
   */
    public final String rightSide(int index, Instances instances) {
        return "";
    }

    /**
   * Returns a string containing java source code equivalent to the test
   * made at this node. The instance being tested is called "i".
   *
   * @param index index of the nominal value tested
   * @param data the data containing instance structure info
   * @return a value of type 'String'
   */
    public final String sourceExpression(int index, Instances data) {
        return "true";
    }

    /**
   * Return the probability for a class value
   *
   * @param classIndex the index of the class value
   * @param instance the instance to generate a probability for
   * @param theSubset the subset to consider
   * @return a probability
   * @exception Exception if an error occurs
   */
    public double classProb(int classIndex, Instance instance, int theSubset) throws Exception {
        m_disc.input(instance);
        Instance temp = m_disc.output();
        return m_nb.distributionForInstance(temp)[classIndex];
    }

    /**
   * Return a textual description of the node
   *
   * @return a <code>String</code> value
   */
    public String toString() {
        return m_nb.toString();
    }

    /**
   * Utility method for fast 5-fold cross validation of a naive bayes
   * model
   *
   * @param fullModel a <code>NaiveBayesUpdateable</code> value
   * @param trainingSet an <code>Instances</code> value
   * @param r a <code>Random</code> value
   * @return a <code>double</code> value
   * @exception Exception if an error occurs
   */
    public static double crossValidate(NaiveBayesUpdateable fullModel, Instances trainingSet, Random r) throws Exception {
        Classifier[] copies = Classifier.makeCopies(fullModel, 5);
        Evaluation eval = new Evaluation(trainingSet);
        for (int j = 0; j < 5; j++) {
            Instances test = trainingSet.testCV(5, j);
            for (int k = 0; k < test.numInstances(); k++) {
                test.instance(k).setWeight(-test.instance(k).weight());
                ((NaiveBayesUpdateable) copies[j]).updateClassifier(test.instance(k));
                test.instance(k).setWeight(-test.instance(k).weight());
            }
            eval.evaluateModel(copies[j], test);
        }
        return eval.incorrect();
    }

    /**
   * Returns the revision string.
   * 
   * @return		the revision
   */
    public String getRevision() {
        return RevisionUtils.extract("$Revision: 1.4 $");
    }
}
