package org.eclipse.emf.edit.ui.dnd;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

/**
 * This is an implementation of {@link DragSourceListener}.
 * It allows the selection in effect at the start of the drag and drop interaction to be recorded,
 * which is especially important for a drag and drop interaction within a single view.
 * This is how one of these adapters is typically hooked up:
 * <pre>
 *   viewer.addDragSupport
 *     (DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK, 
 *      new Transfer [] { LocalTransfer.getInstance() },
 *      ViewerDragAdapter(viewer));
 * </pre>
 * Doing so simply allows a drag operation to be initiated from the viewer
 * such that the viewer's selection is transferred to the drop target.
 * See {@link EditingDomainViewerDropAdapter} and {@link LocalTransfer} for more details.
 */
public class ViewerDragAdapter implements DragSourceListener {

    /**
   * This keeps track of the viewer to which we are listening.
   */
    protected Viewer viewer;

    /**
   * This keeps track of the selection that is in effect at the start of the drag operation
   */
    protected ISelection selection;

    /**
   * This creates an instance for the given viewer.
   */
    public ViewerDragAdapter(Viewer viewer) {
        super();
        this.viewer = viewer;
    }

    /**
   * This is called when dragging is initiated; it records the {@link #selection} of {@link #viewer}.
   */
    public void dragStart(DragSourceEvent event) {
        selection = viewer.getSelection();
    }

    /**
   * This is called when dragging is completed; it forgets the {@link #selection}.
   */
    public void dragFinished(DragSourceEvent event) {
        selection = null;
        LocalTransfer.getInstance().javaToNative(null, null);
    }

    /**
   * This is called to transfer the data.
   */
    public void dragSetData(DragSourceEvent event) {
        if (LocalTransfer.getInstance().isSupportedType(event.dataType)) {
            event.data = selection;
        }
    }
}
