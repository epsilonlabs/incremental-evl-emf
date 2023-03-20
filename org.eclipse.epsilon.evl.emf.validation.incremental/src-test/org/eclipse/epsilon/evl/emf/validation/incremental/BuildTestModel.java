package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;

public class BuildTestModel {

    public IncrementalEvlValidatorAdapter getValidationAdapter (EPackage epackage) {
        IncrementalEvlValidatorAdapter resultingAdapter;
        for(var adapter : epackage.eAdapters()) {
            if(adapter.getClass().equals(IncrementalEvlValidatorAdapter.class)) {
                System.out.println("Found adapter: "+ adapter);
                return (IncrementalEvlValidatorAdapter) adapter;
            }
        }
        return null;
    }

    public static EClass addModelElementToePackage(String name, EPackage ePackage) {
        EClass temp = EcoreFactory.eINSTANCE.createEClass();
        temp.setName(name);
        ePackage.getEClassifiers().add(temp);
        return temp;
    }

    public static void showEPackage (EPackage ePackage) {
        System.out.println("ePackage: " + ePackage.getName() + " " + ePackage.hashCode());
        for(EClassifier item : ePackage.getEClassifiers()){
            System.out.println("hash: "+ item.hashCode() + " " +item);
        }

    }

}
