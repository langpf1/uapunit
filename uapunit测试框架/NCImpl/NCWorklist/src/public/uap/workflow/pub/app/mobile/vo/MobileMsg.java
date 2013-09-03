package uap.workflow.pub.app.mobile.vo;

import java.io.Serializable;

import nc.vo.pub.lang.UFDateTime;

/**
 * 手机短信数据类，包括控制数据和短信内容数据
 *
 * @author 赵继江 2003-6-17
 */
public class MobileMsg implements Serializable {
	//短信内容
	private String msg = null;

	//目标手机
	private String[] targetPhones = null;

	/*
	 * 群发：
	 * private boolean isGroupSending
	 * private ArrayList targetPhones
	 */

	//发送者手机(可选项)
	private String sourcePhone = null;

	//是否定时发送
	private boolean isTiming = false;

	//定时发送的时间
	private UFDateTime time = null;

	/* 
	 * 是否直接发送。这种方式又称免提发送，在这种方式下，
	 * 直接把短信内容显示在目标手机屏幕上，使对方能够立刻看到
	 * 短信内容。 
	 */
	private boolean isDirectSend = false;

	/**
	 * 会话ID数组
	 */
	private String[] serialNumbers = null;

	public String[] getSerialNumbers() {
		return serialNumbers;
	}

	public void setSerialNumbers(String[] serialNumbers) {
		this.serialNumbers = serialNumbers;
	}

	public MobileMsg() {
		super();
	}

	/**
	 * 此处插入方法说明。
	 * @return String
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * 此处插入方法说明。
	 * @return String
	 */
	public String getSourcePhone() {
		return sourcePhone;
	}

	/**
	 * 此处插入方法说明。
	 * @return String
	 */
	public String[] getTargetPhones() {
		return targetPhones;
	}

	/**
	 * 此处插入方法说明。
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTime() {
		return time;
	}

	/**
	 * 此处插入方法说明。
	 * @return boolean
	 */
	public boolean isDirectSend() {
		return isDirectSend;
	}

	/**
	 * 是否有效的手机号
	 *
	 * @return boolean
	 * @param phone String
	 */
	public static boolean isLegalPhone(String phone) {
		if (phone == null || !(phone.length() == 11)) {
			//国内手机号11位
			return false;
		}
		if (!phone.startsWith("13")) {
			//手机号以13开头
			return false;
		}
		try {
			Long.decode(phone);
		} catch (NumberFormatException e) {
			//手机号应由数字构成
			return false;
		}
		return true;
	}

	/**
	 * 此处插入方法说明。
	 * @return boolean
	 */
	public boolean isTiming() {
		return isTiming;
	}

	/**
	 * 此处插入方法说明。
	 * @param newIsDirectSend boolean
	 */
	public void setIsDirectSend(boolean newIsDirectSend) {
		isDirectSend = newIsDirectSend;
	}

	/**
	 * 此处插入方法说明。
	 * @param newIsTiming boolean
	 */
	public void setIsTiming(boolean newIsTiming) {
		isTiming = newIsTiming;
	}

	/**
	 * 此处插入方法说明。
	 * @param newMsg String
	 */
	public void setMsg(String newMsg) {
		msg = newMsg;
	}

	/**
	 * 此处插入方法说明。
	 * @param newSourcePhone String
	 */
	public void setSourcePhone(String newSourcePhone) {
		sourcePhone = newSourcePhone;
	}

	/**
	 * 此处插入方法说明。
	 * @param newTargetPhone String
	 */
	public void setTargetPhones(String[] newTargetPhones) {
		targetPhones = newTargetPhones;
	}

	/**
	 * 此处插入方法说明。
	 * @param newTime nc.vo.pub.lang.UFDateTime
	 */
	public void setTime(UFDateTime newTime) {
		time = newTime;
	}
}
