package uap.workflow.engine.bpmn.behavior;

import java.util.ArrayList;
import java.util.List;


import uap.workflow.engine.bpmn.parser.BpmnParse;
import uap.workflow.engine.bpmn.parser.ErrorEventDefinition;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.UserTaskPrepCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IScope;
import uap.workflow.engine.delegate.BpmnError;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.pvm.process.ActivityImpl;
import uap.workflow.engine.pvm.process.ScopeImpl;
import uap.workflow.engine.pvm.runtime.AtomicOperation;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.utils.NextUserTaskInfoUtil;

public class SignalEndEventActivityBehavior extends  FlowNodeActivityBehavior {

	protected String signalRef;
	public SignalEndEventActivityBehavior(String signalRef) {
		this.signalRef = signalRef;
	}
	public void execute(IActivityInstance execution) throws Exception {
		Leavel(signalRef, execution);
	}
	public String getSignal() {
		return signalRef;
	}
	public void setSignalRef(String signalRef) {
		this.signalRef = signalRef;
	}
	public  void Leavel(String signalRef, IActivityInstance execution) throws Exception {
		// find local signal handler
		if (signalRef!=null){
		       String signalHandlerId=signalRef;
			   if (signalHandlerId != null) {
			          executeCatch(signalHandlerId, execution);
		       }
		}else{
			//signalRef is null  not support
			//broadcast is not supported
		}
	}
	private static void executeCatch(String errorHandlerId, IActivityInstance execution) {
		IProcessDefinition processDefinition = ((ActivityInstanceEntity) execution).getProcessDefinition();
		IActivity errorHandler = processDefinition.findActivity(errorHandlerId);
		if (errorHandler == null) {
			throw new WorkflowException(errorHandlerId + " not found in process definition");
		}
		boolean matchingParentFound = false;
		IActivityInstance leavingExecution = execution;
		ActivityImpl currentActivity = (ActivityImpl) execution.getActivity();
		ScopeImpl catchingScope = (ScopeImpl) errorHandler.getParent();
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
				executeEventHandler(errorHandler, leavingExecution);
			} else {
				throw new WorkflowException("No matching parent execution for activity " + errorHandlerId + " found");
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

