package org.xvr.s3D.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.xvr.s3D.S3DPackage;
import org.xvr.s3D.varMemberDecl;
import org.xvr.s3D.varMemberDeclList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>var Member Decl List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.xvr.s3D.impl.varMemberDeclListImpl#getDecls <em>Decls</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class varMemberDeclListImpl extends MinimalEObjectImpl.Container implements varMemberDeclList {

    /**
   * The cached value of the '{@link #getDecls() <em>Decls</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDecls()
   * @generated
   * @ordered
   */
    protected EList<varMemberDecl> decls;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected varMemberDeclListImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return S3DPackage.Literals.VAR_MEMBER_DECL_LIST;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EList<varMemberDecl> getDecls() {
        if (decls == null) {
            decls = new EObjectContainmentEList<varMemberDecl>(varMemberDecl.class, this, S3DPackage.VAR_MEMBER_DECL_LIST__DECLS);
        }
        return decls;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case S3DPackage.VAR_MEMBER_DECL_LIST__DECLS:
                return ((InternalEList<?>) getDecls()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case S3DPackage.VAR_MEMBER_DECL_LIST__DECLS:
                return getDecls();
        }
        return super.eGet(featureID, resolve, coreType);
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
            case S3DPackage.VAR_MEMBER_DECL_LIST__DECLS:
                getDecls().clear();
                getDecls().addAll((Collection<? extends varMemberDecl>) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case S3DPackage.VAR_MEMBER_DECL_LIST__DECLS:
                getDecls().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case S3DPackage.VAR_MEMBER_DECL_LIST__DECLS:
                return decls != null && !decls.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
