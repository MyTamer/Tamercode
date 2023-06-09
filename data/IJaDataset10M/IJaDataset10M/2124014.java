package org.sbml.simulator.math.odes;

import java.util.List;

/**
 * This Class represents an event-driven DES
 * 
 * @author Alexander D&ouml;rr
 * @author Andreas Dr&auml;ger
 * @date 2010-02-04
 * @version $Rev: 4 $
 * @since 1.0
 */
public interface EventDESystem extends DESystem {

    /**
	 * Counts the number of events in this system.
	 * 
	 * @return The number of events that are to be checked and potentially
	 *         evaluated in each time point.
	 */
    public int getNumEvents();

    /**
	 * Counts the number of rules to be evaluated in each time point.
	 * 
	 * @return The number of rules in the system.
	 */
    public int getNumRules();

    /**
	 * 
	 * @param t
	 *            The current simulation time.
	 * @param Y
	 *            The current change of the system.
	 * @return
	 * @throws IntegrationException
	 */
    public List<DESAssignment> processAssignmentRules(double t, double Y[]) throws IntegrationException;

    /**
	 * Returns a list with event assignments for the events triggered either by
	 * the time t or by the concentrations of the species stored in Y.
	 * 
	 * @param t
	 *            The current simulation time.
	 * @param Y
	 *            The current change of the system.
	 * 
	 * @return Returns a list with event assignments for the events triggered
	 * @throws IntegrationException
	 */
    public List<DESAssignment> getEventAssignments(double t, double Y[]) throws IntegrationException;
}
