/**
 */
package org.eclipse.epsilon.evl.emf.validation.incremental.trace;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Constraint Execution</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.ConstraintExecution#getConstraint <em>Constraint</em>}</li>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.ConstraintExecution#getResult <em>Result</em>}</li>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.ConstraintExecution#getMessage <em>Message</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.TracePackage#getConstraintExecution()
 * @model
 * @generated
 */
public interface ConstraintExecution extends Execution {
	/**
	 * Returns the value of the '<em><b>Constraint</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraint</em>' reference.
	 * @see #setConstraint(Constraint)
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.TracePackage#getConstraintExecution_Constraint()
	 * @model annotation="diagram direction='down'"
	 * @generated
	 */
	Constraint getConstraint();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.ConstraintExecution#getConstraint <em>Constraint</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Constraint</em>' reference.
	 * @see #getConstraint()
	 * @generated
	 */
	void setConstraint(Constraint value);

	/**
	 * Returns the value of the '<em><b>Result</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.ExecutionResult}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Result</em>' attribute.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.ExecutionResult
	 * @see #setResult(ExecutionResult)
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.TracePackage#getConstraintExecution_Result()
	 * @model
	 * @generated
	 */
	ExecutionResult getResult();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.ConstraintExecution#getResult <em>Result</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Result</em>' attribute.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.ExecutionResult
	 * @see #getResult()
	 * @generated
	 */
	void setResult(ExecutionResult value);

	/**
	 * Returns the value of the '<em><b>Message</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Message</em>' attribute.
	 * @see #setMessage(String)
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.TracePackage#getConstraintExecution_Message()
	 * @model
	 * @generated
	 */
	String getMessage();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.ConstraintExecution#getMessage <em>Message</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Message</em>' attribute.
	 * @see #getMessage()
	 * @generated
	 */
	void setMessage(String value);

} // ConstraintExecution
