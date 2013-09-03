package uap.workflow.pub.app.mail.vo;

import java.io.Serializable;

import uap.workflow.pub.app.mail.MailModal;

import nc.bs.framework.common.InvocationInfo;

/**
 * 审批流 邮件发送的信息
 * 
 * @author leijun 2008-12
 */
public class EmailMsg implements Serializable {
	private MailModal mailModal;

	private String billId;

	private String billNo;

	private String billType;

	private String[] userIds;

	private String printTempletId;

	private String topic;

	private String senderman;

	private int iTasktype = -1; // see WFTask

	private String langCode;

	private String datasource;

	private InvocationInfo invocationInfo;

	/**
	 * @deprecated since v6 请使用InvocationInfo
	 * @param langCode
	 */
	public String getDatasource() {
		return datasource;
	}

	/**
	 * @deprecated since v6 请使用InvocationInfo
	 * @param langCode
	 */
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	/**
	 * @deprecated since v6 请使用InvocationInfo
	 * @param langCode
	 */
	public String getLangCode() {
		return langCode;
	}

	/**
	 * @deprecated since v6 请使用InvocationInfo
	 * @param langCode
	 */
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public int getTasktype() {
		return iTasktype;
	}

	public void setTasktype(int tasktype) {
		iTasktype = tasktype;
	}

	public MailModal getMailModal() {
		return mailModal;
	}

	public void setMailModal(MailModal mailModal) {
		this.mailModal = mailModal;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String[] getUserIds() {
		return userIds;
	}

	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}

	public String getPrintTempletId() {
		return printTempletId;
	}

	public void setPrintTempletId(String printTempletId) {
		this.printTempletId = printTempletId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getSenderman() {
		return senderman;
	}

	public void setSenderman(String senderman) {
		this.senderman = senderman;
	}

	public InvocationInfo getInvocationInfo() {
		return invocationInfo;
	}

	public void setInvocationInfo(InvocationInfo invocationInfo) {
		this.invocationInfo = invocationInfo;
	}

}
