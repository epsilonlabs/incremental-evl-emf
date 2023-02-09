package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
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

    public List<Notification> notifications;
    private Set<UnsatisfiedConstraint> unsatisfiedConstraints;
    private IncrementalEvlTrace lastTrace;

    public IncrementalEvlModule() {
        System.out.println(" [i] IncrementalEclModel constructor");
    }

    public IncrementalEvlModule(List<Notification> notifications, Set<UnsatisfiedConstraint> unsatisfiedConstraints, IncrementalEvlTrace lastTrace) {
        System.out.println(" [i] IncrementalEclModel constructor with settings");
        this.notifications = notifications;
        this.unsatisfiedConstraints = unsatisfiedConstraints;
        this.lastTrace = trace;
    }

    protected ConstraintPropertyAccessRecorder propertyAccessRecorder = new ConstraintPropertyAccessRecorder();
    protected IncrementalEvlTrace trace = new IncrementalEvlTrace();

    @Override
    public ModuleElement adapt(AST cst, ModuleElement parentAst) {

        ModuleElement moduleElement = super.adapt(cst, parentAst);

        // OK this gets tricky here as we seem to determine what things are and create new instances and return them
        if (moduleElement instanceof Constraint) {
            Constraint tempContraint = (Constraint) moduleElement;
            System.out.println("what this? hashcode : " + tempContraint);

            return new Constraint() {
                public Optional<UnsatisfiedConstraint> execute(IEolContext context_, Object self) throws EolRuntimeException {

                    // A context and a self, what is a self?
                    System.out.println("self is a : " + self.getClass());
                    EClass model = (EClass) self;
                    System.out.println("self is a model element >> HashCode : " + model.hashCode());

                    // Is model element in the notification list?
                    if (null != notifications){
                        for (Notification n: notifications) {
                            EStructuralFeature feature = (EStructuralFeature) n.getFeature();
                            System.out.println(" !!! - I GOT THAT THING YOU SENT ME - !!! HashCode: " + feature.getName() );
                        }
                    }


                    // We could check here if the last execution -- return the result of the last test
                    // Is there a notification? Is it on the last Trace? Is it listed as an unsatisfied constrain?
                    // else run the test			1
                    propertyAccessRecorder.setExecution(new ConstraintExecution(this, self));
                    return super.execute(context_, self);
                }


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
