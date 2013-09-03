package uap.workflow.ui.client;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pf.PfMessageUtil;
import nc.bs.pub.pf.PfUtilTools;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.uap.pf.busiflow.PfButtonClickContext;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pf.pub.PFClientBizRetObj;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.pf.ActionClientParams;
import nc.ui.pub.pf.DispatchDialog;
import nc.ui.pub.pf.IPFClientBizProcess;
import nc.ui.querytemplate.IBillReferQuery;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype2.Billtype2VO;
import nc.vo.pub.billtype2.ExtendedClassEnum;
import nc.vo.pub.pf.PfAddInfo;
import nc.vo.pub.pf.PfClientBizProcessContext;
import nc.vo.pub.pfflow.BillactionVO;
import nc.vo.querytemplate.TemplateInfo;

import uap.workflow.admin.IWorkflowDefine;
import uap.workflow.admin.IWorkflowMachine;
import uap.workflow.app.action.IPFActionName;
import uap.workflow.app.action.IplatFormEntry;
import uap.workflow.app.exception.PFRuntimeException;
import uap.workflow.app.exeception.FlowDefNotFoundException;
import uap.workflow.app.vo.IApproveflowConst;
import uap.workflow.app.vo.PfProcessBatchRetObject;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.core.WorkflowTypeEnum;
import uap.workflow.engine.core.XPDLNames;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.pub.util.PfDataCache;
import uap.workflow.pub.util.PfUtilBaseTools;
import uap.workflow.pub.util.ProcessDataCache;
import uap.workflow.ui.workitem.ApproveWorkitemAcceptDlg;
import uap.workflow.ui.workitem.AssignDlg;
import uap.workflow.ui.workitem.BatchApproveWorkitemAcceptDlg;
import uap.workflow.ui.workitem.WFStartDispatchDialog;
import uap.workflow.ui.workitem.WorkflowWorkitemAcceptDlg;
import uap.workflow.vo.WorkflownoteVO;


/**
 * ���̿ͻ����࣬�����̿ͻ��˵����
 */
public class PfUtilClient {

	/**
	 * �����������������true��֮false;
	 */
	private static boolean m_checkFlag = true;

	// ��ǰ��������
	private static String m_currentBillType = null;

	/** ��ǰ�����ڵ��������� */
	private static int m_iCheckResult = IApproveflowConst.CHECK_RESULT_PASS;

	private static boolean m_isOk = false;

	/** fgj2001-11-27 �жϵ�ǰ�����Ƿ�ִ�гɹ� */
	private static boolean m_isSuccess = true;

	/** Դ�������� */
	private static String m_sourceBillType = null;

	private static AggregatedValueObject m_tmpRetVo = null;

	private static AggregatedValueObject[] m_tmpRetVos = null;

	// �������Ʊ�־
	public static boolean makeFlag = false;

	//private static IPfExchangeService exchangeService;

	private static int m_classifyMode = PfButtonClickContext.NoClassify;

	protected PfUtilClient() {
	}

	private static boolean isExchange(TaskInstanceVO task) throws BusinessException {
		try {
			IProcessDefinition wf = ProcessDataCache.getWorkflowProcess(task.getProcess_def_id(), task.getPk_process_instance());
			IActivity activity = wf.findActivity(task.getActivity_id());
			Object value = activity.getProperty(XPDLNames.EXCHANGE);
			return "true".equals(value);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e);
		}
	}

	/**
	 * ��������ʱ�Ľ���
	 * �漰���ύ����ʱ,��Ҫ��ָ����Ϣ�ռ�
	 * <li>ֻ��"SAVE","START",""EDIT"�����ŵ���
	 */
	private static WorkflownoteVO checkWorkitemOnStart(Container parent, String actionName, String billType,
			AggregatedValueObject billVo, Stack dlgResult, HashMap hmPfExParams) throws BusinessException {
		WorkflownoteVO worknoteVO = new WorkflownoteVO();

		//guowl+ 2010-5,���������������ȡָ����Ϣ��ֱ�ӷ���
		if(hmPfExParams != null && hmPfExParams.get(PfUtilBaseTools.PARAM_BATCH) != null)
			return worknoteVO;
		
		worknoteVO = NCLocator.getInstance().lookup(IWorkflowMachine.class)
			.checkWorkFlow(actionName, billType, billVo, hmPfExParams);
		
		//�������������ʾ֮ǰ������ҵ����
		PFClientBizRetObj retObj = executeBusinessPlugin(parent, billVo, worknoteVO, true);
		if(retObj != null && retObj.isStopFlow()){
			m_isSuccess = false;
			return null;
		}

		if (worknoteVO != null && worknoteVO.isAssign()) {
			AssignDlg assignDlg = new AssignDlg(worknoteVO);
			assignDlg.setBounds(150, 60, 1200, 800);
			int iClose = assignDlg.showModal();
		}
		return worknoteVO;
	}
	
	/**
	 * ��鵱ǰ�����Ƿ������������У������н���
	 */
	private static WorkflownoteVO checkWorkitemWhenSignal(Container parent, String actionName, 
			String billType, AggregatedValueObject billVo, HashMap hmPfExParams) throws BusinessException {
		WorkflownoteVO noteVO = null;
		UIDialog dlg = null;
		if (hmPfExParams != null && hmPfExParams.get(PfUtilBaseTools.PARAM_BATCH) != null) {
			// Ԥ�㿪����Ҫ������ʱ������û�����̶��壬���������������
			Object notSilent = hmPfExParams.get(PfUtilBaseTools.PARAM_NOTSILENT);
			// ��鵥���Ƿ����������������û�ж��壬�򲻵���,�˴�ֻ�򵥼���һ�ŵ��ݵĵ����������Ƿ������̶���
			if (notSilent == null && !hasApproveflowDef(billType, billVo)) {
				m_checkFlag = true;
				noteVO = new WorkflownoteVO();
				noteVO.setApproveresult("Y");
				return noteVO;
			} else {
				noteVO = new WorkflownoteVO();
				dlg = new BatchApproveWorkitemAcceptDlg(parent, noteVO);
			}
		} else {
			noteVO = NCLocator.getInstance().lookup(IWorkflowMachine.class)
					.checkWorkFlow(actionName, billType, billVo, hmPfExParams);
			if (noteVO == null) {
				m_checkFlag = true;
				return noteVO;
			}

			
			dlg = new ApproveWorkitemAcceptDlg(parent, noteVO, false);
			ApproveWorkitemAcceptDlg acceptDlg = ((ApproveWorkitemAcceptDlg) dlg);

			PFClientBizRetObj retObj = executeBusinessPlugin(parent, billVo, noteVO, false);
			
			if (retObj != null) {
				// yanke1 2012-02-22 ����ǰ̨ҵ����ķ���ֵ��������������
				acceptDlg.setShowPass(retObj.isShowPass());
				acceptDlg.setShowNoPass(retObj.isShowNoPass());
				acceptDlg.setShowReject(retObj.isShowReject());
				acceptDlg.setHintMessage(retObj.getHintMessage());
				acceptDlg.setCheckNote(retObj.getHintMessage());
			}
		}

		if (hmPfExParams != null && hmPfExParams.get(PfUtilBaseTools.PARAM_WORKNOTE) != null) {
			WorkflownoteVO worknote = (WorkflownoteVO) hmPfExParams.get(PfUtilBaseTools.PARAM_WORKNOTE);
			noteVO.setApproveresult(worknote.getApproveresult());
			noteVO.setChecknote(worknote.getChecknote());
			m_checkFlag = true;
			return noteVO;
		}

		if (dlg.showModal() == UIDialog.ID_OK) { // ����û�����
			// ���������Ĺ�����
			m_checkFlag = true;
		} else { // �û�������
			m_checkFlag = false;
			noteVO = null;
		}
		
	    if (hmPfExParams == null) {
	        hmPfExParams = new HashMap<String, Object>();
	    }
	    if((!hmPfExParams.containsKey(PfUtilBaseTools.PARAM_WORKNOTE) 
	    		|| hmPfExParams.get(PfUtilBaseTools.PARAM_WORKNOTE) == null)
				&& hmPfExParams.get(PfUtilBaseTools.PARAM_BATCH) != null)
	    	hmPfExParams.put(PfUtilBaseTools.PARAM_WORKNOTE, noteVO);
		return noteVO;
	}

	private static boolean hasApproveflowDef(String billType, AggregatedValueObject billVo)throws BusinessException {
		IFlowBizItf fbi = PfMetadataTools.getBizItfImpl(billVo, IFlowBizItf.class);
		if (fbi == null)
			throw new PFRuntimeException(NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000000")/*Ԫ����ʵ��û���ṩҵ��ӿ�IFlowBizItf��ʵ����*/);

		IWorkflowDefine wfDefine = NCLocator.getInstance().lookup(IWorkflowDefine.class);
		Logger.debug("��ѯ���̶���: billType=" + billType + ";pkOrg=" + fbi.getPkorg() + ";userId=" + fbi.getBillMaker() + ";��ʼ");
		return wfDefine.hasValidProcessDef(WorkbenchEnvironment.getInstance().getGroupVO().getPk_group(), billType, fbi.getPkorg(), fbi.getBillMaker(), fbi.getEmendEnum());
	}
	
/*ҵ������
	
	/**
	 * ������Դ���ݣ�����
	 * <li>1.��ȡ��Դ���ݵĲ�ѯ�Ի��򣬲���ѯ����Դ����
	 * <li>2.��ʾ��Դ���ݣ�������ѡ��
	 * <li>3.��ȡѡ�����Դ����
	 * 
	 * @param srcBillType  �����Ƶ� ѡ�����Դ��������
	 * @param pk_group
	 * @param userId
	 * @param currBillOrTranstype ��ǰ���ݻ�������
	 * @param parent
	 * @param userObj
	 * @param srcBillId ���Ϊ�գ���ֱ�ӽ���������ε��ݽ���
	 * /
	public static void childButtonClicked(String srcBillType, String pk_group, String userId,
			String currBillOrTranstype, Container parent, Object userObj, String srcBillId) {

		childButtonClickedWithBusi(srcBillType, pk_group, userId, currBillOrTranstype, parent, userObj, srcBillId, null);
	}
	
	public static void childButtonClickedNew(final PfButtonClickContext context){
		makeFlag = false;
		if (context.getSrcBillType().toUpperCase().equals("MAKEFLAG")) {
			Logger.debug("******���Ƶ���******");
			makeFlag = true;
			return;
		}

		Logger.debug("******������Դ����******");
		m_classifyMode = context.getClassifyMode();
		try {
			final String funNode = PfUIDataCache.getBillType(new BillTypeCacheKey().buildBilltype(context.getSrcBillType()).buildPkGroup(context.getPk_group())).getNodecode();
			if (funNode == null || funNode.equals(""))
				throw new PFBusinessException(NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000001", null, new String[]{context.getSrcBillType()})/*��ע�ᵥ��{0}�Ĺ��ܽڵ��* /);

			//��ѯ������Ϣ���Ա�õ�һЩ�����Ƶ��Ķ�����Ϣ
			String src_qrytemplate = null;
			String src_billui = null; // ��Դ���ݵ���ʾUI��
			String src_nodekey = null; // ���ڲ�ѯ��Դ���ݵĲ�ѯģ��Ľڵ��ʶ
			ExchangeVO changeVO = getExchangeService().queryMostSuitableExchangeVO(context.getSrcBillType(),
					context.getCurrBilltype(), null, PfUtilUITools.getLoginGroup(), null);
			if (changeVO != null) {
				src_qrytemplate = changeVO.getSrc_qrytemplate();
				src_qrytemplate = src_qrytemplate == null ? null : src_qrytemplate.trim();
				src_billui = changeVO.getSrc_billui();
				src_nodekey = changeVO.getSrc_nodekey();
			}
			// a.��ȡ��Դ���ݵĲ�ѯ�Ի��򣬼�Ϊm_condition��ֵ
			IBillReferQuery qcDLG = null;
			if (StringUtil.isEmptyWithTrim(src_qrytemplate)) {
				Logger.debug("ʹ����Դ�������Ͷ�Ӧ�ڵ�ʹ�õĲ�ѯģ��Ի���");
				src_qrytemplate = PfUtilUITools.getTemplateId(ITemplateStyle.queryTemplate, context.getPk_group(),
						funNode, context.getUserId(), src_nodekey);
				qcDLG = setConditionClient(src_qrytemplate, context.getParent(), context.getUserId(), funNode, context.getPk_group());
			} else if (src_qrytemplate.startsWith("<")) {
				Logger.debug("ʹ�ò�Ʒ�鶨�Ƶ���Դ���ݲ�ѯ�Ի���");
				src_qrytemplate = src_qrytemplate.substring(1, src_qrytemplate.length() - 1);
				qcDLG = loadUserQuery(context.getParent(), src_qrytemplate, context.getPk_group(), context.getUserId(), funNode,
						context.getCurrBilltype(), context.getSrcBillType(), src_nodekey, context.getUserObj());
			} else {
				Logger.debug("ʹ��ע��Ĳ�ѯģ��Ĳ�ѯ�Ի���");
				qcDLG = setConditionClient(src_qrytemplate, context.getParent(), context.getUserId(), funNode, context.getPk_group());
			}
			//����ѯ�Ի�������ҵ�����ͣ�asked by scm-puqh
			if(context.getBusiTypes()!= null && qcDLG instanceof IBillReferQueryWithBusitype)
				((IBillReferQueryWithBusitype)qcDLG).setBusitypes(context.getBusiTypes());
			//����ѯ�Ի������ý������ͣ����ڹ������ε��ݣ�asked by scm-puqh
			if(context.getTransTypes()!= null && qcDLG instanceof IBillReferQueryWithTranstype)
				((IBillReferQueryWithTranstype)qcDLG).setTranstypes(context.getTransTypes());

			if (context.getSrcBillId() == null) {
				//b.��ʾ��Դ���ݵĲ�ѯ�Ի���
				if (qcDLG.showModal() == UIDialog.ID_OK) {
					// c.��ʾ��Դ���ݣ�������ѡ��
					refBillSource(context.getPk_group(), funNode, context.getUserId(), context.getCurrBilltype(), context.getParent(), context.getUserObj(),
							context.getSrcBillType(), src_qrytemplate, src_billui, src_nodekey, null, qcDLG);
				} else {
					m_isOk = false;
					return;
				}
			} else {
				//b'.ֱ����ʾ��Դ����
				refBillSource(context.getPk_group(), funNode, context.getUserId(), context.getCurrBilltype(), context.getParent(), context.getUserObj(), context.getSrcBillType(),
						src_qrytemplate, src_billui, src_nodekey, context.getSrcBillId(), qcDLG);
				return;
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			MessageDialog.showErrorDlg(context.getParent(), "null", NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000002", null, new String[]{ex.getMessage()})/*�������ε��ݳ����쳣={0}* /);
		}
	}

	/**
	 * ������Դ���ݣ�����
	 * <li>1.��ȡ��Դ���ݵĲ�ѯ�Ի��򣬲���ѯ����Դ����
	 * <li>2.��ʾ��Դ���ݣ�������ѡ��
	 * <li>3.��ȡѡ�����Դ����
	 * 
	 * @param srcBillType  �����Ƶ� ѡ�����Դ��������
	 * @param pk_group
	 * @param userId
	 * @param currBillOrTranstype ��ǰ���ݻ�������
	 * @param parent
	 * @param userObj
	 * @param srcBillId ���Ϊ�գ���ֱ�ӽ���������ε��ݽ���
	 * @param busiTypes �����Ϊ�գ���ʾ����ָ��ҵ�������еĵ���
	 * /
	public static void childButtonClickedWithBusi(String srcBillType, String pk_group, String userId,
			String currBillOrTranstype, Container parent, Object userObj, String srcBillId, List<String> busiTypes) {

		m_classifyMode = PfButtonClickContext.NoClassify;
		makeFlag = false;
		if (srcBillType.toUpperCase().equals("MAKEFLAG")) {
			Logger.debug("******���Ƶ���******");
			makeFlag = true;
			return;
		}

		Logger.debug("******������Դ����******");
		try {
			String funNode = PfUIDataCache.getBillType(new BillTypeCacheKey().buildBilltype(srcBillType).buildPkGroup(pk_group)).getNodecode();
			if (funNode == null || funNode.equals(""))
				throw new PFBusinessException(NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000001", null, new String[]{srcBillType})/*��ע�ᵥ��{0}�Ĺ��ܽڵ��* /);

			//��ѯ������Ϣ���Ա�õ�һЩ�����Ƶ��Ķ�����Ϣ
			String src_qrytemplate = null;
			String src_billui = null; // ��Դ���ݵ���ʾUI��
			String src_nodekey = null; // ���ڲ�ѯ��Դ���ݵĲ�ѯģ��Ľڵ��ʶ
			ExchangeVO changeVO = getExchangeService().queryMostSuitableExchangeVO(srcBillType,
					currBillOrTranstype, null, PfUtilUITools.getLoginGroup(), null);
			if (changeVO != null) {
				src_qrytemplate = changeVO.getSrc_qrytemplate();
				src_qrytemplate = src_qrytemplate == null ? null : src_qrytemplate.trim();
				src_billui = changeVO.getSrc_billui();
				src_nodekey = changeVO.getSrc_nodekey();
			}
			// a.��ȡ��Դ���ݵĲ�ѯ�Ի��򣬼�Ϊm_condition��ֵ
			IBillReferQuery qcDLG = null;
			if (StringUtil.isEmptyWithTrim(src_qrytemplate)) {
				Logger.debug("ʹ����Դ�������Ͷ�Ӧ�ڵ�ʹ�õĲ�ѯģ��Ի���");
				src_qrytemplate = PfUtilUITools.getTemplateId(ITemplateStyle.queryTemplate, pk_group,
						funNode, userId, src_nodekey);
				qcDLG = setConditionClient(src_qrytemplate, parent, userId, funNode, pk_group);
			} else if (src_qrytemplate.startsWith("<")) {
				Logger.debug("ʹ�ò�Ʒ�鶨�Ƶ���Դ���ݲ�ѯ�Ի���");
				src_qrytemplate = src_qrytemplate.substring(1, src_qrytemplate.length() - 1);
				qcDLG = loadUserQuery(parent, src_qrytemplate, pk_group, userId, funNode,
						currBillOrTranstype, srcBillType, src_nodekey, userObj);
			} else {
				Logger.debug("ʹ��ע��Ĳ�ѯģ��Ĳ�ѯ�Ի���");
				qcDLG = setConditionClient(src_qrytemplate, parent, userId, funNode, pk_group);
			}
			if(busiTypes != null && qcDLG instanceof IBillReferQueryWithBusitype)
				((IBillReferQueryWithBusitype)qcDLG).setBusitypes(busiTypes);

			if (srcBillId == null) {
				//b.��ʾ��Դ���ݵĲ�ѯ�Ի���
				if (qcDLG.showModal() == UIDialog.ID_OK) {
					// c.��ʾ��Դ���ݣ�������ѡ��
					refBillSource(pk_group, funNode, userId, currBillOrTranstype, parent, userObj,
							srcBillType, src_qrytemplate, src_billui, src_nodekey, null, qcDLG);

				} else {
					m_isOk = false;
					return;
				}
			} else {
				//b'.ֱ����ʾ��Դ����
				refBillSource(pk_group, funNode, userId, currBillOrTranstype, parent, userObj, srcBillType,
						src_qrytemplate, src_billui, src_nodekey, srcBillId, qcDLG);
				return;
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			MessageDialog.showErrorDlg(parent, NCLangRes.getInstance().getStrByID("pfworkflow1", "SplitRuleRegisterUI-000005")/*����* /, NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000002", null, new String[]{ex.getMessage()})/*�������ε��ݳ����쳣={0}* /);
		}
	}

	/**
	 * �����Ƶ�ʱ����ʾ��Դ���ݣ�������VO����
	 * /
	private static void refBillSource(String pk_group, String funNode, String pkOperator,
			String currBillOrTranstype, Container parent, Object userObj, String billType,
			String strQueryTemplateId, String src_billui, String srcNodekey, String sourceBillId,
			IBillReferQuery qcDLG) throws Exception {
		// ��ȡ����Ĺؼ��ֶ�
		String pkField = PfUtilBaseTools.findPkField(billType);
		if (pkField == null || pkField.trim().length() == 0)
			throw new PFBusinessException(NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000003")/*�޷�ͨ���������ͻ�ȡ����ʵ�������PK�ֶ�* /);
		String whereString = null;
		IQueryScheme qryScheme = null;
		if (sourceBillId == null) {
			if(qcDLG instanceof IBillReferQueryWithScheme) {
				qryScheme = ((IBillReferQueryWithScheme)qcDLG).getQueryScheme();
			}else
				whereString = qcDLG.getWhereSQL();
		} else
			whereString = pkField + "='" + sourceBillId + "'";

		//������Դ����չ�ֶԻ��򣬲���ʾ
		BillSourceVar bsVar = new BillSourceVar();
		bsVar.setBillType(billType);
		bsVar.setCurrBillOrTranstype(currBillOrTranstype);
		bsVar.setFunNode(funNode);
		bsVar.setNodeKey(srcNodekey);
		bsVar.setPk_group(pk_group);
		bsVar.setPkField(pkField);
		bsVar.setQryTemplateId(strQueryTemplateId);
		bsVar.setUserId(pkOperator);
		bsVar.setUserObj(userObj);
		bsVar.setWhereStr(whereString);
		if(qryScheme!= null)
			bsVar.setQueryScheme(qryScheme);

		AbstractBillSourceDLG bsDlg = null;
		if (!StringUtil.isEmptyWithTrim(src_billui)) {
			Logger.debug("��Ʒ�鶨�Ƶ���Դ����չ�ֶԻ��򣬱���̳���" + AbstractBillSourceDLG.class.getName());
			bsDlg = loadCustomBillSourceDLG(src_billui, bsVar, parent);
		} else {
			Logger.debug("ʹ��ͨ�õ���Դ����չ�ֶԻ���");
			bsDlg = new BillSourceDLG(parent, bsVar);
		}

		bsDlg.setQueyDlg(qcDLG); //�����ѯģ��Ի���
		// ����ģ��
		bsDlg.addBillUI();
		// ��������
		bsDlg.loadHeadData();

		if (bsDlg.showModal() == UIDialog.ID_OK) {
			m_sourceBillType = billType;
			m_currentBillType = currBillOrTranstype;
			m_tmpRetVo = bsDlg.getRetVo();
			m_tmpRetVos = bsDlg.getRetVos();
			m_isOk = true;
		} else {
			m_isOk = false;
		}
	}
	*/

	/**
	 * ���� ��ǰ�����ڵ�Ĵ����� lj+ 2005-1-20
	 */
	public static int getCurrentCheckResult() {
		return m_iCheckResult;
	}

	/**
	 * ���� �û�ѡ���VO
	 */
	public static AggregatedValueObject getRetOldVo() {
		return m_tmpRetVo;
	}

	/**
	 * ���� �û�ѡ��VO����.
	 */
	public static AggregatedValueObject[] getRetOldVos() {
		return m_tmpRetVos;
	}


/*
ҵ������

	/**
	 * ���� �û�ѡ���VO�򽻻�����VO
	 * 
	 * @return
	 * /
	public static AggregatedValueObject getRetVo() {
		try {
			// ��Ҫ����VO����
			m_tmpRetVo = getExchangeService().runChangeData(m_sourceBillType, m_currentBillType, m_tmpRetVo, null);
			jumpBusitype(m_tmpRetVo==null?null:new AggregatedValueObject[]{m_tmpRetVo});
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFRuntimeException(NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000004", null, new String[]{ex.getMessage()})/*VO��������{0}* /, ex);
		}
		return m_tmpRetVo;
	}

	private static IPfExchangeService getExchangeService() {
		if(exchangeService==null)
			exchangeService = NCLocator.getInstance().lookup(IPfExchangeService.class);
		return exchangeService;
	}

	/**
	 * ���� �û�ѡ���VO�򽻻�����VO
	 * 
	 * @return
	 * /
	private static AggregatedValueObject[] changeVos(AggregatedValueObject[] vos, int classifyMode) {
		AggregatedValueObject[] tmpRetVos = null;

		try {
			tmpRetVos = getExchangeService().runChangeDataAryNeedClassify(m_sourceBillType, m_currentBillType, vos, null, classifyMode);
		} catch (BusinessException ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFRuntimeException(NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000004", null, new String[]{ex.getMessage()})/*VO��������{0}* /, ex);
		}

		return tmpRetVos;
	}
	
	/**
	 * ���� �û�ѡ��VO����򽻻�����VO����
	 * 
	 * @return
	 * @throws BusinessException 
	 * /
	public static AggregatedValueObject[] getRetVos() throws BusinessException {
		// ��Ҫ����VO����
		m_tmpRetVos = changeVos(m_tmpRetVos, m_classifyMode);
		jumpBusitype(m_tmpRetVos);
		return m_tmpRetVos;
	}

	
	/**
	 * ҵ������ת
	 * @throws BusinessException 
	 * * /
	private static void jumpBusitype(AggregatedValueObject[] vos) throws BusinessException{
		if(vos==null||vos.length==0)
			return;
		IFlowBizItf fbi = PfMetadataTools.getBizItfImpl(vos[0], IFlowBizItf.class);
		//δʵ��ҵ�����ӿڵĵ��ݻ���δ������ֱ��return
		if(fbi==null||StringUtil.isEmptyWithTrim(fbi.getBusitype()))
			return;
		BillbusinessVO condVO = new BillbusinessVO();
		condVO.setPk_businesstype(fbi.getBusitype());
		condVO.setJumpflag(UFBoolean.TRUE);
		//�õ��������ͱ���
		String billtype =PfUtilBaseTools.getRealBilltype(m_currentBillType);
		condVO.setPk_billtype(billtype);
		condVO.setTranstype(fbi.getTranstype());
		try {
			Collection co = NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieve(condVO, true);
			if(co.size()>0){
				HashMap<String, String> busitypeMaps = new HashMap<String, String>();
				for(AggregatedValueObject vo:vos){
					String destBusitypePk = null;
					fbi = PfMetadataTools.getBizItfImpl(vo, IFlowBizItf.class);
					String transtype =fbi.getTranstype();
					String pk_org=fbi.getPkorg();
					String operator =PfUtilUITools.getLoginUser();
					if(StringUtil.isEmptyWithTrim(billtype)){
						//�������Ͳ���Ϊ��
						continue;
					}
					String key =billtype+(StringUtil.isEmptyWithTrim(transtype)?"":transtype)+(StringUtil.isEmptyWithTrim(pk_org)?"":pk_org)
							   +operator;
					if(busitypeMaps.containsKey(key)){
						destBusitypePk = busitypeMaps.get(key);
					}else{
						destBusitypePk = NCLocator.getInstance().lookup(IPFConfig.class).retBusitypeCanStart(billtype, transtype, pk_org, operator);
					}
					//���û���ҵ�Ҫ��ת��ҵ����
					if(StringUtil.isEmptyWithTrim(destBusitypePk)){
						continue;
					}
					fbi.setBusitype(destBusitypePk);
				}
			}
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
		}	
	}
*/
	
	/**
	 * �ж��û��Ƿ����ˣ�ȡ������ť
	 * 
	 * @return boolean leijun+
	 */
	public static boolean isCanceled() {
		return !m_checkFlag;
	}

	/**
	 * ���� ���յ����Ƿ������ر�
	 * 
	 * @return boolean
	 */
	public static boolean isCloseOK() {
		return m_isOk;
	}

	/**
	 * ���� ��ǰ���ݶ���ִ���Ƿ�ɹ�
	 * 
	 * @return boolean
	 */
	public static boolean isSuccess() {
		return m_isSuccess;
	}

	/*
	ҵ������
	/**
	 * �����Ʒ��ע�����Դ������ʾ�Ի���
	 * /
	private static AbstractBillSourceDLG loadCustomBillSourceDLG(String src_billui,
			BillSourceVar bsVar, Container parent) {
		Class c = null;

		try {
			c = Class.forName(src_billui);
			Class[] argCls = new Class[] { Container.class, BillSourceVar.class };
			Object[] args = new Object[] { parent, bsVar };
			// ʵ�������ι��췽��
			Constructor cc = c.getConstructor(argCls);
			return (AbstractBillSourceDLG) cc.newInstance(args);
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			MessageDialog.showErrorDlg(parent, null, NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000005", null, new String[]{ex.getMessage()})/*�����Ʒ��ע�����Դ������ʾ�Ի��� �����쳣��{0}* /);
		}
		return null;
	}

	private static IBillReferQuery loadUserQuery(Container parent, String src_qrytemplate,
			String pk_group, String userId, String FunNode, String currBillOrTranstype,
			String sourceBillType, String nodeKey, Object userObj) {

		Class c = null;
		try {
			// ���ж��Ƿ�Ϊ�²�ѯģ��UI������
			String qtId = PfUtilUITools.getTemplateId(ITemplateStyle.queryTemplate, pk_group, FunNode,
					userId, nodeKey);
			TemplateInfo ti = new TemplateInfo();
			ti.setTemplateId(qtId);
			ti.setPk_Org(pk_group);
			ti.setUserid(userId);
			ti.setCurrentCorpPk(pk_group);
			ti.setFunNode(FunNode);
			ti.setNodekey(nodeKey);

			c = Class.forName(src_qrytemplate);
			Object retObj = c.getConstructor(new Class[] { Container.class, TemplateInfo.class })
					.newInstance(new Object[] { parent, ti });
			//�Բ�ѯģ��Ի����һЩ���Ƴ�ʼ��
			if (retObj instanceof IinitQueryData) {
				((IinitQueryData) retObj).initData(pk_group, userId, FunNode, currBillOrTranstype,
						sourceBillType, nodeKey, userObj);
			}

			return (IBillReferQuery) retObj;
		} catch (NoSuchMethodException e) {
			Logger.warn("�Ҳ����²�ѯģ��UI�Ĺ��췽���������ж��Ƿ����ϲ�ѯģ��Ĺ��췽��", e);
			try {
				// Ӧ��Ϊ�ϲ�ѯģ��UI������
				Object retObj = c.getConstructor(new Class[] { Container.class }).newInstance(
						new Object[] { parent });
				//�Բ�ѯģ��Ի����һЩ���Ƴ�ʼ��
				if (retObj instanceof IinitQueryData) {
					((IinitQueryData) retObj).initData(pk_group, userId, FunNode, currBillOrTranstype,
							sourceBillType, nodeKey, userObj);
				}
				return (IBillReferQuery) retObj;
			} catch (Exception ex) {
				Logger.error(ex.getMessage(), ex);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return null;
	}
	*/
	
	/**
	 * ǰ̨���ݶ�������API���㷨���£�
	 * <li>1.����ִ��ǰ��ʾ�Լ���ǰ��������û�ȡ�����򷽷�ֱ�ӷ���
	 * <li>2.�鿴��չ�������ж��Ƿ���Ҫ��������ش������Ϊ�ύ�������������Ҫ�ռ��ύ�˵�ָ����Ϣ��
	 * ���Ϊ�����������������Ҫ�ռ������˵�������Ϣ
	 * <li>3.��ִ̨�ж����������ض���ִ�н���� 
	 * 
	 * @param parent ������
	 * @param actionCode �������룬����"SAVE"
	 * @param billOrTranstype ���ݣ����ף�����PK
	 * @param billvo ���ݾۺ�VO
	 * @param userObj �û��Զ������
	 * @param checkVo У�鵥�ݾۺ�VO
	 * @param eParam ��չ����
	 * @return ��������ķ��ؽ��
	 * @throws BusinessException 
	 * @throws Exception
	 * @since 5.5
	 */
	public static Object runAction(Container parent, String actionCode, String billOrTranstype,
			AggregatedValueObject billvo, Object userObj, String strBeforeUIClass,
			AggregatedValueObject checkVo, HashMap eParam) throws BusinessException {
		Logger.debug("*���ݶ������� ��ʼ");
		debugParams(actionCode, billOrTranstype, billvo, userObj);
		long start = System.currentTimeMillis();
		m_isSuccess = true;

		// 1.����ִ��ǰ��ʾ
		boolean isContinue = hintBeforeAction(parent, actionCode, billOrTranstype);
		if (!isContinue) {
			Logger.debug("*����ִ��ǰ��ʾ������");
			m_isSuccess = false;
			return null;
		}

		// 2.�鿴��չ�������Ƿ�Ҫ���̽�������
		WorkflownoteVO worknoteVO = null;
		Object paramSilent = getParamFromMap(eParam, PfUtilBaseTools.PARAM_SILENTLY);
		Object paramNoflow = getParamFromMap(eParam, PfUtilBaseTools.PARAM_NOFLOW);

		if (paramNoflow == null && paramSilent == null) {
			// �����̴�����н�������
			if (PfUtilBaseTools.isSaveAction(actionCode, billOrTranstype)
					|| PfUtilBaseTools.isApproveAction(actionCode, billOrTranstype)
					|| PfUtilBaseTools.isStartAction(actionCode, billOrTranstype)
					|| PfUtilBaseTools.isSignalAction(actionCode, billOrTranstype)) {
				// ��������������
				worknoteVO = WorkflowInteraction(parent, actionCode, billOrTranstype, billvo, eParam);
				if (!m_isSuccess)
					return null;
			} 
		}
		//����û��ڽ�������ʱ��ѡ���ǩ���߸��ɣ���������������@2009-5
		if (worknoteVO != null && isAddApproverOrAppoint(worknoteVO)) {
			m_isSuccess = false;
			return null;
		}
		if (worknoteVO == null) {
			//��鲻����������̨�����ٴμ��
			if (eParam == null)
				eParam = new HashMap<String, String>();
			if (paramSilent == null)
				eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED, PfUtilBaseTools.PARAM_NOTE_CHECKED);
		}

		// 4.��ִ̨�ж���
		Object retObj = null;

		Logger.debug("*��̨�������� ��ʼ");
		long start2 = System.currentTimeMillis();
		IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator.getInstance().lookup(IplatFormEntry.class.getName());
		retObj = iIplatFormEntry.processAction(actionCode, billOrTranstype, worknoteVO, billvo,userObj, eParam);
		Logger.debug("*��̨�������� ����=" + (System.currentTimeMillis() - start2) + "ms");

		m_isSuccess = true;

		// 5.���ض���ִ��
		//retObjRun(parent, retObj);
		Logger.debug("*���ݶ������� ����=" + (System.currentTimeMillis() - start) + "ms");
		
/*	to do	
		if(m_isSuccess && worknoteVO != null) {
			//������Ϣ��״̬
			PfMessageUtil.setHandledWithProcessor(worknoteVO);
		}
*/
		return retObj;
	}

	private static Object getParamFromMap(HashMap eParam, String paramKey) {
		return eParam == null ? null : eParam.get(paramKey);
	}

	private static boolean isAddApproverOrAppoint(WorkflownoteVO worknoteVO) {
		if(worknoteVO.getTaskInstanceVO() == null)
			return false;
		
		return (worknoteVO.getAfterAddSignCtx().getAddSingUsers()!=null ||
		worknoteVO.getBeforeAddSignCtx().getAddSingUsers()!=null ||
		worknoteVO.getAssignableInfos().size()>0);
	}

	/**
	 * ���̽������������������͹���������������ת
	 * @throws BusinessException 
	 */
	private static WorkflownoteVO WorkflowInteraction(Container parent, String actionName,
			String billType, AggregatedValueObject billvo, HashMap eParam) throws BusinessException {
		WorkflownoteVO worknoteVO = null;

		if (PfUtilBaseTools.isSaveAction(actionName, billType) 
				|| PfUtilBaseTools.isStartAction(actionName, billType)) {
			Logger.debug("*�ύ����=" + actionName + "�����������");
			// ���Ϊ�ύ������������Ҫ�ռ��ύ�˵�ָ����Ϣ������ͳһ�������� lj@2005-4-8
			Stack dlgResult = new Stack();
			worknoteVO = checkWorkitemOnStart(parent, IPFActionName.SAVE, billType, billvo, dlgResult, eParam);
			if (dlgResult.size() > 0) {
				m_isSuccess = false;
				Logger.debug("*�û�ָ��ʱ�����ȡ������ֹͣ����");
			}
		} else if (PfUtilBaseTools.isApproveAction(actionName, billType)
				|| PfUtilBaseTools.isSignalAction(actionName, billType)) {
			Logger.debug("*��������=" + actionName + "�����������");
			// ���õ����Ƿ����������У����ռ������˵�������Ϣ
			worknoteVO = checkWorkitemWhenSignal(parent, actionName, billType, billvo, eParam);
			if (worknoteVO != null) {
				if ("Y".equals(worknoteVO.getApproveresult())) {
					m_iCheckResult = IApproveflowConst.CHECK_RESULT_PASS;
				} else if("R".equals(worknoteVO.getApproveresult())) {
					/* todo
					// XXX::����Ҳ��Ϊ����ͨ����һ��,��Ҫ�����ж� lj+
					TaskInstanceVO currTask = worknoteVO.getTaskInfo().getTask();
					if (currTask != null && currTask.getCreate_type() == TaskInstanceCreateType.Reject.getIntValue()) {
						if (currTask.isBackToFirstActivity())
							m_iCheckResult = IApproveflowConst.CHECK_RESULT_REJECT_FIRST;
						else
							m_iCheckResult = IApproveflowConst.CHECK_RESULT_REJECT_LAST;
					}
					*/
				} else
					m_iCheckResult = IApproveflowConst.CHECK_RESULT_NOPASS;
			} else if (!m_checkFlag) {
				m_isSuccess = false;
				Logger.debug("*�û�����ʱ�����ȡ������ֹͣ����");
			}
		}
		return worknoteVO;
	}
	
	private static PFClientBizRetObj executeBusinessPlugin(Container parent, AggregatedValueObject billVo, WorkflownoteVO workflownoteVO, boolean isMakeBill) {
		if(workflownoteVO != null && workflownoteVO.getApplicationArgs() != null && workflownoteVO.getApplicationArgs().size()>0){
			String billtype = workflownoteVO.getTaskInstanceVO().getPk_bizobject();
			ArrayList<Billtype2VO> bt2VOs = PfDataCache.getBillType2Info(billtype,
					ExtendedClassEnum.PROC_CLIENT.getIntValue());

			//ʵ����
			for (Iterator iterator = bt2VOs.iterator(); iterator.hasNext();) {
				Billtype2VO bt2VO = (Billtype2VO) iterator.next();
				try {
					Object obj = PfUtilTools.findBizImplOfBilltype(billtype, bt2VO.getClassname());
					PfClientBizProcessContext context = new PfClientBizProcessContext();
					context.setBillvo(billVo);
					context.setArgsList(workflownoteVO.getApplicationArgs());
					context.setMakeBill(isMakeBill);
					return ((IPFClientBizProcess)obj).execute(parent, context);
				} catch (Exception e) {
					Logger.error("�޷�ʵ����ǰ̨ҵ������billType=" + billtype + ",className=" + bt2VO.getClassname(),
							e);
				}
			}
		}
		return null;
		
	}

	/**
	 * ��־һ�¶�������������Ĳ���
	 */
	private static void debugParams(String actionCode, String billType, Object billEntity,
			Object userObj) {
		Logger.debug("*********************************************");
		Logger.debug("* actionCode=" + actionCode);
		Logger.debug("* billType=" + billType);
		Logger.debug("* billEntity=" + billEntity);
		Logger.debug("* userObj=" + userObj);
		Logger.debug("*********************************************");
	}

	/**
	 * ����ִ��ǰ��ʾ
	 */
	private static boolean hintBeforeAction(Container parent, String actionName, String billType) {
		ActionClientParams acp = new ActionClientParams();
		acp.setUiContainer(parent);
		acp.setActionCode(actionName);
		acp.setBillType(billType);

		return PfUtilUITools.beforeAction(acp);
	}
	
	/**
	 * ǰ̨���ݶ���������API���㷨���£�
	 * <li>1.����ִ��ǰ��ʾ�Լ���ǰ��������û�ȡ�����򷽷�ֱ�ӷ���
	 * <li>2.�鿴��չ�������ж��Ƿ���Ҫ��������ش������Ϊ�ύ�������ҵ���VO������ֻ��һ�ŵ���ʱ������Ҫ�ռ��ύ�˵�ָ����Ϣ��
	 * ���Ϊ��������������Ե�һ�ŵ��ݿ�����Ҫ�ռ������˵�������Ϣ
	 * <li>3.��ִ̨���������������ض���ִ�н���� 
	 * 
	 * @param parent ������
	 * @param actionCode �������룬����"SAVE"
	 * @param billOrTranstype �������ͣ��������ͣ�PK
	 * @param voAry ���ݾۺ�VO����
	 * @param userObjAry �û��Զ����������
	 * @param eParam ��չ����
	 * @return ����������ķ��ؽ��
	 * @throws Exception
	 * @since 5.5
	 */
	public static Object[] runBatch(Container parent, String actionCode, String billOrTranstype,
			AggregatedValueObject[] voAry, Object[] userObjAry, String strBeforeUIClass, HashMap eParam)
			throws Exception {
		Logger.debug("*���ݶ��������� ��ʼ");
		debugParams(actionCode, billOrTranstype, voAry, userObjAry);
		long start = System.currentTimeMillis();
		if(voAry!= null && voAry.length == 1) {
			Object obj = runAction(parent, actionCode, billOrTranstype, voAry[0], userObjAry, strBeforeUIClass, null, eParam);
			Object[] ret = null;
			ret = PfUtilBaseTools.composeResultAry(obj,1,0,ret);
			return ret;
		}
		
		m_isSuccess = true;

		// 1.����ִ��ǰ��ʾ�Լ���ǰ����
		boolean isContinue = beforeProcessBatchAction(parent, actionCode, billOrTranstype);
		if (!isContinue) {
			Logger.debug("*����ִ��ǰ��ʾ�Լ���ǰ��������");
			m_isSuccess = false;
			return null;
		}

		WorkflownoteVO workflownote = null;
		
		// 2.�鿴��չ�������Ƿ�Ҫ���̽�������
		Object paramNoflow = getParamFromMap(eParam, PfUtilBaseTools.PARAM_NOFLOW);
		Object paramSilent = getParamFromMap(eParam, PfUtilBaseTools.PARAM_SILENTLY);
		if (paramNoflow == null && paramSilent == null) {
			//XXX:guowl,2010-5,����ʱ����������Ի�����ֻ��ʾ��׼������׼�����أ��ʴ���һ�������������
			//������˵�VO���鳤��Ϊ1��ͬ��������һ��
			if (voAry != null && voAry.length > 1) {
				eParam = putParam(eParam, PfUtilBaseTools.PARAM_BATCH, PfUtilBaseTools.PARAM_BATCH);
			}
			if (PfUtilBaseTools.isSaveAction(actionCode, billOrTranstype) || PfUtilBaseTools.isApproveAction(actionCode, billOrTranstype)) {
				//��������������
				workflownote = WorkflowInteraction(parent, actionCode, billOrTranstype, voAry[0], eParam);
				if (!m_isSuccess)
					return null;
			} else if (PfUtilBaseTools.isStartAction(actionCode, billOrTranstype) || PfUtilBaseTools.isSignalAction(actionCode, billOrTranstype)) {
				//��������������
				workflownote = WorkflowInteraction(parent, actionCode, billOrTranstype, voAry[0], eParam);
				if (!m_isSuccess)
					return null;
			}
		}

		// 3.��̨��������
		Logger.debug("*��̨���������� ��ʼ");
		Object retObj = NCLocator.getInstance().lookup(IplatFormEntry.class).processBatch(actionCode,
				billOrTranstype, workflownote, voAry, userObjAry, eParam);
		if(retObj instanceof PfProcessBatchRetObject) {
			String errMsg = ((PfProcessBatchRetObject)retObj).getExceptionMsg();
			if(!StringUtil.isEmptyWithTrim(errMsg))
				MessageDialog.showErrorDlg(parent, null, errMsg);
			retObj = ((PfProcessBatchRetObject)retObj).getRetObj();
		}
		if(retObj != null && ((Object[]) retObj).length > 0) {
			//������ʱ����һ���ɹ��ľ���Ϊ�ɹ�
			m_isSuccess = true;
		}
		Logger.debug("*���ݶ��������� ����=" + (System.currentTimeMillis() - start) + "ms");
		
		return (Object[]) retObj;
	}
	
	public static PfProcessBatchRetObject runBatchNew(Container parent, String actionCode, String billOrTranstype,
			AggregatedValueObject[] voAry, Object[] userObjAry, String strBeforeUIClass, HashMap eParam)
			throws Exception {
		Logger.debug("*���ݶ��������� ��ʼ");
		debugParams(actionCode, billOrTranstype, voAry, userObjAry);
		long start = System.currentTimeMillis();
		if(voAry!= null && voAry.length == 1) {
			Object obj = runAction(parent, actionCode, billOrTranstype, voAry[0], userObjAry, strBeforeUIClass, null, eParam);
			Object[] retObj = null;
			retObj = PfUtilBaseTools.composeResultAry(obj,1,0,retObj);
			return new PfProcessBatchRetObject(retObj, null);
		}
		
		m_isSuccess = true;

		// 1.����ִ��ǰ��ʾ�Լ���ǰ����
		boolean isContinue = beforeProcessBatchAction(parent, actionCode, billOrTranstype);
		if (!isContinue) {
			Logger.debug("*����ִ��ǰ��ʾ�Լ���ǰ��������");
			m_isSuccess = false;
			return null;
		}

		// 2.�鿴��չ�������Ƿ�Ҫ���̽�������
		WorkflownoteVO workflownote = null;//(WorkflownoteVO)getParamFromMap(eParam, PfUtilBaseTools.PARAM_WORKNOTE);
//		if(workflownote == null){
			Object paramNoflow = getParamFromMap(eParam, PfUtilBaseTools.PARAM_NOFLOW);
			Object paramSilent = getParamFromMap(eParam, PfUtilBaseTools.PARAM_SILENTLY);
			if (paramNoflow == null && paramSilent == null) {
				//XXX:guowl,2010-5,����ʱ����������Ի�����ֻ��ʾ��׼������׼�����أ��ʴ���һ�������������
				//������˵�VO���鳤��Ϊ1��ͬ��������һ��
				if (voAry != null && voAry.length > 1) {
					eParam = putParam(eParam, PfUtilBaseTools.PARAM_BATCH, PfUtilBaseTools.PARAM_BATCH);
				}
				if (PfUtilBaseTools.isSaveAction(actionCode, billOrTranstype) || PfUtilBaseTools.isApproveAction(actionCode, billOrTranstype)) {
					//��������������
					workflownote = WorkflowInteraction(parent, actionCode, billOrTranstype, voAry[0], eParam);
					if (!m_isSuccess)
						return null;
				} else if (PfUtilBaseTools.isStartAction(actionCode, billOrTranstype) || PfUtilBaseTools.isSignalAction(actionCode, billOrTranstype)) {
					//��������������
					workflownote = WorkflowInteraction(parent, actionCode, billOrTranstype, voAry[0], eParam);
					if (!m_isSuccess)
						return null;
				}
//				putParam(eParam, PfUtilBaseTools.PARAM_WORKNOTE, workflownote);
			}
		//}
		// 3.��̨��������
		Logger.debug("*��̨���������� ��ʼ");
		Object retObj = NCLocator.getInstance().lookup(IplatFormEntry.class).processBatch(actionCode,
				billOrTranstype, workflownote, voAry, userObjAry, eParam);
		Logger.debug("*���ݶ��������� ����=" + (System.currentTimeMillis() - start) + "ms");
		
		return (PfProcessBatchRetObject)retObj;
	}
	
	private static HashMap putParam(HashMap eParam, String paramKey, Object value) {
		if(eParam == null) {
			eParam = new HashMap();
		}
		eParam.put(paramKey, value);
		return eParam;
	}

	/**
	 * ����ִ��ǰ��ʾ�Լ���ǰ����
	 */
	private static boolean beforeProcessBatchAction(Container parent, String actionName,
			String billType)
			throws Exception {

		ActionClientParams acp = new ActionClientParams();
		acp.setUiContainer(parent);
		acp.setActionCode(actionName);
		acp.setBillType(billType);

		return PfUtilUITools.beforeAction(acp);
	}

	/**
	 * ����ĳ�����ݻ������Ϳ�ʹ�õġ������������˵���Ϣ
	 * <li>�������ơ���Դ����
	 * 
	 * @param billtype
	 * @param transtype ���û�н������ͣ��ɿ� 
	 * @param pk_group ĳ����PK
	 * @param userId ĳ�û�PK
	 * @param includeBillType �Ƿ�����������͵���Դ������Ϣ��ֻ����transtype�ǿյ����
	 * @return
	 * @throws BusinessException 
	 */
	
	/* todo:
	public static PfAddInfo[] retAddInfo(String billtype, String transtype, String pk_group,
			String userId, boolean includeBillType) throws BusinessException {

		return NCLocator.getInstance().lookup(IPFConfig.class).retAddInfo(billtype, transtype,
				pk_group, userId, includeBillType);
	}

*/
	/**
	 * ����ĳ���û���ĳ�����ݻ������ͣ���ĳ��֯�� ��������ҵ������
	 * @param billtype
	 * @param transtype ���û�н������ͣ��ɿ� 
	 * @param pk_org ĳ��֯PK
	 * @param userId ĳ�û�PK
	 * @return
	 * @throws BusinessException
	 */
	/* todo:
	public static String retBusitypeCanStart(String billtype, String transtype, String pk_org,
			String userId) throws BusinessException {

		return NCLocator.getInstance().lookup(IPFConfig.class).retBusitypeCanStart(billtype, transtype,
				pk_org, userId);
	}
	*/

	/**
	 * ����ĳ���ݻ����������õ�ĳ����������е��ݶ���
	 * 
	 * @param billOrTranstype ���ݻ�������
	 * @param actiongroupCode ���������
	 * @return
	 */
	public static BillactionVO[] getActionsOfActiongroup(String billOrTranstype,
			String actiongroupCode) {
		//��õ�������(��������)�ĵ�������PK
		billOrTranstype = PfUtilBaseTools.getRealBilltype(billOrTranstype);
		BillactionVO[] billActionVos = (BillactionVO[]) PfUIDataCache
				.getButtonByBillAndGrp(billOrTranstype, actiongroupCode);
		return billActionVos;
	}

	/**
	 * ����һ����ѯ�Ի��򣬲�Ϊ�����ò�ѯģ��
	 * 
	 * @param templateId
	 * @param parent
	 * @param isRelationCorp
	 * @param pkOperator
	 * @param funNode
	 */
	private static IBillReferQuery setConditionClient(String templateId, Container parent,
			final String pkOperator, final String funNode, String pkCorp) {
		TemplateInfo ti = new TemplateInfo();
		ti.setTemplateId(templateId);
		ti.setPk_Org(pkCorp);
		ti.setUserid(pkOperator);
		ti.setCurrentCorpPk(pkCorp);
		ti.setFunNode(funNode);

		QueryConditionDLG qcDlg = new QueryConditionDLG(parent, ti);

		qcDlg.setVisibleNormalPanel(false);
		return qcDlg;
	}
}