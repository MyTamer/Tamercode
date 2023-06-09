package org.w3c.domts.level2.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *       The method setAttributeNS adds a new attribute.
 *       
 *       Retrieve an existing element node with attributes and add a new attribute node to it using 
 *       the setAttributeNS method.   Check if the attribute was correctly set by invoking the 
 *       getAttributeNodeNS method and checking the nodeName and nodeValue of the returned nodes.
 *     
* @author IBM
* @author Neil Delima
* @see <a href="http://www.w3.org/TR/DOM-Level-2-Core/core#ID-ElSetAttrNS">http://www.w3.org/TR/DOM-Level-2-Core/core#ID-ElSetAttrNS</a>
*/
public final class elementsetattributens02 extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public elementsetattributens02(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        org.w3c.domts.DocumentBuilderSetting[] settings = new org.w3c.domts.DocumentBuilderSetting[] { org.w3c.domts.DocumentBuilderSetting.namespaceAware };
        DOMTestDocumentBuilderFactory testFactory = factory.newInstance(settings);
        setFactory(testFactory);
        String contentType = getContentType();
        preload(contentType, "staff", true);
    }

    /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
    public void runTest() throws Throwable {
        Document doc;
        Element element;
        Attr attribute;
        NodeList elementList;
        String attrName;
        String attrValue;
        doc = (Document) load("staff", true);
        elementList = doc.getElementsByTagNameNS("*", "address");
        element = (Element) elementList.item(0);
        element.setAttributeNS("http://www.w3.org/DOM/Test/setAttributeNS", "this:street", "Silver Street");
        attribute = element.getAttributeNodeNS("http://www.w3.org/DOM/Test/setAttributeNS", "street");
        attrName = attribute.getNodeName();
        attrValue = attribute.getNodeValue();
        assertEquals("elementsetattributens02_attrName", "this:street", attrName);
        assertEquals("elementsetattributens02_attrValue", "Silver Street", attrValue);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level2/core/elementsetattributens02";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(elementsetattributens02.class, args);
    }
}
