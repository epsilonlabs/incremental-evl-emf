package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.epsilon.evl.trace.ConstraintTraceItem;

import javax.management.Notification;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ConstraintExecutionCache {
    // The Module.getContext() grants access to contraintTrace and UnsatisfiedConstraint, but you can't delete items from them; so we make our own lists in here to delete items from
    protected Set<ConstraintTraceItem> constraintTraceItems;
    protected Collection<UnsatisfiedConstraint> unsatisfiedConstraints;
    protected List <ConstraintPropertyAccess> constraintPropertyAccess;

    public ConstraintExecutionCache(IncrementalEvlModule lastModule) {
        // Change these to "defencive copies"?

        this.constraintTraceItems = lastModule.getContext().getConstraintTrace().getItems();
        this.unsatisfiedConstraints = lastModule.getContext().getUnsatisfiedConstraints();
        //this.constraintPropertyAccess = lastModule.getTrace().getConstraintPropertyAccess();
        this.constraintPropertyAccess = lastModule.trace.propertyAccesses;
        System.out.println("Setting up Execution Cache");
        printExecutionCache();

    }

    public ConstraintTraceItem checkCachedConstraintTrace (Object model, Constraint constraint) {
        for (ConstraintTraceItem item : constraintTraceItems) {
            System.out.println("  Model: " + item.getInstance().hashCode() + " == " + model.hashCode() + " && Constraint: " + item.getConstraint().getName() + " == " + constraint.getName());
            if (item.getInstance().equals(model) && item.getConstraint().equals(constraint)) {
                System.out.println("- MATCHED model & constraint - " + model.hashCode() + " " + constraint.getName());
                return item;
            }
        }
        return null;
    }

    public UnsatisfiedConstraint getCachedUnsatisfiedConstraint (Object model, Constraint constraint) {

        for (UnsatisfiedConstraint unsatisfiedConstraint : unsatisfiedConstraints) {
            System.out.println("    Model: " + unsatisfiedConstraint.getInstance().hashCode() + " == " + model.hashCode() + " && Constraint: " + unsatisfiedConstraint.getConstraint().getName() + " == " + constraint.getName());
            if ( unsatisfiedConstraint.getInstance().equals(model) && unsatisfiedConstraint.getConstraint().equals(constraint)  ) {
                System.out.println (" - MATCHED UC -  UC Result: " + unsatisfiedConstraint.getConstraint().getName());
                return unsatisfiedConstraint;
            }
        }
        return null; // This should not happen if there is an entry on the trace.
    }

    public void processModelNotification (Notification notification) {
        // IF a model element changes we need to remove all the cached results.

        // find any properyAccesses and delete them

        // find any constraintTraceItems and delete them

        // find any unstatisfiedConstraints and delete them


    }


    public void printExecutionCache () {
        // Dump the lists to the Console for inspection
        System.out.println("\n == Execution Cache state ==");
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
