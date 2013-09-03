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
import java.util.Date;
import java.util.List;
import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.app.taskhandling.TaskHandlingContext;
import uap.workflow.engine.actsgy.ActorSgyManager;
import uap.workflow.engine.context.CommitProInsCtx;
import uap.workflow.engine.context.RejectTaskInsCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.ITaskListener;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.dftimpl.FlowResponse;
import uap.workflow.engine.el.ExpressionManager;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.pvm.behavior.SignallableActivityBehavior;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.task.TaskDefinition;
import uap.workflow.engine.utils.TaskUtil;
import uap.workflow.engine.vos.AssignmentVO;
/**
 * activity implementation for the user task.
 * 
 * @author Joram Barrez
 */
public class UserTaskActivityBehavior extends TaskActivityBehavior implements SignallableActivityBehavior {
	protected TaskDefinition taskDefinition;
	protected ExpressionManager expressionManager;
	public UserTaskActivityBehavior(ExpressionManager expressionManager, TaskDefinition taskDefinition) {
		this.expressionManager = expressionManager;
		this.taskDefinition = taskDefinition;
	}
	public void execute(IActivityInstance execution) throws Exception {
		IBusinessKey formInfoCtx = null;
		ITask ptask = null;
		if (WorkflowContext.getCurrentBpmnSession() != null) {
			formInfoCtx = WorkflowContext.getCurrentBpmnSession().getBusinessObject();
			ptask = WorkflowContext.getCurrentBpmnSession().getTask();
		} else {
			ActivityInstanceEntity parent = (ActivityInstanceEntity) execution.getParent();
			List<ITask> tasks = parent.getTasks();
			if (tasks != null && tasks.size() != 0)
				ptask = tasks.get(0);
		}
		IActivity activit = execution.getActivity();
		String[] userPks = ActorSgyManager.getInstance().getRuntimeActors(formInfoCtx, activit, ptask);
		deleteAssginInfo(execution, userPks);
		setCountAll(execution, userPks);
		if (userPks == null || userPks.length == 0) {
			throw new WorkflowRuntimeException("该活动节点没有有效的执行人");
		}
		createTask(execution, userPks);
	}

	private void createTask(IActivityInstance execution, String[] userPks) {
		TaskInstanceCreateType taskInstanceCreateType = TaskInstanceCreateType.Normal;
		FlowInfoCtx flowInfoCtx = WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx();
		if (flowInfoCtx instanceof RejectTaskInsCtx) {
			taskInstanceCreateType = TaskInstanceCreateType.Reject;
		}
		
		if (taskDefinition.isDeliver()) {
			taskInstanceCreateType = TaskInstanceCreateType.Deliver;
			for (int i = 0; i < userPks.length; i++) {
				TaskEntity task = TaskEntity.newTask(execution, taskInstanceCreateType);
				task.setTaskDefinition(taskDefinition);
				task.setOwner(userPks[i]);
				task.setOpenUIStyle(taskDefinition.getOpenUIStyle());
				task.setOpenURI(taskDefinition.getOpenURI());
				FlowResponse response = (FlowResponse) WorkflowContext.getCurrentBpmnSession().getResponse();
				if (response != null) {
					response.addNewTask(task);
				}
				String pTaskPk = WorkflowContext.getCurrentBpmnSession().getTaskPk();
				task.setParentTask(TaskUtil.getTaskByTaskPk(pTaskPk));
				IBusinessKey formCtx = WorkflowContext.getCurrentBpmnSession().getBusinessObject();
				if (formCtx == null) {
					IProcessInstance processInstance = execution.getProcessInstance();
					formCtx = (IBusinessKey) processInstance.getVariable(TaskEntity.APP_FORMINFO);
				}
				task.setFormInfoCtx(formCtx);
				this.handlerMsgSender(task);
				task.complete(true, "消息");
				task.signal();
			}
		}else if(taskDefinition.isUndertake()){
			List<String> CollaborationAndUserPks = new ArrayList<String>();
			for (int i = 0; i < userPks.length; i++) {
				CollaborationAndUserPks.add(userPks[i]) ;
			}
			for (int i = 0; i < taskDefinition.getCollaborationParticipants().size(); i++) {
				CollaborationAndUserPks.add(taskDefinition.getCollaborationParticipants().get(i).getParticipantID()) ;
			}
			for (int i = 0; i < CollaborationAndUserPks.size(); i++) {
				if(i<userPks.length){
					taskInstanceCreateType = TaskInstanceCreateType.Normal;
				}else{
					taskInstanceCreateType = TaskInstanceCreateType.Co_Sponsor;	
				}
				TaskEntity task = TaskEntity.newTask(execution, taskInstanceCreateType);
				task.setTaskDefinition(taskDefinition);
				task.setOwner(CollaborationAndUserPks.get(i)); 
				if (flowInfoCtx instanceof CommitProInsCtx) {
					CommitProInsCtx commitProIns = (CommitProInsCtx) WorkflowContext.getCurrentBpmnSession()
							.getFlowInfoCtx();
					if (commitProIns.isMakeBill()) {
						task.setCreateType(TaskInstanceCreateType.Makebill);
					}
				}
				FlowResponse response = (FlowResponse) WorkflowContext.getCurrentBpmnSession().getResponse();
				if (response != null) {
					response.addNewTask(task);
				}
				String pTaskPk = WorkflowContext.getCurrentBpmnSession().getTaskPk();
				task.setParentTask(TaskUtil.getTaskByTaskPk(pTaskPk));
				if (taskDefinition.getNameExpression() != null) {
					String name = (String) taskDefinition.getNameExpression().getValue(execution);
					task.setName(name);
				}
				if (taskDefinition.getDescriptionExpression() != null) {
					String description = (String) taskDefinition.getDescriptionExpression().getValue(execution);
					task.setDescription(description);
				}
				if (taskDefinition.getDueDateExpression() != null) {
					Object dueDate = taskDefinition.getDueDateExpression().getValue(execution);
					if (dueDate != null) {
						if (!(dueDate instanceof Date)) {
							throw new WorkflowException("Due date expression does not resolve to a Date: "
									+ taskDefinition.getDueDateExpression().getExpressionText());
						}
						task.setDueDate((Date) dueDate);
					}
				}
				if (taskDefinition.getPriorityExpression() != null) {
					final Object priority = taskDefinition.getPriorityExpression().getValue(execution);
					if (priority != null) {
						if (priority instanceof String) {
							try {
								task.setPriority(Integer.valueOf((String) priority));
							} catch (NumberFormatException e) {
								throw new WorkflowException("Priority does not resolve to a number: " + priority, e);
							}
						} else if (priority instanceof Number) {
							task.setPriority(((Number) priority).intValue());
						} else {
							throw new WorkflowException("Priority expression does not resolve to a number: "
									+ taskDefinition.getPriorityExpression().getExpressionText());
						}
					}
				}
				task.setOpenUIStyle(taskDefinition.getOpenUIStyle());
				task.setOpenURI(taskDefinition.getOpenURI());
				IBusinessKey formCtx = WorkflowContext.getCurrentBpmnSession().getBusinessObject();
				if (formCtx == null) {
					IProcessInstance processInstance = execution.getProcessInstance();
					formCtx = (IBusinessKey) processInstance.getVariable(TaskEntity.APP_FORMINFO);
				}
				task.setFormInfoCtx(formCtx);
				//task的pk_group从流程定义上面的集团id
				if(task.getPk_group() == null && execution.getActivity().getProcessDefinition().getPk_group() != null){
					task.setPk_group(execution.getActivity().getProcessDefinition().getPk_group());	
				}
				((TaskEntity) task).asyn();
				this.handlerMsgSender(task);
				task.fireEvent(ITaskListener.EVENTNAME_CREATE);
			}
		}else {
			for (int i = 0; i < userPks.length; i++) {
				TaskEntity task = TaskEntity.newTask(execution, taskInstanceCreateType);
				task.setTaskDefinition(taskDefinition);
				task.setOwner(userPks[i]);
				if (flowInfoCtx instanceof CommitProInsCtx) {
					CommitProInsCtx commitProIns = (CommitProInsCtx) WorkflowContext.getCurrentBpmnSession()
							.getFlowInfoCtx();
					if (commitProIns.isMakeBill()) {
						task.setCreateType(TaskInstanceCreateType.Makebill);
					}
				}
				FlowResponse response = (FlowResponse) WorkflowContext.getCurrentBpmnSession().getResponse();
				if (response != null) {
					response.addNewTask(task);//把任务信息返回到response中，以便带回
				}
				String pTaskPk = WorkflowContext.getCurrentBpmnSession().getTaskPk();
				task.setParentTask(TaskUtil.getTaskByTaskPk(pTaskPk));
				if (taskDefinition.getNameExpression() != null) {
					String name = (String) taskDefinition.getNameExpression().getValue(execution);
					task.setName(name);
				}
				if (taskDefinition.getDescriptionExpression() != null) {
					String description = (String) taskDefinition.getDescriptionExpression().getValue(execution);
					task.setDescription(description);
				}
				if (taskDefinition.getDueDateExpression() != null) {
					Object dueDate = taskDefinition.getDueDateExpression().getValue(execution);
					if (dueDate != null) {
						if (!(dueDate instanceof Date)) {
							throw new WorkflowException("Due date expression does not resolve to a Date: "
									+ taskDefinition.getDueDateExpression().getExpressionText());
						}
						task.setDueDate((Date) dueDate);
					}
				}
				if (taskDefinition.getPriorityExpression() != null) {
					final Object priority = taskDefinition.getPriorityExpression().getValue(execution);
					if (priority != null) {
						if (priority instanceof String) {
							try {
								task.setPriority(Integer.valueOf((String) priority));
							} catch (NumberFormatException e) {
								throw new WorkflowException("Priority does not resolve to a number: " + priority, e);
							}
						} else if (priority instanceof Number) {
							task.setPriority(((Number) priority).intValue());
						} else {
							throw new WorkflowException("Priority expression does not resolve to a number: "
									+ taskDefinition.getPriorityExpression().getExpressionText());
						}
					}
				}
				task.setOpenUIStyle(taskDefinition.getOpenUIStyle());
				task.setOpenURI(taskDefinition.getOpenURI());
				IBusinessKey formCtx = WorkflowContext.getCurrentBpmnSession().getBusinessObject();
				if (formCtx == null) {
					IProcessInstance processInstance = execution.getProcessInstance();
					formCtx = (IBusinessKey) processInstance.getVariable(TaskEntity.APP_FORMINFO);
				}
				task.setFormInfoCtx(formCtx);
				//task的pk_group从流程定义上面的集团id
				if(task.getPk_group() == null && execution.getActivity().getProcessDefinition().getPk_group() != null){
					task.setPk_group(execution.getActivity().getProcessDefinition().getPk_group());	
				}
				((TaskEntity) task).asyn();
				this.handlerMsgSender(task);
				task.fireEvent(ITaskListener.EVENTNAME_CREATE);
			}
		}
	}
	/**
	 * 处理消息发送
	 * 
	 * @param task
	 */
	public void handlerMsgSender(ITask task) {
		TaskHandlingContext taskHandlingContext = new TaskHandlingContext();
		taskHandlingContext.setActivity(task.getExecution().getActivity());
		ITask[] tasks = new ITask[1];
		tasks[0] = task;
		taskHandlingContext.setTasks(tasks);
		WfmServiceFacility.getTaskHandlingService().send(taskHandlingContext);
		//Map<String, Object> messageMap = TaskMessageGatherUtil.getMessageMap(task);
		//messageMap.put(TaskMessageSenderMgr.FrontControlType, WorkflowContext.getCurrentBpmnSession().getCntUserTaskInfo().getMsgType());
		//TaskMessageSenderMgr.sendTaskCreatedMessage(messageMap);
	}
	private void deleteAssginInfo(IActivityInstance execution, String[] userPks) {
		UserTaskRunTimeCtx userTaskCtx = WorkflowContext.getCurrentBpmnSession().getCntUserTaskInfo();
		if (userTaskCtx == null) {
			return;
		}
		if (userTaskCtx.isSequence()) {
			WfmServiceFacility.getAssignmentBill().delete(execution.getProcessInstance().getProInsPk(), execution.getActivity().getId(), userPks[0]);
		}
		AssignmentVO[] assignMentVos = WfmServiceFacility.getAssignmentQry().getAssignmentVos(execution.getProcessInstance().getProInsPk(), execution.getActivity().getId());
		if (assignMentVos == null || assignMentVos.length == 0) {
			return;
		}
		for (int i = 0; i < assignMentVos.length; i++) {
			Integer order = Integer.parseInt(assignMentVos[i].getOrder_str());
			assignMentVos[i].setOrder_str(String.valueOf(order - 1));
		}
		WfmServiceFacility.getAssignmentBill().update(assignMentVos);
	}
	private void setCountAll(IActivityInstance execution, String[] userPks) {
		UserTaskRunTimeCtx[] userTaskCtx = WorkflowContext.getCurrentBpmnSession().getUserTaskCtx();
		UserTaskRunTimeCtx tmpUserTask = null;
		for (int i = 0; i < userTaskCtx.length; i++) {
			tmpUserTask = userTaskCtx[i];
			if (tmpUserTask.getActivityId().equalsIgnoreCase(execution.getActivity().getId())) {
				break;
			}
		}
		int userCount = userPks.length;
		if(tmpUserTask!=null && tmpUserTask.getUserPks() != null)
		{
			userCount=tmpUserTask.getUserPks().length;
		}
		execution.setVariableLocal("nrOfInstances", String.valueOf(userCount));
	}
	public void signal(IActivityInstance execution, String signalName, Object signalData) throws Exception {
		leave(execution);
	}
	// getters and setters
	// //////////////////////////////////////////////////////
	public TaskDefinition getTaskDefinition() {
		return taskDefinition;
	}
	public ExpressionManager getExpressionManager() {
		return expressionManager;
	}
}
