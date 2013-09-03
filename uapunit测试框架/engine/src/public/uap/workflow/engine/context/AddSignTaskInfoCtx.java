package uap.workflow.engine.context;
import uap.workflow.engine.core.TaskInstanceFinishMode;
import uap.workflow.engine.exception.WorkflowRuntimeException;
public class AddSignTaskInfoCtx extends StartedProInsCtx {
	private static final long serialVersionUID = -9122974872229254529L;
	private AddSignUserInfoCtx[] addSignUsers;
	public AddSignUserInfoCtx[] getAddSingUsers() {
		return addSignUsers;
	}
	public void setAddSignUsers(AddSignUserInfoCtx[] addSingUsers) {
		this.addSignUsers = addSingUsers;
	}
	public void check() {
		if (this.getTaskPk() == null || this.getTaskPk().length() == 0) {
			throw new WorkflowRuntimeException("任务加签必须提供任务Pk");
		}
	}
	@Override
	public void setTaskPk(String taskPk) {
		this.taskPk = taskPk;
	}
	@Override
	public TaskInstanceFinishMode getFinishType() {
		return null;
	}
}
