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


import uap.workflow.engine.entity.GroupEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;


/**
 * @author Joram Barrez
 */
public class SaveGroupCmd implements Command<Void>, Serializable {
  
  private static final long serialVersionUID = 1L;
  protected GroupEntity group;
  
  public SaveGroupCmd(GroupEntity group) {
    this.group = group;
  }
  
  public Void execute(CommandContext commandContext) {
    if(group == null) {
      throw new WorkflowException("group is null");
    }
    if (group.getRevision()==0) {
      commandContext
        .getGroupManager()
        .insertGroup(group);
    } else {
      commandContext
        .getGroupManager()
        .updateGroup(group);
    }
    
    return null;
  }

}
