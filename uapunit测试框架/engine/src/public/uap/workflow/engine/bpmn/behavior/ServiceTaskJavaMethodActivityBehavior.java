package uap.workflow.engine.bpmn.behavior;

import java.util.List;


import uap.workflow.engine.bpmn.parser.FieldDeclaration;
import uap.workflow.engine.core.IActivityInstance;

public class ServiceTaskJavaMethodActivityBehavior extends TaskActivityBehavior {
	protected String method;
	protected Object methodObject;
	protected List<FieldDeclaration> fieldDeclarations;
	protected String resultVariable;
	
	public ServiceTaskJavaMethodActivityBehavior(Object obj, String method,
			List<FieldDeclaration> fieldDeclarations,String resultVariable) throws RuntimeException{
		this.method = method;
		this.methodObject = obj;
		this.fieldDeclarations = fieldDeclarations;
		this.resultVariable = resultVariable; 
	}
	
	public void execute(IActivityInstance execution) throws Exception {
		JavaMethodInvocation invocation = new JavaMethodInvocation(methodObject, method, fieldDeclarations,execution);
		Object result = invocation.handlerInvocation();
		execution.setVariable(resultVariable, result);
		execution.setVariableLocal(resultVariable, result);
		super.execute(execution);
		
	}
	
	public void notify(IActivityInstance execution) throws Exception {
		execute((IActivityInstance) execution);
	}
	
/*	public void execute(IActivitiExecution execution) throws Exception {
		//Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new JavaDelegateInvocation(javaDelegate, execution));
		JavaMethodInvocation invocation = new JavaMethodInvocation(methodObject, method, fieldDeclarations,execution);
		invocation.handlerInvocation();
	}*/
}
