package uap.workflow.engine.el;

import java.lang.reflect.Method;

import uap.workflow.engine.javax.ELContext;
import uap.workflow.engine.javax.ELResolver;
import uap.workflow.engine.javax.FunctionMapper;
import uap.workflow.engine.javax.ValueExpression;
import uap.workflow.engine.javax.VariableMapper;

public class ExtELContext extends ELContext {
	
	protected ELResolver elResolver;
	protected ExtFunctions functions;
	protected ExtVariables variables;
	
	public ExtELContext(ELResolver elResolver){
		this.elResolver = elResolver;
		this.functions = new ExtFunctions();
		this.variables = new ExtVariables();
	}
	
	@Override
	public ELResolver getELResolver() {
		return elResolver;
	}
	
	public void setELResolver(ELResolver resolver){
		this.elResolver = resolver;
	}

	@Override
	public FunctionMapper getFunctionMapper() {
		return functions;
	}

	@Override
	public VariableMapper getVariableMapper() {
		return variables;
	}

	public void setFunction(String prefix, String localName, Method method){
		functions.setFunction(prefix, localName, method);
	}
	
	public ValueExpression setVariable(String variable, ValueExpression expression) {
		return variables.setVariable(variable, expression); 
	}
}
