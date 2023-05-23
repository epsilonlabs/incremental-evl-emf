package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccess;

//import org.eclipse.epsilon.evl.emf.validation.incremental.trace.PropertyAccess;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.ConstraintExecution;

public class ConstraintPropertyAccess extends PropertyAccess {
	protected ConstraintExecution execution = null;
	
	public ConstraintPropertyAccess(Object modelElement, String propertyName, ConstraintExecution execution) {
		super(modelElement, propertyName);		
		this.execution = execution;	
	}
	
	public ConstraintExecution getExecution() {
		return execution;
	}
		
	
	public Object getModelElement() {		
		return super.modelElement;
	}
	
	
	public String getPropertyName() {
		return super.propertyName;
		
	}
	
	public String toString() {
		return String.format("PropertyAccess in constraint '%s' of '%s' on model element: '%s'",
			execution.getConstraint().getRaw(),
			this.propertyName,
			this.modelElement
			);
	}
	
}
