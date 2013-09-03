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

import uap.workflow.engine.core.ActivityInstanceStatus;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
/**
 * @author Joram Barrez
 * @author Falko Menge
 */
public class SequentialMultiInstanceBehavior extends MultiInstanceActivityBehavior {
	public SequentialMultiInstanceBehavior(IActivity activity, AbstractBpmnActivityBehavior innerActivityBehavior) {
		super(activity, innerActivityBehavior);
	}
	/**
	 * Handles the sequential case of spawning the instances. Will only create
	 * one instance, since at most one instance can be active.
	 */
	protected void createInstances(IActivityInstance execution) throws Exception {
		int nrOfInstances = resolveNrOfInstances(execution);
		if (nrOfInstances <= 0) {
			throw new WorkflowException("Invalid number of instances: must be positive integer value" + ", but was " + nrOfInstances);
		}
		setLoopVariable(execution, NUMBER_OF_INSTANCES, nrOfInstances);
		// setLoopVariable(execution, NUMBER_OF_COMPLETED_INSTANCES, 0);
		// setLoopVariable(execution, LOOP_COUNTER, 0);
		// setLoopVariable(execution, NUMBER_OF_ACTIVE_INSTANCES, 1);
		// logLoopDetails(execution, "initialized", 0, 0, 1, nrOfInstances);
		((ActivityInstanceEntity) execution).setStatus(ActivityInstanceStatus.Started);
		((ActivityInstanceEntity) execution).asyn();
		executeOriginalBehavior(execution, 0);
	}
	/**
	 * Called when the wrapped {@link ActivityBehavior} calls the
	 * {@link AbstractBpmnActivityBehavior#leave(IActivityInstance)} method.
	 * Handles the completion of one instance, and executes the logic for the
	 * sequential behavior.
	 */
	public void leave(IActivityInstance execution) {
		callActivityEndListeners(execution);
		// int loopCounter = getLoopVariable(execution, LOOP_COUNTER) + 1;
		// int nrOfInstances = getLoopVariable(execution, NUMBER_OF_INSTANCES);
		// int nrOfCompletedInstances = getLoopVariable(execution,
		// NUMBER_OF_COMPLETED_INSTANCES) + 1;
		// int nrOfActiveInstances = getLoopVariable(execution,
		// NUMBER_OF_ACTIVE_INSTANCES);
		// setLoopVariable(execution, LOOP_COUNTER, loopCounter);
		// setLoopVariable(execution, NUMBER_OF_COMPLETED_INSTANCES,
		// nrOfCompletedInstances);
		// logLoopDetails(execution, "instance completed", loopCounter,
		// nrOfCompletedInstances, nrOfActiveInstances, nrOfInstances);
		// if (loopCounter == nrOfInstances ||
		// completionConditionSatisfied(execution)) {
		super.leave(execution);
		// } else {
		// try {
		// executeOriginalBehavior(execution, loopCounter);
		// } catch (BpmnError error) {
		// // re-throw business fault so that it can be caught by an Error
		// // Intermediate Event or Error Event Sub-Process in the process
		// throw error;
		// } catch (Exception e) {
		// throw new
		// ActivitiException("Could not execute inner activity behavior of multi instance behavior",
		// e);
		// }
		// }
	}
}
