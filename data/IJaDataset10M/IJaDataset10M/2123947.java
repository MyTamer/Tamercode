package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     If the sum of the offset and count exceeds the length then 
 *    all the characters to the end of the data are replaced.
 *    
 *    Retrieve the character data from the last child of the
 *    first employee.  The "replaceData(offset,count,arg)"
 *    method is then called with offset=0 and count=50 and
 *    arg="2600".  The method should replace all the characters
 *    with "2600". This is because the sum of the offset and
 *    count exceeds the length of the character data.
* @author NIST
* @author Mary Brady
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-72AB8359">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-72AB8359</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-E5CBA7FB">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-E5CBA7FB</a>
*/
public final class characterdatareplacedataexceedslengthofdata extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public characterdatareplacedataexceedslengthofdata(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        super(factory);
        String contentType = getContentType();
        preload(contentType, "staff", true);
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
        String childData;
        doc = (Document) load("staff", true);
        elementList = doc.getElementsByTagName("address");
        nameNode = elementList.item(0);
        child = (CharacterData) nameNode.getFirstChild();
        child.replaceData(0, 50, "2600");
        childData = child.getData();
        assertEquals("characterdataReplaceDataExceedsLengthOfDataAssert", "2600", childData);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/characterdatareplacedataexceedslengthofdata";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(characterdatareplacedataexceedslengthofdata.class, args);
    }
}
