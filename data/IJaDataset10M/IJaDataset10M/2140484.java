package com.google.web.bindery.requestfactory.apt;

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.SimpleTypeVisitor6;

/**
 * Utility type for reducing complex type declarations to ones suitable for
 * determining assignability based on RequestFactory's type-mapping semantics.
 * <p>
 * Rules:
 * <ul>
 * <li>primitive type {@code ->} boxed type (optional)</li>
 * <li>{@code void -> Void} (optional)</li>
 * <li>{@code <T extends Foo> -> Foo}</li>
 * <li>{@code ? extends Foo -> Foo}</li>
 * <li>{@code Foo<complex type> -> Foo<simplified type>}</li>
 * </ul>
 */
public class TypeSimplifier extends SimpleTypeVisitor6<TypeMirror, State> {

    public static TypeMirror simplify(TypeMirror toBox, boolean boxPrimitives, State state) {
        if (toBox == null) {
            return null;
        }
        return toBox.accept(new TypeSimplifier(boxPrimitives), state);
    }

    private final boolean boxPrimitives;

    private TypeSimplifier(boolean boxPrimitives) {
        this.boxPrimitives = boxPrimitives;
    }

    @Override
    public TypeMirror visitDeclared(DeclaredType x, State state) {
        if (x.getTypeArguments().isEmpty()) {
            return x;
        }
        List<TypeMirror> newArgs = new ArrayList<TypeMirror>(x.getTypeArguments().size());
        for (TypeMirror original : x.getTypeArguments()) {
            if (original.getKind().equals(TypeKind.TYPEVAR) && state.types.isAssignable(original, x)) {
                return state.types.getDeclaredType((TypeElement) x.asElement());
            } else {
                newArgs.add(original.accept(this, state));
            }
        }
        return state.types.getDeclaredType((TypeElement) x.asElement(), newArgs.toArray(new TypeMirror[newArgs.size()]));
    }

    @Override
    public TypeMirror visitNoType(NoType x, State state) {
        if (boxPrimitives) {
            return state.findType(Void.class);
        }
        return x;
    }

    @Override
    public TypeMirror visitPrimitive(PrimitiveType x, State state) {
        if (boxPrimitives) {
            return state.types.boxedClass(x).asType();
        }
        return x;
    }

    @Override
    public TypeMirror visitTypeVariable(TypeVariable x, State state) {
        if (x.equals(x.getUpperBound())) {
            return state.types.erasure(x);
        }
        return x.getUpperBound().accept(this, state);
    }

    @Override
    public TypeMirror visitWildcard(WildcardType x, State state) {
        return state.types.erasure(x).accept(this, state);
    }

    @Override
    protected TypeMirror defaultAction(TypeMirror x, State state) {
        return state.types.getNoType(TypeKind.NONE);
    }
}
