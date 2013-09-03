package uap.workflow.pub.app.message.vo;

import uap.workflow.vo.WorkflownoteVO;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFDateTime;


/**
 * 非业务消息
 * <li>主要用于发送消息到pub_messageinfo表中
 * 
 * @author 陈新宇 2001-4-20 19:24:38
 * @modifier 雷军 2005-4-30 增加字段actionType
 * @modifier leijun 2007-9-7 增加字段billtype
 * @modifier guowl 2008-4-14 增加字段fileContent
 * @modifier leijun 2008-10 增加字段m_titleColor
 */
public class CommonMessageVO extends ValueObject {
	public static final String TYPE_MSN = "MSN";

	/** 消息内容 */
	private String m_message = null;

	/** 发送人PK */
	private String m_sender = null;

	/** 接收人 */
	private UserNameObject[] m_receiver = null;

	/** 发送时间 */
	private UFDateTime m_sendDate = null;

	/** 标题 */
	private String m_title = null;

	/** 主题颜色 */
	private String m_titleColor = null;

	/** 报警消息的html页面在服务器的地址 */
	private String m_mailAddress = null;

	private String m_actionType = WorkflownoteVO.WORKITEM_TYPE_APPROVE;

	private String billtype;

	private String billid;

	private byte[] fileContent; 
	
	private String pk_wf_msg;
	private boolean needFlowCheck;
	private String pk_wf_task;
	
	private String billno;
	/**
	 * 消息类型。huangzg++,
	 */
	private int msgType = MessageTypes.MSG_TYPE_INFO;

	private int priority = MessagePriority.PRI_NORMAL;
	


	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getTitleColor() {
		return m_titleColor;
	}

	public void setTitleColor(String color) {
		m_titleColor = color;
	}

	public String getBillid() {
		return billid;
	}

	public void setBillid(String billid) {
		this.billid = billid;
	}

	public CommonMessageVO() {
		super();
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public String getActionType() {
		return m_actionType;
	}

	public void setActionType(String actionType) {
		this.m_actionType = actionType;
	}

	public String getEntityName() {
		return null;
	}

	/**
	 * 报警消息的html页面在服务器的地址 创建日期：(2001-9-13 17:53:44)
	 * 
	 * @since ：V1.00
	 * @return java.lang.String
	 */
	public java.lang.String getMailAddress() {
		return m_mailAddress;
	}

	/**
	 * 返回消息内容 创建日期：(2001-4-20 19:25:14)
	 * 
	 * @return java.lang.String
	 */
	public String getMessageContent() {
		return m_message;
	}

	/**
	 * 返回消息发送者 创建日期：(2001-6-21 14:54:57)
	 * 
	 * @return UserNameObject
	 */
	public UserNameObject[] getReceiver() {
		return m_receiver;
	}

	/**
	 * 返回发送日期 创建日期：(2001-6-21 14:54:57)
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getSendDataTime() {
		return m_sendDate;
	}

	/**
	 * 返回发送人 创建日期：(2001-6-21 14:54:57)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSender() {
		return m_sender;
	}

	/**
	 * 返回标题 创建日期：(2001-6-21 16:42:49)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTitle() {
		return m_title;
	}

	/**
	 * 报警消息的html页面在服务器的地址 创建日期：(2001-9-13 17:53:44)
	 * 
	 * @since ：V1.00
	 * @param newMailAddress java.lang.String
	 */
	public void setMailAddress(java.lang.String newMailAddress) {
		m_mailAddress = newMailAddress;
	}

	/**
	 * 设置消息内容 创建日期：(2001-4-20 19:26:05)
	 * 
	 * @param content java.lang.String
	 */
	public void setMessageContent(String content) {
		m_message = content;
	}

	/**
	 * 设置消息发送者 创建日期：(2001-6-21 14:54:57)
	 * 
	 * @param newReceiver UserNameObject
	 */
	public void setReceiver(UserNameObject[] newReceiver) {
		m_receiver = newReceiver;
	}

	/**
	 * 设置发送日期 创建日期：(2001-6-21 14:54:57)
	 * 
	 * @param newSendData nc.vo.pub.lang.UFDateTime
	 */
	public void setSendDataTime(nc.vo.pub.lang.UFDateTime newSendData) {
		m_sendDate = newSendData;
	}

	/**
	 * 设置发送人 创建日期：(2001-6-21 14:54:57)
	 * 
	 * @param newSender java.lang.String
	 */
	public void setSender(java.lang.String newSender) {
		m_sender = newSender;
	}

	/**
	 * 设置标题 创建日期：(2001-6-21 16:42:49)
	 * 
	 * @param newTitle java.lang.String
	 */
	public void setTitle(java.lang.String newTitle) {
		m_title = newTitle;
	}

	public void validate() {
	}

	public int getType() {
		return msgType;
	}

	public void setType(int msgType) {
		this.msgType = msgType;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public String getPk_wf_msg() {
		return pk_wf_msg;
	}

	public void setPk_wf_msg(String pk_wf_msg) {
		this.pk_wf_msg = pk_wf_msg;
	}

	public boolean isNeedFlowCheck() {
		return needFlowCheck;
	}

	public void setNeedFlowCheck(boolean needFlowCheck) {
		this.needFlowCheck = needFlowCheck;
	}

	public String getPk_wf_task() {
		return pk_wf_task;
	}

	public void setPk_wf_task(String pk_wf_task) {
		this.pk_wf_task = pk_wf_task;
	}

}