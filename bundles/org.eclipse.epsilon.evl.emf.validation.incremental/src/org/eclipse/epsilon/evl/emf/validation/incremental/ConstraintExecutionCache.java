package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.epsilon.evl.trace.ConstraintTraceItem;

public class ConstraintExecutionCache {
	private static final Logger LOGGER = Logger.getLogger(ConstraintExecutionCache.class.getName());
	/*
	 * Logging levels
	 * 	- System activities finer
	 *  - System states finest
	 */	
	
    // The Module.getContext() grants access to contraintTrace and UnsatisfiedConstraint, but you can't delete items from them; so we make our own lists in here to delete items from
    
    protected Set<ConstraintTraceItem> constraintTraceItems;
    protected Collection<UnsatisfiedConstraint> unsatisfiedConstraints;

    // TODO move into ConstraintExecution class, and replace with a List<Execution>
    protected List<ConstraintPropertyAccess> constraintPropertyAccess;

    public ConstraintExecutionCache(IncrementalEvlModule lastModule) {
        // Change these to "defensive copies"?
        this.constraintTraceItems = lastModule.getContext().getConstraintTrace().getItems();
        this.unsatisfiedConstraints = lastModule.getContext().getUnsatisfiedConstraints();
        this.constraintPropertyAccess = lastModule.trace.propertyAccesses;

        if (LOGGER.isLoggable(Level.FINE)) {
        	LOGGER.finer(() -> "Setting up Execution Cache" + toString());
        }
    }

    public List <ConstraintPropertyAccess> getConstraintsPropertyAccessFor (EObject modelElement, EStructuralFeature modelFeature) {
        // Given a model Element and Feature, find all constraints as constraint property access for the model element & feature
        List <ConstraintPropertyAccess> matchedcpa = new ArrayList<>();
        for (ConstraintPropertyAccess cpa : constraintPropertyAccess) {
            if(cpa.getModelElement() == modelElement && cpa.getPropertyName().equals(modelFeature.getName())) {
                matchedcpa.add(cpa);
            }
        }
        return matchedcpa;
    }

    public ConstraintTraceItem checkCachedConstraintTrace (Object model, Constraint constraint) {
    	LOGGER.finer(() -> "Execution cache - checkCachedConstraintTrace - " + model.hashCode() + " " + constraint.getName());
    	LOGGER.finest(() -> constraintTraceToString(constraintTraceItems));

    	for (ConstraintTraceItem item : constraintTraceItems) {
            if (item.getInstance().equals(model) && item.getConstraint().equals(constraint)) {
                LOGGER.finer(() -> "Execution cache - MATCHED model & constraint - " + item.hashCode() + " " + constraint.getName());
                return item;
            }
        }

    	LOGGER.finer(() -> "Execution cache - NO MATCHES");
        return null;
    }

    public UnsatisfiedConstraint getCachedUnsatisfiedConstraint (Object model, Constraint constraint) {
    	LOGGER.finer(() -> "Execution cache - getCachedUnsatisfiedConstraint - " + model.hashCode() + " " + constraint.getName());
    	LOGGER.finest(() -> unsatisfiedConstraintsToString(unsatisfiedConstraints));
        for (UnsatisfiedConstraint unsatisfiedConstraint : unsatisfiedConstraints) {
            if ( unsatisfiedConstraint.getInstance().equals(model) && unsatisfiedConstraint.getConstraint().equals(constraint)  ) {
            	LOGGER.finer(() -> "Execution cache - MATCHED UC -  UC Result: " + unsatisfiedConstraint.getConstraint().getName());
                return unsatisfiedConstraint;
            }        
        }
		LOGGER.finer(() -> "Execution cache - NO MATCHES");   
        return null; // This should not happen if there is an entry on the trace.
    }

    public void processModelNotification (Notification notification ) {
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
        	LOGGER.fine(() -> "Request remove from cache (not Adapter)\n ModelElement: " 
        			+ modelElement + "\n ModelFeature: " + modelFeature);
        	removeFromCache(modelElement, modelFeature);
        	break;
        case Notification.REMOVING_ADAPTER:
        	if (notification.getOldValue() instanceof IncrementalEvlValidatorAdapter) {
        		removeFromCache(modelElement);
        		LOGGER.fine(() -> "Request remove from cache (Adapter)\n ModelElement: " 
        				+ modelElement + "\n ModelFeature: " + modelFeature);
        	}
        	break;
        }
    }

    private void removeFromCache(EObject modelElement, EStructuralFeature modelFeature ) {
    	LOGGER.finer(() -> "Try and remove model element " + modelElement.hashCode() + " with feature " + modelFeature.getName() + " from execution cache");

    	// TODO remove property accesses from cached operations

    	// Make a method which makes a list of constraints related to the model element and feature
        List <ConstraintPropertyAccess> constraintsToInvalidate = new ArrayList<>(); // List of constraintpropertyaccesses for model/feature to be invalidated

        // find any properyAccesses (make a list of constraints) and delete them
        for (Iterator<ConstraintPropertyAccess> itr = constraintPropertyAccess.iterator(); itr.hasNext(); ) {
            ConstraintPropertyAccess cpa = (ConstraintPropertyAccess) itr.next();
            if( ( cpa.getModelElement() == modelElement )
                    && 
                ( cpa.getPropertyName().equals(modelFeature.getName()) ) ){
            	LOGGER.finer(() -> "marked for clearing and removed " + cpa.toString());
                constraintsToInvalidate.add(cpa);
                itr.remove();
            }
        }
        clearConstraintTracesAndUnsatisfiedConstraints(constraintsToInvalidate);
    }

    private void removeFromCache(EObject modelElement) {
    	LOGGER.finer(() -> "Try and remove model element " + modelElement.hashCode() + " with ANY feature from execution cache");

    	// TODO remove property accesses from cached operations

        List <ConstraintPropertyAccess> constraintsToInvalidate = new ArrayList<>(); // List of constraintpropertyaccesses for model/feature to be invalidated

        // find any properyAccesses (make a list of contraints) and delete them
        for (Iterator<ConstraintPropertyAccess> itr = constraintPropertyAccess.iterator(); itr.hasNext(); ) {
            ConstraintPropertyAccess cpa = itr.next();
            if( cpa.getModelElement() == modelElement ) {
            	LOGGER.finer(() -> "marked for clearing and removed " + cpa.toString());
            	constraintsToInvalidate.add(cpa);
            	itr.remove();
            }
        }

        clearConstraintTracesAndUnsatisfiedConstraints(constraintsToInvalidate);
    }
    
    private void clearConstraintTracesAndUnsatisfiedConstraints(List <ConstraintPropertyAccess> constraintsToInvalidate) {
    	LOGGER.finest(() -> "List of constraintsToInvalidate: " + constraintsToInvalidate);    
        
        // Using the list of constraints to invalidate, remove constraint trace/unsatisfied based on model & constraint matches       
        for (ConstraintPropertyAccess invalidcpa : constraintsToInvalidate) {
            
        	// find any constraintTraceItems and delete them
        	for (Iterator<ConstraintTraceItem> itr = constraintTraceItems.iterator(); itr.hasNext(); ) {
                ConstraintTraceItem ctitem = itr.next();
                if (ctitem.getInstance() == invalidcpa.getExecution().getSelf()
                        && ctitem.getConstraint() == invalidcpa.getExecution().getUnit()) { // need to resolve "feature" for each result
                	LOGGER.finer(() -> "Removed constraintTraceItem model hash " + ctitem.getInstance().hashCode() + " constraint " + ctitem.getConstraint().getName() );               
                    itr.remove();
                }
            }

            // find any unstatisfiedConstraints and delete them
            for (Iterator<UnsatisfiedConstraint> itr = unsatisfiedConstraints.iterator(); itr.hasNext(); ) {
                UnsatisfiedConstraint uc = itr.next();
                if (uc.getInstance() == invalidcpa.getExecution().getSelf()
                        && uc.getConstraint() == invalidcpa.getExecution().getUnit() ) {
                	LOGGER.finer(() -> "Removed unsatisfiedConstraint model hash " + uc.getInstance().hashCode() + " constraint " + uc.getConstraint().getName() );
                    itr.remove();
                }

            }
        }
    }
    
    private String constraintTraceToString (Set<ConstraintTraceItem> constraintTraceItems) {
    	int i = 0;
    	String ctString = "\nConstraintTrace list: ";       
        for (ConstraintTraceItem item : constraintTraceItems) {
            i++;
            ctString = ctString.concat("\n  " + i 
            		+ ", Constraint: " + item.getConstraint().getName() + " " + item.getConstraint().hashCode()
                    + " | Model hashcode: " + item.getInstance().hashCode()
                    + " | Result : " + item.getResult()
            );
        }
        return ctString;
    }
    
    private String  unsatisfiedConstraintsToString (Collection<UnsatisfiedConstraint> unsatisfiedConstraints ) {
        int i = 0;
        String ucString = "\nUnsatisfiedConstraint list: ";
        for (UnsatisfiedConstraint uc : unsatisfiedConstraints) {
            i++;
            ucString = ucString.concat("\n  " + i 
            		+ ", Constraint: " + uc.getConstraint().getName() + " " + uc.getConstraint().hashCode() 
            		+ " | Model hashcode: " + uc.getInstance().hashCode()
            );
        }
        return ucString;
    }

    @Override
    public String toString () {
    	StringJoiner sj = new StringJoiner("\n");

    	sj.add(" == Execution Cache state ==");
    	sj.add("ContraintTraceItems: " + constraintTraceItems.size());
    	sj.add("UnsatisfiedConstraints: " + unsatisfiedConstraints.size());
    	sj.add("ConstraintPropertyAccesses: " + this.constraintPropertyAccess.size());

    	// TODO: pass sj into these functions to keep building up the string
    	// Commenting out the details for demo
        //sj.add(constraintTraceToString(constraintTraceItems));
        //sj.add(unsatisfiedConstraintsToString(unsatisfiedConstraints));
        //sj.add(constraintPropertyAccessToString(constraintPropertyAccess));
        sj.add(" =========================== ");

        return sj.toString();
    }

}
