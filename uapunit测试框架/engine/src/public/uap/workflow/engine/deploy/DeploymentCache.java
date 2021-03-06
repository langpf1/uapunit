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
package uap.workflow.engine.deploy;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.workflow.engine.bpmn.parser.BpmnParse;
import uap.workflow.engine.bpmn.parser.BpmnParser;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.el.ExpressionManager;
import uap.workflow.engine.el.ExtExpressionManager;
import uap.workflow.engine.entity.DeploymentEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.mgr.ProcessDefinitionManager;
/**
 * @author Tom Baeyens
 * @author Falko Menge
 */
public class DeploymentCache {
	protected Map<String, IProcessDefinition> processDefinitionCache = new HashMap<String, IProcessDefinition>();
	protected Map<String, Object> knowledgeBaseCache = new HashMap<String, Object>();
	protected List<Deployer> deployers;
	public void deploy(DeploymentEntity deployment) {
		//processDefinitionCache.clear();
		for (Deployer deployer : deployers) {
			deployer.deploy(deployment);
		}
	}
	public IProcessDefinition getProDefByProDefPk(String proDefPk) {
		// processDefinitionCache.clear();
		if (proDefPk == null) {
			throw new WorkflowException("Invalid process definition id : null");
		}
		
		IProcessDefinition parsedProcessDef = processDefinitionCache.get(proDefPk);
		if (parsedProcessDef != null) {
			return parsedProcessDef;
		}
		
		IProcessDefinition processDefinition = new ProcessDefinitionManager().getProDefByProDefPk(proDefPk);
		if (processDefinition == null) {
			throw new WorkflowException("no deployed process definition found with id '" + proDefPk + "'");
		}
		processDefinition = resolveProcessDefinition(processDefinition);
		return processDefinition;
	}
	public IProcessDefinition getProDefByProDefId(String proDefId) {
		if (proDefId == null) {
			throw new WorkflowException("Invalid process definition id : null");
		}
		IProcessDefinition processDefinition = new ProcessDefinitionManager().getProDefByProDefId(proDefId);
		if (processDefinition == null) {
			throw new WorkflowException("no deployed process definition found with id '" + proDefId + "'");
		}
		processDefinition = resolveProcessDefinition(processDefinition);
		return processDefinition;
	}
	// public PvmProcessDefinition
	// findDeployedLatestProcessDefinitionByKey(String processDefinitionKey) {
	// PvmProcessDefinition processDefinition =
	// Context.getCommandContext().getProcessDefinitionManager().findLatestProcessDefinitionByKey(processDefinitionKey);
	// if (processDefinition == null) {
	// throw new ActivitiException("no processes deployed with key '" +
	// processDefinitionKey + "'");
	// }
	// processDefinition = resolveProcessDefinition(processDefinition);
	// return processDefinition;
	// }
	public IProcessDefinition findDeployedProcessDefinitionByKeyAndVersion(String processDefinitionKey, Integer processDefinitionVersion) {
		ProcessDefinitionManager mgr = Context.getCommandContext().getProcessDefinitionManager();
		IProcessDefinition processDefinition = (IProcessDefinition) mgr.findProcessDefinitionByKeyAndVersion(processDefinitionKey, processDefinitionVersion);
		if (processDefinition == null) {
			throw new WorkflowException("no processes deployed with key = '" + processDefinitionKey + "' and version = '" + processDefinitionVersion + "'");
		}
		processDefinition = resolveProcessDefinition(processDefinition);
		return processDefinition;
	}
	protected IProcessDefinition resolveProcessDefinition(IProcessDefinition processDefinition) {
		IProcessDefinition parsingProcessDef = processDefinition;
		String proDefPk = processDefinition.getProDefPk();
		IProcessDefinition parsedProcessDef = processDefinitionCache.get(proDefPk);
		if (parsedProcessDef == null) {
			BpmnParse bpmnParse = new BpmnParser(new ExtExpressionManager()).createParse().sourceInputStream(new ByteArrayInputStream(parsingProcessDef.getResourceBytes()));
			bpmnParse.execute();
			List<IProcessDefinition> proDefs = bpmnParse.getProcessDefinitions();
			proDefs.get(0).setProDefPk(proDefPk);
			
			parsingProcessDef = bpmnParse.getProcessDefinitionByProDefPk(proDefPk);
			parsingProcessDef.setProDefId(processDefinition.getProDefId());
			parsingProcessDef.setName(processDefinition.getName());
			parsingProcessDef.setDescription(processDefinition.getDescription());
			parsingProcessDef.setResourceBytes(processDefinition.getResourceBytes());
			parsingProcessDef.setDiagramBytes(processDefinition.getDiagramBytes());
			parsingProcessDef.setVersion(processDefinition.getVersion());
			parsingProcessDef.setPublic(processDefinition.isPublic());
			parsingProcessDef.setPk_group(processDefinition.getPk_group());
			parsingProcessDef.setPk_bizobject(processDefinition.getPk_bizobject());
			parsingProcessDef.setPk_biztrans(processDefinition.getPk_biztrans());
			parsingProcessDef.setValidity(processDefinition.getValidity());

			processDefinitionCache.put(parsingProcessDef.getProDefPk(), parsingProcessDef);
			parsedProcessDef = processDefinitionCache.get(proDefPk);
		}
		return parsedProcessDef;
	}
	public void addProcessDefinition(IProcessDefinition processDefinition) {
		processDefinitionCache.put(processDefinition.getProDefPk(), processDefinition);
	}
	public void removeProcessDefinition(String processDefinitionId) {
		processDefinitionCache.remove(processDefinitionId);
	}
	public void addKnowledgeBase(String knowledgeBaseId, Object knowledgeBase) {
		knowledgeBaseCache.put(knowledgeBaseId, knowledgeBase);
	}
	public void removeKnowledgeBase(String knowledgeBaseId) {
		knowledgeBaseCache.remove(knowledgeBaseId);
	}
	public void discardProcessDefinitionCache() {
		processDefinitionCache.clear();
	}
	public void discardKnowledgeBaseCache() {
		knowledgeBaseCache.clear();
	}
	// getters and setters
	// //////////////////////////////////////////////////////
	public Map<String, IProcessDefinition> getProcessDefinitionCache() {
		return processDefinitionCache;
	}
	public void setProcessDefinitionCache(Map<String, IProcessDefinition> processDefinitionCache) {
		this.processDefinitionCache = processDefinitionCache;
	}
	public Map<String, Object> getKnowledgeBaseCache() {
		return knowledgeBaseCache;
	}
	public void setKnowledgeBaseCache(Map<String, Object> knowledgeBaseCache) {
		this.knowledgeBaseCache = knowledgeBaseCache;
	}
	public List<Deployer> getDeployers() {
		return deployers;
	}
	public void setDeployers(List<Deployer> deployers) {
		this.deployers = deployers;
	}
}
