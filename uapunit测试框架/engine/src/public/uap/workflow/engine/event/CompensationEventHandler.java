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
package uap.workflow.engine.event;
import java.util.List;

import uap.workflow.engine.bpmn.helper.ScopeUtil;
import uap.workflow.engine.bpmn.parser.BpmnParse;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.CompensateEventSubscriptionEntity;
import uap.workflow.engine.entity.EventSubscriptionEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.pvm.runtime.AtomicOperation;
/**
 * @author Daniel Meyer
 */
public class CompensationEventHandler implements EventHandler {
	public final static String EVENT_HANDLER_TYPE = "compensate";
	public String getEventHandlerType() {
		return EVENT_HANDLER_TYPE;
	}
	public void handleEvent(EventSubscriptionEntity eventSubscription, Object payload, CommandContext commandContext) {
		String configuration = eventSubscription.getConfiguration();
		if (configuration == null) {
			throw new WorkflowException("Compensating execution not set for compensate event subscription with id " + eventSubscription.getId());
		}
		ActivityInstanceEntity compensatingExecution = commandContext.getExecutionManager().getActInsByActInsPk(configuration);
		IActivity compensationHandler = eventSubscription.getActivity();
		if ((compensationHandler.getProperty(BpmnParse.PROPERTYNAME_IS_FOR_COMPENSATION) == null || !(Boolean) compensationHandler.getProperty(BpmnParse.PROPERTYNAME_IS_FOR_COMPENSATION))
				&& compensationHandler.isScope()) {
			List<CompensateEventSubscriptionEntity> eventsForThisScope = compensatingExecution.getCompensateEventSubscriptions();
			ScopeUtil.throwCompensationEvent(eventsForThisScope, compensatingExecution, false);
		} else {
			try {
				compensatingExecution.setActivity(compensationHandler);
				compensatingExecution.performOperation(AtomicOperation.ACTIVITY_START);
			} catch (Exception e) {
				throw new WorkflowException("Error while handling compensation event " + eventSubscription, e);
			}
		}
	}
}
