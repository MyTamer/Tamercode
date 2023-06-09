package de.fraunhofer.isst.axbench.axlang.syntaxtree;

import java.util.*;

/**
 * Represents a grammar list, e.g. ( A )+
 */
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

    public void accept(de.fraunhofer.isst.axbench.axlang.visitor.Visitor v) {
        v.visit(this);
    }

    public <R, A> R accept(de.fraunhofer.isst.axbench.axlang.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(de.fraunhofer.isst.axbench.axlang.visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(de.fraunhofer.isst.axbench.axlang.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }

    public Vector<Node> nodes;
}
