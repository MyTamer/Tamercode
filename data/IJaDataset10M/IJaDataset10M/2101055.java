package com.ibm.wala.cast.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import com.ibm.wala.cast.tree.CAstNode;

public class CAstToDOM extends CAstPrinter {

    private static final String VALUE_TAG = "value";

    private static final String TYPE_TAG = "type";

    public static Document toDOM(CAstNode astRoot) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            DOMImplementation domImplementation = documentBuilder.getDOMImplementation();
            Document document = domImplementation.createDocument("CAst", "CAst", null);
            Element rootNode = document.getDocumentElement();
            nodeToDOM(document, rootNode, astRoot);
            return document;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new RuntimeException("DOM builder error.");
        }
    }

    private static void nodeToDOM(Document doc, Element root, CAstNode astNode) {
        Element nodeElt = doc.createElement(kindAsString(astNode.getKind()));
        if (astNode.getValue() == null) {
            for (int i = 0; i < astNode.getChildCount(); i++) {
                nodeToDOM(doc, nodeElt, astNode.getChild(i));
            }
        } else {
            Element typeTag = doc.createElement(TYPE_TAG);
            Text type = doc.createTextNode(astNode.getValue().getClass().toString());
            typeTag.appendChild(type);
            nodeElt.appendChild(typeTag);
            Element valueTag = doc.createElement(VALUE_TAG);
            Text value = doc.createTextNode(astNode.getValue().toString());
            valueTag.appendChild(value);
            nodeElt.appendChild(valueTag);
        }
        root.appendChild(nodeElt);
    }
}
