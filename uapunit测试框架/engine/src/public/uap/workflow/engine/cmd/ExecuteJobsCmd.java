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
package uap.workflow.engine.cmd;
import java.io.Serializable;

import uap.workflow.engine.cfg.TransactionState;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.entity.JobEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.interceptor.CommandExecutor;
import uap.workflow.engine.jobexecutor.DecrementJobRetriesListener;
import uap.workflow.engine.jobexecutor.JobExecutorContext;
import uap.workflow.engine.server.BizProcessServer;
/**
 * @author Tom Baeyens
 */
public class ExecuteJobsCmd implements Command<Object>, Serializable {
	private static final long serialVersionUID = 1L;
	protected String jobId;
	public ExecuteJobsCmd(String jobId) {
		this.jobId = jobId;
	}
	// 执行job指令
	public Object execute(CommandContext commandContext) {
		// 利用jobManager找到jobentity;
		JobEntity job = commandContext.getJobManager().findJobById(jobId);
		if (job == null) {
			throw new WorkflowException("No job found with id '" + jobId + "'");
		}
		// 更新上下文信息
		JobExecutorContext jobExecutorContext = Context.getJobExecutorContext();
		jobExecutorContext.setCurrentJob(job);
		// job执行
		try {
			job.execute(commandContext);
		} catch (RuntimeException exception) {
			CommandExecutor commandExecutor = BizProcessServer.getCommandExecutorTxRequiresNew();
			commandContext.getTransactionContext().addTransactionListener(TransactionState.ROLLED_BACK, new DecrementJobRetriesListener(commandExecutor, jobId, exception));
			throw exception;
		} finally {
			if (jobExecutorContext != null) {
				jobExecutorContext.setCurrentJob(null);
			}
		}
		return null;
	}
}
