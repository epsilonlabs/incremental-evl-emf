#!/bin/bash

set -e

EVL_PATH=tests/org.eclipse.epsilon.evl.emf.validation.incremental.benchmarks/src/main/resources/constraints.evl
MODEL_PATH=examples/org.eclipse.epsilon.examples.metamodels.components.model.generated/generated.ccl

mvn -f pom-plain.xml clean verify
java -jar tests/org.eclipse.epsilon.evl.emf.validation.incremental.benchmarks/target/benchmarks.jar \
	-p "constraintsPath=$EVL_PATH" \
	-p "modelPath=$MODEL_PATH" 2>&1 | tee benchmarks-$(date +%s).txt
