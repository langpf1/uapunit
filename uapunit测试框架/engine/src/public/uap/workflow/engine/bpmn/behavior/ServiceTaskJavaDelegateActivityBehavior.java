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

import uap.workflow.engine.cfg.ProcessEngineConfigurationImpl;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IInstanceListener;
import uap.workflow.engine.delegate.JavaDelegate;
import uap.workflow.engine.interceptor.DelegateInterceptor;
import uap.workflow.engine.invocation.JavaDelegateInvocation;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.server.BizProcessServer;
/**
 * @author Tom Baeyens
 */
public class ServiceTaskJavaDelegateActivityBehavior extends TaskActivityBehavior implements ActivityBehavior, IInstanceListener {
	protected JavaDelegate javaDelegate;
	protected ServiceTaskJavaDelegateActivityBehavior() {}
	public ServiceTaskJavaDelegateActivityBehavior(JavaDelegate javaDelegate) {
		this.javaDelegate = javaDelegate;
	}
	public void execute(IActivityInstance execution) throws Exception {
		ProcessEngineConfigurationImpl config = BizProcessServer.getProcessEngineConfig();
		DelegateInterceptor interceptor = config.getDelegateInterceptor();
		interceptor.handleInvocation(new JavaDelegateInvocation(javaDelegate, execution));
		leave(execution);
	}
	public void notify(IActivityInstance execution) throws Exception {
		execute((IActivityInstance) execution);
	}
}
