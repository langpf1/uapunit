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
package uap.workflow.engine.context;
import java.util.Stack;

import uap.workflow.engine.cfg.ProcessEngineConfigurationImpl;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.jobexecutor.JobExecutorContext;
/**
 * @author Tom Baeyens
 * @author Daniel Meyer
 */
public class Context {
	protected static ThreadLocal<Stack<CommandContext>> commandContextThreadLocal = new ThreadLocal<Stack<CommandContext>>();
	protected static ThreadLocal<Stack<ProcessEngineConfigurationImpl>> processEngineConfigurationStackThreadLocal = new ThreadLocal<Stack<ProcessEngineConfigurationImpl>>();
	protected static ThreadLocal<Stack<ActivityInstanceContext>> executionContextStackThreadLocal = new ThreadLocal<Stack<ActivityInstanceContext>>();
	protected static ThreadLocal<JobExecutorContext> jobExecutorContextThreadLocal = new ThreadLocal<JobExecutorContext>();
	public static CommandContext getCommandContext() {
		Stack<CommandContext> stack = getStack(commandContextThreadLocal);
		if (stack.isEmpty()) {
			return null;
		}
		return stack.peek();
	}
	public static void setCommandContext(CommandContext commandContext) {
		getStack(commandContextThreadLocal).push(commandContext);
	}
	public static void removeCommandContext() {
		getStack(commandContextThreadLocal).pop();
	}
	public static ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
		Stack<ProcessEngineConfigurationImpl> stack = getStack(processEngineConfigurationStackThreadLocal);
		if (stack.isEmpty()) {
			return null;
		}
		return stack.peek();
	}
	public static void setProcessEngineConfiguration(ProcessEngineConfigurationImpl processEngineConfiguration) {
		getStack(processEngineConfigurationStackThreadLocal).push(processEngineConfiguration);
	}
	public static void removeProcessEngineConfiguration() {
		getStack(processEngineConfigurationStackThreadLocal).pop();
	}
	public static ActivityInstanceContext getExecutionContext() {
		return getStack(executionContextStackThreadLocal).peek();
	}
	public static void setExecutionContext(IActivityInstance execution) {
		getStack(executionContextStackThreadLocal).push(new ActivityInstanceContext(execution));
	}
	public static void removeExecutionContext() {
		getStack(executionContextStackThreadLocal).pop();
	}
	protected static <T> Stack<T> getStack(ThreadLocal<Stack<T>> threadLocal) {
		Stack<T> stack = threadLocal.get();
		if (stack == null) {
			stack = new Stack<T>();
			threadLocal.set(stack);
		}
		return stack;
	}
	public static JobExecutorContext getJobExecutorContext() {
		return jobExecutorContextThreadLocal.get();
	}
	public static void setJobExecutorContext(JobExecutorContext jobExecutorContext) {
		jobExecutorContextThreadLocal.set(jobExecutorContext);
	}
	public static void removeJobExecutorContext() {
		jobExecutorContextThreadLocal.remove();
	}
}
