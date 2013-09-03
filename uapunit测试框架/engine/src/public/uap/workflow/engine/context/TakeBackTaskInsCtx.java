package uap.workflow.engine.context;
import uap.workflow.engine.core.TaskInstanceFinishMode;
public class TakeBackTaskInsCtx extends StartedProInsCtx {
	private static final long serialVersionUID = 7640753231594552557L;
	private String[] subTaskPks;
	@Override
	public void setTaskPk(String taskPk) {
		this.taskPk = taskPk;
	}
	public String[] getSubTaskPks() {
		return subTaskPks;
	}
	public void setSubTaskPks(String[] subTaskPks) {
		this.subTaskPks = subTaskPks;
	}
	@Override
	public TaskInstanceFinishMode getFinishType() {
		return null;
	}
}
