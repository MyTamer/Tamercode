package org.eclipse.emf.common.ui.action;

import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.emf.common.util.UniqueEList;

/**
 * Action wrapper for a {@link ViewerFilter}  that can be used in multiple viewers. 
 * @since 2.2.0
 */
public abstract class ViewerFilterAction extends Action {

    protected List<Viewer> viewers;

    protected ViewerFilter viewerFilter;

    public ViewerFilterAction(String text, int style) {
        super(text, style);
        setId(Integer.toString(hashCode()));
    }

    public void addViewer(Viewer viewer) {
        if (viewer instanceof StructuredViewer) {
            if (viewers == null) {
                viewers = new UniqueEList.FastCompare<Viewer>();
            }
            if (viewers.add(viewer)) {
                if (viewerFilter == null) {
                    viewerFilter = new ViewerFilter() {

                        @Override
                        public boolean select(Viewer viewer, Object parentElement, Object element) {
                            return ViewerFilterAction.this.select(viewer, parentElement, element);
                        }
                    };
                }
                ((StructuredViewer) viewer).addFilter(viewerFilter);
            }
        }
    }

    public void removeViewer(Viewer viewer) {
        if (viewers != null) {
            if (viewers.remove(viewer)) {
                if (!viewer.getControl().isDisposed()) {
                    ((StructuredViewer) viewer).removeFilter(viewerFilter);
                }
            }
        }
    }

    public void dispose() {
        if (viewers != null) {
            for (Viewer viewer : viewers) {
                if (!viewer.getControl().isDisposed()) {
                    ((StructuredViewer) viewer).removeFilter(viewerFilter);
                }
            }
            viewers.clear();
            viewers = null;
        }
        viewerFilter = null;
    }

    @Override
    public void setChecked(boolean checked) {
        boolean wasChecked = isChecked();
        super.setChecked(checked);
        if (wasChecked != checked) {
            refreshViewers();
        }
    }

    protected void refreshViewers() {
        if (viewers != null) {
            for (Viewer viewer : viewers) {
                if (!viewer.getControl().isDisposed()) {
                    viewer.refresh();
                }
            }
        }
    }

    public abstract boolean select(Viewer viewer, Object parentElement, Object element);
}
