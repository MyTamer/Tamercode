package org.jmlspecs.openjml.eclipse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblem;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;
import org.jmlspecs.annotation.Nullable;

/**
 * This is a class that implements the eclipse IProblem interface
 * tailored for the JmlParser.  It is mostly present to provide
 * an interface between the plugin and the internal class that it
 * extends, but it also provides some utility functions to produce 
 * and report errors and warnings.  It also adds some additional
 * information to its parent class to hold information relevant to
 * line and print oriented problem reporting.
 */
public class JmlEclipseProblem extends DefaultProblem {

    /** This holds a reference to the source text as known
	 *  at the time the problem was created, or null if
	 *  the source text is not available.
	 */
    @Nullable
    public String sourceText;

    /**
	 * Holds the 1-based character position of the beginning of the line indicated
	 * by the line argument (and on which startPosition resides); the
	 * value is -1 if the lineStart information is not available.
	 */
    public int lineStart;

    /**
	 * Holds the 1-based character position of the end of the line indicated
	 * by the line argument (and on which startPosition resides); this is
	 * the character position of the line termination or end of file;
	 * the value is -1 if the line end information is not available.
	 */
    public int lineEnd;

    /** The severity of the problem.  This duplicates a private field in the 
	 * super class, but we have to because isError() just returns the opposite of
	 * isWarning(), and we cannot get at other severities.
	 */
    public final int severity;

    /**
	 * Constructor for a new problem.
	 * @param ifile    The workspace IFile in which the offending source text is located, if any (otherwise null)
	 * @param message  The problem message
	 * @param id       A numerical id for the problem
	 * @param severity The severity of the problem, using one of the constants listed above (e.g. Error, Warning)
	 * @param startPosition The character position within the line that begins the offending region
	 * @param endPosition   The character position within the line that ends (not one after) the offending region
	 * @param line     The line within the source text containing the offending source text
	 * @param source   A reference to the source text, or null
	 * @param lineStart The character position of the beginning of the stated line in the IFile or source text, or -1 if not known
	 * @param lineEnd   The character position of the end of the stated line (that is, where the line termination character is) in the IFile or source text, or -1 if not known
	 */
    public JmlEclipseProblem(IFile ifile, String message, int id, int severity, int startPosition, int endPosition, int line, String source, int lineStart, int lineEnd) {
        super(ifile == null ? null : ifile.getFullPath().toString().toCharArray(), message, id, null, severity, startPosition, endPosition, line, startPosition - lineStart);
        this.sourceText = source;
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.severity = severity;
    }

    /** An identifier for the JML category of problems.  This is a randomly chosen
	 * number that needs to be different than the numbers defined in
	 *  org.eclipse.jdt.core.compiler.CategorizedProblem.
	 */
    private final int JML_CAT = 15423;

    /** An identifier for the JML category of problems.  This is a randomly chosen
	 * number that needs to be different than the numbers defined in
	 *  org.eclipse.jdt.core.compiler.CategorizedProblem.
	 */
    public int getCategoryID() {
        return JML_CAT;
    }

    /**
	 * A utility method to obtain a reference to the source text for
	 * an ICompilationUnit, or null if it could not be obtained.
	 * @param icu The ICompilationUnit
	 * @return The source text for the ICompilationUnit
	 */
    private static String getSource(ICompilationUnit icu) {
        if (icu == null) return null;
        try {
            return icu.getSource();
        } catch (Exception e) {
            return null;
        }
    }

    /**
	 * Converts the given IProblem into an instance of JmlEclipseProblem,
	 * using additional information from the given ICompilationUnit.
	 * The line start and end information is not available.
	 * @param p  The base problem
	 * @param icu The ICompilationUnit from which to get position information
	 */
    public JmlEclipseProblem(IProblem p, ICompilationUnit icu) {
        this((IFile) icu.getResource(), p.getMessage(), p.getID(), p.isError() ? ProblemSeverities.Error : p.isWarning() ? ProblemSeverities.Warning : -1, p.getSourceStart(), p.getSourceEnd(), p.getSourceLineNumber(), getSource(icu), -1, -1);
    }

    /**
	 * A utility method that prints the given problem on the given PrintStream
	 * @param out the stream on which to write the problem
	 * @param p   The IProblem to print
	 */
    public static void printProblem(PrintStream out, IProblem p) {
        char[] filenameAsChars = p.getOriginatingFileName();
        String filename = null;
        IResource resource = null;
        if (filenameAsChars != null) {
            filename = String.valueOf(p.getOriginatingFileName());
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IWorkspaceRoot root = workspace.getRoot();
            resource = root.findMember(filename);
            if (resource != null) {
                filename = resource.getLocation().toOSString();
            }
        }
        int line = p.getSourceLineNumber();
        String lineString = line <= 0 ? "" : String.valueOf(line);
        if (Env.testingMode) {
            out.println((filename == null ? "" : "FILE") + " " + lineString + ": [" + (p.getID() & IProblem.IgnoreCategoriesMask) + "] " + p.getMessage());
        } else {
            String ff = filename == null ? null : filename.replaceAll("\\\\", "/");
            out.println((filename == null ? ("" + lineString) : ("(" + ff + ":" + lineString + ")")) + " [" + (p.getID() & IProblem.IgnoreCategoriesMask) + "] " + p.getMessage());
        }
        if (p.getSourceStart() < 0) return;
        String sline = null;
        int lineStart = -1;
        if (p instanceof JmlEclipseProblem) {
            JmlEclipseProblem cp = (JmlEclipseProblem) p;
            String stext = cp.sourceText;
            lineStart = cp.lineStart;
            if (lineStart >= 0) {
                int k = cp.lineEnd + 1;
                sline = stext.substring(lineStart, k > 0 && k <= stext.length() ? k : stext.length());
            }
        }
        BufferedReader r = null;
        try {
            if (sline == null) try {
                if (p instanceof JmlEclipseProblem) {
                    String stext = ((JmlEclipseProblem) p).sourceText;
                    if (stext != null) r = new BufferedReader(new StringReader(stext));
                }
                if (r == null && resource != null && (resource instanceof IFile)) {
                    r = new BufferedReader(new InputStreamReader(((IFile) resource).getContents()));
                }
                if (r == null) return;
                int count = 0;
                int lastr = -2;
                int c = 0;
                --line;
                if (line > 0) {
                    while (true) {
                        c = r.read();
                        count++;
                        if (c == -1) return;
                        if (c == '\r') {
                            lastr = count;
                            --line;
                            if (line == 0) {
                                c = r.read();
                                count++;
                                if (c == -1) return;
                                break;
                            }
                        } else if (c == '\n') {
                            if (lastr + 1 != count) {
                                --line;
                                c = 0;
                                if (line == 0) break;
                            }
                        }
                    }
                }
                if (c == '\r') {
                    sline = Env.eol;
                } else if (c == 0 || c == '\n') {
                    sline = r.readLine() + Env.eol;
                } else {
                    sline = (char) c + r.readLine() + Env.eol;
                }
                if (lineStart == -1) lineStart = count;
                r.close();
            } catch (Exception e) {
                Log.errorlog("INTERNAL ERROR - Exception occurred while printing a Problem: " + e, e);
                return;
            }
            if (sline != null) {
                out.print(sline);
                {
                    int last = sline.length() - 1;
                    int c = sline.charAt(last);
                    if (!(c == '\n' || c == '\r')) out.println();
                }
                int st = p.getSourceStart();
                int length = p.getSourceEnd() - st + 1;
                st -= lineStart;
                int extra = 0;
                if (st >= sline.length()) {
                    extra = st - sline.length();
                    st = sline.length();
                }
                boolean ellipsis = false;
                if (st + extra + length > sline.length() + 2) {
                    length = sline.length() + 2 - st - extra;
                    if (length <= 0) length = 1;
                    ellipsis = true;
                }
                int i = 0;
                while (--st >= 0) out.print(sline.charAt(i++) == '\t' ? "\t" : " ");
                while (--extra >= 0) out.print(" ");
                while (--length >= 0) out.print("^");
                if (ellipsis) out.print("...");
                out.println();
            }
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (java.io.IOException e) {
                    Log.errorlog("Failed to close a BufferedReader", e);
                }
            }
        }
    }
}
