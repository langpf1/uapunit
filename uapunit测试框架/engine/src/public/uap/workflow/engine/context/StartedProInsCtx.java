package uap.workflow.engine.context;
import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.engine.utils.StringUtil;
/**
 * 
 * @author tianchw
 * 
 */
public abstract class StartedProInsCtx extends FlowInfoCtx {
	private static final long serialVersionUID = 7775929721400640587L;
	protected String taskPk;
	public String getTaskPk() {
		return taskPk;
	}
	public void check() {
		if (StringUtil.isBlank(taskPk)) {
			//throw new WorkflowRuntimeException("���ṩִ�������Pk");
		}
	}
	abstract public void setTaskPk(String taskPk);
}
