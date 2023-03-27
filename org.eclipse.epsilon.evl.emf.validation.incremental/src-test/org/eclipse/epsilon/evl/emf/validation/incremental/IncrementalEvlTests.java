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
        assertEquals("Property Accesses should equal model elements * constraints (3)",
                3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("Trace Items should equal model elements * constraints (3)",
                3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("Unsatisfied Constraints should equal model constraints failed (1)",
                1, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));
    }

    @Test
    public void testCacheClearsOnNotificationPropertySet() {
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        diagnostician.validate(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        assertEquals("Property Accesses should equal model elements * constraints (3)",
                3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("Trace Items should equal model elements * constraints (3)",
                3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("Unsatisfied Constraints should equal model constraints failed (1)",
                1, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        modelElement1.setName("C2");
        TestTools.showExecutionCache(ePackage1);
        assertEquals("After changing the name, Property Accesses should reduce by model elements changed * constraints (0)",
                0, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("After changing the name, Trace Items should reduce by model elements changed * constraints (0)",
                0, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("After changing the name, Unsatisfied Constraints relating to the changed model element are removed (0)",
                0, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        diagnostician.validate(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        assertEquals("After rerunning the validation, Property Accesses should equal model elements * constraints (3)",
                3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("After rerunning the validation, Trace Items should equal model elements * constraints (3)",
                3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("After rerunning the validation, Unsatisfied Constraints should equal model constraints failed (1)",
                1, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));
    }

    @Test
    public void addTwoModelElementsAndChangeOne() {

        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);

        diagnostician.validate(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        assertEquals("Property Accesses should equal model elements * constraints (6)",
                6, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("Trace Items should equal model elements * constraints (6)",
                6, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("Unsatisfied Constraints should equal model constraints failed (2)",
                2, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        modelElement2.setName("C3");
        TestTools.showExecutionCache(ePackage1);
        assertEquals("After changing the name, Property Accesses should reduce by model elements changed * constraints (3)",
                3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("After changing the name, Trace Items should reduce by model elements changed * constraints (3)",
                3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("After changing the name, Unsatisfied Constraints relating to the changed model element are removed (1)",
                1, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        diagnostician.validate(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        assertEquals("After rerunning the validation, Property Accesses should equal model elements * constraints (6)",
                6, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("After rerunning the validation, Trace Items should equal model elements * constraints (6)",
                6, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("After rerunning the validation, Unsatisfied Constraints should equal model constraints failed (2)",
                2, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));
    }

    // TODO Add test for unsetting a feature
    @Test
    public void addOneModelElementAndUnsetOne() {
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        diagnostician.validate(ePackage1);
        TestTools.showExecutionCache(ePackage1);
//        assertEquals("Property Accesses should equal model elements * constraints (3)", 3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
//        assertEquals("Trace Items should equal model elements * constraints (3)", 3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
//        assertEquals("Unsatisfied Constraints should equal model constraints failed (1)",1, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        // modelElement1=null;  // does not generate "unSet"
        //ePackage1.eUnset(ePackage1.eContainingFeature());
        //TestTools.showExecutionCache(ePackage1);
//        assertEquals("After changing the name, Property Accesses should reduce by model elements changed * constraints (0)", 0, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
//        assertEquals("After changing the name, Trace Items should reduce by model elements changed * constraints (0)", 0, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
//        assertEquals("After changing the name, Unsatisfied Constraints relating to the changed model element are removed (0)", 0, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

//        diagnostician.validate(ePackage1);
//        TestTools.showExecutionCache(ePackage1);
//        assertEquals("After rerunning the validation, Property Accesses should equal model elements * constraints (3)", 3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
//        assertEquals("After rerunning the validation, Trace Items should equal model elements * constraints (3)", 3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
//        assertEquals("After rerunning the validation, Unsatisfied Constraints should equal model constraints failed (1)", 1, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));
    }

    // TODO Add test for ePackage.ownedClassifiers.add(xyz) invalidating constraints that use ePackage.ownedClassifiers

    @Test
    public void addTwoModelElementsAndRemoveOne() {
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);

        diagnostician.validate(ePackage1);
        assertEquals("Property Accesses should equal model elements * constraints (6)",
                6, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("Trace Items should equal model elements * constraints (6)",
                6, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("Unsatisfied Constraints should equal model constraints failed (2)",
                2, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        ePackage1.getEClassifiers().remove(modelElement1);
        assertEquals("After removing one model element, Property Accesses should reduce by model elements changed * constraints (3)",
                3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("After removing one model element, Trace Items should reduce by model elements changed * constraints (3)",
                3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("After removing one model element, Unsatisfied Constraints relating to the changed model element are removed (1)",
                1, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));
    }

    @Test
    public void addTwoModelElementsAndClear(){
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);

        diagnostician.validate(ePackage1);
        assertEquals("Model should contain 2 elements, something wrong in EMF?",
                2, TestTools.getModelSize(ePackage1));
        assertEquals("Property Accesses should equal model elements * constraints (6)",
                6, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("Trace Items should equal model elements * constraints (6)",
                6, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("Unsatisfied Constraints should equal model constraints failed (2)",
                2, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        ePackage1.getEClassifiers().clear();
        assertEquals("Model has been cleared there should be 0 elements, something wrong in EMF?",
                0, TestTools.getModelSize(ePackage1));
        assertEquals("After clearing a model, Property Accesses should reduce by model elements changed * constraints (0)",
                0, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("After clearing a model, Trace Items should reduce by model elements changed * constraints (0)",
                0, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("After clearing a model, Unsatisfied Constraints relating to the changed model element are removed (0)",
                0, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));
    }


    @Test
    public void addTwoModelElementsAndMoveOne() {
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);
        diagnostician.validate(ePackage1);

        assertEquals("Model should contain 2 elements, something wrong in EMF?",
                2, TestTools.getModelSize(ePackage1));
        assertEquals("Property Accesses should equal model elements * constraints (6)",
                6, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("Trace Items should equal model elements * constraints (6)",
                6, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("Unsatisfied Constraints should equal model constraints failed (2)",
                2, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        System.out.println("before:"  );
        BuildTestModel.showEPackage(ePackage1);

        // Move model elements about (swap C1 and C2 round)
        ePackage1.getEClassifiers().move(modelElement1.getClassifierID(),modelElement2.getClassifierID());

        System.out.println("after:");
        BuildTestModel.showEPackage(ePackage1);

        assertEquals("After swapping two model elements, Property Accesses should reduce by model elements changed * constraints (0)",
                0, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("After swapping two model elements, Trace Items should reduce by model elements changed * constraints (0)",
                0, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("After swapping two model elements, Unsatisfied Constraints relating to the changed model element are removed (0)",
                0, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));
    }

    @Test
    public void twoModelsAddElementInOneModelOtherModel() {
        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C1", ePackage1);
        modelElement2 = BuildTestModel.createAndAddModelElementToePackage("C2", ePackage1);

        modelElement3 = BuildTestModel.createAndAddModelElementToePackage("D1", ePackage2);
        modelElement4 = BuildTestModel.createAndAddModelElementToePackage("D2", ePackage2);

        diagnostician.validate(ePackage1);

        // assertEquals((("C1", "name"), ("C1", "x"), ...), TestTools.getAccesses(ePackage1))

        assertEquals("Property Accesses should equal model elements * constraints (6)",
                6, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("Trace Items should equal model elements * constraints (6)",
                6, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("Unsatisfied Constraints should equal model constraints failed (2)",
                2, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        diagnostician.validate(ePackage2);
        assertEquals("Property Accesses should equal model elements * constraints (6)",
                6, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage2));
        assertEquals("Trace Items should equal model elements * constraints (6)",
                6, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage2));
        assertEquals("Unsatisfied Constraints should equal model constraints failed (0)",
                0, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage2));

        BuildTestModel.addModelElementToePackage(modelElement1, ePackage2);

        TestTools.showExecutionCache(ePackage1);
        assertEquals("After moving a model element, Property Accesses should reduce by model elements changed * constraints (3)",
                3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("After moving a model element, Trace Items should reduce by model elements changed * constraints (3)",
                3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("After moving a model element, Unsatisfied Constraints relating to the changed model element are removed (1)",
                1, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        TestTools.showExecutionCache(ePackage2);
        assertEquals("After moving a model element, Property Accesses should reduce by model elements changed * constraints (3)",
                6, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage2));
        assertEquals("After moving a model element, Trace Items should reduce by model elements changed * constraints (3)",
                6, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage2));
        assertEquals("After moving a model element, Unsatisfied Constraints relating to the changed model element are removed (1)",
                0, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage2));

        BuildTestModel.showEPackage(ePackage1);
        BuildTestModel.showEPackage(ePackage2);

        diagnostician.validate(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        assertEquals("After revalidating, Property Accesses should equal model elements * constraints (3)",
                3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("After revalidating, Trace Items should reduce by model elements changed * constraints (3)",
                3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("After revalidating, Unsatisfied Constraints should equal model constraints failed  (1)",
                1, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        diagnostician.validate(ePackage2);
        TestTools.showExecutionCache(ePackage2);
        assertEquals("After revalidating, Property Accesses should equal model elements * constraints (9)",
                9, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage2));
        assertEquals("After revalidating, Trace Items should reduce by model elements changed * constraints (9)",
                9, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage2));
        assertEquals("After revalidating, Unsatisfied Constraints should equal model constraints failed  (1)",
                1, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage2));

        //ePackage1.getEClassifiers().move(modelElement2.getClassifierID(),modelElement4);
        //buildTestModel.showEPackage(ePackage1);
    }
    @Test
    public void createAndAddManyModelElements() {

        modelElement1 = BuildTestModel.createAndAddModelElementToePackage("C0",ePackage1);
        BuildTestModel.showEPackage(ePackage1);

        diagnostician.validate(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        assertEquals("Model should contain 1 element, something wrong in EMF?",
                1, TestTools.getModelSize(ePackage1));
        assertEquals("Property Accesses should equal model elements * constraints (3)",
                3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("Trace Items should equal model elements * constraints (3)",
                3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("Unsatisfied Constraints should equal model constraints failed (0)",
                0, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

        // Grow the model with some more elements
        Collection<EClass> listOfModelElements = new ArrayList<EClass>();
        for(int i=1; i<4;i++) {
            listOfModelElements.add(BuildTestModel.createNamedModelElement("C"+i));
        }
        ePackage1.getEClassifiers().addAll(listOfModelElements);
        BuildTestModel.showEPackage(ePackage1);

        diagnostician.validate(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        assertEquals("Model should contain 4 element, something wrong in EMF?",
                4,TestTools.getModelSize(ePackage1));
        assertEquals("Property Accesses should equal model elements * constraints (12)",
                12, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("Trace Items should equal model elements * constraints (12)",
                12, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("Unsatisfied Constraints should equal model constraints failed (3)",
                3, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));

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
        assertEquals("Property Accesses should equal model elements * constraints (3)",
                3, TestTools.getExecutionCacheConstrainPropertyAccessSize(ePackage1));
        assertEquals("Trace Items should equal model elements * constraints (3)",
                3, TestTools.getExecutionCacheConstraintTraceItemSize(ePackage1));
        assertEquals("Unsatisfied Constraints should equal model constraints failed (1)",
                0, TestTools.getExecutionCacheUnsatisfiedConstraintsSize(ePackage1));
        assertTrue("checkExecutionCacheConstraintTraceItemsForModelElementName",
                TestTools.checkExecutionCacheConstraintTraceItemsForModelElementName(ePackage1,"C0"));
        assertTrue("checkExecutionCacheConstraintPropertyAccessForModelElementName",
                TestTools.checkExecutionCacheConstraintPropertyAccessForModelElementName(ePackage1,"C0"));


        // Create C1, C2, C3 in an array to load into the ePackage
        Collection<EClass> listOfModelElements = new ArrayList<EClass>();
        for(int i=1; i<4;i++) {
            listOfModelElements.add(BuildTestModel.createNamedModelElement("C"+i));
        }

        // Add the list of elements to the model
        ePackage1.getEClassifiers().addAll(listOfModelElements);

        diagnostician.validate(ePackage1);
        BuildTestModel.showEPackage(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        // Model now has 4 validated elements
        assertEquals("Model should have 4 elements",
                4, TestTools.getModelSize(ePackage1));

        assertTrue("Model should contain an element C0",
                TestTools.checkModelContainsElementName(ePackage1, "C0"));
        assertTrue("Model should contain an element C1",
                TestTools.checkModelContainsElementName(ePackage1, "C1"));
        assertTrue("Model should contain an element C2",
                TestTools.checkModelContainsElementName(ePackage1, "C2"));
        assertTrue("Model should contain an element C3",
                TestTools.checkModelContainsElementName(ePackage1, "C3"));

        assertFalse("Cache Unsatisfied Constraint should NOT contain C0",
                TestTools.checkConstraintExecutionCacheUnsatisfiedConstraintForModelElementName(ePackage1, "C0"));
        assertTrue( "Cache Unsatisfied Constraint should contain C1",
                TestTools.checkConstraintExecutionCacheUnsatisfiedConstraintForModelElementName(ePackage1,"C1"));
        assertTrue( "Cache Unsatisfied Constraint should contain C2",
                TestTools.checkConstraintExecutionCacheUnsatisfiedConstraintForModelElementName(ePackage1,"C2"));
        assertTrue("Cache Unsatisfied Constraint should contain C3",
                TestTools.checkConstraintExecutionCacheUnsatisfiedConstraintForModelElementName(ePackage1,"C3"));

        // Remove the listed model elements
        ePackage1.getEClassifiers().removeAll(listOfModelElements);

        assertTrue("Cache Property Access should contain an element C0",
                TestTools.checkExecutionCacheConstraintPropertyAccessForModelElementName(ePackage1, "C0"));
        assertFalse("Cache Property Access should NOT contain an element C1",
                TestTools.checkExecutionCacheConstraintPropertyAccessForModelElementName(ePackage1, "C1"));
        assertFalse("Cache Property Access should NOT contain an element C2",
                TestTools.checkExecutionCacheConstraintPropertyAccessForModelElementName(ePackage1, "C2"));
        assertFalse("Cache Property Access should NOT contain an element C3",
                TestTools.checkExecutionCacheConstraintPropertyAccessForModelElementName(ePackage1, "C3"));



        diagnostician.validate(ePackage1);
        BuildTestModel.showEPackage(ePackage1);
        TestTools.showExecutionCache(ePackage1);
        // Model now has 1 validated element
        assertEquals(1, TestTools.getModelSize(ePackage1));
        assertEquals("Model should contain an element C0",
                "C0", TestTools.getModelElementName(ePackage1, 0));
        assertFalse("Model should NOT contain an element C1",
                TestTools.checkConstraintExecutionCacheUnsatisfiedConstraintForModelElementName(ePackage1,"C1"));
        assertFalse("Model should NOT contain an element C2",
                TestTools.checkConstraintExecutionCacheUnsatisfiedConstraintForModelElementName(ePackage1,"C2"));
        assertFalse("Model should NOT contain an element C3",
                TestTools.checkConstraintExecutionCacheUnsatisfiedConstraintForModelElementName(ePackage1,"C3"));



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
