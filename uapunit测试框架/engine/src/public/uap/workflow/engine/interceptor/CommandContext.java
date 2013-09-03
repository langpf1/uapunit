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
package uap.workflow.engine.interceptor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import uap.workflow.engine.cfg.ProcessEngineConfigurationImpl;
import uap.workflow.engine.cfg.TransactionContext;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.db.DbSqlSession;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.mgr.AttachmentManager;
import uap.workflow.engine.mgr.CommentManager;
import uap.workflow.engine.mgr.DeploymentManager;
import uap.workflow.engine.mgr.EventSubscriptionManager;
import uap.workflow.engine.mgr.ExecutionManager;
import uap.workflow.engine.mgr.GroupManager;
import uap.workflow.engine.mgr.HistoricActivityInstanceManager;
import uap.workflow.engine.mgr.HistoricDetailManager;
import uap.workflow.engine.mgr.HistoricTaskInstanceManager;
import uap.workflow.engine.mgr.IdentityInfoManager;
import uap.workflow.engine.mgr.IdentityLinkManager;
import uap.workflow.engine.mgr.JobManager;
import uap.workflow.engine.mgr.MembershipManager;
import uap.workflow.engine.mgr.ProcessDefinitionManager;
import uap.workflow.engine.mgr.ProcessInstanceManager;
import uap.workflow.engine.mgr.PropertyManager;
import uap.workflow.engine.mgr.ResourceManager;
import uap.workflow.engine.mgr.TableDataManager;
import uap.workflow.engine.mgr.TaskManager;
import uap.workflow.engine.mgr.UserManager;
import uap.workflow.engine.mgr.VariableInstanceManager;
import uap.workflow.engine.pvm.runtime.AtomicOperation;
/**
 * @author Tom Baeyens
 * @author Agim Emruli
 */
public class CommandContext {
	private static Logger log = Logger.getLogger(CommandContext.class.getName());
	protected Command<?> command;
	protected TransactionContext transactionContext;
	protected Map<Class<?>, SessionFactory> sessionFactories;
	protected Map<Class<?>, Session> sessions = new HashMap<Class<?>, Session>();
	protected Throwable exception = null;
	protected LinkedList<AtomicOperation> nextOperations = new LinkedList<AtomicOperation>();
	protected ProcessEngineConfigurationImpl processEngineConfiguration;
	public void performOperation(AtomicOperation executionOperation, IActivityInstance execution) {
		nextOperations.add(executionOperation);
		if (nextOperations.size() == 1) {
			try {
				Context.setExecutionContext(execution);
				while (!nextOperations.isEmpty()) {
					AtomicOperation currentOperation = nextOperations.removeFirst();
					if (log.isLoggable(Level.FINEST)) {
						log.finest("AtomicOperation: " + currentOperation + " on " + this);
					}
					currentOperation.execute(execution);
				}
			} finally {
				Context.removeExecutionContext();
			}
		}
	}
	public CommandContext(Command<?> command, ProcessEngineConfigurationImpl processEngineConfiguration) {
		this.command = command;
		this.processEngineConfiguration = processEngineConfiguration;
		sessionFactories = processEngineConfiguration.getSessionFactories();
		this.transactionContext = processEngineConfiguration.getTransactionContextFactory().openTransactionContext(this);
	}
	public void close() {
		// the intention of this method is that all resources are closed
		// properly,
		// even
		// if exceptions occur in close or flush methods of the sessions or the
		// transaction context.
		try {
			try {
				try {
					if (exception == null) {
						flushSessions();
					}
				} catch (Throwable exception) {
					exception(exception);
				} finally {
					try {
						if (exception == null) {
							transactionContext.commit();
						}
					} catch (Throwable exception) {
						exception(exception);
					}
					if (exception != null) {
						log.log(Level.SEVERE, "Error while closing command context", exception);
						transactionContext.rollback();
					}
				}
			} catch (Throwable exception) {
				exception(exception);
			} finally {
				closeSessions();
			}
		} catch (Throwable exception) {
			exception(exception);
		}
		// rethrow the original exception if there was one
		if (exception != null) {
			if (exception instanceof Error) {
				throw (Error) exception;
			} else if (exception instanceof RuntimeException) {
				throw (RuntimeException) exception;
			} else {
				throw new WorkflowException("exception while executing command " + command, exception);
			}
		}
	}
	protected void flushSessions() {
		for (Session session : sessions.values()) {
			session.flush();
		}
	}
	protected void closeSessions() {
		for (Session session : sessions.values()) {
			try {
				session.close();
			} catch (Throwable exception) {
				exception(exception);
			}
		}
	}
	public void exception(Throwable exception) {
		if (this.exception == null) {
			this.exception = exception;
		} else {
			log.log(Level.SEVERE, "masked exception in command context. for root cause, see below as it will be rethrown later.", exception);
		}
	}
	@SuppressWarnings({ "unchecked" })
	public <T> T getSession(Class<T> sessionClass) {
		Session session = sessions.get(sessionClass);
		if (session == null) {
			SessionFactory sessionFactory = sessionFactories.get(sessionClass);
			if (sessionFactory == null) {
				throw new WorkflowException("no session factory configured for " + sessionClass.getName());
			}
			session = sessionFactory.openSession();
			sessions.put(sessionClass, session);
		}
		return (T) session;
	}
	public DbSqlSession getDbSqlSession() {
		return getSession(DbSqlSession.class);
	}
	public DeploymentManager getDeploymentManager() {
		return getSession(DeploymentManager.class);
	}
	public ResourceManager getResourceManager() {
		return getSession(ResourceManager.class);
	}
	public ProcessDefinitionManager getProcessDefinitionManager() {
		return getSession(ProcessDefinitionManager.class);
	}
	public ExecutionManager getExecutionManager() {
		return getSession(ExecutionManager.class);
	}
	public TaskManager getTaskManager() {
		return getSession(TaskManager.class);
	}
	public IdentityLinkManager getIdentityLinkManager() {
		return getSession(IdentityLinkManager.class);
	}
	public VariableInstanceManager getVariableInstanceManager() {
		return getSession(VariableInstanceManager.class);
	}
	public ProcessInstanceManager getProcessInstanceManager() {
		return getSession(ProcessInstanceManager.class);
	}
	public HistoricDetailManager getHistoricDetailManager() {
		return getSession(HistoricDetailManager.class);
	}
	public HistoricActivityInstanceManager getHistoricActivityInstanceManager() {
		return getSession(HistoricActivityInstanceManager.class);
	}
	public HistoricTaskInstanceManager getHistoricTaskInstanceManager() {
		return getSession(HistoricTaskInstanceManager.class);
	}
	public JobManager getJobManager() {
		return getSession(JobManager.class);
	}
	public UserManager getUserManager() {
		return getSession(UserManager.class);
	}
	public GroupManager getGroupManager() {
		return getSession(GroupManager.class);
	}
	public IdentityInfoManager getIdentityInfoManager() {
		return getSession(IdentityInfoManager.class);
	}
	public MembershipManager getMembershipManager() {
		return getSession(MembershipManager.class);
	}
	public AttachmentManager getAttachmentManager() {
		return getSession(AttachmentManager.class);
	}
	public TableDataManager getTableDataManager() {
		return getSession(TableDataManager.class);
	}
	public CommentManager getCommentManager() {
		return getSession(CommentManager.class);
	}
	public EventSubscriptionManager getEventSubscriptionManager() {
		return getSession(EventSubscriptionManager.class);
	}
	public Map<Class<?>, SessionFactory> getSessionFactories() {
		return sessionFactories;
	}
	public PropertyManager getPropertyManager() {
		return getSession(PropertyManager.class);
	}
	// getters and setters
	// //////////////////////////////////////////////////////
	public TransactionContext getTransactionContext() {
		return transactionContext;
	}
	public Command<?> getCommand() {
		return command;
	}
	public Map<Class<?>, Session> getSessions() {
		return sessions;
	}
	public Throwable getException() {
		return exception;
	}
}
