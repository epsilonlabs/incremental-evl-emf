package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;

import org.eclipse.emf.common.notify.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.stream.Collectors;

public class IncrementalEvlTrace {

    private boolean REPORT = false;
    protected List<ConstraintPropertyAccess> propertyAccesses = new ArrayList<>();

    public void addPropertyAccess(ConstraintPropertyAccess propertyAccess) {
        propertyAccesses.add(propertyAccess);
        if(REPORT){System.out.println("   propertyAccess recorded: mE " + propertyAccess.getModelElement().hashCode() + " - mF " + propertyAccess.getPropertyName() + " - c " + propertyAccess.getExecution().getConstraint().getName());}
    }

    protected Set<ConstraintExecution> propertyModified(Object modelElement, String propertyName) {

        Set<ConstraintExecution> invalidatedExecutions = propertyAccesses.stream().
                filter(propertyAccess -> propertyAccess.getModelElement() == modelElement && propertyName.equals(propertyAccess.getPropertyName())).
                map(propertyAccess -> propertyAccess.getExecution()).collect(Collectors.toSet());

        propertyAccesses.removeIf(propertyAccess -> invalidatedExecutions.contains(propertyAccess.getExecution()));

        return invalidatedExecutions;
    }

    protected List<ConstraintExecution> elementAdded(Object modelElement) {
        return null;
    }

    protected List<ConstraintExecution> elementDeleted(Object modelElement) {
        return null;
    }

    //protected Set<UnsatisfiedConstraint> unsatisfiedConstraints = null;
    /*
    public List<ConstraintPropertyAccess> getPropertyAccesses() {
        return propertyAccesses;
    }

    public void setUnsatisfiedConstraints(Set<UnsatisfiedConstraint> listOfUnsatisifedConstraints) {
        unsatisfiedConstraints = listOfUnsatisifedConstraints;
    }
    */

    /*  -- Document and DELETE --
    // Move this to "Execution Cache"
    public boolean checkPropertyAccesses(Object modelElement, Constraint constraint) {
        for (ConstraintPropertyAccess propertyAccess : propertyAccesses) {
            if ((modelElement.hashCode() == propertyAccess.getModelElement().hashCode())
                    && (constraint.hashCode() == propertyAccess.getExecution().getConstraint().hashCode())) {
                //System.out.println("\n trace MATCHED Model HASH " + modelElement.hashCode() + "\n && Const hash: " + constraint.hashCode() + " == " + propertyAccess.getExecution().getConstraint().hashCode());
                return true;
            }
        }
        return false;
    }

    // Move this to "Execution Cache"
    public UnsatisfiedConstraint checkUnsatisfiedContraint(Object modelElement, Constraint constraint) {
        for (UnsatisfiedConstraint result : unsatisfiedConstraints) {
            if ((modelElement.hashCode() == result.getInstance().hashCode())
                    && (constraint.hashCode() == result.getConstraint().hashCode())) {
                //System.out.println("Found old result: " + result);
                return result;
            }
        }
        return null;
    }

    // Move this to "Execution Cache"? -- turn this into a remove "property access object"?
    public void processModelNotification(Notification notification) {

        // Do we also need to remove model elements from UnsatisfiedConstraints?
        // -- UCs are "new" to each Trace, which becomes the "lastTrace". UCs are only returned if a model element is on the trace.

        EObject modelElement = (EObject) notification.getNotifier();
        EStructuralFeature feature = (EStructuralFeature) notification.getFeature();

        System.out.println("BEFORE - PA list size:" + propertyAccesses.size());

        ListIterator<ConstraintPropertyAccess> iterator = propertyAccesses.listIterator();
        while (iterator.hasNext()) {
            ConstraintPropertyAccess current = iterator.next();
            if ((EcoreUtil.getURI((EObject) current.getModelElement()).equals(EcoreUtil.getURI(modelElement)))
                    &&
                    (current.getPropertyName().equals(feature.getName()))) {
                System.out.println("Removed from palist: " + current);

                // 1. Remove the old result from the module's getContext().getConstraintTrace()
                // 2. Remove the old result from the module's getContext().getUnsatisfiedConstraints()
                // 3. Forget all property accesses from current.getExecution() (not just this one)

                iterator.remove();
            }
        }
        System.out.println("AFTER - PA list size:" + propertyAccesses.size());
    }
    */

}
