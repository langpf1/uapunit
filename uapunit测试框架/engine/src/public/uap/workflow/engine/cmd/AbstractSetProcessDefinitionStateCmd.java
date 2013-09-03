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
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.mgr.ProcessDefinitionManager;
/**
 * 
 * @author Daniel Meyer
 */
public abstract class AbstractSetProcessDefinitionStateCmd implements Command<Void> {
	protected final String processDefinitionId;
	private final String processDefinitionKey;
	public AbstractSetProcessDefinitionStateCmd(String processDefinitionId, String processDefinitionKey) {
		this.processDefinitionId = processDefinitionId;
		this.processDefinitionKey = processDefinitionKey;
	}
	public Void execute(CommandContext commandContext) {
		if (processDefinitionId == null && processDefinitionKey == null) {
			throw new WorkflowException("Process definition id / key cannot be null");
		}
		IProcessDefinition processDefinitionEntity = null;
		ProcessDefinitionManager processDefinitionManager = commandContext.getProcessDefinitionManager();
		if (processDefinitionId == null) {
			processDefinitionEntity = processDefinitionManager.getProDefByProDefPk(processDefinitionKey);
			if (processDefinitionEntity == null) {
				throw new WorkflowException("Cannot find process definition for key '" + processDefinitionKey + "'");
			}
		} else {
			processDefinitionEntity = processDefinitionManager.getProDefByProDefId(processDefinitionId);
			if (processDefinitionEntity == null) {
				throw new WorkflowException("Cannot find process definition for id '" + processDefinitionId + "'");
			}
		}
		setState(processDefinitionEntity);
		return null;
	}
	protected abstract void setState(IProcessDefinition processDefinitionEntity);
}
