package uap.workflow.engine.context;
import uap.workflow.engine.core.TaskInstanceFinishMode;
/**
 * ROLLBACKʱ�Ĺ��������˴���UNAPPROVEʱ��������������
 */
public class CallBackTaskInsCtx extends StartedProInsCtx {
	private static final long serialVersionUID = 7085219518885100544L;
	@Override
	public void setTaskPk(String taskPk) {
		this.taskPk = taskPk;
	}
	@Override
	public TaskInstanceFinishMode getFinishType() {
		return null;
	}
}
