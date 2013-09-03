package uap.workflow.engine.command;
import java.util.Date;


import nc.bs.framework.common.NCLocator;
import nc.vo.pub.lang.UFBoolean;

import uap.workflow.app.core.IBusinessKey;
import uap.workflow.engine.context.AddSignUserInfoCtx;
import uap.workflow.engine.context.CreateBeforeAddSignCtx;
import uap.workflow.engine.context.Logic;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IBeforeAddSignBill;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.utils.TaskUtil;
import uap.workflow.engine.vos.BeforeAddSignUserVO;
import uap.workflow.engine.vos.BeforeAddSignVO;
public class CreateBeforeAddSignCmd implements ICommand<Void> {
	@Override
	public Void execute() {
		this.createAddSignTasks();
		this.updateTask();
		return null;
	}
	private void createAddSignTasks() {
		TaskEntity task = (TaskEntity) WorkflowContext.getCurrentBpmnSession().getTask();
		CreateBeforeAddSignCtx flwInfoCtx = (CreateBeforeAddSignCtx) WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx();
		Logic logic = flwInfoCtx.getLogic();
		BeforeAddSignVO beforeAddSignVo = this.builderBeforeAddSignInfo();
		BeforeAddSignUserVO[] userVos = beforeAddSignVo.getBeforeAddSignUserVos();
		if (logic.equals(Logic.Parallel)) {
			for (int i = 0; i < userVos.length; i++) {
				newTask(task, beforeAddSignVo, userVos, i);
			}
		} else if (logic.equals(Logic.Sequence)) {
			newTask(task, beforeAddSignVo, userVos, 0);
		}
		this.updateBeforeAddSignInfo(logic, beforeAddSignVo);
	}
	private void updateTask() {
		TaskEntity task = (TaskEntity) WorkflowContext.getCurrentBpmnSession().getTask();
		task.setStatus(TaskInstanceStatus.BeforeAddSignSend); // 将本身的任务设置成加签发送状态
		task.asyn();
	}
	private void newTask(TaskEntity task, BeforeAddSignVO beforeAddSignVo, BeforeAddSignUserVO[] userVos, int i) {
		TaskEntity newTask = TaskEntity.newTask(task.getExecution(),TaskInstanceCreateType.BeforeAddSign);
		newTask = (TaskEntity) TaskUtil.initTask(newTask);
		newTask.setOwner(userVos[i].getPk_user());
		newTask.setParentTask(task);
		newTask.setSuperTask(task);
		newTask.setBeforeaddsign_times(beforeAddSignVo.getTimes());
		newTask.setScratch(null);
		IBusinessKey formCtx = WorkflowContext.getCurrentBpmnSession().getBusinessObject();
		newTask.setFormInfoCtx(formCtx);
		newTask.asyn();
	}
	// 设置用户的启用标志
	private void updateBeforeAddSignInfo(Logic logic, BeforeAddSignVO beforeAddSignVo) {
		BeforeAddSignUserVO[] userVos = beforeAddSignVo.getBeforeAddSignUserVos();
		for (int i = 0; i < userVos.length; i++) {
			BeforeAddSignUserVO userVo = userVos[i];
			if (logic.equals(Logic.Sequence)) {
				int order = Integer.parseInt(userVo.getOrder_str());
				if (order == 0) {
					userVo.setIsusered(new UFBoolean(true));
				} else {
					userVo.setOrder_str(String.valueOf(order - 1));
				}
			} else {
				userVo.setIsusered(new UFBoolean(true));
			}
			WfmServiceFacility.getBeforeAddSignBill().updateBeforeAddSignUserVo(userVo);
		}
	}
	protected BeforeAddSignVO builderBeforeAddSignInfo() {
		TaskEntity task = (TaskEntity) WorkflowContext.getCurrentBpmnSession().getTask();
		CreateBeforeAddSignCtx flwInfoCtx = (CreateBeforeAddSignCtx) WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx();
		/**
		 * 构造前加签信息
		 */
		BeforeAddSignVO beforeAddSignVo = new BeforeAddSignVO();
		beforeAddSignVo.setPk_task(task.getTaskPk());
		beforeAddSignVo.setScratch(flwInfoCtx.getScratch());
		int maxTime = Integer.parseInt(TaskUtil.getMaxBeforeAddSignTimes(task.getTaskPk()));
		beforeAddSignVo.setTimes(String.valueOf(maxTime + 1));
		Logic logic = flwInfoCtx.getLogic();
		if (logic != null && logic.equals(Logic.Sequence)) {
			beforeAddSignVo.setLogic(logic.toString());
		} else {
			beforeAddSignVo.setLogic(logic.toString());
		}
		/**
		 * 构造前加签人信息
		 */
		AddSignUserInfoCtx[] addSignUserInfo = flwInfoCtx.getAddSingUsers();
		if (addSignUserInfo == null || addSignUserInfo.length == 0) {
			throw new WorkflowRuntimeException("请选择前加签人");
		}
		int length = addSignUserInfo.length;
		BeforeAddSignUserVO[] addSignUserVos = new BeforeAddSignUserVO[length];
		AddSignUserInfoCtx tmpUserInfo = null;
		for (int i = 0; i < length; i++) {
			tmpUserInfo = addSignUserInfo[i];
			BeforeAddSignUserVO userVo = new BeforeAddSignUserVO();
			userVo.setPk_user(tmpUserInfo.getUserPk());
			userVo.setPk_dept(tmpUserInfo.getDeptPk());
			userVo.setIsusered(new UFBoolean(false));
			if (logic.equals(Logic.Parallel)) {
				userVo.setOrder_str(String.valueOf(0));
			}
			if (logic.equals(Logic.Sequence)) {
				userVo.setOrder_str(String.valueOf(i));
			}
			addSignUserVos[i] = userVo;
		}
		beforeAddSignVo.setBeforeAddSignUserVos(addSignUserVos);
		NCLocator.getInstance().lookup(IBeforeAddSignBill.class).saveBeforeAddSignVo(beforeAddSignVo);
		return beforeAddSignVo;
	}
}
