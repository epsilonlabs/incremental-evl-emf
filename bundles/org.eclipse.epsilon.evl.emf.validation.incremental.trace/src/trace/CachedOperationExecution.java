/**
 */
package trace;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cached Operation Execution</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link trace.CachedOperationExecution#getOperation <em>Operation</em>}</li>
 * </ul>
 *
 * @see trace.TracePackage#getCachedOperationExecution()
 * @model
 * @generated
 */
public interface CachedOperationExecution extends Execution {
	/**
	 * Returns the value of the '<em><b>Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operation</em>' reference.
	 * @see #setOperation(Operation)
	 * @see trace.TracePackage#getCachedOperationExecution_Operation()
	 * @model annotation="diagram direction='down'"
	 * @generated
	 */
	Operation getOperation();

	/**
	 * Sets the value of the '{@link trace.CachedOperationExecution#getOperation <em>Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operation</em>' reference.
	 * @see #getOperation()
	 * @generated
	 */
	void setOperation(Operation value);

} // CachedOperationExecution
