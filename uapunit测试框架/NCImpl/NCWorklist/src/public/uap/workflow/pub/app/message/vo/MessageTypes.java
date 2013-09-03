package uap.workflow.pub.app.message.vo;

import java.util.HashMap;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * ��Ϣ���� ������
 * 
 * @author leijun 2006-6-15
 */
public class MessageTypes {
	/**
	 * ������Ϣ�������κ��˿�������Ϣ
	 */
	public final static int MSG_TYPE_PUBLIC = -1;

	/**
	 * Ԥ����Ϣ��Ԥ��ƽ̨������Ԥ����Ϣ
	 */
	public static final int MSG_TYPE_PA = 0;

	/**
	 * �Է���Ϣ��ĳ��������һ��ֱ�ӷ��͵���Ϣ
	 */
	public static final int MSG_TYPE_P2P = 1;

	/**
	 * �����������������ƽ̨��������Ҫҵ����Ĺ�����
	 */
	public final static int MSG_TYPE_APPROVE = 2;

	/**
	 * ҵ������Ϣ��ҵ����ƽ̨��������Ҫҵ�������ʽ��Ϣ
	 */
	public final static int MSG_TYPE_BUSIFLOW_PUSH = 3;

	/**
	 * ҵ������Ϣ��ҵ����ƽ̨��������Ҫҵ�������ʽ��Ϣ
	 */
	public final static int MSG_TYPE_BUSIFLOW_PULL = 4;

	/**
	 * ҵ������Ϣ
	 */
	public final static int MSG_TYPE_BUSIFLOW = 5;

	/**
	 * ��ͨ��Ϣ��һ���֪ͨ��Ϣ
	 */
	public static final int MSG_TYPE_INFO = 6;
	
	/**
	 * ��̨��Ϣ,��ͬ��Ԥ�����ֲ�ͬ��Ԥ��
	 */
	public static final int MSG_TYPE_BG = 10;
	

	public static final MessageTypes PUBLIC = new MessageTypes(MSG_TYPE_PUBLIC);

	public static final MessageTypes APPROVE = new MessageTypes(
			MSG_TYPE_APPROVE);

	public static final MessageTypes PA = new MessageTypes(MSG_TYPE_PA);

	public static final MessageTypes P2P = new MessageTypes(MSG_TYPE_P2P);

	public static final MessageTypes BUSIFLOW_PUSH = new MessageTypes(
			MSG_TYPE_BUSIFLOW_PUSH);

	public static final MessageTypes BUSIFLOW_PULL = new MessageTypes(
			MSG_TYPE_BUSIFLOW_PULL);

	public static final MessageTypes BUSIFLOW = new MessageTypes(
			MSG_TYPE_BUSIFLOW);

	public static final MessageTypes INFO = new MessageTypes(MSG_TYPE_INFO);

	private static final String[] TAGS = { "PUBLIC", "PA", "P2P", "APPROVE",
			"BUSIFLOW_PUSH", "BUSIFLOW_PULL", "BUSIFLOW", "INFO" };

	private static final MessageTypes[] VALUES = { PUBLIC, PA, P2P, APPROVE,
			BUSIFLOW_PUSH, BUSIFLOW_PULL, BUSIFLOW, INFO };

	private static final HashMap tagMap = new HashMap();

	private final int _value;

	static {
		for (int i = 0; i < TAGS.length; i++) {
			tagMap.put(TAGS[i], VALUES[i]);
		}
	}

	private MessageTypes(int value) {
		_value = value;
	}

	public int getValue() {
		return _value;
	}

	public static MessageTypes fromString(String tag) {
		MessageTypes type = (MessageTypes) tagMap.get(tag);
		if (type == null && tag != null)
			throw new IllegalArgumentException(tag);
		return type;
	}

	/**
	 * ��ñ��ֵ
	 */
	public String getTag() {
		return TAGS[_value + 1];
	}

	/**
	 * �����ʾ����
	 */
	public String toString() {
		switch (_value) {
		case MSG_TYPE_APPROVE:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000404")/* "��������Ϣ" */;
		case MSG_TYPE_BUSIFLOW:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000405")/* ҵ������Ϣ" */;
		case MSG_TYPE_BUSIFLOW_PULL:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000406")/* "ҵ������ʽ��Ϣ" */;
		case MSG_TYPE_BUSIFLOW_PUSH:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000407")/* "ҵ������ʽ��Ϣ" */;
		case MSG_TYPE_INFO:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000402")/* "֪ͨ��Ϣ" */;
		case MSG_TYPE_P2P:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000400")/* "�Է���Ϣ" */;
		case MSG_TYPE_PA:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000403")/* "Ԥ����Ϣ" */;
		case MSG_TYPE_PUBLIC:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000401")/* "������Ϣ" */;
		default:
			return "No Such Message";
		}
	}
}
