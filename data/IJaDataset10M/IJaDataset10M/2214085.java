package de.uniwue.tm.textmarker.kernel.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

/** 
 * Updated by JCasGen Tue Jan 25 09:29:36 CET 2011
 * XML source: D:/work/workspace-tm/de.uniwue.tm.textmarker.engine/desc/InternalTypeSystem.xml
 * @generated */
public class TruePositive extends EvalAnnotation {

    /** @generated
   * @ordered 
   */
    public static final int typeIndexID = JCasRegistry.register(TruePositive.class);

    /** @generated
   * @ordered 
   */
    public static final int type = typeIndexID;

    /** @generated  */
    public int getTypeIndexID() {
        return typeIndexID;
    }

    /** Never called.  Disable default constructor
   * @generated */
    protected TruePositive() {
    }

    /** Internal - constructor used by generator 
   * @generated */
    public TruePositive(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public TruePositive(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** @generated */
    public TruePositive(JCas jcas, int begin, int end) {
        super(jcas);
        setBegin(begin);
        setEnd(end);
        readObject();
    }

    /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
    private void readObject() {
    }
}
