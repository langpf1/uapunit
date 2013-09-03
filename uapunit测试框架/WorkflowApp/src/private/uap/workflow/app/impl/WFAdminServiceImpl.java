package uap.workflow.app.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfo;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.uap.lock.PKLock;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.org.IFatherOrgPubQryService;
import nc.ui.ml.NCLangRes;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.IWorkFlowStatus;
import nc.vo.sm.UserVO;
import nc.vo.wfengine.pub.ProcessInsSupervisorType;
import uap.workflow.admin.IWFAdminService;
import uap.workflow.admin.WorkflowManageContext;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.core.WorkflowTypeEnum;
import uap.workflow.engine.vos.ProcessInstanceVO;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.pub.app.message.PfMessageUtil;
import uap.workflow.pub.app.message.vo.MessageTypes;
import uap.workflow.pub.app.message.vo.MessageinfoVO;
import uap.workflow.vo.FlowOverdueVO;
import uap.workflow.vo.FlowTimeSettingVO;
import uap.workflow.vo.WFAppParameter;
import uap.workflow.vo.WorkflownoteVO;

/**
 * 审批流、工作流管理接口实现类
 */
public class WFAdminServiceImpl implements IWFAdminService {

	public void terminateWorkflow(String billid, String pkBilltype,
			String billNo, int iWorkflowtype) throws BusinessException {

		// 不论审批流还是工作流，都可以挂单据组件。所以都需要回退
		// add lock according to billid
		PKLock.getInstance().addDynamicLock(billid);

		// terminate process instance and rollback business
		try {
			if (iWorkflowtype == WorkflowTypeEnum.Approveflow.getIntValue()) {
				terminateApproveflow(billid, pkBilltype, billNo, null, null, false);
			} else if (iWorkflowtype == WorkflowTypeEnum.Workflow.getIntValue()) {
				terminateWorkflow(billid, pkBilltype, billNo, null, null);
			} else
				throw new PFBusinessException(NCLangResOnserver.getInstance()
						.getStrByID("pfworkflow", "wfAdminImpl-0004")/* 传入了错误的参数值iWorkflowtype */);

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
	}

	/**
	 * 终止审批流实例，并进行业务回滚 2011.09.20 xry 增加，提交即审批的流程类型，支持终止
	 * 
	 * @param billid
	 * @param pkBilltype
	 * @param billNo
	 * @throws Exception
	 */
	private void terminateApproveflow(String billid, String pkBilltype,
			String billNo, String reason, AggregatedValueObject billVO,
			boolean autoApproveAfterCommit) throws Exception {
		/*
		// 1.判定该单据是否已经审批完成，如果是，则抛出异常返回
		EngineService es = new EngineService();
		int status = es.queryApproveflowStatus(billid, pkBilltype);
		if ((status == IPfRetCheckInfo.PASSING || status == IPfRetCheckInfo.NOPASS)
				&& !autoApproveAfterCommit)
			throw new PFBusinessException(NCLangResOnserver.getInstance()
					.getStrByID("pfworkflow", "wfAdminImpl-0005")/*
																 * 单据所属的流程已结束，无法终止
																 * ！
																 * /);

		// 2.修改单据数据库状态为自由态
		// boolean hasMeta =
		// PfMetadataTools.checkBilltypeRelatedMeta(pkBilltype);
		AbstractBusiStateCallback absc = new PFBusiStateOfMeta();

		// 查询出单据VO实体
		if (billVO == null) {
			IPFConfig pfcfg = (IPFConfig) NCLocator.getInstance().lookup(
					IPFConfig.class.getName());
			billVO = pfcfg.queryBillDataVO(pkBilltype, billid);
		}

		// 构造工作流参数VO
		PfParameterVO paraVo = construtParamVO(billid, pkBilltype, billNo,
				WorkflowTypeEnum.Approveflow.getIntValue());
		paraVo.m_workFlow = new WorkflownoteVO();
		paraVo.m_workFlow.setChecknote(WfTaskOrInstanceStatus.Terminated
				.toString());
		paraVo.m_workFlow.setActiontype(IPFActionName.TERMINATE);
		paraVo.m_preValueVo = billVO;
		WorknoteManager manager = new WorknoteManager();
		String processId = manager.getProcessId(paraVo,
				WorkflowTypeEnum.Approveflow.getIntValue());
		if (billVO != null) { // 可能billVO == null，单据已经被删除了
			absc.execUnApproveState(paraVo, null, IPfRetCheckInfo.NOSTATE);
			// 业务回滚
			es.rollbackWorkflow(billid, pkBilltype, billVO,
					WorkflowTypeEnum.Approveflow.getIntValue());
		}
		
		// 3.查询流程状态，在pub_wf_instance_h中记录此次操作的历史记录
		List<Object[]> statusList = es.queryProcessStatus(billid, pkBilltype, WorkflowTypeEnum.Approveflow.getIntValue());
		List<FlowInstanceHistoryVO> historyList = new ArrayList<FlowInstanceHistoryVO>();
		
		for (Object[] statusRow : statusList) {
			String pk_wf_instance = String.valueOf(statusRow[0]);
			Integer procStatus = Integer.parseInt(String.valueOf(statusRow[1]));
			
			FlowInstanceHistoryVO history = new FlowInstanceHistoryVO();
			history.setPk_wf_instance(pk_wf_instance);
			history.setPreviousStatus(procStatus);
			history.setOperation(FlowInstanceOperation.TERMINATE.getIntValue());
			history.setReason(reason);
			history.setOperator(InvocationInfoProxy.getInstance().getUserId());
			history.setBilltype(pkBilltype);
			history.setBillid(billid);
			history.setBillno(billNo);
			
			// FIXME: 是否记录业务日期?
			history.setOperationDate(new UFDateTime());
			
			historyList.add(history);
		}
		
		// 4.删除该单据相关的流程信息
		es.deleteWorkflow(billid, pkBilltype, false,
				WorkflowTypeEnum.Approveflow.getIntValue());

		// 5.给相关人员发送消息
		manager.sendMsgWhenWFstateChanged(paraVo, processId,
				WfTaskOrInstanceStatus.Terminated.getIntValue(),
				WorkflowTypeEnum.Approveflow.getIntValue());
				*/

	}

	/**
	 * 终止工作流实例，并进行业务回滚
	 * 
	 * @param billid
	 * @param pkBilltype
	 * @param billNo
	 * @throws Exception
	 */
	private void terminateWorkflow(String billid, String pkBilltype,
			String billNo, String reason, AggregatedValueObject billVO) throws Exception {
		/*
		// 1.判定该单据是否处于工作流进行中，如果不是，则抛出异常返回
		EngineService es = new EngineService();
		int status = es.queryWorkflowStatus(billid, pkBilltype);
		if (status == IPfRetCheckInfo.PASSING)
			throw new PFBusinessException(NCLangResOnserver.getInstance()
					.getStrByID("pfworkflow", "wfAdminImpl-0005")/*
																 * 单据所属的流程已结束，无法终止
																 * ！
																 * /);

		// 2.修改单据数据库状态为自由态
		// boolean hasMeta =
		// PfMetadataTools.checkBilltypeRelatedMeta(pkBilltype);
		AbstractBusiStateCallback absc = new PFBusiStateOfMeta();

		// 查询出单据VO实体
		if (billVO == null) {
			IPFConfig pfcfg = (IPFConfig) NCLocator.getInstance().lookup(
					IPFConfig.class.getName());
			billVO = pfcfg.queryBillDataVO(pkBilltype, billid);
		}
		// 构造工作流参数VO
		PfParameterVO paraVo = construtParamVO(billid, pkBilltype, billNo,
				WorkflowTypeEnum.Workflow.getIntValue());
		paraVo.m_workFlow = new WorkflownoteVO();
		paraVo.m_workFlow.setChecknote(WfTaskOrInstanceStatus.Terminated
				.toString());
		paraVo.m_preValueVo = billVO;
		absc.execUnApproveState(paraVo, null, IPfRetCheckInfo.NOSTATE);
		WorknoteManager manager = new WorknoteManager();
		String processId = manager.getProcessId(paraVo,
				WorkflowTypeEnum.Workflow.getIntValue());
		
		
		// 3.查询流程状态，在pub_wf_instance_h中记录此次操作的历史记录
		List<Object[]> statusList = es.queryProcessStatus(billid, pkBilltype, WorkflowTypeEnum.Workflow.getIntValue());
		List<FlowInstanceHistoryVO> historyList = new ArrayList<FlowInstanceHistoryVO>();
		
		for (Object[] statusRow : statusList) {
			String pk_wf_instance = String.valueOf(statusRow[0]);
			Integer procStatus = Integer.parseInt(String.valueOf(statusRow[1]));
			
			FlowInstanceHistoryVO history = new FlowInstanceHistoryVO();
			history.setPk_wf_instance(pk_wf_instance);
			history.setPreviousStatus(procStatus);
			history.setOperation(FlowInstanceOperation.TERMINATE.getIntValue());
			history.setReason(reason);
			history.setOperator(InvocationInfoProxy.getInstance().getUserId());
			history.setBilltype(pkBilltype);
			history.setBillid(billid);
			history.setBillno(billNo);
			
			// FIXME: 是否记录业务日期?
			history.setOperationDate(new UFDateTime());
			
			historyList.add(history);
		}
		
		new BaseDAO().insertVOList(historyList);
		
		// 4.删除该单据相关的流程信息，并进行业务回滚
		es.rollbackWorkflow(billid, pkBilltype, billVO,
				WorkflowTypeEnum.Workflow.getIntValue());
		es.deleteWorkflow(billid, pkBilltype, false,
				WorkflowTypeEnum.Workflow.getIntValue());

		// 5.给相关人员发送消息，并不给已处理人发送消息
		manager.sendMsgWhenWFstateChanged(paraVo, processId,
				WfTaskOrInstanceStatus.Terminated.getIntValue(),
				WorkflowTypeEnum.Workflow.getIntValue());
				*/
	}

	public void resumeWorkflow(String billid, String pkBilltype, String billNo,
			int iWorkflowtype) throws BusinessException {
		try {
			if (iWorkflowtype == WorkflowTypeEnum.Approveflow.getIntValue()) {
				resumeApproveflow(billid, pkBilltype, billNo, null);
			} else if (iWorkflowtype == WorkflowTypeEnum.Workflow.getIntValue()) {
				resumeWorkflow(billid, pkBilltype, billNo, null);
			} else
				throw new PFBusinessException(NCLangResOnserver.getInstance()
						.getStrByID("pfworkflow", "wfAdminImpl-0004")/* 传入了错误的参数值iWorkflowtype */);

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
	}

	private void resumeWorkflow(String billid, String pkBilltype, String billNo, String reason)
			throws Exception {
		/*
		// 1.判定该单据是否处于审批进行中，如果不是，则抛出异常返回
		EngineService queryDMO = new EngineService();
		int status = queryDMO.queryApproveflowStatus(billid, pkBilltype);
		if (status == IPfRetCheckInfo.PASSING
				|| status == IPfRetCheckInfo.NOPASS)
			throw new PFBusinessException(NCLangResOnserver.getInstance()
					.getStrByID("pfworkflow", "wfAdminImpl-0006")/*
																 * 单据所属的流程已结束，无法恢复
																 * ！
																 * /);
		
		// 2.查询流程状态，在pub_wf_instance_h中记录此次操作的历史记录
		List<Object[]> statusList = queryDMO.queryProcessStatus(billid, pkBilltype, WorkflowTypeEnum.Workflow.getIntValue());
		List<FlowInstanceHistoryVO> historyList = new ArrayList<FlowInstanceHistoryVO>();
		
		for (Object[] statusRow : statusList) {
			String pk_wf_instance = String.valueOf(statusRow[0]);
			Integer procStatus = Integer.parseInt(String.valueOf(statusRow[1]));
			
			FlowInstanceHistoryVO history = new FlowInstanceHistoryVO();
			history.setPk_wf_instance(pk_wf_instance);
			history.setPreviousStatus(procStatus);
			history.setOperation(FlowInstanceOperation.RESUME.getIntValue());
			history.setReason(reason);
			history.setOperator(InvocationInfoProxy.getInstance().getUserId());
			history.setBilltype(pkBilltype);
			history.setBillid(billid);
			history.setBillno(billNo);
			
			// FIXME: 是否记录业务日期?
			history.setOperationDate(new UFDateTime());
			
			historyList.add(history);
		}

		// 3.修改该单据相关的流程信息,将该流程实例状态设置为开始
		queryDMO.updateProcessStatus(billid, pkBilltype,
				WorkflowTypeEnum.Workflow.getIntValue(),
				WfTaskOrInstanceStatus.Started.getIntValue());
		// 3.流程实例状态变化,给相关人员发送消息
		PfParameterVO paramVo = construtParamVO(billid, pkBilltype, billNo,
				WorkflowTypeEnum.Workflow.getIntValue());
		WorknoteManager manager = new WorknoteManager();
		String processId = manager.getProcessId(paramVo,
				WorkflowTypeEnum.Workflow.getIntValue());
		manager.sendMsgWhenWFstateChanged(paramVo, processId, 10,
				WorkflowTypeEnum.Workflow.getIntValue());
				*/
	}

	public void suspendWorkflow(String billid, String pkBilltype,
			String billNo, int iWorkflowtype) throws BusinessException {
		try {
			if (iWorkflowtype == WorkflowTypeEnum.Approveflow.getIntValue()) {
				suspendApproveflow(billid, pkBilltype, billNo, null);
			} else if (iWorkflowtype == WorkflowTypeEnum.Workflow.getIntValue()) {
				suspendWorkflow(billid, pkBilltype, billNo, null);
			} else
				throw new PFBusinessException(NCLangResOnserver.getInstance()
						.getStrByID("pfworkflow", "wfAdminImpl-0004")/* 传入了错误的参数值iWorkflowtype */);

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
	}

	private void resumeApproveflow(String billid, String pkBilltype,
			String billNo, String reason) throws Exception {
		// 1.判定该单据是否处于审批进行中，如果不是，则抛出异常返回
		/*
		EngineService queryDMO = new EngineService();
		int status = queryDMO.queryApproveflowStatus(billid, pkBilltype);
		if (status == IPfRetCheckInfo.PASSING
				|| status == IPfRetCheckInfo.NOPASS)
			throw new PFBusinessException(NCLangResOnserver.getInstance()
					.getStrByID("pfworkflow", "wfAdminImpl-0006")/*
																 * 单据所属的流程已结束，无法恢复
																 * ！
																 * /);
		// 2.查询流程状态，在pub_wf_instance_h中记录此次操作的历史记录
		List<Object[]> statusList = queryDMO.queryProcessStatus(billid, pkBilltype, WorkflowTypeEnum.Approveflow.getIntValue());
		List<FlowInstanceHistoryVO> historyList = new ArrayList<FlowInstanceHistoryVO>();
		
		for (Object[] statusRow : statusList) {
			String pk_wf_instance = String.valueOf(statusRow[0]);
			Integer procStatus = Integer.parseInt(String.valueOf(statusRow[1]));
			
			FlowInstanceHistoryVO history = new FlowInstanceHistoryVO();
			history.setPk_wf_instance(pk_wf_instance);
			history.setPreviousStatus(procStatus);
			history.setOperation(FlowInstanceOperation.RESUME.getIntValue());
			history.setReason(reason);
			history.setOperator(InvocationInfoProxy.getInstance().getUserId());
			history.setBilltype(pkBilltype);
			history.setBillid(billid);
			history.setBillno(billNo);
			
			// FIXME: 是否记录业务日期?
			history.setOperationDate(new UFDateTime());
			
			historyList.add(history);
		}
		
		new BaseDAO().insertVOList(historyList);

		// // 2.修改单据数据库状态为自由态
		// //boolean hasMeta =
		// PfMetadataTools.checkBilltypeRelatedMeta(pkBilltype);
		// AbstractBusiStateCallback absc = new PFBusiStateOfMeta();
		//
		// //查询出单据VO实体
		// IPFConfig pfcfg = (IPFConfig)
		// NCLocator.getInstance().lookup(IPFConfig.class.getName());
		// AggregatedValueObject billVO = pfcfg.queryBillDataVO(pkBilltype,
		// billid);
		// //构造工作流参数VO
		// PfParameterVO paraVo = new PfParameterVO();
		// paraVo.m_workFlow = new WorkflownoteVO();
		// paraVo.m_workFlow.setChecknote("挂起流程恢复");
		// paraVo.m_billId = billid;
		// paraVo.m_billType = pkBilltype;
		// paraVo.m_preValueVo = billVO;
		// absc.execUnApproveState(paraVo, null, IPfRetCheckInfo.GOINGON);

		// FIXME: 针对已完成的子流程，此处应还原为子流程挂起之前的状态（进行中或已完成）
		// 3.修改该单据相关的流程信息,将该流程实例状态设置为开始
		queryDMO.updateProcessStatus(billid, pkBilltype,
				WorkflowTypeEnum.Approveflow.getIntValue(),
				WfTaskOrInstanceStatus.Started.getIntValue());

		// 4.流程实例状态变化,给相关人员发送消息
		PfParameterVO paramVo = construtParamVO(billid, pkBilltype, billNo,
				WorkflowTypeEnum.Approveflow.getIntValue());
		WorknoteManager manager = new WorknoteManager();
		String processId = manager.getProcessId(paramVo,
				WorkflowTypeEnum.Approveflow.getIntValue());
		manager.sendMsgWhenWFstateChanged(paramVo, processId, 10,
				WorkflowTypeEnum.Approveflow.getIntValue());
				*/
	}

	/**
	 * 挂起工作流实例
	 * 
	 * @param billid
	 * @param pkBilltype
	 * @param billNo
	 * @throws Exception
	 */
	private void suspendWorkflow(String billid, String pkBilltype, String billNo, String reason)
			throws Exception {
		// 1.判定该单据是否处于审批进行中，如果不是，则抛出异常返回
		/*
		EngineService queryDMO = new EngineService();
		int status = queryDMO.queryApproveflowStatus(billid, pkBilltype);
		if (status == IPfRetCheckInfo.PASSING
				|| status == IPfRetCheckInfo.NOPASS)
			throw new PFBusinessException(NCLangResOnserver.getInstance()
					.getStrByID("pfworkflow", "wfAdminImpl-0007")/*
																 * 单据所属的流程已结束，无法挂起
																 * ！
																 * /);
		
		
		// 2.查询流程状态，在pub_wf_instance_h中记录此次操作的历史记录
		List<Object[]> statusList = queryDMO.queryProcessStatus(billid, pkBilltype, WorkflowTypeEnum.Workflow.getIntValue());
		List<FlowInstanceHistoryVO> historyList = new ArrayList<FlowInstanceHistoryVO>();
		
		for (Object[] statusRow : statusList) {
			String pk_wf_instance = String.valueOf(statusRow[0]);
			Integer procStatus = Integer.parseInt(String.valueOf(statusRow[1]));
			
			FlowInstanceHistoryVO history = new FlowInstanceHistoryVO();
			history.setPk_wf_instance(pk_wf_instance);
			history.setPreviousStatus(procStatus);
			history.setOperation(FlowInstanceOperation.SUSPEND.getIntValue());
			history.setReason(reason);
			history.setOperator(InvocationInfoProxy.getInstance().getUserId());
			history.setBilltype(pkBilltype);
			history.setBillid(billid);
			history.setBillno(billNo);
			
			// FIXME: 是否记录业务日期?
			history.setOperationDate(new UFDateTime());
			
			historyList.add(history);
		}
		
		new BaseDAO().insertVOList(historyList);
		

		// 3.修改该单据相关的流程信息,将该流程实例状态设置为挂起
		queryDMO.updateProcessStatus(billid, pkBilltype,
				WorkflowTypeEnum.Workflow.getIntValue(),
				WfTaskOrInstanceStatus.Suspended.getIntValue());
		// 4.流程实例状态变化,给相关人员发送消息
		PfParameterVO paramVo = construtParamVO(billid, pkBilltype, billNo,
				WorkflowTypeEnum.Workflow.getIntValue());
		WorknoteManager manager = new WorknoteManager();
		String processId = manager.getProcessId(paramVo,
				WorkflowTypeEnum.Workflow.getIntValue());
		manager.sendMsgWhenWFstateChanged(paramVo, processId,
				WfTaskOrInstanceStatus.Suspended.getIntValue(),
				WorkflowTypeEnum.Workflow.getIntValue());
				*/
	}

	/**
	 * 挂起审批流实例
	 * 
	 * @param billid
	 * @param pkBilltype
	 * @param billNo
	 * @throws Exception
	 */
	private void suspendApproveflow(String billid, String pkBilltype,
			String billNo, String reason) throws Exception {
		/*
		// 1.判定该单据是否处于审批进行中，如果不是，则抛出异常返回
		EngineService queryDMO = new EngineService();
		int status = queryDMO.queryApproveflowStatus(billid, pkBilltype);
		if (status == IPfRetCheckInfo.PASSING
				|| status == IPfRetCheckInfo.NOPASS)
			throw new PFBusinessException(NCLangResOnserver.getInstance()
					.getStrByID("pfworkflow", "wfAdminImpl-0007")/*
																 * 单据所属的流程已结束，无法挂起
																 * ！
																 * /);
		
		// 2.查询流程状态，在pub_wf_instance_h中记录此次操作的历史记录
		List<Object[]> statusList = queryDMO.queryProcessStatus(billid, pkBilltype, WorkflowTypeEnum.Approveflow.getIntValue());
		List<FlowInstanceHistoryVO> historyList = new ArrayList<FlowInstanceHistoryVO>();
		
		for (Object[] statusRow : statusList) {
			String pk_wf_instance = String.valueOf(statusRow[0]);
			Integer procStatus = Integer.parseInt(String.valueOf(statusRow[1]));
			
			FlowInstanceHistoryVO history = new FlowInstanceHistoryVO();
			history.setPk_wf_instance(pk_wf_instance);
			history.setPreviousStatus(procStatus);
			history.setOperation(FlowInstanceOperation.SUSPEND.getIntValue());
			history.setReason(reason);
			history.setOperator(InvocationInfoProxy.getInstance().getUserId());
			history.setBilltype(pkBilltype);
			history.setBillid(billid);
			history.setBillno(billNo);
			
			// FIXME: 是否记录业务日期?
			history.setOperationDate(new UFDateTime());
			
			historyList.add(history);
		}
		
		new BaseDAO().insertVOList(historyList);

		// 2.修改该单据相关的流程信息,将该流程实例状态设置为挂起
		queryDMO.updateProcessStatus(billid, pkBilltype,
				WorkflowTypeEnum.Approveflow.getIntValue(),
				WfTaskOrInstanceStatus.Suspended.getIntValue());
		// 3.流程实例状态变化,给相关人员发送消息
		PfParameterVO paramVo = construtParamVO(billid, pkBilltype, billNo,
				WorkflowTypeEnum.Approveflow.getIntValue());
		WorknoteManager manager = new WorknoteManager();
		String processId = manager.getProcessId(paramVo,
				WorkflowTypeEnum.Approveflow.getIntValue());
		manager.sendMsgWhenWFstateChanged(paramVo, processId,
				WfTaskOrInstanceStatus.Suspended.getIntValue(),
				WorkflowTypeEnum.Approveflow.getIntValue());
				*/
	}

	/**
	 * 邮件催办
	 * 
	 * @param workitemnote
	 * @throws BusinessException
	 */
	public void mailUrgency(WorkflownoteVO workitemnote)
			throws BusinessException {
		/*
		/** 1.给指定的工作项发送催批邮件 * /
		EngineService wfQry = new EngineService();
		LinkedHashMap<String, BasicWorkflowProcess> lhm = new LinkedHashMap();
		/** 通过指定的工作项的PK查询它属于的流程实例的 PK * /

		TaskManagerDMO dmo = new TaskManagerDMO();
		WFTask task = null;
		try {
			task = dmo.getTaskByPK(workitemnote.getPk_wf_task());
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}

		if (task == null)
			return;

		String strProcInstPK = task.getWfProcessInstancePK();
		// 查询审批流定义
		BasicWorkflowProcess bwp = null;
		if (!lhm.containsKey(strProcInstPK)) {
			try {
				bwp = wfQry.findParsedMainWfProcessByInstancePK(strProcInstPK);
				lhm.put(strProcInstPK, bwp);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				throw new PFBusinessException(e.getMessage());
			}
		}
		if (bwp == null)
			return;

		String strCheckman = workitemnote.getCheckman();
		
		String pk_checkflow = workitemnote.getPk_checkflow();
		FlowOverdueVO overdue = getWorknoteOverdueBatch(new String[] { pk_checkflow }).get(pk_checkflow);

		String strMessagenote = null;
		if (overdue.isOverdue()) {
			strMessagenote = NCLangResOnserver.getInstance().getStrByID(
					"pfworkflow",
					"wfAdminImpl-0008",
					null,
					new String[] { String.valueOf(overdue.getOverdueDays()) + DurationUnit.DAY.toString(),
							workitemnote.getMessagenote() })/*
															 * 超期提醒 ： 超期 { 0 } {
															 * 1 } , { 2 }
															 * /;
		} else {
			strMessagenote = workitemnote.getMessagenote();
		}

		/*
		 * added yanke1 2011-3-30 发送邮件前判断用户是否已设置邮箱信息
		 * /
		try {
			IUserPubService userService = NCLocator.getInstance().lookup(
					IUserPubService.class);
			IUAPQueryBS uapQry = NCLocator.getInstance().lookup(
					IUAPQueryBS.class);
			PsndocVO psndoc = (PsndocVO) uapQry.retrieveByPK(PsndocVO.class,
					userService.queryPsndocByUserid(strCheckman));
			String email = psndoc == null ? null : psndoc.getEmail();
			if (StringUtil.isEmptyWithTrim(email)) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new PFBusinessException(NCLangResOnserver.getInstance()
					.getStrByID("pfworkflow", "wfAdminImpl-0009")/* 找不到接收人，发送失败！ * /);
		}

		EmailMsg em = new EmailMsg();
		em.setMailModal(bwp.getMailModal());
		em.setUserIds(new String[] { strCheckman });
		em.setBillId(task.getBillID());
		em.setBillNo(task.getBillNO());
		em.setBillType(task.getBillType());
		em.setPrintTempletId(bwp.getMailPrintTemplet().getTempletid());
		em.setTopic(strMessagenote);
		em.setSenderman(task.getSenderman());
		em.setTasktype(task.getTaskType());
		em.setLangCode(InvocationInfoProxy.getInstance().getLangCode());
		em.setDatasource(InvocationInfoProxy.getInstance().getUserDataSource());
		em.setInvocationInfo(getInvocationInfo());
		
		
		// 异步发送邮件
		PfMailAndSMSUtil.sendEMS(em);
		*/
	}

	@Override
	public UFBoolean hasRunningProcess(String billId, String billType,
			String flowType) {
		String sql = "select pk_wf_instance from pub_wf_instance"
				// +
				// " where billid=? and billtype=? and isnull(src_pk_actinstance,'~')='~' and procstatus="
				// //去掉isnull(src_pk_actinstance,'~')='~'，因为子流程也可以走这里
				+ " where billversionpk=? and billtype=? and procstatus="
				+ TaskInstanceStatus.Started.getIntValue()
				+ " and workflow_type=?";
		

		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();
			para.addParam(billId);
			para.addParam(billType);
			para.addParam(Integer.parseInt(flowType));// 2011-7-12 wcj
														// 增加类型转换，因postgresql
														// 报类型转换错误
			
			Object obj = jdbc.executeQuery(sql, para, new ColumnProcessor(1));
			return obj == null ? UFBoolean.FALSE : UFBoolean.TRUE;
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
		} finally {
			if (persist != null)
				persist.release();
		}
		return UFBoolean.FALSE;
	}

	
	@Override
	public void cpySendByMailAndMsg(WorkflownoteVO worknoteVO,
			String[] titleAndnote) throws BusinessException {
		doCpySendByMsg(worknoteVO, titleAndnote);
		doCpySendByMail(worknoteVO, titleAndnote);
	}

	// 消息方式抄送
	private void doCpySendByMsg(WorkflownoteVO worknoteVO, String[] titleAndnote)
			throws BusinessException {
		String[] msgExtCpySenders = worknoteVO.getMsgExtCpySenders().toArray(
				new String[0]);
		if (msgExtCpySenders == null || msgExtCpySenders.length == 0)
			return;
		List<MessageinfoVO> msgInfoVOs = new ArrayList<MessageinfoVO>();
		String[] checkerNames = getUserNameByPK(msgExtCpySenders);

		String[] senderNames = getUserNameByPK(new String[] { worknoteVO
				.getCheckman() });

		if (checkerNames == null || checkerNames.length == 0)
			return;
		for (int start = 0, end = msgExtCpySenders.length; start < end; start++) {
			MessageinfoVO msgVO = new MessageinfoVO();
			msgVO = new MessageinfoVO();
			msgVO.setBillid(worknoteVO.getBillid());

			msgVO.setSenderman(worknoteVO.getCheckman());

			msgVO.setBillno(worknoteVO.getBillno());
			msgVO.setCheckman(msgExtCpySenders[start]);
			msgVO.setCheckmanName(checkerNames[start]);
			msgVO.setContent(titleAndnote[1]);
			msgVO.setPk_billtype(worknoteVO.getPk_billtype());

			msgVO.setTitle(senderNames[0] + "{UPPpfworkflow-000154}"/*
																	 * @res "抄送"
																	 */+ " "
					+ titleAndnote[0] + " " + "{UPPpfworkflow-000194}" /* "单据号: " */
					+ worknoteVO.getBillno());

			msgVO.setSenddate(new UFDateTime());
			msgVO.setDealdate(worknoteVO.getDealdate());
			msgVO.setDr(worknoteVO.getDr());
			msgVO.setType(MessageTypes.MSG_TYPE_INFO);
			msgVO.setPk_corp(worknoteVO.getPk_group());
			msgInfoVOs.add(msgVO);
		}
		PfMessageUtil.insertBizMessages(msgInfoVOs
				.toArray(new MessageinfoVO[msgInfoVOs.size()]));
	}

	// 邮件方式抄送
	private void doCpySendByMail(WorkflownoteVO worknoteVO,
			String[] titleAndnote) throws BusinessException {
		String[] mailExtCpySenders = worknoteVO.getMailExtCpySenders().toArray(
				new String[0]);
		if (mailExtCpySenders == null || mailExtCpySenders.length == 0)
			return;
		/*
		EngineService wfQry = new EngineService();
		WFTask currentTask = worknoteVO.getTaskInfo().getTask();
		BasicWorkflowProcess bwp = wfQry
				.findParsedMainWfProcessByInstancePK(currentTask
						.getWfProcessInstancePK());
		String ptId = bwp.getMailPrintTemplet().getTempletid();
		EmailMsg em = new EmailMsg();
		em.setMailModal(bwp.getMailModal());
		em.setUserIds(mailExtCpySenders);
		em.setBillId(worknoteVO.getBillid());
		em.setBillNo(worknoteVO.getBillno());
		em.setBillType(worknoteVO.getPk_billtype());
		em.setPrintTempletId(ptId);
		em.setTopic(titleAndnote[0]);
		em.setSenderman(worknoteVO.getSenderman());
		// 2009-5 需要如下赋值
		em.setTasktype(WfTaskType.Makebill.getIntValue());
		em.setLangCode(InvocationInfoProxy.getInstance().getLangCode());
		em.setDatasource(InvocationInfoProxy.getInstance().getUserDataSource());
		em.setInvocationInfo(getInvocationInfo());
		PfMailAndSMSUtil.sendEMS(em);
		*/
	}
	
	private InvocationInfo getInvocationInfo() {
		InvocationInfo info = new InvocationInfo();

		info.setBizDateTime(InvocationInfoProxy.getInstance().getBizDateTime());
		info.setGroupId(InvocationInfoProxy.getInstance().getGroupId());
		info.setGroupNumber(InvocationInfoProxy.getInstance().getGroupNumber());
		info.setLangCode(InvocationInfoProxy.getInstance().getLangCode());
		info.setUserDataSource(InvocationInfoProxy.getInstance().getUserDataSource());
		info.setUserId(InvocationInfoProxy.getInstance().getUserId());

		return info;
	}

	/**
	 * 由用户PK得到用户名
	 * */
	private String[] getUserNameByPK(String[] pks) {
		StringBuffer clause = new StringBuffer("cuserid in ( ");
		List<String> userNames = new ArrayList<String>();
		for (String pk : pks)
			clause.append("'" + pk + "', ");
		String where = clause.substring(0, clause.lastIndexOf(",")) + ")";
		try {
			BaseDAO dao = new BaseDAO();
			Collection<UserVO> users = dao
					.retrieveByClause(UserVO.class, where);
			for (UserVO user : users)
				userNames.add(user.getUser_name());
			return userNames.toArray(new String[userNames.size()]);

		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public boolean isAlreadyTracked(String pk_wf_instance, String supervisor)
			throws BusinessException {
		BaseDAO dao = new BaseDAO();

		String sql = "pk_wf_instance='" + pk_wf_instance + "' and supervisor='"
				+ supervisor + "' and type="
				+ ProcessInsSupervisorType.TRACKER.getIntValue();

		Collection<ProcessInstanceVO> obj = (Collection<ProcessInstanceVO>) dao
				.retrieveByClause(ProcessInstanceVO.class, sql);
		return obj != null && obj.size() != 0;
	}

	public void trackWFinstance(WorkflownoteVO worknoteVO, String supervisor,
			boolean isTrack) throws BusinessException {
		// TODO Auto-generated method stub
		String pk_wf_instance="";//worknoteVO.getTaskInfo().getTask().getWfProcessInstancePK();
		BaseDAO dao = new BaseDAO();
		if (!isTrack) {
			dao.deleteByClause(ProcessInstanceVO.class, "pk_wf_instance='"
					+ pk_wf_instance + "' and supervisor='" + supervisor + "'");
		} else {
			ProcessInstanceVO pvo = new ProcessInstanceVO();
			pvo.setPk_proins(pk_wf_instance);
			pvo.setSupervisor(supervisor);
			pvo.setType(ProcessInsSupervisorType.TRACKER.getIntValue());
			dao.insertVO(pvo);
		}
	}

	public void terminateWorkflow(WFAppParameter paraVo)	throws PFBusinessException {
		// 不论审批流还是工作流，都可以挂单据组件。所以都需要回退
		// add lock according to billid
		PKLock.getInstance().addDynamicLock(paraVo.getBillId());

		// terminate process instance and rollback business
		//TODO:实际的流程实例终止逻辑
		//terminateApproveflow(paraVo.m_billVersionPK, paraVo.m_billType, paraVo.m_billNo, null,paraVo.m_preValueVo, paraVo.m_autoApproveAfterCommit);
		//terminateWorkflow(paraVo.m_billVersionPK, paraVo.m_billType, paraVo.m_billNo, null, paraVo.m_preValueVo);
	}

	public void suspendWorkflow(WorkflowManageContext context) throws BusinessException {
		
		String billId = context.getBillId();
		Integer approvestatus = context.getApproveStatus();
		String billtype = context.getBillType();
		Integer workflow_type = context.getFlowType();
		String billNo = context.getBillNo();
		String reason = context.getManageReason();
	
			// XXX:查询单据在流程中的状态，为了处理驳回到制单人后，单据出于自由态的情况，
			// 此时修单后重新提交会新起一个流程实例，所以对旧实例的挂起没有意义
		int iBillStatus = NCLocator.getInstance().lookup(IPFWorkflowQry.class)
				.queryFlowStatus(billtype, billId, workflow_type);

		if (approvestatus == TaskInstanceStatus.Started.getIntValue()
				&& iBillStatus != IWorkFlowStatus.BILL_NOT_IN_WORKFLOW) {
			// WARN::只有正在运行中的主流程才可挂起
			
			try {
				if (workflow_type == WorkflowTypeEnum.Approveflow.getIntValue()) {
					suspendApproveflow(billId, billtype, billNo, reason);
				} else if (workflow_type == WorkflowTypeEnum.Workflow.getIntValue()) {
					suspendWorkflow(billId, billtype, billNo, reason);
				} else
					throw new Exception("只有主流程才可进行挂起操作！");
			} catch (Exception e) {
				throw new BusinessException(e);
			}
		} else {
			throw new BusinessException(NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000822") /*
																												 * @
																												 * res
																												 * "只有正在运行中，单据不是自由态的主流程才可挂起！"
																												 */);
		}
	}

	public void resumeWorkflow(WorkflowManageContext context) throws BusinessException {
		
		String billId = context.getBillId();
		Integer approvestatus = context.getApproveStatus();
		String billtype = context.getBillType();
		Integer workflow_type = context.getFlowType();
		String billNo = context.getBillNo();
		
		String reason = context.getManageReason();
		
		/** 恢复的前提必须是该流程实例的状态为挂起状态 */
		if (approvestatus == TaskInstanceStatus.Suspended.getIntValue()) {
			try {
				
				if (workflow_type == WorkflowTypeEnum.Approveflow.getIntValue()) {
					resumeApproveflow(billId, billtype, billNo, reason);
				} else if (workflow_type == WorkflowTypeEnum.Workflow.getIntValue()) {
					resumeWorkflow(billId, billtype, billNo, reason);
				} else
					throw new PFBusinessException("只有主流程才可进行恢复操作");

			} catch (Exception e) {
				throw new BusinessException(e);
			}
		} else {
			throw new BusinessException(NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000817") /*
																												 * @
																												 * res
																												 * "只有正在挂起中的主流程才可恢复！"
																												 */);
		}
		
	}

	public void terminateWorkflow(WorkflowManageContext context) throws BusinessException {
		boolean isSucess = false;
		String billId = context.getBillId();
		Integer approvestatus = context.getApproveStatus();
		String billtype = context.getBillType();
		Integer workflow_type = context.getFlowType();
		String billNo = context.getBillNo();
		String reason = context.getManageReason();

		if (approvestatus == TaskInstanceStatus.Started.getIntValue()) {
			// WARN::只有正在运行中的主流程才可终止

			try {
				if (workflow_type == WorkflowTypeEnum.Approveflow.getIntValue()) {
					terminateApproveflow(billId, billtype, billNo, reason, null, false);
				} else if (workflow_type == WorkflowTypeEnum.Workflow.getIntValue()) {
					terminateWorkflow(billId, billtype, billNo, reason, null);
				} else
					throw new PFBusinessException("只有主流程才可进行恢复操作");
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				throw new BusinessException(e);
			}

		} else {
			throw new BusinessException(NCLangResOnserver.getInstance()
					.getStrByID("pfworkflow", "UPPpfworkflow-000535")/*
																	 * @res
																	 * "只有正在运行中的主流程才可终止！"
																	 */);
		}
	}

	@Override
	public void updateFlowTimeSetting(String mainPk_wf_instance, FlowTimeSettingVO[] settings) throws BusinessException {
		BaseDAO dao = new BaseDAO();

		dao.deleteByClause(FlowTimeSettingVO.class, "mainPk_wf_instance='" + mainPk_wf_instance + "'");
		
		if (settings != null && settings.length > 0) {
			dao.insertVOArray(settings);
		}

	}

	@Override
	public FlowTimeSettingVO[] getFlowTimeSetting(String mainPk_wf_instance) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Collection<FlowTimeSettingVO> col = dao.retrieveByClause(FlowTimeSettingVO.class, "mainPk_wf_instance='" + mainPk_wf_instance
				+ "' order by type desc");

		return col.toArray(new FlowTimeSettingVO[0]);
	}

	@Override
	public Map<String, FlowOverdueVO> getWorknoteOverdueBatch(String[] pk_checkflows) throws BusinessException {
		//WorkflowOverdueCalculator calculator = new WorkflowOverdueCalculator();
		Map<String, FlowOverdueVO> map = new HashMap<String, FlowOverdueVO>();
		
		/*
		for (String pk : pk_checkflows) {
			FlowOverdueVO overdue = calculator.getWorknoteOverdue(pk);
			map.put(pk, overdue);
		}
		*/
		
		return map;
	}

	@Override
	public Map<String, FlowOverdueVO> getFlowInstanceOverdue(String[] pk_wf_instances) throws BusinessException {
		//WorkflowOverdueCalculator calculator = new WorkflowOverdueCalculator();
		Map<String, FlowOverdueVO> map = new HashMap<String, FlowOverdueVO>();
		
		/*
		for (String pk : pk_wf_instances) {
			FlowOverdueVO overdue = calculator.getFlowInstanceOverdue(pk);
			map.put(pk, overdue);
		}
		*/
		
		return map;
	}

	public ArrayList<String> findFilterOrgs4Responsibility(TaskInstanceVO task)
			throws BusinessException {
		/*
		if(task.getParticipantType().equals(OrganizeUnitTypes.RESPONSIBILITY.toString())){
			String pk = task.getActivityInstancePK();
			ProcessInstance processInstance = WfInstancePool.getInstance().getProcessInstance(task.getWfProcessInstancePK());			
			ActivityInstance actInst = processInstance.findActivityInstanceByPK(pk);
			Object participantFilterMode =((GenericActivityEx)actInst.getActivity()).getParticipantFilterMode().getValue();			
			
			//没有选择限定模式，或者选择无组织限定
			if(participantFilterMode==null||participantFilterMode.toString().equals(DefaultParticipantFilter.CODE_FREEORG4RESPONSIBILITY)){
				return null;
			}else if(participantFilterMode.toString().equals(DefaultParticipantFilter.CODE_SPECIFYORG4RESPONSIBILITY)){
				//指定组织限定
				return ((GenericActivityEx)actInst.getActivity()).getOrgUnitTree();				
			}else if(participantFilterMode.toString().equals(DefaultParticipantFilter.CODE_SAMEORG4RESPONSIBILITY)){
				//同组织限定							
				return findSameOrg4Responsibility(processInstance,actInst,task.getPk_org());							
			}else if(participantFilterMode.toString().equals(DefaultParticipantFilter.CODE_SUPERORG4RESPONSIBILITY)){
				//上级组织限定
				//组织体系
				String orgSystem =((GenericActivityEx)actInst.getActivity()).getOrgSystem();
				return findSuperiorOrg4Responsibility(processInstance,actInst,task.getPk_org(),orgSystem);
			}
		}	
		*/
		return null;
	}
	
	/*
	private ArrayList<String> findSameOrg4Responsibility(IProcessInstance processInstance,IActivityInstance actInst,String billOrg){
		ArrayList<String> filterOrgs =new ArrayList<String>();
		Vector<String> srcActInsts = actInst.getSrcActivityInstancePKs();
		for(String srcActInst :srcActInsts){
			ActivityInstance src_instance = processInstance.findActivityInstanceByPK(srcActInst);
			GenericActivityEx act = (GenericActivityEx) src_instance.getActivity();
			String participantFilterMode =act.getParticipantFilterMode() ==null?null:(act.getParticipantFilterMode().getValue()==null?null:act.getParticipantFilterMode().getValue().toString());
		    if(participantFilterMode==null||participantFilterMode.equals(DefaultParticipantFilter.CODE_FREEORG4RESPONSIBILITY)){
		    	filterOrgs.add(billOrg);
		    }else if(participantFilterMode.equals(DefaultParticipantFilter.CODE_SPECIFYORG4RESPONSIBILITY)){
		    	filterOrgs.addAll(act.getOrgUnitTree());
		    }else if(participantFilterMode.equals(DefaultParticipantFilter.CODE_SAMEORG4RESPONSIBILITY)||participantFilterMode.equals(DefaultParticipantFilter.CODE_SUPERORG4RESPONSIBILITY)){
		    	filterOrgs.addAll(findSameOrg4Responsibility(processInstance,src_instance,billOrg));
		    }		    
		}
		return filterOrgs;
	}
		*/

	/**
	 * @param
	 * @param orgSystem 组织体系
	 * @throws BusinessException 
	 * */
		ArrayList<String> filterOrgs =new ArrayList<String>();
		/*
		private ArrayList<String> findSuperiorOrg4Responsibility(IProcessInstance processInstance,IActivityInstance actInst,String billOrg,String orgSystem) throws BusinessException{
		Vector<String> srcActInsts = actInst.getSrcActivityInstancePKs();
		for(String srcActInst :srcActInsts){
			ActivityInstance src_instance = processInstance.findActivityInstanceByPK(srcActInst);
			GenericActivityEx act = (GenericActivityEx) src_instance.getActivity();
			String participantFilterMode =act.getParticipantFilterMode() ==null?null:(act.getParticipantFilterMode().getValue()==null?null:act.getParticipantFilterMode().getValue().toString());
		    if(participantFilterMode==null||participantFilterMode.equals(DefaultParticipantFilter.CODE_FREEORG4RESPONSIBILITY)){
		    	filterOrgs.addAll(fetchSuperiorOrgs(new String[]{billOrg},orgSystem));
		    }else if(participantFilterMode.equals(DefaultParticipantFilter.CODE_SPECIFYORG4RESPONSIBILITY)){
		    	filterOrgs.addAll(fetchSuperiorOrgs(act.getOrgUnitTree()==null?null:(String[])act.getOrgUnitTree().toArray(new String[0]),orgSystem));
		    }else if(participantFilterMode.equals(DefaultParticipantFilter.CODE_SAMEORG4RESPONSIBILITY)||participantFilterMode.equals(DefaultParticipantFilter.CODE_SUPERORG4RESPONSIBILITY)){
		    	filterOrgs.addAll(findSuperiorOrg4Responsibility(processInstance,src_instance,billOrg,orgSystem));
		    }		    
		}
		return filterOrgs;
	}
	*/
	
	/**
	 * @param orgs 组织
	 * @param orgSystem 组织体系
	 * @throws BusinessException 
	 * */
	private ArrayList<String> fetchSuperiorOrgs(String[] orgs,String orgSystem) throws BusinessException {
		String[] superiorOrgs = NCLocator.getInstance().lookup(IFatherOrgPubQryService.class).queryFatherOrgByOrgTypeAndPK(orgSystem,orgs);	
		ArrayList<String> results =new ArrayList<String>();
		for(int start =0,end =superiorOrgs==null?0:superiorOrgs.length;start<end;start++){
			results.add(superiorOrgs[start]);
		}
		return results;
	}
}
