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

import uap.workflow.engine.bpmn.helper.ScopeUtil;
import uap.workflow.engine.bpmn.parser.CompensateEventDefinition;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.UserTaskPrepCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.CompensateEventSubscriptionEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.pvm.process.ActivityImpl;
import uap.workflow.engine.pvm.process.ScopeImpl;
import uap.workflow.engine.pvm.runtime.AtomicOperation;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.utils.NextUserTaskInfoUtil;
/**
 * @author Daniel Meyer
 */
public class IntermediateThrowCompensationEventActivityBehavior extends FlowNodeActivityBehavior {
	protected static List<IActivityInstance> unExecution= new ArrayList<IActivityInstance>();
	protected final CompensateEventDefinition compensateEventDefinition;
	public IntermediateThrowCompensationEventActivityBehavior(CompensateEventDefinition compensateEventDefinition) {
		this.compensateEventDefinition = compensateEventDefinition;
	}
	@Override
	public void execute(IActivityInstance execution) throws Exception {
		final String activityRef = compensateEventDefinition.getActivityRef();
		IActivityInstance scopeExecution = ScopeUtil.findScopeExecution((ActivityInstanceEntity) execution, (ActivityImpl) execution.getActivity());
		List<CompensateEventSubscriptionEntity> eventSubscriptions;
		if (activityRef != null) {
			eventSubscriptions = scopeExecution.getCompensateEventSubscriptions(activityRef);
		} else {
			eventSubscriptions = scopeExecution.getCompensateEventSubscriptions();
		}
		
		if (eventSubscriptions.isEmpty()) {
			if (execution.getProcessDefinition().findActivity(activityRef).getActivityBehavior() instanceof EventSubProcessCompensateStartEventActivityBehavior) {
				Leavel(activityRef, execution);
			} else
				leave(execution);
	}
	else {
			// TODO: implement async (waitForCompletion=false in bpmn)
			eventSubscriptions.get(0).setEventType("compensate");
			ScopeUtil.throwCompensationEvent(eventSubscriptions, execution, false);
			leave(execution);
		}
	}

	public void Leavel(String signalRef, IActivityInstance execution) throws Exception {
		// find local error handler
		String signalHandlerId = signalRef;
		executeCatch(signalHandlerId, execution);
	}

	private static void executeCatch(String compensateHandlerId, IActivityInstance execution) {
		IProcessDefinition processDefinition = ((ActivityInstanceEntity) execution).getProcessDefinition();
		IActivity compensateHandler = processDefinition.findActivity(compensateHandlerId);
		if (compensateHandler == null) {
			throw new WorkflowException(compensateHandlerId + " not found in process definition");
		}
		IActivityInstance leavingExecution = execution;
		ScopeImpl catchingScope = (ScopeImpl) compensateHandler.getParent();
		if (catchingScope.contains(compensateHandler))
			executeEventHandler(compensateHandler, leavingExecution);
	}

	private static void executeEventHandler(IActivity borderEventActivity, IActivityInstance leavingExecution) {
		if (borderEventActivity.getActivityBehavior() instanceof EventSubProcessCompensateStartEventActivityBehavior) {
			IActivityInstance execution = leavingExecution;
			NextTaskInsCtx nextTaskCtx = new NextTaskInsCtx();
			List<UserTaskPrepCtx> userTaskPrepCtx = NextUserTaskInfoUtil.getNextUserTaskInfo(borderEventActivity, null);
			if (userTaskPrepCtx.size() > 0) {
				List<UserTaskRunTimeCtx> nextInfo = new ArrayList<UserTaskRunTimeCtx>();
				for (int i = 0; i < userTaskPrepCtx.size(); i++) {
					UserTaskRunTimeCtx runTimeCtx = new UserTaskRunTimeCtx();
					UserTaskPrepCtx oneCtx = userTaskPrepCtx.get(i);
					runTimeCtx.setActivityId(oneCtx.getActivityId());
					runTimeCtx.setUserPks(oneCtx.getUserPks());
					nextInfo.add(runTimeCtx);
				}
				nextTaskCtx.setNextInfo(nextInfo.toArray(new UserTaskRunTimeCtx[0]));
				nextTaskCtx.setUserPk("timer-transi");
			}
			WorkflowContext.WorkflowSession bpmnSession = new WorkflowContext().new WorkflowSession();
			WorkflowContext.setBpmnSession(bpmnSession);
			bpmnSession.setRequest(BizProcessServer.createFlowRequest(null, nextTaskCtx));
			execution.setActivity((ActivityImpl) borderEventActivity.getParent());
			execution.performOperation(AtomicOperation.ACTIVITY_START);
		} else {
			leavingExecution.startSubProcess(borderEventActivity);
		}
	}
}


