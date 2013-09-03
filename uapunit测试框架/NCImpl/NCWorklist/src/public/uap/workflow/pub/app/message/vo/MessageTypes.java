package uap.workflow.pub.app.message.vo;

import java.util.HashMap;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 消息类型 常量类
 * 
 * @author leijun 2006-6-15
 */
public class MessageTypes {
	/**
	 * 公告消息，可由任何人看到的消息
	 */
	public final static int MSG_TYPE_PUBLIC = -1;

	/**
	 * 预警消息，预警平台产生的预警消息
	 */
	public static final int MSG_TYPE_PA = 0;

	/**
	 * 对发消息，某人向另外一人直接发送的消息
	 */
	public static final int MSG_TYPE_P2P = 1;

	/**
	 * 审批流工作项，审批流平台产生的需要业务处理的工作项
	 */
	public final static int MSG_TYPE_APPROVE = 2;

	/**
	 * 业务流消息，业务流平台产生的需要业务处理的推式消息
	 */
	public final static int MSG_TYPE_BUSIFLOW_PUSH = 3;

	/**
	 * 业务流消息，业务流平台产生的需要业务处理的拉式消息
	 */
	public final static int MSG_TYPE_BUSIFLOW_PULL = 4;

	/**
	 * 业务流消息
	 */
	public final static int MSG_TYPE_BUSIFLOW = 5;

	/**
	 * 普通消息，一般的通知消息
	 */
	public static final int MSG_TYPE_INFO = 6;
	
	/**
	 * 后台消息,类同于预警但又不同于预警
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
	 * 获得标记值
	 */
	public String getTag() {
		return TAGS[_value + 1];
	}

	/**
	 * 获得显示名称
	 */
	public String toString() {
		switch (_value) {
		case MSG_TYPE_APPROVE:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000404")/* "审批流消息" */;
		case MSG_TYPE_BUSIFLOW:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000405")/* 业务流消息" */;
		case MSG_TYPE_BUSIFLOW_PULL:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000406")/* "业务流拉式消息" */;
		case MSG_TYPE_BUSIFLOW_PUSH:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000407")/* "业务流推式消息" */;
		case MSG_TYPE_INFO:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000402")/* "通知消息" */;
		case MSG_TYPE_P2P:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000400")/* "对发消息" */;
		case MSG_TYPE_PA:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000403")/* "预警消息" */;
		case MSG_TYPE_PUBLIC:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"UPPpfworkflow-000401")/* "公告消息" */;
		default:
			return "No Such Message";
		}
	}
}
