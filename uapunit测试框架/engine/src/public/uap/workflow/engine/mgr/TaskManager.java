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
import nc.bs.framework.common.NCLocator;
import uap.workflow.engine.bridge.TaskInstanceBridge;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.itf.ITaskInstanceBill;
import uap.workflow.engine.itf.ITaskInstanceQry;
import uap.workflow.engine.persistence.AbstractManager;
import uap.workflow.engine.query.Page;
import uap.workflow.engine.query.TaskQueryImpl;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.vos.TaskInstanceVO;
/**
 * @author Tom Baeyens
 */
public class TaskManager extends AbstractManager {
	public void deleteTasksByProcessInstanceId(String processInstanceId, String deleteReason, boolean cascade) {
		List<ITask> tasks = getDbSqlSession().createTaskQuery().processInstanceId(processInstanceId).list();
		String reason = (deleteReason == null || deleteReason.isEmpty()) ? TaskEntity.DELETE_REASON_DELETED : deleteReason;
		for (ITask task : tasks) {
			deleteTask(task, reason, cascade);
		}
	}
	public void deleteTask(ITask task, String deleteReason, boolean cascade) {
		CommandContext commandContext = Context.getCommandContext();
		String taskId = task.getTaskPk();
		List<ITask> subTasks = findTasksByParentTaskId(taskId);
		for (ITask subTask : subTasks) {
			deleteTask(subTask, deleteReason, cascade);
		}
		commandContext.getIdentityLinkManager().deleteIdentityLinksByTaskId(taskId);
		commandContext.getVariableInstanceManager().deleteVariableInstanceByTask(task);
		if (cascade) {
			commandContext.getHistoricTaskInstanceManager().deleteHistoricTaskInstanceById(taskId);
		} else {
			commandContext.getHistoricTaskInstanceManager().markTaskInstanceEnded(taskId, deleteReason);
		}
		getDbSqlSession().delete(ITask.class, task.getTaskPk());
	}
	public ITask getTaskByTaskPk(String taskPk) {
		if (taskPk == null) {
			throw new WorkflowException("请提供有效的任务Pk");
		}
		ITaskInstanceQry taskQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(ITaskInstanceQry.class);
		TaskInstanceVO taskInsVo = taskQry.getTaskInsVoByPk(taskPk);
		return new TaskInstanceBridge().convertM2T(taskInsVo);
	}
	@SuppressWarnings("unchecked")
	public List<ITask> findTasksByQueryCriteria(TaskQueryImpl taskQuery, Page page) {
		final String query = "selectTaskByQueryCriteria";
		return getDbSqlSession().selectList(query, taskQuery, page);
	}
	public long findTaskCountByQueryCriteria(TaskQueryImpl taskQuery) {
		return (Long) getDbSqlSession().selectOne("selectTaskCountByQueryCriteria", taskQuery);
	}
	@SuppressWarnings("unchecked")
	public List<ITask> findTasksByParentTaskId(String parentTaskId) {
		return getDbSqlSession().selectList("selectTasksByParentTaskId", parentTaskId);
	}
	public void deleteTask(String taskId, boolean cascade) {
		NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(ITaskInstanceBill.class).deleteTaskByPk(taskId);
	}
}
