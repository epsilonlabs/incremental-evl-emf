/**
 */
package org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task#getTitle <em>Title</em>}</li>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task#getStart <em>Start</em>}</li>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task#getDuration <em>Duration</em>}</li>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task#getEffort <em>Effort</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.PslPackage#getTask()
 * @model
 * @generated
 */
public interface Task extends EObject {
	/**
	 * Returns the value of the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Title</em>' attribute.
	 * @see #setTitle(String)
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.PslPackage#getTask_Title()
	 * @model
	 * @generated
	 */
	String getTitle();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
	void setTitle(String value);

	/**
	 * Returns the value of the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start</em>' attribute.
	 * @see #setStart(int)
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.PslPackage#getTask_Start()
	 * @model
	 * @generated
	 */
	int getStart();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task#getStart <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start</em>' attribute.
	 * @see #getStart()
	 * @generated
	 */
	void setStart(int value);

	/**
	 * Returns the value of the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duration</em>' attribute.
	 * @see #setDuration(int)
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.PslPackage#getTask_Duration()
	 * @model
	 * @generated
	 */
	int getDuration();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task#getDuration <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duration</em>' attribute.
	 * @see #getDuration()
	 * @generated
	 */
	void setDuration(int value);

	/**
	 * Returns the value of the '<em><b>Effort</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Effort}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Effort</em>' containment reference list.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.PslPackage#getTask_Effort()
	 * @model containment="true"
	 *        annotation="diagram direction='right'"
	 * @generated
	 */
	EList<Effort> getEffort();

} // Task
