package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.net.URISyntaxException;

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

import java.io.IOException;
// Logging
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.util.logging.*;

public class IncrementalEcoreValidator extends IncrementalEvlValidator {

	private static MyLog MYLOG = MyLog.getMyLog();
	public static Logger MYLOGGER = MYLOG.getMyLogger();

	public static final IncrementalEcoreValidator INSTANCE = new IncrementalEcoreValidator();

	public static void main(String[] args) {
		MYLOGGER.log(Level.FINEST, "working");

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

		Diagnostician diagnostician = new Diagnostician(EValidator.Registry.INSTANCE);
		diagnostician.validate(ePackage);

		//c1.setName("C1");
		//addElement("C2",ePackage);
		//diagnostician.validate(ePackage);

		c1.setName("C1");
		diagnostician.validate(ePackage);

		/*
		c1.setName("C3");

		diagnostician.validate(ePackage);

		EClass c2 = EcoreFactory.eINSTANCE.createEClass();
		c2.setName("C2");
		ePackage.getEClassifiers().add(c2);

		diagnostician.validate(ePackage);

		 */

	}

	private static void addElement(String name, EPackage ePackage) {
		EClass temp = EcoreFactory.eINSTANCE.createEClass();
		temp.setName(name);
		ePackage.getEClassifiers().add(temp);
	}

	@Override
	protected java.net.URI getConstraints() {
		try {
			return IncrementalEcoreValidator.class.getResource("ecore.evl").toURI();
		} catch (URISyntaxException e) {
			return null;
		}
	}

}
