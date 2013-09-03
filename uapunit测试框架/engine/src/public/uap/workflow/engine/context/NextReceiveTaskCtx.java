package uap.workflow.engine.context;
import uap.workflow.engine.core.TaskInstanceFinishMode;
public class NextReceiveTaskCtx extends StartedProInsCtx {
	private static final long serialVersionUID = -6994550226191254707L;
	protected UserTaskRunTimeCtx[] nextInfo;
	private String executionId = null;
	public String getExecutionId() {
		return executionId;
	}
	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}
	@Override
	public void setTaskPk(String taskPk) {
		this.taskPk = taskPk;
	}
	@Override
	public TaskInstanceFinishMode getFinishType() {
		return TaskInstanceFinishMode.Normal;
	}
	public UserTaskRunTimeCtx[] getNextInfo() {
		return nextInfo;
	}
	public void setNextInfo(UserTaskRunTimeCtx[] nextInfo) {
		this.nextInfo = nextInfo;
	}
}
