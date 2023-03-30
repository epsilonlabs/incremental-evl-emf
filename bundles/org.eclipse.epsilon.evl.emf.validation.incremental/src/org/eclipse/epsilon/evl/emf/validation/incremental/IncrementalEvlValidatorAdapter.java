package org.eclipse.epsilon.evl.emf.validation.incremental;

import static org.eclipse.epsilon.evl.emf.validation.incremental.IncrementalEcoreValidator.MYLOGGER;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.*;


import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.epsilon.evl.trace.ConstraintTraceItem;


public class IncrementalEvlValidatorAdapter extends EContentAdapter {
	private static final Logger logger = Logger.getLogger(IncrementalEvlValidatorAdapter.class.getName());
	
//    private boolean REPORT = false;
    private boolean REPORTstate = true;
    private boolean REPORTactivities = false;
    private boolean REPORTnotification = true;
    private int validationCount = 0;

    protected IncrementalEvlModule module;

    public IncrementalEvlModule getModule() {
    	return module;
    }

    protected Optional<ConstraintExecutionCache> constraintExecutionCache = Optional.empty();
    protected IncrementalEvlValidator validator = null;
    protected List<Notification> notifications = new ArrayList<>();

    public IncrementalEvlValidatorAdapter(IncrementalEvlValidator validator) {
        this.validator = validator;
    }

    public void revalidate(ResourceSet resourceSet) throws Exception {  
    	logger.entering(getClass().getName(),"revalidating: " + resourceSet.toString());
    	
    	logger.log(Level.INFO,"\n [!] IncrementalEvlValidatorAdapter.revalidate() called: " + resourceSet + "\n");
        //MYLOGGER.log(MyLog.FLOW, "\n [!] IncrementalEvlValidatorAdapter.revalidate() called: " + resourceSet + "\n");
        validate(resourceSet);
        notifications.clear();
        
        logger.exiting(getClass().getName(), "revalidate complete " + resourceSet.toString());
    }

    public void validate(ResourceSet resourceSet) throws Exception {
        validationCount++;
        //System.out.println("\n ** RUNNING VALIDATION " + validationCount + " **");
        
        if(REPORTactivities) {logger.log(Level.INFO, "\n [!] IncrementalEvlValidatorAdapter.validate() called\n");}

        // Make an in memory version of the Model (root element) for testing
        InMemoryEmfModel model = new InMemoryEmfModel(resourceSet.getResources().get(0));
        model.setConcurrent(true);

        
        // Console output
        if (REPORTstate) {
        	String log = "\nModel name: '" + model.getName() + "' hashCode: " + model.hashCode() + "\nModel elements: ";
            Collection<EObject> elements = model.allContents();
            for (EObject e : elements) {
                log = log.concat("\nhashCode: " + e.hashCode() + " object: " + e.toString());
            }
            logger.log(Level.INFO, log);
        }


        // Module for doing evaluations
        // Two ways to setup a module, with and without prior knowledge of the last validation
        // -------- INITIALISE --------

        // Swap out the module and create a new one

        // Replace lastModule with constraintExecutionCache
        if(constraintExecutionCache.isPresent()){
            module = new IncrementalEvlModule(constraintExecutionCache);
        }
        else {
            module = new IncrementalEvlModule();
        }

        // Load constraints to be checked -- The list from the validator could be pruned using results from lastModule
        module.parse(validator.getConstraints());    // constraints ArrayList<E>[]

        // Load the model to be checked -- Always needs to be "fresh" for testing
        module.getContext().getModelRepository().addModel(model);

        // Console output
        if (REPORTstate) {
        	String log = "\nConstraints to Execute: ";
            List<Constraint> constraintsToExecute = module.getConstraints();
            int i = 0;
            for (Constraint c : constraintsToExecute) {
                i++;
                log = log.concat("\n" + i + ", " + c.getName() + " " + c.hashCode());
            }
            logger.log(Level.INFO, log);
        }

        


        // -------- EXECUTION --------
        //Set<UnsatisfiedConstraint> unsatisfiedConstraints = module.execute();
        
        if(REPORTactivities) {logger.log(Level.INFO, "\n [!] ...Executing validation...\n");}
        module.execute();

        // -------- PROCESS RESULTS --------

        // Extract from module data to populate the ExecutionCache for the next run.

        // Pass module to ExecutionCache constructor and make a NEW one to replace the existing one
        // Constructor extracts = (Constraint)PropertyAccess & ContraintTrace & UnsatisfiedConstraints
        if(REPORTactivities) {logger.log(Level.INFO,"\n [i] Adapter constraintExecutionCache created");}
        constraintExecutionCache = Optional.of (new ConstraintExecutionCache(module));

        // Console output
        if(REPORTstate) {logger.log(Level.INFO, modelStateToString());}
    }

    @Override
    public void notifyChanged(Notification notification) {
        super.notifyChanged(notification);

        if (notification.getNotifier() instanceof EObject) {
            notifications.add(notification);  // Can be removed, won't need a list of update notifications, pass them as they occur to the Trace
            EObject modelElement = (EObject) notification.getNotifier();
            EStructuralFeature modelFeature = (EStructuralFeature) notification.getFeature();
            if(REPORTnotification) {
            	String notificationText = ("\n[MODEL CHANGE NOTIFICATION]  " 
            			+ " Type:" + notification.getEventType()
                        + "\n " + notification);
                        
                
                if(notification.getEventType() != 8){
                    // Type 8 removes the adapter and produces NULL conditions
                        notificationText = notificationText.concat(
                        " element: " + modelElement.hashCode() + " " + EcoreUtil.getURI(modelElement)
                        + "\n feature: " + modelFeature.getName()
                        + "\n was: " + notification.getOldValue()
                        + "\n now: " + notification.getNewValue()
                        + "\n");
                }
                logger.log(Level.INFO, notificationText);
            }

            // IF there is an constraintExecutionCache, then we need update ConstraintTrace and UnsatisfiedConstraints lists
            if(constraintExecutionCache.isPresent()){
                constraintExecutionCache.get().processModelNotification(notification);
                if(REPORTstate) {logger.log(Level.INFO, constraintExecutionCache.get().executionCacheToString());}
            }
        }
    }

    public IncrementalEvlValidator getValidator() {
        return validator;
    }

    public boolean mustRevalidate(ResourceSet resourceSet) {
        return !notifications.isEmpty();
    }

    public String modelStateToString () {
    	String stateString = "\n == Module results ==";
    	
    	stateString = stateString.concat("\nUnsatisfiedConstraints: " + module.getContext().getUnsatisfiedConstraints().size()) ;

        int i = 0;
        stateString = stateString.concat("\nConstrainPropertyAccess list: ");
        for (ConstraintPropertyAccess cpa : module.getTrace().propertyAccesses) {
            i++;
            stateString = stateString.concat("\n" + i 
            		+ ", Constraint: " + cpa.execution.constraint.getName() + " " + cpa.execution.constraint.hashCode()
            		+ " | Model hashcode: " + cpa.getModelElement().hashCode() );            
        }

        i = 0;
        stateString = stateString.concat("\nConstraintTrace list: ");
        for (ConstraintTraceItem item : module.getContext().getConstraintTrace()) {
            i++;
            stateString = stateString.concat("\n" + i 
            		+ ", Constraint: " + item.getConstraint().getName() + " " + item.getConstraint().hashCode()
                    + " | Model hashcode: " + item.getInstance().hashCode()
                    + " | Result : " + item.getResult()
            );
        }

        i = 0;
        stateString = stateString.concat("\nUnsatisfiedConstraint list: ");
        for (UnsatisfiedConstraint uc : module.getContext().getUnsatisfiedConstraints()) {
            i++;
            stateString = stateString.concat("\n" + i 
            		+ ", Constraint: " + uc.getConstraint().getName() + " " + uc.getConstraint().hashCode() 
            		+ " | Model hashcode: " + uc.getInstance().hashCode()
            );
        }

        stateString = stateString.concat("\n ====================\n");
        
        return stateString;
    }

}
