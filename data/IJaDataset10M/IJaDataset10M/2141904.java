package com.sodad.weka.classifiers.functions.supportVector;

import java.util.Enumeration;
import java.util.Vector;
import com.sodad.weka.core.Instances;
import com.sodad.weka.core.Option;
import com.sodad.weka.core.RevisionUtils;
import com.sodad.weka.core.TechnicalInformation;
import com.sodad.weka.core.TechnicalInformationHandler;
import com.sodad.weka.core.Utils;
import com.sodad.weka.core.TechnicalInformation.Field;
import com.sodad.weka.core.TechnicalInformation.Type;

/**
 <!-- globalinfo-start -->
 * Learn SVM for regression using SMO with Shevade, Keerthi, et al. adaption of the stopping criterion.<br/>
 * <br/>
 * For more information see:<br/>
 * <br/>
 * S.K. Shevade, S.S. Keerthi, C. Bhattacharyya, K.R.K. Murthy: Improvements to the SMO Algorithm for SVM Regression. In: IEEE Transactions on Neural Networks, 1999.<br/>
 * <br/>
 * S.K. Shevade, S.S. Keerthi, C. Bhattacharyya, K.R.K. Murthy (1999). Improvements to the SMO Algorithm for SVM Regression. Control Division, Dept. of Mechanical Engineering.
 * <p/>
 <!-- globalinfo-end -->
 *
 <!-- technical-bibtex-start -->
 * BibTeX:
 * <pre>
 * &#64;inproceedings{Shevade1999,
 *    author = {S.K. Shevade and S.S. Keerthi and C. Bhattacharyya and K.R.K. Murthy},
 *    booktitle = {IEEE Transactions on Neural Networks},
 *    title = {Improvements to the SMO Algorithm for SVM Regression},
 *    year = {1999},
 *    PS = {http://guppy.mpe.nus.edu.sg/\~mpessk/svm/ieee_smo_reg.ps.gz}
 * }
 * 
 * &#64;techreport{Shevade1999,
 *    address = {Control Division, Dept. of Mechanical Engineering},
 *    author = {S.K. Shevade and S.S. Keerthi and C. Bhattacharyya and K.R.K. Murthy},
 *    institution = {National University of Singapore},
 *    number = {CD-99-16},
 *    title = {Improvements to the SMO Algorithm for SVM Regression},
 *    year = {1999},
 *    PS = {http://guppy.mpe.nus.edu.sg/\~mpessk/svm/smoreg_mod.ps.gz}
 * }
 * </pre>
 * <p/>
 <!-- technical-bibtex-end -->
 *
 <!-- options-start -->
 * Valid options are: <p/>
 * 
 * <pre> -T &lt;double&gt;
 *  The tolerance parameter for checking the stopping criterion.
 *  (default 0.001)</pre>
 * 
 * <pre> -V
 *  Use variant 1 of the algorithm when true, otherwise use variant 2.
 *  (default true)</pre>
 * 
 * <pre> -P &lt;double&gt;
 *  The epsilon for round-off error.
 *  (default 1.0e-12)</pre>
 * 
 * <pre> -L &lt;double&gt;
 *  The epsilon parameter in epsilon-insensitive loss function.
 *  (default 1.0e-3)</pre>
 * 
 * <pre> -W &lt;double&gt;
 *  The random number seed.
 *  (default 1)</pre>
 * 
 <!-- options-end -->
 *
 * @author  Remco Bouckaert (remco@cs.waikato.ac.nz,rrb@xm.co.nz)
 * @version $Revision: 1.4 $
 */
public class RegSMOImproved extends RegSMO implements TechnicalInformationHandler {

    /** for serialization */
    private static final long serialVersionUID = 471692841446029784L;

    public static final int I0 = 3;

    public static final int I0a = 1;

    public static final int I0b = 2;

    public static final int I1 = 4;

    public static final int I2 = 8;

    public static final int I3 = 16;

    /** The different sets used by the algorithm. */
    protected SMOset m_I0;

    /** Index set {i: 0 < m_alpha[i] < C || 0 < m_alphaStar[i] < C}} */
    protected int[] m_iSet;

    /** b.up and b.low boundaries used to determine stopping criterion */
    protected double m_bUp, m_bLow;

    /** index of the instance that gave us b.up and b.low */
    protected int m_iUp, m_iLow;

    /** tolerance parameter used for checking stopping criterion b.up < b.low + 2 tol */
    double m_fTolerance = 0.001;

    /** set true to use variant 1 of the paper, otherwise use variant 2 */
    boolean m_bUseVariant1 = true;

    /**
   * Returns a string describing the object
   * 
   * @return 		a description suitable for
   * 			displaying in the explorer/experimenter gui
   */
    public String globalInfo() {
        return "Learn SVM for regression using SMO with Shevade, Keerthi, et al. " + "adaption of the stopping criterion.\n\n" + "For more information see:\n\n" + getTechnicalInformation().toString();
    }

    /**
   * Returns an instance of a TechnicalInformation object, containing 
   * detailed information about the technical background of this class,
   * e.g., paper reference or book this class is based on.
   * 
   * @return 		the technical information about this class
   */
    public TechnicalInformation getTechnicalInformation() {
        TechnicalInformation result;
        TechnicalInformation additional;
        result = new TechnicalInformation(Type.INPROCEEDINGS);
        result.setValue(Field.AUTHOR, "S.K. Shevade and S.S. Keerthi and C. Bhattacharyya and K.R.K. Murthy");
        result.setValue(Field.TITLE, "Improvements to the SMO Algorithm for SVM Regression");
        result.setValue(Field.BOOKTITLE, "IEEE Transactions on Neural Networks");
        result.setValue(Field.YEAR, "1999");
        result.setValue(Field.PS, "http://guppy.mpe.nus.edu.sg/~mpessk/svm/ieee_smo_reg.ps.gz");
        additional = result.add(Type.TECHREPORT);
        additional.setValue(Field.AUTHOR, "S.K. Shevade and S.S. Keerthi and C. Bhattacharyya and K.R.K. Murthy");
        additional.setValue(Field.TITLE, "Improvements to the SMO Algorithm for SVM Regression");
        additional.setValue(Field.INSTITUTION, "National University of Singapore");
        additional.setValue(Field.ADDRESS, "Control Division, Dept. of Mechanical Engineering");
        additional.setValue(Field.NUMBER, "CD-99-16");
        additional.setValue(Field.YEAR, "1999");
        additional.setValue(Field.PS, "http://guppy.mpe.nus.edu.sg/~mpessk/svm/smoreg_mod.ps.gz");
        return result;
    }

    /**
   * Returns an enumeration describing the available options
   * 
   * @return 		an enumeration of all the available options
   */
    public Enumeration listOptions() {
        Vector result = new Vector();
        result.addElement(new Option("\tThe tolerance parameter for checking the stopping criterion.\n" + "\t(default 0.001)", "T", 1, "-T <double>"));
        result.addElement(new Option("\tUse variant 1 of the algorithm when true, otherwise use variant 2.\n" + "\t(default true)", "V", 0, "-V"));
        Enumeration enm = super.listOptions();
        while (enm.hasMoreElements()) {
            result.addElement(enm.nextElement());
        }
        return result.elements();
    }

    /**
   * Parses a given list of options. <p/>
   *
   <!-- options-start -->
   * Valid options are: <p/>
   * 
   * <pre> -T &lt;double&gt;
   *  The tolerance parameter for checking the stopping criterion.
   *  (default 0.001)</pre>
   * 
   * <pre> -V
   *  Use variant 1 of the algorithm when true, otherwise use variant 2.
   *  (default true)</pre>
   * 
   * <pre> -P &lt;double&gt;
   *  The epsilon for round-off error.
   *  (default 1.0e-12)</pre>
   * 
   * <pre> -L &lt;double&gt;
   *  The epsilon parameter in epsilon-insensitive loss function.
   *  (default 1.0e-3)</pre>
   * 
   * <pre> -W &lt;double&gt;
   *  The random number seed.
   *  (default 1)</pre>
   * 
   <!-- options-end -->
   *
   * @param options 	the list of options as an array of strings
   * @throws Exception 	if an option is not supported 
   */
    public void setOptions(String[] options) throws Exception {
        String tmpStr;
        tmpStr = Utils.getOption('T', options);
        if (tmpStr.length() != 0) {
            setTolerance(Double.parseDouble(tmpStr));
        } else {
            setTolerance(0.001);
        }
        setUseVariant1(Utils.getFlag('V', options));
        super.setOptions(options);
    }

    /**
   * Gets the current settings of the object.
   *
   * @return 		an array of strings suitable for passing to setOptions
   */
    public String[] getOptions() {
        int i;
        Vector result;
        String[] options;
        result = new Vector();
        options = super.getOptions();
        for (i = 0; i < options.length; i++) result.add(options[i]);
        result.add("-T");
        result.add("" + getTolerance());
        if (m_bUseVariant1) result.add("-V");
        return (String[]) result.toArray(new String[result.size()]);
    }

    /**
   * Returns the tip text for this property
   * 
   * @return 		a description suitable for
   * 			displaying in the explorer/experimenter gui
   */
    public String toleranceTipText() {
        return "tolerance parameter used for checking stopping criterion b.up < b.low + 2 tol";
    }

    /**
   * returns the current tolerance
   * 
   * @return	the tolerance
   */
    public double getTolerance() {
        return m_fTolerance;
    }

    /**
   * sets the tolerance
   * 
   * @param d	the new tolerance
   */
    public void setTolerance(double d) {
        m_fTolerance = d;
    }

    /**
   * Returns the tip text for this property
   * 
   * @return 		a description suitable for
   * 			displaying in the explorer/experimenter gui
   */
    public String useVariant1TipText() {
        return "set true to use variant 1 of the paper, otherwise use variant 2.";
    }

    /**
   * Whether variant 1 is used
   * 
   * @return		true if variant 1 is used
   */
    public boolean isUseVariant1() {
        return m_bUseVariant1;
    }

    /**
   * Sets whether to use variant 1
   * 
   * @param b		if true then variant 1 is used
   */
    public void setUseVariant1(boolean b) {
        m_bUseVariant1 = b;
    }

    /** 
   * takeStep method from Shevade et al.s paper.
   * parameters correspond to pseudocode from paper.
   * 
   * @param i1
   * @param i2
   * @param alpha2
   * @param alpha2Star
   * @param phi2
   * @return
   * @throws Exception
   */
    protected int takeStep(int i1, int i2, double alpha2, double alpha2Star, double phi2) throws Exception {
        if (i1 == i2) {
            return 0;
        }
        double C1 = m_C * m_data.instance(i1).weight();
        double C2 = m_C * m_data.instance(i2).weight();
        double alpha1 = m_alpha[i1];
        double alpha1Star = m_alphaStar[i1];
        double phi1 = m_error[i1];
        double k11 = m_kernel.eval(i1, i1, m_data.instance(i1));
        double k12 = m_kernel.eval(i1, i2, m_data.instance(i1));
        double k22 = m_kernel.eval(i2, i2, m_data.instance(i2));
        double eta = -2 * k12 + k11 + k22;
        double gamma = alpha1 - alpha1Star + alpha2 - alpha2Star;
        double alpha1old = alpha1;
        double alpha1Starold = alpha1Star;
        double alpha2old = alpha2;
        double alpha2Starold = alpha2Star;
        double deltaPhi = phi1 - phi2;
        if (findOptimalPointOnLine(i1, alpha1, alpha1Star, C1, i2, alpha2, alpha2Star, C2, gamma, eta, deltaPhi)) {
            alpha1 = m_alpha[i1];
            alpha1Star = m_alphaStar[i1];
            alpha2 = m_alpha[i2];
            alpha2Star = m_alphaStar[i2];
            double dAlpha1 = alpha1 - alpha1old - (alpha1Star - alpha1Starold);
            double dAlpha2 = alpha2 - alpha2old - (alpha2Star - alpha2Starold);
            for (int j = m_I0.getNext(-1); j != -1; j = m_I0.getNext(j)) {
                if ((j != i1) && (j != i2)) {
                    m_error[j] -= dAlpha1 * m_kernel.eval(i1, j, m_data.instance(i1)) + dAlpha2 * m_kernel.eval(i2, j, m_data.instance(i2));
                }
            }
            m_error[i1] -= dAlpha1 * k11 + dAlpha2 * k12;
            m_error[i2] -= dAlpha1 * k12 + dAlpha2 * k22;
            updateIndexSetFor(i1, C1);
            updateIndexSetFor(i2, C2);
            m_bUp = Double.MAX_VALUE;
            m_bLow = -Double.MAX_VALUE;
            for (int j = m_I0.getNext(-1); j != -1; j = m_I0.getNext(j)) {
                updateBoundaries(j, m_error[j]);
            }
            if (!m_I0.contains(i1)) {
                updateBoundaries(i1, m_error[i1]);
            }
            if (!m_I0.contains(i2)) {
                updateBoundaries(i2, m_error[i2]);
            }
            return 1;
        } else {
            return 0;
        }
    }

    /**
   * updates the index sets I0a, IOb, I1, I2 and I3 for vector i
   * 
   * @param i index of vector
   * @param C capacity for vector i
   * @throws Exception
   */
    protected void updateIndexSetFor(int i, double C) throws Exception {
        if (m_alpha[i] == 0 && m_alphaStar[i] == 0) {
            m_iSet[i] = I1;
            m_I0.delete(i);
        } else if (m_alpha[i] > 0) {
            if (m_alpha[i] < C) {
                if ((m_iSet[i] & I0) == 0) {
                    m_I0.insert(i);
                }
                m_iSet[i] = I0a;
            } else {
                m_iSet[i] = I3;
                m_I0.delete(i);
            }
        } else {
            if (m_alphaStar[i] < C) {
                if ((m_iSet[i] & I0) == 0) {
                    m_I0.insert(i);
                }
                m_iSet[i] = I0b;
            } else {
                m_iSet[i] = I2;
                m_I0.delete(i);
            }
        }
    }

    /**
   * updates boundaries bLow and bHi and corresponding indexes
   * 
   * @param i2 index of vector
   * @param F2 error of vector i2
   */
    protected void updateBoundaries(int i2, double F2) {
        int iSet = m_iSet[i2];
        double FLow = m_bLow;
        if ((iSet & (I2 | I0b)) > 0) {
            FLow = F2 + m_epsilon;
        } else if ((iSet & (I1 | I0a)) > 0) {
            FLow = F2 - m_epsilon;
        }
        if (m_bLow < FLow) {
            m_bLow = FLow;
            m_iLow = i2;
        }
        double FUp = m_bUp;
        if ((iSet & (I3 | I0a)) > 0) {
            FUp = F2 - m_epsilon;
        } else if ((iSet & (I1 | I0b)) > 0) {
            FUp = F2 + m_epsilon;
        }
        if (m_bUp > FUp) {
            m_bUp = FUp;
            m_iUp = i2;
        }
    }

    /** 
   * parameters correspond to pseudocode from paper.
   * 
   * @param i2 index of  candidate
   * @return
   * @throws Exception
   */
    protected int examineExample(int i2) throws Exception {
        double alpha2 = m_alpha[i2];
        double alpha2Star = m_alphaStar[i2];
        int iSet = m_iSet[i2];
        double F2 = m_error[i2];
        if (!m_I0.contains(i2)) {
            F2 = -SVMOutput(i2) - m_b + m_target[i2];
            m_error[i2] = F2;
            if (iSet == I1) {
                if (F2 + m_epsilon < m_bUp) {
                    m_bUp = F2 + m_epsilon;
                    m_iUp = i2;
                } else if (F2 - m_epsilon > m_bLow) {
                    m_bLow = F2 - m_epsilon;
                    m_iLow = i2;
                }
            } else if ((iSet == I2) && (F2 + m_epsilon > m_bLow)) {
                m_bLow = F2 + m_epsilon;
                m_iLow = i2;
            } else if ((iSet == I3) && (F2 - m_epsilon < m_bUp)) {
                m_bUp = F2 - m_epsilon;
                m_iUp = i2;
            }
        }
        int i1 = i2;
        boolean bOptimality = true;
        if (iSet == I0a) {
            if (m_bLow - (F2 - m_epsilon) > 2 * m_fTolerance) {
                bOptimality = false;
                i1 = m_iLow;
                if ((F2 - m_epsilon) - m_bUp > m_bLow - (F2 - m_epsilon)) {
                    i1 = m_iUp;
                }
            } else if ((F2 - m_epsilon) - m_bUp > 2 * m_fTolerance) {
                bOptimality = false;
                i1 = m_iUp;
                if (m_bLow - (F2 - m_epsilon) > (F2 - m_epsilon) - m_bUp) {
                    i1 = m_iLow;
                }
            }
        } else if (iSet == I0b) {
            if (m_bLow - (F2 + m_epsilon) > 2 * m_fTolerance) {
                bOptimality = false;
                i1 = m_iLow;
                if ((F2 + m_epsilon) - m_bUp > m_bLow - (F2 + m_epsilon)) {
                    i1 = m_iUp;
                }
            } else if ((F2 + m_epsilon) - m_bUp > 2 * m_fTolerance) {
                bOptimality = false;
                i1 = m_iUp;
                if (m_bLow - (F2 + m_epsilon) > (F2 + m_epsilon) - m_bUp) {
                    i1 = m_iLow;
                }
            }
        } else if (iSet == I1) {
            if (m_bLow - (F2 + m_epsilon) > 2 * m_fTolerance) {
                bOptimality = false;
                i1 = m_iLow;
                if ((F2 + m_epsilon) - m_bUp > m_bLow - (F2 + m_epsilon)) {
                    i1 = m_iUp;
                }
            } else if ((F2 - m_epsilon) - m_bUp > 2 * m_fTolerance) {
                bOptimality = false;
                i1 = m_iUp;
                if (m_bLow - (F2 - m_epsilon) > (F2 - m_epsilon) - m_bUp) {
                    i1 = m_iLow;
                }
            }
        } else if (iSet == I2) {
            if ((F2 + m_epsilon) - m_bUp > 2 * m_fTolerance) {
                bOptimality = false;
                i1 = m_iUp;
            }
        } else if (iSet == I3) {
            if (m_bLow - (F2 - m_epsilon) > 2 * m_fTolerance) {
                bOptimality = false;
                i1 = m_iLow;
            }
        }
        if (bOptimality) {
            return 0;
        }
        return takeStep(i1, i2, m_alpha[i2], m_alphaStar[i2], F2);
    }

    /** 
   * initialize various variables before starting the actual optimizer 
   * 
   * @param data 	data set used for learning
   * @throws Exception	if something goes wrong
   */
    protected void init(Instances data) throws Exception {
        super.init(data);
        m_I0 = new SMOset(m_data.numInstances());
        m_iSet = new int[m_data.numInstances()];
        for (int i = 0; i < m_nInstances; i++) {
            m_iSet[i] = I1;
        }
        m_iUp = 0;
        m_bUp = m_target[m_iUp] + m_epsilon;
        m_iLow = m_iUp;
        m_bLow = m_target[m_iLow] - m_epsilon;
        m_error = new double[m_nInstances];
        for (int i = 0; i < m_nInstances; i++) {
            m_error[i] = m_target[i];
        }
    }

    /** 
   * use variant 1 of Shevade's et al.s paper
   * 
   * @throws Exception	if something goes wrong
   */
    protected void optimize1() throws Exception {
        int nNumChanged = 0;
        boolean bExamineAll = true;
        while (nNumChanged > 0 || bExamineAll) {
            nNumChanged = 0;
            if (bExamineAll) {
                for (int i = 0; i < m_nInstances; i++) {
                    nNumChanged += examineExample(i);
                }
            } else {
                for (int i = m_I0.getNext(-1); i != -1; i = m_I0.getNext(i)) {
                    nNumChanged += examineExample(i);
                    if (m_bLow - m_bUp < 2 * m_fTolerance) {
                        nNumChanged = 0;
                        break;
                    }
                }
            }
            if (bExamineAll) {
                bExamineAll = false;
            } else if (nNumChanged == 0) {
                bExamineAll = true;
            }
        }
    }

    /** 
   * use variant 2 of Shevade's et al.s paper 
   * 
   * @throws Exception	if something goes wrong
   */
    protected void optimize2() throws Exception {
        int nNumChanged = 0;
        boolean bExamineAll = true;
        while (nNumChanged > 0 || bExamineAll) {
            nNumChanged = 0;
            if (bExamineAll) {
                for (int i = 0; i < m_nInstances; i++) {
                    nNumChanged += examineExample(i);
                }
            } else {
                boolean bInnerLoopSuccess = true;
                do {
                    if (takeStep(m_iUp, m_iLow, m_alpha[m_iLow], m_alphaStar[m_iLow], m_error[m_iLow]) > 0) {
                        bInnerLoopSuccess = true;
                        nNumChanged += 1;
                    } else {
                        bInnerLoopSuccess = false;
                    }
                } while ((m_bUp <= m_bLow - 2 * m_fTolerance) && bInnerLoopSuccess);
                nNumChanged = 0;
            }
            if (bExamineAll) {
                bExamineAll = false;
            } else if (nNumChanged == 0) {
                bExamineAll = true;
            }
        }
    }

    /** 
   * wrap up various variables to save memeory and do some housekeeping after optimization
   * has finished.
   *
   * @throws Exception 	if something goes wrong
   */
    protected void wrapUp() throws Exception {
        m_b = -(m_bLow + m_bUp) / 2.0;
        m_target = null;
        m_error = null;
        super.wrapUp();
    }

    /** 
   * learn SVM parameters from data using Keerthi's SMO algorithm.
   * Subclasses should implement something more interesting.
   * 
   * @param instances	the data to work with
   * @throws Exception	if something goes wrong
   */
    public void buildClassifier(Instances instances) throws Exception {
        init(instances);
        if (m_bUseVariant1) {
            optimize1();
        } else {
            optimize2();
        }
        wrapUp();
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
