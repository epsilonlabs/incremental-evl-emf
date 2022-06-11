package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccess;
import org.eclipse.epsilon.evl.dom.Constraint;

public class ConstraintPropertyAccess extends PropertyAccess {
	
	protected Constraint constraint;
	protected Object self;
	
	public ConstraintPropertyAccess(Object modelElement, String propertyName, Constraint constraint, Object element) {
		super(modelElement, propertyName);
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
