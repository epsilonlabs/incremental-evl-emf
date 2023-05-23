package org.eclipse.epsilon.evl.emf.validation.incremental;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.epsilon.evl.emf.validation.incremental.TestTools.modelObjectsFromConstraintPropertyAccess;
import static org.eclipse.epsilon.evl.emf.validation.incremental.TestTools.modelObjectsFromConstraintTrace;
import static org.eclipse.epsilon.evl.emf.validation.incremental.TestTools.modelObjectsFromUnsatisfiedConstraints;
import static org.eclipse.epsilon.evl.emf.validation.incremental.TestTools.showAdapterNotifications;
import static org.eclipse.epsilon.evl.emf.validation.incremental.TestTools.showExecutionCache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelFactory;
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
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.junit.Before;
import org.junit.Test;;

public class IncrementalEvlTests {


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
        validator.setConstraintsURI(new File("resources/ecore.evl").toURI());
        
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
    public void testValidationWithOneElementModel() {
    	System.out.println("\n\ntestValidationWithOneElementModel");
    	
    	// BUILD
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        //BuildTestModel.showEPackage(ePackage1);

        // VALIDATE
        diagnostician.validate(ePackage1);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
        	.contains(modelElement1);
        //showExecutionCache(ePackage1);
    }

    @Test
    public void testCacheClearsOnNotificationPropertySet() {
    	System.out.println("\n\ntestCacheClearsOnNotificationPropertySet");
    	
    	// BUILD
    	modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);        
        //BuildTestModel.showEPackage(ePackage1);
    	
    	// VALIDATE
        diagnostician.validate(ePackage1);        
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
        	.containsExactlyInAnyOrder(modelElement1);
        //showExecutionCache(ePackage1);

        // CHANGE
        modelElement1.setName("C2");
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
        	.isEmpty();        
        showAdapterNotifications(ePackage1);
        //BuildTestModel.showEPackage(ePackage1);
        //showExecutionCache(ePackage1);
        
    	// RE-VALIDATE
        diagnostician.validate(ePackage1);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
        	.containsExactlyInAnyOrder(modelElement1);
        //showExecutionCache(ePackage1);
        
        
    }

    @Test
    public void addTwoModelElementsAndChangeOne() {
    	System.out.println("\n\naddTwoModelElementsAndChangeOne");
    	
    	// BUILD
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);

        // VALIDATE
        diagnostician.validate(ePackage1);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1, 
        			modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1, 
        			modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
        	.containsExactlyInAnyOrder(modelElement1, modelElement2);
        //showExecutionCache(ePackage1);
        
        // CHANGE
        modelElement2.setName("C3");
        //showExecutionCache(ePackage1);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
        	.containsExactlyInAnyOrder(modelElement1);
        showAdapterNotifications(ePackage1);
        //BuildTestModel.showEPackage(ePackage1);
        //showExecutionCache(ePackage1);
        

        // RE VALIDATE
        diagnostician.validate(ePackage1);      
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
    		.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1, 
    				modelElement2,modelElement2,modelElement2);
	    assertThat(modelObjectsFromConstraintTrace(ePackage1))
	    	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1, 
	    			modelElement2,modelElement2,modelElement2);
	    assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
	    	.containsExactlyInAnyOrder(modelElement1, modelElement2);
        //showExecutionCache(ePackage1);
    }

    // TODO Add test for unsetting a feature
    @Test
    public void addOneModelElementAndUnsetOne() {
    	System.out.println("\n\naddOneModelElementAndUnsetOne");
    	
        // BUILD MODEL
    	ResourceSet rs = new ResourceSetImpl();
        Resource r = new ResourceImpl();
        rs.getResources().add(r);

        GenModel eob = GenModelFactory.eINSTANCE.createGenModel();
        r.getContents().add(eob);
        eob.setEditDirectory("something");
        
        // VALIDATE MODEL
        diagnostician.validate(eob);
        
        /*
        assertThat(getPackageNamesInConstraintTrace(eob)).contains("C1", "C1", "C1", "C3", "C3", "C3");
        assertThat(getPackageNamesInPropertyAccesses(eob)).contains("C1", "C1", "C1", "C3", "C3", "C3");
        assertThat(getPackageNamesInUnsatisfiedConstraints(eob)).contains("C1","C3");
        */
        
        // CHANGE MODEL
        eob.unsetEditDirectory();        
        showExecutionCache(eob);
        
        /*
        assertThat(getPackageNamesInConstraintTrace(ePackage1)).contains("C1", "C1", "C1", "C3", "C3", "C3");
        assertThat(getPackageNamesInPropertyAccesses(ePackage1)).contains("C1", "C1", "C1", "C3", "C3", "C3");
        assertThat(getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains("C1","C3");
        */
        
        // RE-VALIDATE MODEL
        diagnostician.validate(eob);

    }

    // TODO Add test for ePackage.ownedClassifiers.add(xyz) invalidating constraints that use ePackage.ownedClassifiers

    @Test
    public void addTwoModelElementsAndRemoveOne() {
    	System.out.println("\n\naddTwoModelElementsAndRemoveOne");
    	
    	// BUILD
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);
        //BuildTestModel.showEPackage(ePackage1);
        
        // VALIDATE
        diagnostician.validate(ePackage1);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
    		.contains(modelElement1,modelElement1,modelElement1, modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
    		.contains(modelElement1,modelElement1,modelElement1, modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
    		.contains(modelElement1, modelElement2);
        //showExecutionCache(ePackage1);
        
        // CHANGE
        ePackage1.getEClassifiers().remove(modelElement1);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
    		.contains(modelElement2,modelElement2,modelElement2);
	    assertThat(modelObjectsFromConstraintTrace(ePackage1))
	    	.contains(modelElement2,modelElement2,modelElement2);
	    assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
	    	.contains(modelElement2);
        showAdapterNotifications(ePackage1);
        //BuildTestModel.showEPackage(ePackage1);
        //showExecutionCache(ePackage1);
	    
	    // RE-VALIDATE
        diagnostician.validate(ePackage1);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
    		.contains(modelElement2,modelElement2,modelElement2);
	    assertThat(modelObjectsFromConstraintTrace(ePackage1))
	    	.contains(modelElement2,modelElement2,modelElement2);
	    assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
	    	.contains(modelElement2);
        //showExecutionCache(ePackage1);
    }

    @Test
    public void addTwoModelElementsAndClear(){
    	System.out.println("\n\naddTwoModelElementsAndClear");
    	
    	// BUILD
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);
        //BuildTestModel.showEPackage(ePackage1);
        
        // VALIDATE
        diagnostician.validate(ePackage1);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
    		.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1, 
    				modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
    		.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1, 
    				modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
    		.containsExactlyInAnyOrder(modelElement1, modelElement2);
        //showExecutionCache(ePackage1);

        // CHANGE
        ePackage1.getEClassifiers().clear();
        showExecutionCache(ePackage1);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
        	.isEmpty();
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
        	.isEmpty();
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
        	.isEmpty();
        showAdapterNotifications(ePackage1);
        //BuildTestModel.showEPackage(ePackage1);
        //showExecutionCache(ePackage1);
        
        // RE-VALIDATE
        diagnostician.validate(ePackage1);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
	    	.containsExactlyInAnyOrder(ePackage1);
	    assertThat(modelObjectsFromConstraintTrace(ePackage1))
	    	.containsExactlyInAnyOrder(ePackage1);
	    assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
	    	.containsExactlyInAnyOrder(ePackage1);
        //showExecutionCache(ePackage1);
    }


    @Test
    public void addTwoModelElementsAndMoveOne() {
    	System.out.println("\n\naddTwoModelElementsAndMoveOne");
    	
    	// MODEL
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);
        BuildTestModel.showEPackage(ePackage1);
        
        // VALIDATE
        diagnostician.validate(ePackage1);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
    		.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1, 
    				modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
    		.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1, 
    				modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
    		.containsExactlyInAnyOrder(modelElement1, modelElement2);
        showExecutionCache(ePackage1);

        // CHANGE
        // Move model elements about (swap C1 and C2 round) -- cache should clear C1 & C2
        ePackage1.getEClassifiers().move(0, 1);
        showAdapterNotifications(ePackage1);
        
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
    		.containsExactlyInAnyOrder(modelElement1,modelElement1,modelElement1, 
    				modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
    		.containsExactlyInAnyOrder(modelElement1,modelElement1,modelElement1, 
    				modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
    		.containsExactlyInAnyOrder(modelElement1, modelElement2);

        //BuildTestModel.showEPackage(ePackage1);
        //showExecutionCache(ePackage1);
        
        //RE-VALIDATE
        diagnostician.validate(ePackage1);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
    		.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1,
    				modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
    		.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1,
    				modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
    		.containsExactlyInAnyOrder(modelElement1, modelElement2);
        //showExecutionCache(ePackage1);
    }

    @Test
    public void twoModelsAddElementInOneModelOtherModel() {
    	System.out.println("\n\ntwoModelsAddElementInOneModelOtherModel");
    	
    	// BUILD -- 2 MODELS
    	modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);
        
        modelElement3 = BuildTestModel.createAndAddModelElementToePackage("D1", ePackage2);
        modelElement4 = BuildTestModel.createAndAddModelElementToePackage("D2", ePackage2);
        
        //BuildTestModel.showEPackage(ePackage1);
        //BuildTestModel.showEPackage(ePackage2);
        
        // VALIDATE -- 2 MODELS
        diagnostician.validate(ePackage1);
        diagnostician.validate(ePackage2);        
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
    		.contains(modelElement1,modelElement1,modelElement1, modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
    		.contains(modelElement1,modelElement1,modelElement1, modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
    		.contains(modelElement1, modelElement2);

        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage2))
    		.contains(modelElement3,modelElement3,modelElement3, modelElement4,modelElement4,modelElement4);
        assertThat(modelObjectsFromConstraintTrace(ePackage2))
    		.contains(modelElement3,modelElement3,modelElement3, modelElement4,modelElement4,modelElement4);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage2))
    		.isEmpty();

        //showExecutionCache(ePackage1);
        //showExecutionCache(ePackage2);
        
        // CHANGE -- 2 MODELS
        BuildTestModel.addModelElementToePackage(modelElement1, ePackage2);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
    		.contains(modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
    		.contains(modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
    		.contains(modelElement2);
        
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage2))
    		.contains(modelElement3,modelElement3,modelElement3, modelElement4,modelElement4,modelElement4);
        assertThat(modelObjectsFromConstraintTrace(ePackage2))
    		.contains(modelElement3,modelElement3,modelElement3, modelElement4,modelElement4,modelElement4);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage2))
    		.isEmpty();
        
        showAdapterNotifications(ePackage1);
        //BuildTestModel.showEPackage(ePackage1);
        //showExecutionCache(ePackage1);
        
        showAdapterNotifications(ePackage2);
        //BuildTestModel.showEPackage(ePackage2);
        //showExecutionCache(ePackage2);

        
        // RE-VALIDATE -- 2 MODELS
        diagnostician.validate(ePackage1);
        diagnostician.validate(ePackage2);  
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
    		.contains(modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
    		.contains(modelElement2,modelElement2,modelElement2);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
    		.contains(modelElement2);

        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage2))
    		.contains(modelElement1,modelElement1,modelElement1, modelElement3,modelElement3,modelElement3,
    				modelElement4,modelElement4,modelElement4);
        assertThat(modelObjectsFromConstraintTrace(ePackage2))
    		.contains(modelElement1,modelElement1,modelElement1, modelElement3,modelElement3,modelElement3,
    				modelElement4,modelElement4,modelElement4);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage2))
    		.contains(modelElement1);
        
        //showExecutionCache(ePackage1);
        //showExecutionCache(ePackage2);
        
    }

    @Test
    public void createAndAddManyModelElements() {
    	System.out.println("\n\ncreateAndAddManyModelElements");
    	// this test should be looking for the ADD_MANY condition?
    	
    	// BUILD
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C0",ePackage1);
        //BuildTestModel.showEPackage(ePackage1);

        // VALIDATE
        diagnostician.validate(ePackage1);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
	    	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1);
	    assertThat(modelObjectsFromConstraintTrace(ePackage1))
	    	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1);
	    assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
	    	.isEmpty();
        //showExecutionCache(ePackage1);

	    // CHANGE
        List<EClass> listOfModelElements = new ArrayList<EClass>();
        for(int i=1; i<4;i++) {
            listOfModelElements.add(BuildTestModel.createNamedModelElement("C"+i));
        }
        modelElement2 = listOfModelElements.get(0);
        modelElement3 = listOfModelElements.get(1);
        modelElement4 = listOfModelElements.get(2);
        ePackage1.getEClassifiers().addAll(listOfModelElements);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
	    	.containsExactlyInAnyOrder(modelElement1,modelElement1,modelElement1);
	    assertThat(modelObjectsFromConstraintTrace(ePackage1))
	    	.containsExactlyInAnyOrder(modelElement1,modelElement1,modelElement1);
	    assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
	    	.isEmpty();
        showAdapterNotifications(ePackage1);
        //BuildTestModel.showEPackage(ePackage1);
        //showExecutionCache(ePackage1);

	    // RE-VALIDATE
        diagnostician.validate(ePackage1); 
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
	    	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1, modelElement2,modelElement2,modelElement2,
	    			modelElement3,modelElement3,modelElement3, modelElement4,modelElement4,modelElement4);
	    assertThat(modelObjectsFromConstraintTrace(ePackage1))
	    	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1, modelElement2,modelElement2,modelElement2,
	    			modelElement3,modelElement3,modelElement3, modelElement4,modelElement4,modelElement4);
	    assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
	    	.containsExactlyInAnyOrder(modelElement2, modelElement3, modelElement4);
        //showExecutionCache(ePackage1);

    }

    @Test
    public void createAndAddRemoveManyModelElements(){
    	System.out.println("\n\ncreateAndAddRemoveManyModelElements");
    	
    	// BUILD
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C0",ePackage1);       
        //BuildTestModel.showEPackage(ePackage1);
        
        // VALIDATE        
        diagnostician.validate(ePackage1);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
	    	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1);
	    assertThat(modelObjectsFromConstraintTrace(ePackage1))
	    	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1);
	    assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
	    	.isEmpty();
        //showExecutionCache(ePackage1);

        // CHANGE 1 -- ADD_MANY
        List<EClass> listOfModelElements = new ArrayList<>();
        for(int i=1; i<4;i++) {
            listOfModelElements.add(BuildTestModel.createNamedModelElement("C"+i));
        }
        modelElement2 = listOfModelElements.get(0);
        modelElement3 = listOfModelElements.get(1);
        modelElement4 = listOfModelElements.get(2);
        ePackage1.getEClassifiers().addAll(listOfModelElements);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
	    	.containsExactlyInAnyOrder(modelElement1,modelElement1,modelElement1);
	    assertThat(modelObjectsFromConstraintTrace(ePackage1))
	    	.containsExactlyInAnyOrder(modelElement1,modelElement1,modelElement1);
	    assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
	    	.isEmpty();
        showAdapterNotifications(ePackage1);
        //BuildTestModel.showEPackage(ePackage1);
        //showExecutionCache(ePackage1);
        
        // RE-VALIDATE
        diagnostician.validate(ePackage1);        
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1, modelElement2,modelElement2,modelElement2,
        			modelElement3,modelElement3,modelElement3, modelElement4,modelElement4,modelElement4);
        assertThat(modelObjectsFromConstraintTrace(ePackage1))
        	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1, modelElement2,modelElement2,modelElement2,
        			modelElement3,modelElement3,modelElement3, modelElement4,modelElement4,modelElement4);
        assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
        	.containsExactlyInAnyOrder(modelElement2, modelElement3, modelElement4);
        //showExecutionCache(ePackage1);
        
        // CHANGE 2 -- REMOVE_MANY
        ePackage1.getEClassifiers().removeAll(listOfModelElements);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
	    	.containsExactlyInAnyOrder(modelElement1,modelElement1,modelElement1);
	    assertThat(modelObjectsFromConstraintTrace(ePackage1))
	    	.containsExactlyInAnyOrder(modelElement1,modelElement1,modelElement1);
	    assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
	    	.isEmpty();
        showAdapterNotifications(ePackage1);
        //BuildTestModel.showEPackage(ePackage1);
        //showExecutionCache(ePackage1);

        // RE-VALIDATE
        diagnostician.validate(ePackage1);
        assertThat(modelObjectsFromConstraintPropertyAccess(ePackage1))
	    	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1);
	    assertThat(modelObjectsFromConstraintTrace(ePackage1))
	    	.containsExactlyInAnyOrder(ePackage1, modelElement1,modelElement1,modelElement1);
	    assertThat(modelObjectsFromUnsatisfiedConstraints(ePackage1))
	    	.isEmpty();
	    //showExecutionCache(ePackage1);
    }


}
