# Incremental execution of EVL constraints

This project requires [Java 11](https://adoptium.net/es/temurin/releases/?version=11).

Please make sure that you have it installed before continuing.

## Demo from Eclipse

To run the example model editors with integrated incremental validation using EVL:

* Open an Eclipse instance and import the projects in `bundles`, `tests`, `releng`, and these projects in `examples`:
  * `org.eclipse.epsilon.examples.metamodels.flowchart`
  * `org.eclipse.epsilon.examples.metamodels.flowchart.validation`
  * `org.eclipse.epsilon.examples.metamodels.components`
  * `org.eclipse.epsilon.examples.metamodels.components.validation.incremental`
* Open the `org.eclipse.epsilon.target.target` target file in `org.eclipse.epsilon.evl.emf.validation.incremental.targetplatform`, and set it as the target platform.
* Run the launch configuration in `org.eclipse.epsilon.evl.emf.validation.incremental`.

To try out the incremental validation from the nested workbench:

* Open the `MyFlowcharts.flowcharts` model with the sample reflective Ecore model editor. Right-click on the root of the model, and enable "Live Validation". Try editing the name of an action to the empty string or a single letter.
* Open the diagram in the `representations.aird` file, right-click on the background, and select "Validate diagram". Repeat this as needed to re-validate.
* You can similarly open the `representations.aird` file in the `components.model.manual` and `components.model.generated` example projects.

### Accessing the flowchart model with the tree-editor

Live validation uses the Sample Reflective Ecore Model Editor tree-based editor, which may not open the flowchart model by default.

To open the flowchart model with the editor, right-click ‘MyFlowcharts.flowcharts’, select 'Open With', then 'Other...' from the menu.
Type 'Sample' to filter the list of editors and left-click 'Sample Reflective Ecore Model Editor', then click 'OK'.

The editor should open in the main window displaying a node labelled 'platform:/resource...', right-click this node. From the menu check the box 'Live Validation'.
When the model's tree is expanded small red boxes with a white X will be placed on the node icons where a constraint is unsatisfied.

The example projects for the component models work in the same way, except you will need to open the relevant `.ccl` file instead.

## Building from console

Install [Maven](https://maven.apache.org/), and run this command from the root of the repository:

```shell
mvn install
```

## Logging configuration

To change the level of detail for the logging, edit the `logging.properties` file in the `org.eclipse.epsilon.evl.emf.validation.incremental` project.

## Benchmarking

To run the performance benchmarks comparing batch and incremental validation, run the [`run-benchmarks.sh`](./run-benchmarks.sh) shell script.

The benchmarks are written using the [OpenJDK JMH](https://github.com/openjdk/jmh) microbenchmarking framework.
They are located within the [benchmarks](tests/org.eclipse.epsilon.evl.emf.validation.incremental.benchmarks) project.

To work on this project, import the project into your main Eclipse instance: it is best edited from Eclipse, but it has to be run from the above shell script.
You will need to [install IvyDE](https://ant.apache.org/ivy/ivyde/download.html), right-click on the project in the "Package Explorer" Eclipse view, and select "Ivy - Retrieve dependencies...".

For further details on how to develop JMH microbenchmarks, check the [official samples](https://github.com/openjdk/jmh/tree/master/jmh-samples/src/main/java/org/openjdk/jmh/samples).
