package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;

public class IncrementalEvlValidatorAdapter extends EContentAdapter {
	
	protected IncrementalEvlValidator validator = null;
	protected List<Notification> notifications = new ArrayList<>();
	
	public IncrementalEvlValidatorAdapter(IncrementalEvlValidator validator) {
		this.validator = validator;
	}
	
	public void validate(ResourceSet resourceSet) throws Exception {
		
		IncrementalEvlModule module = new IncrementalEvlModule();
		module.parse(validator.getConstraints());
		
		InMemoryEmfModel model = new InMemoryEmfModel(resourceSet.getResources().get(0));
		model.setConcurrent(true);
		module.getContext().getModelRepository().addModel(model);
		
		Set<UnsatisfiedConstraint> unsatisfiedConstraints = module.execute();
		
	}
	
	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		notifications.add(notification);
	}
	
	public IncrementalEvlValidator getValidator() {
		return validator;
	}
	
}
