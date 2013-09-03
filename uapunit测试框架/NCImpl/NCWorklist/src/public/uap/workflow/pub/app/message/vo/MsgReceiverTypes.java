package uap.workflow.pub.app.message.vo;

import java.util.HashMap;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * ��Ϣ����������
 * 
 * @author leijun 2007-4-24
 */
public class MsgReceiverTypes {
	public static final int REVEIVER_TYPE_USER = 2; //�û�������Ա��

	public static final int REVEIVER_TYPE_ROLE = 1; //��->��ɫ

	public static final int REVEIVER_TYPE_STATION = 0; //��λ

	public static final int REVEIVER_TYPE_SYSTEM = 3; //ϵͳ����

	public static final int REVEIVER_TYPE_CUSTOM = 4; //�Զ��壬�ɵ��ݵĲ��ʵ��

	public static final MsgReceiverTypes USERS = new MsgReceiverTypes(REVEIVER_TYPE_USER);

	public static final MsgReceiverTypes ROLES = new MsgReceiverTypes(REVEIVER_TYPE_ROLE);

	public static final MsgReceiverTypes SYSTEM = new MsgReceiverTypes(REVEIVER_TYPE_SYSTEM);

	public static final MsgReceiverTypes CUSTOM = new MsgReceiverTypes(REVEIVER_TYPE_CUSTOM);

	public static final MsgReceiverTypes[] VALUES = { USERS, ROLES, SYSTEM, CUSTOM };

	private static final HashMap tagMap = new HashMap();

	private final int _value;

	public static MsgReceiverTypes fromString(String tag) {
		MsgReceiverTypes style = (MsgReceiverTypes) tagMap.get(tag);
		if (style == null && tag != null)
			throw new IllegalArgumentException(tag);
		return style;
	}

	private MsgReceiverTypes(int value) {
		_value = value;
	}

	public int getValue() {
		return _value;
	}

	/**
	 * �����ʾ����
	 */
	public String toString() {
		//FIXME::i18n
		switch (_value) {
			case REVEIVER_TYPE_USER:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000175") /*@res "����Ա"*/;
			case REVEIVER_TYPE_ROLE:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("101203", "UPP101203-000056") /*@res "��ɫ"*/;
			case REVEIVER_TYPE_SYSTEM:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000036")/*ϵͳ����*/;
			case REVEIVER_TYPE_CUSTOM:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000037")/*�Զ���*/;
			default:
				return "ERROR";
		}
	}

}
