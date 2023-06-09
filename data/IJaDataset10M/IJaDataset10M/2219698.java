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
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.uml2.common.util.CacheAdapter;
import org.eclipse.uml2.common.util.DerivedUnionEObjectEList;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Connector;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageEnd;
import org.eclipse.uml2.uml.MessageKind;
import org.eclipse.uml2.uml.MessageSort;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.StringExpression;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.internal.operations.MessageOperations;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Message</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.MessageImpl#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.MessageImpl#getOwnedElements <em>Owned Element</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.MessageImpl#getMessageKind <em>Message Kind</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.MessageImpl#getMessageSort <em>Message Sort</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.MessageImpl#getReceiveEvent <em>Receive Event</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.MessageImpl#getSendEvent <em>Send Event</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.MessageImpl#getConnector <em>Connector</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.MessageImpl#getInteraction <em>Interaction</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.MessageImpl#getArguments <em>Argument</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.MessageImpl#getSignature <em>Signature</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MessageImpl extends NamedElementImpl implements Message {

    /**
	 * The default value of the '{@link #getMessageKind() <em>Message Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMessageKind()
	 * @generated
	 * @ordered
	 */
    protected static final MessageKind MESSAGE_KIND_EDEFAULT = MessageKind.UNKNOWN_LITERAL;

    /**
	 * The default value of the '{@link #getMessageSort() <em>Message Sort</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMessageSort()
	 * @generated
	 * @ordered
	 */
    protected static final MessageSort MESSAGE_SORT_EDEFAULT = MessageSort.SYNCH_CALL_LITERAL;

    /**
	 * The offset of the flags representing the value of the '{@link #getMessageSort() <em>Message Sort</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected static final int MESSAGE_SORT_EFLAG_OFFSET = 12;

    /**
	 * The flags representing the default value of the '{@link #getMessageSort() <em>Message Sort</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected static final int MESSAGE_SORT_EFLAG_DEFAULT = MESSAGE_SORT_EDEFAULT.ordinal() << MESSAGE_SORT_EFLAG_OFFSET;

    /**
	 * The array of enumeration values for '{@link MessageSort Message Sort}'
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    private static final MessageSort[] MESSAGE_SORT_EFLAG_VALUES = MessageSort.values();

    /**
	 * The flags representing the value of the '{@link #getMessageSort() <em>Message Sort</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMessageSort()
	 * @generated
	 * @ordered
	 */
    protected static final int MESSAGE_SORT_EFLAG = 0x7 << MESSAGE_SORT_EFLAG_OFFSET;

    /**
	 * The cached value of the '{@link #getReceiveEvent() <em>Receive Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReceiveEvent()
	 * @generated
	 * @ordered
	 */
    protected MessageEnd receiveEvent;

    /**
	 * The cached value of the '{@link #getSendEvent() <em>Send Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSendEvent()
	 * @generated
	 * @ordered
	 */
    protected MessageEnd sendEvent;

    /**
	 * The cached value of the '{@link #getConnector() <em>Connector</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnector()
	 * @generated
	 * @ordered
	 */
    protected Connector connector;

    /**
	 * The cached value of the '{@link #getArguments() <em>Argument</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArguments()
	 * @generated
	 * @ordered
	 */
    protected EList<ValueSpecification> arguments;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected MessageImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return UMLPackage.Literals.MESSAGE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Namespace getNamespace() {
        Namespace namespace = basicGetNamespace();
        return namespace != null && namespace.eIsProxy() ? (Namespace) eResolveProxy((InternalEObject) namespace) : namespace;
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
                cache.put(eResource, this, UMLPackage.Literals.ELEMENT__OWNED_ELEMENT, ownedElements = new DerivedUnionEObjectEList<Element>(Element.class, this, UMLPackage.MESSAGE__OWNED_ELEMENT, OWNED_ELEMENT_ESUBSETS));
            }
            return ownedElements;
        }
        return new DerivedUnionEObjectEList<Element>(Element.class, this, UMLPackage.MESSAGE__OWNED_ELEMENT, OWNED_ELEMENT_ESUBSETS);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public MessageKind getMessageKind() {
        return MessageOperations.getMessageKind(this);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public MessageSort getMessageSort() {
        return MESSAGE_SORT_EFLAG_VALUES[(eFlags & MESSAGE_SORT_EFLAG) >>> MESSAGE_SORT_EFLAG_OFFSET];
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMessageSort(MessageSort newMessageSort) {
        MessageSort oldMessageSort = MESSAGE_SORT_EFLAG_VALUES[(eFlags & MESSAGE_SORT_EFLAG) >>> MESSAGE_SORT_EFLAG_OFFSET];
        if (newMessageSort == null) newMessageSort = MESSAGE_SORT_EDEFAULT;
        eFlags = eFlags & ~MESSAGE_SORT_EFLAG | newMessageSort.ordinal() << MESSAGE_SORT_EFLAG_OFFSET;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UMLPackage.MESSAGE__MESSAGE_SORT, oldMessageSort, newMessageSort));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public MessageEnd getReceiveEvent() {
        if (receiveEvent != null && receiveEvent.eIsProxy()) {
            InternalEObject oldReceiveEvent = (InternalEObject) receiveEvent;
            receiveEvent = (MessageEnd) eResolveProxy(oldReceiveEvent);
            if (receiveEvent != oldReceiveEvent) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, UMLPackage.MESSAGE__RECEIVE_EVENT, oldReceiveEvent, receiveEvent));
            }
        }
        return receiveEvent;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public MessageEnd basicGetReceiveEvent() {
        return receiveEvent;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setReceiveEvent(MessageEnd newReceiveEvent) {
        MessageEnd oldReceiveEvent = receiveEvent;
        receiveEvent = newReceiveEvent;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UMLPackage.MESSAGE__RECEIVE_EVENT, oldReceiveEvent, receiveEvent));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public MessageEnd getSendEvent() {
        if (sendEvent != null && sendEvent.eIsProxy()) {
            InternalEObject oldSendEvent = (InternalEObject) sendEvent;
            sendEvent = (MessageEnd) eResolveProxy(oldSendEvent);
            if (sendEvent != oldSendEvent) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, UMLPackage.MESSAGE__SEND_EVENT, oldSendEvent, sendEvent));
            }
        }
        return sendEvent;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public MessageEnd basicGetSendEvent() {
        return sendEvent;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSendEvent(MessageEnd newSendEvent) {
        MessageEnd oldSendEvent = sendEvent;
        sendEvent = newSendEvent;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UMLPackage.MESSAGE__SEND_EVENT, oldSendEvent, sendEvent));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Connector getConnector() {
        if (connector != null && connector.eIsProxy()) {
            InternalEObject oldConnector = (InternalEObject) connector;
            connector = (Connector) eResolveProxy(oldConnector);
            if (connector != oldConnector) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, UMLPackage.MESSAGE__CONNECTOR, oldConnector, connector));
            }
        }
        return connector;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Connector basicGetConnector() {
        return connector;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setConnector(Connector newConnector) {
        Connector oldConnector = connector;
        connector = newConnector;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UMLPackage.MESSAGE__CONNECTOR, oldConnector, connector));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Interaction getInteraction() {
        if (eContainerFeatureID() != UMLPackage.MESSAGE__INTERACTION) return null;
        return (Interaction) eContainer();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Interaction basicGetInteraction() {
        if (eContainerFeatureID() != UMLPackage.MESSAGE__INTERACTION) return null;
        return (Interaction) eInternalContainer();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetInteraction(Interaction newInteraction, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newInteraction, UMLPackage.MESSAGE__INTERACTION, msgs);
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setInteraction(Interaction newInteraction) {
        if (newInteraction != eInternalContainer() || (eContainerFeatureID() != UMLPackage.MESSAGE__INTERACTION && newInteraction != null)) {
            if (EcoreUtil.isAncestor(this, newInteraction)) throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
            if (newInteraction != null) msgs = ((InternalEObject) newInteraction).eInverseAdd(this, UMLPackage.INTERACTION__MESSAGE, Interaction.class, msgs);
            msgs = basicSetInteraction(newInteraction, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UMLPackage.MESSAGE__INTERACTION, newInteraction, newInteraction));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<ValueSpecification> getArguments() {
        if (arguments == null) {
            arguments = new EObjectContainmentEList.Resolving<ValueSpecification>(ValueSpecification.class, this, UMLPackage.MESSAGE__ARGUMENT);
        }
        return arguments;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ValueSpecification createArgument(String name, Type type, EClass eClass) {
        ValueSpecification newArgument = (ValueSpecification) create(eClass);
        getArguments().add(newArgument);
        if (name != null) newArgument.setName(name);
        if (type != null) newArgument.setType(type);
        return newArgument;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ValueSpecification getArgument(String name, Type type) {
        return getArgument(name, type, false, null, false);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ValueSpecification getArgument(String name, Type type, boolean ignoreCase, EClass eClass, boolean createOnDemand) {
        argumentLoop: for (ValueSpecification argument : getArguments()) {
            if (eClass != null && !eClass.isInstance(argument)) continue argumentLoop;
            if (name != null && !(ignoreCase ? name.equalsIgnoreCase(argument.getName()) : name.equals(argument.getName()))) continue argumentLoop;
            if (type != null && !type.equals(argument.getType())) continue argumentLoop;
            return argument;
        }
        return createOnDemand && eClass != null ? createArgument(name, type, eClass) : null;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NamedElement getSignature() {
        NamedElement signature = basicGetSignature();
        return signature != null && signature.eIsProxy() ? (NamedElement) eResolveProxy((InternalEObject) signature) : signature;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NamedElement basicGetSignature() {
        return MessageOperations.getSignature(this);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean validateSendingReceivingMessageEvent(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return MessageOperations.validateSendingReceivingMessageEvent(this, diagnostics, context);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean validateSignatureReferTo(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return MessageOperations.validateSignatureReferTo(this, diagnostics, context);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean validateSignatureIsOperation(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return MessageOperations.validateSignatureIsOperation(this, diagnostics, context);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean validateSignatureIsSignal(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return MessageOperations.validateSignatureIsSignal(this, diagnostics, context);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean validateArguments(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return MessageOperations.validateArguments(this, diagnostics, context);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean validateCannotCrossBoundaries(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return MessageOperations.validateCannotCrossBoundaries(this, diagnostics, context);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean validateOccurrenceSpecifications(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return MessageOperations.validateOccurrenceSpecifications(this, diagnostics, context);
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
            case UMLPackage.MESSAGE__EANNOTATIONS:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getEAnnotations()).basicAdd(otherEnd, msgs);
            case UMLPackage.MESSAGE__CLIENT_DEPENDENCY:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getClientDependencies()).basicAdd(otherEnd, msgs);
            case UMLPackage.MESSAGE__INTERACTION:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return basicSetInteraction((Interaction) otherEnd, msgs);
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
            case UMLPackage.MESSAGE__EANNOTATIONS:
                return ((InternalEList<?>) getEAnnotations()).basicRemove(otherEnd, msgs);
            case UMLPackage.MESSAGE__OWNED_COMMENT:
                return ((InternalEList<?>) getOwnedComments()).basicRemove(otherEnd, msgs);
            case UMLPackage.MESSAGE__CLIENT_DEPENDENCY:
                return ((InternalEList<?>) getClientDependencies()).basicRemove(otherEnd, msgs);
            case UMLPackage.MESSAGE__NAME_EXPRESSION:
                return basicSetNameExpression(null, msgs);
            case UMLPackage.MESSAGE__INTERACTION:
                return basicSetInteraction(null, msgs);
            case UMLPackage.MESSAGE__ARGUMENT:
                return ((InternalEList<?>) getArguments()).basicRemove(otherEnd, msgs);
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
            case UMLPackage.MESSAGE__INTERACTION:
                return eInternalContainer().eInverseRemove(this, UMLPackage.INTERACTION__MESSAGE, Interaction.class, msgs);
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
            case UMLPackage.MESSAGE__EANNOTATIONS:
                return getEAnnotations();
            case UMLPackage.MESSAGE__OWNED_ELEMENT:
                return getOwnedElements();
            case UMLPackage.MESSAGE__OWNER:
                if (resolve) return getOwner();
                return basicGetOwner();
            case UMLPackage.MESSAGE__OWNED_COMMENT:
                return getOwnedComments();
            case UMLPackage.MESSAGE__NAME:
                return getName();
            case UMLPackage.MESSAGE__VISIBILITY:
                return getVisibility();
            case UMLPackage.MESSAGE__QUALIFIED_NAME:
                return getQualifiedName();
            case UMLPackage.MESSAGE__CLIENT_DEPENDENCY:
                return getClientDependencies();
            case UMLPackage.MESSAGE__NAMESPACE:
                if (resolve) return getNamespace();
                return basicGetNamespace();
            case UMLPackage.MESSAGE__NAME_EXPRESSION:
                if (resolve) return getNameExpression();
                return basicGetNameExpression();
            case UMLPackage.MESSAGE__MESSAGE_KIND:
                return getMessageKind();
            case UMLPackage.MESSAGE__MESSAGE_SORT:
                return getMessageSort();
            case UMLPackage.MESSAGE__RECEIVE_EVENT:
                if (resolve) return getReceiveEvent();
                return basicGetReceiveEvent();
            case UMLPackage.MESSAGE__SEND_EVENT:
                if (resolve) return getSendEvent();
                return basicGetSendEvent();
            case UMLPackage.MESSAGE__CONNECTOR:
                if (resolve) return getConnector();
                return basicGetConnector();
            case UMLPackage.MESSAGE__INTERACTION:
                if (resolve) return getInteraction();
                return basicGetInteraction();
            case UMLPackage.MESSAGE__ARGUMENT:
                return getArguments();
            case UMLPackage.MESSAGE__SIGNATURE:
                if (resolve) return getSignature();
                return basicGetSignature();
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
            case UMLPackage.MESSAGE__EANNOTATIONS:
                getEAnnotations().clear();
                getEAnnotations().addAll((Collection<? extends EAnnotation>) newValue);
                return;
            case UMLPackage.MESSAGE__OWNED_COMMENT:
                getOwnedComments().clear();
                getOwnedComments().addAll((Collection<? extends Comment>) newValue);
                return;
            case UMLPackage.MESSAGE__NAME:
                setName((String) newValue);
                return;
            case UMLPackage.MESSAGE__VISIBILITY:
                setVisibility((VisibilityKind) newValue);
                return;
            case UMLPackage.MESSAGE__CLIENT_DEPENDENCY:
                getClientDependencies().clear();
                getClientDependencies().addAll((Collection<? extends Dependency>) newValue);
                return;
            case UMLPackage.MESSAGE__NAME_EXPRESSION:
                setNameExpression((StringExpression) newValue);
                return;
            case UMLPackage.MESSAGE__MESSAGE_SORT:
                setMessageSort((MessageSort) newValue);
                return;
            case UMLPackage.MESSAGE__RECEIVE_EVENT:
                setReceiveEvent((MessageEnd) newValue);
                return;
            case UMLPackage.MESSAGE__SEND_EVENT:
                setSendEvent((MessageEnd) newValue);
                return;
            case UMLPackage.MESSAGE__CONNECTOR:
                setConnector((Connector) newValue);
                return;
            case UMLPackage.MESSAGE__INTERACTION:
                setInteraction((Interaction) newValue);
                return;
            case UMLPackage.MESSAGE__ARGUMENT:
                getArguments().clear();
                getArguments().addAll((Collection<? extends ValueSpecification>) newValue);
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
            case UMLPackage.MESSAGE__EANNOTATIONS:
                getEAnnotations().clear();
                return;
            case UMLPackage.MESSAGE__OWNED_COMMENT:
                getOwnedComments().clear();
                return;
            case UMLPackage.MESSAGE__NAME:
                unsetName();
                return;
            case UMLPackage.MESSAGE__VISIBILITY:
                unsetVisibility();
                return;
            case UMLPackage.MESSAGE__CLIENT_DEPENDENCY:
                getClientDependencies().clear();
                return;
            case UMLPackage.MESSAGE__NAME_EXPRESSION:
                setNameExpression((StringExpression) null);
                return;
            case UMLPackage.MESSAGE__MESSAGE_SORT:
                setMessageSort(MESSAGE_SORT_EDEFAULT);
                return;
            case UMLPackage.MESSAGE__RECEIVE_EVENT:
                setReceiveEvent((MessageEnd) null);
                return;
            case UMLPackage.MESSAGE__SEND_EVENT:
                setSendEvent((MessageEnd) null);
                return;
            case UMLPackage.MESSAGE__CONNECTOR:
                setConnector((Connector) null);
                return;
            case UMLPackage.MESSAGE__INTERACTION:
                setInteraction((Interaction) null);
                return;
            case UMLPackage.MESSAGE__ARGUMENT:
                getArguments().clear();
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
            case UMLPackage.MESSAGE__EANNOTATIONS:
                return eAnnotations != null && !eAnnotations.isEmpty();
            case UMLPackage.MESSAGE__OWNED_ELEMENT:
                return isSetOwnedElements();
            case UMLPackage.MESSAGE__OWNER:
                return isSetOwner();
            case UMLPackage.MESSAGE__OWNED_COMMENT:
                return ownedComments != null && !ownedComments.isEmpty();
            case UMLPackage.MESSAGE__NAME:
                return isSetName();
            case UMLPackage.MESSAGE__VISIBILITY:
                return isSetVisibility();
            case UMLPackage.MESSAGE__QUALIFIED_NAME:
                return QUALIFIED_NAME_EDEFAULT == null ? getQualifiedName() != null : !QUALIFIED_NAME_EDEFAULT.equals(getQualifiedName());
            case UMLPackage.MESSAGE__CLIENT_DEPENDENCY:
                return clientDependencies != null && !clientDependencies.isEmpty();
            case UMLPackage.MESSAGE__NAMESPACE:
                return isSetNamespace();
            case UMLPackage.MESSAGE__NAME_EXPRESSION:
                return nameExpression != null;
            case UMLPackage.MESSAGE__MESSAGE_KIND:
                return getMessageKind() != MESSAGE_KIND_EDEFAULT;
            case UMLPackage.MESSAGE__MESSAGE_SORT:
                return (eFlags & MESSAGE_SORT_EFLAG) != MESSAGE_SORT_EFLAG_DEFAULT;
            case UMLPackage.MESSAGE__RECEIVE_EVENT:
                return receiveEvent != null;
            case UMLPackage.MESSAGE__SEND_EVENT:
                return sendEvent != null;
            case UMLPackage.MESSAGE__CONNECTOR:
                return connector != null;
            case UMLPackage.MESSAGE__INTERACTION:
                return basicGetInteraction() != null;
            case UMLPackage.MESSAGE__ARGUMENT:
                return arguments != null && !arguments.isEmpty();
            case UMLPackage.MESSAGE__SIGNATURE:
                return basicGetSignature() != null;
        }
        return eDynamicIsSet(featureID);
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
        result.append(" (messageSort: ");
        result.append(MESSAGE_SORT_EFLAG_VALUES[(eFlags & MESSAGE_SORT_EFLAG) >>> MESSAGE_SORT_EFLAG_OFFSET]);
        result.append(')');
        return result.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Namespace basicGetNamespace() {
        Interaction interaction = basicGetInteraction();
        if (interaction != null) {
            return interaction;
        }
        return super.basicGetNamespace();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean isSetNamespace() {
        return super.isSetNamespace() || eIsSet(UMLPackage.MESSAGE__INTERACTION);
    }

    /**
	 * The array of subset feature identifiers for the '{@link #getOwnedElements() <em>Owned Element</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedElements()
	 * @generated
	 * @ordered
	 */
    protected static final int[] OWNED_ELEMENT_ESUBSETS = new int[] { UMLPackage.MESSAGE__OWNED_COMMENT, UMLPackage.MESSAGE__NAME_EXPRESSION, UMLPackage.MESSAGE__ARGUMENT };

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean isSetOwnedElements() {
        return super.isSetOwnedElements() || eIsSet(UMLPackage.MESSAGE__ARGUMENT);
    }
}
