package org.eclipse.jdt.internal.core.search.indexing;

import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.internal.compiler.ISourceElementRequestor;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.jdt.internal.core.search.processing.JobManager;

/**
 * This class is used by the JavaParserIndexer. When parsing the java file, the requestor
 * recognizes the java elements (methods, fields, ...) and add them to an index.
 */
public class SourceIndexerRequestor implements ISourceElementRequestor, IIndexConstants {

    SourceIndexer indexer;

    char[] packageName = CharOperation.NO_CHAR;

    char[][] enclosingTypeNames = new char[5][];

    int depth = 0;

    int methodDepth = 0;

    public SourceIndexerRequestor(SourceIndexer indexer) {
        this.indexer = indexer;
    }

    /**
 * @see ISourceElementRequestor#acceptAnnotationTypeReference(char[][], int, int)
 */
    public void acceptAnnotationTypeReference(char[][] typeName, int sourceStart, int sourceEnd) {
        int length = typeName.length;
        for (int i = 0; i < length - 1; i++) acceptUnknownReference(typeName[i], 0);
        acceptAnnotationTypeReference(typeName[length - 1], 0);
    }

    /**
 * @see ISourceElementRequestor#acceptAnnotationTypeReference(char[], int)
 */
    public void acceptAnnotationTypeReference(char[] simpleTypeName, int sourcePosition) {
        this.indexer.addAnnotationTypeReference(simpleTypeName);
    }

    /**
 * @see ISourceElementRequestor#acceptConstructorReference(char[], int, int)
 */
    public void acceptConstructorReference(char[] typeName, int argCount, int sourcePosition) {
        if (CharOperation.indexOf(Signature.C_GENERIC_START, typeName) > 0) {
            typeName = Signature.toCharArray(Signature.getTypeErasure(Signature.createTypeSignature(typeName, false)).toCharArray());
        }
        this.indexer.addConstructorReference(typeName, argCount);
        int lastDot = CharOperation.lastIndexOf('.', typeName);
        if (lastDot != -1) {
            char[][] qualification = CharOperation.splitOn('.', CharOperation.subarray(typeName, 0, lastDot));
            for (int i = 0, length = qualification.length; i < length; i++) {
                this.indexer.addNameReference(qualification[i]);
            }
        }
    }

    /**
 * @see ISourceElementRequestor#acceptFieldReference(char[], int)
 */
    public void acceptFieldReference(char[] fieldName, int sourcePosition) {
        this.indexer.addFieldReference(fieldName);
    }

    /**
 * @see ISourceElementRequestor#acceptImport(int, int, char[][], boolean, int)
 */
    public void acceptImport(int declarationStart, int declarationEnd, char[][] tokens, boolean onDemand, int modifiers) {
    }

    /**
 * @see ISourceElementRequestor#acceptLineSeparatorPositions(int[])
 */
    public void acceptLineSeparatorPositions(int[] positions) {
    }

    /**
 * @see ISourceElementRequestor#acceptMethodReference(char[], int, int)
 */
    public void acceptMethodReference(char[] methodName, int argCount, int sourcePosition) {
        this.indexer.addMethodReference(methodName, argCount);
    }

    /**
 * @see ISourceElementRequestor#acceptPackage(ImportReference)
 */
    public void acceptPackage(ImportReference importReference) {
        this.packageName = CharOperation.concatWith(importReference.getImportName(), '.');
    }

    /**
 * @see ISourceElementRequestor#acceptProblem(CategorizedProblem)
 */
    public void acceptProblem(CategorizedProblem problem) {
    }

    /**
 * @see ISourceElementRequestor#acceptTypeReference(char[][], int, int)
 */
    public void acceptTypeReference(char[][] typeName, int sourceStart, int sourceEnd) {
        int length = typeName.length;
        for (int i = 0; i < length - 1; i++) acceptUnknownReference(typeName[i], 0);
        acceptTypeReference(typeName[length - 1], 0);
    }

    /**
 * @see ISourceElementRequestor#acceptTypeReference(char[], int)
 */
    public void acceptTypeReference(char[] simpleTypeName, int sourcePosition) {
        this.indexer.addTypeReference(simpleTypeName);
    }

    /**
 * @see ISourceElementRequestor#acceptUnknownReference(char[][], int, int)
 */
    public void acceptUnknownReference(char[][] name, int sourceStart, int sourceEnd) {
        for (int i = 0; i < name.length; i++) {
            acceptUnknownReference(name[i], 0);
        }
    }

    /**
 * @see ISourceElementRequestor#acceptUnknownReference(char[], int)
 */
    public void acceptUnknownReference(char[] name, int sourcePosition) {
        this.indexer.addNameReference(name);
    }

    public char[][] enclosingTypeNames() {
        if (depth == 0) return null;
        char[][] qualification = new char[this.depth][];
        System.arraycopy(this.enclosingTypeNames, 0, qualification, 0, this.depth);
        return qualification;
    }

    private void enterAnnotationType(TypeInfo typeInfo) {
        char[][] typeNames;
        if (this.methodDepth > 0) {
            typeNames = ONE_ZERO_CHAR;
        } else {
            typeNames = this.enclosingTypeNames();
        }
        this.indexer.addAnnotationTypeDeclaration(typeInfo.modifiers, packageName, typeInfo.name, typeNames, typeInfo.secondary);
        this.pushTypeName(typeInfo.name);
    }

    private void enterClass(TypeInfo typeInfo) {
        if (typeInfo.superclass != null) {
            typeInfo.superclass = getSimpleName(typeInfo.superclass);
            this.indexer.addConstructorReference(typeInfo.superclass, 0);
        }
        if (typeInfo.superinterfaces != null) {
            for (int i = 0, length = typeInfo.superinterfaces.length; i < length; i++) {
                typeInfo.superinterfaces[i] = getSimpleName(typeInfo.superinterfaces[i]);
            }
        }
        char[][] typeNames;
        if (this.methodDepth > 0) {
            typeNames = ONE_ZERO_CHAR;
        } else {
            typeNames = this.enclosingTypeNames();
        }
        char[][] typeParameterSignatures = null;
        if (typeInfo.typeParameters != null) {
            int typeParametersLength = typeInfo.typeParameters.length;
            typeParameterSignatures = new char[typeParametersLength][];
            for (int i = 0; i < typeParametersLength; i++) {
                ISourceElementRequestor.TypeParameterInfo typeParameterInfo = typeInfo.typeParameters[i];
                typeParameterSignatures[i] = Signature.createTypeParameterSignature(typeParameterInfo.name, typeParameterInfo.bounds == null ? CharOperation.NO_CHAR_CHAR : typeParameterInfo.bounds);
            }
        }
        this.indexer.addClassDeclaration(typeInfo.modifiers, this.packageName, typeInfo.name, typeNames, typeInfo.superclass, typeInfo.superinterfaces, typeParameterSignatures, typeInfo.secondary);
        this.pushTypeName(typeInfo.name);
    }

    /**
 * @see ISourceElementRequestor#enterCompilationUnit()
 */
    public void enterCompilationUnit() {
    }

    /**
 * @see ISourceElementRequestor#enterConstructor(ISourceElementRequestor.MethodInfo)
 */
    public void enterConstructor(MethodInfo methodInfo) {
        this.indexer.addConstructorDeclaration(methodInfo.name, methodInfo.parameterTypes, methodInfo.exceptionTypes);
        this.methodDepth++;
    }

    private void enterEnum(TypeInfo typeInfo) {
        if (typeInfo.superinterfaces != null) {
            for (int i = 0, length = typeInfo.superinterfaces.length; i < length; i++) {
                typeInfo.superinterfaces[i] = getSimpleName(typeInfo.superinterfaces[i]);
            }
        }
        char[][] typeNames;
        if (this.methodDepth > 0) {
            typeNames = ONE_ZERO_CHAR;
        } else {
            typeNames = this.enclosingTypeNames();
        }
        char[] superclass = typeInfo.superclass == null ? CharOperation.concatWith(TypeConstants.JAVA_LANG_ENUM, '.') : typeInfo.superclass;
        this.indexer.addEnumDeclaration(typeInfo.modifiers, packageName, typeInfo.name, typeNames, superclass, typeInfo.superinterfaces, typeInfo.secondary);
        this.pushTypeName(typeInfo.name);
    }

    /**
 * @see ISourceElementRequestor#enterField(ISourceElementRequestor.FieldInfo)
 */
    public void enterField(FieldInfo fieldInfo) {
        this.indexer.addFieldDeclaration(fieldInfo.type, fieldInfo.name);
        this.methodDepth++;
    }

    /**
 * @see ISourceElementRequestor#enterInitializer(int, int)
 */
    public void enterInitializer(int declarationSourceStart, int modifiers) {
        this.methodDepth++;
    }

    private void enterInterface(TypeInfo typeInfo) {
        if (typeInfo.superinterfaces != null) {
            for (int i = 0, length = typeInfo.superinterfaces.length; i < length; i++) {
                typeInfo.superinterfaces[i] = getSimpleName(typeInfo.superinterfaces[i]);
            }
        }
        char[][] typeNames;
        if (this.methodDepth > 0) {
            typeNames = ONE_ZERO_CHAR;
        } else {
            typeNames = this.enclosingTypeNames();
        }
        char[][] typeParameterSignatures = null;
        if (typeInfo.typeParameters != null) {
            int typeParametersLength = typeInfo.typeParameters.length;
            typeParameterSignatures = new char[typeParametersLength][];
            for (int i = 0; i < typeParametersLength; i++) {
                ISourceElementRequestor.TypeParameterInfo typeParameterInfo = typeInfo.typeParameters[i];
                typeParameterSignatures[i] = Signature.createTypeParameterSignature(typeParameterInfo.name, typeParameterInfo.bounds);
            }
        }
        this.indexer.addInterfaceDeclaration(typeInfo.modifiers, packageName, typeInfo.name, typeNames, typeInfo.superinterfaces, typeParameterSignatures, typeInfo.secondary);
        this.pushTypeName(typeInfo.name);
    }

    /**
 * @see ISourceElementRequestor#enterMethod(ISourceElementRequestor.MethodInfo)
 */
    public void enterMethod(MethodInfo methodInfo) {
        this.indexer.addMethodDeclaration(methodInfo.name, methodInfo.parameterTypes, methodInfo.returnType, methodInfo.exceptionTypes);
        this.methodDepth++;
    }

    /**
 * @see ISourceElementRequestor#enterType(ISourceElementRequestor.TypeInfo)
 */
    public void enterType(TypeInfo typeInfo) {
        switch(TypeDeclaration.kind(typeInfo.modifiers)) {
            case TypeDeclaration.CLASS_DECL:
                enterClass(typeInfo);
                break;
            case TypeDeclaration.ANNOTATION_TYPE_DECL:
                enterAnnotationType(typeInfo);
                break;
            case TypeDeclaration.INTERFACE_DECL:
                enterInterface(typeInfo);
                break;
            case TypeDeclaration.ENUM_DECL:
                enterEnum(typeInfo);
                break;
        }
    }

    /**
 * @see ISourceElementRequestor#exitCompilationUnit(int)
 */
    public void exitCompilationUnit(int declarationEnd) {
    }

    /**
 * @see ISourceElementRequestor#exitConstructor(int)
 */
    public void exitConstructor(int declarationEnd) {
        this.methodDepth--;
    }

    /**
 * @see ISourceElementRequestor#exitField(int, int, int)
 */
    public void exitField(int initializationStart, int declarationEnd, int declarationSourceEnd) {
        this.methodDepth--;
    }

    /**
 * @see ISourceElementRequestor#exitInitializer(int)
 */
    public void exitInitializer(int declarationEnd) {
        this.methodDepth--;
    }

    /**
 * @see ISourceElementRequestor#exitMethod(int, Expression)
 */
    public void exitMethod(int declarationEnd, Expression defaultValue) {
        this.methodDepth--;
    }

    /**
 * @see ISourceElementRequestor#exitType(int)
 */
    public void exitType(int declarationEnd) {
        popTypeName();
    }

    private char[] getSimpleName(char[] typeName) {
        int lastDot = -1, lastGenericStart = -1;
        int depthCount = 0;
        int length = typeName.length;
        lastDotLookup: for (int i = length - 1; i >= 0; i--) {
            switch(typeName[i]) {
                case '.':
                    if (depthCount == 0) {
                        lastDot = i;
                        break lastDotLookup;
                    }
                    break;
                case '<':
                    depthCount--;
                    if (depthCount == 0) lastGenericStart = i;
                    break;
                case '>':
                    depthCount++;
                    break;
            }
        }
        if (lastGenericStart < 0) {
            if (lastDot < 0) {
                return typeName;
            }
            return CharOperation.subarray(typeName, lastDot + 1, length);
        }
        return CharOperation.subarray(typeName, lastDot + 1, lastGenericStart);
    }

    public void popTypeName() {
        if (depth > 0) {
            enclosingTypeNames[--depth] = null;
        } else if (JobManager.VERBOSE) {
            try {
                enclosingTypeNames[-1] = null;
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    public void pushTypeName(char[] typeName) {
        if (depth == enclosingTypeNames.length) System.arraycopy(enclosingTypeNames, 0, enclosingTypeNames = new char[depth * 2][], 0, depth);
        enclosingTypeNames[depth++] = typeName;
    }
}
