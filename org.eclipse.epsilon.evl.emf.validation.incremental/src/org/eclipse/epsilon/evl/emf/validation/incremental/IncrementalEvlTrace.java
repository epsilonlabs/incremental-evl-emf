package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IncrementalEvlTrace {
	
	protected List<ConstraintPropertyAccess> propertyAccesses = new ArrayList<>();
	
	public void addPropertyAccess(ConstraintPropertyAccess propertyAccess) {
		propertyAccesses.add(propertyAccess);
		System.out.println("\nLogging ConstraintPropertyAccess to trace: ");
		System.out.println("Constraint:" + propertyAccess.getExecution().getConstraint());
		System.out.println("Model Element:" + propertyAccess.getModelElement() +" . " + propertyAccess.getPropertyName());
		
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
