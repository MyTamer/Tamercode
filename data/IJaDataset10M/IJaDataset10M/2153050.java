package org.eclipse.uml2.uml.internal.impl;

import java.util.Collection;
import java.util.Map;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.uml2.common.util.CacheAdapter;
import org.eclipse.uml2.common.util.DerivedUnionEObjectEList;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ParameterableElement;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.TemplateParameterSubstitution;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.operations.TemplateParameterSubstitutionOperations;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Template Parameter Substitution</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.TemplateParameterSubstitutionImpl#getOwnedElements <em>Owned Element</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.TemplateParameterSubstitutionImpl#getOwner <em>Owner</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.TemplateParameterSubstitutionImpl#getActual <em>Actual</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.TemplateParameterSubstitutionImpl#getFormal <em>Formal</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.TemplateParameterSubstitutionImpl#getOwnedActual <em>Owned Actual</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.TemplateParameterSubstitutionImpl#getTemplateBinding <em>Template Binding</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TemplateParameterSubstitutionImpl extends ElementImpl implements TemplateParameterSubstitution {

    /**
	 * The cached value of the '{@link #getActual() <em>Actual</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActual()
	 * @generated
	 * @ordered
	 */
    protected ParameterableElement actual;

    /**
	 * The cached value of the '{@link #getFormal() <em>Formal</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFormal()
	 * @generated
	 * @ordered
	 */
    protected TemplateParameter formal;

    /**
	 * The cached value of the '{@link #getOwnedActual() <em>Owned Actual</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedActual()
	 * @generated
	 * @ordered
	 */
    protected ParameterableElement ownedActual;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TemplateParameterSubstitutionImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return UMLPackage.Literals.TEMPLATE_PARAMETER_SUBSTITUTION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public EList<Element> getOwnedElements() {
        CacheAdapter cache = getCacheAdapter();
        if (cache != null) {
            Resource eResource = eResource();
            @SuppressWarnings("unchecked") EList<Element> ownedElements = (EList<Element>) cache.get(eResource, this, UMLPackage.Literals.ELEMENT__OWNED_ELEMENT);
            if (ownedElements == null) {
                cache.put(eResource, this, UMLPackage.Literals.ELEMENT__OWNED_ELEMENT, ownedElements = new DerivedUnionEObjectEList<Element>(Element.class, this, UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ELEMENT, OWNED_ELEMENT_ESUBSETS));
            }
            return ownedElements;
        }
        return new DerivedUnionEObjectEList<Element>(Element.class, this, UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ELEMENT, OWNED_ELEMENT_ESUBSETS);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TemplateParameter getFormal() {
        if (formal != null && formal.eIsProxy()) {
            InternalEObject oldFormal = (InternalEObject) formal;
            formal = (TemplateParameter) eResolveProxy(oldFormal);
            if (formal != oldFormal) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__FORMAL, oldFormal, formal));
            }
        }
        return formal;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TemplateParameter basicGetFormal() {
        return formal;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFormal(TemplateParameter newFormal) {
        TemplateParameter oldFormal = formal;
        formal = newFormal;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__FORMAL, oldFormal, formal));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ParameterableElement getOwnedActual() {
        if (ownedActual != null && ownedActual.eIsProxy()) {
            InternalEObject oldOwnedActual = (InternalEObject) ownedActual;
            ownedActual = (ParameterableElement) eResolveProxy(oldOwnedActual);
            if (ownedActual != oldOwnedActual) {
                InternalEObject newOwnedActual = (InternalEObject) ownedActual;
                NotificationChain msgs = oldOwnedActual.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ACTUAL, null, null);
                if (newOwnedActual.eInternalContainer() == null) {
                    msgs = newOwnedActual.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ACTUAL, null, msgs);
                }
                if (msgs != null) msgs.dispatch();
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ACTUAL, oldOwnedActual, ownedActual));
            }
        }
        return ownedActual;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ParameterableElement basicGetOwnedActual() {
        return ownedActual;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetOwnedActual(ParameterableElement newOwnedActual, NotificationChain msgs) {
        ParameterableElement oldOwnedActual = ownedActual;
        ownedActual = newOwnedActual;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ACTUAL, oldOwnedActual, newOwnedActual);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        Resource.Internal eInternalResource = eInternalResource();
        if (eInternalResource == null || !eInternalResource.isLoading()) {
            if (newOwnedActual != null) {
                if (newOwnedActual != actual) {
                    setActual(newOwnedActual);
                }
            }
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setOwnedActual(ParameterableElement newOwnedActual) {
        if (newOwnedActual != ownedActual) {
            NotificationChain msgs = null;
            if (ownedActual != null) msgs = ((InternalEObject) ownedActual).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ACTUAL, null, msgs);
            if (newOwnedActual != null) msgs = ((InternalEObject) newOwnedActual).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ACTUAL, null, msgs);
            msgs = basicSetOwnedActual(newOwnedActual, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ACTUAL, newOwnedActual, newOwnedActual));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ParameterableElement createOwnedActual(EClass eClass) {
        ParameterableElement newOwnedActual = (ParameterableElement) create(eClass);
        setOwnedActual(newOwnedActual);
        return newOwnedActual;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TemplateBinding getTemplateBinding() {
        if (eContainerFeatureID() != UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__TEMPLATE_BINDING) return null;
        return (TemplateBinding) eContainer();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TemplateBinding basicGetTemplateBinding() {
        if (eContainerFeatureID() != UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__TEMPLATE_BINDING) return null;
        return (TemplateBinding) eInternalContainer();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTemplateBinding(TemplateBinding newTemplateBinding, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newTemplateBinding, UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__TEMPLATE_BINDING, msgs);
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTemplateBinding(TemplateBinding newTemplateBinding) {
        if (newTemplateBinding != eInternalContainer() || (eContainerFeatureID() != UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__TEMPLATE_BINDING && newTemplateBinding != null)) {
            if (EcoreUtil.isAncestor(this, newTemplateBinding)) throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
            if (newTemplateBinding != null) msgs = ((InternalEObject) newTemplateBinding).eInverseAdd(this, UMLPackage.TEMPLATE_BINDING__PARAMETER_SUBSTITUTION, TemplateBinding.class, msgs);
            msgs = basicSetTemplateBinding(newTemplateBinding, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__TEMPLATE_BINDING, newTemplateBinding, newTemplateBinding));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean validateMustBeCompatible(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return TemplateParameterSubstitutionOperations.validateMustBeCompatible(this, diagnostics, context);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__EANNOTATIONS:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getEAnnotations()).basicAdd(otherEnd, msgs);
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__TEMPLATE_BINDING:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return basicSetTemplateBinding((TemplateBinding) otherEnd, msgs);
        }
        return eDynamicInverseAdd(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__EANNOTATIONS:
                return ((InternalEList<?>) getEAnnotations()).basicRemove(otherEnd, msgs);
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_COMMENT:
                return ((InternalEList<?>) getOwnedComments()).basicRemove(otherEnd, msgs);
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ACTUAL:
                return basicSetOwnedActual(null, msgs);
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__TEMPLATE_BINDING:
                return basicSetTemplateBinding(null, msgs);
        }
        return eDynamicInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch(eContainerFeatureID()) {
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__TEMPLATE_BINDING:
                return eInternalContainer().eInverseRemove(this, UMLPackage.TEMPLATE_BINDING__PARAMETER_SUBSTITUTION, TemplateBinding.class, msgs);
        }
        return eDynamicBasicRemoveFromContainer(msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__EANNOTATIONS:
                return getEAnnotations();
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ELEMENT:
                return getOwnedElements();
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNER:
                if (resolve) return getOwner();
                return basicGetOwner();
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_COMMENT:
                return getOwnedComments();
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__FORMAL:
                if (resolve) return getFormal();
                return basicGetFormal();
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__ACTUAL:
                if (resolve) return getActual();
                return basicGetActual();
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ACTUAL:
                if (resolve) return getOwnedActual();
                return basicGetOwnedActual();
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__TEMPLATE_BINDING:
                if (resolve) return getTemplateBinding();
                return basicGetTemplateBinding();
        }
        return eDynamicGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__EANNOTATIONS:
                getEAnnotations().clear();
                getEAnnotations().addAll((Collection<? extends EAnnotation>) newValue);
                return;
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_COMMENT:
                getOwnedComments().clear();
                getOwnedComments().addAll((Collection<? extends Comment>) newValue);
                return;
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__FORMAL:
                setFormal((TemplateParameter) newValue);
                return;
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__ACTUAL:
                setActual((ParameterableElement) newValue);
                return;
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ACTUAL:
                setOwnedActual((ParameterableElement) newValue);
                return;
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__TEMPLATE_BINDING:
                setTemplateBinding((TemplateBinding) newValue);
                return;
        }
        eDynamicSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__EANNOTATIONS:
                getEAnnotations().clear();
                return;
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_COMMENT:
                getOwnedComments().clear();
                return;
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__FORMAL:
                setFormal((TemplateParameter) null);
                return;
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__ACTUAL:
                setActual((ParameterableElement) null);
                return;
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ACTUAL:
                setOwnedActual((ParameterableElement) null);
                return;
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__TEMPLATE_BINDING:
                setTemplateBinding((TemplateBinding) null);
                return;
        }
        eDynamicUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__EANNOTATIONS:
                return eAnnotations != null && !eAnnotations.isEmpty();
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ELEMENT:
                return isSetOwnedElements();
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNER:
                return isSetOwner();
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_COMMENT:
                return ownedComments != null && !ownedComments.isEmpty();
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__FORMAL:
                return formal != null;
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__ACTUAL:
                return actual != null;
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ACTUAL:
                return ownedActual != null;
            case UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__TEMPLATE_BINDING:
                return basicGetTemplateBinding() != null;
        }
        return eDynamicIsSet(featureID);
    }

    /**
	 * The array of subset feature identifiers for the '{@link #getOwnedElements() <em>Owned Element</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedElements()
	 * @generated
	 * @ordered
	 */
    protected static final int[] OWNED_ELEMENT_ESUBSETS = new int[] { UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_COMMENT, UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ACTUAL };

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Element getOwner() {
        Element owner = basicGetOwner();
        return owner != null && owner.eIsProxy() ? (Element) eResolveProxy((InternalEObject) owner) : owner;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean isSetOwnedElements() {
        return super.isSetOwnedElements() || eIsSet(UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__OWNED_ACTUAL);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Element basicGetOwner() {
        TemplateBinding templateBinding = basicGetTemplateBinding();
        if (templateBinding != null) {
            return templateBinding;
        }
        return super.basicGetOwner();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ParameterableElement getActual() {
        if (actual != null && actual.eIsProxy()) {
            InternalEObject oldActual = (InternalEObject) actual;
            actual = (ParameterableElement) eResolveProxy(oldActual);
            if (actual != oldActual) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__ACTUAL, oldActual, actual));
            }
        }
        return actual;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ParameterableElement basicGetActual() {
        return actual;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setActual(ParameterableElement newActual) {
        ParameterableElement oldActual = actual;
        actual = newActual;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__ACTUAL, oldActual, actual));
        Resource.Internal eInternalResource = eInternalResource();
        if (eInternalResource == null || !eInternalResource.isLoading()) {
            if (ownedActual != null && ownedActual != newActual) {
                setOwnedActual(null);
            }
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean isSetOwner() {
        return super.isSetOwner() || eIsSet(UMLPackage.TEMPLATE_PARAMETER_SUBSTITUTION__TEMPLATE_BINDING);
    }
}
