package uap.workflow.engine.context;
import java.util.Map;

import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.engine.core.TaskInstanceFinishMode;
import uap.workflow.engine.utils.StringUtil;
/**
 * 
 * @author tianchw
 * 
 */
public class SubmitTaskFormCtx extends FlowInfoCtx {
	private static final long serialVersionUID = 7775929721400640587L;
	private String taskPk;
	private Map<String, String> properties;

	public void check() {
		if (StringUtil.isBlank(taskPk)) {
			//throw new WorkflowRuntimeException("请提供执行任务的Pk");
		}
	}

	public String getTaskPk() {
		return taskPk;
	}

	public void setTaskPk(String taskPk) {
		this.taskPk = taskPk;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	@Override
	public TaskInstanceFinishMode getFinishType() {
		// TODO Auto-generated method stub
		return null;
	}
}
