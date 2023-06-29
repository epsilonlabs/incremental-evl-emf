/**
 */
package org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.PslFactory
 * @model kind="package"
 * @generated
 */
public interface PslPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "psl";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "psl";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PslPackage eINSTANCE = org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.PslPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.ProjectImpl <em>Project</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.ProjectImpl
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.PslPackageImpl#getProject()
	 * @generated
	 */
	int PROJECT = 0;

	/**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT__TITLE = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT__DESCRIPTION = 1;

	/**
	 * The feature id for the '<em><b>Tasks</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT__TASKS = 2;

	/**
	 * The feature id for the '<em><b>People</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT__PEOPLE = 3;

	/**
	 * The number of structural features of the '<em>Project</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Project</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.TaskImpl <em>Task</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.TaskImpl
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.PslPackageImpl#getTask()
	 * @generated
	 */
	int TASK = 1;

	/**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK__TITLE = 0;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK__START = 1;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK__DURATION = 2;

	/**
	 * The feature id for the '<em><b>Effort</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK__EFFORT = 3;

	/**
	 * The number of structural features of the '<em>Task</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Task</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.PersonImpl <em>Person</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.PersonImpl
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.PslPackageImpl#getPerson()
	 * @generated
	 */
	int PERSON = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSON__NAME = 0;

	/**
	 * The number of structural features of the '<em>Person</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSON_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Person</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSON_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.EffortImpl <em>Effort</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.EffortImpl
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.PslPackageImpl#getEffort()
	 * @generated
	 */
	int EFFORT = 3;

	/**
	 * The feature id for the '<em><b>Person</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EFFORT__PERSON = 0;

	/**
	 * The feature id for the '<em><b>Percentage</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EFFORT__PERCENTAGE = 1;

	/**
	 * The number of structural features of the '<em>Effort</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EFFORT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Effort</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EFFORT_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project <em>Project</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Project</em>'.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project
	 * @generated
	 */
	EClass getProject();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Title</em>'.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project#getTitle()
	 * @see #getProject()
	 * @generated
	 */
	EAttribute getProject_Title();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project#getDescription()
	 * @see #getProject()
	 * @generated
	 */
	EAttribute getProject_Description();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project#getTasks <em>Tasks</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Tasks</em>'.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project#getTasks()
	 * @see #getProject()
	 * @generated
	 */
	EReference getProject_Tasks();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project#getPeople <em>People</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>People</em>'.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project#getPeople()
	 * @see #getProject()
	 * @generated
	 */
	EReference getProject_People();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task <em>Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Task</em>'.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task
	 * @generated
	 */
	EClass getTask();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Title</em>'.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task#getTitle()
	 * @see #getTask()
	 * @generated
	 */
	EAttribute getTask_Title();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task#getStart <em>Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start</em>'.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task#getStart()
	 * @see #getTask()
	 * @generated
	 */
	EAttribute getTask_Start();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task#getDuration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task#getDuration()
	 * @see #getTask()
	 * @generated
	 */
	EAttribute getTask_Duration();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task#getEffort <em>Effort</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Effort</em>'.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task#getEffort()
	 * @see #getTask()
	 * @generated
	 */
	EReference getTask_Effort();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Person <em>Person</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Person</em>'.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Person
	 * @generated
	 */
	EClass getPerson();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Person#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Person#getName()
	 * @see #getPerson()
	 * @generated
	 */
	EAttribute getPerson_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Effort <em>Effort</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Effort</em>'.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Effort
	 * @generated
	 */
	EClass getEffort();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Effort#getPerson <em>Person</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Person</em>'.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Effort#getPerson()
	 * @see #getEffort()
	 * @generated
	 */
	EReference getEffort_Person();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Effort#getPercentage <em>Percentage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Percentage</em>'.
	 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Effort#getPercentage()
	 * @see #getEffort()
	 * @generated
	 */
	EAttribute getEffort_Percentage();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	PslFactory getPslFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.ProjectImpl <em>Project</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.ProjectImpl
		 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.PslPackageImpl#getProject()
		 * @generated
		 */
		EClass PROJECT = eINSTANCE.getProject();

		/**
		 * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROJECT__TITLE = eINSTANCE.getProject_Title();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROJECT__DESCRIPTION = eINSTANCE.getProject_Description();

		/**
		 * The meta object literal for the '<em><b>Tasks</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROJECT__TASKS = eINSTANCE.getProject_Tasks();

		/**
		 * The meta object literal for the '<em><b>People</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROJECT__PEOPLE = eINSTANCE.getProject_People();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.TaskImpl <em>Task</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.TaskImpl
		 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.PslPackageImpl#getTask()
		 * @generated
		 */
		EClass TASK = eINSTANCE.getTask();

		/**
		 * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK__TITLE = eINSTANCE.getTask_Title();

		/**
		 * The meta object literal for the '<em><b>Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK__START = eINSTANCE.getTask_Start();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK__DURATION = eINSTANCE.getTask_Duration();

		/**
		 * The meta object literal for the '<em><b>Effort</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TASK__EFFORT = eINSTANCE.getTask_Effort();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.PersonImpl <em>Person</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.PersonImpl
		 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.PslPackageImpl#getPerson()
		 * @generated
		 */
		EClass PERSON = eINSTANCE.getPerson();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PERSON__NAME = eINSTANCE.getPerson_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.EffortImpl <em>Effort</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.EffortImpl
		 * @see org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.impl.PslPackageImpl#getEffort()
		 * @generated
		 */
		EClass EFFORT = eINSTANCE.getEffort();

		/**
		 * The meta object literal for the '<em><b>Person</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EFFORT__PERSON = eINSTANCE.getEffort_Person();

		/**
		 * The meta object literal for the '<em><b>Percentage</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EFFORT__PERCENTAGE = eINSTANCE.getEffort_Percentage();

	}

} //PslPackage
