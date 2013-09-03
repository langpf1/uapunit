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
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.pvm.process.ActivityImpl;
/**
 * @author Tom Baeyens
 */
public class AtomicOperationDeleteCascadeFireActivityEnd extends AbstractEventAtomicOperation {
	private static final long serialVersionUID = 6041510948461906101L;
	@Override
	protected IScope getScope(IActivityInstance execution) {
		ActivityImpl activity = (ActivityImpl) execution.getActivity();
		if (activity != null) {
			return activity;
		} else {
			ActivityInstanceEntity parent = (ActivityInstanceEntity) execution.getParent();
			if (parent != null) {
				return getScope((ActivityInstanceEntity) execution.getParent());
			}
			return execution.getProcessDefinition();
		}
	}
	@Override
	protected String getEventName() {
		return IInstanceListener.EVENTNAME_END;
	}
	@Override
	protected void move(IActivityInstance execution) {
		IActivity activity = (IActivity) execution.getActivity();
		if ((execution.isScope()) && (activity != null) && (!activity.isScope())) {
			execution.setActivity((IActivity) activity.getParent());
			execution.performOperation(AtomicOperation.DELETE_CASCADE_FIRE_ACTIVITY_END);
		} else {
			if (execution.isScope()) {
				execution.destroy();
			}
			execution.remove();
			if (!execution.isDeleteRoot()) {
				IActivityInstance parent = (IActivityInstance) execution.getParent();
				if (parent != null) {
					parent.performOperation(AtomicOperation.DELETE_CASCADE);
				}
			}
		}
	}
}
