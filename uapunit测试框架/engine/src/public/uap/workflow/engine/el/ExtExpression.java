package uap.workflow.engine.el;


import uap.workflow.engine.context.Context;
import uap.workflow.engine.delegate.VariableScope;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.invocation.ExpressionGetInvocation;
import uap.workflow.engine.invocation.ExpressionSetInvocation;
import uap.workflow.engine.javax.ELContext;
import uap.workflow.engine.javax.ELException;
import uap.workflow.engine.javax.MethodNotFoundException;
import uap.workflow.engine.javax.PropertyNotFoundException;
import uap.workflow.engine.javax.ValueExpression;
import uap.workflow.engine.server.BizProcessServer;


public class ExtExpression implements Expression {
	protected String expressionText;
	protected ValueExpression valueExpression;
	protected ExtExpressionManager expressionManager;
	public ExtExpression(ValueExpression valueExpression, ExtExpressionManager expressionManager, String expressionText) {
		this.valueExpression = valueExpression;
		this.expressionManager = expressionManager;
		this.expressionText = expressionText;
	}
	public Object getValue(VariableScope variableScope) {
		ELContext elContext = expressionManager.getElContext(variableScope);
		try {
			ExpressionGetInvocation invocation = new ExpressionGetInvocation(valueExpression, elContext);
			BizProcessServer.getProcessEngineConfig().getDelegateInterceptor().handleInvocation(invocation);
			//Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
			return invocation.getInvocationResult();
		} catch (PropertyNotFoundException pnfe) {
			throw new WorkflowException("Unknown property used in expression", pnfe);
		} catch (MethodNotFoundException mnfe) {
			throw new WorkflowException("Unknown method used in expression", mnfe);
		} catch (ELException ele) {
			throw new WorkflowException("Error while evalutaing expression", ele);
		} catch (Exception e) {
			throw new WorkflowException("Error while evalutaing expression", e);
		}
	}
	public void setValue(Object value, VariableScope variableScope) {
		ELContext elContext = expressionManager.getElContext(variableScope);
		try {
			ExpressionSetInvocation invocation = new ExpressionSetInvocation(valueExpression, elContext, value);
			Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
		} catch (Exception e) {
			throw new WorkflowException("Error while evalutaing expression", e);
		}
	}
	@Override
	public String toString() {
		if (valueExpression != null) {
			return valueExpression.getExpressionString();
		}
		return super.toString();
	}
	public String getExpressionText() {
		return expressionText;
	}
}
