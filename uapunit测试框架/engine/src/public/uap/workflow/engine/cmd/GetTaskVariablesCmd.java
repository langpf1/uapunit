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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
/**
 * @author Tom Baeyens
 */
public class GetTaskVariablesCmd implements Command<Map<String, Object>>, Serializable {
	private static final long serialVersionUID = 1L;
	protected String taskId;
	protected Collection<String> variableNames;
	protected boolean isLocal;
	public GetTaskVariablesCmd(String taskId, Collection<String> variableNames, boolean isLocal) {
		this.taskId = taskId;
		this.variableNames = variableNames;
		this.isLocal = isLocal;
	}
	public Map<String, Object> execute(CommandContext commandContext) {
		if (taskId == null) {
			throw new WorkflowException("taskId is null");
		}
		ITask task = Context.getCommandContext().getTaskManager().getTaskByTaskPk(taskId);
		if (task == null) {
			throw new WorkflowException("task " + taskId + " doesn't exist");
		}
		Map<String, Object> taskVariables;
		if (isLocal) {
			taskVariables = task.getVariablesLocal();
		} else {
			taskVariables = task.getVariables();
		}
		if (variableNames == null) {
			variableNames = taskVariables.keySet();
		}
		// this copy is made to avoid lazy initialization outside a command
		// context
		Map<String, Object> variables = new HashMap<String, Object>();
		for (String variableName : variableNames) {
			variables.put(variableName, task.getVariable(variableName));
		}
		return variables;
	}
}
