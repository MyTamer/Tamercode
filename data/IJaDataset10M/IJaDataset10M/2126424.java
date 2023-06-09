package org.w3c.domts.level2.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     The "importNode(importedNode,deep)" method for a 
 *    Document should import the given importedNode into that Document.
 *    The importedNode is of type Element.
 *    If this document defines default attributes for this element name (importedNode),
 *    those default attributes are assigned.
 *    
 *    Create an element whose name is "emp:employee" in a different document.
 *    Invoke method importNode(importedNode,deep) on this document which
 *    defines default attribute for the element name "emp:employee".
 *    Method should return an the imported element with an assigned default attribute.
* @author NIST
* @author Mary Brady
* @see <a href="http://www.w3.org/TR/DOM-Level-2-Core/core#Core-Document-importNode">http://www.w3.org/TR/DOM-Level-2-Core/core#Core-Document-importNode</a>
* @see <a href="http://www.w3.org/Bugs/Public/show_bug.cgi?id=238">http://www.w3.org/Bugs/Public/show_bug.cgi?id=238</a>
*/
public final class importNode07 extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public importNode07(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        org.w3c.domts.DocumentBuilderSetting[] settings = new org.w3c.domts.DocumentBuilderSetting[] { org.w3c.domts.DocumentBuilderSetting.namespaceAware, org.w3c.domts.DocumentBuilderSetting.validating };
        DOMTestDocumentBuilderFactory testFactory = factory.newInstance(settings);
        setFactory(testFactory);
        String contentType = getContentType();
        preload(contentType, "staffNS", true);
        preload(contentType, "staff", true);
    }

    /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
    public void runTest() throws Throwable {
        Document doc;
        Document aNewDoc;
        Element element;
        Node aNode;
        NamedNodeMap attributes;
        String name;
        Node attr;
        String lname;
        String namespaceURI = "http://www.nist.gov";
        String qualifiedName = "emp:employee";
        doc = (Document) load("staffNS", true);
        aNewDoc = (Document) load("staff", true);
        element = aNewDoc.createElementNS(namespaceURI, qualifiedName);
        aNode = doc.importNode(element, false);
        attributes = aNode.getAttributes();
        assertSize("throw_Size", 1, attributes);
        name = aNode.getNodeName();
        assertEquals("nodeName", "emp:employee", name);
        attr = attributes.item(0);
        lname = attr.getLocalName();
        assertEquals("lname", "defaultAttr", lname);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level2/core/importNode07";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(importNode07.class, args);
    }
}
