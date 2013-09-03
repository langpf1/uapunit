package uap.workflow.engine.command;

import uap.workflow.engine.service.TaskService;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;

/**
 * ROLLBACK时的工作流回退处理，UNAPPROVE时的审批流弃审处理
 */
public class CallBackTaskInsCmd implements ICommand<Void> {
	@Override
	public Void execute() {
		TaskService taskService = WfmServiceFacility.getTaskService();
		String taskPk = WorkflowContext.getCurrentBpmnSession().getTaskPk();
		taskService.callBackTask(taskPk);
		return null;
	}
}
