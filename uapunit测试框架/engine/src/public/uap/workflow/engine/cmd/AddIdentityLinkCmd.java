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
import uap.workflow.engine.entity.CommentEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.identity.Authentication;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.mgr.CommentManager;
import uap.workflow.engine.task.Event;
import uap.workflow.engine.task.IdentityLinkType;
import uap.workflow.engine.util.ClockUtil;
/**
 * @author Joram Barrez
 */
public class AddIdentityLinkCmd implements Command<Void>, Serializable {
	private static final long serialVersionUID = 1L;
	protected String taskId;
	protected String userId;
	protected String groupId;
	protected String type;
	public AddIdentityLinkCmd(String taskId, String userId, String groupId, String type) {
		validateParams(userId, groupId, type, taskId);
		this.taskId = taskId;
		this.userId = userId;
		this.groupId = groupId;
		this.type = type;
	}
	protected void validateParams(String userId, String groupId, String type, String taskId) {
		if (taskId == null) {
			throw new WorkflowException("taskId is null");
		}
		if (type == null) {
			throw new WorkflowException("type is required when adding a new task identity link");
		}
		// Special treatment for assignee, group cannot be used an userId may be
		// null
		if (IdentityLinkType.ASSIGNEE.equals(type)) {
			if (groupId != null) {
				throw new WorkflowException("Incompatible usage: cannot use ASSIGNEE" + " together with a groupId");
			}
		} else {
			if (userId == null && groupId == null) {
				throw new WorkflowException("userId and groupId cannot both be null");
			}
		}
	}
	public Void execute(CommandContext commandContext) {
		ITask task = Context.getCommandContext().getTaskManager().getTaskByTaskPk(taskId);
		if (task == null) {
			throw new WorkflowException("Cannot find task with id " + taskId);
		}
		if (IdentityLinkType.ASSIGNEE.equals(type)) {
			// task.setAssignee(userId);
		} else if (IdentityLinkType.OWNER.equals(type)) {
			task.setOwner(userId);
		} else {
			// task.addIdentityLink(userId, groupId, type);
		}
		CommentManager commentManager = commandContext.getCommentManager();
		if (commentManager.isHistoryEnabled()) {
			String authenticatedUserId = Authentication.getAuthenticatedUserId();
			CommentEntity comment = new CommentEntity();
			comment.setUserId(authenticatedUserId);
			comment.setType(CommentEntity.TYPE_EVENT);
			comment.setTime(ClockUtil.getCurrentTime());
			comment.setTaskId(taskId);
			if (userId != null) {
				comment.setAction(Event.ACTION_ADD_USER_LINK);
				comment.setMessage(new String[] { userId, type });
			} else {
				comment.setAction(Event.ACTION_ADD_GROUP_LINK);
				comment.setMessage(new String[] { groupId, type });
			}
			commentManager.insert(comment);
		}
		return null;
	}
}
