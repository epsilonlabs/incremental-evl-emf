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
    protected IncrementalEvlModule lastModule; // Make this optional too? (avoid null testing)
    protected Optional<ConstraintExecutionCache> constraintExecutionCache;

    protected ConstraintPropertyAccessRecorder propertyAccessRecorder = new ConstraintPropertyAccessRecorder();
    protected IncrementalEvlTrace trace = new IncrementalEvlTrace();

    //private Logger MYLOGGER = MyLog.getMyLogger();

    public IncrementalEvlModule() {
        //System.out.println(" [i] IncrementalEclModel constructor");
        MYLOGGER.log(MyLog.FLOW, " [i] IncrementalEclModel constructor");
    }

    public IncrementalEvlModule(IncrementalEvlModule lastModule) {
        //System.out.println(" [i] IncrementalEclModel constructor");
        MYLOGGER.log(MyLog.FLOW, " [i] IncrementalEclModel constructor -- with the lastModule");
        this.lastModule = lastModule;
        // Setup the Execution Cache here using the last Module
        constraintExecutionCache = Optional.of(new ConstraintExecutionCache(lastModule));

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

                    // Return the old result if available
                    // moduleElement >> this ??
                    if(null != lastModule) {
                        System.out.println("\nSearching lastModule ConstraintTrace : "
                                + lastModule.getContext().getConstraintTrace().getItems().size()
                                + self.hashCode() + " & " + this.getName());
                        for (ConstraintTraceItem item : lastModule.getContext().getConstraintTrace().getItems()) {
                            System.out.print("  Model: " + item.getInstance().hashCode() + " == " + self.hashCode());
                            System.out.println(" && Constraint: " + item.getConstraint().getName() + " == " + this.getName());

                            if (item.getInstance().equals(self) && item.getConstraint().equals(this)) {
                                System.out.println("- MATCHED model & constraint - " + self.hashCode() + " " + this.getName());

                                getContext().getConstraintTrace().addChecked(item.getConstraint(), item.getInstance(), item.getResult()); // Back-fill for bypass
                                if (item.getResult()) {
                                    System.out.println("Result = PASS (TRUE) - [EMPTY] ");
                                    return Optional.empty();
                                } else {
                                    System.out.println("Result = FAIL (FALSE) : ");
                                    // Go find the unsatisfied constraint in the list
                                    System.out.print(" Searching lastModule UnsatisfiedConstraints : "
                                            + lastModule.getContext().getUnsatisfiedConstraints().size()
                                            + self.hashCode() + " & " + this.getName());
                                    for (UnsatisfiedConstraint uc : lastModule.getContext().getUnsatisfiedConstraints()) {
                                        System.out.print("    Model: " + uc.getInstance().hashCode() + " == " + self.hashCode());
                                        System.out.println(" && Constraint: " + uc.getConstraint().getName() + " == " + this.getName());
                                        if ( uc.getInstance().equals(self) && uc.getConstraint().equals(this)  ) {
                                            System.out.println (" - MATCHED UC -  UC Result: " + uc.getConstraint().getName());
                                            getContext().getUnsatisfiedConstraints().add(uc);  // Back-fill for the bypass
                                            return Optional.of(uc);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else {
                        System.out.println(" (No lastModule) -- Validating: "
                                + self.hashCode() + " & " + this.getName());
                    }

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
