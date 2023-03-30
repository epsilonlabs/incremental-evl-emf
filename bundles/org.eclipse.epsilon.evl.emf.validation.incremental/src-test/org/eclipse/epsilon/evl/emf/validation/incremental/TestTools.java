package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.epsilon.evl.trace.ConstraintTraceItem;

import java.util.*;

public class TestTools {

    //
    // Model Inspections

    public static String getModelElementName(EClassifier modelElement) {
        return modelElement.getName();
    }
    public static  String getModelElementName(EPackage ePackage, int index) {
        return ePackage.getEClassifiers().get(index).getName();
    }

    public static int getModelSize(EPackage ePackage){
        return ePackage.getEClassifiers().size();
    }

    public static boolean checkModelContainsElementName (EPackage ePackage, String name) {
        Collection <EClassifier> classifiers = ePackage.getEClassifiers();
        for(EClassifier classifer : classifiers) {
            if(classifer.getName().equals(name)){
                return true;
            }
        }
        return false;
    }


    //
    // Execution Cache inspections

    public static IncrementalEvlValidatorAdapter getValidationAdapter(EObject eob) {
        for(var adapter : eob.eAdapters()) {
            if(adapter.getClass().equals(IncrementalEvlValidatorAdapter.class)) {
                return (IncrementalEvlValidatorAdapter) adapter;
            }
        }
        return null;
    }

    public static int getExecutionCacheConstrainPropertyAccessSize(EPackage ePackage) {
        IncrementalEvlValidatorAdapter adapter = getValidationAdapter(ePackage);
        return adapter.constraintExecutionCache.get().constraintPropertyAccess.size();
    }

    public static int getExecutionCacheConstraintTraceItemSize (EPackage ePackage) {
        IncrementalEvlValidatorAdapter adapter = getValidationAdapter(ePackage);
        return adapter.constraintExecutionCache.get().constraintTraceItems.size();
    }

    public static int getExecutionCacheUnsatisfiedConstraintsSize (EPackage ePackage) {
        IncrementalEvlValidatorAdapter adapter = getValidationAdapter(ePackage);
        return adapter.constraintExecutionCache.get().unsatisfiedConstraints.size();
    }

    public static void showExecutionCache (EObject ePackage) {
        IncrementalEvlValidatorAdapter adapter = getValidationAdapter(ePackage);
        adapter.constraintExecutionCache.get().printExecutionCache();
    }

    public static List<String> getPackageNamesInConstraintTrace(EPackage ePackage) {
        IncrementalEvlValidatorAdapter adapter = getValidationAdapter(ePackage);
        Collection <ConstraintTraceItem> traceItems = adapter.constraintExecutionCache.get().constraintTraceItems;
        List<String> names = new ArrayList<>();
        for(ConstraintTraceItem item : traceItems) {
            EClass modelElement = (EClass) item.getInstance();
            names.add(modelElement.getName());
        }
        return names;
    }

    public static boolean checkExecutionCacheConstraintTraceItemsForModelElementName (EPackage ePackage, String name) {
        IncrementalEvlValidatorAdapter adapter = getValidationAdapter(ePackage);
        Collection <ConstraintTraceItem> traceItems = adapter.constraintExecutionCache.get().constraintTraceItems;
        for(ConstraintTraceItem item : traceItems) {
            EClass modelElement = (EClass) item.getInstance();
            if(modelElement.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkExecutionCacheConstraintPropertyAccessForModelElementName (EPackage ePackage, String name) {
        IncrementalEvlValidatorAdapter adapter = getValidationAdapter(ePackage);
        Collection <ConstraintPropertyAccess> constraintPropertyAccess = adapter.constraintExecutionCache.get().constraintPropertyAccess;
        for(ConstraintPropertyAccess CPA : constraintPropertyAccess) {
            EClass modelElement = (EClass) CPA.getModelElement();
            if(modelElement.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkConstraintExecutionCacheUnsatisfiedConstraintForModelElementName (EPackage ePackage, String name) {
        IncrementalEvlValidatorAdapter adapter = getValidationAdapter(ePackage);
        Collection <UnsatisfiedConstraint> unsatisfiedConstraints = adapter.constraintExecutionCache.get().unsatisfiedConstraints;
        for(UnsatisfiedConstraint UC : unsatisfiedConstraints) {
            EClass modelElement = (EClass) UC.getInstance();
            if(modelElement.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
