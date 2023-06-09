package recoder.io;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import recoder.AbstractService;
import recoder.ParserException;
import recoder.ServiceConfiguration;
import recoder.convenience.Naming;
import recoder.java.CompilationUnit;
import recoder.java.Identifier;
import recoder.java.NonTerminalProgramElement;
import recoder.java.PackageSpecification;
import recoder.java.PrettyPrinter;
import recoder.java.ProgramElement;
import recoder.java.declaration.TypeDeclaration;
import recoder.java.reference.PackageReference;
import recoder.service.AttachChange;
import recoder.service.ChangeHistory;
import recoder.service.ChangeHistoryEvent;
import recoder.service.ChangeHistoryListener;
import recoder.service.DetachChange;
import recoder.service.TreeChange;
import recoder.util.Debug;
import recoder.util.ProgressListener;
import recoder.util.ProgressListenerManager;

/**
 * @author RN
 * @author AL
 */
public class DefaultSourceFileRepository extends AbstractService implements SourceFileRepository, ChangeHistoryListener, PropertyChangeListener {

    private static final boolean DEBUG = false;

    /**
     * Cache: data location to compilation units.
     */
    private final Map<DataLocation, CompilationUnit> location2cu = new HashMap<DataLocation, CompilationUnit>();

    /**
     * Set of units that have been changed and have to be rewritten.
     */
    private final Set<CompilationUnit> changedUnits = new HashSet<CompilationUnit>();

    /**
     * Set of units that are obsolete and should be deleted.
     */
    private final Set<DataLocation> deleteUnits = new HashSet<DataLocation>();

    /**
     * The change history service.
     */
    private ChangeHistory changeHistory;

    /**
     * Cached search path list.
     */
    private PathList searchPathList;

    /**
     * Cached output path.
     */
    private File outputPath;

    /**
     * Progress listener management.
     */
    ProgressListenerManager listeners = new ProgressListenerManager(this);

    /**
     * NAI
     */
    private Properties locationSpecificVersion;

    /**
     * @param config
     *            the configuration this services becomes part of.
     */
    public DefaultSourceFileRepository(ServiceConfiguration config) {
        super(config);
    }

    public void initialize(ServiceConfiguration cfg) {
        super.initialize(cfg);
        changeHistory = cfg.getChangeHistory();
        changeHistory.addChangeHistoryListener(this);
        ProjectSettings settings = cfg.getProjectSettings();
        settings.addPropertyChangeListener(this);
        searchPathList = settings.getSearchPathList();
        outputPath = new File(settings.getProperty(PropertyNames.OUTPUT_PATH));
        locationSpecificVersion = settings.getLocationSpecificVersionProperties();
    }

    protected final PathList getSearchPathList() {
        return searchPathList;
    }

    protected final File getOutputPath() {
        return outputPath;
    }

    public void addProgressListener(ProgressListener l) {
        listeners.addProgressListener(l);
    }

    public void removeProgressListener(ProgressListener l) {
        listeners.removeProgressListener(l);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String changedProp = evt.getPropertyName();
        if (changedProp.equals(PropertyNames.INPUT_PATH)) {
            searchPathList = serviceConfiguration.getProjectSettings().getSearchPathList();
        } else if (changedProp.equals(PropertyNames.OUTPUT_PATH)) {
            outputPath = new File(serviceConfiguration.getProjectSettings().getProperty(PropertyNames.OUTPUT_PATH));
        }
    }

    private void deregister(CompilationUnit cu) {
        DataLocation loc = cu.getDataLocation();
        if (loc != null) {
            if (location2cu.get(loc) == cu) {
                location2cu.remove(loc);
                changedUnits.remove(cu);
                if (DEBUG) Debug.log("Deregistering " + loc);
                DataLocation orig = cu.getOriginalDataLocation();
                if (!loc.equals(orig)) {
                    deleteUnits.add(loc);
                }
            }
        }
    }

    private void register(CompilationUnit cu) {
        DataLocation loc = cu.getDataLocation();
        if (loc == null) {
            changedUnits.add(cu);
            loc = createDataLocation(cu);
            cu.setDataLocation(loc);
        }
        if (location2cu.get(loc) != cu) {
            if (DEBUG) Debug.log("Registering " + loc);
            deleteUnits.remove(loc);
            location2cu.put(loc, cu);
        }
    }

    private boolean isPartOfUnitName(ProgramElement pe) {
        if (pe instanceof Identifier || pe instanceof PackageReference) {
            return isPartOfUnitName(pe.getASTParent());
        }
        if (pe instanceof PackageSpecification) {
            return true;
        }
        if (pe instanceof TypeDeclaration) {
            NonTerminalProgramElement parent = pe.getASTParent();
            return (parent instanceof CompilationUnit) && (((CompilationUnit) parent).getPrimaryTypeDeclaration() == pe);
        }
        return false;
    }

    public void modelChanged(ChangeHistoryEvent changes) {
        List<TreeChange> changed = changes.getChanges();
        for (int i = changed.size() - 1; i >= 0; i -= 1) {
            TreeChange tc = changed.get(i);
            ProgramElement pe = tc.getChangeRoot();
            CompilationUnit cu = tc.getCompilationUnit();
            if (pe == cu) {
                if (tc instanceof AttachChange) {
                    register(cu);
                } else if (tc instanceof DetachChange) {
                    deregister(cu);
                }
            } else {
                if (isPartOfUnitName(pe)) {
                    DataLocation loc = cu.getDataLocation();
                    DataLocation loc2 = createDataLocation(cu);
                    if (!loc.equals(loc2)) {
                        deregister(cu);
                        cu.setDataLocation(loc2);
                        register(cu);
                    }
                }
                changedUnits.add(cu);
            }
            if (cu == null) {
                Debug.log("Null Unit changed in " + tc);
            }
        }
    }

    /**
     * Searches for the location of the source file for the given class.
     * 
     * @param classname
     *            the name of the class for which the source file should be
     *            looked up.
     */
    public DataLocation findSourceFile(String classname) {
        String file = Naming.dot(Naming.makeFilename(classname), "java");
        return getSearchPathList().find(file);
    }

    protected CompilationUnit getCompilationUnitFromLocation(DataLocation loc) {
        Debug.assertNonnull(loc, "Null location for compilation unit");
        CompilationUnit result = location2cu.get(loc);
        if (result != null) {
            return result;
        }
        try {
            String locationStr = loc.toString();
            String version = null;
            Iterator<Object> keys = locationSpecificVersion.keySet().iterator();
            while (keys.hasNext()) {
                String location = (String) keys.next();
                if (locationStr.startsWith(location)) {
                    version = (String) locationSpecificVersion.get(location);
                }
            }
            Reader in;
            if (!loc.hasReaderSupport() || (in = loc.getReader()) == null) {
                Debug.error("Location of source file provides no reader");
                return null;
            }
            if (version != null) result = serviceConfiguration.getProgramFactory().parseCompilationUnit(in, version); else result = serviceConfiguration.getProgramFactory().parseCompilationUnit(in);
            in.close();
            loc.readerClosed();
            result.setDataLocation(loc);
            location2cu.put(loc, result);
            changeHistory.attached(result);
        } catch (Throwable e) {
            getServiceConfiguration().getProjectSettings().getErrorHandler().reportError(new Exception(e.getClass().getSimpleName() + " occured while parsing " + loc + "\n" + e.getMessage()));
        }
        return result;
    }

    public CompilationUnit getCompilationUnitFromFile(String filename) {
        Debug.assertNonnull(filename);
        File f = new File(filename);
        DataLocation loc = null;
        if (f.isFile() && f.isAbsolute()) {
            String newfilename = getSearchPathList().getRelativeName(filename);
            if (newfilename.equals(filename)) {
                loc = new DataFileLocation(f);
            } else {
                loc = getSearchPathList().find(newfilename);
            }
        } else {
            loc = getSearchPathList().find(filename);
        }
        return loc != null ? getCompilationUnitFromLocation(loc) : null;
    }

    public List<CompilationUnit> getCompilationUnitsFromFiles(String[] filenames) {
        Debug.assertNonnull(filenames);
        List<CompilationUnit> res = new ArrayList<CompilationUnit>();
        listeners.fireProgressEvent(0, filenames.length, "Importing Source Files");
        for (int i = 0; i < filenames.length; i += 1) {
            listeners.fireProgressEvent(i, "Parsing " + filenames[i].toString());
            CompilationUnit cu = getCompilationUnitFromFile(filenames[i]);
            if (cu != null) {
                res.add(cu);
            }
        }
        listeners.fireProgressEvent(filenames.length);
        return res;
    }

    public CompilationUnit getCompilationUnit(String classname) {
        DataLocation loc = findSourceFile(classname);
        if (loc == null || loc instanceof ArchiveDataLocation) {
            return null;
        }
        return getCompilationUnitFromLocation(loc);
    }

    public List<CompilationUnit> getCompilationUnits() {
        changeHistory.updateModel();
        return getKnownCompilationUnits();
    }

    public List<CompilationUnit> getKnownCompilationUnits() {
        int n = location2cu.size();
        List<CompilationUnit> res = new ArrayList<CompilationUnit>(n);
        for (CompilationUnit cu : location2cu.values()) {
            res.add(cu);
        }
        return res;
    }

    public static final FilenameFilter JAVA_FILENAME_FILTER = new FilenameFilter() {

        public boolean accept(File dir, String name) {
            return name.endsWith(".java");
        }
    };

    public List<CompilationUnit> getAllCompilationUnitsFromPath() throws ParserException {
        return getAllCompilationUnitsFromPath(JAVA_FILENAME_FILTER);
    }

    public List<CompilationUnit> getAllCompilationUnitsFromPath(FilenameFilter filter) {
        DataLocation[] locations = getSearchPathList().findAll(filter, getServiceConfiguration().getProjectSettings().getErrorHandler());
        List<CompilationUnit> res = new ArrayList<CompilationUnit>(locations.length);
        listeners.fireProgressEvent(0, res.size(), "Importing Source Files From Path");
        for (int i = 0; i < locations.length; i++) {
            listeners.fireProgressEvent(i, "Parsing " + locations[i]);
            CompilationUnit cu = getCompilationUnitFromLocation(locations[i]);
            res.add(cu);
        }
        listeners.fireProgressEvent(locations.length);
        return res;
    }

    public boolean isUpToDate(CompilationUnit cu) {
        Debug.assertNonnull(cu);
        if (cu.getDataLocation() == null) {
            return false;
        }
        return !changedUnits.contains(cu);
    }

    protected DataLocation createDataLocation(CompilationUnit cu) {
        String filename = Naming.toCanonicalFilename(cu);
        File f = new File(getOutputPath(), filename);
        return new DataFileLocation(f);
    }

    private void printUnit(CompilationUnit cu) throws IOException {
        DataLocation location = cu.getDataLocation();
        if (location == null || cu.getOriginalDataLocation() == location) {
            if (location != null) {
                location2cu.remove(location);
            }
            location = createDataLocation(cu);
            cu.setDataLocation(location);
            location2cu.put(location, cu);
        }
        if (!location.isWritable()) {
            throw new IOException("Data location for " + location + " is not writable");
        }
        if (location instanceof DataFileLocation) {
            File f = ((DataFileLocation) location).getFile();
            File parent = new File(f.getParent());
            if (!parent.exists()) {
                parent.mkdirs();
            }
        }
        Writer w = location.getWriter();
        PrettyPrinter pprinter = serviceConfiguration.getProgramFactory().getPrettyPrinter(w);
        cu.accept(pprinter);
        w.flush();
        w.close();
        location.writerClosed();
    }

    public void print(CompilationUnit cu) throws IOException {
        Debug.assertNonnull(cu);
        printUnit(cu);
        changedUnits.remove(cu);
    }

    public void printAll(boolean always) throws IOException {
        changeHistory.updateModel();
        int size = always ? location2cu.size() : changedUnits.size();
        listeners.fireProgressEvent(0, size, "Exporting Source Files");
        CompilationUnit[] units = new CompilationUnit[size];
        int j = 0;
        for (CompilationUnit cu : always ? location2cu.values() : changedUnits) {
            units[j++] = cu;
        }
        if (DEBUG) {
            Debug.log("printing...");
        }
        for (int i = 0; i < size; i += 1) {
            if (DEBUG) {
                Debug.log("units[i].getName()");
            }
            printUnit(units[i]);
            listeners.fireProgressEvent(i + 1, units[i]);
        }
        changedUnits.clear();
    }

    /**
     * Deletes all superfluous (renamed, detached) compilation unit files. Does
     * not remove source files from other sources.
     */
    public void cleanUp() {
        for (DataLocation loc : deleteUnits) {
            if (loc instanceof DataFileLocation) {
                File f = ((DataFileLocation) loc).getFile();
                f.delete();
            }
        }
        deleteUnits.clear();
    }

    public String information() {
        return "" + location2cu.size() + " compilation units (" + changedUnits.size() + " currently changed)";
    }
}
