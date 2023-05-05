package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.common.module.ModuleElement;

public class Execution {
		
	protected ModuleElement unit;
	protected Object self;
	
	// context: EJavaObject ?

	protected final List<Execution> dependencies = new ArrayList<>();
	
	// TODO property accesses to go here	
	protected List<Access> accesses = new ArrayList<>();
	
	public void addAccess (Access access) {
		this.accesses.add(access);
		System.out.println("   Execution.addAccess() " + accesses.size() + " Execution:" + unit + " accesses:"  + accesses);
	}
	
	public List<Access> getAccesses () {
		return accesses;
	}
	
	
	public Execution(ModuleElement constraint, Object self) {
		this.unit = constraint;
		this.self = self;
	}

	public Object getSelf() {
		return self;
	}
	
	public void setSelf(Object self) {
		this.self = self;
	}

	public ModuleElement getUnit() {
		return unit;
	}

	public void setUnit(ModuleElement unit) {
		this.unit = unit;
	}

	public List<Execution> getDependencies() {
		return dependencies;
	}

	@Override
	public String toString() {
		return "ConstraintExecution [constraint=" + unit + ", self=" + self + "]";
	}
	
}
