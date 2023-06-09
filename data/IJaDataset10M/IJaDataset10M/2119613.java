package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 * An attempt to add an element to the named node map returned by notations should 
 * result in a NO_MODIFICATION_ERR or HIERARCHY_REQUEST_ERR.
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-D46829EF">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-D46829EF</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-1025163788">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-1025163788</a>
*/
public final class hc_notationssetnameditem1 extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public hc_notationssetnameditem1(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        super(factory);
        if (factory.hasFeature("XML", null) != true) {
            throw org.w3c.domts.DOMTestIncompatibleException.incompatibleFeature("XML", null);
        }
        String contentType = getContentType();
        preload(contentType, "hc_staff", true);
    }

    /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
    public void runTest() throws Throwable {
        Document doc;
        NamedNodeMap notations;
        DocumentType docType;
        Node retval;
        Element elem;
        doc = (Document) load("hc_staff", true);
        docType = doc.getDoctype();
        if (!(("text/html".equals(getContentType())))) {
            assertNotNull("docTypeNotNull", docType);
            notations = docType.getNotations();
            assertNotNull("notationsNotNull", notations);
            elem = doc.createElement("br");
            try {
                retval = notations.setNamedItem(elem);
                fail("throw_HIER_OR_NO_MOD_ERR");
            } catch (DOMException ex) {
                switch(ex.code) {
                    case 3:
                        break;
                    case 7:
                        break;
                    default:
                        throw ex;
                }
            }
        }
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/hc_notationssetnameditem1";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(hc_notationssetnameditem1.class, args);
    }
}
