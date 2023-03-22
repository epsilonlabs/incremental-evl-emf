package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;

public class TestTools {

    //
    // Model Inspections

    public static String getName (EClassifier modelElement) {
        return modelElement.getName();
    }
    public static  String getName (EPackage ePackage, int index) {
        return ePackage.getEClassifiers().get(index).getName();
    }

    public static int getSize(EPackage ePackage){
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
}
