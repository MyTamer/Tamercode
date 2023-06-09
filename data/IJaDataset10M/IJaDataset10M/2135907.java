package com.sodad.weka.classifiers.bayes;

import com.sodad.weka.classifiers.AbstractClassifier;
import com.sodad.weka.classifiers.Classifier;
import com.sodad.weka.core.Capabilities;
import com.sodad.weka.core.Instance;
import com.sodad.weka.core.Instances;
import com.sodad.weka.core.RevisionUtils;
import com.sodad.weka.core.TechnicalInformation;
import com.sodad.weka.core.TechnicalInformationHandler;
import com.sodad.weka.core.Utils;
import com.sodad.weka.core.WeightedInstancesHandler;
import com.sodad.weka.core.Capabilities.Capability;
import com.sodad.weka.core.TechnicalInformation.Field;
import com.sodad.weka.core.TechnicalInformation.Type;

/**
 <!-- globalinfo-start -->
 * Class for building and using a multinomial Naive Bayes classifier. For more information see,<br/>
 * <br/>
 * Andrew Mccallum, Kamal Nigam: A Comparison of Event Models for Naive Bayes Text Classification. In: AAAI-98 Workshop on 'Learning for Text Categorization', 1998.<br/>
 * <br/>
 * The core equation for this classifier:<br/>
 * <br/>
 * P[Ci|D] = (P[D|Ci] x P[Ci]) / P[D] (Bayes rule)<br/>
 * <br/>
 * where Ci is class i and D is a document.
 * <p/>
 <!-- globalinfo-end -->
 *
 <!-- technical-bibtex-start -->
 * BibTeX:
 * <pre>
 * &#64;inproceedings{Mccallum1998,
 *    author = {Andrew Mccallum and Kamal Nigam},
 *    booktitle = {AAAI-98 Workshop on 'Learning for Text Categorization'},
 *    title = {A Comparison of Event Models for Naive Bayes Text Classification},
 *    year = {1998}
 * }
 * </pre>
 * <p/>
 <!-- technical-bibtex-end -->
 *
 <!-- options-start -->
 * Valid options are: <p/>
 * 
 * <pre> -D
 *  If set, classifier is run in debug mode and
 *  may output additional info to the console</pre>
 * 
 <!-- options-end -->
 *
 * @author Andrew Golightly (acg4@cs.waikato.ac.nz)
 * @author Bernhard Pfahringer (bernhard@cs.waikato.ac.nz)
 * @version $Revision: 6302 $ 
 */
public class NaiveBayesMultinomial extends AbstractClassifier implements WeightedInstancesHandler, TechnicalInformationHandler {

    /** for serialization */
    static final long serialVersionUID = 5932177440181257085L;

    /**
   * probability that a word (w) exists in a class (H) (i.e. Pr[w|H])
   * The matrix is in the this format: probOfWordGivenClass[class][wordAttribute]
   * NOTE: the values are actually the log of Pr[w|H]
   */
    protected double[][] m_probOfWordGivenClass;

    /** the probability of a class (i.e. Pr[H]) */
    protected double[] m_probOfClass;

    /** number of unique words */
    protected int m_numAttributes;

    /** number of class values */
    protected int m_numClasses;

    /** cache lnFactorial computations */
    protected double[] m_lnFactorialCache = new double[] { 0.0, 0.0 };

    /** copy of header information for use in toString method */
    protected Instances m_headerInfo;

    /**
   * Returns a string describing this classifier
   * @return a description of the classifier suitable for
   * displaying in the explorer/experimenter gui
   */
    public String globalInfo() {
        return "Class for building and using a multinomial Naive Bayes classifier. " + "For more information see,\n\n" + getTechnicalInformation().toString() + "\n\n" + "The core equation for this classifier:\n\n" + "P[Ci|D] = (P[D|Ci] x P[Ci]) / P[D] (Bayes rule)\n\n" + "where Ci is class i and D is a document.";
    }

    /**
   * Returns an instance of a TechnicalInformation object, containing 
   * detailed information about the technical background of this class,
   * e.g., paper reference or book this class is based on.
   * 
   * @return the technical information about this class
   */
    public TechnicalInformation getTechnicalInformation() {
        TechnicalInformation result;
        result = new TechnicalInformation(Type.INPROCEEDINGS);
        result.setValue(Field.AUTHOR, "Andrew Mccallum and Kamal Nigam");
        result.setValue(Field.YEAR, "1998");
        result.setValue(Field.TITLE, "A Comparison of Event Models for Naive Bayes Text Classification");
        result.setValue(Field.BOOKTITLE, "AAAI-98 Workshop on 'Learning for Text Categorization'");
        return result;
    }

    /**
   * Returns default capabilities of the classifier.
   *
   * @return      the capabilities of this classifier
   */
    public Capabilities getCapabilities() {
        Capabilities result = super.getCapabilities();
        result.disableAll();
        result.enable(Capability.NUMERIC_ATTRIBUTES);
        result.enable(Capability.NOMINAL_CLASS);
        result.enable(Capability.MISSING_CLASS_VALUES);
        return result;
    }

    /**
   * Generates the classifier.
   *
   * @param instances set of instances serving as training data 
   * @throws Exception if the classifier has not been generated successfully
   */
    public void buildClassifier(Instances instances) throws Exception {
        getCapabilities().testWithFail(instances);
        instances = new Instances(instances);
        instances.deleteWithMissingClass();
        m_headerInfo = new Instances(instances, 0);
        m_numClasses = instances.numClasses();
        m_numAttributes = instances.numAttributes();
        m_probOfWordGivenClass = new double[m_numClasses][];
        for (int c = 0; c < m_numClasses; c++) {
            m_probOfWordGivenClass[c] = new double[m_numAttributes];
            for (int att = 0; att < m_numAttributes; att++) {
                m_probOfWordGivenClass[c][att] = 1;
            }
        }
        Instance instance;
        int classIndex;
        double numOccurences;
        double[] docsPerClass = new double[m_numClasses];
        double[] wordsPerClass = new double[m_numClasses];
        java.util.Enumeration enumInsts = instances.enumerateInstances();
        while (enumInsts.hasMoreElements()) {
            instance = (Instance) enumInsts.nextElement();
            classIndex = (int) instance.value(instance.classIndex());
            docsPerClass[classIndex] += instance.weight();
            for (int a = 0; a < instance.numValues(); a++) if (instance.index(a) != instance.classIndex()) {
                if (!instance.isMissing(a)) {
                    numOccurences = instance.valueSparse(a) * instance.weight();
                    if (numOccurences < 0) throw new Exception("Numeric attribute values must all be greater or equal to zero.");
                    wordsPerClass[classIndex] += numOccurences;
                    m_probOfWordGivenClass[classIndex][instance.index(a)] += numOccurences;
                }
            }
        }
        for (int c = 0; c < m_numClasses; c++) for (int v = 0; v < m_numAttributes; v++) m_probOfWordGivenClass[c][v] = Math.log(m_probOfWordGivenClass[c][v] / (wordsPerClass[c] + m_numAttributes - 1));
        final double numDocs = instances.sumOfWeights() + m_numClasses;
        m_probOfClass = new double[m_numClasses];
        for (int h = 0; h < m_numClasses; h++) m_probOfClass[h] = (double) (docsPerClass[h] + 1) / numDocs;
    }

    /**
   * Calculates the class membership probabilities for the given test 
   * instance.
   *
   * @param instance the instance to be classified
   * @return predicted class probability distribution
   * @throws Exception if there is a problem generating the prediction
   */
    public double[] distributionForInstance(Instance instance) throws Exception {
        double[] probOfClassGivenDoc = new double[m_numClasses];
        double[] logDocGivenClass = new double[m_numClasses];
        for (int h = 0; h < m_numClasses; h++) logDocGivenClass[h] = probOfDocGivenClass(instance, h);
        double max = logDocGivenClass[Utils.maxIndex(logDocGivenClass)];
        double probOfDoc = 0.0;
        for (int i = 0; i < m_numClasses; i++) {
            probOfClassGivenDoc[i] = Math.exp(logDocGivenClass[i] - max) * m_probOfClass[i];
            probOfDoc += probOfClassGivenDoc[i];
        }
        Utils.normalize(probOfClassGivenDoc, probOfDoc);
        return probOfClassGivenDoc;
    }

    /**
   * log(N!) + (for all the words)(log(Pi^ni) - log(ni!))
   *  
   *  where 
   *      N is the total number of words
   *      Pi is the probability of obtaining word i
   *      ni is the number of times the word at index i occurs in the document
   *
   * @param inst       The instance to be classified
   * @param classIndex The index of the class we are calculating the probability with respect to
   *
   * @return The log of the probability of the document occuring given the class
   */
    private double probOfDocGivenClass(Instance inst, int classIndex) {
        double answer = 0;
        double freqOfWordInDoc;
        for (int i = 0; i < inst.numValues(); i++) if (inst.index(i) != inst.classIndex()) {
            freqOfWordInDoc = inst.valueSparse(i);
            answer += (freqOfWordInDoc * m_probOfWordGivenClass[classIndex][inst.index(i)]);
        }
        return answer;
    }

    /**
   * Fast computation of ln(n!) for non-negative ints
   *
   * negative ints are passed on to the general gamma-function
   * based version in weka.core.SpecialFunctions
   *
   * if the current n value is higher than any previous one,
   * the cache is extended and filled to cover it
   *
   * the common case is reduced to a simple array lookup
   *
   * @param  n the integer 
   * @return ln(n!)
   */
    public double lnFactorial(int n) {
        if (n < 0) return com.sodad.weka.core.SpecialFunctions.lnFactorial(n);
        if (m_lnFactorialCache.length <= n) {
            double[] tmp = new double[n + 1];
            System.arraycopy(m_lnFactorialCache, 0, tmp, 0, m_lnFactorialCache.length);
            for (int i = m_lnFactorialCache.length; i < tmp.length; i++) tmp[i] = tmp[i - 1] + Math.log(i);
            m_lnFactorialCache = tmp;
        }
        return m_lnFactorialCache[n];
    }

    /**
   * Returns a string representation of the classifier.
   * 
   * @return a string representation of the classifier
   */
    public String toString() {
        StringBuffer result = new StringBuffer("The independent probability of a class\n--------------------------------------\n");
        for (int c = 0; c < m_numClasses; c++) result.append(m_headerInfo.classAttribute().value(c)).append("\t").append(Double.toString(m_probOfClass[c])).append("\n");
        result.append("\nThe probability of a word given the class\n-----------------------------------------\n\t");
        for (int c = 0; c < m_numClasses; c++) result.append(m_headerInfo.classAttribute().value(c)).append("\t");
        result.append("\n");
        for (int w = 0; w < m_numAttributes; w++) {
            if (w != m_headerInfo.classIndex()) {
                result.append(m_headerInfo.attribute(w).name()).append("\t");
                for (int c = 0; c < m_numClasses; c++) result.append(Double.toString(Math.exp(m_probOfWordGivenClass[c][w]))).append("\t");
                result.append("\n");
            }
        }
        return result.toString();
    }

    /**
   * Returns the revision string.
   * 
   * @return		the revision
   */
    public String getRevision() {
        return RevisionUtils.extract("$Revision: 6302 $");
    }

    /**
   * Main method for testing this class.
   *
   * @param argv the options
   */
    public static void main(String[] argv) {
        runClassifier(new NaiveBayesMultinomial(), argv);
    }
}
