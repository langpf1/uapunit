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
package uap.workflow.engine.pvm.runtime;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
/**
 * @author Tom Baeyens
 */
public class AtomicOperationActivityExecute implements AtomicOperation {
	private static final long serialVersionUID = 7209994153111218269L;
	public boolean isAsync(IActivityInstance execution) {
		return false;
	}
	public void execute(IActivityInstance execution) {
		IActivity activity = execution.getActivity();
		ActivityBehavior activityBehavior = activity.getActivityBehavior();
		if (activityBehavior == null) {
			throw new WorkflowRuntimeException("no behavior specified in " + activity);
		}
		try {
			activityBehavior.execute(execution);
		} catch (Exception e) {
			throw new WorkflowRuntimeException("couldn't execute activity <" + activity.getProperty("type") + " id=\"" + activity.getId() + "\" ...>: " + e.getMessage(), e);
		}
	}
}
