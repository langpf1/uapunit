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
import java.io.Serializable;
import uap.workflow.engine.core.IActivityInstance;
/**
 * @author Tom Baeyens
 * @author Daniel Meyer
 */
public interface AtomicOperation extends Serializable {
	AtomicOperation PROCESS_START = new AtomicOperationProcessStart();// 流程实例启动
	AtomicOperation PROCESS_END = new AtomicOperationProcessEnd(); // 实例结束
	AtomicOperation PROCESS_START_INITIAL = new AtomicOperationProcessStartInitial();// 实例初始化（这个会被实例启动的时候调用）
	AtomicOperation ACTIVITY_START = new AtomicOperationActivityStart();// 活动启动
	AtomicOperation ACTIVITY_END = new AtomicOperationActivityEnd();// 活动结束
	AtomicOperation ACTIVITY_EXECUTE = new AtomicOperationActivityExecute(); // 活动执行,用来创建任务
	AtomicOperation TRANSITION_DESTROY_SCOPE = new AtomicOperationTransitionDestroyScope();//
	AtomicOperation TRANSITION_CREATE_SCOPE = new AtomicOperationTransitionCreateScope();//
	AtomicOperation TRANSITION_NOTIFY_LISTENER_TAKE = new AtomicOperationTransitionNotifyListenerTake();// 这个用来把当前活动切入到下个活动
	AtomicOperation TRANSITION_NOTIFY_LISTENER_START = new AtomicOperationTransitionNotifyListenerStart();// 活动进入后的启动listener执行
	AtomicOperation TRANSITION_NOTIFY_LISTENER_END = new AtomicOperationTransitionNotifyListenerEnd();// 活动完成后的结束listener执行
	AtomicOperation DELETE_CASCADE = new AtomicOperationDeleteCascade();
	AtomicOperation DELETE_CASCADE_FIRE_ACTIVITY_END = new AtomicOperationDeleteCascadeFireActivityEnd();
	void execute(IActivityInstance execution);
	boolean isAsync(IActivityInstance execution);
}
