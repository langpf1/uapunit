package uap.workflow.engine.el;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import uap.workflow.engine.javax.ValueExpression;
import uap.workflow.engine.javax.VariableMapper;

class ExtVariables extends VariableMapper {
	Map<String, ValueExpression> map = Collections.emptyMap();

	public ExtVariables(){
		
	}
	
	@Override
	public ValueExpression resolveVariable(String variable) {
		return map.get(variable);
	}

	@Override
	public ValueExpression setVariable(String variable, ValueExpression expression) {
		if (map.isEmpty()) {
			map = new HashMap<String, ValueExpression>();
		}
		return map.put(variable, expression);
	}
}



