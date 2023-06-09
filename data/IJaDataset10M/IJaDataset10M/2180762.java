package org.eclipse.ui;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;

/**
 * Implementors represent creation wizards that are to be
 * contributed to the workbench's creation wizard extension point.
 *
 * @see org.eclipse.jface.wizard.IWizard
 */
public interface IWorkbenchWizard extends IWizard {

    /**
     * Initializes this creation wizard using the passed workbench and
     * object selection.
     * <p>
     * This method is called after the no argument constructor and
     * before other methods are called.
     * </p>
     *
     * @param workbench the current workbench
     * @param selection the current object selection
     */
    void init(IWorkbench workbench, IStructuredSelection selection);
}
