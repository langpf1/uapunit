package uap.workflow.pub.app.notice;

import java.util.HashMap;


import nc.vo.ml.NCLangRes4VoTransl;

public class PfPluginConditionTypes4Workflow extends PfPluginConditionTypes{

	public PfPluginConditionTypes4Workflow(int value){
		super(value);
	}

	public static final int NONE_INT = 0;
	public static final int CHECKPASS_INT = 1;
	public static final int NOPASS_INT = 2;

	public static final PfPluginConditionTypes4Workflow NONE = new PfPluginConditionTypes4Workflow(NONE_INT);
	public static final PfPluginConditionTypes4Workflow CHECKPASS = new PfPluginConditionTypes4Workflow(CHECKPASS_INT);
	public static final PfPluginConditionTypes4Workflow NOPASS = new PfPluginConditionTypes4Workflow(NOPASS_INT);

	private static final String[] TAGS = { "NONE", "CHECKPASS", "NOPASS" };
	private static final String[] CONDITION_XPRESSIONS = { "true", "CurrentWorkFlow.getIsCheckPass()==true",
			"CurrentWorkFlow.getIsCheckPass()==false" };

	public static final PfPluginConditionTypes4Workflow[] VALUES = { NONE, CHECKPASS, NOPASS };

	private static final HashMap tagMap = new HashMap();


	static {
		for (int i = 0; i < TAGS.length; i++) {
			tagMap.put(TAGS[i], VALUES[i]);
		}
	}

	public static PfPluginConditionTypes4Workflow fromString(String tag) {
		PfPluginConditionTypes4Workflow style = (PfPluginConditionTypes4Workflow) tagMap.get(tag);
		if (style == null && tag != null)
			throw new IllegalArgumentException(tag);
		return style;
	}


	public int getValue() {
		return _value;
	}

	/**
	 * ��ñ��ֵ
	 */
	public String getTag() {
		return TAGS[_value];
	}

	public String getXpression() {
		return CONDITION_XPRESSIONS[_value];
	}

	/**
	 * ��NC30�Ĳ������������ת��ΪNC31�ĸ�ʽ <li>�÷�����NC31�Ժ󽫷���
	 * 
	 * @param oldTag
	 * @return
	 */
	public static String oldTagToNew(String oldTag) {
		for (int i = 0; i < CONDITION_XPRESSIONS.length; i++) {
			if (CONDITION_XPRESSIONS[i].equals(oldTag))
				return TAGS[i];
		}
		return null;
	}

	/**
	 * �����ʾ����
	 */
	public String toString() {
		switch (_value) {
		case NONE_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000188")/*
																									 * @
																									 * res
																									 * "������"
																									 */;
		case CHECKPASS_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000069")/* ͨ�� */;
		case NOPASS_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000070")/* ��ͨ�� */;
		default:
			return "ERROR";
		}
	}
}
