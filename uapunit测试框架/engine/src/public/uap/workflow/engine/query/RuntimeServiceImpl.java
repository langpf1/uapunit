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
package uap.workflow.engine.query;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.workflow.engine.cmd.ActivateProcessInstanceCmd;
import uap.workflow.engine.cmd.DeleteProcessInstanceCmd;
import uap.workflow.engine.cmd.FindActiveActivityIdsCmd;
import uap.workflow.engine.cmd.GetExecutionVariableCmd;
import uap.workflow.engine.cmd.GetExecutionVariablesCmd;
import uap.workflow.engine.cmd.GetStartFormCmd;
import uap.workflow.engine.cmd.SetExecutionVariablesCmd;
import uap.workflow.engine.cmd.SignalCmd;
import uap.workflow.engine.cmd.SignalEventReceivedCmd;
import uap.workflow.engine.cmd.StartProcessInstanceByMessageCmd;
import uap.workflow.engine.cmd.StartProcessInstanceCmd;
import uap.workflow.engine.cmd.SuspendProcessInstanceCmd;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.form.FormData;
import uap.workflow.engine.runtime.ExecutionQuery;
import uap.workflow.engine.runtime.ProcessInstanceQuery;
import uap.workflow.engine.service.RuntimeService;
/**
 * @author Tom Baeyens
 * @author Daniel Meyer
 */
public class RuntimeServiceImpl extends ServiceImpl implements RuntimeService {
	public IProcessInstance startProcessInstanceByKey(String proDefPk) {
		return commandExecutor.execute(new StartProcessInstanceCmd<IProcessInstance>(proDefPk, null, null));
	}
	public IProcessInstance startProcessInstanceByKey(String proDefPk, Map<String, Object> variables) {
		return commandExecutor.execute(new StartProcessInstanceCmd<IProcessInstance>(proDefPk, null, variables));
	}
	public IProcessInstance startProcessInstanceById(String proDefId) {
		return commandExecutor.execute(new StartProcessInstanceCmd<IProcessInstance>(null, proDefId, null));
	}
	public IProcessInstance startProcessInstanceById(String proDefId, Map<String, Object> variables) {
		return commandExecutor.execute(new StartProcessInstanceCmd<IProcessInstance>(null, proDefId, variables));
	}
	public void deleteProcessInstance(String processInstanceId, String deleteReason) {
		commandExecutor.execute(new DeleteProcessInstanceCmd(processInstanceId, deleteReason));
	}
	public ExecutionQuery createExecutionQuery() {
		return new ExecutionQueryImpl(commandExecutor);
	}
	public Map<String, Object> getVariables(String executionId) {
		return commandExecutor.execute(new GetExecutionVariablesCmd(executionId, null, false));
	}
	public Map<String, Object> getVariablesLocal(String executionId) {
		return commandExecutor.execute(new GetExecutionVariablesCmd(executionId, null, true));
	}
	public Map<String, Object> getVariables(String executionId, Collection<String> variableNames) {
		return commandExecutor.execute(new GetExecutionVariablesCmd(executionId, variableNames, false));
	}
	public Map<String, Object> getVariablesLocal(String executionId, Collection<String> variableNames) {
		return commandExecutor.execute(new GetExecutionVariablesCmd(executionId, variableNames, true));
	}
	public Object getVariable(String executionId, String variableName) {
		return commandExecutor.execute(new GetExecutionVariableCmd(executionId, variableName, false));
	}
	public Object getVariableLocal(String executionId, String variableName) {
		return commandExecutor.execute(new GetExecutionVariableCmd(executionId, variableName, true));
	}
	public void setVariable(String executionId, String variableName, Object value) {
		if (variableName == null) {
			throw new WorkflowException("variableName is null");
		}
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(variableName, value);
		commandExecutor.execute(new SetExecutionVariablesCmd(executionId, variables, false));
	}
	public void setVariableLocal(String executionId, String variableName, Object value) {
		if (variableName == null) {
			throw new WorkflowException("variableName is null");
		}
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(variableName, value);
		commandExecutor.execute(new SetExecutionVariablesCmd(executionId, variables, true));
	}
	public void setVariables(String executionId, Map<String, ? extends Object> variables) {
		commandExecutor.execute(new SetExecutionVariablesCmd(executionId, variables, false));
	}
	public void setVariablesLocal(String executionId, Map<String, ? extends Object> variables) {
		commandExecutor.execute(new SetExecutionVariablesCmd(executionId, variables, true));
	}
	public void signal(String executionId) {
		commandExecutor.execute(new SignalCmd(executionId, null, null, null));
	}
	public void signal(String executionId, Map<String, Object> processVariables) {
		commandExecutor.execute(new SignalCmd(executionId, null, null, processVariables));
	}
	public ProcessInstanceQuery createProcessInstanceQuery() {
		return new ProcessInstanceQueryImpl(commandExecutor);
	}
	public List<String> getActiveActivityIds(String executionId) {
		return commandExecutor.execute(new FindActiveActivityIdsCmd(executionId));
	}
	public FormData getFormInstanceById(String processDefinitionId) {
		return commandExecutor.execute(new GetStartFormCmd(processDefinitionId));
	}
	public void suspendProcessInstanceById(String processInstanceId) {
		commandExecutor.execute(new SuspendProcessInstanceCmd(processInstanceId));
	}
	public void activateProcessInstanceById(String processInstanceId) {
		commandExecutor.execute(new ActivateProcessInstanceCmd(processInstanceId));
	}
	public IProcessInstance startProcessInstanceByMessage(String messageName) {
		return commandExecutor.execute(new StartProcessInstanceByMessageCmd(messageName, null));
	}
	public IProcessInstance startProcessInstanceByMessage(String messageName, Map<String, Object> processVariables) {
		return commandExecutor.execute(new StartProcessInstanceByMessageCmd(messageName, processVariables));
	}
	public void signalEventReceived(String signalName) {
		commandExecutor.execute(new SignalEventReceivedCmd(signalName, null, null));
	}
	public void signalEventReceived(String signalName, Map<String, Object> processVariables) {
		commandExecutor.execute(new SignalEventReceivedCmd(signalName, null, processVariables));
	}
	public void signalEventReceived(String signalName, String executionId) {
		commandExecutor.execute(new SignalEventReceivedCmd(signalName, executionId, null));
	}
	public void signalEventReceived(String signalName, String executionId, Map<String, Object> processVariables) {
		commandExecutor.execute(new SignalEventReceivedCmd(signalName, executionId, processVariables));
	}
}
