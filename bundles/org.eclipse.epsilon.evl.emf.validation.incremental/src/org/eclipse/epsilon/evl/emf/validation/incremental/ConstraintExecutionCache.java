package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.Access;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.ConstraintExecution;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.Execution;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.ExecutionResult;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.PropertyAccess;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.Trace;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.impl.ConstraintExecutionImpl;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.impl.PropertyAccessImpl;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.epsilon.evl.trace.ConstraintTraceItem;

// TODO Double check the trace model notification removal processes. They pass tests, but is it really cleaning everything it should? 

public class ConstraintExecutionCache {
	private static final Logger LOGGER = Logger.getLogger(ConstraintExecutionCache.class.getName());

	/*
	 * This contains a Trace model that the last EVLtrace updated during an EVLmodule
	 * validation.
	 * 
	 * In the Execution Cache provides methods to manipulate the
	 * traceModel in response to notifications that invalidate known results.
	 * The Execution Cache also provides methods to recall know execution results for Model Constraint pairs
	 * 
	 */

	protected Trace traceModel;
	
	// These lists only get populated when a validation process starts, we need only need to know what model & constraint results 
	// Using lists reduces the search space, we parse all executions in the model once and sort them into pass and fail, omiting all the blocked
	private boolean cacheListsValid = false;
	private List<ConstraintTraceItem> cachedConstraintTraceItems;
	private List<UnsatisfiedConstraint> cachedUnsatisfiedConstraints;


	public ConstraintExecutionCache(IncrementalEvlModule lastModule) {
		// Pull in the traceModel from the last execution
		traceModel = lastModule.evlTrace.traceModel;
		
		if (LOGGER.isLoggable(Level.FINE)) {
			LOGGER.fine(() -> "Setting up Execution Cache: \n" + toString());
		}
	}

	
	//
	// CACHE LOOKUP
	//
	public void buildCachedResultLists () {
		cachedConstraintTraceItems = this.getListOfConstraintTraceItems();
		cachedUnsatisfiedConstraints = this.getListOfUnsatisfiedConstraints();
	
		LOGGER.info("CACHE Lists built:\n " 
				+ cachedConstraintTraceItems.size() + " cachedConstraintTraceItems\n " 
				+ cachedUnsatisfiedConstraints.size() + " cachedUnsatisfiedConstraints");
	}
	
	// ConstraintTraceItem search uses cache list built from the TraceModel information
	public ConstraintTraceItem checkCacheFor(Object model, Constraint constraint) {
		if(cacheListsValid != true) {
			buildCachedResultLists();
			cacheListsValid = true;
		}
		
		LOGGER.finer(() -> "Execution cache - checkCachedConstraintTrace - " 
				+ model.hashCode() + " " + constraint.getName());

		for (ConstraintTraceItem item : cachedConstraintTraceItems) {
			if (item.getInstance().equals(model) && item.getConstraint().equals(constraint)) {
				LOGGER.finer(() -> "Execution cache - MATCHED model & constraint - " + item.hashCode() + " " + constraint.getName());
				return item;
			}
		}

		LOGGER.finer(() -> "Execution cache - NO MATCHES");
		return null;
	}

	// UnsatisfiedConstraints search uses cache list built from the TraceModel information
	public UnsatisfiedConstraint getCachedUnsatisfiedConstraint(Object model, Constraint constraint) {
		if(cacheListsValid != true) {
			buildCachedResultLists();
		}
		
		LOGGER.finer(() -> "Execution cache - getCachedUnsatisfiedConstraint - "
				+ model.hashCode() + " " + constraint.getName());

		for (UnsatisfiedConstraint unsatisfiedConstraint : cachedUnsatisfiedConstraints) {
			if (unsatisfiedConstraint.getInstance().equals(model)
					&& unsatisfiedConstraint.getConstraint().equals(constraint)) {
				LOGGER.finer(() -> "Execution cache - MATCHED UC -  UC Result: "
						+ unsatisfiedConstraint.getConstraint().getName());
				return unsatisfiedConstraint;
			}
		}
		LOGGER.finer(() -> "Execution cache - NO MATCHES");
		return null; // This should not happen if there is an entry on the trace.
	}

	
	//
	// NOTIFICATION PROCESSING
	// 
	public void processModelNotification(Notification notification) {
		// IF a model element changes we need to remove all the cached results.
		int notificationType = notification.getEventType();
		EObject modelElement = (EObject) notification.getNotifier();
		EStructuralFeature modelFeature = (EStructuralFeature) notification.getFeature();
		
		switch (notificationType) {
		case Notification.UNSET:
		case Notification.SET:
		case Notification.REMOVE:
		case Notification.REMOVE_MANY:
		case Notification.ADD:
		case Notification.ADD_MANY:
		case Notification.MOVE:
			LOGGER.finer(() -> "Request remove from cache (not Adapter)"
					+ "\n ModelElement: " + modelElement.hashCode() + " - " + modelElement 
					+ "\n ModelFeature: " + modelFeature
					+ executionCacheContentSyntheticLists());
			removeFromCache(modelElement, modelFeature);
			break;
		case Notification.REMOVING_ADAPTER:
			if (notification.getOldValue() instanceof IncrementalEvlValidatorAdapter) {
				removeFromCache(modelElement);
				LOGGER.finer(() -> "Request remove from cache (Adapter)" 
						+ "\n ModelElement: " + modelElement.hashCode() + " - " + modelElement 
						+ executionCacheContentSyntheticLists());
			}
			break;
		}
		LOGGER.finer(() ->("UPDATED CACHE"+ executionCacheContentSyntheticLists()));
		
	}

	private void removeFromCache(EObject modelElement) {
		LOGGER.finer(() -> "Try and remove model element " + modelElement.hashCode()
				+ " with ANY feature from execution cache");

		// Delete from the the traceModel
		EList<Access> accessList = traceModel.getAccesses();
		for (Iterator<Access> itr = accessList.iterator(); itr.hasNext();) {
			PropertyAccess propertyAccess = (PropertyAccess) itr.next();
			LOGGER.finest(">>> propertyAccess: " + propertyAccess.hashCode());

			// Is the property access for the Model element notification?
			if (propertyAccess.getElement() == modelElement) {
				LOGGER.finest(" modelElement matches access, delete " + propertyAccess.getProperty());
				removeOrphenedExecutionsFromTraceModel(propertyAccess);
				itr.remove();
			}
		}
	}

	private void removeFromCache(EObject modelElement, EStructuralFeature modelFeature) {
		LOGGER.finer(() -> "Try and remove model element " + modelElement.hashCode() + " with feature "
				+ modelFeature.getName() + " from execution cache");

		// Delete from the the traceModel
		EList<Access> accessList = traceModel.getAccesses();
		for (Iterator<Access> itr = accessList.iterator(); itr.hasNext();) {
			PropertyAccess propertyAccess = (PropertyAccess) itr.next();
			LOGGER.finest(">>> propertyAccess: " + propertyAccess.hashCode());
			if (propertyAccess.getElement() == modelElement) {
				LOGGER.finest("Check feature, delete? " + propertyAccess.getProperty() + "==" + modelFeature.getName());
				if (propertyAccess.getProperty().contentEquals(modelFeature.getName())) {
					removeOrphenedExecutionsFromTraceModel(propertyAccess);
					itr.remove();
				}
			}
		}
	}

	private void removeOrphenedExecutionsFromTraceModel(Access access) {
		LOGGER.finer("removeOrphenedExecutions: ");
		for (Iterator<Execution> itr = access.getExecutions().iterator(); itr.hasNext();) {
			ConstraintExecution execution = (ConstraintExecution) itr.next();
			removeExecutionsWithConstraintFromTraceModel(execution.getConstraint());
			if (execution.getAccesses().size() < 2) {
				LOGGER.finer("\n [!] Execution with no accesses deleted");
				itr.remove();
			}
		}
	}

	private void removeExecutionsWithConstraintFromTraceModel(
			org.eclipse.epsilon.evl.emf.validation.incremental.trace.Constraint constraint) {
		LOGGER.finer("removeExecutionsWithConstraint: " + constraint);
		for (Iterator<Execution> itr = traceModel.getExecutions().iterator(); itr.hasNext();) {
			ConstraintExecution execution = (ConstraintExecution) itr.next();
			if (execution.getConstraint() == constraint) {
				LOGGER.finer("\n [!] Execution with constraint deleted: " + execution.hashCode() + ":" + constraint);
				// TODO Check the Accesses connected to this execution, if the accesses have no
				// other executions, they will be orphaned therefore need removing
				itr.remove();
			}
		}
	}


	//
	// REPORTING THE EXECUTION CACHE STATE 	
	//
	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner("\n");

		sj.add(executionCacheContentSyntheticLists());
		
		return sj.toString();
	}
	
	private String executionCacheContentSyntheticLists() {
		StringJoiner sj = new StringJoiner("\n");
		StringJoiner executionReport = new StringJoiner("\n");
		executionReport.add("\nExecutions represented in trace model");
		
		List<ConstraintTraceItem> constraintTraceItems = this.getListOfConstraintTraceItems(); // ALL RESULTS
		List<UnsatisfiedConstraint> unsatisfiedConstraints = this.getListOfUnsatisfiedConstraints(); // FAILED RESULTS
		List<ConstraintPropertyAccess> constraintPropertyAccess = this.getListOfConstraintPropertyAccesses(); // ALL ACCESSES

		// Compile list to report
		sj.add("\n == Execution Cache state ==\n");
		sj.add("Trace Model size: " 
				+ traceModel.getExecutions().size() + " Executions, "
				+traceModel.getAccesses().size() + " Accesses");
		
		sj.add(executionReport.toString());
		
		sj.add("\nSynthetic lists of cached execution knowledge about the last EVL Module\n");
		sj.add("ContraintTraceItems: " + constraintTraceItems.size());
		sj.add("UnsatisfiedConstraints: " + unsatisfiedConstraints.size());
		sj.add("ConstraintPropertyAccesses: " + constraintPropertyAccess.size());

		// Commenting out the details for demo
		sj.add(constraintTraceToString(constraintTraceItems));
		sj.add(unsatisfiedConstraintsToString(unsatisfiedConstraints));
		sj.add(constraintPropertyAccessToString(constraintPropertyAccess));
		sj.add("\n =========================== \n");

		return sj.toString();
	}

	private String constraintTraceToString(List<ConstraintTraceItem> constraintTraceItems) {
		int i = 0;
		StringJoiner sj = new StringJoiner("\n");
		sj.add("\nConstraintTrace list: ");
		for (ConstraintTraceItem item : constraintTraceItems) {
			i++;
			sj.add(" " + i + ", Constraint: " + item.getConstraint().getName() + " "
					+ item.getConstraint().hashCode() + " | Model hashcode: " + item.getInstance().hashCode()
					+ " | Result : " + item.getResult());
		}
		return sj.toString();
	}

	private String unsatisfiedConstraintsToString(List<UnsatisfiedConstraint> unsatisfiedConstraints) {
		int i = 0;
		StringJoiner sj = new StringJoiner("\n");
		sj.add("\nUnsatisfiedConstraint list: ");
		for (UnsatisfiedConstraint uc : unsatisfiedConstraints) {
			i++;
			sj.add(" " + i + ", Constraint: " + uc.getConstraint().getName() + " "
					+ uc.getConstraint().hashCode() + " | Model hashcode: " + uc.getInstance().hashCode());
		}
		return sj.toString();
	}

	private String constraintPropertyAccessToString(List<ConstraintPropertyAccess> constraintPropertyAccess2) {
		int i = 0;
		StringJoiner sj = new StringJoiner("\n");
				sj.add("\n(Constraint)PropertyAccess list: ");

		for (ConstraintPropertyAccess cpa : constraintPropertyAccess2) {
			i++;
			sj.add(" " + i + ", Constraint: " + cpa.execution.getConstraint().getRaw() + " | Model hashcode: "
							+ cpa.getModelElement().hashCode() + " | Property " + cpa.getPropertyName());
		}
		return sj.toString();
	}
	
	
	//
	// Get Lists of information from the TraceModel
	//
	
	// Cached results use lists.
	// Reporting the status of the execution cache uses lists.
	// Junit tests check lists of how many of these concepts are in the cache.
	
	public List<ConstraintTraceItem> getListOfConstraintTraceItems(){
		List<ConstraintTraceItem> constraintTraceItems = new ArrayList<>();
		for (Execution mExecution : traceModel.getExecutions()) {
			ConstraintExecutionImpl mConstraintExecution = (ConstraintExecutionImpl) mExecution;
			Constraint rawConstraint = (Constraint) mConstraintExecution.getConstraint().getRaw();
			ExecutionResult result = mConstraintExecution.getResult();

			// Create the ConstraintTraceItems for both PASS and FAIL!
			if (result == ExecutionResult.PASS || result == ExecutionResult.FAIL) {
				constraintTraceItems.add(new ConstraintTraceItem(mExecution.getModelElement(), rawConstraint, true));
			}
		}
		return constraintTraceItems;
	}
	
	public List<UnsatisfiedConstraint> getListOfUnsatisfiedConstraints() {
		List<UnsatisfiedConstraint> unsatisfiedConstraints = new ArrayList<>();		
		for (Execution mExecution : traceModel.getExecutions()) {
			ConstraintExecutionImpl mConstraintExecution = (ConstraintExecutionImpl) mExecution;
			Constraint rawConstraint = (Constraint) mConstraintExecution.getConstraint().getRaw();
			ExecutionResult result = mConstraintExecution.getResult();

			if (result == ExecutionResult.FAIL) {
				UnsatisfiedConstraint uC = new UnsatisfiedConstraint();
				uC.setConstraint(rawConstraint);
				uC.setInstance(mExecution.getModelElement());
				unsatisfiedConstraints.add(uC);
			}
		}
		return unsatisfiedConstraints;
	}
	
	public List<ConstraintPropertyAccess>  getListOfConstraintPropertyAccesses(){
		List<ConstraintPropertyAccess> constraintPropertyAccesses = new ArrayList<>();
		for (Execution mExecution : traceModel.getExecutions()) {
			ConstraintExecutionImpl mConstraintExecution = (ConstraintExecutionImpl) mExecution;
			for (Access modelAccess : mConstraintExecution.getAccesses()) {
				PropertyAccess propertyAccess = (PropertyAccess) modelAccess;
				Object modelElement = propertyAccess.getElement();
				String propertyName = propertyAccess.getProperty();
				ConstraintExecution execution = (ConstraintExecution) mConstraintExecution;
				constraintPropertyAccesses.add(new ConstraintPropertyAccess(modelElement, propertyName, execution));
			}			
		}
		return constraintPropertyAccesses;
	}	
}

/*

Draft code from Antonio, found in EVLTrace.

protected Set<Execution> propertyModified(Object modelElement, String propertyName) {

   Set<Execution> invalidatedExecutions = propertyAccesses.stream().
           filter(propertyAccess -> propertyAccess.getModelElement() == modelElement && propertyName.equals(propertyAccess.getPropertyName())).
           map(propertyAccess -> propertyAccess.getExecution()).collect(Collectors.toSet());

   propertyAccesses.removeIf(propertyAccesses -> invalidatedExecutions.contains(propertyAccesses.getExecution()));

   return invalidatedExecutions;
}
*/
