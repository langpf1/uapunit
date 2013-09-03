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
package uap.workflow.engine.context;

import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.DeploymentEntity;
/**
 * @author Tom Baeyens
 */
public class ActivityInstanceContext {
	protected IActivityInstance activityInstance;
	public ActivityInstanceContext(IActivityInstance execution) {
		this.activityInstance = (ActivityInstanceEntity) execution;
	}
	public IActivityInstance getExecution() {
		return activityInstance;
	}
	public IProcessInstance getProcessInstance() {
		return activityInstance.getProcessInstance();
	}
	public IProcessDefinition getProcessDefinition() {
		return  activityInstance.getProcessDefinition();
	}
	public DeploymentEntity getDeployment() {
		String deploymentId = getProcessDefinition().getDeploymentId();
		DeploymentEntity deployment = Context.getCommandContext().getDeploymentManager().findDeploymentById(deploymentId);
		return deployment;
	}
}
