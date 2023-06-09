package gnu.classpath.tools.rmi.rmic;

import java.lang.reflect.Method;
import java.util.Iterator;
import gnu.classpath.tools.AbstractMethodGenerator;
import gnu.classpath.tools.giop.grmic.GiopRmicCompiler;

/**
 * RMI stub source code generator, required to support java.rmi.*
 *
 * @author Audrius Meskauskas (AudriusA@Bioinformatics.org) 
 */
public class RmicCompiler extends GiopRmicCompiler {

    /**
   * If true, the zero size object array is declared in the stub to reduce
   * garbage generation.
   */
    public boolean addZeroSizeObjecArray;

    /**
   * Generate a RMI stub.
   * 
   * @return the string, containing the text of the generated stub.
   */
    public String generateStub() {
        String template = getResource("Stub_12.jav");
        StringBuffer b = new StringBuffer();
        Iterator iter = methods.iterator();
        while (iter.hasNext()) {
            RmiMethodGenerator m = (RmiMethodGenerator) iter.next();
            b.append(m.generateStubMethod());
        }
        vars.put("#stub_methods", b.toString());
        vars.put("#imports", getImportStatements());
        vars.put("#interfaces", getAllInterfaces());
        vars.put("#stub_method_declarations", getStubMethodDeclarations());
        vars.put("#stub_method_initializations", getStubMethodInitializations());
        if (addZeroSizeObjecArray) {
            vars.put("#zeroSizeObjecArray", "private static final Object[] NO_ARGS = new Object[0];");
            vars.put("#zeroSizeClassArray", "final Class[]  NO_ARGSc = new Class[0];");
        } else {
            vars.put("#zeroSizeObjecArray", "");
            vars.put("#zeroSizeClassArray", "");
        }
        String output = replaceAll(template, vars);
        return output;
    }

    /**
   * Create a method generator, applicable for RMI stub methods.
   */
    protected AbstractMethodGenerator createMethodGenerator(Method m) {
        return new RmiMethodGenerator(m, this);
    }

    /**
   * Get the stub method declarations.
   */
    public String getStubMethodDeclarations() {
        StringBuffer b = new StringBuffer();
        Iterator iter = methods.iterator();
        while (iter.hasNext()) {
            RmiMethodGenerator method = (RmiMethodGenerator) iter.next();
            b.append("    ");
            b.append("private static final Method met_");
            b.append(method.method.getName());
            b.append(';');
            if (iter.hasNext()) b.append('\n');
        }
        return b.toString();
    }

    /**
   * Get stub method initializations. These must be done in a try-catch
   * statement to catch {@link NoSuchMethodException}.
   */
    public String getStubMethodInitializations() {
        StringBuffer b = new StringBuffer();
        Iterator iter = methods.iterator();
        while (iter.hasNext()) {
            RmiMethodGenerator method = (RmiMethodGenerator) iter.next();
            b.append("             ");
            b.append("met_");
            b.append(method.method.getName());
            b.append(" =\n               ");
            b.append(name(method.method.getDeclaringClass()));
            b.append(".class.getMethod(");
            b.append('"');
            b.append(method.method.getName());
            b.append("\", ");
            if (method.method.getParameterTypes().length == 0) b.append("NO_ARGSc);"); else {
                b.append("new Class[]\n                 {\n                   ");
                b.append(method.getArgListAsClassArray());
                b.append("\n                 }");
                b.append(");");
            }
            b.append('\n');
        }
        return b.toString();
    }

    /**
   * Prepare for the compilation of the next class.
   */
    public void reset() {
        addZeroSizeObjecArray = false;
        super.reset();
    }

    /**
   * Additional processing of the stub name (nothing to do for JRMP stubs).
   */
    public String convertStubName(String name) {
        return name;
    }
}
