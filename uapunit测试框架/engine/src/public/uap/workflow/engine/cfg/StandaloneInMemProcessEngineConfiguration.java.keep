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
package uap.workflow.engine.cfg;
import java.sql.SQLException;
import nc.bs.mw.sql.IerpConnection;
import nc.jdbc.framework.DataSourceCenter;
import uap.workflow.engine.exception.WorkflowRuntimeException;
/**
 * @author Tom Baeyens
 */
@SuppressWarnings("restriction")
public class StandaloneInMemProcessEngineConfiguration extends StandaloneProcessEngineConfiguration {
	public StandaloneInMemProcessEngineConfiguration() {
		DataSourceCenter sourceCenter = DataSourceCenter.getInstance();
		IerpConnection connection = null;
		try {
			connection = (IerpConnection) sourceCenter.getConnection();
		} catch (SQLException e) {
			throw new WorkflowRuntimeException(e);
		}
		this.databaseSchemaUpdate = DB_SCHEMA_UPDATE_CREATE_DROP;
		this.jdbcUrl = connection.getDataSource().getDbUrl();
		this.jdbcDriver = connection.getDataSource().getDbDriver();
		this.jdbcUsername = connection.getDataSource().getUser();
		this.jdbcPassword = connection.getDataSource().getPassword();
	}
}
