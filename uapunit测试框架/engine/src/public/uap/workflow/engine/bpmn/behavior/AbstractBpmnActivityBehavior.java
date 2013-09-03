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
import uap.workflow.engine.bpmn.parser.BpmnParse;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IScope;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.CompensateEventSubscriptionEntity;
/**
 * Denotes an 'activity' in the sense of BPMN 2.0: a parent class for all tasks,
 * subprocess and callActivity.
 * 
 * @author Joram Barrez
 */
public class AbstractBpmnActivityBehavior extends FlowNodeActivityBehavior {
	protected MultiInstanceActivityBehavior multiInstanceActivityBehavior;
	/**
	 * Subclasses that call leave() will first pass through this method, before
	 * the regular {@link FlowNodeActivityBehavior#leave(IActivityInstance)} is
	 * called. This way, we can check if the activity has loop characteristics,
	 * and delegate to the behavior if this is the case.
	 */
	protected void leave(IActivityInstance execution) {
  		if (hasCompensationHandler(execution)) {// 是否需要进行补偿
			createCompensateEventSubscription1(execution);// 创建补偿订阅
		}
		if (!hasLoopCharacteristics()) {
			super.leave(execution);
		} else if (hasMultiInstanceCharacteristics()) {
			multiInstanceActivityBehavior.leave(execution);
		}
	}
	protected boolean hasCompensationHandler(IActivityInstance execution) {
		return execution.getActivity().getProperty(BpmnParse.PROPERTYNAME_COMPENSATION_HANDLER_ID) != null;
	}
	protected void createCompensateEventSubscription(IActivityInstance execution) {
		String compensationHandlerId = (String) execution.getActivity().getProperty(BpmnParse.PROPERTYNAME_COMPENSATION_HANDLER_ID);
		ActivityInstanceEntity executionEntity = (ActivityInstanceEntity) execution;
		IActivity compensationHandlder = executionEntity.getProcessDefinition().findActivity(compensationHandlerId);
		IScope scopeActivitiy = compensationHandlder.getParent();
		IActivityInstance scopeExecution = ScopeUtil.findScopeExecution(executionEntity, scopeActivitiy);
		CompensateEventSubscriptionEntity compensateEventSubscriptionEntity = CompensateEventSubscriptionEntity.createAndInsert(scopeExecution);
		compensateEventSubscriptionEntity.setActivity(compensationHandlder);
	}
	protected void createCompensateEventSubscription1(IActivityInstance execution) {
		String compensationHandlerId = (String) execution.getActivity().getProperty(BpmnParse.PROPERTYNAME_COMPENSATION_HANDLER_ID);
		ActivityInstanceEntity executionEntity = (ActivityInstanceEntity) execution;
		IActivity compensationHandlder = executionEntity.getProcessDefinition().findActivity(compensationHandlerId);
		//modify begin
		//IScope scopeActivitiy = compensationHandlder.getParent();
		//IActivitiExecution scopeExecution = ScopeUtil.findScopeExecution(executionEntity, scopeActivitiy);
		CompensateEventSubscriptionEntity compensateEventSubscriptionEntity = CompensateEventSubscriptionEntity.createAndInsert1(execution);
		//modify end
		compensateEventSubscriptionEntity.setActivity(compensationHandlder);
	}
	protected boolean hasLoopCharacteristics() {
		return hasMultiInstanceCharacteristics();
	}
	protected boolean hasMultiInstanceCharacteristics() {
		return multiInstanceActivityBehavior != null;
	}
	public MultiInstanceActivityBehavior getMultiInstanceActivityBehavior() {
		return multiInstanceActivityBehavior;
	}
	public void setMultiInstanceActivityBehavior(MultiInstanceActivityBehavior multiInstanceActivityBehavior) {
		this.multiInstanceActivityBehavior = multiInstanceActivityBehavior;
	}
	@Override
	public void signal(IActivityInstance execution, String signalName, Object signalData) throws Exception {
		if ("compensationDone".equals(signalName)) {
			signalCompensationDone(execution, signalData);
		} else {
			super.signal(execution, signalName, signalData);
		}
	}
	protected void signalCompensationDone(IActivityInstance execution, Object signalData) {
		if (execution.getExecutions().isEmpty()) {
			if (execution.getParent() != null) {
				IActivityInstance parent = execution.getParent();
				((ActivityInstanceEntity) parent).remove();
				((ActivityInstanceEntity) parent).signal("compensationDone", signalData);
			}
		}
	}
}
