package uap.workflow.pub.app.message.vo;

import java.io.Serializable;

import nc.vo.pub.lang.UFDateTime;

/**
 * 分三个数组保存每次刷新查询到的消息
 * <li>用于前后台传递
 * 
 * @author leijun 2009-4
 * @since 6.0
 */
public class MessageDateTimeVO implements Serializable {
	/**
	 * 所有在accessTime后接收到的新消息，包括审批工作项、业务流工作项、普通消息 
	 */
	private MessageVO[] workitems = null;
	
	/**
	 * 所有在accessTime后接收到的新消息，包括通知消息 
	 */
	private MessageVO[] infoMsgs = null;

	/**
	 * 所有在accessTime后接收到的新消息，包括预警消息 
	 */
	private MessageVO[] paMsgs = null;

	/**
	 * 所有在accessTime后接收到的新消息，包括公告消息、对发消息
	 */
	private MessageVO[] bulletinMsgs = null;

	/**
	 * 读取消息的截止时间
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
