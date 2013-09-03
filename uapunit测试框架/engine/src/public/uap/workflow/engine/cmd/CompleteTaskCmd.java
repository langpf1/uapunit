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
package uap.workflow.engine.cmd;
import java.util.Map;

import nc.vo.pub.lang.UFBoolean;

import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.dftimpl.FlowResponse;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.utils.TaskUtil;
import uap.workflow.engine.vos.AssignmentVO;
/**
 * @author Joram Barrez
 */
public class CompleteTaskCmd implements Command<Void> {
	protected String taskId;
	protected Map<String, Object> variables;
	public CompleteTaskCmd(String taskId, Map<String, Object> variables) {
		this.taskId = taskId;
		this.variables = variables;
	}
	public Void execute(CommandContext commandContext) {
		if (taskId == null) {
			throw new WorkflowException("taskId is null");
		}
		ITask task = TaskUtil.getTaskByTaskPk(taskId);
		if (task == null) {
			throw new WorkflowException("Cannot find task with id " + taskId);
		}
		check(task);
		updateCtx(task);
		if (variables != null) {
			task.setVariables(variables);
		}
		if(task.getCreateType()!= TaskInstanceCreateType.Co_Sponsor){
			handlerAssignInfo(task);	
		}
		completeTask(task);
		return null;
	}
	private void updateCtx(ITask task) {
		UserTaskRunTimeCtx[] userTaskCtx = WorkflowContext.getCurrentBpmnSession().getUserTaskCtx();
		if (userTaskCtx != null && userTaskCtx.length == 1) {
			WorkflowContext.getCurrentBpmnSession().setCntUserTaskInfo(userTaskCtx[0]);
		}
		FlowResponse response = (FlowResponse) WorkflowContext.getCurrentBpmnSession().getResponse();
		response.setCurrentTask(task);
	}
	private void check(ITask taskIns) {
		if (taskIns.getStatus().equals(TaskInstanceStatus.BeforeAddSignSend)) {
			throw new WorkflowRuntimeException("前加签发送的任务不能执行");
		}
		if (taskIns.getStatus().equals(TaskInstanceStatus.BeforeAddSignUnderway)) {
			throw new WorkflowRuntimeException("前加签中的任务不能执行");
		}
	}
	private void handlerAssignInfo(ITask task) {
		FlowInfoCtx flowInfoCtx = WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx(); // 保存下一步骤的指派信息
		if (flowInfoCtx instanceof NextTaskInsCtx) {
			NextTaskInsCtx commitProInsCtx = (NextTaskInsCtx) flowInfoCtx;
			UserTaskRunTimeCtx[] nextInfos = commitProInsCtx.getNextInfo();
			if (nextInfos == null) {
				return;
			}
			int length = nextInfos.length;
			for (int i = 0; i < length; i++) {
				UserTaskRunTimeCtx tmpCtx = nextInfos[i];
				String proInsPk = task.getExecution().getProcessInstance().getProInsPk();
				String activityId = task.getExecution().getActivity().getId();
				// 如果往一下步推进的任务和当前任务是在同一个活动节点，说明当前指派信息已经出来过来
				if (tmpCtx.getActivityId().equalsIgnoreCase(activityId)) {
					updateAssignInfo(tmpCtx, proInsPk, activityId);
				} else {
					saveAssignInfo(task, tmpCtx);
				}
			}
		}
	}
	private void saveAssignInfo(ITask task, UserTaskRunTimeCtx tmpCtx) {
		AssignmentVO[] vos = new AssignmentVO[tmpCtx.getUserPks().length];
		//清理以前的存储的AssignmenInfo
		for (int j = 0; j < tmpCtx.getUserPks().length; j++) {
			clearBeforeBuilderAssginInfo(task.getExecution().getProcessInstance(), tmpCtx, vos, j);
		}
		for (int j = 0; j < tmpCtx.getUserPks().length; j++) {
			builderAssginInfo(task.getExecution().getProcessInstance(), tmpCtx, vos, j);
		}
		WfmServiceFacility.getAssignmentBill().insert(vos);
	}
	private void clearBeforeBuilderAssginInfo(IProcessInstance processInstance, UserTaskRunTimeCtx tmpCtx,
			AssignmentVO[] vos, int j) {
		IActivity activity = processInstance.getProcessDefinition().findActivity(tmpCtx.getActivityId());
		WfmServiceFacility.getAssignmentBill().delete(processInstance.getProInsPk(), tmpCtx.getActivityId(), tmpCtx.getUserPks()[j]);	
	}
	private void updateAssignInfo(UserTaskRunTimeCtx tmpCtx, String proInsPk, String activityId) {
		WfmServiceFacility.getAssignmentBill().delete(proInsPk, activityId, tmpCtx.getUserPks()[0]);
		AssignmentVO[] assignMentVos = WfmServiceFacility.getAssignmentQry().getAssignmentVos(proInsPk, activityId);
		if (assignMentVos == null || assignMentVos.length == 0) {
			return;
		}
		for (int k = 0; k < assignMentVos.length; k++) {
			Integer order = Integer.parseInt(assignMentVos[k].getOrder_str());
			assignMentVos[k].setOrder_str(String.valueOf(order - 1));
		}
		WfmServiceFacility.getAssignmentBill().update(assignMentVos);
	}
	private void builderAssginInfo(IProcessInstance processInstance, UserTaskRunTimeCtx tmpCtx, AssignmentVO[] vos, int j) {
		IActivity activity = processInstance.getProcessDefinition().findActivity(tmpCtx.getActivityId());
		boolean sequence = false;
		if(activity.isAfterSign()){
			sequence = activity.isSequence();
		}
		else{
			sequence = tmpCtx.isSequence();
		}
		AssignmentVO assignInfo = new AssignmentVO();
		assignInfo.setActivity_id(tmpCtx.getActivityId());
		assignInfo.setPk_proins(processInstance.getProInsPk());
		assignInfo.setSequence(UFBoolean.valueOf(sequence));
		assignInfo.setPk_user(tmpCtx.getUserPks()[j]);
		if (sequence) {
			assignInfo.setOrder_str(String.valueOf(j));
		} else {
			assignInfo.setOrder_str(String.valueOf(0));
		}
		assignInfo.setDr(0);
		vos[j] = assignInfo;
	}
	protected void completeTask(ITask task) {
		boolean isPass = true;
		String opinion = "";
		FlowInfoCtx flowInfoCtx = WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx(); // 保存下一步骤的指派信息
		if (flowInfoCtx instanceof NextTaskInsCtx) {
			isPass = ((NextTaskInsCtx)flowInfoCtx).isPass();
			opinion = ((NextTaskInsCtx)flowInfoCtx).getComment();
		}
		task.next(isPass, opinion);
	}
}
