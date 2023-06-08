/**
 */
package org.eclipse.epsilon.evl.emf.validation.incremental.trace;

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
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.Execution#getModelElement <em>Model Element</em>}</li>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.Execution#getAccesses <em>Accesses</em>}</li>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.Execution#getDependencies <em>Dependencies</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.TracePackage#getExecution()
 * @model abstract="true"
 * @generated
 */
public interface Execution extends EObject {
	/**
	 * Returns the value of the '<em><b>Model Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Element</em>' reference.
	 * @see #setModelElement(EObject)
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.TracePackage#getExecution_ModelElement()
	 * @model
	 * @generated
	 */
	EObject getModelElement();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.Execution#getModelElement <em>Model Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Element</em>' reference.
	 * @see #getModelElement()
	 * @generated
	 */
	void setModelElement(EObject value);

	/**
	 * Returns the value of the '<em><b>Accesses</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.Access}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.Access#getExecutions <em>Executions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Accesses</em>' reference list.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.TracePackage#getExecution_Accesses()
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.Access#getExecutions
	 * @model opposite="executions"
	 * @generated
	 */
	EList<Access> getAccesses();

	/**
	 * Returns the value of the '<em><b>Dependencies</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.epsilon.evl.emf.validation.incremental.trace.Execution}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dependencies</em>' reference list.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.trace.TracePackage#getExecution_Dependencies()
	 * @model
	 * @generated
	 */
	EList<Execution> getDependencies();

} // Execution
