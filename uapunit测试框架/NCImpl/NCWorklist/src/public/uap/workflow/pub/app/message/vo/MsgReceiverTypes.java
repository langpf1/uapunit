package uap.workflow.pub.app.message.vo;

import java.util.HashMap;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 消息接收者类型
 * 
 * @author leijun 2007-4-24
 */
public class MsgReceiverTypes {
	public static final int REVEIVER_TYPE_USER = 2; //用户（操作员）

	public static final int REVEIVER_TYPE_ROLE = 1; //组->角色

	public static final int REVEIVER_TYPE_STATION = 0; //岗位

	public static final int REVEIVER_TYPE_SYSTEM = 3; //系统变量

	public static final int REVEIVER_TYPE_CUSTOM = 4; //自定义，由单据的插件实现

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
	 * 获得显示名称
	 */
	public String toString() {
		//FIXME::i18n
		switch (_value) {
			case REVEIVER_TYPE_USER:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000175") /*@res "操作员"*/;
			case REVEIVER_TYPE_ROLE:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("101203", "UPP101203-000056") /*@res "角色"*/;
			case REVEIVER_TYPE_SYSTEM:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000036")/*系统变量*/;
			case REVEIVER_TYPE_CUSTOM:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000037")/*自定义*/;
			default:
				return "ERROR";
		}
	}

}
