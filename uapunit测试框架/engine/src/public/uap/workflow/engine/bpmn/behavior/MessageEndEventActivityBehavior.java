package uap.workflow.engine.bpmn.behavior;

import java.util.ArrayList;
import java.util.List;


import uap.workflow.engine.bpmn.behavior.EventSubProcessStartEventActivityBehavior;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.UserTaskPrepCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.pvm.process.ActivityImpl;
import uap.workflow.engine.pvm.process.ScopeImpl;
import uap.workflow.engine.pvm.runtime.AtomicOperation;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.utils.NextUserTaskInfoUtil;

public class MessageEndEventActivityBehavior implements ActivityBehavior {
	protected  String  activityRef;
	public MessageEndEventActivityBehavior(String activityRef) {
	this.activityRef=activityRef;
	}
	@Override
	public void execute(IActivityInstance execution) throws Exception {
		  executeCatch(this.activityRef, execution);
	}
	private static void executeCatch(String compensateHandlerId, IActivityInstance execution) {
		IProcessDefinition processDefinition = ((ActivityInstanceEntity) execution).getProcessDefinition();
		IActivity compensateHandler = processDefinition.findActivity(compensateHandlerId);
		if (compensateHandler == null) {
			throw new WorkflowException(compensateHandlerId + " not found in process definition");
		}
		boolean matchingParentFound = false;
		IActivityInstance leavingExecution = execution;
		ActivityImpl currentActivity = (ActivityImpl) execution.getActivity();
		ScopeImpl catchingScope = (ScopeImpl) compensateHandler.getParent();
		if (catchingScope instanceof ActivityImpl) {
			ActivityImpl catchingScopeActivity = (ActivityImpl) catchingScope;
			if (!catchingScopeActivity.isScope()) { // event subprocesses
				catchingScope = (ScopeImpl) catchingScopeActivity.getParent();
			}
		}
		if (catchingScope instanceof IProcessDefinition) {
			// executeEventHandler(errorHandler, ((ExecutionEntity)
			// execution).getProcessInstance());
		} else {
			if (currentActivity.getId().equals(catchingScope.getId())) {
				matchingParentFound = true;
			} else {
				currentActivity = (ActivityImpl) currentActivity.getParent();
				// Traverse parents until one is found that is a scope
				// and matches the activity the boundary event is defined on
				while (!matchingParentFound && leavingExecution != null && currentActivity != null) {
					if (!leavingExecution.isConcurrent() && currentActivity.getId().equals(catchingScope.getId())) {
						matchingParentFound = true;
					} else if (leavingExecution.isConcurrent()) {
						leavingExecution = leavingExecution.getParent();
					} else {
						currentActivity = (ActivityImpl) currentActivity.getParentActivity();
						leavingExecution = leavingExecution.getParent();
					}
				}
				// Follow parents up until matching scope can't be found anymore
				// (needed to support for multi-instance)
				while (leavingExecution != null && leavingExecution.getParent() != null && leavingExecution.getParent().getActivity() != null
						&& leavingExecution.getParent().getActivity().getId().equals(catchingScope.getId())) {
					leavingExecution = leavingExecution.getParent();
				}
			}
			if (matchingParentFound && leavingExecution != null) {
				executeEventHandler(compensateHandler, leavingExecution);
			} else {
				throw new WorkflowException("No matching parent execution for activity " + compensateHandlerId + " found");
			}
		}
	}
	private static void executeEventHandler(IActivity borderEventActivity, IActivityInstance leavingExecution) {
		if (borderEventActivity.getActivityBehavior() instanceof EventSubProcessStartEventActivityBehavior) {
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

