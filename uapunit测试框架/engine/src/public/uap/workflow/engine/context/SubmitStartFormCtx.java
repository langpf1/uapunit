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
public class SubmitStartFormCtx extends FlowInfoCtx {
	private static final long serialVersionUID = 7775929721400640587L;
	private String processDefinitionId;
	private Map<String, String> properties;

	public void check() {
		if (StringUtil.isBlank(processDefinitionId)) {
			//throw new WorkflowRuntimeException("请提供执行任务的Pk");
		}
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
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
