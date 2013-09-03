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
package uap.workflow.engine.form;
import java.util.Map;

import uap.workflow.engine.bpmn.parser.BpmnParse;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.DeploymentEntity;
import uap.workflow.engine.xml.Element;
/**
 * @author Tom Baeyens
 */
public class DefaultStartFormHandler extends DefaultFormHandler implements StartFormHandler {
	@Override
	public void parseConfiguration(Element activityElement, DeploymentEntity deployment, IProcessDefinition processDefinition, BpmnParse bpmnParse) {
		super.parseConfiguration(activityElement, deployment, processDefinition, bpmnParse);
		if (formKey != null) {
			processDefinition.setStartFormKey(true);
		}
	}
	public StartFormData createStartFormData(IProcessDefinition processDefinition) {
		StartFormDataImpl startFormData = new StartFormDataImpl();
		startFormData.setFormKey(formKey);
		startFormData.setDeploymentId(deploymentId);
		startFormData.setProcessDefinition(processDefinition);
		initializeFormProperties(startFormData, null);
		return startFormData;
	}
	public ActivityInstanceEntity submitStartFormData(ActivityInstanceEntity processInstance, Map<String, String> properties) {
		submitFormProperties(properties, processInstance);
		return processInstance;
	}
}
