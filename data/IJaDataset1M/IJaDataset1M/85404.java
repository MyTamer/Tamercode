package org.crossmobile.source.out;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.ctype.CStruct;
import org.crossmobile.source.out.cutils.CObjectOut;
import org.crossmobile.source.out.cutils.CStructOut;
import org.crossmobile.source.utils.FileUtils;
import org.crossmobile.source.utils.WriteCallBack;
import org.crossmobile.source.xtype.AdvisorWrapper;
import org.crossmobile.source.xtype.XInjectedMethod;
import org.crossmobile.source.xtype.XObject;
import org.crossmobile.source.xtype.XProperty;
import org.crossmobile.source.guru.Advisor;
import org.crossmobile.source.out.cutils.Constants;

/**
 * Serves as the entry point for generation of C wrappers. The .m and .h files
 * are created and the necessary code generation methods are called. The wrapper
 * code required for headers are generated here - Eg: The
 * ADDITIONAL_INSTANCE_FIELDS in case a C reference has to be kept in order to
 * tell garbage collector about the association. Any code that needs to be
 * injected (specified via the advisor.xml), is generated in this class.
 * 
 */
public class COut implements Generator {

    private final String outdir;

    private CLibrary lib;

    public static String packageName = null;

    public COut(String outdir) {
        this.outdir = outdir;
    }

    @Override
    public void generate(final CLibrary library) {
        this.lib = library;
        packageName = lib.getPackagename().replace(".", "_") + "_";
        File out = new File(outdir);
        FileUtils.delete(out);
        CObject o = null;
        int i = 0;
        Collection<CObject> col = (Collection<CObject>) lib.getObjects();
        Object[] objs = col.toArray();
        for (i = 0; i < objs.length; i++) {
            o = (CObject) objs[i];
            final CObject fo = o;
            if (!Advisor.isInIgnoreList(fo.name)) {
                FileUtils.putFile(new File(out, o.getcClassName() + ".m"), new WriteCallBack<Writer>() {

                    @Override
                    public void exec(Writer out) throws IOException {
                        emitImplementation(fo, out);
                    }
                });
                FileUtils.putFile(new File(out, o.getcClassName() + ".h"), new WriteCallBack<Writer>() {

                    @Override
                    public void exec(Writer out) throws IOException {
                        emitHeader(fo, out);
                    }
                });
            }
        }
    }

    /**
     * Used to emit the wrappers into *.m files
     * 
     * @param object
     * @param out
     * @throws IOException
     */
    private void emitImplementation(CObject object, Writer out) throws IOException {
        if (CStruct.isStruct(object.name)) {
            CStructOut cStructOut = new CStructOut(out, lib, object);
            cStructOut.emitImpl();
        } else if (!(object.isProtocol())) {
            CObjectOut cObjectOut = new CObjectOut(out, lib, object);
            cObjectOut.emitImpl();
        } else {
        }
        if (AdvisorWrapper.classHasExternallyInjectedCode(object.name)) emitInjectedCode(object, out);
    }

    /**
     * Used to emit code to *.h files
     * 
     * @param object
     * @param out
     * @throws IOException
     */
    private static void emitHeader(CObject object, Writer out) throws IOException {
        out.append(Constants.BEGIN_DECL + Constants.N);
        if (!object.name.contains("NSObject")) out.append("#include \"xmlvm-ios.h\"" + Constants.N);
        if (CStruct.isStruct(object.name)) {
            out.append(Constants.N + object.getName() + " to" + object.getName() + "(void * obj);" + Constants.N);
            out.append("JAVA_OBJECT from" + object.getName() + "(" + object.getName() + " obj);" + Constants.N);
            out.append("#define __ADDITIONAL_INSTANCE_FIELDS_" + object.getcClassName() + " \\ " + Constants.N);
        } else if (!(object.isProtocol())) {
            out.append("#define __ADDITIONAL_INSTANCE_FIELDS_" + object.getcClassName() + " \\ " + Constants.N);
            if (object.name.contains("NSObject")) out.append(" void *wrappedObjCObj;");
        } else {
        }
        emitAccumulatorReplacer(object.name, out);
        out.append(Constants.N);
        out.append(Constants.END_DECL);
    }

    /**
     * Used to emit code into *.h in case a C reference needs to be stored in
     * order to tell the garbage collector about association
     * 
     * @param className
     * @param out
     * @throws IOException
     */
    private static void emitAccumulatorReplacer(String className, Writer out) throws IOException {
        if (AdvisorWrapper.needsReplacer(className)) {
            XObject obj = AdvisorWrapper.getSpecialClass(className);
            for (XProperty p : obj.getProperties()) {
                if (p.isReplace()) {
                    out.append("JAVA_OBJECT " + p.getName() + ";\\ " + Constants.N);
                }
            }
        }
        if (AdvisorWrapper.needsAccumulator(className)) out.append("JAVA_OBJECT acc_array_" + className + ";");
    }

    /**
     * Used to emit injected code which is provided by the advice.xml
     * 
     * @param object
     * @param out
     * @throws IOException
     */
    private static void emitInjectedCode(CObject object, Writer out) throws IOException {
        List<XInjectedMethod> iMethods = AdvisorWrapper.getExternallyInjectedCode(object.name);
        for (XInjectedMethod im : iMethods) {
            out.append(Constants.BEGIN_WRAPPER + "[" + object.getcClassName() + "_" + im.getName() + "__]");
            out.append(im.getInjectedCode().get(0).getCode() + Constants.N);
            out.append(Constants.END_WRAPPER);
        }
    }
}
