package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccess;
import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccessRecorder;
import org.eclipse.epsilon.evl.dom.Constraint;

public class ConstraintPropertyAccessRecorder extends PropertyAccessRecorder {
	
	protected Constraint constraint;
	protected Object self;
	
	@Override
	protected PropertyAccess createPropertyAccess(Object modelElement, String propertyName) {
		return new ConstraintPropertyAccess(modelElement, propertyName, constraint, self);
	}
	
	public Object getSelf() {
		return self;
	}
	
	public void setSelf(Object self) {
		this.self = self;
	}
	
	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}
	
	public Constraint getConstraint() {
		return constraint;
	}
	
}
