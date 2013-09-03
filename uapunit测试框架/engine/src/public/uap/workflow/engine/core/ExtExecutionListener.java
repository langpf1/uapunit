package uap.workflow.engine.core;

import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.bizimpl.listener.ListenerDefinition;
import uap.workflow.engine.session.WorkflowContext;

public abstract class ExtExecutionListener implements IInstanceListener {

	private ListenerDefinition listenerDefinition; 
	@Override
	public abstract void notify(IActivityInstance execution) throws Exception;
	
	public void setListenerDefinition(ListenerDefinition listenerDefinition) {
		this.listenerDefinition = listenerDefinition;
	}
	public ListenerDefinition getListenerDefinition() {
		return listenerDefinition;
	}

	protected FlowInfoCtx getFlowInfoContext(){
		return WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx();
	}
	
	protected IBusinessKey getFormInfoContext(){
		return WorkflowContext.getCurrentBpmnSession().getBusinessObject();
	}
}
