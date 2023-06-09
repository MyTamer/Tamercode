package net.jonbuck.tassoo.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Types</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.jonbuck.tassoo.model.Types#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.jonbuck.tassoo.model.TassooPackage#getTypes()
 * @model
 * @generated
 */
public interface Types extends EObject {

    /**
	 * Returns the value of the '<em><b>Type</b></em>' containment reference list.
	 * The list contents are of type {@link net.jonbuck.tassoo.model.Type}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' containment reference list.
	 * @see net.jonbuck.tassoo.model.TassooPackage#getTypes_Type()
	 * @model containment="true"
	 * @generated
	 */
    EList<Type> getType();
}
