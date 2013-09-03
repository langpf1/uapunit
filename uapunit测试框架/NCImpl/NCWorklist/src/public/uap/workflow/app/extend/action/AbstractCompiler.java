package uap.workflow.app.extend.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.BillTypeCacheKey;
import nc.bs.pub.compiler.IPfRun;
import nc.bs.pub.compiler.IWorkFlowRet;
import nc.bs.pub.pf.IPfPersonFilter2;
import nc.itf.uap.pf.IPFConfig;
import nc.itf.uap.pf.IPFMessage;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.change.PublicHeadVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.msg.MessageTypes;
import nc.vo.pub.msg.MessageinfoVO;
import nc.vo.pub.pf.Pfi18nTools;
import nc.vo.pub.pfflow01.BillbusinessVO;
import uap.workflow.admin.IWorkflowMachine;
import uap.workflow.app.client.PfUtilTools;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.app.extend.bizstate.AbstractBusiStateCallback;
import uap.workflow.app.extend.bizstate.PFBusiStateOfMeta;
import uap.workflow.app.vo.IPFConfigInfo;
import uap.workflow.app.vo.IPfRetCheckInfo;
import uap.workflow.app.vo.RetBackWfVo;
import uap.workflow.engine.core.WorkflowTypeEnum;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.pub.util.PfDataCache;
import uap.workflow.pub.util.PfUtilBaseTools;

/**
 * ʵ��ƽ̨������л����ӿڵ���
 * 
 * @author ���ھ� 2002-2-28
 * @modifier leijun 2005-3
 *           �޸ķ���procActionFlow()��procFlowBacth(),���Ϊ�Ƶ�������ͨ��,�򲻴���������
 * @modifier leijun 2005-6 �޸ķ���runClass(),��ȫί�и�PfUtilTools
 * @modifier leijun 2006-5 ��������ͨ������Ҫ����"��ʽ"��Ϣ
 * @modifier leijun 2009-6 ���ӹ������Ļ��˹���
 */
public class AbstractCompiler implements IPfRun, ICodeRemark {

	public AbstractCompiler() {
		super();
	}

	/**
	 * ִ������ͨ����״̬
	 */
	private void execApprovePass(PfParameterVO paraVo) throws Exception {
		try {
			if (!PfUtilBaseTools.isApproveAction(paraVo.m_actionName, paraVo.m_billType))
				return;

			// boolean hasMeta =
			// PfMetadataTools.checkBilltypeRelatedMeta(paraVo.m_billType);
			AbstractBusiStateCallback absc = new PFBusiStateOfMeta();
			absc.execApproveState(paraVo, IPfRetCheckInfo.PASSING);

		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}
	
	/**
	 * �����������������ִ��������ͨ����״̬
	 * */
	private void execApproveNOPass(PfParameterVO paraVo) throws Exception {
		try {
			if (!PfUtilBaseTools.isApproveAction(paraVo.m_actionName, paraVo.m_billType))
				return;

			AbstractBusiStateCallback absc = new PFBusiStateOfMeta();
			absc.execApproveState(paraVo, IPfRetCheckInfo.NOPASS);

		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}
	
	/**
	 * �����������������ִ�в��ص�״̬
	 * */
	private void execApproveReject(PfParameterVO paraVo) throws Exception{
		try {
			if (!PfUtilBaseTools.isApproveAction(paraVo.m_actionName, paraVo.m_billType))
				return;

			AbstractBusiStateCallback absc = new PFBusiStateOfMeta();
			absc.execApproveState(paraVo, IPfRetCheckInfo.NOSTATE);

		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	/**
	 * ������VO�л�ȡһЩ���ݣ�����billId��billNo��
	 */
	protected void getHeadInfo(PfParameterVO paraVo) {
		// ��ȡ������ʵ����ƽ̨�����Ϣ
		PublicHeadVO standHeadVo = new PublicHeadVO();

		// ����Ԫ����ģ����Ϣ����ȡ
		PfUtilBaseTools.getHeadInfoByMeta(standHeadVo, paraVo.m_preValueVo, paraVo.m_billType);

		if (StringUtil.isEmptyWithTrim(standHeadVo.businessType))
			paraVo.m_businessType = IPFConfigInfo.STATEBUSINESSTYPE;
		else
			paraVo.m_businessType = standHeadVo.businessType;
		paraVo.m_billNo = standHeadVo.billNo;
		paraVo.m_billVersionPK = standHeadVo.pkBillVersion;
		paraVo.m_billId =standHeadVo.pkBillId;
		paraVo.m_pkOrg = standHeadVo.pkOrg;
		paraVo.m_makeBillOperator = standHeadVo.operatorId;
	}

	/**
	 * ������������ת
	 * <li>������"APPROVE"�����ű�����,ʵ�ֶԵ������ݵ�������Ҳ�ɵ���procFlowBatch()��ʵ�ֶԵ������ݵ�����
	 * <li>��"SIGNAL"�����ű�����,ʵ�ֶԵ������ݵĹ�������ת
	 * 
	 * @return Object ���Ϊ�������������е�����,�򷵻�IWorkFlowRet; ���Ϊ������������ͨ��������,�򷵻�null.
	 */
	public Object procActionFlow(PfParameterVO paraVo) throws Exception {
		IWorkFlowRet retObj = null;
		int intFlag = processWorkFlow(paraVo);
		if (intFlag != IPfRetCheckInfo.PASSING) {
			// δͨ��������еĴ���
			retObj = new IWorkFlowRet();
			retObj.m_inVo = paraVo.m_preValueVo;
		} else {
			// ͨ����Ĵ���-����"��ʽ"��Ϣ lj+2006-5-30
			insertPullWorkitems(paraVo);
		}

		return retObj;
	}

	/**
	 * ����"��ʽ"��Ϣ
	 * 
	 * @param paraVo
	 */
	private void insertPullWorkitems(PfParameterVO paravo) {
		Logger.debug(">>����ͨ��������ʽ��Ϣ=" + paravo.m_billType + "��ʼ");

		// �ж��Ƿ�����ʽ��Ϣ
		boolean isNeed = false; // XXX:leijun 2009-12 �ж��Ƿ���Ҫ����������Ϣ
		BillbusinessVO condVO = new BillbusinessVO();
		condVO.setPk_businesstype(paravo.m_businessType);
		String billtype = paravo.m_billType;
		BilltypeVO billtypeVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(billtype).buildPkGroup(paravo.m_pkGroup));
		if (billtypeVO.getIstransaction() != null && billtypeVO.getIstransaction().booleanValue())
			condVO.setTranstype(billtype);
		else
			condVO.setPk_billtype(billtype);

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
			Logger.debug(">>Դ����" + paravo.m_billType + "���ɷ���������Ϣ������");
			return;
		}

		try {
			// 1.�������ε�������
			IPFConfig pfcfg = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());
			BillbusinessVO[] billbusiVOs = pfcfg.queryBillDest(paravo.m_billType, paravo.m_businessType);
			if (billbusiVOs == null || billbusiVOs.length == 0) {
				Logger.debug("��ҵ������û��Ϊ����" + paravo.m_billType + "�������ε��ݣ�����");
				return;
			}

			// 2.�������ε��ݵĹ�������
			Object checkClzInstance = PfUtilTools.getBizRuleImpl(paravo.m_billType);
			IPfPersonFilter2 filter = null;
			if (checkClzInstance instanceof IPfPersonFilter2)
				filter = (IPfPersonFilter2) checkClzInstance;
			for (int k = 0; k < billbusiVOs.length; k++) {
				BillbusinessVO destBillbusiVO = billbusiVOs[k];
				// 3.������д�������Ϣ���û�(�������� -- ����������)
				String[] hsUserPKs = pfcfg.queryForwardMessageUser(paravo.m_billType, destBillbusiVO.getPk_billtype(), paravo.m_businessType, paravo.m_preValueVo, filter);
				if (hsUserPKs == null || hsUserPKs.length == 0) {
					Logger.warn(">>�޷�������ʽ��Ϣ����Ϊ�����û�Ϊ��");
					return;
				}
				// 4.����Щ�û�����"��ʽ"��������Ϣ
				ArrayList alItems = new ArrayList();
				for (int i = 0; i < hsUserPKs.length; i++) {
					String userId = hsUserPKs[i];
					MessageinfoVO wi = new MessageinfoVO();
					wi.setPk_billtype(destBillbusiVO.getPk_billtype());
					wi.setBillid(paravo.m_billVersionPK); // ���ε���ID
					wi.setPk_srcbilltype(paravo.m_billType);
					wi.setBillno(paravo.m_billNo);

					wi.setCheckman(userId);
					// FIXME::i18n
					wi.setTitle(Pfi18nTools.i18nBilltypeName(paravo.m_billType, null) + paravo.m_billNo + NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "AbstractCompiler-000000")/*�Ѿ�����ͨ�����ɴ�����ʽ������*/ + Pfi18nTools.i18nBilltypeName(destBillbusiVO.getPk_billtype(), null));
					// wi.setPk_busitype(destBillbusiVO.getPk_businesstype());
					wi.setPk_corp(paravo.m_pkGroup);
					wi.setSenderman(paravo.m_operator);
					alItems.add(wi);
				}

				// dao.insertVOList(alItems);
				IPFMessage pfmsg = (IPFMessage) NCLocator.getInstance().lookup(IPFMessage.class.getName());
				pfmsg.insertPushOrPullMsgs((MessageinfoVO[]) alItems.toArray(new MessageinfoVO[alItems.size()]), MessageTypes.MSG_TYPE_BUSIFLOW_PULL);
			}
		} catch (Exception e) {
			// WARN::������־�쳣������Ӱ��ҵ������
			Logger.error(e.getMessage(), e);
		}
		Logger.debug(">>����ͨ��������ʽ��Ϣ=" + paravo.m_billType + "����");
	}

	/**
	 * ��������;�������ݵ�����״̬
	 * 
	 * @param paraVo
	 * @throws Exception
	 */
	public int processWorkFlow(PfParameterVO paraVo) throws Exception {
		Logger.info("****��������processWorkFlow��ʼ****"+ System.currentTimeMillis() + "ms");
		int intFlag = IPfRetCheckInfo.NOPASS;
		// lj@ 2005-3-14 ������Ƶ�������ͨ��,�򲻴�������
		if (paraVo.m_workFlow == null || paraVo.m_autoApproveAfterCommit) {
			// ִ�����ͨ�������
			execApprovePass(paraVo);
			intFlag = IPfRetCheckInfo.PASSING;
			return intFlag;
		}
		
		if (paraVo.m_workFlow != null && paraVo.m_workFlow.isAnyoneCanApprove()) {
			if ("Y".equals(paraVo.m_workFlow.getApproveresult())) {
				execApprovePass(paraVo);
				intFlag = IPfRetCheckInfo.PASSING;
			} else if ("N".equals(paraVo.m_workFlow.getApproveresult())) {
				execApproveNOPass(paraVo);
				intFlag = IPfRetCheckInfo.NOPASS;
			} else if ("R".equals(paraVo.m_workFlow.getApproveresult())) {
				execApproveReject(paraVo);
				intFlag = IPfRetCheckInfo.NOSTATE;
			}
			return intFlag;
		}
		try {
			// 1.������ǰ��
			intFlag = NCLocator.getInstance().lookup(IWorkflowMachine.class).signalWorkflow(paraVo);

			TaskInstanceVO currentTask = paraVo.m_workFlow.getTaskInstanceVO();
			int iCurrentWfType = WorkflowTypeEnum.Approveflow.getIntValue();//TODO:  currentTask.getWorkflowType();
			boolean isWorkflow = WorkflowTypeEnum.Workflow.getIntValue() == iCurrentWfType || WorkflowTypeEnum.SubWorkflow.getIntValue() == iCurrentWfType;
			if (!isWorkflow) {
				// 2.����״̬�޸ġ���ֻ������������Ҫ
				// boolean hasMeta =
				// PfMetadataTools.checkBilltypeRelatedMeta(paraVo.m_billType);
				AbstractBusiStateCallback absc = new PFBusiStateOfMeta();
				absc.execApproveState(paraVo, intFlag);
			}
		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw e;
			else {
				Throwable exp = e.getCause();
				throw new PFBusinessException(exp == null ? e.getMessage() : exp.getMessage(), e);
			}
		}
		Logger.debug(">>��ǰ���ݵ�����״̬=" + intFlag);
		Logger.info("****��������processWorkFlow����****" + System.currentTimeMillis() + "ms");
		return intFlag;
	}

	/**
	 * ������"APPROVE"�Ķ����ű�����,ʵ����������
	 * ע�⣺
	 * ��60������ƽ̨�������ں�̨��ڴ��ͱ��ֽ�Ϊ��������������зֱ���
	 * ��PFBusiAction.processBatch()����
	 * 
	 * ��ˣ������õ�ҵ���鶯���ű�ʱ���˷���ʵ���ϴ���Ľ����ǵ�����������
	 * 
	 * @return Hashtable δͨ�������صĵ���vo��paraVO.m_preValueVOs�е����������������hashTable��
	 * @throws Exception
	 */
	public Hashtable procFlowBacth(PfParameterVO paraVo) throws Exception {
		// ���������ж�(�ṩ�������ִ��)
		Hashtable<String, String> retHas = new Hashtable<String, String>();
		IWorkFlowRet retObj = null;
		int intFlag = processWorkFlow(paraVo);
		if (intFlag != IPfRetCheckInfo.PASSING) {
			// δͨ��������еĴ���
			retObj = new IWorkFlowRet();
			retObj.m_inVo = paraVo.m_preValueVo;
			
			// �򷽷�ʵ��ֻ����������(�����ں�̨�ᱻ��ɵ�����������ֱ���)
			// ����ֱ��putһ��0��ȥ
			retHas.put(String.valueOf(0), String.valueOf(0));
		} else {
			// ͨ����Ĵ���-����"��ʽ"��Ϣ lj+2006-5-30
			insertPullWorkitems(paraVo);
		}
		return retHas;
	}

	/**
	 * �������ĳ������,������ʽ����:
	 * <li>������� :"AggressVo:20,pkbillType:String"
	 * <li>�������2:"&AggressVo:key,&key:DataType",DataTypeΪ������׼����
	 * 
	 * @param className
	 * @param method
	 * @param parameter
	 * @param paraVo
	 * @param keyHas
	 * @return
	 * @throws BusinessException
	 */
	public Object runClass(String className, String method, String parameter, PfParameterVO paraVo, Hashtable keyHas) throws BusinessException {
		return PfUtilTools.runClass(className, method, parameter, paraVo, keyHas);
	}

	public Object runComClass(PfParameterVO paraVo) throws BusinessException {
		// Noop!
		return null;
	}

	/**
	 * ��������
	 * <li>������"UNAPPROVE"�����ű�����,ʵ�ֶԵ������ݵ�����;
	 * <li>������"ROLLBACK"�����ű�����,ʵ�ֶԵ������ݵĻ���;
	 * 
	 * @param paraVo
	 * @return true-���������̬���ص�����̬;false-�������
	 */
	public boolean procUnApproveFlow(PfParameterVO paraVo) throws BusinessException {
			RetBackWfVo backWfVo = NCLocator.getInstance().lookup(IWorkflowMachine.class).backCheckFlow(paraVo);
			boolean needChangeStatus = false;
			if(paraVo.m_workFlow == null) {
				//û�����̣�ֱ���ĵ�������ʱҲ��Ҫ�ı�״̬
				needChangeStatus = true;
			}else {
				int wftype = paraVo.m_workFlow.getWorkflow_type();
				//ֻ�������������������̡��������������̣�������Ҫ�ı�״̬
				if (wftype == WorkflowTypeEnum.Approveflow.getIntValue()||wftype == WorkflowTypeEnum.SubApproveflow.getIntValue()||wftype == WorkflowTypeEnum.SubWorkApproveflow.getIntValue()) {
					needChangeStatus = true;
				}
			}
			if(needChangeStatus)
				// ����ʱ���޸ĵ��ݵ�����״̬
				unApproveState(paraVo, backWfVo.getBackState(), backWfVo.getPreCheckMan());

			return backWfVo.getIsFinish().booleanValue();
	}

	/**
	 * ȡ���ύ
	 * <li>���ջ�"UNSAVE"��ȡ���ύ"RECALL"�����ű�����,ʵ�ֶԵ������ݵ�ȡ���ύ;
	 * 
	 * @param paraVo
	 * @return 
	 */
	public void procRecallFlow(PfParameterVO paraVo) throws BusinessException {
		try {
			// ȡ���ύ leijun@2008-9
			NCLocator.getInstance().lookup(IWorkflowMachine.class).backCheckFlow(paraVo);
			//�ı䵥��״̬Ϊ����̬
			unApproveState(paraVo, IPfRetCheckInfo.NOSTATE, null);
//			return backWfVo.getIsFinish().booleanValue();
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFBusinessException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * ����ʱ���޸ĵ��ݵ�����״̬
	 */
	private void unApproveState(PfParameterVO paraVo, int iBackState, String preCheckMan) throws BusinessException {
		Logger.info("****����ʱ���޸ĵ��ݵ�����״̬unApproveState��ʼ*****");
		try {
			// boolean hasMeta =
			// PfMetadataTools.checkBilltypeRelatedMeta(paraVo.m_billType);
			AbstractBusiStateCallback absc = new PFBusiStateOfMeta();
			switch (iBackState) {
				case IPfRetCheckInfo.GOINGON: {
					absc.execUnApproveState(paraVo, preCheckMan, IPfRetCheckInfo.GOINGON);
					break;
				}
				case IPfRetCheckInfo.NOSTATE: {
					paraVo.m_workFlow = null;
					absc.execUnApproveState(paraVo, null, IPfRetCheckInfo.NOSTATE);
					break;
				}
				case IPfRetCheckInfo.COMMIT: {
					absc.execUnApproveState(paraVo, null, IPfRetCheckInfo.COMMIT);
					break;
				}
			}
		} catch (BusinessException ex) {
			throw ex;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFBusinessException(ex.getMessage(), ex);
		}
		Logger.info("****����ʱ���޸ĵ��ݵ�����״̬unApproveState����*****");
	}

	public String getCodeRemark() {
		return null;
	}
}