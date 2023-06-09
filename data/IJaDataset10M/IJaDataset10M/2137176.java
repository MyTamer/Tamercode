package net.sf.kerner.utils.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author <a href="mailto:alex.kerner.24@googlemail.com">Alexander Kerner</a>
 * @version 2011-02-15
 * 
 */
public class TestRandomFactory {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
	 * Test method for
	 * {@link net.sf.kerner.utils.math.RandomFactory#generateAround(double, double)}
	 * .
	 */
    @Test
    public void testGenerateAroundDoubleDouble() {
        final double delta = 0.04;
        for (int i = 0; i < 1000; i++) {
            assertEquals((double) i, RandomFactory.generateAround(i, delta), delta);
        }
    }

    /**
	 * Test method for
	 * {@link net.sf.kerner.utils.math.RandomFactory#generateBetween(double, double)}
	 * .
	 */
    @Test
    public void testGenerateBetweenDoubleDouble() {
        final double low = 0.04;
        final double high = 0.8;
        for (int i = 0; i < 1000; i++) {
            final double r = RandomFactory.generateBetween(low, high);
            assertTrue(low <= r);
            assertTrue(r < high);
        }
    }

    /**
	 * Test method for
	 * {@link net.sf.kerner.utils.math.RandomFactory#generateAround(int, int)}.
	 */
    @Test
    public void testGenerateAroundIntInt() {
        final int delta = 4;
        for (int i = 0; i < 1000; i++) {
            assertEquals((double) i, RandomFactory.generateAround(i, delta), delta);
        }
    }

    /**
	 * Test method for
	 * {@link net.sf.kerner.utils.math.RandomFactory#generateBetween(int, int)}.
	 */
    @Test
    public void testGenerateBetweenIntInt() {
        final int low = 4;
        final int high = 80;
        for (int i = 0; i < 1000; i++) {
            final int r = RandomFactory.generateBetween(low, high);
            assertTrue(low <= r);
            assertTrue(r <= high);
        }
    }
}
