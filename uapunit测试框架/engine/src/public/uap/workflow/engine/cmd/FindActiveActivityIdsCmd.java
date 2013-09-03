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


import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;


/**
 * @author Tom Baeyens
 */
public class FindActiveActivityIdsCmd implements Command<List<String>>, Serializable {

  private static final long serialVersionUID = 1L;
  protected String executionId;
  
  public FindActiveActivityIdsCmd(String executionId) {
    this.executionId = executionId;
  }

  public List<String> execute(CommandContext commandContext) {
    if(executionId == null) {
      throw new WorkflowException("executionId is null");
    }
    
    ActivityInstanceEntity execution = commandContext
      .getExecutionManager()
      .getActInsByActInsPk(executionId);
    
    if (execution==null) {
      throw new WorkflowException("execution "+executionId+" doesn't exist");
    }
    
    return execution.findActiveActivityIds();
  }
}
