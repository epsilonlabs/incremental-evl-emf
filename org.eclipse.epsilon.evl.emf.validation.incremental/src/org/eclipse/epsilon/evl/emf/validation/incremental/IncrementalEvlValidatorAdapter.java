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
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;


public class IncrementalEvlValidatorAdapter extends EContentAdapter {

    private boolean validationHasRun = false; //Track that we have run one Validation
    private IncrementalEvlTrace lastTrace = null;
    private Set<UnsatisfiedConstraint> unsatisfiedConstraints;

    protected IncrementalEvlValidator validator = null;
    protected List<Notification> notifications = new ArrayList<>();

    public IncrementalEvlValidatorAdapter(IncrementalEvlValidator validator) {
        this.validator = validator;
    }

    public void revalidate(ResourceSet resourceSet) throws Exception {
        System.out.println("\n [!] IncrementalEvlValidatorAdapter.revalidate() called: " + resourceSet + "\n");


        validate(resourceSet);

        notifications.clear();

    }

    public void validate(ResourceSet resourceSet) throws Exception {
        System.out.print("\n [!] IncrementalEvlValidatorAdapter.validate() called\n");

        // Model (root element)
        InMemoryEmfModel model = new InMemoryEmfModel(resourceSet.getResources().get(0));
        model.setConcurrent(true);
        System.out.println("Model name : '" + model.getName() + "' hashCode: " + model.hashCode());

        // All Model elements
        System.out.println("Model elements :");
        Collection<EObject> elements = model.allContents();
        for (EObject e : elements) {
            System.out.println("hashCode: " + e.hashCode() + " object: " + e.toString());
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


        System.out.println("\nConstraints to Execute : ");
        List<Constraint> constraintsToExecute = module.getConstraints();
        int i = 0;
        for (Constraint c : constraintsToExecute) {
            i++;
            System.out.println(i + ", " + c.getName());
        }


        //Set<UnsatisfiedConstraint> unsatisfiedConstraints = module.execute();
        System.out.println("\n [!] ...Executing validation...\n");
        unsatisfiedConstraints = module.execute();

        validationHasRun = true; // Confirm at least one validation hasRun
        lastTrace = module.getTrace();
        System.out.println("\n [!] Review trace (constraintPropertyAccess Objects) in EvlModule:\n" + lastTrace.propertyAccesses.toString());
        if (null != lastTrace) {
            i = 0;
            for (ConstraintPropertyAccess cpa : lastTrace.propertyAccesses) {
                i++;
                System.out.println("\nConstrain Property Access: " + i);

                System.out.println("CPA name: " + cpa.execution.constraint.getName());        // constraint name?
                cpa.getExecution().getConstraint().getName();    // constraint name?
                EObject elementAsEobject = (EObject) cpa.getModelElement();
                System.out.println("element hashCode: " + elementAsEobject.hashCode());    //
                System.out.println("property name: " + cpa.getPropertyName());    //
                cpa.getExecution().getConstraint().getMessageBlock();

            }
        }
    }

    public IncrementalEvlTrace getLastTrace() {
        //System.out.println("\ngetLastTrace(): " + lastTrace.propertyAccesses.toString());
        return lastTrace;
    }


    @Override
    public void notifyChanged(Notification notification) {
        super.notifyChanged(notification);
        notifications.add(notification);

        EStructuralFeature feature = (EStructuralFeature) notification.getFeature(); // unpack the feature from the notification

        System.out.println("\n [!] notifyChanged(Notification notification) : " + notification.getFeature().hashCode() + " " + notification.getOldValue() + " to " + notification.getNewValue() + "\n");
        System.out.println("notification for feature name: " + feature.getName());
        System.out.println("");
    }

    public IncrementalEvlValidator getValidator() {
        return validator;
    }

    public boolean mustRevalidate(ResourceSet resourceSet) {
        return !notifications.isEmpty();
    }

}
