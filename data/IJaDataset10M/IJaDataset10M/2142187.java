package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     If the sum of the "offset" and "count" exceeds the
 *    "length" then the "substringData(offset,count)" method
 *    returns all the characters to the end of the data. 
 *    
 *    Retrieve the character data from the second child 
 *    of the first employee and access part of the data 
 *    by using the substringData(offset,count) method
 *    with offset=9 and count=10.  The method should return 
 *    the substring "Martin" since offset+count > length
 *    (19 > 15).
* @author NIST
* @author Mary Brady
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-6531BCCF">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-6531BCCF</a>
*/
public final class characterdatasubstringexceedsvalue extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public characterdatasubstringexceedsvalue(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        super(factory);
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
        Node nameNode;
        CharacterData child;
        String substring;
        doc = (Document) load("staff", false);
        elementList = doc.getElementsByTagName("name");
        nameNode = elementList.item(0);
        child = (CharacterData) nameNode.getFirstChild();
        substring = child.substringData(9, 10);
        assertEquals("characterdataSubStringExceedsValueAssert", "Martin", substring);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/characterdatasubstringexceedsvalue";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(characterdatasubstringexceedsvalue.class, args);
    }
}
