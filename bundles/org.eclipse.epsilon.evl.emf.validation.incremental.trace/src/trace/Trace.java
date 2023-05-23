/**
 */
package trace;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Trace</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link trace.Trace#getExecutions <em>Executions</em>}</li>
 *   <li>{@link trace.Trace#getAccesses <em>Accesses</em>}</li>
 * </ul>
 *
 * @see trace.TracePackage#getTrace()
 * @model
 * @generated
 */
public interface Trace extends EObject {
	/**
	 * Returns the value of the '<em><b>Executions</b></em>' containment reference list.
	 * The list contents are of type {@link trace.Execution}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Executions</em>' containment reference list.
	 * @see trace.TracePackage#getTrace_Executions()
	 * @model containment="true"
	 * @generated
	 */
	EList<Execution> getExecutions();

	/**
	 * Returns the value of the '<em><b>Accesses</b></em>' containment reference list.
	 * The list contents are of type {@link trace.Access}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Accesses</em>' containment reference list.
	 * @see trace.TracePackage#getTrace_Accesses()
	 * @model containment="true"
	 * @generated
	 */
	EList<Access> getAccesses();

} // Trace
