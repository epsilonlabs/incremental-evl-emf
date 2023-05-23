/**
 */
package trace;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link trace.Constraint#getRaw <em>Raw</em>}</li>
 * </ul>
 *
 * @see trace.TracePackage#getConstraint()
 * @model
 * @generated
 */
public interface Constraint extends EObject {
	/**
	 * Returns the value of the '<em><b>Raw</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Raw</em>' attribute.
	 * @see #setRaw(Object)
	 * @see trace.TracePackage#getConstraint_Raw()
	 * @model
	 * @generated
	 */
	Object getRaw();

	/**
	 * Sets the value of the '{@link trace.Constraint#getRaw <em>Raw</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Raw</em>' attribute.
	 * @see #getRaw()
	 * @generated
	 */
	void setRaw(Object value);

} // Constraint
