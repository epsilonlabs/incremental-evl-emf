/**
 */
package org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project#getTitle <em>Title</em>}</li>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project#getTasks <em>Tasks</em>}</li>
 *   <li>{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project#getPeople <em>People</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.PslPackage#getProject()
 * @model
 * @generated
 */
public interface Project extends EObject {
	/**
	 * Returns the value of the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Title</em>' attribute.
	 * @see #setTitle(String)
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.PslPackage#getProject_Title()
	 * @model
	 * @generated
	 */
	String getTitle();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
	void setTitle(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.PslPackage#getProject_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Tasks</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tasks</em>' containment reference list.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.PslPackage#getProject_Tasks()
	 * @model containment="true"
	 * @generated
	 */
	EList<Task> getTasks();

	/**
	 * Returns the value of the '<em><b>People</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Person}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>People</em>' containment reference list.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.PslPackage#getProject_People()
	 * @model containment="true"
	 *        annotation="diagram direction='right'"
	 * @generated
	 */
	EList<Person> getPeople();

} // Project
