package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccess;
import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccessRecorder;

public class ConstraintPropertyAccessRecorder extends PropertyAccessRecorder {
	
	protected Execution execution = null;
	
	@Override
	protected PropertyAccess createPropertyAccess(Object modelElement, String propertyName) {
		return new ConstraintPropertyAccess(modelElement, propertyName, execution);
	}
	
	public void setExecution(Execution execution) {
		this.execution = execution;
	}
	
	public Execution getExecution() {
		return execution;
	}
	
}
