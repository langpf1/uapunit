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
package uap.workflow.engine.handler;

import uap.workflow.engine.cfg.IdGenerator;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IInstanceListener;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.HistoricActivityInstanceEntity;
import uap.workflow.engine.util.ClockUtil;
/**
 * @author Tom Baeyens
 */
public class ActivityInstanceStartHandler implements IInstanceListener {
	public void notify(IActivityInstance execution) {
		IdGenerator idGenerator = Context.getProcessEngineConfiguration().getIdGenerator();
		ActivityInstanceEntity executionEntity = (ActivityInstanceEntity) execution;
		String processDefinitionId = executionEntity.getProcessDefinition().getId();
		String processInstanceId = executionEntity.getProcessInstance().getProInsPk();
		String executionId = executionEntity.getActInsPk();
		HistoricActivityInstanceEntity historicActivityInstance = new HistoricActivityInstanceEntity();
		historicActivityInstance.setId(idGenerator.getNextId());
		historicActivityInstance.setProcessDefinitionId(processDefinitionId);
		historicActivityInstance.setProcessInstanceId(processInstanceId);
		historicActivityInstance.setExecutionId(executionId);
		historicActivityInstance.setActivityId(executionEntity.getActivity().getId());
		historicActivityInstance.setActivityName((String) executionEntity.getActivity().getProperty("name"));
		historicActivityInstance.setActivityType((String) executionEntity.getActivity().getProperty("type"));
		historicActivityInstance.setStartTime(ClockUtil.getCurrentTime());
		Context.getCommandContext().getDbSqlSession().insert(historicActivityInstance);
	}
}
