/**
 */
package ccl;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>In Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ccl.InPort#getConnector <em>Connector</em>}</li>
 * </ul>
 *
 * @see ccl.CclPackage#getInPort()
 * @model
 * @generated
 */
public interface InPort extends Port {

	/**
	 * Returns the value of the '<em><b>Connector</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link ccl.Connector#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connector</em>' reference.
	 * @see #setConnector(Connector)
	 * @see ccl.CclPackage#getInPort_Connector()
	 * @see ccl.Connector#getTarget
	 * @model opposite="target"
	 * @generated
	 */
	Connector getConnector();

	/**
	 * Sets the value of the '{@link ccl.InPort#getConnector <em>Connector</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Connector</em>' reference.
	 * @see #getConnector()
	 * @generated
	 */
	void setConnector(Connector value);
} // InPort
