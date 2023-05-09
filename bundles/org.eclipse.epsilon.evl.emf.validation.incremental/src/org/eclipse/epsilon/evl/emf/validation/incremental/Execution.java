package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import org.eclipse.epsilon.common.module.ModuleElement;

public class Execution {
		
	protected ModuleElement unit;
	protected Object self;
	
	// context: EJavaObject ?

	protected final List<Execution> dependencies = new ArrayList<>();
	
	// TODO property accesses to go here	
	protected Set<Access> accesses = new HashSet<>();
	
	public void addAccess (Access access) {
		this.accesses.add(access);
		System.out.println("   Execution.addAccess() " + accesses.size() + " Execution:" + unit + " accesses:"  + accesses);
	}
	
	public Set<Access> getAccesses () {
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
		StringJoiner sj = new StringJoiner("");
		sj.add("ConstraintExecution [constraint=" + unit + ", self=" + self + "]");
		
		if(accesses.size()>1) {
			sj.add("\n > Property Accesses: ");		
			int i =0;
			for(Access a : accesses) {
				sj.add("\n  > " + String.valueOf(i) + ",");
				sj.add(String.valueOf(a.getModelElement()));
				sj.add(" " + a.getPropertyName());
				i++;
			}
			sj.add("\n");
		}
		return sj.toString();
	}
		
	public boolean inValidate(Access access) {
		
		if(accesses.contains(access)) {
			// invalidate this execution
			System.out.println("Execution delete self: " + this);
			return true;		
		}		
		return false;
	}
	
}
