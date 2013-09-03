package uap.workflow.pub.app.message.vo;

import java.io.Serializable;

import nc.vo.pub.lang.UFDateTime;

/**
 * ���������鱣��ÿ��ˢ�²�ѯ������Ϣ
 * <li>����ǰ��̨����
 * 
 * @author leijun 2009-4
 * @since 6.0
 */
public class MessageDateTimeVO implements Serializable {
	/**
	 * ������accessTime����յ�������Ϣ���������������ҵ�����������ͨ��Ϣ 
	 */
	private MessageVO[] workitems = null;
	
	/**
	 * ������accessTime����յ�������Ϣ������֪ͨ��Ϣ 
	 */
	private MessageVO[] infoMsgs = null;

	/**
	 * ������accessTime����յ�������Ϣ������Ԥ����Ϣ 
	 */
	private MessageVO[] paMsgs = null;

	/**
	 * ������accessTime����յ�������Ϣ������������Ϣ���Է���Ϣ
	 */
	private MessageVO[] bulletinMsgs = null;

	/**
	 * ��ȡ��Ϣ�Ľ�ֹʱ��
	 */
	private UFDateTime m_accessTime = null;

	public MessageDateTimeVO() {
		super();
	}

	public void setAccessTime(UFDateTime newAccessTime) {
		m_accessTime = newAccessTime;
	}

	public UFDateTime getAccessTime() {
		return m_accessTime;
	}

	public MessageVO[] getWorkitems() {
		return workitems;
	}

	public void setWorkitems(MessageVO[] newMessages) {
		workitems = newMessages;
	}

	public MessageVO[] getBulletinMsgs() {
		return bulletinMsgs;
	}

	public void setScrollMsgs(MessageVO[] scrollMsgs) {
		this.bulletinMsgs = scrollMsgs;
	}

	public MessageVO[] getPAMsgs() {
		return paMsgs;
	}

	public void setPAMsgs(MessageVO[] infoMsgs) {
		this.paMsgs = infoMsgs;
	}

	public MessageVO[] getInfoMsgs() {
		return infoMsgs;
	}

	public void setInfoMsgs(MessageVO[] infoMsgs) {
		this.infoMsgs = infoMsgs;
	}
}
