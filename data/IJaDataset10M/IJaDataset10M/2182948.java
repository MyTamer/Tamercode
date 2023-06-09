package org.makumba.devel.eclipse.mdd.scoping;

import static org.eclipse.xtext.scoping.Scopes.scopeFor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider;
import org.makumba.devel.eclipse.mdd.MDDUtils;
import org.makumba.devel.eclipse.mdd.MDD.DataDefinition;
import org.makumba.devel.eclipse.mdd.MDD.Declaration;
import org.makumba.devel.eclipse.mdd.MDD.FieldDeclaration;
import org.makumba.devel.eclipse.mdd.MDD.FieldPath;
import org.makumba.devel.eclipse.mdd.MDD.FieldType;
import org.makumba.devel.eclipse.mdd.MDD.PointerType;
import org.makumba.devel.eclipse.mdd.MDD.SetType;
import org.makumba.devel.eclipse.mdd.MDD.SubFieldDeclaration;
import org.makumba.devel.eclipse.mdd.MDD.ValidationRuleDeclaration;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * This class contains custom scoping description.
 * 
 * see : http://www.eclipse.org/Xtext/documentation/latest/xtext.html#scoping on
 * how and when to use it
 * 
 * @author filip
 * 
 */
public class MDDScopeProvider extends AbstractDeclarativeScopeProvider {

    @Inject
    private Provider<ResourceSet> resourceSet;

    /**
	 * Computes the scope of field path by getting all the field declarations at
	 * the end of the path
	 * 
	 * @param context
	 * @param reference
	 * @return
	 */
    public IScope scope_FieldPath_field(FieldPath context, EReference reference) {
        if (context.eContainer() instanceof FieldPath) {
            FieldPath parent = (FieldPath) context.eContainer();
            if (parent.getField().getTypedef() instanceof PointerType) {
                EcoreUtil2.resolveAll(resourceSet.get());
                DataDefinition pointsTo = (DataDefinition) EcoreUtil2.resolve(((PointerType) parent.getField().getTypedef()).getRef(), resourceSet.get());
                Iterable<FieldDeclaration> fields = Iterables.filter(pointsTo.getD(), FieldDeclaration.class);
                return scopeFor(fields);
            }
        } else {
            EObject current = context.eContainer();
            while (!(current instanceof Declaration) && current != null) {
                current = current.eContainer();
            }
            if (current != null) {
                Iterable<Declaration> declarations = MDDUtils.getSiblingsOf((Declaration) current);
                Iterable<FieldDeclaration> fields = Iterables.filter(declarations, FieldDeclaration.class);
                return scopeFor(fields);
            }
        }
        return null;
    }

    /**
	 * Creates the scope of only the relational and structure fields.
	 * 
	 * @param context
	 * @param reference
	 * @return
	 */
    public IScope scope_SubFieldDeclaration_subFieldOf(SubFieldDeclaration context, EReference reference) {
        Iterable<Declaration> declarations = MDDUtils.getSiblingsOf(context);
        Iterable<FieldDeclaration> fields = Iterables.filter(declarations, FieldDeclaration.class);
        fields = Iterables.filter(fields, new Predicate<FieldDeclaration>() {

            public boolean apply(FieldDeclaration field) {
                return field.getTypedef() instanceof SetType || field.getTypedef() instanceof PointerType;
            }
        });
        return scopeFor(fields);
    }

    /**
	 * Creates the scope of only the fields at the same level as native
	 * validation rule
	 * 
	 * @param context
	 * @param ref
	 * @return
	 */
    public IScope scope_NativeValidationRuleDeclaration_field(DataDefinition context, EReference ref) {
        Iterable<FieldDeclaration> fields = Iterables.filter(context.getD(), FieldDeclaration.class);
        return scopeFor(fields);
    }

    /**
	 * Creates only scope of only the simple type fields.
	 * 
	 * @param context
	 * @param ref
	 * @return
	 */
    public IScope scope_FieldReference_field(ValidationRuleDeclaration context, EReference ref) {
        Iterable<Declaration> declarations = MDDUtils.getSiblingsOf(context);
        Iterable<FieldDeclaration> fields = Iterables.filter(declarations, FieldDeclaration.class);
        fields = Iterables.filter(fields, new Predicate<FieldDeclaration>() {

            public boolean apply(FieldDeclaration field) {
                return !(field.getTypedef() instanceof SetType || field.getTypedef() instanceof PointerType);
            }
        });
        return scopeFor(fields);
    }

    /**
	 * Creates the scope of only the fields at the same level as native
	 * validation rule
	 * 
	 * @param context
	 * @param ref
	 * @return
	 */
    public IScope scope_NativeValidationRuleDeclaration_field(SubFieldDeclaration context, EReference ref) {
        Iterable<Declaration> declarations = MDDUtils.getDeclarationsOf(context);
        Iterable<FieldDeclaration> fields = Iterables.filter(declarations, FieldDeclaration.class);
        return scopeFor(fields);
    }

    /**
	 * Filters the scope to provide only "mdd" data definitions
	 * 
	 * @param context
	 * @param ref
	 * @return
	 */
    public IScope scope_PointerType_ref(FieldType context, EReference ref) {
        IScope result = delegateGetScope(context, ref);
        return new FilteringScope(result, new Predicate<IEObjectDescription>() {

            public boolean apply(IEObjectDescription input) {
                if (input != null) {
                    return input.getEObjectURI().fileExtension().equals("mdd");
                }
                return false;
            }
        });
    }

    public IScope scope_SetType_ref(FieldType context, EReference ref) {
        return scope_PointerType_ref(context, ref);
    }

    /**
	 * Filters the scope to provide only "idd" data definitions
	 * 
	 * @param context
	 * @param ref
	 * @return
	 */
    public IScope scope_IncludeDeclaration_importedNamespace(Declaration context, EReference ref) {
        IScope result = delegateGetScope(context, ref);
        return new FilteringScope(result, new Predicate<IEObjectDescription>() {

            public boolean apply(IEObjectDescription input) {
                if (input != null) {
                    return input.getEObjectURI().fileExtension().equals("idd");
                }
                return false;
            }
        });
    }
}
