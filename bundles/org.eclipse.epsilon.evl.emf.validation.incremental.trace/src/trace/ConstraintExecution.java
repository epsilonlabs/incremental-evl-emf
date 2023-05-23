/**
 */
package trace;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Constraint Execution</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link trace.ConstraintExecution#getConstraint <em>Constraint</em>}</li>
 *   <li>{@link trace.ConstraintExecution#isResult <em>Result</em>}</li>
 *   <li>{@link trace.ConstraintExecution#getMessage <em>Message</em>}</li>
 * </ul>
 *
 * @see trace.TracePackage#getConstraintExecution()
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
	 * @see trace.TracePackage#getConstraintExecution_Constraint()
	 * @model annotation="diagram direction='down'"
	 * @generated
	 */
	Constraint getConstraint();

	/**
	 * Sets the value of the '{@link trace.ConstraintExecution#getConstraint <em>Constraint</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Constraint</em>' reference.
	 * @see #getConstraint()
	 * @generated
	 */
	void setConstraint(Constraint value);

	/**
	 * Returns the value of the '<em><b>Result</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Result</em>' attribute.
	 * @see #setResult(boolean)
	 * @see trace.TracePackage#getConstraintExecution_Result()
	 * @model
	 * @generated
	 */
	boolean isResult();

	/**
	 * Sets the value of the '{@link trace.ConstraintExecution#isResult <em>Result</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Result</em>' attribute.
	 * @see #isResult()
	 * @generated
	 */
	void setResult(boolean value);

	/**
	 * Returns the value of the '<em><b>Message</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Message</em>' attribute.
	 * @see #setMessage(String)
	 * @see trace.TracePackage#getConstraintExecution_Message()
	 * @model
	 * @generated
	 */
	String getMessage();

	/**
	 * Sets the value of the '{@link trace.ConstraintExecution#getMessage <em>Message</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Message</em>' attribute.
	 * @see #getMessage()
	 * @generated
	 */
	void setMessage(String value);

} // ConstraintExecution
