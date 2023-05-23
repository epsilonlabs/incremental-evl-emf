package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.epsilon.evl.trace.ConstraintTraceItem;

public class TestTools {

	// Model Inspections

	public static String getModelElementName(EClassifier modelElement) {
		return modelElement.getName();
	}

	public static String getModelElementName(EPackage ePackage, int index) {
		return ePackage.getEClassifiers().get(index).getName();
	}

	public static int getModelSize(EPackage ePackage) {
		return ePackage.getEClassifiers().size();
	}

	public static boolean checkModelContainsElementName(EPackage ePackage, String name) {
		Collection<EClassifier> classifiers = ePackage.getEClassifiers();
		for (EClassifier classifer : classifiers) {
			if (classifer.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	// Adapter Inspections

	public static String getStringOfNotifications(EPackage ePackage) {
		IncrementalEvlValidatorAdapter adapter = getValidationAdapter(ePackage);
		return adapter.getStringOfNotifications();
	}

	public static void showAdapterNotifications(EPackage ePackage) {
		System.out.println(getStringOfNotifications(ePackage));
	}

	// Execution Cache inspections

	public static IncrementalEvlValidatorAdapter getValidationAdapter(EObject eob) {
		for (var adapter : eob.eAdapters()) {
			if (adapter.getClass().equals(IncrementalEvlValidatorAdapter.class)) {
				return (IncrementalEvlValidatorAdapter) adapter;
			}
		}
		return null;
	}

	public static void showExecutionCache(EObject ePackage) {
		IncrementalEvlValidatorAdapter adapter = getValidationAdapter(ePackage);
		String output = adapter.constraintExecutionCache.get().toString();
		System.out.println(output);
	}

	public static List<ConstraintPropertyAccess> allAccesses(EObject modelElement) {
		IncrementalEvlValidatorAdapter adapter = getValidationAdapter(modelElement);
		return adapter.constraintExecutionCache.get().constraintPropertyAccess;
	}

	public static List<ConstraintPropertyAccess> accessesOf(EObject modelElement, String featureName) {
		IncrementalEvlValidatorAdapter adapter = getValidationAdapter(modelElement);
		Collection<ConstraintPropertyAccess> propertyAccesses = adapter.constraintExecutionCache
				.get().constraintPropertyAccess;

		return propertyAccesses.stream()
				.filter(pa -> pa.getModelElement() == modelElement && featureName.equals(pa.getPropertyName()))
				.collect(Collectors.toList());
	}

	public static List<Object> modelObjectsFromConstraintPropertyAccess(EPackage ePackage) {
		IncrementalEvlValidatorAdapter adapter = getValidationAdapter(ePackage);
		
		Collection<ConstraintPropertyAccess> propertyAccesses = adapter.constraintExecutionCache
				.get().constraintPropertyAccess;
		
		return propertyAccesses.stream().map(pa -> pa.getModelElement()).collect(Collectors.toList());
	}

	public static List<Object> modelObjectsFromConstraintTrace(EPackage ePackage) {
		IncrementalEvlValidatorAdapter adapter = getValidationAdapter(ePackage);
		Collection<ConstraintTraceItem> traceItems = adapter.constraintExecutionCache.get().constraintTraceItems;

		return traceItems.stream().map(pa -> pa.getInstance()).collect(Collectors.toList());
	}

	public static List<Object> modelObjectsFromUnsatisfiedConstraints(EPackage ePackage) {
		IncrementalEvlValidatorAdapter adapter = getValidationAdapter(ePackage);
		Collection<UnsatisfiedConstraint> unsatisfiedConstraints = adapter.constraintExecutionCache
				.get().unsatisfiedConstraints;

		return unsatisfiedConstraints.stream().map(pa -> pa.getInstance()).collect(Collectors.toList());
	}

}
