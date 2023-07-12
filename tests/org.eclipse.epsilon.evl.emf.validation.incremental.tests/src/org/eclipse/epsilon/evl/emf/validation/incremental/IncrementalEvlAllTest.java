package org.eclipse.epsilon.evl.emf.validation.incremental;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Effort;
import org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Person;
import org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Project;
import org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.PslFactory;
import org.eclipse.epsilon.evl.emf.validation.incremental.tests.metamodels.psl.Task;
import org.junit.Before;
import org.junit.Test;

public class IncrementalEvlAllTest {
	
	protected Resource resource;
    protected ResourceSet resourceSet;
	private IncrementalEcoreValidator validator;
	
	private final PslFactory factory = PslFactory.eINSTANCE;

    @Before
    public void setup() {
        resourceSet = new ResourceSetImpl();
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new ResourceFactoryImpl());
        resource = resourceSet.createResource(URI.createURI("foo.xmi"));

        validator = new IncrementalEcoreValidator();
        validator.setConstraintsURI(new File("resources/projects.evl").toURI());
    }

    @Test
	public void addEffortToProject() {
    	System.out.println("\n\naddEffortToProject()\n");
    	boolean validationResult;
		// An empty Project should be valid
		Project project = factory.createProject();
		resource.getContents().add(project);

		// Create a task with an effort assigned to a person
		Person person = factory.createPerson();
		project.getPeople().add(person);

		// Initially, should NOT be valid (not assigned to anything)
		assertFalse(validator.validate(person.eClass(), person, null, null));
		
		// Now we add a task and assign it to this person
		Task task = factory.createTask();
		project.getTasks().add(task);  // Notification feature tasks on model root

		Effort effort = factory.createEffort();
		effort.setPerson(person);
		task.getEffort().add(effort);  // Notification Feature effort on element Task

		// Revalidate: Person should become valid now
		assertTrue(validator.validate(person.eClass(), person, null, null));
	}

    @Test
    public void removeEffortFromProject() {
    	System.out.println("\n\nremoveEffortFromProject()\n");
    	
		// An empty Project should be valid
		Project project = factory.createProject();
		resource.getContents().add(project);

		// Create a task with an effort assigned to a person
		Person person = factory.createPerson();
		project.getPeople().add(person);

		Task task = factory.createTask();
		project.getTasks().add(task);

		Effort effort = factory.createEffort();
		effort.setPerson(person);
		task.getEffort().add(effort);

		// Validate: Person should become valid now
		assertTrue(validator.validate(person.eClass(), person, null, null));
				
		// Remove the person from the effort
		effort.setPerson(null); // Notification on Feature person on element tasks/effort 
								// Causes a notification for the person that removes result from Cache, does not trigger notification for "effort"
		
		// Initially, should NOT be valid (not assigned to anything)
		assertFalse(validator.validate(person.eClass(), person, null, null));
    	
    }
	
	
	// move within model (tree with A as root, B and C as children of A, and then you move C to become a child of B)
	public void moveModelElementBetweenModels() {}
	
	
	
}