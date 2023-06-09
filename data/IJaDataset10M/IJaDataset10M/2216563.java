package reconcile.weka.experiment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import reconcile.weka.core.Attribute;
import reconcile.weka.core.FastVector;
import reconcile.weka.core.Instance;
import reconcile.weka.core.Instances;

/**
 * InstancesResultListener outputs the received results in arff format to
 * a Writer. All results must be received before the instances can be
 * written out.
 *
 * @author Len Trigg (trigg@cs.waikato.ac.nz)
 * @version $Revision: 1.1 $
 */
public class InstancesResultListener extends CSVResultListener {

    /** Stores the instances created so far, before assigning to a header */
    protected transient FastVector m_Instances;

    /** Stores the attribute types for each column */
    protected transient int[] m_AttributeTypes;

    /** For lookup of indices given a string value for each nominal attribute */
    protected transient Hashtable[] m_NominalIndexes;

    /** Contains strings seen so far for each nominal attribute */
    protected transient FastVector[] m_NominalStrings;

    /** 
   * Sets temporary file.
   */
    public InstancesResultListener() {
        File resultsFile;
        try {
            resultsFile = File.createTempFile("weka_experiment", ".arff");
            resultsFile.deleteOnExit();
        } catch (Exception e) {
            System.err.println("Cannot create temp file, writing to standard out.");
            resultsFile = new File("-");
        }
        setOutputFile(resultsFile);
        setOutputFileName("");
    }

    /**
   * Returns a string describing this result listener
   * @return a description of the result listener suitable for
   * displaying in the explorer/experimenter gui
   */
    public String globalInfo() {
        return "Takes results from a result producer and assembles them into " + "a set of instances.";
    }

    /**
   * Prepare for the results to be received.
   *
   * @param rp the ResultProducer that will generate the results
   * @exception Exception if an error occurs during preprocessing.
   */
    public void preProcess(ResultProducer rp) throws Exception {
        m_RP = rp;
        if ((m_OutputFile == null) || (m_OutputFile.getName().equals("-"))) {
            m_Out = new PrintWriter(System.out, true);
        } else {
            m_Out = new PrintWriter(new BufferedOutputStream(new FileOutputStream(m_OutputFile)), true);
        }
        Object[] keyTypes = m_RP.getKeyTypes();
        Object[] resultTypes = m_RP.getResultTypes();
        m_AttributeTypes = new int[keyTypes.length + resultTypes.length];
        m_NominalIndexes = new Hashtable[m_AttributeTypes.length];
        m_NominalStrings = new FastVector[m_AttributeTypes.length];
        m_Instances = new FastVector();
        for (int i = 0; i < m_AttributeTypes.length; i++) {
            Object attribute = null;
            if (i < keyTypes.length) {
                attribute = keyTypes[i];
            } else {
                attribute = resultTypes[i - keyTypes.length];
            }
            if (attribute instanceof String) {
                m_AttributeTypes[i] = Attribute.NOMINAL;
                m_NominalIndexes[i] = new Hashtable();
                m_NominalStrings[i] = new FastVector();
            } else if (attribute instanceof Double) {
                m_AttributeTypes[i] = Attribute.NUMERIC;
            } else {
                throw new Exception("Unknown attribute type in column " + (i + 1));
            }
        }
    }

    /**
   * Perform any postprocessing. When this method is called, it indicates
   * that no more results will be sent that need to be grouped together
   * in any way.
   *
   * @param rp the ResultProducer that generated the results
   * @exception Exception if an error occurs
   */
    public void postProcess(ResultProducer rp) throws Exception {
        if (m_RP != rp) {
            throw new Error("Unrecognized ResultProducer sending results!!");
        }
        String[] keyNames = m_RP.getKeyNames();
        String[] resultNames = m_RP.getResultNames();
        FastVector attribInfo = new FastVector();
        for (int i = 0; i < m_AttributeTypes.length; i++) {
            String attribName = "Unknown";
            if (i < keyNames.length) {
                attribName = "Key_" + keyNames[i];
            } else {
                attribName = resultNames[i - keyNames.length];
            }
            switch(m_AttributeTypes[i]) {
                case Attribute.NOMINAL:
                    if (m_NominalStrings[i].size() > 0) {
                        attribInfo.addElement(new Attribute(attribName, m_NominalStrings[i]));
                    } else {
                        attribInfo.addElement(new Attribute(attribName, (FastVector) null));
                    }
                    break;
                case Attribute.NUMERIC:
                    attribInfo.addElement(new Attribute(attribName));
                    break;
                case Attribute.STRING:
                    attribInfo.addElement(new Attribute(attribName, (FastVector) null));
                    break;
                default:
                    throw new Exception("Unknown attribute type");
            }
        }
        Instances result = new Instances("InstanceResultListener", attribInfo, m_Instances.size());
        for (int i = 0; i < m_Instances.size(); i++) {
            result.add((Instance) m_Instances.elementAt(i));
        }
        m_Out.println(new Instances(result, 0));
        for (int i = 0; i < result.numInstances(); i++) {
            m_Out.println(result.instance(i));
        }
        if (!(m_OutputFile == null) && !(m_OutputFile.getName().equals("-"))) {
            m_Out.close();
        }
    }

    /**
   * Collects each instance and adjusts the header information.
   *
   * @param rp the ResultProducer that generated the result
   * @param key The key for the results.
   * @param result The actual results.
   * @exception Exception if the result could not be accepted.
   */
    public void acceptResult(ResultProducer rp, Object[] key, Object[] result) throws Exception {
        if (m_RP != rp) {
            throw new Error("Unrecognized ResultProducer sending results!!");
        }
        Instance newInst = new Instance(m_AttributeTypes.length);
        for (int i = 0; i < m_AttributeTypes.length; i++) {
            Object val = null;
            if (i < key.length) {
                val = key[i];
            } else {
                val = result[i - key.length];
            }
            if (val == null) {
                newInst.setValue(i, Instance.missingValue());
            } else {
                switch(m_AttributeTypes[i]) {
                    case Attribute.NOMINAL:
                        String str = (String) val;
                        Double index = (Double) m_NominalIndexes[i].get(str);
                        if (index == null) {
                            index = new Double(m_NominalStrings[i].size());
                            m_NominalIndexes[i].put(str, index);
                            m_NominalStrings[i].addElement(str);
                        }
                        newInst.setValue(i, index.floatValue());
                        break;
                    case Attribute.NUMERIC:
                        float dou = ((Double) val).floatValue();
                        newInst.setValue(i, (float) dou);
                        break;
                    default:
                        newInst.setValue(i, Instance.missingValue());
                }
            }
        }
        m_Instances.addElement(newInst);
    }
}
