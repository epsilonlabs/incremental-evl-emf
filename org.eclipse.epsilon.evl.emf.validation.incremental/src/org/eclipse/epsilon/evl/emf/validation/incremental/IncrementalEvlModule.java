package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.Optional;
import java.util.Set;

import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.introspection.recording.IPropertyAccess;
import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccessExecutionListener;
import org.eclipse.epsilon.evl.EvlModule;
import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;

public class IncrementalEvlModule extends EvlModule {
	
	protected ConstraintPropertyAccessRecorder propertyAccessRecorder = new ConstraintPropertyAccessRecorder();
	protected IncrementalEvlTrace trace = new IncrementalEvlTrace();
	
	@Override
	public ModuleElement adapt(AST cst, ModuleElement parentAst) {
		ModuleElement moduleElement = super.adapt(cst, parentAst);
		if (moduleElement instanceof Constraint) {
			
			
			return new Constraint() {
				public Optional<UnsatisfiedConstraint> execute(IEolContext context_, Object self) throws EolRuntimeException {
					
					// We could check here if the last execution -- return the result of the last test
					
					// else run the test					
					propertyAccessRecorder.setExecution(new ConstraintExecution(this, self));
					return super.execute(context_, self);
				};
			};
		}
		return moduleElement;
	}
	
	@Override
	public Set<UnsatisfiedConstraint> execute() throws EolRuntimeException {
		propertyAccessRecorder.startRecording();
		getContext().getExecutorFactory().addExecutionListener(new PropertyAccessExecutionListener(propertyAccessRecorder));
		
		Set<UnsatisfiedConstraint> unsatisfiedConstraints = super.execute();  // persist this in the instance?	
		
		for (IPropertyAccess propertyAccess : propertyAccessRecorder.getPropertyAccesses().all()) {
			trace.addPropertyAccess((ConstraintPropertyAccess) propertyAccess);
		}
		
		return unsatisfiedConstraints;
	}
	
	public ConstraintPropertyAccessRecorder getPropertyAccessRecorder() {
		return propertyAccessRecorder;
	}
	
	public IncrementalEvlTrace getTrace() {
		return trace;
	}
	
}
