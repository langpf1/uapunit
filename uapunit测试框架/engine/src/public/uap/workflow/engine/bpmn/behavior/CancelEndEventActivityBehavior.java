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
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.pvm.process.ActivityImpl;
/**
 * @author Daniel Meyer
 */
public class CancelEndEventActivityBehavior extends FlowNodeActivityBehavior {
	@Override
	public void execute(IActivityInstance execution) throws Exception {
		// find cancel boundary event:
		IActivity cancelBoundaryEvent = ScopeUtil.findInParentScopesByBehaviorType((ActivityImpl) execution.getActivity(), CancelBoundaryEventActivityBehavior.class);
		if (cancelBoundaryEvent == null) {
			throw new WorkflowException("Could not find cancel boundary event for cancel end event " + execution.getActivity());
		}
		IActivityInstance scopeExecution = ScopeUtil.findScopeExecution((ActivityInstanceEntity) execution, cancelBoundaryEvent.getParent());
		// end all executions and process instances in the scope of the
		// transaction
		ScopeUtil.destroyScope(scopeExecution, "cancel end event fired");
		// the scope execution executes the boundary event
		IActivityInstance outgoingExecution = (IActivityInstance) scopeExecution;
		outgoingExecution.setActivity(cancelBoundaryEvent);
		outgoingExecution.setActive(true);
		// execute the boundary
		cancelBoundaryEvent.getActivityBehavior().execute(outgoingExecution);
	}
}
