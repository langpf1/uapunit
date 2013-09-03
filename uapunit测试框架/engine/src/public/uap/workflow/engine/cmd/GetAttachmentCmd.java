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
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.task.Attachment;


/**
 * @author Tom Baeyens
 */
public class GetAttachmentCmd implements Command<Attachment>, Serializable {

  private static final long serialVersionUID = 1L;
  protected String attachmentId;
  
  public GetAttachmentCmd(String attachmentId) {
    this.attachmentId = attachmentId;
  }

  public Attachment execute(CommandContext commandContext) {
    return commandContext
      .getDbSqlSession()
      .selectById(AttachmentEntity.class, attachmentId);
  }

}
