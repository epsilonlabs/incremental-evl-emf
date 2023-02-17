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

    protected List<ConstraintPropertyAccess> propertyAccesses = new ArrayList<>();
    protected Set<UnsatisfiedConstraint> unsatisfiedConstraints = null;

    public void addPropertyAccess(ConstraintPropertyAccess propertyAccess) {
        propertyAccesses.add(propertyAccess);
    }

    public List<ConstraintPropertyAccess> getPropertyAccesses() {
        return propertyAccesses;
    }

    public void setUnsatisfiedConstraints(Set<UnsatisfiedConstraint> listOfUnsatisifedConstraints) {
        unsatisfiedConstraints = listOfUnsatisifedConstraints;
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

    public void processModelNotification(Notification notification) {
        EObject modelElement = (EObject) notification.getNotifier();
        EStructuralFeature feature = (EStructuralFeature) notification.getFeature();

        System.out.println("\nNOTIFICATION from : " + EcoreUtil.getURI(modelElement) + " feature: " + feature.getName());

        System.out.println("BEFORE - PA list size:" + propertyAccesses.size());

        ListIterator<ConstraintPropertyAccess> iterator = propertyAccesses.listIterator();
        while (iterator.hasNext()) {
            ConstraintPropertyAccess current = iterator.next();
            if ((EcoreUtil.getURI((EObject) current.getModelElement()).equals(EcoreUtil.getURI(modelElement)))
                    &&
                    (current.getPropertyName().equals(feature.getName()))) {
                System.out.println("Removed from palist: " + current);
                iterator.remove();
            }
        }
        System.out.println("AFTER - PA list size:" + propertyAccesses.size());
    }
}
