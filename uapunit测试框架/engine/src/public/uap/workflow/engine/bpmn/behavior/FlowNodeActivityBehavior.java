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
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.pvm.behavior.SignallableActivityBehavior;
/**
 * Superclass for all 'connectable' BPMN 2.0 process elements: tasks, gateways
 * and events. This means that any subclass can be the source or target of a
 * sequenceflow.
 * 
 * Corresponds with the notion of the 'flownode' in BPMN 2.0.
 * 
 * @author Joram Barrez
 */
public abstract class FlowNodeActivityBehavior implements SignallableActivityBehavior {
	protected BpmnActivityBehavior bpmnActivityBehavior = new BpmnActivityBehavior();
	/**
	 * Default behaviour: just leave the activity with no extra functionality.
	 */
	public void execute(IActivityInstance execution) throws Exception {
		leave(execution);
	}
	/**
	 * Default way of leaving a BPMN 2.0 activity: evaluate the conditions on
	 * the outgoing sequence flow and take those that evaluate to true.
	 */
	protected void leave(IActivityInstance execution) {
		bpmnActivityBehavior.performDefaultOutgoingBehavior(execution);
	}
	protected void leaveIgnoreConditions(IActivityInstance activityContext) {
		bpmnActivityBehavior.performIgnoreConditionsOutgoingBehavior(activityContext);
	}
	public void signal(IActivityInstance execution, String signalName, Object signalData) throws Exception {
		throw new WorkflowException("this activity doesn't accept signals");
	}
}
