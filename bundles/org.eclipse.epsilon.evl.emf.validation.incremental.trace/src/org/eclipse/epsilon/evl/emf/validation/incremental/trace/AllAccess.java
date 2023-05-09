/**
 */
package org.eclipse.epsilon.evl.emf.validation.incremental.trace;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>All Access</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.AllAccess#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.AllAccess#isAllOfKind <em>All Of Kind</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.TracePackage#getAllAccess()
 * @model
 * @generated
 */
public interface AllAccess extends Access {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.TracePackage#getAllAccess_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.AllAccess#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>All Of Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>All Of Kind</em>' attribute.
	 * @see #setAllOfKind(boolean)
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.TracePackage#getAllAccess_AllOfKind()
	 * @model
	 * @generated
	 */
	boolean isAllOfKind();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.AllAccess#isAllOfKind <em>All Of Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>All Of Kind</em>' attribute.
	 * @see #isAllOfKind()
	 * @generated
	 */
	void setAllOfKind(boolean value);

} // AllAccess
