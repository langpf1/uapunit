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
import uap.workflow.engine.core.IInstanceListener;
import uap.workflow.engine.core.IScope;
/**
 * @author Tom Baeyens
 */
public class AtomicOperationActivityStart extends AbstractEventAtomicOperation {
	private static final long serialVersionUID = 4040706387225979695L;
	@Override
	protected void move(IActivityInstance execution) {
		execution.performOperation(AtomicOperation.ACTIVITY_EXECUTE);
	}
	@Override
	protected String getEventName() {
		return IInstanceListener.EVENTNAME_START;
	}
	@Override
	protected IScope getScope(IActivityInstance execution) {
		return execution.getActivity();
	}
}
