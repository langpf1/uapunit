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
package uap.workflow.engine.bpmn.listener;

import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.ITaskListener;
import uap.workflow.engine.delegate.Expression;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.invocation.TaskListenerInvocation;
/**
 * @author Joram Barrez
 */
public class DelegateExpressionTaskListener implements ITaskListener {
	protected Expression expression;
	public DelegateExpressionTaskListener(Expression expression) {
		this.expression = expression;
	}
	public void notify(ITask delegateTask) {
		// Note: we can't cache the result of the expression, because the
		// execution can change: eg.
		// delegateExpression='${mySpringBeanFactory.randomSpringBean()}'
		Object delegate = expression.getValue(delegateTask.getExecution());
		if (delegate instanceof ITaskListener) {
			try {
				Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new TaskListenerInvocation((ITaskListener) delegate, delegateTask));
			} catch (Exception e) {
				throw new WorkflowException("Exception while invoking TaskListener: " + e.getMessage(), e);
			}
		} else {
			throw new WorkflowException("Delegate expression " + expression + " did not resolve to an implementation of " + ITaskListener.class);
		}
	}
}
