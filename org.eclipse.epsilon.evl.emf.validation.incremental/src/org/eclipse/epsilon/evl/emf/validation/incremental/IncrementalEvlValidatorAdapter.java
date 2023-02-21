package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;

import static org.eclipse.epsilon.evl.emf.validation.incremental.IncrementalEcoreValidator.MYLOGGER;


public class IncrementalEvlValidatorAdapter extends EContentAdapter {
    private static boolean REPORT = true;

    private boolean validationHasRun = false; //Track that we have run one Validation
    private IncrementalEvlTrace lastTrace = null;
    private Set<UnsatisfiedConstraint> unsatisfiedConstraints;

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
        MYLOGGER.log(MyLog.FLOW, "\n [!] IncrementalEvlValidatorAdapter.validate() called\n");

        // Model (root element)
        InMemoryEmfModel model = new InMemoryEmfModel(resourceSet.getResources().get(0));
        model.setConcurrent(true);
        MYLOGGER.log(MyLog.STATE, "Model name : '" + model.getName() + "' hashCode: " + model.hashCode());

        // All Model elements
        if (REPORT) {
            System.out.println("\nModel elements :");
            Collection<EObject> elements = model.allContents();
            for (EObject e : elements) {
                System.out.println("hashCode: " + e.hashCode() + " object: " + e.toString());
            }
        }


        // Module for doing evaluations
        // Two ways to setup a module, with and without prior knowledge of the last validation
        IncrementalEvlModule module = null;
        if (validationHasRun) {
            module = new IncrementalEvlModule(notifications, unsatisfiedConstraints, lastTrace);
        } else {
            module = new IncrementalEvlModule();
        }
        module.parse(validator.getConstraints());    // constraints ArrayList<E>[]
        module.getContext().getModelRepository().addModel(model);

        // Console output
        if (REPORT) {
            System.out.println("\nConstraints to Execute : ");
            List<Constraint> constraintsToExecute = module.getConstraints();
            int i = 0;
            for (Constraint c : constraintsToExecute) {
                i++;
                System.out.println(i + ", " + c.getName());
            }
        }


        //Set<UnsatisfiedConstraint> unsatisfiedConstraints = module.execute();

        MYLOGGER.log(MyLog.FLOW, "\n [!] ...Executing validation...\n");


        // -------- EXECUTION --------
        unsatisfiedConstraints = module.execute();

        System.out.println("Unsat: " + module.getContext().getUnsatisfiedConstraints().size()) ;

        validationHasRun = true; // Confirm at least one validation hasRun
        // -------- PROCESS RESULTS --------
        lastTrace = module.getTrace();

        // Console output
        if (REPORT) {
            System.out.println("\nModule Trace");

            int i = 0;
            System.out.println("\nConstrainPropertyAccess list: ");
            for (ConstraintPropertyAccess cpa : module.getTrace().propertyAccesses) {
                i++;
                System.out.print(i + ", ");

                System.out.print("Constraint: " + cpa.execution.constraint.getName());
                System.out.println(" | model hashcode: " + cpa.getModelElement().hashCode());
            }

            i = 0;
            System.out.println("\nUnsatisfiedConstraint list: ");
            for (UnsatisfiedConstraint uc : module.getContext().getUnsatisfiedConstraints()) {
                i++;
                System.out.println(i + ", Constraint: " + uc.getConstraint().getName() + " " + uc.getConstraint().hashCode() +
                        " | model hashcode: " + uc.getInstance().hashCode()
                );
            }
        }
    }

    @Override
    public void notifyChanged(Notification notification) {
        super.notifyChanged(notification);
        notifications.add(notification);  // Can be removed, won't need a list of update notifications, pass them as they occur to the Trace

        // IF there is a lastTrace, then we need to send the update to EVLTrace and update the ConstraintPropertyAccess list
        if (null != lastTrace) {
            lastTrace.processModelNotification(notification);
        }

        EStructuralFeature feature = (EStructuralFeature) notification.getFeature();
        MYLOGGER.log(MyLog.NOTIFICATION, "\n [!] notifyChanged(Notification notification) : " +
                notification.getFeature().hashCode() + " " +
                "\n notification for feature name: " + feature.getName() +
                notification.getOldValue() + " to " + notification.getNewValue());
    }

    public IncrementalEvlValidator getValidator() {
        return validator;
    }

    public boolean mustRevalidate(ResourceSet resourceSet) {
        return !notifications.isEmpty();
    }

}
