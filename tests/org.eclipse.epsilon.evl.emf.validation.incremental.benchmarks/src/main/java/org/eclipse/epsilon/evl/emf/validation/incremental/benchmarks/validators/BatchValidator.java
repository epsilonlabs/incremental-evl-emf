package org.eclipse.epsilon.evl.emf.validation.incremental.benchmarks.validators;

import java.io.File;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.evl.EvlModule;

public class BatchValidator implements IValidator {

	private final String constraintsPath;

	public BatchValidator(String constraintsPath) {
		this.constraintsPath = constraintsPath;
	}

	@Override
	public void validate(Resource r) throws Exception {
		EvlModule module = new EvlModule();
		module.parse(new File(constraintsPath));

		InMemoryEmfModel model = new InMemoryEmfModel(r);
		model.setConcurrent(true);
		model.setName("Model");
    	try {
    		module.getContext().getModelRepository().addModel(model);
    		module.execute();
    	} finally {
    		module.getContext().getModelRepository().dispose();
    		module.getContext().dispose();
    	}
	}

}
