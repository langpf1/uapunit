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
import java.util.Map;

import uap.workflow.app.core.IBusinessKey;
import uap.workflow.app.core.IFlowRequest;
import uap.workflow.app.core.IFlowResponse;
import uap.workflow.engine.cmd.GetRenderedStartFormCmd;
import uap.workflow.engine.cmd.GetRenderedTaskFormCmd;
import uap.workflow.engine.cmd.GetStartFormCmd;
import uap.workflow.engine.cmd.GetTaskFormCmd;
import uap.workflow.engine.cmd.SubmitStartFormCmd;
import uap.workflow.engine.cmd.SubmitTaskFormCmd;
import uap.workflow.engine.context.CommitProInsCtx;
import uap.workflow.engine.context.SubmitStartFormCtx;
import uap.workflow.engine.context.SubmitTaskFormCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.form.StartFormData;
import uap.workflow.engine.form.TaskFormData;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.service.FormService;
/**
 * @author Tom Baeyens
 */
public class FormServiceImpl extends ServiceImpl implements FormService {
	public Object getRenderedStartForm(String processDefinitionId) {
		return commandExecutor.execute(new GetRenderedStartFormCmd(processDefinitionId, null));
	}
	public Object getRenderedStartForm(String processDefinitionId, String engineName) {
		return commandExecutor.execute(new GetRenderedStartFormCmd(processDefinitionId, engineName));
	}
	public Object getRenderedTaskForm(String taskId) {
		return commandExecutor.execute(new GetRenderedTaskFormCmd(taskId, null));
	}
	public Object getRenderedTaskForm(String taskId, String engineName) {
		return commandExecutor.execute(new GetRenderedTaskFormCmd(taskId, engineName));
	}
	public StartFormData getStartFormData(String processDefinitionId) {
		return commandExecutor.execute(new GetStartFormCmd(processDefinitionId));
	}
	public TaskFormData getTaskFormData(String taskId) {
		return commandExecutor.execute(new GetTaskFormCmd(taskId));
	}
	public IProcessInstance submitStartFormData(String processDefinitionId, Map<String, String> properties) {
		return commandExecutor.execute(new SubmitStartFormCmd(processDefinitionId, properties));
	}
	public IProcessInstance submitStartFormData(String processDefinitionId, String businessKey, Map<String, String> properties) {
		return commandExecutor.execute(new SubmitStartFormCmd(processDefinitionId, properties));
	}
	public void submitTaskFormData(String taskId, Map<String, String> properties) {
		commandExecutor.execute(new SubmitTaskFormCmd(taskId, properties));
	}
}
