package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.eol.dom.Operation;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.introspection.recording.IPropertyAccess;
import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccessExecutionListener;
import org.eclipse.epsilon.evl.EvlModule;
import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.ConstraintExecution;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.PropertyAccess;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.TraceFactory;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.epsilon.evl.trace.ConstraintTraceItem;

public class IncrementalEvlModule extends EvlModule {
	private static final Logger LOGGER = Logger.getLogger(IncrementalEvlModule.class.getName());
	/*
	 * Logging levels
	 * 	- System activities finer
	 *  - System states finest
	 */
	
	private final boolean CACHEACTIVE = true;
	
	protected TraceFactory traceFactory =  TraceFactory.eINSTANCE;
	
    protected Optional<ConstraintExecutionCache> constraintExecutionCache = Optional.empty();

    protected ConstraintPropertyAccessRecorder propertyAccessRecorder = new ConstraintPropertyAccessRecorder();
    
    protected IncrementalEvlTrace evlTrace;
    
    public IncrementalEvlModule() {
        LOGGER.finest(() -> "IncrementalEVLModule started without constraintExecutionCache");
        evlTrace = new IncrementalEvlTrace();
    }

    public IncrementalEvlModule(Optional <ConstraintExecutionCache> constraintExecutionCache) {        
        LOGGER.finest(() -> "IncrementalEVLModule started with constraintExecutionCache");
        this.constraintExecutionCache = constraintExecutionCache;
        
        // Now we setup the evlTrace with a Trace model from the execution cache to this module for recording to
        evlTrace = new IncrementalEvlTrace(this.constraintExecutionCache.get().traceModel);
    }

    @Override
    public ModuleElement adapt(AST cst, ModuleElement parentAst) {
    	ModuleElement moduleElement = super.adapt(cst, parentAst);

    	if (moduleElement instanceof Operation) {
    		if (((Operation) moduleElement).hasAnnotation("cached")) {
    			//return new CachedOperation();
    			System.out.println("Cached Operation not supported yet...");
    			return null;
    		}
    	} else if (moduleElement instanceof Constraint) {
            return new Constraint() {
                public Optional<UnsatisfiedConstraint> execute(IEolContext context_, Object self) throws EolRuntimeException {
                    // this -- is the constraint
                    // self -- is the model element under test
                    // evlTrace.traceModel -- is the last execution trace of the constraints                    

                    // We ask the execution cache for known results for the validation being requested -- return the of the last execution instead of executing the constraint validation
                    // Notifications REMOVE Executions/Accesses from the trace model in the Execution Cache. (Elements with no property accesses get tested)

                    // The ExecutionCache is searched for any usable results from a prior execution
                    // This should back fill the module "propertyAccess (trace) with the pa in the Execution Cache
                    // Then if nothing is found in the ExecutionCache a validation test is performed

                	//
                	//  CHECK CACHE FOR KNOWN VALIDATION RESULTS
                	//
                	
                    if(constraintExecutionCache.isPresent() && CACHEACTIVE) {
                    	// TODO the constraint execution cache needs to be updated to return results from the trace model not the cache lists.
                    	
                        LOGGER.finer(() -> "Searching constraintExecutionCache ConstraintTrace: " + self.hashCode() + " & " + this.getName());
                        
                        ConstraintTraceItem ctitem = constraintExecutionCache.get().checkCachedConstraintTrace(self,this );
                        if (null != ctitem) {
                            getContext().getConstraintTrace().addChecked(ctitem.getConstraint(), ctitem.getInstance(), ctitem.getResult()); // Back-fill for bypass
                            if(ctitem.getResult()) {
                            	LOGGER.finest(() -> "Cached Result = PASS (TRUE) - [EMPTY] ");
                                return Optional.empty();
                            } else {
                            	UnsatisfiedConstraint uc = constraintExecutionCache.get().getCachedUnsatisfiedConstraint(self,this);
                            	LOGGER.finest(() -> "Cached Result = FAIL (FALSE) - " + uc.getMessage());                                
                                getContext().getUnsatisfiedConstraints().add(uc);  // Back-fill for the bypass
                                return Optional.of(uc);
                            }
                        }
                    }
                    
                    //
                    // EXECUTE VALIDATION
                    //
                    
                    LOGGER.finer(() -> "Need for Validation: " + self.hashCode() + " & " + this.getName());

                    // Set up the recorder and execute the constraint test to get a result
                    // propertyAccessRecorder.setExecution(new ConstraintExecutionOld(this, self));
                    
                    // Add a "new" execution accesses to the trace, this execution will be updates with accesses during execution ( ConstraintProperyAccessRecorder.createConstraintPropertyAccess() )
                    ConstraintExecution mExecution = traceFactory.createConstraintExecution();  
                    evlTrace.addExecutionToTraceModel(mExecution);
                    mExecution.setContext(self);                    
                    
                    //var mConstraint = traceFactory.createConstraint();                    
                    org.eclipse.epsilon.evl.emf.validation.incremental.trace.Constraint mConstraint = traceFactory.createConstraint();
                    
                    //var constraint = this;
                    Constraint constraint = this;  //org.eclipse.epsilon.evl.dom.Constraint
                    
                    mConstraint.setRaw(constraint);
                    mExecution.setConstraint(mConstraint);
                    
                    propertyAccessRecorder.setExecution(mExecution);    
                    
                    // Do not unload the propertyAccessRecorder propertyAccesses to the trace here, the propertyAccess list doesn't clear until after this method returns.
                                      
                    // EXECUTE the Constraint and collect the resulting unsatisfied constraint, if one occurs                    
                    Optional<UnsatisfiedConstraint> Result = super.execute(context_, self);
                    mExecution.setResult(Result.isEmpty());

                    LOGGER.finest(() -> "Validation test Result - " + Result);
                    return Result;
                }
            };
        }

    	return moduleElement;
    }

    @Override
    public Set<UnsatisfiedConstraint> execute() throws EolRuntimeException {

        getContext().getExecutorFactory().addExecutionListener(new PropertyAccessExecutionListener(propertyAccessRecorder));

        propertyAccessRecorder.startRecording(); // Moved this closer to the actual execution call
        // Jumps to >> public ModuleElement adapt(AST cst, ModuleElement parentAst)        
        Set<UnsatisfiedConstraint> unsatisfiedConstraints = super.execute();  // The returning Unsatisfied Constraints come in here.
        propertyAccessRecorder.stopRecording();
        
        // PROCESS THE RECORDED PROPERY ACCESSES HERE       
        for (IPropertyAccess propertyAccess : propertyAccessRecorder.getPropertyAccesses().all()) {
    		PropertyAccess tPropertyAccess = traceFactory.createPropertyAccess();
    		((ConstraintPropertyAccess) propertyAccess).getExecution();		
    		tPropertyAccess.setElement(propertyAccess.getModelElement());  		
    		tPropertyAccess.setProperty(propertyAccess.getPropertyName());
        	// The property accesses in the recorder can have different executions
    		tPropertyAccess.getExecutions().add(((ConstraintPropertyAccess) propertyAccess).getExecution());       		
    		evlTrace.addPropertyAccessToTraceModel(tPropertyAccess);                        
        }                
        
        // Transfer captured propertyAccesses from the Recorder to the ConstrainPropertyAccess (trace).
        for (IPropertyAccess propertyAccess : propertyAccessRecorder.getPropertyAccesses().all()) {
            evlTrace.addConstraintPropertyAccessToList((ConstraintPropertyAccess) propertyAccess);
        }
        
        System.out.println("  evlTrace Contains : "  
        + evlTrace.traceModel.getExecutions().size() + " executions" 
        + " - " + evlTrace.traceModel.getAccesses().size() + " accesses\n"
        + "ConstraintPropertyAccessRecorder : " + propertyAccessRecorder.getPropertyAccesses().all().size());
        
        return unsatisfiedConstraints;
    }

    public ConstraintPropertyAccessRecorder getPropertyAccessRecorder() {
        return propertyAccessRecorder;
    }

    public IncrementalEvlTrace getEvlTrace() {
        return evlTrace;
    }

}
