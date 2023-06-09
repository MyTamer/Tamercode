package axt.db;

import java.util.HashSet;
import java.util.Set;

/**
 * Node generated by hbm2java
 */
public class Node implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2203005845345835051L;

    private String node;

    private Boolean active;

    private Set<Nodeconnector> nodeconnectors = new HashSet<Nodeconnector>(0);

    private Set<Nodevalue> nodevalues = new HashSet<Nodevalue>(0);

    /** default constructor */
    public Node() {
    }

    /** minimal constructor */
    public Node(String node) {
        this.node = node;
    }

    /** full constructor */
    public Node(String node, Boolean active, Set<Nodeconnector> Nodeconnectors, Set<Nodevalue> Nodevalues) {
        this.node = node;
        this.active = active;
        this.nodeconnectors = Nodeconnectors;
        this.nodevalues = Nodevalues;
    }

    public String getNode() {
        return this.node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Nodeconnector> getNodeconnectors() {
        return this.nodeconnectors;
    }

    public void setNodeconnectors(Set<Nodeconnector> Nodeconnectors) {
        this.nodeconnectors = Nodeconnectors;
    }

    public Set<Nodevalue> getNodevalues() {
        return this.nodevalues;
    }

    public void setNodevalues(Set<Nodevalue> Nodevalues) {
        this.nodevalues = Nodevalues;
    }
}
