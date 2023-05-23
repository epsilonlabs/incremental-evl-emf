package org.eclipse.epsilon.evl.emf.validation.incremental;


import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccessRecorder;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.ConstraintExecution;

public class ConstraintPropertyAccessRecorder extends PropertyAccessRecorder {
	
	//
	// It may be possible to remove the constraint property access recorder and use a the original property access recorder.
	//
	
	protected ConstraintExecution execution;
	
	@Override
	protected  ConstraintPropertyAccess createPropertyAccess(Object modelElement, String propertyName) {				
		return new ConstraintPropertyAccess(modelElement, propertyName, execution);
	}
	
	public void setExecution(ConstraintExecution tExecution) {
		this.execution = tExecution;			
	}
	
	public ConstraintExecution getExecution() {
		return execution;
	}
	
}
