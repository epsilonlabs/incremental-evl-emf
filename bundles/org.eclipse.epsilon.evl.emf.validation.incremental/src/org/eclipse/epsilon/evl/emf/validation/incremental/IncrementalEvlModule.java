package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.introspection.recording.IPropertyAccess;
import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccessExecutionListener;
import org.eclipse.epsilon.evl.EvlModule;
import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.epsilon.evl.trace.ConstraintTraceItem;

public class IncrementalEvlModule extends EvlModule {
	private static final Logger LOGGER = Logger.getLogger(IncrementalEvlModule.class.getName());
	/*
	 * Logging levels
	 * 	- System activities finer
	 *  - System states finest
	 */

    protected Optional<ConstraintExecutionCache> constraintExecutionCache = Optional.empty();

    protected ConstraintPropertyAccessRecorder propertyAccessRecorder = new ConstraintPropertyAccessRecorder();
    protected IncrementalEvlTrace trace = new IncrementalEvlTrace();

    public IncrementalEvlModule() {
        LOGGER.finer(() -> "IncrementalEVLModule started without constraintExecutionCache");
    }

    public IncrementalEvlModule(Optional <ConstraintExecutionCache> constraintExecutionCache) {        
        LOGGER.finer(() -> "IncrementalEVLModule started with constraintExecutionCache");
        this.constraintExecutionCache = constraintExecutionCache;

        // Transfer prior propertyAccesses from the constraintExecutionCache into this modules trace.
        for (IPropertyAccess propertyAccess : constraintExecutionCache.get().constraintPropertyAccess) {
            trace.addPropertyAccess((ConstraintPropertyAccess) propertyAccess);
        }
    }

    @Override
    public ModuleElement adapt(AST cst, ModuleElement parentAst) {
        ModuleElement moduleElement = super.adapt(cst, parentAst);

        if (moduleElement instanceof Constraint) {
            return new Constraint() {
                public Optional<UnsatisfiedConstraint> execute(IEolContext context_, Object self) throws EolRuntimeException {
                    // this -- is the constraint
                    // self -- is the model element under test
                    // lastTrace -- is the last execution trace of the constraints
                    Optional<UnsatisfiedConstraint> Result; // The Result of executing the Constraint.

                    // We could check here if the last execution -- return the result of the last execution instead of executing the constraint test
                    // Notifications REMOVE PropertyAccesses from the LastTrace. (Elements with no property accesses get tested)

                    // The ExecutionCache is searched for any useable results from a prior execution
                    // This should backfill the module "propertyAccess (trace) with the pa in the Execution Cache
                    // Then if nothing is found in the ExecutionCache a validation test is performed

                    if(constraintExecutionCache.isPresent()) {
                    	
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
                    // else { if(REPORTstate) {logger.log(Level.INFO,"No constraintExecutionCache "); } }
                    LOGGER.finer(() -> "Need for Validation: " + self.hashCode() + " & " + this.getName());

                    // Set up the recorder and execute the constraint test to get a result
                    propertyAccessRecorder.setExecution(new ConstraintExecution(this, self));
                    Result = super.execute(context_, self);

                    LOGGER.finest(() -> "Validation test Result - " + Result);
                    return Result;
                }


            };
        }
        return moduleElement;
    }

    @Override
    public Set<UnsatisfiedConstraint> execute() throws EolRuntimeException {
        propertyAccessRecorder.startRecording();
        getContext().getExecutorFactory().addExecutionListener(new PropertyAccessExecutionListener(propertyAccessRecorder));

        // Jumps to >> public ModuleElement adapt(AST cst, ModuleElement parentAst)
        Set<UnsatisfiedConstraint> unsatisfiedConstraints = super.execute();  // The returning Unsatisfied Constraints come in here.

        // Transfer captured propertyAccesses from the Recorder to the ConstrainPropertyAccess (trace).
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
