package uap.workflow.pub.app.message.vo;

import java.util.HashMap;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * ��Ϣ���ȼ�
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
	 * �����ʾ����
	 */
	public String toString() {
		switch (_value) {
		case PRI_NORMAL:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000417")/* "��" */;
		case PRI_LOW:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000418")/* "��" */;
		case PRI_HIGH:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000416")/* "��" */;
		default:
			return "No Such Priority";
		}
	}

	/**
	 * ��ñ��ֵ
	 */
	public String getTag() {
		return TAGS[_value + 1];
	}
}
