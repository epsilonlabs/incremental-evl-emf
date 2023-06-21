package org.eclipse.epsilon.evl.emf.validation.incremental.benchmarks;

import java.io.File;

import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.evl.EvlModule;
import org.eclipse.epsilon.evl.emf.validation.incremental.IncrementalEvlModule;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import ccl.CclPackage;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
public class SingleValidation {

	@Param("constraints.evl")
	private String constraintsPath;

	@Param("generated.ccl")
	private String modelPath;

    @Benchmark
    public void batch() throws Exception {
    	runValidation(new EvlModule());
    }

    @Benchmark
    public void incremental() throws Exception {
    	runValidation(new IncrementalEvlModule());
    }

	private void runValidation(EvlModule module) throws Exception {
		module.parse(new File(constraintsPath));

    	CclPackage.eINSTANCE.getName();
    	EmfModel model = new EmfModel();
    	model.setMetamodelUri(CclPackage.eINSTANCE.getNsURI());
    	model.setModelFile(modelPath);
    	model.setReadOnLoad(true);
    	model.setStoredOnDisposal(false);
    	model.setName("Model");
    	model.load();

    	try {
    		module.getContext().getModelRepository().addModel(model);
    		module.execute();
    	} finally {
    		module.getContext().getModelRepository().dispose();
    		module.getContext().dispose();
    	}
	}
}
