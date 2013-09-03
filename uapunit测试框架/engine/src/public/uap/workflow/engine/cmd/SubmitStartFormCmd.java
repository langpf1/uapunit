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
import java.util.Map;


import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
/**
 * @author Tom Baeyens
 */
public class SubmitStartFormCmd implements Command<IProcessInstance>, Serializable {
	private static final long serialVersionUID = 1L;
	protected String processDefinitionId = null;
	protected Map<String, String> properties = null;
	public SubmitStartFormCmd(String processDefinitionId, Map<String, String> properties) {
		this.processDefinitionId = processDefinitionId;
		this.properties = properties;
	}
	public IProcessInstance execute(CommandContext commandContext) {
		IProcessDefinition processDefinition = Context.getProcessEngineConfiguration().getDeploymentCache().getProDefByProDefId(processDefinitionId);
		if (processDefinition == null) {
			throw new WorkflowException("No process definition found for id = '" + processDefinitionId + "'");
		}
		IProcessInstance processInstance = processDefinition.createProcessInstance();
		// int historyLevel =
		// Context.getProcessEngineConfiguration().getHistoryLevel();
		// if (historyLevel >=
		// ProcessEngineConfigurationImpl.HISTORYLEVEL_ACTIVITY) {
		// DbSqlSession dbSqlSession =
		// commandContext.getSession(DbSqlSession.class);
		// if (historyLevel >=
		// ProcessEngineConfigurationImpl.HISTORYLEVEL_AUDIT) {
		// for (String propertyId : properties.keySet()) {
		// String propertyValue = properties.get(propertyId);
		// HistoricFormPropertyEntity historicFormProperty = new
		// HistoricFormPropertyEntity(processInstance, propertyId,
		// propertyValue);
		// dbSqlSession.insert(historicFormProperty);
		// }
		// }
		// }
		// StartFormHandler startFormHandler =
		// processDefinition.getStartFormHandler();
		// startFormHandler.submitFormProperties(properties, processInstance);
		processInstance.start();
		return processInstance;
	}
}
