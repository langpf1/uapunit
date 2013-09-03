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

import java.io.InputStream;


import uap.workflow.engine.db.DbSqlSession;
import uap.workflow.engine.entity.AttachmentEntity;
import uap.workflow.engine.entity.ByteArrayEntity;
import uap.workflow.engine.entity.CommentEntity;
import uap.workflow.engine.identity.Authentication;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.io.IoUtil;
import uap.workflow.engine.mgr.CommentManager;
import uap.workflow.engine.task.Attachment;
import uap.workflow.engine.task.Event;
import uap.workflow.engine.util.ClockUtil;


/**
 * @author Tom Baeyens
 */
// Not Serializable
public class CreateAttachmentCmd implements Command<Attachment> {  
  
  protected String attachmentType;
  protected String taskId;
  protected String processInstanceId;
  protected String attachmentName;
  protected String attachmentDescription;
  protected InputStream content;
  protected String url;
  
  public CreateAttachmentCmd(String attachmentType, String taskId, String processInstanceId, String attachmentName, String attachmentDescription, InputStream content, String url) {
    this.attachmentType = attachmentType;
    this.taskId = taskId;
    this.processInstanceId = processInstanceId;
    this.attachmentName = attachmentName;
    this.attachmentDescription = attachmentDescription;
    this.content = content;
    this.url = url;
  }

  public Attachment execute(CommandContext commandContext) {
    AttachmentEntity attachment = new AttachmentEntity();
    attachment.setName(attachmentName);
    attachment.setDescription(attachmentDescription);
    attachment.setType(attachmentType);
    attachment.setTaskId(taskId);
    attachment.setProcessInstanceId(processInstanceId);
    attachment.setUrl(url);
    
    DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
    dbSqlSession.insert(attachment);
    
    if (content!=null) {
      byte[] bytes = IoUtil.readInputStream(content, attachmentName);
      ByteArrayEntity byteArray = new ByteArrayEntity(bytes);
      dbSqlSession.insert(byteArray);
      attachment.setContentId(byteArray.getId());
    }

    CommentManager commentManager = commandContext.getCommentManager();
    if (commentManager.isHistoryEnabled()) {
      String userId = Authentication.getAuthenticatedUserId();
      CommentEntity comment = new CommentEntity();
      comment.setUserId(userId);
      comment.setType(CommentEntity.TYPE_EVENT);
      comment.setTime(ClockUtil.getCurrentTime());
      comment.setTaskId(taskId);
      comment.setProcessInstanceId(processInstanceId);
      comment.setAction(Event.ACTION_ADD_ATTACHMENT);
      comment.setMessage(attachmentName);
      commentManager.insert(comment);
    }
    
    return attachment;
  }

}
