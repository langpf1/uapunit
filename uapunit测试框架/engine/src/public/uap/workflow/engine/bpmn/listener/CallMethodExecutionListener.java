package uap.workflow.engine.bpmn.listener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import uap.workflow.bizimpl.listener.ListenerDefinition;
import uap.workflow.engine.bpmn.parser.FieldDeclaration;
import uap.workflow.engine.core.ExtExecutionListener;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.delegate.Expression;
import uap.workflow.engine.el.ExtExpressionManager;

public class CallMethodExecutionListener extends ExtExecutionListener {

	@Override
	public void notify(IActivityInstance execution) throws Exception {
		executeExtended(getListenerDefinition(),execution);

	}
	private void executeExtended(ListenerDefinition definition, IActivityInstance execution) {
		try {
			Object clzInstance = Class.forName(definition.getImplementation()).newInstance();
			List<Class<?>> parameterTypes = new ArrayList<Class<?>>();
			List<Object> parameterValues = new ArrayList<Object>();
			for (FieldDeclaration field : definition.getFieldExtensions()) {
				String expression = (String)field.getValue();
				Object value = null;
				if (expression == null || expression.isEmpty())
					value = execution.getVariable(field.getName());
				else
					value = evaluateExpression("${"+expression+"}", execution);
				if (value == null)
					parameterTypes.add(Class.forName(field.getType()));
				else 
					parameterTypes.add(value.getClass());
				parameterValues.add(value);
			}
			Class<?>[] typeList = parameterTypes.toArray(new Class[0]);
			Method method = clzInstance.getClass().getMethod(definition.getMethod(), typeList);
			method.invoke(clzInstance, parameterValues.toArray(new Object[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object evaluateExpression(String exprStr, IActivityInstance execution) {
		ExtExpressionManager expressionManager = new ExtExpressionManager();
		Expression expression = expressionManager.createExpression(exprStr);
		Object object = expression.getValue(execution);
		return object;
	}

}
