package mulan.classifier.meta.thresholding;

import java.util.logging.Level;
import java.util.logging.Logger;
import mulan.classifier.InvalidDataException;
import mulan.classifier.MultiLabelLearner;
import mulan.classifier.MultiLabelOutput;
import mulan.classifier.meta.MultiLabelMetaLearner;
import mulan.classifier.transformation.BinaryRelevance;
import mulan.core.MulanRuntimeException;
import mulan.data.InvalidDataFormatException;
import mulan.data.LabelsMetaData;
import mulan.data.MultiLabelInstances;
import mulan.evaluation.measure.BipartitionMeasureBase;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;
import weka.core.Utils;

/**
 * RCut(Rank-based cut): Selects the k top ranked labels for each instance,
 * where k is a parameter provided by the user or automatically tuned.
 *
 <!-- technical-bibtex-start -->
 * BibTeX:
 * <pre>
 * &#64;inproceedings{Yang2001,
 *    author = {Yiming Yang},
 *    booktitle = {Proceedings of the 24th annual international ACM SIGIR conference on Research and development in information retrieval},
 *    pages = {137 - 145},
 *    title = {A study of thresholding strategies for text categorization},
 *    year = {2001},
 *    location = {New Orleans, Louisiana, United States}
 * }
 * </pre>
 * <p/>
 <!-- technical-bibtex-end -->
 *
 * @author Marios Ioannou
 * @author George Sakkas
 * @author Grigorios Tsoumakas
 * @version 2010.12.14
 */
public class RCut extends MultiLabelMetaLearner {

    /** the top t number of labels to consider relevant */
    private int t = 0;

    /** measure for auto-tuning the threshold */
    private BipartitionMeasureBase measure;

    /** the folds of the cv to evaluate different thresholds */
    private int folds;

    /** copy of a clean multi-label learner to use at each fold */
    private MultiLabelLearner foldLearner;

    public RCut() {
        this(new BinaryRelevance(new J48()));
    }

    /**
     * Creates a new instance of RCut
     *
     * @param baseLearner the underlying multi-label learner
     */
    public RCut(MultiLabelLearner baseLearner) {
        super(baseLearner);
    }

    /**
     * Creates a new instance of RCut
     *
     * @param baseLearner the underlying multi-label learner
     * @param aMeasure measure to optimize
     * @param someFolds cross-validation folds
     */
    public RCut(MultiLabelLearner baseLearner, BipartitionMeasureBase aMeasure, int someFolds) {
        super(baseLearner);
        measure = aMeasure;
        folds = someFolds;
        try {
            foldLearner = baseLearner.makeCopy();
        } catch (Exception ex) {
            Logger.getLogger(RCut.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates a new instance of RCut
     *
     * @param baseLearner the underlying multi-label learner
     * @param aMeasure measure to optimize
     */
    public RCut(MultiLabelLearner baseLearner, BipartitionMeasureBase aMeasure) {
        super(baseLearner);
        measure = aMeasure;
    }

    /**
     * Automatically selects a threshold based on training set performance
     * evaluated using cross-validation
     *
     * @param measure performance is evaluated based on this parameter
     * @param folds number of cross-validation folds
     * @throws InvalidDataFormatException
     * @throws Exception
     */
    private void autoTuneThreshold(MultiLabelInstances trainingData, BipartitionMeasureBase measure, int folds) throws InvalidDataFormatException, Exception {
        if (folds < 2) {
            throw new IllegalArgumentException("folds should be more than 1");
        }
        double[] totalDiff = new double[numLabels + 1];
        LabelsMetaData labelsMetaData = trainingData.getLabelsMetaData();
        MultiLabelLearner tempLearner = foldLearner.makeCopy();
        for (int f = 0; f < folds; f++) {
            Instances train = trainingData.getDataSet().trainCV(folds, f);
            MultiLabelInstances trainMulti = new MultiLabelInstances(train, labelsMetaData);
            Instances test = trainingData.getDataSet().testCV(folds, f);
            MultiLabelInstances testMulti = new MultiLabelInstances(test, labelsMetaData);
            tempLearner.build(trainMulti);
            double[] diff = computeThreshold(tempLearner, testMulti, measure);
            for (int k = 0; k < diff.length; k++) {
                totalDiff[k] += diff[k];
            }
        }
        t = Utils.minIndex(totalDiff);
    }

    /**
     * Evaluates the performance of different threshold values
     *
     * @param data the test data to evaluate different thresholds
     * @param measure the evaluation is based on this parameter
     * @return the sum of differences from the optimal value of the measure for
     * each instance and threshold
     * @throws Exception
     */
    private double[] computeThreshold(MultiLabelLearner learner, MultiLabelInstances data, BipartitionMeasureBase measure) throws Exception {
        double[] diff = new double[numLabels + 1];
        measure.reset();
        for (int j = 0; j < data.getNumInstances(); j++) {
            Instance instance = data.getDataSet().instance(j);
            if (data.hasMissingLabels(instance)) {
                continue;
            }
            MultiLabelOutput mlo = learner.makePrediction(instance);
            boolean[] trueLabels = new boolean[numLabels];
            for (int counter = 0; counter < numLabels; counter++) {
                int classIdx = labelIndices[counter];
                String classValue = instance.attribute(classIdx).value((int) instance.value(classIdx));
                trueLabels[counter] = classValue.equals("1");
            }
            int[] ranking = mlo.getRanking();
            for (int threshold = 0; threshold <= numLabels; threshold++) {
                boolean[] bipartition = new boolean[numLabels];
                for (int k = 0; k < numLabels; k++) {
                    if (ranking[k] <= threshold) {
                        bipartition[k] = true;
                    }
                }
            }
        }
        return diff;
    }

    protected void buildInternal(MultiLabelInstances trainingData) throws Exception {
        baseLearner.build(trainingData);
        MultiLabelOutput mlo = baseLearner.makePrediction(trainingData.getDataSet().firstInstance());
        if (!mlo.hasRanking()) {
            throw new MulanRuntimeException("Learner is not a ranker");
        }
        if (measure == null) {
            t = (int) Math.round(trainingData.getCardinality());
            t = 2;
        } else {
            if (folds == 0) {
                double[] diff = computeThreshold(baseLearner, trainingData, measure);
                t = Utils.minIndex(diff);
            } else {
                autoTuneThreshold(trainingData, measure, folds);
            }
        }
    }

    public TechnicalInformation getTechnicalInformation() {
        TechnicalInformation result = new TechnicalInformation(Type.INPROCEEDINGS);
        result.setValue(Field.AUTHOR, "Yiming Yang");
        result.setValue(Field.TITLE, "A study of thresholding strategies for text categorization");
        result.setValue(Field.BOOKTITLE, "Proceedings of the 24th annual international ACM SIGIR conference on Research and development in information retrieval");
        result.setValue(Field.PAGES, "137 - 145");
        result.setValue(Field.LOCATION, "New Orleans, Louisiana, United States");
        result.setValue(Field.YEAR, "2001");
        return result;
    }

    protected MultiLabelOutput makePredictionInternal(Instance instance) throws Exception, InvalidDataException {
        boolean[] predictedLabels;
        MultiLabelOutput mlo = baseLearner.makePrediction(instance);
        int[] ranking = mlo.getRanking();
        predictedLabels = new boolean[numLabels];
        for (int i = 0; i < numLabels; i++) {
            if (ranking[i] <= t) {
                predictedLabels[i] = true;
            } else {
                predictedLabels[i] = false;
            }
        }
        MultiLabelOutput newOutput = new MultiLabelOutput(predictedLabels);
        return newOutput;
    }

    @Override
    public void setDebug(boolean debug) {
        super.setDebug(debug);
        baseLearner.setDebug(debug);
    }
}
