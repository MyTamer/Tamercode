package de.uniwue.tm.cev.editor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.tools.stylemap.StyleMapEntry;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XmlCasSerializer;
import org.eclipse.core.internal.resources.Resource;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.xml.sax.SAXException;
import de.uniwue.tm.cev.CEVPlugin;
import de.uniwue.tm.cev.artifactViewer.ArtifactModifier;
import de.uniwue.tm.cev.data.CEVData;
import de.uniwue.tm.cev.data.CEVDocument;
import de.uniwue.tm.cev.data.ICEVAnnotationListener;
import de.uniwue.tm.cev.extension.ICEVArtifactViewer;
import de.uniwue.tm.cev.extension.ICEVArtifactViewerFactory;
import de.uniwue.tm.cev.extension.ICEVEditor;
import de.uniwue.tm.cev.extension.ICEVEditorFactory;
import de.uniwue.tm.cev.extension.ICEVSearchStrategy;
import de.uniwue.tm.cev.extension.ICEVView;
import de.uniwue.tm.cev.extension.ICEVViewFactory;
import de.uniwue.tm.cev.preferences.CEVPreferenceConstants;

/**
 * CEVViewer
 * 
 * Eclipse-Plugin zum Anzeigen, Analysieren und Editieren von CAS-Annotationen in HTML-Seiten
 * 
 * @author Marco Nehmeier, Peter Klügl
 */
public class CEVViewer extends MultiPageEditorPart implements IResourceChangeListener, ICEVAnnotationListener, MouseListener, SelectionListener {

    private CTabFolder[] folderArray;

    private CEVDocument casDocument;

    private CEVData activeCASData;

    private boolean dirty;

    private Map<Class<?>, ICEVView> views;

    private Map<Class<?>, ICEVEditor> editors;

    private Map<Class<?>, ICEVViewFactory> viewAdapter;

    private Map<Class<?>, ICEVEditorFactory> editorAdapter;

    private List<ICEVSearchStrategy> searchStrategies;

    private List<ICEVArtifactViewerFactory> artifactViewerFactories;

    private FileEditorInput inputFile;

    private Map<Integer, List<ICEVArtifactViewer>> casViews;

    private List<Type> initialVisibleTypes;

    /**
   * Erzeugt den CEVViewer
   */
    public CEVViewer() {
        super();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
        dirty = false;
        views = new HashMap<Class<?>, ICEVView>();
        editors = new HashMap<Class<?>, ICEVEditor>();
        viewAdapter = CEVPlugin.getViewAdapters();
        editorAdapter = CEVPlugin.getEditorAdapters();
        searchStrategies = CEVPlugin.getSearchStrategies();
        artifactViewerFactories = CEVPlugin.getArtifactViewerFactories();
    }

    /**
   * Erzeugt die einzelnen Seiten im MultiPageEditor
   */
    @Override
    protected void createPages() {
        if (casDocument == null) {
            return;
        }
        folderArray = new CTabFolder[casDocument.count()];
        casViews = new HashMap<Integer, List<ICEVArtifactViewer>>();
        int index = 0;
        for (CEVData each : casDocument.getCASData()) {
            CTabFolder folder = new CTabFolder(getContainer(), SWT.BOTTOM | SWT.FLAT);
            folder.addSelectionListener(this);
            addPage(index, folder);
            setPageText(index, each.getViewName());
            folderArray[index] = folder;
            ArrayList<ICEVArtifactViewer> artifactViewerListOfFolder = new ArrayList<ICEVArtifactViewer>();
            casViews.put(index, artifactViewerListOfFolder);
            for (ICEVArtifactViewerFactory eachFactory : artifactViewerFactories) {
                if (eachFactory.isAble(each.getCAS())) {
                    CTabItem tabItem = new CTabItem(folder, SWT.NONE);
                    try {
                        ICEVArtifactViewer artifactViewer = eachFactory.createArtifactViewer(this, tabItem, each);
                        artifactViewerListOfFolder.add(artifactViewer);
                    } catch (PartInitException e) {
                        CEVPlugin.error(e);
                    }
                }
            }
            folder.setSelection(0);
            index++;
        }
    }

    @Override
    public void dispose() {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        if (casViews != null) {
            for (List<ICEVArtifactViewer> eachList : casViews.values()) {
                for (ICEVArtifactViewer each : eachList) {
                    if (each != null) {
                        each.dispose();
                    }
                }
            }
            casViews.clear();
        }
        if (casDocument != null) {
            casDocument.dispose();
            casDocument = null;
        }
        super.dispose();
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        CAS newCas = casDocument.getMainCas();
        List<ArtifactModifier> modifiers = new ArrayList<ArtifactModifier>();
        for (Integer eachIndex : casViews.keySet()) {
            for (ICEVArtifactViewer each : casViews.get(eachIndex)) {
                if (each instanceof ArtifactModifier) {
                    modifiers.add((ArtifactModifier) each);
                }
            }
        }
        if (!modifiers.isEmpty()) {
            try {
                newCas = casDocument.createCas();
            } catch (ResourceInitializationException e) {
                CEVPlugin.error(e);
            } catch (InvalidXMLException e) {
                CEVPlugin.error(e);
            }
            org.apache.uima.util.CasCopier.copyCas(casDocument.getMainCas(), newCas, false);
            for (Integer eachIndex : casViews.keySet()) {
                for (ICEVArtifactViewer each : casViews.get(eachIndex)) {
                    if (each instanceof ArtifactModifier) {
                        ArtifactModifier am = (ArtifactModifier) each;
                        am.modifyCas(casDocument, newCas, eachIndex);
                    }
                }
            }
        }
        try {
            IFile iFile = ((FileEditorInput) getEditorInput()).getFile();
            File file = iFile.getLocation().toFile();
            XmlCasSerializer.serialize(newCas, new FileOutputStream(file));
            iFile.getParent().refreshLocal(Resource.DEPTH_INFINITE, new NullProgressMonitor());
        } catch (Exception e) {
            CEVPlugin.error(e);
        }
        setDirty(false);
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public void init(IEditorSite site, final IEditorInput editorInput) throws PartInitException {
        if (!(editorInput instanceof FileEditorInput)) throw new PartInitException("Invalid Input: Must be IFileEditorInput");
        super.init(site, editorInput);
        setPartName(((FileEditorInput) editorInput).getName());
        createCAS((FileEditorInput) editorInput);
        if (casDocument != null) {
            for (CEVData casData : casDocument.getCASData()) {
                casData.addAnnotationListener(CEVViewer.this);
            }
        }
        initialVisibleTypes = new ArrayList<Type>();
        if (casDocument != null) {
            Map<String, StyleMapEntry> styleMap = casDocument.getStyleMap();
            if (styleMap != null) {
                for (Entry<String, StyleMapEntry> each : styleMap.entrySet()) {
                    Type type = casDocument.getMainCas().getTypeSystem().getType(each.getKey());
                    if (type != null) {
                        initialVisibleTypes.add(type);
                    }
                }
            }
        }
    }

    private void closeEditor() {
        getSite().getPage().closeEditor(CEVViewer.this, false);
    }

    /**
   * CAS-File einlesen
   * 
   * @param inputFile
   *          Input-File
   * @throws PartInitException
   *           Exception
   */
    private void createCAS(FileEditorInput inputFile) throws PartInitException {
        this.inputFile = inputFile;
        try {
            IFile file = null;
            for (ICEVSearchStrategy each : searchStrategies) {
                try {
                    file = each.searchDescriptor(inputFile.getFile());
                } catch (Exception e) {
                }
                if (file != null) {
                    break;
                }
            }
            if (file != null) {
                casDocument = new CEVDocument(file, inputFile.getFile());
            }
        } catch (IllegalArgumentException e) {
            throw new PartInitException(e.getMessage());
        } catch (IOException e) {
            throw new PartInitException(e.getMessage());
        } catch (InvalidXMLException e) {
            throw new PartInitException(e.getMessage());
        } catch (SAXException e) {
            throw new PartInitException(e.getMessage());
        } catch (ResourceInitializationException e) {
            throw new PartInitException(e.getMessage());
        }
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    /**
   * Dirty setzen
   * 
   * @param dirty
   *          Dirty
   */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        firePropertyChange(PROP_DIRTY);
    }

    @Override
    protected void pageChange(int newPageIndex) {
        super.pageChange(newPageIndex);
        activeCASData = casDocument.getCASData(newPageIndex);
        for (List<ICEVArtifactViewer> eachList : casViews.values()) {
            for (ICEVArtifactViewer each : eachList) {
                each.viewChanged(activeCASData);
            }
        }
        for (ICEVView each : views.values()) {
            each.viewChanged(newPageIndex);
        }
        for (ICEVEditor each : editors.values()) {
            each.viewChanged(newPageIndex);
        }
    }

    public void resourceChanged(final IResourceChangeEvent event) {
        IPreferenceStore store = CEVPlugin.getDefault().getPreferenceStore();
        boolean reload = store.getBoolean(CEVPreferenceConstants.P_AUTO_REFRESH);
        if (reload) {
            IResourceDelta findInDelta = findInDelta(event.getDelta(), inputFile.getFile().getFullPath());
            if (findInDelta != null) {
                final CEVViewer t = this;
                Display display = Display.getCurrent();
                if (display == null) {
                    display = getEditorSite().getWorkbenchWindow().getShell().getDisplay();
                    if (display == null) {
                        display = getActiveEditor().getSite().getShell().getDisplay();
                    }
                }
                display.asyncExec(new Runnable() {

                    public void run() {
                        int page = getActivePage();
                        if (casDocument != null) {
                            for (CEVData casData : casDocument.getCASData()) {
                                casData.removeAnnotationListener(t);
                            }
                        }
                        try {
                            init(getEditorSite(), inputFile);
                        } catch (PartInitException e) {
                            CEVPlugin.error(e);
                        }
                        for (ICEVView each : views.values()) {
                            each.casChanged(casDocument);
                        }
                        for (ICEVEditor each : editors.values()) {
                            each.casChanged(casDocument);
                        }
                        pageChange(page);
                    }
                });
            }
        }
    }

    private IResourceDelta findInDelta(IResourceDelta delta, IPath target) {
        if (delta.getFullPath().equals(target)) {
            return delta;
        } else {
            for (IResourceDelta each : delta.getAffectedChildren(IResourceDelta.CHANGED)) {
                IResourceDelta findInDelta = findInDelta(each, target);
                if (findInDelta != null) {
                    return findInDelta;
                }
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class required) {
        if (viewAdapter.containsKey(required)) {
            ICEVView view = null;
            if (views.containsKey(required)) {
                view = views.get(required);
            } else {
                ICEVViewFactory viewFactory = viewAdapter.get(required);
                view = viewFactory.createView(this, casDocument, getActivePage());
                if (view != null) {
                    views.put(required, view);
                }
            }
            return view;
        }
        if (editorAdapter.containsKey(required)) {
            ICEVEditor editor = null;
            if (editors.containsKey(required)) {
                editor = editors.get(required);
            } else {
                ICEVEditorFactory editorFactory = editorAdapter.get(required);
                editor = editorFactory.createEditor(this, casDocument, getActivePage());
                if (editor != null) {
                    editors.put(required, editor);
                }
            }
            return editor;
        }
        return super.getAdapter(required);
    }

    public void annotationStateChanged(Type type) {
        int activePage = getActivePage();
        int selectionIndex = folderArray[activePage].getSelectionIndex();
        ICEVArtifactViewer viewer = getSelectedViewer(activePage, selectionIndex);
        viewer.annotationStateChanged(type);
    }

    public void annotationStateChanged(AnnotationFS annot) {
        int activePage = getActivePage();
        int selectionIndex = folderArray[activePage].getSelectionIndex();
        ICEVArtifactViewer viewer = getSelectedViewer(activePage, selectionIndex);
        viewer.annotationStateChanged();
    }

    private ICEVArtifactViewer getSelectedViewer(int folderIndex, int tabIndex) {
        List<ICEVArtifactViewer> list = casViews.get(folderIndex);
        ICEVArtifactViewer viewer = list.get(tabIndex);
        return viewer;
    }

    public void mouseDoubleClick(MouseEvent e) {
    }

    public void mouseDown(MouseEvent event) {
        int activePage = getActivePage();
        int selectionIndex = folderArray[activePage].getSelectionIndex();
        ICEVArtifactViewer viewer = getSelectedViewer(activePage, selectionIndex);
        if (event.button == 3) {
        } else {
            try {
                int offsetAtLocation = viewer.getOffsetAtLocation(new Point(event.x, event.y));
                if (offsetAtLocation >= 0) {
                    showSelection(offsetAtLocation);
                }
            } catch (Exception e) {
            }
        }
    }

    public void mouseUp(MouseEvent event) {
        int activePage = getActivePage();
        int selectionIndex = folderArray[activePage].getSelectionIndex();
        ICEVArtifactViewer viewer = getSelectedViewer(activePage, selectionIndex);
        IPreferenceStore store = CEVPlugin.getDefault().getPreferenceStore();
        boolean selectOnly = store.getBoolean(CEVPreferenceConstants.P_SELECT_ONLY);
        if (event.button == 1 && (selectOnly || (event.stateMask & SWT.ALT) != 0)) {
            Point selection = viewer.getViewerSelectionRange();
            for (ICEVEditor each : editors.values()) {
                each.textSelected(selection.x, selection.x + selection.y);
            }
        }
    }

    public void colorChanged(Type type) {
        annotationStateChanged(type);
    }

    public void annotationsAdded(List<AnnotationFS> annots) {
        setDirty(true);
    }

    public void annotationsRemoved(List<AnnotationFS> annots) {
        setDirty(true);
    }

    /**
   * Zu einer Annotation springen
   * 
   * @param annot
   *          Annotation
   */
    public void moveToAnnotation(AnnotationFS annot) {
        int activePage = getActivePage();
        int selectionIndex = folderArray[activePage].getSelectionIndex();
        ICEVArtifactViewer viewer = getSelectedViewer(activePage, selectionIndex);
        viewer.moveToAnnotation(annot);
    }

    /**
   * Selektion auf der SelektionPage anzeigen
   * 
   * @param pos
   *          Offset
   */
    public void showSelection(int pos) {
        for (ICEVView each : views.values()) {
            each.newSelection(pos);
        }
    }

    public void widgetDefaultSelected(SelectionEvent e) {
    }

    public void widgetSelected(SelectionEvent e) {
        int activePage = getActivePage();
        int selectionIndex = folderArray[activePage].getSelectionIndex();
        ICEVArtifactViewer viewer = getSelectedViewer(activePage, selectionIndex);
        viewer.viewerWidgetSelected();
    }

    public void handleViewerPropertyChange(int propertyId) {
        handlePropertyChange(propertyId);
    }

    public List<Type> getInitialVisibleTypes() {
        return initialVisibleTypes;
    }
}
