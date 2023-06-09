package org.rubypeople.rdt.internal.ti.util;

import org.jruby.ast.Node;
import org.jruby.evaluator.Instruction;

/**
 * Visitor to find the first node that precedes a given offset that satisfies a given condition.
 * @author Jason Morrison
 */
public class FirstPrecursorNodeLocator extends NodeLocator {

    private FirstPrecursorNodeLocator() {
    }

    private static FirstPrecursorNodeLocator staticInstance = new FirstPrecursorNodeLocator();

    public static FirstPrecursorNodeLocator Instance() {
        return staticInstance;
    }

    /** Offset to start searching backwards from. */
    private int offset;

    /** INodeAcceptor that defines the desired node. */
    private INodeAcceptor acceptor;

    /** Running best match for closest precursor */
    private Node locatedNode;

    /**
	 * Finds the first node preceding the given offset that is accepted by the acceptor.
	 * @param rootNode Root Node that contains all nodes to search.
	 * @param offset Offset to search backwards from; returned node must occur strictly before this (i.e. end before offset.)
	 * @param acceptor INodeAcceptor defining the condition which the desired node fulfills.
	 * @return First precursor or null.
	 */
    public Node findFirstPrecursor(Node rootNode, int offset, INodeAcceptor acceptor) {
        locatedNode = null;
        this.offset = offset;
        this.acceptor = acceptor;
        rootNode.accept(this);
        return locatedNode;
    }

    /**
	 * Searches via InOrderVisitor for the closest precursor.
	 */
    public Instruction handleNode(Node iVisited) {
        if ((iVisited.getPosition().getEndOffset() <= offset) || (iVisited.getPosition().getStartOffset() <= offset)) {
            if (acceptor.doesAccept(iVisited)) {
                System.out.println("Recording accepted node: " + iVisited.getClass().getSimpleName() + "@" + iVisited.getPosition().getStartOffset() + ".." + iVisited.getPosition().getEndOffset());
                locatedNode = iVisited;
            }
        }
        return super.handleNode(iVisited);
    }
}
