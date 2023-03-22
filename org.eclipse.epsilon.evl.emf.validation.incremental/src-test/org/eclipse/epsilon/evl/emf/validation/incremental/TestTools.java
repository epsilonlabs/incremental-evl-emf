package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
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



    //
    // Execution Cache inspections

    public static IncrementalEvlValidatorAdapter getValidationAdapter (EPackage epackage) {
        IncrementalEvlValidatorAdapter resultingAdapter;
        for(var adapter : epackage.eAdapters()) {
            if(adapter.getClass().equals(IncrementalEvlValidatorAdapter.class)) {
                System.out.println("Found adapter: "+ adapter);
                return (IncrementalEvlValidatorAdapter) adapter;
            }
        }
        return null;
    }

    public static int getExecutionCacheConstrainProperyAccessSize (EPackage ePackage) {
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

    public static void showExecutionCache (EPackage ePackage) {
        IncrementalEvlValidatorAdapter adapter = getValidationAdapter(ePackage);
        adapter.constraintExecutionCache.get().printExecutionCache();
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

}
