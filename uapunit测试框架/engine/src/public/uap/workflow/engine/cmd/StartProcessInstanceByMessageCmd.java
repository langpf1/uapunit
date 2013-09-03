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
import java.util.Map;

import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.deploy.DeploymentCache;
import uap.workflow.engine.entity.MessageEventSubscriptionEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
/**
 * @author Daniel Meyer
 */
public class StartProcessInstanceByMessageCmd implements Command<IProcessInstance> {
	protected final String messageName;
	protected final Map<String, Object> processVariables;
	public StartProcessInstanceByMessageCmd(String messageName, Map<String, Object> processVariables) {
		this.messageName = messageName;
		this.processVariables = processVariables;
	}
	public IProcessInstance execute(CommandContext commandContext) {
		if (messageName == null) {
			throw new WorkflowException("Cannot start process instance by message: message name is null");
		}
		MessageEventSubscriptionEntity messageEventSubscription = commandContext.getEventSubscriptionManager().findMessageStartEventSubscriptionByName(messageName);
		if (messageEventSubscription == null) {
			throw new WorkflowException("Cannot start process instance by message: no subscription to message with name '" + messageName + "' found.");
		}
		String processDefinitionId = messageEventSubscription.getConfiguration();
		if (processDefinitionId == null) {
			throw new WorkflowException("Cannot start process instance by message: subscription to message with name '" + messageName + "' is not a message start event.");
		}
		DeploymentCache deploymentCache = Context.getProcessEngineConfiguration().getDeploymentCache();
		IProcessDefinition processDefinition = deploymentCache.getProDefByProDefPk(processDefinitionId);
		if (processDefinition == null) {
			throw new WorkflowException("No process definition found for id '" + processDefinitionId + "'");
		}
		IActivity startActivity = processDefinition.findActivity(messageEventSubscription.getActivityId());
		IProcessInstance processInstance = processDefinition.createProcessInstance(startActivity);
		if (processVariables != null) {
			processInstance.setVariables(processVariables);
		}
		processInstance.start();
		return processInstance;
	}
}
