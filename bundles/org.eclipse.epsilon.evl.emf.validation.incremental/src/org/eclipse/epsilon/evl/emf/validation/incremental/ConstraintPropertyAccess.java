package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccess;

public class ConstraintPropertyAccess extends PropertyAccess {
	
	protected ConstraintExecution execution = null;
	
	public ConstraintPropertyAccess(Object modelElement, String propertyName, ConstraintExecution execution) {
		super(modelElement, propertyName);
		this.execution = execution;
	}
	
	public ConstraintExecution getExecution() {
		return execution;
	}

	@Override
	public String toString() {
		return String.format("PropertyAccess in constraint '%s' of '%s' on model element: '%s'",
			execution.getConstraint().getName(),
			this.propertyName,
			this.modelElement);
	}
	
}
