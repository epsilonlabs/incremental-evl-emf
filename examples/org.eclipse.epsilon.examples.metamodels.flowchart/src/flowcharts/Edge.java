/**
 */
package flowcharts;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Edge</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link flowcharts.Edge#getSource <em>Source</em>}</li>
 *   <li>{@link flowcharts.Edge#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @see flowcharts.FlowchartsPackage#getEdge()
 * @model
 * @generated
 */
public interface Edge extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Source</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link flowcharts.Node#getOut <em>Out</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' reference.
	 * @see #setSource(Node)
	 * @see flowcharts.FlowchartsPackage#getEdge_Source()
	 * @see flowcharts.Node#getOut
	 * @model opposite="out"
	 * @generated
	 */
	Node getSource();

	/**
	 * Sets the value of the '{@link flowcharts.Edge#getSource <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' reference.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(Node value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link flowcharts.Node#getIn <em>In</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(Node)
	 * @see flowcharts.FlowchartsPackage#getEdge_Target()
	 * @see flowcharts.Node#getIn
	 * @model opposite="in"
	 * @generated
	 */
	Node getTarget();

	/**
	 * Sets the value of the '{@link flowcharts.Edge#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(Node value);

} // Edge
