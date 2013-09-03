package uap.workflow.pub.app.message.vo;

import java.util.HashMap;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 消息优先级
 * 
 * @author leijun 2006-6-26
 */
public class MessagePriority {
	public final static int PRI_NORMAL = 0;

	public final static int PRI_LOW = -1;

	public final static int PRI_HIGH = 1;

	public static final MessagePriority NORMAL = new MessagePriority(PRI_NORMAL);

	public static final MessagePriority LOW = new MessagePriority(PRI_LOW);

	public static final MessagePriority HIGH = new MessagePriority(PRI_HIGH);

	private static final String[] TAGS = { "LOW", "NORMAL", "HIGH" };

	private static final MessagePriority[] VALUES = { LOW, NORMAL, HIGH };

	private static final HashMap tagMap = new HashMap();

	private final int _value;

	static {
		for (int i = 0; i < TAGS.length; i++) {
			tagMap.put(TAGS[i], VALUES[i]);
		}
	}

	public static MessagePriority fromString(String tag) {
		MessagePriority type = (MessagePriority) tagMap.get(tag);
		if (type == null && tag != null)
			throw new IllegalArgumentException(tag);
		return type;
	}

	public MessagePriority(int value) {
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
		case PRI_NORMAL:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000417")/* "中" */;
		case PRI_LOW:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000418")/* "低" */;
		case PRI_HIGH:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000416")/* "高" */;
		default:
			return "No Such Priority";
		}
	}

	/**
	 * 获得标记值
	 */
	public String getTag() {
		return TAGS[_value + 1];
	}
}
