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
package uap.workflow.engine.pvm.runtime;
import java.util.List;
import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.engine.bpmn.behavior.BoundaryEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.BusinessRuleTaskActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ComplexGatewayActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ErrorEndEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import uap.workflow.engine.bpmn.behavior.IntermediateCatchEventActivityBehaviour;
import uap.workflow.engine.bpmn.behavior.IntermediateThrowSignalEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.MailActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ManualTaskActivityBehavior;
import uap.workflow.engine.bpmn.behavior.NoneEndEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.NoneStartEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ParallelGatewayActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ReceiveTaskActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ScriptTaskActivityBehavior;
import uap.workflow.engine.bpmn.behavior.SubProcessActivityBehavior;
import uap.workflow.engine.bpmn.helper.ClassDelegate;
import uap.workflow.engine.context.CommitProInsCtx;
import uap.workflow.engine.context.NextReceiveTaskCtx;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.ActivityInstanceStatus;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IInstanceListener;
import uap.workflow.engine.core.IScope;
import uap.workflow.engine.core.ITransition;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.pvm.process.ActivityImpl;
import uap.workflow.engine.session.WorkflowContext;
/**
 * @author Tom Baeyens
 */
public class AtomicOperationTransitionNotifyListenerTake implements AtomicOperation {
	private static final long serialVersionUID = -6402785322880952116L;
	protected final String NUMBER_OF_INSTANCES = "nrOfInstances";
	protected final String NUMBER_OF_ACTIVE_INSTANCES = "nrOfActiveInstances";
	protected final String NUMBER_OF_COMPLETED_INSTANCES = "nrOfCompletedInstances";
	public boolean isAsync(IActivityInstance execution) {
		return false;
	}
	public void execute(IActivityInstance execution) {
		notify(execution);
		move(execution);
	}
	private void notify(IActivityInstance execution) {
		ITransition transition = execution.getTransition();
		List<IInstanceListener> executionListeners = transition.getExecutionListeners();
		int size = executionListeners.size();
		for (int i = 0; i < size; i++) {
			execution.setEventName(IInstanceListener.EVENTNAME_TAKE);
			execution.setEventSource(transition);
			IInstanceListener listener = executionListeners.get(i);
			try {
				listener.notify(execution);
			} catch (Exception e) {
				throw new WorkflowRuntimeException(e);
			}
			execution.setEventName(null);
			execution.setEventSource(null);
		}
	}
	private void move(IActivityInstance execution) {
		ITransition transition = execution.getTransition();
		IActivity activity = execution.getActivity();
		IActivity nextScope=null;
		if(activity.getActivityBehavior()instanceof BoundaryEventActivityBehavior){
			 nextScope = transition.getDestination();
		}else{
		    nextScope = findNextScope(activity.getParent(), transition.getDestination());
		}
		ActivityInstanceEntity newExe = createNextExecution(execution, nextScope);
		newExe.initialize();
		initVariableLocal(execution, newExe);
	}
	private ActivityInstanceEntity createNextExecution(IActivityInstance execution, IActivity nextScope) {
		ActivityInstanceEntity nextExecution = (ActivityInstanceEntity) execution.createExecution(nextScope);
		nextExecution.setStatus(ActivityInstanceStatus.Wait);
		//nextExecution.setProcessInstance(execution.getProcessInstance());
		//nextExecution.setParent(execution);
		//nextExecution.setActivity(nextScope);
		//nextExecution.setSuperExecution(execution.getSuperExecution());
		nextExecution.asyn();
		return nextExecution;
	}
	private void initVariableLocal(IActivityInstance execution, ActivityInstanceEntity newExe) {
		newExe.setVariableLocal(NUMBER_OF_INSTANCES, String.valueOf(this.getNrOfInstances(execution)));
		newExe.setVariableLocal(NUMBER_OF_COMPLETED_INSTANCES, String.valueOf(this.getNrOfCompletedInstances(execution)));
		newExe.performOperation(TRANSITION_CREATE_SCOPE);
	}
	private Integer getNrOfInstances(IActivityInstance execution) {
		return 0;
	}
	private Integer getNrOfCompletedInstances(IActivityInstance execution) {
		return 0;
	}
	/** finds the next scope to enter. the most outer scope is found first */
	public static IActivity findNextScope(IScope outerScopeElement, IActivity destination) {
		IActivity nextScope = destination;
		while ((nextScope.getParent() instanceof ActivityImpl) && (nextScope.getParent() != outerScopeElement)) {
			nextScope = (ActivityImpl) nextScope.getParent();
		}
		if (nextScope.getActivityBehavior() instanceof SubProcessActivityBehavior) {
			return nextScope;
		} else if (nextScope.getActivityBehavior() instanceof NoneStartEventActivityBehavior) {
			return nextScope;
		} else if (nextScope.getActivityBehavior() instanceof MailActivityBehavior) {
			return nextScope;
		} else if (nextScope.getActivityBehavior() instanceof NoneEndEventActivityBehavior) {
			return nextScope;
		} else if (nextScope.getActivityBehavior() instanceof ClassDelegate) {
			return nextScope;
		} else if (nextScope.getActivityBehavior() instanceof ExclusiveGatewayActivityBehavior) {
			return nextScope;
		} else if(nextScope.getActivityBehavior() instanceof ComplexGatewayActivityBehavior){
			return nextScope;
		}else if (nextScope.getActivityBehavior() instanceof ParallelGatewayActivityBehavior) {
			return nextScope;
		} else if (nextScope.getActivityBehavior() instanceof BoundaryEventActivityBehavior) {
			return nextScope;
		} else if (nextScope.getActivityBehavior() instanceof IntermediateCatchEventActivityBehaviour) {
			return nextScope;
		} else if (nextScope.getActivityBehavior() instanceof ErrorEndEventActivityBehavior) {
			return nextScope;
		} else if (nextScope.getActivityBehavior() instanceof ManualTaskActivityBehavior) {
			return nextScope;
		} else if (nextScope.getActivityBehavior() instanceof ReceiveTaskActivityBehavior) {
			return nextScope;
		} else if (nextScope.getActivityBehavior() instanceof ScriptTaskActivityBehavior) {
			return nextScope;
		}else if(nextScope.getActivityBehavior() instanceof IntermediateThrowSignalEventActivityBehavior){
			return nextScope;
		}else if(nextScope.getActivityBehavior() instanceof BusinessRuleTaskActivityBehavior){
			return nextScope;
		}
		if (WorkflowContext.getCurrentBpmnSession() == null) {
			return nextScope;
		}
		FlowInfoCtx flowInfoCtx = WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx();
		boolean flag = false;
		if (flowInfoCtx instanceof NextTaskInsCtx || flowInfoCtx instanceof CommitProInsCtx || flowInfoCtx instanceof NextReceiveTaskCtx) {
			UserTaskRunTimeCtx[] nextInfo = WorkflowContext.getCurrentBpmnSession().getUserTaskCtx();
			// 如果没有下一步活动信息，再进行一次，下一步信息的加载
			if (nextInfo == null) {
				throw new WorkflowRuntimeException("无下一步骤活动信息");
			} else {
				UserTaskRunTimeCtx tmpCtx = null;
				for (int i = 0; i < nextInfo.length; i++) {
					tmpCtx = nextInfo[i];
					if (tmpCtx.getActivityId().equalsIgnoreCase(nextScope.getId())) {
						WorkflowContext.getCurrentBpmnSession().setCntUserTaskInfo(tmpCtx);
						flag = true;
					}
				}
			}
		}
		if (flag) {
			return nextScope;
		} else {
			return nextScope;
			// throw new WorkflowRuntimeException("无下一步骤活动信息");
		}
	}
}
