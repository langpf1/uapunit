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
 * ��������״̬�жϵ���״̬
 */
public class WorkflowStausUtil
{
	public int queryFlowStatus(WFAppParameter paraVo) {

		ProcessInstanceVO[] proInss = ProcessInstanceUtil.getProcessInstanceVOs(paraVo.getBillId());
		// �����Ѿ�ӵ������ʵ��
		if (proInss != null && proInss.length > 0) {
			ProcessInstanceVO proIns = proInss[0];
			int status = proIns.getState_proins();
			String result = paraVo.getWorkFlow().approveresult;
			// ����δ����
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
					// ������޸ĵ��ݵ�����,Ϊ����̬
					return IReturnCheckInfo.NOSTATE;

				if (hasFinishedItem) {
					// �����������ɵ����������������
					return IReturnCheckInfo.GOINGON;
				} else if (hasStartedItem) {
					// ������������������������ύ̬
					return IReturnCheckInfo.COMMIT;
				} else
					// ���������/��ɵ���������������̬
					return IReturnCheckInfo.NOSTATE;
			}
			// �����Ѿ�����
			if (result != null && result.equalsIgnoreCase("Y"))
				return IReturnCheckInfo.PASSING;
			if (result != null && result.equalsIgnoreCase("N"))
				return IReturnCheckInfo.NOPASS;
		}
		// ��������ʵ������Ϊ����̬
		return IReturnCheckInfo.NOSTATE;
	}
}
