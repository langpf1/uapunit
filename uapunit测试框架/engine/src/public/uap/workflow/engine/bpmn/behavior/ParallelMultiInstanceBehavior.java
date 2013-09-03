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
import java.util.ArrayList;
import java.util.List;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
/**
 * @author Joram Barrez
 */
public class ParallelMultiInstanceBehavior extends MultiInstanceActivityBehavior {
	public ParallelMultiInstanceBehavior(IActivity activity, AbstractBpmnActivityBehavior originalActivityBehavior) {
		super(activity, originalActivityBehavior);
	}
	/**
	 * Handles the parallel case of spawning the instances. Will create child
	 * executions accordingly for every instance needed.
	 */
	protected void createInstances(IActivityInstance execution) throws Exception {
		int nrOfInstances = resolveNrOfInstances(execution);
		if (nrOfInstances <= 0) {
			throw new WorkflowException("Invalid number of instances: must be positive integer value" + ", but was " + nrOfInstances);
		}
		setLoopVariable(execution, NUMBER_OF_INSTANCES, nrOfInstances);
		setLoopVariable(execution, NUMBER_OF_COMPLETED_INSTANCES, 0);
		//setLoopVariable(execution, NUMBER_OF_ACTIVE_INSTANCES, nrOfInstances);
		List<IActivityInstance> concurrentExecutions = new ArrayList<IActivityInstance>();
		for (int loopCounter = 0; loopCounter < nrOfInstances; loopCounter++) {
			IActivityInstance concurrentExecution = execution.createExecution(execution.getActivity());
			concurrentExecution.setActive(true);
			concurrentExecution.setConcurrent(true);
			concurrentExecution.setScope(false);
			concurrentExecution.setSuperExecution(execution);	//9.2 +
			// In case of an embedded subprocess, and extra child execution is
			// required
			// Otherwise, all child executions would end up under the same
			// parent,
			// without any differentation to which embedded subprocess they
			// belong
			if (isExtraScopeNeeded()) {
				IActivityInstance extraScopedExecution = concurrentExecution.createExecution(execution.getActivity());
				extraScopedExecution.setActive(true);
				extraScopedExecution.setConcurrent(false);
				extraScopedExecution.setScope(true);
				concurrentExecution = extraScopedExecution;
			}
			execution.getExecutions().add(concurrentExecution);
			concurrentExecutions.add(concurrentExecution);
			logLoopDetails(concurrentExecution, "initialized", loopCounter, 0, nrOfInstances, nrOfInstances);
		}
		// Before the activities are executed, all executions MUST be created up
		// front
		// Do not try to merge this loop with the previous one, as it will lead
		// to bugs,
		// due to possible child execution pruning.
		for (int loopCounter = 0; loopCounter < nrOfInstances; loopCounter++) {
			IActivityInstance concurrentExecution = concurrentExecutions.get(loopCounter);
			// executions can be inactive, if instances are all automatics
			// (no-waitstate)
			// and completionCondition has been met in the meantime
			if (concurrentExecution.isActive() && !concurrentExecution.isEnded() && concurrentExecution.getParent().isActive() && !concurrentExecution.getParent().isEnded()) {
				setLoopVariable(concurrentExecution, LOOP_COUNTER, loopCounter);
				executeOriginalBehavior(concurrentExecution, loopCounter);
			}
		}
	}
	/**
	 * Called when the wrapped {@link ActivityBehavior} calls the
	 * {@link AbstractBpmnActivityBehavior#leave(IActivityInstance)} method.
	 * Handles the completion of one of the parallel instances
	 */
	public void leave(IActivityInstance execution) {
		callActivityEndListeners(execution);
		//int loopCounter = getLoopVariable(execution, LOOP_COUNTER);
		int nrOfInstances = getLoopVariable(execution, NUMBER_OF_INSTANCES);
		int nrOfCompletedInstances = getLoopVariable(execution, NUMBER_OF_COMPLETED_INSTANCES) + 1;
	//	int nrOfActiveInstances = getLoopVariable(execution, NUMBER_OF_ACTIVE_INSTANCES) - 1;
		//if (isExtraScopeNeeded()) {
			// In case an extra scope was created, it must be destroyed first
			// before going further
			//ActivityInstanceEntity extraScope = (ActivityInstanceEntity) execution;
			//execution = execution.getParent();
			//extraScope.remove();
		//}
		setLoopVariable(execution, NUMBER_OF_COMPLETED_INSTANCES, nrOfCompletedInstances);
		//setLoopVariable(execution.getParent(), NUMBER_OF_COMPLETED_INSTANCES, nrOfCompletedInstances);
		//setLoopVariable(execution.getParent(), NUMBER_OF_ACTIVE_INSTANCES, nrOfActiveInstances);
		//logLoopDetails(execution, "instance completed", loopCounter, nrOfCompletedInstances, nrOfActiveInstances, nrOfInstances);
		ActivityInstanceEntity executionEntity = (ActivityInstanceEntity) execution;
		List<IActivityInstance> joinedExecutions = executionEntity.findInactiveConcurrentExecutions(execution.getActivity());
		if (joinedExecutions.size() == nrOfInstances || completionConditionSatisfied(execution)) {
			
			if(execution.getSuperExecution()!= null)
			{
				execution.end();
				execution.getSuperExecution().takeAll(execution.getSuperExecution().getActivity().getOutgoingTransitions(), joinedExecutions);
			}
			// Removing all active child executions (ie because
			// completionCondition is true)
//			List<ActivityInstanceEntity> executionsToRemove = new ArrayList<ActivityInstanceEntity>();
//			if(executionEntity.getParent()!= null){
//			for (IActivityInstance childExecution : executionEntity.getParent().getExecutions()) {
//				if (childExecution.isActive()) {
//					executionsToRemove.add((ActivityInstanceEntity) childExecution);
//				}
//			}
//			for (ActivityInstanceEntity executionToRemove : executionsToRemove) {
//				if (LOGGER.isLoggable(Level.FINE)) {
//					LOGGER.fine("Execution " + executionToRemove + " still active, " + "but multi-instance is completed. Removing this execution.");
//				}
//				executionToRemove.deleteCascade("multi-instance completed");
//			}
//			}
//			executionEntity.takeAll(executionEntity.getActivity().getOutgoingTransitions(), joinedExecutions);
		}
	}
}
