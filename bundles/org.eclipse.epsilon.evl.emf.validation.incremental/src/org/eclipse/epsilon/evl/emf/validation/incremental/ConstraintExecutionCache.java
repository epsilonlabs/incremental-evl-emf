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
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.PropertyAccess;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.Trace;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.impl.ConstraintExecutionImpl;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.impl.PropertyAccessImpl;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.epsilon.evl.trace.ConstraintTraceItem;


// TODO The Cache needs to clean orphans from the Trace Accesses and Executions when removing references 


public class ConstraintExecutionCache {
	private static final Logger LOGGER = Logger.getLogger(ConstraintExecutionCache.class.getName());
	/*
	 * Logging levels
	 * 	- System activities finer
	 *  - System states finest
	 */	
	
	/* 
	 * This contains a Trace model that the last EVLtrace updated during an EVL execution.
	 * 
	 * In the context of an Execution Cache, methods are provided to manipulate the Cache content in the trace (e.g. remove executions relating to a notification).
	 * The Cache also provides methods to recall information about a prior executions based on some partial information (e.g. Model access or Constraint)
	 * 
	 */
	    
	protected Trace traceModel;

	// These would be replaced with indexes in an optimal solutions, for now we will used traceModel directly
	protected final Set<ConstraintTraceItem> constraintTraceItems = new HashSet<>(); // All executions of type ConstraintExecution
    protected final List<UnsatisfiedConstraint> unsatisfiedConstraints = new ArrayList<>(); // Subset of Executions of ConstraintExecution type with a result of false
    protected final List<ConstraintPropertyAccess> constraintPropertyAccess = new ArrayList<>();  // An execution refers to the PropertyAccess it made

    // These are only for debugging during the transition - delete them after the transition
    @Deprecated
    protected final Set<ConstraintTraceItem> modulesConstraintTraceItems;
    @Deprecated
    protected final List <ConstraintPropertyAccess> originalConstraintPropertyAccess;
    
    public ConstraintExecutionCache(IncrementalEvlModule lastModule) {
        
    	
    	// Legacy lists to be removed next
    	
    	//
    	// ConstraintTraceItem
    	
        //this.constraintTraceItems = lastModule.getContext().getConstraintTrace().getItems();
    	this.modulesConstraintTraceItems = lastModule.getContext().getConstraintTrace().getItems();

    	//
    	// ConstraintPropertyAccess
    	
    	//this.constraintPropertyAccess = lastModule.trace.propertyAccesses;
    	this.originalConstraintPropertyAccess = lastModule.evlTrace.originalCPA;

        // Pull in the traceModel from the last execution 
        traceModel = lastModule.evlTrace.traceModel;
        
        
        System.out.println("\n !!!! NEW CACHE !!!!! \n" + toString());
        
        System.out.println("mExecutions:" + traceModel.getExecutions().size()); 
        System.out.println("mAccesses:" + traceModel.getAccesses().size());
        
        if(lastModule.constraintExecutionCache.isPresent()) {
        	System.out.println("last constraintPropertyAccess:" + lastModule.constraintExecutionCache.get().constraintPropertyAccess.size());	
        }
        
    	for(ConstraintPropertyAccess oCPA : originalConstraintPropertyAccess) {
    		System.out.println("<oCPA> " + oCPA.hashCode() + " exec: " + oCPA.execution.hashCode() 
    		+ " ModelElement: " + oCPA.getModelElement() + " Property: " + oCPA.getPropertyName()); 
    		System.out.println("  CPAstring " + oCPA);
    		System.out.println("  exec " + oCPA.execution);
    	}
        
        
        // Temporary patch for compatibility walk through the executions in the trace model and populate the constrain property accesses 
        for (Execution mExecution : traceModel.getExecutions()) {
        	ConstraintExecutionImpl mConstraintExecution = (ConstraintExecutionImpl) mExecution;
        	Constraint rawConstraint = (Constraint) mConstraintExecution.getConstraint().getRaw();
        	Boolean result = mConstraintExecution.isResult();
        	System.out.println("[mEXECUTION] " + mExecution.hashCode() + " accesses: " + mExecution.getAccesses().size()
        			+ " context: " + mExecution.getContext().hashCode() + " constraint: " + rawConstraint.hashCode());

        	for (Access mAccess : mExecution.getAccesses()) {
        		PropertyAccess mPA = (PropertyAccess) mAccess;
        		
        		System.out.println("<mExc:mPA> " + mPA.hashCode() +" & " +mExecution.hashCode());
        		
        		Object modelElement = mPA.getElement();
        		String propertyName = mPA.getProperty();
        		ConstraintExecution execution = (ConstraintExecution) mConstraintExecution;

        		constraintPropertyAccess.add(new ConstraintPropertyAccess(modelElement,propertyName,execution));
        		
        		ConstraintTraceItem cti = new ConstraintTraceItem(modelElement,rawConstraint,result);
        		constraintTraceItems.add(cti);
        		System.out.println("CTI: "+rawConstraint.hashCode() + " " + cti);
        	}

        	// Create UnsatisfiedConstraint list
    		if (!result) {
    			UnsatisfiedConstraint uC = new UnsatisfiedConstraint();
    			uC.setConstraint(rawConstraint);
    			uC.setInstance(mExecution.getContext());
    			unsatisfiedConstraints.add(uC);
    			
    			System.out.println("Result: " + result + " Added uC: "+uC.getConstraint().getName());
    		}
        	
        }
        
        System.out.println("\n !!!! NEW CACHE BUILT !!!!! \n" + toString());
        
        System.out.println("mExecutions:" + traceModel.getExecutions().size()); 
        System.out.println("mAccesses:" + traceModel.getAccesses().size());
        
        if (LOGGER.isLoggable(Level.FINE)) {
        	LOGGER.info(() -> "Setting up Execution Cache" + toString());
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
        	LOGGER.info(() -> "Request remove from cache (not Adapter)\n ModelElement: " 
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

    private void removeFromCache(EObject modelElement) {
    	LOGGER.info(() -> "Try and remove model element " + modelElement.hashCode() + " with ANY feature from execution cache");

    	System.out.println("\n\nYOU MISSED THIS\n\n");
    	
    	// TODO remove property accesses from cached operations

        List <ConstraintPropertyAccess> constraintsToInvalidate = new ArrayList<>(); // List of constraintpropertyaccesses for model/feature to be invalidated

        // find any properyAccesses (make a list of contraints) and delete them

        for (Iterator<ConstraintPropertyAccess> itr = constraintPropertyAccess.iterator(); itr.hasNext(); ) {
            ConstraintPropertyAccess cpa = itr.next();
            if( cpa.getModelElement() == modelElement ) {
            	LOGGER.info(() -> "marked for clearing and removed " + cpa.toString());
            	constraintsToInvalidate.add(cpa);
            	itr.remove();
            }
        }
        clearConstraintTracesAndUnsatisfiedConstraints(constraintsToInvalidate);
    }
    
    private void removeFromCache(EObject modelElement, EStructuralFeature modelFeature ) {
    	LOGGER.info(() -> "Try and remove model element " + modelElement.hashCode() + " with feature " + modelFeature.getName() + " from execution cache");

    	// TODO remove property accesses from cached operations
    	System.out.println("removeFromCache() traceModel before");
    	System.out.println("traceModel Accesses: " + traceModel.getAccesses().size());
    	System.out.println("traceModel Executions: " + traceModel.getExecutions().size());
    	
    	
    	// Delete from the the traceModel
    	EList<Access> accessList = traceModel.getAccesses();
    	for(Iterator<Access> itr = accessList.iterator(); itr.hasNext();)
    	{
    		PropertyAccess propertyAccess = (PropertyAccess) itr.next(); 
    		System.out.print(">>> propertyAccess: " + propertyAccess.hashCode());
    		if(propertyAccess.getElement() == modelElement) {
    			System.out.print(" delete? " + propertyAccess.getProperty() + "==" + modelFeature.getName());
    			if(propertyAccess.getProperty().contentEquals(modelFeature.getName())) {
    				System.out.print (" yes");
    				removeOrphenedExecutionsFromTraceModel(propertyAccess);
    				itr.remove();
    			}
    			
    		}
    		System.out.println();

    	}  	
    	System.out.println("removeFromCache() traceModel after");
    	System.out.println("traceModel Accesses: " + traceModel.getAccesses().size());
    	System.out.println("traceModel Executions: " + traceModel.getExecutions().size());
    	
    	
    	// Make a method which makes a list of constraints related to the model element and feature
        List <ConstraintPropertyAccess> constraintsToInvalidate = new ArrayList<>(); // List of constraintpropertyaccesses for model/feature to be invalidated

        // find any properyAccesses (make a list of constraints) and delete them

        for (Iterator<ConstraintPropertyAccess> itr = constraintPropertyAccess.iterator(); itr.hasNext(); ) {    	
            ConstraintPropertyAccess cpa = (ConstraintPropertyAccess) itr.next();
            System.out.println("<itr CPA> " + cpa);
            
            if( ( cpa.getModelElement() == modelElement )
                    && 
                ( cpa.getPropertyName().equals(modelFeature.getName()) ) ){
            	LOGGER.info(() -> "marked for clearing and removed " + cpa.toString());
                constraintsToInvalidate.add(cpa);
                itr.remove();
            }
        }

        clearConstraintTracesAndUnsatisfiedConstraints(constraintsToInvalidate);
    }

    private void removeOrphenedExecutionsFromTraceModel(Access access) {
    	System.out.println("removeOrphenedExecutions: ");
    	for (Iterator<Execution> itr = access.getExecutions().iterator(); itr.hasNext();) {
    		ConstraintExecution execution = (ConstraintExecution) itr.next();
    		
    		System.out.println("exec: " + execution.hashCode() + " has accesses: " + execution.getAccesses().size() + " Constraint: " + execution.getConstraint());    		
    		removeExecutionsWithConstraintFromTraceModel(execution.getConstraint());
    		if(execution.getAccesses().size()<2) {
    			System.out.println("\n [!] Execution with no accesses deleted");
    			itr.remove();
    		}
    	}
    }
    
    private void removeExecutionsWithConstraintFromTraceModel(org.eclipse.epsilon.evl.emf.validation.incremental.trace.Constraint constraint) {
    	System.out.println("removeExecutionsWithConstraint: "+ constraint);
    	for (Iterator<Execution> itr = traceModel.getExecutions().iterator(); itr.hasNext();) {
    		ConstraintExecution execution = (ConstraintExecution) itr.next();
    		
    		System.out.println("exec: " + execution.hashCode() + " has Constraint: " + execution.getConstraint());    		
    		
    		if(execution.getConstraint() == constraint) {
    			System.out.println("\n [!] Execution with constraint deleted: " + execution.hashCode() + ":"+constraint);
    			itr.remove();
    		}
    	}
    }
    
    
    private void clearConstraintTracesAndUnsatisfiedConstraints(List <ConstraintPropertyAccess> constraintsToInvalidate) {
    	LOGGER.info(() -> "List of constraintsToInvalidate: " + constraintsToInvalidate);    
        
        // Using the list of constraints to invalidate, remove constraint trace/unsatisfied based on model & constraint matches       
        for (ConstraintPropertyAccess invalidcpa : constraintsToInvalidate) {
        	
        	// find any constraintTraceItems and delete them
        	for (Iterator<ConstraintTraceItem> itr = constraintTraceItems.iterator(); itr.hasNext(); ) {
                ConstraintTraceItem ctitem = itr.next();               
                if (ctitem.getInstance() == invalidcpa.getExecution().getContext()
                		
                        && ctitem.getConstraint() == invalidcpa.getExecution().getConstraint().getRaw()) { // need to resolve "feature" for each result
                	LOGGER.finer(() -> "Removed constraintTraceItem model hash " + ctitem.getInstance().hashCode() + " constraint " + ctitem.getConstraint().getName() );               
                    itr.remove();
                }
            }

            // find any unstatisfiedConstraints and delete them
            for (Iterator<UnsatisfiedConstraint> itr = unsatisfiedConstraints.iterator(); itr.hasNext(); ) {
                UnsatisfiedConstraint uc = itr.next();
                if (uc.getInstance() == invalidcpa.getExecution().getContext()
                        && uc.getConstraint() == invalidcpa.getExecution().getConstraint().getRaw() ) {
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
        sj.add(constraintTraceToString(constraintTraceItems));
        sj.add(unsatisfiedConstraintsToString(unsatisfiedConstraints));
        //sj.add(constraintPropertyAccessToString(constraintPropertyAccess));
        sj.add(this.constraintPropertyAccess.toString());
        sj.add(" =========================== ");

        return sj.toString();
    }

}
