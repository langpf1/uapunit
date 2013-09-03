package uap.workflow.engine.cmd;

import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.ProcessDefinitionStatusEnum;
import uap.workflow.engine.entity.ProcessDefinitionEntity;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;

public class SetProcessDefinitionStatusCmd implements Command<Void> {
	protected IProcessDefinition procDef = null;
	protected ProcessDefinitionStatusEnum status  = null;
	public SetProcessDefinitionStatusCmd(IProcessDefinition procDef, ProcessDefinitionStatusEnum status) {
		this.procDef = procDef;
		this.status  = status;
	}

	@Override
	public Void execute(CommandContext commandContext) {
		
		//SetProcessDefinitionStatusCtx ctx = (SetProcessDefinitionStatusCtx)WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx();
		
		procDef.setValidity(status.getIntValue());
		((ProcessDefinitionEntity)procDef).asyn();

		return null;
	}

}
