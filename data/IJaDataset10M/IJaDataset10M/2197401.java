package batch.tasks;

import batch.BatchResultEntry;
import de.tu_berlin.math.coga.common.algorithm.AlgorithmListener;
import de.tu_berlin.math.coga.zet.NetworkFlowModel;
import ds.z.Project;
import ds.z.Assignment;
import ds.z.ConcreteAssignment;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class BatchGraphCreateOnlyTask {

    /** The batch object which stores the calculated results. */
    private BatchResultEntry res;

    /** The number of the run, used for accessing the result in {@link res} */
    private int runNumber;

    /** The {@link ds.z.Project} */
    private Project project;

    /** The used assignment for the ca run. */
    private Assignment assignment;

    /** The concrete assignment. */
    private ConcreteAssignment[] concreteAssignments = null;

    /**
	 * Initializes a new instance of this task.
	 * @param graphAlgo the graph algorithm automaton enumeration containing the graph algorithm that should be used
	 * @param res the object containing which stores the results
	 * @param runNumber the number of the run, used to access the results
	 * @param maxTime the maximal time for the algorithm, if needed
	 * @param project the project on which the ca runs
	 * @param assignment the selected assignment
	 * @param concreteAssignments the concrete assignments that were already calculated for the cellular automaton. can be null.
	 */
    public BatchGraphCreateOnlyTask(BatchResultEntry res, int runNumber, Project project, Assignment assignment, ConcreteAssignment[] concreteAssignments) {
        this.res = res;
        this.runNumber = runNumber;
        this.project = project;
        this.assignment = assignment;
        this.concreteAssignments = concreteAssignments;
    }

    public AlgorithmListener listener;

    /**
	 * Runs a graph algorithm. At first the {@link ds.NetworkFlowModel}
	 * is created. After that the algorithm stored in the submitted
	 * {@link GraphAlgorithm} is executed. After execution the results are stored in an
	 * {@link BatchResultEntry}.
	 */
    public void run() {
        NetworkFlowModel nfo = new NetworkFlowModel();
        res.setNetworkFlowModel(nfo);
        ConcreteAssignment concreteAssignment;
        if (runNumber < 0) concreteAssignment = assignment.createConcreteAssignment(400); else concreteAssignment = concreteAssignments[runNumber];
        res = null;
    }
}
