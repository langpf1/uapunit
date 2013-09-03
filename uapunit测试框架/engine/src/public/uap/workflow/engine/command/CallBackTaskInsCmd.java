package uap.workflow.engine.command;

import uap.workflow.engine.service.TaskService;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;

/**
 * ROLLBACKʱ�Ĺ��������˴���UNAPPROVEʱ��������������
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
