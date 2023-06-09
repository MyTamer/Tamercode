package org.ontoware.rdf2go.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class for RDF syntaxes, and registry for them. A framework can register new
 * syntaxes at runtime by creating them with the constructor that automatically
 * registers, or by calling the {@link #register(Syntax)} method.
 * 
 * You can chose to use a Syntax in your application without registering it.
 * 
 * @author Max Völkel
 * @author Leo Sauermann
 * @author Roland Stühmer
 */
public class Syntax {

    /**
	 * List of known RDF file formats. Note: This has to be defined at the top
	 * of the class, otherwise the constructor of the final Syntaxes cannot use
	 * it.
	 */
    private static Set<Syntax> SYNTAXES = new HashSet<Syntax>();

    public static void resetFactoryDefaults() {
        SYNTAXES.clear();
        SYNTAXES.add(Ntriples);
        SYNTAXES.add(Nquads);
        SYNTAXES.add(RdfXml);
        SYNTAXES.add(Trig);
        SYNTAXES.add(Trix);
        SYNTAXES.add(Turtle);
    }

    /**
	 * RDF syntax RDF/XML.
	 * 
	 * @see <a href="http://www.w3.org/TR/rdf-syntax-grammar/">RDF/XML
	 *      syntax</a>
	 */
    public static final Syntax RdfXml = new Syntax("rdfxml", "application/rdf+xml", ".rdf", true);

    /**
	 * RDF syntax Turtle.
	 * 
	 * @see <a href="http://www.w3.org/TeamSubmission/turtle/">Turtle syntax</a>
	 */
    public static final Syntax Turtle = new Syntax("turtle", "application/x-turtle", ".ttl", true);

    /**
	 * RDF syntax N-Triples.
	 * 
	 * @see <a href="http://www.w3.org/2001/sw/RDFCore/ntriples/">N-Triples
	 *      syntax</a>
	 */
    public static final Syntax Ntriples = new Syntax("ntriples", "text/plain", ".nt", true);

    /**
	 * RDF syntax N-Quads.
	 * 
	 * @see <a href="http://sw.deri.org/2008/07/n-quads/">N-Quads syntax</a>
	 */
    public static final Syntax Nquads = new Syntax("nquads", "text/x-nquads", ".nq", true);

    /**
	 * RDF syntax TriX.
	 * 
	 * @see <a
	 *      href="http://www.hpl.hp.com/techreports/2004/HPL-2004-56.html">TriX
	 *      syntax</a>
	 */
    public static final Syntax Trix = new Syntax("trix", "application/trix", ".trix", true);

    /**
	 * RDF syntax TriG.
	 * 
	 * @see <a href="http://www4.wiwiss.fu-berlin.de/bizer/trig/">TriG
	 *      syntax</a>
	 */
    public static final Syntax Trig = new Syntax("trig", "application/x-trig", ".trig", true);

    /**
	 * register a new RDF Syntax you want to have available throughout your
	 * application.
	 * 
	 * @param syntax the new syntax to register.
	 */
    public static void register(Syntax syntax) {
        SYNTAXES.add(syntax);
    }

    /**
	 * return the RDF syntax with the given name.
	 * 
	 * @param name the name of the syntax to search
	 * @return the syntax or <code>null</code>, if none registered
	 */
    public static Syntax forName(String name) {
        for (Syntax x : SYNTAXES) {
            if (x.getName().equals(name)) return x;
        }
        return null;
    }

    /**
	 * return the RDF syntax with the given MIME-type.
	 * 
	 * @param mimeType the MIME-type of the syntax to find
	 * @return the syntax or <code>null</code>, if none registered
	 */
    public static Syntax forMimeType(String mimeType) {
        for (Syntax x : SYNTAXES) {
            if (x.getMimeType().equals(mimeType)) return x;
        }
        return null;
    }

    /**
	 * unregister an RDF Syntax from which you know that your application will
	 * never ever support it. This may help you to build user interfaces where
	 * users can select RDF syntaxes. If the syntax was unknown, returns false
	 * 
	 * @param syntax the syntax to unregister
	 * @return true, if the syntax was found and removed
	 */
    public static boolean unregister(Syntax syntax) {
        return SYNTAXES.remove(syntax);
    }

    /**
	 * list all available syntaxes. Collection is not modifyable.
	 * 
	 * @return a Collection of available syntaxes
	 */
    public static Collection<Syntax> collection() {
        return Collections.unmodifiableCollection(SYNTAXES);
    }

    /**
	 * list all available syntaxes. List is not modifyable.
	 * 
	 * @return a Collection of available syntaxes
	 * @deprecated Use #collection() instead
	 */
    @Deprecated
    public static List<Syntax> list() {
        return new ArrayList<Syntax>(collection());
    }

    private final String name;

    private final String mimeType;

    private final String filenameExtension;

    /**
	 * return the MIME-type of this format.
	 * 
	 * @return the MIME type
	 */
    public String getMimeType() {
        return this.mimeType;
    }

    /**
	 * @return the common name of this format
	 */
    public String getName() {
        return this.name;
    }

    /**
	 * @return the suggested filename-extension, including the leading '.'
	 */
    public String getFilenameExtension() {
        return this.filenameExtension;
    }

    /**
	 * Generate a new Syntax
	 * 
	 * @param name the name of the RDF syntax
	 * @param mimeType the MIMEtype of the RDF syntax
	 * @param filenameExtension including the dot
	 */
    public Syntax(final String name, final String mimeType, final String filenameExtension) {
        super();
        this.name = name;
        this.mimeType = mimeType;
        this.filenameExtension = filenameExtension;
    }

    /**
	 * Generate a new Syntax and register it
	 * 
	 * @param name the name of the RDF syntax
	 * @param mimeType the MIMEtype of the RDF syntax
	 * @param registerItNow register the new Syntax now.
	 * @param filenameExtension including the dot
	 */
    public Syntax(final String name, final String mimeType, final String filenameExtension, boolean registerItNow) {
        this(name, mimeType, filenameExtension);
        if (registerItNow) register(this);
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Syntax) {
            return this.mimeType.equals(((Syntax) obj).mimeType);
        }
        return false;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        return this.mimeType.hashCode();
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return "RDF Syntax '" + this.name + "' MIME-type=" + this.mimeType;
    }
}
