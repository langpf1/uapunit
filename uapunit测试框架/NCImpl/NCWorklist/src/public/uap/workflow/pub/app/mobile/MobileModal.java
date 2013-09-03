package uap.workflow.pub.app.mobile;

import java.util.HashMap;

import nc.vo.ml.NCLangRes4VoTransl;

public class MobileModal {

	//��
	public static final int BLANK_INT = -1;
	
	//�޶���
	public static final int NO_MOBILE_INT = 0;

	//����֪ͨ
	public static final int MOBILE_INFO_INT = 1;

	//��������
	public static final int MOBILE_APPROVE_INT = 2;
	
	public static final MobileModal BLANK = new MobileModal(BLANK_INT);

	public static final MobileModal NO_MOBILE = new MobileModal(NO_MOBILE_INT);

	public static final MobileModal MOBILE_INFO = new MobileModal(MOBILE_INFO_INT);

	public static final MobileModal MOBILE_APPROVE = new MobileModal(MOBILE_APPROVE_INT);

	public static final String[] TAGS = { "BLANK","NOMOBILE", "MOBILEINFO", "MOBILEAPPROVE" };

	public static final MobileModal[] VALUES = { BLANK,NO_MOBILE, MOBILE_INFO, MOBILE_APPROVE };

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
	public static MobileModal fromString(String tag) {
		MobileModal race = (MobileModal) tagMap.get(tag);
		if (race == null && tag != null)
			throw new IllegalArgumentException(tag);
		return race;
	}

	/**
	 * �������
	 * @param value The value
	 */
	private MobileModal(int value) {
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
			case NO_MOBILE_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("101203", "UPP101203-000022")/*@res "��"*/;
			case MOBILE_INFO_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("101203", "UPP101203-000157")/*@res "����֪ͨ"*/;
			case MOBILE_APPROVE_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("101203", "UPP101203-000158")/*@res "��������"*/;
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
