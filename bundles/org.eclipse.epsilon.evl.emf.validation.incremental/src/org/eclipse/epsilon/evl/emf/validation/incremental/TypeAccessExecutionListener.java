package org.eclipse.epsilon.evl.emf.validation.incremental;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.eol.dom.AssignmentStatement;
import org.eclipse.epsilon.eol.dom.PropertyCallExpression;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.control.IExecutionListener;
import org.eclipse.epsilon.eol.types.EolModelElementType;

public class TypeAccessExecutionListener implements IExecutionListener {

	private final List<EolModelElementType> accessedTypes = new ArrayList<>();

	private final WeakHashMap<ModuleElement, Object> cache = new WeakHashMap<>();

	@Override
	public void aboutToExecute(ModuleElement ast, IEolContext context) {
		// nothing to do
	}

	@Override
	public void finishedExecuting(ModuleElement ast, Object result, IEolContext context) {
		/*
		 * When the left-hand side of a point expression has been executed, store the
		 * object on which the point expression was invoked, so that we can pass it to
		 * any property access recorders.
		 */
		if (isLeftHandSideOfPointExpression(ast)) {
			cache.put(ast, result);
		}

		// When a property access is executed, notify the recorders
		if (isPropertyAccessExpression(ast)) {
			PropertyCallExpression propertyCallExpression = (PropertyCallExpression) ast;

			final Object modelElementType = cache.get(propertyCallExpression.getTargetExpression());
			final String propertyName = propertyCallExpression.getNameExpression().getName();

			if (modelElementType instanceof EolModelElementType && "all".equals(propertyName)) {
				EolModelElementType eolType = (EolModelElementType) modelElementType;
				accessedTypes.add(eolType);
			}
		}
	}

	@Override
	public void finishedExecutingWithException(ModuleElement ast, EolRuntimeException exception, IEolContext context) {
		// nothing to do
	}

	public List<EolModelElementType> getAccessedTypes() {
		return accessedTypes;
	}

	private static boolean isLeftHandSideOfPointExpression(ModuleElement ast) {
		return ast.getParent() instanceof PropertyCallExpression
				&& ((PropertyCallExpression) ast.getParent()).getTargetExpression() == ast;
	}

	private static boolean isPropertyAccessExpression(ModuleElement ast) {
		return ast instanceof PropertyCallExpression && // AST is a point expression
				!isAssignee(ast); // AST is not the left-hand side of an assignment
	}

	/*
	 * Determines whether the specified AST is the left-hand side of an assignment
	 * expression.
	 */
	private static boolean isAssignee(ModuleElement ast) {
		return ast.getParent() instanceof AssignmentStatement
				&& ((AssignmentStatement) ast.getParent()).getTargetExpression() == ast;
	}
}
