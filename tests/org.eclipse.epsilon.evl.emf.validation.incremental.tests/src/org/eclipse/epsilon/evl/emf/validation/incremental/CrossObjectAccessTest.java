package org.eclipse.epsilon.evl.emf.validation.incremental;

import static org.assertj.core.api.Assertions.assertThat;

import static org.eclipse.epsilon.evl.emf.validation.incremental.TestTools.accessesOf;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.junit.Before;
import org.junit.Test;

public class CrossObjectAccessTest {

    private ResourceSetImpl resourceSet;
	private Resource resource;
	private EPackage ePackage;
	private IncrementalEcoreValidator validator;
	private Diagnostician diagnostician;

	@Before
    public void setup() {
        resourceSet = new ResourceSetImpl();
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new ResourceFactoryImpl());
        resource = resourceSet.createResource(URI.createURI("foo.ecore1"));

        ePackage = EcoreFactory.eINSTANCE.createEPackage();
        ePackage.setName("p");
        resource.getContents().add(ePackage);

        validator = new IncrementalEcoreValidator();
        validator.setConstraintsURI(new File("resources/ecoreSuperclass.evl").toURI());
        
        EValidator.Registry.INSTANCE.put(EcorePackage.eINSTANCE, new EValidator.Descriptor() {
            public EValidator getEValidator() {
                return validator;
            }
        });

        diagnostician = new Diagnostician(EValidator.Registry.INSTANCE);
    }
	
	@Test
	public void changingSuperNameIsDetected() {
		// INITIAL RUN
		EClass baseClass = BuildTestModel.createAndAddModelElementToePackage("Manager", ePackage);
		EClass subClass = BuildTestModel.createAndAddModelElementToePackage("ConcreteManager", ePackage);
		subClass.getESuperTypes().add(baseClass);
		diagnostician.validate(ePackage);

		// 1 through the subclass rule execution
		assertThat(accessesOf(baseClass, "name")).size().isEqualTo(1);
		// 1 for the guard on the baseClass
		assertThat(accessesOf(baseClass, "eSuperTypes")).size().isEqualTo(1);
		// 1 through the subclass rule execution
		assertThat(accessesOf(subClass, "name")).size().isEqualTo(1);
		// 1 through the subclass guard + 1 through the subclass check
		assertThat(accessesOf(subClass, "eSuperTypes")).size().isEqualTo(2);

		// Only the subclass rule gets executed, and passes
		assertThat(TestTools.modelObjectsFromExecutionCacheConstraintTrace(ePackage))
			.containsExactlyInAnyOrder(subClass);
		assertThat(TestTools.modelObjectsFromExecutionCacheUnsatisfiedConstraints(ePackage))
			.isEmpty();

		// CHANGE
		baseClass.setName("SomethingElse");

		// Should have invalidated the base class name accesses
		assertThat(accessesOf(baseClass, "name")).isEmpty();

		// Should have invalidated the subclass rule execution
		assertThat(TestTools.modelObjectsFromExecutionCacheConstraintTrace(ePackage))
			.isEmpty();

		// REVALIDATION
		diagnostician.validate(ePackage);

		// Now the subclass shouldn't pass validation
		assertThat(TestTools.modelObjectsFromExecutionCacheConstraintTrace(ePackage))
			.containsExactlyInAnyOrder(subClass);
		assertThat(TestTools.modelObjectsFromExecutionCacheUnsatisfiedConstraints(ePackage))
			.containsExactly(subClass);
	}
}
