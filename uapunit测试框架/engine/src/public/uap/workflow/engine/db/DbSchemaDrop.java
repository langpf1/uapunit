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

package uap.workflow.engine.db;


import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.interceptor.CommandExecutor;
import uap.workflow.engine.query.ProcessEngineImpl;
import uap.workflow.engine.service.ProcessEngines;
import uap.workflow.engine.util.LogUtil;


/**
 * @author Tom Baeyens
 */
public class DbSchemaDrop {

  static {
    LogUtil.readJavaUtilLoggingConfigFromClasspath();
  }

  public static void main(String[] args) {
    ProcessEngineImpl processEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
    CommandExecutor commandExecutor = processEngine.getProcessEngineConfiguration().getCommandExecutorTxRequired();
    commandExecutor.execute(new Command<Object> (){
      public Object execute(CommandContext commandContext) {
        commandContext
          .getSession(DbSqlSession.class)
          .dbSchemaDrop();
        return null;
      }
    });
  }
}
