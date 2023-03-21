package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;

public class TestTools {

    public static String getName (EClassifier modelElement) {
        return modelElement.getName();
    }
    public static  String getName (EPackage ePackage, int index) {
        return ePackage.getEClassifiers().get(index).getName();
    }

    public static int getSize(EPackage ePackage){
        return ePackage.getEClassifiers().size();
    }

}
