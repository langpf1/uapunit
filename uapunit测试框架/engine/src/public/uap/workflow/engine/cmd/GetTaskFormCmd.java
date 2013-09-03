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
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.form.TaskFormData;
import uap.workflow.engine.form.TaskFormHandler;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
/**
 * @author Tom Baeyens
 */
public class GetTaskFormCmd implements Command<TaskFormData>, Serializable {
	private static final long serialVersionUID = 1L;
	protected String taskId;
	public GetTaskFormCmd(String taskId) {
		this.taskId = taskId;
	}
	public TaskFormData execute(CommandContext commandContext) {
		ITask task = Context.getCommandContext().getTaskManager().getTaskByTaskPk(taskId);
		if (task == null) {
			throw new WorkflowException("No task found for taskId '" + taskId + "'");
		}
		if (task.getTaskDefinition() != null) {
			TaskFormHandler taskFormHandler = task.getTaskDefinition().getTaskFormHandler();
			if (taskFormHandler == null) {
				throw new WorkflowException("No taskFormHandler specified for task '" + taskId + "'");
			}
			return taskFormHandler.createTaskForm(task);
		} else {
			// Standalone task, no TaskFormData available
			return null;
		}
	}
}
