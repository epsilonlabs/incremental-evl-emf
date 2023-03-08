package org.eclipse.epsilon.evl.emf.validation.incremental;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;

public class BuildTestModel {

    public static void addModelElementToePackage(String name, EPackage ePackage) {
        EClass temp = EcoreFactory.eINSTANCE.createEClass();
        temp.setName(name);
        ePackage.getEClassifiers().add(temp);
    }

    public EPackage getOneElementModel(EPackage ePackage) {
        addModelElementToePackage("c1", ePackage);
        return ePackage;
    }

    public EPackage getTwoElementModel(EPackage ePackage) {
        addModelElementToePackage("c1", ePackage);
        addModelElementToePackage("c2", ePackage);
        return ePackage;
    }

}
