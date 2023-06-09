package com.sodad.weka.estimators;

import java.util.Random;
import com.sodad.weka.core.Statistics;

/**
 * Simple weighted normal density estimator.
 *
 * @author Eibe Frank (eibe@cs.waikato.ac.nz)
 * @version $Revision: 5680 $
 */
public class UnivariateNormalEstimator implements UnivariateDensityEstimator, UnivariateIntervalEstimator {

    /** The weighted sum of values */
    protected double m_WeightedSum = 0;

    /** The weighted sum of squared values */
    protected double m_WeightedSumSquared = 0;

    /** The weight of the values collected so far */
    protected double m_SumOfWeights = 0;

    /** The mean value (only updated when needed) */
    protected double m_Mean = 0;

    /** The variance (only updated when needed) */
    protected double m_Variance = Double.MAX_VALUE;

    /** The minimum allowed value of the variance (default: 1.0E-6 * 1.0E-6) */
    protected double m_MinVar = 1.0E-6 * 1.0E-6;

    /** Constant for Gaussian density */
    public static final double CONST = Math.log(2 * Math.PI);

    /**
   * Adds a value to the density estimator.
   *
   * @param value the value to add
   * @param weight the weight of the value
   */
    public void addValue(double value, double weight) {
        m_WeightedSum += value * weight;
        m_WeightedSumSquared += value * value * weight;
        m_SumOfWeights += weight;
    }

    /**
   * Updates mean and variance based on sufficient statistics.
   * Variance is set to m_MinVar if it becomes smaller than that
   * value. It is set to Double.MAX_VALUE if the sum of weights is
   * zero.
   */
    protected void updateMeanAndVariance() {
        m_Mean = 0;
        if (m_SumOfWeights > 0) {
            m_Mean = m_WeightedSum / m_SumOfWeights;
        }
        m_Variance = Double.MAX_VALUE;
        if (m_SumOfWeights > 0) {
            m_Variance = m_WeightedSumSquared / m_SumOfWeights - m_Mean * m_Mean;
        }
        if (m_Variance <= m_MinVar) {
            m_Variance = m_MinVar;
        }
    }

    /**
   * Returns the interval for the given confidence value. 
   * 
   * @param conf the confidence value in the interval [0, 1]
   * @return the interval
   */
    public double[][] predictIntervals(double conf) {
        updateMeanAndVariance();
        double val = Statistics.normalInverse(1.0 - (1.0 - conf) / 2.0);
        double[][] arr = new double[1][2];
        arr[0][1] = m_Mean + val * Math.sqrt(m_Variance);
        arr[0][0] = m_Mean - val * Math.sqrt(m_Variance);
        return arr;
    }

    /**
   * Returns the natural logarithm of the density estimate at the given
   * point.
   *
   * @param value the value at which to evaluate
   * @return the natural logarithm of the density estimate at the given
   * value
   */
    public double logDensity(double value) {
        updateMeanAndVariance();
        double val = -0.5 * (CONST + Math.log(m_Variance) + (value - m_Mean) * (value - m_Mean) / m_Variance);
        return val;
    }

    /**
   * Returns textual description of this estimator.
   */
    public String toString() {
        updateMeanAndVariance();
        return "Mean: " + m_Mean + "\t" + "Variance: " + m_Variance;
    }

    /**
   * Main method, used for testing this class.
   */
    public static void main(String[] args) {
        Random r = new Random();
        UnivariateNormalEstimator e = new UnivariateNormalEstimator();
        System.out.println(e);
        double sum = 0;
        for (int i = 0; i < 100000; i++) {
            sum += Math.exp(e.logDensity(r.nextDouble() * 10.0 - 5.0));
        }
        System.out.println("Approximate integral: " + 10.0 * sum / 100000);
        for (int i = 0; i < 100000; i++) {
            e.addValue(r.nextGaussian(), 1);
            e.addValue(r.nextGaussian() * 2.0, 3);
        }
        System.out.println(e);
        sum = 0;
        for (int i = 0; i < 100000; i++) {
            sum += Math.exp(e.logDensity(r.nextDouble() * 10.0 - 5.0));
        }
        System.out.println("Approximate integral: " + 10.0 * sum / 100000);
        e = new UnivariateNormalEstimator();
        for (int i = 0; i < 100000; i++) {
            e.addValue(r.nextGaussian(), 1);
            e.addValue(r.nextGaussian() * 2.0, 1);
            e.addValue(r.nextGaussian() * 2.0, 1);
            e.addValue(r.nextGaussian() * 2.0, 1);
        }
        System.out.println(e);
        sum = 0;
        for (int i = 0; i < 100000; i++) {
            sum += Math.exp(e.logDensity(r.nextDouble() * 10.0 - 5.0));
        }
        System.out.println("Approximate integral: " + 10.0 * sum / 100000);
        e = new UnivariateNormalEstimator();
        for (int i = 0; i < 100000; i++) {
            e.addValue(r.nextGaussian() * 5.0 + 3.0, 1);
        }
        System.out.println(e);
        double[][] intervals = e.predictIntervals(0.95);
        System.out.println("Lower: " + intervals[0][0] + " Upper: " + intervals[0][1]);
        double covered = 0;
        for (int i = 0; i < 100000; i++) {
            double val = r.nextGaussian() * 5.0 + 3.0;
            if (val >= intervals[0][0] && val <= intervals[0][1]) {
                covered++;
            }
        }
        System.out.println("Coverage: " + covered / 100000);
        intervals = e.predictIntervals(0.8);
        System.out.println("Lower: " + intervals[0][0] + " Upper: " + intervals[0][1]);
        covered = 0;
        for (int i = 0; i < 100000; i++) {
            double val = r.nextGaussian() * 5.0 + 3.0;
            if (val >= intervals[0][0] && val <= intervals[0][1]) {
                covered++;
            }
        }
        System.out.println("Coverage: " + covered / 100000);
    }
}
