package org.xmlpull.v1.tests;

import java.io.*;
import junit.framework.TestSuite;
import org.xmlpull.v1.XmlPullParserException;

public class TestXmlTypical extends XmlTestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(TestXmlTypical.class));
    }

    public TestXmlTypical(String name) {
        super(name);
    }

    public void testTypical() throws IOException, XmlPullParserException {
        testXml("typical.xml");
    }
}
