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
import java.util.Collections;
import java.util.List;
import nc.bs.framework.common.NCLocator;
import uap.workflow.engine.bridge.ProcessInstanceBridge;
import uap.workflow.engine.cfg.ProcessEngineConfigurationImpl;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.entity.HistoricProcessInstanceEntity;
import uap.workflow.engine.entity.ProcessInstanceEntity;
import uap.workflow.engine.history.HistoricProcessInstance;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.itf.IProcessInstanceQry;
import uap.workflow.engine.persistence.AbstractHistoricManager;
import uap.workflow.engine.query.HistoricProcessInstanceQueryImpl;
import uap.workflow.engine.query.Page;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.vos.ProcessInstanceVO;
/**
 * @author Tom Baeyens
 */
public class ProcessInstanceManager extends AbstractHistoricManager {
	public HistoricProcessInstanceEntity findHistoricProcessInstance(String processInstanceId) {
		if (historyLevel > ProcessEngineConfigurationImpl.HISTORYLEVEL_NONE) {
			return (HistoricProcessInstanceEntity) getDbSqlSession().selectById(HistoricProcessInstanceEntity.class, processInstanceId);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public void deleteHistoricProcessInstanceByProcessDefinitionId(String processDefinitionId) {
		if (historyLevel > ProcessEngineConfigurationImpl.HISTORYLEVEL_NONE) {
			List<String> historicProcessInstanceIds = getDbSqlSession().selectList("selectHistoricProcessInstanceIdsByProcessDefinitionId", processDefinitionId);
			for (String historicProcessInstanceId : historicProcessInstanceIds) {
				deleteHistoricProcessInstanceById(historicProcessInstanceId);
			}
		}
	}
	public ProcessInstanceEntity getProcessInstanceByProInsPk(String proInsPk) {
		IProcessInstanceQry proInsQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IProcessInstanceQry.class);
		ProcessInstanceVO proInsVo = proInsQry.getProInsVo(proInsPk);
		ProcessInstanceEntity proIns = new ProcessInstanceBridge().convertM2T(proInsVo);
		return proIns;
	}
	public void deleteHistoricProcessInstanceById(String historicProcessInstanceId) {
		if (historyLevel > ProcessEngineConfigurationImpl.HISTORYLEVEL_NONE) {
			CommandContext commandContext = Context.getCommandContext();
			commandContext.getHistoricDetailManager().deleteHistoricDetailsByProcessInstanceId(historicProcessInstanceId);
			commandContext.getHistoricActivityInstanceManager().deleteHistoricActivityInstancesByProcessInstanceId(historicProcessInstanceId);
			commandContext.getHistoricTaskInstanceManager().deleteHistoricTaskInstancesByProcessInstanceId(historicProcessInstanceId);
			getDbSqlSession().delete(HistoricProcessInstanceEntity.class, historicProcessInstanceId);
		}
	}
	public long findHistoricProcessInstanceCountByQueryCriteria(HistoricProcessInstanceQueryImpl historicProcessInstanceQuery) {
		if (historyLevel > ProcessEngineConfigurationImpl.HISTORYLEVEL_NONE) {
			return (Long) getDbSqlSession().selectOne("selectHistoricProcessInstanceCountByQueryCriteria", historicProcessInstanceQuery);
		}
		return 0;
	}
	@SuppressWarnings("unchecked")
	public List<HistoricProcessInstance> findHistoricProcessInstancesByQueryCriteria(HistoricProcessInstanceQueryImpl historicProcessInstanceQuery, Page page) {
		if (historyLevel > ProcessEngineConfigurationImpl.HISTORYLEVEL_NONE) {
			return getDbSqlSession().selectList("selectHistoricProcessInstancesByQueryCriteria", historicProcessInstanceQuery, page);
		}
		return Collections.EMPTY_LIST;
	}
}
