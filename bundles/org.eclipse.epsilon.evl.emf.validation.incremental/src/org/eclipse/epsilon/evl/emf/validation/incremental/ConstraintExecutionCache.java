package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.epsilon.evl.trace.ConstraintTraceItem;

public class ConstraintExecutionCache {
	private static final Logger logger = Logger.getLogger(ConstraintExecutionCache.class.getName());
	private boolean REPORT = false;
	private boolean REPORTactivity = false;
	private boolean REPORTstate = false;
	
    // The Module.getContext() grants access to contraintTrace and UnsatisfiedConstraint, but you can't delete items from them; so we make our own lists in here to delete items from
    
    protected Set<ConstraintTraceItem> constraintTraceItems;
    protected Collection<UnsatisfiedConstraint> unsatisfiedConstraints;
    protected List <ConstraintPropertyAccess> constraintPropertyAccess;

    public ConstraintExecutionCache(IncrementalEvlModule lastModule) {
        // Change these to "defensive copies"?
        this.constraintTraceItems = lastModule.getContext().getConstraintTrace().getItems();
        this.unsatisfiedConstraints = lastModule.getContext().getUnsatisfiedConstraints();
        this.constraintPropertyAccess = lastModule.trace.propertyAccesses;

        if (REPORTactivity){logger.log(Level.INFO,"Setting up Execution Cache" + executionCacheToString());}
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
    	if(REPORTactivity) {logger.log(Level.INFO,"Execution cache - checkCachedConstraintTrace - " + model.hashCode() + " " + constraint.getName());}
    	if(REPORTstate) logger.log(Level.INFO,constraintTraceToString(constraintTraceItems));
    	for (ConstraintTraceItem item : constraintTraceItems) {
            if (item.getInstance().equals(model) && item.getConstraint().equals(constraint)) {
                if(REPORTactivity) {logger.log(Level.INFO,"Execution cache - MATCHED model & constraint - " + item.hashCode() + " " + constraint.getName());}
                return item;
            }
        }
    	if(REPORTactivity) {logger.log(Level.INFO,"Execution cache - NO MATCHES");}
        return null;
    }

    public UnsatisfiedConstraint getCachedUnsatisfiedConstraint (Object model, Constraint constraint) {
    	if(REPORTactivity) {logger.log(Level.INFO,"Execution cache - getCachedUnsatisfiedConstraint - " + model.hashCode() + " " + constraint.getName());}
    	if(REPORTstate) {logger.log(Level.INFO, unsatisfiedConstraintsToString(unsatisfiedConstraints));}
        for (UnsatisfiedConstraint unsatisfiedConstraint : unsatisfiedConstraints) {
            if ( unsatisfiedConstraint.getInstance().equals(model) && unsatisfiedConstraint.getConstraint().equals(constraint)  ) {
            	if(REPORTactivity) {logger.log(Level.INFO, "Execution cache - MATCHED UC -  UC Result: " + unsatisfiedConstraint.getConstraint().getName());}
                return unsatisfiedConstraint;
            }        
        }
        if(REPORTactivity) {logger.log(Level.INFO, "Execution cache - NO MATCHES");}   
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
        	removeFromCache(modelElement, modelFeature);
        	break;
        case Notification.REMOVING_ADAPTER:
        	if (notification.getOldValue() instanceof IncrementalEvlValidatorAdapter) {
        		removeFromCache(modelElement);
        	}
        	break;
        }
    }

    private void removeFromCache(EObject modelElement, EStructuralFeature modelFeature ) {
    	if(REPORTactivity) {logger.log(Level.INFO, "Try and remove model element " + modelElement.hashCode() + " with feature " + modelFeature.getName() + " from execution cache");}
    	
    	// Make a method which makes a list of constraints related to the model element and feature
        List <ConstraintPropertyAccess> constraintsToInvalidate = new ArrayList<>(); // List of constraintpropertyaccesses for model/feature to be invalidated
        // find any properyAccesses (make a list of constraints) and delete them
        for (Iterator<ConstraintPropertyAccess> itr = constraintPropertyAccess.iterator(); itr.hasNext(); ) {
            ConstraintPropertyAccess cpa = (ConstraintPropertyAccess) itr.next();
            if( ( cpa.getModelElement() == modelElement )
                    && 
                ( cpa.getPropertyName().equals(modelFeature.getName()) ) ){
                if(REPORTactivity) {logger.log(Level.INFO, "marked for clearing and removed " + cpa.toString());}
                constraintsToInvalidate.add(cpa);
                itr.remove();
            }
        }
        clearConstraintTracesAndUnsatisfiedConstraints(constraintsToInvalidate);
        
    }

    private void removeFromCache(EObject modelElement) {
        //System.out.println("\nremoveFromCache: " + modelElement.hashCode() + " " + modelElement);
        if(REPORTactivity) {logger.log(Level.INFO, "Try and remove model element " + modelElement.hashCode() + " with ANY feature from execution cache");}

        List <ConstraintPropertyAccess> constraintsToInvalidate = new ArrayList<>(); // List of constraintpropertyaccesses for model/feature to be invalidated

        // find any properyAccesses (make a list of contraints) and delete them
        for (Iterator<ConstraintPropertyAccess> itr = constraintPropertyAccess.iterator(); itr.hasNext(); ) {
            ConstraintPropertyAccess cpa = itr.next();
            if( cpa.getModelElement() == modelElement ) {
            	if(REPORTactivity) {logger.log(Level.INFO, "marked for clearing and removed " + cpa.toString());}
            	constraintsToInvalidate.add(cpa);
            	itr.remove();
            }
        }

        clearConstraintTracesAndUnsatisfiedConstraints(constraintsToInvalidate);
    }
    
    private void clearConstraintTracesAndUnsatisfiedConstraints(List <ConstraintPropertyAccess> constraintsToInvalidate) {
        if(REPORTstate) {logger.log(Level.INFO,"List of constraintsToInvalidate: " + constraintsToInvalidate);}        
        
        // Using the list of constraints to invalidate, remove constraint trace/unsatisfied based on model & constraint matches       
        for (ConstraintPropertyAccess invalidcpa : constraintsToInvalidate) {
            
        	// find any constraintTraceItems and delete them
        	for (Iterator<ConstraintTraceItem> itr = constraintTraceItems.iterator(); itr.hasNext(); ) {
                ConstraintTraceItem ctitem = itr.next();
                if (ctitem.getInstance() == invalidcpa.getModelElement()
                        && ctitem.getConstraint() == invalidcpa.getExecution().getConstraint()) { // need to resolve "feature" for each result
                    if(REPORTactivity) {logger.log(Level.INFO, "Removed constraintTraceItem model hash " + ctitem.getInstance().hashCode() + " constraint " + ctitem.getConstraint().getName() );}               
                    itr.remove();
                }
            }

            // find any unstatisfiedConstraints and delete them
            for (Iterator<UnsatisfiedConstraint> itr = unsatisfiedConstraints.iterator(); itr.hasNext(); ) {
                UnsatisfiedConstraint uc = itr.next();
                if (uc.getInstance() == invalidcpa.getModelElement()
                        && uc.getConstraint() == invalidcpa.getExecution().getConstraint() ) {
                	if(REPORTactivity) {logger.log(Level.INFO, "Removed unsatisfiedConstraint model hash " + uc.getInstance().hashCode() + " constraint " + uc.getConstraint().getName() );}
                    itr.remove();
                }

            }
        }
    }
    
    
    // Duplicate code
    /*
    private void clearConstraintTracesAndUnsatisfiedConstraintsB(List <ConstraintPropertyAccess> constraintsToInvalidate) {
    	// Using the list of constraintsTobe invalidated remove constraint trace/unsatisfied based on model & constraint matches
        System.out.println("\nClear Constraint Trace/Unsatisfied where Model & Constraint match removed constraintPropertyAccesses");
        for (ConstraintPropertyAccess invalidcpa : constraintsToInvalidate) {
            // find any constraintTraceItems and delete them
            for (Iterator<ConstraintTraceItem> itr = constraintTraceItems.iterator(); itr.hasNext(); ) {
                ConstraintTraceItem ctitem = (ConstraintTraceItem) itr.next();
                if (ctitem.getInstance() == invalidcpa.getModelElement()
                        && ctitem.getConstraint() == invalidcpa.getExecution().getConstraint()) { // need to resolve "feature" for each result
                    System.out.println("  - Notification: ConstraintTraceItem Removed "
                            + ctitem.getInstance().hashCode() + " == " + invalidcpa.getModelElement().hashCode()
                            + " && "
                            + ctitem.getConstraint().getName() + " == " + invalidcpa.getExecution().getConstraint().getName() );
                    itr.remove();
                }
            }

            // find any unstatisfiedConstraints and delete them
            for (Iterator<UnsatisfiedConstraint> itr = unsatisfiedConstraints.iterator(); itr.hasNext(); ) {
                UnsatisfiedConstraint uc = itr.next();
                if (uc.getInstance() == invalidcpa.getModelElement()
                        && uc.getConstraint() == invalidcpa.getExecution().getConstraint() ) {
                    System.out.println("  - Notification: UnsatisfiedConstraints Removed "
                            + uc.getInstance().hashCode() + " == " + invalidcpa.getModelElement().hashCode()
                            + " && "
                            + uc.getConstraint().getName() + " == " + invalidcpa.getExecution().getConstraint().getName() );
                    itr.remove();
                }

            }
        }
    }
    */
    

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
    
    private String constraintPropertyAccessToString(List<ConstraintPropertyAccess> constraintPropertyAccess) {
    	int i = 0;
        String cpaString = "\n[Trace] (Constraint)PropertyAccess list: ";


        for (ConstraintPropertyAccess cpa : constraintPropertyAccess) {
            i++;
            cpaString = cpaString.concat("\n  " + i 
            		+ ", Constraint: " + cpa.execution.constraint.getName()
            		+ " | Model hashcode: " + cpa.getModelElement().hashCode());
        }
        return cpaString;
    }
    
    public String executionCacheToString () {
        // Dump the lists to the Console for inspection
    	String exeCacheString = 				"\n == Execution Cache state ==";
    	
    	
    	exeCacheString = exeCacheString.concat("\n ContraintTraceItems: " + constraintTraceItems.size());
    	exeCacheString = exeCacheString.concat("\n UnsatisfiedConstraints: " + unsatisfiedConstraints.size()) ;
    	exeCacheString = exeCacheString.concat("\n ConstraintPropertyAccesses: " + this.constraintPropertyAccess.size()) ;

        int i = 0;

        exeCacheString = exeCacheString.concat(constraintTraceToString(constraintTraceItems));
        /*
        exeCacheString = exeCacheString.concat("\nConstraintTrace list: ");
        i = 0;
        for (ConstraintTraceItem item : constraintTraceItems) {
            i++;
            exeCacheString = exeCacheString.concat("\n  " + i 
            		+ ", Constraint: " + item.getConstraint().getName() + " " + item.getConstraint().hashCode()
                    + " | Model hashcode: " + item.getInstance().hashCode()
                    + " | Result : " + item.getResult()
            );
        }
        */
        exeCacheString = exeCacheString.concat(unsatisfiedConstraintsToString(unsatisfiedConstraints));
        /*
        i = 0;
        exeCacheString = exeCacheString.concat("\nUnsatisfiedConstraint list: ");
        for (UnsatisfiedConstraint uc : unsatisfiedConstraints) {
            i++;
            exeCacheString = exeCacheString.concat("\n  " + i 
            		+ ", Constraint: " + uc.getConstraint().getName() + " " + uc.getConstraint().hashCode() 
            		+ " | Model hashcode: " + uc.getInstance().hashCode()
            );
        }
        */
        
        exeCacheString = exeCacheString.concat(constraintPropertyAccessToString(constraintPropertyAccess));
        /*
        i = 0;
        exeCacheString = exeCacheString.concat("\n[Trace] (Constraint)PropertyAccess list: ");
        for (ConstraintPropertyAccess cpa : constraintPropertyAccess) {
            i++;
            exeCacheString = exeCacheString.concat("\n  " + i 
            		+ ", Constraint: " + cpa.execution.constraint.getName()
            		+ " | Model hashcode: " + cpa.getModelElement().hashCode());
        }        
        */
        exeCacheString = exeCacheString.concat(	"\n ===========================\n");
        return exeCacheString;
    }

}
