package uap.workflow.engine.cmd;
import java.util.List;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.utils.TaskUtil;
public class CallBackTaskCmd implements Command<Void> {
	protected String taskPk;
	public CallBackTaskCmd(String taskPk) {
		super();
		this.taskPk = taskPk;
	}
	@Override
	public Void execute(CommandContext commandContext) {
		List<ITask> subTasks = TaskUtil.getSubTasksByTaskPk(taskPk);
		if(subTasks != null)
		{
			TaskEntity taskEntity = null;
			for (int i = 0; i < subTasks.size(); i++) {
				taskEntity = (TaskEntity) subTasks.get(i);
				taskEntity.delete();
			}
		}
		ITask task = WorkflowContext.getCurrentBpmnSession().getTask();
		task.callBack();
		
		return null;
	}
}
