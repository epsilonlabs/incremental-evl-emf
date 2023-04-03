package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelFactory;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelPackage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.*;

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

        //diagnostician.validate(ePackage);
        //validator.setConstraintFile("ecore.evl");
    }

    @Test
    public void testValidationWithOneElementModel() {
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        //BuildTestModel.showEPackage(ePackage1);

        diagnostician.validate(ePackage1);
        //TestTools.showExecutionCache(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).containsExactly("C1", "C1", "C1");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).containsExactly("C1", "C1", "C1");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).containsExactly("C1");
    }

    @Test
    public void testCacheClearsOnNotificationPropertySet() {
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);        
        
        diagnostician.validate(ePackage1);
        //TestTools.showExecutionCache(ePackage1);        
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).containsExactly("C1", "C1", "C1");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).containsExactly("C1", "C1", "C1");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).containsExactly("C1");

        modelElement1.setName("C2");
        //TestTools.showExecutionCache(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).containsExactly();
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).containsExactly();
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).containsExactly();
        
        diagnostician.validate(ePackage1);
        //TestTools.showExecutionCache(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).containsExactly("C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).containsExactly("C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).containsExactly("C2");
        
        
    }

    @Test
    public void addTwoModelElementsAndChangeOne() {
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);

        diagnostician.validate(ePackage1);
        //TestTools.showExecutionCache(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).contains("C1", "C1", "C1", "C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).contains("C1", "C1", "C1", "C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains("C1","C2");
        
        modelElement2.setName("C3");
        //TestTools.showExecutionCache(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).containsExactly("C1", "C1", "C1");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).containsExactly("C1", "C1", "C1");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).containsExactly("C1");

        diagnostician.validate(ePackage1);
        //TestTools.showExecutionCache(ePackage1);        
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).contains("C1", "C1", "C1", "C3", "C3", "C3");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).contains("C1", "C1", "C1", "C3", "C3", "C3");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains("C1","C3");       
    }

    // TODO Add test for unsetting a feature
    @Test
    public void addOneModelElementAndUnsetOne() {
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
        assertThat(TestTools.getPackageNamesInConstraintTrace(eob)).contains("C1", "C1", "C1", "C3", "C3", "C3");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(eob)).contains("C1", "C1", "C1", "C3", "C3", "C3");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(eob)).contains("C1","C3");  
        */
        
        // CHANGE MODEL
        eob.unsetEditDirectory();        
        TestTools.showExecutionCache(eob);
        
        /*
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).contains("C1", "C1", "C1", "C3", "C3", "C3");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).contains("C1", "C1", "C1", "C3", "C3", "C3");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains("C1","C3");
        */
        
        // RE-VALIDATE MODEL
        diagnostician.validate(eob);

    }

    // TODO Add test for ePackage.ownedClassifiers.add(xyz) invalidating constraints that use ePackage.ownedClassifiers

    @Test
    public void addTwoModelElementsAndRemoveOne() {
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);

        diagnostician.validate(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).contains("C1", "C1", "C1", "C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).contains("C1", "C1", "C1", "C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains("C1","C2");
        
        ePackage1.getEClassifiers().remove(modelElement1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).containsExactly("C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).containsExactly("C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).containsExactly("C2");
                
        diagnostician.validate(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).containsExactly("C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).containsExactly("C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).containsExactly("C2");
    }

    @Test
    public void addTwoModelElementsAndClear(){
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);

        diagnostician.validate(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).contains("C1", "C1", "C1", "C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).contains("C1", "C1", "C1", "C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains("C1","C2");

        ePackage1.getEClassifiers().clear();
        //TestTools.showExecutionCache(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).containsExactly();
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).containsExactly();
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).containsExactly();
        
        diagnostician.validate(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).containsExactly();
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).containsExactly();
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).containsExactly();
    }


    @Test
    public void addTwoModelElementsAndMoveOne() {
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);
        
        diagnostician.validate(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).contains("C1", "C1", "C1", "C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).contains("C1", "C1", "C1", "C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains("C1","C2");

        // Move model elements about (swap C1 and C2 round) -- cache should clear C1 & C2
        ePackage1.getEClassifiers().move(modelElement1.getClassifierID(),modelElement2.getClassifierID());
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).contains();
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).contains();
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains();
        
        diagnostician.validate(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).contains("C1", "C1", "C1", "C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).contains("C1", "C1", "C1", "C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains("C1","C2");

    }

    @Test
    public void twoModelsAddElementInOneModelOtherModel() {
    	// BUILD 2 MODELS
    	modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);

        modelElement3 = BuildTestModel.createAndAddModelElementToePackage("D1", ePackage2);
        modelElement4 = BuildTestModel.createAndAddModelElementToePackage("D2", ePackage2);

        // VALIDATE 2 MODELS
        diagnostician.validate(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).contains("C1", "C1", "C1", "C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).contains("C1", "C1", "C1", "C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains("C1","C2");

        diagnostician.validate(ePackage2);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage2)).contains("D1", "D1", "D1", "D2", "D2", "D2");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage2)).contains("D1", "D1", "D1", "D2", "D2", "D2");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage2)).contains();
        
        // CHANGE THE MODELS
        BuildTestModel.addModelElementToePackage(modelElement1, ePackage2);
        //BuildTestModel.showEPackage(ePackage1);
        //BuildTestModel.showEPackage(ePackage2);                
        //TestTools.showExecutionCache(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).contains("C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).contains("C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains("C2");
        //TestTools.showExecutionCache(ePackage2);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage2)).contains("D1", "D1", "D1", "D2", "D2", "D2");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage2)).contains("D1", "D1", "D1", "D2", "D2", "D2");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage2)).contains();

        
        // RE-VALIDATE MODELS
        diagnostician.validate(ePackage1);
        //TestTools.showExecutionCache(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).contains("C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).contains("C2", "C2", "C2");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains("C2");
        
        diagnostician.validate(ePackage2);        
        //TestTools.showExecutionCache(ePackage2);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage2)).contains("D1", "D1", "D1", "D2", "D2", "D2", "C1", "C1", "C1");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage2)).contains("D1", "D1", "D1", "D2", "D2", "D2","C1", "C1", "C1");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage2)).contains("C1");
        
    }

    @Test
    public void createAndAddManyModelElements() {

    	// this test should be looking for the ADD_MANY condition?
    	
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C0",ePackage1);
        //BuildTestModel.showEPackage(ePackage1);

        diagnostician.validate(ePackage1);
        //TestTools.showExecutionCache(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).contains("C0", "C0", "C0" );
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).contains("C0", "C0", "C0");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains();

        // Grow the model with some more elements
        Collection<EClass> listOfModelElements = new ArrayList<EClass>();
        for(int i=1; i<4;i++) {
            listOfModelElements.add(BuildTestModel.createNamedModelElement("C"+i));
        }
        
        // MODEL CHANGE
        ePackage1.getEClassifiers().addAll(listOfModelElements);
        //BuildTestModel.showEPackage(ePackage1);

        diagnostician.validate(ePackage1);
        //TestTools.showExecutionCache(ePackage1);
        
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).contains("C0", "C0", "C0","C1", "C1", "C1", "C2", "C2", "C2", "C3", "C3", "C3");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).contains("C0", "C0", "C0","C1", "C1", "C1", "C2", "C2", "C2", "C3", "C3", "C3");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains("C1", "C2", "C3");

    }


    @Test
    public void createAndAddRemoveManyModelElements(){
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C0",ePackage1);       
        //BuildTestModel.showEPackage(ePackage1);
        
        // Model has 1 validated element        
        diagnostician.validate(ePackage1);
        //TestTools.showExecutionCache(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).contains("C0", "C0", "C0" );
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).contains("C0", "C0", "C0");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains();


        // Create C1, C2, C3 in an array to add to ePackage1
        Collection<EClass> listOfModelElements = new ArrayList<EClass>();
        for(int i=1; i<4;i++) {
            listOfModelElements.add(BuildTestModel.createNamedModelElement("C"+i));
        }

        // Add the list of elements to the model
        ePackage1.getEClassifiers().addAll(listOfModelElements);
        //BuildTestModel.showEPackage(ePackage1);
        
        // Model now has 4 validated elements
        diagnostician.validate(ePackage1);        
        TestTools.showExecutionCache(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).contains("C0", "C0", "C0","C1", "C1", "C1", "C2", "C2", "C2", "C3", "C3", "C3");
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).contains("C0", "C0", "C0","C1", "C1", "C1", "C2", "C2", "C2", "C3", "C3", "C3");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains("C1", "C2", "C3");

        // Remove the listed model elements
        ePackage1.getEClassifiers().removeAll(listOfModelElements);
        //BuildTestModel.showEPackage(ePackage1);

        // Model now has 1 validated element
        diagnostician.validate(ePackage1);
        //TestTools.showExecutionCache(ePackage1);
        assertThat(TestTools.getPackageNamesInConstraintTrace(ePackage1)).contains("C0", "C0", "C0" );
        assertThat(TestTools.getPackageNamesInPropertyAccesses(ePackage1)).contains("C0", "C0", "C0");
        assertThat(TestTools.getPackageNamesInUnsatisfiedConstraints(ePackage1)).contains();
    }

    /*
    @Test
    public void collectPropertyAccess() {
        // 1. Set up model

        // 2. Run EVL

        // 3. Check that constraint information is collected
        ConstraintExecutionCache cache = null;
        assertPropertyAccessCount(3, cache);
        assertPropertyAccessed(cache, element, property1);
        assertPropertyAccessed(cache, element, property2);
        assertPropertyAccessed(cache, element, property3);

        assertConstraintExecutionCount(3, cache);
        assertConstraintSatisfied(cache, element, "C1");
        assertConstraintSatisfied(cache, element, "C2");
        assertConstraintUnsatisfied(cache, element, "C3");
    }

    public static void assertPropertyAccessCount(int expectedCount, ConstraintExecutionCache cache) {
        // TODO assert that the number of property accesses in the cache is the expected one
    }

    @Test
    public void changedPropertyValueNoImpact() {
        // 1. Set up model

        // 2. Run EVL one time
        ConstraintExecutionCache firstTime = null;

        // 3. Make change
        EObject eToChange = null;

        // 4. Re-run EVL
        ConstraintExecutionCache secondTime = null;

        // 5. Check that no constraints were re-evaluated
        assertConstraintExecutionCount(0, secondTime, firstTime);

        assertConstraintSatisfied(secondTime, element, "C1");
        assertConstraintSatisfied(secondTime, element, "C2");
        assertConstraintUnsatisfied(secondTime, element, "C3");
    }

    @Test
    public void changedPropertyValueWithImpact() {
        // 1. Set up model

        // 2. Run EVL one time
        ConstraintExecutionCache firstTime = null;

        // 3. Make change
        EObject eToChange = null;

        // 4. Re-run EVL
        ConstraintExecutionCache secondTime = null;

        // 5. Check that no constraints were re-evaluated
        assertConstraintExecutionCount(2, secondTime, firstTime);
        assertConstraintWasExecuted(secondTime, firstTime, element, "C1");
        assertConstraintWasNotExecuted(secondTime, firstTime, element, "C2");
        assertConstraintWasExecuted(secondTime, firstTime, element, "C3");

        assertConstraintSatisfied(secondTime, element, "C1");
        assertConstraintSatisfied(secondTime, element, "C2");
        assertConstraintUnsatisfied(secondTime, element, "C3");
    }

    @Test
    public void newElement() {
        // TODO
    }

    @Test
    public void deletedElement() {
        // TODO
    }

    @Test
    public void movedElementToDifferentContainer() {
        // TODO
    }

    @Test
    public void reorderedElementWithinContainer() {
        // TODO
    }
*/
}
