package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.emf.ecore.EObject;
import org.junit.Before;
import org.junit.Test;

public class IncrementalEvlTests {
    @Before
    public void setUp() {

    }

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

}
