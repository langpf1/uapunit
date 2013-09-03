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
import nc.bs.framework.common.NCLocator;
import uap.workflow.engine.bridge.ProcessDefinitionBridge;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.entity.ProcessDefinitionEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.itf.IProcessDefinitionQry;
import uap.workflow.engine.persistence.AbstractManager;
import uap.workflow.engine.query.Page;
import uap.workflow.engine.query.ProcessDefinitionQueryImpl;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.vos.ProcessDefinitionVO;
/**
 * @author Tom Baeyens
 * @author Falko Menge
 */
public class ProcessDefinitionManager extends AbstractManager {
	public IProcessDefinition getProDefByProDefPk(String proDefPk) {
		IProcessDefinitionQry proDefQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IProcessDefinitionQry.class);
		ProcessDefinitionVO proDefVo = proDefQry.getProDefVoByPk(proDefPk);
		ProcessDefinitionEntity proDef = new ProcessDefinitionBridge().convertM2T(proDefVo);
		return proDef;
	}
	public IProcessDefinition getProDefByProDefId(String proDefId) {
		IProcessDefinitionQry proDefQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IProcessDefinitionQry.class);
		ProcessDefinitionVO proDefVo = proDefQry.getProDefVoById(proDefId);
		ProcessDefinitionEntity proDef = new ProcessDefinitionBridge().convertM2T(proDefVo);
		return proDef;
	}
	public void deleteProcessDefinitionsByDeploymentId(String deploymentId) {
		getDbSqlSession().delete("deleteProcessDefinitionsByDeploymentId", deploymentId);
	}
	@SuppressWarnings("unchecked")
	public List<IProcessDefinition> findProcessDefinitionsByQueryCriteria(ProcessDefinitionQueryImpl processDefinitionQuery, Page page) {
		final String query = "selectProcessDefinitionsByQueryCriteria";
		return getDbSqlSession().selectList(query, processDefinitionQuery, page);
	}
	public long findProcessDefinitionCountByQueryCriteria(ProcessDefinitionQueryImpl processDefinitionQuery) {
		return (Long) getDbSqlSession().selectOne("selectProcessDefinitionCountByQueryCriteria", processDefinitionQuery);
	}
	public IProcessDefinition findProcessDefinitionByDeploymentAndKey(String deploymentId, String processDefinitionKey) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("deploymentId", deploymentId);
		parameters.put("processDefinitionKey", processDefinitionKey);
		return (IProcessDefinition) getDbSqlSession().selectOne("selectProcessDefinitionByDeploymentAndKey", parameters);
	}
	public IProcessDefinition findProcessDefinitionByKeyAndVersion(String processDefinitionKey, Integer processDefinitionVersion) {
		ProcessDefinitionQueryImpl processDefinitionQuery = new ProcessDefinitionQueryImpl().processDefinitionKey(processDefinitionKey).processDefinitionVersion(processDefinitionVersion);
		List<IProcessDefinition> results = findProcessDefinitionsByQueryCriteria(processDefinitionQuery, null);
		if (results.size() == 1) {
			return results.get(0);
		} else if (results.size() > 1) {
			throw new WorkflowException("There are " + results.size() + " process definitions with key = '" + processDefinitionKey + "' and version = '" + processDefinitionVersion + "'.");
		}
		return null;
	}
}
