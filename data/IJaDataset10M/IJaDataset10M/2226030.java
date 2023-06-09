package org.emftext.language.models.resource.model.analysis;

public class ModelBLUETokenResolver implements org.emftext.language.models.resource.model.IModelTokenResolver {

    private org.emftext.language.models.resource.model.analysis.ModelDefaultTokenResolver defaultTokenResolver = new org.emftext.language.models.resource.model.analysis.ModelDefaultTokenResolver();

    public java.lang.String deResolve(java.lang.Object value, org.eclipse.emf.ecore.EStructuralFeature feature, org.eclipse.emf.ecore.EObject container) {
        java.lang.String result = defaultTokenResolver.deResolve(value, feature, container);
        return result;
    }

    public void resolve(java.lang.String lexem, org.eclipse.emf.ecore.EStructuralFeature feature, org.emftext.language.models.resource.model.IModelTokenResolveResult result) {
        defaultTokenResolver.resolve(lexem, feature, result);
    }

    public void setOptions(java.util.Map<?, ?> options) {
        defaultTokenResolver.setOptions(options);
    }
}
