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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import uap.workflow.engine.bpmn.parser.BpmnParse;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.ITransition;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.query.Condition;
/**
 * Implementation of the Inclusive Gateway/OR gateway/inclusive data-based
 * gateway as defined in the BPMN specification.
 * 
 * @author Tijs Rademakers
 * @author Tom Van Buskirk
 * @author Joram Barrez
 */
public class InclusiveGatewayActivityBehavior extends GatewayActivityBehavior {
	private static Logger log = Logger.getLogger(InclusiveGatewayActivityBehavior.class.getName());
	public void execute(IActivityInstance execution) throws Exception {
	    execution.setActive(false);
		lockConcurrentRoot(execution);
		IActivity activity = execution.getActivity();
		if (!activeConcurrentExecutionsExist(execution)) {
			if (log.isLoggable(Level.FINE)) {
				log.fine("inclusive gateway '" + activity.getId() + "' activates");
			}
			List<IActivityInstance> joinedExecutions = execution.findInactiveConcurrentExecutions(activity);
			String defaultSequenceFlow = (String) execution.getActivity().getProperty("default");
			List<ITransition> transitionsToTake = new ArrayList<ITransition>();
			for (ITransition outgoingTransition : execution.getActivity().getOutgoingTransitions()) {
				if (defaultSequenceFlow == null || !outgoingTransition.getId().equals(defaultSequenceFlow)) {
					Condition condition = (Condition) outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
					if (condition == null || condition.evaluate(execution)) {
						transitionsToTake.add(outgoingTransition);
					}
				}
			}
			if (transitionsToTake.size() > 0) {
				execution.takeAll(transitionsToTake, joinedExecutions);
			} else {
				if (defaultSequenceFlow != null) {
					ITransition defaultTransition = execution.getActivity().findOutgoingTransition(defaultSequenceFlow);
					if (defaultTransition != null) {
						execution.take(defaultTransition);
					} else {
						throw new WorkflowException("Default sequence flow '" + defaultSequenceFlow + "' could not be not found");
					}
				} else {
					// No sequence flow could be found, not even a default one
					throw new WorkflowException("No outgoing sequence flow of the inclusive gateway '" + execution.getActivity().getId() + "' could be selected for continuing the process");
				}
			}
		} else {
			if (log.isLoggable(Level.FINE)) {
				log.fine("Inclusive gateway '" + activity.getId() + "' does not activate");
			}
		}
	}
	public boolean activeConcurrentExecutionsExist(IActivityInstance execution) {
		IActivity activity = execution.getActivity();
		if (execution.isConcurrent()) {
			for (IActivityInstance concurrentExecution : execution.getParent().getExecutions()) {
				if (concurrentExecution.isActive() && concurrentExecution.getActivity() != activity) {
					// TODO: when is transitionBeingTaken cleared? Should we
					// clear it?
					boolean reachable = false;
					ITransition pvmTransition = ((ActivityInstanceEntity) concurrentExecution).getTransitionBeingTaken();
					if (pvmTransition != null) {
						reachable = isReachable(pvmTransition.getDestination(), activity, new HashSet<IActivity>());
					} else {
						reachable = isReachable(concurrentExecution.getActivity(), activity, new HashSet<IActivity>());
					}
					if (reachable) {
						if (log.isLoggable(Level.FINE)) {
							log.fine("an active concurrent execution found: '" + concurrentExecution.getActivity());
						}
						return true;
					}
				}
			}
		} else if (execution.isActive()) { // is this ever true?
			if (log.isLoggable(Level.FINE)) {
				log.fine("an active concurrent execution found: '" + execution.getActivity());
			}
			return true;
		}
		return false;
	}
	protected boolean isReachable(IActivity srcActivity, IActivity targetActivity, Set<IActivity> visitedActivities) {
		if (srcActivity.equals(targetActivity)) {
			return true;
		}
		// To avoid infinite looping, we must capture every node we visit
		// and check before going further in the graph if we have already
		// visitied the node.
		visitedActivities.add(srcActivity);
		List<ITransition> transitionList = srcActivity.getOutgoingTransitions();
		if (transitionList != null && transitionList.size() > 0) {
			for (ITransition pvmTransition : transitionList) {
				IActivity destinationActivity = pvmTransition.getDestination();
				if (destinationActivity != null && !visitedActivities.contains(destinationActivity)) {
					boolean reachable = isReachable(destinationActivity, targetActivity, visitedActivities);
					// If false, we should investigate other paths, and not yet
					// return the result
					if (reachable) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
