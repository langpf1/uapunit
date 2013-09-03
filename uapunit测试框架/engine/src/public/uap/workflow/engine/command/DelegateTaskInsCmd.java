package uap.workflow.engine.command;

import uap.workflow.engine.context.DelegateTaskInsCtx;
import uap.workflow.engine.service.TaskService;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;
public class DelegateTaskInsCmd implements ICommand<Void> {
	@Override
	public Void execute() {
		TaskService taskService = WfmServiceFacility.getTaskService();
		DelegateTaskInsCtx flowInfox = (DelegateTaskInsCtx) WorkflowContext.getCurrentBpmnSession().getRequest().getFlowInfoCtx();
		taskService.delegateTask(flowInfox.getTaskPk(), flowInfox.getTurnUserPks());
		return null;
	}
}
