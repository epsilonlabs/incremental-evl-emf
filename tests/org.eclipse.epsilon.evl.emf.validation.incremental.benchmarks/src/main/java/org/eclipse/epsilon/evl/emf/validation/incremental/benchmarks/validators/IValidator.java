package org.eclipse.epsilon.evl.emf.validation.incremental.benchmarks.validators;

import org.eclipse.emf.ecore.resource.Resource;

public interface IValidator {

	public void validate(Resource r) throws Exception;

}
