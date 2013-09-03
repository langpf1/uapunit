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
package uap.workflow.engine.bpmn.behavior;

import uap.workflow.engine.bpmn.helper.ScopeUtil;
import uap.workflow.engine.bpmn.parser.CompensateEventDefinition;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.exception.WorkflowException;
/**
 * Specialization of the Start Event for Event Sub-Processes.
 * 
 * Assumes that we enter with the "right" execution, which is the top-most
 * execution for the current scope
 * 
 * @author Daniel Meyer
 * @author Falko Menge
 */
public class EventSubProcessCompensateStartEventActivityBehavior extends NoneStartEventActivityBehavior {
	protected boolean isInterrupting = true;
	protected  CompensateEventDefinition compensateStartEventDefinition;
	public EventSubProcessCompensateStartEventActivityBehavior(CompensateEventDefinition compensateStartEventDefinition) {
		this.compensateStartEventDefinition=compensateStartEventDefinition;
	}
	@Override
	public void execute(IActivityInstance execution) throws Exception {
		IActivityInstance outgoingExecution = execution;
		if (isInterrupting) {
			ScopeUtil.destroyScope(execution, "interrupting event subprocess started");
		} else {
			outgoingExecution = execution.createExecution(execution.getActivity());
			outgoingExecution.setActive(true);
			outgoingExecution.setScope(false);
			outgoingExecution.setConcurrent(true);
		}
		super.execute(outgoingExecution);
	}
	public void signal(IActivityInstance execution, String signalName, Object signalData) throws Exception {
		//补偿事件子流程为中断事件，则主流成中断，事件子流程流转
		//标志事件子流程中某一个活动引发的补偿结束，而不是事件子流程结束
		return;
	}
}
