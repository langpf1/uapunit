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
 * �������������ʵ����
 * 
 * @author wzhy 2004-2-21
 * @modifier leijun 2006-4-7 ʹ�ö�̬�����Ʋ����ͷ�����
 * @modifier leijun 2008-8 ���ӹ�������ش���
 * @modifier leijun 2008-12 ������չ���������Ƿ�����װ��VO
 * @modifier guowl 2010-5 6.0����֧��һ�����׵�ģʽ
 */
public class WorkflowMachineImpl implements IWorkflowMachine {
	public static final String APP_FORMINFO = "APP_FORMINFO";

	public WorkflowMachineImpl() {
	}

	/**
	 * �Ƿ�Ϊ����У��Ķ�������
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
		Logger.debug("*���̼�� EngineService.checkWorkFlow��ʼ");
		Logger.debug("*********************************************");
		Logger.debug("* actionName=" + actionCode);
		Logger.debug("* billType=" + billType);
		Logger.debug("* billEntity=" + billVO);
		Logger.debug("* eParam=" + hmPfExParams);
		Logger.debug("*********************************************");

		long start = System.currentTimeMillis();
		// ���ж϶�������
		if (!isCheckAction(actionCode, billType))
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"wfMachineImpl-0000", null, new String[] { actionCode })/* ���Ϸ��ĵ��ݶ������� ={0} */);

		IPfBusinessLock pfLock = new PfBusinessLock();
		String strBillId = null;
		try {
			// ���ݼ�����һ����У��
			// �ж��Ƿ���Ҫ���� leijun+2008-12
			Object paramNoLock = hmPfExParams == null ? null : hmPfExParams.get(PfUtilBaseTools.PARAM_NO_LOCK);
			if (paramNoLock == null)
				pfLock.lock(new VOLockData(billVO, billType), new VOConsistenceCheck(billVO, billType));

			// �ж��Ƿ���Ҫ���¼���VO leijun+2008-12
			Object paramReloadVO = hmPfExParams == null ? null : hmPfExParams.get(PfUtilBaseTools.PARAM_RELOAD_VO);

			// XXX:guowl+2010-5 ����ǰ��ҵ������ȫVO
			// ���bd_billtype.checkclassnameע���ҵ����ʵ��
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
			// Ϊ���̶��帳ֵ
			Object paramDefPK = hmPfExParams == null ? null : hmPfExParams.get(PfUtilBaseTools.PARAM_FLOWPK);
			paraVO.m_flowDefPK = paramDefPK == null ? null : String.valueOf(paramDefPK);

			if (PfUtilBaseTools.isSaveAction(actionCode, billType)
					|| PfUtilBaseTools.isStartAction(actionCode, billType)) {
				// �ύ������,��������������鹤����
				return checkWorkflowWhenStart(paraVO);
			} else if (PfUtilBaseTools.isApproveAction(actionCode, billType)
					|| PfUtilBaseTools.isSignalAction(actionCode, billType)) {
				// ��������ת����������������鹤����
				return NCLocator.getInstance().lookup(IWFEngineService.class).checkUnfinishedWorkitem(paraVO.toWFAppParameter());
			}
			return null;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000004")/* ��鹤����ʱ�������ݿ��쳣 �� */
					+ ex.getMessage());
		} finally {
			Logger.debug("*���̼�� EngineService.checkWorkFlow��������ʱ" + (System.currentTimeMillis() - start) + "ms");
			// XXX::�����Ƴ�,�����ڴ�й©
			ActionEnvironment.getInstance().putParaVo(strBillId, null);
			// �ͷ���
			if (pfLock != null)
				pfLock.unLock();
		}
	}

	/**
	 * �ڶ���START ����������ʱ �����ã���ѯ������
	 * 
	 * @param paraVO
	 * @throws BusinessException
	 */
	private WorkflownoteVO checkWorkflowWhenStart(PfParameterVO paraVO) throws BusinessException {
		int status;
		BillStausUtil billStatus = new BillStausUtil();
		// ȡ���ݵĹ�����״̬������̬���ύ̬�������У�������ͨ����
		status = billStatus.queryBillStatus(paraVO.m_preValueVo);

		switch (status) {
		case IPfRetCheckInfo.COMMIT:
		case IPfRetCheckInfo.NOSTATE:
			return NCLocator.getInstance().lookup(IWFEngineService.class).getWorkflowItemsOnStart(paraVO.toWFAppParameter(), status);
		case IPfRetCheckInfo.GOINGON:
			// ������������,ȴ�ٴ�����,���ؿ�,����Ӱ������
			return null;
		case IPfRetCheckInfo.PASSING:
			// �������Ѿ�������ͨ����,ȴ�ٴ�����,���ؿ�,����Ӱ������
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
			// XXX::�����Ƴ�,�����ڴ�й©
			ActionEnvironment.getInstance().putParaVo(paraVo.m_billVersionPK, null);
			ActionEnvironment.getInstance().putMethodReturn(paraVo.m_billVersionPK, null);
		}
	}

	/**
	 * �ƶ�����������
	 */
	public int signalWorkflow(PfParameterVO paraVo) throws BusinessException {
		Logger.error("###WorkflowMachineImpl signalWorkflow ��ʼ " + System.currentTimeMillis() + "ms");
		ActionEnvironment.getInstance().putParaVo(paraVo.m_billVersionPK, paraVo);
		return NCLocator.getInstance().lookup(IWFEngineService.class).signalWorkflow(paraVo.toWFAppParameter());
	}

	public RetBackWfVo backCheckFlow(PfParameterVO paraVo) throws BusinessException {
		ActionEnvironment.getInstance().putParaVo(paraVo.m_billVersionPK, paraVo);
		ReturnBackWfVo returnBackWfVo = null;
		if (PfUtilBaseTools.isUnapproveAction(paraVo.m_actionName, paraVo.m_billType)
				|| PfUtilBaseTools.isRollbackAction(paraVo.m_actionName, paraVo.m_billType))
			// ���������󣬹���������
			returnBackWfVo = NCLocator.getInstance().lookup(IWFEngineService.class).rollbackWorkflow(paraVo.toWFAppParameter());
		else if (PfUtilBaseTools.isUnSaveAction(paraVo.m_actionName, paraVo.m_billType)
				|| PfUtilBaseTools.isRecallAction(paraVo.m_actionName, paraVo.m_billType))
			// �������ջ�,������ȡ���ύ
			returnBackWfVo = NCLocator.getInstance().lookup(IWFEngineService.class).cancelSubmitWorkflow(paraVo.toWFAppParameter());
		else
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"wfMachineImpl-0006")/* �Ƿ����ݶ���= */
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

	/*ǰ��ǩ*/
	public void beforeAddSign(WorkflownoteVO noteVO) throws BusinessException {
		NCLocator.getInstance().lookup(IWFEngineService.class).beforeAddSign(noteVO);
	}

	/*���ǩ*/
	public void afterAddSign(WorkflownoteVO noteVO) throws BusinessException {
		NCLocator.getInstance().lookup(IWFEngineService.class).afterAddSign(noteVO);
	}
	
	/*����*/
	public void delegateTask(WorkflownoteVO noteVo,List<String> turnUserPks) throws BusinessException {
		NCLocator.getInstance().lookup(IWFEngineService.class).delegateTask(noteVo, turnUserPks, InvocationInfoProxy.getInstance().getUserId());
	}
	/**
	 * web designer �ĵ������
	 * ��������/������������������ʱ�������칤����
	 * @param actionName    ��������
	 * @param billType      ��������PK
	 * @param hmPfExParams  ��չ����
	 * @return ������VO
	 * @author zhailzh 
	 * @throws PFBusinessException 
	 */
	public WorkflownoteVO webDesigercheckWorkFlow(WFAppParameter paraVo, HashMap hmPfExParams) throws PFBusinessException{
		long start = System.currentTimeMillis();
		String actionName = paraVo.getActionName();
		String billType = paraVo.getBillType();
		// ���ж϶�������
		if (!isCheckAction(actionName, billType))
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"wfMachineImpl-0000", null, new String[] { actionName })/* ���Ϸ��ĵ��ݶ������� ={0} */);
		try {
			if (PfUtilBaseTools.isSaveAction(actionName, billType)
					|| PfUtilBaseTools.isStartAction(actionName, billType)) {
				// �ύ������,��������������鹤����
				return NCLocator.getInstance().lookup(IWFEngineService.class).getWorkflowItemsOnStart(paraVo, IReturnCheckInfo.NOSTATE);
			} else if (PfUtilBaseTools.isApproveAction(actionName, billType)
					|| PfUtilBaseTools.isSignalAction(actionName, billType)) {
				// ��������ת����������������鹤����
				return NCLocator.getInstance().lookup(IWFEngineService.class).checkUnfinishedWorkitem(paraVo);
			}
			return null;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000004")/* ��鹤����ʱ�������ݿ��쳣 �� */
					+ ex.getMessage());
		} finally {
			Logger.debug("*���̼�� EngineService.checkWorkFlow��������ʱ" + (System.currentTimeMillis() - start) + "ms");
		}	
	}
}