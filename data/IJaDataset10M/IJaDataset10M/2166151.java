package test.dr.inference.operators;

import dr.evolution.io.NewickImporter;
import dr.evolution.tree.FlexibleTree;
import dr.evolution.tree.Tree;
import dr.evolution.util.Units;
import dr.evomodel.speciation.*;
import dr.evomodelxml.tree.TreeModelParser;
import dr.inference.model.Likelihood;
import dr.inference.model.Parameter;
import dr.inference.operators.CoercionMode;
import dr.inference.operators.RandomWalkOperator;
import junit.framework.*;

/**
 * RandomWalkOperatorTest.
 *
 * @author Andrew Rambaut
 * @version 1.0
 * @since <pre>08/26/2007</pre>
 */
public class RandomWalkOperatorTest extends TestCase {

    public RandomWalkOperatorTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void testRandomWalkOperator() {
        Parameter parameter = new Parameter.Default("test", 0.5, 0.0, 1.0);
        RandomWalkOperator rwo = new RandomWalkOperator(parameter, 1.0, RandomWalkOperator.BoundaryCondition.reflecting, 1.0, CoercionMode.COERCION_OFF);
        double test1 = rwo.reflectValue(8.7654321, 3.14159265, Double.POSITIVE_INFINITY);
        double test2 = rwo.reflectValue(8.7654321, Double.NEGATIVE_INFINITY, 3.14159265);
        double test3 = rwo.reflectValue(1.2345678, 3.14159265, 2.0 * 3.14159265);
        double test4 = rwo.reflectValue(12345678.987654321, 3.14159265, 2.0 * 3.14159265);
        double test1b = rwo.reflectValueLoop(8.7654321, 3.14159265, Double.POSITIVE_INFINITY);
        double test2b = rwo.reflectValueLoop(8.7654321, Double.NEGATIVE_INFINITY, 3.14159265);
        double test3b = rwo.reflectValueLoop(1.2345678, 3.14159265, 2.0 * 3.14159265);
        double test4b = rwo.reflectValueLoop(12345678.987654321, 3.14159265, 2.0 * 3.14159265);
        assertEquals(test1, test1b);
        assertEquals(test2, test2b);
        assertEquals(test3, test3b);
        assertTrue(Math.abs(test4 - test4b) < 0.001);
    }

    public static Test suite() {
        return new TestSuite(RandomWalkOperatorTest.class);
    }
}
