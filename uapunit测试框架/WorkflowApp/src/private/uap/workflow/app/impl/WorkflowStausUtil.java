package uap.workflow.app.impl;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import uap.workflow.vo.IReturnCheckInfo;
import uap.workflow.engine.core.ProcessInstanceStatus;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.itf.ITaskInstanceQry;
import uap.workflow.engine.utils.ProcessInstanceUtil;
import uap.workflow.engine.vos.ProcessInstanceVO;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.vo.WFAppParameter;

/**
 * 根据流程状态判断单据状态
 */
public class WorkflowStausUtil
{
	public int queryFlowStatus(WFAppParameter paraVo) {

		ProcessInstanceVO[] proInss = ProcessInstanceUtil.getProcessInstanceVOs(paraVo.getBillId());
		// 单据已经拥有流程实例
		if (proInss != null && proInss.length > 0) {
			ProcessInstanceVO proIns = proInss[0];
			int status = proIns.getState_proins();
			String result = paraVo.getWorkFlow().approveresult;
			// 流程未结束
			if (status != ProcessInstanceStatus.Finished.getIntValue()) {
				List<TaskInstanceVO> tasks = NCLocator.getInstance().lookup(ITaskInstanceQry.class)
						.getTasksByFormInstancePk(paraVo.getBillId());
				boolean hasFinishedItem = false;
				boolean hasStartedItem = false;
				boolean hasMakeBillItem = false;
				for (TaskInstanceVO task : tasks) {
					if (task.getState_task() == TaskInstanceStatus.Started.getIntValue()) {
						hasStartedItem = true;
						if (task.getCreate_type() == TaskInstanceCreateType.Makebill.getIntValue()) {
							hasMakeBillItem = true;
						}
					}
					if (task.getState_task() == TaskInstanceStatus.Finished.getIntValue())
						hasFinishedItem = true;
				}

				if (hasMakeBillItem)
					// 如果有修改单据的任务,为自由态
					return IReturnCheckInfo.NOSTATE;

				if (hasFinishedItem) {
					// 如果存在已完成的审批任务，则进行中
					return IReturnCheckInfo.GOINGON;
				} else if (hasStartedItem) {
					// 如果存在启动的审批任务，则提交态
					return IReturnCheckInfo.COMMIT;
				} else
					// 如果无启动/完成的审批任务，则自由态
					return IReturnCheckInfo.NOSTATE;
			}
			// 流程已经结束
			if (result != null && result.equalsIgnoreCase("Y"))
				return IReturnCheckInfo.PASSING;
			if (result != null && result.equalsIgnoreCase("N"))
				return IReturnCheckInfo.NOPASS;
		}
		// 尚无流程实例，则为自由态
		return IReturnCheckInfo.NOSTATE;
	}
}
