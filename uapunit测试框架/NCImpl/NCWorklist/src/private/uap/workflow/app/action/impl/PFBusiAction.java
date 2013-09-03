package uap.workflow.app.action.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import uap.workflow.admin.IWorkflowMachine;
import uap.workflow.app.action.IPFActionName;
import uap.workflow.app.action.IPFBusiAction;
import uap.workflow.app.client.PfUtilTools;
import uap.workflow.app.exception.PFBatchExceptionInfo;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.app.extend.PfUtilDMO;
import uap.workflow.app.extend.action.IBusiBillStatusCallBack;
import uap.workflow.app.extend.action.IWorkflowBatch;
import uap.workflow.app.extend.action.JumpStatusCallbackContext;
import uap.workflow.app.extend.action.PFRunClass;
import uap.workflow.app.impl.WorkflowMachineImpl;
import uap.workflow.app.message.PFMessageImpl;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.core.WorkflowTypeEnum;
import uap.workflow.pub.app.message.vo.MessageTypes;
import uap.workflow.pub.app.message.vo.MessageinfoVO;
import uap.workflow.pub.util.PfDataCache;
import uap.workflow.pub.util.PfUtilBaseTools;
import uap.workflow.pub.util.SingleBillFlowTask;
import uap.workflow.pub.util.WfInstancePool;
import uap.workflow.vo.WorkflownoteVO;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.BillTypeCacheKey;
import nc.itf.uap.pf.IPFExptLog;
import nc.jdbc.framework.exception.DbException;
import nc.message.vo.AttachmentVO;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pf.exptlog.PfExptLogVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.billtype2.Billtype2VO;
import nc.vo.pub.billtype2.ExtendedClassEnum;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pf.IPFSourceBillFinder;
import nc.vo.pub.pf.PfUtilActionVO;
import nc.vo.pub.pf.Pfi18nTools;
import nc.vo.pub.pf.SourceBillInfo;
import nc.vo.pub.pfflow01.BillbusinessVO;
import nc.vo.pub.pfflow04.BackmsgVO;
import nc.vo.pub.workflownote.WorkflownoteAttVO;

/**
 * ����ƽ̨�Ķ���������,ԭPfUtilBOת��ΪProxy
 * 
 * @author fangj 2005-1-24 16:02
 * @modifier leijun 2005-3-10 �޸ķ���startApproveFlowAfterAction(),�ٴλ�ȡbillNo
 * @modifier leijun 2005-7-11 �޸ķ���actiondrive()�������������ʱ��ȡ����Ա��Bug
 * @modifier zsb 2006-4-30 ���ݲ���Ա��ѯ��������������������ˣ������ڶ���������ѯ������ֱ�ӹ���
 * @modifier leijun 2006-5-30 ���������󣬷���"��ʽ"��Ϣ
 * @modifier leijun 2006-7-11 �����Ա��������Ϊ��������״̬Bean
 */
public class PFBusiAction implements IPFBusiAction {

	public PFBusiAction() {
		super();
	}
	
	/**
	 * ִ�������ж���������PfParameterVO��Ϣ������ҵ�����̵���ת
	 * */
	private PfParameterVO lastParamVO =null;

	/**
	 * ���� ����������
	 * <li>����SAVE��EDIT����,�������»�ȡ����No�͵���Id
	 * @param billtype
	 * @param billVO            ����VO
	 * @param userObj           �û�����
	 * @param retObj            �����ű�ִ�к�ķ���ֵ
	 * @param eParam            ��չ����
	 * @param hashBilltypeToParavo ����VO�뵥������ӳ��
	 * @param hashMethodReturn
	 * @return
	 * @throws BusinessException
	 */
	private Object[] startWorkflowAfterAction(String billtype, AggregatedValueObject billVO, Object userObj, Object retObj, HashMap eParam, Hashtable hashBilltypeToParavo, Hashtable hashMethodReturn,String src_billtypePK)
			throws BusinessException {
		Logger.debug("************���� ����������*************");
		boolean isNeedStart = true;
		boolean bStarted = false;
		Object noApprove = eParam == null ? null : eParam.get(PfUtilBaseTools.PARAM_NOFLOW);
		if (noApprove != null) {
			Logger.debug(">>PARAM_NOFLOW");
			isNeedStart = false;
		}

		String startupTrace = "************��������������*************";
		if (isNeedStart) {
			PfParameterVO paraVo = (PfParameterVO) hashBilltypeToParavo.get(billtype+src_billtypePK);
			if(paraVo==null){
				//���漴�ύ�ĵ��ݴ�ʱ������û�����ɣ���˶�Ӧ��key�ǵ������ͱ��롣
				paraVo = (PfParameterVO) hashBilltypeToParavo.get(billtype);
			}
			PfUtilBaseTools.fetchBillId(paraVo, billVO, retObj);

			if (paraVo.m_billVersionPK != null/* && paraVo.m_billNo != null*/) {
				// ֻ�е���id�͵��ݺŶ���Ϊ��,������������
				//try {
				boolean[] wfRet = NCLocator.getInstance().lookup(IWorkflowMachine.class).sendWorkFlowOnSave_RequiresNew(paraVo, hashMethodReturn, eParam);
				bStarted = wfRet[0]; 
				//xry 2011.6.28 �쳣��ʽ��ֹ������ִ�����̣����һع������񣬵�������ʵ����Ϣû�д浽���ݿ�
				//} catch (ApproveAfterCommitException e) {
				if(wfRet[1]){
					//try
					//{
						// �ύ������ͨ�� ��־λ
						paraVo.m_autoApproveAfterCommit = true;
						
						// ִ��ҵ�����
						retObj = actionOnStep(IPFActionName.APPROVE, paraVo);
						// ִ����������
						//xry TODO:actiondrive(billVO, userObj, hashBilltypeToParavo, hashMethodReturn, paraVo, eParam);
						bStarted = true;
					//}
					//catch(Exception e)
					//{
					//	NCLocator.getInstance().lookup(IWorkflowMachine.class).reCallFlow_RequiresNew(paraVo,WorkflowTypeEnum.Approveflow.getIntValue());
					//	throw new BusinessException(e.getMessage());
					//}
				}
			}
			if (bStarted){
				startupTrace = "************�������ɹ�����*************";
			} else {
				startupTrace = "************����id�򵥾ݺ�Ϊ��,��������������*************";
			}
		}
		Logger.warn(startupTrace);
		return new Object[] {bStarted,retObj};
	}

	/**
	 * ��������ʱ�������˷�����Ϣ
	 * @throws BusinessException 
	 * */
	private void sendMessageWhenStartWorkflow(PfParameterVO paraVo,int iworkflowtype) throws BusinessException{
		/* xry ��Ҫ TODO:  
		WorknoteManager manager = new WorknoteManager();
		String processId = manager.getProcessId(paraVo,iworkflowtype);
		if(StringUtil.isEmptyWithTrim(processId)){
			return;
		}		
		manager.sendMsgWhenWFstateChanged(paraVo, processId,WfTaskOrInstanceStatus.Started.getIntValue(),iworkflowtype);
		*/
	}

	/**
	 * ����ִ��ǰ������������
	 */
	private void deleteWorkFlow(PfParameterVO paraVo) throws BusinessException {
		// ɾ������
		new WorkflowMachineImpl().deleteCheckFlow(paraVo.m_billType, paraVo.m_billVersionPK, paraVo.m_preValueVo, paraVo.m_operator);
	}

	/**
	 * �жϵ�ǰ�����Ƿ�Ϊ�õ������͵Ľ�������
	 * <li>FIXME:���Ƿ�Ϊ�õ������͵�����������
	 * 
	 * @param actionName
	 * @return
	 * @throws PFBusinessException
	 */
	private boolean isDriveAction(String pkBilltype, String actionName) throws PFBusinessException {
		// ���巵�ر���
		boolean retflag = true;
		Logger.debug("****�ж϶���" + actionName + "�Ƿ��������isDriveAction��ʼ****");
		try {
			PfUtilDMO dmo = new PfUtilDMO();
			String realBilltype = PfUtilBaseTools.getRealBilltype(pkBilltype);
			retflag = dmo.queryLastStep(realBilltype, actionName);
			Logger.debug("==" + (retflag ? "��" : "����") + "��������==");
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
		Logger.debug("****�ж϶���" + actionName + "�Ƿ��������isDriveAction����****");
		return retflag;
	}

	/**
	 * ʵ���������ű��࣬��ִ��
	 * @param actionName ��������,����"SAVE","EDIT"
	 * @param paraVo
	 * @return
	 */
	private Object actionOnStep(String actionName, PfParameterVO paraVo) throws BusinessException {
		// ƽ̨��־
		Logger.debug(">>>PFBusiAction.actionOnStep(" + actionName + "," + paraVo.m_billType + ") ��ʼ<<<");
		long begin = System.currentTimeMillis();

		// �������ز���
		Object actionReturnObj = null;

		/* ֧�ֵ����������桢���,��Ϊ����ʱ��������������Ϣ,���������ʱ���������� */
		if (paraVo.m_billVersionPK == null && paraVo.m_preValueVo != null) {
			paraVo.m_billVersionPK = paraVo.m_preValueVo.getParentVO().getPrimaryKey();
			Logger.debug("*********�����������桢���(�������ʱ��������)*****");
		}
		actionReturnObj = new PFRunClass().runComBusi(paraVo, UFBoolean.FALSE, actionName);

		long end = System.currentTimeMillis();
		Logger.info(">>>PFBusiAction.actionOnStep(" + actionName + "," + paraVo.m_billType + ") ����,��ʱ=" + (end - begin) + "ms<<<");
		return actionReturnObj;
	}

	/**  xry TODO:
	 * ִ�ж�������
	 * <li>Դ��������+Դ���������ƶ���������͵Ķ������
	 * <li>���ĳĿ�ĵ������ʹ���SAVE/EDIT����,�Ӷ�����������������,����ִ�иõ������͵���������
	 * 
	 * @param srcBillVO
	 * @param userObj
	 * @param hashBilltypeToParavo
	 * @param hashMethodReturn
	 * @param srcParaVo
	 * @param eParam
	 * @return
	 * @throws BusinessException
	 * /
	private boolean actiondrive(AggregatedValueObject srcBillVO, Object userObj, Hashtable hashBilltypeToParavo, Hashtable hashMethodReturn, PfParameterVO srcParaVo, HashMap eParam)
			throws BusinessException {
		Logger.debug("*********ִ�ж�������actiondrive��ʼ********");
		// 1.��õ�ǰ���ݶ����Ŀ���������
		PfUtilActionVO[] drivedActions = queryActionDriveVOs(srcParaVo);
		String srcBilltype = srcParaVo.m_billType;

		if (drivedActions == null || drivedActions.length == 0) {
			Logger.debug("�ö���=" + srcParaVo.m_actionName + "û�п������Ķ���");
			return false;
		}

		// ��ִ�е���������
		LinkedHashSet<String> hsFinishedDriveAction = new LinkedHashSet<String>();
		// �Ѿ����������������������ĵ�������
		LinkedHashSet<String> hsFlowStartedBilltypes = new LinkedHashSet<String>();

		// ���ε���VO
		// AggregatedValueObject destVo = null;
		AggregatedValueObject[] destVos = null;
		// Դ���ݵĲ���Ա
		String srcOperator = srcParaVo.m_operator;

		
		
		for (int i = 0; i < drivedActions.length; i++) { // �����������Ķ���
			
			//���cloneһ����Դvo���ֵ�������Դvo�仯��
			AggregatedValueObject srcBillCloneVO =(AggregatedValueObject) deepClone(srcBillVO);
			
			// �������ĵ�������
			String destBillType = drivedActions[i].getBillType();
			// �������ĵ��ݶ���
			String beDrivedActionName = drivedActions[i].getActionName();

			String currentExecDrive = destBillType + ":" + beDrivedActionName;
			Logger.debug("ִ������" + currentExecDrive + "��ʼ");
			if (hsFinishedDriveAction.contains(currentExecDrive)) {
				Logger.debug("����������:" + currentExecDrive + "��ִ��,����ѭ������.");
				continue;
			}

			if (hsFlowStartedBilltypes.contains(destBillType)) {
				// ������������������������������������,����ִ�����̶��� lj@2005-6-7
				Logger.debug("����������������������������������������ִ������" + currentExecDrive);
				continue;
			}
			// 0.������������Լ����� leijun+2008-7
			PFActionConstrict aConstrict = new PFActionConstrict();
			boolean isPermit = aConstrict.actionConstrictBeforeDrive(drivedActions[i].getPkMessageDrive(), destBillType, beDrivedActionName, srcParaVo);
			if (!isPermit) {
				Logger.debug("��ǰ����" + currentExecDrive + "��Լ�� �����㣬�����¸�����");
				continue;
			}
			hsFinishedDriveAction.add(currentExecDrive);

			// 0.1��ҵ�����ж��Ƿ�����ǰ���� leijun+2008-10
			Object checkClzInstance = PfUtilTools.getBizRuleImpl(srcBilltype);
			if (checkClzInstance != null && checkClzInstance instanceof IActionDriveChecker) {
				boolean isPermited = ((IActionDriveChecker) checkClzInstance).isEnableDrive(srcBilltype, srcBillCloneVO, srcParaVo.m_actionName, destBillType, beDrivedActionName);
				if (!isPermited) {
					Logger.debug("��ǰ����" + currentExecDrive + "��У�鲻���㣬�����¸�����");
					continue;
				}
			}

			// 1.��õ�ǰ������paraVo
			PfParameterVO destParaVo = null;
			//�õ�������+��Դ����������Ϊkey  һ�Ƶ��׵�ҵ���������ֵ�ʱֻ�õ���������Ϊkey�����⡣
			String src_billtypePK =StringUtil.isEmptyWithTrim(srcBillVO.getParentVO().getPrimaryKey())?"":srcBillVO.getParentVO().getPrimaryKey();
			if ((hashBilltypeToParavo.containsKey(destBillType+src_billtypePK))||PfUtilBaseTools.getRealBilltype(srcBilltype).equals(PfUtilBaseTools.getRealBilltype(destBillType))) {
				//FIXME:ͬ�ֵ�������֮��Ҳ������
				Logger.debug("���������paraVo�Ѵ��ڣ�����VO����");
				destParaVo = (PfParameterVO) hashBilltypeToParavo.get(destBillType+src_billtypePK);
				if(destParaVo == null && PfUtilBaseTools.getRealBilltype(srcBilltype).equals(PfUtilBaseTools.getRealBilltype(destBillType))) {
					destParaVo = srcParaVo.clone();
					hashBilltypeToParavo.put(destBillType+src_billtypePK, destParaVo);
				}
				if (destParaVo.m_preValueVos == null) {
					destVos = new AggregatedValueObject[] { destParaVo.m_preValueVo };
				} else
					destVos = destParaVo.m_preValueVos;
				destParaVo.m_splitValueVos = destVos;
			} else {
				Logger.debug("�����ڱ���������VO,�������Դ����Ϊ׼��VO����ת��");
				// У���Ƿ�������VO���� leijun+2006-8-18
				if (checkClzInstance instanceof IChangeVOCheck) {
					boolean bValid = ((IChangeVOCheck) checkClzInstance).checkValidOrNeed(srcBillCloneVO, srcParaVo.m_actionName, destBillType, beDrivedActionName);
					if (!bValid) {
						Logger.debug("Դ����VO����������ת����������¸�����");
						continue;
					}
				}

				// ����VO����ת��
				IPfExchangeService exchangeService = NCLocator.getInstance().lookup(IPfExchangeService.class);
				destVos = exchangeService.runChangeDataAry(srcBilltype, destBillType, new AggregatedValueObject[] { srcBillCloneVO }, srcParaVo);
				Logger.debug("��õ���:" + destBillType + "�����ݽ���VO���");

				if (destVos == null || destVos.length == 0) {
					// WARN::����������ĵ���VOΪ�գ�������¸�����
					Logger.warn(">�������ĵ���VOΪ�գ�������¸�����");
					continue;
				}

				// destVos =
				// PfUtilBaseTools.pfInitVos(destVo.getClass().getName(), 1);
				// destVos[0] = destVo;

				Object[] driveObjs = null;
				if (userObj != null) {
					driveObjs = (Object[]) java.lang.reflect.Array.newInstance(userObj.getClass(), 1);
					// driveObj =
					// PfUtilTools.pfInitVosClass(userObj.getClass().getName());
					driveObjs[0] = userObj;
				}
				Logger.debug("���е���:" + destBillType + "����������VO[0]���");
				// WARN::�Ӹղ�����destVo�л�ȡ����������VO!
				destParaVo = PfUtilBaseTools.getVariableValue(destBillType, beDrivedActionName, null, destVos, userObj, driveObjs, null, eParam, hashBilltypeToParavo,src_billtypePK);
				destParaVo.m_splitValueVos = destVos;
				// WARN::���destVo��û�л�ȡ��operator��Ϣ,��ֵΪ�������ݵĲ���Ա
				// lj@2005-7-11
				if (destParaVo.m_operator == null)
					destParaVo.m_operator = srcOperator;
			}

			// 2.ִ����������
			// 2.1.���ж���ǰԼ�����
			aConstrict.actionConstrictBefore(destParaVo);

			// 2.2.ִ�ж����ű�
			Object tmpObj = actionOnStep(beDrivedActionName, destParaVo);

			// 2.3.���ж�����Լ�����
			aConstrict.actionConstrictAfter(destParaVo);

			// 2.4.������Ϣ����
			backMsg(destParaVo, tmpObj);

			// 2.5.��������, Ϊ��֧�ֵ��ݶ����ļ����������˴��ٴε���actiondrive, @guowl+.
			if (isDriveAction(destBillType, beDrivedActionName)) {
				if (destParaVo.m_splitValueVos == null)
					continue;
				// XXX:�������ִ���з����ֵ���������������� leijun@2008-12
				for (int j = 0; j < destParaVo.m_splitValueVos.length; j++) {
					actiondrive(destParaVo.m_splitValueVos[j], userObj, hashBilltypeToParavo, hashMethodReturn, destParaVo, eParam);
				}
			}

			// 3.���ݱ������Ķ�������������������������
			boolean bAfStarted = false;
			if (beDrivedActionName.toUpperCase().endsWith(IPFActionName.SAVE) || beDrivedActionName.toUpperCase().endsWith(IPFActionName.EDIT)) {
				// ����������Ķ�����"SAVE","EDIT"��β,������������
				bAfStarted = (Boolean)startApproveFlowAfterAction(destParaVo.m_billType, destVos[0], userObj, tmpObj, null, hashBilltypeToParavo, hashMethodReturn,src_billtypePK)[0];
				if (bAfStarted){
					hsFlowStartedBilltypes.add(destBillType);
					sendMessageWhenStartWorkflow(destParaVo,WorkflowTypeEnum.Approveflow.getIntValue());
				}
					
			} else if (beDrivedActionName.toUpperCase().endsWith(IPFActionName.START)) {  
				// ����������Ķ���Ϊ"START",������������
				boolean bWfStarted = startWorkflowAfterAction(destParaVo.m_billType, destVos[0], userObj, tmpObj, null, hashBilltypeToParavo, hashMethodReturn,src_billtypePK);
				if (bWfStarted){
					hsFlowStartedBilltypes.add(destBillType);
					sendMessageWhenStartWorkflow(destParaVo,WorkflowTypeEnum.Workflow.getIntValue());
				}
					
			}
			
			// 4.���δ����������������ԴĿ�ĵ��ݲ�ͬ���ŷ���"��ʽ"��Ϣ lj+2006-5-30
			/* xry TODO:
			if (!bAfStarted && !srcBilltype.equals(destBillType))
				insertPushWorkitems(destParaVo, srcBilltype, destBillType, tmpObj, srcParaVo.m_billVersionPK);
				* /

			lastParamVO =destParaVo;
			Logger.debug("***ִ���������ݶ���:" + currentExecDrive + "����***");
		}// /{end for}

		Logger.debug("*********ִ�ж�������actiondrive����********");
		return true;
	}
	
*/

	  /**
	   * ������ȿ�¡
	   * 
	   * @param oIn Ҫ��¡�Ķ���
	   * @return ��¡�������¶���
	   * @throws IOException 
	   * @throws ClassNotFoundException 
	   */

	private Object deepClone(Object oIn) {
		Object value = null;
		try {
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			ObjectOutputStream o = new ObjectOutputStream(buf);
			o.writeObject(oIn);
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buf.toByteArray()));
			value = in.readObject();
		} catch (IOException ex) {
			Logger.error(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			Logger.error(ex.getMessage());
		}
		return value;
	}
	
	/* xry TODO:
	/**
	 * ҵ��������ת
	 * @throws BusinessException 
	 * /
	private void jumpBusitype(PfParameterVO paravo) throws BusinessException {
		if(paravo==null)
			return;
		String pk_busitype = paravo.m_businessType;
		// ������Ϊ��ǰ��½��
		String operator =InvocationInfoProxy.getInstance().getUserId();
		// �������ͻ��߽������ͱ���
		String destBillType = paravo.m_billType;
		BilltypeVO billvo =PfDataCache.getBillType(destBillType);
		if(billvo==null)
			return;
		BaseDAO dao = new BaseDAO();
		BillbusinessVO condVO = new BillbusinessVO();
		condVO.setPk_businesstype(pk_busitype);
		condVO.setJumpflag(UFBoolean.TRUE);
		if(billvo.getIstransaction()!=null&&billvo.getIstransaction().booleanValue()){
			condVO.setPk_billtype(billvo.getParentbilltype());
			condVO.setTranstype(billvo.getPk_billtypecode());
		}else{
			condVO.setPk_billtype(billvo.getPk_billtypecode());
		}
		try {
			Collection co = dao.retrieve(condVO, true);
			//֧��ҵ��������ת
			if (co.size() > 0 && paravo.m_preValueVos != null) {
				HashMap<String, String> busitypeMaps = new HashMap<String, String>();
				for (AggregatedValueObject vo : paravo.m_preValueVos) {
					IFlowBizItf fbi = PfMetadataTools.getBizItfImpl(vo, IFlowBizItf.class);
					//δʵ��ҵ��ӿڵĵ��ݣ�ֱ������
					if(fbi==null)
						continue;
					String destBusitypePk = null;
					fbi.getBusitype();
					//�п��ܷ��ص��ǽ�������
					String billtype=fbi.getBilltype();
					if(!StringUtil.isEmptyWithTrim(billtype)){
						//ȷ�����ص��ǵ������ͱ���
						billtype =PfUtilBaseTools.getRealBilltype(billtype);
					}else{
						BilltypeVO billtypevo =PfDataCache.getBillTypeInfo(fbi.getTranstype());
						billtype =billtypevo==null?null:billtypevo.getParentbilltype();
					}
					if(StringUtil.isEmptyWithTrim(billtype)){
						billtype=paravo.m_billType;
					}
					if(StringUtil.isEmptyWithTrim(billtype)){
						continue;
					}
					String key = billtype + fbi.getTranstype() == null ? "" : fbi.getTranstype()
							+ fbi.getPkorg() == null ? "" : fbi.getPkorg() + operator;
					if (busitypeMaps.containsKey(key)) {
						destBusitypePk = busitypeMaps.get(key);
					} else {
						destBusitypePk = NCLocator.getInstance().lookup(IPFConfig.class)
								.retBusitypeCanStart(billtype, fbi.getTranstype(), fbi.getPkorg(), operator);
						busitypeMaps.put(key, destBusitypePk);
					}
					//���û���ҵ�Ҫ��ת��ҵ����
					if(StringUtil.isEmptyWithTrim(destBusitypePk)){
						continue;
					}
					fbi.setBusitype(destBusitypePk);
					JumpStatusCallbackContext context =new JumpStatusCallbackContext();
					context.setBillVo(vo);
					context.setBusitype(destBusitypePk);
					context.setBilltypeOrTranstype(StringUtil.isEmptyWithTrim(fbi.getTranstype())?billtype:fbi.getTranstype());
					callbackBillStatus(context);
				}
			}
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
		}
	}
	*/
	
	/**
	 * ��д����״̬��ҵ��������תʹ��
	 * */
	private void callbackBillStatus(JumpStatusCallbackContext context){
		//����״̬��д
		ArrayList<Billtype2VO> vos= PfDataCache.getBillType2Info(context.getBilltypeOrTranstype(), ExtendedClassEnum.BUSI_CALLBACK.getIntValue());
		for(Billtype2VO bt2VO:vos){
			String checkClsName = bt2VO.getClassname();
			if (StringUtil.isEmptyWithTrim(checkClsName))
				continue;
			try {
				Object objImpl = Class.forName(checkClsName).newInstance();
				if(objImpl instanceof IBusiBillStatusCallBack){
					((IBusiBillStatusCallBack)objImpl).callCheckStatus(context);
				}
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * ����"��ʽ"ҵ������Ϣ
	 * <li>��ɾ���õ��ݲ�������ʽ��Ϣ
	 * 
	 * @param paravo
	 * @param srcBillType
	 * @param destBillType
	 * @param retObj
	 * @param srcBillId
	 *            ��Դ����ID
	 * /
	private void insertPushWorkitems(PfParameterVO paravo, String srcBillType, String destBillType, Object retObj, String srcBillId) {
		Logger.debug(">>������ʽ��Ϣ=" + destBillType + "��ʼ");
		String pkGroup = paravo.m_pkGroup;
		String pk_busitype = paravo.m_businessType;
		String senderman = paravo.m_operator;

		// �ж��Ƿ�����ʽ��Ϣ
		boolean isNeed = false; // XXX:leijun 2009-12 �ж��Ƿ���Ҫ����������Ϣ
		BillbusinessVO condVO = new BillbusinessVO();
		condVO.setPk_group(pkGroup);
		condVO.setPk_businesstype(pk_busitype);
		
		if(PfUtilBaseTools.isTranstype(srcBillType)){
			BilltypeVO transtypeVO = PfDataCache.getBillTypeInfo(srcBillType);
			condVO.setPk_billtype(transtypeVO.getParentbilltype());
			condVO.setTranstype(srcBillType);
		}else{
			condVO.setPk_billtype(srcBillType);
		}	
		
		BaseDAO dao = new BaseDAO();
		try {
			Collection co = dao.retrieve(condVO, true);
			if (co.size() > 0) {
				BillbusinessVO vo = (BillbusinessVO) co.iterator().next();
				UFBoolean isMsg = vo.getForwardmsgflag();
				if (isMsg != null && isMsg.booleanValue())
					isNeed = true;
			}
		} catch (DAOException ex) {
			Logger.error(ex.getMessage(), ex);
			return;
		}

		if (!isNeed) {
			Logger.debug(">>Դ����" + srcBillType + "���ɷ���������Ϣ������");
			return;
		}

		// XXX:�ֵ�VO����
		if (paravo.m_splitValueVos == null || paravo.m_splitValueVos.length == 0)
			return;

		Logger.debug(">>��ʽ��Ϣ���ֵ���=" + paravo.m_splitValueVos.length);

		IPfPersonFilter2 filter = null;
		try {
			Object checkClzInstance = PfUtilTools.getBizRuleImpl(paravo.m_billType);
			if (checkClzInstance instanceof IPfPersonFilter2)
				filter = (IPfPersonFilter2) checkClzInstance;
		} catch (BusinessException ex) {
			// XXX:����־�쳣������ִ��
			Logger.error("����ƽ̨������ҵ����������Ϣ��Ա����IPfPersonFilter2�ӿ��쳣��" + ex.getMessage(), ex);
		}

		for (int k = 0; k < paravo.m_splitValueVos.length; k++) {
			// ����ÿ���ֵ���Ŀ�ĵ���VO
			AggregatedValueObject billvo = paravo.m_splitValueVos[k];
			// �ٴλ�ȡBillId,BillNo
			PfUtilBaseTools.fetchBillId(paravo, billvo, retObj);
			IPFConfig pfcfg = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());
			try {
				// ������д�������Ϣ���û�(�������� -- ����������)
				String[] hsUserPKs = pfcfg.queryForwardMessageUser(srcBillType, destBillType, pk_busitype, billvo, filter);
				if (hsUserPKs == null || hsUserPKs.length == 0) {
					Logger.warn(">>�޷�������ʽ��Ϣ����Ϊ�����û�Ϊ��");
					return;
				}

				// 4.����Щ�û�����"��ʽ"��������Ϣ
				ArrayList<MessageinfoVO> alItems = new ArrayList<MessageinfoVO>();
				for (String userId : hsUserPKs) {
					MessageinfoVO wi = new MessageinfoVO();
					wi.setPk_billtype(destBillType);
					wi.setPk_srcbilltype(srcBillType);
					wi.setBillid(paravo.m_billVersionPK); // ���ε���ID
					wi.setBillno(paravo.m_billNo);
					wi.setCheckman(userId);
					// FIXME::i18n
					wi.setTitle(Pfi18nTools.i18nBilltypeName(srcBillType, null) + NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFBusiAction-0000")/*��ʽ�����µ��ݣ�* / + Pfi18nTools.i18nBilltypeName(destBillType, null) + paravo.m_billNo + NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFBusiAction-0001")/*���봦��* /);
					// wi.setPk_busitype(pk_busitype);
					wi.setPk_corp(pkGroup);
					wi.setSenderman(senderman);
					alItems.add(wi);
				}

				// ���Ҫ������ʽ��Ϣ������ɾ����ʽ��Ϣ
				if (alItems.size() > 0) {
					PfMessageUtil.deletePullMessage(srcBillId);
					new PFMessageImpl().insertPushOrPullMsgs((MessageinfoVO[]) alItems.toArray(new MessageinfoVO[alItems.size()]), MessageTypes.MSG_TYPE_BUSIFLOW_PUSH);
					Logger.debug(">>������ʽ��Ϣ��=" + alItems.size());
				}
			} catch (Exception e) {
				// WARN::������־�쳣������Ӱ��ҵ������
				Logger.error(e.getMessage(), e);
			}
		} // /{end for}
		Logger.debug(">>������ʽ��Ϣ=" + destBillType + "����");
	}
	*/

	/**
	 * ��õ�ǰ���ݶ������п������Ķ���
	 * 
	 * @param paraVo
	 * @return
	 * @throws PFBusinessException
	 */
	private PfUtilActionVO[] queryActionDriveVOs(PfParameterVO paraVo) throws PFBusinessException {
		PfUtilActionVO[] driveActions = null;
		try {
			PfUtilDMO dmo = new PfUtilDMO();
			driveActions = dmo.queryDriveAction(paraVo.m_billType, paraVo.m_businessType, paraVo.m_pkOrg, paraVo.m_actionName, paraVo.m_operator);

			// FIXME::arap����Ҫ��-�����������û����������������������ҵ������͵���������
			if (driveActions == null || driveActions.length == 0) {
				if (PfUtilBaseTools.isTranstype(paraVo.m_billType)) {
					Logger.debug("�Ҳ����������͵Ķ��������������ҵ������͵���������");
					String strRealBilltype = PfUtilBaseTools.getRealBilltype(paraVo.m_billType);
					driveActions = dmo.queryDriveAction(strRealBilltype, paraVo.m_businessType, paraVo.m_pkOrg, paraVo.m_actionName, paraVo.m_operator);
				}
			}
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
		return driveActions;
	}

	public Object processAction(String actionName, String billOrTranstype, WorkflownoteVO workflowVo, AggregatedValueObject billvo, Object userObj, HashMap eParam) throws BusinessException {
		Logger.init("workflow");
		Logger.debug("*��̨���ݶ�������PFBusiAction.processAction��ʼ");
		debugParams(actionName, billOrTranstype, workflowVo, billvo, userObj, eParam);
		long start = System.currentTimeMillis();

		Hashtable hashBilltypeToParavo = new Hashtable();
		Hashtable hashMethodReturn = new Hashtable();
		
		
		// 0.��������(�ϴ�����)
		processWorknote(workflowVo);

		// 1.���춯������Ĳ���VO
		AggregatedValueObject[] inVos = null;
		if (billvo != null) {
			inVos = PfUtilBaseTools.pfInitVos(billvo.getClass().getName(), 1);
			inVos[0] = billvo;
		}
		PfParameterVO paraVoOfBilltype = PfUtilBaseTools.getVariableValue(billOrTranstype, actionName, billvo, inVos, userObj, null, workflowVo, eParam, hashBilltypeToParavo);

		try {
			// 2.���Ϊɾ����������ɾ��������Ϣ
			if (PfUtilBaseTools.isDeleteAction(paraVoOfBilltype.m_actionName, paraVoOfBilltype.m_billType))
				deleteWorkFlow(paraVoOfBilltype);

			// 3.���ж���ǰԼ�����
			IPFActionConstrict aConstrict = new PFActionConstrict();
			aConstrict.actionConstrictBefore(paraVoOfBilltype);

			// 4.ִ�ж����ű�
			Object retObj = actionOnStep(paraVoOfBilltype.m_actionName, paraVoOfBilltype);

			// 5.���ж�����Լ�����
			aConstrict.actionConstrictAfter(paraVoOfBilltype);

			// 6.�ű�����ֵ�Ĵ���
			if (retObj instanceof nc.bs.pub.compiler.IWorkFlowRet) {
				// XXX::ֻ�нű�����procActionFlow@@,������������������ʱ�ŷ��ظö���
				return ((nc.bs.pub.compiler.IWorkFlowRet) retObj).m_inVo;
			}

			try {
				Object[] tmpObj = (Object[]) retObj;
				Hashtable hasNoProc = null;
				// XXX::���뱣֤�����Ķ����ű����������ķ���ֵ lj
				if (tmpObj != null && tmpObj.length > 0 && tmpObj[0] instanceof IWorkflowBatch) {
					IWorkflowBatch wfBatch = (IWorkflowBatch) tmpObj[0];
					hasNoProc = wfBatch.getNoPassAndGoing();
					Object[] userObjs = (Object[]) wfBatch.getUserObj();
					retObj = userObjs[0];
				}
				if (hasNoProc != null && hasNoProc.containsKey("0"))
					return null;
			} catch (Exception e) {
				// FIXME::��������ű�����ֵ������������쳣��������֮
			}

			// 7.������Ϣ����
			backMsg(paraVoOfBilltype, retObj);

			/* xry TODO:
			// 8.��������
			if (isDriveAction(paraVoOfBilltype.m_billType, paraVoOfBilltype.m_actionName)&&isNeedDriveAction(retObj)) {
				actiondrive(paraVoOfBilltype.m_preValueVo, userObj, hashBilltypeToParavo, hashMethodReturn, paraVoOfBilltype, eParam);
			}
			
			*/
			String src_billtypePK =paraVoOfBilltype.m_preValueVo!=null?StringUtil.isEmptyWithTrim(paraVoOfBilltype.m_preValueVo.getParentVO().getPrimaryKey())?"":paraVoOfBilltype.m_preValueVo.getParentVO().getPrimaryKey():"";

			// 9.�������Ϊ�ύ����������������������
			if (PfUtilBaseTools.isSaveAction(paraVoOfBilltype.m_actionName, paraVoOfBilltype.m_billType)
					||PfUtilBaseTools.isStartAction(paraVoOfBilltype.m_actionName, paraVoOfBilltype.m_billType)){
				retObj = startWorkflowAfterAction(paraVoOfBilltype.m_billType, paraVoOfBilltype.m_preValueVo, 
						userObj, retObj, eParam, hashBilltypeToParavo, hashMethodReturn,src_billtypePK)[1];
				sendMessageWhenStartWorkflow(paraVoOfBilltype,WorkflowTypeEnum.Approveflow.getIntValue());
			}				
			
			/* xry TODO:
			//10 .���ҵ����������ת��������ִ�����̵���ת
			jumpBusitype(lastParamVO);
			*/	

			// �������ʵ�����е����ݣ������޷��ͷ�
			WfInstancePool.getInstance().clear();

			Logger.debug("*��̨���ݶ�������PFBusiAction.processAction��������ʱ" + (System.currentTimeMillis() - start) + "ms");

			return retObj;
		} catch (BusinessException ex) {
			logWorkflowExptInfo(ex, paraVoOfBilltype);
			throw ex;
		}
	}
	
	
	private void processWorknote(WorkflownoteVO worknoteVO) throws BusinessException {
		
		if (worknoteVO == null || worknoteVO.getAttachmentSetting() == null)
			return;
		
		List<WorkflownoteAttVO> noteAttList = new ArrayList<WorkflownoteAttVO>();
		List<AttachmentVO> attList = worknoteVO.getAttachmentSetting();
		
		String pk_wf_task = worknoteVO.getPk_wf_task();
		String pk_checkflow = worknoteVO.getPk_checkflow();
		
		for (AttachmentVO attVO : attList) {
			WorkflownoteAttVO noteAtt = new WorkflownoteAttVO();
			
			noteAtt.setPk_wf_task(pk_wf_task);
			noteAtt.setPk_checkflow(pk_checkflow);
			noteAtt.setPk_file(attVO.getPk_file());
			noteAtt.setFilename(attVO.getFilename());
			noteAtt.setFilesize(attVO.getFilesize());
			
			noteAttList.add(noteAtt);
		}
		
		new BaseDAO().insertVOList(noteAttList);
	}
	
	/**
	 * ��ǰ�����Ƿ���Ҫִ�ж������� 
	 * @param eParam ִ�ж����ű��󷵻ص�obj
	 * @return 
	 * */
	private boolean isNeedDriveAction(Object retObj){
		
		if(retObj==null||!(retObj instanceof AggregatedValueObject))
			return true;	
		AggregatedValueObject aggObj =(AggregatedValueObject)retObj;
		CircularlyAccessibleValueObject parentvo =aggObj.getParentVO();
		if(parentvo==null)
			return true;
		Object driveFlag = parentvo.getAttributeValue(PfUtilBaseTools.PARAM_DRIVEACTION);
		if(driveFlag==null)
			return true;
		return driveFlag.toString().equalsIgnoreCase("Y");
	}

	/**
	 * added by chengsc. exception info log for workflow/checkflow/business flow
	 */
	private void logWorkflowExptInfo(BusinessException ex, PfParameterVO paramVO) {
		if (paramVO == null || paramVO.m_workFlow == null)
			return;
		IPFExptLog itf = (IPFExptLog) NCLocator.getInstance().lookup(IPFExptLog.class);
		PfExptLogVO logVO = new PfExptLogVO();
		logVO.setPk_org(paramVO.m_pkOrg);
		logVO.setBillno(paramVO.m_billNo);
		logVO.setBilltype(paramVO.m_billType);
		logVO.setMsghint(ex.getMessage());
		logVO.setContent(ex.getMessage());

		logVO.setFlowtype(paramVO.m_workFlow.getWorkflow_type() == null ? WorkflowTypeEnum.Approveflow.getIntValue() : paramVO.m_workFlow.getWorkflow_type());
		try {
			if (paramVO.m_workFlow.getTaskInstanceVO() != null && paramVO.m_workFlow.getTaskInstanceVO().getPk_process_def()!= null)
				itf.insertLog_RequiresNew(paramVO.m_workFlow.getTaskInstanceVO().getPk_process_def(), logVO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.error("��¼�����쳣��־����");
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * ��־һ�¶�������������Ĳ���
	 */
	private void debugParams(String actionName, String billOrTranstype, WorkflownoteVO worknoteVO, Object billEntity, Object userObj, HashMap eParam) {
		Logger.debug("*********************************************");
		Logger.debug("* actionName=" + actionName);
		Logger.debug("* billType=" + billOrTranstype);
		Logger.debug("* worknoteVO=" + worknoteVO);
		Logger.debug("* billEntity=" + billEntity);
		Logger.debug("* userObj=" + userObj);
		Logger.debug("* eParam=" + eParam);
		Logger.debug("*********************************************");
	}

	/**
	 * ������Ϣ����
	 * 
	 * @param paraVoOfBilltype
	 */
	private void backMsg(PfParameterVO paravo, Object retObj) {
		Logger.debug(">>������Ϣ����=" + paravo.m_billType + "��ʼ");

		// 1.�ж��Ƿ���Ҫ����������Ϣ
		BaseDAO dao = new BaseDAO();
		try {
			BillbusinessVO condVO = new BillbusinessVO();
			condVO.setPk_group(paravo.m_pkOrg);
			condVO.setPk_businesstype(paravo.m_businessType);
			
			if(PfUtilBaseTools.isTranstype(paravo.m_billType)){
				BilltypeVO transtypeVO = PfDataCache.getBillTypeInfo(paravo.m_billType);
				condVO.setPk_billtype(transtypeVO.getParentbilltype());
				condVO.setTranstype(paravo.m_billType);
			}else{
				condVO.setPk_billtype(paravo.m_billType);
			}			
			Collection co = dao.retrieve(condVO, true);
			if (co.size() > 0) {
				BillbusinessVO vo = (BillbusinessVO) co.iterator().next();
				UFBoolean isMsg = vo.getBackmsgflag();
				if (isMsg == null || !isMsg.booleanValue()) {
					Logger.debug(">>����" + paravo.m_billType + "���ɷ���������Ϣ������");
					return;
				}
			}
		} catch (DAOException ex) {
			Logger.error(ex.getMessage(), ex);
			return;
		}

		// 2.��ѯ��ǰ���ݶ��� �����õ�������Ϣ
		try {
			BackmsgVO condVO = new BackmsgVO();
			condVO.setPk_group(paravo.m_pkGroup);
			condVO.setPk_busitype(paravo.m_businessType);
			condVO.setPk_srcbilltype(paravo.m_billType);
			condVO.setActiontype(paravo.m_actionName);
			condVO.setIsapprover(null);
			condVO.setIsbillmaker(null);
			Collection coBackmsg = dao.retrieve(condVO, true);
			if (coBackmsg.size() == 0) {
				Logger.debug(">>����" + paravo.m_billType + "û�н���������Ϣ���ã�����");
				return;
			}

			// 3.���ݵ�������ע���������������ѯ���ε�����Ϣ����ִ��������Ϣ����
			BilltypeVO billVo = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(paravo.m_billType).buildPkGroup(paravo.m_pkGroup));
			if (billVo.getCheckclassname() != null && billVo.getCheckclassname().trim().length() != 0) {
				Object obj = PfUtilTools.instantizeObject(billVo.getPk_billtypecode(), billVo.getCheckclassname().trim());
				if (obj instanceof IPFSourceBillFinder) {
					IPFSourceBillFinder srcFinder = (IPFSourceBillFinder) obj;
					// �ٴλ�ȡBillId,BillNo
					PfUtilBaseTools.fetchBillId(paravo, paravo.m_preValueVo, retObj);

					for (Iterator iter = coBackmsg.iterator(); iter.hasNext();)
						// ��������������
						executeBackmsgs(srcFinder, (BackmsgVO) iter.next(), paravo);
				} else {
					Logger.debug(">>����" + paravo.m_billType + "�������������û��ʵ�ֽӿ�IPFSourceBillFinder������");
					return;
				}
			} else {
				Logger.debug(">>����" + paravo.m_billType + "û��ע������������࣬����");
				return;
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * ����������Ϣ
	 * 
	 * @param srcFinder
	 * @param backmsgVO
	 * @param paravo
	 * @throws BusinessException
	 */
	private void executeBackmsgs(IPFSourceBillFinder srcFinder, BackmsgVO backmsgVO, PfParameterVO paravo) throws BusinessException {
		Logger.debug(">>�����ε���" + backmsgVO.getPk_destbilltype() + "����������Ϣ ��ʼ");

		// 1.��ѯ��ǰ���ݵ����ε�����Ϣ
		SourceBillInfo[] infos = srcFinder.findSourceBill(backmsgVO.getPk_destbilltype(), paravo.m_preValueVo);
		HashSet<String> hsBillmakers = new HashSet<String>();
		HashSet<String> hsApprovers = new HashSet<String>();
		for (int i = 0; i < (infos == null ? 0 : infos.length); i++) {
			hsBillmakers.add(infos[i].getBillmaker());
			hsApprovers.add(infos[i].getApprover());
		}

		// 2.������Ϣ���ݲ�����
		// TODO::i18n
		ArrayList alActions = PfDataCache.getBillactionVOs(paravo.m_billType);
		String strDefaultName = Pfi18nTools.findActionName(paravo.m_actionName, alActions);
		String msgContent = NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFBusiAction-0002")/*���ε��ݣ�*/ + Pfi18nTools.i18nBilltypeName(paravo.m_billType, null) + " " + paravo.m_billNo + NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFBusiAction-0003")/*�����˵��ݶ�����*/
				+ Pfi18nTools.i18nActionName(paravo.m_billType, paravo.m_actionName, strDefaultName) + "(" + paravo.m_actionName + ")" + NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFBusiAction-0004")/*�Ĵ�����鿴*/;
		ArrayList<MessageinfoVO> alItems = new ArrayList<MessageinfoVO>();
		if (backmsgVO.getIsbillmaker().booleanValue() && backmsgVO.getIsapprover().booleanValue()) {
			hsBillmakers.addAll(hsApprovers);
			constructBackmsgs(paravo, hsBillmakers, msgContent, alItems);
		} else if (backmsgVO.getIsbillmaker().booleanValue()) {
			constructBackmsgs(paravo, hsBillmakers, msgContent, alItems);
		} else if (backmsgVO.getIsapprover().booleanValue()) {
			constructBackmsgs(paravo, hsApprovers, msgContent, alItems);
		}

		// new PFMessageImpl().insertBizMsgs((WorkflownoteVO[])
		// alNotes.toArray(new WorkflownoteVO[0]));
		new PFMessageImpl().insertPushOrPullMsgs((MessageinfoVO[]) alItems.toArray(new MessageinfoVO[alItems.size()]), MessageTypes.MSG_TYPE_BUSIFLOW);

		Logger.debug(">>�����ε���" + backmsgVO.getPk_destbilltype() + "����������Ϣ ����");
	}

	/**
	 * ����������Ϣ����
	 * 
	 * @param paravo
	 * @param hsReceivers
	 * @param msgContent
	 * @param alItems
	 */
	private void constructBackmsgs(PfParameterVO paravo, HashSet<String> hsReceivers, String msgContent, ArrayList<MessageinfoVO> alItems) {
		for (String receiver : hsReceivers) {
			MessageinfoVO wi = new MessageinfoVO();
			wi.setPk_billtype(paravo.m_billType);
			// wi.setPk_srcbilltype(srcBillType);
			wi.setBillid(paravo.m_billVersionPK);
			wi.setBillno(paravo.m_billNo);
			wi.setCheckman(receiver);
			wi.setTitle(msgContent);
			// wi.setPk_busitype(paravo.m_businessType);
			wi.setPk_corp(paravo.m_pkOrg);
			wi.setSenderman(paravo.m_operator);
			alItems.add(wi);
		}
	}

	public Object[] processBatch_MultiThread(String actionName, String billOrTranstype, AggregatedValueObject[] billvos, Object[] userObjAry, WorkflownoteVO worknoteVO, HashMap eParam) throws BusinessException {

		ExecutorService exe = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
		CompletionService service = new ExecutorCompletionService(exe);
		Object[] ret = null;
		for (int i = 0; i < billvos.length; i++) {
			HashMap map = eParam == null ? null : (HashMap) eParam.clone();
			Object obj = (userObjAry == null || userObjAry.length == 0) ? null : userObjAry[i];
			SingleBillFlowTask task = new SingleBillFlowTask(actionName, billOrTranstype, billvos[i], obj, worknoteVO, map);
			service.submit(task);// /
		}
		for (int i = 0; i < billvos.length; i++) {
			Future f;
			try {
				f = service.take();
				Object obj = f.get();
				if (ret == null)
					ret = (Object[]) Array.newInstance(obj.getClass(), billvos.length);
				ret[i] = obj;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Logger.error(e);
			}
		}

		return ret;
	}

	/**
	 * @param actionName
	 * @param billOrTranstype
	 * @param billvos
	 * @param userObjAry 
	 * @param worknoteVO
	 * @param param 
	 * @return
	 * @throws BusinessException
	 */
	private WorkflownoteVO[] prepareWorkflownotesForBatch(String actionName, String billOrTranstype, AggregatedValueObject[] billvos, Object[] userObjAry, WorkflownoteVO worknoteVO, HashMap hmParam, PFBatchExceptionInfo exceptionInfo) throws BusinessException {
		
		WorkflownoteVO[] noteArray = new WorkflownoteVO[billvos.length];
		for (int i = 0; i < billvos.length; i++) {
			if(!(PfUtilBaseTools.isStartFlowAction(actionName, billOrTranstype) || PfUtilBaseTools.isSignalFlowAction(actionName,billOrTranstype))) {
				noteArray[i] = new WorkflownoteVO();
				continue;
			}
			try {
				noteArray[i] = NCLocator.getInstance().lookup(IWorkflowMachine.class).checkWorkFlow(actionName, billOrTranstype, billvos[i], null);
				if(noteArray[i] == null) {
					//��ֱ������ͨ������
					noteArray[i] = new WorkflownoteVO();
					noteArray[i].setAnyoneCanApprove(true);
					noteArray[i].setApproveresult("Y");
				} else if (!"R".equals(worknoteVO.getApproveresult())){	// ���ڷǲ��ز�������Ҫ�ж�ָ����Ϣ
					if(noteArray[i].getAssignableInfos()!= null && noteArray[i].getAssignableInfos().size() > 0){
						//��Ҫָ�ɵĵ��ݣ����ټ���ִ�����̣��ڴ˴�����Ϊ��
						noteArray[i]=null;
						throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFBusiAction-0005")/*������Ҫָ�ɣ�������������*/);
					}
					if(noteArray[i].getTransitionSelectableInfos()!= null && noteArray[i].getTransitionSelectableInfos().size() > 0){
						throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFBusiAction-0006")/*������Ҫ�ֹ�ѡ���֧���������������*/);
					}
				}
				if(noteArray[i] != null && worknoteVO != null) {
					noteArray[i].setIscheck(worknoteVO.getIscheck());
					noteArray[i].setApproveresult(worknoteVO.getApproveresult());
					noteArray[i].setChecknote(worknoteVO.getChecknote());
					if("R".equals(worknoteVO.getApproveresult())&&!noteArray[i].isAnyoneCanApprove()) {
						//xry TODO:noteArray[i].getTaskInfo().getTask().setBackToFirstActivity(true);
						noteArray[i].getTaskInstanceVO().setCreate_type(TaskInstanceCreateType.Reject.getIntValue());
					}
				}
			}catch(BusinessException e) {
				Logger.error(e.getMessage());
				exceptionInfo.putErrorMessage(i, billvos[i], e.getMessage());
				continue;
			}
			
		}
		return noteArray;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.itf.uap.pf.IPFBusiAction#processBatch(java.lang.String,
	 *      java.lang.String, nc.vo.pub.AggregatedValueObject[],
	 *      java.lang.Object[], nc.vo.pub.workflownote.WorkflownoteVO,
	 *      java.util.HashMap)
	 */
	public Object[] processBatch(String actionName, String billOrTranstype, AggregatedValueObject[] billvos, Object[] userObjAry, WorkflownoteVO worknoteVO, HashMap eParam, PFBatchExceptionInfo exceptionInfo) throws BusinessException {
		WorkflownoteVO[] noteArray = prepareWorkflownotesForBatch(actionName, billOrTranstype, billvos, userObjAry, worknoteVO, eParam, exceptionInfo);
		Object[] ret = null;
		for (int i = 0; i < billvos.length; i++) {
			if(noteArray[i] == null)
				continue;
			HashMap map = eParam == null ? null : (HashMap) eParam.clone();
			Object obj = (userObjAry == null || userObjAry.length == 0) ? null : userObjAry[i];
			Object singleVal = null;
			try {
				singleVal = NCLocator.getInstance().lookup(IWorkflowMachine.class).processSingleBillFlow_RequiresNew(actionName, billOrTranstype, noteArray[i], billvos[i], obj, map);

			} catch (Exception e) {//FIXME��Exception����ķ��أ���BusinessException��ΪException�������쳣��������Ϣ���
				Logger.error(e);
				exceptionInfo.putErrorMessage(i, billvos[i], e.getMessage());
			}
			ret = PfUtilBaseTools.composeResultAry(singleVal, billvos.length, i, ret);
		}
		return ret;
	}

	public Object[] processBatch(String actionName, String billOrTranstype, AggregatedValueObject[] billvos, Object[] userObjAry, WorkflownoteVO worknoteVO, HashMap eParam)
			throws BusinessException {
		// ƽ̨��־
		Logger.init("workflow");
		Logger.debug("*��̨���ݶ���������PFBusiAction.processBatch��ʼ");
		debugParams(actionName, billOrTranstype, worknoteVO, billvos, userObjAry, null);
		long start = System.currentTimeMillis();

		if (billvos == null)
			return null;

		// ����Լ�����
		IPFActionConstrict aConstrict = new PFActionConstrict();

		// ��ʼ������
		Hashtable<String, Object> hashBilltypeToParavo = new Hashtable<String, Object>();
		Hashtable hashMethodReturn = new Hashtable();
		PfParameterVO paravoOfLastSrcBill = null;
		for (int i = 0; i < billvos.length; i++) {
			PfParameterVO paraOfThisBill = null;
			// ��������VO����
			if (userObjAry != null && userObjAry.length >= 1) {
				// ��ȡ�����
				paraOfThisBill = PfUtilBaseTools.getVariableValue(billOrTranstype, actionName, billvos[i], billvos, userObjAry[i], userObjAry, worknoteVO, eParam, hashBilltypeToParavo);
			} else {
				// ��ȡ�����
				paraOfThisBill = PfUtilBaseTools.getVariableValue(billOrTranstype, actionName, billvos[i], billvos, null, null, worknoteVO, eParam, hashBilltypeToParavo);
			}
			// 2.���Ϊɾ����������ɾ��������Ϣ
			if (PfUtilBaseTools.isDeleteAction(paraOfThisBill.m_actionName, paraOfThisBill.m_billType))
				deleteWorkFlow(paraOfThisBill);

			// ��ÿ������VO�������ж���ǰԼ�����
			aConstrict.actionConstrictBefore(paraOfThisBill);

			if (i == billvos.length - 1) {
				paravoOfLastSrcBill = paraOfThisBill;
			}
		} // /{end for}

		paravoOfLastSrcBill.m_preValueVo = null;
		// ִ�е��ݶ���->WARN::��ʱ��m_paraVoΪ���һ�����ݵĲ���VO
		Object[] retObjs = (Object[]) actionOnStep(paravoOfLastSrcBill.m_actionName, paravoOfLastSrcBill);

		// ����δͨ��������еĵ���������
		Hashtable hasNoProc = null;
		// WARN::���뱣֤�����Ķ����ű����������ķ���ֵ lj
		if (retObjs != null && retObjs.length > 0 && retObjs[0] instanceof IWorkflowBatch) {
			IWorkflowBatch wfBatch = (IWorkflowBatch) retObjs[0];
			hasNoProc = wfBatch.getNoPassAndGoing();
			retObjs = (Object[]) wfBatch.getUserObj();
		}

		if (hasNoProc == null) {
			hasNoProc = new Hashtable();
		}

		// ˵��VO�������޸ĺ��Ϊ׼,����������
		// ���²��������ȫ�ֱ���paraVo�л�ȡVO����,������ԭVO���в���
		AggregatedValueObject[] beforeVos = paravoOfLastSrcBill.m_preValueVos;
		for (int i = 0; beforeVos != null && i < beforeVos.length; i++) {
			// XXX:guowl+,
			// �ӵڶ��ŵ��ݿ�ʼ,���workflownote����Ϊǰ̨��������workflownote�Ǹ��ݵ�һ�ŵ��ݲ��
			if (i == 1) {
				worknoteVO = null;
			}
			// **********fgj2002-10-23edit******************
			if (beforeVos[i] == null) {
				continue;
			}

			// ***********2002-06-26edit **************
			if (hasNoProc.containsKey(String.valueOf(i))) {
				continue;
			}

			Object tmpActionObj = null;
			// leijun@2008-9 ���»�ȡUserObjects,�п��ܶ����ű��޸�����
			userObjAry = paravoOfLastSrcBill.m_userObjs;
			if (userObjAry != null && userObjAry.length != 0) {
				tmpActionObj = userObjAry[i];
			}
			// ����������
			PfParameterVO currParaVo = null;
			if (userObjAry != null && userObjAry.length >= 1) {
				// ��ȡ�����
				currParaVo = PfUtilBaseTools.getVariableValue(billOrTranstype, actionName, beforeVos[i], beforeVos, tmpActionObj, userObjAry, worknoteVO, eParam, hashBilltypeToParavo);
			} else {
				// ��ȡ�����
				currParaVo = PfUtilBaseTools.getVariableValue(billOrTranstype, actionName, beforeVos[i], beforeVos, null, null, worknoteVO, eParam, hashBilltypeToParavo);
			}

			// ��ÿ������VO�������ж�����Լ�����
			aConstrict.actionConstrictAfter(currParaVo);

			// ������Ϣ����
			backMsg(currParaVo, retObjs == null ? null : retObjs[i >= retObjs.length ? 0 : i]);

			// ��������
			String strActionNameOfPara = currParaVo.m_actionName;
			
			String src_billtypePK =currParaVo.m_preValueVo!=null?StringUtil.isEmptyWithTrim(currParaVo.m_preValueVo.getParentVO().getPrimaryKey())?"":currParaVo.m_preValueVo.getParentVO().getPrimaryKey():"";
			
			/* xry TODO:
			if (isDriveAction(currParaVo.m_billType, strActionNameOfPara)) {
				// XXX::��������ʵ������������������ʱ����������
				hashBilltypeToParavo = new Hashtable<String, Object>();
				hashBilltypeToParavo.put(currParaVo.m_billType+src_billtypePK, currParaVo);
				actiondrive(currParaVo.m_preValueVo, tmpActionObj, hashBilltypeToParavo, hashMethodReturn, currParaVo, new HashMap());
			}
			*/
			

			// ��������������������
			if (PfUtilBaseTools.isSaveAction(strActionNameOfPara, currParaVo.m_billType) ||
					PfUtilBaseTools.isStartAction(strActionNameOfPara, currParaVo.m_billType)){
				startWorkflowAfterAction(currParaVo.m_billType, beforeVos[i], tmpActionObj, retObjs == null ? null : retObjs[i], eParam, hashBilltypeToParavo, hashMethodReturn,src_billtypePK);
				sendMessageWhenStartWorkflow(currParaVo,WorkflowTypeEnum.Approveflow.getIntValue());
			}				
		}

		// �������ʵ�����е����ݣ������޷��ͷ�
		WfInstancePool.getInstance().clear();

		Logger.debug("*��̨���ݶ���������PFBusiAction.processBatch��������ʱ" + (System.currentTimeMillis() - start) + "ms");

		return retObjs;
	}

}