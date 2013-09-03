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
package uap.workflow.engine.entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nc.bs.framework.common.NCLocator;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.engine.bpmn.parser.BpmnParse;
import uap.workflow.engine.bpmn.parser.SignalEventDefinition;
import uap.workflow.engine.bridge.TaskInstanceBridge;
import uap.workflow.engine.cfg.ProcessEngineConfigurationImpl;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.ActivityInstanceStatus;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IProcessElement;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.core.IScope;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.ITransition;
import uap.workflow.engine.delegate.VariableScope;
import uap.workflow.engine.event.CompensationEventHandler;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.handler.ActivityInstanceEndHandler;
import uap.workflow.engine.impl.WorkflowInstanceQry;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.itf.IWorkflowInstanceQry;
import uap.workflow.engine.itf.IEventSubscriptionQry;
import uap.workflow.engine.itf.ITaskInstanceQry;
import uap.workflow.engine.jobexecutor.AsyncContinuationJobHandler;
import uap.workflow.engine.jobexecutor.Job;
import uap.workflow.engine.jobexecutor.TimerDeclarationImpl;
import uap.workflow.engine.pvm.behavior.SignallableActivityBehavior;
import uap.workflow.engine.pvm.process.TransitionImpl;
import uap.workflow.engine.pvm.runtime.AtomicOperation;
import uap.workflow.engine.pvm.runtime.OutgoingExecution;
import uap.workflow.engine.query.JobQueryImpl;
import uap.workflow.engine.query.TaskQueryImpl;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.utils.ActivityInstanceUtil;
import uap.workflow.engine.utils.DateUtil;
import uap.workflow.engine.utils.EventSubscriptionUtil;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.utils.ProcessInstanceUtil;
import uap.workflow.engine.utils.TaskUtil;
import uap.workflow.engine.utils.VariableInstanceUtil;
import uap.workflow.engine.variable.VariableDeclaration;
import uap.workflow.engine.vos.ActivityInstanceVO;
import uap.workflow.engine.vos.EventSubscriptionVO;
import uap.workflow.engine.vos.TaskInstanceVO;
/**
 * @author Tom Baeyens
 */
public class ActivityInstanceEntity extends VariableScopeImpl implements IActivityInstance {
	private static final long serialVersionUID = 1L;
	protected boolean isActive = true;
	protected boolean isScope = true;
	protected boolean isConcurrent = false;
	protected boolean isEnded = false;
	protected boolean isEventScope = false;
	protected boolean isOperating = false;
	protected String eventName;
	protected boolean deleteRoot;
	protected String deleteReason;
	protected IProcessElement eventSource;
	protected List<CompensateEventSubscriptionEntity> compensateEventSubscriptions;
	protected AtomicOperation nextOperation;
	protected String pk_actins = null;
	protected IActivity activity;
	protected IProcessInstance processInstance;
	protected List<IActivityInstance> executions;
	protected IActivityInstance parent;
	protected List<ITask> tasks;
	protected IActivityInstance superExecution;
	protected IProcessInstance subProcessInstance;
	protected ITransition transition = null;
	protected ITransition transitionBeingTaken = null;
	protected ActivityInstanceStatus status;
	protected boolean isRejected;
	private boolean isExe;
	private boolean isPass;
	private IBusinessKey formInfoCtx = null;
	private Date begindate;
	private ActivityInstanceVO actInsVo;
	public ActivityInstanceEntity() {
		this.begindate = new Date();
	}
	public ActivityInstanceEntity(IActivity activityImpl) {
		this.activity = activityImpl;
	}
	// 创建活动实列
	public ActivityInstanceEntity createExecution(IActivity activity) {
		ActivityInstanceEntity createdExecution = newExecution();
		createdExecution.setRejected(false);
		this.getExecutions().add(createdExecution);
		createdExecution.setParent(this);
		createdExecution.setProcessInstance(getProcessInstance());
		createdExecution.setActivity(activity);
		return createdExecution;
	}
	// 销毁活动实列
	public void destroy() {
		removeVariablesLocal();
		removeVariables();
		setScope(false);
	}
	// 活动实列的销毁
	public void remove() {
		if (parent != null) {
			parent.getExecutions().remove(this);
			return;
		}
		//ensureVariableInstancesInitialized();
		//removeVariablesLocal();
		CommandContext commandContext = Context.getCommandContext();
		List<TaskInstanceVO> tasks = TaskUtil.getTaskByActivityInstancePk(pk_actins);
		if (tasks != null) {
			for (TaskInstanceVO task : tasks) {
				ITask taskInstance = new TaskInstanceBridge().convertM2T(task);
				commandContext.getTaskManager().deleteTask(taskInstance, TaskEntity.DELETE_REASON_DELETED, false);
			}
		
		destoryJob(commandContext);	
	
		destoryEventSubscrip(commandContext);
		desctoryExecution();
		commandContext.getDbSqlSession().delete(ActivityInstanceEntity.class, pk_actins);
		}
	}
	// 发出信号量，离开当前活动节点
	public void signal(String signalName, Object signalData) {
		SignallableActivityBehavior activityBehavior = (SignallableActivityBehavior) this.getActivity().getActivityBehavior();
		try {
  			activityBehavior.signal(this, signalName, signalData);
		} catch (Exception e) {
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	// 执行实例启动操作
	public void startMainProcess() {
		performOperation(AtomicOperation.PROCESS_START);
	}
	// 准备转移，执行listenrend
	public void take(ITransition transition) {
		setActivity(transition.getSource());
		setTransition(transition);
		performOperation(AtomicOperation.TRANSITION_NOTIFY_LISTENER_END);
	}
	// 活动结束
	public void end() {
		isActive = false;
		isEnded = true;
		performOperation(AtomicOperation.ACTIVITY_END);
	}
	// 活动启动
	public void startSubProcess(IActivity activity) {
		this.setParent(null);
		setActivity(activity);
		performOperation(AtomicOperation.ACTIVITY_START);
	}
	// 执行活动的原子操作
	public void performOperation(AtomicOperation executionOperation) {
		if (executionOperation.isAsync(this)) {
			MessageEntity message = new MessageEntity();
			message.setExecution(this);
			message.setExclusive(getActivity().isExclusive());
			message.setJobHandlerType(AsyncContinuationJobHandler.TYPE);
			Context.getCommandContext().getJobManager().send(message);
		} else {
			Context.getCommandContext().performOperation(executionOperation, this);
		}
	}
	// 找到没有活动的并行executions()
	public List<IActivityInstance> findInactiveConcurrentExecutions(IActivity activity) {
		List<IActivityInstance> inactiveConcurrentExecutionsInActivity = new ArrayList<IActivityInstance>();
		List<IActivityInstance> otherConcurrentExecutions = new ArrayList<IActivityInstance>();
		if (isConcurrent()) {
			List<? extends IActivityInstance> concurrentExecutions = getParent().getAllChildExecutions();
			for (IActivityInstance concurrentExecution : concurrentExecutions) {
				if (concurrentExecution.getActivity() == activity) {
					if (!concurrentExecution.isActive()) {
						inactiveConcurrentExecutionsInActivity.add(concurrentExecution);
					}
				} else {
					otherConcurrentExecutions.add(concurrentExecution);
				}
			}
		} else {
			if (isActive()) {
				otherConcurrentExecutions.add(this);
			} else {
				inactiveConcurrentExecutionsInActivity.add(this);
			}
		}
		return inactiveConcurrentExecutionsInActivity;
	}
	// 递归获取所有的子executions
	public List<IActivityInstance> getAllChildExecutions() {
		List<IActivityInstance> childExecutions = new ArrayList<IActivityInstance>();
		for (IActivityInstance childExecution : getExecutions()) {
			childExecutions.add(childExecution);
			childExecutions.addAll(childExecution.getAllChildExecutions());
		}
		return childExecutions;
	}
	public void takeAll(List<ITransition> outgoingTransitions, List<IActivityInstance> joinedExecutions) {
		outgoingTransitions = new ArrayList<ITransition>(outgoingTransitions);
		joinedExecutions = new ArrayList<IActivityInstance>(joinedExecutions);
		IActivityInstance concurrentRoot = ((isConcurrent && !isScope) ? getParent() : this);
		if(concurrentRoot==null)    concurrentRoot=this;
		List<IActivityInstance> concurrentActiveExecutions = new ArrayList<IActivityInstance>();
		List<IActivityInstance> concurrentInActiveExecutions = new ArrayList<IActivityInstance>();
		for (IActivityInstance execution : concurrentRoot.getExecutions()) {
			if (execution.isActive()) {
				concurrentActiveExecutions.add(execution);
			} else {
				concurrentInActiveExecutions.add(execution);
			}
		}
		if ((outgoingTransitions.size() == 1) && (concurrentActiveExecutions.isEmpty()) && allExecutionsInSameActivity(concurrentInActiveExecutions)) {
			List<IActivityInstance> recyclableExecutionImpls = joinedExecutions;
			joinedExecutions.remove(concurrentRoot);
			for (IActivityInstance prunedExecution : recyclableExecutionImpls) {
				if (!prunedExecution.isEnded()) {
					prunedExecution.remove();
				}
			}
			concurrentRoot.setActive(true);
			concurrentRoot.setActivity(activity);
			concurrentRoot.setConcurrent(false);
			concurrentRoot.take(outgoingTransitions.get(0));
		} else {
			List<OutgoingExecution> outgoingExecutions = new ArrayList<OutgoingExecution>();
			joinedExecutions.remove(concurrentRoot);
			// first create the concurrent executions
			while (!outgoingTransitions.isEmpty()) {
				ITransition outgoingTransition = outgoingTransitions.remove(0);
				IActivityInstance outgoingExecution = concurrentRoot;
				// if (joinedExecutions.isEmpty()) {
				// outgoingExecution =
				// concurrentRoot.createExecution(outgoingTransition.getDestination());
				// } else {
				// outgoingExecution = (ExecutionEntity)
				// joinedExecutions.remove(0);
				// }
				outgoingExecution.setActive(true);
				outgoingExecution.setScope(false);
				outgoingExecution.setConcurrent(true);
				outgoingExecution.setTransitionBeingTaken((TransitionImpl) outgoingTransition);
				outgoingExecutions.add(new OutgoingExecution(outgoingExecution, outgoingTransition, true));
			}
			// prune the executions that are not recycled
			for (IActivityInstance prunedExecution : joinedExecutions) {
				prunedExecution.end();
			}
			// then launch all the concurrent executions
			for (OutgoingExecution outgoingExecution : outgoingExecutions) {
				outgoingExecution.take();
			}
		}
	}
	protected boolean allExecutionsInSameActivity(List<IActivityInstance> executions) {
		if (executions.size() > 1) {
			String activityId = executions.get(0).getActivity().getId();
			for (IActivityInstance execution : executions) {
				String otherActivityId = execution.getActivity().getId();
				if (!execution.isEnded()) {
					if ((activityId == null && otherActivityId != null) || (activityId != null && otherActivityId == null)
							|| (activityId != null && otherActivityId != null && !otherActivityId.equals(activityId))) {
						return false;
					}
				}
			}
		}
		return true;
	}
	@Override
	protected void initializeActivityInstanceId(HistoricVariableUpdateEntity historicVariableUpdate) {
		int historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
		if (historyLevel >= ProcessEngineConfigurationImpl.HISTORYLEVEL_FULL) {
			HistoricActivityInstanceEntity historicActivityInstance = ActivityInstanceEndHandler.findActivityInstance(this);
			if (historicActivityInstance != null) {
				historicVariableUpdate.setActivityInstanceId(historicActivityInstance.getId());
			}
		}
	}
	/*
	public List<CompensateEventSubscriptionEntity> getCompensateEventSubscriptions(String activityId) {
		ensureCompensateEventSubscriptionsInitialized();
		ArrayList<CompensateEventSubscriptionEntity> result = new ArrayList<CompensateEventSubscriptionEntity>();
		for (CompensateEventSubscriptionEntity eventSubscriptionEntity : compensateEventSubscriptions) {
			if (eventSubscriptionEntity.getActivityId().equals(activityId)) {
				result.add(eventSubscriptionEntity);
			}
		}
		return result;
	}*/
	
	public List<CompensateEventSubscriptionEntity> getCompensateEventSubscriptions() {
		ensureCompensateEventSubscriptionsInitialized();
		return new ArrayList<CompensateEventSubscriptionEntity>(compensateEventSubscriptions);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void ensureCompensateEventSubscriptionsInitialized() {
		if (compensateEventSubscriptions == null) {
			//compensateEventSubscriptions = EventSubscriptionUtil.getCompensateEventSubscriptionByExecutionID(pk_actins);
			/*modify begin*/
			compensateEventSubscriptions = EventSubscriptionUtil.getEventSubscriptionByProcessInstance(this.getProcessInstance().getProInsPk());
			/*modify end*/
		}
	}
	// modify begine 
	public List<CompensateEventSubscriptionEntity> getCompensateEventSubscriptions(String activityId) {
		ensureCompensateEventSubscriptionsInitialized();
		ArrayList<CompensateEventSubscriptionEntity> result = new ArrayList<CompensateEventSubscriptionEntity>();
		for (CompensateEventSubscriptionEntity eventSubscriptionEntity : compensateEventSubscriptions) {
			if (eventSubscriptionEntity.getActivityId().equals(activityId)) {
				result.add(eventSubscriptionEntity);
			}
		}
		return result;
	}
	//modify end 
	
	// 增加补偿订阅
	public void addCompensateEventSubscription(CompensateEventSubscriptionEntity eventSubscriptionEntity) {
		ensureCompensateEventSubscriptionsInitialized();
		compensateEventSubscriptions.add(eventSubscriptionEntity);
	}
	// 移出补偿订阅
	public void removeCompensateEventSubscription(CompensateEventSubscriptionEntity eventSubscriptionEntity) {
		ensureCompensateEventSubscriptionsInitialized();
		compensateEventSubscriptions.remove(eventSubscriptionEntity);
	}
	// 流程定义
	public IProcessDefinition getProcessDefinition() {
		return this.getProcessInstance().getProcessDefinition();
	}
	// 流程实列
	public IProcessInstance getProcessInstance() {
		if (processInstance == null && actInsVo != null) {
			processInstance = ProcessInstanceUtil.getProcessInstance(actInsVo.getPk_proins());
		}
		return processInstance;
	}
	public void setProcessInstance(IProcessInstance processInstance) {
		this.processInstance = processInstance;
	}
	// 父活动
	public IActivity getParentActivity() {
		if (parent != null) {
			return parent.getActivity();
		} else {
			return null;
		}
	}
	// 当前活动
	public IActivity getActivity() {
		if (activity == null && actInsVo != null) {
			activity = this.getProcessDefinition().findActivity(actInsVo.getPort_id());
		}
		return activity;
	}
	public void setActivity(IActivity activity) {
		this.activity = activity;
	}
	// 子流程的处理
	public IActivityInstance getSuperExecution() {
		if (superExecution == null && actInsVo != null) {
			if (actInsVo.getPk_super() != null) {
				superExecution = ActivityInstanceUtil.getActInsByActInsPk(actInsVo.getPk_super());
			}
		}
		return superExecution;
	}
	public void setSuperExecution(IActivityInstance superExecution) {
		this.superExecution = superExecution;
		if (superExecution != null) {
			superExecution.setSubProcessInstance(null);
		}
	}
	public IProcessInstance getSubProcessInstance() {
		if (subProcessInstance == null) {
			subProcessInstance = Context.getCommandContext().getExecutionManager().findSubProcessInstanceBySuperExecutionId(pk_actins);
		}
		return subProcessInstance;
	}
	public void setSubProcessInstance(IProcessInstance subProcessInstance) {
		this.subProcessInstance = (ProcessInstanceEntity) subProcessInstance;
	}
	// 父活动实列
	public IActivityInstance getParent() {
		if (actInsVo != null && parent == null) {
			if (actInsVo.getPk_parent() == null) {
				return null;
			}
			parent = ActivityInstanceUtil.getActInsByActInsPk(actInsVo.getPk_parent());
		}
		return parent;
	}
	public void setParent(IActivityInstance parent) {
		this.parent = parent;
		if (parent != null && parent.getSuperExecution() != null) {
			this.setSuperExecution(parent.getSuperExecution());
		}
	}
	// 子活动实列处理
	public List<IActivityInstance> getExecutions() {
		if (executions == null) {
			this.executions = ActivityInstanceUtil.getSubActInsByActInsPk(this.getActInsPk());
		}
		return executions;
	}
	public void setExecutions(List<IActivityInstance> executions) {
		this.executions = executions;
	}
	public IActivityInstance findExecution(String activityId) {
		if ((getActivity() != null) && (getActivity().getId().equals(activityId))) {
			return this;
		}
		for (IActivityInstance nestedExecution : getExecutions()) {
			IActivityInstance result = nestedExecution.findExecution(activityId);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	// 是否是scope
	public boolean isScope() {
		return isScope;
	}
	public void setScope(boolean isScope) {
		this.isScope = isScope;
	}
	// 是否是eventscope
	public boolean isEventScope() {
		return isEventScope;
	}
	public void setEventScope(boolean isEventScope) {
		this.isEventScope = isEventScope;
	}
	// 是否是活动
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean isActive(String activityId) {
		return findExecution(activityId) != null;
	}
	// 是否并发
	public boolean isConcurrent() {
		return isConcurrent;
	}
	public void setConcurrent(boolean isConcurrent) {
		this.isConcurrent = isConcurrent;
	}
	// 事件触发的临时存储
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public IProcessElement getEventSource() {
		return eventSource;
	}
	public void setEventSource(IProcessElement eventSource) {
		this.eventSource = eventSource;
	}
	// 转移图元
	public ITransition getTransition() {
		return transition;
	}
	public void setTransition(ITransition object) {
		this.transition = object;
	}
	public ITransition getTransitionBeingTaken() {
		return transitionBeingTaken;
	}
	public void setTransitionBeingTaken(ITransition transitionBeingTaken) {
		this.transitionBeingTaken = transitionBeingTaken;
	}
	// 活动世列下面的状态信息
	public List<ITask> getTasks() {
		if (tasks == null) {
			tasks = new ArrayList<ITask>();
		}
		if (tasks.size() == 0) {
			ITaskInstanceQry taskQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(ITaskInstanceQry.class);
			List<TaskInstanceVO> taskInsVos = taskQry.getTasksByActivitiPk(this.getActInsPk());
			if (taskInsVos == null || taskInsVos.size() == 0) {
				return tasks;
			}
			for (int i = 0; i < taskInsVos.size(); i++) {
				tasks.add(new TaskInstanceBridge().convertM2T((TaskInstanceVO) taskInsVos.get(i)));
			}
		}
		return tasks;
	}
	public void setTasks(List<ITask> tasks) {
		this.tasks = tasks;
	}
	// 活动状态
	public ActivityInstanceStatus getStatus() {
		if (actInsVo != null && status == null) {
			status = ActivityInstanceStatus.fromIntValue(actInsVo.getState_actins());
		}
		return status;
	}
	public void setStatus(ActivityInstanceStatus status) {
		this.status = status;
	}
	// 单剧信息
	public IBusinessKey getFromInfoCtx() {
		if (formInfoCtx == null) {
			if (WorkflowContext.getCurrentBpmnSession() != null) {
				formInfoCtx = WorkflowContext.getCurrentBpmnSession().getBusinessObject();
			}
		}
		return formInfoCtx;
	}
	public void setFormInfoCtx(IBusinessKey formInfoCtx) {
		this.formInfoCtx = formInfoCtx;
	}
	// 对应的VO信息
	public ActivityInstanceVO getActInsVo() {
		return actInsVo;
	}
	public void setActInsVo(ActivityInstanceVO actInsVo) {
		this.actInsVo = actInsVo;
	}
	// 创建日期
	public Date getBegindate() {
		if (begindate == null && actInsVo != null) {
			begindate = DateUtil.convert(actInsVo.getBegindate());
		}
		return begindate;
	}
	public void setBegindate(Date begindate) {
		this.begindate = begindate;
	}
	// 是否又退回产生
	public boolean isRejected() {
		return isRejected;
	}
	public void setRejected(boolean isRejected) {
		this.isRejected = isRejected;
	}
	// 是否执行
	public boolean isExe() {
		return isExe;
	}
	public void setExe(boolean isExe) {
		this.isExe = isExe;
	}
	// 是否通过
	public boolean isPass() {
		return isPass;
	}
	public void setPass(boolean isPass) {
		this.isPass = isPass;
	}
	// 主键
	public String getPk_actins() {
		return pk_actins;
	}
	public void setPk_actins(String pk_actins) {
		this.pk_actins = pk_actins;
	}
	public String getActInsPk() {
		return pk_actins;
	}
	private ActivityInstanceEntity newExecution() {
		ActivityInstanceEntity newExecution = new ActivityInstanceEntity();
		if (WorkflowContext.getCurrentBpmnSession() != null) {
			newExecution.setFormInfoCtx(WorkflowContext.getCurrentBpmnSession().getBusinessObject());
		}
		newExecution.setBegindate(new Date());
		newExecution.executions = new ArrayList<IActivityInstance>();
		return newExecution;
	}
	public void initialize() {
		IScope scope = getScope();
		initVariable(scope);
		initTimerDeclaration(scope);
		initSignalDefinition(scope);
	}
	// 初始化信号量
	@SuppressWarnings("unchecked")
	private void initSignalDefinition(IScope scope) {
		List<SignalEventDefinition> signalDefinitions = (List<SignalEventDefinition>) scope.getProperty(BpmnParse.PROPERTYNAME_SIGNAL_DEFINITION_NAME);
		if (signalDefinitions != null) {
			for (SignalEventDefinition signalDefinition : signalDefinitions) {
				SignalEventSubscriptionEntity signalEventSubscriptionEntity = new SignalEventSubscriptionEntity(this);
				signalEventSubscriptionEntity.setEventName(signalDefinition.getSignalName());
				if (signalDefinition.getActivityId() != null) {
					IActivity activity = getActivity().findActivity(signalDefinition.getActivityId());
					signalEventSubscriptionEntity.setActivity(activity);
				}
				Context.getCommandContext().getEventSubscriptionManager().insert(signalEventSubscriptionEntity);
			}
		}
	}
	// 初始化time
	@SuppressWarnings("unchecked")
	private void initTimerDeclaration(IScope scope) {
		List<TimerDeclarationImpl> timerDeclarations = (List<TimerDeclarationImpl>) scope.getProperty(BpmnParse.PROPERTYNAME_TIMER_DECLARATION);
		if (timerDeclarations != null) {
			for (TimerDeclarationImpl timerDeclaration : timerDeclarations) {
				TimerEntity timer = timerDeclaration.prepareTimerEntity(this);
				Context.getCommandContext().getJobManager().schedule(timer);
			}
		}
	}
	// 初始化Variable
	private void initVariable(IScope scope) {
		@SuppressWarnings("unchecked") List<VariableDeclaration> variableDeclarations = (List<VariableDeclaration>) scope.getProperty(BpmnParse.PROPERTYNAME_VARIABLE_DECLARATIONS);
		if (variableDeclarations != null) {
			for (VariableDeclaration variableDeclaration : variableDeclarations) {
				variableDeclaration.initialize(this, parent);
			}
		}
	}
	// 销毁job
	private void destoryJob(CommandContext commandContext) {
		List<Job> jobs = new JobQueryImpl(commandContext).executionId(pk_actins).list();
		for (Job job : jobs) {
			((JobEntity) job).delete();
		}
	}
	// 销毁execution
	private void desctoryExecution() {
		List<IActivityInstance> childExecutions = new ArrayList<IActivityInstance>(getExecutions());
		for (IActivityInstance childExecution : childExecutions) {
			if (childExecution.isEventScope()) {
				childExecution.destroy();
				childExecution.remove();
			}
		}
	}
	// 销毁事件订阅
	private void destoryEventSubscrip(CommandContext commandContext) {
		List<SignalEventSubscriptionEntity> eventSubscriptions = commandContext.getEventSubscriptionManager().findSignalEventSubscriptionsByExecution(pk_actins);
		for (EventSubscriptionEntity eventSubscription : eventSubscriptions) {
			eventSubscription.delete();
		}
		for (CompensateEventSubscriptionEntity compensateEventSubscription : getCompensateEventSubscriptions()) {
			removeCompensateEventSubscription(compensateEventSubscription);
			compensateEventSubscription.delete();
		}
	}
	// 扩展变量
	@Override
	protected void initializeVariableInstanceBackPointer(VariableInstanceEntity variableInstance) {
		variableInstance.setExecutionId(pk_actins);
	}
	@Override
	protected List<VariableInstanceEntity> loadVariableInstances() {
		return VariableInstanceUtil.getVarInsByActInsPk(this.getActInsPk());
	}
	public VariableScope getParentVariableScope() {
		return getParent();
	}
	public List<String> findActiveActivityIds() {
		List<String> activeActivityIds = new ArrayList<String>();
		collectActiveActivityIds(activeActivityIds);
		return activeActivityIds;
	}
	public void collectActiveActivityIds(List<String> activeActivityIds) {
		if (isActive && this.getActivity() != null) {
			activeActivityIds.add(activity.getId());
		}
		for (IActivityInstance execution : this.getExecutions()) {
			execution.collectActiveActivityIds(activeActivityIds);
		}
	}
	public boolean isEnded() {
		return isEnded;
	}
	public void deleteCascade(String deleteReason) {
		this.deleteReason = deleteReason;
		this.deleteRoot = true;
		performOperation(AtomicOperation.DELETE_CASCADE);
	}
	public boolean isDeleteRoot() {
		return deleteRoot;
	}
	public String getDeleteReason() {
		return deleteReason;
	}
	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}
	protected IScope getScope() {
		return getActivity();
	}

	public List<IActivityInstance> findIncomeCompletedExecutions(
			IProcessInstance processInstance, IActivity activity) {
		List<IActivityInstance> completedExecutions = new ArrayList<IActivityInstance>();
		List<ITransition> incomingTransitions = activity.getIncomingTransitions();
		for (ITransition transition : incomingTransitions) {
			IActivityInstance activitiExecution = ActivityInstanceUtil.getActivityInstanceVoByActivityID(
					processInstance.getProInsPk(), transition.getSource().getId());
			if (activitiExecution.getStatus().getIntValue() == ActivityInstanceStatus.Finished.getIntValue()) 
			{
				completedExecutions.add(activitiExecution);
			}
		}
		return completedExecutions;
	}
}
