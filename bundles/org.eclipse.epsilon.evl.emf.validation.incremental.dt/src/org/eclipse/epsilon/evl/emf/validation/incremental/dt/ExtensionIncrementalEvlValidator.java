package org.eclipse.epsilon.evl.emf.validation.incremental.dt;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.epsilon.evl.emf.validation.EvlMarkerResolutionGenerator;
import org.eclipse.epsilon.evl.emf.validation.EvlValidator;
import org.eclipse.epsilon.evl.emf.validation.incremental.IncrementalEvlValidator;
import org.eclipse.epsilon.evl.emf.validation.incremental.IncrementalEvlValidatorAdapter;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;

/**
 * <p>
 * Incremental version of the regular EvlValidator, which is usable from
 * <a href=
 * "https://www.eclipse.org/epsilon/doc/articles/evl-emf-integration/#via-the-extension-point">the
 * regular extension point</a>.
 * </p>
 * 
 * TODO decide whether to move this into a .dt plugin, or to fold this
 * functionality into the regular IncrementalEvlValidator.
 */
public class ExtensionIncrementalEvlValidator extends EvlValidator {

	private IncrementalEvlValidator validator;

	@Override
	public void initialise(URI source, String modelName, String ePackageUri, String bundleId) {
		super.initialise(source, modelName, ePackageUri, bundleId);

		validator = new IncrementalEvlValidator() {
			@Override
			public URI getConstraintsURI() {
				return source;
			}
		};
	}

	@Override
	public boolean validate(EClass eClass, EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (eObject.eResource() == null)
			return false;

		if (diagnostics != null) {
			// A complete validation is performed, so clear old fixes
			EvlMarkerResolutionGenerator.INSTANCE.removeFixesFor(eObject);
		}

		if (diagnostics != this.diagnostics) {
			this.diagnostics = diagnostics;
		}

		final boolean isValid = validator.validate(eClass, eObject, diagnostics, context);

		if (!isValid) {
			// Get hold of the resource set of the eObject
			// We only want to validate each resource set once in batch mode
			// and then listen for changes to update validation results incrementally
			ResourceSet resourceSet = eObject.eResource().getResourceSet();

			// Check to see if the resource set already has an
			// IncrementalEvlValidatorAdapter
			IncrementalEvlValidatorAdapter adapter = validator.getIncrementalEvlValidatorAdapter(resourceSet);

			// Get the unsatisfied constraints from the EVL module
			results.clear();
			for (UnsatisfiedConstraint unsat : adapter.getModule().getContext().getUnsatisfiedConstraints()) {
				Collection<UnsatisfiedConstraint> listUnsatisfied = results.computeIfAbsent(unsat.getInstance(),
						(key) -> new ArrayList<>());
				listUnsatisfied.add(unsat);
			}
		}
		addMarkers("[" + eObject.eResource().getURI() + "] ", eObject, diagnostics);

		return results.isEmpty();
	}

}
