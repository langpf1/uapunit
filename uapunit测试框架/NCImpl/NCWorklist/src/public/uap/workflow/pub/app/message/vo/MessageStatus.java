package uap.workflow.pub.app.message.vo;

import java.util.HashMap;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 消息状态
 * 
 * @author leijun 2006-6-26
 * @modifier guowl 2008-4-1, 为公告消息增加一个状态："已封存"
 */
public class MessageStatus {
	/**
	 * 消息状态:0:未处理.1:已处理.2:已封存
	 */
	public static final int STATE_NOT_CHECK = 0;

	public static final int STATE_CHECKED = 1;

	public static final int STATE_SEALED = 2;

	public static final MessageStatus NOT_CHECK = new MessageStatus(STATE_NOT_CHECK);

	public static final MessageStatus CHECKED = new MessageStatus(STATE_CHECKED);

	public static final MessageStatus SEALED = new MessageStatus(STATE_SEALED);

	private static final String[] TAGS = { "NOTCHECK", "CHECKED", "SEALED" };

	private static final MessageStatus[] VALUES = { NOT_CHECK, CHECKED, SEALED };

	private static final HashMap tagMap = new HashMap();

	private final int _value;

	static {
		for (int i = 0; i < TAGS.length; i++) {
			tagMap.put(TAGS[i], VALUES[i]);
		}
	}

	private MessageStatus(int value) {
		_value = value;
	}

	public int getValue() {
		return _value;
	}

	/**
	 * 获得显示名称
	 */
	public String toString() {
		switch (_value) {
			case STATE_NOT_CHECK:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000222")/* 未处理 */;
			case STATE_CHECKED:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000223")/* 已处理 */;
			case STATE_SEALED:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000667")/*"已封存"*/; 
			default:
				return "ERROR";
		}
	}

	/**
	 * 获得标记值
	 */
	public String getTag() {
		return TAGS[_value];
	}
}
