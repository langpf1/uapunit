package uap.workflow.engine.command;

import uap.workflow.engine.context.RejectTaskInsCtx;
import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.service.TaskService;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;
public class RejectTaskInsCmd implements ICommand<Void> {
	@Override
	public Void execute() {
		TaskEntity task = (TaskEntity) WorkflowContext.getCurrentBpmnSession().getTask();
		TaskService taskService = WfmServiceFacility.getTaskService();
		RejectTaskInsCtx rejectTaskCtx = (RejectTaskInsCtx) WorkflowContext.getCurrentBpmnSession().getRequest().getFlowInfoCtx();
		taskService.rejectTask(task.getTaskPk(), rejectTaskCtx.getRejectInfo().getActivityId(), rejectTaskCtx.getRejectInfo().getUserPks());
		return null;
	}
}
