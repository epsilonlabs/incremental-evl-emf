package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.net.URI;
import java.util.Map;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.ResourceSet;

public abstract class IncrementalEvlValidator implements EValidator {
	
	protected abstract URI getConstraints();
	
	@Override
	public boolean validate(EClass eClass, EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		try {
			return validateImpl(eClass, eObject, diagnostics, context);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean validateImpl(EClass eClass, EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) throws Exception {
		
		// Get hold of the resource set of the eObject
		// We only want to validate each resource set once in batch mode
		// and then listen for changes to update validation results incrementally
		ResourceSet resourceSet = eObject.eResource().getResourceSet();
		
		// Check to see if the resource set already has an IncrementalEvlValidatorAdapter
		IncrementalEvlValidatorAdapter adapter = getIncrementalEvlValidatorAdapter(resourceSet);
		
		// If it has such an adapter it means that the resource set has already
		// been batch validated
		if (adapter != null) {
			System.out.println("Already has adapter: " + eObject);
			if (adapter.mustRevalidate(resourceSet)) {
				adapter.revalidate(resourceSet);
			}
		}
		// otherwise, we need to add the adapter and trigger batch validation
		else {
			adapter = new IncrementalEvlValidatorAdapter(this);
			resourceSet.eAdapters().add(adapter);
			adapter.validate(resourceSet);
			System.out.println("Added adapter: " + eObject);
		}
		
		return false;
	}
	
	public IncrementalEvlValidatorAdapter getIncrementalEvlValidatorAdapter(ResourceSet resourceSet) {
		return (IncrementalEvlValidatorAdapter) resourceSet.eAdapters().stream().
				filter(a -> a instanceof IncrementalEvlValidatorAdapter && ((IncrementalEvlValidatorAdapter) a).getValidator() == this).
				findFirst().orElse(null);
	}
	
	@Override
	public boolean validate(EDataType eDataType, Object value, DiagnosticChain diagnostics,
			Map<Object, Object> context) { return false; }
	
	@Override
	public boolean validate(EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) { return false; }

}
