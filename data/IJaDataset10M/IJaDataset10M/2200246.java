package com.ibm.wala.eclipse.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.collections.HashSetFactory;

/**
 * Convenience methods to get information from JDT IJavaElement model.
 * 
 * @author aying
 */
public class JdtUtil {

    public static String getFilePath(IJavaElement javaElt) {
        if (javaElt == null) {
            throw new IllegalArgumentException("javaElt is null");
        }
        String filePath = javaElt.getPath().toString();
        return filePath;
    }

    public static String getPackageName(ICompilationUnit cu) {
        if (cu == null) {
            throw new IllegalArgumentException("cu is null");
        }
        try {
            IPackageDeclaration[] pkgDecl = cu.getPackageDeclarations();
            if (pkgDecl != null && pkgDecl.length > 0) {
                String packageName = pkgDecl[0].getElementName();
                return packageName;
            }
        } catch (JavaModelException e) {
        }
        return "";
    }

    public static String getFullyQualifiedClassName(IType type) {
        if (type == null) {
            throw new IllegalArgumentException("type is null");
        }
        ICompilationUnit cu = (ICompilationUnit) type.getParent();
        String packageName = getPackageName(cu);
        String className = type.getElementName();
        String fullyQName = packageName + "." + className;
        return fullyQName;
    }

    public static String getClassName(IType type) {
        if (type == null) {
            throw new IllegalArgumentException("type is null");
        }
        String className = type.getElementName();
        return className;
    }

    /**
   * Return a unique string representing the specified Java element across
   * projects in the workspace. The returned string can be used as a handle to
   * create JavaElement by 'JavaCore.create(String)'
   * 
   * For example, suppose we have the method
   * 'fooPackage.barPackage.FooClass.fooMethod(int)' which is in the
   * 'FooProject' and source folder 'src' the handle would be '=FooProject/src<fooPackage.barPackage{FooClass.java[FooClass~fooMethod~I'
   * 
   * @param javaElt
   * @throws IllegalArgumentException
   *             if javaElt is null
   */
    public static String getJdtHandleString(IJavaElement javaElt) {
        if (javaElt == null) {
            throw new IllegalArgumentException("javaElt is null");
        }
        return javaElt.getHandleIdentifier();
    }

    public static IJavaElement createJavaElementFromJdtHandle(String jdtHandle) {
        return JavaCore.create(jdtHandle);
    }

    public static IType[] getClasses(ICompilationUnit cu) {
        if (cu == null) {
            throw new IllegalArgumentException("cu is null");
        }
        try {
            return cu.getAllTypes();
        } catch (JavaModelException e) {
        }
        return null;
    }

    public static IJavaProject getProject(IJavaElement javaElt) {
        if (javaElt == null) {
            throw new IllegalArgumentException("javaElt is null");
        }
        IJavaProject javaProject = javaElt.getJavaProject();
        return javaProject;
    }

    public static String getProjectName(IJavaProject javaProject) {
        if (javaProject == null) {
            throw new IllegalArgumentException("javaProject is null");
        }
        return javaProject.getElementName();
    }

    /**
   * @param typeSignature
   *            Some of the type signatures examples are "QString;" (String) and
   *            "I" (int) The type signatures may be either unresolved (for
   *            source types) or resolved (for binary types), and either basic
   *            (for basic types) or rich (for parameterized types). See
   *            {@link Signature} for details.
   */
    public static String getHumanReadableType(String typeSignature) {
        String simpleName = Signature.getSignatureSimpleName(typeSignature);
        return simpleName;
    }

    public static IJavaProject getJavaProject(IFile appJar) {
        if (appJar == null) {
            throw new IllegalArgumentException("appJar is null");
        }
        String projectName = appJar.getProject().getName();
        return getJavaProject(projectName);
    }

    public static IJavaProject getJavaProject(String projectName) {
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        IJavaModel javaModel = JavaCore.create(workspaceRoot);
        IJavaProject javaProject = javaModel.getJavaProject(projectName);
        return javaProject;
    }

    /**
   * compute the java projects in the active workspace
   */
    public static Collection<IJavaProject> getWorkspaceJavaProjects() {
        Collection<IJavaProject> result = HashSetFactory.make();
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        for (IProject p : workspaceRoot.getProjects()) {
            try {
                if (p.hasNature(JavaCore.NATURE_ID)) {
                    IJavaProject jp = JavaCore.create(p);
                    if (jp != null) {
                        result.add(jp);
                    }
                }
            } catch (CoreException e) {
            }
        }
        return result;
    }

    /**
   * Find the {@link IType} in the workspace corresponding to a class name.
   * 
   * @return null if not found
   * @throws IllegalArgumentException
   *             if projects == null
   */
    public static IType findJavaClassInProjects(String fullyQualifiedName, Collection<IJavaProject> projects) throws IllegalArgumentException {
        if (projects == null) {
            throw new IllegalArgumentException("projects == null");
        }
        for (IJavaProject project : projects) {
            try {
                IType t = project.findType(fullyQualifiedName);
                if (t != null) {
                    return t;
                }
            } catch (JavaModelException e) {
                e.printStackTrace();
            }
        }
        System.err.println("failed to find " + fullyQualifiedName);
        return null;
    }

    public static IType findJavaClassInResources(String className, Collection<IResource> resources) {
        Collection<IJavaProject> projects = HashSetFactory.make();
        for (IResource r : resources) {
            projects.add(JavaCore.create(r).getJavaProject());
        }
        return findJavaClassInProjects(className, projects);
    }

    /**
   * Find the IMethod in the workspace corresponding to a method selector.
   * 
   * TODO: this is way too slow. figure out something better.
   * 
   * @return null if not found
   */
    public static IMethod findJavaMethodInProjects(String klass, String selector, Collection<IJavaProject> projects) {
        IType type = null;
        try {
            type = findJavaClassInProjects(klass, projects);
        } catch (Throwable t) {
            return null;
        }
        if (type == null) {
            return null;
        }
        String name = parseForName(selector, type);
        String[] paramTypes = parseForParameterTypes(selector);
        IMethod m = type.getMethod(name, paramTypes);
        IMethod[] methods = type.findMethods(m);
        if (methods != null && methods.length == 1) {
            return methods[0];
        } else {
            try {
                List<IMethod> matches = new ArrayList<IMethod>();
                Collection<String> typeParameterNames = getTypeParameterNames(type);
                METHODS: for (IMethod x : type.getMethods()) {
                    if (x.getElementName().equals(name)) {
                        if (x.getParameterTypes().length == paramTypes.length) {
                            for (int i = 0; i < x.getParameterTypes().length; i++) {
                                String s1 = Signature.getTypeErasure(Signature.getSignatureSimpleName(x.getParameterTypes()[i]));
                                String s2 = Signature.getTypeErasure(Signature.getSignatureSimpleName(paramTypes[i]));
                                if (typeParameterNames.contains(s1)) {
                                } else {
                                    if (!s1.equals(s2)) {
                                        continue METHODS;
                                    }
                                }
                            }
                            matches.add(x);
                        }
                    }
                }
                if (matches.size() == 1) {
                    return matches.get(0);
                } else {
                    System.err.println("findJavaMethodInWorkspace FAILED TO MATCH " + m);
                    return null;
                }
            } catch (JavaModelException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static Collection<String> getTypeParameterNames(IType type) throws IllegalArgumentException, JavaModelException {
        if (type == null) {
            throw new IllegalArgumentException("type == null");
        }
        ITypeParameter[] tp = type.getTypeParameters();
        Collection<String> typeParameterNames = HashSetFactory.make(tp.length);
        for (ITypeParameter p : tp) {
            typeParameterNames.add(p.getElementName());
        }
        return typeParameterNames;
    }

    public static String parseForName(String selector, IType type) {
        if (selector == null) {
            throw new IllegalArgumentException("selector is null");
        }
        String result = selector.substring(0, selector.indexOf('('));
        if (result.equals("<init>")) {
            return type.getElementName();
        } else {
            return result;
        }
    }

    public static final String[] parseForParameterTypes(String selector) throws IllegalArgumentException {
        try {
            if (selector == null) {
                throw new IllegalArgumentException("selector is null");
            }
            String d = selector.substring(selector.indexOf('('));
            if (d.length() <= 2) {
                throw new IllegalArgumentException("invalid descriptor: " + d);
            }
            if (d.charAt(0) != '(') {
                throw new IllegalArgumentException("invalid descriptor: " + d);
            }
            ArrayList<String> sigs = new ArrayList<String>(10);
            int i = 1;
            while (true) {
                switch(d.charAt(i++)) {
                    case TypeReference.VoidTypeCode:
                        sigs.add(TypeReference.VoidName.toString());
                        continue;
                    case TypeReference.BooleanTypeCode:
                        sigs.add(TypeReference.BooleanName.toString());
                        continue;
                    case TypeReference.ByteTypeCode:
                        sigs.add(TypeReference.ByteName.toString());
                        continue;
                    case TypeReference.ShortTypeCode:
                        sigs.add(TypeReference.ShortName.toString());
                        continue;
                    case TypeReference.IntTypeCode:
                        sigs.add(TypeReference.IntName.toString());
                        continue;
                    case TypeReference.LongTypeCode:
                        sigs.add(TypeReference.LongName.toString());
                        continue;
                    case TypeReference.FloatTypeCode:
                        sigs.add(TypeReference.FloatName.toString());
                        continue;
                    case TypeReference.DoubleTypeCode:
                        sigs.add(TypeReference.DoubleName.toString());
                        continue;
                    case TypeReference.CharTypeCode:
                        sigs.add(TypeReference.CharName.toString());
                        continue;
                    case TypeReference.ArrayTypeCode:
                        {
                            int off = i - 1;
                            while (d.charAt(i) == TypeReference.ArrayTypeCode) {
                                ++i;
                            }
                            if (d.charAt(i++) == TypeReference.ClassTypeCode) {
                                while (d.charAt(i++) != ';') ;
                                sigs.add(d.substring(off, i).replaceAll("/", "."));
                            } else {
                                sigs.add(d.substring(off, i));
                            }
                            continue;
                        }
                    case (byte) ')':
                        return toArray(sigs);
                    default:
                        {
                            int off = i - 1;
                            char c;
                            do {
                                c = d.charAt(i++);
                            } while (c != ',' && c != ')');
                            sigs.add("L" + d.substring(off, i - 1) + ";");
                            if (c == ')') {
                                return toArray(sigs);
                            }
                            continue;
                        }
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("error parsing selector " + selector);
        }
    }

    private static String[] toArray(ArrayList<String> sigs) {
        int size = sigs.size();
        if (size == 0) {
            return new String[0];
        }
        Iterator<String> it = sigs.iterator();
        String[] result = new String[size];
        for (int j = 0; j < size; j++) {
            result[j] = it.next();
        }
        return result;
    }

    /**
   * Find the IMethod in the workspace corresponding to a method signature.
   * 
   * This doesn't work for elements declared in inner classes. It's possible
   * this is a 3.2 bug fixed in 3.3
   * 
   * @return null if not found
   */
    @Deprecated
    public static IMethod findJavaMethodInWorkspaceBrokenForInnerClasses(String methodSig) {
        System.err.println("Search for " + methodSig);
        SearchPattern p = SearchPattern.createPattern(methodSig, IJavaSearchConstants.METHOD, IJavaSearchConstants.DECLARATIONS, SearchPattern.R_EXACT_MATCH);
        IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
        SearchEngine engine = new SearchEngine();
        final Collection<IJavaElement> kludge = HashSetFactory.make();
        SearchRequestor requestor = new SearchRequestor() {

            @Override
            public void acceptSearchMatch(SearchMatch match) throws CoreException {
                kludge.add((IJavaElement) match.getElement());
            }
        };
        try {
            engine.search(p, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope, requestor, null);
        } catch (CoreException e) {
            e.printStackTrace();
        }
        if (kludge.size() == 1) {
            return (IMethod) kludge.iterator().next();
        } else {
            System.err.println("RETURNED " + kludge.size() + " " + kludge);
            return null;
        }
    }

    /**
   * Use the search engine to find all methods in a java element
   */
    public static Collection<IMethod> findMethods(IJavaElement elt) {
        if (elt instanceof ICompilationUnit) {
            Collection<IMethod> result = HashSetFactory.make();
            for (IType type : getClasses((ICompilationUnit) elt)) {
                try {
                    for (IMethod m : type.getMethods()) {
                        result.add(m);
                    }
                } catch (JavaModelException e) {
                    e.printStackTrace();
                }
            }
            return result;
        } else {
            final Collection<IMethod> result = HashSetFactory.make();
            SearchPattern p = SearchPattern.createPattern("*", IJavaSearchConstants.METHOD, IJavaSearchConstants.DECLARATIONS, SearchPattern.R_PATTERN_MATCH);
            IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaElement[] { elt }, IJavaSearchScope.SOURCES);
            SearchEngine engine = new SearchEngine();
            SearchRequestor requestor = new SearchRequestor() {

                @Override
                public void acceptSearchMatch(SearchMatch match) throws CoreException {
                    result.add((IMethod) match.getElement());
                }
            };
            try {
                engine.search(p, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope, requestor, null);
            } catch (CoreException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
