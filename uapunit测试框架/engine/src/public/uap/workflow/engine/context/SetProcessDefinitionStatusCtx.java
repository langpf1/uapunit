package uap.workflow.engine.context;
import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.ProcessDefinitionStatusEnum;
import uap.workflow.engine.core.TaskInstanceFinishMode;
/**
 * 
 * @author tianchw
 * 
 */
public class SetProcessDefinitionStatusCtx extends FlowInfoCtx {
	private static final long serialVersionUID = 7775929721400640587L;
	private IProcessDefinition procDef;
	private ProcessDefinitionStatusEnum status;

	public void check() {
		if (procDef == null) {
			//throw new WorkflowRuntimeException("请提供执行任务的Pk");
		}
	}

	public IProcessDefinition getProcDef() {
		return procDef;
	}

	public void setProcDef(IProcessDefinition procDef) {
		this.procDef = procDef;
	}

	public ProcessDefinitionStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ProcessDefinitionStatusEnum status) {
		this.status = status;
	}

	@Override
	public TaskInstanceFinishMode getFinishType() {
		// TODO Auto-generated method stub
		return null;
	}
}
