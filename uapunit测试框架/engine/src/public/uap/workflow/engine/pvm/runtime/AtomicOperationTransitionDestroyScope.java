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
import uap.workflow.engine.core.ActivityInstanceStatus;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IScope;
import uap.workflow.engine.entity.ActivityInstanceEntity;
/**
 * @author Tom Baeyens
 */
public class AtomicOperationTransitionDestroyScope implements AtomicOperation {
	private static final long serialVersionUID = 5988148974199987923L;
	public boolean isAsync(IActivityInstance execution) {
		return false;
	}
	public void execute(IActivityInstance activityInstance) {
		// 标志上一个活动节点是完成状态
		activityInstance.setStatus(ActivityInstanceStatus.Finished);
		activityInstance.setPass(true);
		if (!activityInstance.isExe()) {
			activityInstance.setExe(true);
		}
		activityInstance.asyn();
		// 执行跳转到下一个活动接点
		activityInstance.performOperation(TRANSITION_NOTIFY_LISTENER_TAKE);
	}
	public boolean transitionLeavesNextOuterScope(IScope nextScopeElement, IActivity destination) {
		return !nextScopeElement.contains(destination);
	}
}
