package org.eclipse.bpel.model.impl;

import java.util.Collection;
import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Branches;
import org.eclipse.bpel.model.CompletionCondition;
import org.eclipse.bpel.model.Documentation;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Completion Condition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.bpel.model.impl.CompletionConditionImpl#getBranches <em>Branches</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CompletionConditionImpl extends ExtensibleElementImpl implements CompletionCondition {

    /**
	 * The cached value of the '{@link #getBranches() <em>Branches</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBranches()
	 * @generated
	 * @ordered
	 */
    protected Branches branches;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected CompletionConditionImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return BPELPackage.Literals.COMPLETION_CONDITION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Branches getBranches() {
        return branches;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetBranches(Branches newBranches, NotificationChain msgs) {
        Branches oldBranches = branches;
        branches = newBranches;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BPELPackage.COMPLETION_CONDITION__BRANCHES, oldBranches, newBranches);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setBranches(Branches newBranches) {
        if (newBranches != branches) {
            NotificationChain msgs = null;
            if (branches != null) msgs = ((InternalEObject) branches).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BPELPackage.COMPLETION_CONDITION__BRANCHES, null, msgs);
            if (newBranches != null) msgs = ((InternalEObject) newBranches).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BPELPackage.COMPLETION_CONDITION__BRANCHES, null, msgs);
            msgs = basicSetBranches(newBranches, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, BPELPackage.COMPLETION_CONDITION__BRANCHES, newBranches, newBranches));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case BPELPackage.COMPLETION_CONDITION__BRANCHES:
                return basicSetBranches(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case BPELPackage.COMPLETION_CONDITION__BRANCHES:
                return getBranches();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case BPELPackage.COMPLETION_CONDITION__BRANCHES:
                setBranches((Branches) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(int featureID) {
        switch(featureID) {
            case BPELPackage.COMPLETION_CONDITION__BRANCHES:
                setBranches((Branches) null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case BPELPackage.COMPLETION_CONDITION__BRANCHES:
                return branches != null;
        }
        return super.eIsSet(featureID);
    }
}
