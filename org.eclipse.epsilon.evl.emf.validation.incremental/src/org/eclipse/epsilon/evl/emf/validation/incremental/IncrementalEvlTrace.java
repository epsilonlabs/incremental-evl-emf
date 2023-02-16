package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.eclipse.epsilon.evl.emf.validation.incremental.IncrementalEcoreValidator.MYLOGGER;

public class IncrementalEvlTrace {

    protected List<ConstraintPropertyAccess> propertyAccesses = new ArrayList<>();
    protected Set<UnsatisfiedConstraint> unsatisfiedConstraints = null;

    public void addPropertyAccess(ConstraintPropertyAccess propertyAccess) {
        propertyAccesses.add(propertyAccess);
    }

    public void setUnsatisfiedConstraints (Set<UnsatisfiedConstraint> listOfUnsatisifedConstraints) {
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

}
