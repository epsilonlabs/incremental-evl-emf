/**
 */
package flowcharts;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link flowcharts.Node#getOut <em>Out</em>}</li>
 *   <li>{@link flowcharts.Node#getIn <em>In</em>}</li>
 * </ul>
 *
 * @see flowcharts.FlowchartsPackage#getNode()
 * @model abstract="true"
 * @generated
 */
public interface Node extends EObject {
	/**
	 * Returns the value of the '<em><b>Out</b></em>' reference list.
	 * The list contents are of type {@link flowcharts.Edge}.
	 * It is bidirectional and its opposite is '{@link flowcharts.Edge#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Out</em>' reference list.
	 * @see flowcharts.FlowchartsPackage#getNode_Out()
	 * @see flowcharts.Edge#getSource
	 * @model opposite="source"
	 * @generated
	 */
	EList<Edge> getOut();

	/**
	 * Returns the value of the '<em><b>In</b></em>' reference list.
	 * The list contents are of type {@link flowcharts.Edge}.
	 * It is bidirectional and its opposite is '{@link flowcharts.Edge#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>In</em>' reference list.
	 * @see flowcharts.FlowchartsPackage#getNode_In()
	 * @see flowcharts.Edge#getTarget
	 * @model opposite="target"
	 * @generated
	 */
	EList<Edge> getIn();

} // Node
