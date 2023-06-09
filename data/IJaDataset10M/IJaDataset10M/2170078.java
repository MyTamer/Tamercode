package org.w3c.domts.level2.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     The "hasAttributeNS()" method for an Element should 
 *    return false if the element does not have an attribute with the given local name 
 *    and/or namespace URI specified on this element or does not have a default value.
 *    Retrieve the first "emp:address" element.
 *    The boolean value returned by the "hasAttributeNS()" should be false 
 *    since the attribute does not have a default value.
* @author NIST
* @author Mary Brady
* @see <a href="http://www.w3.org/TR/DOM-Level-2-Core/core#ID-ElHasAttrNS">http://www.w3.org/TR/DOM-Level-2-Core/core#ID-ElHasAttrNS</a>
*/
public final class hasAttributeNS03 extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public hasAttributeNS03(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        super(factory);
        String contentType = getContentType();
        preload(contentType, "staffNS", false);
    }

    /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
    public void runTest() throws Throwable {
        String localName = "blank";
        String namespaceURI = "http://www.nist.gov";
        Document doc;
        NodeList elementList;
        Element testNode;
        boolean state;
        doc = (Document) load("staffNS", false);
        elementList = doc.getElementsByTagName("emp:address");
        testNode = (Element) elementList.item(0);
        assertNotNull("empAddrNotNull", testNode);
        state = testNode.hasAttributeNS(namespaceURI, localName);
        assertFalse("throw_False", state);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level2/core/hasAttributeNS03";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(hasAttributeNS03.class, args);
    }
}
