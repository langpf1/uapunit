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
 * 流程平台的动作处理类,原PfUtilBO转换为Proxy
 * 
 * @author fangj 2005-1-24 16:02
 * @modifier leijun 2005-3-10 修改方法startApproveFlowAfterAction(),再次获取billNo
 * @modifier leijun 2005-7-11 修改方法actiondrive()，驱动多个动作时获取操作员的Bug
 * @modifier zsb 2006-4-30 根据操作员查询动作驱动不在这里过滤了，而改在动作驱动查询方法中直接过滤
 * @modifier leijun 2006-5-30 动作驱动后，发送"推式"消息
 * @modifier leijun 2006-7-11 无需成员变量，成为单例的无状态Bean
 */
public class PFBusiAction implements IPFBusiAction {

	public PFBusiAction() {
		super();
	}
	
	/**
	 * 执行完所有动作驱动后PfParameterVO信息，用于业务流程的跳转
	 * */
	private PfParameterVO lastParamVO =null;

	/**
	 * 尝试 启动审批流
	 * <li>对于SAVE或EDIT动作,都需重新获取单据No和单据Id
	 * @param billtype
	 * @param billVO            单据VO
	 * @param userObj           用户对象
	 * @param retObj            动作脚本执行后的返回值
	 * @param eParam            扩展参数
	 * @param hashBilltypeToParavo 参数VO与单据类型映射
	 * @param hashMethodReturn
	 * @return
	 * @throws BusinessException
	 */
	private Object[] startWorkflowAfterAction(String billtype, AggregatedValueObject billVO, Object userObj, Object retObj, HashMap eParam, Hashtable hashBilltypeToParavo, Hashtable hashMethodReturn,String src_billtypePK)
			throws BusinessException {
		Logger.debug("************尝试 启动审批流*************");
		boolean isNeedStart = true;
		boolean bStarted = false;
		Object noApprove = eParam == null ? null : eParam.get(PfUtilBaseTools.PARAM_NOFLOW);
		if (noApprove != null) {
			Logger.debug(">>PARAM_NOFLOW");
			isNeedStart = false;
		}

		String startupTrace = "************审批流不可启动*************";
		if (isNeedStart) {
			PfParameterVO paraVo = (PfParameterVO) hashBilltypeToParavo.get(billtype+src_billtypePK);
			if(paraVo==null){
				//保存即提交的单据此时主键还没有生成，因此对应的key是单据类型编码。
				paraVo = (PfParameterVO) hashBilltypeToParavo.get(billtype);
			}
			PfUtilBaseTools.fetchBillId(paraVo, billVO, retObj);

			if (paraVo.m_billVersionPK != null/* && paraVo.m_billNo != null*/) {
				// 只有单据id和单据号都不为空,才启动审批流
				//try {
				boolean[] wfRet = NCLocator.getInstance().lookup(IWorkflowMachine.class).sendWorkFlowOnSave_RequiresNew(paraVo, hashMethodReturn, eParam);
				bStarted = wfRet[0]; 
				//xry 2011.6.28 异常方式终止了正常执行流程，而且回滚了事务，导致流程实例信息没有存到数据库
				//} catch (ApproveAfterCommitException e) {
				if(wfRet[1]){
					//try
					//{
						// 提交后即审批通过 标志位
						paraVo.m_autoApproveAfterCommit = true;
						
						// 执行业务审核
						retObj = actionOnStep(IPFActionName.APPROVE, paraVo);
						// 执行驱动操作
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
				startupTrace = "************审批流成功启动*************";
			} else {
				startupTrace = "************单据id或单据号为空,不能启动审批流*************";
			}
		}
		Logger.warn(startupTrace);
		return new Object[] {bStarted,retObj};
	}

	/**
	 * 启动流程时候，向监控人发送消息
	 * @throws BusinessException 
	 * */
	private void sendMessageWhenStartWorkflow(PfParameterVO paraVo,int iworkflowtype) throws BusinessException{
		/* xry 重要 TODO:  
		WorknoteManager manager = new WorknoteManager();
		String processId = manager.getProcessId(paraVo,iworkflowtype);
		if(StringUtil.isEmptyWithTrim(processId)){
			return;
		}		
		manager.sendMsgWhenWFstateChanged(paraVo, processId,WfTaskOrInstanceStatus.Started.getIntValue(),iworkflowtype);
		*/
	}

	/**
	 * 动作执行前的审批流处理
	 */
	private void deleteWorkFlow(PfParameterVO paraVo) throws BusinessException {
		// 删除处理
		new WorkflowMachineImpl().deleteCheckFlow(paraVo.m_billType, paraVo.m_billVersionPK, paraVo.m_preValueVo, paraVo.m_operator);
	}

	/**
	 * 判断当前动作是否为该单据类型的结束动作
	 * <li>FIXME:即是否为该单据类型的驱动动作？
	 * 
	 * @param actionName
	 * @return
	 * @throws PFBusinessException
	 */
	private boolean isDriveAction(String pkBilltype, String actionName) throws PFBusinessException {
		// 定义返回变量
		boolean retflag = true;
		Logger.debug("****判断动作" + actionName + "是否结束动作isDriveAction开始****");
		try {
			PfUtilDMO dmo = new PfUtilDMO();
			String realBilltype = PfUtilBaseTools.getRealBilltype(pkBilltype);
			retflag = dmo.queryLastStep(realBilltype, actionName);
			Logger.debug("==" + (retflag ? "是" : "不是") + "结束动作==");
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
		Logger.debug("****判断动作" + actionName + "是否结束动作isDriveAction结束****");
		return retflag;
	}

	/**
	 * 实例化动作脚本类，并执行
	 * @param actionName 动作编码,比如"SAVE","EDIT"
	 * @param paraVo
	 * @return
	 */
	private Object actionOnStep(String actionName, PfParameterVO paraVo) throws BusinessException {
		// 平台日志
		Logger.debug(">>>PFBusiAction.actionOnStep(" + actionName + "," + paraVo.m_billType + ") 开始<<<");
		long begin = System.currentTimeMillis();

		// 方法返回参数
		Object actionReturnObj = null;

		/* 支持单据驱动保存、审核,因为保存时本单据无主键信息,所以在审核时必须获得主键 */
		if (paraVo.m_billVersionPK == null && paraVo.m_preValueVo != null) {
			paraVo.m_billVersionPK = paraVo.m_preValueVo.getParentVO().getPrimaryKey();
			Logger.debug("*********单据驱动保存、审核(获得驱动时单据主键)*****");
		}
		actionReturnObj = new PFRunClass().runComBusi(paraVo, UFBoolean.FALSE, actionName);

		long end = System.currentTimeMillis();
		Logger.info(">>>PFBusiAction.actionOnStep(" + actionName + "," + paraVo.m_billType + ") 结束,耗时=" + (end - begin) + "ms<<<");
		return actionReturnObj;
	}

	/**  xry TODO:
	 * 执行动作驱动
	 * <li>源单据类型+源动作可能推多个单据类型的多个动作
	 * <li>如果某目的单据类型存在SAVE/EDIT动作,从而导致启动了审批流,则不再执行该单据类型的其他动作
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
		Logger.debug("*********执行动作驱动actiondrive开始********");
		// 1.获得当前单据动作的可驱动动作
		PfUtilActionVO[] drivedActions = queryActionDriveVOs(srcParaVo);
		String srcBilltype = srcParaVo.m_billType;

		if (drivedActions == null || drivedActions.length == 0) {
			Logger.debug("该动作=" + srcParaVo.m_actionName + "没有可驱动的动作");
			return false;
		}

		// 已执行的驱动动作
		LinkedHashSet<String> hsFinishedDriveAction = new LinkedHashSet<String>();
		// 已经启动审批流（或工作流）的单据类型
		LinkedHashSet<String> hsFlowStartedBilltypes = new LinkedHashSet<String>();

		// 下游单据VO
		// AggregatedValueObject destVo = null;
		AggregatedValueObject[] destVos = null;
		// 源单据的操作员
		String srcOperator = srcParaVo.m_operator;

		
		
		for (int i = 0; i < drivedActions.length; i++) { // 遍历被驱动的动作
			
			//深度clone一份来源vo，分单导致来源vo变化。
			AggregatedValueObject srcBillCloneVO =(AggregatedValueObject) deepClone(srcBillVO);
			
			// 被驱动的单据类型
			String destBillType = drivedActions[i].getBillType();
			// 被驱动的单据动作
			String beDrivedActionName = drivedActions[i].getActionName();

			String currentExecDrive = destBillType + ":" + beDrivedActionName;
			Logger.debug("执行驱动" + currentExecDrive + "开始");
			if (hsFinishedDriveAction.contains(currentExecDrive)) {
				Logger.debug("被驱动动作:" + currentExecDrive + "已执行,继续循环操作.");
				continue;
			}

			if (hsFlowStartedBilltypes.contains(destBillType)) {
				// 如果这个单据类型启动了审批流（或工作流）,则不再执行其后继动作 lj@2005-6-7
				Logger.debug("被驱动单据类型启动了审批流（或工作流），不执行驱动" + currentExecDrive);
				continue;
			}
			// 0.被驱动动作的约束检查 leijun+2008-7
			PFActionConstrict aConstrict = new PFActionConstrict();
			boolean isPermit = aConstrict.actionConstrictBeforeDrive(drivedActions[i].getPkMessageDrive(), destBillType, beDrivedActionName, srcParaVo);
			if (!isPermit) {
				Logger.debug("当前驱动" + currentExecDrive + "的约束 不满足，继续下个驱动");
				continue;
			}
			hsFinishedDriveAction.add(currentExecDrive);

			// 0.1由业务来判定是否允许当前驱动 leijun+2008-10
			Object checkClzInstance = PfUtilTools.getBizRuleImpl(srcBilltype);
			if (checkClzInstance != null && checkClzInstance instanceof IActionDriveChecker) {
				boolean isPermited = ((IActionDriveChecker) checkClzInstance).isEnableDrive(srcBilltype, srcBillCloneVO, srcParaVo.m_actionName, destBillType, beDrivedActionName);
				if (!isPermited) {
					Logger.debug("当前驱动" + currentExecDrive + "的校验不满足，继续下个驱动");
					continue;
				}
			}

			// 1.获得当前驱动的paraVo
			PfParameterVO destParaVo = null;
			//用单据类型+来源单据主键作为key  一推到底的业务流产生分单时只用单据类型作为key有问题。
			String src_billtypePK =StringUtil.isEmptyWithTrim(srcBillVO.getParentVO().getPrimaryKey())?"":srcBillVO.getParentVO().getPrimaryKey();
			if ((hashBilltypeToParavo.containsKey(destBillType+src_billtypePK))||PfUtilBaseTools.getRealBilltype(srcBilltype).equals(PfUtilBaseTools.getRealBilltype(destBillType))) {
				//FIXME:同种单据类型之间也不交换
				Logger.debug("驱动所需的paraVo已存在，无需VO交换");
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
				Logger.debug("不存在被驱动单据VO,则进行以源单据为准的VO数据转换");
				// 校验是否允许做VO交换 leijun+2006-8-18
				if (checkClzInstance instanceof IChangeVOCheck) {
					boolean bValid = ((IChangeVOCheck) checkClzInstance).checkValidOrNeed(srcBillCloneVO, srcParaVo.m_actionName, destBillType, beDrivedActionName);
					if (!bValid) {
						Logger.debug("源单据VO不允许数据转换，则继续下个驱动");
						continue;
					}
				}

				// 进行VO数据转换
				IPfExchangeService exchangeService = NCLocator.getInstance().lookup(IPfExchangeService.class);
				destVos = exchangeService.runChangeDataAry(srcBilltype, destBillType, new AggregatedValueObject[] { srcBillCloneVO }, srcParaVo);
				Logger.debug("获得单据:" + destBillType + "的数据交换VO完成");

				if (destVos == null || destVos.length == 0) {
					// WARN::如果交换到的单据VO为空，则继续下个驱动
					Logger.warn(">交换到的单据VO为空，则继续下个驱动");
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
				Logger.debug("进行单据:" + destBillType + "的数据数组VO[0]完成");
				// WARN::从刚产生的destVo中获取工作流参数VO!
				destParaVo = PfUtilBaseTools.getVariableValue(destBillType, beDrivedActionName, null, destVos, userObj, driveObjs, null, eParam, hashBilltypeToParavo,src_billtypePK);
				destParaVo.m_splitValueVos = destVos;
				// WARN::如果destVo中没有获取到operator信息,则赋值为驱动单据的操作员
				// lj@2005-7-11
				if (destParaVo.m_operator == null)
					destParaVo.m_operator = srcOperator;
			}

			// 2.执行驱动动作
			// 2.1.进行动作前约束检查
			aConstrict.actionConstrictBefore(destParaVo);

			// 2.2.执行动作脚本
			Object tmpObj = actionOnStep(beDrivedActionName, destParaVo);

			// 2.3.进行动作后约束检查
			aConstrict.actionConstrictAfter(destParaVo);

			// 2.4.上游消息处理
			backMsg(destParaVo, tmpObj);

			// 2.5.动作驱动, 为了支持单据动作的级联驱动，此处再次调用actiondrive, @guowl+.
			if (isDriveAction(destBillType, beDrivedActionName)) {
				if (destParaVo.m_splitValueVos == null)
					continue;
				// XXX:如果动作执行中发生分单，则逐个级联驱动 leijun@2008-12
				for (int j = 0; j < destParaVo.m_splitValueVos.length; j++) {
					actiondrive(destParaVo.m_splitValueVos[j], userObj, hashBilltypeToParavo, hashMethodReturn, destParaVo, eParam);
				}
			}

			// 3.根据被驱动的动作，尝试启动审批流或工作流
			boolean bAfStarted = false;
			if (beDrivedActionName.toUpperCase().endsWith(IPFActionName.SAVE) || beDrivedActionName.toUpperCase().endsWith(IPFActionName.EDIT)) {
				// 如果被驱动的动作以"SAVE","EDIT"结尾,则启动审批流
				bAfStarted = (Boolean)startApproveFlowAfterAction(destParaVo.m_billType, destVos[0], userObj, tmpObj, null, hashBilltypeToParavo, hashMethodReturn,src_billtypePK)[0];
				if (bAfStarted){
					hsFlowStartedBilltypes.add(destBillType);
					sendMessageWhenStartWorkflow(destParaVo,WorkflowTypeEnum.Approveflow.getIntValue());
				}
					
			} else if (beDrivedActionName.toUpperCase().endsWith(IPFActionName.START)) {  
				// 如果被驱动的动作为"START",则启动工作流
				boolean bWfStarted = startWorkflowAfterAction(destParaVo.m_billType, destVos[0], userObj, tmpObj, null, hashBilltypeToParavo, hashMethodReturn,src_billtypePK);
				if (bWfStarted){
					hsFlowStartedBilltypes.add(destBillType);
					sendMessageWhenStartWorkflow(destParaVo,WorkflowTypeEnum.Workflow.getIntValue());
				}
					
			}
			
			// 4.如果未启动审批流，且来源目的单据不同，才发送"推式"消息 lj+2006-5-30
			/* xry TODO:
			if (!bAfStarted && !srcBilltype.equals(destBillType))
				insertPushWorkitems(destParaVo, srcBilltype, destBillType, tmpObj, srcParaVo.m_billVersionPK);
				* /

			lastParamVO =destParaVo;
			Logger.debug("***执行驱动单据动作:" + currentExecDrive + "结束***");
		}// /{end for}

		Logger.debug("*********执行动作驱动actiondrive结束********");
		return true;
	}
	
*/

	  /**
	   * 进行深度克隆
	   * 
	   * @param oIn 要克隆的对象
	   * @return 克隆出来的新对象
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
	 * 业务流的跳转
	 * @throws BusinessException 
	 * /
	private void jumpBusitype(PfParameterVO paravo) throws BusinessException {
		if(paravo==null)
			return;
		String pk_busitype = paravo.m_businessType;
		// 参与者为当前登陆人
		String operator =InvocationInfoProxy.getInstance().getUserId();
		// 单据类型或者交易类型编码
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
			//支持业务流的跳转
			if (co.size() > 0 && paravo.m_preValueVos != null) {
				HashMap<String, String> busitypeMaps = new HashMap<String, String>();
				for (AggregatedValueObject vo : paravo.m_preValueVos) {
					IFlowBizItf fbi = PfMetadataTools.getBizItfImpl(vo, IFlowBizItf.class);
					//未实现业务接口的单据，直接跳过
					if(fbi==null)
						continue;
					String destBusitypePk = null;
					fbi.getBusitype();
					//有可能返回的是交易类型
					String billtype=fbi.getBilltype();
					if(!StringUtil.isEmptyWithTrim(billtype)){
						//确保返回的是单据类型编码
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
					//如果没有找到要跳转的业务流
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
	 * 回写单据状态，业务流程跳转使用
	 * */
	private void callbackBillStatus(JumpStatusCallbackContext context){
		//单据状态回写
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
	 * 发送"推式"业务流消息
	 * <li>先删除该单据产生的拉式消息
	 * 
	 * @param paravo
	 * @param srcBillType
	 * @param destBillType
	 * @param retObj
	 * @param srcBillId
	 *            来源单据ID
	 * /
	private void insertPushWorkitems(PfParameterVO paravo, String srcBillType, String destBillType, Object retObj, String srcBillId) {
		Logger.debug(">>发送推式消息=" + destBillType + "开始");
		String pkGroup = paravo.m_pkGroup;
		String pk_busitype = paravo.m_businessType;
		String senderman = paravo.m_operator;

		// 判断是否发送推式消息
		boolean isNeed = false; // XXX:leijun 2009-12 判断是否需要发送下游消息
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
			Logger.debug(">>源单据" + srcBillType + "不可发送下游消息，返回");
			return;
		}

		// XXX:分单VO数组
		if (paravo.m_splitValueVos == null || paravo.m_splitValueVos.length == 0)
			return;

		Logger.debug(">>推式消息，分单数=" + paravo.m_splitValueVos.length);

		IPfPersonFilter2 filter = null;
		try {
			Object checkClzInstance = PfUtilTools.getBizRuleImpl(paravo.m_billType);
			if (checkClzInstance instanceof IPfPersonFilter2)
				filter = (IPfPersonFilter2) checkClzInstance;
		} catch (BusinessException ex) {
			// XXX:仅日志异常，继续执行
			Logger.error("流程平台：查找业务流下游消息人员过滤IPfPersonFilter2接口异常：" + ex.getMessage(), ex);
		}

		for (int k = 0; k < paravo.m_splitValueVos.length; k++) {
			// 遍历每个分单的目的单据VO
			AggregatedValueObject billvo = paravo.m_splitValueVos[k];
			// 再次获取BillId,BillNo
			PfUtilBaseTools.fetchBillId(paravo, billvo, retObj);
			IPFConfig pfcfg = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());
			try {
				// 获得所有待发送消息的用户(流程配置 -- 参与者配置)
				String[] hsUserPKs = pfcfg.queryForwardMessageUser(srcBillType, destBillType, pk_busitype, billvo, filter);
				if (hsUserPKs == null || hsUserPKs.length == 0) {
					Logger.warn(">>无法发送推式消息，因为接收用户为空");
					return;
				}

				// 4.给这些用户发送"推式"工作流消息
				ArrayList<MessageinfoVO> alItems = new ArrayList<MessageinfoVO>();
				for (String userId : hsUserPKs) {
					MessageinfoVO wi = new MessageinfoVO();
					wi.setPk_billtype(destBillType);
					wi.setPk_srcbilltype(srcBillType);
					wi.setBillid(paravo.m_billVersionPK); // 下游单据ID
					wi.setBillno(paravo.m_billNo);
					wi.setCheckman(userId);
					// FIXME::i18n
					wi.setTitle(Pfi18nTools.i18nBilltypeName(srcBillType, null) + NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFBusiAction-0000")/*推式产生新单据：* / + Pfi18nTools.i18nBilltypeName(destBillType, null) + paravo.m_billNo + NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFBusiAction-0001")/*，请处理* /);
					// wi.setPk_busitype(pk_busitype);
					wi.setPk_corp(pkGroup);
					wi.setSenderman(senderman);
					alItems.add(wi);
				}

				// 如果要发送推式消息，则先删除拉式消息
				if (alItems.size() > 0) {
					PfMessageUtil.deletePullMessage(srcBillId);
					new PFMessageImpl().insertPushOrPullMsgs((MessageinfoVO[]) alItems.toArray(new MessageinfoVO[alItems.size()]), MessageTypes.MSG_TYPE_BUSIFLOW_PUSH);
					Logger.debug(">>发送推式消息数=" + alItems.size());
				}
			} catch (Exception e) {
				// WARN::仅仅日志异常，不能影响业务流程
				Logger.error(e.getMessage(), e);
			}
		} // /{end for}
		Logger.debug(">>发送推式消息=" + destBillType + "结束");
	}
	*/

	/**
	 * 获得当前单据动作所有可驱动的动作
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

			// FIXME::arap升级要求-如果交易类型没有配置下游驱动，则继续找单据类型的下游驱动
			if (driveActions == null || driveActions.length == 0) {
				if (PfUtilBaseTools.isTranstype(paraVo.m_billType)) {
					Logger.debug("找不到交易类型的动作驱动，继续找单据类型的下游驱动");
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
		Logger.debug("*后台单据动作处理PFBusiAction.processAction开始");
		debugParams(actionName, billOrTranstype, workflowVo, billvo, userObj, eParam);
		long start = System.currentTimeMillis();

		Hashtable hashBilltypeToParavo = new Hashtable();
		Hashtable hashMethodReturn = new Hashtable();
		
		
		// 0.处理工作项(上传附件)
		processWorknote(workflowVo);

		// 1.构造动作处理的参数VO
		AggregatedValueObject[] inVos = null;
		if (billvo != null) {
			inVos = PfUtilBaseTools.pfInitVos(billvo.getClass().getName(), 1);
			inVos[0] = billvo;
		}
		PfParameterVO paraVoOfBilltype = PfUtilBaseTools.getVariableValue(billOrTranstype, actionName, billvo, inVos, userObj, null, workflowVo, eParam, hashBilltypeToParavo);

		try {
			// 2.如果为删除动作，则删除流程信息
			if (PfUtilBaseTools.isDeleteAction(paraVoOfBilltype.m_actionName, paraVoOfBilltype.m_billType))
				deleteWorkFlow(paraVoOfBilltype);

			// 3.进行动作前约束检查
			IPFActionConstrict aConstrict = new PFActionConstrict();
			aConstrict.actionConstrictBefore(paraVoOfBilltype);

			// 4.执行动作脚本
			Object retObj = actionOnStep(paraVoOfBilltype.m_actionName, paraVoOfBilltype);

			// 5.进行动作后约束检查
			aConstrict.actionConstrictAfter(paraVoOfBilltype);

			// 6.脚本返回值的处理
			if (retObj instanceof nc.bs.pub.compiler.IWorkFlowRet) {
				// XXX::只有脚本调用procActionFlow@@,且在流程审批进行中时才返回该对象
				return ((nc.bs.pub.compiler.IWorkFlowRet) retObj).m_inVo;
			}

			try {
				Object[] tmpObj = (Object[]) retObj;
				Hashtable hasNoProc = null;
				// XXX::必须保证批量的动作脚本具有这样的返回值 lj
				if (tmpObj != null && tmpObj.length > 0 && tmpObj[0] instanceof IWorkflowBatch) {
					IWorkflowBatch wfBatch = (IWorkflowBatch) tmpObj[0];
					hasNoProc = wfBatch.getNoPassAndGoing();
					Object[] userObjs = (Object[]) wfBatch.getUserObj();
					retObj = userObjs[0];
				}
				if (hasNoProc != null && hasNoProc.containsKey("0"))
					return null;
			} catch (Exception e) {
				// FIXME::如果动作脚本返回值非数组或其他异常，都忽略之
			}

			// 7.上游消息处理
			backMsg(paraVoOfBilltype, retObj);

			/* xry TODO:
			// 8.动作驱动
			if (isDriveAction(paraVoOfBilltype.m_billType, paraVoOfBilltype.m_actionName)&&isNeedDriveAction(retObj)) {
				actiondrive(paraVoOfBilltype.m_preValueVo, userObj, hashBilltypeToParavo, hashMethodReturn, paraVoOfBilltype, eParam);
			}
			
			*/
			String src_billtypePK =paraVoOfBilltype.m_preValueVo!=null?StringUtil.isEmptyWithTrim(paraVoOfBilltype.m_preValueVo.getParentVO().getPrimaryKey())?"":paraVoOfBilltype.m_preValueVo.getParentVO().getPrimaryKey():"";

			// 9.如果动作为提交，尝试启动审批流或工作流
			if (PfUtilBaseTools.isSaveAction(paraVoOfBilltype.m_actionName, paraVoOfBilltype.m_billType)
					||PfUtilBaseTools.isStartAction(paraVoOfBilltype.m_actionName, paraVoOfBilltype.m_billType)){
				retObj = startWorkflowAfterAction(paraVoOfBilltype.m_billType, paraVoOfBilltype.m_preValueVo, 
						userObj, retObj, eParam, hashBilltypeToParavo, hashMethodReturn,src_billtypePK)[1];
				sendMessageWhenStartWorkflow(paraVoOfBilltype,WorkflowTypeEnum.Approveflow.getIntValue());
			}				
			
			/* xry TODO:
			//10 .如果业务流符合跳转的条件，执行流程的跳转
			jumpBusitype(lastParamVO);
			*/	

			// 清空引擎实例池中的数据，否则无法释放
			WfInstancePool.getInstance().clear();

			Logger.debug("*后台单据动作处理PFBusiAction.processAction结束，耗时" + (System.currentTimeMillis() - start) + "ms");

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
	 * 当前环节是否需要执行动作驱动 
	 * @param eParam 执行动作脚本后返回的obj
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
			Logger.error("记录流程异常日志出错！");
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 日志一下动作处理的上下文参数
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
	 * 上游消息处理
	 * 
	 * @param paraVoOfBilltype
	 */
	private void backMsg(PfParameterVO paravo, Object retObj) {
		Logger.debug(">>上游消息处理=" + paravo.m_billType + "开始");

		// 1.判断是否需要发送上游消息
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
					Logger.debug(">>单据" + paravo.m_billType + "不可发送上游消息，返回");
					return;
				}
			}
		} catch (DAOException ex) {
			Logger.error(ex.getMessage(), ex);
			return;
		}

		// 2.查询当前单据动作 所配置的上游消息
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
				Logger.debug(">>单据" + paravo.m_billType + "没有进行上游消息配置，返回");
				return;
			}

			// 3.根据单据类型注册的审批流检查类查询上游单据信息，并执行上游消息发送
			BilltypeVO billVo = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(paravo.m_billType).buildPkGroup(paravo.m_pkGroup));
			if (billVo.getCheckclassname() != null && billVo.getCheckclassname().trim().length() != 0) {
				Object obj = PfUtilTools.instantizeObject(billVo.getPk_billtypecode(), billVo.getCheckclassname().trim());
				if (obj instanceof IPFSourceBillFinder) {
					IPFSourceBillFinder srcFinder = (IPFSourceBillFinder) obj;
					// 再次获取BillId,BillNo
					PfUtilBaseTools.fetchBillId(paravo, paravo.m_preValueVo, retObj);

					for (Iterator iter = coBackmsg.iterator(); iter.hasNext();)
						// 遍历的上游配置
						executeBackmsgs(srcFinder, (BackmsgVO) iter.next(), paravo);
				} else {
					Logger.debug(">>单据" + paravo.m_billType + "的审批流检查类没有实现接口IPFSourceBillFinder，返回");
					return;
				}
			} else {
				Logger.debug(">>单据" + paravo.m_billType + "没有注册审批流检查类，返回");
				return;
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 发送上游消息
	 * 
	 * @param srcFinder
	 * @param backmsgVO
	 * @param paravo
	 * @throws BusinessException
	 */
	private void executeBackmsgs(IPFSourceBillFinder srcFinder, BackmsgVO backmsgVO, PfParameterVO paravo) throws BusinessException {
		Logger.debug(">>给上游单据" + backmsgVO.getPk_destbilltype() + "发送上游消息 开始");

		// 1.查询当前单据的上游单据信息
		SourceBillInfo[] infos = srcFinder.findSourceBill(backmsgVO.getPk_destbilltype(), paravo.m_preValueVo);
		HashSet<String> hsBillmakers = new HashSet<String>();
		HashSet<String> hsApprovers = new HashSet<String>();
		for (int i = 0; i < (infos == null ? 0 : infos.length); i++) {
			hsBillmakers.add(infos[i].getBillmaker());
			hsApprovers.add(infos[i].getApprover());
		}

		// 2.构造消息内容并发送
		// TODO::i18n
		ArrayList alActions = PfDataCache.getBillactionVOs(paravo.m_billType);
		String strDefaultName = Pfi18nTools.findActionName(paravo.m_actionName, alActions);
		String msgContent = NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFBusiAction-0002")/*下游单据：*/ + Pfi18nTools.i18nBilltypeName(paravo.m_billType, null) + " " + paravo.m_billNo + NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFBusiAction-0003")/*进行了单据动作：*/
				+ Pfi18nTools.i18nActionName(paravo.m_billType, paravo.m_actionName, strDefaultName) + "(" + paravo.m_actionName + ")" + NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFBusiAction-0004")/*的处理，请查看*/;
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

		Logger.debug(">>给上游单据" + backmsgVO.getPk_destbilltype() + "发送上游消息 结束");
	}

	/**
	 * 构造上游消息对象
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
					//可直接审批通过？？
					noteArray[i] = new WorkflownoteVO();
					noteArray[i].setAnyoneCanApprove(true);
					noteArray[i].setApproveresult("Y");
				} else if (!"R".equals(worknoteVO.getApproveresult())){	// 对于非驳回操作，需要判断指派信息
					if(noteArray[i].getAssignableInfos()!= null && noteArray[i].getAssignableInfos().size() > 0){
						//需要指派的单据，不再继续执行流程，在此处设置为空
						noteArray[i]=null;
						throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFBusiAction-0005")/*单据需要指派，请后续单独审核*/);
					}
					if(noteArray[i].getTransitionSelectableInfos()!= null && noteArray[i].getTransitionSelectableInfos().size() > 0){
						throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFBusiAction-0006")/*单据需要手工选择分支，请后续单独处理*/);
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

			} catch (Exception e) {//FIXME：Exception对象的返回，将BusinessException改为Exception，避免异常将错误信息冲掉
				Logger.error(e);
				exceptionInfo.putErrorMessage(i, billvos[i], e.getMessage());
			}
			ret = PfUtilBaseTools.composeResultAry(singleVal, billvos.length, i, ret);
		}
		return ret;
	}

	public Object[] processBatch(String actionName, String billOrTranstype, AggregatedValueObject[] billvos, Object[] userObjAry, WorkflownoteVO worknoteVO, HashMap eParam)
			throws BusinessException {
		// 平台日志
		Logger.init("workflow");
		Logger.debug("*后台单据动作批处理PFBusiAction.processBatch开始");
		debugParams(actionName, billOrTranstype, worknoteVO, billvos, userObjAry, null);
		long start = System.currentTimeMillis();

		if (billvos == null)
			return null;

		// 进行约束检查
		IPFActionConstrict aConstrict = new PFActionConstrict();

		// 初始化环境
		Hashtable<String, Object> hashBilltypeToParavo = new Hashtable<String, Object>();
		Hashtable hashMethodReturn = new Hashtable();
		PfParameterVO paravoOfLastSrcBill = null;
		for (int i = 0; i < billvos.length; i++) {
			PfParameterVO paraOfThisBill = null;
			// 遍历单据VO数组
			if (userObjAry != null && userObjAry.length >= 1) {
				// 获取类变量
				paraOfThisBill = PfUtilBaseTools.getVariableValue(billOrTranstype, actionName, billvos[i], billvos, userObjAry[i], userObjAry, worknoteVO, eParam, hashBilltypeToParavo);
			} else {
				// 获取类变量
				paraOfThisBill = PfUtilBaseTools.getVariableValue(billOrTranstype, actionName, billvos[i], billvos, null, null, worknoteVO, eParam, hashBilltypeToParavo);
			}
			// 2.如果为删除动作，则删除流程信息
			if (PfUtilBaseTools.isDeleteAction(paraOfThisBill.m_actionName, paraOfThisBill.m_billType))
				deleteWorkFlow(paraOfThisBill);

			// 对每个单据VO，都进行动作前约束检查
			aConstrict.actionConstrictBefore(paraOfThisBill);

			if (i == billvos.length - 1) {
				paravoOfLastSrcBill = paraOfThisBill;
			}
		} // /{end for}

		paravoOfLastSrcBill.m_preValueVo = null;
		// 执行单据动作->WARN::此时的m_paraVo为最后一个单据的参数VO
		Object[] retObjs = (Object[]) actionOnStep(paravoOfLastSrcBill.m_actionName, paravoOfLastSrcBill);

		// 定义未通过或进行中的单据索引号
		Hashtable hasNoProc = null;
		// WARN::必须保证批量的动作脚本具有这样的返回值 lj
		if (retObjs != null && retObjs.length > 0 && retObjs[0] instanceof IWorkflowBatch) {
			IWorkflowBatch wfBatch = (IWorkflowBatch) retObjs[0];
			hasNoProc = wfBatch.getNoPassAndGoing();
			retObjs = (Object[]) wfBatch.getUserObj();
		}

		if (hasNoProc == null) {
			hasNoProc = new Hashtable();
		}

		// 说明VO数据已修改后的为准,其他数据已
		// 以下操作必须从全局变量paraVo中获取VO数组,不能用原VO进行操作
		AggregatedValueObject[] beforeVos = paravoOfLastSrcBill.m_preValueVos;
		for (int i = 0; beforeVos != null && i < beforeVos.length; i++) {
			// XXX:guowl+,
			// 从第二张单据开始,清空workflownote，因为前台传过来的workflownote是根据第一张单据查的
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
			// leijun@2008-9 重新获取UserObjects,有可能动作脚本修改了它
			userObjAry = paravoOfLastSrcBill.m_userObjs;
			if (userObjAry != null && userObjAry.length != 0) {
				tmpActionObj = userObjAry[i];
			}
			// 置入新数据
			PfParameterVO currParaVo = null;
			if (userObjAry != null && userObjAry.length >= 1) {
				// 获取类变量
				currParaVo = PfUtilBaseTools.getVariableValue(billOrTranstype, actionName, beforeVos[i], beforeVos, tmpActionObj, userObjAry, worknoteVO, eParam, hashBilltypeToParavo);
			} else {
				// 获取类变量
				currParaVo = PfUtilBaseTools.getVariableValue(billOrTranstype, actionName, beforeVos[i], beforeVos, null, null, worknoteVO, eParam, hashBilltypeToParavo);
			}

			// 对每个单据VO，都进行动作后约束检查
			aConstrict.actionConstrictAfter(currParaVo);

			// 上游消息处理
			backMsg(currParaVo, retObjs == null ? null : retObjs[i >= retObjs.length ? 0 : i]);

			// 动作驱动
			String strActionNameOfPara = currParaVo.m_actionName;
			
			String src_billtypePK =currParaVo.m_preValueVo!=null?StringUtil.isEmptyWithTrim(currParaVo.m_preValueVo.getParentVO().getPrimaryKey())?"":currParaVo.m_preValueVo.getParentVO().getPrimaryKey():"";
			
			/* xry TODO:
			if (isDriveAction(currParaVo.m_billType, strActionNameOfPara)) {
				// XXX::必须重新实例化，避免批量处理时的垃圾数据
				hashBilltypeToParavo = new Hashtable<String, Object>();
				hashBilltypeToParavo.put(currParaVo.m_billType+src_billtypePK, currParaVo);
				actiondrive(currParaVo.m_preValueVo, tmpActionObj, hashBilltypeToParavo, hashMethodReturn, currParaVo, new HashMap());
			}
			*/
			

			// 尝试启动审批流或工作流
			if (PfUtilBaseTools.isSaveAction(strActionNameOfPara, currParaVo.m_billType) ||
					PfUtilBaseTools.isStartAction(strActionNameOfPara, currParaVo.m_billType)){
				startWorkflowAfterAction(currParaVo.m_billType, beforeVos[i], tmpActionObj, retObjs == null ? null : retObjs[i], eParam, hashBilltypeToParavo, hashMethodReturn,src_billtypePK);
				sendMessageWhenStartWorkflow(currParaVo,WorkflowTypeEnum.Approveflow.getIntValue());
			}				
		}

		// 清空引擎实例池中的数据，否则无法释放
		WfInstancePool.getInstance().clear();

		Logger.debug("*后台单据动作批处理PFBusiAction.processBatch结束，耗时" + (System.currentTimeMillis() - start) + "ms");

		return retObjs;
	}

}