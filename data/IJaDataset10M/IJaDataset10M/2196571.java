package au.edu.qut.yawl.elements;

import au.edu.qut.yawl.elements.state.TestYMarking;
import au.edu.qut.yawl.elements.state.YIdentifier;
import au.edu.qut.yawl.engine.TestEngineAgainstImproperCompletionOfASubnet;
import au.edu.qut.yawl.unmarshal.YMarshal;
import au.edu.qut.yawl.util.YMessagePrinter;
import au.edu.qut.yawl.util.YVerificationMessage;
import au.edu.qut.yawl.exceptions.YSyntaxException;
import au.edu.qut.yawl.exceptions.YSchemaBuildingException;
import au.edu.qut.yawl.exceptions.YPersistenceException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jdom.JDOMException;

/**
 * @author aldredl
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TestYNet extends TestCase {

    private YNet _goodNet;

    private YNet _badNet;

    private YNet _copy;

    private YNet _loopedNet;

    private YIdentifier _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8;

    private YSpecification _badSpecification;

    private YSpecification _weirdSpecification;

    /**
     * Constructor for NetElementTest.
     * @param name
     */
    public TestYNet(String name) {
        super(name);
    }

    public void setUp() throws YSchemaBuildingException, YSyntaxException, JDOMException, IOException, YPersistenceException {
        File file1 = new File(getClass().getResource("GoodNetSpecification.xml").getFile());
        File file2 = new File(getClass().getResource("BadNetSpecification.xml").getFile());
        YSpecification specification1 = null;
        specification1 = (YSpecification) YMarshal.unmarshalSpecifications(file1.getAbsolutePath()).get(0);
        _badSpecification = (YSpecification) YMarshal.unmarshalSpecifications(file2.getAbsolutePath()).get(0);
        _goodNet = specification1.getRootNet();
        _badNet = _badSpecification.getRootNet();
        _copy = null;
        _copy = (YNet) this._goodNet.clone();
        URL fileURL = TestYMarking.class.getResource("YAWLOrJoinTestSpecificationLongLoops.xml");
        File yawlXMLFile = new File(fileURL.getFile());
        YSpecification specification = null;
        specification = (YSpecification) YMarshal.unmarshalSpecifications(yawlXMLFile.getAbsolutePath()).get(0);
        _loopedNet = specification.getRootNet();
        _id1 = new YIdentifier();
        _id1.addLocation(null, (YCondition) _loopedNet.getNetElement("c{d_f}"));
        _id1.addLocation(null, (YCondition) _loopedNet.getNetElement("c{d_f}"));
        _id1.addLocation(null, (YCondition) _loopedNet.getNetElement("cC"));
        _id2 = new YIdentifier();
        _id2.addLocation(null, (YCondition) _loopedNet.getNetElement("c{d_f}"));
        _id2.addLocation(null, (YCondition) _loopedNet.getNetElement("cA"));
        _id3 = new YIdentifier();
        _id3.addLocation(null, (YCondition) _loopedNet.getNetElement("cC"));
        _id4 = new YIdentifier();
        _id4.addLocation(null, (YCondition) _loopedNet.getNetElement("i-top"));
        _id4.addLocation(null, (YCondition) _loopedNet.getNetElement("c{d_f}"));
        _id5 = new YIdentifier();
        _id5.addLocation(null, (YCondition) _loopedNet.getNetElement("c{d_f}"));
        _id5.addLocation(null, (YCondition) _loopedNet.getNetElement("c{b_w}"));
        _id6 = new YIdentifier();
        _id6.addLocation(null, (YTask) _loopedNet.getNetElement("d"));
        _id6.addLocation(null, (YCondition) _loopedNet.getNetElement("c{d_f}"));
        _id7 = new YIdentifier();
        _id7.addLocation(null, (YCondition) _loopedNet.getNetElement("cA"));
        _id7.addLocation(null, (YCondition) _loopedNet.getNetElement("cB"));
        _id7.addLocation(null, (YCondition) _loopedNet.getNetElement("c{q_f}"));
        _id8 = new YIdentifier();
        _id8.addLocation(null, (YCondition) _loopedNet.getNetElement("cA"));
        _id8.addLocation(null, (YCondition) _loopedNet.getNetElement("cB"));
        _id8.addLocation(null, (YCondition) _loopedNet.getNetElement("cC"));
        _id8.addLocation(null, (YCondition) _loopedNet.getNetElement("c{q_f}"));
        File file3 = new File(TestEngineAgainstImproperCompletionOfASubnet.class.getResource("ImproperCompletion.xml").getFile());
        try {
            _weirdSpecification = (YSpecification) YMarshal.unmarshalSpecifications(file3.getAbsolutePath()).get(0);
        } catch (YSyntaxException e) {
            e.printStackTrace();
        }
    }

    public void testGoodNetVerify() {
        List messages = _goodNet.verify();
        if (messages.size() > 3) {
            YMessagePrinter.printMessages(messages);
            fail(((YVerificationMessage) messages.get(0)).getMessage() + " num msg = " + messages.size());
        }
    }

    public void testBadNetVerify() {
        List messages = _badSpecification.verify();
        if (messages.size() != 5) {
            YMessagePrinter.printMessages(messages);
            ;
            fail("BadNet should have produced 5 error messages, but didn't. msgs == " + YMessagePrinter.getMessageString(messages));
        }
    }

    public void testCloneBasics() {
        YInputCondition inputGoodNet = this._goodNet.getInputCondition();
        YInputCondition inputCopy = _copy.getInputCondition();
        assertNotSame(this._goodNet.getNetElements(), _copy.getNetElements());
        Map originalNetElements = _goodNet.getNetElements();
        Map copiedNetElements = _copy.getNetElements();
        Iterator iter = originalNetElements.keySet().iterator();
        while (iter.hasNext()) {
            String nextElementID = (String) iter.next();
            assertNotNull(originalNetElements.get(nextElementID));
            assertNotNull(copiedNetElements.get(nextElementID));
            assertNotSame(originalNetElements.get(nextElementID), copiedNetElements.get(nextElementID));
        }
        assertNotSame(inputGoodNet, inputCopy);
    }

    public void testCloneVerify() {
        List messages = _copy.verify();
        if (messages.size() > 3) {
            fail(YMessagePrinter.getMessageString(messages));
        }
    }

    public void testORJoinEnabled() {
        assertFalse(_loopedNet.orJoinEnabled((YTask) _loopedNet.getNetElement("f"), _id1));
        assertTrue(_loopedNet.orJoinEnabled((YTask) _loopedNet.getNetElement("f"), _id2));
        assertFalse(_loopedNet.orJoinEnabled((YTask) _loopedNet.getNetElement("f"), _id3));
        assertFalse(_loopedNet.orJoinEnabled((YTask) _loopedNet.getNetElement("f"), _id4));
        assertTrue(_loopedNet.orJoinEnabled((YTask) _loopedNet.getNetElement("f"), _id5));
        assertTrue(_loopedNet.orJoinEnabled((YTask) _loopedNet.getNetElement("f"), _id6));
        assertFalse(_loopedNet.orJoinEnabled((YTask) _loopedNet.getNetElement("f"), _id7));
        assertTrue(_id7.getLocations().contains(_loopedNet.getNetElement("cA")));
        assertTrue(_id7.getLocations().contains(_loopedNet.getNetElement("cB")));
        assertTrue(_id7.getLocations().contains(_loopedNet.getNetElement("c{q_f}")));
        assertFalse(_loopedNet.orJoinEnabled((YTask) _loopedNet.getNetElement("f"), _id8));
    }

    public void testCloneWithNewDataModel() throws YSchemaBuildingException, YSyntaxException, JDOMException, IOException {
        File specificationFile = new File(YMarshal.class.getResource("MakeRecordings.xml").getFile());
        List specifications = null;
        specifications = YMarshal.unmarshalSpecifications(specificationFile.getAbsolutePath());
        YSpecification originalSpec = (YSpecification) specifications.iterator().next();
        YNet originalNet = originalSpec.getRootNet();
        YNet clonedNet = null;
        clonedNet = (YNet) originalNet.clone();
        List messages = originalNet.verify();
        if (messages.size() > 0) {
            fail(YMessagePrinter.getMessageString(messages));
        }
        assertTrue(originalNet.verify().size() == 0);
        if (clonedNet.verify().size() != 0) {
            fail(YMessagePrinter.getMessageString(clonedNet.verify()));
        }
        assertEquals(originalNet.toXML(), clonedNet.toXML());
    }

    public void testDataStructureAgainstWierdSpecification() {
        YNet weirdRootClone = (YNet) _weirdSpecification.getRootNet().clone();
        try {
            YNet weirdleafDClone = (YNet) ((YTask) weirdRootClone.getNetElement("d-top")).getDecompositionPrototype().clone();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public static void main(String args[]) {
        TestRunner runner = new TestRunner();
        runner.doRun(suite());
        System.exit(0);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(TestYNet.class);
        return suite;
    }
}
