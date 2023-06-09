package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     The "getNodeType()" method for a CDATASection Node
 *     returns the constant value 4.
 *     
 *     Retrieve the CDATASection node contained inside the
 *     second child of the second employee and invoke the 
 *     "getNodeType()" method.   The method should return 4. 
* @author NIST
* @author Mary Brady
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-111237558">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-111237558</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-667469212">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-667469212</a>
*/
public final class nodecdatasectionnodetype extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public nodecdatasectionnodetype(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        org.w3c.domts.DocumentBuilderSetting[] settings = new org.w3c.domts.DocumentBuilderSetting[] { org.w3c.domts.DocumentBuilderSetting.notCoalescing };
        DOMTestDocumentBuilderFactory testFactory = factory.newInstance(settings);
        setFactory(testFactory);
        String contentType = getContentType();
        preload(contentType, "staff", false);
    }

    /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
    public void runTest() throws Throwable {
        Document doc;
        NodeList elementList;
        Element testName;
        Node cdataNode;
        int nodeType;
        doc = (Document) load("staff", false);
        elementList = doc.getElementsByTagName("name");
        testName = (Element) elementList.item(1);
        cdataNode = testName.getLastChild();
        nodeType = (int) cdataNode.getNodeType();
        if (equals(3, nodeType)) {
            cdataNode = doc.createCDATASection("");
            nodeType = (int) cdataNode.getNodeType();
        }
        assertEquals("nodeTypeCDATA", 4, nodeType);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/nodecdatasectionnodetype";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(nodecdatasectionnodetype.class, args);
    }
}
