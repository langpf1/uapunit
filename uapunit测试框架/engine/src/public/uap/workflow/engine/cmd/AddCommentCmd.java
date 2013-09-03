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


import uap.workflow.engine.entity.CommentEntity;
import uap.workflow.engine.identity.Authentication;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.task.Event;
import uap.workflow.engine.util.ClockUtil;


/**
 * @author Tom Baeyens
 */
public class AddCommentCmd implements Command<Object>, Serializable {

  private static final long serialVersionUID = 1L;
  
  protected String taskId;
  protected String processInstanceId;
  protected String message;
  
  public AddCommentCmd(String taskId, String processInstanceId, String message) {
    this.taskId = taskId;
    this.processInstanceId = processInstanceId;
    this.message = message;
  }

  public Object execute(CommandContext commandContext) {
    String userId = Authentication.getAuthenticatedUserId();
    CommentEntity comment = new CommentEntity();
    comment.setUserId(userId);
    comment.setType(CommentEntity.TYPE_COMMENT);
    comment.setTime(ClockUtil.getCurrentTime());
    comment.setTaskId(taskId);
    comment.setProcessInstanceId(processInstanceId);
    comment.setAction(Event.ACTION_ADD_COMMENT);
    
    String eventMessage = message.replaceAll("\\s+", " ");
    if (eventMessage.length()>163) {
      eventMessage = eventMessage.substring(0, 160)+"...";
    }
    comment.setMessage(eventMessage);
    
    comment.setFullMessage(message);
    
    commandContext
      .getCommentManager()
      .insert(comment);
    
    return null;
  }
}
