package uap.workflow.app.impl;

import java.util.ArrayList;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import uap.workflow.admin.IWorkflowAdmin;
import uap.workflow.admin.WorkflowManageContext;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.vo.FlowOverdueVO;
import uap.workflow.vo.FlowTimeSettingVO;
import uap.workflow.vo.WorkflownoteVO;

/**
 * 审批流、工作流管理接口实现类
 * 
 * @author dingxm 2009-5
 * @since 6.0
 */
public class WorkflowAdminImpl implements IWorkflowAdmin {

	public void terminateWorkflow(String billid, String pkBilltype,
			String billNo, int iWorkflowtype) throws BusinessException {
		NCLocator.getInstance().lookup(WFAdminServiceImpl.class).terminateWorkflow(billid, pkBilltype, billNo, iWorkflowtype);
	}

	public void resumeWorkflow(String billid, String pkBilltype, String billNo,
			int iWorkflowtype) throws BusinessException {
		NCLocator.getInstance().lookup(WFAdminServiceImpl.class).resumeWorkflow(billid, pkBilltype, billNo, iWorkflowtype);
	}

	public void suspendWorkflow(String billid, String pkBilltype,
			String billNo, int iWorkflowtype) throws BusinessException {
		NCLocator.getInstance().lookup(WFAdminServiceImpl.class).suspendWorkflow(billid, pkBilltype, billNo, iWorkflowtype);
	}

	/**
	 * 邮件催办
	 * 
	 * @param workitemnote
	 * @throws BusinessException
	 */
	public void mailUrgency(WorkflownoteVO workitemnote)
			throws BusinessException {
		NCLocator.getInstance().lookup(WFAdminServiceImpl.class).mailUrgency(workitemnote);
	}

	public UFBoolean hasRunningProcess(String billId, String billType,
			String flowType) {
		return NCLocator.getInstance().lookup(WFAdminServiceImpl.class).hasRunningProcess(billId, billType, flowType);
	}
	
	public void cpySendByMailAndMsg(WorkflownoteVO worknoteVO,
			String[] titleAndnote) throws BusinessException {
		NCLocator.getInstance().lookup(WFAdminServiceImpl.class).cpySendByMailAndMsg(worknoteVO, titleAndnote);
	}

	public boolean isAlreadyTracked(String pk_wf_instance, String supervisor)
			throws BusinessException {
		return NCLocator.getInstance().lookup(WFAdminServiceImpl.class).isAlreadyTracked(pk_wf_instance, supervisor);
	}

	public void trackWFinstance(WorkflownoteVO worknoteVO, String supervisor,
			boolean isTrack) throws BusinessException {
		NCLocator.getInstance().lookup(WFAdminServiceImpl.class).trackWFinstance(worknoteVO, supervisor, isTrack);
	}

	public void terminateWorkflow(PfParameterVO paraVo)	throws PFBusinessException {
		NCLocator.getInstance().lookup(WFAdminServiceImpl.class).terminateWorkflow(paraVo.toWFAppParameter());
	}

	public void suspendWorkflow(WorkflowManageContext context) throws BusinessException {
		NCLocator.getInstance().lookup(WFAdminServiceImpl.class).suspendWorkflow(context);
	}

	public void resumeWorkflow(WorkflowManageContext context) throws BusinessException {
		NCLocator.getInstance().lookup(WFAdminServiceImpl.class).resumeWorkflow(context);
	}

	public void terminateWorkflow(WorkflowManageContext context) throws BusinessException {
		NCLocator.getInstance().lookup(WFAdminServiceImpl.class).terminateWorkflow(context);
	}

	public void updateFlowTimeSetting(String mainPk_wf_instance, FlowTimeSettingVO[] settings) throws BusinessException {
		NCLocator.getInstance().lookup(WFAdminServiceImpl.class).updateFlowTimeSetting(mainPk_wf_instance, settings);
	}

	public FlowTimeSettingVO[] getFlowTimeSetting(String mainPk_wf_instance) throws BusinessException {
		return NCLocator.getInstance().lookup(WFAdminServiceImpl.class).getFlowTimeSetting(mainPk_wf_instance);
	}

	public Map<String, FlowOverdueVO> getWorknoteOverdueBatch(String[] pk_checkflows) throws BusinessException {
		return NCLocator.getInstance().lookup(WFAdminServiceImpl.class).getWorknoteOverdueBatch(pk_checkflows);
	}

	public Map<String, FlowOverdueVO> getFlowInstanceOverdue(String[] pk_wf_instances) throws BusinessException {
		return NCLocator.getInstance().lookup(WFAdminServiceImpl.class).getFlowInstanceOverdue(pk_wf_instances);
	}

	public ArrayList<String> findFilterOrgs4Responsibility(TaskInstanceVO task)
			throws BusinessException {
		return NCLocator.getInstance().lookup(WFAdminServiceImpl.class).findFilterOrgs4Responsibility(task);
	}
}
