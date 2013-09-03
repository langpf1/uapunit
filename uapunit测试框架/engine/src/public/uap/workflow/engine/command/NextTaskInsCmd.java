package uap.workflow.engine.command;

import uap.workflow.engine.service.TaskService;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;
public class NextTaskInsCmd implements ICommand<Void> {
	public Void execute() {
		ICommandService commandService = new CommandService();
		commandService.execute(new GenFormNumbCmd());
		TaskService taskService = WfmServiceFacility.getTaskService();
		taskService.completeTask(WorkflowContext.getCurrentBpmnSession().getTaskPk());
		return null;
	}
}
