package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IncrementalEvlTrace {

    protected List<ConstraintPropertyAccess> propertyAccesses = new ArrayList<>();

    public void addPropertyAccess(ConstraintPropertyAccess propertyAccess) {
        propertyAccesses.add(propertyAccess);
    }

    protected Set<Execution> propertyModified(Object modelElement, String propertyName) {

        Set<Execution> invalidatedExecutions = propertyAccesses.stream().
                filter(propertyAccess -> propertyAccess.getModelElement() == modelElement && propertyName.equals(propertyAccess.getPropertyName())).
                map(propertyAccess -> propertyAccess.getExecution()).collect(Collectors.toSet());

        propertyAccesses.removeIf(propertyAccess -> invalidatedExecutions.contains(propertyAccess.getExecution()));

        return invalidatedExecutions;
    }

    protected List<Execution> elementAdded(Object modelElement) {
        return null;
    }

    protected List<Execution> elementDeleted(Object modelElement) {
        return null;
    }
}
