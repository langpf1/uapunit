package uap.workflow.pub.app.mobile.vo;

import java.io.Serializable;

/**
 * ���յ��Ķ�����Ϣ
 * 
 * @author leijun 2007-3-26
 */
public class ReceivedSmsVO implements Serializable {
	private String mobileNumber;

	private String sessionId;

	private String smsContent;

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

}
