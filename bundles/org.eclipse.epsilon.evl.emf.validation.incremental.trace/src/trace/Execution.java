/**
 */
package trace;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Execution</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link trace.Execution#getContext <em>Context</em>}</li>
 *   <li>{@link trace.Execution#getAccesses <em>Accesses</em>}</li>
 *   <li>{@link trace.Execution#getDependencies <em>Dependencies</em>}</li>
 * </ul>
 *
 * @see trace.TracePackage#getExecution()
 * @model abstract="true"
 * @generated
 */
public interface Execution extends EObject {
	/**
	 * Returns the value of the '<em><b>Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Context</em>' attribute.
	 * @see #setContext(Object)
	 * @see trace.TracePackage#getExecution_Context()
	 * @model
	 * @generated
	 */
	Object getContext();

	/**
	 * Sets the value of the '{@link trace.Execution#getContext <em>Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context</em>' attribute.
	 * @see #getContext()
	 * @generated
	 */
	void setContext(Object value);

	/**
	 * Returns the value of the '<em><b>Accesses</b></em>' reference list.
	 * The list contents are of type {@link trace.Access}.
	 * It is bidirectional and its opposite is '{@link trace.Access#getExecutions <em>Executions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Accesses</em>' reference list.
	 * @see trace.TracePackage#getExecution_Accesses()
	 * @see trace.Access#getExecutions
	 * @model opposite="executions"
	 * @generated
	 */
	EList<Access> getAccesses();

	/**
	 * Returns the value of the '<em><b>Dependencies</b></em>' reference list.
	 * The list contents are of type {@link trace.Execution}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dependencies</em>' reference list.
	 * @see trace.TracePackage#getExecution_Dependencies()
	 * @model
	 * @generated
	 */
	EList<Execution> getDependencies();

} // Execution
