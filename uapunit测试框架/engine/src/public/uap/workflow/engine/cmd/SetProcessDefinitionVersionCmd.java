/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uap.workflow.engine.cmd;
import java.io.Serializable;

import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.deploy.DeploymentCache;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.HistoricProcessInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.mgr.ExecutionManager;
import uap.workflow.engine.mgr.ProcessInstanceManager;
/**
 * {@link Command} that changes the process definition version of an existing
 * process instance.
 * 
 * Warning: This command will NOT perform any migration magic and simply set the
 * process definition version in the database, assuming that the user knows,
 * what he or she is doing.
 * 
 * This is only useful for simple migrations. The new process definition MUST
 * have the exact same activity id to make it still run.
 * 
 * Furthermore, activities referenced by sub-executions and jobs that belong to
 * the process instance MUST exist in the new process definition version.
 * 
 * 
 * If the process instance is not currently waiting but actively running, then
 * this would be a case for optimistic locking, meaning either the version
 * update or the "real work" wins, i.e., this is a race condition.
 * 
 * @see http://forums.activiti.org/en/viewtopic.php?t=2918
 * @author Falko Menge
 */
public class SetProcessDefinitionVersionCmd implements Command<Void>, Serializable {
	private static final long serialVersionUID = 1L;
	private final String processInstanceId;
	private final Integer processDefinitionVersion;
	public SetProcessDefinitionVersionCmd(String processInstanceId, Integer processDefinitionVersion) {
		if (processInstanceId == null || processInstanceId.length() < 1) {
			throw new WorkflowException("The process instance id is mandatory, but '" + processInstanceId + "' has been provided.");
		}
		if (processDefinitionVersion == null) {
			throw new WorkflowException("The process definition version is mandatory, but 'null' has been provided.");
		}
		if (processDefinitionVersion < 1) {
			throw new WorkflowException("The process definition version must be positive, but '" + processDefinitionVersion + "' has been provided.");
		}
		this.processInstanceId = processInstanceId;
		this.processDefinitionVersion = processDefinitionVersion;
	}
	public Void execute(CommandContext commandContext) {
		// check that the new process definition is just another version of the
		// same
		// process definition that the process instance is using
		ExecutionManager executionManager = commandContext.getExecutionManager();
		ActivityInstanceEntity processInstance = executionManager.getActInsByActInsPk(processInstanceId);
		if (processInstance == null) {
			throw new WorkflowException("No process instance found for id = '" + processInstanceId + "'.");
		}
		IProcessDefinition currentProcessDefinitionImpl = processInstance.getProcessDefinition();
		DeploymentCache deploymentCache = Context.getProcessEngineConfiguration().getDeploymentCache();
		IProcessDefinition currentProcessDefinition;
		currentProcessDefinition = deploymentCache.getProDefByProDefPk(currentProcessDefinitionImpl.getProDefPk());
		IProcessDefinition newProcessDefinition = deploymentCache.findDeployedProcessDefinitionByKeyAndVersion(currentProcessDefinition.getProDefPk(), processDefinitionVersion);
		// check that the new process definition version contains the current
		// activity
		if (!newProcessDefinition.contains(processInstance.getActivity())) {
			throw new WorkflowException("The new process definition " + "(key = '" + newProcessDefinition.getProDefPk() + "') " + "does not contain the current activity " + "(id = '"
					+ processInstance.getActivity().getId() + "') " + "of the process instance " + "(id = '" + processInstanceId + "').");
		}
		// switch the process instance to the new process definition version
		// processInstance.setProcessDefinition(newProcessDefinition);
		// switch the historic process instance to the new process definition
		// version
		ProcessInstanceManager historicProcessInstanceManager = commandContext.getProcessInstanceManager();
		if (historicProcessInstanceManager.isHistoryEnabled()) {
			HistoricProcessInstanceEntity historicProcessInstance = historicProcessInstanceManager.findHistoricProcessInstance(processInstanceId);
			historicProcessInstance.setProcessDefinitionId(newProcessDefinition.getId());
		}
		// switch all sub-executions of the process instance to the new process
		// definition version
		// List<PvmActivitiExecution> childExecutions =
		// executionManager.findChildExecutionsByParentExecutionId(processInstanceId);
		// for (PvmActivitiExecution executionEntity : childExecutions) {
		// executionEntity.setProcessDefinition(newProcessDefinition);
		// }
		return null;
	}
}
