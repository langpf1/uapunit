package uap.workflow.engine.javax;

import uap.workflow.engine.delegate.VariableScope;


public abstract class ExtELResolver extends ELResolver {
	
	protected VariableScope variableScope;
	
	public VariableScope getVariableScope() {
		return variableScope;
	}
	public void setVariableScope(VariableScope variableScope) {
		this.variableScope = variableScope;
	}
}
