package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.epsilon.common.module.IModule;
import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.dom.PropertyCallExpression;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.SingleFrame;
import org.eclipse.epsilon.eol.execute.introspection.recording.IPropertyAccess;
import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccessExecutionListener;
import org.eclipse.epsilon.evl.EvlModule;
import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.epsilon.evl.trace.ConstraintTraceItem;

import static org.eclipse.epsilon.evl.emf.validation.incremental.IncrementalEcoreValidator.MYLOGGER;

public class IncrementalEvlModule extends EvlModule {
    private static boolean REPORT = false;

    protected Optional<ConstraintExecutionCache> constraintExecutionCache = Optional.empty();

    protected ConstraintPropertyAccessRecorder propertyAccessRecorder = new ConstraintPropertyAccessRecorder();
    protected IncrementalEvlTrace trace = new IncrementalEvlTrace();

    //private Logger MYLOGGER = MyLog.getMyLogger();

    public IncrementalEvlModule() {
        //System.out.println(" [i] IncrementalEclModel constructor");
        MYLOGGER.log(MyLog.FLOW, " [i] IncrementalEclModel constructor");
        System.out.println("\n -- Module init --");
    }


    public IncrementalEvlModule(Optional <ConstraintExecutionCache> constraintExecutionCache) {
        MYLOGGER.log(MyLog.FLOW, " [i] IncrementalEclModel constructor -- with the constraintExecutionCache");
        System.out.println("\n -- Module init with 'constraintExecutionCache' -- ");
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
                    // Notifications REMOVE PropertyAccesses from the LastTrace. (Elements with no access get tested)

                    // the "lastModule" in this section needs to be change to ask the ExecutionCache if there are any useable results from a prior execution
                    // This should backfill the module "propertyAccess (trace) with the pa in the Execution Cache

                    if(constraintExecutionCache.isPresent()) {
                        System.out.println("\nSearching constraintExecutionCache ConstraintTrace : "
                                + self.hashCode() + " & " + this.getName());
                        ConstraintTraceItem ctitem = constraintExecutionCache.get().checkCachedConstraintTrace(self,this );
                        if (null != ctitem) {
                            getContext().getConstraintTrace().addChecked(ctitem.getConstraint(), ctitem.getInstance(), ctitem.getResult()); // Back-fill for bypass
                            if(ctitem.getResult()) {
                                System.out.println("Result = PASS (TRUE) - [EMPTY] ");
                                return Optional.empty();
                            } else {
                                UnsatisfiedConstraint uc = constraintExecutionCache.get().getCachedUnsatisfiedConstraint(self,this);
                                getContext().getUnsatisfiedConstraints().add(uc);  // Back-fill for the bypass
                                return Optional.of(uc);
                            }
                        }
                    }
                    else {
                        System.out.println(" [!] No constraintExecutionCache ");
                    }

                    System.out.println(" Need for Validation: " + self.hashCode() + " & " + this.getName());

                    // Set up the recorder and execute the constraint test to get a result
                    propertyAccessRecorder.setExecution(new ConstraintExecution(this, self));
                    Result = super.execute(context_, self);

                    // Just some Logging
                    MYLOGGER.log(MyLog.EXPLORE, " [exec] model: " + ((EClass) self).getName() + " | constraint: " + this.getName() + " | Result: " + Result);
                    //System.out.println(" [exec] model: " + ((EClass) self).getName() + " | constraint: " + this.getName() + " | Result: " + Result);
                    //System.out.println("model: " + ((EClass) self).hashCode() + " | constraint: " + this.hashCode() + " | Result: " + Result);
                    return Result;
                }


            };
        }
        return moduleElement;
    }

    @Override
    public Set<UnsatisfiedConstraint> execute() throws EolRuntimeException {

        //System.out.println("\nEXECUTING CONSTRAINTS");

        propertyAccessRecorder.startRecording();
        getContext().getExecutorFactory().addExecutionListener(new PropertyAccessExecutionListener(propertyAccessRecorder));

        // Jumps to >> public ModuleElement adapt(AST cst, ModuleElement parentAst)
        Set<UnsatisfiedConstraint> unsatisfiedConstraints = super.execute();  // The returning Unsatisfied Constraints come in here.

        // Transfer captured propertyAccesses from the Recorder to the ConstrainPropertyAccess (trace).
        for (IPropertyAccess propertyAccess : propertyAccessRecorder.getPropertyAccesses().all()) {
            trace.addPropertyAccess((ConstraintPropertyAccess) propertyAccess);
        }

        //trace.setUnsatisfiedConstraints(unsatisfiedConstraints);  // Store the list on the trace, which will become the "last trace"
        //System.out.println("UnsatisfiedConstraints: " + unsatisfiedConstraints.size());

        return unsatisfiedConstraints;
    }

    public ConstraintPropertyAccessRecorder getPropertyAccessRecorder() {
        return propertyAccessRecorder;
    }

    public IncrementalEvlTrace getTrace() {
        return trace;
    }

}
