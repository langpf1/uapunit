package uap.workflow.engine.bpmn.behavior;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import nc.bs.logging.Logger;
import nc.vo.jcom.lang.StringUtil;
import uap.workflow.bizitf.exception.BizException;
import uap.workflow.engine.bpmn.parser.FieldDeclaration;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.delegate.Expression;

public class JavaMethodInvocation {
	private Object classInstance;
	private String method;
	private List<FieldDeclaration> fieldDeclarations;
	private IActivityInstance execution;
	public JavaMethodInvocation(){
		
	}
	
	public JavaMethodInvocation(Object classInstance, String method, 
			List<FieldDeclaration> fieldDeclarations ,IActivityInstance execution){
		this.classInstance = classInstance;
		this.method = method;
		this.fieldDeclarations = fieldDeclarations; 
		this.execution = (IActivityInstance)execution;
	}
	
	public Object handlerInvocation(){ 
		
		//get fields value
		//ExtExpressionManager expressionMgr = new ExtExpressionManager();
		Expression expression = null;
		List<Object> fieldValues = new ArrayList<Object>();
		for(int i = 0; i < fieldDeclarations.size(); i++){
			expression = (Expression)fieldDeclarations.get(i).getValue();//expressionMgr.createExpression("${" + ((NCExpression)fieldDeclarations.get(i).getValue()).getExpressionText() + "}");
			fieldValues.add(expression.getValue(execution));
		}

		//get method
		List<Class<?>> list = new ArrayList<Class<?>>();
		for(int i = 0; i < fieldDeclarations.size(); i++){
			if (!StringUtil.isEmptyWithTrim(fieldDeclarations.get(i).getType()))
				try {
					list.add(Class.forName(fieldDeclarations.get(i).getType()));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
		}
		Class<?>[] fields = null;
		fields = list.toArray(new Class<?>[0]);
		Method invocation = null;
		try{
			invocation = classInstance.getClass().getMethod(method, fields);
		}catch(NoSuchMethodException e){
			throw new BizException(e.getMessage());
		}
		
		//Invocation
		try{
			Object result = invocation.invoke(classInstance, fieldValues.toArray());
			return result;
		}catch(IllegalAccessException e){
			Logger.error("Invocation Error:" + e.getMessage());
			throw new BizException(e.getMessage());
		}catch(InvocationTargetException e){
			Logger.error("Invocation Error:" + e.getMessage());
			throw new BizException(e.getMessage());
		}
		
	}	
}
