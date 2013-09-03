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
	 * 获得标记值
	 */
	public String getTag() {
		return TAGS[_value];
	}

	public String getXpression() {
		return CONDITION_XPRESSIONS[_value];
	}

	/**
	 * 将NC30的插件的条件类型转换为NC31的格式 <li>该方法自NC31以后将废弃
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
	 * 获得显示名称
	 */
	public String toString() {
		switch (_value) {
		case NONE_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000188")/*
																									 * @
																									 * res
																									 * "无条件"
																									 */;
		case CHECKPASS_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000069")/* 通过 */;
		case NOPASS_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000070")/* 不通过 */;
		default:
			return "ERROR";
		}
	}
}
