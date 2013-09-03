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
 * 流程客户端类，是流程客户端的入口
 */
public class PfUtilClient {

	/**
	 * 审批变量如果审批则true反之false;
	 */
	private static boolean m_checkFlag = true;

	// 当前单据类型
	private static String m_currentBillType = null;

	/** 当前审批节点的审批结果 */
	private static int m_iCheckResult = IApproveflowConst.CHECK_RESULT_PASS;

	private static boolean m_isOk = false;

	/** fgj2001-11-27 判断当前动作是否执行成功 */
	private static boolean m_isSuccess = true;

	/** 源单据类型 */
	private static String m_sourceBillType = null;

	private static AggregatedValueObject m_tmpRetVo = null;

	private static AggregatedValueObject[] m_tmpRetVos = null;

	// 单据自制标志
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
	 * 流程启动时的交互
	 * 涉及到提交单据时,需要的指派信息收集
	 * <li>只有"SAVE","START",""EDIT"动作才调用
	 */
	private static WorkflownoteVO checkWorkitemOnStart(Container parent, String actionName, String billType,
			AggregatedValueObject billVo, Stack dlgResult, HashMap hmPfExParams) throws BusinessException {
		WorkflownoteVO worknoteVO = new WorkflownoteVO();

		//guowl+ 2010-5,如果是批处理，不用取指派信息，直接返回
		if(hmPfExParams != null && hmPfExParams.get(PfUtilBaseTools.PARAM_BATCH) != null)
			return worknoteVO;
		
		worknoteVO = NCLocator.getInstance().lookup(IWorkflowMachine.class)
			.checkWorkFlow(actionName, billType, billVo, hmPfExParams);
		
		//在审批处理框显示之前，调用业务处理
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
	 * 检查当前单据是否处于审批流程中，并进行交互
	 */
	private static WorkflownoteVO checkWorkitemWhenSignal(Container parent, String actionName, 
			String billType, AggregatedValueObject billVo, HashMap hmPfExParams) throws BusinessException {
		WorkflownoteVO noteVO = null;
		UIDialog dlg = null;
		if (hmPfExParams != null && hmPfExParams.get(PfUtilBaseTools.PARAM_BATCH) != null) {
			// 预算开发部要求批审时不管有没有流程定义，都弹出审批意见框
			Object notSilent = hmPfExParams.get(PfUtilBaseTools.PARAM_NOTSILENT);
			// 检查单据是否定义了审批流，如果没有定义，则不弹出,此处只简单检查第一张单据的单据类型上是否有流程定义
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
				// yanke1 2012-02-22 根据前台业务处理的返回值来控制审批界面
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

		if (dlg.showModal() == UIDialog.ID_OK) { // 如果用户审批
			// 返回审批的工作项
			m_checkFlag = true;
		} else { // 用户不审批
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
			throw new PFRuntimeException(NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000000")/*元数据实体没有提供业务接口IFlowBizItf的实现类*/);

		IWorkflowDefine wfDefine = NCLocator.getInstance().lookup(IWorkflowDefine.class);
		Logger.debug("查询流程定义: billType=" + billType + ";pkOrg=" + fbi.getPkorg() + ";userId=" + fbi.getBillMaker() + ";开始");
		return wfDefine.hasValidProcessDef(WorkbenchEnvironment.getInstance().getGroupVO().getPk_group(), billType, fbi.getPkorg(), fbi.getBillMaker(), fbi.getEmendEnum());
	}
	
/*业务流的
	
	/**
	 * 参照来源单据，用于
	 * <li>1.获取来源单据的查询对话框，并查询出来源单据
	 * <li>2.显示来源单据，并进行选择
	 * <li>3.获取选择的来源单据
	 * 
	 * @param srcBillType  参照制单 选择的来源单据类型
	 * @param pk_group
	 * @param userId
	 * @param currBillOrTranstype 当前单据或交易类型
	 * @param parent
	 * @param userObj
	 * @param srcBillId 如果为空，则直接进入参照上游单据界面
	 * /
	public static void childButtonClicked(String srcBillType, String pk_group, String userId,
			String currBillOrTranstype, Container parent, Object userObj, String srcBillId) {

		childButtonClickedWithBusi(srcBillType, pk_group, userId, currBillOrTranstype, parent, userObj, srcBillId, null);
	}
	
	public static void childButtonClickedNew(final PfButtonClickContext context){
		makeFlag = false;
		if (context.getSrcBillType().toUpperCase().equals("MAKEFLAG")) {
			Logger.debug("******自制单据******");
			makeFlag = true;
			return;
		}

		Logger.debug("******参照来源单据******");
		m_classifyMode = context.getClassifyMode();
		try {
			final String funNode = PfUIDataCache.getBillType(new BillTypeCacheKey().buildBilltype(context.getSrcBillType()).buildPkGroup(context.getPk_group())).getNodecode();
			if (funNode == null || funNode.equals(""))
				throw new PFBusinessException(NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000001", null, new String[]{context.getSrcBillType()})/*请注册单据{0}的功能节点号* /);

			//查询交换信息，以便得到一些参照制单的定制信息
			String src_qrytemplate = null;
			String src_billui = null; // 来源单据的显示UI类
			String src_nodekey = null; // 用于查询来源单据的查询模板的节点标识
			ExchangeVO changeVO = getExchangeService().queryMostSuitableExchangeVO(context.getSrcBillType(),
					context.getCurrBilltype(), null, PfUtilUITools.getLoginGroup(), null);
			if (changeVO != null) {
				src_qrytemplate = changeVO.getSrc_qrytemplate();
				src_qrytemplate = src_qrytemplate == null ? null : src_qrytemplate.trim();
				src_billui = changeVO.getSrc_billui();
				src_nodekey = changeVO.getSrc_nodekey();
			}
			// a.获取来源单据的查询对话框，即为m_condition赋值
			IBillReferQuery qcDLG = null;
			if (StringUtil.isEmptyWithTrim(src_qrytemplate)) {
				Logger.debug("使用来源单据类型对应节点使用的查询模板对话框");
				src_qrytemplate = PfUtilUITools.getTemplateId(ITemplateStyle.queryTemplate, context.getPk_group(),
						funNode, context.getUserId(), src_nodekey);
				qcDLG = setConditionClient(src_qrytemplate, context.getParent(), context.getUserId(), funNode, context.getPk_group());
			} else if (src_qrytemplate.startsWith("<")) {
				Logger.debug("使用产品组定制的来源单据查询对话框");
				src_qrytemplate = src_qrytemplate.substring(1, src_qrytemplate.length() - 1);
				qcDLG = loadUserQuery(context.getParent(), src_qrytemplate, context.getPk_group(), context.getUserId(), funNode,
						context.getCurrBilltype(), context.getSrcBillType(), src_nodekey, context.getUserObj());
			} else {
				Logger.debug("使用注册的查询模板的查询对话框");
				qcDLG = setConditionClient(src_qrytemplate, context.getParent(), context.getUserId(), funNode, context.getPk_group());
			}
			//给查询对话框设置业务类型，asked by scm-puqh
			if(context.getBusiTypes()!= null && qcDLG instanceof IBillReferQueryWithBusitype)
				((IBillReferQueryWithBusitype)qcDLG).setBusitypes(context.getBusiTypes());
			//给查询对话框设置交易类型，用于过滤上游单据，asked by scm-puqh
			if(context.getTransTypes()!= null && qcDLG instanceof IBillReferQueryWithTranstype)
				((IBillReferQueryWithTranstype)qcDLG).setTranstypes(context.getTransTypes());

			if (context.getSrcBillId() == null) {
				//b.显示来源单据的查询对话框
				if (qcDLG.showModal() == UIDialog.ID_OK) {
					// c.显示来源单据，并进行选择
					refBillSource(context.getPk_group(), funNode, context.getUserId(), context.getCurrBilltype(), context.getParent(), context.getUserObj(),
							context.getSrcBillType(), src_qrytemplate, src_billui, src_nodekey, null, qcDLG);
				} else {
					m_isOk = false;
					return;
				}
			} else {
				//b'.直接显示来源单据
				refBillSource(context.getPk_group(), funNode, context.getUserId(), context.getCurrBilltype(), context.getParent(), context.getUserObj(), context.getSrcBillType(),
						src_qrytemplate, src_billui, src_nodekey, context.getSrcBillId(), qcDLG);
				return;
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			MessageDialog.showErrorDlg(context.getParent(), "null", NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000002", null, new String[]{ex.getMessage()})/*参照上游单据出现异常={0}* /);
		}
	}

	/**
	 * 参照来源单据，用于
	 * <li>1.获取来源单据的查询对话框，并查询出来源单据
	 * <li>2.显示来源单据，并进行选择
	 * <li>3.获取选择的来源单据
	 * 
	 * @param srcBillType  参照制单 选择的来源单据类型
	 * @param pk_group
	 * @param userId
	 * @param currBillOrTranstype 当前单据或交易类型
	 * @param parent
	 * @param userObj
	 * @param srcBillId 如果为空，则直接进入参照上游单据界面
	 * @param busiTypes 如果不为空，表示查找指定业务流程中的单据
	 * /
	public static void childButtonClickedWithBusi(String srcBillType, String pk_group, String userId,
			String currBillOrTranstype, Container parent, Object userObj, String srcBillId, List<String> busiTypes) {

		m_classifyMode = PfButtonClickContext.NoClassify;
		makeFlag = false;
		if (srcBillType.toUpperCase().equals("MAKEFLAG")) {
			Logger.debug("******自制单据******");
			makeFlag = true;
			return;
		}

		Logger.debug("******参照来源单据******");
		try {
			String funNode = PfUIDataCache.getBillType(new BillTypeCacheKey().buildBilltype(srcBillType).buildPkGroup(pk_group)).getNodecode();
			if (funNode == null || funNode.equals(""))
				throw new PFBusinessException(NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000001", null, new String[]{srcBillType})/*请注册单据{0}的功能节点号* /);

			//查询交换信息，以便得到一些参照制单的定制信息
			String src_qrytemplate = null;
			String src_billui = null; // 来源单据的显示UI类
			String src_nodekey = null; // 用于查询来源单据的查询模板的节点标识
			ExchangeVO changeVO = getExchangeService().queryMostSuitableExchangeVO(srcBillType,
					currBillOrTranstype, null, PfUtilUITools.getLoginGroup(), null);
			if (changeVO != null) {
				src_qrytemplate = changeVO.getSrc_qrytemplate();
				src_qrytemplate = src_qrytemplate == null ? null : src_qrytemplate.trim();
				src_billui = changeVO.getSrc_billui();
				src_nodekey = changeVO.getSrc_nodekey();
			}
			// a.获取来源单据的查询对话框，即为m_condition赋值
			IBillReferQuery qcDLG = null;
			if (StringUtil.isEmptyWithTrim(src_qrytemplate)) {
				Logger.debug("使用来源单据类型对应节点使用的查询模板对话框");
				src_qrytemplate = PfUtilUITools.getTemplateId(ITemplateStyle.queryTemplate, pk_group,
						funNode, userId, src_nodekey);
				qcDLG = setConditionClient(src_qrytemplate, parent, userId, funNode, pk_group);
			} else if (src_qrytemplate.startsWith("<")) {
				Logger.debug("使用产品组定制的来源单据查询对话框");
				src_qrytemplate = src_qrytemplate.substring(1, src_qrytemplate.length() - 1);
				qcDLG = loadUserQuery(parent, src_qrytemplate, pk_group, userId, funNode,
						currBillOrTranstype, srcBillType, src_nodekey, userObj);
			} else {
				Logger.debug("使用注册的查询模板的查询对话框");
				qcDLG = setConditionClient(src_qrytemplate, parent, userId, funNode, pk_group);
			}
			if(busiTypes != null && qcDLG instanceof IBillReferQueryWithBusitype)
				((IBillReferQueryWithBusitype)qcDLG).setBusitypes(busiTypes);

			if (srcBillId == null) {
				//b.显示来源单据的查询对话框
				if (qcDLG.showModal() == UIDialog.ID_OK) {
					// c.显示来源单据，并进行选择
					refBillSource(pk_group, funNode, userId, currBillOrTranstype, parent, userObj,
							srcBillType, src_qrytemplate, src_billui, src_nodekey, null, qcDLG);

				} else {
					m_isOk = false;
					return;
				}
			} else {
				//b'.直接显示来源单据
				refBillSource(pk_group, funNode, userId, currBillOrTranstype, parent, userObj, srcBillType,
						src_qrytemplate, src_billui, src_nodekey, srcBillId, qcDLG);
				return;
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			MessageDialog.showErrorDlg(parent, NCLangRes.getInstance().getStrByID("pfworkflow1", "SplitRuleRegisterUI-000005")/*错误* /, NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000002", null, new String[]{ex.getMessage()})/*参照上游单据出现异常={0}* /);
		}
	}

	/**
	 * 参照制单时，显示来源单据；并进行VO交换
	 * /
	private static void refBillSource(String pk_group, String funNode, String pkOperator,
			String currBillOrTranstype, Container parent, Object userObj, String billType,
			String strQueryTemplateId, String src_billui, String srcNodekey, String sourceBillId,
			IBillReferQuery qcDLG) throws Exception {
		// 获取主表的关键字段
		String pkField = PfUtilBaseTools.findPkField(billType);
		if (pkField == null || pkField.trim().length() == 0)
			throw new PFBusinessException(NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000003")/*无法通过单据类型获取单据实体的主表PK字段* /);
		String whereString = null;
		IQueryScheme qryScheme = null;
		if (sourceBillId == null) {
			if(qcDLG instanceof IBillReferQueryWithScheme) {
				qryScheme = ((IBillReferQueryWithScheme)qcDLG).getQueryScheme();
			}else
				whereString = qcDLG.getWhereSQL();
		} else
			whereString = pkField + "='" + sourceBillId + "'";

		//载入来源单据展现对话框，并显示
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
			Logger.debug("产品组定制的来源单据展现对话框，必须继承自" + AbstractBillSourceDLG.class.getName());
			bsDlg = loadCustomBillSourceDLG(src_billui, bsVar, parent);
		} else {
			Logger.debug("使用通用的来源单据展现对话框");
			bsDlg = new BillSourceDLG(parent, bsVar);
		}

		bsDlg.setQueyDlg(qcDLG); //放入查询模板对话框
		// 加载模版
		bsDlg.addBillUI();
		// 加载数据
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
	 * 返回 当前审批节点的处理结果 lj+ 2005-1-20
	 */
	public static int getCurrentCheckResult() {
		return m_iCheckResult;
	}

	/**
	 * 返回 用户选择的VO
	 */
	public static AggregatedValueObject getRetOldVo() {
		return m_tmpRetVo;
	}

	/**
	 * 返回 用户选择VO数组.
	 */
	public static AggregatedValueObject[] getRetOldVos() {
		return m_tmpRetVos;
	}


/*
业务流的

	/**
	 * 返回 用户选择的VO或交换过的VO
	 * 
	 * @return
	 * /
	public static AggregatedValueObject getRetVo() {
		try {
			// 需要进行VO交换
			m_tmpRetVo = getExchangeService().runChangeData(m_sourceBillType, m_currentBillType, m_tmpRetVo, null);
			jumpBusitype(m_tmpRetVo==null?null:new AggregatedValueObject[]{m_tmpRetVo});
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFRuntimeException(NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000004", null, new String[]{ex.getMessage()})/*VO交换错误：{0}* /, ex);
		}
		return m_tmpRetVo;
	}

	private static IPfExchangeService getExchangeService() {
		if(exchangeService==null)
			exchangeService = NCLocator.getInstance().lookup(IPfExchangeService.class);
		return exchangeService;
	}

	/**
	 * 返回 用户选择的VO或交换过的VO
	 * 
	 * @return
	 * /
	private static AggregatedValueObject[] changeVos(AggregatedValueObject[] vos, int classifyMode) {
		AggregatedValueObject[] tmpRetVos = null;

		try {
			tmpRetVos = getExchangeService().runChangeDataAryNeedClassify(m_sourceBillType, m_currentBillType, vos, null, classifyMode);
		} catch (BusinessException ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFRuntimeException(NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000004", null, new String[]{ex.getMessage()})/*VO交换错误：{0}* /, ex);
		}

		return tmpRetVos;
	}
	
	/**
	 * 返回 用户选择VO数组或交换过的VO数组
	 * 
	 * @return
	 * @throws BusinessException 
	 * /
	public static AggregatedValueObject[] getRetVos() throws BusinessException {
		// 需要进行VO交换
		m_tmpRetVos = changeVos(m_tmpRetVos, m_classifyMode);
		jumpBusitype(m_tmpRetVos);
		return m_tmpRetVos;
	}

	
	/**
	 * 业务流跳转
	 * @throws BusinessException 
	 * * /
	private static void jumpBusitype(AggregatedValueObject[] vos) throws BusinessException{
		if(vos==null||vos.length==0)
			return;
		IFlowBizItf fbi = PfMetadataTools.getBizItfImpl(vos[0], IFlowBizItf.class);
		//未实现业务流接口的单据或者未走流程直接return
		if(fbi==null||StringUtil.isEmptyWithTrim(fbi.getBusitype()))
			return;
		BillbusinessVO condVO = new BillbusinessVO();
		condVO.setPk_businesstype(fbi.getBusitype());
		condVO.setJumpflag(UFBoolean.TRUE);
		//得到单据类型编码
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
						//单据类型不能为空
						continue;
					}
					String key =billtype+(StringUtil.isEmptyWithTrim(transtype)?"":transtype)+(StringUtil.isEmptyWithTrim(pk_org)?"":pk_org)
							   +operator;
					if(busitypeMaps.containsKey(key)){
						destBusitypePk = busitypeMaps.get(key);
					}else{
						destBusitypePk = NCLocator.getInstance().lookup(IPFConfig.class).retBusitypeCanStart(billtype, transtype, pk_org, operator);
					}
					//如果没有找到要跳转的业务流
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
	 * 判断用户是否点击了＂取消＂按钮
	 * 
	 * @return boolean leijun+
	 */
	public static boolean isCanceled() {
		return !m_checkFlag;
	}

	/**
	 * 返回 参照单据是否正常关闭
	 * 
	 * @return boolean
	 */
	public static boolean isCloseOK() {
		return m_isOk;
	}

	/**
	 * 返回 当前单据动作执行是否成功
	 * 
	 * @return boolean
	 */
	public static boolean isSuccess() {
		return m_isSuccess;
	}

	/*
	业务流的
	/**
	 * 载入产品组注册的来源单据显示对话框
	 * /
	private static AbstractBillSourceDLG loadCustomBillSourceDLG(String src_billui,
			BillSourceVar bsVar, Container parent) {
		Class c = null;

		try {
			c = Class.forName(src_billui);
			Class[] argCls = new Class[] { Container.class, BillSourceVar.class };
			Object[] args = new Object[] { parent, bsVar };
			// 实例化带参构造方法
			Constructor cc = c.getConstructor(argCls);
			return (AbstractBillSourceDLG) cc.newInstance(args);
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			MessageDialog.showErrorDlg(parent, null, NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000005", null, new String[]{ex.getMessage()})/*载入产品组注册的来源单据显示对话框 发生异常：{0}* /);
		}
		return null;
	}

	private static IBillReferQuery loadUserQuery(Container parent, String src_qrytemplate,
			String pk_group, String userId, String FunNode, String currBillOrTranstype,
			String sourceBillType, String nodeKey, Object userObj) {

		Class c = null;
		try {
			// 先判定是否为新查询模板UI的子类
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
			//对查询模版对话框的一些定制初始化
			if (retObj instanceof IinitQueryData) {
				((IinitQueryData) retObj).initData(pk_group, userId, FunNode, currBillOrTranstype,
						sourceBillType, nodeKey, userObj);
			}

			return (IBillReferQuery) retObj;
		} catch (NoSuchMethodException e) {
			Logger.warn("找不到新查询模板UI的构造方法，继续判定是否有老查询模板的构造方法", e);
			try {
				// 应该为老查询模板UI的子类
				Object retObj = c.getConstructor(new Class[] { Container.class }).newInstance(
						new Object[] { parent });
				//对查询模版对话框的一些定制初始化
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
	 * 前台单据动作处理API，算法如下：
	 * <li>1.动作执行前提示以及事前处理，如果用户取消，则方法直接返回
	 * <li>2.查看扩展参数，判断是否需要审批流相关处理。如果为提交动作，则可能需要收集提交人的指派信息；
	 * 如果为审批动作，则可能需要收集审批人的审批信息
	 * <li>3.后台执行动作。并返回动作执行结果。 
	 * 
	 * @param parent 父窗体
	 * @param actionCode 动作编码，比如"SAVE"
	 * @param billOrTranstype 单据（或交易）类型PK
	 * @param billvo 单据聚合VO
	 * @param userObj 用户自定义对象
	 * @param checkVo 校验单据聚合VO
	 * @param eParam 扩展参数
	 * @return 动作处理的返回结果
	 * @throws BusinessException 
	 * @throws Exception
	 * @since 5.5
	 */
	public static Object runAction(Container parent, String actionCode, String billOrTranstype,
			AggregatedValueObject billvo, Object userObj, String strBeforeUIClass,
			AggregatedValueObject checkVo, HashMap eParam) throws BusinessException {
		Logger.debug("*单据动作处理 开始");
		debugParams(actionCode, billOrTranstype, billvo, userObj);
		long start = System.currentTimeMillis();
		m_isSuccess = true;

		// 1.动作执行前提示
		boolean isContinue = hintBeforeAction(parent, actionCode, billOrTranstype);
		if (!isContinue) {
			Logger.debug("*动作执行前提示，返回");
			m_isSuccess = false;
			return null;
		}

		// 2.查看扩展参数，是否要流程交互处理
		WorkflownoteVO worknoteVO = null;
		Object paramSilent = getParamFromMap(eParam, PfUtilBaseTools.PARAM_SILENTLY);
		Object paramNoflow = getParamFromMap(eParam, PfUtilBaseTools.PARAM_NOFLOW);

		if (paramNoflow == null && paramSilent == null) {
			// 对流程处理进行交互处理
			if (PfUtilBaseTools.isSaveAction(actionCode, billOrTranstype)
					|| PfUtilBaseTools.isApproveAction(actionCode, billOrTranstype)
					|| PfUtilBaseTools.isStartAction(actionCode, billOrTranstype)
					|| PfUtilBaseTools.isSignalAction(actionCode, billOrTranstype)) {
				// 审批流交互处理
				worknoteVO = WorkflowInteraction(parent, actionCode, billOrTranstype, billvo, eParam);
				if (!m_isSuccess)
					return null;
			} 
		}
		//如果用户在交互处理时候选择加签或者改派，则无需驱动流程@2009-5
		if (worknoteVO != null && isAddApproverOrAppoint(worknoteVO)) {
			m_isSuccess = false;
			return null;
		}
		if (worknoteVO == null) {
			//检查不到工作项，则后台无需再次检查
			if (eParam == null)
				eParam = new HashMap<String, String>();
			if (paramSilent == null)
				eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED, PfUtilBaseTools.PARAM_NOTE_CHECKED);
		}

		// 4.后台执行动作
		Object retObj = null;

		Logger.debug("*后台动作处理 开始");
		long start2 = System.currentTimeMillis();
		IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator.getInstance().lookup(IplatFormEntry.class.getName());
		retObj = iIplatFormEntry.processAction(actionCode, billOrTranstype, worknoteVO, billvo,userObj, eParam);
		Logger.debug("*后台动作处理 结束=" + (System.currentTimeMillis() - start2) + "ms");

		m_isSuccess = true;

		// 5.返回对象执行
		//retObjRun(parent, retObj);
		Logger.debug("*单据动作处理 结束=" + (System.currentTimeMillis() - start) + "ms");
		
/*	to do	
		if(m_isSuccess && worknoteVO != null) {
			//更新消息的状态
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
	 * 流程交互处理，包括审批流和工作流的启动和流转
	 * @throws BusinessException 
	 */
	private static WorkflownoteVO WorkflowInteraction(Container parent, String actionName,
			String billType, AggregatedValueObject billvo, HashMap eParam) throws BusinessException {
		WorkflownoteVO worknoteVO = null;

		if (PfUtilBaseTools.isSaveAction(actionName, billType) 
				|| PfUtilBaseTools.isStartAction(actionName, billType)) {
			Logger.debug("*提交动作=" + actionName + "，检查审批流");
			// 如果为提交动作，可能需要收集提交人的指派信息，这里统一动作名称 lj@2005-4-8
			Stack dlgResult = new Stack();
			worknoteVO = checkWorkitemOnStart(parent, IPFActionName.SAVE, billType, billvo, dlgResult, eParam);
			if (dlgResult.size() > 0) {
				m_isSuccess = false;
				Logger.debug("*用户指派时点击了取消，则停止送审");
			}
		} else if (PfUtilBaseTools.isApproveAction(actionName, billType)
				|| PfUtilBaseTools.isSignalAction(actionName, billType)) {
			Logger.debug("*审批动作=" + actionName + "，检查审批流");
			// 检查该单据是否处于审批流中，并收集审批人的审批信息
			worknoteVO = checkWorkitemWhenSignal(parent, actionName, billType, billvo, eParam);
			if (worknoteVO != null) {
				if ("Y".equals(worknoteVO.getApproveresult())) {
					m_iCheckResult = IApproveflowConst.CHECK_RESULT_PASS;
				} else if("R".equals(worknoteVO.getApproveresult())) {
					/* todo
					// XXX::驳回也作为审批通过的一种,需要继续判断 lj+
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
				Logger.debug("*用户审批时点击了取消，则停止审批");
			}
		}
		return worknoteVO;
	}
	
	private static PFClientBizRetObj executeBusinessPlugin(Container parent, AggregatedValueObject billVo, WorkflownoteVO workflownoteVO, boolean isMakeBill) {
		if(workflownoteVO != null && workflownoteVO.getApplicationArgs() != null && workflownoteVO.getApplicationArgs().size()>0){
			String billtype = workflownoteVO.getTaskInstanceVO().getPk_bizobject();
			ArrayList<Billtype2VO> bt2VOs = PfDataCache.getBillType2Info(billtype,
					ExtendedClassEnum.PROC_CLIENT.getIntValue());

			//实例化
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
					Logger.error("无法实例化前台业务插件类billType=" + billtype + ",className=" + bt2VO.getClassname(),
							e);
				}
			}
		}
		return null;
		
	}

	/**
	 * 日志一下动作处理的上下文参数
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
	 * 动作执行前提示
	 */
	private static boolean hintBeforeAction(Container parent, String actionName, String billType) {
		ActionClientParams acp = new ActionClientParams();
		acp.setUiContainer(parent);
		acp.setActionCode(actionName);
		acp.setBillType(billType);

		return PfUtilUITools.beforeAction(acp);
	}
	
	/**
	 * 前台单据动作批处理API，算法如下：
	 * <li>1.动作执行前提示以及事前处理，如果用户取消，则方法直接返回
	 * <li>2.查看扩展参数，判断是否需要审批流相关处理。如果为提交动作，且单据VO数组中只有一张单据时可能需要收集提交人的指派信息；
	 * 如果为审批动作，则针对第一张单据可能需要收集审批人的审批信息
	 * <li>3.后台执行批动作。并返回动作执行结果。 
	 * 
	 * @param parent 父窗体
	 * @param actionCode 动作编码，比如"SAVE"
	 * @param billOrTranstype 单据类型（或交易类型）PK
	 * @param voAry 单据聚合VO数组
	 * @param userObjAry 用户自定义对象数组
	 * @param eParam 扩展参数
	 * @return 动作批处理的返回结果
	 * @throws Exception
	 * @since 5.5
	 */
	public static Object[] runBatch(Container parent, String actionCode, String billOrTranstype,
			AggregatedValueObject[] voAry, Object[] userObjAry, String strBeforeUIClass, HashMap eParam)
			throws Exception {
		Logger.debug("*单据动作批处理 开始");
		debugParams(actionCode, billOrTranstype, voAry, userObjAry);
		long start = System.currentTimeMillis();
		if(voAry!= null && voAry.length == 1) {
			Object obj = runAction(parent, actionCode, billOrTranstype, voAry[0], userObjAry, strBeforeUIClass, null, eParam);
			Object[] ret = null;
			ret = PfUtilBaseTools.composeResultAry(obj,1,0,ret);
			return ret;
		}
		
		m_isSuccess = true;

		// 1.动作执行前提示以及事前处理
		boolean isContinue = beforeProcessBatchAction(parent, actionCode, billOrTranstype);
		if (!isContinue) {
			Logger.debug("*动作执行前提示以及事前处理，返回");
			m_isSuccess = false;
			return null;
		}

		WorkflownoteVO workflownote = null;
		
		// 2.查看扩展参数，是否要流程交互处理
		Object paramNoflow = getParamFromMap(eParam, PfUtilBaseTools.PARAM_NOFLOW);
		Object paramSilent = getParamFromMap(eParam, PfUtilBaseTools.PARAM_SILENTLY);
		if (paramNoflow == null && paramSilent == null) {
			//XXX:guowl,2010-5,批审时，审批处理对话框上只显示批准、不批准、驳回，故传入一个参数用于甄别
			//如果传人的VO数组长度为1，同单个处理一样
			if (voAry != null && voAry.length > 1) {
				eParam = putParam(eParam, PfUtilBaseTools.PARAM_BATCH, PfUtilBaseTools.PARAM_BATCH);
			}
			if (PfUtilBaseTools.isSaveAction(actionCode, billOrTranstype) || PfUtilBaseTools.isApproveAction(actionCode, billOrTranstype)) {
				//审批流交互处理
				workflownote = WorkflowInteraction(parent, actionCode, billOrTranstype, voAry[0], eParam);
				if (!m_isSuccess)
					return null;
			} else if (PfUtilBaseTools.isStartAction(actionCode, billOrTranstype) || PfUtilBaseTools.isSignalAction(actionCode, billOrTranstype)) {
				//工作流交互处理
				workflownote = WorkflowInteraction(parent, actionCode, billOrTranstype, voAry[0], eParam);
				if (!m_isSuccess)
					return null;
			}
		}

		// 3.后台批处理动作
		Logger.debug("*后台动作批处理 开始");
		Object retObj = NCLocator.getInstance().lookup(IplatFormEntry.class).processBatch(actionCode,
				billOrTranstype, workflownote, voAry, userObjAry, eParam);
		if(retObj instanceof PfProcessBatchRetObject) {
			String errMsg = ((PfProcessBatchRetObject)retObj).getExceptionMsg();
			if(!StringUtil.isEmptyWithTrim(errMsg))
				MessageDialog.showErrorDlg(parent, null, errMsg);
			retObj = ((PfProcessBatchRetObject)retObj).getRetObj();
		}
		if(retObj != null && ((Object[]) retObj).length > 0) {
			//批处理时，有一个成功的就认为成功
			m_isSuccess = true;
		}
		Logger.debug("*单据动作批处理 结束=" + (System.currentTimeMillis() - start) + "ms");
		
		return (Object[]) retObj;
	}
	
	public static PfProcessBatchRetObject runBatchNew(Container parent, String actionCode, String billOrTranstype,
			AggregatedValueObject[] voAry, Object[] userObjAry, String strBeforeUIClass, HashMap eParam)
			throws Exception {
		Logger.debug("*单据动作批处理 开始");
		debugParams(actionCode, billOrTranstype, voAry, userObjAry);
		long start = System.currentTimeMillis();
		if(voAry!= null && voAry.length == 1) {
			Object obj = runAction(parent, actionCode, billOrTranstype, voAry[0], userObjAry, strBeforeUIClass, null, eParam);
			Object[] retObj = null;
			retObj = PfUtilBaseTools.composeResultAry(obj,1,0,retObj);
			return new PfProcessBatchRetObject(retObj, null);
		}
		
		m_isSuccess = true;

		// 1.动作执行前提示以及事前处理
		boolean isContinue = beforeProcessBatchAction(parent, actionCode, billOrTranstype);
		if (!isContinue) {
			Logger.debug("*动作执行前提示以及事前处理，返回");
			m_isSuccess = false;
			return null;
		}

		// 2.查看扩展参数，是否要流程交互处理
		WorkflownoteVO workflownote = null;//(WorkflownoteVO)getParamFromMap(eParam, PfUtilBaseTools.PARAM_WORKNOTE);
//		if(workflownote == null){
			Object paramNoflow = getParamFromMap(eParam, PfUtilBaseTools.PARAM_NOFLOW);
			Object paramSilent = getParamFromMap(eParam, PfUtilBaseTools.PARAM_SILENTLY);
			if (paramNoflow == null && paramSilent == null) {
				//XXX:guowl,2010-5,批审时，审批处理对话框上只显示批准、不批准、驳回，故传入一个参数用于甄别
				//如果传人的VO数组长度为1，同单个处理一样
				if (voAry != null && voAry.length > 1) {
					eParam = putParam(eParam, PfUtilBaseTools.PARAM_BATCH, PfUtilBaseTools.PARAM_BATCH);
				}
				if (PfUtilBaseTools.isSaveAction(actionCode, billOrTranstype) || PfUtilBaseTools.isApproveAction(actionCode, billOrTranstype)) {
					//审批流交互处理
					workflownote = WorkflowInteraction(parent, actionCode, billOrTranstype, voAry[0], eParam);
					if (!m_isSuccess)
						return null;
				} else if (PfUtilBaseTools.isStartAction(actionCode, billOrTranstype) || PfUtilBaseTools.isSignalAction(actionCode, billOrTranstype)) {
					//工作流交互处理
					workflownote = WorkflowInteraction(parent, actionCode, billOrTranstype, voAry[0], eParam);
					if (!m_isSuccess)
						return null;
				}
//				putParam(eParam, PfUtilBaseTools.PARAM_WORKNOTE, workflownote);
			}
		//}
		// 3.后台批处理动作
		Logger.debug("*后台动作批处理 开始");
		Object retObj = NCLocator.getInstance().lookup(IplatFormEntry.class).processBatch(actionCode,
				billOrTranstype, workflownote, voAry, userObjAry, eParam);
		Logger.debug("*单据动作批处理 结束=" + (System.currentTimeMillis() - start) + "ms");
		
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
	 * 动作执行前提示以及事前处理
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
	 * 返回某个单据或交易类型可使用的“新增”下拉菜单信息
	 * <li>包括自制、来源单据
	 * 
	 * @param billtype
	 * @param transtype 如果没有交易类型，可空 
	 * @param pk_group 某集团PK
	 * @param userId 某用户PK
	 * @param includeBillType 是否包括单据类型的来源单据信息，只用于transtype非空的情况
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
	 * 返回某个用户对某个单据或交易类型，在某组织下 可启动的业务流程
	 * @param billtype
	 * @param transtype 如果没有交易类型，可空 
	 * @param pk_org 某组织PK
	 * @param userId 某用户PK
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
	 * 返回某单据或交易类型配置的某动作组的所有单据动作
	 * 
	 * @param billOrTranstype 单据或交易类型
	 * @param actiongroupCode 动作组编码
	 * @return
	 */
	public static BillactionVO[] getActionsOfActiongroup(String billOrTranstype,
			String actiongroupCode) {
		//获得单据类型(或交易类型)的单据类型PK
		billOrTranstype = PfUtilBaseTools.getRealBilltype(billOrTranstype);
		BillactionVO[] billActionVos = (BillactionVO[]) PfUIDataCache
				.getButtonByBillAndGrp(billOrTranstype, actiongroupCode);
		return billActionVos;
	}

	/**
	 * 构造一个查询对话框，并为其设置查询模板
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