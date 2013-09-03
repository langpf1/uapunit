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
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.workflow.engine.cmd.AddCommentCmd;
import uap.workflow.engine.cmd.AddIdentityLinkCmd;
import uap.workflow.engine.cmd.CallBackTaskCmd;
import uap.workflow.engine.cmd.ClaimTaskCmd;
import uap.workflow.engine.cmd.CompleteTaskCmd;
import uap.workflow.engine.cmd.CreateAttachmentCmd;
import uap.workflow.engine.cmd.DelegateTaskCmd;
import uap.workflow.engine.cmd.DeleteAttachmentCmd;
import uap.workflow.engine.cmd.DeleteTaskCmd;
import uap.workflow.engine.cmd.GetAttachmentCmd;
import uap.workflow.engine.cmd.GetAttachmentContentCmd;
import uap.workflow.engine.cmd.GetProcessInstanceAttachmentsCmd;
import uap.workflow.engine.cmd.GetProcessInstanceCommentsCmd;
import uap.workflow.engine.cmd.GetSubTasksCmd;
import uap.workflow.engine.cmd.GetTaskAttachmentsCmd;
import uap.workflow.engine.cmd.GetTaskCommentsCmd;
import uap.workflow.engine.cmd.GetTaskEventsCmd;
import uap.workflow.engine.cmd.GetTaskVariableCmd;
import uap.workflow.engine.cmd.GetTaskVariablesCmd;
import uap.workflow.engine.cmd.RejectTaskCmd;
import uap.workflow.engine.cmd.ResolveTaskCmd;
import uap.workflow.engine.cmd.SaveAttachmentCmd;
import uap.workflow.engine.cmd.SaveTaskCmd;
import uap.workflow.engine.cmd.SetTaskPriorityCmd;
import uap.workflow.engine.cmd.SetTaskVariablesCmd;
import uap.workflow.engine.cmd.TakeBackTaskCmd;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.runtime.TaskQuery;
import uap.workflow.engine.service.TaskService;
import uap.workflow.engine.task.Attachment;
import uap.workflow.engine.task.Comment;
import uap.workflow.engine.task.Event;
import uap.workflow.engine.task.IdentityLinkType;
/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class TaskServiceImpl extends ServiceImpl implements TaskService {
	public void saveTask(ITask task) {
		commandExecutor.execute(new SaveTaskCmd(task));
	}
	public void deleteTask(String taskId) {
		commandExecutor.execute(new DeleteTaskCmd(taskId, false));
	}
	public void deleteTasks(Collection<String> taskIds) {
		commandExecutor.execute(new DeleteTaskCmd(taskIds, false));
	}
	public void deleteTask(String taskId, boolean cascade) {
		commandExecutor.execute(new DeleteTaskCmd(taskId, cascade));
	}
	public void deleteTasks(Collection<String> taskIds, boolean cascade) {
		commandExecutor.execute(new DeleteTaskCmd(taskIds, cascade));
	}
	public void setAssignee(String taskId, String userId) {
		commandExecutor.execute(new AddIdentityLinkCmd(taskId, userId, null, IdentityLinkType.ASSIGNEE));
	}
	public void setOwner(String taskId, String userId) {
		commandExecutor.execute(new AddIdentityLinkCmd(taskId, userId, null, IdentityLinkType.OWNER));
	}
	public void claim(String taskId, String userId) {
		commandExecutor.execute(new ClaimTaskCmd(taskId, userId));
	}
	public void completeTask(String taskId) {
		commandExecutor.execute(new CompleteTaskCmd(taskId, null));
	}
	public void completeTask(String taskId, Map<String, Object> variables) {
		commandExecutor.execute(new CompleteTaskCmd(taskId, variables));
	}
	public void delegateTask(String taskId, List<String> userIds) {
		commandExecutor.execute(new DelegateTaskCmd(taskId, userIds));
	}
	public void resolveTask(String taskId) {
		commandExecutor.execute(new ResolveTaskCmd(taskId, null));
	}
	public void resolve(String taskId, Map<String, Object> variables) {
		commandExecutor.execute(new ResolveTaskCmd(taskId, variables));
	}
	public void setPriority(String taskId, int priority) {
		commandExecutor.execute(new SetTaskPriorityCmd(taskId, priority));
	}
	public List<ITask> getSubTasks(String parentTaskId) {
		return commandExecutor.execute(new GetSubTasksCmd(parentTaskId));
	}
	public void callBackTask(String taskId) {
		commandExecutor.execute(new CallBackTaskCmd(taskId));
	}
	public void takeBackTask(String taskId, String[] backIds) {
		commandExecutor.execute(new TakeBackTaskCmd(taskId, backIds));
	}
	public void rejectTask(String taskPk, String activitiId, String[] userPks) {
		commandExecutor.execute(new RejectTaskCmd(taskPk, activitiId, userPks));
	}
	public Map<String, Object> getVariables(String executionId) {
		return commandExecutor.execute(new GetTaskVariablesCmd(executionId, null, false));
	}
	public Map<String, Object> getVariablesLocal(String executionId) {
		return commandExecutor.execute(new GetTaskVariablesCmd(executionId, null, true));
	}
	public Map<String, Object> getVariables(String executionId, Collection<String> variableNames) {
		return commandExecutor.execute(new GetTaskVariablesCmd(executionId, variableNames, false));
	}
	public Map<String, Object> getVariablesLocal(String executionId, Collection<String> variableNames) {
		return commandExecutor.execute(new GetTaskVariablesCmd(executionId, variableNames, true));
	}
	public Object getVariable(String executionId, String variableName) {
		return commandExecutor.execute(new GetTaskVariableCmd(executionId, variableName, false));
	}
	public Object getVariableLocal(String executionId, String variableName) {
		return commandExecutor.execute(new GetTaskVariableCmd(executionId, variableName, true));
	}
	public void setVariable(String executionId, String variableName, Object value) {
		if (variableName == null) {
			throw new WorkflowException("variableName is null");
		}
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(variableName, value);
		commandExecutor.execute(new SetTaskVariablesCmd(executionId, variables, false));
	}
	public void setVariableLocal(String executionId, String variableName, Object value) {
		if (variableName == null) {
			throw new WorkflowException("variableName is null");
		}
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(variableName, value);
		commandExecutor.execute(new SetTaskVariablesCmd(executionId, variables, true));
	}
	public void setVariables(String executionId, Map<String, ? extends Object> variables) {
		commandExecutor.execute(new SetTaskVariablesCmd(executionId, variables, false));
	}
	public void setVariablesLocal(String executionId, Map<String, ? extends Object> variables) {
		commandExecutor.execute(new SetTaskVariablesCmd(executionId, variables, true));
	}
	public void addComment(String taskId, String processInstance, String message) {
		commandExecutor.execute(new AddCommentCmd(taskId, processInstance, message));
	}
	public List<Comment> getTaskComments(String taskId) {
		return commandExecutor.execute(new GetTaskCommentsCmd(taskId));
	}
	public List<Event> getTaskEvents(String taskId) {
		return commandExecutor.execute(new GetTaskEventsCmd(taskId));
	}
	public List<Comment> getProcessInstanceComments(String processInstanceId) {
		return commandExecutor.execute(new GetProcessInstanceCommentsCmd(processInstanceId));
	}
	public Attachment createAttachment(String attachmentType, String taskId, String processInstanceId, String attachmentName, String attachmentDescription, InputStream content) {
		return commandExecutor.execute(new CreateAttachmentCmd(attachmentType, taskId, processInstanceId, attachmentName, attachmentDescription, content, null));
	}
	public Attachment createAttachment(String attachmentType, String taskId, String processInstanceId, String attachmentName, String attachmentDescription, String url) {
		return commandExecutor.execute(new CreateAttachmentCmd(attachmentType, taskId, processInstanceId, attachmentName, attachmentDescription, null, url));
	}
	public InputStream getAttachmentContent(String attachmentId) {
		return commandExecutor.execute(new GetAttachmentContentCmd(attachmentId));
	}
	public void deleteAttachment(String attachmentId) {
		commandExecutor.execute(new DeleteAttachmentCmd(attachmentId));
	}
	public Attachment getAttachment(String attachmentId) {
		return commandExecutor.execute(new GetAttachmentCmd(attachmentId));
	}
	public List<Attachment> getTaskAttachments(String taskId) {
		return commandExecutor.execute(new GetTaskAttachmentsCmd(taskId));
	}
	public List<Attachment> getProcessInstanceAttachments(String processInstanceId) {
		return commandExecutor.execute(new GetProcessInstanceAttachmentsCmd(processInstanceId));
	}
	public void saveAttachment(Attachment attachment) {
		commandExecutor.execute(new SaveAttachmentCmd(attachment));
	}
	public TaskQuery createTaskQuery() {
		return new TaskQueryImpl(commandExecutor);
	}
}
