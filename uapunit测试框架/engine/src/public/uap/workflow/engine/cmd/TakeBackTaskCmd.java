package uap.workflow.engine.cmd;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.utils.TaskUtil;
public class TakeBackTaskCmd implements Command<Void> {
	protected String taskPk;
	protected String[] subTaskPks;
	public TakeBackTaskCmd(String taskPk, String[] subTaskPks) {
		super();
		this.taskPk = taskPk;
		this.subTaskPks = subTaskPks;
	}
	@Override
	public Void execute(CommandContext commandContext) {
		List<ITask> subTasks = TaskUtil.getSubTasksByTaskPk(taskPk);
		TaskEntity taskEntity = null;
		for (int i = 0; i < subTasks.size(); i++) {
			taskEntity = (TaskEntity) subTasks.get(i);
			if (ArrayUtils.contains(subTaskPks, taskEntity.getTaskPk())) {
				taskEntity.delete();
			}
		}
		return null;
	}
}
