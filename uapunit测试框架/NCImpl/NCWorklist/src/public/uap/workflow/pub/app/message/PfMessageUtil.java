package uap.workflow.pub.app.message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import uap.workflow.app.message.TaskTopicResolver;
import uap.workflow.engine.itf.ITaskInstanceQry;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.pub.app.message.vo.MessageTypes;
import uap.workflow.pub.app.message.vo.MessageinfoVO;
import uap.workflow.vo.WorkflownoteVO;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.ml.DataMultiLangAccessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.message.itf.IMessageQueryService;
import nc.message.itf.IMessageService;
import nc.message.msgcenter.event.IStateChangeProcessor;
import nc.message.util.IDefaultMsgConst;
import nc.message.util.MessageCenter;
import nc.message.vo.AttachmentVO;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.Language;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;

public class PfMessageUtil {
	/** 消息中心提供的默认发送人 */
	public final static String DEFAULT_SENDER = "NC_USER0000000000000";
	private static IStateChangeProcessor processor = null;
	private static NCMessage message = null;

	// yanke1 2012-5-17 双击消息中心和审批Action可能在不同前台线程中执行
	// 因此ThreadLocal也不靠谱，改回static变量方式
	/** introduce this local var to avoid potential multi-threading problem */
	// obj[0] = IStateChangeProcessor
	// obj[1] = NCMessage
	// private static ThreadLocal<Object[]> local = new ThreadLocal<Object[]>();

	private static IStateChangeProcessor getProcessor() {
		return processor;
	}

	private static NCMessage getCurrMsg() {
		return message;
	}

	private static IMessageQueryService lookupMessageQueryService() {
		return NCLocator.getInstance().lookup(IMessageQueryService.class);
	}

	private static IMessageService lookupMessageService() {
		return NCLocator.getInstance().lookup(IMessageService.class);
	}

	public static void sendMessageOfWorknoteBatch(TaskInstanceVO[] taskInstanceVOs) throws BusinessException {
		if (taskInstanceVOs == null || taskInstanceVOs.length == 0)
			return;

		for (int i = 0; i < taskInstanceVOs.length; i++) {
			NCMessage ncMsg = transferToNCMessage(taskInstanceVOs[i]);

			try {
				// yanke1 2011-8-25
				// 对于每一个NCMessage，MessageCenter将其发送后，会在收件箱、发件箱中各保存一份或多份
				// 因此返回的msgpks为所有这些消息的pk，即发送一个ncmsg会返回多个pk
				// 这些消息都应当跟array[i]中workflownoteVO的附件进行关联
				// 因此此处对应Batch发送的情况，依然要单独发送每个NCMessage
				// 若不单独发送而是批量发送，那么返回的pks将无法辨认哪些pk对应哪个ncmessage，也就无法为其关联附件
				String[] msgpks = MessageCenter.sendMessage(new NCMessage[] { ncMsg });
				assembleAttachment(taskInstanceVOs[i], msgpks);

			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				throw new BusinessException(e);
			}
		}
	}

	private static NCMessage transferToNCMessage(TaskInstanceVO taskInstanceVO) throws BusinessException {
		String messageReceiver = taskInstanceVO.getPk_agenter();
		if(StringUtil.isEmptyWithTrim(messageReceiver))
		{
			messageReceiver = taskInstanceVO.getPk_owner();
		}
		String topic = getMessageTopic(taskInstanceVO, messageReceiver);
		
		NCMessage ncMessage = null;//taskInstanceVO.getNcMsg();
		MessageVO messageVO = null;
		if (ncMessage == null) {
			ncMessage = new NCMessage();
			messageVO = new MessageVO();
		} else {
			messageVO = ncMessage.getMessage();

			if (messageVO == null)
				messageVO = new MessageVO();
		}

		messageVO.setMsgsourcetype(IDefaultMsgConst.WORKLIST);
		messageVO.setReceiver(messageReceiver);

		String subject = getI18NMessageNoteForUser(topic, messageReceiver);

		messageVO.setSubject(subject);

		String content = messageVO.getContent();
		if (!StringUtil.isEmptyWithTrim(content)) {

			content = getI18NMessageNoteForUser(content, messageReceiver);
			messageVO.setContent(content);
		}

		messageVO.setSender(taskInstanceVO.getPk_creater());
		/** 接收删除标记 */
		messageVO.setIsdelete(UFBoolean.FALSE);//taskInstanceVO没有对应字段，先写默认值UFBoolean.FALSE
		messageVO.setIshandled(UFBoolean.FALSE);
		messageVO.setPk_group(taskInstanceVO.getPk_group());
		messageVO.setSendtime(taskInstanceVO.getBegindate());
		messageVO.setSendstate(new UFBoolean(true));
		messageVO.setPriority(0);//taskInstanceVO没有对应字段，先写默认值0
		messageVO.setTs(taskInstanceVO.getTs());
		messageVO.setDr(taskInstanceVO.getDr());
		messageVO.setPk_org(taskInstanceVO.getPk_org());
		messageVO.setDetail(taskInstanceVO.getPk_form_ins_version() + "@" + taskInstanceVO.getPk_bizobject() 
				+ "@" + taskInstanceVO.getForm_no() + "@" + taskInstanceVO.getPk_task());
		messageVO.setPk_detail(taskInstanceVO.getPk_task());

		ncMessage.setMessage(messageVO);
		ncMessage.getMessage().setPk_detail(taskInstanceVO.getPk_task());
		// +附件
		// ncMessage.setAttachmentSetting(workflownote.getAttachmentSetting());
		ncMessage.getMessage().setContenttype(taskInstanceVO.getActiontype());

		return ncMessage;

	}

	private static NCMessage transferToNCMessage(MessageinfoVO msginfovo)
			throws BusinessException {
		NCMessage ncMessage = new NCMessage();
		MessageVO messageVO = new MessageVO();
		if (msginfovo.getType() == MessageTypes.MSG_TYPE_INFO) {
			messageVO.setMsgsourcetype(IDefaultMsgConst.NOTICE);
		} else {
			messageVO.setMsgsourcetype(IDefaultMsgConst.WORKLIST);
		}
		messageVO.setReceiver(msginfovo.getCheckman());
		String subject = getI18NMessageNoteForUser(msginfovo.getTitle(),
				msginfovo.getCheckman());
		messageVO.setSubject(subject);
		messageVO.setSender(msginfovo.getSenderman());
		/** 接收删除标记 */
		messageVO.setIsdelete(msginfovo.getReceivedeleteflag());
		messageVO.setIshandled(UFBoolean.FALSE);
		messageVO.setSendtime(msginfovo.getSenddate());
		messageVO.setSendstate(new UFBoolean(true));
		messageVO.setPriority(msginfovo.getPriority());
		messageVO.setTs(msginfovo.getTs());
		messageVO.setDr(msginfovo.getDr());
		messageVO.setContent(msginfovo.getContent());
		messageVO.setPk_group(msginfovo.getPk_corp());
		messageVO.setPk_detail(msginfovo.getPk_wf_msg() + "@"
				+ msginfovo.getPk_wf_task() + "@" + msginfovo.getType() + "@"
				+ msginfovo.getType() + "@");
		messageVO.setDetail(msginfovo.getBillid() + "@"	+ msginfovo.getPk_billtype() 
				+ "@" + msginfovo.getBillno() + "@" + msginfovo.getPk_wf_task());
		messageVO.setContenttype(WorkflownoteVO.WORKITEM_TYPE_BIZ);
		messageVO.setDomainflag(getModuleOfBilltype(msginfovo.getPk_billtype()));

		ncMessage.setMessage(messageVO);
		return ncMessage;
	}
	
	public static String getModuleOfBilltype(String pk_billtypecode) {
		try {
			BilltypeVO btvo = PfDataCache.getBillTypeInfo(pk_billtypecode);
			if (btvo != null) {
				return btvo.getSystemcode();
			} else {
				return null;
			}
		} catch (Exception e) {
			// 取不到不要抛异常影响流程
			Logger.error(e.getMessage(), e);
			return null;
		}
	}	
	public static void insertBizMessages(MessageinfoVO[] array)
			throws BusinessException {
		// TODO Auto-generated method stub
		if (array == null || array.length == 0)
			return;

		NCMessage[] ncMsgs = new NCMessage[array.length];
		for (int i = 0; i < array.length; i++) {
			ncMsgs[i] = transferToNCMessage(array[i]);
		}
		try {
			MessageCenter.sendMessage(ncMsgs);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e);
		}
	}

	//获取消息的主题
	public static String getMessageTopic(TaskInstanceVO taskInstanceVO, String messageReceiver) {
		// 消息的主题
		String topic;
		WorkitemMsgContext workitemMsgContext = BuilderWorkitemMsgContext(taskInstanceVO, messageReceiver);
		NCMessage ncmsg = TaskTopicResolver.constructNCMsg(workitemMsgContext);
		// 为task和noteVO设置标题
		topic = ncmsg.getMessage().getSubject();
		return topic;
	}

	private static WorkitemMsgContext BuilderWorkitemMsgContext(TaskInstanceVO taskInstanceVO, String messageReceiver) {
		WorkitemMsgContext context = new WorkitemMsgContext();
		
//		int actType = act.getActivityType();
//		boolean isWorkflow = WorkflowTypeEnum.Workflow.getIntValue() == task.getWorkflowType();
//		String actionType = TaskTopicResolver.getActionType(actType, isWorkflow);
//		String result = TaskTopicResolver.getResult(isWorkflow, paraVO.m_workFlow.getActiontype(), act, task, task.getApproveResult()); 
		
		context.setActionType(taskInstanceVO.getActiontype());//actionType);
		context.setAgent(taskInstanceVO.getPk_agenter());
		context.setBillid(taskInstanceVO.getPk_form_ins_version());
		context.setBillno(taskInstanceVO.getForm_no());
		context.setBillType(taskInstanceVO.getPk_bizobject());
		//context.setBusiObj(paraVO.m_preValueVo);
		context.setCheckman(messageReceiver);
		//context.setCheckNote(paraVO.m_workFlow.getChecknote());
		context.setSender(taskInstanceVO.getPk_creater());
		//context.setResult(result);
		return context;
	}

	private static String getI18NMessageNoteForUser(String note, String cuserid) throws BusinessException {
		String langcode = getLangcodeOfUser(cuserid);

		if (StringUtil.isEmptyWithTrim(langcode))
			langcode = Language.SIMPLE_CHINESE_CODE;

		String originLang = InvocationInfoProxy.getInstance().getLangCode();

		InvocationInfoProxy.getInstance().setLangCode(langcode);
		String i18nNote = uap.workflow.pub.app.message.vo.MessageVO.getMessageNoteAfterI18N(note);
		InvocationInfoProxy.getInstance().setLangCode(originLang);

		return i18nNote;
	}

	public static uap.workflow.pub.app.message.vo.MessageVO transferNCMessageToMessageVO(NCMessage ncmsg) {
		if (ncmsg == null)
			return null;
		uap.workflow.pub.app.message.vo.MessageVO msgVO = new uap.workflow.pub.app.message.vo.MessageVO();
		msgVO.setActionTypeCode(ncmsg.getMessage().getContenttype());
		msgVO.setCheckerCode(ncmsg.getMessage().getReceiver());
		msgVO.setPrimaryKey(ncmsg.getMessage().getPrimaryKey());
		// 业务组要求此处传入的是组织pk
		msgVO.setCorpPK(ncmsg.getMessage().getPk_org());
		String detail = ncmsg.getMessage().getDetail();
		String[] values = detail.split("@");
		msgVO.setBillPK(values[0]);
		msgVO.setPk_billtype(values[1]);
		msgVO.setBillNO(values[2]);

		// 查表得到业务组需要的WorkflownoteVO中的userobject
		// 签收消息或者工作项打开单据UI时需要
		ITaskInstanceQry taskQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(ITaskInstanceQry.class);
		TaskInstanceVO taskInsVo = taskQry.getTaskInsVoByPk(ncmsg.getMessage().getPk_detail());
		if (taskInsVo != null) {
			msgVO.setUserobject(taskInsVo.getUserobject());
		}
		msgVO.setMessageNote(ncmsg.getMessage().getContent());
		return msgVO;
	}

	private static String getLangcodeOfUser(String cuserid) throws BusinessException {
		String sql = "select langcode from pub_multilang l join sm_user u"
				+ " on u.contentlang=l.pk_multilang where u.cuserid='" + cuserid + "'";
		IUAPQueryBS qry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List list = (List) qry.executeQuery(sql, new ColumnListProcessor("langcode"));

		if (list.size() > 0) {
			return String.valueOf(list.get(0));
		} else {
			return DataMultiLangAccessor.getInstance().getDefaultLang().getLangcode();
		}
	}

	public static void sendMessageOfWorknote(TaskInstanceVO taskInstanceVO) throws BusinessException {
		if (taskInstanceVO == null)
			return;

		NCMessage ncMsg = transferToNCMessage(taskInstanceVO);
		try {
			String[] msgpk = MessageCenter.sendMessage(new NCMessage[] { ncMsg });

			// yanke1+ 2011-8-25 见assembleAttachment方法注解
			// assembleAttachment(new WorkflownoteVO[] { workflownoteVO },
			// msgpk);
			assembleAttachment(taskInstanceVO, msgpk);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e);
		}

	}

	private static void assembleAttachment(TaskInstanceVO taskInstanceVO, String[] msgpks) throws BusinessException {

		List<AttachmentVO> resultAtts = new ArrayList<AttachmentVO>();
		List<AttachmentVO> originAtts = null;//taskInstanceVO.getAttachmentSetting();

		if (originAtts == null || originAtts.size() == 0)
			return;

		for (AttachmentVO attVO : originAtts) {
			for (String msgpk : msgpks) {
				AttachmentVO tempVO = (AttachmentVO) attVO.clone();

				tempVO.setPk_attachment(null);
				tempVO.setPk_message(msgpk);

				resultAtts.add(tempVO);
			}
		}

		BaseDAO dao = new BaseDAO();
		dao.insertVOList(resultAtts);
	}

	/**
	 * 组装成完整的附件VO，填充并且往表sm_msg_attachment中插入条目
	 * 
	 * @deprecated MessageCenter.sendMessage(NCMessage[] msgs)方法的返回值String[]
	 *             <p>
	 *             返回的pks数目与传入的NCMessage[]数目并不对应
	 *             <p>
	 *             sendMessage在发送ncmessage时，不光会在inbox中插入消息，还会在outbox中插入消息
	 *             <p>
	 *             因此一个ncmessage可能最终对应多个pks
	 *             <p>
	 *             因此此处WorkflownoteVO[]的数目与String[] msgpks的数目并不对应，无法有效为消息关联附件
	 *             <p>
	 *             因此此方法废弃
	 *             <p>
	 *             请使用assembleAttachment(WorkflownoteVO note, String[] msgpks)
	 * 
	 */
	private static void assembleAttachment(WorkflownoteVO[] array, String[] msgpks) throws BusinessException {
		ArrayList<AttachmentVO> attachs = new ArrayList<AttachmentVO>();
		for (int i = 0; i < array.length; i++) {
			List<AttachmentVO> attchvos = array[i].getAttachmentSetting();
			if (attchvos != null) {
				for (AttachmentVO vo : attchvos) {
					vo.setPk_message(msgpks[i]);
				}
				attachs.addAll(attchvos);
			}
		}
		BaseDAO dao = new BaseDAO();
		dao.insertVOList(attachs);
	}

	public static void deleteMessagesOfWorknote(WorkflownoteVO[] aryWorknote) throws BusinessException {
		if (aryWorknote == null || aryWorknote.length == 0)
			return;

		Logger.info("deleteMessagesOfWorknote aryWorknote:" + aryWorknote);

		// yanke1 2011-9-27 因oracle的in语句中条目不能超过1000个 因此，此处对sql的拼接采用特殊处理
		int UNIT = 999;

		int length = aryWorknote.length;
		StringBuffer sb = new StringBuffer();

		sb.append("pk_detail in (");

		for (int i = 0; i <= length / UNIT; i++) {

			if (i > 0) {
				sb.append(" or pk_detail in (");
			}

			int jRange = i * UNIT + UNIT;
			jRange = jRange > length ? length : jRange;

			for (int j = i * UNIT; j < jRange; j++) {
				if (j % UNIT > 0) {
					sb.append(", ");
				}

				sb.append("'");
				sb.append(aryWorknote[j].getPk_checkflow());
				sb.append("'");
			}

			sb.append(")");
		}

		Logger.info("deleteMessagesOfWorknote inSql:" + sb.toString());
		NCMessage[] ncMsgs = lookupMessageQueryService().queryNCMessages(sb.toString());
		lookupMessageService().deleteMessage(ncMsgs);
	}

	public static void deleteMessagesOfWorknoteSubQuery(String sqlSubQuery) throws BusinessException {
		if (sqlSubQuery == null || StringUtil.isEmptyWithTrim(sqlSubQuery))
			return;

		List<NCMessage> msgList = new ArrayList<NCMessage>();

		Logger.info("deleteMessagesOfWorknote sqlSubQuery:" + sqlSubQuery);
		NCMessage[] ncMsgs = lookupMessageQueryService().queryNCMessages(
				"sm_msg_content.pk_detail in (" + sqlSubQuery + ")");

		lookupMessageService().deleteMessage(ncMsgs);
	}

	/**
	 * 删除拉单消息
	 * 
	 * @throws BusinessException
	 * */
	public static void deletePullMessage(String billid) throws BusinessException {
		String delSql = " detail like '%@" + MessageTypes.MSG_TYPE_BUSIFLOW_PULL + "@%' and pk_detail like '"
				+ billid + "@%'";
		NCMessage[] ncMsgs = lookupMessageQueryService().queryNCMessages(delSql);
		lookupMessageService().deleteMessage(ncMsgs);
	}

	/**
	 * 后台调用，改变消息的处理状态
	 * 
	 * @param worknote
	 * @throws BusinessException
	 */
	public static void setHandled(WorkflownoteVO worknote) throws BusinessException {
		NCMessage[] ncMsgs = lookupMessageQueryService().queryNCMessages(
				"pk_detail='" + worknote.pk_checkflow + "'");
		if (ncMsgs == null || ncMsgs.length == 0)
			return;

		for (NCMessage message : ncMsgs) {
			message.getMessage().setIshandled(UFBoolean.TRUE);
		}

		lookupMessageService().udpateMessage(ncMsgs);
	}

	/**
	 * jvm中唯一，只允许前台调用！
	 * 
	 * 用于前台代码处理消息(PfUtilClient.runAction)后，将消息中心中的消息设为已处理
	 * 
	 * @param worknote
	 * @throws BusinessException
	 */
	public static void setHandledWithProcessor(WorkflownoteVO worknote) throws BusinessException {
		IStateChangeProcessor processor = getProcessor();
		NCMessage currMsg = getCurrMsg();
		if (processor == null || currMsg == null) {
			return;
		} else {
			currMsg.getMessage().setIshandled(UFBoolean.TRUE);
			processor.processStateChange(currMsg);

			processor = null;
			message = null;
		}
	}

	/**
	 * jvm中唯一，只允许前台调用！
	 * 
	 * 用于消息中心中处理消息之前把消息set进来
	 * 
	 * @param msg
	 * @param stateprocessor
	 */
	public static void setSateProcessor(NCMessage msg, IStateChangeProcessor stateprocessor) {
		message = msg;
		processor = stateprocessor;
	}

}
