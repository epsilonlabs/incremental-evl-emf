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

import static org.eclipse.epsilon.evl.emf.validation.incremental.IncrementalEcoreValidator.MYLOGGER;

public class IncrementalEvlModule extends EvlModule {
    private static boolean REPORT = false;

    //private Logger MYLOGGER = MyLog.getMyLogger();
    protected List<Notification> notifications;
    protected Set<UnsatisfiedConstraint> unsatisfiedConstraints;
    protected IncrementalEvlTrace lastTrace;

    public IncrementalEvlModule() {
        //System.out.println(" [i] IncrementalEclModel constructor");
        MYLOGGER.log(MyLog.FLOW, " [i] IncrementalEclModel constructor");
    }

    public IncrementalEvlModule(List<Notification> notifications, Set<UnsatisfiedConstraint> unsatisfiedConstraints, IncrementalEvlTrace lastTrace) {
        //System.out.println(" [i] IncrementalEclModel constructor with settings");
        MYLOGGER.log(MyLog.FLOW, " [i] IncrementalEclModel constructor with settings");
        this.notifications = notifications;
        this.unsatisfiedConstraints = unsatisfiedConstraints;
        this.lastTrace = lastTrace;
    }

    protected ConstraintPropertyAccessRecorder propertyAccessRecorder = new ConstraintPropertyAccessRecorder();
    protected IncrementalEvlTrace trace = new IncrementalEvlTrace();

    @Override
    public ModuleElement adapt(AST cst, ModuleElement parentAst) {

        ModuleElement moduleElement = super.adapt(cst, parentAst);

        if (moduleElement instanceof Constraint) {
            return new Constraint() {
                public Optional<UnsatisfiedConstraint> execute(IEolContext context_, Object self) throws EolRuntimeException {
                    // this -- is the constraint
                    // self -- is the model element under test
                    // lastTrace -- is the last execution trace of the constraints

                    // We could check here if the last execution -- return the result of the last test
                    // Is there a notification? Is it on the last Trace? Is it listed as an unsatisfied constraint?
                    // else run the test

                    if(null != lastTrace) {
                        lastTrace.checkPropertyAccesses(self,this);  // check to see if there is a property access listed in the lastTrace
                        lastTrace.checkUnsatisfiedContraint(self, this); // get the last result if there is one
                        for (ConstraintPropertyAccess propertyAccess : lastTrace.propertyAccesses) {
                            if ((self.hashCode() == propertyAccess.getModelElement().hashCode())
                                &&
                                    (this.hashCode() == propertyAccess.getExecution().getConstraint().hashCode())) {
                                System.out.println("\n MATCHED Model HASH " + self.hashCode()
                                        + "\n && Const hash: " + this.hashCode() + " == " + propertyAccess.getExecution().getConstraint().hashCode());
                            }
                        }

                    }
                    // Set up the recorder and execute the constraint test to get a result
                    propertyAccessRecorder.setExecution(new ConstraintExecution(this, self));
                    Optional<UnsatisfiedConstraint> Result = super.execute(context_, self);

                    MYLOGGER.log(MyLog.EXPLORE, " [exec] model: " + ((EClass) self).getName() + " | constraint: " + this.getName() + " | Result: " + Result);
                    System.out.println(" [exec] model: " + ((EClass) self).getName() + " | constraint: " + this.getName() + " | Result: " + Result);
                    //System.out.println("model: " + ((EClass) self).hashCode() + " | constraint: " + this.hashCode() + " | Result: " + Result);
                    return Result;
                }


            };
        }
        return moduleElement;
    }

    @Override
    public Set<UnsatisfiedConstraint> execute() throws EolRuntimeException {

        // RUN or fake RUN here?
        System.out.println("\nEXECUTING CONSTRAINTS");


        propertyAccessRecorder.startRecording();
        getContext().getExecutorFactory().addExecutionListener(new PropertyAccessExecutionListener(propertyAccessRecorder));

        // Jumps to >> public ModuleElement adapt(AST cst, ModuleElement parentAst)
        Set<UnsatisfiedConstraint> unsatisfiedConstraints = super.execute();  // persist this in the instance?

        for (IPropertyAccess propertyAccess : propertyAccessRecorder.getPropertyAccesses().all()) {
            trace.addPropertyAccess((ConstraintPropertyAccess) propertyAccess);
        }
        trace.setUnsatisfiedConstraints(unsatisfiedConstraints);

        System.out.println("UnsatisfiedConstraints: " + unsatisfiedConstraints.size());
        return unsatisfiedConstraints;
    }

    public ConstraintPropertyAccessRecorder getPropertyAccessRecorder() {
        return propertyAccessRecorder;
    }

    public IncrementalEvlTrace getTrace() {
        return trace;
    }

}
