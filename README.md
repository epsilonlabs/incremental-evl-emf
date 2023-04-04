# Incremental execution of EVL constraints

This project requires [Java 11](https://adoptium.net/es/temurin/releases/?version=11).

Please make sure that you have it installed before continuing.

## Demo from Eclipse

To run the example model editors with integrated incremental validation using EVL:

* Open an Eclipse instance and import the projects in `bundles`, `tests`, `releng`, and these two projects in `examples`:

  * `org.eclipse.epsilon.examples.metamodels.flowchart`
  * `org.eclipse.epsilon.examples.metamodels.flowchart.validation`

* Open the `org.eclipse.epsilon.target.target` target file in `org.eclipse.epsilon.evl.emf.validation.incremental.targetplatform`, and set it as the target platform.
* Run the launch configuration in `org.eclipse.epsilon.evl.emf.validation.incremental`.

To try out the incremental validation from the nested workbench:

* Open the `MyFlowcharts.flowcharts` model with the sample reflective Ecore model editor. Right-click on the root of the model, and enable "Live Validation". Try editing the name of an action to the empty string or a single letter.
* Open the diagram in the `representations.aird` file, right-click on the background, and select "Validate diagram". Repeat this as needed to re-validate.

___More help for accessing the flowchart model with the tree-editor___

Live validation uses the Sample Reflective Ecore Model Editor tree-based editor, which may not open the flowchart model by default. 

To open the flowchart model with the editor, right-click ‘MyFlowcharts.flowcharts’, select 'Open With', then 'Other...' from the menu. Type 'Sample' to filter the list of editors and left-click 'Sample Reflective Ecore Model Editor', then click 'OK'.  

The editor should open in the main window displaying a node labelled 'platform:/resource...', right-click this node. From the menu check the box 'Live Validation'. When the model's tree is expanded small red boxes with a white X will be placed on the node icons where a constraint is unsatisfied. 



## Building from console

Install [Maven](https://maven.apache.org/), and run this command from the root of the repository:

```shell
mvn install
```

## Logging configuration

To change the level of detail for the logging, edit the `logging.properties` file in the `org.eclipse.epsilon.evl.emf.validation.incremental` project.
