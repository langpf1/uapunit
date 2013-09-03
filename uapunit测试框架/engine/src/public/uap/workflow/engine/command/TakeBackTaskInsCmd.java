package uap.workflow.engine.command;

import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.engine.context.TakeBackTaskInsCtx;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.service.TaskService;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;
/**
 * 取消提交
 */
public class TakeBackTaskInsCmd implements ICommand<Void> {
	@Override
	public Void execute() {
		TaskService taskService = WfmServiceFacility.getTaskService();
		ITask task = WorkflowContext.getCurrentBpmnSession().getTask();
		FlowInfoCtx flowInfoCtx = WorkflowContext.getCurrentBpmnSession().getRequest().getFlowInfoCtx();
		TakeBackTaskInsCtx takeBackTaskCtx = (TakeBackTaskInsCtx) flowInfoCtx;
		taskService.takeBackTask(task.getTaskPk(), takeBackTaskCtx.getSubTaskPks());
		return null;
	}
}
