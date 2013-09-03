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
package uap.workflow.engine.service;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.runtime.TaskQuery;
import uap.workflow.engine.task.Attachment;
import uap.workflow.engine.task.Comment;
import uap.workflow.engine.task.Event;
/**
 * Service which provides access to {@link ITask} and form related operations.
 * 
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public interface TaskService {
	void saveTask(ITask task);
	void deleteTask(String taskId);
	void deleteTasks(Collection<String> taskIds);
	void deleteTask(String taskId, boolean cascade);
	void deleteTasks(Collection<String> taskIds, boolean cascade);
	void claim(String taskId, String userId);
	void resolveTask(String taskId);
	void completeTask(String taskId, Map<String, Object> variables);
	void completeTask(String taskId);
	void delegateTask(String taskId, List<String> userIds);
	void callBackTask(String taskId);
	void takeBackTask(String taskId, String[] backIds);
	void rejectTask(String taskPk, String activitiId, String[] userPks);
	void setAssignee(String taskId, String userId);
	void setOwner(String taskId, String userId);
	void setPriority(String taskId, int priority);
	TaskQuery createTaskQuery();
	void setVariable(String taskId, String variableName, Object value);
	void setVariables(String taskId, Map<String, ? extends Object> variables);
	void setVariableLocal(String taskId, String variableName, Object value);
	void setVariablesLocal(String taskId, Map<String, ? extends Object> variables);
	Object getVariable(String taskId, String variableName);
	Object getVariableLocal(String taskId, String variableName);
	Map<String, Object> getVariables(String taskId);
	Map<String, Object> getVariablesLocal(String taskId);
	Map<String, Object> getVariables(String taskId, Collection<String> variableNames);
	Map<String, Object> getVariablesLocal(String taskId, Collection<String> variableNames);
	void addComment(String taskId, String processInstanceId, String message);
	List<Comment> getTaskComments(String taskId);
	List<Event> getTaskEvents(String taskId);
	List<Comment> getProcessInstanceComments(String processInstanceId);
	Attachment createAttachment(String attachmentType, String taskId, String processInstanceId, String attachmentName, String attachmentDescription, InputStream content);
	Attachment createAttachment(String attachmentType, String taskId, String processInstanceId, String attachmentName, String attachmentDescription, String url);
	void saveAttachment(Attachment attachment);
	Attachment getAttachment(String attachmentId);
	InputStream getAttachmentContent(String attachmentId);
	List<Attachment> getTaskAttachments(String taskId);
	List<Attachment> getProcessInstanceAttachments(String processInstanceId);
	void deleteAttachment(String attachmentId);
	List<ITask> getSubTasks(String parentTaskId);
}
