/**
 */
package trace;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Access</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link trace.Access#getExecutions <em>Executions</em>}</li>
 * </ul>
 *
 * @see trace.TracePackage#getAccess()
 * @model abstract="true"
 * @generated
 */
public interface Access extends EObject {
	/**
	 * Returns the value of the '<em><b>Executions</b></em>' reference list.
	 * The list contents are of type {@link trace.Execution}.
	 * It is bidirectional and its opposite is '{@link trace.Execution#getAccesses <em>Accesses</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Executions</em>' reference list.
	 * @see trace.TracePackage#getAccess_Executions()
	 * @see trace.Execution#getAccesses
	 * @model opposite="accesses"
	 * @generated
	 */
	EList<Execution> getExecutions();

} // Access
