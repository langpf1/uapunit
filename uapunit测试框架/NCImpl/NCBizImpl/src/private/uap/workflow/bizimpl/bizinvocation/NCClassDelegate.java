
package uap.workflow.bizimpl.bizinvocation;

import java.util.List;
import uap.workflow.bizimpl.bizinvocation.GadgetInvocationActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ServiceTaskJavaMethodActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ServiceTaskJavaDelegateActivityBehavior;
import uap.workflow.engine.bpmn.helper.ClassDelegate;
import uap.workflow.engine.bpmn.parser.FieldDeclaration;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.delegate.JavaDelegate;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.extend.gadget.IWorkflowGadget;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.util.ReflectUtil;

public class NCClassDelegate extends ClassDelegate{

	private String method;
	
	public NCClassDelegate(Class<?> clazz, List<FieldDeclaration> fieldDeclarations) {
		this(clazz.getName(), fieldDeclarations);
	}
	
	public NCClassDelegate(String className,
			List<FieldDeclaration> fieldDeclarations) {
		super(className, fieldDeclarations);
	}
	
	public NCClassDelegate(Class<?> clazz, List<FieldDeclaration> fieldDeclarations, String method) {
		this(clazz.getName(), fieldDeclarations);
	}
	
	public NCClassDelegate(String className, String method, 
			List<FieldDeclaration> fieldDeclarations) {
		this(className, fieldDeclarations);
		this.method = method;
	}
	
	protected ActivityBehavior getActivityBehaviorInstance(IActivityInstance execution) {
		Object delegateInstance = null;
		delegateInstance = instantiateDelegate(className, fieldDeclarations);
		if (delegateInstance instanceof ActivityBehavior) {
			return determineBehaviour((ActivityBehavior) delegateInstance, execution);
		} else if (delegateInstance instanceof JavaDelegate) {
			return determineBehaviour(new ServiceTaskJavaDelegateActivityBehavior((JavaDelegate) delegateInstance),
					execution);
		} else if (delegateInstance instanceof IWorkflowGadget) {
			return determineBehaviour(new GadgetInvocationActivityBehavior(
					(IWorkflowGadget) delegateInstance), execution);
		} else {
			throw new WorkflowException(delegateInstance.getClass().getName() + " doesn't implement "
					+ JavaDelegate.class.getName() + " nor " + ActivityBehavior.class.getName());
		}
	}
}
