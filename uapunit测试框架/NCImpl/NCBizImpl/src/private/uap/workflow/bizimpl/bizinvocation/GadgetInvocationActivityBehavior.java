package uap.workflow.bizimpl.bizinvocation;


import uap.workflow.bpmn2.model.NameSpaceConst;
import uap.workflow.engine.bpmn.behavior.ExtensionService;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.extend.gadget.IWorkflowGadget;
import uap.workflow.engine.util.ReflectUtil;
import uap.workflow.engine.xml.Element;

public class GadgetInvocationActivityBehavior extends ExtensionService {
	protected IWorkflowGadget gadget;
	
	protected GadgetInvocationActivityBehavior() {}

	public GadgetInvocationActivityBehavior(IWorkflowGadget delegate) {
		this.gadget = delegate;
	}
	
	public void notify(IActivityInstance execution) throws Exception {
		execute((IActivityInstance) execution);
	}

	@Override
	public void executeBehavior(IActivityInstance execution) throws Exception {
		GadgetInvocation invocation = new GadgetInvocation(gadget, execution);
		Object obj = invocation.handlerInvocation();	
		execution.setVariable(resultVariable, obj);
	}

	@Override
	public void parseExtensionService(Element serviceTaskElement) throws RuntimeException {
		className = serviceTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "class");
		resultVariable = serviceTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "resultVariable");
		if (className == null || resultVariable == null)
			throw new RuntimeException("MUST set class name and resultVariable");
		//methodName = serviceTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "method");
		gadget = (IWorkflowGadget)ReflectUtil.instantiate(className); 
	}

}
