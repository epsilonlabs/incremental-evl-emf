package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.net.URI;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.ResourceSet;

public abstract class IncrementalEvlValidator implements EValidator {

	private static final Logger LOGGER = Logger.getLogger(IncrementalEcoreValidator.class.getName());
	
	public abstract URI getConstraintsURI();
	
	@Override
	public boolean validate(EClass eClass, EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {

		try {
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine("\n [!] IncrementalEvlValidator.validate() called\n");
				LOGGER.fine("\n\n--- IncrementalEvlValidator.validate()");
				LOGGER.fine("eClass: " + eClass);
				LOGGER.fine("eObject: " + eObject);		
				LOGGER.fine("diagnostic: "+ diagnostics); // return for the constraint check
				LOGGER.fine("Context (Map): "+ context);
				LOGGER.fine("---\n");
			}

			return validateImpl(eClass, eObject, diagnostics, context);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean validateImpl(EClass eClass, EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) throws Exception {
		LOGGER.fine("\n [!] IncrementalEvlValidator.validateImpl() called\n");

		// Get hold of the resource set of the eObject
		// We only want to validate each resource set once in batch mode
		// and then listen for changes to update validation results incrementally
		ResourceSet resourceSet = eObject.eResource().getResourceSet();
		
		// Check to see if the resource set already has an IncrementalEvlValidatorAdapter
		IncrementalEvlValidatorAdapter adapter = getIncrementalEvlValidatorAdapter(resourceSet);
		
		// If it has such an adapter it means that the resource set has already
		// been batch validated
		if (adapter != null) {
			LOGGER.fine("Already has adapter : hashCode: " + adapter.hashCode());

			if (adapter.mustRevalidate(resourceSet)) {
				adapter.revalidate(resourceSet);
				return adapter.getModule().getContext().getUnsatisfiedConstraints().isEmpty();
			}
			
		}
		// otherwise, we need to add the adapter and trigger batch validation
		else {
			adapter = new IncrementalEvlValidatorAdapter(this);
			resourceSet.eAdapters().add(adapter);
			adapter.validate(resourceSet);

			LOGGER.fine("Added adapter : hashCode" + adapter.hashCode());
			return adapter.getModule().getContext().getUnsatisfiedConstraints().isEmpty();
		}
		
		return true;
	}
	
	public IncrementalEvlValidatorAdapter getIncrementalEvlValidatorAdapter(ResourceSet resourceSet) {
		return (IncrementalEvlValidatorAdapter) resourceSet.eAdapters().stream().
				filter(a -> a instanceof IncrementalEvlValidatorAdapter && ((IncrementalEvlValidatorAdapter) a).getValidator() == this).
				findFirst().orElse(null);
	}

	@Override
	public boolean validate(EDataType eDataType, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) { return false; }
	
	@Override
	public boolean validate(EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) { return false; }

}
