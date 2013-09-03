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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import nc.vo.pub.lang.UFBoolean;


import uap.workflow.app.core.IBusinessKey;
import uap.workflow.app.participant.IParticipant;
import uap.workflow.engine.bpmn.behavior.UserTaskActivityBehavior;
import uap.workflow.engine.bridge.TaskInstanceBridge;
import uap.workflow.engine.cmpltsgy.CompleteSgyManager;
import uap.workflow.engine.constant.CommonConstant;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.context.Logic;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.ITaskListener;
import uap.workflow.engine.core.ITransition;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.core.TaskInstanceFinishMode;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.invocation.TaskListenerInvocation;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.task.TaskDefinition;
import uap.workflow.engine.utils.ActivityInstanceUtil;
import uap.workflow.engine.utils.DateUtil;
import uap.workflow.engine.utils.TaskUtil;
import uap.workflow.engine.utils.VariableInstanceUtil;
import uap.workflow.engine.vos.BeforeAddSignUserVO;
import uap.workflow.engine.vos.BeforeAddSignVO;
import uap.workflow.engine.vos.TaskInstanceVO;
/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class TaskEntity extends VariableScopeImpl implements ITask {
	public static final String DELETE_REASON_COMPLETED = "completed";
	public static final String DELETE_REASON_DELETED = "deleted";
	public static final String APP_FORMINFO = "APP_FORMINFO";
	protected int priority = ITask.PRIORITY_NORMAL;
	protected String taskPk;
	protected String name;
	protected String description;
	protected String owner;
	protected String creater;
	protected String agenter;
	protected String executer;
	/*
	 * 创建本任务的任务
	 */
	protected ITask parentTask;
	protected ITask superTask;
	protected List<ITask> subTasks;
	protected IActivityInstance execution;
	protected IProcessInstance processInstance;
	protected TaskDefinition taskDefinition;
	protected Date createTime;
	protected Date signTime;
	protected Date finishTime;
	protected Date dueDate;
	protected TaskInstanceStatus status;
	protected boolean isExe;
	protected boolean isPass;
	protected TaskInstanceFinishMode finishType = TaskInstanceFinishMode.Unfinished;
	private String finish;
	protected TaskInstanceCreateType createType;
	protected List<IdentityLinkEntity> taskIdentityLinkEntities = new ArrayList<IdentityLinkEntity>();
	private TaskInstanceVO taskInsVo;
	private String scratch;
	private String opinion;
	private String pk_group;
	private String pk_org;
	private String pk_form_ins;
	private String pk_form_ins_version;
	private String form_no;
	private String beforeaddsign_times;
	private String pk_bizobject;
	private String pk_biztrans;
	private String title;
	private String openUIStyle;
	private String openURI;

	/** 用户对象 */
	private String userobject;
	public TaskEntity() {}
	/** creates and initializes a new persistent task. */
	public static TaskEntity newTask(IActivityInstance execution,
			TaskInstanceCreateType createType) {
		TaskEntity task = new TaskEntity();
		task.setCreateTime(new Date());
		task.setCreateType(createType);
		task.setCreater(WorkflowContext.getCurrentBpmnSession().getCntUserPk());
		task.setExe(false);
		task.setPass(false);
		task.setStatus(TaskInstanceStatus.Wait);
		task.setExecution(execution);
		task.setProcessInstance(execution.getProcessInstance());
		//task.setOpinion(WorkflowContext.getCurrentBpmnSession().getOpinion());
		return task;
	}
	public void update(ITask task) {
		setOwner(task.getOwner());
		setName(task.getName());
		setDescription(task.getDescription());
		setCreateTime(task.getCreateTime());
		setDueDate(task.getDueDate());
	}
	public void complete(boolean isPass, String opinion) {
		//this.getExecution().getParent().getVariableLocal("result");
		this.setExe(true);
		this.getExecution().setExe(true);
		this.setPass(isPass);
		this.setOpinion(opinion);
		this.setFinishType(WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx().getFinishType());
		this.setExecuter(WorkflowContext.getCurrentBpmnSession().getCntUserPk());
		this.setFinishTime(new Date());
		this.setStatus(TaskInstanceStatus.Finished);
		this.asyn();
		if (!this.getCreateType().equals(TaskInstanceCreateType.Co_Sponsor)){
			/**针对协办，前加签，改派来说，任务的完成对“主任务（这三类任务都是由这个任务所产生的）”来说，任务实例数不在增加*/
			if (!this.getCreateType().equals(TaskInstanceCreateType.BeforeAddSign))
				if (!this.getCreateType().equals(TaskInstanceCreateType.AlterAssign)) {
					Integer complteCount = Integer.parseInt((String) this.getExecution().getVariableLocal(
							"nrOfCompletedInstances"));
					this.getExecution().setVariableLocal("nrOfCompletedInstances", String.valueOf(complteCount + 1));
					fireEvent(ITaskListener.EVENTNAME_COMPLETE_AFTER);
				}
			
		} else{
			fireEvent(ITaskListener.EVENTNAME_COMPLETE_AFTER);
			/*
			 * if
			 * (!this.getCreateType().equals(TaskInstanceCreateType.BeforeAddSign
			 * )) { Integer complteCount = Integer.parseInt((String)
			 * this.getExecution().getVariableLocal("nrOfCompletedInstances"));
			 * this.getExecution().setVariableLocal("nrOfCompletedInstances",
			 * String.valueOf(complteCount + 1)); }
			 * fireEvent(ITaskListener.EVENTNAME_COMPLETE_AFTER);
			 */
		}
	}
	public void signal() {
		getExecution().signal(null, null);
	}
	
	/*改派*/
	public void delegate(List<String> userIds) {
		this.complete(true,"改派");
		for(String userId : userIds)
		{
			TaskEntity newTask = this.clone();
			newTask = (TaskEntity) TaskUtil.initTask(newTask);
			newTask.setStatus(TaskInstanceStatus.Wait);
			newTask.setOwner(userId);
			newTask.setCreateType(TaskInstanceCreateType.AlterAssign);
			newTask.asyn();
		}
		fireEvent(ITaskListener.EVENTNAME_DELEGATE_AFTER);
	}
	
	/*指派*/
	public void assign(String userId) {
		this.complete(true,"指派");
		TaskEntity newTask = this.clone();
		newTask = (TaskEntity) TaskUtil.initTask(newTask);
		newTask.setStatus(TaskInstanceStatus.Wait);
		newTask.setOwner(userId);
		newTask.setCreateType(TaskInstanceCreateType.Assign);
		newTask.asyn();
		fireEvent(ITaskListener.EVENTNAME_DELEGATE_AFTER);
	}

	
	// 设置发出加签任务的状态
	public void updateSuperTaskStatus() {
		if (TaskUtil.isCmpletBeforeAddSignTask(this.getSuperTask().getTaskPk())) {
			superTask.setStatus(TaskInstanceStatus.BeforeAddSignComplete);
		} else {
			superTask.setStatus(TaskInstanceStatus.BeforeAddSignUnderway);
		}
		superTask.asyn();
	}
	// 设置使用标志
	private void updateBeforeAddSignInfo() {
		BeforeAddSignVO beforeAddSignVo = TaskUtil.getBeforeAddSignInfo(this.getSuperTask().getTaskPk());
		Logic logic = Logic.getLogicByString(beforeAddSignVo.getLogic());
		if (logic == Logic.Parallel) {
			return;
		}
		BeforeAddSignUserVO[] userVos = beforeAddSignVo.getBeforeAddSignUserVos();
		for (int i = 0; i < userVos.length; i++) {
			BeforeAddSignUserVO userVo = userVos[i];
			int order = Integer.parseInt(userVo.getOrder_str());
			if (order == 0) {
				userVo.setIsusered(new UFBoolean(true));
			} else {
				userVo.setOrder_str(String.valueOf(order - 1));
			}
			WfmServiceFacility.getBeforeAddSignBill().updateBeforeAddSignUserVo(userVo);
		}
	}
	public boolean isSignal() {
		String completeCount_str = (String) this.getExecution().getVariableLocal(CommonConstant.nrOfCompletedInstances);
		Integer completeCount_int = Integer.parseInt(completeCount_str);
		return CompleteSgyManager.getInstance().isComplete(this.getExecution(), completeCount_int);
	}
	public
	void next(boolean isPass, String opinion) {
		this.complete(isPass, opinion);
		if (TaskInstanceCreateType.BeforeAddSign.equals(this.getCreateType())) {
			this.updateSuperTaskStatus();
			this.updateBeforeAddSignInfo();
		}
		if(this.getTaskDefinition().isUndertake()){ 
			for(IParticipant participant : this.getTaskDefinition().getCollaborationParticipants())
			if(	participant.getParticipantID().equalsIgnoreCase(this.taskInsVo.getPk_owner())){
				return ;	
			}
		}
		if (this.isSignal()) {
			this.signal();
		} else {
			if (this.isCreateTask()) {
				createTask();
			}
		}
	}

	/**以下三种情况会调用
	 * 1 顺序后加签
	 * 2 顺序前加签
	 * 3 顺序指派
	 */
	private void createTask() {
		if (this.getSuperTask() != null) {
			TaskEntity superTask = (TaskEntity) this.getSuperTask();
			if (superTask.getStatus() == TaskInstanceStatus.BeforeAddSignComplete) {
				return;
			}
		}
		boolean afterSignSequence = false;
		IActivity activity = this.getExecution().getActivity();
		if (activity.isAfterSign() && activity.isSequence()) {
			afterSignSequence = true;
		}
		String[] userPk = null;
		if(afterSignSequence){
			userPk = WorkflowContext.getCurrentBpmnSession().getCntUserTaskInfo().getUserPks();
		}
		else{
			if(this.getSuperTask() != null)
			{
				BeforeAddSignVO beforeAddSignVo = TaskUtil.getBeforeAddSignInfo(this.getSuperTask().getTaskPk());
				Logic logic = Logic.getLogicByString(beforeAddSignVo.getLogic());
				if (logic == Logic.Parallel) {
					return;
				}
			}
			userPk = WorkflowContext.getCurrentBpmnSession().getCntUserTaskInfo().getUserPks();
		}
		if(userPk == null){
			return;
		}
		TaskEntity task = TaskEntity.newTask(execution,TaskInstanceCreateType.Normal);
		task.setOwner(userPk[0]);
		task.setStatus(TaskInstanceStatus.Wait);
		task.setParentTask(this);
		if (this.getSuperTask() != null) {
			task.setSuperTask(this.getSuperTask());
		}
		if (this.getCreateType().equals(TaskInstanceCreateType.BeforeAddSign)) {
			task.setCreateType(TaskInstanceCreateType.BeforeAddSign);
		}
		IProcessInstance processInstance = this.getExecution().getProcessInstance();
		IBusinessKey forminfo = (IBusinessKey) processInstance.getVariable(APP_FORMINFO);
		task.setFormInfoCtx(forminfo);
		task.asyn();
	}
	
	private boolean isCreateTask() {
		String cntActivityId = this.getExecution().getActivity().getId();
		UserTaskRunTimeCtx preUserRunTimeCtx = WorkflowContext.getCurrentBpmnSession().getCntUserTaskInfo();
		if (preUserRunTimeCtx != null) {
			String perpActivityId = preUserRunTimeCtx.getActivityId();
			if (cntActivityId.equalsIgnoreCase(perpActivityId)) {
				return true;
			} else {
				return false;
			}
		}
		IActivity activity = this.getExecution().getActivity();
		if (activity.isAfterSign() && activity.isSequence()){
			return true;
		}
		return false;
	}
	public TaskInstanceStatus getStatus() {
		if (taskInsVo != null && status == null) {
			status = TaskInstanceStatus.fromIntValue(taskInsVo.getState_task());
		}
		return status;
	}
	public void setStatus(TaskInstanceStatus status) {
		this.status = status;
	}
	public Date getSignTime() {
		if (taskInsVo != null && signTime == null) {
			signTime = DateUtil.convert(taskInsVo.getSigndate());
		}
		return signTime;
	}
	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}
	public Date getFinishTime() {
		if (taskInsVo != null && finishTime == null) {
			finishTime = DateUtil.convert(taskInsVo.getFinishdate());
		}
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	public List<ITask> getSubTasks() {
		if (subTasks == null) {
			subTasks = TaskUtil.getSubTasksByTaskPk(this.taskPk);
		}
		return subTasks;
	}
	public void setSubTasks(List<ITask> subTasks) {
		this.subTasks = subTasks;
	}
	public ITask getParentTask() {
		if (taskInsVo != null && parentTask == null) {
			parentTask = TaskUtil.getTaskByTaskPk(taskInsVo.getPk_parent());
		}
		return parentTask;
	}
	public void setParentTask(ITask parentTask) {
		this.parentTask = parentTask;
	}
	public TaskEntity(String taskPk) {
		this.taskPk = taskPk;
	}
	@Override
	protected VariableScopeImpl getParentVariableScope() {
		if (getExecution() != null) {
			return (VariableScopeImpl) execution;
		}
		return null;
	}
	@Override
	protected void initializeVariableInstanceBackPointer(VariableInstanceEntity variableInstance) {
		variableInstance.setTaskId(taskPk);
	}
	@Override
	protected List<VariableInstanceEntity> loadVariableInstances() {
		return VariableInstanceUtil.getVarInsByTaskPk(taskPk);
	}
	public IActivityInstance getExecution() {
		if (execution == null && taskInsVo != null) {
			String actInsPk = taskInsVo.getPk_activity_instance();
			IActivityInstance actIns = ActivityInstanceUtil.getActInsByActInsPk(actInsPk);
			this.execution = actIns;
		}
		return execution;
	}
	public void setExecution(IActivityInstance execution) {
		if (execution != null) {
			this.execution = (ActivityInstanceEntity) execution;
		} else {
			this.execution = null;
		}
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> getActivityInstanceVariables() {
		if (execution != null) {
			return execution.getVariables();
		}
		return Collections.EMPTY_MAP;
	}
	public void setExecutionVariables(Map<String, Object> parameters) {
		if (getExecution() != null) {
			execution.setVariables(parameters);
		}
	}
	public String toString() {
		return "Task[" + taskPk + "]";
	}
	// special setters
	// //////////////////////////////////////////////////////////
	public void setName(String taskName) {
		this.name = taskName;
	}
	/* plain setter for persistence */
	public void setNameWithoutCascade(String taskName) {
		this.name = taskName;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	/* plain setter for persistence */
	public void setDescriptionWithoutCascade(String description) {
		this.description = description;
	}
	public void setOwner(String owner) {
		if (owner == null && this.owner == null) {
			return;
		}
		if (owner != null && owner.equals(this.owner)) {
			return;
		}
		this.owner = owner;
	}
	/* plain setter for persistence */
	public void setOwnerWithoutCascade(String owner) {
		this.owner = owner;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public void setDueDateWithoutCascade(Date dueDate) {
		this.dueDate = dueDate;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public void setPriorityWithoutCascade(int priority) {
		this.priority = priority;
	}
	public void fireEvent(String taskEventName) {
		TaskDefinition taskDefinition = getTaskDefinition();
		if (taskDefinition != null) {
			List<ITaskListener> taskEventListeners = getTaskDefinition().getTaskListener(taskEventName);
			if (taskEventListeners == null)
				taskEventListeners = getTaskDefinition().getTaskListener("taskListener");
			else
				taskEventListeners.addAll(getTaskDefinition().getTaskListener("taskListener"));
			if (taskEventListeners != null) {
				for (ITaskListener taskListener : taskEventListeners) {
					try {
						Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new TaskListenerInvocation(taskListener, this));
					} catch (Exception e) {
						throw new WorkflowException("Exception while invoking TaskListener: " + e.getMessage(), e);
					}
				}
			}
		}
	}
	public void setTaskDefinition(TaskDefinition taskDefinition) {
		this.taskDefinition = taskDefinition;
	}
	public TaskDefinition getTaskDefinition() {
		return taskDefinition;
	}
	public String getTaskPk() {
		return taskPk;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public Date getCreateTime() {
		if (createTime == null && taskInsVo != null) {
			createTime = DateUtil.convert(taskInsVo.getBegindate());
		}
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public IProcessInstance getProcessInstance() {
		if (processInstance == null) {
			processInstance = this.getExecution().getProcessInstance();
		}
		return processInstance;
	}
	public void setProcessInstance(IProcessInstance processInstance) {
		this.processInstance = processInstance;
	}
	public void setExecution(ActivityInstanceEntity execution) {
		this.execution = execution;
	}
	public String getOwner() {
		if (owner == null && taskInsVo != null) {
			this.setOwner(taskInsVo.getPk_owner());
		}
		return owner;
	}
	public Map<String, VariableInstanceEntity> getVariableInstances() {
		ensureVariableInstancesInitialized();
		return variableInstances;
	}
	public TaskInstanceVO getTaskInsVo() {
		return taskInsVo;
	}
	public void setTaskInsVo(TaskInstanceVO taskInsVo) {
		this.taskInsVo = taskInsVo;
	}
	@Override
	public IProcessDefinition getProcessDefinition() {
		return this.getExecution().getProcessDefinition();
	}
	protected TaskEntity clone() {
		TaskInstanceVO taskVo = new TaskInstanceBridge().convertT2M(this);
		try {
			TaskEntity task = (TaskEntity) new TaskInstanceBridge().convertM2T(taskVo);
			return task;
		} catch (Exception e) {
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@Override
	public void delete() {
		Context.getCommandContext().getTaskManager().deleteTask(this.getTaskPk(), false);
	}
	@Override
	public void reject(String toActivityID) {
		// if (this.getStatus() == TaskInstanceStatus.State_End) {
		// throw new WorkflowRuntimeException("任务已经被执行");
		// }
		this.setExe(false);
		this.getExecution().setExe(false);
		this.setPass(false);
		this.setFinishTime(new Date());
		this.setStatus(TaskInstanceStatus.Finished);
		this.setFinishType(TaskInstanceFinishMode.Reject);
		this.setExecuter(WorkflowContext.getCurrentBpmnSession().getCntUserPk());
		this.setStatus(TaskInstanceStatus.Finished);
		this.asyn();
		fireEvent(ITaskListener.EVENTNAME_REJECT_AFTER);
		
		IActivity toActivity = this.getProcessDefinition().findActivity(toActivityID);
		IActivity srcActivity = this.getExecution().getActivity();
		ITransition virtualTransition = new uap.workflow.engine.pvm.process.TransitionImpl(srcActivity.getId() + "R", this.getExecution().getProcessDefinition());
		virtualTransition.setSource(srcActivity);
		virtualTransition.setDestination(toActivity);
		this.getExecution().take(virtualTransition);
	}
	
	@Override
	public void callBack() {
		this.setExe(false);
		this.getExecution().setExe(false);
		this.setPass(false);
		this.setExecuter(null);
		this.setStatus(TaskInstanceStatus.Wait);
		this.setFinishTime(null);
		this.asyn();
		fireEvent(ITaskListener.EVENTNAME_CALLBACK_AFTER);
	}
	
	@Override
	public void updateProgress(String finish, String opinion) {
		this.setPass(false);
		this.setFinish(finish);
		this.setOpinion(opinion);
		this.setStatus(TaskInstanceStatus.Run);
		this.asyn();
	}

	public void setTaskPk(String taskPk) {
		this.taskPk = taskPk;
	}
	public boolean isExe() {
		return isExe;
	}
	public void setExe(boolean isExe) {
		this.isExe = isExe;
	}
	public boolean isPass() {
		return isPass;
	}
	public void setPass(boolean isPass) {
		this.isPass = isPass;
	}
	public String getCreater() {
		if (creater == null && taskInsVo != null) {
			this.creater = taskInsVo.getPk_creater();
		}
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getAgenter() {
		if (agenter == null && taskInsVo != null) {
			agenter = taskInsVo.getPk_agenter();
		}
		return agenter;
	}
	public void setAgenter(String agenter) {
		this.agenter = agenter;
	}
	public String getExecuter() {
		if (executer == null && taskInsVo != null) {
			executer = taskInsVo.getPk_executer();
		}
		return executer;
	}
	public void setExecuter(String executer) {
		this.executer = executer;
	}
	public TaskInstanceFinishMode getFinishType() {
		if (taskInsVo != null && finishType == null) {
			finishType = TaskInstanceFinishMode.fromIntValue(taskInsVo.getFinish_type());
		}
		return finishType;
	}
	public void setFinishType(TaskInstanceFinishMode finishType) {
		this.finishType = finishType;
	}
	
	public String getFinish() {
		if (taskInsVo != null && finish == null) {
			finish = taskInsVo.getFinish();
		}
		return finish;
	}
	public void setFinish(String finish) {
		this.finish = finish;
	}
	
	public TaskInstanceCreateType getCreateType() {
		if (taskInsVo != null && createType == null) {
			createType = TaskInstanceCreateType.fromIntValue(taskInsVo.getCreate_type());
		}
		return createType;
	}
	public void setCreateType(TaskInstanceCreateType createType) {
		this.createType = createType;
	}
	public String getScratch() {
		if (scratch == null && taskInsVo != null) {
			scratch = taskInsVo.getScratch();
		}
		return scratch;
	}
	public void setScratch(String scratch) {
		this.scratch = scratch;
	}
	public String getOpinion() {
		if (opinion == null && taskInsVo != null) {
			opinion = taskInsVo.getOpinion();
		}
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	
	public String getPk_form_ins() {
		if (pk_form_ins == null && taskInsVo != null) {
			pk_form_ins = taskInsVo.getPk_form_ins();
		}
		return pk_form_ins;
	}
	public void setPk_form_ins(String pk_form_ins) {
		this.pk_form_ins = pk_form_ins;
	}
	public String getPk_form_ins_version() {
		if (pk_form_ins_version == null && taskInsVo != null) {
			pk_form_ins_version = taskInsVo.getPk_form_ins_version();
		}
		return pk_form_ins_version;
	}
	public void setPk_form_ins_version(String pk_form_ins_version) {
		this.pk_form_ins_version = pk_form_ins_version;
	}
	public String getForm_no() {
		if (form_no == null && taskInsVo != null) {
			form_no = taskInsVo.getForm_no();
		}
		return form_no;
	}
	public void setForm_no(String form_no) {
		this.form_no = form_no;
	}
	public ITask getSuperTask() {
		if (superTask == null && taskInsVo != null) {
			superTask = TaskUtil.getTaskByTaskPk(taskInsVo.getPk_super());
		}
		return superTask;
	}
	public void setSuperTask(ITask superTask) {
		this.superTask = superTask;
	}
	public String getBeforeaddsign_times() {
		if (beforeaddsign_times == null && taskInsVo != null) {
			beforeaddsign_times = taskInsVo.getBeforeaddsign_times();
		}
		return beforeaddsign_times;
	}
	public void setBeforeaddsign_times(String beforeaddsign_times) {
		this.beforeaddsign_times = beforeaddsign_times;
	}
	public String getPk_group() {
		if (pk_group == null && taskInsVo != null) {
			pk_group = taskInsVo.getPk_group();
		}
		return pk_group;
	}
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}
	public String getPk_org() {
		if (pk_org == null && taskInsVo != null) {
			pk_org = taskInsVo.getPk_org();
		}
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public String getPk_bizobject() {
		if (pk_bizobject == null && taskInsVo != null) {
			pk_bizobject = taskInsVo.getPk_bizobject();
		}
		return pk_bizobject;
	}
	public void setPk_bizobject(String pk_bizobject) {
		this.pk_bizobject = pk_bizobject;
	}
	public String getPk_biztrans() {
		if (pk_biztrans == null && taskInsVo != null) {
			pk_biztrans = taskInsVo.getPk_biztrans();
		}
		return pk_biztrans;
	}
	public void setPk_biztrans(String pk_biztrans) {
		this.pk_biztrans = pk_biztrans;
	}
	public String getTitle() {
		if (title == null && taskInsVo != null) {
			title = taskInsVo.getTitle();
		}
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserobject() {
		return userobject;
	}

	public void setUserobject(String userobject) {
		this.userobject = userobject;
	}
	
	public void setFormInfoCtx(IBusinessKey businessObject)
	{
		if (businessObject != null) {
			this.setPk_form_ins(businessObject.getBillId());
			this.setPk_form_ins_version(businessObject.getBillId());
			//this.setPk_form_ins_version(formCtx.getBillVersionPK());
			this.setForm_no(businessObject.getBillNo());
			//this.setPk_org(formCtx.getPkorg());
			//this.setPk_group(formCtx.getPk_group());
			this.setPk_bizobject(businessObject.getBillType());
			//this.setPk_biztrans(formCtx.getTranstype());
		}
	}
	@Override
	public String getOpenUIStyle() {
		return openUIStyle;
	}
	@Override
	public String getOpenURI() {
		return openURI;
	}
	@Override
	public void setOpenUIStyle(String openUIStyle) {
		this.openUIStyle = openUIStyle;
	}
	@Override
	public void setOpenURI(String openURI) {
		this.openURI = openURI;
	}
}
