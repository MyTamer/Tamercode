package org.eclipse.jface.text.reconciler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;

/**
 * Abstract implementation of a reconcile step.
 *
 * @since 3.0
 */
public abstract class AbstractReconcileStep implements IReconcileStep {

    private IReconcileStep fNextStep;

    private IReconcileStep fPreviousStep;

    private IProgressMonitor fProgressMonitor;

    protected IReconcilableModel fInputModel;

    /**
	 * Creates an intermediate reconcile step which adds
	 * the given step to the pipe.
	 *
	 * @param step the reconcile step
	 */
    public AbstractReconcileStep(IReconcileStep step) {
        Assert.isNotNull(step);
        fNextStep = step;
        fNextStep.setPreviousStep(this);
    }

    /**
	 * Creates the last reconcile step of the pipe.
	 */
    public AbstractReconcileStep() {
    }

    public boolean isLastStep() {
        return fNextStep == null;
    }

    public boolean isFirstStep() {
        return fPreviousStep == null;
    }

    public void setProgressMonitor(IProgressMonitor monitor) {
        fProgressMonitor = monitor;
        if (!isLastStep()) fNextStep.setProgressMonitor(monitor);
    }

    public IProgressMonitor getProgressMonitor() {
        return fProgressMonitor;
    }

    public final IReconcileResult[] reconcile(IRegion partition) {
        IReconcileResult[] result = reconcileModel(null, partition);
        if (!isLastStep()) {
            fNextStep.setInputModel(getModel());
            IReconcileResult[] nextResult = fNextStep.reconcile(partition);
            return merge(result, convertToInputModel(nextResult));
        }
        return result;
    }

    public final IReconcileResult[] reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
        IReconcileResult[] result = reconcileModel(dirtyRegion, subRegion);
        if (!isLastStep()) {
            fNextStep.setInputModel(getModel());
            IReconcileResult[] nextResult = fNextStep.reconcile(dirtyRegion, subRegion);
            return merge(result, convertToInputModel(nextResult));
        }
        return result;
    }

    /**
	 * Reconciles the model of this reconcile step. The
	 * result is based on the input model.
	 *
	 * @param dirtyRegion the document region which has been changed
	 * @param subRegion the sub region in the dirty region which should be reconciled
	 * @return an array with reconcile results
	 */
    protected abstract IReconcileResult[] reconcileModel(DirtyRegion dirtyRegion, IRegion subRegion);

    /**
	 * Adapts the given an array with reconcile results to
	 * this step's input model and returns it.
	 *
	 * @param inputResults an array with reconcile results
	 * @return an array with the reconcile results adapted to the input model
	 */
    protected IReconcileResult[] convertToInputModel(IReconcileResult[] inputResults) {
        return inputResults;
    }

    /**
	 * Merges the two reconcile result arrays.
	 *
	 * @param results1 an array with reconcile results
	 * @param results2 an array with reconcile results
	 * @return an array with the merged reconcile results
	 */
    private IReconcileResult[] merge(IReconcileResult[] results1, IReconcileResult[] results2) {
        if (results1 == null) return results2;
        if (results2 == null) return results1;
        Collection collection = new ArrayList(Arrays.asList(results1));
        collection.addAll(Arrays.asList(results2));
        return (IReconcileResult[]) collection.toArray(new IReconcileResult[collection.size()]);
    }

    protected final boolean isCanceled() {
        return fProgressMonitor != null && fProgressMonitor.isCanceled();
    }

    public void setPreviousStep(IReconcileStep step) {
        Assert.isNotNull(step);
        Assert.isTrue(fPreviousStep == null);
        fPreviousStep = step;
    }

    public void setInputModel(IReconcilableModel inputModel) {
        fInputModel = inputModel;
        if (!isLastStep()) fNextStep.setInputModel(getModel());
    }

    /**
	 * Returns the reconcilable input model.
	 *
	 * @return the reconcilable input model.
	 */
    public IReconcilableModel getInputModel() {
        return fInputModel;
    }

    /**
	 * Returns the reconcilable model.
	 *
	 * @return the reconcilable model
	 */
    public abstract IReconcilableModel getModel();
}
