package com.sodad.weka.core;

import java.lang.reflect.Method;

/**
 * Generates Javadoc comments from the class's globalInfo method. Can 
 * automatically update the comments if they're surrounded by
 * the GLOBALINFO_STARTTAG and GLOBALINFO_ENDTAG (the indention is determined via
 * the GLOBALINFO_STARTTAG). <p/>
 * 
 <!-- options-start -->
 * Valid options are: <p/>
 * 
 * <pre> -W &lt;classname&gt;
 *  The class to load.</pre>
 * 
 * <pre> -nostars
 *  Suppresses the '*' in the Javadoc.</pre>
 * 
 * <pre> -dir &lt;dir&gt;
 *  The directory above the package hierarchy of the class.</pre>
 * 
 * <pre> -silent
 *  Suppresses printing in the console.</pre>
 * 
 <!-- options-end -->
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 5953 $
 * @see #GLOBALINFO_METHOD
 * @see #GLOBALINFO_STARTTAG
 * @see #GLOBALINFO_ENDTAG
 */
public class GlobalInfoJavadoc extends Javadoc {

    /** the globalInfo method name */
    public static final String GLOBALINFO_METHOD = "globalInfo";

    /** the start comment tag for inserting the generated Javadoc */
    public static final String GLOBALINFO_STARTTAG = "<!-- globalinfo-start -->";

    /** the end comment tag for inserting the generated Javadoc */
    public static final String GLOBALINFO_ENDTAG = "<!-- globalinfo-end -->";

    /**
   * default constructor 
   */
    public GlobalInfoJavadoc() {
        super();
        m_StartTag = new String[1];
        m_EndTag = new String[1];
        m_StartTag[0] = GLOBALINFO_STARTTAG;
        m_EndTag[0] = GLOBALINFO_ENDTAG;
    }

    /**
   * generates and returns the Javadoc for the specified start/end tag pair.
   * 
   * @param index	the index in the start/end tag array
   * @return		the generated Javadoc
   * @throws Exception 	in case the generation fails
   */
    protected String generateJavadoc(int index) throws Exception {
        String result;
        Method method;
        result = "";
        if (index == 0) {
            if (!canInstantiateClass()) return result;
            try {
                method = getInstance().getClass().getMethod(GLOBALINFO_METHOD, (Class[]) null);
            } catch (Exception e) {
                return result;
            }
            result = toHTML((String) method.invoke(getInstance(), (Object[]) null));
            result = result.trim() + "\n<p/>\n";
            if (getUseStars()) result = indent(result, 1, "* ");
        }
        return result;
    }

    /**
   * Returns the revision string.
   * 
   * @return		the revision
   */
    public String getRevision() {
        return RevisionUtils.extract("$Revision: 5953 $");
    }

    /**
   * Parses the given commandline parameters and generates the Javadoc.
   * 
   * @param args	the commandline parameters for the object
   */
    public static void main(String[] args) {
        runJavadoc(new GlobalInfoJavadoc(), args);
    }
}
