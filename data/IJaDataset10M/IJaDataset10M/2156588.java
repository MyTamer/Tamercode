package org.systemsbiology.math.probability;

/**
 * @author sramsey
 *
 */
public class HalfLorentz implements IContinuousDistribution {

    private double mWidth;

    public HalfLorentz(double pStdev) {
        mWidth = Lorentz.RATIO_WIDTH_TO_STDEV * pStdev;
    }

    public double pdf(double x) {
        if (x < 0.0) {
            throw new IllegalArgumentException("x value out of range");
        }
        return 2.0 * Lorentz.pdf(0.0, mWidth, x);
    }

    public double cdf(double x) {
        return 2.0 * Lorentz.cdf(0.0, mWidth, x) - 1.0;
    }

    public double domainMin() {
        return 0.0;
    }

    public double domainMax() {
        return Double.POSITIVE_INFINITY;
    }

    public double mean() {
        return Double.POSITIVE_INFINITY;
    }

    public double variance() {
        return mWidth * mWidth;
    }

    public String name() {
        return "HalfLorentz";
    }
}
