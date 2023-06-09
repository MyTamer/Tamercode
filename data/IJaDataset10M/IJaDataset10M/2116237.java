package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     The "insertBefore(newChild,refChild)" method raises a 
 *     WRONG_DOCUMENT_ERR DOMException if the "newChild" was
 *     created from a different document than the one that 
 *     created this node.
 *     
 *     Retrieve the second employee and attempt to insert a new 
 *     child that was created from a different document than the
 *     one that created the second employee.   An attempt to
 *     insert such a child should raise the desired exception.
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#xpointer(id('ID-258A00AF')/constant[@name='WRONG_DOCUMENT_ERR'])">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#xpointer(id('ID-258A00AF')/constant[@name='WRONG_DOCUMENT_ERR'])</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-952280727">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-952280727</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#xpointer(id('ID-952280727')/raises/exception[@name='DOMException']/descr/p[substring-before(.,':')='WRONG_DOCUMENT_ERR'])">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#xpointer(id('ID-952280727')/raises/exception[@name='DOMException']/descr/p[substring-before(.,':')='WRONG_DOCUMENT_ERR'])</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-952280727">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-952280727</a>
* @see <a href="http://www.w3.org/Bugs/Public/show_bug.cgi?id=247">http://www.w3.org/Bugs/Public/show_bug.cgi?id=247</a>
*/
public final class hc_nodeinsertbeforenewchilddiffdocument extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public hc_nodeinsertbeforenewchilddiffdocument(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        super(factory);
        String contentType = getContentType();
        preload(contentType, "hc_staff", false);
        preload(contentType, "hc_staff", true);
    }

    /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
    public void runTest() throws Throwable {
        Document doc1;
        Document doc2;
        Node refChild;
        Node newChild;
        NodeList elementList;
        Node elementNode;
        Node insertedNode;
        doc1 = (Document) load("hc_staff", false);
        doc2 = (Document) load("hc_staff", true);
        newChild = doc1.createElement("br");
        elementList = doc2.getElementsByTagName("p");
        elementNode = elementList.item(1);
        refChild = elementNode.getFirstChild();
        {
            boolean success = false;
            try {
                insertedNode = elementNode.insertBefore(newChild, refChild);
            } catch (DOMException ex) {
                success = (ex.code == DOMException.WRONG_DOCUMENT_ERR);
            }
            assertTrue("throw_WRONG_DOCUMENT_ERR", success);
        }
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/hc_nodeinsertbeforenewchilddiffdocument";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(hc_nodeinsertbeforenewchilddiffdocument.class, args);
    }
}
