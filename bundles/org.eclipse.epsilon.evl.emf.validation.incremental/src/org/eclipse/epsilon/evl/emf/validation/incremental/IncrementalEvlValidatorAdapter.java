package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.epsilon.evl.trace.ConstraintTraceItem;


public class IncrementalEvlValidatorAdapter extends EContentAdapter {
	private static final Logger LOGGER = Logger.getLogger(IncrementalEvlValidatorAdapter.class.getName());

    protected IncrementalEvlModule module;

    public IncrementalEvlModule getModule() {
    	return module;
    }
    
	protected final Optional<Consumer<IEolContext>> contextSetup;
    protected Optional<ConstraintExecutionCache> constraintExecutionCache = Optional.empty();
    protected IncrementalEvlValidator validator = null;
    protected List<Notification> notifications = new ArrayList<>();

    public IncrementalEvlValidatorAdapter(IncrementalEvlValidator validator, Optional<Consumer<IEolContext>> contextSetup) {
        this.validator = validator;
        this.contextSetup = contextSetup;
    }

    public void revalidate(ResourceSet resourceSet) throws Exception {     	
    	LOGGER.finer("\n [!] IncrementalEvlValidatorAdapter.revalidate() called: " + resourceSet + "\n");
        validate(resourceSet);
        notifications.clear();
    }

    public void validate(ResourceSet resourceSet) throws Exception {
        LOGGER.finer("\n [!] IncrementalEvlValidatorAdapter.validate() called\n");

        // Make an in memory version of the MODEL (root element) for testing
        InMemoryEmfModel model = new InMemoryEmfModel(resourceSet.getResources().get(0));
        model.setConcurrent(true);
        
        // Console output of the MODEL being validated
        if(LOGGER.isLoggable(Level.FINEST)) {
        	String log = "\nModel name: '" + model.getName() + "' hashCode: " + model.hashCode() + "\nModel elements: ";
            Collection<EObject> elements = model.allContents();
            for (EObject e : elements) {
                log = log.concat("\nhashCode: " + e.hashCode() + " object: " + e.toString());
            }
            LOGGER.finest(log);
        }

        // -------- INITIALISE MODULE FOR VALIDATION --------

        // The validation process always uses a new Module for validation
        // There are two ways to setup a module, with or without an ExecutionCache
        if(constraintExecutionCache.isPresent()){
            module = new IncrementalEvlModule(constraintExecutionCache);
        }
        else {
            module = new IncrementalEvlModule();
        }

        // Load CONSTRAINTS to be checked
        module.parse(validator.getConstraintsURI());

        // Load MODEL to be checked
        module.getContext().getModelRepository().addModel(model);

        // Do any other necessary setup
        if (contextSetup.isPresent()) {
            contextSetup.get().accept(module.getContext());
        }

        // Console output of constraints to be executed
        if(LOGGER.isLoggable(Level.FINEST)) {
        	String log = "\nConstraints to Execute: ";
            List<Constraint> constraintsToExecute = module.getConstraints();
            int i = 0;
            for (Constraint c : constraintsToExecute) {
                i++;
                log = log.concat("\n" + i + ", " + c.getName() + " " + c.hashCode());
            }
            LOGGER.finest(log);
        }

        // -------- EXECUTE THE VALIDATION --------
        LOGGER.finer("\n [!] ...Executing validation...\n");
        module.execute();

        // -------- INITIALISE THE EXECUTION CACHE --------

        // Extract the EVLtrace containing the traceModel and pass to a new ExecutionCache to manage notification updates.
        LOGGER.finer("\n [i] Adapter constraintExecutionCache created");
        constraintExecutionCache = Optional.of(new ConstraintExecutionCache(module.getEvlTrace()));

        // Console output
        LOGGER.finest(() -> modelStateToString());
    }

    @Override
    public void notifyChanged(Notification notification) {
        super.notifyChanged(notification);
        notifications.add(notification);  // Can be removed, won't need a list of update notifications, pass them as they occur to the Trace
        
        // TODO: handle Resources that are notifiers (needs test with Type.all)
        if (notification.getNotifier() instanceof EObject) {            
            EObject modelElement = (EObject) notification.getNotifier();
            EStructuralFeature modelFeature = (EStructuralFeature) notification.getFeature();
            
            if(LOGGER.isLoggable(Level.INFO)) {
            	String notificationText = ("\n[MODEL CHANGE NOTIFICATION]  " 
            			+ " Type:" + notification.getEventType()
                        + "\n " + notification);
                        
                if(notification.getEventType() != Notification.REMOVING_ADAPTER){
                    // Type 8 removes the adapter and produces NULL conditions
                    notificationText = notificationText.concat(
                        "\n element: " + modelElement.hashCode() + " " + EcoreUtil.getURI(modelElement)
                        + "\n feature: " + modelFeature.getName()
                        + "\n was: " + notification.getOldValue()
                        + "\n now: " + notification.getNewValue()
                        + "\n");
                }
                LOGGER.info(notificationText);
            }

            // IF there is an constraintExecutionCache, then we need update ConstraintTrace and UnsatisfiedConstraints lists
            if(constraintExecutionCache.isPresent()){
                constraintExecutionCache.get().processModelNotification(notification);
                if (LOGGER.isLoggable(Level.FINEST)) {
                	LOGGER.finest(constraintExecutionCache.get().toString());
                }
            }
        }
    }

    public IncrementalEvlValidator getValidator() {
        return validator;
    }

    public boolean mustRevalidate(ResourceSet resourceSet) {
        return !notifications.isEmpty();
    }

    // TODO move to utility class for debugging?
    public String modelStateToString () {
    	int i = 0;
    	
    	String stateString = "\n == Module results ==";    	
    	stateString = stateString.concat("\nUnsatisfiedConstraints: " 
    	+ module.getContext().getUnsatisfiedConstraints().size()) ;

        i = 0;
        stateString = stateString.concat("\nConstraintTrace list: ");
        for (ConstraintTraceItem item : module.getContext().getConstraintTrace()) {
            i++;
            stateString = stateString.concat("\n " + i 
            		+ ", Constraint: " + item.getConstraint().getName() + " " + item.getConstraint().hashCode()
                    + " | Model hashcode: " + item.getInstance().hashCode()
                    + " | Result : " + item.getResult()
            );
        }

        i = 0;
        stateString = stateString.concat("\nUnsatisfiedConstraint list: ");
        for (UnsatisfiedConstraint uc : module.getContext().getUnsatisfiedConstraints()) {
            i++;
            stateString = stateString.concat("\n " + i 
            		+ ", Constraint: " + uc.getConstraint().getName() + " " + uc.getConstraint().hashCode() 
            		+ " | Model hashcode: " + uc.getInstance().hashCode()
            );
        }

        stateString = stateString.concat("\nExecutions list: ");
        
        stateString = stateString.concat(module.evlTrace.toString());
        
        stateString = stateString.concat("\n ====================\n");
        
        return stateString;
    }

    // TODO move to utility class for debugging?
    public String getStringOfNotifications() {
    	String stringOfNotifications = "\nAdapter has recieved " + notifications.size() + " notification(s): ";
    	int i = 0;
    	for(Notification notification : notifications) {
    		i++;
    		stringOfNotifications = stringOfNotifications.concat("\n" + i + ", " + notification.toString());
    	}   	    	
    	return stringOfNotifications;
    }

}
