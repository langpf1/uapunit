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
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.form.FormEngine;
import uap.workflow.engine.form.StartFormData;
import uap.workflow.engine.form.StartFormHandler;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class GetRenderedStartFormCmd implements Command<Object>, Serializable {
	private static final long serialVersionUID = 1L;
	protected String proDefPk;
	protected String formEngineName;
	public GetRenderedStartFormCmd(String proDefPk, String formEngineName) {
		this.proDefPk = proDefPk;
		this.formEngineName = formEngineName;
	}
	public Object execute(CommandContext commandContext) {
		IProcessDefinition processDefinition = Context.getProcessEngineConfiguration().getDeploymentCache().getProDefByProDefPk(proDefPk);
		if (processDefinition == null) {
			throw new WorkflowException("Process Definition '" + proDefPk + "' not found");
		}
		StartFormHandler startFormHandler = processDefinition.getStartFormHandler();
		if (startFormHandler == null) {
			return null;
		}
		FormEngine formEngine = Context.getProcessEngineConfiguration().getFormEngines().get(formEngineName);
		if (formEngine == null) {
			throw new WorkflowException("No formEngine '" + formEngineName + "' defined process engine configuration");
		}
		StartFormData startForm = startFormHandler.createStartFormData(processDefinition);
		return formEngine.renderStartForm(startForm);
	}
}
