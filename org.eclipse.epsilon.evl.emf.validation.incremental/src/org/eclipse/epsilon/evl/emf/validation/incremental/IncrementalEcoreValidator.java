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
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogManager;
import java.util.Date;
import java.util.logging.*;

public class IncrementalEcoreValidator extends IncrementalEvlValidator {

	private static Logger LOGGER = Logger.getLogger(IncrementalEcoreValidator.class.getName());
	static {
		ConsoleHandler consoleHandler = new ConsoleHandler();
		try {
			// Something in XML for parsing
			FileHandler xmlFileHandler = new FileHandler("XMLLOG.xml");
			LOGGER.addHandler(xmlFileHandler);
			
			// A copy of the console output
			FileHandler textFileHandler = new FileHandler("TEXTLOG.txt");
			textFileHandler.setFormatter(new SimpleFormatter() {
				private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

				@Override
				public String formatMessage(LogRecord record) {
					return String.format(format, new Date(record.getMillis()), record.getLevel().getLocalizedName(),
							record.getMessage());
				}
			});
			LOGGER.addHandler(textFileHandler);
			
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		consoleHandler.setFormatter(new SimpleFormatter() {
			private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

			@Override
			public String formatMessage(LogRecord record) {
				return String.format(format, new Date(record.getMillis()), record.getLevel().getLocalizedName(),
						record.getMessage());
			}
		});
		
		LOGGER.setUseParentHandlers(false);
		LOGGER.addHandler(consoleHandler);
		LOGGER.setLevel(Level.FINEST);
		
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

		Diagnostician diagnostician = new Diagnostician(EValidator.Registry.INSTANCE);
		diagnostician.validate(ePackage);

		c1.setName("C3");

		diagnostician.validate(ePackage);

		EClass c2 = EcoreFactory.eINSTANCE.createEClass();
		c2.setName("C2");
		ePackage.getEClassifiers().add(c2);

		diagnostician.validate(ePackage);

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
