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
package uap.workflow.engine.rules;
import java.util.Map;
import org.drools.KnowledgeBase;

import uap.workflow.engine.context.Context;
import uap.workflow.engine.entity.DeploymentEntity;
import uap.workflow.engine.exception.WorkflowException;
/**
 * @author Tom Baeyens
 */
public class RulesHelper {
	public static KnowledgeBase findKnowledgeBaseByDeploymentId(String deploymentId) {
		Map<String, Object> knowledgeBaseCache = Context.getProcessEngineConfiguration().getDeploymentCache().getKnowledgeBaseCache();
		KnowledgeBase knowledgeBase = (KnowledgeBase) knowledgeBaseCache.get(deploymentId);
		if (knowledgeBase == null) {
			DeploymentEntity deployment = Context.getCommandContext().getDeploymentManager().findDeploymentById(deploymentId);
			if (deployment == null) {
				throw new WorkflowException("no deployment with id " + deploymentId);
			}
			Context.getProcessEngineConfiguration().getDeploymentCache().deploy(deployment);
			knowledgeBase = (KnowledgeBase) knowledgeBaseCache.get(deploymentId);
			if (knowledgeBase == null) {
				throw new WorkflowException("deployment " + deploymentId + " doesn't contain any rules");
			}
		}
		return knowledgeBase;
	}
}
