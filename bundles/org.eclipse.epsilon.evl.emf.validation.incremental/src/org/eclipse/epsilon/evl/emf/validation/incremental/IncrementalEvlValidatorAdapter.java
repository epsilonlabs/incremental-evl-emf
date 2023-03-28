package org.eclipse.epsilon.evl.emf.validation.incremental;

import static org.eclipse.epsilon.evl.emf.validation.incremental.IncrementalEcoreValidator.MYLOGGER;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    private boolean REPORT = false;
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
        MYLOGGER.log(MyLog.FLOW, "\n [!] IncrementalEvlValidatorAdapter.revalidate() called: " + resourceSet + "\n");
        validate(resourceSet);
        notifications.clear();
    }

    public void validate(ResourceSet resourceSet) throws Exception {
        validationCount++;
        System.out.println("\n ** RUNNING VALIDATION " + validationCount + " **");

        MYLOGGER.log(MyLog.FLOW, "\n [!] IncrementalEvlValidatorAdapter.validate() called\n");

        // Make an in memory version of the Model (root element) for testing
        InMemoryEmfModel model = new InMemoryEmfModel(resourceSet.getResources().get(0));
        model.setConcurrent(true);

        MYLOGGER.log(MyLog.STATE, "Model name : '" + model.getName() + "' hashCode: " + model.hashCode());
        // Console output
        if (REPORT) {
            System.out.println("\nModel elements :");
            Collection<EObject> elements = model.allContents();
            for (EObject e : elements) {
                System.out.println("hashCode: " + e.hashCode() + " object: " + e.toString());
            }
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
        if (REPORT) {
            System.out.println("\nConstraints to Execute : ");
            List<Constraint> constraintsToExecute = module.getConstraints();
            int i = 0;
            for (Constraint c : constraintsToExecute) {
                i++;
                System.out.println(i + ", " + c.getName() + " " + c.hashCode());
            }
        }

        MYLOGGER.log(MyLog.FLOW, "\n [!] ...Executing validation...\n");


        // -------- EXECUTION --------
        //Set<UnsatisfiedConstraint> unsatisfiedConstraints = module.execute();
        if(REPORT) {System.out.println( "\n ! EXECUTING !");}
        module.execute();

        // -------- PROCESS RESULTS --------

        // Extract from module data to populate the ExecutionCache for the next run.

        // Pass module to ExecutionCache constructor and make a NEW one to replace the existing one
        // Constructor extracts = (Constraint)PropertyAccess & ContraintTrace & UnsatisfiedConstraints
        if(REPORT) {System.out.println("\n [i] Adapter constraintExecutionCache created");}
        constraintExecutionCache = Optional.of (new ConstraintExecutionCache(module));

        // Console output
        if (REPORT) {
            printModuleState();
        }
    }

    @Override
    public void notifyChanged(Notification notification) {
        super.notifyChanged(notification);

        if (notification.getNotifier() instanceof EObject) {
            notifications.add(notification);  // Can be removed, won't need a list of update notifications, pass them as they occur to the Trace
            EObject modelElement = (EObject) notification.getNotifier();
            EStructuralFeature modelFeature = (EStructuralFeature) notification.getFeature();
            if(REPORTnotification) {

                System.out.println("\n[MODEL CHANGE NOTIFICATION]  " + " Type:" + notification.getEventType()
                        + "\n " + notification);

                if(notification.getEventType() != 8){
                    // Type 8 removes the adapter and produces NULL conditions
                        System.out.println(" element: " + modelElement.hashCode() + " " + EcoreUtil.getURI(modelElement)
                        + "\n feature: " + modelFeature.getName()
                        + "\n was: " + notification.getOldValue()
                        + "\n now: " + notification.getNewValue()
                        + "\n");
                }

            }

            // IF there is an constraintExecutionCache, then we need update ConstraintTrace and UnsatisfiedConstraints lists
            if(constraintExecutionCache.isPresent()){
                constraintExecutionCache.get().processModelNotification(notification);
                if(REPORT) {constraintExecutionCache.get().printExecutionCache();}
            }
        }
    }

    public IncrementalEvlValidator getValidator() {
        return validator;
    }

    public boolean mustRevalidate(ResourceSet resourceSet) {
        return !notifications.isEmpty();
    }

    public void printModuleState () {
        System.out.println("\n == Module results ==");
        System.out.println("UnsatisfiedConstraints: " + module.getContext().getUnsatisfiedConstraints().size()) ;

        int i = 0;
        System.out.println("\nConstrainPropertyAccess list: ");
        for (ConstraintPropertyAccess cpa : module.getTrace().propertyAccesses) {
            i++;
            System.out.print(i + ", ");

            System.out.print("Constraint: " + cpa.execution.constraint.getName() + " " + cpa.execution.constraint.hashCode());
            System.out.println(" | Model hashcode: " + cpa.getModelElement().hashCode());
        }

        i = 0;
        System.out.println("\nConstraintTrace list: ");
        for (ConstraintTraceItem item : module.getContext().getConstraintTrace()) {
            i++;
            System.out.println(i + ", Constraint: " + item.getConstraint().getName() + " " + item.getConstraint().hashCode()
                    + " | Model hashcode: " + item.getInstance().hashCode()
                    + " | Result : " + item.getResult()
            );
        }

        i = 0;
        System.out.println("\nUnsatisfiedConstraint list: ");
        for (UnsatisfiedConstraint uc : module.getContext().getUnsatisfiedConstraints()) {
            i++;
            System.out.println(i + ", Constraint: " + uc.getConstraint().getName() + " " + uc.getConstraint().hashCode() +
                    " | Model hashcode: " + uc.getInstance().hashCode()
            );
        }

        System.out.println("\n ====================\n");
    }

}
