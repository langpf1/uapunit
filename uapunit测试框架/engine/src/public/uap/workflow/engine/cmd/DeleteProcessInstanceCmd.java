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


import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;


/**
 * @author Joram Barrez
 */
public class DeleteProcessInstanceCmd implements Command<Void>, Serializable {
  
  private static final long serialVersionUID = 1L;
  protected String processInstanceId;
  protected String deleteReason;

  public DeleteProcessInstanceCmd(String processInstanceId, String deleteReason) {
    this.processInstanceId = processInstanceId;
    this.deleteReason = deleteReason;
  }

  public Void execute(CommandContext commandContext) { 
    if(processInstanceId == null) {
      throw new WorkflowException("processInstanceId is null");
    }
    
    commandContext
      .getExecutionManager()
      .deleteProcessInstance(processInstanceId, deleteReason);
    return null;
  }

}
