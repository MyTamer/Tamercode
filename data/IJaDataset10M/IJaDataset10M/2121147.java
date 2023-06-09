package mockit.coverage.modification;

import java.io.*;
import java.lang.instrument.*;
import java.security.*;
import java.util.*;
import mockit.coverage.standalone.*;
import mockit.external.asm4.*;

public final class ClassModification {

    private final Set<String> modifiedClasses;

    private final ClassSelection classSelection;

    public ClassModification() {
        modifiedClasses = new HashSet<String>();
        classSelection = new ClassSelection();
        redefineClassesAlreadyLoadedForCoverage();
    }

    private void redefineClassesAlreadyLoadedForCoverage() {
        Class<?>[] loadedClasses = Startup.instrumentation().getAllLoadedClasses();
        for (Class<?> loadedClass : loadedClasses) {
            if (!loadedClass.isAnnotation() && !loadedClass.isSynthetic() && isToBeConsideredForCoverage(loadedClass.getName(), loadedClass.getProtectionDomain())) {
                redefineClassForCoverage(loadedClass);
            }
        }
    }

    private void redefineClassForCoverage(Class<?> loadedClass) {
        byte[] modifiedClassfile = readAndModifyClassForCoverage(loadedClass);
        if (modifiedClassfile != null) {
            redefineClassForCoverage(loadedClass, modifiedClassfile);
            registerClassAsModifiedForCoverage(loadedClass.getName(), modifiedClassfile);
        }
    }

    private byte[] readAndModifyClassForCoverage(Class<?> aClass) {
        try {
            return modifyClassForCoverage(aClass);
        } catch (VisitInterruptedException ignore) {
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (AssertionError e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] modifyClassForCoverage(Class<?> aClass) {
        String className = aClass.getName();
        byte[] modifiedBytecode = CoverageModifier.recoverModifiedByteCodeIfAvailable(className);
        if (modifiedBytecode != null) {
            return modifiedBytecode;
        }
        String classFileName = className.replace('.', '/') + ".class";
        ClassLoader cl = aClass.getClassLoader();
        if (cl == null) {
            cl = ClassModification.class.getClassLoader();
        }
        InputStream classFile = cl.getResourceAsStream(classFileName);
        ClassReader cr;
        try {
            cr = new ClassReader(classFile);
        } catch (IOException e) {
            return null;
        }
        return modifyClassForCoverage(cr);
    }

    private byte[] modifyClassForCoverage(ClassReader cr) {
        CoverageModifier modifier = new CoverageModifier(cr);
        cr.accept(modifier, 0);
        return modifier.toByteArray();
    }

    private void redefineClassForCoverage(Class<?> loadedClass, byte[] modifiedClassfile) {
        ClassDefinition[] classDefs = { new ClassDefinition(loadedClass, modifiedClassfile) };
        try {
            Startup.instrumentation().redefineClasses(classDefs);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnmodifiableClassException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isToBeConsideredForCoverage(String className, ProtectionDomain protectionDomain) {
        return !modifiedClasses.contains(className) && classSelection.isSelected(className, protectionDomain);
    }

    private void registerClassAsModifiedForCoverage(String className, byte[] modifiedClassfile) {
        modifiedClasses.add(className);
        if (!Startup.isStandalone()) {
            mockit.internal.state.TestRun.mockFixture().addFixedClass(className, modifiedClassfile);
        }
    }

    public byte[] modifyClass(String className, ProtectionDomain protectionDomain, byte[] originalClassfile) {
        boolean modifyClassForCoverage = isToBeConsideredForCoverage(className, protectionDomain);
        if (modifyClassForCoverage) {
            try {
                byte[] modifiedClassfile = modifyClassForCoverage(className, originalClassfile);
                registerClassAsModifiedForCoverage(className, modifiedClassfile);
                return modifiedClassfile;
            } catch (VisitInterruptedException ignore) {
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (AssertionError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private byte[] modifyClassForCoverage(String className, byte[] classBytecode) {
        byte[] modifiedBytecode = CoverageModifier.recoverModifiedByteCodeIfAvailable(className);
        if (modifiedBytecode != null) {
            return modifiedBytecode;
        }
        ClassReader cr = new ClassReader(classBytecode);
        return modifyClassForCoverage(cr);
    }
}
