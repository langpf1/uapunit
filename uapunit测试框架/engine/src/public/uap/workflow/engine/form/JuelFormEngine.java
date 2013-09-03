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
package uap.workflow.engine.form;

import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.entity.ResourceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.scripting.ScriptingEngines;
/**
 * @author Tom Baeyens
 */
public class JuelFormEngine implements FormEngine {
	public String getName() {
		return "juel";
	}
	public Object renderStartForm(StartFormData startForm) {
		if (startForm.getFormKey() == null) {
			return null;
		}
		String formTemplateString = getFormTemplateString(startForm);
		ScriptingEngines scriptingEngines = Context.getProcessEngineConfiguration().getScriptingEngines();
		return scriptingEngines.evaluate(formTemplateString, ScriptingEngines.DEFAULT_SCRIPTING_LANGUAGE, null);
	}
	public Object renderTaskForm(TaskFormData taskForm) { 
		if (taskForm.getFormKey() == null) {
			return null;
		}
		String formTemplateString = getFormTemplateString(taskForm);
		ScriptingEngines scriptingEngines = Context.getProcessEngineConfiguration().getScriptingEngines();
		ITask task = (ITask) taskForm.getTask();
		return scriptingEngines.evaluate(formTemplateString, ScriptingEngines.DEFAULT_SCRIPTING_LANGUAGE, task.getExecution());
	}
	private String getFormTemplateString(FormData formInstance) {
		String deploymentId = formInstance.getDeploymentId();
		String formKey = formInstance.getFormKey();
		ResourceEntity resourceStream = Context.getCommandContext().getResourceManager().findResourceByDeploymentIdAndResourceName(deploymentId, formKey);
		if (resourceStream == null) {
			throw new WorkflowException("Form with formKey '" + formKey + "' does not exist");
		}
		byte[] resourceBytes = resourceStream.getBytes();
		String formTemplateString = new String(resourceBytes);
		return formTemplateString;
	}
}
