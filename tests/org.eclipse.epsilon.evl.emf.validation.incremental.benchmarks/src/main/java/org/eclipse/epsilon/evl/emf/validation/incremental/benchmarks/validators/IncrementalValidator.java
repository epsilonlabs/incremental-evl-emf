package org.eclipse.epsilon.evl.emf.validation.incremental.benchmarks.validators;

import java.io.File;
import java.net.URI;
import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.epsilon.evl.emf.validation.incremental.IncrementalEvlValidator;

public class IncrementalValidator implements IValidator {
	private final IncrementalEvlValidator validator;

	public IncrementalValidator(String constraintsPath) {
		this.validator = new IncrementalEvlValidator() {
			@Override
			public URI getConstraintsURI() {
				return new File(constraintsPath).toURI();
			}
		};
	}

	@Override
	public void validate(Resource r) throws Exception {
		EObject root = r.getContents().get(0);
		validator.validate(root.eClass(), root, new BasicDiagnostic(), Map.of());
	}
}
