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
import uap.workflow.engine.core.IInstanceListener;
import uap.workflow.engine.core.IScope;
import uap.workflow.engine.core.ITransition;
/**
 * @author Tom Baeyens
 */
public class AtomicOperationTransitionNotifyListenerStart extends AbstractEventAtomicOperation {
	private static final long serialVersionUID = -5058173642487002681L;
	@Override
	protected String getEventName() {
		return IInstanceListener.EVENTNAME_START;
	}
	@Override
	protected void move(IActivityInstance execution) {
		ITransition transition = execution.getTransition();
		IActivity destination = null;
		if (transition == null) {
			destination = execution.getActivity();
		} else {
			destination = transition.getDestination();
		}
		execution.setTransition(null);
		execution.setActivity(destination);
		execution.performOperation(ACTIVITY_EXECUTE);
	}
	@Override
	protected IScope getScope(IActivityInstance execution) {
		return execution.getActivity();
	}
}
