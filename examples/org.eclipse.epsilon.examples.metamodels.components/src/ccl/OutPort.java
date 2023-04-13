/**
 */
package ccl;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Out Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ccl.OutPort#getConnector <em>Connector</em>}</li>
 * </ul>
 *
 * @see ccl.CclPackage#getOutPort()
 * @model
 * @generated
 */
public interface OutPort extends Port {

	/**
	 * Returns the value of the '<em><b>Connector</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link ccl.Connector#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connector</em>' reference.
	 * @see #setConnector(Connector)
	 * @see ccl.CclPackage#getOutPort_Connector()
	 * @see ccl.Connector#getSource
	 * @model opposite="source"
	 * @generated
	 */
	Connector getConnector();

	/**
	 * Sets the value of the '{@link ccl.OutPort#getConnector <em>Connector</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Connector</em>' reference.
	 * @see #getConnector()
	 * @generated
	 */
	void setConnector(Connector value);
} // OutPort
