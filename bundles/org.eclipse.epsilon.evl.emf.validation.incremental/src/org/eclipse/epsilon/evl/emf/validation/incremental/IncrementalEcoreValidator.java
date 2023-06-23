package org.eclipse.epsilon.evl.emf.validation.incremental;

public class IncrementalEcoreValidator extends IncrementalEvlValidator {

	private java.net.URI constraintsURI;

	public void setConstraintsURI(java.net.URI uri) {
		this.constraintsURI = uri;
	}

	@Override
	public java.net.URI getConstraintsURI() {
		return constraintsURI;
	}

}
