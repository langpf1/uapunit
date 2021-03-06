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

package uap.workflow.engine.persistence;


import uap.workflow.engine.cfg.ProcessEngineConfigurationImpl;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.exception.WorkflowException;


/**
 * @author Tom Baeyens
 */
public class AbstractHistoricManager extends AbstractManager {

  protected int historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
  protected boolean isHistoryEnabled = historyLevel > ProcessEngineConfigurationImpl.HISTORYLEVEL_NONE;
  
  protected void checkHistoryEnabled() {
    if (!isHistoryEnabled) {
      throw new WorkflowException("history is not enabled");
    }
  }

  public boolean isHistoryEnabled() {
    return isHistoryEnabled;
  }
}
