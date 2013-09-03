package uap.workflow.pub.app.message.vo;

import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;


/**
 * 消息包装VO类
 * 
 * @author 陈新宇 2001-6-22
 * @modifier 雷军 2005-3-30 多语化i18n
 * @modifier guowl 2008-4-2 增加是否封存标志
 * @modifier guowl 2008-4-14 增加filecontent属性，用于表示附件内容
 * @modifier leijun 2008-10 增加m_titleColor属性
 */
public class MessageVO extends ValueObject {
	/** 非业务消息标识 */
	public final static String NOT_BUSINESS_MSG = "XX";

	/** 消息状态类型 */
	public final static int MESSAGE_NOT_DEALED = 0;

	public final static int MESSAGE_DEALED = 1;

	public final static int MESSAGE_ALL = 2;

	/**
	 * 消息所在空间：待办
	 * <li>XXX:6.0合并为一栏 
	 */
	//public final static int SPACE_IN_BULLETIN = -1; // 公告栏
	//public final static int SPACE_IN_PREALERT = 1;// 预警消息
	public final static int SPACE_IN_WORKLIST = 0;// 待办事务

	/** 消息主键 */
	private String primaryKey = null;

	/** 单据类型编码 */
	private String pk_billtype = null;

	/** 源单据类型编码 */
	private String pk_srcbilltype = null;

	/** 单据号 */
	private String billNO = null;

	/** 单据主键 */
	private String billPK = null;

	/** 公司主键 */
	private String corpPK = null;

	/** 动作类型编码，消息扩展代码 */
	private String actionTypeCode = null;

	/** 发送人编码,即发送人PK */
	private String senderCode = null;

	/** 发送人名称 */
	private String senderName = null;

	/** 处理人编码,即处理人PK */
	private String checkerCode = null;

	/** 处理人名称 */
	private String checkerName = null;

	/** 是否已处理 */
	private UFBoolean isCheck = null;

	/** 处理意见 */
	private String checkNote = null;

	/** 发送时间 */
	private UFDateTime sendDateTime = null;

	/** 处理时间 */
	private UFDateTime dealDateTime = null;

	/** 消息内容 */
	private String messageNote = null;

	/** 标题 */
	private String m_title = null;

	/** 标题颜色 */
	private String m_titleColor = null;

	/** 报警消息的html页面在服务器的地址 */
	private String m_mailAddress = null;

	// 消息类型 huangzg++
	private int msgType = MessageTypes.MSG_TYPE_APPROVE;

	private int priority = MessagePriority.PRI_NORMAL;

	/** 该消息的打开方式是否强制为对话框 */
	private boolean isForceDialogOpen = false;

	/** 附件内容*/
	private byte[] filecontent = null;

	/** 附件是否被修改*/
	private boolean isAttachChanged = false;

	/** 是否已封存*/
	private UFBoolean isSealed = UFBoolean.FALSE;

	/** 用户对象 */
	private String userobject;

	/** 待办工作项所属的工作流类型：1=审批流 3=工作流，见<code>IApproveflowConst</code> */
	private int iWorkflowtype;

	public MessageVO() {
		super();
	}

	public String getTitleColor() {
		return m_titleColor;
	}

	public void setTitleColor(String color) {
		m_titleColor = color;
	}

	public int getWorkflowtype() {
		return iWorkflowtype;
	}

	public void setWorkflowtype(int workflowtype) {
		iWorkflowtype = workflowtype;
	}

	public String getUserobject() {
		return userobject;
	}

	public void setUserobject(String userobject) {
		this.userobject = userobject;
	}

	public boolean isForceDialogOpen() {
		return isForceDialogOpen;
	}

	public void setForceDialogOpen(boolean isForceDialogOpen) {
		this.isForceDialogOpen = isForceDialogOpen;
	}

	public String getPk_srcbilltype() {
		return pk_srcbilltype;
	}

	public void setPk_srcbilltype(String pk_srcbilltype) {
		this.pk_srcbilltype = pk_srcbilltype;
	}

	/**
	 * 返回动作类型编码 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getActionTypeCode() {
		return actionTypeCode;
	}

	/**
	 * 返回单据编码 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getBillNO() {
		return billNO;
	}

	/**
	 * 单据主键。 创建日期：(2001-10-11 15:12:36)
	 * 
	 * @since：V1.00
	 * @return String
	 */
	public String getBillPK() {
		return billPK;
	}

	/**
	 * 返回业务类型编码 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getPk_billtype() {
		return pk_billtype;
	}

	/**
	 * 返回处理人编码  
	 * 
	 * @return String
	 */
	public String getCheckerCode() {
		return checkerCode;
	}

	/**
	 * 返回处理人名称 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getCheckerName() {
		return checkerName;
	}

	/**
	 * 返回处理意见 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getCheckNote() {
		return checkNote;
	}

	/**
	 * 公司主键。 创建日期：(2001-10-11 15:05:52)
	 * 
	 * @since：V1.00
	 * @return String
	 */
	public String getCorpPK() {
		return corpPK;
	}

	/**
	 * 返回处理时间 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getDealDateTime() {
		return dealDateTime;
	}

	/**
	 * 返回数值对象的显示名称。 创建日期：(2001-2-15 14:18:08)
	 * 
	 * @return String 返回数值对象的显示名称。
	 */
	public String getEntityName() {
		return NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000049")/*消息对象*/;
	}

	/**
	 * 报警消息的html页面在服务器的地址 创建日期：(2001-9-13 17:53:44)
	 * 
	 * @since ：V1.00
	 * @return String
	 */
	public String getMailAddress() {
		return m_mailAddress;
	}

	/**
	 * 返回消息内容 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getMessageNote() {
		return messageNote;
	}

	/**
	 * 返加主键 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * 返加发送时间 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getSendDateTime() {
		return sendDateTime;
	}

	/**
	 * 返加发送人编码 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getSenderCode() {
		return senderCode;
	}

	/**
	 * 返加发送人名称 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 * 返回标题 创建日期：(2001-6-21 16:42:49)
	 * 
	 * @return String
	 */
	public String getTitle() {
		return m_title;
	}

	/**
	 * 返回是否已被处理 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean isCheck() {
		return isCheck;
	}

	/**
	 * 设置动作类型编码 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @param newActionTypeCode String
	 */
	public void setActionTypeCode(String newActionTypeCode) {
		actionTypeCode = newActionTypeCode;
	}

	/**
	 * 设置单据编码 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @param newBillNO String
	 */
	public void setBillNO(String newBillNO) {
		billNO = newBillNO;
	}

	/**
	 * 单据主键。 创建日期：(2001-10-11 15:12:36)
	 * 
	 * @since：V1.00
	 * @param newBillPK String
	 */
	public void setBillPK(String newBillPK) {
		billPK = newBillPK;
	}

	/**
	 * 设置业务类型编码 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @param newBillTypeCode String
	 */
	public void setPk_billtype(String newBillTypeCode) {
		pk_billtype = newBillTypeCode;
	}

	/**
	 * 设置处理人编码 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @param newCheckerCode String
	 */
	public void setCheckerCode(String newCheckerCode) {
		checkerCode = newCheckerCode;
	}

	/**
	 * 设置处理人名称 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @param newCheckerName String
	 */
	public void setCheckerName(String newCheckerName) {
		checkerName = newCheckerName;
	}

	/**
	 * 设置处理意见 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @param newCheckNote String
	 */
	public void setCheckNote(String newCheckNote) {
		checkNote = newCheckNote;
	}

	/**
	 * 公司主键。 创建日期：(2001-10-11 15:05:52)
	 * 
	 * @since：V1.00
	 * @param newCorpPK String
	 */
	public void setCorpPK(String newCorpPK) {
		corpPK = newCorpPK;
	}

	/**
	 * 设置处理时间 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @param newDealDateTime nc.vo.pub.lang.UFDateTime
	 */
	public void setDealDateTime(nc.vo.pub.lang.UFDateTime newDealDateTime) {
		dealDateTime = newDealDateTime;
	}

	/**
	 * 设置是否处理 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @param newIsCheck nc.vo.pub.lang.UFBoolean
	 */
	public void setIsCheck(nc.vo.pub.lang.UFBoolean newIsCheck) {
		isCheck = newIsCheck;
	}

	/**
	 * 报警消息的html页面在服务器的地址 创建日期：(2001-9-13 17:53:44)
	 * 
	 * @since ：V1.00
	 * @param newMailAddress String
	 */
	public void setMailAddress(String newMailAddress) {
		m_mailAddress = newMailAddress;
	}

	/**
	 * 设置消息内容 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @param newMessageNote String
	 */
	public void setMessageNote(String newMessageNote) {
		messageNote = newMessageNote;
	}

	/**
	 * 设置消息主键 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @param newPrimaryKey String
	 */
	public void setPrimaryKey(String newPrimaryKey) {
		primaryKey = newPrimaryKey;
	}

	/**
	 * 设置发送时间 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @param newSendDateTime nc.vo.pub.lang.UFDateTime
	 */
	public void setSendDateTime(nc.vo.pub.lang.UFDateTime newSendDateTime) {
		sendDateTime = newSendDateTime;
	}

	/**
	 * 设置发送人编码 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @param newSenderCode String
	 */
	public void setSenderCode(String newSenderCode) {
		senderCode = newSenderCode;
	}

	/**
	 * 设置发送人名称 创建日期：(2001-6-22 13:39:44)
	 * 
	 * @param newSenderName String
	 */
	public void setSenderName(String newSenderName) {
		senderName = newSenderName;
	}

	/**
	 * 设置标题 创建日期：(2001-6-21 16:42:49)
	 * 
	 * @param newTitle String
	 */
	public void setTitle(String newTitle) {
		m_title = newTitle;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int msgPriority) {
		this.priority = msgPriority;
	}

	/**
	 * 只显示人名和内容
	 * <li>NOTE::待办事务列表就是直接显示的该对象!
	 * <li>对于预警平台消息,name固定,即不从数据库读取
	 * 
	 * @modifier huangzg 由于现在没有必要考虑发件人和接收的差异,所以只显示发送人。
	 */
	public String toString() {
		StringBuffer name = new StringBuffer();
		if (getMsgType() == MessageTypes.MSG_TYPE_PA) {
			// 预警平台的消息
			name.append(getPAMutliLangName());
			for (int i = name.toString().getBytes().length; i < 20; i++){
				name.append(".");
			}
			name.append(getAlertNoteAfterI18N());
			return name.toString();
		} else {
			name.append(getSenderName());
			if (name.length() == 0) {
				name.append("Oops!");
			}
			for (int i = name.toString().getBytes().length; i < 20; i++) {
				name.append(".");
			}
			name.append(getMessageNoteAfterI18N(getMessageNote()));
			return name.toString();
		}
	}

	/**
	 * 返回预警平台这一名称的多语显示
	 */
	public static String getPAMutliLangName() {
		return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "alertSender");/* 预警平台 */
	}

	/**
	 * 预警平台的消息内容
	 * <li>pub_workflownote表中仅保存了"2005-03-23 13:26:54"时间串
	 * 
	 * @author 雷军 2005-3-30 20:34:22
	 */
	private String getAlertNoteAfterI18N() {
		String originalNote = getMessageNote();
		if (originalNote == null || originalNote.length() < 1)
			return "Oops!";
		// 如果是NC31以前版本残留的数据,则不进行I18N
		if (originalNote.length() > "2005-03-23 13:26:54".length())
			return originalNote;
		return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "alertNote", null,
				new String[] { originalNote });
	}

	/**
	 * pub_workflownote表messagenote字段从NC31开始存储的格式为:
	 * <li>如"t5{commitBill}a{notify}b{checkBill}test111",
	 * <li>需要翻译转换
	 * 
	 * @author 雷军 2005-3-30 20:34:22
	 */
	public static String getMessageNoteAfterI18N(String originalNote) {
		if (originalNote == null || originalNote.length() < 1)
			//return "Oops!";
			return "";

		boolean bStart = false;
		StringBuffer sbb = new StringBuffer();
		StringBuffer sbc = new StringBuffer();
		for (int i = 0; i < (originalNote == null ? 0 : originalNote.length()); i++) {
			char c = originalNote.charAt(i);
			// Logger.debug(c);
			if (c == '{') {
				bStart = true;
				sbb.setLength(0);
				continue;
			} else if (c == '}') {
				bStart = false;
				sbc
						.append(" "
								+ NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", sbb.toString()));
				sbb.setLength(0);
				continue;
			}
			if (bStart) {
				sbb.append(c);
			} else {
				sbc.append(c);
			}
		}
		return sbc.toString();
	}

	/* (non-Javadoc)
	 * @see nc.vo.pub.ValueObject#validate()
	 */
	public void validate() throws nc.vo.pub.ValidationException {
	}

	public UFBoolean isSealed() {
		return isSealed;
	}

	public void setIsSealed(UFBoolean isSealed) {
		this.isSealed = isSealed;
	}

	public byte[] getFilecontent() {
		return filecontent;
	}

	public void setFilecontent(byte[] filecontent) {
		this.filecontent = filecontent;
	}

	public boolean isAttachChanged() {
		return isAttachChanged;
	}

	public void setAttachChanged(boolean isAttachChanged) {
		this.isAttachChanged = isAttachChanged;
	}

}