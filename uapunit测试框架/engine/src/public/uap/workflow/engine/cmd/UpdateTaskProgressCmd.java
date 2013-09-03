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
import java.util.List;


import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.session.WorkflowContext;
/**
 * 更新任务进度命令 
 */
public class UpdateTaskProgressCmd implements Command<Object>, Serializable {
	private static final long serialVersionUID = 1L;
	protected String taskId;
	protected String finish;
	public UpdateTaskProgressCmd(String taskId) {
		this.taskId = taskId;
	}
	public Object execute(CommandContext commandContext) {
		if (taskId == null) {
			throw new WorkflowException("taskId is null");
		}
		ITask task = Context.getCommandContext().getTaskManager().getTaskByTaskPk(taskId);
		if (task == null) {
			throw new WorkflowException("Cannot find task with id " + taskId);
		}
		String opinion = "";
		FlowInfoCtx flowInfoCtx = WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx(); // 保存下一步骤的指派信息
		if (flowInfoCtx instanceof NextTaskInsCtx) {
			opinion = ((NextTaskInsCtx)flowInfoCtx).getComment();
		}
		task.updateProgress(finish, opinion);
		return null;
	}
}
