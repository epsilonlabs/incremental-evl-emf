/**
 */
package org.eclipse.epsilon.evl.emf.validation.incremental.trace;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Access</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.PropertyAccess#getElement <em>Element</em>}</li>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.PropertyAccess#getProperty <em>Property</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.TracePackage#getPropertyAccess()
 * @model
 * @generated
 */
public interface PropertyAccess extends Access {
	/**
	 * Returns the value of the '<em><b>Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element</em>' attribute.
	 * @see #setElement(Object)
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.TracePackage#getPropertyAccess_Element()
	 * @model
	 * @generated
	 */
	Object getElement();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.PropertyAccess#getElement <em>Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element</em>' attribute.
	 * @see #getElement()
	 * @generated
	 */
	void setElement(Object value);

	/**
	 * Returns the value of the '<em><b>Property</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Property</em>' attribute.
	 * @see #setProperty(String)
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.TracePackage#getPropertyAccess_Property()
	 * @model
	 * @generated
	 */
	String getProperty();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.PropertyAccess#getProperty <em>Property</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Property</em>' attribute.
	 * @see #getProperty()
	 * @generated
	 */
	void setProperty(String value);

} // PropertyAccess
