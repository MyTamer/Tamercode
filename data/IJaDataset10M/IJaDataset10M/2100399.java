package se.mdh.mrtc.saveccm.composite.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;

/**
 * @generated
 */
public class ConnectionItemSemanticEditPolicy extends SaveccmBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        return getGEFWrapper(new DestroyElementCommand(req));
    }
}
