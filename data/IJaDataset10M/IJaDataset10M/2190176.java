package org.opt4j.operator.crossover;

import java.util.List;
import org.opt4j.common.random.Rand;
import org.opt4j.operator.normalize.NormalizeDouble;
import org.opt4j.start.Constant;
import com.google.inject.Inject;

/**
 * The {@link CrossoverDoubleUnfairAverage} implements the unfair average
 * crossover proposed by Nomura and Miyhoshi, 1996.
 * 
 * <p>
 * The unfair average crossover is applied to the whole genotype, i.e., the used
 * vector of double values. In the original work, two offspring created using
 * </p>
 * <p>
 * {@code z_1 = (1 + alpha)x - alpha * y} for {@code i = 1, ..., j} and<br>
 * {@code z_1 = -alpha * x + (1 + alpha)y} for {@code i = j + 1, ..., n}<br>
 * {@code z_2 = (1 - alpha)x + alpha * y} for {@code i = 1, ..., j} and<br>
 * {@code z_2 = alpha * x + (1 - alpha)y} for {@code i = j + 1, ..., n}
 * </p>
 * <p>
 * for each double value in the double vector. In this implementation, one
 * offspring is created following {@code z_1} with a probability of {@code 0.5}
 * and following {@code z_2} with a probability of {@code 0.5}, respectively. At
 * this juncture, {@code n} is the number of variables in the vector and
 * {@code j} a randomly chosen integer between {@code 1} and {@code n}. The
 * value {@code alpha} is within the range (0, 0.5).
 * </p>
 * <p>
 * Unlike the BLX and SBX operator, the unfair average will create offspring
 * towards one of the parent solutions.
 * </p>
 * 
 * @author glass
 * 
 */
public class CrossoverDoubleUnfairAverage extends CrossoverDouble {

    protected final double alpha;

    /**
	 * Constructs an {@link CrossoverDoubleUnfairAverage} with an alpha value
	 * and a random number generator.
	 * 
	 * @param alpha
	 *            the alpha value
	 * @param normalize
	 *            a normalize operator
	 * @param random
	 *            the random number generator
	 */
    @Inject
    public CrossoverDoubleUnfairAverage(@Constant(value = "alpha", namespace = CrossoverDoubleUnfairAverage.class) double alpha, NormalizeDouble normalize, Rand random) {
        super(normalize, random);
        this.alpha = alpha;
    }

    @Override
    protected void crossover(List<Double> p1, List<Double> p2, List<Double> o1, List<Double> o2) {
        double z1, z2, x, y;
        int size = p1.size();
        int j = random.nextInt(size);
        for (int i = 0; i < size; i++) {
            x = p1.get(i);
            y = p2.get(i);
            if (i <= j) {
                z1 = (1 + alpha) * x - alpha * y;
                z2 = (1 - alpha) * x + alpha * y;
            } else {
                z1 = -alpha * x + (1 + alpha) * y;
                z2 = alpha * x + (1 - alpha) * y;
            }
            o1.add(z1);
            o2.add(z2);
        }
    }
}
