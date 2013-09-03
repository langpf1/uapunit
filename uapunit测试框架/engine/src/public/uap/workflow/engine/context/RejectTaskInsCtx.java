package uap.workflow.engine.context;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.TaskInstanceFinishMode;
import uap.workflow.engine.utils.TaskUtil;
/**
 * 
 * @author tianchw
 * 
 */
public class RejectTaskInsCtx extends StartedProInsCtx {
	private static final long serialVersionUID = -8465525455658888162L;
	protected UserTaskRunTimeCtx rejectInfo;
	@Override
	public void setTaskPk(String taskPk) {
		this.taskPk = taskPk;
	}
	public UserTaskRunTimeCtx getRejectInfo() {
		return rejectInfo;
	}
	public void setRejectInfo(UserTaskRunTimeCtx rejectInfo) {
		this.rejectInfo = rejectInfo;
	}
	@Override
	public TaskInstanceFinishMode getFinishType() {
		return TaskInstanceFinishMode.Reject;
	}
	public boolean isRejectBillMaker() {
		ITask task = TaskUtil.getTaskByTaskPk(taskPk);
		IActivity activity = task.getProcessDefinition().findActivity(rejectInfo.getActivityId());
		IActivity inial = task.getProcessDefinition().getInitial();
		if (activity == inial) {
			return true;
		} else {
			return false;
		}
	}
}
