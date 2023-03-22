package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;

public class BuildTestModel {



    public static EClass createNamedModelElement (String name) {
        EClass modelElement = EcoreFactory.eINSTANCE.createEClass();
        modelElement.setName(name);
        return modelElement;
    }

    public static void addModelElementToePackage(EClass modelElement, EPackage ePackage) {
        ePackage.getEClassifiers().add(modelElement);
    }

    public static EClass createAndAddModelElementToePackage(String name, EPackage ePackage) {
        EClass modelElement = createNamedModelElement(name);
        addModelElementToePackage(modelElement,ePackage);
        return modelElement;
    }
    public static void removeModelElementFromPackage(EPackage ePackage){
    }

    public static void showEPackage (EPackage ePackage) {
        System.out.println("ePackage: " + ePackage.getName() + " " + ePackage.hashCode());
        for(EClassifier item : ePackage.getEClassifiers()){
            System.out.println("hash: "+ item.hashCode() + " " +item);
        }

    }

}
