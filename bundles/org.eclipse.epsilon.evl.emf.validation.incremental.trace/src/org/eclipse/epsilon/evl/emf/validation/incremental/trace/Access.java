/**
 */
package org.eclipse.epsilon.evl.emf.validation.incremental.trace;

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
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.Access#getExecutions <em>Executions</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.TracePackage#getAccess()
 * @model abstract="true"
 * @generated
 */
public interface Access extends EObject {
	/**
	 * Returns the value of the '<em><b>Executions</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.Execution}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.Execution#getAccesses <em>Accesses</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Executions</em>' reference list.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.TracePackage#getAccess_Executions()
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.Execution#getAccesses
	 * @model opposite="accesses"
	 * @generated
	 */
	EList<Execution> getExecutions();

} // Access
