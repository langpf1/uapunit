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
import java.util.ArrayList;
import java.util.List;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.UserTaskPrepCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.ITransition;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.utils.NextUserTaskInfoUtil;
/**
 * @author Joram Barrez
 */
public class BoundaryEventActivityBehavior extends FlowNodeActivityBehavior {
	protected boolean interrupting;
	public BoundaryEventActivityBehavior() {}
	public BoundaryEventActivityBehavior(boolean interrupting) {
		this.interrupting = interrupting;
	}
	public void execute(IActivityInstance execution) throws Exception {
		WorkflowContext.WorkflowSession currSession = WorkflowContext.getCurrentBpmnSession();
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
		bpmnSession.setRequest(BizProcessServer.createFlowRequest(currSession.getBusinessObject(), nextTask));
		List<ITransition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();
		List<IActivityInstance> interruptedExecutions =  new ArrayList<IActivityInstance>() ;
		if (interrupting) {
//			if (executionEntity.getSubProcessInstance() != null) {
//				//executionEntity.getSubProcessInstance().deleteCascade(executionEntity.getDeleteReason());
//			}
//			interruptedExecutions = new ArrayList<IActivitiExecution>(executionEntity.getExecutions());
//			for (IActivitiExecution interruptedExecution : interruptedExecutions) {
//				interruptedExecution.deleteCascade("interrupting boundary event '" + execution.getActivity().getId() + "' fired");
//			}
			execution.takeAll(outgoingTransitions, interruptedExecutions);
		} else {
			IActivityInstance outgoingExecution = execution;
			outgoingExecution.setActive(true);
			outgoingExecution.setScope(false);
			outgoingExecution.setConcurrent(true);
			outgoingExecution.takeAll(outgoingTransitions,interruptedExecutions);
		}
	}
	public boolean isInterrupting() {
		return interrupting;
	}
	public void setInterrupting(boolean interrupting) {
		this.interrupting = interrupting;
	}
}
