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
package uap.workflow.engine.core;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import uap.workflow.engine.entity.ProcessDefinitionEntity;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.pvm.process.TransitionImpl;
/**
 * @author Tom Baeyens
 */
public class ProcessDefinitionBuilder {
	protected IProcessDefinition processDefinition;
	protected Stack<IScope> scopeStack = new Stack<IScope>();
	protected IProcessElement processElement;
	protected ITransition transition;
	protected List<Object[]> unresolvedTransitions = new ArrayList<Object[]>();
	public ProcessDefinitionBuilder() {
		this(null);
	}
	public ProcessDefinitionBuilder(String processDefinitionId) {
		processDefinition = new ProcessDefinitionEntity(processDefinitionId);
		scopeStack.push(processDefinition);
	}
	public ProcessDefinitionBuilder createActivity(String id) {
		IActivity activity = scopeStack.peek().createActivity(id);
		scopeStack.push(activity);
		processElement = activity;
		transition = null;
		return this;
	}
	public ProcessDefinitionBuilder endActivity() {
		scopeStack.pop();
		processElement = scopeStack.peek();
		transition = null;
		return this;
	}
	public ProcessDefinitionBuilder initial() {
		processDefinition.setInitial(getActivity());
		return this;
	}
	public ProcessDefinitionBuilder startTransition(String destinationActivityId) {
		return startTransition(destinationActivityId, null);
	}
	public ProcessDefinitionBuilder startTransition(String destinationActivityId, String transitionId) {
		IActivity activity = getActivity();
		transition = (TransitionImpl) activity.createOutgoingTransition(transitionId);
		unresolvedTransitions.add(new Object[] { transition, destinationActivityId });
		processElement = transition;
		return this;
	}
	public ProcessDefinitionBuilder endTransition() {
		processElement = scopeStack.peek();
		transition = null;
		return this;
	}
	public ProcessDefinitionBuilder transition(String destinationActivityId) {
		return transition(destinationActivityId, null);
	}
	public ProcessDefinitionBuilder transition(String destinationActivityId, String transitionId) {
		startTransition(destinationActivityId, transitionId);
		endTransition();
		return this;
	}
	public ProcessDefinitionBuilder behavior(ActivityBehavior activityBehaviour) {
		getActivity().setActivityBehavior(activityBehaviour);
		return this;
	}
	public ProcessDefinitionBuilder property(String name, Object value) {
		processElement.setProperty(name, value);
		return this;
	}
	public IProcessDefinition buildProcessDefinition() {
		for (Object[] unresolvedTransition : unresolvedTransitions) {
			TransitionImpl transition = (TransitionImpl) unresolvedTransition[0];
			String destinationActivityName = (String) unresolvedTransition[1];
			IActivity destination = processDefinition.findActivity(destinationActivityName);
			if (destination == null) {
				throw new RuntimeException("destination '" + destinationActivityName + "' not found.  (referenced from transition in '" + transition.getSource().getId() + "')");
			}
			transition.setDestination(destination);
		}
		return processDefinition;
	}
	protected IActivity getActivity() {
		return (IActivity) scopeStack.peek();
	}
	public ProcessDefinitionBuilder scope() {
		getActivity().setScope(true);
		return this;
	}
	public ProcessDefinitionBuilder executionListener(IInstanceListener executionListener) {
		if (transition != null) {
			transition.addExecutionListener(executionListener);
		}
		return this;
	}
	public ProcessDefinitionBuilder executionListener(String eventName, IInstanceListener executionListener) {
		if (transition == null) {
			scopeStack.peek().addExecutionListener(eventName, executionListener);
		}
		return this;
	}
}
