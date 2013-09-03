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
import java.util.List;

import uap.workflow.engine.bpmn.helper.ScopeUtil;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.CompensateEventSubscriptionEntity;
/**
 * @author Daniel Meyer
 */
public class CancelBoundaryEventActivityBehavior extends FlowNodeActivityBehavior {
	@Override
	public void execute(IActivityInstance execution) throws Exception {
		List<CompensateEventSubscriptionEntity> eventSubscriptions = ((ActivityInstanceEntity) execution).getCompensateEventSubscriptions();
		if (eventSubscriptions.isEmpty()) {
			leave(execution);
		} else {
			// cancel boundary is always sync
			ScopeUtil.throwCompensationEvent(eventSubscriptions, execution, false);
		}
	}
	@Override
	public void signal(IActivityInstance execution, String signalName, Object signalData) throws Exception {
		// join compensating executions
		if (execution.getExecutions().isEmpty()) {
			leave(execution);
		}
	}
}
