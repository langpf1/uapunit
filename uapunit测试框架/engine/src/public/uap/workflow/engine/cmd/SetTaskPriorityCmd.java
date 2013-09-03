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
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
/**
 * @author Joram Barrez
 */
public class SetTaskPriorityCmd implements Command<Void>, Serializable {
	private static final long serialVersionUID = 1L;
	protected String taskId;
	protected int priority;
	public SetTaskPriorityCmd(String taskId, int priority) {
		this.taskId = taskId;
		this.priority = priority;
	}
	public Void execute(CommandContext commandContext) {
		if (taskId == null) {
			throw new WorkflowException("taskId is null");
		}
		ITask task = Context.getCommandContext().getTaskManager().getTaskByTaskPk(taskId);
		if (task == null) {
			throw new WorkflowException("Cannot find task with id " + taskId);
		}
		//task.setPriority(priority);
		return null;
	}
}
