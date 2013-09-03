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
package uap.workflow.engine.json;
import java.io.Reader;

import uap.workflow.engine.core.IProcessDefinition;
/**
 * @author Tom Baeyens
 */
public class JsonProcessDefinitionConverter extends JsonObjectConverter<IProcessDefinition> {
	public JSONObject toJsonObject(IProcessDefinition processDefinition) {
		IProcessDefinition processDefinitionEntity = (IProcessDefinition) processDefinition;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", processDefinitionEntity.getId());
		if (processDefinitionEntity != null) {
			jsonObject.put("key", processDefinitionEntity.getProDefPk());
		}
		if (processDefinitionEntity.getDeploymentId() != null) {
			jsonObject.put("deploymentId", processDefinitionEntity.getDeploymentId());
		}
		return jsonObject;
	}
	public IProcessDefinition toObject(Reader reader) {
		return null;
	}
}
