package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.epsilon.eol.execute.introspection.recording.IPropertyAccess;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.Execution;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.PropertyAccess;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.Trace;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.TraceFactory;

public class IncrementalEvlTrace {
	private static final Logger LOGGER = Logger.getLogger(IncrementalEvlTrace.class.getName());
	
	/*
	 * Contains a trace model that can be used for capturing the execution of an EVL.
	 * 
	 * The methods only permit adding to the trace, you can't and should not be removing anything from a trace model in this context!
	 * 
	 */
    
	protected List<ConstraintPropertyAccess> originalCPA = new ArrayList<>();
    public void addConstraintPropertyAccess(ConstraintPropertyAccess propertyAccess) {
    	originalCPA.add(propertyAccess);
    }
	
    protected TraceFactory traceFactory =  TraceFactory.eINSTANCE;
	
	protected Trace traceModel; // The trace model representing the state for an "IncrementalEvlTrace"
	
	public IncrementalEvlTrace() {
		traceModel = TraceFactory.eINSTANCE.createTrace();
	}
	
	public IncrementalEvlTrace(Trace traceModel) {
		this.traceModel = traceModel;
	}

	public void addExecution (Execution execution) {
		LOGGER.finer("  IncrementalEvlTrace.addExecution() : " + execution);
		traceModel.getExecutions().add(execution);		
	}
	
    public void addPropertyAccess(PropertyAccess propertyAccess) {
    	LOGGER.finer("  IncrementalEvlTrace.addProperyAccess() : " + propertyAccess);
        traceModel.getAccesses().add(propertyAccess);
    }
    
    


    
    
    
    /*
    
     Invalidates Executions?  -- this should move to Execution Cache?
     
    protected Set<Execution> propertyModified(Object modelElement, String propertyName) {

        Set<Execution> invalidatedExecutions = propertyAccesses.stream().
                filter(propertyAccess -> propertyAccess.getModelElement() == modelElement && propertyName.equals(propertyAccess.getPropertyName())).
                map(propertyAccess -> propertyAccess.getExecution()).collect(Collectors.toSet());

        propertyAccesses.removeIf(propertyAccesses -> invalidatedExecutions.contains(propertyAccesses.getExecution()));

        return invalidatedExecutions;
    }
	*/
    
    
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
