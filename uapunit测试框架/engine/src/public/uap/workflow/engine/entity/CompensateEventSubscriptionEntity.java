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
package uap.workflow.engine.entity;

import uap.workflow.engine.bpmn.parser.BpmnParse;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.event.CompensationEventHandler;
import uap.workflow.engine.event.EventHandler;
import uap.workflow.engine.exception.WorkflowException;
/**
 * @author Daniel Meyer
 */
public class CompensateEventSubscriptionEntity extends EventSubscriptionEntity {
	public CompensateEventSubscriptionEntity() {}
	private CompensateEventSubscriptionEntity(IActivityInstance executionEntity) {
		super(executionEntity);
		eventType = CompensationEventHandler.EVENT_HANDLER_TYPE;
	}
	public static CompensateEventSubscriptionEntity createAndInsert(IActivityInstance executionEntity) {
		CompensateEventSubscriptionEntity eventSubscription = new CompensateEventSubscriptionEntity(executionEntity);
		eventSubscription.insert();
		return eventSubscription;
	}
	public static CompensateEventSubscriptionEntity createAndInsert1(IActivityInstance executionEntity) {
		CompensateEventSubscriptionEntity eventSubscription = new CompensateEventSubscriptionEntity(executionEntity);
		eventSubscription.activityId=(String) executionEntity.getActivity().getProperty(BpmnParse.PROPERTYNAME_COMPENSATION_HANDLER_ID);
		eventSubscription.insert();
		return eventSubscription;
	}
	
	// custom processing behavior
	// //////////////////////////////////////////////////////////////////////////////
	@Override
	protected void processEventSync(Object payload) {
	
		delete();
		super.processEventSync(payload);
	}
	// custom persistence behavior
	// /////////////////////////////////////////////////////////////////////////////
	@Override
	public void insert() {
		addToExecution();
		super.insert();
	}
	@Override
	public void delete() {
		removeFromExecution();
		super.delete();
	}
	// referential integrity CompensateEventSubscription -> ExecutionEntity
	// ////////////////////////////////////
	protected void addToExecution() {
		// add reference in execution
		IActivityInstance execution = getExecution();
		if (execution != null) {
			execution.addCompensateEventSubscription(this);//configuration set£¿£¿
		}
	}
	protected void removeFromExecution() {
		// remove reference in execution
		IActivityInstance execution = getExecution();
		if (execution != null) {
			execution.removeCompensateEventSubscription(this);
		}
	}
	public CompensateEventSubscriptionEntity moveUnder(IActivityInstance newExecution) {
		delete();
		CompensateEventSubscriptionEntity newSubscription = createAndInsert(newExecution);
		newSubscription.setActivity(getActivity());
		newSubscription.setConfiguration(configuration);
		// use the original date
		newSubscription.setCreated(created);
		return newSubscription;
	}	
}
