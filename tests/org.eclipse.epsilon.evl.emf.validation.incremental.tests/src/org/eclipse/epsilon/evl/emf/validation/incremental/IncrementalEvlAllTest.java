package org.eclipse.epsilon.evl.emf.validation.incremental;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.epsilon.evl.emf.validation.incremental.TestTools.modelObjectsFromConstraintPropertyAccess;
import static org.eclipse.epsilon.evl.emf.validation.incremental.TestTools.modelObjectsFromConstraintTrace;
import static org.eclipse.epsilon.evl.emf.validation.incremental.TestTools.modelObjectsFromUnsatisfiedConstraints;
import static org.eclipse.epsilon.evl.emf.validation.incremental.TestTools.showAdapterNotifications;
import static org.eclipse.epsilon.evl.emf.validation.incremental.TestTools.showExecutionCache;

import java.io.File;

import org.eclipse.emf.codegen.ecore.genmodel.GenModelPackage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.junit.Before;
import org.junit.Test;

public class IncrementalEvlAllTest {
	
	protected Resource resource1;
    protected EPackage ePackage1;
    protected ResourceSet resourceSet1;

    protected Resource resource2;
    protected EPackage ePackage2;
    protected ResourceSet resourceSet2;

    IncrementalEcoreValidator validator;
    Diagnostician diagnostician;
    protected EClass modelElement1, modelElement2, modelElement3, modelElement4;
    IncrementalEvlValidatorAdapter resultingAdapter;

    @Before
    public void setup() {
        System.out.println("Setup ModelPackage with Validator...");

        resourceSet1 = new ResourceSetImpl();
        resourceSet1.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new ResourceFactoryImpl());
        resource1 = resourceSet1.createResource(URI.createURI("foo.ecore1"));

        ePackage1 = EcoreFactory.eINSTANCE.createEPackage();
        ePackage1.setName("p");
        resource1.getContents().add(ePackage1);

        resourceSet2 = new ResourceSetImpl();
        resourceSet2.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new ResourceFactoryImpl());
        resource2 = resourceSet2.createResource(URI.createURI("foo.ecore2"));

        ePackage2 = EcoreFactory.eINSTANCE.createEPackage();
        ePackage2.setName("q");
        resource2.getContents().add(ePackage2);

        validator = new IncrementalEcoreValidator();
        validator.setConstraintsURI(new File("resources/ecoreAll.evl").toURI());
        
        EValidator.Registry.INSTANCE.put(EcorePackage.eINSTANCE, new EValidator.Descriptor() {
            public EValidator getEValidator() {
                return validator;
            }
        });
        EValidator.Registry.INSTANCE.put(GenModelPackage.eINSTANCE, new EValidator.Descriptor() {
            public EValidator getEValidator() {
                return validator;
            }
        });

        diagnostician = new Diagnostician(EValidator.Registry.INSTANCE);
    }
    
    @Test
    public void addTwoModelElementsAndChangeOne() {
    	System.out.println("\n\naddTwoModelElementsAndChangeOne");
    	
    	// BUILD
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        BuildTestModel.showEPackage(ePackage1);

        // VALIDATE
        diagnostician.validate(ePackage1);
        /*
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1, 
        			modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1, 
        			modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
        	.containsExactlyInAnyOrder(modelElement1, modelElement2);
        */
        showExecutionCache(ePackage1);
        
        // CHANGE
        modelElement2.setName("C3");
        //showExecutionCache(ePackage1);
        /*
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
        	.containsExactlyInAnyOrder(modelElement1);
        showAdapterNotifications(ePackage1);
        */
        
        //BuildTestModel.showEPackage(ePackage1);
        //showExecutionCache(ePackage1);
        

        // RE VALIDATE
        diagnostician.validate(ePackage1); 
        /*
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
    		.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1, 
    				modelElement2,modelElement2,modelElement2);
	    assertThat(modelObjectsFromConstraintTrace(ePackage1))
	    	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1, 
	    			modelElement2,modelElement2,modelElement2);
	    assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
	    	.containsExactlyInAnyOrder(modelElement1, modelElement2);
	    */
        //showExecutionCache(ePackage1);
    }
	
	
}