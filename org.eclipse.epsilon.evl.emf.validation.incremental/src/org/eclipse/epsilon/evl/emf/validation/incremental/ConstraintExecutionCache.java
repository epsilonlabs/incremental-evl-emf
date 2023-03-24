package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.epsilon.evl.trace.ConstraintTraceItem;

import org.eclipse.emf.common.notify.Notification;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ConstraintExecutionCache {
    // The Module.getContext() grants access to contraintTrace and UnsatisfiedConstraint, but you can't delete items from them; so we make our own lists in here to delete items from
    private boolean REPORT = false;
    protected Set<ConstraintTraceItem> constraintTraceItems;
    protected Collection<UnsatisfiedConstraint> unsatisfiedConstraints;
    protected List <ConstraintPropertyAccess> constraintPropertyAccess;

    public ConstraintExecutionCache(IncrementalEvlModule lastModule) {
        // Change these to "defencive copies"?

        this.constraintTraceItems = lastModule.getContext().getConstraintTrace().getItems();
        this.unsatisfiedConstraints = lastModule.getContext().getUnsatisfiedConstraints();
        //this.constraintPropertyAccess = lastModule.getTrace().getConstraintPropertyAccess();
        this.constraintPropertyAccess = lastModule.trace.propertyAccesses;

        if (REPORT){
            System.out.println("Setting up Execution Cache");
            printExecutionCache();
        }

    }

    public List <ConstraintPropertyAccess> getConstraintsPropertyAccessFor (EObject modelElement, EStructuralFeature modelFeature) {
        // Given a model Element and Feature, find all constraints as constraint property access for the model element & feature
        List <ConstraintPropertyAccess> matchedcpa = null;
        for (ConstraintPropertyAccess cpa : constraintPropertyAccess) {
            if(cpa.getModelElement() == modelElement && cpa.getPropertyName().equals(modelFeature.getName())) {
                matchedcpa.add(cpa);
            }
        }
        return matchedcpa;
    }

    public ConstraintTraceItem checkCachedConstraintTrace (Object model, Constraint constraint) {
        for (ConstraintTraceItem item : constraintTraceItems) {
            System.out.println("  Model: " + item.getInstance().hashCode() + " == " + model.hashCode()
                    + " && Constraint: " + item.getConstraint().getName() + " == " + constraint.getName());
            if (item.getInstance().equals(model) && item.getConstraint().equals(constraint)) {
                System.out.println("- MATCHED model & constraint - " + model.hashCode() + " " + constraint.getName());
                return item;
            }
        }
        return null;
    }

    public UnsatisfiedConstraint getCachedUnsatisfiedConstraint (Object model, Constraint constraint) {

        for (UnsatisfiedConstraint unsatisfiedConstraint : unsatisfiedConstraints) {
            System.out.println("    Model: " + unsatisfiedConstraint.getInstance().hashCode() + " == " + model.hashCode()
                    + " && Constraint: " + unsatisfiedConstraint.getConstraint().getName() + " == " + constraint.getName());
            if ( unsatisfiedConstraint.getInstance().equals(model) && unsatisfiedConstraint.getConstraint().equals(constraint)  ) {
                System.out.println (" - MATCHED UC -  UC Result: " + unsatisfiedConstraint.getConstraint().getName());
                return unsatisfiedConstraint;
            }
        }
        return null; // This should not happen if there is an entry on the trace.
    }

    public void processModelNotification (Notification notification ) {
        // IF a model element changes we need to remove all the cached results.
        int notificationType= notification.getEventType();
        EObject modelElement = (EObject) notification.getNotifier();
        EStructuralFeature modelFeature = (EStructuralFeature)notification.getFeature();

        switch (notificationType) {
            case Notification.UNSET:
            case Notification.SET: // SET
                System.out.println(" [i] ConstraintExecutionCache processModelNotification() -- "
                        + "notificationType: SET "
                        + modelElement.hashCode() + " & " + modelFeature.getName() );
                removeFromCache(modelElement, modelFeature);
                break;
            case Notification.ADD: // ADD
                // newValue is the model Element being added
                System.out.println("\n [n] Notification: ADD");
                removeFromCache(modelElement, modelFeature);
                if (notification.getNewValue() instanceof EObject) {
                    removeFromCache((EObject) notification.getNewValue());
                }
                break;
            case Notification.REMOVE: // REMOVE -- the "wasValue" is the model element being removed.
                System.out.println(" [i] ConstraintExecutionCache processModelNotification() -- "
                        + "notificationType: REMOVE "
                        + modelElement.hashCode()
                        + "\nclass: " + modelElement.getClass());
                // The notification is for the eClassifiers feature and the old value is the model element being removed.
                // Removing here is better than on adapter remove, as we don't know what other adapters might be removed

                removeFromCache(modelElement, modelFeature);
                if (notification.getOldValue() instanceof EObject) {
                    removeFromCache((EObject) notification.getOldValue());
                }

                break;
            case Notification.ADD_MANY: // ADD_MANY
                // newValue contains an Array list of modelElements being added
                for (EObject item : (Collection<EObject>) notification.getNewValue()) {
                    removeFromCache(item);
                }
                break;
            case Notification.REMOVE_MANY: // REMOVE_MANY
                for (EObject item : (Collection<EObject>) notification.getOldValue()) {
                    removeFromCache(item);
                }
                break;
            case Notification.MOVE: // MOVE

                // When a MOVE occurs, two model elements swap places, remove both from the execution cache
                EPackage ePackage = (EPackage) modelElement;
                System.out.println(notification.getNewValue().hashCode());
                System.out.println("now @: "+notification.getPosition()
                        + " " + ePackage.getEClassifiers().get(notification.getPosition()).getName()
                        + " " + ePackage.getEClassifiers().get(notification.getPosition()).hashCode()
                );
                System.out.println("was @: " + notification.getOldValue()
                        + " " + ePackage.getEClassifiers().get((int)notification.getOldValue()).getName()
                        + " " + ePackage.getEClassifiers().get((int)notification.getOldValue()).hashCode()
                );

                EModelElement modelElement1 = ePackage.getEClassifiers().get(notification.getPosition());
                EModelElement modelElement2 = ePackage.getEClassifiers().get((int)notification.getOldValue());
                removeFromCache(modelElement1);
                removeFromCache(modelElement2);

                break;
            case 8: // REMOVEING_ADAPTER -- does this equate to removing a model element from validation?

                // nada

                /*
                System.out.println(" [i] ConstraintExecutionCache processModelNotification() -- "
                        + "notificationType: REMOVEING_ADAPTER "
                        + modelElement.hashCode() );
                removeFromCache(modelElement);
                */


                break;
            case 9: //n RESOLVE
                System.out.println("\n [n] Notification: RESOLVE");
                break;
            case 10: // EVENT_TYPE_COUNT
                System.out.println("\n [n] Notification: EVENT_TYPE_COUNT");
                break;
            case -1: // NO_FEATURE_ID or NO_INDEX
                System.out.println("\n [n] Notification: NO_FEATURE_ID");
                break;
            default: // Unexpected type
                break;

        }
    }

    private void removeFromCache(EObject modelElement, EStructuralFeature modelFeature ) {
        Iterator itr = null;
        List <ConstraintPropertyAccess> constraintsToInvalidate = new ArrayList<>(); // List of constraintpropertyaccesses for model/feature to be invalidated

        // find any properyAccesses (make a list of contraints) and delete them
        itr = constraintPropertyAccess.iterator();
        while (itr.hasNext()){
            ConstraintPropertyAccess cpa = (ConstraintPropertyAccess) itr.next();
            if( ( cpa.getModelElement() == modelElement )
                    && cpa.getPropertyName().equals(modelFeature.getName()) ){
                System.out.println("  - Notification: ConstraintPropertyAccess Removed "
                        + cpa.getModelElement().hashCode() + " == " + modelElement.hashCode()
                        + " && "
                        + cpa.getPropertyName() + " == " + modelFeature.getName()
                        + " (constraint: " + cpa.getExecution().getConstraint().getName() + ")");
                constraintsToInvalidate.add(cpa);
                itr.remove();
            }
        }

        // Using the list of constraintsTobe invalidated remove constraint trace/unsatisfied based on model & constraint matches
        System.out.println("\nClear Constraint Trace/Unsatisfied where Model & Constraint match removed constraintPropertyAccesses");
        for(ConstraintPropertyAccess invalidcpa : constraintsToInvalidate) {
            // find any constraintTraceItems and delete them
            itr = constraintTraceItems.iterator();
            while (itr.hasNext()) {
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
            itr = unsatisfiedConstraints.iterator();
            while (itr.hasNext()) {
                UnsatisfiedConstraint uc = (UnsatisfiedConstraint) itr.next();
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

    private void removeFromCache(EObject modelElement) {
        System.out.println("\nremoveFromCache: " + modelElement.hashCode() + " " + modelElement);

        Iterator itr = null;
        List <ConstraintPropertyAccess> constraintsToInvalidate = new ArrayList<>(); // List of constraintpropertyaccesses for model/feature to be invalidated

        // find any properyAccesses (make a list of contraints) and delete them
        itr = constraintPropertyAccess.iterator();
        while (itr.hasNext()){
            ConstraintPropertyAccess cpa = (ConstraintPropertyAccess) itr.next();
            if( cpa.getModelElement() == modelElement ) {
                System.out.println("  - Notification: ConstraintPropertyAccess Removed "
                        + cpa.getModelElement().hashCode() + " == " + modelElement.hashCode()
                        + " (constraint: " + cpa.getExecution().getConstraint().getName() + ")");
                constraintsToInvalidate.add(cpa);
                itr.remove();
            }
        }

        // Using the list of constraintsTobe invalidated remove constraint trace/unsatisfied based on model & constraint matches
        System.out.println("\nClear Constraint Trace/Unsatisfied where Model & Constraint match removed constraintPropertyAccesses");
        for(ConstraintPropertyAccess invalidcpa : constraintsToInvalidate) {
            // find any constraintTraceItems and delete them
            itr = constraintTraceItems.iterator();
            while (itr.hasNext()) {
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
            itr = unsatisfiedConstraints.iterator();
            while (itr.hasNext()) {
                UnsatisfiedConstraint uc = (UnsatisfiedConstraint) itr.next();
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


    public void printExecutionCache () {
        // Dump the lists to the Console for inspection
        System.out.println(" == Execution Cache state ==");
        System.out.println("ContraintTraceItems: " + constraintTraceItems.size());
        System.out.println("UnsatisfiedConstraints: " + unsatisfiedConstraints.size()) ;
        System.out.println("ConstraintPropertyAccesses: " + this.constraintPropertyAccess.size()) ;

        int i = 0;

        i = 0;
        System.out.println("\nConstraintTrace list: ");
        for (ConstraintTraceItem item : constraintTraceItems) {
            i++;
            System.out.println(i + ", Constraint: " + item.getConstraint().getName() + " " + item.getConstraint().hashCode()
                    + " | Model hashcode: " + item.getInstance().hashCode()
                    + " | Result : " + item.getResult()
            );
        }

        i = 0;
        System.out.println("\nUnsatisfiedConstraint list: ");
        for (UnsatisfiedConstraint uc : unsatisfiedConstraints) {
            i++;
            System.out.println(i + ", Constraint: " + uc.getConstraint().getName() + " " + uc.getConstraint().hashCode() +
                    " | Model hashcode: " + uc.getInstance().hashCode()
            );
        }

        i = 0;
        System.out.println("\n[Trace] (Constraint)PropertyAccess list: ");


        for (ConstraintPropertyAccess cpa : constraintPropertyAccess) {
            i++;
            System.out.print(i + ", ");

            System.out.print("Constraint: " + cpa.execution.constraint.getName());
            System.out.println(" | Model hashcode: " + cpa.getModelElement().hashCode());
        }

        System.out.println("\n ====================\n");
    }

}
