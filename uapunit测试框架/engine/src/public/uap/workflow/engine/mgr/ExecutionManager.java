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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.ProcessInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.persistence.AbstractManager;
import uap.workflow.engine.query.Page;
import uap.workflow.engine.utils.ActivityInstanceUtil;
/**
 * @author Tom Baeyens
 */
public class ExecutionManager extends AbstractManager {
	@SuppressWarnings("unchecked")
	public void deleteProcessInstancesByProcessDefinition(String processDefinitionId, String deleteReason, boolean cascade) {
		List<String> processInstanceIds = getDbSqlSession().selectList("selectProcessInstanceIdsByProcessDefinitionId", processDefinitionId);
		for (String processInstanceId : processInstanceIds) {
			deleteProcessInstance(processInstanceId, deleteReason, cascade);
		}
		if (cascade) {
			Context.getCommandContext().getProcessInstanceManager().deleteHistoricProcessInstanceByProcessDefinitionId(processDefinitionId);
		}
	}
	public void deleteProcessInstance(String processInstanceId, String deleteReason) {
		deleteProcessInstance(processInstanceId, deleteReason, false);
	}
	public void deleteProcessInstance(String processInstanceId, String deleteReason, boolean cascade) {
		ActivityInstanceEntity execution = getActInsByActInsPk(processInstanceId);
		if (execution == null) {
			throw new WorkflowException("No process instance found for id '" + processInstanceId + "'");
		}
		CommandContext commandContext = Context.getCommandContext();
		commandContext.getTaskManager().deleteTasksByProcessInstanceId(processInstanceId, deleteReason, cascade);
		if (cascade) {
			commandContext.getProcessInstanceManager().deleteHistoricProcessInstanceById(processInstanceId);
		}
		execution.deleteCascade(deleteReason);
	}
	public ProcessInstanceEntity findSubProcessInstanceBySuperExecutionId(String superExecutionId) {
		return (ProcessInstanceEntity) getDbSqlSession().selectOne("selectSubProcessInstanceBySuperExecutionId", superExecutionId);
	}
	public List<IActivityInstance> findChildExecutionsByParentExecutionId(String parentExecutionId) {
		return ActivityInstanceUtil.getSubActInsByActInsPk(parentExecutionId);
	}
	public ActivityInstanceEntity getActInsByActInsPk(String actInsPk) {
		return (ActivityInstanceEntity) ActivityInstanceUtil.getActInsByActInsPk(actInsPk);
	}
	public long findExecutionCountByQueryCriteria(Object executionQuery) {
		return (Long) getDbSqlSession().selectOne("selectExecutionCountByQueryCriteria", executionQuery);
	}
	@SuppressWarnings("unchecked")
	public List<ActivityInstanceEntity> findExecutionsByQueryCriteria(Object executionQuery, Page page) {
		return getDbSqlSession().selectList("selectExecutionsByQueryCriteria", executionQuery, page);
	}
	public long findProcessInstanceCountByQueryCriteria(Object executionQuery) {
		return (Long) getDbSqlSession().selectOne("selectProcessInstanceCountByQueryCriteria", executionQuery);
	}
	@SuppressWarnings("unchecked")
	public List<IProcessInstance> findProcessInstanceByQueryCriteria(Object executionQuery, Page page) {
		return getDbSqlSession().selectList("selectProcessInstanceByQueryCriteria", executionQuery, page);
	}
	@SuppressWarnings("unchecked")
	public List<ActivityInstanceEntity> findEventScopeExecutionsByActivityId(String activityRef, String parentExecutionId) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("activityId", activityRef);
		parameters.put("parentExecutionId", parentExecutionId);
		return getDbSqlSession().selectList("selectExecutionsByParentExecutionId", parameters);
	}
}
