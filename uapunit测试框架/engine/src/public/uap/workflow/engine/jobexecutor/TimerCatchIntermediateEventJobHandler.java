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
package uap.workflow.engine.jobexecutor;
import java.util.ArrayList;
import java.util.List;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.UserTaskPrepCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.utils.NextUserTaskInfoUtil;
public class TimerCatchIntermediateEventJobHandler implements JobHandler {
	public static final String TYPE = "timer-intermediate-transition";
	public String getType() {
		return TYPE;
	}
	public void execute(String configuration, ActivityInstanceEntity execution, CommandContext commandContext) {
		setRuntime(execution);
		// 这个用来执行中间的time
		IActivity intermediateEventActivity = execution.getProcessDefinition().findActivity(configuration);
		if (intermediateEventActivity == null) {
			throw new WorkflowException("Error while firing timer: intermediate event activity " + configuration + " not found");
		}
		try {
			if (!execution.getActivity().getId().equals(intermediateEventActivity.getId())) {
				execution.setActivity(intermediateEventActivity);
			}
			// 这个用来执行行为
			execution.signal(null, null);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new WorkflowException("exception during timer execution: " + e.getMessage(), e);
		}
	}
	private void setRuntime(ActivityInstanceEntity execution) {
		WorkflowContext.WorkflowSession bpmnSession = new WorkflowContext().new WorkflowSession();
		WorkflowContext.setBpmnSession(bpmnSession);
		NextTaskInsCtx nextTask = new NextTaskInsCtx();
		List<UserTaskPrepCtx> userTaskPrepCtx = NextUserTaskInfoUtil.getNextUserTaskInfo(execution.getActivity(), null);
		if (userTaskPrepCtx.size() > 0) {
			List<UserTaskRunTimeCtx> nextInfo = new ArrayList<UserTaskRunTimeCtx>();
			for (int i = 0; i < userTaskPrepCtx.size(); i++) {
				UserTaskRunTimeCtx runTimeCtx = new UserTaskRunTimeCtx();
				UserTaskPrepCtx oneCtx = userTaskPrepCtx.get(i);
				runTimeCtx.setActivityId(oneCtx.getActivityId());
				runTimeCtx.setUserPks(oneCtx.getUserPks());
				nextInfo.add(runTimeCtx);
			}
			nextTask.setNextInfo(nextInfo.toArray(new UserTaskRunTimeCtx[0]));
			nextTask.setUserPk("timer-transi");
		}
		bpmnSession.setRequest(BizProcessServer.createFlowRequest(null, nextTask));
	}
}
