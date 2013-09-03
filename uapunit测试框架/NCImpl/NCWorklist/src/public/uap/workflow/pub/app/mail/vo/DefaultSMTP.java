package uap.workflow.pub.app.mail.vo;

import java.io.Serializable;

/**
 * 默认邮件账号配置信息
 * 
 * @author 雷军 2003-11-4
 * @modifier leijun 2008-1-30 增加senderName属性
 */
public class DefaultSMTP implements Serializable {
	//邮件帐号名
	public String user = "";

	//邮件帐号密码
	public String password = "";

	//发送人邮件帐号
	public String sender = "";

	//发送人别名
	public String senderName = "";

	//SMTP服务器
	public String smtp = "";

	//POP3服务器
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
	 * 此处插入方法说明。
	 * 创建日期：(2003-11-4 16:17:52)
	 * @return java.lang.String
	 */
	public java.lang.String getPassword() {
		return password;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-11-4 16:17:52)
	 * @return java.lang.String
	 */
	public java.lang.String getPop3() {
		return pop3;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-11-4 16:17:52)
	 * @return java.lang.String
	 */
	public java.lang.String getSender() {
		return sender;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-11-4 16:17:52)
	 * @return java.lang.String
	 */
	public java.lang.String getSmtp() {
		return smtp;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-11-4 16:17:52)
	 * @return java.lang.String
	 */
	public java.lang.String getUser() {
		return user;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-11-4 16:17:52)
	 * @param newPassword java.lang.String
	 */
	public void setPassword(java.lang.String newPassword) {
		password = newPassword;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-11-4 16:17:52)
	 * @param newPop3 java.lang.String
	 */
	public void setPop3(java.lang.String newPop3) {
		pop3 = newPop3;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-11-4 16:17:52)
	 * @param newSender java.lang.String
	 */
	public void setSender(java.lang.String newSender) {
		sender = newSender;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-11-4 16:17:52)
	 * @param newSmtp java.lang.String
	 */
	public void setSmtp(java.lang.String newSmtp) {
		smtp = newSmtp;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-11-4 16:17:52)
	 * @param newUser java.lang.String
	 */
	public void setUser(java.lang.String newUser) {
		user = newUser;
	}
}
