package uap.workflow.engine.context;
import uap.workflow.engine.core.TaskInstanceFinishMode;
public class ReStartBeforeAddSignTaskInsCtx extends StartedProInsCtx {
	private static final long serialVersionUID = 3582398842054453999L;
	@Override
	public void setTaskPk(String taskPk) {
		this.taskPk = taskPk;
	}
	@Override
	public TaskInstanceFinishMode getFinishType() {
		return null;
	}
	public String getTaskPk() {
		return taskPk;
	}
}
