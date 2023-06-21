package org.eclipse.epsilon.evl.emf.validation.incremental.benchmarks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.evl.emf.validation.incremental.benchmarks.validators.BatchValidator;
import org.eclipse.epsilon.evl.emf.validation.incremental.benchmarks.validators.IValidator;
import org.eclipse.epsilon.evl.emf.validation.incremental.benchmarks.validators.IncrementalValidator;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import ccl.CclPackage;
import ccl.Component;

@State(Scope.Thread)
@Fork(value = 5, warmups = 1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
public class ComponentBenchmarks {

	@Param("constraints.evl")
	private String constraintsPath;

	@Param("generated.ccl")
	private String modelPath;

	@Param({ "batch", "incremental" })
	private String mode;

	private Random rnd = new Random(1234);

	@Benchmark
	public void once() throws Exception {
		Resource r = loadModel();
		createValidator().validate(r);
	}

	@Benchmark
	public void noChange() throws Exception {
		revalidateAfterChange((r) -> {});
	}
	
	@Benchmark
	public void renameOne() throws Exception {
		revalidateAfterChange(this::renameRandomComponent);
	}
	
	private void revalidateAfterChange(Consumer<Resource> change) throws Exception {
		Resource r = loadModel();
		IValidator validator = createValidator();
		validator.validate(r);
		change.accept(r);
		validator.validate(r);
	}
	
	private IValidator createValidator() {
		if ("batch".equals(mode)) {
			return new BatchValidator(constraintsPath);
		} else {
			return new IncrementalValidator(constraintsPath);
		}
	}

	private Resource loadModel() throws IOException {
		CclPackage.eINSTANCE.getNsURI();

		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		Resource r = rs.createResource(URI.createFileURI(new File(modelPath).getCanonicalPath()));
		r.load(null);

		return r;
	}

	private void renameRandomComponent(Resource r) {
		List<Component> components = new ArrayList<>();
		for (TreeIterator<EObject> it = r.getAllContents(); it.hasNext();) {
			EObject eob = it.next();
			if (eob instanceof Component) {
				components.add((Component) eob);
			}
		}
		Component component = components.get(rnd.nextInt(components.size()));
		component.setName("a" + component.getName());
	}
}
