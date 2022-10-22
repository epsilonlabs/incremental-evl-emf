package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.epsilon.evl.dom.Constraint;

public class ConstraintExecution {
	
	protected Constraint constraint;
	protected Object self;
	
	public ConstraintExecution(Constraint constraint, Object self) {
		super();
		this.constraint = constraint;
		this.self = self;
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
