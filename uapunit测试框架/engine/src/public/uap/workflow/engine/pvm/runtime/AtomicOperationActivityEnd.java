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

import uap.workflow.engine.bpmn.behavior.CallActivityBehavior;
import uap.workflow.engine.bpmn.behavior.SequentialMultiInstanceBehavior;
import uap.workflow.engine.bpmn.behavior.SubProcessActivityBehavior;
import uap.workflow.engine.cmpltsgy.CompleteSgyManager;
import uap.workflow.engine.core.ActivityInstanceStatus;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IInstanceListener;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IScope;
import uap.workflow.engine.core.ITransition;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.pvm.behavior.CompositeActivityBehavior;
import uap.workflow.engine.pvm.process.ActivityImpl;
import uap.workflow.engine.pvm.process.ScopeImpl;
/**
 * @author Tom Baeyens
 */
public class AtomicOperationActivityEnd extends AbstractEventAtomicOperation {
	private static final long serialVersionUID = 1L;
	@Override
	protected String getEventName() {
		return IInstanceListener.EVENTNAME_END;
	}
	@Override
	protected void move(IActivityInstance activityInstance) {
		activityInstance.setStatus(ActivityInstanceStatus.Finished);
		activityInstance.setPass(true);
		if(!activityInstance.isExe())
		{
			activityInstance.setExe(true);
		}
		activityInstance.asyn();
		
		IActivity activity = (IActivity) activityInstance.getActivity();
		IScope parentScope = activity.getParent();
		if (activityInstance.getSuperExecution() != null) {
			IActivityInstance superExecution = activityInstance.getSuperExecution();
			Integer cmpleteCount = Integer.parseInt(String.valueOf( superExecution.getVariableLocal("nrOfCompletedInstances")));
			superExecution.setVariableLocal("nrOfCompletedInstances", String.valueOf(cmpleteCount + 1));
			boolean flag = CompleteSgyManager.getInstance().isComplete(superExecution, cmpleteCount + 1);
			if (parentScope instanceof IActivity) {
				IActivity parentActivity = (IActivity) parentScope;
				if (flag) {
					ActivityBehavior parentActivityBehavior = (parentActivity != null ? parentActivity.getActivityBehavior() : null);
					if (parentActivityBehavior instanceof CompositeActivityBehavior) {
						CompositeActivityBehavior compositeActivityBehavior = (CompositeActivityBehavior) parentActivity.getActivityBehavior();
						compositeActivityBehavior.lastExecutionEnded(superExecution);
					}
				} else {
					ActivityBehavior parentActivityBehavior = (parentActivity != null ? parentActivity.getActivityBehavior() : null);
					if (parentActivityBehavior instanceof CompositeActivityBehavior) {
						if(parentActivityBehavior instanceof SequentialMultiInstanceBehavior){
							try {
								SequentialMultiInstanceBehavior compositeActivityBehavior = (SequentialMultiInstanceBehavior) parentActivity.getActivityBehavior();
								compositeActivityBehavior.execute(superExecution);
							} catch (Exception e) {
								throw new WorkflowRuntimeException(e);
							}
						}
						if(parentActivityBehavior instanceof SubProcessActivityBehavior){
							SubProcessActivityBehavior subProcessActivityBehavior = (SubProcessActivityBehavior) parentActivity.getActivityBehavior();
							if (subProcessActivityBehavior.isSequential()) {
								subProcessActivityBehavior.lastExecutionEnded(superExecution);
							}
						}
					}
				}
			}
		}
		/**
		 * 排除子流程多实例情况
		 */
		if (parentScope instanceof IProcessDefinition && activityInstance.getSuperExecution() == null) {
			activityInstance.performOperation(PROCESS_END);
		}else{
			/**
			 * 子流程被callActivity调用的情况  zhailzh 2013.1.30 为callActivity改
			 */
			if(parentScope instanceof IProcessDefinition && activityInstance.getSuperExecution().getActivity().getActivityBehavior() instanceof CallActivityBehavior) {
				CallActivityBehavior activityBehavior = (CallActivityBehavior) activityInstance.getSuperExecution().getActivity().getActivityBehavior();
				try {
					activityBehavior.completed(activityInstance.getSuperExecution());
				} catch (Exception e) {
					throw new WorkflowRuntimeException("can not executor "+activityBehavior+"return to callActivity throw error ", e);
				}
			}
		}
		
	}
	protected boolean isExecutionAloneInParent(IActivityInstance execution) {
		ScopeImpl parentScope = (ScopeImpl) execution.getActivity().getParent();
		for (IActivityInstance other : (List<IActivityInstance>) execution.getParent().getExecutions()) {
			if (other != execution && parentScope.contains((ActivityImpl) other.getActivity())) {
				return false;
			}
		}
		return true;
	}
	@Override
	protected IScope getScope(IActivityInstance execution) {
		return execution.getActivity();
	}
}
