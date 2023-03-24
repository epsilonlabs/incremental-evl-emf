package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

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

        diagnostician = new Diagnostician(EValidator.Registry.INSTANCE);

        //diagnostician.validate(ePackage);
        //validator.setConstraintFile("ecore.evl");
    }

    @Test
    public void testValidationWithOneElementModel() {
        BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        BuildTestModel.showEPackage(ePackage1);

        diagnostician.validate(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        assertEquals(3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals(3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals(1, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));
    }

    @Test
    public void testCacheClearsOnNotificationPropertySet() {
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        diagnostician.validate(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        assertEquals(3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals(3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals(1, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        modelElement1.setName("C2");
        TestTools.showExecutionCache(ePackage1);
        assertEquals("After changing the name, the property access trace should remain empty", 0, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("After changing the name, the execution cache should remain empty", 0, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("After changing the name, the execution cache should not report any unsatisfied constraints yet", 0, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        diagnostician.validate(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        assertEquals("After rerunning the validation, the property access trace should be populated again!", 3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals(3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals(1, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));
    }

    // TODO Add test where out of two elements, only one gets updated (only that one should be re-validated)

    // TODO Add test for unsetting a feature

    // TODO Add test for ePackage.ownedClassifiers.add(xyz) invalidating constraints that use ePackage.ownedClassifiers

    @Test
    public void addTwoModelElementsAndRemoveOne() {
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);

        diagnostician.validate(ePackage1);
        assertEquals(6, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals(6, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals(2, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        ePackage1.getEClassifiers().remove(modelElement1);
        assertEquals(3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals(3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals(1, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));
    }

    @Test
    public void addTwoModelElementsAndClear(){
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);

        diagnostician.validate(ePackage1);
        assertEquals(2, TestTools.getModelSize(ePackage1));
        assertEquals(6, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals(6, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals(2, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        ePackage1.getEClassifiers().clear();
        assertEquals(0, TestTools.getModelSize(ePackage1));
        assertEquals(0, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals(0, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals(0, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));
    }


    @Test
    public void addTwoModelElementsAndMoveOne() {
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);
        diagnostician.validate(ePackage1);

        assertEquals(2, TestTools.getModelSize(ePackage1));
        assertEquals(6, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals(6, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals(2, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        System.out.println("before:"  );
        BuildTestModel.showEPackage(ePackage1);

        ePackage1.getEClassifiers().move(modelElement1.getClassifierID(),modelElement2.getClassifierID());

        System.out.println("after:");
        BuildTestModel.showEPackage(ePackage1);

        assertEquals(0, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals(0, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals(0, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));
    }

    @Test
    public void twoModelsAddElementInOneModelOtherModel() {
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);

        modelElement3 = BuildTestModel.createAndAddModelElementToePackage("D1", ePackage2);
        modelElement4 = BuildTestModel.createAndAddModelElementToePackage("D2", ePackage2);

        diagnostician.validate(ePackage1);

        // assertEquals((("C1", "name"), ("C1", "x"), ...), TestTools.getAccesses(ePackage1))

        assertEquals(6, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals(6, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals(2, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        diagnostician.validate(ePackage2);
        assertEquals(6, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage2));
        assertEquals(6, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage2));
        assertEquals(0, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage2));

        BuildTestModel.addModelElementToePackage(modelElement1, ePackage2);

        // TODO add messages explaining where these expectations are coming from
        TestTools.showExecutionCache(ePackage1);
        assertEquals(3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals(3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals(1, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        TestTools.showExecutionCache(ePackage2);
        assertEquals(6, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage2));
        assertEquals(6, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage2));
        assertEquals(0, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage2));

        BuildTestModel.showEPackage(ePackage1);
        BuildTestModel.showEPackage(ePackage2);
        //ePackage1.getEClassifiers().move(modelElement2.getClassifierID(),modelElement4);
        //buildTestModel.showEPackage(ePackage1);
    }
    @Test
    public void createAndAddManyModelElements() {

        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C0",ePackage1);
        BuildTestModel.showEPackage(ePackage1);

        diagnostician.validate(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        assertEquals(1, TestTools.getModelSize(ePackage1));

        // Grow the model with some more elements
        Collection<EClass> listOfModelElements = new ArrayList<EClass>();
        for(int i=1; i<4;i++) {
            listOfModelElements.add(BuildTestModel.createNamedModelElement("C"+i));
        }
        ePackage1.getEClassifiers().addAll(listOfModelElements);
        BuildTestModel.showEPackage(ePackage1);

        diagnostician.validate(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        assertEquals(4,TestTools.getModelSize(ePackage1));

    }


    @Test
    public void createAndAddRemoveManyModelElements(){

        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C0",ePackage1);
        diagnostician.validate(ePackage1);
        BuildTestModel.showEPackage(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        // Model has 1 validated element
        assertEquals(1, TestTools.getModelSize(ePackage1));
        assertEquals("C0", TestTools.getModelElementName(ePackage1, 0));
        assertEquals(3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals(3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals(0, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));
        assertTrue("checkExecutionCacheConstraintTraceItemsForModelElementName",TestTools.checkExecutionCacheConstraintTraceItemsForModelElementName(ePackage1,"C0"));
        assertTrue("checkExecutionCacheConstraintPropertyAccessForModelElementName",TestTools.checkExecutionCacheConstraintPropertyAccessForModelElementName(ePackage1,"C0"));


        // Create C1, C2, C3 in an array to load into the ePackage
        Collection<EClass> listOfModelElements = new ArrayList<EClass>();
        for(int i=1; i<4;i++) {
            listOfModelElements.add(BuildTestModel.createNamedModelElement("C"+i));
        }


        ePackage1.getEClassifiers().addAll(listOfModelElements);
        diagnostician.validate(ePackage1);
        BuildTestModel.showEPackage(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        // Model now has 4 validated elements
        assertEquals(4, TestTools.getModelSize(ePackage1));
        assertEquals("C0", TestTools.getModelElementName(ePackage1, 0));
        assertEquals("C1", TestTools.getModelElementName(ePackage1, 1));
        assertEquals("C2", TestTools.getModelElementName(ePackage1, 2));
        assertEquals("C3", TestTools.getModelElementName(ePackage1, 3));
        assertTrue(TestTools.checkConstraintExecutionCacheUnsatisfiedConstraintForModelElementName(ePackage1,"C1"));
        assertTrue(TestTools.checkConstraintExecutionCacheUnsatisfiedConstraintForModelElementName(ePackage1,"C2"));
        assertTrue(TestTools.checkConstraintExecutionCacheUnsatisfiedConstraintForModelElementName(ePackage1,"C3"));


        ePackage1.getEClassifiers().removeAll(listOfModelElements);
        diagnostician.validate(ePackage1);
        BuildTestModel.showEPackage(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        // Model now has 1 validated element
        assertEquals(1, TestTools.getModelSize(ePackage1));
        assertEquals("C0", TestTools.getModelElementName(ePackage1, 0));
        assertFalse(TestTools.checkConstraintExecutionCacheUnsatisfiedConstraintForModelElementName(ePackage1,"C1"));
        assertFalse(TestTools.checkConstraintExecutionCacheUnsatisfiedConstraintForModelElementName(ePackage1,"C2"));
        assertFalse(TestTools.checkConstraintExecutionCacheUnsatisfiedConstraintForModelElementName(ePackage1,"C3"));



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
