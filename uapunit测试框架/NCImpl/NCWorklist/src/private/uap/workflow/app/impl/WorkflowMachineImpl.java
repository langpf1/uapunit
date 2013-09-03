package uap.workflow.app.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.pflock.IPfBusinessLock;
import nc.bs.pub.pflock.PfBusinessLock;
import nc.bs.pub.pflock.VOConsistenceCheck;
import nc.bs.pub.pflock.VOLockData;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import uap.workflow.admin.IWFEngineService;
import uap.workflow.admin.IWorkflowMachine;
import uap.workflow.app.action.IPFBusiAction;
import uap.workflow.app.client.PfUtilTools;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.app.extend.action.IPfBeforeAction;
import uap.workflow.app.extend.bizstate.BillStausUtil;
import uap.workflow.app.vo.IPfRetCheckInfo;
import uap.workflow.app.vo.RetBackWfVo;
import uap.workflow.pub.util.PfUtilBaseTools;
import uap.workflow.vo.IReturnCheckInfo;
import uap.workflow.vo.ReturnBackWfVo;
import uap.workflow.vo.WFAppParameter;
import uap.workflow.vo.WorkflownoteVO;

/**
 * 工作流引擎服务实现类
 * 
 * @author wzhy 2004-2-21
 * @modifier leijun 2006-4-7 使用动态锁机制不需释放锁了
 * @modifier leijun 2008-8 增加工作流相关处理
 * @modifier leijun 2008-12 根据扩展参数决定是否重新装载VO
 * @modifier guowl 2010-5 6.0不再支持一弃到底的模式
 */
public class WorkflowMachineImpl implements IWorkflowMachine {
	public static final String APP_FORMINFO = "APP_FORMINFO";

	public WorkflowMachineImpl() {
	}

	/**
	 * 是否为触发校验的动作编码
	 * 
	 * @param actionName
	 * @param billType
	 * @return
	 */
	private boolean isCheckAction(String actionName, String billType) {
		if (PfUtilBaseTools.isStartFlowAction(actionName, billType)
				|| PfUtilBaseTools.isSignalFlowAction(actionName, billType))
			return true;
		return false;
	}

	public WorkflownoteVO checkWorkFlow(String actionCode, String billType, AggregatedValueObject billVO,
			HashMap hmPfExParams) throws BusinessException {
		Logger.init("workflow");
		Logger.debug("*流程检查 EngineService.checkWorkFlow开始");
		Logger.debug("*********************************************");
		Logger.debug("* actionName=" + actionCode);
		Logger.debug("* billType=" + billType);
		Logger.debug("* billEntity=" + billVO);
		Logger.debug("* eParam=" + hmPfExParams);
		Logger.debug("*********************************************");

		long start = System.currentTimeMillis();
		// 先判断动作编码
		if (!isCheckAction(actionCode, billType))
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"wfMachineImpl-0000", null, new String[] { actionCode })/* 不合法的单据动作编码 ={0} */);

		IPfBusinessLock pfLock = new PfBusinessLock();
		String strBillId = null;
		try {
			// 单据加锁和一致性校验
			// 判定是否需要加锁 leijun+2008-12
			Object paramNoLock = hmPfExParams == null ? null : hmPfExParams.get(PfUtilBaseTools.PARAM_NO_LOCK);
			if (paramNoLock == null)
				pfLock.lock(new VOLockData(billVO, billType), new VOConsistenceCheck(billVO, billType));

			// 判定是否需要重新加载VO leijun+2008-12
			Object paramReloadVO = hmPfExParams == null ? null : hmPfExParams.get(PfUtilBaseTools.PARAM_RELOAD_VO);

			// XXX:guowl+2010-5 动作前的业务处理，补全VO
			// 获得bd_billtype.checkclassname注册的业务类实例
			Object checkObj = PfUtilTools.getBizRuleImpl(billType);
			AggregatedValueObject completeVO = billVO;
			if (checkObj instanceof IPfBeforeAction) {
				completeVO = ((IPfBeforeAction) checkObj).beforeAction(billVO, null, hmPfExParams);
			}

			Hashtable hashBilltypeToParavo = new Hashtable();
			PfParameterVO paraVO = PfUtilBaseTools.getVariableValue(billType, actionCode, completeVO, null, null,
					null, null, hmPfExParams, hashBilltypeToParavo);
			strBillId = paraVO.m_billVersionPK;
			ActionEnvironment.getInstance().putParaVo(strBillId, paraVO);
			// 为流程定义赋值
			Object paramDefPK = hmPfExParams == null ? null : hmPfExParams.get(PfUtilBaseTools.PARAM_FLOWPK);
			paraVO.m_flowDefPK = paramDefPK == null ? null : String.valueOf(paramDefPK);

			if (PfUtilBaseTools.isSaveAction(actionCode, billType)
					|| PfUtilBaseTools.isStartAction(actionCode, billType)) {
				// 提交审批流,启动工作流，检查工作项
				return checkWorkflowWhenStart(paraVO);
			} else if (PfUtilBaseTools.isApproveAction(actionCode, billType)
					|| PfUtilBaseTools.isSignalAction(actionCode, billType)) {
				// 工作流流转，审批流审批，检查工作项
				return NCLocator.getInstance().lookup(IWFEngineService.class).checkUnfinishedWorkitem(paraVO.toWFAppParameter());
			}
			return null;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000004")/* 检查工作项时出现数据库异常 ： */
					+ ex.getMessage());
		} finally {
			Logger.debug("*流程检查 EngineService.checkWorkFlow结束，耗时" + (System.currentTimeMillis() - start) + "ms");
			// XXX::必须移除,否则内存泄漏
			ActionEnvironment.getInstance().putParaVo(strBillId, null);
			// 释放锁
			if (pfLock != null)
				pfLock.unLock();
		}
	}

	/**
	 * 在动作START 启动工作流时 被调用，查询工作项
	 * 
	 * @param paraVO
	 * @throws BusinessException
	 */
	private WorkflownoteVO checkWorkflowWhenStart(PfParameterVO paraVO) throws BusinessException {
		int status;
		BillStausUtil billStatus = new BillStausUtil();
		// 取单据的工作流状态：自由态，提交态，进行中，结束（通过）
		status = billStatus.queryBillStatus(paraVO.m_preValueVo);

		switch (status) {
		case IPfRetCheckInfo.COMMIT:
		case IPfRetCheckInfo.NOSTATE:
			return NCLocator.getInstance().lookup(IWFEngineService.class).getWorkflowItemsOnStart(paraVO.toWFAppParameter(), status);
		case IPfRetCheckInfo.GOINGON:
			// 工作流进行中,却再次启动,返回空,即不影响流程
			return null;
		case IPfRetCheckInfo.PASSING:
			// 工作流已经结束（通过）,却再次启动,返回空,即不影响流程
			return null;
		default:
			return null;
		}
	}

	@Override
	public boolean[] sendWorkFlowOnSave_RequiresNew(PfParameterVO paraVo, Hashtable returnHas, HashMap hmPfExParams)
			throws BusinessException {
		Logger.debug("BILLNO**********" + paraVo.m_billNo + "**********");
		Logger.debug("BILLID**********" + paraVo.m_billVersionPK + "**********");

		ActionEnvironment.getInstance().putParaVo(paraVo.m_billVersionPK, paraVo);
		ActionEnvironment.getInstance().putMethodReturn(paraVo.m_billVersionPK, returnHas);
		try {
			return NCLocator.getInstance().lookup(IWFEngineService.class).startWorkflow(paraVo.toWFAppParameter(), hmPfExParams);
		} finally {
			// XXX::必须移除,否则内存泄漏
			ActionEnvironment.getInstance().putParaVo(paraVo.m_billVersionPK, null);
			ActionEnvironment.getInstance().putMethodReturn(paraVo.m_billVersionPK, null);
		}
	}

	/**
	 * 推动流程往下走
	 */
	public int signalWorkflow(PfParameterVO paraVo) throws BusinessException {
		Logger.error("###WorkflowMachineImpl signalWorkflow 开始 " + System.currentTimeMillis() + "ms");
		ActionEnvironment.getInstance().putParaVo(paraVo.m_billVersionPK, paraVo);
		return NCLocator.getInstance().lookup(IWFEngineService.class).signalWorkflow(paraVo.toWFAppParameter());
	}

	public RetBackWfVo backCheckFlow(PfParameterVO paraVo) throws BusinessException {
		ActionEnvironment.getInstance().putParaVo(paraVo.m_billVersionPK, paraVo);
		ReturnBackWfVo returnBackWfVo = null;
		if (PfUtilBaseTools.isUnapproveAction(paraVo.m_actionName, paraVo.m_billType)
				|| PfUtilBaseTools.isRollbackAction(paraVo.m_actionName, paraVo.m_billType))
			// 审批流弃审，工作流回退
			returnBackWfVo = NCLocator.getInstance().lookup(IWFEngineService.class).rollbackWorkflow(paraVo.toWFAppParameter());
		else if (PfUtilBaseTools.isUnSaveAction(paraVo.m_actionName, paraVo.m_billType)
				|| PfUtilBaseTools.isRecallAction(paraVo.m_actionName, paraVo.m_billType))
			// 审批流收回,工作流取消提交
			returnBackWfVo = NCLocator.getInstance().lookup(IWFEngineService.class).cancelSubmitWorkflow(paraVo.toWFAppParameter());
		else
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"wfMachineImpl-0006")/* 非法单据动作= */
					+ paraVo.m_actionName);
		return RetBackWfVo.fromReturnBackWfVo(returnBackWfVo);
	}

	public void deleteCheckFlow(String billType, String billId, AggregatedValueObject billVO, String checkMan)
			throws BusinessException {
		// xry TODO:
	}

	public Object processSingleBillFlow_RequiresNew(String actionName, String billOrTranstype,
			WorkflownoteVO workflowVo, AggregatedValueObject billvo, Object userObj, HashMap param)
			throws BusinessException {
		return NCLocator.getInstance().lookup(IPFBusiAction.class)
				.processAction(actionName, billOrTranstype, workflowVo, billvo, userObj, param);
	}

	public String findParentProcessInstancePK(String subProcessInstancePK) throws DAOException {
		return "";
	}

	public void appointWorkitem(String billId, String pk_workflownote,
			String checkman, String userID) throws BusinessException {

	}

	/*前加签*/
	public void beforeAddSign(WorkflownoteVO noteVO) throws BusinessException {
		NCLocator.getInstance().lookup(IWFEngineService.class).beforeAddSign(noteVO);
	}

	/*后加签*/
	public void afterAddSign(WorkflownoteVO noteVO) throws BusinessException {
		NCLocator.getInstance().lookup(IWFEngineService.class).afterAddSign(noteVO);
	}
	
	/*改派*/
	public void delegateTask(WorkflownoteVO noteVo,List<String> turnUserPks) throws BusinessException {
		NCLocator.getInstance().lookup(IWFEngineService.class).delegateTask(noteVo, turnUserPks, InvocationInfoProxy.getInstance().getUserId());
	}
	/**
	 * web designer 的单据入口
	 * 单据启动/驱动审批流、工作流时，检查待办工作项
	 * @param actionName    动作编码
	 * @param billType      单据类型PK
	 * @param hmPfExParams  扩展参数
	 * @return 工作项VO
	 * @author zhailzh 
	 * @throws PFBusinessException 
	 */
	public WorkflownoteVO webDesigercheckWorkFlow(WFAppParameter paraVo, HashMap hmPfExParams) throws PFBusinessException{
		long start = System.currentTimeMillis();
		String actionName = paraVo.getActionName();
		String billType = paraVo.getBillType();
		// 先判断动作编码
		if (!isCheckAction(actionName, billType))
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"wfMachineImpl-0000", null, new String[] { actionName })/* 不合法的单据动作编码 ={0} */);
		try {
			if (PfUtilBaseTools.isSaveAction(actionName, billType)
					|| PfUtilBaseTools.isStartAction(actionName, billType)) {
				// 提交审批流,启动工作流，检查工作项
				return NCLocator.getInstance().lookup(IWFEngineService.class).getWorkflowItemsOnStart(paraVo, IReturnCheckInfo.NOSTATE);
			} else if (PfUtilBaseTools.isApproveAction(actionName, billType)
					|| PfUtilBaseTools.isSignalAction(actionName, billType)) {
				// 工作流流转，审批流审批，检查工作项
				return NCLocator.getInstance().lookup(IWFEngineService.class).checkUnfinishedWorkitem(paraVo);
			}
			return null;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000004")/* 检查工作项时出现数据库异常 ： */
					+ ex.getMessage());
		} finally {
			Logger.debug("*流程检查 EngineService.checkWorkFlow结束，耗时" + (System.currentTimeMillis() - start) + "ms");
		}	
	}
}