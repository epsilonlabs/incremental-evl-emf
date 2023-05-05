package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccess;
import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccessRecorder;

public class ConstraintPropertyAccessRecorder extends PropertyAccessRecorder {
	
	protected Execution execution = null;
	
	@Override
	protected ConstraintPropertyAccess createPropertyAccess(Object modelElement, String propertyName) {	
		// When creating a PropertyAccess add it to the propertyAccesses list in the execution	
		execution.addAccess(new Access(new PropertyAccess(modelElement, propertyName)));
		
		// this return will be removed
		return new ConstraintPropertyAccess(modelElement, propertyName, execution);
	}
	
	public void setExecution(Execution execution) {
		this.execution = execution;
		System.out.println("  ConstraintPropertyAccessRecorder.setExecution() : " + execution);			
	}
	
	public Execution getExecution() {
		return execution;
	}
	
}
