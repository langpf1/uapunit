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
 * ������������ı�����Ϣ
 *
 * @author wzhy 2005-1-2
 * @modifier leijun 2005-3-30 ���ﻯi18n
 * @modifier leijun 2006-3-29 ʹ�ö��Ƶ���������乤����ı���
 * @modifier leijun 2006-11-20 ����UFDouble����ֵ������Ĭ��Ϊ2
 * @modifier leijun 2007-11-29 ����ȡ�����ȡ���ƵĹ���
 * @modifier leijun 2008-9 ��Թ������Ĺ����������޸�
 */
public class TaskTopicResolver {
	private TaskTopicResolver() {
		super();
	}

//	/**
//	 * ���ø����������������Ϣ
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
//		// �����Ǩ�Ƶ��Ƶ��ڵ㡢�����ڵ��������������,��Ҫ���֪ͨ��Ϣ��
//		// �������������̵��²��������topic�������̾���
////		if (actType == ActivityTypeEnum.Checkbill.getIntValue()) {
////			// ��������
////			String actionType = "{checkBill}";
////			constructTopic(context, tasks, actionType);
////		} else if (actType == ActivityTypeEnum.Manual.getIntValue()) {
////			// ������
////			String actionType = "{dealBill}";
////			constructTopic(context, tasks, actionType);
////		} else if (actType == ActivityTypeEnum.Subflow.getIntValue()) {
////			// ������
////			String actionType = isWorkflow ? "{dealBill}" : "{checkBill}";
////			constructTopic(context, tasks, actionType);
////		} else if (actType == ActivityTypeEnum.Makebill.getIntValue()) {
////			// �޸ĵ���
////			String actionType = "{modifyBill}";
////			constructTopic(context, tasks, actionType);
////		}
//	}

	public static String getActionType(int actType, boolean isWorkflow) {
		if (actType == TaskInstanceCreateType.Normal.getIntValue()) {
			// ��������
			return "{checkBill}";
		} else if (actType == TaskInstanceCreateType.Normal.getIntValue()) {
			// ������
			return "{dealBill}";
		} else if (actType == TaskInstanceCreateType.AfterAddSign.getIntValue()) {
			// ������
			return isWorkflow ? "{dealBill}" : "{checkBill}";
		} else if (actType == TaskInstanceCreateType.Reject.getIntValue()) {
			// �޸ĵ���
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
//		// ����������ύ��ͨ������ͨ�������ء�����
//		String approveResult = getReason(task);
//		// ���������ƣ�
//		String senderId = getInformerId(task);
//
//		String agentInfo = "";
//
//		WorkitemMsgContext msgContext = new WorkitemMsgContext();
//
////		if (task.getParticipantProcessMode() == WFTask.ProcessMode_Single_Together
////				|| task.getParticipantProcessMode() == WFTask.ProcessMode_Single_Race) {
//		
//			// ����ֻ�в�����Ϊ����Ա�Ļ�Ż�ȡ��������Ϣ
//			
//			agentInfo = getAgentInfo(null, task);//TODO:�������������ϵĴ�����
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
			// yanke+ 2011-8-25 ���������֧��
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
			Logger.debug("ʹ��MsgContentCreator������������Ϣ����������Ĭ�ϸ�ʽ������Ϣ");

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
	 * ȡ�õ�ǰ��¶������Ϣģ��code
	 *
	 * @param context
	 * @param task
	 * @return ��Ϣģ��code����δ�����򷵻�null
	 */
	public static String getMsgTempCode(TaskInstanceVO task) {
		String tempcode = null;

		// ȡ��ǰ��������Ϣģ��pk
		tempcode = task.getTitle();

		// �����̶�����δΪ�û������Ϣģ�壬��ȡ�õ������͵�Ĭ����Ϣģ��
		if (StringUtil.isEmptyWithTrim(tempcode)) {
			tempcode = getDefaultTempcodeOfBillType(task.getPk_bizobject());
		}
		return tempcode;
	}

	public static String getDefaultTempcodeOfBillType(String billType) {

		String tempcode = null;

		BilltypeVO bvo = PfDataCache.getBillType(billType);
		String billTypeId = bvo.getPk_billtypeid();

		// ��Ϊ�������ͣ���ȡ��������
		String parentType = bvo.getParentbilltype();
		BilltypeVO pbvo = PfDataCache.getBillType(parentType);
		String parentTypeId = pbvo == null ? null : pbvo.getPk_billtypeid();

		String sqlCond = null;

		if (StringUtil.isEmptyWithTrim(parentTypeId)) {

			// ��Ϊ�������ͣ�����Ҹõ����������õ�Ĭ��ģ��
			sqlCond = "pk_billtype='" + billTypeId + "'";
		} else {

			// ��Ϊ�������ͣ�����ҽ������ͼ��䵥���������õ�Ĭ��ģ��
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

				// �����н���ŵ���ϣ����
				// ����а�����������Ĭ����Ϣģ��͵�������Ĭ����Ϣģ��
				while (it.hasNext()) {
					WorkitemconfigVO wicVO = it.next();
					map.put(wicVO.getPk_billtype(), wicVO.getItem());
				}

				// ȡ��������/�������͵�Ĭ��ģ��
				tempcode = map.get(billTypeId);

				// ��Ϊ����������ģ��Ϊ�գ���ȡ��������Ĭ��ģ��
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
	 * ���������ʱ����ѯ�䶯̬������ <li>����[�������û�����]�ڵ����õĴ�����
	 *
	 * @param checkman
	 *            ������PK
	 * @param pkBilltype
	 *            ��������
	 * @return ������PK + ��������Ϣ
	 * @throws DbException
	 */
	public static String[] queryDynamicAgentOfCheckman(String checkman,
			String pkBilltype) throws DbException {
		String[] retStr = new String[] { checkman, "" };
		/*
		WorkflowPersonDAO wpDao = new WorkflowPersonDAO();
		// ֻ�е�ǰ����������ˣ��Ż�ȡ�䶯̬��������Ϣ
		if (wpDao.isUserOut(checkman)) {
			// ��ȡ��̬������
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
			// "UPPpfworkflow-000356")/* ��̻�������˲����ڲ����޴����˻�����˶����ڣ����ݲ��ܱ�������� * /);
		}
	    */
		return retStr;
	}

//	/**
//	 * ��ȡ��������Ϣ <li>ͨ��"�������û�����"���õĶ�̬�����������ڻ�϶���Ĵ�����
//	 *
//	 * @modify zhouzhenga
//	 *         ���û���ĳһ������ָ��������ʱ����������������δָ��������ʱ���ҵ��û�OUTʱ���ύδδָ�������˵�����������ʱ
//	 *         ����������Ҫ���ܷ������û�
//	 * @param context
//	 * @param task
//	 * @throws EngineException
//	 */
//	private static String getAgentInfo(String agents, TaskInstanceVO task) {
//		String agentInfo = "";
//		/***
//		 * �׳��쳣��־λ 1.���Ҿ�̬�����ˣ�������˳�������쳣�� 2.��ȡ��̬�����ˣ��޴����˻�����˳����򷢸����ˣ������쳣
//		 * */
//		boolean throwExpFlag = false;
//		try {
//			WorkflowPersonDAO wpDao = new WorkflowPersonDAO();
//			// ֻ�е�ǰ����������ˣ��Ż�ȡ���������Ϣ
//			if (wpDao.isUserOut(task.getPk_executer())) {
//
//				String agentUserId = null;
//				ArrayList<String> agentUsers = getAgents(agents);
//				// 1.�����˻����ϴ����ˣ���Ϊ��ʱ����Ҫ����
//				// if (agentUsers != null && agentUsers.size() == 0)
//				// return agentInfo;
//
//				// 2.��û�϶���ľ�̬������
//				if (agentUsers != null && agentUsers.size() > 0) {
//					agentUserId = wpDao.findFirstUserNotOut(agentUsers,
//							task.getPk_bizobject());
//					throwExpFlag = true;
//				}
//				// 3.��ȡ��̬������
//				else {
//					ArrayList<String> dynAgentUsers = getDynamicAgents(
//							task.getPk_executer(), task.getPk_bizobject());
//					agentUserId = wpDao.findFirstUserNotOut(dynAgentUsers,
//							task.getPk_bizobject());
//					throwExpFlag = false;
//				}
//
//				if (agentUserId != null) {
//					// XXX::����Ϊ�����˵�PK
//					//task.setApproveAgent(task.getPk_executer()); // XXX:wcj��һ����������еĵط��߼������߰��㣬��֪�������һ�仰Ϊʲô�����ģ�����
//					//task.setParticipantID(agentUserId);
//					// agentInfo = "������" + queryOperatorName(agentUser);
//
//					// yk1+ ��ʽ����Ϊ{agent}+operatorName
//					// ����ֱ�Ӹ�ΪoperatorName
//					// ����TaskTopicBusiVarCalculator�м�⵽agent�ֶβ�Ϊ��ʱ
//					// ��ȡagent��user�Ķ������ƣ�Ȼ����ǰ�����{agent}
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
//																 * ������������
//																 * ��̻�Ĳ����ߴ��ڳ���״̬ȴû����Ч�Ĵ�����
//																 */);
//				}
//			}
//		} catch (DbException ex) {
//			Logger.error(ex.getMessage(), ex);
//			throw new RuntimeException(NCLangResOnserver.getInstance()
//					.getStrByID("pfworkflow", "UPPpfworkflow-000357")/*
//																	 * ����������ʱ�������ݿ��쳣
//																	 * ��
//																	 */
//					+ ex.getMessage());
//		}
//		return agentInfo;
//	}

	/**
	 * ���ĳ��϶�������д�����
	 *
	 * @param activity
	 * @return ����ԱId����
	 */
	private static ArrayList<String> getAgents(String value) {
		ArrayList<String> alRet = new ArrayList<String>();
		// Ĭ�ϵĴ�����
		if (value == null)
			return null;
		else {
			// ���̻����϶��ƵĴ�����
			StringTokenizer st = new StringTokenizer(value.toString(), ";");
			while (st.hasMoreTokens())
				alRet.add(st.nextToken());

			return alRet;
		}
	}

//	/**
//	 * ����ͨ��"�������û�����"���õĶ�̬������
//	 *
//	 * @return
//	 */
//	private static ArrayList getDynamicAgents(String userId, String billType)
//			throws DbException {
//		WorkflowPersonDAO wpDao = new WorkflowPersonDAO();
//		return wpDao.queryDynamicAgents(userId, billType);
//	}


	private static String getInformerId(TaskInstanceVO task) {
		// �����һ���˵����巢֪ͨ
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
	 * �����û�ID����ѯ���û�����
	 *
	 * @param userId
	 * @return
	 * @throws EngineException
	 */
	private static String queryOperatorName(String userId) {
		nc.vo.sm.UserVO userVo = null;
		try {
			// ���Ҳ����˵�����
			userVo = NCLocator.getInstance().lookup(IUserManageQuery.class)
					.getUser(userId);
		} catch (BusinessException ex) {
			Logger.error(ex.getMessage(), ex);
			throw new RuntimeException(NCLangResOnserver.getInstance()
					.getStrByID("pfworkflow", "UPPpfworkflow-000358" /*
																	 * @res
																	 * "���Ҳ����˴���:"
																	 */)
					+ ex.getMessage());
		}

		String userName = NCLangResOnserver.getInstance().getStrByID(
				"pfworkflow", "UPPpfworkflow-000359" /*
													 * @res "�޷�ʶ�����"
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
			reason = "{rejectBill}" /* "���ص���," */;
		} else if (task.getCreate_type() == TaskInstanceCreateType.Reject.getIntValue()) {
			reason = isWorkflow ? "{rollbackBill}" /* "���˵���," */ : "{unapproveBill}" /* "���󵥾�," */;
		} else if (iActType == TaskInstanceCreateType.Makebill.getIntValue() || IPFActionName.SAVE.equals(actionCode)
				|| IPFActionName.START.equals(actionCode)) {
			reason = isWorkflow ? "{startworkflow}" /* �������� */: "{commitBill}" /* "�ύ����," */;
		} else if (iActType == TaskInstanceCreateType.Normal.getIntValue()
				//|| iActType == TaskInstanceCreateType.Subflow.getIntValue()
				//|| iActType == TaskInstanceCreateType.Route.getIntValue()
				//|| iActType == TaskInstanceCreateType.Manual.getIntValue() || iActType == TaskInstanceCreateType.Auto.getIntValue()
				) {

			if (yesNoRejectResult.equals("Y")) {
				reason = isWorkflow ? "{dealfinish}" /* ������� */
				: "{checkPass}" /* "����ͨ��," */;
			} else if (yesNoRejectResult.equals("N")) {
				reason = "{checkNoPass}" /* "������ͨ��," */;
			}

			// if (context.getCurrentActivityInstance().getResult() != null) {
			// if (context.getCurrentActivityInstance().getResult().equals("Y"))
			// {
			// reason = isWorkflow ? "{dealfinish}" /* ������� */
			// : "{checkPass}" /* "����ͨ��," */;
			// } else if
			// (context.getCurrentActivityInstance().getResult().equals("N")) {
			// reason = "{checkNoPass}" /* "������ͨ��," */;
			// }
			// } else {
			// String resultStr = (String)
			// context.getDatas().get(IApproveflowConst.ACT_CHECK_RESULT);
			// if ("Y".equals(resultStr)) {
			// reason = isWorkflow ? "{dealfinish}" /* ������� */
			// : "{checkPass}" /* "����ͨ��," */;
			// } else if ("N".equals(resultStr)) {
			// reason = "{checkNoPass}" /* "������ͨ��," */;
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