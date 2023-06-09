package org.codecover.instrumentation.java15.syntaxtree;

import java.util.*;

/**
 * Represents a grammar list, e.g. ( A )+
 */
@SuppressWarnings("all")
public class NodeList implements NodeListInterface {

    public NodeList() {
        nodes = new Vector<Node>();
    }

    public NodeList(Node firstNode) {
        nodes = new Vector<Node>();
        addNode(firstNode);
    }

    public void addNode(Node n) {
        nodes.addElement(n);
        n.setParent(this);
    }

    public Enumeration<Node> elements() {
        return nodes.elements();
    }

    public Node elementAt(int i) {
        return nodes.elementAt(i);
    }

    public int size() {
        return nodes.size();
    }

    public void accept(org.codecover.instrumentation.java15.visitor.Visitor v) {
        v.visit(this);
    }

    public void accept(org.codecover.instrumentation.java15.visitor.VisitorWithException v) throws java.io.IOException {
        v.visit(this);
    }

    public <R, A> R accept(org.codecover.instrumentation.java15.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(org.codecover.instrumentation.java15.visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(org.codecover.instrumentation.java15.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }

    public void setParent(Node n) {
        parent = n;
    }

    public Node getParent() {
        return parent;
    }

    /** for debugging purposes */
    @Override
    public String toString() {
        return org.codecover.instrumentation.java15.visitor.TreeSourceFileImageDumper.convertToString(this);
    }

    private Node parent;

    public Vector<Node> nodes;
}
