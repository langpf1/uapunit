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

import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.delegate.Expression;
import uap.workflow.engine.delegate.JavaDelegate;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.invocation.ActivityBehaviorInvocation;
import uap.workflow.engine.invocation.JavaDelegateInvocation;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.pvm.behavior.SignallableActivityBehavior;
/**
 * {@link ActivityBehavior} used when 'delegateExpression' is used for a
 * serviceTask.
 * 
 * @author Joram Barrez
 * @author Josh Long
 */
public class ServiceTaskDelegateExpressionActivityBehavior extends TaskActivityBehavior {
	protected Expression expression;
	public ServiceTaskDelegateExpressionActivityBehavior(Expression expression) {
		this.expression = expression;
	}
	@Override
	public void signal(IActivityInstance execution, String signalName, Object signalData) throws Exception {
		Object delegate = expression.getValue(execution);
		if (delegate instanceof SignallableActivityBehavior) {
			((SignallableActivityBehavior) delegate).signal(execution, signalName, signalData);
		}
	}
	public void execute(IActivityInstance execution) throws Exception {
		// Note: we can't cache the result of the expression, because the
		// execution can change: eg.
		// delegateExpression='${mySpringBeanFactory.randomSpringBean()}'
		Object delegate = expression.getValue(execution);
		if (delegate instanceof ActivityBehavior) {
			Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new ActivityBehaviorInvocation((ActivityBehavior) delegate, execution));
		} else if (delegate instanceof JavaDelegate) {
			Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new JavaDelegateInvocation((JavaDelegate) delegate, execution));
			leave(execution);
		} else {
			throw new WorkflowException("Delegate expression " + expression + " did not resolve to an implementation of " + ActivityBehavior.class + " nor " + JavaDelegate.class);
		}
	}
}
