package pl.wcislo.sbql4j.mirror.util;

import pl.wcislo.sbql4j.mirror.type.AnnotationType;
import pl.wcislo.sbql4j.mirror.type.ArrayType;
import pl.wcislo.sbql4j.mirror.type.ClassType;
import pl.wcislo.sbql4j.mirror.type.DeclaredType;
import pl.wcislo.sbql4j.mirror.type.EnumType;
import pl.wcislo.sbql4j.mirror.type.InterfaceType;
import pl.wcislo.sbql4j.mirror.type.PrimitiveType;
import pl.wcislo.sbql4j.mirror.type.ReferenceType;
import pl.wcislo.sbql4j.mirror.type.TypeMirror;
import pl.wcislo.sbql4j.mirror.type.TypeVariable;
import pl.wcislo.sbql4j.mirror.type.VoidType;
import pl.wcislo.sbql4j.mirror.type.WildcardType;

/**
 * A simple visitor for types.
 *
 * <p> The implementations of the methods of this class do nothing but
 * delegate up the type hierarchy.  A subclass should override the
 * methods that correspond to the kinds of types on which it will
 * operate.
 *
 * @author Joseph D. Darcy
 * @author Scott Seligman
 * @since 1.5
 */
public class SimpleTypeVisitor implements TypeVisitor {

    /**
     * Creates a new <tt>SimpleTypeVisitor</tt>.
     */
    public SimpleTypeVisitor() {
    }

    /**
     * Visits a type mirror.
     * The implementation does nothing.
     * @param t the type to visit
     */
    public void visitTypeMirror(TypeMirror t) {
    }

    /**
     * Visits a primitive type.
     * The implementation simply invokes
     * {@link #visitTypeMirror visitTypeMirror}.
     * @param t the type to visit
     */
    public void visitPrimitiveType(PrimitiveType t) {
        visitTypeMirror(t);
    }

    /**
     * Visits a void type.
     * The implementation simply invokes
     * {@link #visitTypeMirror visitTypeMirror}.
     * @param t the type to visit
     */
    public void visitVoidType(VoidType t) {
        visitTypeMirror(t);
    }

    /**
     * Visits a reference type.
     * The implementation simply invokes
     * {@link #visitTypeMirror visitTypeMirror}.
     * @param t the type to visit
     */
    public void visitReferenceType(ReferenceType t) {
        visitTypeMirror(t);
    }

    /**
     * Visits a declared type.
     * The implementation simply invokes
     * {@link #visitReferenceType visitReferenceType}.
     * @param t the type to visit
     */
    public void visitDeclaredType(DeclaredType t) {
        visitReferenceType(t);
    }

    /**
     * Visits a class type.
     * The implementation simply invokes
     * {@link #visitDeclaredType visitDeclaredType}.
     * @param t the type to visit
     */
    public void visitClassType(ClassType t) {
        visitDeclaredType(t);
    }

    /**
     * Visits an enum type.
     * The implementation simply invokes
     * {@link #visitClassType visitClassType}.
     * @param t the type to visit
     */
    public void visitEnumType(EnumType t) {
        visitClassType(t);
    }

    /**
     * Visits an interface type.
     * The implementation simply invokes
     * {@link #visitDeclaredType visitDeclaredType}.
     * @param t the type to visit
     */
    public void visitInterfaceType(InterfaceType t) {
        visitDeclaredType(t);
    }

    /**
     * Visits an annotation type.
     * The implementation simply invokes
     * {@link #visitInterfaceType visitInterfaceType}.
     * @param t the type to visit
     */
    public void visitAnnotationType(AnnotationType t) {
        visitInterfaceType(t);
    }

    /**
     * Visits an array type.
     * The implementation simply invokes
     * {@link #visitReferenceType visitReferenceType}.
     * @param t the type to visit
     */
    public void visitArrayType(ArrayType t) {
        visitReferenceType(t);
    }

    /**
     * Visits a type variable.
     * The implementation simply invokes
     * {@link #visitReferenceType visitReferenceType}.
     * @param t the type to visit
     */
    public void visitTypeVariable(TypeVariable t) {
        visitReferenceType(t);
    }

    /**
     * Visits a wildcard.
     * The implementation simply invokes
     * {@link #visitTypeMirror visitTypeMirror}.
     * @param t the type to visit
     */
    public void visitWildcardType(WildcardType t) {
        visitTypeMirror(t);
    }
}
