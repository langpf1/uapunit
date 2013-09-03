package uap.workflow.app.message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pf.pub.PfDataCache;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.ml.DataMultiLangAccessor;
import nc.itf.uap.pf.IPFConfig;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.message.templet.bs.IMsgVarCalculater;
import nc.message.templet.bs.MsgContentCreator;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.MultiLangContext;
import nc.vo.pf.msg.WorkflowMsgTempVar;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkitemconfigVO;
import nc.vo.wfengine.engine.exception.EngineException;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.pub.app.message.WorkitemMsgContext;

/**
 * 解析审批任务的标题信息
 *
 * @author wzhy 2005-1-2
 * @modifier leijun 2005-3-30 多语化i18n
 * @modifier leijun 2006-3-29 使用定制的配置来填充工作项的标题
 * @modifier leijun 2006-11-20 对于UFDouble类型值，精度默认为2
 * @modifier leijun 2007-11-29 增加取编码或取名称的功能
 * @modifier leijun 2008-9 针对工作流的工作项作出修改
 */
public class TaskTopicResolver {
	private TaskTopicResolver() {
		super();
	}

//	/**
//	 * 设置该审批任务的描述信息
//	 */
//	public static void fillNotifyInfo(TaskInstanceVO task, Vector tasks,
//			boolean checkParticipant) {
//		if (tasks == null || tasks.size() == 0)
//			return;
//
//		int actType = task.getCreate_type();
//
//
//		String actionType = getActionType(actType, true);
//		constructTopic(tasks, actionType);
//
//		// 如果是迁移到制单节点、审批节点或者审批子流程,需要填充通知信息。
//		// 其中审批子流程的新产生任务的topic由主流程决定
////		if (actType == ActivityTypeEnum.Checkbill.getIntValue()) {
////			// 审批单据
////			String actionType = "{checkBill}";
////			constructTopic(context, tasks, actionType);
////		} else if (actType == ActivityTypeEnum.Manual.getIntValue()) {
////			// 处理单据
////			String actionType = "{dealBill}";
////			constructTopic(context, tasks, actionType);
////		} else if (actType == ActivityTypeEnum.Subflow.getIntValue()) {
////			// 子流程
////			String actionType = isWorkflow ? "{dealBill}" : "{checkBill}";
////			constructTopic(context, tasks, actionType);
////		} else if (actType == ActivityTypeEnum.Makebill.getIntValue()) {
////			// 修改单据
////			String actionType = "{modifyBill}";
////			constructTopic(context, tasks, actionType);
////		}
//	}

	public static String getActionType(int actType, boolean isWorkflow) {
		if (actType == TaskInstanceCreateType.Normal.getIntValue()) {
			// 审批单据
			return "{checkBill}";
		} else if (actType == TaskInstanceCreateType.Normal.getIntValue()) {
			// 处理单据
			return "{dealBill}";
		} else if (actType == TaskInstanceCreateType.AfterAddSign.getIntValue()) {
			// 子流程
			return isWorkflow ? "{dealBill}" : "{checkBill}";
		} else if (actType == TaskInstanceCreateType.Reject.getIntValue()) {
			// 修改单据
			return "{modifyBill}";
		} else {
			return "N/A";
		}
	}

//	private static void constructTopic(Vector tasks, String actionType) {
//		fillMsgContext(tasks, actionType);
//	}

//	public static WorkitemMsgContext getMsgContext(TaskInstanceVO task, String actionType) {
//
////		boolean isWorkflow = WorkflowTypeEnum.Workflow.getIntValue() == context.getWfProcessDef().getWorkflowType();
////		int actType = context.getTransferTargetActivityInstance().getActivity().getActivityType();
////
////		String actionType = getActionType(actType, isWorkflow);
//
//		// 审批结果：提交、通过、不通过、驳回、弃审
//		String approveResult = getReason(task);
//		// 发送人名称：
//		String senderId = getInformerId(task);
//
//		String agentInfo = "";
//
//		WorkitemMsgContext msgContext = new WorkitemMsgContext();
//
////		if (task.getParticipantProcessMode() == WFTask.ProcessMode_Single_Together
////				|| task.getParticipantProcessMode() == WFTask.ProcessMode_Single_Race) {
//		
//			// 这里只有参与者为操作员的活动才获取代理人信息
//			
//			agentInfo = getAgentInfo(null, task);//TODO:不再设置流程上的代理人
////		}
//
//		Logger.debug("1__________get pk_msgtemp: ");
//		String tempcode = getMsgTempCode(task);
//
//		String checknote = task.getOpinion();
//
//		Object busiObj = null;//task.getInObject();
//		if (busiObj == null) {
//			try {
//				busiObj = NCLocator.getInstance().lookup(IPFConfig.class)
//						.queryBillDataVO(task.getPk_bizobject(), task.getPk_form_ins_version());
//			} catch (BusinessException e) {
//				Logger.error(e.getMessage(), e);
//			}
//		}
//
//		msgContext.setSender(senderId);
//		msgContext.setBillno(task.getForm_no());
//		msgContext.setBillType(task.getPk_bizobject());
//		msgContext.setBillid(task.getPk_form_ins_version());
//		msgContext.setResult(approveResult);
//		msgContext.setActionType(actionType);
//		msgContext.setAgent(agentInfo);
//		msgContext.setCheckNote(checknote);
//		msgContext.setBusiObj(busiObj);
//		msgContext.setMsgtempcode(tempcode);
//
//		Logger.debug("5______________print out context: ");
//		Logger.debug("6_____________senderName: " + senderId);
//		Logger.debug("7_____________billno: " + msgContext.getBillno());
//		Logger.debug("8_____________billtype: " + msgContext.getBillType());
//		Logger.debug("9____________billid: " + msgContext.getBillid());
//		Logger.debug("10___________approveresult: " + msgContext.getResult());
//		Logger.debug("11____________action type: " + msgContext.getActionType());
//		Logger.debug("12____________agent info: " + msgContext.getAgent());
//		Logger.debug("13____________checknote: " + msgContext.getCheckNote());
//		Logger.debug("15____________busiObj: " + msgContext.getBusiObj());
//
//		return msgContext;
//	}

//	private static void fillMsgContext(Vector<TaskInstanceVO> tasks, String actionType) {
//		for (int i = 0; i < tasks.size(); i++) {
//			TaskInstanceVO task = tasks.get(i);
//			WorkitemMsgContext msgContext = getMsgContext(task, actionType);
//			//task.setContext(msgContext);
//			//Debug.throwRuntimeException("task.setContext(msgContext);");
//		}
//	}

	public static NCMessage constructNCMsg(WorkitemMsgContext context) {
		String tempcode = context.getMsgtempcode();

		Logger.debug("16____________construct ncmsg, tempcode: " + tempcode);

		if (!StringUtil.isEmptyWithTrim(tempcode)) {
			return constructByTemplet(context);
		} else {
			return constructDefault(context);
		}
	}

	private static NCMessage constructByTemplet(WorkitemMsgContext context) {
		TaskTopicBusiVarCalculator calculator = new TaskTopicBusiVarCalculator(
				context);

		Logger.debug("17__________begin construct by templet");
		MsgContentCreator creator = new MsgContentCreator();

		NCMessage ncmsg = null;

		String checkman = context.getCheckman();

		try {
			// yanke+ 2011-8-25 加入多语种支持
			String langcode = getLangCodeOfUser(checkman);

			if (StringUtil.isEmptyWithTrim(langcode)) {
				langcode = MultiLangContext.getInstance().getCurrentLangVO()
						.getLangcode();
			}

			String pk_group = InvocationInfoProxy.getInstance().getGroupId();

			ncmsg = new NCMessage();
			Map<String, NCMessage> map = creator.createMessageUsingTemp(
					context.getMsgtempcode(), pk_group, new String[] { langcode }, ncmsg,
					calculator, context.getBusiObj(), null);

			ncmsg = map.get(langcode);

			if (ncmsg == null)
				throw new PFBusinessException(
						"Error constructing NCMessage using MsgContentCreator!");
			Logger.debug("18_____________construct by templet completed");
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			Logger.debug("使用MsgContentCreator构建工作项消息出错，将改用默认格式构造消息");

			Logger.debug("19_____________error constructing by templet, use default instead");
			ncmsg = constructDefault(context);
		}

		return ncmsg;
	}

	private static NCMessage constructDefault(WorkitemMsgContext context) {

		IMsgVarCalculater calculator = new TaskTopicBusiVarCalculator(context);

		Logger.debug("20___________begin construct by default");

		StringBuffer sb = new StringBuffer();

		sb.append(calculator.calculateValue(WorkflowMsgTempVar.SENDER));
		sb.append(calculator.calculateValue(WorkflowMsgTempVar.RESULT));
		sb.append("{notify}");
		sb.append(calculator.calculateValue(WorkflowMsgTempVar.AGENT));
		sb.append(calculator.calculateValue(WorkflowMsgTempVar.ACTION_TYPE));
		sb.append(calculator.calculateValue(WorkflowMsgTempVar.BILL_NO));

		String topic = sb.toString();

		MessageVO msgVO = new MessageVO();
		msgVO.setSubject(topic);

		NCMessage ncmsg = new NCMessage();
		ncmsg.setMessage(msgVO);

		Logger.debug("21___________construct by default completed, title: "
				+ topic);

		return ncmsg;
	}

	/**
	 * 取得当前活动下定义的消息模板code
	 *
	 * @param context
	 * @param task
	 * @return 消息模板code。若未定义则返回null
	 */
	public static String getMsgTempCode(TaskInstanceVO task) {
		String tempcode = null;

		// 取当前活动定义的消息模板pk
		tempcode = task.getTitle();

		// 若流程定义中未为该活动配置消息模板，则取该单据类型的默认消息模板
		if (StringUtil.isEmptyWithTrim(tempcode)) {
			tempcode = getDefaultTempcodeOfBillType(task.getPk_bizobject());
		}
		return tempcode;
	}

	public static String getDefaultTempcodeOfBillType(String billType) {

		String tempcode = null;

		BilltypeVO bvo = PfDataCache.getBillType(billType);
		String billTypeId = bvo.getPk_billtypeid();

		// 若为交易类型，再取单据类型
		String parentType = bvo.getParentbilltype();
		BilltypeVO pbvo = PfDataCache.getBillType(parentType);
		String parentTypeId = pbvo == null ? null : pbvo.getPk_billtypeid();

		String sqlCond = null;

		if (StringUtil.isEmptyWithTrim(parentTypeId)) {

			// 若为单据类型，则查找该单据类型配置的默认模板
			sqlCond = "pk_billtype='" + billTypeId + "'";
		} else {

			// 若为交易类型，则查找交易类型及其单据类型配置的默认模板
			sqlCond = "pk_billtype in ('" + billTypeId + "', '" + parentTypeId
					+ "')";
		}

		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class);

		Collection<WorkitemconfigVO> c;
		try {
			c = query.retrieveByClause(WorkitemconfigVO.class, sqlCond);
			if (c != null && c.size() > 0) {
				Iterator<WorkitemconfigVO> it = c.iterator();

				Map<String, String> map = new HashMap<String, String>();

				// 将所有结果放到哈希表中
				// 结果中包含交易类型默认消息模板和单据类型默认消息模板
				while (it.hasNext()) {
					WorkitemconfigVO wicVO = it.next();
					map.put(wicVO.getPk_billtype(), wicVO.getItem());
				}

				// 取单据类型/交易类型的默认模板
				tempcode = map.get(billTypeId);

				// 若为交易类型且模板为空，则取单据类型默认模板
				if (StringUtil.isEmptyWithTrim(tempcode)
						&& !StringUtil.isEmptyWithTrim(parentTypeId)) {
					tempcode = map.get(parentTypeId);
				}

			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}

		return tempcode;
	}

	/**
	 * 审批人外出时，查询其动态代理人 <li>即在[审批流用户管理]节点配置的代理人
	 *
	 * @param checkman
	 *            审批人PK
	 * @param pkBilltype
	 *            单据类型
	 * @return 代理人PK + 代理人信息
	 * @throws DbException
	 */
	public static String[] queryDynamicAgentOfCheckman(String checkman,
			String pkBilltype) throws DbException {
		String[] retStr = new String[] { checkman, "" };
		/*
		WorkflowPersonDAO wpDao = new WorkflowPersonDAO();
		// 只有当前审批人外出了，才获取其动态代理人信息
		if (wpDao.isUserOut(checkman)) {
			// 获取动态代理人
			ArrayList dynAgentUsers = getDynamicAgents(checkman, pkBilltype);
			String agentUserId = wpDao.findFirstUserNotOut(dynAgentUsers,
					pkBilltype);
			if (!StringUtil.isEmptyWithTrim(agentUserId)) {
				retStr[0] = agentUserId;
				retStr[1] = "{agent}" + queryOperatorName(agentUserId);
			}
			// else
			// throw new
			// EngineException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
			// "UPPpfworkflow-000356")/* 后继活动的审批人不存在并且无代理人或代理人都不在，单据不能保存或审批 * /);
		}
	    */
		return retStr;
	}

//	/**
//	 * 获取代理人信息 <li>通过"审批流用户管理"设置的动态代理人优先于活动上定义的代理人
//	 *
//	 * @modify zhouzhenga
//	 *         当用户在某一审批流指定代理人时，而在其它审批流未指定代理人时，且当用户OUT时，提交未未指定代理人的审批流单据时
//	 *         ，流程任务要仍能发给该用户
//	 * @param context
//	 * @param task
//	 * @throws EngineException
//	 */
//	private static String getAgentInfo(String agents, TaskInstanceVO task) {
//		String agentInfo = "";
//		/***
//		 * 抛出异常标志位 1.查找静态代理人，如代理人出差，则抛异常； 2.获取动态代理人，无代理人或代理人出差则发给本人，不抛异常
//		 * */
//		boolean throwExpFlag = false;
//		try {
//			WorkflowPersonDAO wpDao = new WorkflowPersonDAO();
//			// 只有当前审批人外出了，才获取其代理人信息
//			if (wpDao.isUserOut(task.getPk_executer())) {
//
//				String agentUserId = null;
//				ArrayList<String> agentUsers = getAgents(agents);
//				// 1.设置了环节上代理人，且为空时不需要代理
//				// if (agentUsers != null && agentUsers.size() == 0)
//				// return agentInfo;
//
//				// 2.获得活动上定义的静态代理人
//				if (agentUsers != null && agentUsers.size() > 0) {
//					agentUserId = wpDao.findFirstUserNotOut(agentUsers,
//							task.getPk_bizobject());
//					throwExpFlag = true;
//				}
//				// 3.获取动态代理人
//				else {
//					ArrayList<String> dynAgentUsers = getDynamicAgents(
//							task.getPk_executer(), task.getPk_bizobject());
//					agentUserId = wpDao.findFirstUserNotOut(dynAgentUsers,
//							task.getPk_bizobject());
//					throwExpFlag = false;
//				}
//
//				if (agentUserId != null) {
//					// XXX::设置为代理人的PK
//					//task.setApproveAgent(task.getPk_executer()); // XXX:wcj这一句代码搞的所有的地方逻辑都乱七八糟，不知道下面的一句话为什么这样改？？？
//					//task.setParticipantID(agentUserId);
//					// agentInfo = "代理人" + queryOperatorName(agentUser);
//
//					// yk1+ 格式不再为{agent}+operatorName
//					// 而是直接改为operatorName
//					// 当在TaskTopicBusiVarCalculator中检测到agent字段不为空时
//					// 先取agent中user的多语名称，然后在前面加上{agent}
////					agentInfo = "{agent}" + queryOperatorName(agentUserId);
//
//					agentInfo = agentUserId;
//				} else {
//					if (!throwExpFlag)
//						return agentInfo;
//					else
//						throw new RuntimeException(NCLangResOnserver
//								.getInstance().getStrByID("pfworkflow",
//										"UPPpfworkflow-000356")/*
//																 * 工作项分配错误：
//																 * 后继活动的参与者处于出差状态却没有有效的代理人
//																 */);
//				}
//			}
//		} catch (DbException ex) {
//			Logger.error(ex.getMessage(), ex);
//			throw new RuntimeException(NCLangResOnserver.getInstance()
//					.getStrByID("pfworkflow", "UPPpfworkflow-000357")/*
//																	 * 处理出差代理时出现数据库异常
//																	 * ：
//																	 */
//					+ ex.getMessage());
//		}
//		return agentInfo;
//	}

	/**
	 * 获得某活动上定义的所有代理人
	 *
	 * @param activity
	 * @return 操作员Id数组
	 */
	private static ArrayList<String> getAgents(String value) {
		ArrayList<String> alRet = new ArrayList<String>();
		// 默认的代理人
		if (value == null)
			return null;
		else {
			// 流程环节上定制的代理人
			StringTokenizer st = new StringTokenizer(value.toString(), ";");
			while (st.hasMoreTokens())
				alRet.add(st.nextToken());

			return alRet;
		}
	}

//	/**
//	 * 返回通过"审批流用户管理"设置的动态代理人
//	 *
//	 * @return
//	 */
//	private static ArrayList getDynamicAgents(String userId, String billType)
//			throws DbException {
//		WorkflowPersonDAO wpDao = new WorkflowPersonDAO();
//		return wpDao.queryDynamicAgents(userId, billType);
//	}


	private static String getInformerId(TaskInstanceVO task) {
		// 以最后一个人的名义发通知
		String userId = task.getPk_executer();
		if (userId == null) {
			userId = task.getPk_creater();
		}

		return userId;
	}

	/**
	 * @deprecated should do i18n
	 * @param context
	 * @return
	 */
	private static String getInformer(TaskInstanceVO task) {
		String informer = "";
		String userId = getInformerId(task);

		if (userId != null) {
			String operatorName = queryOperatorName(userId);
			informer = operatorName;
		}

		return informer;
	}

	/**
	 * 根据用户ID，查询其用户名称
	 *
	 * @param userId
	 * @return
	 * @throws EngineException
	 */
	private static String queryOperatorName(String userId) {
		nc.vo.sm.UserVO userVo = null;
		try {
			// 查找操作人的姓名
			userVo = NCLocator.getInstance().lookup(IUserManageQuery.class)
					.getUser(userId);
		} catch (BusinessException ex) {
			Logger.error(ex.getMessage(), ex);
			throw new RuntimeException(NCLangResOnserver.getInstance()
					.getStrByID("pfworkflow", "UPPpfworkflow-000358" /*
																	 * @res
																	 * "查找操作人错误:"
																	 */)
					+ ex.getMessage());
		}

		String userName = NCLangResOnserver.getInstance().getStrByID(
				"pfworkflow", "UPPpfworkflow-000359" /*
													 * @res "无法识别的人"
													 */);
		if (userVo != null)
			userName = userVo.getUser_name();

		return userName;
	}

	public static String getResult(boolean isWorkflow, String actionCode, TaskInstanceVO task,
			String yesNoRejectResult) {
		// boolean isWorkflow = WorkflowTypeEnum.Workflow.getIntValue() ==
		// context.getWfProcessDef().getWorkflowType();

		String reason = "";
		// String strActionCode =
		// context.getCurrentTask().getWorknoteVO().getActiontype();

		int iActType = task.getCreate_type();

		if (task.getCreate_type() == TaskInstanceCreateType.Reject.getIntValue()) {
			reason = "{rejectBill}" /* "驳回单据," */;
		} else if (task.getCreate_type() == TaskInstanceCreateType.Reject.getIntValue()) {
			reason = isWorkflow ? "{rollbackBill}" /* "回退单据," */ : "{unapproveBill}" /* "弃审单据," */;
		} else if (iActType == TaskInstanceCreateType.Makebill.getIntValue() || IPFActionName.SAVE.equals(actionCode)
				|| IPFActionName.START.equals(actionCode)) {
			reason = isWorkflow ? "{startworkflow}" /* 启动流程 */: "{commitBill}" /* "提交单据," */;
		} else if (iActType == TaskInstanceCreateType.Normal.getIntValue()
				//|| iActType == TaskInstanceCreateType.Subflow.getIntValue()
				//|| iActType == TaskInstanceCreateType.Route.getIntValue()
				//|| iActType == TaskInstanceCreateType.Manual.getIntValue() || iActType == TaskInstanceCreateType.Auto.getIntValue()
				) {

			if (yesNoRejectResult.equals("Y")) {
				reason = isWorkflow ? "{dealfinish}" /* 处理完成 */
				: "{checkPass}" /* "审批通过," */;
			} else if (yesNoRejectResult.equals("N")) {
				reason = "{checkNoPass}" /* "审批不通过," */;
			}

			// if (context.getCurrentActivityInstance().getResult() != null) {
			// if (context.getCurrentActivityInstance().getResult().equals("Y"))
			// {
			// reason = isWorkflow ? "{dealfinish}" /* 处理完成 */
			// : "{checkPass}" /* "审批通过," */;
			// } else if
			// (context.getCurrentActivityInstance().getResult().equals("N")) {
			// reason = "{checkNoPass}" /* "审批不通过," */;
			// }
			// } else {
			// String resultStr = (String)
			// context.getDatas().get(IApproveflowConst.ACT_CHECK_RESULT);
			// if ("Y".equals(resultStr)) {
			// reason = isWorkflow ? "{dealfinish}" /* 处理完成 */
			// : "{checkPass}" /* "审批通过," */;
			// } else if ("N".equals(resultStr)) {
			// reason = "{checkNoPass}" /* "审批不通过," */;
			// }
			// }
		}
		return reason;
	}

	private static String getReason(TaskInstanceVO task) {

		String strActionCode = task.getActiontype();

		//Activity activity = context.getCurrentActivity();

		String yesNoRejectResult = null;

		if (task.getOpinion() != null) {
			yesNoRejectResult = task.getOpinion();
		} else {
			yesNoRejectResult = null;//(String) context.getDatas().get(IApproveflowConst.ACT_CHECK_RESULT);
		}

		return getResult(true, strActionCode, task, yesNoRejectResult);
	}

	private static String getLangCodeOfUser(String cuserid)
			throws BusinessException {

		String sql = "select langcode from pub_multilang l join sm_user u"
				+ " on u.contentlang=l.pk_multilang where u.cuserid='"
				+ cuserid + "'";
		IUAPQueryBS qry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List list = (List) qry.executeQuery(sql, new ColumnListProcessor(
				"langcode"));

		if (list.size() > 0) {
			return String.valueOf(list.get(0));
		} else {
			return DataMultiLangAccessor.getInstance().getDefaultLang()
					.getLangcode();
		}
	}

}