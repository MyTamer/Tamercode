package algo.ca.rule;

import ds.ca.evac.Cell;
import ds.ca.evac.Individual;
import ds.ca.evac.StaticPotential;
import ds.ca.evac.TargetCell;
import java.util.HashMap;

/**
 * This rule applies the exit mapping to the cellular automaton. It is not
 * allowed that one individual in the mapping has no exit assigned.
 * @author Jan-Philipp Kappmeier
 */
public class InitialPotentialExitMappingRuleStrict extends AbstractInitialRule {

    boolean initialized = false;

    HashMap<TargetCell, StaticPotential> potentialMapping;

    private void init() {
        potentialMapping = new HashMap<TargetCell, StaticPotential>();
        for (StaticPotential potential : esp.eca.getPotentialManager().getStaticPotentials()) {
            for (TargetCell target : potential.getAssociatedExitCells()) {
                if (potentialMapping.put(target, potential) != null) {
                    throw new IllegalArgumentException("There were two potentials leading to the same exit. This method can currently not deal with this.");
                }
            }
        }
        initialized = true;
    }

    /**
	 * Checks, whether the rule is executable or not.
	 * @param cell the cell on which the rule should be executed
	 * @return Returns true, if an Individual is standing
	 * on this cell, and moreover this Individual does 
	 * not already have a StaticPotential.
	 */
    @Override
    public boolean executableOn(Cell cell) {
        return cell.getIndividual() != null;
    }

    /**
	 * Assignes an exit (more precisely: the potential) for an individual.
	 * @param cell the cell on which the individual stands
	 * @throws java.lang.IllegalArgumentException if an individual has not been mapped to an exit.
	 */
    @Override
    protected void onExecute(Cell cell) throws IllegalArgumentException {
        if (!initialized) init();
        Individual individual = cell.getIndividual();
        TargetCell target = esp.eca.getIndividualToExitMapping().getExit(individual);
        if (target == null) {
            if (!individual.isDead()) throw new IllegalArgumentException("The individual " + individual.getNumber() + " lives, but has not been mapped to an exit." + " Therefore, I cannot map it to a potential.");
        } else {
            StaticPotential potential = potentialMapping.get(target);
            if (potential == null) throw new IllegalArgumentException("The target cell (room id, x, y) " + target.getRoom().getID() + ", " + target.getX() + ", " + target.getY() + " does not correspond to a static potential.");
            individual.setStaticPotential(potential);
        }
    }
}
