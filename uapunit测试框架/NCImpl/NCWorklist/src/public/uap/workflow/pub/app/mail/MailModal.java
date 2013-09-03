package uap.workflow.pub.app.mail;

import java.util.HashMap;

import nc.vo.ml.NCLangRes4VoTransl;

public class MailModal {

	//无
	public static final int BLANK_INT = -1;
	//无邮件
	public static final int NO_MAIL_INT = 0;

	//邮件通知
	public static final int MAIL_INFO_INT = 1;

	//邮件审批
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
	 * 根据字串来构造对象，字串必取值于TAGS数组中的某值。
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
	 * 构造对象
	 * @param value The value
	 */
	private MailModal(int value) {
		_value = value;
	}

	/**
	 * 获得对象的数值
	 * @return An int value
	 */
	public int getValue() {
		return _value;
	}

	/**
	 * 获得显示名称
	 */
	public String toString() {
		//return VISUAL_NAMES[_value];
		switch (_value) {
		case BLANK_INT:
			return " ";
		case NO_MAIL_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("101203", "UPP101203-000022")/*
																							 * @res
																							 * "无"
																							 */;
		case MAIL_INFO_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("101203", "UPP101203-000155")/*
																							 * @res
																							 * "邮件通知"
																							 */;
		case MAIL_APPROVE_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("101203", "UPP101203-000156")/*
																							 * @res
																							 * "邮件审批"
																							 */;
		default:
			return "ERROR";
		}
	}

	/**
	 * 获得标记值
	 */
	public String getTag() {
		return TAGS[_value+1];
	}

}
