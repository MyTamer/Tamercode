package furniture;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Addition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link furniture.Addition#getValues <em>Values</em>}</li>
 * </ul>
 * </p>
 *
 * @see furniture.FurniturePackage#getAddition()
 * @model
 * @generated
 */
public interface Addition extends Value {

    /**
	 * Returns the value of the '<em><b>Values</b></em>' containment reference list.
	 * The list contents are of type {@link furniture.Value}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Values</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Values</em>' containment reference list.
	 * @see furniture.FurniturePackage#getAddition_Values()
	 * @model containment="true"
	 * @generated
	 */
    EList<Value> getValues();
}
