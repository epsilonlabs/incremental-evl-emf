#!/bin/sh

set -e

mvn -f pom-plain.xml clean verify
java -jar tests/org.eclipse.epsilon.evl.emf.validation.incremental.benchmarks/target/benchmarks.jar
