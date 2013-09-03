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

import uap.workflow.engine.bpmn.parser.BpmnParse;
import uap.workflow.engine.core.ActivityInstanceStatus;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.delegate.Expression;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.pvm.process.ActivityImpl;
/**
 * Implementation of the BPMN 2.0 subprocess (formally known as 'embedded'
 * subprocess): a subprocess defined within another process definition.
 * 
 * @author Joram Barrez
 */
public class SubProcessActivityBehavior extends AbstractBpmnActivityBehavior implements uap.workflow.engine.pvm.behavior.SubProcessActivityBehavior {
	protected boolean isSequential = false;
	protected Expression countExpression;
	public void execute(IActivityInstance execution) throws Exception {
//		((ExecutionEntity) execution).setStatus(ActivityInstanceStatus.Started);
//		((ExecutionEntity) execution).asyn();
		//this.createInstance(execution);
		ActivityInstanceEntity startActivityInstance = null;
		IActivity activity = execution.getActivity();
		ActivityImpl initialActivity = (ActivityImpl) activity.getProperty(BpmnParse.PROPERTYNAME_INITIAL);
		if (isSequential) {
			if (initialActivity == null) {
				throw new WorkflowException("No initial activity found for subprocess " + execution.getActivity().getId());
			}
			startActivityInstance = (ActivityInstanceEntity) execution.createExecution(initialActivity);
			startActivityInstance.setSuperExecution(execution);
			startActivityInstance.setStatus(ActivityInstanceStatus.Wait);
			startActivityInstance.asyn();
			startActivityInstance.startSubProcess(initialActivity);
		} else {
			Integer count = createInstance(execution);
			for (int i = 0; i < count; i++) {
				startActivityInstance = (ActivityInstanceEntity) execution.createExecution(initialActivity);
				startActivityInstance.setSuperExecution(execution);
				startActivityInstance.setStatus(ActivityInstanceStatus.Wait);
				startActivityInstance.asyn();
				startActivityInstance.startSubProcess(initialActivity);
			}
		}
	}
	private Integer createInstance(IActivityInstance execution) {
		// Object obj = countExpression.getValue(execution);
		Integer count = 1;
		((ActivityInstanceEntity) execution).setStatus(ActivityInstanceStatus.Started);
		((ActivityInstanceEntity) execution).asyn();
		execution.setVariableLocal("nrOfInstances", String.valueOf(count));
		return count;
	}
	public boolean isSequential() {
		return isSequential;
	}
	public void setSequential(boolean isSequential) {
		this.isSequential = isSequential;
	}
	public void lastExecutionEnded(IActivityInstance execution) {
		// ScopeUtil.createEventScopeExecution((ExecutionEntity) execution);
		bpmnActivityBehavior.performDefaultOutgoingBehavior(execution);
	}
	@Override
	public void completing(IActivityInstance execution, IActivityInstance subProcessInstance) throws Exception {
		// Scope
	}
	@Override
	public void completed(IActivityInstance execution) throws Exception {
		
	}
}
