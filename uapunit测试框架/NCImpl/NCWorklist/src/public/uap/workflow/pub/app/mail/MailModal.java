package uap.workflow.pub.app.mail;

import java.util.HashMap;

import nc.vo.ml.NCLangRes4VoTransl;

public class MailModal {

	//��
	public static final int BLANK_INT = -1;
	//���ʼ�
	public static final int NO_MAIL_INT = 0;

	//�ʼ�֪ͨ
	public static final int MAIL_INFO_INT = 1;

	//�ʼ�����
	public static final int MAIL_APPROVE_INT = 2;
	
	public static final MailModal BLANK = new MailModal(BLANK_INT);

	public static final MailModal NO_MAIL = new MailModal(NO_MAIL_INT);

	public static final MailModal MAIL_INFO = new MailModal(MAIL_INFO_INT);

	public static final MailModal MAIL_APPROVE = new MailModal(MAIL_APPROVE_INT);

	public static final String[] TAGS = { "BLANK","NOMAIL", "MAILINFO", "MAILAPPROVE" };

	public static final MailModal[] VALUES = { BLANK,NO_MAIL, MAIL_INFO, MAIL_APPROVE };

	private static final HashMap tagMap = new HashMap();

	private final int _value;

	static {
		for (int i = 0; i < TAGS.length; i++) {
			tagMap.put(TAGS[i], VALUES[i]);
		}
	}

	/**
	 * �����ִ�����������ִ���ȡֵ��TAGS�����е�ĳֵ��
	 *
	 * @param tag The String
	 * @return The ConditionType object
	 */
	public static MailModal fromString(String tag) {
		MailModal race = (MailModal) tagMap.get(tag);
		if (race == null && tag != null)
			throw new IllegalArgumentException(tag);
		return race;
	}

	/**
	 * �������
	 * @param value The value
	 */
	private MailModal(int value) {
		_value = value;
	}

	/**
	 * ��ö������ֵ
	 * @return An int value
	 */
	public int getValue() {
		return _value;
	}

	/**
	 * �����ʾ����
	 */
	public String toString() {
		//return VISUAL_NAMES[_value];
		switch (_value) {
		case BLANK_INT:
			return " ";
		case NO_MAIL_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("101203", "UPP101203-000022")/*
																							 * @res
																							 * "��"
																							 */;
		case MAIL_INFO_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("101203", "UPP101203-000155")/*
																							 * @res
																							 * "�ʼ�֪ͨ"
																							 */;
		case MAIL_APPROVE_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("101203", "UPP101203-000156")/*
																							 * @res
																							 * "�ʼ�����"
																							 */;
		default:
			return "ERROR";
		}
	}

	/**
	 * ��ñ��ֵ
	 */
	public String getTag() {
		return TAGS[_value+1];
	}

}
