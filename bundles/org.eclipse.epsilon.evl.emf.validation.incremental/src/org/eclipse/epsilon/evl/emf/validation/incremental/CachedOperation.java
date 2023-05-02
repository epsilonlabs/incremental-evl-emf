package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.List;

import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.eol.dom.Operation;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;

public class CachedOperation extends Operation implements ModuleElement {

	@Override
	public Object execute(Object self, List<?> parameterValues, IEolContext context, boolean inNewStackFrame) throws EolRuntimeException {
		// If the operation has not been invoked yet, set up context to capture accesses
		Object result = super.execute(self, parameterValues, context, inNewStackFrame);

		// Add all accesses to the caller context (may be another @cached operation, or not)
		
		return result;
	}
	
}
