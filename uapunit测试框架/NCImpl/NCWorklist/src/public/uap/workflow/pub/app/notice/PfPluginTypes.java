package uap.workflow.pub.app.notice;

import java.io.Serializable;
import java.util.HashMap;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 消息配置中消息插件的类型
 * @author 雷军 2005-4-13
 * @modifier leijun 2008-7 增加一种消息类型：业务消息（可直接打开单据UI）
 */
public class PfPluginTypes   implements Serializable{
	public static final int NOTE_INT = 0;

	public static final int MAIL_INT = 1;

	public static final int SMS_INT = 2;

	public static final int BIZ_INT = 3;
	
	public static final int MSG_INT =4;

	public static final PfPluginTypes NOTE = new PfPluginTypes(NOTE_INT);

	public static final PfPluginTypes MAIL = new PfPluginTypes(MAIL_INT);

	public static final PfPluginTypes SMS = new PfPluginTypes(SMS_INT);

	public static final PfPluginTypes BIZ = new PfPluginTypes(BIZ_INT);
	
	public static final PfPluginTypes MSG = new PfPluginTypes(MSG_INT);
	
	private static final String[] TAGS = { "NOTE", "MAIL", "SMS", "BIZ","MSG" };

//	private static final String[] OLD_TAGS = { "实时消息", "邮件", "短消息", "业务消息","通知消息" };

	public static final PfPluginTypes[] VALUES = { NOTE, MAIL, SMS, BIZ ,MSG};

	private static final HashMap tagMap = new HashMap();

	private final int _value;

	static {
		for (int i = 0; i < TAGS.length; i++) {
			tagMap.put(TAGS[i], VALUES[i]);
		}
	}

	public static PfPluginTypes fromString(String tag) {
		PfPluginTypes style = (PfPluginTypes) tagMap.get(tag);
		if (style == null && tag != null)
			throw new IllegalArgumentException(tag);
		return style;
	}

	private PfPluginTypes(int value) {
		_value = value;
	}

	public int getValue() {
		return _value;
	}

	/**
	 * 获得标记值
	 */
	public String getTag() {
		return TAGS[_value];
	}

	/**
	 * 将NC30的插件类型转换为NC31的格式
	 * <li>该方法自NC31以后将废弃
	 * @param oldTag
	 * @return
	 */
//	public static String oldTagToNew(String oldTag) {
//		for (int i = 0; i < OLD_TAGS.length; i++) {
//			if (OLD_TAGS[i].equals(oldTag))
//				return TAGS[i];
//		}
//		return null;
//	}

	/**
	 * 获得显示名称
	 */
	public String toString() {
		switch (_value) {
			case NOTE_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000187")/*@res "实时消息"*/;
			case MAIL_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000185")/*@res "邮件"*/;
			case SMS_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000186")/*@res "短消息"*/;
			case BIZ_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000201")/*@res "业务消息"*/;
			case MSG_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000402")/*通知消息*/;
			default:
				return "ERROR";
		}
	}

}