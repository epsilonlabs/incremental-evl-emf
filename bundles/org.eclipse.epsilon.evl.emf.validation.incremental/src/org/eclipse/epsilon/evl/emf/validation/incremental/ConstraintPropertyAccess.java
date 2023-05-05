package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccess;

public class ConstraintPropertyAccess extends PropertyAccess {
//public class ConstraintPropertyAccess {	
	protected Execution execution = null;
	
	public ConstraintPropertyAccess(Object modelElement, String propertyName, Execution execution) {
		super(modelElement, propertyName);		
		this.execution = execution;	
		execution.addAccess(new Access(new PropertyAccess(modelElement,propertyName) ));	
		
	}
	
	public Execution getExecution() {
		return execution;
	}
		
	
	public Object getModelElement() {		
		//return execution.getAccesses().get(0).getModelElement();
		return super.modelElement;
	}
	
	
	public String getPropertyName() {
		//return execution.getAccesses().get(0).getPropertyName();
		return super.propertyName;
		
	}

	
	public String toString() {
		/*
		return String.format("PropertyAccess in constraint '%s' of '%s' on model element: '%s'",
				execution.getUnit(),
				execution.getAccesses().get(0).getPropertyName(),
				execution.getAccesses().get(0).getModelElement()
				);
		*/
		return String.format("PropertyAccess in constraint '%s' of '%s' on model element: '%s'",
			execution.getUnit(),
			this.propertyName,
			this.modelElement
			);
	}
	
}
