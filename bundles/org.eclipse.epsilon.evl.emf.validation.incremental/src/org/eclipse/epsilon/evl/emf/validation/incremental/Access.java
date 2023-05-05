package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccess;

// Should this be Abstract?
public class Access {

	// Adapter for ConstraintPropertyAccess and ConstraintAllAccess

	
	protected PropertyAccess propertyAccess;
	
	public Access (PropertyAccess cpa) {
		this.propertyAccess = cpa;
	}
	
	public PropertyAccess getPropertyAccess () {
		return this.propertyAccess;
	}
	
	public Object getModelElement () {		
		return propertyAccess.getModelElement();
	}
		
	public String getPropertyName() {
		return propertyAccess.getPropertyName();
	}
	
	
	
	public String toString() {
		if (propertyAccess != null) {
			return propertyAccess.toString();
		}
		return "";
	}

}
