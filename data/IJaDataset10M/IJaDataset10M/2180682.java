package org.openrdf.query.algebra.evaluation.iterator;

import java.util.List;
import info.aduna.iteration.CloseableIteration;
import info.aduna.iteration.CloseableIterationBase;
import info.aduna.iteration.Iteration;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.algebra.MultiProjection;
import org.openrdf.query.algebra.ProjectionElemList;

public class MultiProjectionIterator extends CloseableIterationBase<BindingSet, QueryEvaluationException> {

    private final List<ProjectionElemList> projections;

    private final Iteration<BindingSet, QueryEvaluationException> iter;

    private final BindingSet parentBindings;

    private BindingSet currentBindings;

    private int nextProjectionIdx;

    public MultiProjectionIterator(MultiProjection multiProjection, CloseableIteration<BindingSet, QueryEvaluationException> iter, BindingSet bindings) {
        this.projections = multiProjection.getProjections();
        this.iter = iter;
        this.parentBindings = bindings;
    }

    public boolean hasNext() throws QueryEvaluationException {
        return currentBindings != null && nextProjectionIdx < projections.size() || iter.hasNext();
    }

    public BindingSet next() throws QueryEvaluationException {
        if (currentBindings == null || nextProjectionIdx >= projections.size()) {
            currentBindings = iter.next();
            nextProjectionIdx = 0;
        }
        ProjectionElemList nextProjection = projections.get(nextProjectionIdx++);
        return ProjectionIterator.project(nextProjection, currentBindings, parentBindings);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void handleClose() throws QueryEvaluationException {
        nextProjectionIdx = projections.size();
        currentBindings = null;
        super.handleClose();
    }
}
