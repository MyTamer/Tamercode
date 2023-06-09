package weka.filters.unsupervised.attribute;

import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.Utils;
import weka.filters.AbstractFilterTest;
import weka.filters.Filter;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests MathExpression. Run from the command line with: <p/>
 * java weka.filters.unsupervised.attribute.MathExpressionTest
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 1.2 $
 */
public class MathExpressionTest extends AbstractFilterTest {

    /** the attribute to work on */
    protected int m_AttIndex = 2;

    public MathExpressionTest(String name) {
        super(name);
    }

    /** Creates a MathExpression with the default expression */
    public Filter getFilter() {
        return getFilter(new MathExpression().getExpression());
    }

    /** Creates a MathExpression filter with the given expression */
    protected Filter getFilter(String expression) {
        MathExpression f = new MathExpression();
        f.setExpression(expression);
        f.setIgnoreRange("" + (m_AttIndex + 1));
        f.setInvertSelection(true);
        return f;
    }

    public void testTypical() {
        m_Filter = getFilter();
        Instances result = useFilter();
        assertEquals(m_Instances.numAttributes(), result.numAttributes());
        assertEquals(m_Instances.numInstances(), result.numInstances());
    }

    /**
   * checks a certain statistic
   * @param expr the filter expression
   * @param stats the value of the corresponding attribute statistics
   */
    protected void checkStatistics(String expr, double stats) {
        m_Filter = getFilter(expr);
        Instances result = useFilter();
        assertEquals(m_Instances.numAttributes(), result.numAttributes());
        assertEquals(m_Instances.numInstances(), result.numInstances());
        boolean equal = true;
        for (int i = 0; i < result.numInstances(); i++) {
            if (!Utils.eq(stats, result.instance(i).value(m_AttIndex))) {
                equal = false;
                break;
            }
        }
        if (!equal) fail("Filter and Attribute statistics differ ('" + expr + "')!");
    }

    /**
   * checks the statistics of the attribute
   */
    public void testStats() {
        checkStatistics("MIN", m_Instances.attributeStats(m_AttIndex).numericStats.min);
        checkStatistics("MAX", m_Instances.attributeStats(m_AttIndex).numericStats.max);
        checkStatistics("MEAN", m_Instances.attributeStats(m_AttIndex).numericStats.mean);
        checkStatistics("SD", m_Instances.attributeStats(m_AttIndex).numericStats.stdDev);
        checkStatistics("COUNT", m_Instances.attributeStats(m_AttIndex).numericStats.count);
        checkStatistics("SUM", m_Instances.attributeStats(m_AttIndex).numericStats.sum);
        checkStatistics("SUMSQUARED", m_Instances.attributeStats(m_AttIndex).numericStats.sumSq);
    }

    /**
   * checks whether attribute value stays the same
   */
    public void testEquality() {
        m_Filter = getFilter("A");
        Instances result = useFilter();
        assertEquals(m_Instances.numAttributes(), result.numAttributes());
        assertEquals(m_Instances.numInstances(), result.numInstances());
        boolean equal = true;
        for (int i = 0; i < result.numInstances(); i++) {
            if (m_Instances.instance(i) instanceof SparseInstance) continue;
            if (!Utils.eq(m_Instances.instance(i).value(m_AttIndex), result.instance(i).value(m_AttIndex))) {
                equal = false;
                break;
            }
        }
        if (!equal) fail("Filter modifies attribute values)!");
    }

    public void testAbs() {
        m_Filter = getFilter("abs(A)");
        Instances result = useFilter();
        assertEquals(m_Instances.numAttributes(), result.numAttributes());
        assertEquals(m_Instances.numInstances(), result.numInstances());
        boolean equal = true;
        for (int i = 0; i < result.numInstances(); i++) {
            if (m_Instances.instance(i) instanceof SparseInstance) continue;
            if (!Utils.eq(Math.abs(m_Instances.instance(i).value(m_AttIndex)), result.instance(i).value(m_AttIndex))) {
                equal = false;
                break;
            }
        }
        if (!equal) fail("Filter produces different result)!");
    }

    public void testsqrt() {
        m_Filter = getFilter("sqrt(A)");
        Instances result = useFilter();
        assertEquals(m_Instances.numAttributes(), result.numAttributes());
        assertEquals(m_Instances.numInstances(), result.numInstances());
        boolean equal = true;
        for (int i = 0; i < result.numInstances(); i++) {
            if (m_Instances.instance(i) instanceof SparseInstance) continue;
            if (!Utils.eq(Math.sqrt(m_Instances.instance(i).value(m_AttIndex)), result.instance(i).value(m_AttIndex))) {
                equal = false;
                break;
            }
        }
        if (!equal) fail("Filter produces different result)!");
    }

    public void testLog() {
        m_Filter = getFilter("log(A)");
        Instances result = useFilter();
        assertEquals(m_Instances.numAttributes(), result.numAttributes());
        assertEquals(m_Instances.numInstances(), result.numInstances());
        boolean equal = true;
        for (int i = 0; i < result.numInstances(); i++) {
            if (m_Instances.instance(i) instanceof SparseInstance) continue;
            if (!Utils.eq(Math.log(m_Instances.instance(i).value(m_AttIndex)), result.instance(i).value(m_AttIndex))) {
                equal = false;
                break;
            }
        }
        if (!equal) fail("Filter produces different result)!");
    }

    public void testExp() {
        m_Filter = getFilter("exp(A)");
        Instances result = useFilter();
        assertEquals(m_Instances.numAttributes(), result.numAttributes());
        assertEquals(m_Instances.numInstances(), result.numInstances());
        boolean equal = true;
        for (int i = 0; i < result.numInstances(); i++) {
            if (m_Instances.instance(i) instanceof SparseInstance) continue;
            if (!Utils.eq(Math.exp(m_Instances.instance(i).value(m_AttIndex)), result.instance(i).value(m_AttIndex))) {
                equal = false;
                break;
            }
        }
        if (!equal) fail("Filter produces different result)!");
    }

    public void testSin() {
        m_Filter = getFilter("sin(A)");
        Instances result = useFilter();
        assertEquals(m_Instances.numAttributes(), result.numAttributes());
        assertEquals(m_Instances.numInstances(), result.numInstances());
        boolean equal = true;
        for (int i = 0; i < result.numInstances(); i++) {
            if (m_Instances.instance(i) instanceof SparseInstance) continue;
            if (!Utils.eq(Math.sin(m_Instances.instance(i).value(m_AttIndex)), result.instance(i).value(m_AttIndex))) {
                equal = false;
                break;
            }
        }
        if (!equal) fail("Filter produces different result)!");
    }

    public void testCos() {
        m_Filter = getFilter("cos(A)");
        Instances result = useFilter();
        assertEquals(m_Instances.numAttributes(), result.numAttributes());
        assertEquals(m_Instances.numInstances(), result.numInstances());
        boolean equal = true;
        for (int i = 0; i < result.numInstances(); i++) {
            if (m_Instances.instance(i) instanceof SparseInstance) continue;
            if (!Utils.eq(Math.cos(m_Instances.instance(i).value(m_AttIndex)), result.instance(i).value(m_AttIndex))) {
                equal = false;
                break;
            }
        }
        if (!equal) fail("Filter produces different result)!");
    }

    public void testTan() {
        m_Filter = getFilter("tan(A)");
        Instances result = useFilter();
        assertEquals(m_Instances.numAttributes(), result.numAttributes());
        assertEquals(m_Instances.numInstances(), result.numInstances());
        boolean equal = true;
        for (int i = 0; i < result.numInstances(); i++) {
            if (m_Instances.instance(i) instanceof SparseInstance) continue;
            if (!Utils.eq(Math.tan(m_Instances.instance(i).value(m_AttIndex)), result.instance(i).value(m_AttIndex))) {
                equal = false;
                break;
            }
        }
        if (!equal) fail("Filter produces different result)!");
    }

    public void testRint() {
        m_Filter = getFilter("rint(A)");
        Instances result = useFilter();
        assertEquals(m_Instances.numAttributes(), result.numAttributes());
        assertEquals(m_Instances.numInstances(), result.numInstances());
        boolean equal = true;
        for (int i = 0; i < result.numInstances(); i++) {
            if (m_Instances.instance(i) instanceof SparseInstance) continue;
            if (!Utils.eq(Math.rint(m_Instances.instance(i).value(m_AttIndex)), result.instance(i).value(m_AttIndex))) {
                equal = false;
                break;
            }
        }
        if (!equal) fail("Filter produces different result)!");
    }

    public void testFloor() {
        m_Filter = getFilter("floor(A)");
        Instances result = useFilter();
        assertEquals(m_Instances.numAttributes(), result.numAttributes());
        assertEquals(m_Instances.numInstances(), result.numInstances());
        boolean equal = true;
        for (int i = 0; i < result.numInstances(); i++) {
            if (m_Instances.instance(i) instanceof SparseInstance) continue;
            if (!Utils.eq(Math.floor(m_Instances.instance(i).value(m_AttIndex)), result.instance(i).value(m_AttIndex))) {
                equal = false;
                break;
            }
        }
        if (!equal) fail("Filter produces different result)!");
    }

    public void testPow2() {
        m_Filter = getFilter("pow(A,2)");
        Instances result = useFilter();
        assertEquals(m_Instances.numAttributes(), result.numAttributes());
        assertEquals(m_Instances.numInstances(), result.numInstances());
        boolean equal = true;
        for (int i = 0; i < result.numInstances(); i++) {
            if (m_Instances.instance(i) instanceof SparseInstance) continue;
            if (!Utils.eq(Math.pow(m_Instances.instance(i).value(m_AttIndex), 2), result.instance(i).value(m_AttIndex))) {
                equal = false;
                break;
            }
        }
        if (!equal) fail("Filter produces different result)!");
    }

    public void testCeil() {
        m_Filter = getFilter("ceil(A)");
        Instances result = useFilter();
        assertEquals(m_Instances.numAttributes(), result.numAttributes());
        assertEquals(m_Instances.numInstances(), result.numInstances());
        boolean equal = true;
        for (int i = 0; i < result.numInstances(); i++) {
            if (m_Instances.instance(i) instanceof SparseInstance) continue;
            if (!Utils.eq(Math.ceil(m_Instances.instance(i).value(m_AttIndex)), result.instance(i).value(m_AttIndex))) {
                equal = false;
                break;
            }
        }
        if (!equal) fail("Filter produces different result)!");
    }

    public static Test suite() {
        return new TestSuite(MathExpressionTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
