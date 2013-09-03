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
import java.util.Arrays;
import java.util.List;

import uap.workflow.engine.bpmn.parser.BpmnParse;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.ITransition;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.query.Condition;
/**
 * Helper class for implementing BPMN 2.0 activities, offering convenience
 * methods specific to BPMN 2.0.
 * 
 * This class can be used by inheritance or aggregation.
 * 
 * @author Joram Barrez
 */
public class BpmnActivityBehavior {
	/**
	 * Performs the default outgoing BPMN 2.0 behavior, which is having parallel
	 * paths of executions for the outgoing sequence flow.
	 * 
	 * More precisely: every sequence flow that has a condition which evaluates
	 * to true (or which doesn't have a condition), is selected for continuation
	 * of the process instance. If multiple sequencer flow are selected,
	 * multiple, parallel paths of executions are created.
	 */
	public void performDefaultOutgoingBehavior(IActivityInstance activityExceution) {
		performOutgoingBehavior(activityExceution, true, false, null);
	}
	/**
	 * Performs the default outgoing BPMN 2.0 behavior (@see
	 * {@link #performDefaultOutgoingBehavior(IActivityInstance)}), but without
	 * checking the conditions on the outgoing sequence flow.
	 * 
	 * This means that every outgoing sequence flow is selected for continuing
	 * the process instance, regardless of having a condition or not. In case of
	 * multiple outgoing sequence flow, multiple parallel paths of executions
	 * will be created.
	 */
	public void performIgnoreConditionsOutgoingBehavior(IActivityInstance activityExecution) {
		performOutgoingBehavior(activityExecution, false, false, null);
	}
	/**
	 * Actual implementation of leaving an activity.
	 * 
	 * @param execution
	 *            The current execution context
	 * @param checkConditions
	 *            Whether or not to check conditions before determining whether
	 *            or not to take a transition.
	 * @param throwExceptionIfExecutionStuck
	 *            If true, an {@link WorkflowException} will be thrown in case
	 *            no transition could be found to leave the activity.
	 */
	protected void performOutgoingBehavior(IActivityInstance execution, boolean checkConditions, boolean throwExceptionIfExecutionStuck, List<IActivityInstance> reusableExecutions) {
		List<ITransition> transitionsToTake = new ArrayList<ITransition>();
		collectTransition(execution, checkConditions, transitionsToTake);
		if (transitionsToTake.size() == 1) {
			turnOne(execution, transitionsToTake);
		}
		if (transitionsToTake.size() == 0) {
			turnZero(execution, throwExceptionIfExecutionStuck);
		}
		if (transitionsToTake.size() > 1) {
			turnMore(execution, reusableExecutions, transitionsToTake);
		}
	}
	private void turnZero(IActivityInstance execution, boolean throwExceptionIfExecutionStuck) {
		String defaultSequenceFlow = (String) execution.getActivity().getProperty("default");
		if (defaultSequenceFlow == null) {
			Object isForCompensation = execution.getActivity().getProperty(BpmnParse.PROPERTYNAME_IS_FOR_COMPENSATION);
			if (isForCompensation == null || (isForCompensation != null && !(Boolean) isForCompensation)) {
				execution.end();
				if (throwExceptionIfExecutionStuck) {
					throw new WorkflowException("No outgoing sequence flow of the inclusive gateway '" + execution.getActivity().getId() + "' could be selected for continuing the process");
				}
			} else {
				IActivityInstance parentExecution = execution.getParent();
				execution.remove();
				parentExecution.signal("compensationDone", null);
			}
		} else {
			ITransition defaultTransition = execution.getActivity().findOutgoingTransition(defaultSequenceFlow);
			if (defaultTransition == null) {
				throw new WorkflowException("Default sequence flow '" + defaultSequenceFlow + "' could not be not found");
			} else {
				execution.take(defaultTransition);
			}
		}
	}
	private void turnMore(IActivityInstance execution, List<IActivityInstance> reusableExecutions, List<ITransition> transitionsToTake) {
		if (reusableExecutions == null || reusableExecutions.isEmpty()) {
			execution.takeAll(transitionsToTake, Arrays.asList(execution));
		} else {
			execution.takeAll(transitionsToTake, reusableExecutions);
		}
	}
	private void turnOne(IActivityInstance execution, List<ITransition> transitionsToTake) {
		execution.take(transitionsToTake.get(0));
	}
	private void collectTransition(IActivityInstance execution, boolean checkConditions, List<ITransition> transitionsToTake) {
		String defaultSequenceFlow = (String) execution.getActivity().getProperty("default");
		List<ITransition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();
		for (ITransition outgoingTransition : outgoingTransitions) {
			if (defaultSequenceFlow == null || !outgoingTransition.getId().equals(defaultSequenceFlow)) {
				Condition condition = (Condition) outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
				if (condition == null || !checkConditions || condition.evaluate(execution)) {
					transitionsToTake.add(outgoingTransition);
				}
			}
		}
	}
}
