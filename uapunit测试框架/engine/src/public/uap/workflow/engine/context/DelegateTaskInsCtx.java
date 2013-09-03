package uap.workflow.engine.context;
import java.util.List;

import uap.workflow.engine.core.TaskInstanceFinishMode;
public class DelegateTaskInsCtx extends StartedProInsCtx {
	private static final long serialVersionUID = 8564323238863628337L;
	private List<String> turnUserPks = null;
	@Override
	public void setTaskPk(String taskPk) {
		this.taskPk = taskPk;
	}
	public List<String> getTurnUserPks() {
		return turnUserPks;
	}
	public void setTurnUserPk(List<String> turnUserPks) {
		this.turnUserPks = turnUserPks;
	}
	@Override
	public TaskInstanceFinishMode getFinishType() {
		return TaskInstanceFinishMode.Tramsmit;
	}
}
