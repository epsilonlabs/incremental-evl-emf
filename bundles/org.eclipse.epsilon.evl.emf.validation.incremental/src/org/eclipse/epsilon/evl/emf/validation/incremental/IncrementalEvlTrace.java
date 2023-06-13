package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.StringJoiner;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.epsilon.eol.execute.introspection.recording.IPropertyAccess;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.Constraint;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.ConstraintExecution;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.Execution;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.PropertyAccess;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.Trace;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.TraceFactory;

public class IncrementalEvlTrace {
	private static final Logger LOGGER = Logger.getLogger(IncrementalEvlTrace.class.getName());
	
	/*
	 * Contains a trace model that is used to store the recorded constraint executions of an IncrementalEVLmodule.
	 * 
	 * The methods only permit adding to the trace, you can't and should not be removing anything from a trace model while recording!
	 */
    
    // NEW MODEL APPROACH
    protected TraceFactory traceFactory =  TraceFactory.eINSTANCE;	
    protected Trace traceModel; // The trace model representing the state for an "IncrementalEvlTrace"
	
	public IncrementalEvlTrace() {
		traceModel = TraceFactory.eINSTANCE.createTrace();
	}
	
	public IncrementalEvlTrace(Trace traceModel) {
		this.traceModel = traceModel;
	}	
	
	public void processPropertyAccessRecorder(ConstraintPropertyAccessRecorder propertyAccessRecorder) {
		// The property accesses in the recorder can have different executions to the
		// one currently in the property Access recorder
		
		for (IPropertyAccess propertyAccess : propertyAccessRecorder.getPropertyAccesses().all()) {
			PropertyAccess traceModelPropertyAccess = traceFactory.createPropertyAccess();

			ConstraintExecution executionForPropertyAccess = ((ConstraintPropertyAccess) propertyAccess).getExecution();

			traceModelPropertyAccess.setElement((EObject) propertyAccess.getModelElement());
			traceModelPropertyAccess.setProperty(propertyAccess.getPropertyName());
			traceModelPropertyAccess.getExecutions().add(executionForPropertyAccess);

			this.addPropertyAccessToTraceModel(traceModelPropertyAccess);
		}
	}
	
	public ConstraintExecution createExecutionTraceModel(Object modelElement, Object constraint) {

		ConstraintExecution mExecution = traceFactory.createConstraintExecution();
		mExecution.setModelElement((EObject) modelElement);

		Constraint mConstraint = traceFactory.createConstraint();			
		mConstraint.setRaw(constraint);
		mExecution.setConstraint(mConstraint);
		
		this.addExecutionToTraceModel(mExecution);		
		return mExecution;
		
	}
	
	public void addExecutionToTraceModel (Execution execution) {
		
		//
		// Do we need to filter/check before adding to the model?
		//
		
		LOGGER.finest("  IncrementalEvlTrace.addExecution() : " + execution);
		traceModel.getExecutions().add(execution);		
	}
	
    public void addPropertyAccessToTraceModel(PropertyAccess propertyAccess) {
    	
		//
		// Do we need to filter/check before adding to the model?
		//
    	
    	LOGGER.finest("  IncrementalEvlTrace.addProperyAccess() : " + propertyAccess);
        traceModel.getAccesses().add(propertyAccess);
    }
    
	public Trace getTraceModel() {
		return traceModel;
	}

    public String toString() {
    	StringJoiner sj = new StringJoiner("");
    	int i = 0;
    	for (Execution e : traceModel.getExecutions()) {
    		i++;
    		sj.add("\n" + String.valueOf(i) + ", ");
    		sj.add(e.toString());
    	}
    	
		return sj.toString();
    	
    }
}
