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
import java.io.InputStream;
import java.util.List;

import uap.workflow.engine.cmd.ActivateProcessDefinitionCmd;
import uap.workflow.engine.cmd.DeleteDeploymentCmd;
import uap.workflow.engine.cmd.DeployCmd;
import uap.workflow.engine.cmd.FeatchProDefCmd;
import uap.workflow.engine.cmd.GetDeploymentProcessDefinitionCmd;
import uap.workflow.engine.cmd.GetDeploymentResourceCmd;
import uap.workflow.engine.cmd.GetDeploymentResourceNamesCmd;
import uap.workflow.engine.cmd.SetProcessDefinitionStatusCmd;
import uap.workflow.engine.cmd.SuspendProcessDefinitionCmd;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.ProcessDefinitionStatusEnum;
import uap.workflow.engine.repository.Deployment;
import uap.workflow.engine.repository.DeploymentBuilder;
import uap.workflow.engine.repository.DeploymentBuilderImpl;
import uap.workflow.engine.repository.DeploymentQuery;
import uap.workflow.engine.repository.ProcessDefinitionQuery;
import uap.workflow.engine.service.RepositoryService;
/**
 * @author Tom Baeyens
 */
public class RepositoryServiceImpl extends ServiceImpl implements RepositoryService {
	public DeploymentBuilder createDeployment() {
		return new DeploymentBuilderImpl(this);
	}
	public Deployment deploy(DeploymentBuilderImpl deploymentBuilder) {
		return commandExecutor.execute(new DeployCmd<Deployment>(deploymentBuilder));
	}
	public void deleteDeployment(String deploymentId) {
		commandExecutor.execute(new DeleteDeploymentCmd(deploymentId, false));
	}
	public void deleteDeploymentCascade(String deploymentId) {
		commandExecutor.execute(new DeleteDeploymentCmd(deploymentId, true));
	}
	public void deleteDeployment(String deploymentId, boolean cascade) {
		commandExecutor.execute(new DeleteDeploymentCmd(deploymentId, cascade));
	}
	public ProcessDefinitionQuery createProcessDefinitionQuery() {
		return new ProcessDefinitionQueryImpl(commandExecutor);
	}
	@SuppressWarnings("unchecked")
	public List<String> getDeploymentResourceNames(String deploymentId) {
		return commandExecutor.execute(new GetDeploymentResourceNamesCmd(deploymentId));
	}
	public InputStream getResourceAsStream(String deploymentId, String resourceName) {
		return commandExecutor.execute(new GetDeploymentResourceCmd(deploymentId, resourceName));
	}
	public DeploymentQuery createDeploymentQuery() {
		return new DeploymentQueryImpl(commandExecutor);
	}
	public IProcessDefinition getDeployedProcessDefinition(String processDefinitionId) {
		return commandExecutor.execute(new GetDeploymentProcessDefinitionCmd(processDefinitionId));
	}
	public void suspendProcessDefinitionById(String processDefinitionId) {
		commandExecutor.execute(new SuspendProcessDefinitionCmd(processDefinitionId, null));
	}
	public void suspendProcessDefinitionByKey(String processDefinitionKey) {
		commandExecutor.execute(new SuspendProcessDefinitionCmd(null, processDefinitionKey));
	}
	public void activateProcessDefinitionById(String processDefinitionId) {
		commandExecutor.execute(new ActivateProcessDefinitionCmd(processDefinitionId, null));
	}
	public void activateProcessDefinitionByKey(String processDefinitionKey) {
		commandExecutor.execute(new ActivateProcessDefinitionCmd(null, processDefinitionKey));
	}
	@Override
	public IProcessDefinition getProcessDefinitionById(String proDefId) {
		return commandExecutor.execute(new FeatchProDefCmd(proDefId, null));
	}
	@Override
	public IProcessDefinition getProcessDefinitionByPk(String proDefPk) {
		return commandExecutor.execute(new FeatchProDefCmd(null, proDefPk));
	}
	public void setProcessDefinitionStatus(String pkProcDef, ProcessDefinitionStatusEnum status){
		commandExecutor.execute(new SetProcessDefinitionStatusCmd(getProcessDefinitionByPk(pkProcDef), status));
	}
}
