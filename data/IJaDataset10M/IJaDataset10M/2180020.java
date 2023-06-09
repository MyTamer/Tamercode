package org.eclipsetrader.ui.internal.navigator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipsetrader.core.instruments.ISecurity;
import org.eclipsetrader.core.repositories.IRepository;
import org.eclipsetrader.core.views.IViewItem;
import org.eclipsetrader.core.views.IViewItemVisitor;
import org.eclipsetrader.core.views.IWatchList;
import org.eclipsetrader.ui.UIConstants;
import org.eclipsetrader.ui.internal.UIActivator;
import org.eclipsetrader.ui.internal.securities.SecurityObjectTransfer;
import org.eclipsetrader.ui.navigator.INavigatorContentGroup;

public class Navigator extends ViewPart {

    private TreeViewer viewer;

    private Action collapseAllAction;

    private Action expandAllAction;

    public Navigator() {
    }

    @Override
    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site, memento);
        ImageRegistry imageRegistry = UIActivator.getDefault().getImageRegistry();
        collapseAllAction = new Action("Collapse All", imageRegistry.getDescriptor(UIConstants.COLLAPSEALL_ICON)) {

            @Override
            public void run() {
                viewer.collapseAll();
            }
        };
        expandAllAction = new Action("Expand All", imageRegistry.getDescriptor(UIConstants.EXPANDALL_ICON)) {

            @Override
            public void run() {
                viewer.expandAll();
            }
        };
        IToolBarManager toolBarManager = site.getActionBars().getToolBarManager();
        toolBarManager.add(expandAllAction);
        toolBarManager.add(collapseAllAction);
        site.getActionBars().updateActionBars();
    }

    @Override
    public void createPartControl(Composite parent) {
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
        viewer.setUseHashlookup(true);
        viewer.setLabelProvider(new DecoratingLabelProvider(new NavigatorLabelProvider(), null));
        viewer.setContentProvider(new NavigatorContentProvider());
        viewer.setSorter(new ViewerSorter() {

            @Override
            public int category(Object element) {
                if (element instanceof IAdaptable) {
                    if (((IAdaptable) element).getAdapter(ISecurity.class) != null) return 1;
                    if (((IAdaptable) element).getAdapter(IWatchList.class) != null) return 2;
                    if (((IAdaptable) element).getAdapter(IRepository.class) != null) return 3;
                }
                return 0;
            }
        });
        DragSource dragSource = new DragSource(viewer.getControl(), DND.DROP_COPY | DND.DROP_MOVE);
        dragSource.setTransfer(new Transfer[] { SecurityObjectTransfer.getInstance() });
        dragSource.addDragListener(new DragSourceListener() {

            public void dragStart(DragSourceEvent event) {
                event.doit = getSelectedObject(viewer.getSelection()).length != 0;
            }

            public void dragSetData(DragSourceEvent event) {
                if (SecurityObjectTransfer.getInstance().isSupportedType(event.dataType)) event.data = getSelectedObject(viewer.getSelection());
            }

            public void dragFinished(DragSourceEvent event) {
            }
        });
        MenuManager menuMgr = new MenuManager("#popupMenu", "popupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager menuManager) {
                menuManager.add(new Separator("group.new"));
                menuManager.add(new GroupMarker("group.goto"));
                menuManager.add(new Separator("group.open"));
                menuManager.add(new GroupMarker("group.openWith"));
                menuManager.add(new Separator("group.show"));
                menuManager.add(new Separator("group.edit"));
                menuManager.add(new GroupMarker("group.reorganize"));
                menuManager.add(new GroupMarker("group.port"));
                menuManager.add(new Separator("group.generate"));
                menuManager.add(new Separator("group.search"));
                menuManager.add(new Separator("group.build"));
                menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
                menuManager.add(new Separator("group.properties"));
                menuManager.appendToGroup("group.show", new Action("Expand All") {

                    @Override
                    public void run() {
                        IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
                        for (Iterator<?> iter = selection.iterator(); iter.hasNext(); ) viewer.expandToLevel(iter.next(), TreeViewer.ALL_LEVELS);
                    }
                });
            }
        });
        viewer.getControl().setMenu(menuMgr.createContextMenu(viewer.getControl()));
        getSite().registerContextMenu(menuMgr, getSite().getSelectionProvider());
        viewer.addOpenListener(new IOpenListener() {

            public void open(OpenEvent event) {
                try {
                    IHandlerService service = (IHandlerService) getSite().getService(IHandlerService.class);
                    service.executeCommand("org.eclipse.ui.file.open", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        getViewSite().setSelectionProvider(viewer);
        NavigatorView view = new NavigatorView();
        view.setContentProviders(new IStructuredContentProvider[] { new SecuritiesContentProvider(), new WatchListsContentProvider() });
        view.setGroups(new INavigatorContentGroup[] { new InstrumentTypeGroup(), new MarketGroup() });
        view.update();
        viewer.setInput(view);
    }

    @Override
    public void setFocus() {
        if (!viewer.getControl().isDisposed()) viewer.getControl().setFocus();
    }

    @Override
    public void dispose() {
        NavigatorView view = (NavigatorView) viewer.getInput();
        if (view != null) view.dispose();
        super.dispose();
    }

    protected IAdaptable[] getSelectedObject(ISelection selection) {
        final Set<ISecurity> list = new HashSet<ISecurity>();
        if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
            for (Object o : ((IStructuredSelection) selection).toArray()) {
                if (o instanceof NavigatorViewItem) {
                    ((NavigatorViewItem) o).accept(new IViewItemVisitor() {

                        public boolean visit(IViewItem viewItem) {
                            ISecurity reference = (ISecurity) viewItem.getAdapter(ISecurity.class);
                            if (reference != null) list.add(reference);
                            return true;
                        }
                    });
                }
            }
        }
        return list.toArray(new IAdaptable[list.size()]);
    }
}
