package org.deltaj.deltaj;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Negation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.deltaj.deltaj.Negation#getFormula <em>Formula</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.deltaj.deltaj.DeltajPackage#getNegation()
 * @model
 * @generated
 */
public interface Negation extends PropositionalFormula {

    /**
   * Returns the value of the '<em><b>Formula</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Formula</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Formula</em>' containment reference.
   * @see #setFormula(PropositionalFormula)
   * @see org.deltaj.deltaj.DeltajPackage#getNegation_Formula()
   * @model containment="true"
   * @generated
   */
    PropositionalFormula getFormula();

    /**
   * Sets the value of the '{@link org.deltaj.deltaj.Negation#getFormula <em>Formula</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Formula</em>' containment reference.
   * @see #getFormula()
   * @generated
   */
    void setFormula(PropositionalFormula value);
}
