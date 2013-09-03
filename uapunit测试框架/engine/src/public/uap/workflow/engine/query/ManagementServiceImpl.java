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
package uap.workflow.engine.query;

import java.sql.Connection;
import java.util.Map;


import uap.workflow.engine.cmd.ExecuteJobsCmd;
import uap.workflow.engine.cmd.GetJobExceptionStacktraceCmd;
import uap.workflow.engine.cmd.GetPropertiesCmd;
import uap.workflow.engine.cmd.GetTableCountCmd;
import uap.workflow.engine.cmd.GetTableMetaDataCmd;
import uap.workflow.engine.cmd.SetJobRetriesCmd;
import uap.workflow.engine.db.DbSqlSession;
import uap.workflow.engine.db.DbSqlSessionFactory;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.management.TableMetaData;
import uap.workflow.engine.management.TablePageQuery;
import uap.workflow.engine.runtime.JobQuery;
import uap.workflow.engine.service.ManagementService;


/**
 * @author Tom Baeyens
 * @author Joram Barrez
 * @author Falko Menge
 */
public class ManagementServiceImpl extends ServiceImpl implements ManagementService {

  public Map<String, Long> getTableCount() {
    return commandExecutor.execute(new GetTableCountCmd());
  }
  
  public TableMetaData getTableMetaData(String tableName) {
    return commandExecutor.execute(new GetTableMetaDataCmd(tableName));
  }

  public void executeJob(String jobId) {
    commandExecutor.execute(new ExecuteJobsCmd(jobId));
  }

  public void setJobRetries(String jobId, int retries) {
    commandExecutor.execute(new SetJobRetriesCmd(jobId, retries));
  }

  public TablePageQuery createTablePageQuery() {
    return new TablePageQueryImpl(commandExecutor);
  }
  
  public JobQuery createJobQuery() {
    return new JobQueryImpl(commandExecutor);
  }

  public String getJobExceptionStacktrace(String jobId) {
    return commandExecutor.execute(new GetJobExceptionStacktraceCmd(jobId));
  }

  public Map<String, String> getProperties() {
    return commandExecutor.execute(new GetPropertiesCmd());
  }

  public String databaseSchemaUpgrade(final Connection connection, final String catalog, final String schema) {
    return commandExecutor.execute(new Command<String>(){
      public String execute(CommandContext commandContext) {
        DbSqlSessionFactory dbSqlSessionFactory = (DbSqlSessionFactory) commandContext.getSessionFactories().get(DbSqlSession.class);
        DbSqlSession dbSqlSession = new DbSqlSession(dbSqlSessionFactory, connection, catalog, schema);
        commandContext.getSessions().put(DbSqlSession.class, dbSqlSession);
        return dbSqlSession.dbSchemaUpdate();
      }
    });
  }
}
