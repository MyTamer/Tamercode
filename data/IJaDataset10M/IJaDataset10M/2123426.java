package com.sun.tools.apt.main;

import java.io.*;
import java.util.Map;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.jvm.*;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.apt.comp.*;
import com.sun.tools.apt.util.Bark;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.tools.javac.parser.DocCommentScanner;

/**
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own
 *  risk.  This code and its internal interfaces are subject to change
 *  or deletion without notice.</b>
 */
@SuppressWarnings("deprecation")
public class AptJavaCompiler extends com.sun.tools.javac.main.JavaCompiler {

    /** The context key for the compiler. */
    protected static final Context.Key<AptJavaCompiler> compilerKey = new Context.Key<AptJavaCompiler>();

    /** Get the JavaCompiler instance for this context. */
    public static AptJavaCompiler instance(Context context) {
        AptJavaCompiler instance = context.get(compilerKey);
        if (instance == null) instance = new AptJavaCompiler(context);
        return instance;
    }

    java.util.Set<String> genSourceFileNames;

    java.util.Set<String> genClassFileNames;

    public java.util.Set<String> getSourceFileNames() {
        return genSourceFileNames;
    }

    /** List of names of generated class files.
     */
    public java.util.Set<String> getClassFileNames() {
        return genClassFileNames;
    }

    java.util.Set<java.io.File> aggregateGenFiles = java.util.Collections.emptySet();

    public java.util.Set<java.io.File> getAggregateGenFiles() {
        return aggregateGenFiles;
    }

    /** The bark to be used for error reporting.
     */
    Bark bark;

    /** The log to be used for error reporting.
     */
    Log log;

    /** The annotation framework
     */
    Apt apt;

    private static Context preRegister(Context context) {
        Bark.preRegister(context);
        if (context.get(JavaFileManager.class) == null) JavacFileManager.preRegister(context);
        return context;
    }

    /** Construct a new compiler from a shared context.
     */
    public AptJavaCompiler(Context context) {
        super(preRegister(context));
        context.put(compilerKey, this);
        apt = Apt.instance(context);
        ClassReader classReader = ClassReader.instance(context);
        classReader.preferSource = true;
        log = Log.instance(context);
        bark = Bark.instance(context);
        Options options = Options.instance(context);
        classOutput = options.get("-retrofit") == null;
        nocompile = options.get("-nocompile") != null;
        print = options.get("-print") != null;
        classesAsDecls = options.get("-XclassesAsDecls") != null;
        genSourceFileNames = new java.util.LinkedHashSet<String>();
        genClassFileNames = new java.util.LinkedHashSet<String>();
        lineDebugInfo = true;
    }

    /** Emit class files. This switch is always set, except for the first
     *  phase of retrofitting, where signatures are parsed.
     */
    public boolean classOutput;

    /** The internal printing annotation processor should be used.
     */
    public boolean print;

    /** Compilation should not be done after annotation processing.
     */
    public boolean nocompile;

    /** Are class files being treated as declarations
     */
    public boolean classesAsDecls;

    @Override
    public CharSequence readSource(JavaFileObject filename) {
        try {
            inputFiles.add(filename);
            boolean prev = bark.setDiagnosticsIgnored(true);
            try {
                return filename.getCharContent(false);
            } finally {
                bark.setDiagnosticsIgnored(prev);
            }
        } catch (IOException e) {
            bark.error(Position.NOPOS, "cant.read.file", filename);
            return null;
        }
    }

    @Override
    protected JCCompilationUnit parse(JavaFileObject filename, CharSequence content) {
        boolean prev = bark.setDiagnosticsIgnored(true);
        try {
            return super.parse(filename, content);
        } finally {
            bark.setDiagnosticsIgnored(prev);
        }
    }

    @Override
    protected boolean keepComments() {
        return true;
    }

    /** Track when the JavaCompiler has been used to compile something. */
    private boolean hasBeenUsed = false;

    /** Main method: compile a list of files, return all compiled classes
     *  @param filenames     The names of all files to be compiled.
     */
    public List<ClassSymbol> compile(List<String> filenames, Map<String, String> origOptions, ClassLoader aptCL, AnnotationProcessorFactory providedFactory, java.util.Set<Class<? extends AnnotationProcessorFactory>> productiveFactories, java.util.Set<java.io.File> aggregateGenFiles) throws Throwable {
        assert !hasBeenUsed : "attempt to reuse JavaCompiler";
        hasBeenUsed = true;
        this.aggregateGenFiles = aggregateGenFiles;
        long msec = System.currentTimeMillis();
        ListBuffer<ClassSymbol> classes = new ListBuffer<ClassSymbol>();
        try {
            JavacFileManager fm = (JavacFileManager) fileManager;
            ListBuffer<JCCompilationUnit> trees = new ListBuffer<JCCompilationUnit>();
            for (List<String> l = filenames; l.nonEmpty(); l = l.tail) {
                if (classesAsDecls) {
                    if (!l.head.endsWith(".java")) {
                        ClassSymbol cs = reader.enterClass(names.fromString(l.head));
                        try {
                            cs.complete();
                        } catch (Symbol.CompletionFailure cf) {
                            bark.aptError("CantFindClass", l);
                            continue;
                        }
                        classes.append(cs);
                        continue;
                    }
                }
                JavaFileObject fo = fm.getJavaFileObjectsFromStrings(List.of(l.head)).iterator().next();
                trees.append(parse(fo));
            }
            List<JCCompilationUnit> roots = trees.toList();
            if (errorCount() == 0) {
                boolean prev = bark.setDiagnosticsIgnored(true);
                try {
                    enter.main(roots);
                } finally {
                    bark.setDiagnosticsIgnored(prev);
                }
            }
            if (errorCount() == 0) {
                apt.main(roots, classes, origOptions, aptCL, providedFactory, productiveFactories);
                genSourceFileNames.addAll(apt.getSourceFileNames());
                genClassFileNames.addAll(apt.getClassFileNames());
            }
        } catch (Abort ex) {
        }
        if (verbose) log.printVerbose("total", Long.toString(System.currentTimeMillis() - msec));
        chk.reportDeferredDiagnostics();
        printCount("error", errorCount());
        printCount("warn", warningCount());
        return classes.toList();
    }
}
