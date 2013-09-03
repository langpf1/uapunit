package uap.workflow.app.action.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uap.workflow.admin.IWorkflowMachine;
import uap.workflow.app.action.IPFBusiAction;
import uap.workflow.app.action.IplatFormEntry;
import uap.workflow.app.client.PfUtilTools;
import uap.workflow.app.exception.PFBatchExceptionInfo;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.app.extend.PfUtilDMO;
import uap.workflow.app.extend.action.IPfAfterAction;
import uap.workflow.app.extend.action.IPfBeforeAction;
import uap.workflow.app.vo.PfProcessBatchRetObject;
import uap.workflow.pub.util.PfUtilBaseTools;
import uap.workflow.vo.WorkflownoteVO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pflock.PfBusinessLock;
import nc.bs.pub.pflock.VOConsistenceCheck;
import nc.bs.pub.pflock.VOLockData;
import nc.bs.pub.pflock.VOsConsistenceCheck;
import nc.bs.pub.pflock.VOsLockData;
import nc.itf.uap.pf.IPFConfig;
import nc.jdbc.framework.exception.DbException;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.PfUtilActionVO;
import nc.vo.sm.UserVO;

/**
 * ƽ̨��ڵ�ʵ����.
 * 
 * @author fangj
 * @modifier leijun 2008-2-1 �����ӿڷ�����ʵ��
 * @modifier leijun 2008-12 ������չ���������Ƿ�����װ��VO
 */
public class PlatFormEntryImpl implements IplatFormEntry {

	public Object processAction(String actionName, String billType, WorkflownoteVO worknoteVO,
			AggregatedValueObject billvo, Object userObj, HashMap hmPfExParams) throws BusinessException {
		Logger.debug("******����PlatFormEntryImpl.processAction����******************");
		Logger.debug("* actionName=" + actionName);
		Logger.debug("* billType=" + billType);
		
		PfBusinessLock pfLock = null;
		try {
			// ������һ���Խ���
			pfLock = new PfBusinessLock();
			Object paramNoLock = hmPfExParams == null ? null : hmPfExParams.get(PfUtilBaseTools.PARAM_NO_LOCK);
			if (paramNoLock == null)
			   pfLock.lock(new VOLockData(billvo, billType), new VOConsistenceCheck(billvo, billType));

			/* xry TODO:�������Ƶ����ڳ�������ݻ�Զ��ж���
			// yanke1+ 2011-11-12 bugfix for Portal
			// ���ϱ�������������ʱ����ѡ������Ƶ����ڣ�portal���޷���task��setBackToFirstActivity��Ϊtrue
			// �˴�ͨ���ж�task��jumpToActivity�Ƿ�Ϊ�Ƶ�������Ϊtask��setBackToFirstActivity������ȷ��ֵ
			if (worknoteVO != null && worknoteVO.getTaskInstanceVO() !=null 
					&& worknoteVO.getTaskInstanceVO().getTask() != null) {
				
				TaskInstanceVO task = worknoteVO.getTaskInstanceVO();
				
				if (task.getCreate_type() == TaskInstanceCreateType.Reject.getIntValue()) {
					String backTo = task..getJumpToActivity();

					try {
						IProcessDefinition wp = PfDataCache.getWorkflowProcess(task.getPk_Process_Def());

						IActivity activity = wp.findActivity(backTo);
						if (wp.findStartActivity().getId().equals(backTo)) {
							task.setBackToFirstActivity(true);
						}
					} catch (XPDLParserException e) {
						throw new BusinessException(e);
					}
				}
			}
			*/
			
			//�ж��Ƿ���Ҫ���¼���VO leijun+2008-12
			Object paramReloadVO = null;//hmPfExParams == null ? null : hmPfExParams.get(PfUtilBaseTools.PARAM_RELOAD_VO);
			AggregatedValueObject reloadvo = billvo;
			if (paramReloadVO != null) {
				String billId = billvo.getParentVO().getPrimaryKey();
				reloadvo = null;//new PFConfigImpl().queryBillDataVO(billType, billId);
				if (reloadvo == null)
					throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PlatFormEntryImpl-0000", null, new String[]{billType,billId})/*���󣺸��ݵ�������{0}�͵���{1}��ȡ�������ݾۺ�VO*/);
				hmPfExParams.remove(PfUtilBaseTools.PARAM_RELOAD_VO);
			}

			//XXX:leijun+2010-3 ����ǳ�Ĭ���������̨���»�ȡ������
			Object paramSilent = hmPfExParams == null ? null : hmPfExParams.get(PfUtilBaseTools.PARAM_SILENTLY);
			if (worknoteVO == null
					&& paramSilent != null
					&& (PfUtilBaseTools.isApproveAction(actionName, billType) 
							|| PfUtilBaseTools.isSignalAction(actionName, billType))) {
				worknoteVO = NCLocator.getInstance().lookup(IWorkflowMachine.class).checkWorkFlow(
						actionName, billType, reloadvo, hmPfExParams);
			}
			
			//XXX:guowl+2010-5 ����ǰ��ҵ����
			//���bd_billtype.checkclassnameע���ҵ����ʵ��
			Object checkObj = PfUtilTools.getBizRuleImpl(billType);
			AggregatedValueObject completeVO = reloadvo;
			AggregatedValueObject cloneVO = reloadvo;
			if (checkObj instanceof IPfBeforeAction) {
				completeVO = ((IPfBeforeAction) checkObj).beforeAction(reloadvo, userObj, hmPfExParams);
				AggregatedValueObject[] tmpAry = ((IPfBeforeAction) checkObj).getCloneVO();
				if(tmpAry != null && tmpAry.length > 0)
					cloneVO = tmpAry[0];
			}
			
			// ��������
			Object retObjAfterAction = NCLocator.getInstance().lookup(IPFBusiAction.class).processAction(
					actionName, billType, worknoteVO, completeVO, userObj, hmPfExParams);

			//XXX:leijun+2010-2 �������ҵ����
			if (checkObj instanceof IPfAfterAction) {
				retObjAfterAction = ((IPfAfterAction) checkObj).afterAction(cloneVO, retObjAfterAction, hmPfExParams);
			}
			Logger.debug("******�뿪PlatFormEntryImpl.processAction����******************");
			return retObjAfterAction;
		} finally {
			if (pfLock != null)
				// �ͷ���
				pfLock.unLock();
		}
	}
	
	public Object processBatch(String actionName, String billType, WorkflownoteVO worknoteVO,
			AggregatedValueObject[] billvos, Object[] userObjAry, HashMap hmPfExParams)
			throws BusinessException {
		PfBusinessLock pfLock = null;
		try {
			// ������һ���Խ���
			pfLock = new PfBusinessLock();
			pfLock.lock(new VOsLockData(billvos, billType), new VOsConsistenceCheck(billvos, billType));
			
			/* xry TODO:�������Ƶ����ڳ�������ݻ�Զ��ж���
			// yanke1+ 2011-11-12 bugfix for Portal
			// ���ϱ�������������ʱ����ѡ������Ƶ����ڣ�portal���޷���task��setBackToFirstActivity��Ϊtrue
			// �˴�ͨ���ж�task��jumpToActivity�Ƿ�Ϊ�Ƶ�������Ϊtask��setBackToFirstActivity������ȷ��ֵ
			//
			if (worknoteVO != null && worknoteVO.getTaskInstanceVO() !=null 
					&& worknoteVO.getTaskInstanceVO().getTask() != null) {
				
				WFTask task = worknoteVO.getTaskInstanceVO().getTask();
				
				if (task.getTaskType() == WfTaskType.Backward.getIntValue()) {
					String backTo = task.getJumpToActivity();

					try {
						WorkflowProcess wp = PfDataCache.getWorkflowProcess(task.getWfProcessDefPK());

						Activity activity = wp.findActivityByID(backTo);
						if (wp.findStartActivity().getId().equals(backTo)) {
							task.setBackToFirstActivity(true);
						}
					} catch (XPDLParserException e) {
						throw new BusinessException(e);
					}
				}
			}
			*/

			//�ж��Ƿ���Ҫ���¼���VO leijun+2008-12
			Object paramReloadVO = hmPfExParams == null ? null : hmPfExParams
					.get(PfUtilBaseTools.PARAM_RELOAD_VO);
			AggregatedValueObject[] reloadvos = billvos;
			if (paramReloadVO != null) {
				//AggregatedValueObject[] reloadVOs = new AggregatedValueObject[billvos.length];
				reloadvos = (AggregatedValueObject[]) java.lang.reflect.Array.newInstance(billvos[0]
						.getClass(), billvos.length);

				for (int i = 0; i < billvos.length; i++) {
					//FIXME:ѭ����ȡÿ������VO��Ч�ʽϲ
					String billId = billvos[i].getParentVO().getPrimaryKey();
					reloadvos[i] = null;//new PFConfigImpl().queryBillDataVO(billType, billId);
					if (reloadvos[i] == null)
						throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PlatFormEntryImpl-0000", null, new String[]{billType,billId})/*���󣺸��ݵ�������{0}�͵���{1}��ȡ�������ݾۺ�VO*/);
				}

				hmPfExParams.remove(PfUtilBaseTools.PARAM_RELOAD_VO);
			}
			
		  //XXX:guowl+2010-5 ����ǰ��ҵ����
			//���bd_billtype.checkclassnameע���ҵ����ʵ��
			Object checkObj = PfUtilTools.getBizRuleImpl(billType);
			AggregatedValueObject[] completeVOs = reloadvos;
			AggregatedValueObject[] cloneVOs = reloadvos;
			if (checkObj instanceof IPfBeforeAction) {
				completeVOs = ((IPfBeforeAction) checkObj).beforeBatch(billvos, userObjAry, hmPfExParams);
				cloneVOs = ((IPfBeforeAction) checkObj).getCloneVO();
			}

			//XXX:leijun+2010-3 ����ǳ�Ĭ���������̨���»�ȡ������
			Object paramSilent = hmPfExParams == null ? null : hmPfExParams
					.get(PfUtilBaseTools.PARAM_SILENTLY);
			if (worknoteVO == null
					&& paramSilent != null
					&& (PfUtilBaseTools.isSignalFlowAction(actionName, billType))) {
				worknoteVO = NCLocator.getInstance().lookup(IWorkflowMachine.class).checkWorkFlow(
						actionName, billType, completeVOs[0], hmPfExParams);
			}
			
			// ����������
			PFBatchExceptionInfo batchExceptionInfo = new PFBatchExceptionInfo();
			Object[] retObjsAfterAction = NCLocator.getInstance().lookup(IPFBusiAction.class)
					.processBatch(actionName, billType, completeVOs, userObjAry, worknoteVO, hmPfExParams, batchExceptionInfo);

			//XXX:leijun+2010-2 ���������ҵ����
			//���bd_billtype.checkclassnameע���ҵ����ʵ��
			if (checkObj instanceof IPfAfterAction) {
				retObjsAfterAction = ((IPfAfterAction) checkObj).afterBatch(cloneVOs, retObjsAfterAction, hmPfExParams);
			}

			return new PfProcessBatchRetObject(retObjsAfterAction, batchExceptionInfo);
		} finally {
			if (pfLock != null)
				// �ͷ���
				pfLock.unLock();
		}
	}
	
/* xry TODO:
	
	public UserVO[] queryValidCheckers(String billId, String billType) throws BusinessException {
		WorknoteManager noteMgr = new WorknoteManager();
		try {
			return noteMgr.queryValidCheckers(billId, billType);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PlatFormEntryImpl-0001", null, new String[]{e.getMessage()})/*��ѯ��Ч������ʱ�������ݿ��쳣��{0}* /);
		}
	}
	*/

	public PfUtilActionVO[] getActionDriveVOs(String billType, String busiType, String pkCorp,
			String actionName) throws BusinessException {
		PfUtilActionVO[] driveActions = null;
		try {
			PfUtilDMO dmo = new PfUtilDMO();
			driveActions = dmo.queryDriveAction(billType, busiType, pkCorp, actionName, null);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PlatFormEntryImpl-0002", null, new String[]{e.getMessage()})/*��ѯ�������Ķ���ʱ�������ݿ��쳣��{0}*/);
		}
		return driveActions;
	}

	@Override
	public Object processBatch(String actionName, WorkflownoteVO worknoteVO, String[] billTypes, String[] billIds)
			throws BusinessException {

		List<Object> retList = new ArrayList<Object>();

		IPFConfig pfConf = NCLocator.getInstance().lookup(IPFConfig.class);

		// ���ҵ���vo�����յ������ͽ��з���
		Map<String, List<AggregatedValueObject>> billVOMap = new HashMap<String, List<AggregatedValueObject>>();
		for (int i = 0; i < billTypes.length; i++) {
			String billType = billTypes[i];
			String billId = billIds[i];

			AggregatedValueObject billvo = pfConf.queryBillDataVO(billType, billId);

			if (!billVOMap.containsKey(billType)) {
				List<AggregatedValueObject> list = new ArrayList<AggregatedValueObject>();
				billVOMap.put(billType, list);
			}

			List<AggregatedValueObject> list = billVOMap.get(billType);
			list.add(billvo);
		}

		HashMap param = new HashMap();
		param.put(PfUtilBaseTools.PARAM_BATCH, PfUtilBaseTools.PARAM_BATCH);

		for (Iterator<String> it = billVOMap.keySet().iterator(); it.hasNext();) {

			// ��ÿ�ֵ��������µĵ��ݽ�����������
			String billtype = it.next();

			WorkflownoteVO currNote = (WorkflownoteVO) worknoteVO.clone();
			HashMap currParam = (HashMap) param.clone();

			currParam.put(PfUtilBaseTools.PARAM_WORKNOTE, currNote);

			List<AggregatedValueObject> list = billVOMap.get(billtype);
			Object ret = processBatch(actionName, billtype, currNote, list.toArray(new AggregatedValueObject[0]), null,
					currParam);

			retList.add(ret);
		}

		return retList;
	}

}
