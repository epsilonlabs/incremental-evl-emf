package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.io.File;
// Logging
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.Operation;
import org.eclipse.epsilon.evl.emf.validation.incremental.trace.TraceFactory;

public class IncrementalEcoreValidator extends IncrementalEvlValidator {

	private static final Logger LOGGER = Logger.getLogger(IncrementalEcoreValidator.class.getName());

	private java.net.URI constraintsURI;

	public void setConstraintsURI(java.net.URI uri) {
		this.constraintsURI = uri;
	}

	@Override
	public java.net.URI getConstraintsURI() {
		return constraintsURI;
	}

	public static final IncrementalEcoreValidator INSTANCE = new IncrementalEcoreValidator();

	public static void main(String[] args) {
		
		
		
		
		LOGGER.log(Level.FINEST, "working");
		System.out.print("GO!!!\n\n\n");

		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new ResourceFactoryImpl());
		Resource resource = resourceSet.createResource(URI.createURI("foo.ecore"));

		EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName("p");
		resource.getContents().add(ePackage);

		EClass c1 = EcoreFactory.eINSTANCE.createEClass();
		c1.setName("C1");
		ePackage.getEClassifiers().add(c1);

		IncrementalEcoreValidator validator = new IncrementalEcoreValidator();
		EValidator.Registry.INSTANCE.put(EcorePackage.eINSTANCE, new EValidator.Descriptor() {
			public EValidator getEValidator() {
				return validator;
			}
		});
		validator.setConstraintsURI(new File("resources/ecore.evl").toURI());

		Diagnostician diagnostician = new Diagnostician(EValidator.Registry.INSTANCE);
		diagnostician.validate(ePackage);

		EClass c2 = EcoreFactory.eINSTANCE.createEClass();
		c2.setName("C2");
		ePackage.getEClassifiers().add(c2);
		diagnostician.validate(ePackage);

		c1.setName("C3");
		diagnostician.validate(ePackage);

		c2.setName("C3");
		diagnostician.validate(ePackage);

		System.out.println("c1 Adapters: "+c1.eAdapters());
		System.out.println("c2 Adapters: "+c2.eAdapters());
		System.out.println("resourceSet adapter: " + resourceSet.eAdapters());
		System.out.println("resource adapter: " + resource.eAdapters());
		IncrementalEvlValidatorAdapter adapter =  (IncrementalEvlValidatorAdapter) resource.eAdapters().get(0);
		
		System.out.println(adapter.module.constraintExecutionCache.get().toString());
	}

}
