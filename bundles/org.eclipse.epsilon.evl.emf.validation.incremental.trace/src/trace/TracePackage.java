/**
 */
package trace;

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
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see trace.TraceFactory
 * @model kind="package"
 * @generated
 */
public interface TracePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "trace";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "trace";

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
	TracePackage eINSTANCE = trace.impl.TracePackageImpl.init();

	/**
	 * The meta object id for the '{@link trace.impl.TraceImpl <em>Trace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see trace.impl.TraceImpl
	 * @see trace.impl.TracePackageImpl#getTrace()
	 * @generated
	 */
	int TRACE = 0;

	/**
	 * The feature id for the '<em><b>Executions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE__EXECUTIONS = 0;

	/**
	 * The feature id for the '<em><b>Accesses</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE__ACCESSES = 1;

	/**
	 * The number of structural features of the '<em>Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link trace.impl.ExecutionImpl <em>Execution</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see trace.impl.ExecutionImpl
	 * @see trace.impl.TracePackageImpl#getExecution()
	 * @generated
	 */
	int EXECUTION = 1;

	/**
	 * The feature id for the '<em><b>Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION__CONTEXT = 0;

	/**
	 * The feature id for the '<em><b>Accesses</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION__ACCESSES = 1;

	/**
	 * The feature id for the '<em><b>Dependencies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION__DEPENDENCIES = 2;

	/**
	 * The number of structural features of the '<em>Execution</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link trace.impl.ConstraintExecutionImpl <em>Constraint Execution</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see trace.impl.ConstraintExecutionImpl
	 * @see trace.impl.TracePackageImpl#getConstraintExecution()
	 * @generated
	 */
	int CONSTRAINT_EXECUTION = 2;

	/**
	 * The feature id for the '<em><b>Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_EXECUTION__CONTEXT = EXECUTION__CONTEXT;

	/**
	 * The feature id for the '<em><b>Accesses</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_EXECUTION__ACCESSES = EXECUTION__ACCESSES;

	/**
	 * The feature id for the '<em><b>Dependencies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_EXECUTION__DEPENDENCIES = EXECUTION__DEPENDENCIES;

	/**
	 * The feature id for the '<em><b>Constraint</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_EXECUTION__CONSTRAINT = EXECUTION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Result</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_EXECUTION__RESULT = EXECUTION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Message</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_EXECUTION__MESSAGE = EXECUTION_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Constraint Execution</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_EXECUTION_FEATURE_COUNT = EXECUTION_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link trace.impl.ConstraintImpl <em>Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see trace.impl.ConstraintImpl
	 * @see trace.impl.TracePackageImpl#getConstraint()
	 * @generated
	 */
	int CONSTRAINT = 3;

	/**
	 * The feature id for the '<em><b>Raw</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__RAW = 0;

	/**
	 * The number of structural features of the '<em>Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link trace.impl.CachedOperationExecutionImpl <em>Cached Operation Execution</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see trace.impl.CachedOperationExecutionImpl
	 * @see trace.impl.TracePackageImpl#getCachedOperationExecution()
	 * @generated
	 */
	int CACHED_OPERATION_EXECUTION = 4;

	/**
	 * The feature id for the '<em><b>Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CACHED_OPERATION_EXECUTION__CONTEXT = EXECUTION__CONTEXT;

	/**
	 * The feature id for the '<em><b>Accesses</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CACHED_OPERATION_EXECUTION__ACCESSES = EXECUTION__ACCESSES;

	/**
	 * The feature id for the '<em><b>Dependencies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CACHED_OPERATION_EXECUTION__DEPENDENCIES = EXECUTION__DEPENDENCIES;

	/**
	 * The feature id for the '<em><b>Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CACHED_OPERATION_EXECUTION__OPERATION = EXECUTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Cached Operation Execution</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CACHED_OPERATION_EXECUTION_FEATURE_COUNT = EXECUTION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link trace.impl.OperationImpl <em>Operation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see trace.impl.OperationImpl
	 * @see trace.impl.TracePackageImpl#getOperation()
	 * @generated
	 */
	int OPERATION = 5;

	/**
	 * The number of structural features of the '<em>Operation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link trace.impl.AccessImpl <em>Access</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see trace.impl.AccessImpl
	 * @see trace.impl.TracePackageImpl#getAccess()
	 * @generated
	 */
	int ACCESS = 6;

	/**
	 * The feature id for the '<em><b>Executions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCESS__EXECUTIONS = 0;

	/**
	 * The number of structural features of the '<em>Access</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCESS_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link trace.impl.PropertyAccessImpl <em>Property Access</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see trace.impl.PropertyAccessImpl
	 * @see trace.impl.TracePackageImpl#getPropertyAccess()
	 * @generated
	 */
	int PROPERTY_ACCESS = 7;

	/**
	 * The feature id for the '<em><b>Executions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_ACCESS__EXECUTIONS = ACCESS__EXECUTIONS;

	/**
	 * The feature id for the '<em><b>Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_ACCESS__ELEMENT = ACCESS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Property</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_ACCESS__PROPERTY = ACCESS_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Property Access</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_ACCESS_FEATURE_COUNT = ACCESS_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link trace.impl.AllAccessImpl <em>All Access</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see trace.impl.AllAccessImpl
	 * @see trace.impl.TracePackageImpl#getAllAccess()
	 * @generated
	 */
	int ALL_ACCESS = 8;

	/**
	 * The feature id for the '<em><b>Executions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALL_ACCESS__EXECUTIONS = ACCESS__EXECUTIONS;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALL_ACCESS__TYPE = ACCESS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>All Of Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALL_ACCESS__ALL_OF_KIND = ACCESS_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>All Access</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALL_ACCESS_FEATURE_COUNT = ACCESS_FEATURE_COUNT + 2;


	/**
	 * Returns the meta object for class '{@link trace.Trace <em>Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trace</em>'.
	 * @see trace.Trace
	 * @generated
	 */
	EClass getTrace();

	/**
	 * Returns the meta object for the containment reference list '{@link trace.Trace#getExecutions <em>Executions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Executions</em>'.
	 * @see trace.Trace#getExecutions()
	 * @see #getTrace()
	 * @generated
	 */
	EReference getTrace_Executions();

	/**
	 * Returns the meta object for the containment reference list '{@link trace.Trace#getAccesses <em>Accesses</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Accesses</em>'.
	 * @see trace.Trace#getAccesses()
	 * @see #getTrace()
	 * @generated
	 */
	EReference getTrace_Accesses();

	/**
	 * Returns the meta object for class '{@link trace.Execution <em>Execution</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Execution</em>'.
	 * @see trace.Execution
	 * @generated
	 */
	EClass getExecution();

	/**
	 * Returns the meta object for the attribute '{@link trace.Execution#getContext <em>Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Context</em>'.
	 * @see trace.Execution#getContext()
	 * @see #getExecution()
	 * @generated
	 */
	EAttribute getExecution_Context();

	/**
	 * Returns the meta object for the reference list '{@link trace.Execution#getAccesses <em>Accesses</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Accesses</em>'.
	 * @see trace.Execution#getAccesses()
	 * @see #getExecution()
	 * @generated
	 */
	EReference getExecution_Accesses();

	/**
	 * Returns the meta object for the reference list '{@link trace.Execution#getDependencies <em>Dependencies</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Dependencies</em>'.
	 * @see trace.Execution#getDependencies()
	 * @see #getExecution()
	 * @generated
	 */
	EReference getExecution_Dependencies();

	/**
	 * Returns the meta object for class '{@link trace.ConstraintExecution <em>Constraint Execution</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Constraint Execution</em>'.
	 * @see trace.ConstraintExecution
	 * @generated
	 */
	EClass getConstraintExecution();

	/**
	 * Returns the meta object for the reference '{@link trace.ConstraintExecution#getConstraint <em>Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Constraint</em>'.
	 * @see trace.ConstraintExecution#getConstraint()
	 * @see #getConstraintExecution()
	 * @generated
	 */
	EReference getConstraintExecution_Constraint();

	/**
	 * Returns the meta object for the attribute '{@link trace.ConstraintExecution#isResult <em>Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Result</em>'.
	 * @see trace.ConstraintExecution#isResult()
	 * @see #getConstraintExecution()
	 * @generated
	 */
	EAttribute getConstraintExecution_Result();

	/**
	 * Returns the meta object for the attribute '{@link trace.ConstraintExecution#getMessage <em>Message</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Message</em>'.
	 * @see trace.ConstraintExecution#getMessage()
	 * @see #getConstraintExecution()
	 * @generated
	 */
	EAttribute getConstraintExecution_Message();

	/**
	 * Returns the meta object for class '{@link trace.Constraint <em>Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Constraint</em>'.
	 * @see trace.Constraint
	 * @generated
	 */
	EClass getConstraint();

	/**
	 * Returns the meta object for the attribute '{@link trace.Constraint#getRaw <em>Raw</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Raw</em>'.
	 * @see trace.Constraint#getRaw()
	 * @see #getConstraint()
	 * @generated
	 */
	EAttribute getConstraint_Raw();

	/**
	 * Returns the meta object for class '{@link trace.CachedOperationExecution <em>Cached Operation Execution</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cached Operation Execution</em>'.
	 * @see trace.CachedOperationExecution
	 * @generated
	 */
	EClass getCachedOperationExecution();

	/**
	 * Returns the meta object for the reference '{@link trace.CachedOperationExecution#getOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Operation</em>'.
	 * @see trace.CachedOperationExecution#getOperation()
	 * @see #getCachedOperationExecution()
	 * @generated
	 */
	EReference getCachedOperationExecution_Operation();

	/**
	 * Returns the meta object for class '{@link trace.Operation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation</em>'.
	 * @see trace.Operation
	 * @generated
	 */
	EClass getOperation();

	/**
	 * Returns the meta object for class '{@link trace.Access <em>Access</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Access</em>'.
	 * @see trace.Access
	 * @generated
	 */
	EClass getAccess();

	/**
	 * Returns the meta object for the reference list '{@link trace.Access#getExecutions <em>Executions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Executions</em>'.
	 * @see trace.Access#getExecutions()
	 * @see #getAccess()
	 * @generated
	 */
	EReference getAccess_Executions();

	/**
	 * Returns the meta object for class '{@link trace.PropertyAccess <em>Property Access</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property Access</em>'.
	 * @see trace.PropertyAccess
	 * @generated
	 */
	EClass getPropertyAccess();

	/**
	 * Returns the meta object for the attribute '{@link trace.PropertyAccess#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Element</em>'.
	 * @see trace.PropertyAccess#getElement()
	 * @see #getPropertyAccess()
	 * @generated
	 */
	EAttribute getPropertyAccess_Element();

	/**
	 * Returns the meta object for the attribute '{@link trace.PropertyAccess#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Property</em>'.
	 * @see trace.PropertyAccess#getProperty()
	 * @see #getPropertyAccess()
	 * @generated
	 */
	EAttribute getPropertyAccess_Property();

	/**
	 * Returns the meta object for class '{@link trace.AllAccess <em>All Access</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>All Access</em>'.
	 * @see trace.AllAccess
	 * @generated
	 */
	EClass getAllAccess();

	/**
	 * Returns the meta object for the attribute '{@link trace.AllAccess#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see trace.AllAccess#getType()
	 * @see #getAllAccess()
	 * @generated
	 */
	EAttribute getAllAccess_Type();

	/**
	 * Returns the meta object for the attribute '{@link trace.AllAccess#isAllOfKind <em>All Of Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>All Of Kind</em>'.
	 * @see trace.AllAccess#isAllOfKind()
	 * @see #getAllAccess()
	 * @generated
	 */
	EAttribute getAllAccess_AllOfKind();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TraceFactory getTraceFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link trace.impl.TraceImpl <em>Trace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see trace.impl.TraceImpl
		 * @see trace.impl.TracePackageImpl#getTrace()
		 * @generated
		 */
		EClass TRACE = eINSTANCE.getTrace();

		/**
		 * The meta object literal for the '<em><b>Executions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE__EXECUTIONS = eINSTANCE.getTrace_Executions();

		/**
		 * The meta object literal for the '<em><b>Accesses</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE__ACCESSES = eINSTANCE.getTrace_Accesses();

		/**
		 * The meta object literal for the '{@link trace.impl.ExecutionImpl <em>Execution</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see trace.impl.ExecutionImpl
		 * @see trace.impl.TracePackageImpl#getExecution()
		 * @generated
		 */
		EClass EXECUTION = eINSTANCE.getExecution();

		/**
		 * The meta object literal for the '<em><b>Context</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXECUTION__CONTEXT = eINSTANCE.getExecution_Context();

		/**
		 * The meta object literal for the '<em><b>Accesses</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXECUTION__ACCESSES = eINSTANCE.getExecution_Accesses();

		/**
		 * The meta object literal for the '<em><b>Dependencies</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXECUTION__DEPENDENCIES = eINSTANCE.getExecution_Dependencies();

		/**
		 * The meta object literal for the '{@link trace.impl.ConstraintExecutionImpl <em>Constraint Execution</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see trace.impl.ConstraintExecutionImpl
		 * @see trace.impl.TracePackageImpl#getConstraintExecution()
		 * @generated
		 */
		EClass CONSTRAINT_EXECUTION = eINSTANCE.getConstraintExecution();

		/**
		 * The meta object literal for the '<em><b>Constraint</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONSTRAINT_EXECUTION__CONSTRAINT = eINSTANCE.getConstraintExecution_Constraint();

		/**
		 * The meta object literal for the '<em><b>Result</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONSTRAINT_EXECUTION__RESULT = eINSTANCE.getConstraintExecution_Result();

		/**
		 * The meta object literal for the '<em><b>Message</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONSTRAINT_EXECUTION__MESSAGE = eINSTANCE.getConstraintExecution_Message();

		/**
		 * The meta object literal for the '{@link trace.impl.ConstraintImpl <em>Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see trace.impl.ConstraintImpl
		 * @see trace.impl.TracePackageImpl#getConstraint()
		 * @generated
		 */
		EClass CONSTRAINT = eINSTANCE.getConstraint();

		/**
		 * The meta object literal for the '<em><b>Raw</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONSTRAINT__RAW = eINSTANCE.getConstraint_Raw();

		/**
		 * The meta object literal for the '{@link trace.impl.CachedOperationExecutionImpl <em>Cached Operation Execution</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see trace.impl.CachedOperationExecutionImpl
		 * @see trace.impl.TracePackageImpl#getCachedOperationExecution()
		 * @generated
		 */
		EClass CACHED_OPERATION_EXECUTION = eINSTANCE.getCachedOperationExecution();

		/**
		 * The meta object literal for the '<em><b>Operation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CACHED_OPERATION_EXECUTION__OPERATION = eINSTANCE.getCachedOperationExecution_Operation();

		/**
		 * The meta object literal for the '{@link trace.impl.OperationImpl <em>Operation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see trace.impl.OperationImpl
		 * @see trace.impl.TracePackageImpl#getOperation()
		 * @generated
		 */
		EClass OPERATION = eINSTANCE.getOperation();

		/**
		 * The meta object literal for the '{@link trace.impl.AccessImpl <em>Access</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see trace.impl.AccessImpl
		 * @see trace.impl.TracePackageImpl#getAccess()
		 * @generated
		 */
		EClass ACCESS = eINSTANCE.getAccess();

		/**
		 * The meta object literal for the '<em><b>Executions</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ACCESS__EXECUTIONS = eINSTANCE.getAccess_Executions();

		/**
		 * The meta object literal for the '{@link trace.impl.PropertyAccessImpl <em>Property Access</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see trace.impl.PropertyAccessImpl
		 * @see trace.impl.TracePackageImpl#getPropertyAccess()
		 * @generated
		 */
		EClass PROPERTY_ACCESS = eINSTANCE.getPropertyAccess();

		/**
		 * The meta object literal for the '<em><b>Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPERTY_ACCESS__ELEMENT = eINSTANCE.getPropertyAccess_Element();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPERTY_ACCESS__PROPERTY = eINSTANCE.getPropertyAccess_Property();

		/**
		 * The meta object literal for the '{@link trace.impl.AllAccessImpl <em>All Access</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see trace.impl.AllAccessImpl
		 * @see trace.impl.TracePackageImpl#getAllAccess()
		 * @generated
		 */
		EClass ALL_ACCESS = eINSTANCE.getAllAccess();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ALL_ACCESS__TYPE = eINSTANCE.getAllAccess_Type();

		/**
		 * The meta object literal for the '<em><b>All Of Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ALL_ACCESS__ALL_OF_KIND = eINSTANCE.getAllAccess_AllOfKind();

	}

} //TracePackage