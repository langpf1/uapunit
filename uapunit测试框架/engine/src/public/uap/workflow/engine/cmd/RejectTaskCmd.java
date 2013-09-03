package uap.workflow.engine.cmd;
import uap.workflow.engine.core.ActivityInstanceStatus;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.utils.TaskUtil;
public class RejectTaskCmd implements Command<Void> {
	private String taskPk;
	private String activitiId;
	private String[] userPks;
	public RejectTaskCmd(String taskPk, String activitiId, String[] userPks) {
		super();
		this.taskPk = taskPk;
		this.activitiId = activitiId;
		this.userPks = userPks;
	}
	@Override
	public Void execute(CommandContext commandContext) {
		ITask task = TaskUtil.getTaskByTaskPk(taskPk);
		task.reject(activitiId);
		return null;
		/*
		String activiId = activitiId;
		IActivity activity = task.getProcessDefinition().findActivity(activiId);
		IActivitiExecution newExe = task.getExecution().createExecution(activity);
		newExe.setStatus(ActivityInstanceStatus.Wait);
		newExe.asyn();
		if(userPks == null)
			return null;
		for (int i = 0; i < userPks.length; i++) {
			TaskEntity newTask = TaskEntity.newTask(newExe);
			newTask.setCreateType(TaskInstanceCreateType.Reject);
			newTask.setParentTask(task);
			newTask.setOwner(userPks[i]);
			newTask.asyn();
		}
		return null;
		*/
	}
}
