package uap.workflow.pub.app.message.vo;

import java.io.Serializable;

import uap.workflow.pub.app.mail.vo.DefaultSMTP;

/**
 * ϵͳ��Ϣ���ã���Ҫ��������Ĭ���ʼ��˺źͶ�������
 * 
 * @author �׾� 2003-11-4
 * @modifier leijun 2008-4 ��������msgFileMaxSize
 */
public class SysMessageParam implements Serializable {
	/**
	 * NCϵͳĬ���ʼ��˺�
	 */
	public DefaultSMTP smtp = null;

	/**
	 * ��Ϣ�������ļ���С����MBΪ��λ
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
