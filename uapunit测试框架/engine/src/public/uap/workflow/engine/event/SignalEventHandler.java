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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import uap.workflow.engine.bpmn.behavior.BoundaryEventActivityBehavior;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.entity.EventSubscriptionEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.CommandContext;
/**
 * @author Daniel Meyer
 */
public class SignalEventHandler implements EventHandler {
	private static Logger log = Logger.getLogger(SignalEventHandler.class.getName());
	public static final String SIGNAL_EVENT_HANDLER_TYPE = "signal";
	public String getEventHandlerType() {
		return SIGNAL_EVENT_HANDLER_TYPE;
	}
	public void handleEvent(EventSubscriptionEntity eventSubscription, Object payload, CommandContext commandContext) {
		IActivityInstance execution = eventSubscription.getExecution();
		IActivity activity = eventSubscription.getActivity();
		if (activity == null) {
			throw new WorkflowException("Error while sending signal for event subscription '" + eventSubscription.getId() + "': " + "no activity associated with event subscription");
		}
		if (!execution.getActivity().equals(activity)) {
			execution.setActivity(activity);
		}
		if (payload instanceof Map) {
			@SuppressWarnings("unchecked") Map<String, Object> processVariables = (Map<String, Object>) payload;
			execution.setVariables(processVariables);
		}
		if (activity.getActivityBehavior() instanceof BoundaryEventActivityBehavior) {
			try {
				activity.getActivityBehavior().execute(execution);
			} catch (RuntimeException e) {
				log.log(Level.SEVERE, "exception while sending signal for event subscription '" + eventSubscription + "'", e);
				throw e;
			} catch (Exception e) {
				log.log(Level.SEVERE, "exception while sending signal for event subscription '" + eventSubscription + "'", e);
				throw new WorkflowException("exception while sending signal for event subscription '" + eventSubscription + "':" + e.getMessage(), e);
			}
		} else { // not boundary
			execution.signal("signal", null);
		}
	}
}
