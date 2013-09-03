package uap.workflow.engine.cmd;

import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
public class FeatchProDefCmd implements Command<IProcessDefinition> {
	protected String proDefId = null;
	protected String proDefPk = null;
	public FeatchProDefCmd(String proDefId, String proDefPk) {
		this.proDefId = proDefId;
		this.proDefPk = proDefPk;
	}
	@Override
	public IProcessDefinition execute(CommandContext commandContext) {
		if (proDefId == null && proDefPk == null) {
			throw new WorkflowException("Process definition id / key cannot be null");
		}
		IProcessDefinition processDefinitionEntity = null;
		// ProcessDefinitionManager processDefinitionManager =
		// commandContext.getProcessDefinitionManager();
		if (proDefId == null) {
			processDefinitionEntity = Context.getProcessEngineConfiguration().getDeploymentCache().getProDefByProDefPk(proDefPk);
		} else {
			processDefinitionEntity = Context.getProcessEngineConfiguration().getDeploymentCache().getProDefByProDefId(proDefId);
		}
		return processDefinitionEntity;
	}
}
