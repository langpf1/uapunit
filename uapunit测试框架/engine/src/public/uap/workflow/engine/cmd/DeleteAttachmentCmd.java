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


import uap.workflow.engine.entity.AttachmentEntity;
import uap.workflow.engine.entity.CommentEntity;
import uap.workflow.engine.identity.Authentication;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.mgr.CommentManager;
import uap.workflow.engine.task.Event;
import uap.workflow.engine.util.ClockUtil;


/**
 * @author Tom Baeyens
 */
public class DeleteAttachmentCmd implements Command<Object>, Serializable {

  private static final long serialVersionUID = 1L;
  protected String attachmentId;
  
  public DeleteAttachmentCmd(String attachmentId) {
    this.attachmentId = attachmentId;
  }

  public Object execute(CommandContext commandContext) {
    AttachmentEntity attachment = commandContext
      .getDbSqlSession()
      .selectById(AttachmentEntity.class, attachmentId);

    commandContext
      .getDbSqlSession()
      .delete(AttachmentEntity.class, attachmentId);
    
    if (attachment.getTaskId()!=null) {
      CommentManager commentManager = commandContext.getCommentManager();
      if (commentManager.isHistoryEnabled()) {
        String authenticatedUserId = Authentication.getAuthenticatedUserId();
        CommentEntity comment = new CommentEntity();
        comment.setUserId(authenticatedUserId);
        comment.setType(CommentEntity.TYPE_EVENT);
        comment.setTime(ClockUtil.getCurrentTime());
        comment.setAction(Event.ACTION_DELETE_ATTACHMENT);
        comment.setMessage(attachment.getName());
        comment.setTaskId(attachment.getTaskId());
        commentManager.insert(comment);
      }
    }

    return null;
  }

}
