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
package uap.workflow.engine.core;
import java.util.List;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.engine.delegate.VariableScope;
import uap.workflow.engine.entity.CompensateEventSubscriptionEntity;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.pvm.runtime.AtomicOperation;
/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public interface IActivityInstance extends VariableScope {
	/* Process instance/activity/transition retrieval */
	/**
	 * returns the current {@link IActivity} of the execution.
	 */
	ActivityInstanceStatus getStatus();
	String getActInsPk();
	IProcessInstance getProcessInstance();
	void setProcessInstance(IProcessInstance processInstance);
	IActivityInstance getSuperExecution();
	void setSuperExecution(IActivityInstance execution);
	List<ITask> getTasks();
	ITransition getTransition();
	void setEventName(String eventName);
	void setTransition(ITransition transition);
	void initialize();
	IActivity getActivity();
	void setEventSource(IProcessElement eventSource);
	void setSubProcessInstance(IProcessInstance subProcessInstance);
	void setEventScope(boolean isEventScope);
	IProcessDefinition getProcessDefinition();
	void performOperation(AtomicOperation executionOperation);
	void setParent(IActivityInstance parent);
	void addCompensateEventSubscription(CompensateEventSubscriptionEntity eventSubscriptionEntity);
	List<CompensateEventSubscriptionEntity> getCompensateEventSubscriptions(String activityId);
	List<CompensateEventSubscriptionEntity> getCompensateEventSubscriptions();
	void removeCompensateEventSubscription(CompensateEventSubscriptionEntity eventSubscriptionEntity);
	/**
	 * leaves the current activity by taking the given transition.
	 */
	void take(ITransition transition);
	IActivityInstance findExecution(String activityId);
	List<IActivityInstance> getAllChildExecutions();
	void signal(String signalName, Object signalData);
	/* Execution management */
	/**
	 * creates a new execution. This execution will be the parent of the newly
	 * created execution. properties processDefinition, processInstance and
	 * activity will be initialized.
	 */
	IActivityInstance createExecution(IActivity activity);
	void setTransitionBeingTaken(ITransition transitionBeingTaken);
	boolean isDeleteRoot();
	void asyn();
	/**
	 * returns the parent of this execution, or null if there no parent.
	 */
	IActivityInstance getParent();
	void collectActiveActivityIds(List<String> activeActivityIds);
	boolean isEventScope();
	void destroy();
	void remove();
	void setActivity(IActivity activity);
	/**
	 * returns the list of execution of which this execution the parent of.
	 */
	List<IActivityInstance> getExecutions();
	IProcessInstance getSubProcessInstance();
	void deleteCascade(String deleteReason);
	/**
	 * ends this execution.
	 */
	void end();
	/* State management */
	/**
	 * makes this execution active or inactive.
	 */
	void setActive(boolean isActive);
	/**
	 * returns whether this execution is currently active.
	 */
	boolean isActive();
	/**
	 * returns whether this execution has ended or not.
	 */
	boolean isEnded();
	/**
	 * changes the concurrent indicator on this execution.
	 */
	void setConcurrent(boolean isConcurrent);
	/**
	 * returns whether this execution is concurrent or not.
	 */
	boolean isConcurrent();
	/**
	 * Inactivates this execution. This is useful for example in a join: the
	 * execution still exists, but it is not longer active.
	 */
	/**
	 * Returns whether this execution is a scope.
	 */
	boolean isScope();
	/**
	 * Changes whether this execution is a scope or not
	 */
	void setScope(boolean isScope);
	/**
	 * Retrieves all executions which are concurrent and inactive at the given
	 * activity.
	 */
	List<IActivityInstance> findInactiveConcurrentExecutions(IActivity activity);
	/**
	 * Retrieves all executions which are concurrent and inactive at the given
	 * activity.
	 */
	List<IActivityInstance> findIncomeCompletedExecutions(IProcessInstance processInstance, IActivity activity);
	/**
	 * Takes the given outgoing transitions, and potentially reusing the given
	 * list of executions that were previously joined.
	 */
	void takeAll(List<ITransition> outgoingTransitions, List<IActivityInstance> joinedExecutions);
	/**
	 * Executes the {@link ActivityBehavior} associated with the given activity.
	 */
	void startSubProcess(IActivity activity);
	IBusinessKey getFromInfoCtx();
	void setStatus(ActivityInstanceStatus status);
	
	/**
	 *  是否执行
	 */
	boolean isExe();
	void setExe(boolean isExe);
	
	/** 是否通过
	 */
	boolean isPass();
	void setPass(boolean isPass);
}
