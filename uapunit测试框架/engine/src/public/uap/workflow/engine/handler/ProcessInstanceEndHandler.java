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
import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IInstanceListener;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.HistoricProcessInstanceEntity;
/**
 * @author Tom Baeyens
 */
public class ProcessInstanceEndHandler implements IInstanceListener {
	public void notify(IActivityInstance execution) {
		HistoricProcessInstanceEntity historicProcessInstance = Context.getCommandContext().getProcessInstanceManager()
				.findHistoricProcessInstance(execution.getProcessInstance().getProInsPk());
		if (historicProcessInstance != null) {
			String deleteReason = ((ActivityInstanceEntity) execution).getDeleteReason();
			historicProcessInstance.markEnded(deleteReason);
			historicProcessInstance.setEndActivityId(((ActivityInstanceEntity) execution).getActivity().getId());
		}
	}
}
