package org.eclipse.gef.examples.flow.model.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.flow.model.Activity;

/**
 * Command to rename Activities.
 * @author Daniel Lee
 */
public class RenameActivityCommand extends Command {

    private Activity source;

    private String name, oldName;

    /**
 * @see org.eclipse.gef.commands.Command#execute()
 */
    public void execute() {
        source.setName(name);
    }

    /**
 * Sets the new Activity name
 * @param string the new name
 */
    public void setName(String string) {
        name = string;
    }

    /**
 * Sets the old Activity name
 * @param string the old name
 */
    public void setOldName(String string) {
        oldName = string;
    }

    /**
 * Sets the source Activity
 * @param activity the source Activity
 */
    public void setSource(Activity activity) {
        source = activity;
    }

    /**
 * @see org.eclipse.gef.commands.Command#undo()
 */
    public void undo() {
        source.setName(oldName);
    }
}
