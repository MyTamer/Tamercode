package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     Retrieve the textual data from the last child of the 
 *     second employee.   That node is composed of two   
 *     EntityReference nodes and two Text nodes.   After
 *     the content node is parsed, the "acronym" Element
 *     should contain four children with each one of the
 *     EntityReferences containing one child.
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-1451460987">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-1451460987</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-11C98490">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-11C98490</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-745549614">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-745549614</a>
*/
public final class hc_textparseintolistofelements extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public hc_textparseintolistofelements(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        super(factory);
        String contentType = getContentType();
        preload(contentType, "hc_staff", false);
    }

    /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
    public void runTest() throws Throwable {
        Document doc;
        NodeList elementList;
        Node addressNode;
        NodeList childList;
        Node child;
        String value;
        Node grandChild;
        int length;
        java.util.List result = new java.util.ArrayList();
        java.util.List expectedNormal = new java.util.ArrayList();
        expectedNormal.add("β");
        expectedNormal.add(" Dallas, ");
        expectedNormal.add("γ");
        expectedNormal.add("\n 98554");
        java.util.List expectedExpanded = new java.util.ArrayList();
        expectedExpanded.add("β Dallas, γ\n 98554");
        doc = (Document) load("hc_staff", false);
        elementList = doc.getElementsByTagName("acronym");
        addressNode = elementList.item(1);
        childList = addressNode.getChildNodes();
        length = (int) childList.getLength();
        for (int indexN1007C = 0; indexN1007C < childList.getLength(); indexN1007C++) {
            child = (Node) childList.item(indexN1007C);
            value = child.getNodeValue();
            if ((value == null)) {
                grandChild = child.getFirstChild();
                assertNotNull("grandChildNotNull", grandChild);
                value = grandChild.getNodeValue();
                result.add(value);
            } else {
                result.add(value);
            }
        }
        if (equals(1, length)) {
            assertEquals("assertEqCoalescing", expectedExpanded, result);
        } else {
            assertEquals("assertEqNormal", expectedNormal, result);
        }
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/hc_textparseintolistofelements";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(hc_textparseintolistofelements.class, args);
    }
}
