package uap.workflow.pub.app.mail.vo;

import java.io.Serializable;

/**
 * Ĭ���ʼ��˺�������Ϣ
 * 
 * @author �׾� 2003-11-4
 * @modifier leijun 2008-1-30 ����senderName����
 */
public class DefaultSMTP implements Serializable {
	//�ʼ��ʺ���
	public String user = "";

	//�ʼ��ʺ�����
	public String password = "";

	//�������ʼ��ʺ�
	public String sender = "";

	//�����˱���
	public String senderName = "";

	//SMTP������
	public String smtp = "";

	//POP3������
	public String pop3 = "";

	public DefaultSMTP() {
		super();
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2003-11-4 16:17:52)
	 * @return java.lang.String
	 */
	public java.lang.String getPassword() {
		return password;
	}

	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2003-11-4 16:17:52)
	 * @return java.lang.String
	 */
	public java.lang.String getPop3() {
		return pop3;
	}

	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2003-11-4 16:17:52)
	 * @return java.lang.String
	 */
	public java.lang.String getSender() {
		return sender;
	}

	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2003-11-4 16:17:52)
	 * @return java.lang.String
	 */
	public java.lang.String getSmtp() {
		return smtp;
	}

	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2003-11-4 16:17:52)
	 * @return java.lang.String
	 */
	public java.lang.String getUser() {
		return user;
	}

	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2003-11-4 16:17:52)
	 * @param newPassword java.lang.String
	 */
	public void setPassword(java.lang.String newPassword) {
		password = newPassword;
	}

	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2003-11-4 16:17:52)
	 * @param newPop3 java.lang.String
	 */
	public void setPop3(java.lang.String newPop3) {
		pop3 = newPop3;
	}

	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2003-11-4 16:17:52)
	 * @param newSender java.lang.String
	 */
	public void setSender(java.lang.String newSender) {
		sender = newSender;
	}

	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2003-11-4 16:17:52)
	 * @param newSmtp java.lang.String
	 */
	public void setSmtp(java.lang.String newSmtp) {
		smtp = newSmtp;
	}

	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2003-11-4 16:17:52)
	 * @param newUser java.lang.String
	 */
	public void setUser(java.lang.String newUser) {
		user = newUser;
	}
}
