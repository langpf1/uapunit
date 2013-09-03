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
import uap.workflow.engine.core.IActivityInstance;
public class IntermediateCatchEventActivityBehaviour extends AbstractBpmnActivityBehavior {
	public void execute(IActivityInstance execution) throws Exception {
//		IActivity execActiviy = execution.getActivity();
//		List<SignalEventDefinition> signalDefinitions = (List<SignalEventDefinition>) execActiviy.getProperty(BpmnParse.PROPERTYNAME_SIGNAL_DEFINITION_NAME);
//		if (signalDefinitions != null) {
//			for (SignalEventDefinition signalDefinition : signalDefinitions) {
//				SignalEventSubscriptionEntity signalEventSubscriptionEntity = new SignalEventSubscriptionEntity((ExecutionEntity) execution);
//				signalEventSubscriptionEntity.setEventName(signalDefinition.getSignalName());
//				if (signalDefinition.getActivityId() != null) {
//					IActivity activity = execActiviy.findActivity(signalDefinition.getActivityId());
//					signalEventSubscriptionEntity.setActivity(activity);
//				}
//				Context.getCommandContext().getEventSubscriptionManager().insert(signalEventSubscriptionEntity);
//			}
//		}
//		signal(execution, null, null);	
	}
	public void signal(IActivityInstance execution, String signalName, Object signalData) throws Exception {
		leave(execution);
	}
}
