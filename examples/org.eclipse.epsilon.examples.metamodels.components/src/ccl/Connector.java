/**
 */
package ccl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Connector</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ccl.Connector#getSource <em>Source</em>}</li>
 *   <li>{@link ccl.Connector#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @see ccl.CclPackage#getConnector()
 * @model
 * @generated
 */
public interface Connector extends EObject {
	/**
	 * Returns the value of the '<em><b>Source</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link ccl.OutPort#getConnector <em>Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' reference.
	 * @see #setSource(OutPort)
	 * @see ccl.CclPackage#getConnector_Source()
	 * @see ccl.OutPort#getConnector
	 * @model opposite="connector"
	 * @generated
	 */
	OutPort getSource();

	/**
	 * Sets the value of the '{@link ccl.Connector#getSource <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' reference.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(OutPort value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link ccl.InPort#getConnector <em>Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(InPort)
	 * @see ccl.CclPackage#getConnector_Target()
	 * @see ccl.InPort#getConnector
	 * @model opposite="connector"
	 * @generated
	 */
	InPort getTarget();

	/**
	 * Sets the value of the '{@link ccl.Connector#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(InPort value);

} // Connector
