package reconcile.weka.classifiers.trees.m5;

import java.io.Serializable;
import reconcile.weka.core.Instances;
import reconcile.weka.core.Utils;

/**
 * Stores split information.
 *
 * @author Yong Wang (yongwang@cs.waikato.ac.nz)
 * @author Mark Hall (mhall@cs.waikato.ac.nz)
 * @version $Revision: 1.1 $
 */
public final class YongSplitInfo implements Cloneable, Serializable, SplitEvaluate {

    private int number;

    private int first;

    private int last;

    private int position;

    private double maxImpurity;

    private double leftAve;

    private double rightAve;

    private int splitAttr;

    private double splitValue;

    /**
   * Constructs an object which contains the split information
   * @param low the index of the first instance
   * @param high the index of the last instance
   * @param attr an attribute
   */
    public YongSplitInfo(int low, int high, int attr) {
        number = high - low + 1;
        first = low;
        last = high;
        position = -1;
        maxImpurity = -1.e20;
        splitAttr = attr;
        splitValue = 0.0;
        Utils.SMALL = 1e-10;
    }

    /**
   * Makes a copy of this SplitInfo object
   */
    public final SplitEvaluate copy() throws Exception {
        YongSplitInfo s = (YongSplitInfo) this.clone();
        return s;
    }

    /**
   * Resets the object of split information
   * @param low the index of the first instance
   * @param high the index of the last instance
   * @param attr the attribute
   */
    public final void initialize(int low, int high, int attr) {
        number = high - low + 1;
        first = low;
        last = high;
        position = -1;
        maxImpurity = -1.e20;
        splitAttr = attr;
        splitValue = 0.0;
    }

    /**
   * Converts the spliting information to string
   * @param inst the instances
   */
    public final String toString(Instances inst) {
        StringBuffer text = new StringBuffer();
        text.append("Print SplitInfo:\n");
        text.append("    Instances:\t\t" + number + " (" + first + "-" + position + "," + (position + 1) + "-" + last + ")\n");
        text.append("    Maximum Impurity Reduction:\t" + Utils.doubleToString(maxImpurity, 1, 4) + "\n");
        text.append("    Left average:\t" + leftAve + "\n");
        text.append("    Right average:\t" + rightAve + "\n");
        if (maxImpurity > 0.0) text.append("    Splitting function:\t" + inst.attribute(splitAttr).name() + " = " + splitValue + "\n"); else text.append("    Splitting function:\tnull\n");
        return text.toString();
    }

    /** 
   * Finds the best splitting point for an attribute in the instances
   * @param attr the splitting attribute
   * @param inst the instances
   * @exception Exception if something goes wrong
   */
    public final void attrSplit(int attr, Instances inst) throws Exception {
        int i, len, count, part;
        Impurity imp;
        int low = 0;
        int high = inst.numInstances() - 1;
        this.initialize(low, high, attr);
        if (number < 4) {
            return;
        }
        len = ((high - low + 1) < 5) ? 1 : (high - low + 1) / 5;
        position = low;
        part = low + len - 1;
        imp = new Impurity(part, attr, inst, 5);
        count = 0;
        for (i = low + len; i <= high - len - 1; i++) {
            imp.incremental(inst.instance(i).classValue(), 1);
            if (Utils.eq(inst.instance(i + 1).value(attr), inst.instance(i).value(attr)) == false) {
                count = i;
                if (imp.impurity > maxImpurity) {
                    maxImpurity = imp.impurity;
                    splitValue = (inst.instance(i).value(attr) + inst.instance(i + 1).value(attr)) * 0.5;
                    leftAve = imp.sl / imp.nl;
                    rightAve = imp.sr / imp.nr;
                    position = i;
                }
            }
        }
    }

    /**
   * Returns the impurity of this split
   *
   * @return the impurity of this split
   */
    public double maxImpurity() {
        return maxImpurity;
    }

    /**
   * Returns the attribute used in this split
   *
   * @return the attribute used in this split
   */
    public int splitAttr() {
        return splitAttr;
    }

    /**
   * Returns the position of the split in the sorted values. -1 indicates that
   * a split could not be found.
   *
   * @return an <code>int</code> value
   */
    public int position() {
        return position;
    }

    /**
   * Returns the split value
   *
   * @return the split value
   */
    public double splitValue() {
        return splitValue;
    }
}
