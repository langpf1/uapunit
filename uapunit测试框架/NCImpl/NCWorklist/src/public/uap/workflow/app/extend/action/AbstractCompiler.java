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
 * 实现平台编程运行环境接口的类
 * 
 * @author 樊冠军 2002-2-28
 * @modifier leijun 2005-3
 *           修改方法procActionFlow()和procFlowBacth(),如果为制单即审批通过,则不处理审批流
 * @modifier leijun 2005-6 修改方法runClass(),完全委托给PfUtilTools
 * @modifier leijun 2006-5 单据审批通过后需要发送"拉式"消息
 * @modifier leijun 2009-6 增加工作流的回退功能
 */
public class AbstractCompiler implements IPfRun, ICodeRemark {

	public AbstractCompiler() {
		super();
	}

	/**
	 * 执行审批通过的状态
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
	 * 用于无审批流情况，执行审批不通过的状态
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
	 * 用于无审批流情况，执行驳回的状态
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
	 * 从主表VO中获取一些数据，比如billId和billNo等
	 */
	protected void getHeadInfo(PfParameterVO paraVo) {
		// 获取单据主实体中平台相关信息
		PublicHeadVO standHeadVo = new PublicHeadVO();

		// 根据元数据模型信息来获取
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
	 * 审批或工作流流转
	 * <li>被审批"APPROVE"动作脚本调用,实现对单个单据的审批；也可调用procFlowBatch()来实现对单个单据的审批
	 * <li>被"SIGNAL"动作脚本调用,实现对单个单据的工作流流转
	 * 
	 * @return Object 如果为流程审批进行中的审批,则返回IWorkFlowRet; 如果为导致流程审批通过的审批,则返回null.
	 */
	public Object procActionFlow(PfParameterVO paraVo) throws Exception {
		IWorkFlowRet retObj = null;
		int intFlag = processWorkFlow(paraVo);
		if (intFlag != IPfRetCheckInfo.PASSING) {
			// 未通过或进行中的处理
			retObj = new IWorkFlowRet();
			retObj.m_inVo = paraVo.m_preValueVo;
		} else {
			// 通过后的处理-发送"拉式"消息 lj+2006-5-30
			insertPullWorkitems(paraVo);
		}

		return retObj;
	}

	/**
	 * 发送"拉式"消息
	 * 
	 * @param paraVo
	 */
	private void insertPullWorkitems(PfParameterVO paravo) {
		Logger.debug(">>审批通过后发送拉式消息=" + paravo.m_billType + "开始");

		// 判断是否发送拉式消息
		boolean isNeed = false; // XXX:leijun 2009-12 判断是否需要发送下游消息
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
			Logger.debug(">>源单据" + paravo.m_billType + "不可发送下游消息，返回");
			return;
		}

		try {
			// 1.查找下游单据类型
			IPFConfig pfcfg = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());
			BillbusinessVO[] billbusiVOs = pfcfg.queryBillDest(paravo.m_billType, paravo.m_businessType);
			if (billbusiVOs == null || billbusiVOs.length == 0) {
				Logger.debug("该业务流程没有为单据" + paravo.m_billType + "配置下游单据，返回");
				return;
			}

			// 2.查找上游单据的过滤器类
			Object checkClzInstance = PfUtilTools.getBizRuleImpl(paravo.m_billType);
			IPfPersonFilter2 filter = null;
			if (checkClzInstance instanceof IPfPersonFilter2)
				filter = (IPfPersonFilter2) checkClzInstance;
			for (int k = 0; k < billbusiVOs.length; k++) {
				BillbusinessVO destBillbusiVO = billbusiVOs[k];
				// 3.获得所有待发送消息的用户(流程配置 -- 参与者配置)
				String[] hsUserPKs = pfcfg.queryForwardMessageUser(paravo.m_billType, destBillbusiVO.getPk_billtype(), paravo.m_businessType, paravo.m_preValueVo, filter);
				if (hsUserPKs == null || hsUserPKs.length == 0) {
					Logger.warn(">>无法发送拉式消息，因为接收用户为空");
					return;
				}
				// 4.给这些用户发送"拉式"工作流消息
				ArrayList alItems = new ArrayList();
				for (int i = 0; i < hsUserPKs.length; i++) {
					String userId = hsUserPKs[i];
					MessageinfoVO wi = new MessageinfoVO();
					wi.setPk_billtype(destBillbusiVO.getPk_billtype());
					wi.setBillid(paravo.m_billVersionPK); // 上游单据ID
					wi.setPk_srcbilltype(paravo.m_billType);
					wi.setBillno(paravo.m_billNo);

					wi.setCheckman(userId);
					// FIXME::i18n
					wi.setTitle(Pfi18nTools.i18nBilltypeName(paravo.m_billType, null) + paravo.m_billNo + NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "AbstractCompiler-000000")/*已经审批通过，可从它拉式产生：*/ + Pfi18nTools.i18nBilltypeName(destBillbusiVO.getPk_billtype(), null));
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
			// WARN::仅仅日志异常，不能影响业务流程
			Logger.error(e.getMessage(), e);
		}
		Logger.debug(">>审批通过后发送拉式消息=" + paravo.m_billType + "结束");
	}

	/**
	 * 处理工作流;并处理单据的审批状态
	 * 
	 * @param paraVo
	 * @throws Exception
	 */
	public int processWorkFlow(PfParameterVO paraVo) throws Exception {
		Logger.info("****处理工作流processWorkFlow开始****"+ System.currentTimeMillis() + "ms");
		int intFlag = IPfRetCheckInfo.NOPASS;
		// lj@ 2005-3-14 如果是制单即审批通过,则不处理工作流
		if (paraVo.m_workFlow == null || paraVo.m_autoApproveAfterCommit) {
			// 执行审核通过的组件
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
			// 1.工作流前进
			intFlag = NCLocator.getInstance().lookup(IWorkflowMachine.class).signalWorkflow(paraVo);

			TaskInstanceVO currentTask = paraVo.m_workFlow.getTaskInstanceVO();
			int iCurrentWfType = WorkflowTypeEnum.Approveflow.getIntValue();//TODO:  currentTask.getWorkflowType();
			boolean isWorkflow = WorkflowTypeEnum.Workflow.getIntValue() == iCurrentWfType || WorkflowTypeEnum.SubWorkflow.getIntValue() == iCurrentWfType;
			if (!isWorkflow) {
				// 2.审批状态修改——只有审批流才需要
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
		Logger.debug(">>当前单据的审批状态=" + intFlag);
		Logger.info("****处理工作流processWorkFlow结束****" + System.currentTimeMillis() + "ms");
		return intFlag;
	}

	/**
	 * 被审批"APPROVE"的动作脚本调用,实现批量审批
	 * 注意：
	 * 从60起，流程平台的批审在后台入口处就被分解为单个审批任务进行分别处理
	 * 见PFBusiAction.processBatch()方法
	 * 
	 * 因此，当调用到业务组动作脚本时，此方法实际上处理的仅仅是单个审批任务
	 * 
	 * @return Hashtable 未通过、驳回的单据vo在paraVO.m_preValueVOs中的索引将被放在这个hashTable中
	 * @throws Exception
	 */
	public Hashtable procFlowBacth(PfParameterVO paraVo) throws Exception {
		// 工作流的判断(提供组件进行执行)
		Hashtable<String, String> retHas = new Hashtable<String, String>();
		IWorkFlowRet retObj = null;
		int intFlag = processWorkFlow(paraVo);
		if (intFlag != IPfRetCheckInfo.PASSING) {
			// 未通过或进行中的处理
			retObj = new IWorkFlowRet();
			retObj.m_inVo = paraVo.m_preValueVo;
			
			// 因方法实际只处理单个单据(批审在后台会被拆成单个审批任务分别处理)
			// 这里直接put一个0进去
			retHas.put(String.valueOf(0), String.valueOf(0));
		} else {
			// 通过后的处理-发送"拉式"消息 lj+2006-5-30
			insertPullWorkitems(paraVo);
		}
		return retHas;
	}

	/**
	 * 运行类的某个方法,参数格式如下:
	 * <li>输入参数 :"AggressVo:20,pkbillType:String"
	 * <li>输入参数2:"&AggressVo:key,&key:DataType",DataType为其它标准类型
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
	 * 弃审或回退
	 * <li>被弃审"UNAPPROVE"动作脚本调用,实现对单个单据的弃审;
	 * <li>被回退"ROLLBACK"动作脚本调用,实现对单个单据的回退;
	 * 
	 * @param paraVo
	 * @return true-流程由完成态返回到运行态;false-其他情况
	 */
	public boolean procUnApproveFlow(PfParameterVO paraVo) throws BusinessException {
			RetBackWfVo backWfVo = NCLocator.getInstance().lookup(IWorkflowMachine.class).backCheckFlow(paraVo);
			boolean needChangeStatus = false;
			if(paraVo.m_workFlow == null) {
				//没有流程，直批的单据弃审时也需要改变状态
				needChangeStatus = true;
			}else {
				int wftype = paraVo.m_workFlow.getWorkflow_type();
				//只有审批流、审批子流程、工作审批子流程，弃审需要改变状态
				if (wftype == WorkflowTypeEnum.Approveflow.getIntValue()||wftype == WorkflowTypeEnum.SubApproveflow.getIntValue()||wftype == WorkflowTypeEnum.SubWorkApproveflow.getIntValue()) {
					needChangeStatus = true;
				}
			}
			if(needChangeStatus)
				// 弃审时，修改单据的审批状态
				unApproveState(paraVo, backWfVo.getBackState(), backWfVo.getPreCheckMan());

			return backWfVo.getIsFinish().booleanValue();
	}

	/**
	 * 取消提交
	 * <li>被收回"UNSAVE"，取消提交"RECALL"动作脚本调用,实现对单个单据的取消提交;
	 * 
	 * @param paraVo
	 * @return 
	 */
	public void procRecallFlow(PfParameterVO paraVo) throws BusinessException {
		try {
			// 取消提交 leijun@2008-9
			NCLocator.getInstance().lookup(IWorkflowMachine.class).backCheckFlow(paraVo);
			//改变单据状态为自由态
			unApproveState(paraVo, IPfRetCheckInfo.NOSTATE, null);
//			return backWfVo.getIsFinish().booleanValue();
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFBusinessException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * 弃审时，修改单据的审批状态
	 */
	private void unApproveState(PfParameterVO paraVo, int iBackState, String preCheckMan) throws BusinessException {
		Logger.info("****弃审时，修改单据的审批状态unApproveState开始*****");
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
		Logger.info("****弃审时，修改单据的审批状态unApproveState结束*****");
	}

	public String getCodeRemark() {
		return null;
	}
}