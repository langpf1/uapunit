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
package uap.workflow.engine.command;
import java.io.Serializable;
import java.util.Map;


import uap.workflow.engine.context.SubmitStartFormCtx;
import uap.workflow.engine.context.SubmitTaskFormCtx;
import uap.workflow.engine.service.FormService;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;
/**
 * @author Tom Baeyens
 */
public class SubmitStartFormCmd implements ICommand<Void>, Serializable {
	private static final long serialVersionUID = 1L;
	protected String processDefinitionId = null;
	protected Map<String, String> properties = null;
	public SubmitStartFormCmd(){
	}
	public Void execute() {
		SubmitStartFormCtx ctx = (SubmitStartFormCtx)(WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx());
		this.processDefinitionId = ctx.getProcessDefinitionId();
		this.properties = ctx.getProperties();
		FormService formService = WfmServiceFacility.getFormService();
		formService.submitStartFormData(processDefinitionId, properties);
		return null;
	}
}
