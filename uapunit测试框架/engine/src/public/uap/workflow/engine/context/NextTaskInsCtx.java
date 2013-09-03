package uap.workflow.engine.context;
import uap.workflow.engine.core.TaskInstanceFinishMode;
/**
 * 
 * @author tianchw
 * 
 */
public class NextTaskInsCtx extends StartedProInsCtx {
	private static final long serialVersionUID = 240337931135947443L;
	protected UserTaskRunTimeCtx[] nextInfo;
	protected boolean isMulti;
	private boolean isPass = true;
	
	public void setTaskPk(String taskPk) {
		this.taskPk = taskPk;
	}
	
	public UserTaskRunTimeCtx[] getNextInfo() {
		return nextInfo;
	}
	
	public void setNextInfo(UserTaskRunTimeCtx[] nextInfo) {
		this.nextInfo = nextInfo;
	}

	public TaskInstanceFinishMode getFinishType() {
		return TaskInstanceFinishMode.Normal;
	}
	
	public boolean isMulti() {
		return isMulti;
	}
	
	public void setMulti(boolean isMulti) {
		this.isMulti = isMulti;
	}

	public boolean isPass() {
		return isPass;
	}

	public void setPass(boolean isPass) {
		this.isPass = isPass;
	}
}
