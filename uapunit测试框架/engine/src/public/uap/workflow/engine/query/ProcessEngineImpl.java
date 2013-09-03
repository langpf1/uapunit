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
import java.util.Map;
import java.util.logging.Logger;

import uap.workflow.engine.cfg.ProcessEngineConfigurationImpl;
import uap.workflow.engine.cfg.TransactionContextFactory;
import uap.workflow.engine.db.DbSqlSession;
import uap.workflow.engine.db.DbSqlSessionFactory;
import uap.workflow.engine.el.ExpressionManager;
import uap.workflow.engine.interceptor.CommandExecutor;
import uap.workflow.engine.interceptor.SessionFactory;
import uap.workflow.engine.jobexecutor.JobExecutor;
import uap.workflow.engine.service.FormService;
import uap.workflow.engine.service.HistoryService;
import uap.workflow.engine.service.IdentityService;
import uap.workflow.engine.service.ManagementService;
import uap.workflow.engine.service.ProcessEngine;
import uap.workflow.engine.service.ProcessEngines;
import uap.workflow.engine.service.RepositoryService;
import uap.workflow.engine.service.RuntimeService;
import uap.workflow.engine.service.TaskService;
/**
 * @author Tom Baeyens
 */
public class ProcessEngineImpl implements ProcessEngine {
	private static Logger log = Logger.getLogger(ProcessEngineImpl.class.getName());
	protected String name;
	protected RepositoryService repositoryService;
	protected RuntimeService runtimeService;
	protected HistoryService historicDataService;
	protected IdentityService identityService;
	protected TaskService taskService;
	protected FormService formService;
	protected ManagementService managementService;
	protected String databaseSchemaUpdate;
	protected JobExecutor jobExecutor;
	protected CommandExecutor commandExecutor;
	protected Map<Class<?>, SessionFactory> sessionFactories;
	protected ExpressionManager expressionManager;
	protected int historyLevel;
	protected TransactionContextFactory transactionContextFactory;
	protected ProcessEngineConfigurationImpl processEngineConfiguration;
	public ProcessEngineImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
		this.processEngineConfiguration = processEngineConfiguration;
		this.name = processEngineConfiguration.getProcessEngineName();
		this.repositoryService = processEngineConfiguration.getRepositoryService();
		this.runtimeService = processEngineConfiguration.getRuntimeService();
		this.historicDataService = processEngineConfiguration.getHistoryService();
		this.identityService = processEngineConfiguration.getIdentityService();
		this.taskService = processEngineConfiguration.getTaskService();
		this.formService = processEngineConfiguration.getFormService();
		this.managementService = processEngineConfiguration.getManagementService();
		this.databaseSchemaUpdate = processEngineConfiguration.getDatabaseSchemaUpdate();
		this.jobExecutor = processEngineConfiguration.getJobExecutor();
		this.commandExecutor = processEngineConfiguration.getCommandExecutorTxRequired();
		this.sessionFactories = processEngineConfiguration.getSessionFactories();
		this.historyLevel = processEngineConfiguration.getHistoryLevel();
		this.transactionContextFactory = processEngineConfiguration.getTransactionContextFactory();
		// commandExecutor.execute(new SchemaOperationsProcessEngineBuild());
		if (name == null) {
			log.info("default activiti ProcessEngine created");
		} else {
			log.info("ProcessEngine " + name + " created");
		}
		ProcessEngines.registerProcessEngine(this);
		// if ((jobExecutor != null) && (jobExecutor.isAutoActivate())) {
		jobExecutor.start();
		// }
	}
	public void close() {
		ProcessEngines.unregister(this);
		if ((jobExecutor != null) && (jobExecutor.isActive())) {
			jobExecutor.shutdown();
		}
		commandExecutor.execute(new SchemaOperationProcessEngineClose());
	}
	public DbSqlSessionFactory getDbSqlSessionFactory() {
		return (DbSqlSessionFactory) sessionFactories.get(DbSqlSession.class);
	}
	// getters and setters
	// //////////////////////////////////////////////////////
	public String getName() {
		return name;
	}
	public IdentityService getIdentityService() {
		return identityService;
	}
	public ManagementService getManagementService() {
		return managementService;
	}
	public TaskService getTaskService() {
		return taskService;
	}
	public HistoryService getHistoryService() {
		return historicDataService;
	}
	public RuntimeService getRuntimeService() {
		return runtimeService;
	}
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}
	public FormService getFormService() {
		return formService;
	}
	public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
		return processEngineConfiguration;
	}
}
