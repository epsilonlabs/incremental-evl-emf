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

import ccl.CclFactory;
import ccl.CclPackage;
import ccl.Component;
import ccl.Connector;
import ccl.InPort;
import ccl.OutPort;
import ccl.Port;
import ccl.impl.SystemImpl;

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
	
	@Benchmark
	public void addOneComponent() throws Exception {
		revalidateAfterChange(this::addComponent);
	}
	
	@Benchmark
	public void removeOneComponent() throws Exception {
		revalidateAfterChange(this::removeComponent);
	}
	
	
	@Benchmark
	public void addFiveChainedComponents() throws Exception {
		revalidateAfterChange(this::addFiveConnectedComponents);
	}
	
	@Benchmark
	public void addFiveLoopedComponents() throws Exception {
		revalidateAfterChange(this::addFiveLoopConnectedComponents);
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

	private void addComponent(Resource r) {
		SystemImpl mSystem = (SystemImpl) r.getContents().get(0);
		//System.out.println("pack:"+mSystem.getClass());
		//System.out.println("components:"+mSystem.getComponents().size());
		
		Component nComponent = CclFactory.eINSTANCE.createComponent();
		nComponent.setName("newComponent");
		mSystem.getComponents().add(nComponent);
		//System.out.println("components:"+mSystem.getComponents().size());
		
	}
	
	private void removeComponent(Resource r) {
		SystemImpl mSystem = (SystemImpl) r.getContents().get(0);
		//System.out.println("pack:"+mSystem.getClass());
		//System.out.println("components:"+mSystem.getComponents().size());	
		mSystem.getComponents().remove(0);
		//System.out.println("components:"+mSystem.getComponents().size());
		
	}
	
	private void addFiveConnectedComponents(Resource r) {
		
		Component nC1 = CclFactory.eINSTANCE.createComponent();
		InPort nC1in = CclFactory.eINSTANCE.createInPort();
		OutPort nC1out = CclFactory.eINSTANCE.createOutPort();
		nC1.getPorts().add(nC1in);
		nC1.getPorts().add(nC1out);
		
		Component nC2 = CclFactory.eINSTANCE.createComponent();
		InPort nC2in = CclFactory.eINSTANCE.createInPort();
		OutPort nC2out = CclFactory.eINSTANCE.createOutPort();	
		nC2.getPorts().add(nC2in);
		nC2.getPorts().add(nC2out);
		
		Component nC3 = CclFactory.eINSTANCE.createComponent();
		InPort nC3in = CclFactory.eINSTANCE.createInPort();
		OutPort nC3out = CclFactory.eINSTANCE.createOutPort();
		nC3.getPorts().add(nC3in);
		nC3.getPorts().add(nC3out);
		
		Component nC4 = CclFactory.eINSTANCE.createComponent();
		InPort nC4in = CclFactory.eINSTANCE.createInPort();
		OutPort nC4out = CclFactory.eINSTANCE.createOutPort();	
		nC4.getPorts().add(nC4in);
		nC4.getPorts().add(nC4out);
		
		Component nC5 = CclFactory.eINSTANCE.createComponent();
		InPort nC5in = CclFactory.eINSTANCE.createInPort();
		OutPort nC5out = CclFactory.eINSTANCE.createOutPort();
		nC5.getPorts().add(nC5in);
		nC5.getPorts().add(nC5out);
		
		Connector c1 = CclFactory.eINSTANCE.createConnector();
		Connector c2 = CclFactory.eINSTANCE.createConnector();
		Connector c3 = CclFactory.eINSTANCE.createConnector();
		Connector c4 = CclFactory.eINSTANCE.createConnector();
		
		c1.setSource(nC1out);
		c1.setTarget(nC2in);
		
		c2.setSource(nC2out);
		c2.setTarget(nC3in);
		
		c3.setSource(nC3out);
		c3.setTarget(nC4in);
		
		c4.setSource(nC4out);
		c4.setTarget(nC5in);
		
		SystemImpl mSystem = (SystemImpl) r.getContents().get(0);
		
		mSystem.getComponents().add(nC1);
		mSystem.getComponents().add(nC2);
		mSystem.getComponents().add(nC3);
		mSystem.getComponents().add(nC4);
		mSystem.getComponents().add(nC5);		
		
		mSystem.getConnectors().add(c1);
		mSystem.getConnectors().add(c2);
		mSystem.getConnectors().add(c3);
		mSystem.getConnectors().add(c4);
		
	}
	
	private void addFiveLoopConnectedComponents(Resource r) {
		
		Component nC1 = CclFactory.eINSTANCE.createComponent();
		InPort nC1in = CclFactory.eINSTANCE.createInPort();
		OutPort nC1out = CclFactory.eINSTANCE.createOutPort();
		nC1.getPorts().add(nC1in);
		nC1.getPorts().add(nC1out);
		
		Component nC2 = CclFactory.eINSTANCE.createComponent();
		InPort nC2in = CclFactory.eINSTANCE.createInPort();
		OutPort nC2out = CclFactory.eINSTANCE.createOutPort();	
		nC2.getPorts().add(nC2in);
		nC2.getPorts().add(nC2out);
		
		Component nC3 = CclFactory.eINSTANCE.createComponent();
		InPort nC3in = CclFactory.eINSTANCE.createInPort();
		OutPort nC3out = CclFactory.eINSTANCE.createOutPort();
		nC3.getPorts().add(nC3in);
		nC3.getPorts().add(nC3out);
		
		Component nC4 = CclFactory.eINSTANCE.createComponent();
		InPort nC4in = CclFactory.eINSTANCE.createInPort();
		OutPort nC4out = CclFactory.eINSTANCE.createOutPort();	
		nC4.getPorts().add(nC4in);
		nC4.getPorts().add(nC4out);
		
		Component nC5 = CclFactory.eINSTANCE.createComponent();
		InPort nC5in = CclFactory.eINSTANCE.createInPort();
		OutPort nC5out = CclFactory.eINSTANCE.createOutPort();
		nC5.getPorts().add(nC5in);
		nC5.getPorts().add(nC5out);
		
		Connector c1 = CclFactory.eINSTANCE.createConnector();
		Connector c2 = CclFactory.eINSTANCE.createConnector();
		Connector c3 = CclFactory.eINSTANCE.createConnector();
		Connector c4 = CclFactory.eINSTANCE.createConnector();
		Connector c5 = CclFactory.eINSTANCE.createConnector();
		
		c1.setSource(nC1out);
		c1.setTarget(nC2in);
		
		c2.setSource(nC2out);
		c2.setTarget(nC3in);
		
		c3.setSource(nC3out);
		c3.setTarget(nC4in);
		
		c4.setSource(nC4out);
		c4.setTarget(nC5in);
		
		c5.setSource(nC5out);
		c5.setTarget(nC1in);
		
		SystemImpl mSystem = (SystemImpl) r.getContents().get(0);
		
		mSystem.getComponents().add(nC1);
		mSystem.getComponents().add(nC2);
		mSystem.getComponents().add(nC3);
		mSystem.getComponents().add(nC4);
		mSystem.getComponents().add(nC5);		
		
		mSystem.getConnectors().add(c1);
		mSystem.getConnectors().add(c2);
		mSystem.getConnectors().add(c3);
		mSystem.getConnectors().add(c4);
		mSystem.getConnectors().add(c5);
		
	}
}
