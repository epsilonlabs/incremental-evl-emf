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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IncrementalEvlTests {


    protected Resource resource1;
    protected EPackage ePackage1;
    protected ResourceSet resourceSet1;

    protected Resource resource2;
    protected EPackage ePackage2;
    protected ResourceSet resourceSet2;

    IncrementalEcoreValidator validator;
    Diagnostician diagnostician;
    protected EClass modelElement1, modelElement2;
    protected BuildTestModel buildTestModel = new BuildTestModel();
    IncrementalEvlValidatorAdapter resultingAdapter;

    @Before
    public void setUp() {
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
    public void testAOneElementModelExists() {
        buildTestModel.addModelElementToePackage("C1", ePackage1);
        assertEquals(1, ePackage1.getEClassifiers().size());
    }

    @Test
    public void testATwoElementModelExists() {
        buildTestModel.addModelElementToePackage("C1", ePackage1);
        buildTestModel.addModelElementToePackage("C2", ePackage1);
        assertEquals(2, ePackage1.getEClassifiers().size());
    }


    @Test
    public void testValidationWithOneElementModel() {
        buildTestModel.addModelElementToePackage("C1", ePackage1);

        diagnostician.validate(ePackage1);
        resultingAdapter = buildTestModel.getValidationAdapter(ePackage1);
        assertEquals(
                resultingAdapter.module.trace.propertyAccesses.size(),
                resultingAdapter.module.getContext().getConstraintTrace().getItems().size() );
    }

    @Test
    public void testCacheClearsOnNotificationPropertySet() {
        modelElement1 = buildTestModel.addModelElementToePackage("C1", ePackage1);
        diagnostician.validate(ePackage1);
        modelElement1.setName("C2");

        resultingAdapter = buildTestModel.getValidationAdapter(ePackage1);
        assertTrue(resultingAdapter.constraintExecutionCache.isPresent());
        assertEquals(0, resultingAdapter.constraintExecutionCache.get().constraintPropertyAccess.size());
        assertEquals(0, resultingAdapter.constraintExecutionCache.get().constraintTraceItems.size());
        assertEquals(0, resultingAdapter.constraintExecutionCache.get().unsatisfiedConstraints.size());
        diagnostician.validate(ePackage1);
    }

    @Test
    public void addTwoModelElementsAndRemoveOne() {
        modelElement1 = buildTestModel.addModelElementToePackage("C1", ePackage1);
        modelElement2 = buildTestModel.addModelElementToePackage("C2", ePackage1);
        diagnostician.validate(ePackage1);

        ePackage1.getEClassifiers().remove(modelElement1);

        resultingAdapter = buildTestModel.getValidationAdapter(ePackage1);
        assertEquals(
                resultingAdapter.module.getConstraints().size(),
                resultingAdapter.constraintExecutionCache.get().constraintPropertyAccess.size());
        assertEquals(
                resultingAdapter.module.getConstraints().size(),
                resultingAdapter.constraintExecutionCache.get().constraintTraceItems.size());
    }

    @Test
    public void addTwoModelElementsAndClear(){

        modelElement1 = buildTestModel.addModelElementToePackage("C1", ePackage1);
        modelElement2 = buildTestModel.addModelElementToePackage("C2", ePackage1);
        diagnostician.validate(ePackage1);

        ePackage1.getEClassifiers().clear();

        resultingAdapter = buildTestModel.getValidationAdapter(ePackage1);
        assertEquals(0, resultingAdapter.constraintExecutionCache.get().constraintPropertyAccess.size());
        assertEquals(0, resultingAdapter.constraintExecutionCache.get().constraintTraceItems.size());
        assertEquals(0, resultingAdapter.constraintExecutionCache.get().unsatisfiedConstraints.size());
    }
    
    @Test
    public void addTwoModelElementsAndMoveOne() {
        modelElement1 = buildTestModel.addModelElementToePackage("C1", ePackage1);
        modelElement2 = buildTestModel.addModelElementToePackage("C2", ePackage1);
        diagnostician.validate(ePackage1);

        System.out.println("before:" + ePackage1.getEClassifiers());
        ePackage1.getEClassifiers().move(modelElement1.getClassifierID(),modelElement2.getClassifierID());
        System.out.println("after:" + ePackage1.getEClassifiers());
        
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
