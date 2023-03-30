package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccess;
import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccessRecorder;

public class ConstraintPropertyAccessRecorder extends PropertyAccessRecorder {
	
	protected ConstraintExecution execution = null;
	
	@Override
	protected PropertyAccess createPropertyAccess(Object modelElement, String propertyName) {
		return new ConstraintPropertyAccess(modelElement, propertyName, execution);
	}
	
	public void setExecution(ConstraintExecution execution) {
		this.execution = execution;
	}
	
	public ConstraintExecution getExecution() {
		return execution;
	}
	
}
