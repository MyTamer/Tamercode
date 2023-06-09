package etch.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.After;
import org.junit.Before;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** */
public class TestInheritanceXml {

    Document document;

    /** @throws Exception */
    @Before
    public void setUp() throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        try {
            File f = new File("tests/target/generated-sources/main/etch/xml/etch/tests/Inheritance.xml");
            document = builder.parse(f);
        } catch (Exception e) {
            System.out.println("Unable to load Test.xml document: " + e);
            System.out.println("Current dir is " + new File(".").getAbsolutePath());
            assertTrue(false);
        }
    }

    /** @throws Exception */
    @After
    public void tearDown() throws Exception {
        document = null;
    }

    /** @throws Exception */
    @org.junit.Test
    public void testModule() throws Exception {
        NodeList list = document.getElementsByTagName("module");
        assertEquals(list.getLength(), 1);
        String moduleName = list.item(0).getAttributes().getNamedItem("name").getNodeValue();
        assertEquals(moduleName, "etch.tests");
        assertTrue(onlyAttrsOfName(list.item(0), new String[] { "name" }));
        assertTrue(onlyChildNodesOfName(list.item(0), new String[] { "description", "service" }));
        Node child = getDescriptionElement(list.item(0));
        assertNull(child);
    }

    /** @throws Exception */
    @org.junit.Test
    public void testService() throws Exception {
        NodeList list = document.getElementsByTagName("service");
        assertEquals(list.getLength(), 1);
        String serviceName = list.item(0).getAttributes().getNamedItem("name").getNodeValue();
        assertEquals(serviceName, "Inheritance");
        assertTrue(onlyAttrsOfName(list.item(0), new String[] { "name" }));
        assertTrue(onlyChildNodesOfName(list.item(0), new String[] { "description", "consts", "externs", "enums", "structs", "exceptions", "methods" }));
        Node child = getDescriptionElement(list.item(0));
        assertNull(child);
    }

    /** @throws Exception */
    @org.junit.Test
    public void testConsts() throws Exception {
        NodeList list = document.getElementsByTagName("const");
        assertEquals(list.getLength(), 0);
    }

    /** @throws Exception */
    @org.junit.Test
    public void testExterns() throws Exception {
        NodeList list = document.getElementsByTagName("extern");
        assertEquals(list.getLength(), 0);
    }

    /** @throws Exception */
    @org.junit.Test
    public void testEnums() throws Exception {
        NodeList list = document.getElementsByTagName("enum");
        assertEquals(list.getLength(), 0);
    }

    /** @throws Exception */
    @org.junit.Test
    public void testStructs() throws Exception {
        NodeList list = document.getElementsByTagName("struct");
        assertEquals(list.getLength(), 3);
        int nodeCount = 0;
        Node structNode = list.item(nodeCount++);
        checkStructProperties(structNode, "S1", "162907748", "etch.tests.Inheritance.S1", "null", "S1 descr.");
        checkStructValueProperties(structNode, "a", "352988316", "a", "int", "true", "false", null, "blah about a.");
        checkStructValueProperties(structNode, "b", "352988317", "b", "int", "true", "false", null, "blah about b.");
        structNode = list.item(nodeCount++);
        checkStructProperties(structNode, "S2", "162907749", "etch.tests.Inheritance.S2", "etch.tests.Inheritance.S1", "S2 descr.");
        checkStructValueProperties(structNode, "c", "352988318", "c", "int", "true", "false", null, "blah about c.");
        checkStructValueProperties(structNode, "d", "352988319", "d", "int", "true", "false", null, "blah about d.");
        structNode = list.item(nodeCount++);
        checkStructProperties(structNode, "S3", "162907750", "etch.tests.Inheritance.S3", "etch.tests.Inheritance.S2", "S3 descr.");
        checkStructValueProperties(structNode, "e", "352988320", "e", "int", "true", "false", null, "blah about e.");
        checkStructValueProperties(structNode, "f", "352988321", "f", "int", "true", "false", null, "blah about f.");
    }

    /** @throws Exception */
    @org.junit.Test
    public void textExceptions() throws Exception {
        NodeList exceptionsNodeList = document.getElementsByTagName("exceptions");
        assertEquals(exceptionsNodeList.getLength(), 1);
        Node exceptionsNode = exceptionsNodeList.item(0);
        ArrayList<Node> list = new ArrayList<Node>();
        NodeList exceptionsNodeChildren = exceptionsNode.getChildNodes();
        for (int i = 0; i < exceptionsNodeChildren.getLength(); i++) {
            if (exceptionsNodeChildren.item(i).getNodeName() == "exception") {
                list.add(exceptionsNodeChildren.item(i));
            }
        }
        assertEquals(list.size(), 3);
        int nodeCount = 0;
        Node exceptionNode = list.get(nodeCount++);
        checkExceptionProperties(exceptionNode, "E1", "false", "161989362", "etch.tests.Inheritance.E1", "null", "E1 descr.");
        checkExceptionValueProperties(exceptionNode, "a", "352988316", "a", "int", "false", null, "blah about a.");
        checkExceptionValueProperties(exceptionNode, "b", "352988317", "b", "int", "false", null, "blah about b.");
        exceptionNode = list.get(nodeCount++);
        checkExceptionProperties(exceptionNode, "E2", "false", "161989363", "etch.tests.Inheritance.E2", "etch.tests.Inheritance.E1", "E2 descr.");
        checkExceptionValueProperties(exceptionNode, "c", "352988318", "c", "int", "false", null, "blah about c.");
        checkExceptionValueProperties(exceptionNode, "d", "352988319", "d", "int", "false", null, "blah about d.");
        exceptionNode = list.get(nodeCount++);
        checkExceptionProperties(exceptionNode, "E3", "false", "161989364", "etch.tests.Inheritance.E3", "etch.tests.Inheritance.E2", "E3 descr.");
        checkExceptionValueProperties(exceptionNode, "e", "352988320", "e", "int", "false", null, "blah about e.");
        checkExceptionValueProperties(exceptionNode, "f", "352988321", "f", "int", "false", null, "blah about f.");
    }

    /** @throws Exception */
    @org.junit.Test
    public void testMethods() throws Exception {
        NodeList list = document.getElementsByTagName("method");
        assertEquals(16, list.getLength());
        int nodeCount = 0;
        Node methodNode = list.item(nodeCount++);
        checkMethodProperties(methodNode, "f1", "164154129", "etch.tests.Inheritance.f1", "none", "false", "server", null, "Blah about f1.", null);
        checkAuthorize(methodNode, null);
        checkMethodFieldProperties(methodNode, "v", "352988337", "v", "object", "true", "false", null, "a value.");
        checkResult(methodNode, "1104909012", "etch.tests.Inheritance._result_f1", "object", "true", "false", null, "the same value.");
        methodNode = list.item(nodeCount++);
        checkMethodProperties(methodNode, "_result_f1", "1104909012", "etch.tests.Inheritance._result_f1", "none", "true", "client", "0", null, "result");
        checkAuthorize(methodNode, null);
        checkMethodFieldProperties(methodNode, "result", "-2130379326", "result", "object", "true", "false", null, null);
        methodNode = list.item(nodeCount++);
        checkMethodProperties(methodNode, "f2", "164154130", "etch.tests.Inheritance.f2", "none", "false", "server", null, "Blah about f2.", null);
        checkAuthorize(methodNode, null);
        checkMethodFieldProperties(methodNode, "v", "352988337", "v", "etch.tests.Inheritance.S1", "false", "false", null, "a value.");
        checkResult(methodNode, "1104909013", "etch.tests.Inheritance._result_f2", "etch.tests.Inheritance.S1", "false", "false", null, "the same value.");
        methodNode = list.item(nodeCount++);
        checkMethodProperties(methodNode, "_result_f2", "1104909013", "etch.tests.Inheritance._result_f2", "none", "true", "client", "0", null, "result");
        checkAuthorize(methodNode, null);
        checkMethodFieldProperties(methodNode, "result", "-2130379326", "result", "etch.tests.Inheritance.S1", "false", "false", null, null);
        methodNode = list.item(nodeCount++);
        checkMethodProperties(methodNode, "f3", "164154131", "etch.tests.Inheritance.f3", "none", "false", "server", null, "Blah about f3.", null);
        checkAuthorize(methodNode, null);
        checkMethodFieldProperties(methodNode, "v", "352988337", "v", "etch.tests.Inheritance.S2", "false", "false", null, "a value.");
        checkResult(methodNode, "1104909014", "etch.tests.Inheritance._result_f3", "etch.tests.Inheritance.S2", "false", "false", null, "the same value.");
        methodNode = list.item(nodeCount++);
        checkMethodProperties(methodNode, "_result_f3", "1104909014", "etch.tests.Inheritance._result_f3", "none", "true", "client", "0", null, "result");
        checkAuthorize(methodNode, null);
        checkMethodFieldProperties(methodNode, "result", "-2130379326", "result", "etch.tests.Inheritance.S2", "false", "false", null, null);
        methodNode = list.item(nodeCount++);
        checkMethodProperties(methodNode, "f4", "164154132", "etch.tests.Inheritance.f4", "none", "false", "server", null, "Blah about f4.", null);
        checkAuthorize(methodNode, null);
        checkMethodFieldProperties(methodNode, "v", "352988337", "v", "etch.tests.Inheritance.S3", "false", "false", null, "a value.");
        checkResult(methodNode, "1104909015", "etch.tests.Inheritance._result_f4", "etch.tests.Inheritance.S3", "false", "false", null, "the same value.");
        methodNode = list.item(nodeCount++);
        checkMethodProperties(methodNode, "_result_f4", "1104909015", "etch.tests.Inheritance._result_f4", "none", "true", "client", "0", null, "result");
        checkAuthorize(methodNode, null);
        checkMethodFieldProperties(methodNode, "result", "-2130379326", "result", "etch.tests.Inheritance.S3", "false", "false", null, null);
        methodNode = list.item(nodeCount++);
        checkMethodProperties(methodNode, "f5", "164154133", "etch.tests.Inheritance.f5", "none", "false", "server", null, "Blah about f5.", null);
        checkAuthorize(methodNode, null);
        checkMethodFieldProperties(methodNode, "v", "352988337", "v", "object", "true", "true", "1", "a value.");
        checkResult(methodNode, "1104909016", "etch.tests.Inheritance._result_f5", "object", "true", "true", "1", "the same value.");
        methodNode = list.item(nodeCount++);
        checkMethodProperties(methodNode, "_result_f5", "1104909016", "etch.tests.Inheritance._result_f5", "none", "true", "client", "0", null, "result");
        checkAuthorize(methodNode, null);
        checkMethodFieldProperties(methodNode, "result", "-2130379326", "result", "object", "true", "true", "1", null);
        methodNode = list.item(nodeCount++);
        checkMethodProperties(methodNode, "f6", "164154134", "etch.tests.Inheritance.f6", "none", "false", "server", null, "Blah about f6.", null);
        checkAuthorize(methodNode, null);
        checkMethodFieldProperties(methodNode, "v", "352988337", "v", "etch.tests.Inheritance.S1", "false", "true", "1", "a value.");
        checkResult(methodNode, "1104909017", "etch.tests.Inheritance._result_f6", "etch.tests.Inheritance.S1", "false", "true", "1", "the same value.");
        methodNode = list.item(nodeCount++);
        checkMethodProperties(methodNode, "_result_f6", "1104909017", "etch.tests.Inheritance._result_f6", "none", "true", "client", "0", null, "result");
        checkAuthorize(methodNode, null);
        checkMethodFieldProperties(methodNode, "result", "-2130379326", "result", "etch.tests.Inheritance.S1", "false", "true", "1", null);
        methodNode = list.item(nodeCount++);
        checkMethodProperties(methodNode, "f7", "164154135", "etch.tests.Inheritance.f7", "none", "false", "server", null, "Blah about f7.", null);
        checkAuthorize(methodNode, null);
        checkMethodFieldProperties(methodNode, "v", "352988337", "v", "etch.tests.Inheritance.S2", "false", "true", "1", "a value.");
        checkResult(methodNode, "1104909018", "etch.tests.Inheritance._result_f7", "etch.tests.Inheritance.S2", "false", "true", "1", "the same value.");
        methodNode = list.item(nodeCount++);
        checkMethodProperties(methodNode, "_result_f7", "1104909018", "etch.tests.Inheritance._result_f7", "none", "true", "client", "0", null, "result");
        checkAuthorize(methodNode, null);
        checkMethodFieldProperties(methodNode, "result", "-2130379326", "result", "etch.tests.Inheritance.S2", "false", "true", "1", null);
        methodNode = list.item(nodeCount++);
        checkMethodProperties(methodNode, "f8", "164154136", "etch.tests.Inheritance.f8", "none", "false", "server", null, "Blah about f8.", null);
        checkAuthorize(methodNode, null);
        checkMethodFieldProperties(methodNode, "v", "352988337", "v", "etch.tests.Inheritance.S3", "false", "true", "1", "a value.");
        checkResult(methodNode, "1104909019", "etch.tests.Inheritance._result_f8", "etch.tests.Inheritance.S3", "false", "true", "1", "the same value.");
        methodNode = list.item(nodeCount++);
        checkMethodProperties(methodNode, "_result_f8", "1104909019", "etch.tests.Inheritance._result_f8", "none", "true", "client", "0", null, "result");
        checkAuthorize(methodNode, null);
        checkMethodFieldProperties(methodNode, "result", "-2130379326", "result", "etch.tests.Inheritance.S3", "false", "true", "1", null);
    }

    private void checkStructProperties(Node structNode, String structName, String structTypeId, String structTypeName, String baseTypeName, String description) {
        NamedNodeMap attrs = structNode.getAttributes();
        assertTrue(onlyAttrsOfName(structNode, new String[] { "name", "typeId", "typeName", "baseType" }));
        assertTrue(onlyChildNodesOfName(structNode, new String[] { "description", "field" }));
        assertEquals(attrs.getLength(), 4);
        String nameAttr = attrs.getNamedItem("name").getNodeValue();
        String typeAttr = attrs.getNamedItem("typeId").getNodeValue();
        String valueAttr = attrs.getNamedItem("typeName").getNodeValue();
        String baseTypeAttr = attrs.getNamedItem("baseType").getNodeValue();
        assertEquals(nameAttr, structName);
        assertEquals(typeAttr, structTypeId);
        assertEquals(valueAttr, structTypeName);
        assertEquals(baseTypeAttr, baseTypeName);
        Node child = getDescriptionElement(structNode);
        if (description == null) {
            assertNull(child);
        } else {
            String descriptionValue = child.getTextContent().trim();
            assertEquals(descriptionValue, description);
            assertEquals(getChildElementCount(child), 0);
        }
    }

    private void checkStructValueProperties(Node structNode, String structName, String fieldId, String fieldName, String type, String isPrimitiveType, String isArray, String dimension, String description) {
        Node fieldNode = null;
        NodeList children = structNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE && children.item(i).getNodeName() == "field") {
                Node thisNode = children.item(i);
                NamedNodeMap attrs = thisNode.getAttributes();
                Node valueAttr = attrs.getNamedItem("name");
                String thisValue = valueAttr.getNodeValue();
                if (thisValue.compareTo(structName) == 0) {
                    fieldNode = children.item(i);
                    break;
                }
            }
        }
        assertNotNull(fieldNode);
        assertTrue(onlyAttrsOfName(fieldNode, new String[] { "name", "fieldId", "fieldName", "type", "isPrimitiveType", "isArray", "dimension" }));
        assertTrue(onlyChildNodesOfName(fieldNode, new String[] { "description" }));
        NamedNodeMap attrs = fieldNode.getAttributes();
        assertEquals(attrs.getLength(), isArray == "false" ? 6 : 7);
        String fieldIdAttr = attrs.getNamedItem("fieldId").getNodeValue();
        String fieldNameAttr = attrs.getNamedItem("fieldName").getNodeValue();
        String typeAttr = attrs.getNamedItem("type").getNodeValue();
        String isArrayAttr = attrs.getNamedItem("isArray").getNodeValue();
        Node dimensionNode = attrs.getNamedItem("dimension");
        String dimensionAttr = dimensionNode == null ? dimension : dimensionNode.getNodeValue();
        assertEquals(fieldIdAttr, fieldId);
        assertEquals(fieldNameAttr, fieldName);
        assertEquals(typeAttr, type);
        assertEquals(isArrayAttr, isArray);
        assertEquals(dimensionAttr, dimension);
        Node child = getDescriptionElement(fieldNode);
        if (description == null) {
            assertNull(child);
        } else {
            String descriptionValue = child.getTextContent().trim();
            assertEquals(descriptionValue, description);
            assertEquals(getChildElementCount(child), 0);
        }
    }

    private void checkExceptionProperties(Node exceptionNode, String name, String isUnchecked, String exceptionId, String exceptionName, String baseTypeName, String description) {
        NamedNodeMap attrs = exceptionNode.getAttributes();
        assertTrue(onlyAttrsOfName(exceptionNode, new String[] { "name", "isUnchecked", "typeId", "typeName", "baseType" }));
        assertTrue(onlyChildNodesOfName(exceptionNode, new String[] { "description", "field" }));
        assertEquals(attrs.getLength(), 5);
        String nameAttr = attrs.getNamedItem("name").getNodeValue();
        String isUncheckedAttr = attrs.getNamedItem("isUnchecked").getNodeValue();
        String typeAttr = attrs.getNamedItem("typeId").getNodeValue();
        String typeName = attrs.getNamedItem("typeName").getNodeValue();
        String baseTypeAttr = attrs.getNamedItem("baseType").getNodeValue();
        assertEquals(nameAttr, name);
        assertEquals(isUncheckedAttr, isUnchecked);
        assertEquals(typeAttr, exceptionId);
        assertEquals(typeName, exceptionName);
        assertEquals(baseTypeName, baseTypeAttr);
        Node child = getDescriptionElement(exceptionNode);
        if (description == null) {
            assertNull(child);
        } else {
            String descriptionValue = child.getTextContent().trim();
            assertEquals(descriptionValue, description);
            assertEquals(getChildElementCount(child), 0);
        }
    }

    private void checkExceptionValueProperties(Node exceptionNode, String name, String fieldId, String fieldName, String type, String isArray, String dimension, String description) {
        Node fieldNode = null;
        NodeList children = exceptionNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE && children.item(i).getNodeName() == "field") {
                Node thisNode = children.item(i);
                NamedNodeMap attrs = thisNode.getAttributes();
                Node valueAttr = attrs.getNamedItem("name");
                String thisValue = valueAttr.getNodeValue();
                if (thisValue.compareTo(name) == 0) {
                    fieldNode = children.item(i);
                    break;
                }
            }
        }
        assertNotNull(fieldNode);
        assertTrue(onlyAttrsOfName(fieldNode, new String[] { "name", "fieldId", "fieldName", "type", "isArray", "dimension" }));
        assertTrue(onlyChildNodesOfName(fieldNode, new String[] { "description", "field" }));
        NamedNodeMap attrs = fieldNode.getAttributes();
        assertEquals(attrs.getLength(), isArray == "false" ? 5 : 6);
        String fieldIdAttr = attrs.getNamedItem("fieldId").getNodeValue();
        String fieldNameAttr = attrs.getNamedItem("fieldName").getNodeValue();
        String typeAttr = attrs.getNamedItem("type").getNodeValue();
        String isArrayAttr = attrs.getNamedItem("isArray").getNodeValue();
        Node dimensionNode = attrs.getNamedItem("dimension");
        String dimensionAttr = dimensionNode == null ? dimension : dimensionNode.getNodeValue();
        assertEquals(fieldIdAttr, fieldId);
        assertEquals(fieldNameAttr, fieldName);
        assertEquals(typeAttr, type);
        assertEquals(isArrayAttr, isArray);
        assertEquals(dimensionAttr, dimension);
        Node child = getDescriptionElement(fieldNode);
        if (description == null) {
            assertNull(child);
        } else {
            String descriptionValue = child.getTextContent().trim();
            assertEquals(descriptionValue, description);
            assertEquals(getChildElementCount(child), 0);
        }
    }

    private void checkMethodProperties(Node methodNode, String name, String methodId, String methodName, String pool, String isOneway, String direction, String timeout, String description, String result) {
        NamedNodeMap attrs = methodNode.getAttributes();
        boolean isResultMessage = false;
        assertTrue(onlyAttrsOfName(methodNode, new String[] { "name", "typeId", "typeName", "asyncReceiverMode", "isOneway", "messageDirection", "timeout", "responseField" }));
        assertTrue(onlyChildNodesOfName(methodNode, new String[] { "description", "exception", "authorize", "field", "result" }));
        isResultMessage = name.startsWith("_result");
        assertEquals(isResultMessage ? 8 : 6, attrs.getLength());
        String nameAttr = attrs.getNamedItem("name").getNodeValue();
        String typeIdAttr = attrs.getNamedItem("typeId").getNodeValue();
        String typeNameAttr = attrs.getNamedItem("typeName").getNodeValue();
        String poolAttr = attrs.getNamedItem("asyncReceiverMode").getNodeValue();
        String isOnewayAttr = attrs.getNamedItem("isOneway").getNodeValue();
        String directionAttr = attrs.getNamedItem("messageDirection").getNodeValue();
        String timeoutAttr = "";
        String resultAttr = "";
        if (isResultMessage) {
            timeoutAttr = attrs.getNamedItem("timeout").getNodeValue();
            resultAttr = attrs.getNamedItem("responseField").getNodeValue();
        }
        assertEquals(nameAttr, name);
        assertEquals(typeIdAttr, methodId);
        assertEquals(typeNameAttr, methodName);
        assertEquals(poolAttr, pool);
        assertEquals(isOnewayAttr, isOneway);
        assertEquals(directionAttr, direction);
        if (isResultMessage) {
            assertEquals(timeoutAttr, timeout);
            assertEquals(resultAttr, result);
        }
        Node child = getDescriptionElement(methodNode);
        if (description == null) {
            assertNull(child);
        } else {
            String descriptionValue = child.getTextContent().trim();
            assertEquals(descriptionValue, description);
            assertEquals(getChildElementCount(child), 0);
        }
    }

    private void checkAuthorize(Node methodNode, String authorizeMethodName) {
        Node child = getAuthorizeElement(methodNode);
        if (authorizeMethodName == null) {
            assertNull(child);
        } else {
            assertTrue(onlyAttrsOfName(child, new String[] { "methodName" }));
            assertTrue(onlyChildNodesOfName(child, new String[] {}));
            String authorizeAttr = child.getAttributes().getNamedItem("methodName").getNodeValue();
            assertEquals(authorizeAttr, authorizeMethodName);
            assertEquals(getChildElementCount(child), 0);
        }
    }

    private void checkResult(Node methodNode, String fieldId, String fieldName, String type, String isPrimitiveType, String isArray, String dimension, String description) {
        Node resultNode = getResultElement(methodNode);
        assertTrue(onlyAttrsOfName(resultNode, new String[] { "fieldId", "fieldName", "type", "isPrimitiveType", "isArray", "dimension" }));
        assertNotNull(resultNode);
        assertTrue(onlyChildNodesOfName(resultNode, new String[] { "description" }));
        NamedNodeMap attrs = resultNode.getAttributes();
        String fieldIdAttr = null;
        String fieldNameAttr = null;
        if (fieldId == null) {
            assertEquals(attrs.getLength(), 2);
        } else {
            fieldIdAttr = attrs.getNamedItem("fieldId").getNodeValue();
            fieldNameAttr = attrs.getNamedItem("fieldName").getNodeValue();
            assertEquals(attrs.getLength(), isArray == "false" ? 5 : 6);
        }
        String typeAttr = attrs.getNamedItem("type").getNodeValue();
        String isArrayAttr = attrs.getNamedItem("isArray").getNodeValue();
        Node dimensionNode = attrs.getNamedItem("dimension");
        String dimensionAttr = dimensionNode == null ? dimension : dimensionNode.getNodeValue();
        assertEquals(fieldIdAttr, fieldId);
        assertEquals(fieldNameAttr, fieldName);
        assertEquals(typeAttr, type);
        assertEquals(isArrayAttr, isArray);
        assertEquals(dimensionAttr, dimension);
        Node child = getDescriptionElement(resultNode);
        if (description == null) {
            assertNull(child);
        } else {
            String descriptionValue = child.getTextContent().trim();
            assertEquals(descriptionValue, description);
            assertEquals(getChildElementCount(child), 0);
        }
    }

    private void checkMethodFieldProperties(Node methodNode, String name, String fieldId, String fieldName, String type, String isPrimitiveType, String isArray, String dimension, String description) {
        Node fieldNode = null;
        NodeList children = methodNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE && children.item(i).getNodeName() == "field") {
                Node thisNode = children.item(i);
                NamedNodeMap attrs = thisNode.getAttributes();
                Node valueAttr = attrs.getNamedItem("name");
                String thisValue = valueAttr.getNodeValue();
                if (thisValue.compareTo(name) == 0) {
                    fieldNode = children.item(i);
                    break;
                }
            }
        }
        assertNotNull(fieldNode);
        assertTrue(onlyAttrsOfName(fieldNode, new String[] { "name", "fieldId", "fieldName", "type", "isPrimitiveType", "isArray", "dimension" }));
        assertTrue(onlyChildNodesOfName(fieldNode, new String[] { "description" }));
        NamedNodeMap attrs = fieldNode.getAttributes();
        assertEquals(attrs.getLength(), isArray == "false" ? 6 : 7);
        String fieldIdAttr = attrs.getNamedItem("fieldId").getNodeValue();
        String fieldNameAttr = attrs.getNamedItem("fieldName").getNodeValue();
        String typeAttr = attrs.getNamedItem("type").getNodeValue();
        String isArrayAttr = attrs.getNamedItem("isArray").getNodeValue();
        String isPrimitiveTypeAttr = attrs.getNamedItem("isPrimitiveType").getNodeValue();
        Node dimensionNode = attrs.getNamedItem("dimension");
        String dimensionAttr = dimensionNode == null ? dimension : dimensionNode.getNodeValue();
        assertEquals(fieldIdAttr, fieldId);
        assertEquals(fieldNameAttr, fieldName);
        assertEquals(typeAttr, type);
        assertEquals(isArrayAttr, isArray);
        assertEquals(dimensionAttr, dimension);
        assertEquals(isPrimitiveType, isPrimitiveTypeAttr);
        Node child = getDescriptionElement(fieldNode);
        if (description == null) {
            assertNull(child);
        } else {
            String descriptionValue = child.getTextContent().trim();
            assertEquals(descriptionValue, description);
            assertEquals(getChildElementCount(child), 0);
        }
    }

    private Node getDescriptionElement(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            short nodeType = children.item(i).getNodeType();
            String nodeName = children.item(i).getNodeName();
            if (nodeType == Node.ELEMENT_NODE && nodeName == "description") {
                return children.item(i);
            }
        }
        return null;
    }

    private Node getAuthorizeElement(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            short nodeType = children.item(i).getNodeType();
            String nodeName = children.item(i).getNodeName();
            if (nodeType == Node.ELEMENT_NODE && nodeName == "authorize") {
                return children.item(i);
            }
        }
        return null;
    }

    private Node getResultElement(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            short nodeType = children.item(i).getNodeType();
            String nodeName = children.item(i).getNodeName();
            if (nodeType == Node.ELEMENT_NODE && nodeName == "result") {
                return children.item(i);
            }
        }
        return null;
    }

    private int getChildElementCount(Node node) {
        int count = 0;
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                count++;
            }
        }
        return count;
    }

    private boolean onlyChildNodesOfName(Node node, String[] elementNamesToCheck) {
        NodeList children = node.getChildNodes();
        boolean hasValidChildren = true;
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                String elementName = children.item(i).getNodeName();
                boolean matches = false;
                for (int j = 0; j < elementNamesToCheck.length; j++) {
                    if (0 == elementName.compareTo(elementNamesToCheck[j])) {
                        matches = true;
                        break;
                    }
                }
                if (matches == false) {
                    hasValidChildren = false;
                    break;
                }
            }
        }
        return hasValidChildren;
    }

    private boolean onlyAttrsOfName(Node node, String[] attrNamesToCheck) {
        NamedNodeMap attrs = node.getAttributes();
        boolean hasValidAttrs = true;
        for (int i = 0; i < attrs.getLength(); i++) {
            String attrName = attrs.item(i).getNodeName();
            boolean matches = false;
            for (int j = 0; j < attrNamesToCheck.length; j++) {
                if (0 == attrName.compareTo(attrNamesToCheck[j])) {
                    matches = true;
                    break;
                }
            }
            if (matches == false) {
                hasValidAttrs = false;
                break;
            }
        }
        return hasValidAttrs;
    }
}
