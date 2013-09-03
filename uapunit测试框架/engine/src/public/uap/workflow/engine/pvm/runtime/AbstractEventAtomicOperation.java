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
import java.util.List;
import uap.workflow.bizimpl.listener.ListenerBizImplExtend;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IInstanceListener;
import uap.workflow.engine.core.IScope;
import uap.workflow.engine.exception.WorkflowRuntimeException;
/**
 * @author Tom Baeyens
 */
public abstract class AbstractEventAtomicOperation implements AtomicOperation {
	private static final long serialVersionUID = 160366235819771879L;
	public boolean isAsync(IActivityInstance execution) {
		return false;
	}
	public void execute(IActivityInstance execution) {
		notifyAll(execution);
		move(execution);
	}
	protected void notifyAll(IActivityInstance execution) {
		IScope scope = getScope(execution);
		List<IInstanceListener> exectionListeners = getListeners(scope);
		int size = exectionListeners.size();
		for (int i = 0; i < size; i++) {
			IInstanceListener listener = exectionListeners.get(i);
			execution.setEventSource(scope);
			try {
				listener.notify(execution);
			} catch (Exception e) {
				throw new WorkflowRuntimeException(e);
			}
		}
/*		if (scope instanceof IActivity) {
			IActivity activity = (IActivity) scope;
			ListenerBizImplExtend listenerBizImplExtend = activity.getListenerBizImplExtend();
			if(listenerBizImplExtend==null){
				return;
			}
			listenerBizImplExtend.executeNotify(this.getEventName(), "executionListener", execution);
		}
*/	}
	private List<IInstanceListener> getListeners(IScope scope) {
		return scope.getExecutionListeners(getEventName());
	}
	protected abstract IScope getScope(IActivityInstance execution);
	protected abstract String getEventName();
	protected abstract void move(IActivityInstance execution);
}
