package uap.workflow.pub.app.mobile.vo;

import java.io.Serializable;

import nc.vo.pub.lang.UFDateTime;

/**
 * �ֻ����������࣬�����������ݺͶ�����������
 *
 * @author �Լ̽� 2003-6-17
 */
public class MobileMsg implements Serializable {
	//��������
	private String msg = null;

	//Ŀ���ֻ�
	private String[] targetPhones = null;

	/*
	 * Ⱥ����
	 * private boolean isGroupSending
	 * private ArrayList targetPhones
	 */

	//�������ֻ�(��ѡ��)
	private String sourcePhone = null;

	//�Ƿ�ʱ����
	private boolean isTiming = false;

	//��ʱ���͵�ʱ��
	private UFDateTime time = null;

	/* 
	 * �Ƿ�ֱ�ӷ��͡����ַ�ʽ�ֳ����ᷢ�ͣ������ַ�ʽ�£�
	 * ֱ�ӰѶ���������ʾ��Ŀ���ֻ���Ļ�ϣ�ʹ�Է��ܹ����̿���
	 * �������ݡ� 
	 */
	private boolean isDirectSend = false;

	/**
	 * �ỰID����
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
	 * �˴����뷽��˵����
	 * @return String
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * �˴����뷽��˵����
	 * @return String
	 */
	public String getSourcePhone() {
		return sourcePhone;
	}

	/**
	 * �˴����뷽��˵����
	 * @return String
	 */
	public String[] getTargetPhones() {
		return targetPhones;
	}

	/**
	 * �˴����뷽��˵����
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTime() {
		return time;
	}

	/**
	 * �˴����뷽��˵����
	 * @return boolean
	 */
	public boolean isDirectSend() {
		return isDirectSend;
	}

	/**
	 * �Ƿ���Ч���ֻ���
	 *
	 * @return boolean
	 * @param phone String
	 */
	public static boolean isLegalPhone(String phone) {
		if (phone == null || !(phone.length() == 11)) {
			//�����ֻ���11λ
			return false;
		}
		if (!phone.startsWith("13")) {
			//�ֻ�����13��ͷ
			return false;
		}
		try {
			Long.decode(phone);
		} catch (NumberFormatException e) {
			//�ֻ���Ӧ�����ֹ���
			return false;
		}
		return true;
	}

	/**
	 * �˴����뷽��˵����
	 * @return boolean
	 */
	public boolean isTiming() {
		return isTiming;
	}

	/**
	 * �˴����뷽��˵����
	 * @param newIsDirectSend boolean
	 */
	public void setIsDirectSend(boolean newIsDirectSend) {
		isDirectSend = newIsDirectSend;
	}

	/**
	 * �˴����뷽��˵����
	 * @param newIsTiming boolean
	 */
	public void setIsTiming(boolean newIsTiming) {
		isTiming = newIsTiming;
	}

	/**
	 * �˴����뷽��˵����
	 * @param newMsg String
	 */
	public void setMsg(String newMsg) {
		msg = newMsg;
	}

	/**
	 * �˴����뷽��˵����
	 * @param newSourcePhone String
	 */
	public void setSourcePhone(String newSourcePhone) {
		sourcePhone = newSourcePhone;
	}

	/**
	 * �˴����뷽��˵����
	 * @param newTargetPhone String
	 */
	public void setTargetPhones(String[] newTargetPhones) {
		targetPhones = newTargetPhones;
	}

	/**
	 * �˴����뷽��˵����
	 * @param newTime nc.vo.pub.lang.UFDateTime
	 */
	public void setTime(UFDateTime newTime) {
		time = newTime;
	}
}
