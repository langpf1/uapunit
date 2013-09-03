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
package uap.workflow.engine.mgr;
import java.util.List;

import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.entity.DeploymentEntity;
import uap.workflow.engine.entity.EventSubscriptionEntity;
import uap.workflow.engine.entity.JobEntity;
import uap.workflow.engine.event.MessageEventHandler;
import uap.workflow.engine.jobexecutor.Job;
import uap.workflow.engine.jobexecutor.TimerStartEventJobHandler;
import uap.workflow.engine.persistence.AbstractManager;
import uap.workflow.engine.query.DeploymentQueryImpl;
import uap.workflow.engine.query.Page;
import uap.workflow.engine.repository.Deployment;
/**
 * @author Tom Baeyens
 */
public class DeploymentManager extends AbstractManager {
	public void insertDeployment(DeploymentEntity deployment) {
		Context.getProcessEngineConfiguration().getDeploymentCache().deploy(deployment);
	}
	public void deleteDeployment(String deploymentId, boolean cascade) {
		List<IProcessDefinition> processDefinitions = getDbSqlSession().createProcessDefinitionQuery().deploymentId(deploymentId).list();
		if (cascade) {
			// delete process instances
			for (IProcessDefinition processDefinition : processDefinitions) {
				String processDefinitionId = processDefinition.getId();
				getProcessInstanceManager().deleteProcessInstancesByProcessDefinition(processDefinitionId, "deleted deployment", cascade);
			}
		}
		// delete process definitions from db
		getProcessDefinitionManager().deleteProcessDefinitionsByDeploymentId(deploymentId);
		for (IProcessDefinition processDefinition : processDefinitions) {
			String processDefinitionId = processDefinition.getId();
			// remove process definitions from cache:
			Context.getProcessEngineConfiguration().getDeploymentCache().removeProcessDefinition(processDefinitionId);
			// remove timer start events:
			List<Job> timerStartJobs = Context.getCommandContext().getJobManager().findJobsByConfiguration(TimerStartEventJobHandler.TYPE, processDefinition.getProDefPk());
			for (Job job : timerStartJobs) {
				((JobEntity) job).delete();
			}
			// remove message event subscriptions:
			List<EventSubscriptionEntity> findEventSubscriptionsByConfiguration = Context.getCommandContext().getEventSubscriptionManager()
					.findEventSubscriptionsByConfiguration(MessageEventHandler.TYPE, processDefinition.getId());
			for (EventSubscriptionEntity eventSubscriptionEntity : findEventSubscriptionsByConfiguration) {
				eventSubscriptionEntity.delete();
			}
		}
		getResourceManager().deleteResourcesByDeploymentId(deploymentId);
		getDbSqlSession().delete("deleteDeployment", deploymentId);
	}
	public DeploymentEntity findLatestDeploymentByName(String deploymentName) {
		List<?> list = getDbSqlSession().selectList("selectDeploymentsByName", deploymentName, new Page(0, 1));
		if (list != null && !list.isEmpty()) {
			return (DeploymentEntity) list.get(0);
		}
		return null;
	}
	public DeploymentEntity findDeploymentById(String deploymentId) {
		return (DeploymentEntity) getDbSqlSession().selectOne("selectDeploymentById", deploymentId);
	}
	public long findDeploymentCountByQueryCriteria(DeploymentQueryImpl deploymentQuery) {
		return (Long) getDbSqlSession().selectOne("selectDeploymentCountByQueryCriteria", deploymentQuery);
	}
	@SuppressWarnings("unchecked")
	public List<Deployment> findDeploymentsByQueryCriteria(DeploymentQueryImpl deploymentQuery, Page page) {
		final String query = "selectDeploymentsByQueryCriteria";
		return getDbSqlSession().selectList(query, deploymentQuery, page);
	}
	@SuppressWarnings("unchecked")
	public List<String> getDeploymentResourceNames(String deploymentId) {
		return getDbSqlSession().getSqlSession().selectList("selectResourceNamesByDeploymentId", deploymentId);
	}
	public void close() {}
	public void flush() {}
}
