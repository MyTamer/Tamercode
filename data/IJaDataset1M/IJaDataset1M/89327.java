package edu.uci.ics.jung.random.generators;

import java.util.Random;
import edu.uci.ics.jung.graph.ArchetypeGraph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.Indexer;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.random.generators.GraphGenerator;

/**
 *
 * @author Eytan Adar, modification of SimpleRandomGenerator
 */
public class SimpleRandomDirectedGenerator implements GraphGenerator {

    private int mNumVertices;

    private int mNumEdges;

    protected Vertex newVertex() {
        return new SparseVertex();
    }

    /**
     * Constructs the generator
     *
     * @param numVertices
     *            number of vertices the graph should have
     * @param numEdges
     *            number of edges the graph should have
     */
    public SimpleRandomDirectedGenerator(int numVertices, int numEdges) {
        if (numVertices <= 0) {
            throw new IllegalArgumentException("A positive # of vertices must be specified.");
        }
        mNumVertices = numVertices;
        long calcVertices = numVertices;
        if (numEdges < 0 || numEdges > (calcVertices * (calcVertices - 1) / 2)) {
            throw new IllegalArgumentException("# of edges [" + numEdges + "] must be between 0 and |V|(|V|-1)/2, v=" + numVertices);
        }
        mNumEdges = numEdges;
    }

    public void setSeed(long seed) {
        this.seedSet = true;
        this.seed = seed;
    }

    boolean seedSet = false;

    long seed = 0;

    /**
     * Generated the graph by creating |V| vertics and then picking |E| random
     * edges
     *
     * @return generated graph
     */
    public ArchetypeGraph generateGraph() {
        Random rand;
        if (seedSet) {
            rand = new Random(seed);
        } else {
            rand = new Random();
        }
        DirectedSparseGraph g = new DirectedSparseGraph();
        for (int i = 0; i < mNumVertices; i++) {
            g.addVertex(newVertex());
        }
        Indexer id = Indexer.getIndexer(g);
        int ctr = 0;
        while (ctr < mNumEdges) {
            Vertex v1 = (Vertex) id.getVertex(rand.nextInt(mNumVertices));
            Vertex v2 = (Vertex) id.getVertex(rand.nextInt(mNumVertices));
            if ((v1 != v2) && !v1.isNeighborOf(v2)) {
                g.addEdge(new DirectedSparseEdge(v1, v2));
                ctr++;
            }
        }
        return g;
    }

    /**
     * @return Returns the mNumEdges.
     */
    public long getNumEdges() {
        return mNumEdges;
    }

    /**
     * @return Returns the mNumVertices.
     */
    public long getNumVertices() {
        return mNumVertices;
    }
}
