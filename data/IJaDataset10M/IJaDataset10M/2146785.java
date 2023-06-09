package org.hl7.v3.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.hl7.v3.BL1;
import org.hl7.v3.CE;
import org.hl7.v3.CS1;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040InfrastructureRootTypeId;
import org.hl7.v3.POCDMT000040LanguageCommunication;
import org.hl7.v3.V3Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>POCDMT000040 Language Communication</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040LanguageCommunicationImpl#getRealmCode <em>Realm Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040LanguageCommunicationImpl#getTypeId <em>Type Id</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040LanguageCommunicationImpl#getTemplateId <em>Template Id</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040LanguageCommunicationImpl#getLanguageCode <em>Language Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040LanguageCommunicationImpl#getModeCode <em>Mode Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040LanguageCommunicationImpl#getProficiencyLevelCode <em>Proficiency Level Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040LanguageCommunicationImpl#getPreferenceInd <em>Preference Ind</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040LanguageCommunicationImpl#getNullFlavor <em>Null Flavor</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class POCDMT000040LanguageCommunicationImpl extends EObjectImpl implements POCDMT000040LanguageCommunication {

    /**
	 * The cached value of the '{@link #getRealmCode() <em>Realm Code</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRealmCode()
	 * @generated
	 * @ordered
	 */
    protected EList<CS1> realmCode;

    /**
	 * The cached value of the '{@link #getTypeId() <em>Type Id</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeId()
	 * @generated
	 * @ordered
	 */
    protected POCDMT000040InfrastructureRootTypeId typeId;

    /**
	 * The cached value of the '{@link #getTemplateId() <em>Template Id</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemplateId()
	 * @generated
	 * @ordered
	 */
    protected EList<II> templateId;

    /**
	 * The cached value of the '{@link #getLanguageCode() <em>Language Code</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLanguageCode()
	 * @generated
	 * @ordered
	 */
    protected CS1 languageCode;

    /**
	 * The cached value of the '{@link #getModeCode() <em>Mode Code</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModeCode()
	 * @generated
	 * @ordered
	 */
    protected CE modeCode;

    /**
	 * The cached value of the '{@link #getProficiencyLevelCode() <em>Proficiency Level Code</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProficiencyLevelCode()
	 * @generated
	 * @ordered
	 */
    protected CE proficiencyLevelCode;

    /**
	 * The cached value of the '{@link #getPreferenceInd() <em>Preference Ind</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPreferenceInd()
	 * @generated
	 * @ordered
	 */
    protected BL1 preferenceInd;

    /**
	 * The default value of the '{@link #getNullFlavor() <em>Null Flavor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNullFlavor()
	 * @generated
	 * @ordered
	 */
    protected static final Enumerator NULL_FLAVOR_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getNullFlavor() <em>Null Flavor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNullFlavor()
	 * @generated
	 * @ordered
	 */
    protected Enumerator nullFlavor = NULL_FLAVOR_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected POCDMT000040LanguageCommunicationImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return V3Package.eINSTANCE.getPOCDMT000040LanguageCommunication();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<CS1> getRealmCode() {
        if (realmCode == null) {
            realmCode = new EObjectContainmentEList<CS1>(CS1.class, this, V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__REALM_CODE);
        }
        return realmCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public POCDMT000040InfrastructureRootTypeId getTypeId() {
        return typeId;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTypeId(POCDMT000040InfrastructureRootTypeId newTypeId, NotificationChain msgs) {
        POCDMT000040InfrastructureRootTypeId oldTypeId = typeId;
        typeId = newTypeId;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__TYPE_ID, oldTypeId, newTypeId);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTypeId(POCDMT000040InfrastructureRootTypeId newTypeId) {
        if (newTypeId != typeId) {
            NotificationChain msgs = null;
            if (typeId != null) msgs = ((InternalEObject) typeId).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__TYPE_ID, null, msgs);
            if (newTypeId != null) msgs = ((InternalEObject) newTypeId).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__TYPE_ID, null, msgs);
            msgs = basicSetTypeId(newTypeId, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__TYPE_ID, newTypeId, newTypeId));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<II> getTemplateId() {
        if (templateId == null) {
            templateId = new EObjectContainmentEList<II>(II.class, this, V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__TEMPLATE_ID);
        }
        return templateId;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CS1 getLanguageCode() {
        return languageCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetLanguageCode(CS1 newLanguageCode, NotificationChain msgs) {
        CS1 oldLanguageCode = languageCode;
        languageCode = newLanguageCode;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__LANGUAGE_CODE, oldLanguageCode, newLanguageCode);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLanguageCode(CS1 newLanguageCode) {
        if (newLanguageCode != languageCode) {
            NotificationChain msgs = null;
            if (languageCode != null) msgs = ((InternalEObject) languageCode).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__LANGUAGE_CODE, null, msgs);
            if (newLanguageCode != null) msgs = ((InternalEObject) newLanguageCode).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__LANGUAGE_CODE, null, msgs);
            msgs = basicSetLanguageCode(newLanguageCode, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__LANGUAGE_CODE, newLanguageCode, newLanguageCode));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CE getModeCode() {
        return modeCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetModeCode(CE newModeCode, NotificationChain msgs) {
        CE oldModeCode = modeCode;
        modeCode = newModeCode;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__MODE_CODE, oldModeCode, newModeCode);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setModeCode(CE newModeCode) {
        if (newModeCode != modeCode) {
            NotificationChain msgs = null;
            if (modeCode != null) msgs = ((InternalEObject) modeCode).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__MODE_CODE, null, msgs);
            if (newModeCode != null) msgs = ((InternalEObject) newModeCode).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__MODE_CODE, null, msgs);
            msgs = basicSetModeCode(newModeCode, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__MODE_CODE, newModeCode, newModeCode));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CE getProficiencyLevelCode() {
        return proficiencyLevelCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetProficiencyLevelCode(CE newProficiencyLevelCode, NotificationChain msgs) {
        CE oldProficiencyLevelCode = proficiencyLevelCode;
        proficiencyLevelCode = newProficiencyLevelCode;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PROFICIENCY_LEVEL_CODE, oldProficiencyLevelCode, newProficiencyLevelCode);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setProficiencyLevelCode(CE newProficiencyLevelCode) {
        if (newProficiencyLevelCode != proficiencyLevelCode) {
            NotificationChain msgs = null;
            if (proficiencyLevelCode != null) msgs = ((InternalEObject) proficiencyLevelCode).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PROFICIENCY_LEVEL_CODE, null, msgs);
            if (newProficiencyLevelCode != null) msgs = ((InternalEObject) newProficiencyLevelCode).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PROFICIENCY_LEVEL_CODE, null, msgs);
            msgs = basicSetProficiencyLevelCode(newProficiencyLevelCode, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PROFICIENCY_LEVEL_CODE, newProficiencyLevelCode, newProficiencyLevelCode));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BL1 getPreferenceInd() {
        return preferenceInd;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetPreferenceInd(BL1 newPreferenceInd, NotificationChain msgs) {
        BL1 oldPreferenceInd = preferenceInd;
        preferenceInd = newPreferenceInd;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PREFERENCE_IND, oldPreferenceInd, newPreferenceInd);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPreferenceInd(BL1 newPreferenceInd) {
        if (newPreferenceInd != preferenceInd) {
            NotificationChain msgs = null;
            if (preferenceInd != null) msgs = ((InternalEObject) preferenceInd).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PREFERENCE_IND, null, msgs);
            if (newPreferenceInd != null) msgs = ((InternalEObject) newPreferenceInd).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PREFERENCE_IND, null, msgs);
            msgs = basicSetPreferenceInd(newPreferenceInd, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PREFERENCE_IND, newPreferenceInd, newPreferenceInd));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Enumerator getNullFlavor() {
        return nullFlavor;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setNullFlavor(Enumerator newNullFlavor) {
        Enumerator oldNullFlavor = nullFlavor;
        nullFlavor = newNullFlavor;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__NULL_FLAVOR, oldNullFlavor, nullFlavor));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__REALM_CODE:
                return ((InternalEList<?>) getRealmCode()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__TYPE_ID:
                return basicSetTypeId(null, msgs);
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__TEMPLATE_ID:
                return ((InternalEList<?>) getTemplateId()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__LANGUAGE_CODE:
                return basicSetLanguageCode(null, msgs);
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__MODE_CODE:
                return basicSetModeCode(null, msgs);
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PROFICIENCY_LEVEL_CODE:
                return basicSetProficiencyLevelCode(null, msgs);
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PREFERENCE_IND:
                return basicSetPreferenceInd(null, msgs);
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
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__REALM_CODE:
                return getRealmCode();
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__TYPE_ID:
                return getTypeId();
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__TEMPLATE_ID:
                return getTemplateId();
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__LANGUAGE_CODE:
                return getLanguageCode();
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__MODE_CODE:
                return getModeCode();
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PROFICIENCY_LEVEL_CODE:
                return getProficiencyLevelCode();
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PREFERENCE_IND:
                return getPreferenceInd();
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__NULL_FLAVOR:
                return getNullFlavor();
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
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__REALM_CODE:
                getRealmCode().clear();
                getRealmCode().addAll((Collection<? extends CS1>) newValue);
                return;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__TYPE_ID:
                setTypeId((POCDMT000040InfrastructureRootTypeId) newValue);
                return;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__TEMPLATE_ID:
                getTemplateId().clear();
                getTemplateId().addAll((Collection<? extends II>) newValue);
                return;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__LANGUAGE_CODE:
                setLanguageCode((CS1) newValue);
                return;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__MODE_CODE:
                setModeCode((CE) newValue);
                return;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PROFICIENCY_LEVEL_CODE:
                setProficiencyLevelCode((CE) newValue);
                return;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PREFERENCE_IND:
                setPreferenceInd((BL1) newValue);
                return;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__NULL_FLAVOR:
                setNullFlavor((Enumerator) newValue);
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
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__REALM_CODE:
                getRealmCode().clear();
                return;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__TYPE_ID:
                setTypeId((POCDMT000040InfrastructureRootTypeId) null);
                return;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__TEMPLATE_ID:
                getTemplateId().clear();
                return;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__LANGUAGE_CODE:
                setLanguageCode((CS1) null);
                return;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__MODE_CODE:
                setModeCode((CE) null);
                return;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PROFICIENCY_LEVEL_CODE:
                setProficiencyLevelCode((CE) null);
                return;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PREFERENCE_IND:
                setPreferenceInd((BL1) null);
                return;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__NULL_FLAVOR:
                setNullFlavor(NULL_FLAVOR_EDEFAULT);
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
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__REALM_CODE:
                return realmCode != null && !realmCode.isEmpty();
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__TYPE_ID:
                return typeId != null;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__TEMPLATE_ID:
                return templateId != null && !templateId.isEmpty();
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__LANGUAGE_CODE:
                return languageCode != null;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__MODE_CODE:
                return modeCode != null;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PROFICIENCY_LEVEL_CODE:
                return proficiencyLevelCode != null;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__PREFERENCE_IND:
                return preferenceInd != null;
            case V3Package.POCDMT000040_LANGUAGE_COMMUNICATION__NULL_FLAVOR:
                return NULL_FLAVOR_EDEFAULT == null ? nullFlavor != null : !NULL_FLAVOR_EDEFAULT.equals(nullFlavor);
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (nullFlavor: ");
        result.append(nullFlavor);
        result.append(')');
        return result.toString();
    }
}
