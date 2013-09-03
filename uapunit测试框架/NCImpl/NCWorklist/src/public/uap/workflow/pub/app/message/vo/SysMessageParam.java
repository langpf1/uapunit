package uap.workflow.pub.app.message.vo;

import java.io.Serializable;

import uap.workflow.pub.app.mail.vo.DefaultSMTP;

/**
 * 系统消息配置，主要用于配置默认邮件账号和短信网关
 * 
 * @author 雷军 2003-11-4
 * @modifier leijun 2008-4 增加属性msgFileMaxSize
 */
public class SysMessageParam implements Serializable {
	/**
	 * NC系统默认邮件账号
	 */
	public DefaultSMTP smtp = null;

	/**
	 * 消息附件的文件大小，以MB为单位
	 */
	public float msgFileMaxSize = 5.5f;

	public float getMsgFileMaxSize() {
		return msgFileMaxSize;
	}

	public void setMsgFileMaxSize(float msgFileMaxSize) {
		this.msgFileMaxSize = msgFileMaxSize;
	}

	public SysMessageParam() {
		super();
		smtp = new DefaultSMTP();
	}

	public DefaultSMTP getSmtp() {
		return smtp;
	}

	public void setSmtp(DefaultSMTP newSmtp) {
		smtp = newSmtp;
	}
}
