package hu.cubussapiens.modembed.model.editor.internal;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * @author balazs.grill
 *
 */
public class WrappedSelectionProvider implements ISelectionProvider {

    private ISelectionProvider internalSelectionProvider;

    private ISelectionChangedListener internalListener = new ISelectionChangedListener() {

        @Override
        public void selectionChanged(SelectionChangedEvent event) {
            fireEvent(event.getSelection());
        }
    };

    public void setInternalSelectionProvider(ISelectionProvider selectionProvider) {
        if (internalSelectionProvider != null) {
            internalSelectionProvider.removeSelectionChangedListener(internalListener);
        }
        this.internalSelectionProvider = selectionProvider;
        if (this.internalSelectionProvider != null) {
            this.internalSelectionProvider.addSelectionChangedListener(internalListener);
        }
    }

    private final Set<ISelectionChangedListener> listeners = new HashSet<ISelectionChangedListener>();

    private void fireEvent(ISelection selection) {
        for (ISelectionChangedListener l : listeners) l.selectionChanged(new SelectionChangedEvent(this, selection));
    }

    @Override
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        listeners.add(listener);
    }

    @Override
    public ISelection getSelection() {
        if (internalSelectionProvider != null) return internalSelectionProvider.getSelection();
        return new StructuredSelection();
    }

    @Override
    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void setSelection(ISelection selection) {
        if (internalSelectionProvider != null) internalSelectionProvider.setSelection(selection);
    }
}
